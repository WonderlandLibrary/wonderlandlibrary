// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import java.util.Collection;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonArray;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.common.collect.Sets;
import java.util.Set;
import com.google.common.collect.ForwardingSet;

public class JsonSerializableSet extends ForwardingSet implements IJsonSerializable
{
    private final Set underlyingSet;
    private static final String __OBFID = "CL_00001482";
    
    public JsonSerializableSet() {
        this.underlyingSet = Sets.newHashSet();
    }
    
    public void func_152753_a(final JsonElement p_152753_1_) {
        if (p_152753_1_.isJsonArray()) {
            for (final JsonElement var3 : p_152753_1_.getAsJsonArray()) {
                this.add((Object)var3.getAsString());
            }
        }
    }
    
    public JsonElement getSerializableElement() {
        final JsonArray var1 = new JsonArray();
        for (final String var3 : this) {
            var1.add((JsonElement)new JsonPrimitive(var3));
        }
        return (JsonElement)var1;
    }
    
    protected Set delegate() {
        return this.underlyingSet;
    }
}
