package koks.manager.file.impl;

import koks.Koks;
import koks.manager.file.Files;
import koks.manager.file.IFile;
import koks.manager.module.Module;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author kroko
 * @created on 29.11.2020 : 22:58
 */

@IFile(name = "binds")
public class Binds extends Files {

    @Override
    public void readFile(BufferedReader bufferedReader) throws IOException {
        String line;
        while((line = bufferedReader.readLine()) != null) {
            String[] args = line.split(":");
            Module module = Koks.getKoks().moduleManager.getModule(args[0]);
            if(module != null && args.length == 2) {
                module.setKey(Integer.parseInt(args[1]));
            }
        }
    }

    @Override
    public void writeFile(FileWriter fileWriter) throws IOException {
        for(Module module : Koks.getKoks().moduleManager.getModules()) {
            fileWriter.write(module.getName() + ":" + module.getKey() + "\n");
        }
    }
}
