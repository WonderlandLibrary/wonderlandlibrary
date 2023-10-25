/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.ReadOnlyByteBuf;
import io.netty.buffer.ReadOnlyByteBufferBuf;
import io.netty.buffer.ReadOnlyUnsafeDirectByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import io.netty.buffer.UnreleasableByteBuf;
import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class Unpooled {
    public static final ByteOrder LITTLE_ENDIAN;
    public static final ByteBuf EMPTY_BUFFER;
    public static final ByteOrder BIG_ENDIAN;
    private static final ByteBufAllocator ALLOC;

    public static ByteBuf copyDouble(double d) {
        ByteBuf byteBuf = Unpooled.buffer(8);
        byteBuf.writeDouble(d);
        return byteBuf;
    }

    public static ByteBuf copiedBuffer(char[] cArray, int n, int n2, Charset charset) {
        if (cArray == null) {
            throw new NullPointerException("array");
        }
        if (n2 == 0) {
            return EMPTY_BUFFER;
        }
        return Unpooled.copiedBuffer(CharBuffer.wrap(cArray, n, n2), charset);
    }

    public static CompositeByteBuf compositeBuffer(int n) {
        return new CompositeByteBuf(ALLOC, false, n);
    }

    public static ByteBuf copyShort(int n) {
        ByteBuf byteBuf = Unpooled.buffer(2);
        byteBuf.writeShort(n);
        return byteBuf;
    }

    public static ByteBuf copyLong(long ... lArray) {
        if (lArray == null || lArray.length == 0) {
            return EMPTY_BUFFER;
        }
        ByteBuf byteBuf = Unpooled.buffer(lArray.length * 8);
        for (long l : lArray) {
            byteBuf.writeLong(l);
        }
        return byteBuf;
    }

    public static ByteBuf copyBoolean(boolean ... blArray) {
        if (blArray == null || blArray.length == 0) {
            return EMPTY_BUFFER;
        }
        ByteBuf byteBuf = Unpooled.buffer(blArray.length);
        for (boolean bl : blArray) {
            byteBuf.writeBoolean(bl);
        }
        return byteBuf;
    }

    public static ByteBuf copyShort(int ... nArray) {
        if (nArray == null || nArray.length == 0) {
            return EMPTY_BUFFER;
        }
        ByteBuf byteBuf = Unpooled.buffer(nArray.length * 2);
        for (int n : nArray) {
            byteBuf.writeShort(n);
        }
        return byteBuf;
    }

    public static CompositeByteBuf compositeBuffer() {
        return Unpooled.compositeBuffer(16);
    }

    private static ByteBuf copiedBuffer(CharBuffer charBuffer, Charset charset) {
        return ByteBufUtil.encodeString0(ALLOC, true, charBuffer, charset);
    }

    public static ByteBuf copyInt(int ... nArray) {
        if (nArray == null || nArray.length == 0) {
            return EMPTY_BUFFER;
        }
        ByteBuf byteBuf = Unpooled.buffer(nArray.length * 4);
        for (int n : nArray) {
            byteBuf.writeInt(n);
        }
        return byteBuf;
    }

    public static ByteBuf copiedBuffer(byte[] byArray, int n, int n2) {
        if (n2 == 0) {
            return EMPTY_BUFFER;
        }
        byte[] byArray2 = new byte[n2];
        System.arraycopy(byArray, n, byArray2, 0, n2);
        return Unpooled.wrappedBuffer(byArray2);
    }

    public static ByteBuf copyDouble(double ... dArray) {
        if (dArray == null || dArray.length == 0) {
            return EMPTY_BUFFER;
        }
        ByteBuf byteBuf = Unpooled.buffer(dArray.length * 8);
        for (double d : dArray) {
            byteBuf.writeDouble(d);
        }
        return byteBuf;
    }

    public static ByteBuf wrappedBuffer(int n, ByteBuf ... byteBufArray) {
        switch (byteBufArray.length) {
            case 0: {
                break;
            }
            case 1: {
                if (!byteBufArray[0].isReadable()) break;
                return Unpooled.wrappedBuffer(byteBufArray[0].order(BIG_ENDIAN));
            }
            default: {
                for (ByteBuf byteBuf : byteBufArray) {
                    if (!byteBuf.isReadable()) continue;
                    return new CompositeByteBuf(ALLOC, false, n, byteBufArray);
                }
            }
        }
        return EMPTY_BUFFER;
    }

    public static ByteBuf copiedBuffer(CharSequence charSequence, int n, int n2, Charset charset) {
        if (charSequence == null) {
            throw new NullPointerException("string");
        }
        if (n2 == 0) {
            return EMPTY_BUFFER;
        }
        if (charSequence instanceof CharBuffer) {
            CharBuffer charBuffer = (CharBuffer)charSequence;
            if (charBuffer.hasArray()) {
                return Unpooled.copiedBuffer(charBuffer.array(), charBuffer.arrayOffset() + charBuffer.position() + n, n2, charset);
            }
            charBuffer = charBuffer.slice();
            charBuffer.limit(n2);
            charBuffer.position(n);
            return Unpooled.copiedBuffer(charBuffer, charset);
        }
        return Unpooled.copiedBuffer(CharBuffer.wrap(charSequence, n, n + n2), charset);
    }

    public static ByteBuf wrappedBuffer(ByteBuffer byteBuffer) {
        if (!byteBuffer.hasRemaining()) {
            return EMPTY_BUFFER;
        }
        if (byteBuffer.hasArray()) {
            return Unpooled.wrappedBuffer(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining()).order(byteBuffer.order());
        }
        if (PlatformDependent.hasUnsafe()) {
            if (byteBuffer.isReadOnly()) {
                if (byteBuffer.isDirect()) {
                    return new ReadOnlyUnsafeDirectByteBuf(ALLOC, byteBuffer);
                }
                return new ReadOnlyByteBufferBuf(ALLOC, byteBuffer);
            }
            return new UnpooledUnsafeDirectByteBuf(ALLOC, byteBuffer, byteBuffer.remaining());
        }
        if (byteBuffer.isReadOnly()) {
            return new ReadOnlyByteBufferBuf(ALLOC, byteBuffer);
        }
        return new UnpooledDirectByteBuf(ALLOC, byteBuffer, byteBuffer.remaining());
    }

    public static ByteBuf copyMedium(int ... nArray) {
        if (nArray == null || nArray.length == 0) {
            return EMPTY_BUFFER;
        }
        ByteBuf byteBuf = Unpooled.buffer(nArray.length * 3);
        for (int n : nArray) {
            byteBuf.writeMedium(n);
        }
        return byteBuf;
    }

    public static ByteBuf buffer() {
        return ALLOC.heapBuffer();
    }

    public static ByteBuf directBuffer(int n, int n2) {
        return ALLOC.directBuffer(n, n2);
    }

    public static ByteBuf wrappedBuffer(ByteBuf byteBuf) {
        if (byteBuf.isReadable()) {
            return byteBuf.slice();
        }
        return EMPTY_BUFFER;
    }

    public static ByteBuf directBuffer(int n) {
        return ALLOC.directBuffer(n);
    }

    public static ByteBuf copyFloat(float f) {
        ByteBuf byteBuf = Unpooled.buffer(4);
        byteBuf.writeFloat(f);
        return byteBuf;
    }

    public static ByteBuf copiedBuffer(CharSequence charSequence, Charset charset) {
        if (charSequence == null) {
            throw new NullPointerException("string");
        }
        if (charSequence instanceof CharBuffer) {
            return Unpooled.copiedBuffer((CharBuffer)charSequence, charset);
        }
        return Unpooled.copiedBuffer(CharBuffer.wrap(charSequence), charset);
    }

    public static ByteBuf copyMedium(int n) {
        ByteBuf byteBuf = Unpooled.buffer(3);
        byteBuf.writeMedium(n);
        return byteBuf;
    }

    public static ByteBuf wrappedBuffer(byte[] byArray) {
        if (byArray.length == 0) {
            return EMPTY_BUFFER;
        }
        return new UnpooledHeapByteBuf(ALLOC, byArray, byArray.length);
    }

    public static ByteBuf wrappedBuffer(ByteBuf ... byteBufArray) {
        return Unpooled.wrappedBuffer(16, byteBufArray);
    }

    static {
        ALLOC = UnpooledByteBufAllocator.DEFAULT;
        BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
        LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
        EMPTY_BUFFER = ALLOC.buffer(0, 0);
    }

    public static ByteBuf buffer(int n) {
        return ALLOC.heapBuffer(n);
    }

    public static ByteBuf copiedBuffer(byte[] ... byArray) {
        switch (byArray.length) {
            case 0: {
                return EMPTY_BUFFER;
            }
            case 1: {
                if (byArray[0].length == 0) {
                    return EMPTY_BUFFER;
                }
                return Unpooled.copiedBuffer(byArray[0]);
            }
        }
        int n = 0;
        for (byte[] byArray2 : byArray) {
            if (Integer.MAX_VALUE - n < byArray2.length) {
                throw new IllegalArgumentException("The total length of the specified arrays is too big.");
            }
            n += byArray2.length;
        }
        if (n == 0) {
            return EMPTY_BUFFER;
        }
        byte[] byArray3 = new byte[n];
        int n2 = 0;
        for (int i = 0; i < byArray.length; ++i) {
            byte[] byArray2;
            byArray2 = byArray[i];
            System.arraycopy(byArray2, 0, byArray3, n2, byArray2.length);
            n2 += byArray2.length;
        }
        return Unpooled.wrappedBuffer(byArray3);
    }

    public static ByteBuf wrappedBuffer(byte[] ... byArray) {
        return Unpooled.wrappedBuffer(16, byArray);
    }

    public static ByteBuf copiedBuffer(ByteBuffer ... byteBufferArray) {
        int n;
        switch (byteBufferArray.length) {
            case 0: {
                return EMPTY_BUFFER;
            }
            case 1: {
                return Unpooled.copiedBuffer(byteBufferArray[0]);
            }
        }
        ByteOrder byteOrder = null;
        int n2 = 0;
        for (ByteBuffer byteBuffer : byteBufferArray) {
            n = byteBuffer.remaining();
            if (n <= 0) continue;
            if (Integer.MAX_VALUE - n2 < n) {
                throw new IllegalArgumentException("The total length of the specified buffers is too big.");
            }
            n2 += n;
            if (byteOrder != null) {
                if (byteOrder.equals(byteBuffer.order())) continue;
                throw new IllegalArgumentException("inconsistent byte order");
            }
            byteOrder = byteBuffer.order();
        }
        if (n2 == 0) {
            return EMPTY_BUFFER;
        }
        byte[] byArray = new byte[n2];
        int n3 = 0;
        for (int i = 0; i < byteBufferArray.length; ++i) {
            ByteBuffer byteBuffer;
            byteBuffer = byteBufferArray[i];
            n = byteBuffer.remaining();
            int n4 = byteBuffer.position();
            byteBuffer.get(byArray, n3, n);
            byteBuffer.position(n4);
            n3 += n;
        }
        return Unpooled.wrappedBuffer(byArray).order(byteOrder);
    }

    public static ByteBuf copiedBuffer(byte[] byArray) {
        if (byArray.length == 0) {
            return EMPTY_BUFFER;
        }
        return Unpooled.wrappedBuffer((byte[])byArray.clone());
    }

    public static ByteBuf unmodifiableBuffer(ByteBuf byteBuf) {
        ByteOrder byteOrder = byteBuf.order();
        if (byteOrder == BIG_ENDIAN) {
            return new ReadOnlyByteBuf(byteBuf);
        }
        return new ReadOnlyByteBuf(byteBuf.order(BIG_ENDIAN)).order(LITTLE_ENDIAN);
    }

    public static ByteBuf wrappedBuffer(int n, ByteBuffer ... byteBufferArray) {
        switch (byteBufferArray.length) {
            case 0: {
                break;
            }
            case 1: {
                if (!byteBufferArray[0].hasRemaining()) break;
                return Unpooled.wrappedBuffer(byteBufferArray[0].order(BIG_ENDIAN));
            }
            default: {
                ArrayList<ByteBuf> arrayList = new ArrayList<ByteBuf>(byteBufferArray.length);
                for (ByteBuffer byteBuffer : byteBufferArray) {
                    if (byteBuffer == null) break;
                    if (byteBuffer.remaining() <= 0) continue;
                    arrayList.add(Unpooled.wrappedBuffer(byteBuffer.order(BIG_ENDIAN)));
                }
                if (arrayList.isEmpty()) break;
                return new CompositeByteBuf(ALLOC, false, n, arrayList);
            }
        }
        return EMPTY_BUFFER;
    }

    public static ByteBuf copyInt(int n) {
        ByteBuf byteBuf = Unpooled.buffer(4);
        byteBuf.writeInt(n);
        return byteBuf;
    }

    public static ByteBuf wrappedBuffer(int n, byte[] ... byArray) {
        switch (byArray.length) {
            case 0: {
                break;
            }
            case 1: {
                if (byArray[0].length == 0) break;
                return Unpooled.wrappedBuffer(byArray[0]);
            }
            default: {
                ArrayList<ByteBuf> arrayList = new ArrayList<ByteBuf>(byArray.length);
                for (byte[] byArray2 : byArray) {
                    if (byArray2 == null) break;
                    if (byArray2.length <= 0) continue;
                    arrayList.add(Unpooled.wrappedBuffer(byArray2));
                }
                if (arrayList.isEmpty()) break;
                return new CompositeByteBuf(ALLOC, false, n, arrayList);
            }
        }
        return EMPTY_BUFFER;
    }

    public static ByteBuf wrappedBuffer(ByteBuffer ... byteBufferArray) {
        return Unpooled.wrappedBuffer(16, byteBufferArray);
    }

    public static ByteBuf buffer(int n, int n2) {
        return ALLOC.heapBuffer(n, n2);
    }

    public static ByteBuf copyShort(short ... sArray) {
        if (sArray == null || sArray.length == 0) {
            return EMPTY_BUFFER;
        }
        ByteBuf byteBuf = Unpooled.buffer(sArray.length * 2);
        for (short s : sArray) {
            byteBuf.writeShort(s);
        }
        return byteBuf;
    }

    public static ByteBuf copiedBuffer(ByteBuf ... byteBufArray) {
        int n;
        switch (byteBufArray.length) {
            case 0: {
                return EMPTY_BUFFER;
            }
            case 1: {
                return Unpooled.copiedBuffer(byteBufArray[0]);
            }
        }
        ByteOrder byteOrder = null;
        int n2 = 0;
        for (ByteBuf byteBuf : byteBufArray) {
            n = byteBuf.readableBytes();
            if (n <= 0) continue;
            if (Integer.MAX_VALUE - n2 < n) {
                throw new IllegalArgumentException("The total length of the specified buffers is too big.");
            }
            n2 += n;
            if (byteOrder != null) {
                if (byteOrder.equals(byteBuf.order())) continue;
                throw new IllegalArgumentException("inconsistent byte order");
            }
            byteOrder = byteBuf.order();
        }
        if (n2 == 0) {
            return EMPTY_BUFFER;
        }
        byte[] byArray = new byte[n2];
        int n3 = 0;
        for (int i = 0; i < byteBufArray.length; ++i) {
            ByteBuf byteBuf;
            byteBuf = byteBufArray[i];
            n = byteBuf.readableBytes();
            byteBuf.getBytes(byteBuf.readerIndex(), byArray, n3, n);
            n3 += n;
        }
        return Unpooled.wrappedBuffer(byArray).order(byteOrder);
    }

    public static ByteBuf directBuffer() {
        return ALLOC.directBuffer();
    }

    public static ByteBuf wrappedBuffer(byte[] byArray, int n, int n2) {
        if (n2 == 0) {
            return EMPTY_BUFFER;
        }
        if (n == 0 && n2 == byArray.length) {
            return Unpooled.wrappedBuffer(byArray);
        }
        return Unpooled.wrappedBuffer(byArray).slice(n, n2);
    }

    public static ByteBuf copyFloat(float ... fArray) {
        if (fArray == null || fArray.length == 0) {
            return EMPTY_BUFFER;
        }
        ByteBuf byteBuf = Unpooled.buffer(fArray.length * 4);
        for (float f : fArray) {
            byteBuf.writeFloat(f);
        }
        return byteBuf;
    }

    public static ByteBuf copyBoolean(boolean bl) {
        ByteBuf byteBuf = Unpooled.buffer(1);
        byteBuf.writeBoolean(bl);
        return byteBuf;
    }

    private Unpooled() {
    }

    public static ByteBuf copiedBuffer(ByteBuf byteBuf) {
        int n = byteBuf.readableBytes();
        if (n > 0) {
            ByteBuf byteBuf2 = Unpooled.buffer(n);
            byteBuf2.writeBytes(byteBuf, byteBuf.readerIndex(), n);
            return byteBuf2;
        }
        return EMPTY_BUFFER;
    }

    public static ByteBuf copyLong(long l) {
        ByteBuf byteBuf = Unpooled.buffer(8);
        byteBuf.writeLong(l);
        return byteBuf;
    }

    public static ByteBuf copiedBuffer(char[] cArray, Charset charset) {
        if (cArray == null) {
            throw new NullPointerException("array");
        }
        return Unpooled.copiedBuffer(cArray, 0, cArray.length, charset);
    }

    public static ByteBuf copiedBuffer(ByteBuffer byteBuffer) {
        int n = byteBuffer.remaining();
        if (n == 0) {
            return EMPTY_BUFFER;
        }
        byte[] byArray = new byte[n];
        int n2 = byteBuffer.position();
        byteBuffer.get(byArray);
        byteBuffer.position(n2);
        return Unpooled.wrappedBuffer(byArray).order(byteBuffer.order());
    }

    public static ByteBuf unreleasableBuffer(ByteBuf byteBuf) {
        return new UnreleasableByteBuf(byteBuf);
    }
}

