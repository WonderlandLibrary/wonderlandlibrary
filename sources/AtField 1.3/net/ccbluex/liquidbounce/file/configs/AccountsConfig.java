/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonSyntaxException
 */
package net.ccbluex.liquidbounce.file.configs;

import com.google.gson.Gson;
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
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;

public class AccountsConfig
extends FileConfig {
    private final List accounts = new ArrayList();

    public void addAccount(String string, String string2, String string3) {
        AccountsConfig accountsConfig = this;
        if (string != false) {
            return;
        }
        this.accounts.add(new MinecraftAccount(string, string2, string3));
    }

    public List getAccounts() {
        return this.accounts;
    }

    public void addAccount(String string) {
        AccountsConfig accountsConfig = this;
        if (string != false) {
            return;
        }
        this.accounts.add(new MinecraftAccount(string));
    }

    public void clearAccounts() {
        this.accounts.clear();
    }

    public void removeAccount(int n) {
        this.accounts.remove(n);
    }

    public void addAccount(String string, String string2) {
        AccountsConfig accountsConfig = this;
        if (string != false) {
            return;
        }
        this.accounts.add(new MinecraftAccount(string, string2));
    }

    public void removeAccount(MinecraftAccount minecraftAccount) {
        this.accounts.remove(minecraftAccount);
    }

    @Override
    protected void saveConfig() throws IOException {
        JsonArray jsonArray = new JsonArray();
        for (MinecraftAccount minecraftAccount : this.accounts) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", minecraftAccount.getName());
            jsonObject.addProperty("password", minecraftAccount.getPassword());
            jsonObject.addProperty("inGameName", minecraftAccount.getAccountName());
            jsonArray.add((JsonElement)jsonObject);
        }
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson((JsonElement)jsonArray));
        printWriter.close();
    }

    @Override
    protected void loadConfig() throws IOException {
        this.clearAccounts();
        try {
            JsonElement jsonElement = new JsonParser().parse((Reader)new BufferedReader(new FileReader(this.getFile())));
            if (jsonElement instanceof JsonNull) {
                return;
            }
            for (JsonElement jsonElement2 : jsonElement.getAsJsonArray()) {
                JsonObject jsonObject = jsonElement2.getAsJsonObject();
                JsonElement jsonElement3 = jsonObject.get("name");
                JsonElement jsonElement4 = jsonObject.get("password");
                JsonElement jsonElement5 = jsonObject.get("inGameName");
                if (jsonElement5 == null || jsonElement5.isJsonNull()) {
                    this.addAccount(jsonElement3.getAsString(), jsonElement4.getAsString());
                    continue;
                }
                if (jsonElement5.isJsonNull() && jsonElement4.isJsonNull()) {
                    this.addAccount(jsonElement3.getAsString());
                    continue;
                }
                this.addAccount(jsonElement3.getAsString(), jsonObject.get("password").getAsString(), jsonObject.get("inGameName").getAsString());
            }
        }
        catch (JsonSyntaxException | IllegalStateException throwable) {
            ClientUtils.getLogger().info("[FileManager] Try to load old Accounts config...");
            List list = (List)new Gson().fromJson((Reader)new BufferedReader(new FileReader(this.getFile())), List.class);
            if (list == null) {
                return;
            }
            this.accounts.clear();
            for (String string : list) {
                String[] stringArray = string.split(":");
                if (stringArray.length >= 3) {
                    this.accounts.add(new MinecraftAccount(stringArray[0], stringArray[1], stringArray[2]));
                    continue;
                }
                if (stringArray.length == 2) {
                    this.accounts.add(new MinecraftAccount(stringArray[0], stringArray[1]));
                    continue;
                }
                this.accounts.add(new MinecraftAccount(stringArray[0]));
            }
            ClientUtils.getLogger().info("[FileManager] Loaded old Accounts config...");
            this.saveConfig();
            ClientUtils.getLogger().info("[FileManager] Saved Accounts to new config...");
        }
    }

    public AccountsConfig(File file) {
        super(file);
    }
}

