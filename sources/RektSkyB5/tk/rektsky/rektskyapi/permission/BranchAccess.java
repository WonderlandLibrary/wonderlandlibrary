/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi.permission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BranchAccess {
    @Expose
    @SerializedName(value="beta")
    public Boolean betaAccess = false;
    @Expose
    @SerializedName(value="snapshot")
    public Boolean snapshotAccess = false;
    @Expose
    @SerializedName(value="stable")
    public Boolean stableAccess = true;

    public BranchAccess() {
    }

    public BranchAccess(boolean stableAccess, boolean betaAccess, boolean snapshotAccess) {
        this.stableAccess = stableAccess;
        this.betaAccess = betaAccess;
        this.snapshotAccess = snapshotAccess;
    }
}

