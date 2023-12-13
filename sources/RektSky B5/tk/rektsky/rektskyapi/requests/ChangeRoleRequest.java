/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeRoleRequest {
    @Expose
    @SerializedName(value="username")
    public String username;
    @Expose
    @SerializedName(value="role")
    public String role;
}

