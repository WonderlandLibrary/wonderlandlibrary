// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.metadata;

import com.viaversion.viaversion.api.type.Type;

public interface MetaType
{
    Type type();
    
    int typeId();
    
    default MetaType create(final int typeId, final Type<?> type) {
        return new MetaTypeImpl(typeId, type);
    }
    
    public static final class MetaTypeImpl implements MetaType
    {
        private final int typeId;
        private final Type<?> type;
        
        MetaTypeImpl(final int typeId, final Type<?> type) {
            this.typeId = typeId;
            this.type = type;
        }
        
        @Override
        public int typeId() {
            return this.typeId;
        }
        
        @Override
        public Type<?> type() {
            return this.type;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final MetaTypeImpl metaType = (MetaTypeImpl)o;
            return this.typeId == metaType.typeId && this.type.equals(metaType.type);
        }
        
        @Override
        public int hashCode() {
            int result = this.typeId;
            result = 31 * result + this.type.hashCode();
            return result;
        }
    }
}
