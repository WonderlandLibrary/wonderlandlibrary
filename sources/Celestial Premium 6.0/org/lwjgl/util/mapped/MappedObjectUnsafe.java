/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.mapped;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import sun.misc.Unsafe;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
final class MappedObjectUnsafe {
    static final Unsafe INSTANCE = MappedObjectUnsafe.getUnsafeInstance();
    private static final long BUFFER_ADDRESS_OFFSET = MappedObjectUnsafe.getObjectFieldOffset(ByteBuffer.class, "address");
    private static final long BUFFER_CAPACITY_OFFSET = MappedObjectUnsafe.getObjectFieldOffset(ByteBuffer.class, "capacity");
    private static final ByteBuffer global = ByteBuffer.allocateDirect(4096);

    MappedObjectUnsafe() {
    }

    static ByteBuffer newBuffer(long address, int capacity) {
        if (address <= 0L || capacity < 0) {
            throw new IllegalStateException("you almost crashed the jvm");
        }
        ByteBuffer buffer = global.duplicate().order(ByteOrder.nativeOrder());
        INSTANCE.putLong(buffer, BUFFER_ADDRESS_OFFSET, address);
        INSTANCE.putInt(buffer, BUFFER_CAPACITY_OFFSET, capacity);
        buffer.position(0);
        buffer.limit(capacity);
        return buffer;
    }

    private static long getObjectFieldOffset(Class<?> type, String fieldName) {
        while (type != null) {
            try {
                return INSTANCE.objectFieldOffset(type.getDeclaredField(fieldName));
            }
            catch (Throwable t) {
                type = type.getSuperclass();
            }
        }
        throw new UnsupportedOperationException();
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

