/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.ResourceLeak;
import io.netty.util.internal.EmptyArrays;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CompositeByteBuf
extends AbstractReferenceCountedByteBuf {
    private final ResourceLeak leak;
    private boolean freed;
    private final int maxNumComponents;
    private final List<Component> components = new ArrayList<Component>();
    private final boolean direct;
    private static final ByteBuffer FULL_BYTEBUFFER = (ByteBuffer)ByteBuffer.allocate(1).position(1);
    private final ByteBufAllocator alloc;

    public CompositeByteBuf consolidate() {
        this.ensureAccessible();
        int n = this.numComponents();
        if (n <= 1) {
            return this;
        }
        Component component = this.components.get(n - 1);
        int n2 = component.endOffset;
        ByteBuf byteBuf = this.allocBuffer(n2);
        for (int i = 0; i < n; ++i) {
            Component component2 = this.components.get(i);
            ByteBuf byteBuf2 = component2.buf;
            byteBuf.writeBytes(byteBuf2);
            component2.freeIfNecessary();
        }
        this.components.clear();
        this.components.add(new Component(byteBuf));
        this.updateComponentOffsets(0);
        return this;
    }

    @Override
    public CompositeByteBuf writeChar(int n) {
        return (CompositeByteBuf)super.writeChar(n);
    }

    private Component findComponent(int n) {
        this.checkIndex(n);
        int n2 = 0;
        int n3 = this.components.size();
        while (n2 <= n3) {
            int n4 = n2 + n3 >>> 1;
            Component component = this.components.get(n4);
            if (n >= component.endOffset) {
                n2 = n4 + 1;
                continue;
            }
            if (n < component.offset) {
                n3 = n4 - 1;
                continue;
            }
            return component;
        }
        throw new Error("should not reach here");
    }

    @Override
    protected void deallocate() {
        if (this.freed) {
            return;
        }
        this.freed = true;
        int n = this.components.size();
        for (int i = 0; i < n; ++i) {
            this.components.get(i).freeIfNecessary();
        }
        if (this.leak != null) {
            this.leak.close();
        }
    }

    @Override
    protected int _getInt(int n) {
        Component component = this.findComponent(n);
        if (n + 4 <= component.endOffset) {
            return component.buf.getInt(n - component.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (this._getShort(n) & 0xFFFF) << 16 | this._getShort(n + 2) & 0xFFFF;
        }
        return this._getShort(n) & 0xFFFF | (this._getShort(n + 2) & 0xFFFF) << 16;
    }

    @Override
    public boolean isDirect() {
        int n = this.components.size();
        if (n == 0) {
            return false;
        }
        for (int i = 0; i < n; ++i) {
            if (this.components.get((int)i).buf.isDirect()) continue;
            return false;
        }
        return true;
    }

    @Override
    public CompositeByteBuf writeBytes(byte[] byArray) {
        return (CompositeByteBuf)super.writeBytes(byArray);
    }

    @Override
    public CompositeByteBuf getBytes(int n, byte[] byArray) {
        return (CompositeByteBuf)super.getBytes(n, byArray);
    }

    @Override
    public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) throws IOException {
        this.checkIndex(n, n2);
        if (n2 == 0) {
            return scatteringByteChannel.read(FULL_BYTEBUFFER);
        }
        int n3 = this.toComponentIndex(n);
        int n4 = 0;
        do {
            Component component = this.components.get(n3);
            ByteBuf byteBuf = component.buf;
            int n5 = component.offset;
            int n6 = Math.min(n2, byteBuf.capacity() - (n - n5));
            int n7 = byteBuf.setBytes(n - n5, scatteringByteChannel, n6);
            if (n7 == 0) break;
            if (n7 < 0) {
                if (n4 != 0) break;
                return -1;
            }
            if (n7 == n6) {
                n += n6;
                n2 -= n6;
                n4 += n6;
                ++n3;
                continue;
            }
            n += n7;
            n2 -= n7;
            n4 += n7;
        } while (n2 > 0);
        return n4;
    }

    private int addComponents0(int n, Iterable<ByteBuf> iterable) {
        ArrayList<ByteBuf> arrayList;
        if (iterable == null) {
            throw new NullPointerException("buffers");
        }
        if (iterable instanceof ByteBuf) {
            return this.addComponent0(n, (ByteBuf)((Object)iterable));
        }
        if (!(iterable instanceof Collection)) {
            arrayList = new ArrayList<ByteBuf>();
            for (ByteBuf byteBuf : iterable) {
                arrayList.add(byteBuf);
            }
            iterable = arrayList;
        }
        arrayList = (ArrayList<ByteBuf>)iterable;
        return this.addComponents0(n, arrayList.toArray(new ByteBuf[arrayList.size()]));
    }

    @Override
    public ByteBuffer[] nioBuffers(int n, int n2) {
        this.checkIndex(n, n2);
        if (n2 == 0) {
            return EmptyArrays.EMPTY_BYTE_BUFFERS;
        }
        ArrayList<ByteBuffer> arrayList = new ArrayList<ByteBuffer>(this.components.size());
        int n3 = this.toComponentIndex(n);
        while (n2 > 0) {
            Component component = this.components.get(n3);
            ByteBuf byteBuf = component.buf;
            int n4 = component.offset;
            int n5 = Math.min(n2, byteBuf.capacity() - (n - n4));
            switch (byteBuf.nioBufferCount()) {
                case 0: {
                    throw new UnsupportedOperationException();
                }
                case 1: {
                    arrayList.add(byteBuf.nioBuffer(n - n4, n5));
                    break;
                }
                default: {
                    Collections.addAll(arrayList, byteBuf.nioBuffers(n - n4, n5));
                }
            }
            n += n5;
            n2 -= n5;
            ++n3;
        }
        return arrayList.toArray(new ByteBuffer[arrayList.size()]);
    }

    @Override
    protected void _setShort(int n, int n2) {
        Component component = this.findComponent(n);
        if (n + 2 <= component.endOffset) {
            component.buf.setShort(n - component.offset, n2);
        } else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setByte(n, (byte)(n2 >>> 8));
            this._setByte(n + 1, (byte)n2);
        } else {
            this._setByte(n, (byte)n2);
            this._setByte(n + 1, (byte)(n2 >>> 8));
        }
    }

    private void checkComponentIndex(int n) {
        this.ensureAccessible();
        if (n < 0 || n > this.components.size()) {
            throw new IndexOutOfBoundsException(String.format("cIndex: %d (expected: >= 0 && <= numComponents(%d))", n, this.components.size()));
        }
    }

    public CompositeByteBuf addComponents(ByteBuf ... byteBufArray) {
        this.addComponents0(this.components.size(), byteBufArray);
        this.consolidateIfNeeded();
        return this;
    }

    @Override
    public CompositeByteBuf readBytes(byte[] byArray) {
        return (CompositeByteBuf)super.readBytes(byArray);
    }

    @Override
    public CompositeByteBuf writeBytes(ByteBuf byteBuf, int n, int n2) {
        return (CompositeByteBuf)super.writeBytes(byteBuf, n, n2);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return this.nioBuffers(this.readerIndex(), this.readableBytes());
    }

    @Override
    public CompositeByteBuf readBytes(byte[] byArray, int n, int n2) {
        return (CompositeByteBuf)super.readBytes(byArray, n, n2);
    }

    private void checkComponentIndex(int n, int n2) {
        this.ensureAccessible();
        if (n < 0 || n + n2 > this.components.size()) {
            throw new IndexOutOfBoundsException(String.format("cIndex: %d, numComponents: %d (expected: cIndex >= 0 && cIndex + numComponents <= totalNumComponents(%d))", n, n2, this.components.size()));
        }
    }

    public ByteBuf component(int n) {
        return this.internalComponent(n).duplicate();
    }

    @Override
    public CompositeByteBuf getBytes(int n, byte[] byArray, int n2, int n3) {
        this.checkDstIndex(n, n3, n2, byArray.length);
        if (n3 == 0) {
            return this;
        }
        int n4 = this.toComponentIndex(n);
        while (n3 > 0) {
            Component component = this.components.get(n4);
            ByteBuf byteBuf = component.buf;
            int n5 = component.offset;
            int n6 = Math.min(n3, byteBuf.capacity() - (n - n5));
            byteBuf.getBytes(n - n5, byArray, n2, n6);
            n += n6;
            n2 += n6;
            n3 -= n6;
            ++n4;
        }
        return this;
    }

    @Override
    public CompositeByteBuf getBytes(int n, ByteBuf byteBuf) {
        return (CompositeByteBuf)super.getBytes(n, byteBuf);
    }

    @Override
    public CompositeByteBuf setByte(int n, int n2) {
        Component component = this.findComponent(n);
        component.buf.setByte(n - component.offset, n2);
        return this;
    }

    @Override
    public boolean hasArray() {
        if (this.components.size() == 1) {
            return this.components.get((int)0).buf.hasArray();
        }
        return false;
    }

    @Override
    public CompositeByteBuf writeShort(int n) {
        return (CompositeByteBuf)super.writeShort(n);
    }

    public CompositeByteBuf discardReadComponents() {
        this.ensureAccessible();
        int n = this.readerIndex();
        if (n == 0) {
            return this;
        }
        int n2 = this.writerIndex();
        if (n == n2 && n2 == this.capacity()) {
            for (Component component : this.components) {
                component.freeIfNecessary();
            }
            this.components.clear();
            this.setIndex(0, 0);
            this.adjustMarkers(n);
            return this;
        }
        int n3 = this.toComponentIndex(n);
        for (int i = 0; i < n3; ++i) {
            this.components.get(i).freeIfNecessary();
        }
        this.components.subList(0, n3).clear();
        Component component = this.components.get(0);
        int n4 = component.offset;
        this.updateComponentOffsets(0);
        this.setIndex(n - n4, n2 - n4);
        this.adjustMarkers(n4);
        return this;
    }

    @Override
    public CompositeByteBuf markReaderIndex() {
        return (CompositeByteBuf)super.markReaderIndex();
    }

    @Override
    public CompositeByteBuf retain(int n) {
        return (CompositeByteBuf)super.retain(n);
    }

    @Override
    public CompositeByteBuf setInt(int n, int n2) {
        return (CompositeByteBuf)super.setInt(n, n2);
    }

    @Override
    public CompositeByteBuf writeDouble(double d) {
        return (CompositeByteBuf)super.writeDouble(d);
    }

    @Override
    public CompositeByteBuf setIndex(int n, int n2) {
        return (CompositeByteBuf)super.setIndex(n, n2);
    }

    @Override
    public CompositeByteBuf getBytes(int n, ByteBuffer byteBuffer) {
        int n2 = byteBuffer.limit();
        int n3 = byteBuffer.remaining();
        this.checkIndex(n, n3);
        if (n3 == 0) {
            return this;
        }
        int n4 = this.toComponentIndex(n);
        while (n3 > 0) {
            Component component = this.components.get(n4);
            ByteBuf byteBuf = component.buf;
            int n5 = component.offset;
            int n6 = Math.min(n3, byteBuf.capacity() - (n - n5));
            byteBuffer.limit(byteBuffer.position() + n6);
            byteBuf.getBytes(n - n5, byteBuffer);
            n += n6;
            n3 -= n6;
            ++n4;
        }
        byteBuffer.limit(n2);
        return this;
    }

    @Override
    protected short _getShort(int n) {
        Component component = this.findComponent(n);
        if (n + 2 <= component.endOffset) {
            return component.buf.getShort(n - component.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (short)((this._getByte(n) & 0xFF) << 8 | this._getByte(n + 1) & 0xFF);
        }
        return (short)(this._getByte(n) & 0xFF | (this._getByte(n + 1) & 0xFF) << 8);
    }

    public int toComponentIndex(int n) {
        this.checkIndex(n);
        int n2 = 0;
        int n3 = this.components.size();
        while (n2 <= n3) {
            int n4 = n2 + n3 >>> 1;
            Component component = this.components.get(n4);
            if (n >= component.endOffset) {
                n2 = n4 + 1;
                continue;
            }
            if (n < component.offset) {
                n3 = n4 - 1;
                continue;
            }
            return n4;
        }
        throw new Error("should not reach here");
    }

    private int addComponents0(int n, ByteBuf ... byteBufArray) {
        this.checkComponentIndex(n);
        if (byteBufArray == null) {
            throw new NullPointerException("buffers");
        }
        int n2 = 0;
        for (ByteBuf byteBuf : byteBufArray) {
            if (byteBuf == null) break;
            n2 += byteBuf.readableBytes();
        }
        if (n2 == 0) {
            return n;
        }
        for (ByteBuf byteBuf : byteBufArray) {
            if (byteBuf == null) break;
            if (byteBuf.isReadable()) {
                int n3;
                if ((n = this.addComponent0(n, byteBuf) + 1) <= (n3 = this.components.size())) continue;
                n = n3;
                continue;
            }
            byteBuf.release();
        }
        return n;
    }

    @Override
    public CompositeByteBuf readerIndex(int n) {
        return (CompositeByteBuf)super.readerIndex(n);
    }

    @Override
    public CompositeByteBuf readBytes(ByteBuf byteBuf, int n, int n2) {
        return (CompositeByteBuf)super.readBytes(byteBuf, n, n2);
    }

    public CompositeByteBuf addComponent(int n, ByteBuf byteBuf) {
        this.addComponent0(n, byteBuf);
        this.consolidateIfNeeded();
        return this;
    }

    @Override
    public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
        int n3 = this.nioBufferCount();
        if (n3 == 1) {
            return gatheringByteChannel.write(this.internalNioBuffer(n, n2));
        }
        long l = gatheringByteChannel.write(this.nioBuffers(n, n2));
        if (l > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int)l;
    }

    @Override
    public CompositeByteBuf readBytes(ByteBuf byteBuf) {
        return (CompositeByteBuf)super.readBytes(byteBuf);
    }

    @Override
    public CompositeByteBuf getBytes(int n, ByteBuf byteBuf, int n2) {
        return (CompositeByteBuf)super.getBytes(n, byteBuf, n2);
    }

    public CompositeByteBuf addComponents(Iterable<ByteBuf> iterable) {
        this.addComponents0(this.components.size(), iterable);
        this.consolidateIfNeeded();
        return this;
    }

    public CompositeByteBuf(ByteBufAllocator byteBufAllocator, boolean bl, int n, ByteBuf ... byteBufArray) {
        super(Integer.MAX_VALUE);
        if (byteBufAllocator == null) {
            throw new NullPointerException("alloc");
        }
        if (n < 2) {
            throw new IllegalArgumentException("maxNumComponents: " + n + " (expected: >= 2)");
        }
        this.alloc = byteBufAllocator;
        this.direct = bl;
        this.maxNumComponents = n;
        this.addComponents0(0, byteBufArray);
        this.consolidateIfNeeded();
        this.setIndex(0, this.capacity());
        this.leak = leakDetector.open(this);
    }

    @Override
    public CompositeByteBuf resetWriterIndex() {
        return (CompositeByteBuf)super.resetWriterIndex();
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.alloc;
    }

    @Override
    public CompositeByteBuf setLong(int n, long l) {
        return (CompositeByteBuf)super.setLong(n, l);
    }

    @Override
    public long memoryAddress() {
        if (this.components.size() == 1) {
            return this.components.get((int)0).buf.memoryAddress();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public CompositeByteBuf skipBytes(int n) {
        return (CompositeByteBuf)super.skipBytes(n);
    }

    @Override
    public CompositeByteBuf setZero(int n, int n2) {
        return (CompositeByteBuf)super.setZero(n, n2);
    }

    @Override
    public CompositeByteBuf setBytes(int n, ByteBuffer byteBuffer) {
        int n2 = byteBuffer.limit();
        int n3 = byteBuffer.remaining();
        this.checkIndex(n, n3);
        if (n3 == 0) {
            return this;
        }
        int n4 = this.toComponentIndex(n);
        while (n3 > 0) {
            Component component = this.components.get(n4);
            ByteBuf byteBuf = component.buf;
            int n5 = component.offset;
            int n6 = Math.min(n3, byteBuf.capacity() - (n - n5));
            byteBuffer.limit(byteBuffer.position() + n6);
            byteBuf.setBytes(n - n5, byteBuffer);
            n += n6;
            n3 -= n6;
            ++n4;
        }
        byteBuffer.limit(n2);
        return this;
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        if (this.components.size() == 1) {
            return this.components.get((int)0).buf.internalNioBuffer(n, n2);
        }
        throw new UnsupportedOperationException();
    }

    public CompositeByteBuf(ByteBufAllocator byteBufAllocator, boolean bl, int n) {
        super(Integer.MAX_VALUE);
        if (byteBufAllocator == null) {
            throw new NullPointerException("alloc");
        }
        this.alloc = byteBufAllocator;
        this.direct = bl;
        this.maxNumComponents = n;
        this.leak = leakDetector.open(this);
    }

    public CompositeByteBuf removeComponents(int n, int n2) {
        this.checkComponentIndex(n, n2);
        List<Component> list = this.components.subList(n, n + n2);
        for (Component component : list) {
            component.freeIfNecessary();
        }
        list.clear();
        this.updateComponentOffsets(n);
        return this;
    }

    public ByteBuf componentAtOffset(int n) {
        return this.internalComponentAtOffset(n).duplicate();
    }

    @Override
    public int setBytes(int n, InputStream inputStream, int n2) throws IOException {
        this.checkIndex(n, n2);
        if (n2 == 0) {
            return inputStream.read(EmptyArrays.EMPTY_BYTES);
        }
        int n3 = this.toComponentIndex(n);
        int n4 = 0;
        do {
            Component component = this.components.get(n3);
            ByteBuf byteBuf = component.buf;
            int n5 = component.offset;
            int n6 = Math.min(n2, byteBuf.capacity() - (n - n5));
            int n7 = byteBuf.setBytes(n - n5, inputStream, n6);
            if (n7 < 0) {
                if (n4 != 0) break;
                return -1;
            }
            if (n7 == n6) {
                n += n6;
                n2 -= n6;
                n4 += n6;
                ++n3;
                continue;
            }
            n += n7;
            n2 -= n7;
            n4 += n7;
        } while (n2 > 0);
        return n4;
    }

    @Override
    public CompositeByteBuf setDouble(int n, double d) {
        return (CompositeByteBuf)super.setDouble(n, d);
    }

    public int toByteIndex(int n) {
        this.checkComponentIndex(n);
        return this.components.get((int)n).offset;
    }

    @Override
    protected byte _getByte(int n) {
        Component component = this.findComponent(n);
        return component.buf.getByte(n - component.offset);
    }

    @Override
    public CompositeByteBuf writeBoolean(boolean bl) {
        return (CompositeByteBuf)super.writeBoolean(bl);
    }

    @Override
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }

    public ByteBuf internalComponentAtOffset(int n) {
        return this.findComponent((int)n).buf;
    }

    private int addComponent0(int n, ByteBuf byteBuf) {
        this.checkComponentIndex(n);
        if (byteBuf == null) {
            throw new NullPointerException("buffer");
        }
        int n2 = byteBuf.readableBytes();
        if (n2 == 0) {
            return n;
        }
        Component component = new Component(byteBuf.order(ByteOrder.BIG_ENDIAN).slice());
        if (n == this.components.size()) {
            this.components.add(component);
            if (n == 0) {
                component.endOffset = n2;
            } else {
                Component component2 = this.components.get(n - 1);
                component.offset = component2.endOffset;
                component.endOffset = component.offset + n2;
            }
        } else {
            this.components.add(n, component);
            this.updateComponentOffsets(n);
        }
        return n;
    }

    @Override
    public CompositeByteBuf writeMedium(int n) {
        return (CompositeByteBuf)super.writeMedium(n);
    }

    @Override
    public int capacity() {
        if (this.components.isEmpty()) {
            return 0;
        }
        return this.components.get((int)(this.components.size() - 1)).endOffset;
    }

    @Override
    protected void _setInt(int n, int n2) {
        Component component = this.findComponent(n);
        if (n + 4 <= component.endOffset) {
            component.buf.setInt(n - component.offset, n2);
        } else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setShort(n, (short)(n2 >>> 16));
            this._setShort(n + 2, (short)n2);
        } else {
            this._setShort(n, (short)n2);
            this._setShort(n + 2, (short)(n2 >>> 16));
        }
    }

    @Override
    public CompositeByteBuf readBytes(ByteBuf byteBuf, int n) {
        return (CompositeByteBuf)super.readBytes(byteBuf, n);
    }

    @Override
    public byte getByte(int n) {
        return this._getByte(n);
    }

    @Override
    protected long _getLong(int n) {
        Component component = this.findComponent(n);
        if (n + 8 <= component.endOffset) {
            return component.buf.getLong(n - component.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return ((long)this._getInt(n) & 0xFFFFFFFFL) << 32 | (long)this._getInt(n + 4) & 0xFFFFFFFFL;
        }
        return (long)this._getInt(n) & 0xFFFFFFFFL | ((long)this._getInt(n + 4) & 0xFFFFFFFFL) << 32;
    }

    private void consolidateIfNeeded() {
        int n = this.components.size();
        if (n > this.maxNumComponents) {
            int n2 = this.components.get((int)(n - 1)).endOffset;
            ByteBuf byteBuf = this.allocBuffer(n2);
            for (int i = 0; i < n; ++i) {
                Component component = this.components.get(i);
                ByteBuf byteBuf2 = component.buf;
                byteBuf.writeBytes(byteBuf2);
                component.freeIfNecessary();
            }
            Component component = new Component(byteBuf);
            component.endOffset = component.length;
            this.components.clear();
            this.components.add(component);
        }
    }

    @Override
    public CompositeByteBuf discardReadBytes() {
        this.ensureAccessible();
        int n = this.readerIndex();
        if (n == 0) {
            return this;
        }
        int n2 = this.writerIndex();
        if (n == n2 && n2 == this.capacity()) {
            for (Component component : this.components) {
                component.freeIfNecessary();
            }
            this.components.clear();
            this.setIndex(0, 0);
            this.adjustMarkers(n);
            return this;
        }
        int n3 = this.toComponentIndex(n);
        for (int i = 0; i < n3; ++i) {
            this.components.get(i).freeIfNecessary();
        }
        this.components.subList(0, n3).clear();
        Component component = this.components.get(0);
        int n4 = n - component.offset;
        if (n4 == component.length) {
            this.components.remove(0);
        } else {
            Component component2 = new Component(component.buf.slice(n4, component.length - n4));
            this.components.set(0, component2);
        }
        this.updateComponentOffsets(0);
        this.setIndex(0, n2 - n);
        this.adjustMarkers(n);
        return this;
    }

    public List<ByteBuf> decompose(int n, int n2) {
        int n3;
        this.checkIndex(n, n2);
        if (n2 == 0) {
            return Collections.emptyList();
        }
        int n4 = this.toComponentIndex(n);
        ArrayList<ByteBuf> arrayList = new ArrayList<ByteBuf>(this.components.size());
        Component component = this.components.get(n4);
        ByteBuf byteBuf = component.buf.duplicate();
        byteBuf.readerIndex(n - component.offset);
        ByteBuf byteBuf2 = byteBuf;
        int n5 = n2;
        do {
            if (n5 <= (n3 = byteBuf2.readableBytes())) {
                byteBuf2.writerIndex(byteBuf2.readerIndex() + n5);
                arrayList.add(byteBuf2);
                break;
            }
            arrayList.add(byteBuf2);
            byteBuf2 = this.components.get((int)(++n4)).buf.duplicate();
        } while ((n5 -= n3) > 0);
        for (n3 = 0; n3 < arrayList.size(); ++n3) {
            arrayList.set(n3, ((ByteBuf)arrayList.get(n3)).slice());
        }
        return arrayList;
    }

    public CompositeByteBuf consolidate(int n, int n2) {
        this.checkComponentIndex(n, n2);
        if (n2 <= 1) {
            return this;
        }
        int n3 = n + n2;
        Component component = this.components.get(n3 - 1);
        int n4 = component.endOffset - this.components.get((int)n).offset;
        ByteBuf byteBuf = this.allocBuffer(n4);
        for (int i = n; i < n3; ++i) {
            Component component2 = this.components.get(i);
            ByteBuf byteBuf2 = component2.buf;
            byteBuf.writeBytes(byteBuf2);
            component2.freeIfNecessary();
        }
        this.components.subList(n + 1, n3).clear();
        this.components.set(n, new Component(byteBuf));
        this.updateComponentOffsets(n);
        return this;
    }

    @Override
    protected void _setLong(int n, long l) {
        Component component = this.findComponent(n);
        if (n + 8 <= component.endOffset) {
            component.buf.setLong(n - component.offset, l);
        } else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setInt(n, (int)(l >>> 32));
            this._setInt(n + 4, (int)l);
        } else {
            this._setInt(n, (int)l);
            this._setInt(n + 4, (int)(l >>> 32));
        }
    }

    private void updateComponentOffsets(int n) {
        int n2 = this.components.size();
        if (n2 <= n) {
            return;
        }
        Component component = this.components.get(n);
        if (n == 0) {
            component.offset = 0;
            component.endOffset = component.length;
            ++n;
        }
        for (int i = n; i < n2; ++i) {
            Component component2 = this.components.get(i - 1);
            Component component3 = this.components.get(i);
            component3.offset = component2.endOffset;
            component3.endOffset = component3.offset + component3.length;
        }
    }

    @Override
    public CompositeByteBuf writeBytes(ByteBuffer byteBuffer) {
        return (CompositeByteBuf)super.writeBytes(byteBuffer);
    }

    @Override
    public ByteBuffer nioBuffer(int n, int n2) {
        Comparable<ByteBuffer> comparable;
        if (this.components.size() == 1 && ((ByteBuf)(comparable = this.components.get((int)0).buf)).nioBufferCount() == 1) {
            return this.components.get((int)0).buf.nioBuffer(n, n2);
        }
        comparable = ByteBuffer.allocate(n2).order(this.order());
        ByteBuffer[] byteBufferArray = this.nioBuffers(n, n2);
        for (int i = 0; i < byteBufferArray.length; ++i) {
            ((ByteBuffer)comparable).put(byteBufferArray[i]);
        }
        ((Buffer)((Object)comparable)).flip();
        return comparable;
    }

    @Override
    public CompositeByteBuf setBoolean(int n, boolean bl) {
        return (CompositeByteBuf)super.setBoolean(n, bl);
    }

    @Override
    public CompositeByteBuf getBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.checkDstIndex(n, n3, n2, byteBuf.capacity());
        if (n3 == 0) {
            return this;
        }
        int n4 = this.toComponentIndex(n);
        while (n3 > 0) {
            Component component = this.components.get(n4);
            ByteBuf byteBuf2 = component.buf;
            int n5 = component.offset;
            int n6 = Math.min(n3, byteBuf2.capacity() - (n - n5));
            byteBuf2.getBytes(n - n5, byteBuf, n2, n6);
            n += n6;
            n2 += n6;
            n3 -= n6;
            ++n4;
        }
        return this;
    }

    public CompositeByteBuf removeComponent(int n) {
        this.checkComponentIndex(n);
        this.components.remove(n).freeIfNecessary();
        this.updateComponentOffsets(n);
        return this;
    }

    @Override
    public CompositeByteBuf readBytes(OutputStream outputStream, int n) throws IOException {
        return (CompositeByteBuf)super.readBytes(outputStream, n);
    }

    @Override
    public CompositeByteBuf writerIndex(int n) {
        return (CompositeByteBuf)super.writerIndex(n);
    }

    @Override
    public CompositeByteBuf setBytes(int n, ByteBuf byteBuf, int n2) {
        return (CompositeByteBuf)super.setBytes(n, byteBuf, n2);
    }

    public CompositeByteBuf addComponents(int n, Iterable<ByteBuf> iterable) {
        this.addComponents0(n, iterable);
        this.consolidateIfNeeded();
        return this;
    }

    @Override
    public boolean hasMemoryAddress() {
        if (this.components.size() == 1) {
            return this.components.get((int)0).buf.hasMemoryAddress();
        }
        return false;
    }

    @Override
    public ByteBuf unwrap() {
        return null;
    }

    @Override
    public CompositeByteBuf setBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.checkSrcIndex(n, n3, n2, byteBuf.capacity());
        if (n3 == 0) {
            return this;
        }
        int n4 = this.toComponentIndex(n);
        while (n3 > 0) {
            Component component = this.components.get(n4);
            ByteBuf byteBuf2 = component.buf;
            int n5 = component.offset;
            int n6 = Math.min(n3, byteBuf2.capacity() - (n - n5));
            byteBuf2.setBytes(n - n5, byteBuf, n2, n6);
            n += n6;
            n2 += n6;
            n3 -= n6;
            ++n4;
        }
        return this;
    }

    public CompositeByteBuf(ByteBufAllocator byteBufAllocator, boolean bl, int n, Iterable<ByteBuf> iterable) {
        super(Integer.MAX_VALUE);
        if (byteBufAllocator == null) {
            throw new NullPointerException("alloc");
        }
        if (n < 2) {
            throw new IllegalArgumentException("maxNumComponents: " + n + " (expected: >= 2)");
        }
        this.alloc = byteBufAllocator;
        this.direct = bl;
        this.maxNumComponents = n;
        this.addComponents0(0, iterable);
        this.consolidateIfNeeded();
        this.setIndex(0, this.capacity());
        this.leak = leakDetector.open(this);
    }

    @Override
    public ByteBuf copy(int n, int n2) {
        this.checkIndex(n, n2);
        ByteBuf byteBuf = Unpooled.buffer(n2);
        if (n2 != 0) {
            this.copyTo(n, n2, this.toComponentIndex(n), byteBuf);
        }
        return byteBuf;
    }

    private void copyTo(int n, int n2, int n3, ByteBuf byteBuf) {
        int n4 = 0;
        int n5 = n3;
        while (n2 > 0) {
            Component component = this.components.get(n5);
            ByteBuf byteBuf2 = component.buf;
            int n6 = component.offset;
            int n7 = Math.min(n2, byteBuf2.capacity() - (n - n6));
            byteBuf2.getBytes(n - n6, byteBuf, n4, n7);
            n += n7;
            n4 += n7;
            n2 -= n7;
            ++n5;
        }
        byteBuf.writerIndex(byteBuf.capacity());
    }

    @Override
    public CompositeByteBuf setBytes(int n, byte[] byArray, int n2, int n3) {
        this.checkSrcIndex(n, n3, n2, byArray.length);
        if (n3 == 0) {
            return this;
        }
        int n4 = this.toComponentIndex(n);
        while (n3 > 0) {
            Component component = this.components.get(n4);
            ByteBuf byteBuf = component.buf;
            int n5 = component.offset;
            int n6 = Math.min(n3, byteBuf.capacity() - (n - n5));
            byteBuf.setBytes(n - n5, byArray, n2, n6);
            n += n6;
            n2 += n6;
            n3 -= n6;
            ++n4;
        }
        return this;
    }

    @Override
    public CompositeByteBuf markWriterIndex() {
        return (CompositeByteBuf)super.markWriterIndex();
    }

    @Override
    public CompositeByteBuf ensureWritable(int n) {
        return (CompositeByteBuf)super.ensureWritable(n);
    }

    @Override
    public CompositeByteBuf retain() {
        return (CompositeByteBuf)super.retain();
    }

    public ByteBuf internalComponent(int n) {
        this.checkComponentIndex(n);
        return this.components.get((int)n).buf;
    }

    @Override
    protected int _getUnsignedMedium(int n) {
        Component component = this.findComponent(n);
        if (n + 3 <= component.endOffset) {
            return component.buf.getUnsignedMedium(n - component.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (this._getShort(n) & 0xFFFF) << 8 | this._getByte(n + 2) & 0xFF;
        }
        return this._getShort(n) & 0xFFFF | (this._getByte(n + 2) & 0xFF) << 16;
    }

    @Override
    public int arrayOffset() {
        if (this.components.size() == 1) {
            return this.components.get((int)0).buf.arrayOffset();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public CompositeByteBuf writeBytes(ByteBuf byteBuf) {
        return (CompositeByteBuf)super.writeBytes(byteBuf);
    }

    @Override
    public CompositeByteBuf setMedium(int n, int n2) {
        return (CompositeByteBuf)super.setMedium(n, n2);
    }

    @Override
    public CompositeByteBuf writeByte(int n) {
        return (CompositeByteBuf)super.writeByte(n);
    }

    @Override
    public CompositeByteBuf writeLong(long l) {
        return (CompositeByteBuf)super.writeLong(l);
    }

    @Override
    public CompositeByteBuf capacity(int n) {
        this.ensureAccessible();
        if (n < 0 || n > this.maxCapacity()) {
            throw new IllegalArgumentException("newCapacity: " + n);
        }
        int n2 = this.capacity();
        if (n > n2) {
            int n3 = n - n2;
            int n4 = this.components.size();
            if (n4 < this.maxNumComponents) {
                ByteBuf byteBuf = this.allocBuffer(n3);
                byteBuf.setIndex(0, n3);
                this.addComponent0(this.components.size(), byteBuf);
            } else {
                ByteBuf byteBuf = this.allocBuffer(n3);
                byteBuf.setIndex(0, n3);
                this.addComponent0(this.components.size(), byteBuf);
                this.consolidateIfNeeded();
            }
        } else if (n < n2) {
            int n5 = n2 - n;
            ListIterator<Component> listIterator = this.components.listIterator(this.components.size());
            while (listIterator.hasPrevious()) {
                Component component = listIterator.previous();
                if (n5 >= component.length) {
                    n5 -= component.length;
                    listIterator.remove();
                    continue;
                }
                Component component2 = new Component(component.buf.slice(0, component.length - n5));
                component2.offset = component.offset;
                component2.endOffset = component2.offset + component2.length;
                listIterator.set(component2);
                break;
            }
            if (this.readerIndex() > n) {
                this.setIndex(n, n);
            } else if (this.writerIndex() > n) {
                this.writerIndex(n);
            }
        }
        return this;
    }

    @Override
    public CompositeByteBuf setFloat(int n, float f) {
        return (CompositeByteBuf)super.setFloat(n, f);
    }

    @Override
    public CompositeByteBuf setBytes(int n, ByteBuf byteBuf) {
        return (CompositeByteBuf)super.setBytes(n, byteBuf);
    }

    @Override
    public CompositeByteBuf setShort(int n, int n2) {
        return (CompositeByteBuf)super.setShort(n, n2);
    }

    public CompositeByteBuf addComponent(ByteBuf byteBuf) {
        this.addComponent0(this.components.size(), byteBuf);
        this.consolidateIfNeeded();
        return this;
    }

    @Override
    public CompositeByteBuf readBytes(ByteBuffer byteBuffer) {
        return (CompositeByteBuf)super.readBytes(byteBuffer);
    }

    @Override
    public CompositeByteBuf getBytes(int n, OutputStream outputStream, int n2) throws IOException {
        this.checkIndex(n, n2);
        if (n2 == 0) {
            return this;
        }
        int n3 = this.toComponentIndex(n);
        while (n2 > 0) {
            Component component = this.components.get(n3);
            ByteBuf byteBuf = component.buf;
            int n4 = component.offset;
            int n5 = Math.min(n2, byteBuf.capacity() - (n - n4));
            byteBuf.getBytes(n - n4, outputStream, n5);
            n += n5;
            n2 -= n5;
            ++n3;
        }
        return this;
    }

    @Override
    public CompositeByteBuf writeZero(int n) {
        return (CompositeByteBuf)super.writeZero(n);
    }

    @Override
    protected void _setByte(int n, int n2) {
        this.setByte(n, n2);
    }

    private ByteBuf allocBuffer(int n) {
        if (this.direct) {
            return this.alloc().directBuffer(n);
        }
        return this.alloc().heapBuffer(n);
    }

    @Override
    public int nioBufferCount() {
        if (this.components.size() == 1) {
            return this.components.get((int)0).buf.nioBufferCount();
        }
        int n = 0;
        int n2 = this.components.size();
        for (int i = 0; i < n2; ++i) {
            Component component = this.components.get(i);
            n += component.buf.nioBufferCount();
        }
        return n;
    }

    @Override
    public CompositeByteBuf writeBytes(ByteBuf byteBuf, int n) {
        return (CompositeByteBuf)super.writeBytes(byteBuf, n);
    }

    @Override
    public CompositeByteBuf writeFloat(float f) {
        return (CompositeByteBuf)super.writeFloat(f);
    }

    @Override
    public CompositeByteBuf setChar(int n, int n2) {
        return (CompositeByteBuf)super.setChar(n, n2);
    }

    @Override
    public CompositeByteBuf clear() {
        return (CompositeByteBuf)super.clear();
    }

    public CompositeByteBuf addComponents(int n, ByteBuf ... byteBufArray) {
        this.addComponents0(n, byteBufArray);
        this.consolidateIfNeeded();
        return this;
    }

    @Override
    public CompositeByteBuf writeInt(int n) {
        return (CompositeByteBuf)super.writeInt(n);
    }

    @Override
    protected void _setMedium(int n, int n2) {
        Component component = this.findComponent(n);
        if (n + 3 <= component.endOffset) {
            component.buf.setMedium(n - component.offset, n2);
        } else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setShort(n, (short)(n2 >> 8));
            this._setByte(n + 2, (byte)n2);
        } else {
            this._setShort(n, (short)n2);
            this._setByte(n + 2, (byte)(n2 >>> 16));
        }
    }

    public int numComponents() {
        return this.components.size();
    }

    public Iterator<ByteBuf> iterator() {
        this.ensureAccessible();
        ArrayList<ByteBuf> arrayList = new ArrayList<ByteBuf>(this.components.size());
        for (Component component : this.components) {
            arrayList.add(component.buf);
        }
        return arrayList.iterator();
    }

    @Override
    public CompositeByteBuf resetReaderIndex() {
        return (CompositeByteBuf)super.resetReaderIndex();
    }

    public int maxNumComponents() {
        return this.maxNumComponents;
    }

    @Override
    public String toString() {
        String string = super.toString();
        string = string.substring(0, string.length() - 1);
        return string + ", components=" + this.components.size() + ')';
    }

    @Override
    public CompositeByteBuf setBytes(int n, byte[] byArray) {
        return (CompositeByteBuf)super.setBytes(n, byArray);
    }

    @Override
    public CompositeByteBuf writeBytes(byte[] byArray, int n, int n2) {
        return (CompositeByteBuf)super.writeBytes(byArray, n, n2);
    }

    @Override
    public byte[] array() {
        if (this.components.size() == 1) {
            return this.components.get((int)0).buf.array();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public CompositeByteBuf discardSomeReadBytes() {
        return this.discardReadComponents();
    }

    private static final class Component {
        final int length;
        final ByteBuf buf;
        int endOffset;
        int offset;

        void freeIfNecessary() {
            this.buf.release();
        }

        Component(ByteBuf byteBuf) {
            this.buf = byteBuf;
            this.length = byteBuf.readableBytes();
        }
    }
}

