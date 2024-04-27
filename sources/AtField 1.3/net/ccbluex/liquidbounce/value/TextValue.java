/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonPrimitive
 */
package net.ccbluex.liquidbounce.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.ccbluex.liquidbounce.value.Value;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class TextValue
extends Value {
    @Override
    public JsonElement toJson() {
        return (JsonElement)this.toJson();
    }

    @Override
    public void fromJson(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            this.setValue(jsonElement.getAsString());
        }
    }

    public TextValue(String string, String string2) {
        super(string, string2);
    }

    public JsonPrimitive toJson() {
        return new JsonPrimitive((String)this.getValue());
    }
}

