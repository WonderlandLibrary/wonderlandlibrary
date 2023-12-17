// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.datasync;

public class DataParameter<T>
{
    private final int id;
    private final DataSerializer<T> serializer;
    
    public DataParameter(final int idIn, final DataSerializer<T> serializerIn) {
        this.id = idIn;
        this.serializer = serializerIn;
    }
    
    public int getId() {
        return this.id;
    }
    
    public DataSerializer<T> getSerializer() {
        return this.serializer;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            final DataParameter<?> dataparameter = (DataParameter<?>)p_equals_1_;
            return this.id == dataparameter.id;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.id;
    }
}
