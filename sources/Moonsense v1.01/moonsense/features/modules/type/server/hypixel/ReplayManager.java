// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.server.hypixel;

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
import java.util.Map;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.io.File;
import java.util.HashMap;

public class ReplayManager extends HashMap<String, String>
{
    private File replaysFile;
    
    public ReplayManager() {
        this.replaysFile = new File("Moonsense Client", "replays.json");
    }
    
    public void load() {
        this.createStructure();
        final JsonObject replaysJson = this.loadJsonFile(this.replaysFile);
        final JsonArray jsonArray = replaysJson.getAsJsonArray("replays");
        for (final JsonElement elem : jsonArray) {
            final JsonObject obj = elem.getAsJsonObject();
            final String name = obj.get("name").getAsString();
            final String cmd = obj.get("cmd").getAsString();
            this.put(name, cmd);
        }
    }
    
    public void save() {
        this.createStructure();
        final JsonObject replaysJson = new JsonObject();
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(this.replaysFile));
            final JsonArray jsonArray = new JsonArray();
            try {
                for (final Map.Entry<String, String> rp : this.entrySet()) {
                    final JsonObject replayObject = new JsonObject();
                    replayObject.addProperty("name", rp.getKey());
                    replayObject.addProperty("cmd", rp.getValue());
                    jsonArray.add(replayObject);
                }
                replaysJson.add("replays", jsonArray);
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(replaysJson.toString())));
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
        if (!this.replaysFile.exists() || !MoonsenseClient.dir.exists()) {
            MoonsenseClient.dir.mkdirs();
            try {
                this.replaysFile.createNewFile();
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
