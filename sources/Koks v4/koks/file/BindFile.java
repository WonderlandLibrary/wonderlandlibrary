package koks.file;

import koks.api.registry.file.Files;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Files.Info(name = "binds")
public class BindFile extends Files {

    @Override
    public void writeFile(BufferedWriter writer) throws Exception {
        for(Module module : ModuleRegistry.getModules()) {
            writer.write(module.getName() + ":" + module.getKey() + "\n");
        }
    }

    @Override
    public void readFile(BufferedReader reader) throws Exception {
        String line;
        while((line = reader.readLine()) != null) {
            String[] args = line.split(":");
            if(args.length == 2) {
                Module module = ModuleRegistry.getModule(args[0]);
                if(module != null){
                    module.setKey(Integer.parseInt(args[1]));
                }
            }
        }
    }
}
