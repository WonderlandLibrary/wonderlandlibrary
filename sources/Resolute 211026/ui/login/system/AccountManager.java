// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.login.system;

import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.Reader;
import java.io.FileReader;
import com.google.gson.JsonParser;
import java.io.IOException;
import com.google.gson.JsonElement;
import java.io.PrintWriter;
import com.google.gson.GsonBuilder;
import java.io.File;
import com.google.gson.Gson;
import java.util.ArrayList;

public class AccountManager
{
    private ArrayList<Account> accounts;
    private final Gson gson;
    private File altsFile;
    private String alteningKey;
    private String lastAlteningAlt;
    private Account lastAlt;
    
    public AccountManager(final File parent) {
        this.accounts = new ArrayList<Account>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.altsFile = new File(parent.toString() + File.separator + "alts.txt");
        this.load();
    }
    
    public void save() {
        if (this.altsFile == null) {
            return;
        }
        try {
            if (!this.altsFile.exists()) {
                this.altsFile.createNewFile();
            }
            final PrintWriter printWriter = new PrintWriter(this.altsFile);
            printWriter.write(this.gson.toJson((JsonElement)this.toJson()));
            printWriter.close();
        }
        catch (IOException ex) {}
    }
    
    public void load() {
        if (!this.altsFile.exists()) {
            this.save();
            return;
        }
        try {
            final JsonObject json = new JsonParser().parse((Reader)new FileReader(this.altsFile)).getAsJsonObject();
            this.fromJson(json);
        }
        catch (IOException ex) {}
    }
    
    public JsonObject toJson() {
        final JsonObject jsonObject = new JsonObject();
        final JsonArray jsonArray = new JsonArray();
        this.getAccounts().forEach(account -> jsonArray.add((JsonElement)account.toJson()));
        if (this.alteningKey != null) {
            jsonObject.addProperty("altening", this.alteningKey);
        }
        if (this.lastAlteningAlt != null) {
            jsonObject.addProperty("alteningAlt", this.lastAlteningAlt);
        }
        if (this.lastAlt != null) {
            jsonObject.add("lastalt", (JsonElement)this.lastAlt.toJson());
        }
        jsonObject.add("accounts", (JsonElement)jsonArray);
        return jsonObject;
    }
    
    public void fromJson(final JsonObject json) {
        if (json.has("altening")) {
            this.alteningKey = json.get("altening").getAsString();
        }
        if (json.has("alteningAlt")) {
            this.lastAlteningAlt = json.get("alteningAlt").getAsString();
        }
        if (json.has("lastalt")) {
            final Account account = new Account();
            account.fromJson(json.get("lastalt").getAsJsonObject());
            this.lastAlt = account;
        }
        final JsonArray jsonArray = json.get("accounts").getAsJsonArray();
        final JsonObject jsonObject;
        final Account account2;
        jsonArray.forEach(jsonElement -> {
            jsonObject = jsonElement;
            account2 = new Account();
            account2.fromJson(jsonObject);
            this.getAccounts().add(account2);
        });
    }
    
    public void remove(final String username) {
        for (final Account account : this.getAccounts()) {
            if (account.getName().equalsIgnoreCase(username)) {
                this.getAccounts().remove(account);
            }
        }
    }
    
    public Account getLastAlt() {
        return this.lastAlt;
    }
    
    public void setLastAlt(final Account lastAlt) {
        this.lastAlt = lastAlt;
    }
    
    public ArrayList<Account> getAccounts() {
        return this.accounts;
    }
}
