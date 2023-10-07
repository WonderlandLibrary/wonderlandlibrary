package koks.api.registry.file;

import koks.Koks;
import koks.api.registry.Registry;
import koks.file.ApiKeyFile;
import koks.file.BindFile;
import koks.file.SettingFile;
import koks.file.ToggleFile;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */
public class FileRegistry implements Registry {

    @Getter
    private static final File DIR = new File(Koks.getKoks().DIR, "Files");

    private static final ArrayList<Files> FILES = new ArrayList<>();

    @Override
    public void initialize() {
        if(!DIR.exists())
            DIR.mkdirs();
        addFile(new ApiKeyFile());
        addFile(new SettingFile());
        addFile(new BindFile());
        addFile(new ToggleFile());
        readAll();
    }

    public void addFile(Files file) {
        FILES.add(file);
    }

    public static ArrayList<Files> getFiles() {
        return FILES;
    }

    public static void writeAll() {
        for(Files files : FILES) {
            writeFile(files);
        }
    }

    public static void readAll() {
        for(Files files : FILES) {
            readFile(files);
        }
    }

    public static void readFile(Files files) {
        try {
            final File file = new File(DIR, files.name + ".koks");
            if(!file.exists())
                file.createNewFile();
            final BufferedReader reader = new BufferedReader(new FileReader(file));
            checkDIR();
            checkFile(file);
            files.readFile(reader);
            reader.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void writeFile(Files files) {
        try {
            final File file = new File(DIR, files.name + ".koks");
            final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            checkDIR();
            checkFile(file);
            files.writeFile(writer);
            writer.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Files getFile(Class<? extends Files> clazz) {
        for(Files files : FILES) {
            if(files.getClass().equals(clazz))
                return files;
        }
        return null;
    }

    public static void checkFile(File file) {
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void checkDIR() {
        if(!DIR.exists())
            DIR.mkdirs();
    }

}
