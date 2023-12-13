package koks.file;

import koks.api.registry.file.Files;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Files.Info(name = "toggle")
public class ToggleFile extends Files {

    @Override
    public void writeFile(BufferedWriter writer) throws Exception {
        ModuleRegistry.getModules().forEach(module -> {
            try {
                writer.write(module.getName() + ":" + module.isToggled() + ":" + module.isBypass() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void readFile(BufferedReader reader) throws Exception {
        String line;
        while((line = reader.readLine()) != null) {
            String[] args = line.split(":");
            Module module = ModuleRegistry.getModule(args[0]);
            if(module != null) {
                module.setToggled(Boolean.parseBoolean(args[1]));
                module.setBypass(Boolean.parseBoolean(args[2]));
            }
        }
    }
}
