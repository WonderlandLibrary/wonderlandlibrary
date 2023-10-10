/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi.requests;

import com.google.gson.annotations.SerializedName;

public class VersionUpdateRequest {
    @SerializedName(value="build-number")
    public String buildNumber;
    @SerializedName(value="version")
    public String version;
    @SerializedName(value="branch-name")
    public String branchName;
}

