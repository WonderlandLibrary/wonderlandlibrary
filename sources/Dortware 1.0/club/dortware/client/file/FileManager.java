package club.dortware.client.file;

import club.dortware.client.manager.Manager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileManager extends Manager<MFile> {

    public FileManager() {
        super(new ArrayList<>());
    }

    @Override
    public void onCreated() {
        this.getList().forEach(MFile::load);
    }

    public void writeFile(MFile file, String contents) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Memeware/" + file.getName()));
        bufferedWriter.write(contents);
        bufferedWriter.close();
    }

    public List<String> loadFileContents(MFile file) throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("Memeware/" + file.getName()));
        return bufferedReader.lines().collect(Collectors.toCollection(ArrayList::new));
    }

    public BufferedReader getBufferedReaderForFile(MFile file) throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("Memeware/" + file.getName()));
        return bufferedReader;
    }

    public BufferedReader initializeFile(MFile file) {
        try {
            return getBufferedReaderForFile(file);
        }
        catch (Exception e) {
            return null;
        }
    }
}
