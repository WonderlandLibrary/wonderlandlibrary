/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.util.IJsonSerializable;

public class TupleIntJsonSerializable {
    private IJsonSerializable jsonSerializableValue;
    private int integerValue;

    public void setJsonSerializableValue(IJsonSerializable iJsonSerializable) {
        this.jsonSerializableValue = iJsonSerializable;
    }

    public void setIntegerValue(int n) {
        this.integerValue = n;
    }

    public int getIntegerValue() {
        return this.integerValue;
    }

    public <T extends IJsonSerializable> T getJsonSerializableValue() {
        return (T)this.jsonSerializableValue;
    }
}

