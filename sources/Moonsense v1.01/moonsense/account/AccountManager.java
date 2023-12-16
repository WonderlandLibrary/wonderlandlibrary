// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.account;

import java.util.Collection;
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
    private File accountsFile;
    private Account lastAlt;
    
    public AccountManager(final File parent) {
        this.accounts = new ArrayList<Account>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.accountsFile = new File(String.valueOf(parent.toString()) + File.separator + "Moonsense-Accounts.json");
        this.load();
    }
    
    public void save() {
        if (this.accountsFile == null) {
            return;
        }
        try {
            if (!this.accountsFile.exists()) {
                this.accountsFile.createNewFile();
            }
            final PrintWriter printWriter = new PrintWriter(this.accountsFile);
            printWriter.write(this.gson.toJson(this.toJson()));
            printWriter.close();
        }
        catch (IOException ex) {}
    }
    
    public void load() {
        if (!this.accountsFile.exists()) {
            this.save();
            return;
        }
        try {
            final JsonObject json = new JsonParser().parse(new FileReader(this.accountsFile)).getAsJsonObject();
            this.fromJson(json);
        }
        catch (IOException ex) {}
    }
    
    public JsonObject toJson() {
        final JsonObject jsonObject = new JsonObject();
        final JsonArray jsonArray = new JsonArray();
        this.getAccounts().forEach(account -> jsonArray.add(account.toJson()));
        if (this.lastAlt != null) {
            jsonObject.add("lastalt", this.lastAlt.toJson());
        }
        jsonObject.add("accounts", jsonArray);
        return jsonObject;
    }
    
    public void fromJson(final JsonObject json) {
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
            if (this.getAccountByName(account2.getUsername()) == null) {
                this.getAccounts().add(account2);
            }
        });
    }
    
    public void remove(final String username) {
        for (final Account account : this.getAccounts()) {
            if (account.getUsername().equalsIgnoreCase(username)) {
                this.getAccounts().remove(account);
            }
        }
    }
    
    public Account getAccountByName(final String name) {
        for (final Account account : this.getAccounts()) {
            if (account.getUsername().equalsIgnoreCase(name)) {
                return account;
            }
        }
        return null;
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
    
    public void optimizeAccounts() {
        final ArrayList<Account> toRemove = new ArrayList<Account>();
        final ArrayList<Account> duplicate = this.accounts;
        for (final Account acc : this.accounts) {
            duplicate.remove(acc);
            if (duplicate.contains(acc)) {
                toRemove.add(acc);
            }
        }
        this.accounts.removeAll(toRemove);
        this.save();
    }
}
