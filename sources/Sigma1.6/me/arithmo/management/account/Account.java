/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.Expose
 */
package me.arithmo.management.account;

import com.google.gson.annotations.Expose;
import java.util.HashMap;
import javax.crypto.spec.SecretKeySpec;
import me.arithmo.management.Saveable;
import me.arithmo.management.SubFolder;
import me.arithmo.management.account.relation.UserStatus;
import me.arithmo.util.security.Crypto;

public class Account
extends Saveable {
    @Expose
    private String id;
    @Expose
    private String capeURL;
    @Expose
    private boolean premium;
    @Expose
    private HashMap<String, UserStatus> relationships = new HashMap();
    @Expose
    private HashMap<String, String> aliases = new HashMap();
    private String loginUser;
    private String loginPass;
    private String display;
    @Expose
    private String cryptoUser;
    @Expose
    private String cryptoPass;
    @Expose
    private String cryptoDisplay;

    public Account(String id) {
        this.id = id;
        this.setFolderType(SubFolder.Alt);
        this.load();
    }

    public Account(String loginUser, String loginPass) {
        this.loginUser = loginUser;
        this.loginPass = loginPass;
        this.display = loginUser;
        this.premium = true;
        this.id = loginUser.contains("@") ? loginUser.substring(0, loginUser.indexOf("@")) : loginUser;
        this.updateCrypto();
        this.setFolderType(SubFolder.Alt);
    }

    @Override
    public Saveable load() {
        Account loaded = (Account)super.load();
        try {
            this.id = loaded.getID();
            this.loginUser = Crypto.decrypt(this.getSecret(), loaded.getCryptoUser());
            this.loginPass = Crypto.decrypt(this.getSecret(), loaded.getCryptoPass());
            this.display = Crypto.decrypt(this.getSecret(), loaded.getCryptoDisplay());
            this.premium = loaded.premium;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.updateCrypto();
        return this;
    }

    private SecretKeySpec getSecret() {
        byte[] secret = Crypto.getUserKey(16);
        return new SecretKeySpec(secret, 0, secret.length, "AES");
    }

    public String getUser() {
        return this.loginUser;
    }

    public String getPass() {
        return this.loginPass;
    }

    public String getDisplay() {
        return this.display;
    }

    public String getID() {
        return this.id;
    }

    public String getCapeURL() {
        return this.capeURL;
    }

    public boolean hasCape() {
        return this.capeURL != null && this.capeURL.length() > 7;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public boolean isPremium() {
        return this.premium;
    }

    public HashMap<String, String> getAliases() {
        return this.aliases;
    }

    public HashMap<String, UserStatus> getRelationships() {
        return this.relationships;
    }

    public void setAlias(String username, String newName) {
        if (this.aliases.containsKey(username)) {
            this.aliases.remove(username);
        }
        this.aliases.put(username, newName);
    }

    public String getAliasedText(String username, String text) {
        return text.replace(username, this.aliases.get(username));
    }

    public UserStatus getRelation(String username) {
        if (!this.relationships.containsKey(username)) {
            return this.relationships.get(username);
        }
        return null;
    }

    public void setRelationships(HashMap<String, UserStatus> relationships) {
        this.relationships = relationships;
    }

    public void setPass(String loginPass) {
        this.loginPass = loginPass;
    }

    public String getCryptoUser() {
        return this.cryptoUser;
    }

    public String getCryptoPass() {
        return this.cryptoPass;
    }

    public String getCryptoDisplay() {
        return this.cryptoDisplay;
    }

    public void setUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void updateCrypto() {
        try {
            this.cryptoUser = Crypto.encrypt(this.getSecret(), this.loginUser);
            this.cryptoPass = Crypto.encrypt(this.getSecret(), this.loginPass);
            this.cryptoDisplay = Crypto.encrypt(this.getSecret(), this.display);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFileName() {
        return this.id + ".json";
    }
}

