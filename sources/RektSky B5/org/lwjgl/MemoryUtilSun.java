/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.Buffer;
import org.lwjgl.MemoryUtil;
import sun.misc.Unsafe;
import sun.reflect.FieldAccessor;

final class MemoryUtilSun {
    private MemoryUtilSun() {
    }

    private static class AccessorReflectFast
    implements MemoryUtil.Accessor {
        private final FieldAccessor addressAccessor;

        AccessorReflectFast() {
            Field address;
            try {
                address = MemoryUtil.getAddressField();
            }
            catch (NoSuchFieldException e2) {
                throw new UnsupportedOperationException(e2);
            }
            address.setAccessible(true);
            try {
                Method m2 = Field.class.getDeclaredMethod("acquireFieldAccessor", Boolean.TYPE);
                m2.setAccessible(true);
                this.addressAccessor = (FieldAccessor)m2.invoke(address, true);
            }
            catch (Exception e3) {
                throw new UnsupportedOperationException(e3);
            }
        }

        public long getAddress(Buffer buffer) {
            return this.addressAccessor.getLong(buffer);
        }
    }

    private static class AccessorUnsafe
    implements MemoryUtil.Accessor {
        private final Unsafe unsafe;
        private final long address;

        AccessorUnsafe() {
            try {
                this.unsafe = AccessorUnsafe.getUnsafeInstance();
                this.address = this.unsafe.objectFieldOffset(MemoryUtil.getAddressField());
            }
            catch (Exception e2) {
                throw new UnsupportedOperationException(e2);
            }
        }

        public long getAddress(Buffer buffer) {
            return this.unsafe.getLong((Object)buffer, this.address);
        }

        private static Unsafe getUnsafeInstance() {
            Field[] fields;
            for (Field field : fields = Unsafe.class.getDeclaredFields()) {
                int modifiers;
                if (!field.getType().equals(Unsafe.class) || !Modifier.isStatic(modifiers = field.getModifiers()) || !Modifier.isFinal(modifiers)) continue;
                field.setAccessible(true);
                try {
                    return (Unsafe)field.get(null);
                }
                catch (IllegalAccessException e2) {
                    break;
                }
            }
            throw new UnsupportedOperationException();
        }
    }
}

