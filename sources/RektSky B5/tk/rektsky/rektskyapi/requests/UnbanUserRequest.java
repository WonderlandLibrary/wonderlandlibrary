/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnbanUserRequest {
    @Expose
    @SerializedName(value="username")
    public String username;
}

