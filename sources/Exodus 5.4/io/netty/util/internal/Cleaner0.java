/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.internal.PlatformDependent0;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import sun.misc.Cleaner;

final class Cleaner0 {
    private static final long CLEANER_FIELD_OFFSET;
    private static final InternalLogger logger;

    static void freeDirectBuffer(ByteBuffer byteBuffer) {
        if (CLEANER_FIELD_OFFSET == -1L || !byteBuffer.isDirect()) {
            return;
        }
        try {
            Cleaner cleaner = (Cleaner)PlatformDependent0.getObject(byteBuffer, CLEANER_FIELD_OFFSET);
            if (cleaner != null) {
                cleaner.clean();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    static {
        logger = InternalLoggerFactory.getInstance(Cleaner0.class);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1);
        long l = -1L;
        if (PlatformDependent0.hasUnsafe()) {
            try {
                Field field = byteBuffer.getClass().getDeclaredField("cleaner");
                field.setAccessible(true);
                Cleaner cleaner = (Cleaner)field.get(byteBuffer);
                cleaner.clean();
                l = PlatformDependent0.objectFieldOffset(field);
            }
            catch (Throwable throwable) {
                l = -1L;
            }
        }
        logger.debug("java.nio.ByteBuffer.cleaner(): {}", (Object)(l != -1L ? "available" : "unavailable"));
        CLEANER_FIELD_OFFSET = l;
        Cleaner0.freeDirectBuffer(byteBuffer);
    }

    private Cleaner0() {
    }
}

