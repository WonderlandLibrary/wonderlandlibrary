package me.aquavit.liquidsense.file.configs;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.file.FileConfig;
import me.aquavit.liquidsense.file.FileManager;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class SetNameConfig extends FileConfig {
    /**
     * Constructor of config
     *
     * @param file of config
     */
    public SetNameConfig(File file) {
        super(file);
    }

    @Override
    protected void loadConfig() throws IOException {
        final JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(getFile())));

        if(jsonElement instanceof JsonNull)
            return;

        final Iterator<Map.Entry<String, JsonElement>> entryIterator = jsonElement.getAsJsonObject().entrySet().iterator();
        while(entryIterator.hasNext()) {
            final Map.Entry<String, JsonElement> entry = entryIterator.next();
            final Module module = LiquidSense.moduleManager.getModule(entry.getKey());
            if(module != null) {
                final JsonObject jsonModule = (JsonObject) entry.getValue();

                module.setArrayListName(jsonModule.get("arrayListName").getAsString());
             //   module.setName(jsonModule.get("arrayListName").getAsString());
            }
        }
    }

    @Override
    protected void saveConfig() throws IOException {
        final JsonObject jsonObject = new JsonObject();

        for (final Module module : LiquidSense.moduleManager.getModules()) {
            final JsonObject jsonMod = new JsonObject();
            jsonMod.addProperty("arrayListName", module.getArrayListName());
            jsonObject.add(module.getName(), jsonMod);
        }
        final PrintWriter printWriter = new PrintWriter(new FileWriter(getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson(jsonObject));
        printWriter.close();
    }
}
