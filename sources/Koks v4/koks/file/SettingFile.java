package koks.file;

import koks.api.registry.file.Files;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.manager.value.Value;
import koks.api.manager.value.ValueManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Files.Info(name = "settings")
public class SettingFile extends Files {

    @Override
    public void writeFile(BufferedWriter writer) throws Exception {
        for (Value<?> value : ValueManager.getInstance().getValues()) {
            Module module = (Module) value.getObject();
            if (module != null) {
                writer.write(module.getName() + ":" + value.getName() + ":" + value.getValue() +  "\n");
            }
        }
    }

    @Override
    public void readFile(BufferedReader reader) throws Exception {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] args = line.split(":");
            Module module = ModuleRegistry.getModule(args[0]);
            if(module != null && args.length >= 3) {
                Value<?> value = ValueManager.getInstance().getValue(args[1], module);
                if (value != null) {
                    value.castIfPossible(args[2]);
                }
            }
        }
    }
}
