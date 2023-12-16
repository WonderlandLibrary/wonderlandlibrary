// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.integrations.spotify;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import com.google.gson.JsonParseException;
import moonsense.MoonsenseClient;
import java.util.function.Consumer;
import java.io.IOException;
import java.util.Collections;
import com.google.gson.JsonParser;
import com.google.gson.GsonBuilder;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.io.File;
import java.util.ArrayList;

public class FavoriteSongManager extends ArrayList<String>
{
    private File configFile;
    
    public FavoriteSongManager() {
        this.configFile = new File("Moonsense Client", "favorite-songs.json");
    }
    
    public void load() {
        this.createStructure();
        final JsonObject moduleJson = this.loadJsonFile(this.configFile);
        final JsonArray jsonArray = moduleJson.getAsJsonArray("ids");
        for (final JsonElement elem : jsonArray) {
            final String obj = elem.getAsString();
            this.add(obj);
        }
    }
    
    public void save() {
        this.createStructure();
        final JsonObject waypointsJson = new JsonObject();
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(this.configFile));
            final JsonArray jsonArray = new JsonArray();
            try {
                for (final String s : this) {
                    jsonArray.add(s);
                }
                waypointsJson.add("ids", jsonArray);
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(waypointsJson.toString())));
            }
            finally {
                if (Collections.singletonList(writer).get(0) != null) {
                    writer.close();
                }
            }
            if (Collections.singletonList(writer).get(0) != null) {
                writer.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void getNonNull(final JsonObject jsonObject, final String key, final Consumer<JsonElement> consumer) {
        if (jsonObject != null && jsonObject.get(key) != null) {
            consumer.accept(jsonObject.get(key));
        }
    }
    
    private void createStructure() {
        if (!this.configFile.exists() || !MoonsenseClient.dir.exists()) {
            MoonsenseClient.dir.mkdirs();
            try {
                this.configFile.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private JsonObject loadJsonFile(final File file) {
        if (!file.exists()) {
            return null;
        }
        final JsonElement fileElement = new JsonParser().parse(this.getFileContents(file));
        if (fileElement == null || fileElement.isJsonNull()) {
            throw new JsonParseException("File \"" + file.getName() + "\" is null!");
        }
        return fileElement.getAsJsonObject();
    }
    
    private String getFileContents(final File file) {
        if (file.exists()) {
            try {
                final BufferedReader reader = new BufferedReader(new FileReader(file));
                try {
                    final StringBuilder builder = new StringBuilder();
                    String nextLine;
                    while ((nextLine = reader.readLine()) != null) {
                        builder.append(nextLine);
                    }
                    return builder.toString().equals("") ? "{}" : builder.toString();
                }
                finally {
                    if (Collections.singletonList(reader).get(0) != null) {
                        reader.close();
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "{}";
    }
}
