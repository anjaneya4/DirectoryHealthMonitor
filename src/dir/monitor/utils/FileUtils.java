package dir.monitor.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FileUtils {

    public static void deleteFiles(List<File> files){
        for(File f : files){
            f.delete();
        }
    }

    public static String getFileExtension(String fileName){
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }

    public static void archive(List<File> files, String archiveDir) {
        for (File f : files) {
            try {
                Files.move(f.toPath(), Paths.get(archiveDir + File.separator + f.getName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
