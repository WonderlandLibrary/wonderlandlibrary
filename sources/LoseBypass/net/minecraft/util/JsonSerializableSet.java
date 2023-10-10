/*
 * Decompiled with CFR 0.152.
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
extends ForwardingSet<String>
implements IJsonSerializable {
    private final Set<String> underlyingSet = Sets.newHashSet();

    @Override
    public void fromJson(JsonElement json) {
        if (!json.isJsonArray()) return;
        Iterator<JsonElement> iterator = json.getAsJsonArray().iterator();
        while (iterator.hasNext()) {
            JsonElement jsonelement = iterator.next();
            this.add(jsonelement.getAsString());
        }
    }

    @Override
    public JsonElement getSerializableElement() {
        JsonArray jsonarray = new JsonArray();
        Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            String s = (String)iterator.next();
            jsonarray.add(new JsonPrimitive(s));
        }
        return jsonarray;
    }

    @Override
    protected Set<String> delegate() {
        return this.underlyingSet;
    }
}

