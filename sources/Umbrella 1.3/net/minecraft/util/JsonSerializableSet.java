/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ForwardingSet
 *  com.google.common.collect.Sets
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonPrimitive
 */
package net.minecraft.util;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.util.IJsonSerializable;

public class JsonSerializableSet
extends ForwardingSet
implements IJsonSerializable {
    private final Set underlyingSet = Sets.newHashSet();
    private static final String __OBFID = "CL_00001482";

    @Override
    public void func_152753_a(JsonElement p_152753_1_) {
        if (p_152753_1_.isJsonArray()) {
            for (JsonElement var3 : p_152753_1_.getAsJsonArray()) {
                this.add(var3.getAsString());
            }
        }
    }

    @Override
    public JsonElement getSerializableElement() {
        JsonArray var1 = new JsonArray();
        Iterator var2 = this.iterator();
        while (var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.add((JsonElement)new JsonPrimitive(var3));
        }
        return var1;
    }

    protected Set delegate() {
        return this.underlyingSet;
    }
}

