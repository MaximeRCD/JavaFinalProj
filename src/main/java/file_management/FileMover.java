package file_management;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileMover {
    final private String file_name;
    final private String source_folder;
    final private String dest_folder;

    public FileMover(String file_name, String source_folder, String dest_folder) {
        this.file_name = file_name;
        this.source_folder = source_folder;
        this.dest_folder = dest_folder;
    }


    public String getFile_name() {
        return file_name;
    }

    public String getSource_folder() {
        return source_folder;
    }

    public String getDest_folder() {
        return dest_folder;
    }

    public void move(){
        Path source = Paths.get(this.getSource_folder());
        Path newdir = Paths.get(this.getDest_folder());
        try {
            Files.move(source, newdir.resolve(source.getFileName()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
