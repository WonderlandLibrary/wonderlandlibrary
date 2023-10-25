/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.utils;

import com.google.gson.annotations.SerializedName;

public class MyChunkPos {
    @SerializedName(value="x")
    public int x;
    @SerializedName(value="z")
    public int z;

    public String toString() {
        return this.x + ", " + this.z;
    }
}

