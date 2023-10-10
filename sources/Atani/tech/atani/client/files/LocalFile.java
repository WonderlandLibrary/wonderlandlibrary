package tech.atani.client.files;

import com.google.gson.Gson;
import tech.atani.client.files.data.FileData;

import java.io.*;

public abstract class LocalFile {

    private final String name;

    public LocalFile() {
        FileData fileData = this.getClass().getAnnotation(FileData.class);
        if(fileData == null)
            throw new RuntimeException();
        this.name = fileData.fileName();
    }

    protected File file;

    public abstract void save(Gson gson);

    public abstract void load(Gson gson);

    public void setFile(File root) {
        this.file = new File(root, name);
    }

    public void writeFile(String content, File file) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile(File file) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }
}