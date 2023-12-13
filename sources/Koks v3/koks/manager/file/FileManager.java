package koks.manager.file;

import koks.Koks;
import koks.api.interfaces.Methods;
import koks.manager.file.impl.AlteningToken;
import koks.manager.file.impl.Binds;
import koks.manager.file.impl.Settings;
import koks.manager.file.impl.Toggle;

import java.io.*;
import java.util.ArrayList;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 04:10
 */
public class FileManager implements Methods {

    public final File DIR = new File(mc.mcDataDir + "/" + Koks.getKoks().NAME + "/Files");
    public ArrayList<Files> files = new ArrayList<>();

    public FileManager() {
        addFile(new AlteningToken());
        addFile(new Toggle());
        addFile(new Settings());
        addFile(new Binds());
    }

    public void writeFile(Class<? extends Files> clazz) {
        if (!DIR.exists()) DIR.mkdirs();

        for (Files file : files) {
            if (file.getClass().equals(clazz)) {
                if (!file.getFile().exists()) {
                    try {
                        file.getFile().createNewFile();
                    } catch (IOException ignored) {
                    }
                }
                try {
                    FileWriter fileWriter = new FileWriter(file.getFile());
                    file.writeFile(fileWriter);
                    fileWriter.close();
                } catch (IOException ignored) {
                }

            }

        }
    }

    public void writeAllFiles() {
        if (!DIR.exists()) DIR.mkdirs();

        for (Files file : files) {
            if (!file.getFile().exists()) {
                try {
                    file.getFile().createNewFile();
                } catch (IOException ignored) {
                }
            }
            try {
                FileWriter fileWriter = new FileWriter(file.getFile());
                file.writeFile(fileWriter);
                fileWriter.close();
            } catch (IOException ignored) {
            }
        }
    }

    public void readAllFiles() {
        if (!DIR.exists()){
            Koks.getKoks().isNew = true;
            DIR.mkdirs();
        }

        if (DIR.exists()) {
            for (Files file : files) {
                if (!file.getFile().exists()) {
                    try {
                        file.getFile().createNewFile();
                    } catch (IOException ignored) {
                    }
                }
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file.getFile()));
                    file.readFile(reader);
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }

    }

    public void addFile(Files file) {
        files.add(file);
    }
}
