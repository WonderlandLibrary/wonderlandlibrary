/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi.permission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestServerAccess {
    @Expose
    @SerializedName(value="op")
    public Boolean op = false;
    @Expose
    @SerializedName(value="access")
    public Boolean hasAccess = false;

    public TestServerAccess() {
    }

    public TestServerAccess(boolean op, boolean hasAccess) {
        this.op = op;
        this.hasAccess = hasAccess;
    }
}

