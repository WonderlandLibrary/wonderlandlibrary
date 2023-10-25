/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.AbstractByteBufAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import io.netty.util.CharsetUtil;
import io.netty.util.Recycler;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Locale;

public final class ByteBufUtil {
    private static final InternalLogger logger;
    static final ByteBufAllocator DEFAULT_ALLOCATOR;
    private static final char[] HEXDUMP_TABLE;
    private static final int THREAD_LOCAL_BUFFER_SIZE;

    private ByteBufUtil() {
    }

    static ByteBuf encodeString0(ByteBufAllocator byteBufAllocator, boolean bl, CharBuffer charBuffer, Charset charset) {
        ByteBuf byteBuf;
        block4: {
            CharsetEncoder charsetEncoder = CharsetUtil.getEncoder(charset);
            int n = (int)((double)charBuffer.remaining() * (double)charsetEncoder.maxBytesPerChar());
            boolean bl2 = true;
            ByteBuf byteBuf2 = bl ? byteBufAllocator.heapBuffer(n) : byteBufAllocator.buffer(n);
            try {
                ByteBuffer byteBuffer = byteBuf2.internalNioBuffer(0, n);
                int n2 = byteBuffer.position();
                CoderResult coderResult = charsetEncoder.encode(charBuffer, byteBuffer, true);
                if (!coderResult.isUnderflow()) {
                    coderResult.throwException();
                }
                if (!(coderResult = charsetEncoder.flush(byteBuffer)).isUnderflow()) {
                    coderResult.throwException();
                }
                byteBuf2.writerIndex(byteBuf2.writerIndex() + byteBuffer.position() - n2);
                bl2 = false;
                byteBuf = byteBuf2;
                if (!bl2) break block4;
            }
            catch (CharacterCodingException characterCodingException) {
                throw new IllegalStateException(characterCodingException);
            }
            byteBuf2.release();
        }
        return byteBuf;
    }

    public static int indexOf(ByteBuf byteBuf, int n, int n2, byte by) {
        if (n <= n2) {
            return ByteBufUtil.firstIndexOf(byteBuf, n, n2, by);
        }
        return ByteBufUtil.lastIndexOf(byteBuf, n, n2, by);
    }

    static {
        AbstractByteBufAllocator abstractByteBufAllocator;
        logger = InternalLoggerFactory.getInstance(ByteBufUtil.class);
        HEXDUMP_TABLE = new char[1024];
        char[] cArray = "0123456789abcdef".toCharArray();
        for (int i = 0; i < 256; ++i) {
            ByteBufUtil.HEXDUMP_TABLE[i << 1] = cArray[i >>> 4 & 0xF];
            ByteBufUtil.HEXDUMP_TABLE[(i << 1) + 1] = cArray[i & 0xF];
        }
        String string = SystemPropertyUtil.get("io.netty.allocator.type", "unpooled").toLowerCase(Locale.US).trim();
        if ("unpooled".equals(string)) {
            abstractByteBufAllocator = UnpooledByteBufAllocator.DEFAULT;
            logger.debug("-Dio.netty.allocator.type: {}", (Object)string);
        } else if ("pooled".equals(string)) {
            abstractByteBufAllocator = PooledByteBufAllocator.DEFAULT;
            logger.debug("-Dio.netty.allocator.type: {}", (Object)string);
        } else {
            abstractByteBufAllocator = UnpooledByteBufAllocator.DEFAULT;
            logger.debug("-Dio.netty.allocator.type: unpooled (unknown: {})", (Object)string);
        }
        DEFAULT_ALLOCATOR = abstractByteBufAllocator;
        THREAD_LOCAL_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.threadLocalDirectBufferSize", 65536);
        logger.debug("-Dio.netty.threadLocalDirectBufferSize: {}", (Object)THREAD_LOCAL_BUFFER_SIZE);
    }

    public static int hashCode(ByteBuf byteBuf) {
        int n;
        int n2 = byteBuf.readableBytes();
        int n3 = n2 >>> 2;
        int n4 = n2 & 3;
        int n5 = 1;
        int n6 = byteBuf.readerIndex();
        if (byteBuf.order() == ByteOrder.BIG_ENDIAN) {
            for (n = n3; n > 0; --n) {
                n5 = 31 * n5 + byteBuf.getInt(n6);
                n6 += 4;
            }
        } else {
            for (n = n3; n > 0; --n) {
                n5 = 31 * n5 + ByteBufUtil.swapInt(byteBuf.getInt(n6));
                n6 += 4;
            }
        }
        for (n = n4; n > 0; --n) {
            n5 = 31 * n5 + byteBuf.getByte(n6++);
        }
        if (n5 == 0) {
            n5 = 1;
        }
        return n5;
    }

    public static ByteBuf encodeString(ByteBufAllocator byteBufAllocator, CharBuffer charBuffer, Charset charset) {
        return ByteBufUtil.encodeString0(byteBufAllocator, false, charBuffer, charset);
    }

    public static int compare(ByteBuf byteBuf, ByteBuf byteBuf2) {
        long l;
        long l2;
        int n;
        int n2 = byteBuf.readableBytes();
        int n3 = byteBuf2.readableBytes();
        int n4 = Math.min(n2, n3);
        int n5 = n4 >>> 2;
        int n6 = n4 & 3;
        int n7 = byteBuf.readerIndex();
        int n8 = byteBuf2.readerIndex();
        if (byteBuf.order() == byteBuf2.order()) {
            for (n = n5; n > 0; --n) {
                l2 = byteBuf.getUnsignedInt(n7);
                if (l2 > (l = byteBuf2.getUnsignedInt(n8))) {
                    return 1;
                }
                if (l2 < l) {
                    return -1;
                }
                n7 += 4;
                n8 += 4;
            }
        } else {
            for (n = n5; n > 0; --n) {
                l2 = byteBuf.getUnsignedInt(n7);
                if (l2 > (l = (long)ByteBufUtil.swapInt(byteBuf2.getInt(n8)) & 0xFFFFFFFFL)) {
                    return 1;
                }
                if (l2 < l) {
                    return -1;
                }
                n7 += 4;
                n8 += 4;
            }
        }
        for (n = n6; n > 0; --n) {
            short s;
            short s2 = byteBuf.getUnsignedByte(n7);
            if (s2 > (s = byteBuf2.getUnsignedByte(n8))) {
                return 1;
            }
            if (s2 < s) {
                return -1;
            }
            ++n7;
            ++n8;
        }
        return n2 - n3;
    }

    public static String hexDump(ByteBuf byteBuf, int n, int n2) {
        if (n2 < 0) {
            throw new IllegalArgumentException("length: " + n2);
        }
        if (n2 == 0) {
            return "";
        }
        int n3 = n + n2;
        char[] cArray = new char[n2 << 1];
        int n4 = n;
        int n5 = 0;
        while (n4 < n3) {
            System.arraycopy(HEXDUMP_TABLE, byteBuf.getUnsignedByte(n4) << 1, cArray, n5, 2);
            ++n4;
            n5 += 2;
        }
        return new String(cArray);
    }

    public static ByteBuf threadLocalDirectBuffer() {
        if (THREAD_LOCAL_BUFFER_SIZE <= 0) {
            return null;
        }
        if (PlatformDependent.hasUnsafe()) {
            return ThreadLocalUnsafeDirectByteBuf.newInstance();
        }
        return ThreadLocalDirectByteBuf.newInstance();
    }

    public static boolean equals(ByteBuf byteBuf, ByteBuf byteBuf2) {
        int n;
        int n2 = byteBuf.readableBytes();
        if (n2 != byteBuf2.readableBytes()) {
            return false;
        }
        int n3 = n2 >>> 3;
        int n4 = n2 & 7;
        int n5 = byteBuf.readerIndex();
        int n6 = byteBuf2.readerIndex();
        if (byteBuf.order() == byteBuf2.order()) {
            for (n = n3; n > 0; --n) {
                if (byteBuf.getLong(n5) != byteBuf2.getLong(n6)) {
                    return false;
                }
                n5 += 8;
                n6 += 8;
            }
        } else {
            for (n = n3; n > 0; --n) {
                if (byteBuf.getLong(n5) != ByteBufUtil.swapLong(byteBuf2.getLong(n6))) {
                    return false;
                }
                n5 += 8;
                n6 += 8;
            }
        }
        for (n = n4; n > 0; --n) {
            if (byteBuf.getByte(n5) != byteBuf2.getByte(n6)) {
                return false;
            }
            ++n5;
            ++n6;
        }
        return true;
    }

    static String decodeString(ByteBuffer byteBuffer, Charset charset) {
        CharsetDecoder charsetDecoder = CharsetUtil.getDecoder(charset);
        CharBuffer charBuffer = CharBuffer.allocate((int)((double)byteBuffer.remaining() * (double)charsetDecoder.maxCharsPerByte()));
        try {
            CoderResult coderResult = charsetDecoder.decode(byteBuffer, charBuffer, true);
            if (!coderResult.isUnderflow()) {
                coderResult.throwException();
            }
            if (!(coderResult = charsetDecoder.flush(charBuffer)).isUnderflow()) {
                coderResult.throwException();
            }
        }
        catch (CharacterCodingException characterCodingException) {
            throw new IllegalStateException(characterCodingException);
        }
        return charBuffer.flip().toString();
    }

    public static short swapShort(short s) {
        return Short.reverseBytes(s);
    }

    public static ByteBuf readBytes(ByteBufAllocator byteBufAllocator, ByteBuf byteBuf, int n) {
        boolean bl = true;
        ByteBuf byteBuf2 = byteBufAllocator.buffer(n);
        byteBuf.readBytes(byteBuf2);
        bl = false;
        ByteBuf byteBuf3 = byteBuf2;
        if (bl) {
            byteBuf2.release();
        }
        return byteBuf3;
    }

    public static int swapMedium(int n) {
        int n2 = n << 16 & 0xFF0000 | n & 0xFF00 | n >>> 16 & 0xFF;
        if ((n2 & 0x800000) != 0) {
            n2 |= 0xFF000000;
        }
        return n2;
    }

    public static long swapLong(long l) {
        return Long.reverseBytes(l);
    }

    private static int lastIndexOf(ByteBuf byteBuf, int n, int n2, byte by) {
        if ((n = Math.min(n, byteBuf.capacity())) < 0 || byteBuf.capacity() == 0) {
            return -1;
        }
        for (int i = n - 1; i >= n2; --i) {
            if (byteBuf.getByte(i) != by) continue;
            return i;
        }
        return -1;
    }

    private static int firstIndexOf(ByteBuf byteBuf, int n, int n2, byte by) {
        if ((n = Math.max(n, 0)) >= n2 || byteBuf.capacity() == 0) {
            return -1;
        }
        for (int i = n; i < n2; ++i) {
            if (byteBuf.getByte(i) != by) continue;
            return i;
        }
        return -1;
    }

    public static int swapInt(int n) {
        return Integer.reverseBytes(n);
    }

    public static String hexDump(ByteBuf byteBuf) {
        return ByteBufUtil.hexDump(byteBuf, byteBuf.readerIndex(), byteBuf.readableBytes());
    }

    static final class ThreadLocalDirectByteBuf
    extends UnpooledDirectByteBuf {
        private static final Recycler<ThreadLocalDirectByteBuf> RECYCLER = new Recycler<ThreadLocalDirectByteBuf>(){

            @Override
            protected ThreadLocalDirectByteBuf newObject(Recycler.Handle handle) {
                return new ThreadLocalDirectByteBuf(handle);
            }
        };
        private final Recycler.Handle handle;

        static ThreadLocalDirectByteBuf newInstance() {
            ThreadLocalDirectByteBuf threadLocalDirectByteBuf = RECYCLER.get();
            threadLocalDirectByteBuf.setRefCnt(1);
            return threadLocalDirectByteBuf;
        }

        private ThreadLocalDirectByteBuf(Recycler.Handle handle) {
            super((ByteBufAllocator)UnpooledByteBufAllocator.DEFAULT, 256, Integer.MAX_VALUE);
            this.handle = handle;
        }

        @Override
        protected void deallocate() {
            if (this.capacity() > THREAD_LOCAL_BUFFER_SIZE) {
                super.deallocate();
            } else {
                this.clear();
                RECYCLER.recycle(this, this.handle);
            }
        }
    }

    static final class ThreadLocalUnsafeDirectByteBuf
    extends UnpooledUnsafeDirectByteBuf {
        private final Recycler.Handle handle;
        private static final Recycler<ThreadLocalUnsafeDirectByteBuf> RECYCLER = new Recycler<ThreadLocalUnsafeDirectByteBuf>(){

            @Override
            protected ThreadLocalUnsafeDirectByteBuf newObject(Recycler.Handle handle) {
                return new ThreadLocalUnsafeDirectByteBuf(handle);
            }
        };

        @Override
        protected void deallocate() {
            if (this.capacity() > THREAD_LOCAL_BUFFER_SIZE) {
                super.deallocate();
            } else {
                this.clear();
                RECYCLER.recycle(this, this.handle);
            }
        }

        static ThreadLocalUnsafeDirectByteBuf newInstance() {
            ThreadLocalUnsafeDirectByteBuf threadLocalUnsafeDirectByteBuf = RECYCLER.get();
            threadLocalUnsafeDirectByteBuf.setRefCnt(1);
            return threadLocalUnsafeDirectByteBuf;
        }

        private ThreadLocalUnsafeDirectByteBuf(Recycler.Handle handle) {
            super((ByteBufAllocator)UnpooledByteBufAllocator.DEFAULT, 256, Integer.MAX_VALUE);
            this.handle = handle;
        }
    }
}

