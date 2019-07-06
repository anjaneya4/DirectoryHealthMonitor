package dir.monitor;

import dir.monitor.impl.DirectoryHealthMonitor;

import java.util.logging.Logger;

public class DirectoryHealthMonitorApp {

    private static final Logger logger = Logger.getLogger(DirectoryHealthMonitorApp.class.getSimpleName());

    public static void main(String[] args) {

        String watchDirName = args[0];
        logger.info("Using watch directory: " + watchDirName);

        long  maxSizeMBs = Long.valueOf(args[1]);
        logger.info("Allowed maximum size: " + maxSizeMBs + " MB");

        long watchIntervalSec = Long.valueOf(args[2]);
        logger.info("Using watch interval: " + watchIntervalSec + " Seconds");

        String archiveDirName = args[3];
        logger.info("Using archive directory: " + archiveDirName);

        DirectoryHealthMonitor.schedule(watchDirName,maxSizeMBs,watchIntervalSec,archiveDirName);
    }
}
