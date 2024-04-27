/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonPrimitive
 *  kotlin.jvm.internal.DefaultConstructorMarker
 */
package net.ccbluex.liquidbounce.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.value.Value;

public class IntegerValue
extends Value<Integer> {
    private final int minimum;
    private final int maximum;

    @Override
    public final void set(Number newValue) {
        this.set(newValue.intValue());
    }

    public JsonPrimitive toJson() {
        return new JsonPrimitive((Number)this.getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            this.setValue(element.getAsInt());
        }
    }

    public final int getMinimum() {
        return this.minimum;
    }

    public final int getMaximum() {
        return this.maximum;
    }

    public IntegerValue(String name, int value, int minimum, int maximum) {
        super(name, value);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public /* synthetic */ IntegerValue(String string, int n, int n2, int n3, int n4, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n4 & 4) != 0) {
            n2 = 0;
        }
        if ((n4 & 8) != 0) {
            n3 = Integer.MAX_VALUE;
        }
        this(string, n, n2, n3);
    }
}

