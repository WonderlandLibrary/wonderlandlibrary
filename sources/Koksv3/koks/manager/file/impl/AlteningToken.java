package koks.manager.file.impl;

import koks.Koks;
import koks.manager.file.Files;
import koks.manager.file.IFile;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author kroko
 * @created on 06.10.2020 : 21:20
 */
@IFile(name = "api-keys")
public class AlteningToken extends Files {

    @Override
    public void readFile(BufferedReader bufferedReader) throws IOException {
        Koks.getKoks().alteningApiKey = "";
        String line;
        while((line = bufferedReader.readLine()) != null) {
            String[] args = line.split(":");
            if(args[0].equalsIgnoreCase("apiToken")) {
                if(args.length == 2) {
                    Koks.getKoks().alteningApiKey = args[1];
                }
            }
        }
    }

    @Override
    public void writeFile(FileWriter fileWriter) throws IOException {
        fileWriter.write("apiToken:" + Koks.getKoks().alteningApiKey);
    }
}
