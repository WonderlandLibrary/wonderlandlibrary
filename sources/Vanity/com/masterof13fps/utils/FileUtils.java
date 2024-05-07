package com.masterof13fps.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.masterof13fps.Client;

public abstract class FileUtils {

    public static void saveFile(File f, List<String> lines) {
        try {
            FileOutputStream fos = new FileOutputStream(f);
            final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));

            lines.forEach(line -> {
                try {
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadFile(File f) {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(f));
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createFile(String name) {
        try {
            File file = new File(Client.main().getClientDir().getAbsolutePath(), name + ".txt");
            if (!file.exists()) {
                PrintWriter printWriter = new PrintWriter(new FileWriter(file));
                printWriter.println();
                printWriter.close();
            }
            System.out.println(Client.main().getClientDir().getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}