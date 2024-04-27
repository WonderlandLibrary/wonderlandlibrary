/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonPrimitive
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.FloatCompanionObject
 */
package net.ccbluex.liquidbounce.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FloatCompanionObject;
import net.ccbluex.liquidbounce.value.Value;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class FloatValue
extends Value {
    private final float maximum;
    private final float minimum;

    public FloatValue(String string, float f, float f2, float f3, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 4) != 0) {
            f2 = 0.0f;
        }
        if ((n & 8) != 0) {
            f3 = FloatCompanionObject.INSTANCE.getMAX_VALUE();
        }
        this(string, f, f2, f3);
    }

    public JsonPrimitive toJson() {
        return new JsonPrimitive((Number)this.getValue());
    }

    @Override
    public void fromJson(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            this.setValue(Float.valueOf(jsonElement.getAsFloat()));
        }
    }

    public final void set(Number number) {
        this.set((Object)Float.valueOf(number.floatValue()));
    }

    public FloatValue(String string, float f, float f2, float f3) {
        super(string, Float.valueOf(f));
        this.minimum = f2;
        this.maximum = f3;
    }

    @Override
    public JsonElement toJson() {
        return (JsonElement)this.toJson();
    }

    public final float getMinimum() {
        return this.minimum;
    }

    public final float getMaximum() {
        return this.maximum;
    }
}

