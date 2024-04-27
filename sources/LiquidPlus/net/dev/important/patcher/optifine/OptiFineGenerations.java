/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package net.dev.important.patcher.optifine;

import com.google.gson.annotations.SerializedName;
import java.util.HashSet;
import java.util.Set;

public class OptiFineGenerations {
    @SerializedName(value="iGeneration")
    private final Set<String> iGeneration = new HashSet<String>(1);
    @SerializedName(value="lGeneration")
    private final Set<String> lGeneration = new HashSet<String>(2);
    @SerializedName(value="mGeneration")
    private final Set<String> mGeneration = new HashSet<String>();
    @SerializedName(value="futureGeneration")
    private final Set<String> futureGeneration = new HashSet<String>();

    public Set<String> getIGeneration() {
        return this.iGeneration;
    }

    public Set<String> getLGeneration() {
        return this.lGeneration;
    }

    public Set<String> getMGeneration() {
        return this.mGeneration;
    }

    public Set<String> getFutureGeneration() {
        return this.futureGeneration;
    }
}

