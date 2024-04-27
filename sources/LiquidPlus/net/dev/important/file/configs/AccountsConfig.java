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
package net.dev.important.file.configs;

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
import net.dev.important.file.FileConfig;
import net.dev.important.file.FileManager;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.login.MinecraftAccount;

public class AccountsConfig
extends FileConfig {
    private final List<MinecraftAccount> accounts = new ArrayList<MinecraftAccount>();

    public AccountsConfig(File file) {
        super(file);
    }

    @Override
    protected void loadConfig() throws IOException {
        this.clearAccounts();
        try {
            JsonElement jsonElement = new JsonParser().parse((Reader)new BufferedReader(new FileReader(this.getFile())));
            if (jsonElement instanceof JsonNull) {
                return;
            }
            for (JsonElement accountElement : jsonElement.getAsJsonArray()) {
                JsonObject accountObject = accountElement.getAsJsonObject();
                JsonElement name = accountObject.get("name");
                JsonElement password = accountObject.get("password");
                JsonElement inGameName = accountObject.get("inGameName");
                if (inGameName == null || inGameName.isJsonNull()) {
                    this.addAccount(name.getAsString(), password.getAsString());
                    continue;
                }
                if (inGameName.isJsonNull() && password.isJsonNull()) {
                    this.addAccount(name.getAsString());
                    continue;
                }
                this.addAccount(name.getAsString(), accountObject.get("password").getAsString(), accountObject.get("inGameName").getAsString());
            }
        }
        catch (JsonSyntaxException | IllegalStateException ex) {
            ClientUtils.getLogger().info("[FileManager] Try to load old Accounts config...");
            List accountList = (List)new Gson().fromJson((Reader)new BufferedReader(new FileReader(this.getFile())), List.class);
            if (accountList == null) {
                return;
            }
            this.accounts.clear();
            for (String account : accountList) {
                String[] information = account.split(":");
                if (information.length >= 3) {
                    this.accounts.add(new MinecraftAccount(information[0], information[1], information[2]));
                    continue;
                }
                if (information.length == 2) {
                    this.accounts.add(new MinecraftAccount(information[0], information[1]));
                    continue;
                }
                this.accounts.add(new MinecraftAccount(information[0]));
            }
            ClientUtils.getLogger().info("[FileManager] Loaded old Accounts config...");
            this.saveConfig();
            ClientUtils.getLogger().info("[FileManager] Saved Accounts to new config...");
        }
    }

    @Override
    protected void saveConfig() throws IOException {
        JsonArray jsonArray = new JsonArray();
        for (MinecraftAccount minecraftAccount : this.accounts) {
            JsonObject friendObject = new JsonObject();
            friendObject.addProperty("name", minecraftAccount.getName());
            friendObject.addProperty("password", minecraftAccount.getPassword());
            friendObject.addProperty("inGameName", minecraftAccount.getAccountName());
            jsonArray.add((JsonElement)friendObject);
        }
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson((JsonElement)jsonArray));
        printWriter.close();
    }

    public void addAccount(String name) {
        if (this.accountExists(name)) {
            return;
        }
        this.accounts.add(new MinecraftAccount(name));
    }

    public void addAccount(String name, String password) {
        if (this.accountExists(name)) {
            return;
        }
        this.accounts.add(new MinecraftAccount(name, password));
    }

    public void addAccount(String name, String password, String inGameName) {
        if (this.accountExists(name)) {
            return;
        }
        this.accounts.add(new MinecraftAccount(name, password, inGameName));
    }

    public void removeAccount(int selectedSlot) {
        this.accounts.remove(selectedSlot);
    }

    public void removeAccount(MinecraftAccount account) {
        this.accounts.remove(account);
    }

    public boolean accountExists(String name) {
        for (MinecraftAccount minecraftAccount : this.accounts) {
            if (!minecraftAccount.getName().equals(name)) continue;
            return true;
        }
        return false;
    }

    public void clearAccounts() {
        this.accounts.clear();
    }

    public List<MinecraftAccount> getAccounts() {
        return this.accounts;
    }
}

