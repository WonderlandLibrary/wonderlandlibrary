// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.altmanager.helpers;

import dev.tenacity.utils.misc.NetworkingUtils;
import java.io.FileNotFoundException;
import java.io.Reader;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.JsonElement;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import com.google.gson.JsonObject;
import dev.tenacity.Tenacity;
import com.google.gson.GsonBuilder;
import java.io.File;
import com.google.gson.Gson;

public class KingGenApi
{
    private final Gson gson;
    private final File kingAltData;
    public String generated;
    public String generatedToday;
    public String username;
    private String key;
    
    public KingGenApi() {
        this.gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
        this.kingAltData = new File(Tenacity.DIRECTORY, "KingGen.json");
        this.generated = "0";
        this.generatedToday = "0";
        this.username = "";
        this.key = "";
    }
    
    public void setKey(final String key) {
        this.create();
        final JsonObject keyObject = new JsonObject();
        keyObject.addProperty("key", key);
        try {
            final Writer writer = new BufferedWriter(new FileWriter(this.kingAltData));
            this.gson.toJson((JsonElement)keyObject, (Appendable)writer);
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.key = key;
    }
    
    public void refreshKey() {
        this.create();
        JsonObject fileContent;
        try {
            fileContent = JsonParser.parseReader((Reader)new FileReader(this.kingAltData)).getAsJsonObject();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        if (fileContent.has("key")) {
            this.key = fileContent.get("key").getAsString();
        }
        final NetworkingUtils.HttpResponse request = NetworkingUtils.httpsConnection("https://kinggen.wtf/api/v2/profile?key=" + this.key);
        if (request == null) {
            return;
        }
        if (request.getResponse() != 200) {
            return;
        }
        final JsonObject responseObject = JsonParser.parseString(request.getContent()).getAsJsonObject();
        if (responseObject.has("username")) {
            this.username = responseObject.get("username").getAsString();
        }
        if (responseObject.has("generated")) {
            this.generated = responseObject.get("generated").getAsString();
            this.generatedToday = responseObject.get("generatedToday").getAsString();
        }
    }
    
    public boolean checkKey() {
        this.create();
        if (this.key.equals("")) {
            return false;
        }
        final NetworkingUtils.HttpResponse request = NetworkingUtils.httpsConnection("https://kinggen.wtf/api/v2/profile?key=" + this.key);
        if (request == null) {
            return false;
        }
        if (request.getResponse() != 200) {
            return false;
        }
        final JsonObject responseObject = JsonParser.parseString(request.getContent()).getAsJsonObject();
        if (responseObject.has("username")) {
            this.username = responseObject.get("username").getAsString();
        }
        if (responseObject.has("generated")) {
            this.generated = responseObject.get("generated").getAsString();
            this.generatedToday = responseObject.get("generatedToday").getAsString();
        }
        return true;
    }
    
    public boolean hasKeyInFile() {
        this.create();
        JsonObject fileContent;
        try {
            fileContent = JsonParser.parseReader((Reader)new FileReader(this.kingAltData)).getAsJsonObject();
        }
        catch (FileNotFoundException e) {
            return false;
        }
        return fileContent.has("key") && !fileContent.get("key").getAsString().equals("");
    }
    
    public final String[] genAlt() {
        this.create();
        final String[] errorResponse = { "error", "error" };
        if (this.key.isEmpty()) {
            try {
                final Reader reader = new FileReader(this.kingAltData);
                final JsonObject fileContent = JsonParser.parseReader(reader).getAsJsonObject();
                if (!fileContent.has("key")) {
                    return errorResponse;
                }
                this.key = fileContent.get("key").getAsString();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        final NetworkingUtils.HttpResponse request = NetworkingUtils.httpsConnection("https://kinggen.wtf/api/v2/alt?key=" + this.key);
        if (request == null) {
            return errorResponse;
        }
        if (request.getResponse() != 200) {
            return errorResponse;
        }
        final JsonObject responseObject = JsonParser.parseString(request.getContent()).getAsJsonObject();
        if (responseObject.has("email") && responseObject.has("password")) {
            return new String[] { responseObject.get("email").getAsString(), responseObject.get("password").getAsString() };
        }
        return errorResponse;
    }
    
    private void create() {
        try {
            if (!this.kingAltData.exists() && this.kingAltData.getParentFile().mkdirs()) {
                this.kingAltData.createNewFile();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
