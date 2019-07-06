package dir.monitor.processors.impl;

import dir.monitor.impl.MonitorProcessRequest;
import dir.monitor.processors.IHealthMonitorProcessor;
import dir.monitor.processors.IHealthMonitorProcessResult;
import dir.monitor.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CleanupProcessor implements IHealthMonitorProcessor {

    private static volatile IHealthMonitorProcessor instance = new CleanupProcessor();
    private static final List<String> UNACCEPTABLE_FILE_EXTENSIONS = Arrays.asList("bat", "sh");

    synchronized static public IHealthMonitorProcessor getInstance() {
        return instance;
    }

    @Override
    public IHealthMonitorProcessResult process(MonitorProcessRequest request) {
        List<File> allFiles = request.getDirectoryDetails().getFiles();
        List<File> filesToCleanUp = new ArrayList<>();
        for(File f : allFiles){
            if(toBeCleaned(f.getName())){
                filesToCleanUp.add(f);
            }
        }
        FileUtils.deleteFiles(filesToCleanUp);
        return new CleanupResult(filesToCleanUp);
    }

    private static boolean toBeCleaned(String fileName){
        return UNACCEPTABLE_FILE_EXTENSIONS.contains(FileUtils.getFileExtension(fileName));
    }

    public static class CleanupResult implements IHealthMonitorProcessResult {

        List<File> files;

        public CleanupResult(List<File> files) {
            this.files = files;
        }

        @Override
        public String getFormattedResult() {
            String message = "";
            if(this.files.size() > 0){
                message = this.files.size() + " files were cleaned-up: " + Arrays.toString(this.files.toArray());
            } else {
                message = "No Files to Cleanup!";
            }
            return message;
        }
    }
}
