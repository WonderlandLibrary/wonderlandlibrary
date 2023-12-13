package koks.file;

import koks.Koks;
import koks.api.registry.file.Files;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Files.Info(name = "api-key")
public class ApiKeyFile extends Files {

    @Override
    public void writeFile(BufferedWriter writer) throws Exception {
        writer.write("altening:" + Koks.getKoks().alteningApiKey);
    }

    @Override
    public void readFile(BufferedReader reader) throws Exception {
        String line;
        while((line = reader.readLine()) != null) {
            String[] args = line.split(":");
            if(args[0].equalsIgnoreCase("altening"))
                Koks.getKoks().alteningApiKey = args[1];
        }
    }
}
