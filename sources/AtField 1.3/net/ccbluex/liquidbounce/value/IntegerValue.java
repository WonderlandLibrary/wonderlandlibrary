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

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class IntegerValue
extends Value {
    private final int maximum;
    private final int minimum;

    public IntegerValue(String string, int n, int n2, int n3) {
        super(string, n);
        this.minimum = n2;
        this.maximum = n3;
    }

    @Override
    public JsonElement toJson() {
        return (JsonElement)this.toJson();
    }

    public final int getMinimum() {
        return this.minimum;
    }

    public IntegerValue(String string, int n, int n2, int n3, int n4, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n4 & 4) != 0) {
            n2 = 0;
        }
        if ((n4 & 8) != 0) {
            n3 = Integer.MAX_VALUE;
        }
        this(string, n, n2, n3);
    }

    public JsonPrimitive toJson() {
        return new JsonPrimitive((Number)this.getValue());
    }

    @Override
    public void fromJson(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            this.setValue(jsonElement.getAsInt());
        }
    }

    public final void set(Number number) {
        this.set((Object)number.intValue());
    }

    public final int getMaximum() {
        return this.maximum;
    }
}

