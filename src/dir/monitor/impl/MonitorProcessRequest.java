package dir.monitor.impl;

public class MonitorProcessRequest {

    private String watchDirName;
    private long maxSizeMBs;
    private String archiveDirName;

    public MonitorProcessRequest(String watchDirName) {
        this.watchDirName = watchDirName;
    }

    public MonitorProcessRequest withMaxSizeMB(long maxSizeMB){
        this.maxSizeMBs = maxSizeMB;
        return this;
    }

    public MonitorProcessRequest withArchiveDirName(String archiveDirName){
        this.archiveDirName = archiveDirName;
        return this;
    }

    public String getWatchDirName() {
        return watchDirName;
    }

    public long getMaxSizeMBs() {
        return maxSizeMBs;
    }

    public String getArchiveDirName() {
        return archiveDirName;
    }

    public DirectoryDetails getDirectoryDetails(){
        return DirectoryDetails.getAllFilesInDirectoryRecursively(this.watchDirName);
    }
}
