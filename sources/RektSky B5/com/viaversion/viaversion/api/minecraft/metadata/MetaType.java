/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.api.minecraft.metadata;

import com.viaversion.viaversion.api.type.Type;

public interface MetaType {
    public Type type();

    public int typeId();

    public static MetaType create(int typeId, Type<?> type) {
        return new MetaTypeImpl(typeId, type);
    }

    public static final class MetaTypeImpl
    implements MetaType {
        private final int typeId;
        private final Type<?> type;

        MetaTypeImpl(int typeId, Type<?> type) {
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

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            MetaTypeImpl metaType = (MetaTypeImpl)o2;
            if (this.typeId != metaType.typeId) {
                return false;
            }
            return this.type.equals(metaType.type);
        }

        public int hashCode() {
            int result = this.typeId;
            result = 31 * result + this.type.hashCode();
            return result;
        }
    }
}

