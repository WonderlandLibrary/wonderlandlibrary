/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import tk.rektsky.rektskyapi.user.Role;

public class User {
    @JsonProperty(value="_id")
    public String id;
    @JsonProperty(value="username")
    public String username;
    @JsonProperty(value="discord-id")
    public String discordId;
    @JsonProperty(value="discord-avatar")
    public String discordAvatar;
    @JsonProperty(value="discord-name")
    public String discordName;
    @JsonProperty(value="role")
    public Role role;
    @JsonProperty(value="hwid")
    public String hwid;
    @JsonProperty(value="session")
    public String session;
    @JsonProperty(value="hwid-change-time")
    public Long changeTime = 0L;
    @JsonProperty(value="banned")
    public Boolean banned = false;
    @JsonProperty(value="ban-reason")
    public String banReason = "";

    public User() {
    }

    public User(String username, String discordId, Role role, String hwid, String session) {
        this.username = username;
        this.discordId = discordId;
        this.role = role;
        this.hwid = hwid;
        this.session = session;
    }

    public JsonObject toJsonObject() {
        Gson gson = new Gson();
        JsonObject object = gson.fromJson(gson.toJson(this), JsonObject.class);
        return object;
    }
}

