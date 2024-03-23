package dev.zprestige.prestige.client.protection;

public class Session {
    public int uid;
    public String username;
    public String email;
    public String id;
    public String hwid;

    public Session(int n, String string, String string2, String string3, String string4) {
        this.uid = n;
        this.username = string;
        this.email = string2;
        this.id = string3;
        this.hwid = string4;
    }

    public int getUid() {
        return this.uid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getID() {
        return this.id;
    }

    public String getHWID() {
        return this.hwid;
    }
}
