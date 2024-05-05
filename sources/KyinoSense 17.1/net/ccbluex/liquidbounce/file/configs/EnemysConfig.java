/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonSyntaxException
 */
package net.ccbluex.liquidbounce.file.configs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.file.FileManager;
import net.ccbluex.liquidbounce.utils.ClientUtils;

public class EnemysConfig
extends FileConfig {
    private final List<Enemy> enemys = new ArrayList<Enemy>();

    public EnemysConfig(File file) {
        super(file);
    }

    @Override
    protected void loadConfig() throws IOException {
        this.clearEnemys();
        try {
            JsonElement jsonElement = new JsonParser().parse((Reader)new BufferedReader(new FileReader(this.getFile())));
            if (jsonElement instanceof JsonNull) {
                return;
            }
            for (JsonElement enemyElement : jsonElement.getAsJsonArray()) {
                JsonObject enemyObject = enemyElement.getAsJsonObject();
                this.addEnemy(enemyObject.get("playerName").getAsString(), enemyObject.get("alias").getAsString());
            }
        }
        catch (JsonSyntaxException | IllegalStateException ex) {
            String line;
            ClientUtils.getLogger().info("[FileManager] Try to load old Enemys config...");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.getFile()));
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("{") || line.contains("}")) continue;
                if ((line = line.replace(" ", "").replace("\"", "").replace(",", "")).contains(":")) {
                    String[] data = line.split(":");
                    this.addEnemy(data[0], data[1]);
                    continue;
                }
                this.addEnemy(line);
            }
            bufferedReader.close();
            ClientUtils.getLogger().info("[FileManager] Loaded old Enemys config...");
            this.saveConfig();
            ClientUtils.getLogger().info("[FileManager] Saved Enemys to new config...");
        }
    }

    @Override
    protected void saveConfig() throws IOException {
        JsonArray jsonArray = new JsonArray();
        for (Enemy enemy : this.getEnemys()) {
            JsonObject enemyObject = new JsonObject();
            enemyObject.addProperty("playerName", enemy.getPlayerName());
            enemyObject.addProperty("alias", enemy.getAlias());
            jsonArray.add((JsonElement)enemyObject);
        }
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson((JsonElement)jsonArray));
        printWriter.close();
    }

    public boolean addEnemy(String playerName) {
        return this.addEnemy(playerName, playerName);
    }

    public boolean addEnemy(String playerName, String alias) {
        if (this.isEnemy(playerName)) {
            return false;
        }
        this.enemys.add(new Enemy(playerName, alias));
        return true;
    }

    public boolean removeEnemy(String playerName) {
        if (!this.isEnemy(playerName)) {
            return false;
        }
        this.enemys.removeIf(enemy -> enemy.getPlayerName().equals(playerName));
        return true;
    }

    public boolean isEnemy(String playerName) {
        for (Enemy enemy : this.enemys) {
            if (!enemy.getPlayerName().equals(playerName)) continue;
            return true;
        }
        return false;
    }

    public void clearEnemys() {
        this.enemys.clear();
    }

    public List<Enemy> getEnemys() {
        return this.enemys;
    }

    public class Enemy {
        private final String playerName;
        private final String alias;

        Enemy(String playerName, String alias) {
            this.playerName = playerName;
            this.alias = alias;
        }

        public String getPlayerName() {
            return this.playerName;
        }

        public String getAlias() {
            return this.alias;
        }
    }
}

