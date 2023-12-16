// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.quickplay;

import java.util.Collections;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import com.google.gson.JsonParseException;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonElement;
import com.google.gson.GsonBuilder;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import moonsense.utils.StringUtils;
import com.google.gson.JsonParser;
import java.io.IOException;
import moonsense.MoonsenseClient;
import java.util.LinkedHashMap;
import java.io.File;
import java.util.Map;

public class QuickplayManager
{
    private Map<String, QuickplayGame> games;
    private final File quickplayBackupFile;
    
    public QuickplayManager() {
        this.games = new LinkedHashMap<String, QuickplayGame>();
        this.quickplayBackupFile = new File(MoonsenseClient.dir, "quickplay.json");
        if (!this.quickplayBackupFile.exists() || !MoonsenseClient.dir.exists()) {
            MoonsenseClient.dir.mkdirs();
            try {
                this.quickplayBackupFile.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.initDatabase();
    }
    
    public QuickplayGame getGame(final String id) {
        return this.games.get(id);
    }
    
    public Map<String, QuickplayGame> getGames() {
        return this.games;
    }
    
    private void initDatabase() {
        try {
            final JsonObject obj = new JsonParser().parse(StringUtils.urlToString(StringUtils.sneakyParse("https://bugg.co/quickplay/mod/gamelist"))).getAsJsonObject();
            final BufferedWriter writer = new BufferedWriter(new FileWriter(this.quickplayBackupFile));
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(obj.toString())));
            writer.close();
            final JsonArray array = obj.get("content").getAsJsonObject().get("games").getAsJsonArray();
            for (final JsonElement gameElement : array) {
                this.games.put(gameElement.getAsJsonObject().get("unlocalizedName").getAsString(), new QuickplayGame(gameElement.getAsJsonObject()));
            }
        }
        catch (IOException | JsonSyntaxException ex2) {
            final Exception ex;
            final Exception error = ex;
            error.printStackTrace();
            try {
                final JsonObject obj2 = this.loadGamesFromFile();
                final JsonArray array = obj2.get("content").getAsJsonObject().get("games").getAsJsonArray();
                for (final JsonElement gameElement : array) {
                    this.games.put(gameElement.getAsJsonObject().get("unlocalizedName").getAsString(), new QuickplayGame(gameElement.getAsJsonObject()));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private JsonObject loadGamesFromFile() {
        if (!this.quickplayBackupFile.exists()) {
            return null;
        }
        final JsonElement fileElement = new JsonParser().parse(this.getFileContents());
        if (fileElement == null || fileElement.isJsonNull()) {
            throw new JsonParseException("File \"" + this.quickplayBackupFile.getName() + "\" is null!");
        }
        return fileElement.getAsJsonObject();
    }
    
    private String getFileContents() {
        if (this.quickplayBackupFile.exists()) {
            try {
                final BufferedReader reader = new BufferedReader(new FileReader(this.quickplayBackupFile));
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
