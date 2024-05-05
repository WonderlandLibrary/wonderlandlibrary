/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonPrimitive
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.value.Value;

public class BoolValue
extends Value<Boolean> {
    public JsonPrimitive toJson() {
        return new JsonPrimitive((Boolean)this.getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            this.setValue(element.getAsBoolean() || StringsKt.equals((String)element.getAsString(), (String)"true", (boolean)true));
        }
    }

    public BoolValue(String name, boolean value) {
        super(name, value);
    }
}

