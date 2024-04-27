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

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class BoolValue
extends Value {
    private float tempX = 35.0f;

    public final void setTempX(float f) {
        this.tempX = f;
    }

    public BoolValue(String string, boolean bl) {
        super(string, bl);
    }

    public final float getTempX() {
        return this.tempX;
    }

    @Override
    public void fromJson(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            this.setValue(jsonElement.getAsBoolean() || StringsKt.equals((String)jsonElement.getAsString(), (String)"true", (boolean)true));
        }
    }

    public JsonPrimitive toJson() {
        return new JsonPrimitive((Boolean)this.getValue());
    }

    @Override
    public JsonElement toJson() {
        return (JsonElement)this.toJson();
    }
}

