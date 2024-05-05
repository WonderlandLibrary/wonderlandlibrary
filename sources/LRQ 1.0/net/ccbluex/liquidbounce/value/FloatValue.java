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

public class FloatValue
extends Value<Float> {
    private final float minimum;
    private final float maximum;

    @Override
    public final void set(Number newValue) {
        this.set(Float.valueOf(newValue.floatValue()));
    }

    public JsonPrimitive toJson() {
        return new JsonPrimitive((Number)this.getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            this.setValue(Float.valueOf(element.getAsFloat()));
        }
    }

    public final float getMinimum() {
        return this.minimum;
    }

    public final float getMaximum() {
        return this.maximum;
    }

    public FloatValue(String name, float value, float minimum, float maximum) {
        super(name, Float.valueOf(value));
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public /* synthetic */ FloatValue(String string, float f, float f2, float f3, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 4) != 0) {
            f2 = 0.0f;
        }
        if ((n & 8) != 0) {
            f3 = FloatCompanionObject.INSTANCE.getMAX_VALUE();
        }
        this(string, f, f2, f3);
    }
}

