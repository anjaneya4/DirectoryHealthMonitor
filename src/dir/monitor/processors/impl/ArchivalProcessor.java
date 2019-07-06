package dir.monitor.processors.impl;

import dir.monitor.impl.MonitorProcessRequest;
import dir.monitor.processors.IHealthMonitorProcessor;
import dir.monitor.processors.IHealthMonitorProcessResult;
import dir.monitor.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ArchivalProcessor implements IHealthMonitorProcessor {

    private static volatile IHealthMonitorProcessor instance = new ArchivalProcessor();

    synchronized static public IHealthMonitorProcessor getInstance() {
        return instance;
    }

    @Override
    public IHealthMonitorProcessResult process(MonitorProcessRequest request) {
        long currentSizeInMB = request.getDirectoryDetails().getSizeInMB();
        if (currentSizeInMB <= request.getMaxSizeMBs()) {
            return new ArchivalResult(new ArrayList());
        } else {
            return archiveOldFiles(request);
        }
    }

    /**
     * 1. Sorts all files as most recently modified first.
     * 2. Identifies which files to keep -> which files to archive.
     * 3. Archives files if needed.
     *
     * @param request
     * @return
     */
    private static IHealthMonitorProcessResult archiveOldFiles(MonitorProcessRequest request) {
        List<File> files = request.getDirectoryDetails().getFiles();
        files.sort(new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                return -1 * Long.compare(f1.lastModified(), f2.lastModified());
            }
        });
        long maxFileSizeBytesAllowed = request.getMaxSizeMBs() * 1024 * 1024;
        long sizeCounter = 0;
        int filesToKeep = 0;
        while (sizeCounter + files.get(filesToKeep).length() <= maxFileSizeBytesAllowed) {
            sizeCounter += files.get(filesToKeep).length();
            filesToKeep++;
        }
        List<File> filesToArchive = new ArrayList<>(files.subList(filesToKeep, files.size()));
        FileUtils.archive(filesToArchive, request.getArchiveDirName());
        return new ArchivalResult(filesToArchive);
    }

    public static class ArchivalResult implements IHealthMonitorProcessResult {

        private List<File> filesToArchive;

        public ArchivalResult(List<File> filesToArchive) {
            this.filesToArchive = filesToArchive;
        }

        @Override
        public String getFormattedResult() {
            String message = "";
            if (filesToArchive.size() > 0) {
                message = filesToArchive.size() + " files were Archived: " + Arrays.toString(filesToArchive.toArray());
            } else {
                message = "No Files to Archive!";
            }
            return message;
        }
    }
}
