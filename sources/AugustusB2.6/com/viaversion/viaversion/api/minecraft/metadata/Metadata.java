// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.metadata;

import java.util.Objects;
import com.google.common.base.Preconditions;

public final class Metadata
{
    private int id;
    private MetaType metaType;
    private Object value;
    
    public Metadata(final int id, final MetaType metaType, final Object value) {
        this.id = id;
        this.metaType = metaType;
        this.value = this.checkValue(metaType, value);
    }
    
    public int id() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public MetaType metaType() {
        return this.metaType;
    }
    
    public void setMetaType(final MetaType metaType) {
        this.checkValue(metaType, this.value);
        this.metaType = metaType;
    }
    
    public <T> T value() {
        return (T)this.value;
    }
    
    public Object getValue() {
        return this.value;
    }
    
    public void setValue(final Object value) {
        this.value = this.checkValue(this.metaType, value);
    }
    
    public void setTypeAndValue(final MetaType metaType, final Object value) {
        this.value = this.checkValue(metaType, value);
        this.metaType = metaType;
    }
    
    private Object checkValue(final MetaType metaType, final Object value) {
        Preconditions.checkNotNull(metaType);
        if (value != null && !metaType.type().getOutputClass().isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException("Metadata value and metaType are incompatible. Type=" + metaType + ", value=" + value + " (" + value.getClass().getSimpleName() + ")");
        }
        return value;
    }
    
    @Deprecated
    public void setMetaTypeUnsafe(final MetaType type) {
        this.metaType = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Metadata metadata = (Metadata)o;
        return this.id == metadata.id && this.metaType == metadata.metaType && Objects.equals(this.value, metadata.value);
    }
    
    @Override
    public int hashCode() {
        int result = this.id;
        result = 31 * result + this.metaType.hashCode();
        result = 31 * result + ((this.value != null) ? this.value.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "Metadata{id=" + this.id + ", metaType=" + this.metaType + ", value=" + this.value + '}';
    }
}
