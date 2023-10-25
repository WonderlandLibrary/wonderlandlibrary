/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  sun.reflect.FieldAccessor
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
            catch (NoSuchFieldException e) {
                throw new UnsupportedOperationException(e);
            }
            address.setAccessible(true);
            try {
                Method m = Field.class.getDeclaredMethod("acquireFieldAccessor", Boolean.TYPE);
                m.setAccessible(true);
                this.addressAccessor = (FieldAccessor)m.invoke(address, true);
            }
            catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }

        public long getAddress(Buffer buffer) {
            return this.addressAccessor.getLong((Object)buffer);
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
            catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }

        public long getAddress(Buffer buffer) {
            return this.unsafe.getLong(buffer, this.address);
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
                catch (IllegalAccessException e) {
                    break;
                }
            }
            throw new UnsupportedOperationException();
        }
    }
}

