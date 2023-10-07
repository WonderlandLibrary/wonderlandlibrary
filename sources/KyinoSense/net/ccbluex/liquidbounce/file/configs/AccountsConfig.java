/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 */
package net.ccbluex.liquidbounce.file.configs;

import com.google.gson.Gson;
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
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;

public class AccountsConfig
extends FileConfig {
    public final List<MinecraftAccount> altManagerMinecraftAccounts = new ArrayList<MinecraftAccount>();

    public AccountsConfig(File file) {
        super(file);
    }

    @Override
    protected void loadConfig() throws IOException {
        List accountList = (List)new Gson().fromJson((Reader)new BufferedReader(new FileReader(this.getFile())), List.class);
        if (accountList == null) {
            return;
        }
        this.altManagerMinecraftAccounts.clear();
        for (String account : accountList) {
            String[] information = account.split(":");
            if (information.length >= 3) {
                this.altManagerMinecraftAccounts.add(new MinecraftAccount(information[0], information[1], information[2]));
                continue;
            }
            if (information.length == 2) {
                this.altManagerMinecraftAccounts.add(new MinecraftAccount(information[0], information[1]));
                continue;
            }
            this.altManagerMinecraftAccounts.add(new MinecraftAccount(information[0]));
        }
    }

    @Override
    protected void saveConfig() throws IOException {
        ArrayList<String> accountList = new ArrayList<String>();
        for (MinecraftAccount minecraftAccount : this.altManagerMinecraftAccounts) {
            accountList.add(minecraftAccount.getName() + ":" + (minecraftAccount.getPassword() == null ? "" : minecraftAccount.getPassword()) + ":" + (minecraftAccount.getAccountName() == null ? "" : minecraftAccount.getAccountName()));
        }
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson(accountList));
        printWriter.close();
    }
}

