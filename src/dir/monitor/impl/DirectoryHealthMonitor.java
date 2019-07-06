package dir.monitor.impl;

import dir.monitor.processors.IHealthMonitorProcessor;
import dir.monitor.processors.IHealthMonitorProcessResult;
import dir.monitor.processors.impl.ArchivalProcessor;
import dir.monitor.processors.impl.CleanupProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DirectoryHealthMonitor {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final List<IHealthMonitorProcessor> dirHealthProcessors = new ArrayList();
    private static final Logger logger = Logger.getLogger(DirectoryHealthMonitor.class.getSimpleName());

    static {
        dirHealthProcessors.add(CleanupProcessor.getInstance());
        dirHealthProcessors.add(ArchivalProcessor.getInstance());
    }

    public static void schedule(final String watchDirName,
                                final long maxSizeMBs,
                                final long watchIntervalSec,
                                final String archiveDirName) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.info(LOG_SEPARATOR + "Starting Directory Health Monitor Process..." + LOG_SEPARATOR);
                process(watchDirName, maxSizeMBs, watchIntervalSec, archiveDirName);
                logger.info(LOG_SEPARATOR + "Directory Health Monitor Process finished iteration." + LOG_SEPARATOR);
            }
        };
        scheduler.scheduleAtFixedRate(task,
                0,
                watchIntervalSec,
                TimeUnit.SECONDS);
    }

    /**
     * 1. scan and get all data snapshot
     * <p>
     * Invoke all health monitor processors
     * 2. cleanup and get result in cleanupResult
     * 3. archive and get result in archivalResult
     * <p>
     * 4. get current scan result for new size information
     * 5. report all results
     *
     * @param watchDirName
     * @param maxSizeMBs
     * @param watchIntervalSec
     * @param archiveDirName
     */

    private static void process(final String watchDirName,
                                final long maxSizeMBs,
                                final long watchIntervalSec,
                                final String archiveDirName) {
        DirectoryDetails initialFiles = DirectoryDetails.getAllFilesInDirectoryRecursively(watchDirName);
        MonitorProcessRequest request = new MonitorProcessRequest(watchDirName)
                                                                .withMaxSizeMB(maxSizeMBs)
                                                                .withArchiveDirName(archiveDirName);
        List<IHealthMonitorProcessResult> results = new ArrayList();
        for (IHealthMonitorProcessor processor : dirHealthProcessors) {
            IHealthMonitorProcessResult result = processor.process(request);
            results.add(result);
        }
        for (IHealthMonitorProcessResult result : results) {
            logger.info(result.getFormattedResult());
        }
        DirectoryDetails finalDetailSnapshot = DirectoryDetails.getAllFilesInDirectoryRecursively(watchDirName);
        logger.info(finalDetailSnapshot.getSizeFormattedString());
    }

    private static final String LOG_SEPARATOR = "\n========================================================================\n";
}
