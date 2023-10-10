package tech.atani.client.feature.account;

import com.google.gson.JsonObject;
import tech.atani.client.feature.account.thread.AltLoginThread;

public class Account {
    private String name, password;
    private boolean cracked;

    public Account(String name, String password, boolean cracked) {
        this.name = name;
        this.password = password;
        this.cracked = cracked;
    }

    public void login() {
        new AltLoginThread(this).run();
    }

    public JsonObject save() {
        JsonObject object = new JsonObject();
        object.addProperty("Password", password);
        object.addProperty("Cracked", cracked);
        return object;
    }

    public void load(JsonObject object) {
        if (object.has("Password"))
            setPassword(object.get("Password").getAsString());
        if (object.has("Cracked"))
            setCracked(object.get("Cracked").getAsBoolean());
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean isCracked() {
        return cracked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCracked(boolean cracked) {
        this.cracked = cracked;
    }
}
