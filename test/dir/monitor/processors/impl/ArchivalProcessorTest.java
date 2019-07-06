package dir.monitor.processors.impl;

import dir.monitor.impl.MonitorProcessRequest;
import dir.monitor.processors.IHealthMonitorProcessResult;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static dir.monitor.impl.DirectoryDetailsTest.*;

public class ArchivalProcessorTest {

    public static final String EMPTY_DIR_NAME = TEST_RESOURCES_DIR_NAME + File.separator + "EmptyDirectory";
    public static final String NON_EMPTY_DIR_NAME = TEST_RESOURCES_DIR_NAME + File.separator + "NonEmptyDirectory";
    public static final String ARCHIVE_DIR_NAME = TEST_RESOURCES_DIR_NAME + File.separator + "ArchiveDir";
    public static final String ARCHIVE_FILE_NAME = ARCHIVE_DIR_NAME + File.separator + "abc.txt";

    @Test
    public void test_process_noFilesInWatchDir(){
        MonitorProcessRequest request = new MonitorProcessRequest(EMPTY_DIR_NAME)
                .withMaxSizeMB(1)
                .withArchiveDirName(ARCHIVE_DIR_NAME);
        IHealthMonitorProcessResult result = ArchivalProcessor.getInstance().process(request);
        Assert.assertEquals("No Files to Archive!", result.getFormattedResult());
    }

    @Test
    public void test_process_FilesLessThanMaxSize(){
        MonitorProcessRequest request = new MonitorProcessRequest(NON_EMPTY_DIR_NAME)
                .withMaxSizeMB(0)
                .withArchiveDirName(ARCHIVE_DIR_NAME);
        createEmptyFile(NEW_FILE_NAME);
        IHealthMonitorProcessResult result = ArchivalProcessor.getInstance().process(request);
        Assert.assertEquals("No Files to Archive!", result.getFormattedResult());
    }

    @Test
    public void test_process_OneFileExceedingMaxSize(){
        MonitorProcessRequest request = new MonitorProcessRequest(NON_EMPTY_DIR_NAME)
                .withMaxSizeMB(0)
                .withArchiveDirName(ARCHIVE_DIR_NAME);
        createFileAndAddContent(NEW_FILE_NAME);
        IHealthMonitorProcessResult result = ArchivalProcessor.getInstance().process(request);
        Assert.assertEquals( true, result.getFormattedResult().startsWith("1 files were Archived"));
        File archivedFile = new File(ARCHIVE_FILE_NAME);
        Assert.assertEquals(true, archivedFile.exists());
    }

    @Before
    public void setup(){
        cleanup();
    }

    @After
    public void cleanup(){
        File f = new File(NEW_FILE_NAME);
        f.delete();
        File f2 = new File(ARCHIVE_FILE_NAME);
        f2.delete();
    }


}
