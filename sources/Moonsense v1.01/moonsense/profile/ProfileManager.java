// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.profile;

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

public class ProfileManager extends ArrayList<Profile>
{
    private File configFile;
    private Profile selected;
    
    public ProfileManager(final File profilesFile) {
        this.configFile = profilesFile;
    }
    
    public void load() {
        this.createStructure();
        final JsonObject profilesJson = this.loadJsonFile(this.configFile);
        try {
            final JsonObject currentProfile = profilesJson.getAsJsonObject("selected");
            final String name = currentProfile.get("name").getAsString();
            final String folderName = currentProfile.get("folderName").getAsString();
            final Profile p = new Profile(name, folderName);
            if (p.getName().equals("Default") && p.getFolderName().isEmpty()) {
                this.selected = Profile.DEFAULT_PROFILE;
            }
            else {
                this.selected = p;
            }
        }
        catch (Exception e) {
            this.selected = Profile.DEFAULT_PROFILE;
        }
        try {
            final JsonArray jsonArray = profilesJson.getAsJsonArray("profiles");
            for (final JsonElement elem : jsonArray) {
                final JsonObject obj = elem.getAsJsonObject();
                final String name2 = obj.get("name").getAsString();
                final String folderName2 = obj.get("folderName").getAsString();
                this.add(new Profile(name2, folderName2));
            }
        }
        catch (Exception ex) {}
    }
    
    public void save() {
        this.createStructure();
        final JsonObject profilesJson = new JsonObject();
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(this.configFile));
            final JsonArray jsonArray = new JsonArray();
            try {
                final JsonObject obj = new JsonObject();
                obj.addProperty("name", this.selected.getName());
                obj.addProperty("folderName", this.selected.getFolderName());
                for (final Profile p : this) {
                    final JsonObject obj2 = new JsonObject();
                    obj2.addProperty("name", p.getName());
                    obj2.addProperty("folderName", p.getFolderName());
                    jsonArray.add(obj2);
                }
                profilesJson.add("selected", obj);
                profilesJson.add("profiles", jsonArray);
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(profilesJson.toString())));
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
    
    public Profile getSelected() {
        return this.selected;
    }
    
    public void setSelected(final Profile selected) {
        this.selected = selected;
    }
}
