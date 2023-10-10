/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Branch {
    @JsonProperty(value="_id")
    public String id;
    @SerializedName(value="name")
    @JsonProperty(value="name")
    @Expose
    public String name;
    @SerializedName(value="description")
    @JsonProperty(value="description")
    @Expose
    public String description;
    @SerializedName(value="download-url")
    @JsonProperty(value="download-url")
    @Expose
    public String downloadUrl;
    @SerializedName(value="build-number")
    @JsonProperty(value="build-numver")
    @Expose
    public String buildNumber;
    @SerializedName(value="version")
    @JsonProperty(value="version")
    @Expose
    public String version;

    public Branch(String name, String description, String downloadUrl, String buildNumber, String version) {
        this.name = name;
        this.description = description;
        this.downloadUrl = downloadUrl;
        this.buildNumber = buildNumber;
        this.version = version;
    }

    public Branch() {
    }

    public Branch clone() {
        Branch branch = new Branch(this.name, this.description, this.downloadUrl, this.buildNumber, this.version);
        branch.id = this.id;
        return branch;
    }

    public JsonObject toJsonObject() {
        Gson gson = new Gson();
        JsonObject object = gson.fromJson(gson.toJson(this), JsonObject.class);
        object.remove("id");
        return object;
    }
}

