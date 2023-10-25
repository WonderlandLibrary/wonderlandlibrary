/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viabackwards.api.data;

import com.viaversion.viabackwards.api.data.VBMappingDataLoader;
import com.viaversion.viaversion.api.data.IntArrayMappings;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.util.Arrays;

public class VBMappings
extends IntArrayMappings {
    public VBMappings(int size, JsonObject oldMapping, JsonObject newMapping, JsonObject diffMapping, boolean warnOnMissing) {
        super(VBMappings.create(size, oldMapping, newMapping, diffMapping, warnOnMissing));
    }

    public VBMappings(JsonObject oldMapping, JsonObject newMapping, JsonObject diffMapping, boolean warnOnMissing) {
        super(VBMappings.create(oldMapping.entrySet().size(), oldMapping, newMapping, diffMapping, warnOnMissing));
    }

    public VBMappings(JsonObject oldMapping, JsonObject newMapping, boolean warnOnMissing) {
        this(oldMapping, newMapping, null, warnOnMissing);
    }

    public VBMappings(JsonArray oldMapping, JsonArray newMapping, JsonObject diffMapping, boolean warnOnMissing) {
        super(oldMapping.size(), oldMapping, newMapping, diffMapping, warnOnMissing);
    }

    private static int[] create(int size, JsonObject oldMapping, JsonObject newMapping, JsonObject diffMapping, boolean warnOnMissing) {
        int[] oldToNew = new int[size];
        Arrays.fill(oldToNew, -1);
        VBMappingDataLoader.mapIdentifiers(oldToNew, oldMapping, newMapping, diffMapping, warnOnMissing);
        return oldToNew;
    }
}

