package dir.monitor.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static dir.monitor.processors.impl.ArchivalProcessorTest.ARCHIVE_DIR_NAME;
import static dir.monitor.processors.impl.ArchivalProcessorTest.ARCHIVE_FILE_NAME;

public class DirectoryDetailsTest {

    public static final String TEST_RESOURCES_DIR_NAME = "C:\\Users\\home-user\\Documents\\software\\heroku\\DirectoryHealthMonitor\\test\\testResources";
    public static final String NEW_FILE_NAME = TEST_RESOURCES_DIR_NAME + "\\NonEmptyDirectory\\abc.txt";

    @Before
    public void setUp(){
        deleteFile(NEW_FILE_NAME);
        deleteFile(ARCHIVE_FILE_NAME);
    }

    @Test
    public void test_getSnapshot(){
        String dirName = TEST_RESOURCES_DIR_NAME;
        List<File> files = new ArrayList<>();
        files.add(new File(dirName + "\\Test.txt"));
        DirectoryDetails expected = new DirectoryDetails(files);
        DirectoryDetails details = DirectoryDetails.getAllFilesInDirectoryRecursively(dirName);
        Assert.assertEquals(new HashSet(expected.getFileNames()), new HashSet(details.getFileNames()));
        Assert.assertEquals("Directory Contents Size is: 1MB", details.getSizeFormattedString());
    }

    @Test
    public void test_getSnapshot_NoFiles(){
        String dirName = TEST_RESOURCES_DIR_NAME + "\\EmptyDirectory";
        List<File> files = new ArrayList<>();
        DirectoryDetails expected = new DirectoryDetails(files);
        DirectoryDetails details = DirectoryDetails.getAllFilesInDirectoryRecursively(dirName);
        Assert.assertEquals(expected.getFileNames(), details.getFileNames());
        Assert.assertEquals("Directory Contents Size is: 0MB", details.getSizeFormattedString());
    }

    @Test
    public void test_getSnapshot_RecursiveFiles(){
        String dirName = TEST_RESOURCES_DIR_NAME;
        List<File> files = new ArrayList<>();
        files.add(new File(dirName + "\\Test.txt"));
        files.add(new File(NEW_FILE_NAME));
        DirectoryDetails expected = new DirectoryDetails(files);
        createFileAndAddContent(NEW_FILE_NAME);
        DirectoryDetails details = DirectoryDetails.getAllFilesInDirectoryRecursively(dirName);
        deleteFile(NEW_FILE_NAME);
        Assert.assertEquals(new HashSet<String>(expected.getFileNames()), new HashSet<String>(details.getFileNames()));
        Assert.assertEquals("Directory Contents Size is: 1MB", details.getSizeFormattedString());
    }

    public static void createFileAndAddContent(String pathName){
        File newFile = new File(pathName);
        try {
            newFile.createNewFile();
            File existingFile = new File(TEST_RESOURCES_DIR_NAME + "\\Test.txt");
            FileInputStream fstream = new FileInputStream(existingFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            List<String> allLines = new ArrayList();
            while ((strLine = br.readLine()) != null)   {
                allLines.add(strLine);
            }
            Files.write(newFile.toPath(), allLines, StandardCharsets.UTF_8);
        } catch (IOException e) {
        }
    }

    public static void createEmptyFile(String pathName){
        File newFile = new File(pathName);
        try {
            newFile.createNewFile();
        } catch (IOException e) {
        }
    }

    private static void deleteFile(String pathName){
        File f = new File(pathName);
        if(f.exists())
            f.delete();
    }
}
