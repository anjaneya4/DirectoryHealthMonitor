package dir.monitor.processors.impl;

import dir.monitor.impl.MonitorProcessRequest;
import dir.monitor.processors.IHealthMonitorProcessResult;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static dir.monitor.impl.DirectoryDetailsTest.NEW_FILE_NAME;
import static dir.monitor.impl.DirectoryDetailsTest.createEmptyFile;
import static dir.monitor.processors.impl.ArchivalProcessorTest.ARCHIVE_DIR_NAME;
import static dir.monitor.processors.impl.ArchivalProcessorTest.EMPTY_DIR_NAME;
import static dir.monitor.processors.impl.ArchivalProcessorTest.NON_EMPTY_DIR_NAME;

public class CleanupProcessorTest {

    private static final String BAD_FILE_NAME = NON_EMPTY_DIR_NAME + File.separator + "abc.bat";

    @Test
    public void test_process_NoFiles(){
        MonitorProcessRequest request = new MonitorProcessRequest(EMPTY_DIR_NAME)
                .withMaxSizeMB(1)
                .withArchiveDirName(ARCHIVE_DIR_NAME);
        IHealthMonitorProcessResult result = CleanupProcessor.getInstance().process(request);
        Assert.assertEquals("No Files to Cleanup!", result.getFormattedResult());
    }

    @Test
    public void test_process_OneBadFile(){
        MonitorProcessRequest request = new MonitorProcessRequest(NON_EMPTY_DIR_NAME)
                .withMaxSizeMB(1)
                .withArchiveDirName(ARCHIVE_DIR_NAME);
        createEmptyFile(BAD_FILE_NAME);
        IHealthMonitorProcessResult result = CleanupProcessor.getInstance().process(request);
        Assert.assertEquals(true, result.getFormattedResult().startsWith("1 files were cleaned-up"));
    }

    @Before
    public void setup(){
        cleanup();
    }

    @After
    public void cleanup(){
        File f = new File(NEW_FILE_NAME);
        f.delete();
        File f2 = new File(BAD_FILE_NAME);
        f2.delete();
    }

}
