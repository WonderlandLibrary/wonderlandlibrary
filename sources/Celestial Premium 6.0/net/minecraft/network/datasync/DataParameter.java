/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.datasync;

import net.minecraft.network.datasync.DataSerializer;

public class DataParameter<T> {
    private final int id;
    private final DataSerializer<T> serializer;

    public DataParameter(int idIn, DataSerializer<T> serializerIn) {
        this.id = idIn;
        this.serializer = serializerIn;
    }

    public int getId() {
        return this.id;
    }

    public DataSerializer<T> getSerializer() {
        return this.serializer;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            DataParameter dataparameter = (DataParameter)p_equals_1_;
            return this.id == dataparameter.id;
        }
        return false;
    }

    public int hashCode() {
        return this.id;
    }
}

