// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.config.impl;

import com.google.gson.annotations.SerializedName;
import Lavish.altmanager.Alt;
import java.util.Arrays;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import Lavish.altmanager.AltManager;
import com.google.gson.JsonArray;
import com.google.gson.Gson;
import java.io.File;
import Lavish.config.IFile;

public class AccountsFile implements IFile
{
    private File file;
    
    @Override
    public void save(final Gson gson) {
        final JsonArray array = new JsonArray();
        final JsonObject object;
        final JsonArray jsonArray;
        AltManager.registry.forEach(alt -> {
            object = new JsonObject();
            object.addProperty("mask", alt.getMask());
            object.addProperty("username", alt.getUsername());
            object.addProperty("password", alt.getPassword());
            jsonArray.add((JsonElement)object);
            return;
        });
        this.writeFile(gson.toJson((JsonElement)array), this.file);
    }
    
    @Override
    public void load(final Gson gson) {
        if (!this.file.exists()) {
            return;
        }
        final Account[] accounts = (Account[])gson.fromJson(this.readFile(this.file), (Class)Account[].class);
        if (accounts != null) {
            Arrays.stream(accounts).forEach(account -> AltManager.registry.add(new Alt(account.username, account.password, account.mask)));
        }
    }
    
    @Override
    public void setFile(final File root) {
        this.file = new File(root, "/accounts.txt");
    }
    
    final class Account
    {
        @SerializedName("mask")
        final String mask;
        @SerializedName("username")
        final String username;
        @SerializedName("password")
        final String password;
        
        Account(final String mask, final String username, final String password) {
            this.mask = mask;
            this.username = username;
            this.password = password;
        }
    }
}
