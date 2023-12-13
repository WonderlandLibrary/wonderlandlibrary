package wtf.diablo.config;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    private String name;
    private String author;
    private String lastUpdated;
    private File file;
    private boolean isOnline;

    public Config(String name, File file) {
        this.name = name;
        this.file = file;
    }


    public void rename(String newName) {
        try {
            Path original = Paths.get(file.getCanonicalPath());
            Path to = Paths.get(file.getPath());
            Files.move(original, to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("all")
    public String getName() {
        return this.name;
    }

    @SuppressWarnings("all")
    public void setName(final String name) {
        this.name = name;
    }


    @SuppressWarnings("all")
    public File getFile() {
        return this.file;
    }

    @SuppressWarnings("all")
    public void setFile(final File file) {
        this.file = file;
    }

}
