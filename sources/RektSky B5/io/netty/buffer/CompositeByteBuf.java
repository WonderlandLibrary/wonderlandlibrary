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
    private final ByteBufAllocator alloc;
    private final boolean direct;
    private final List<Component> components = new ArrayList<Component>();
    private final int maxNumComponents;
    private static final ByteBuffer FULL_BYTEBUFFER = (ByteBuffer)ByteBuffer.allocate(1).position(1);
    private boolean freed;

    public CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents) {
        super(Integer.MAX_VALUE);
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        this.alloc = alloc;
        this.direct = direct;
        this.maxNumComponents = maxNumComponents;
        this.leak = leakDetector.open(this);
    }

    public CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents, ByteBuf ... buffers) {
        super(Integer.MAX_VALUE);
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        if (maxNumComponents < 2) {
            throw new IllegalArgumentException("maxNumComponents: " + maxNumComponents + " (expected: >= 2)");
        }
        this.alloc = alloc;
        this.direct = direct;
        this.maxNumComponents = maxNumComponents;
        this.addComponents0(0, buffers);
        this.consolidateIfNeeded();
        this.setIndex(0, this.capacity());
        this.leak = leakDetector.open(this);
    }

    public CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents, Iterable<ByteBuf> buffers) {
        super(Integer.MAX_VALUE);
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        if (maxNumComponents < 2) {
            throw new IllegalArgumentException("maxNumComponents: " + maxNumComponents + " (expected: >= 2)");
        }
        this.alloc = alloc;
        this.direct = direct;
        this.maxNumComponents = maxNumComponents;
        this.addComponents0(0, buffers);
        this.consolidateIfNeeded();
        this.setIndex(0, this.capacity());
        this.leak = leakDetector.open(this);
    }

    public CompositeByteBuf addComponent(ByteBuf buffer) {
        this.addComponent0(this.components.size(), buffer);
        this.consolidateIfNeeded();
        return this;
    }

    public CompositeByteBuf addComponents(ByteBuf ... buffers) {
        this.addComponents0(this.components.size(), buffers);
        this.consolidateIfNeeded();
        return this;
    }

    public CompositeByteBuf addComponents(Iterable<ByteBuf> buffers) {
        this.addComponents0(this.components.size(), buffers);
        this.consolidateIfNeeded();
        return this;
    }

    public CompositeByteBuf addComponent(int cIndex, ByteBuf buffer) {
        this.addComponent0(cIndex, buffer);
        this.consolidateIfNeeded();
        return this;
    }

    private int addComponent0(int cIndex, ByteBuf buffer) {
        this.checkComponentIndex(cIndex);
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        int readableBytes = buffer.readableBytes();
        if (readableBytes == 0) {
            return cIndex;
        }
        Component c2 = new Component(buffer.order(ByteOrder.BIG_ENDIAN).slice());
        if (cIndex == this.components.size()) {
            this.components.add(c2);
            if (cIndex == 0) {
                c2.endOffset = readableBytes;
            } else {
                Component prev = this.components.get(cIndex - 1);
                c2.offset = prev.endOffset;
                c2.endOffset = c2.offset + readableBytes;
            }
        } else {
            this.components.add(cIndex, c2);
            this.updateComponentOffsets(cIndex);
        }
        return cIndex;
    }

    public CompositeByteBuf addComponents(int cIndex, ByteBuf ... buffers) {
        this.addComponents0(cIndex, buffers);
        this.consolidateIfNeeded();
        return this;
    }

    private int addComponents0(int cIndex, ByteBuf ... buffers) {
        this.checkComponentIndex(cIndex);
        if (buffers == null) {
            throw new NullPointerException("buffers");
        }
        int readableBytes = 0;
        for (ByteBuf b2 : buffers) {
            if (b2 == null) break;
            readableBytes += b2.readableBytes();
        }
        if (readableBytes == 0) {
            return cIndex;
        }
        for (ByteBuf b2 : buffers) {
            if (b2 == null) break;
            if (b2.isReadable()) {
                int size;
                if ((cIndex = this.addComponent0(cIndex, b2) + 1) <= (size = this.components.size())) continue;
                cIndex = size;
                continue;
            }
            b2.release();
        }
        return cIndex;
    }

    public CompositeByteBuf addComponents(int cIndex, Iterable<ByteBuf> buffers) {
        this.addComponents0(cIndex, buffers);
        this.consolidateIfNeeded();
        return this;
    }

    private int addComponents0(int cIndex, Iterable<ByteBuf> buffers) {
        if (buffers == null) {
            throw new NullPointerException("buffers");
        }
        if (buffers instanceof ByteBuf) {
            return this.addComponent0(cIndex, (ByteBuf)((Object)buffers));
        }
        if (!(buffers instanceof Collection)) {
            ArrayList<ByteBuf> list = new ArrayList<ByteBuf>();
            for (ByteBuf b2 : buffers) {
                list.add(b2);
            }
            buffers = list;
        }
        Collection col = (Collection)buffers;
        return this.addComponents0(cIndex, col.toArray(new ByteBuf[col.size()]));
    }

    private void consolidateIfNeeded() {
        int numComponents = this.components.size();
        if (numComponents > this.maxNumComponents) {
            int capacity = this.components.get((int)(numComponents - 1)).endOffset;
            ByteBuf consolidated = this.allocBuffer(capacity);
            for (int i2 = 0; i2 < numComponents; ++i2) {
                Component c2 = this.components.get(i2);
                ByteBuf b2 = c2.buf;
                consolidated.writeBytes(b2);
                c2.freeIfNecessary();
            }
            Component c3 = new Component(consolidated);
            c3.endOffset = c3.length;
            this.components.clear();
            this.components.add(c3);
        }
    }

    private void checkComponentIndex(int cIndex) {
        this.ensureAccessible();
        if (cIndex < 0 || cIndex > this.components.size()) {
            throw new IndexOutOfBoundsException(String.format("cIndex: %d (expected: >= 0 && <= numComponents(%d))", cIndex, this.components.size()));
        }
    }

    private void checkComponentIndex(int cIndex, int numComponents) {
        this.ensureAccessible();
        if (cIndex < 0 || cIndex + numComponents > this.components.size()) {
            throw new IndexOutOfBoundsException(String.format("cIndex: %d, numComponents: %d (expected: cIndex >= 0 && cIndex + numComponents <= totalNumComponents(%d))", cIndex, numComponents, this.components.size()));
        }
    }

    private void updateComponentOffsets(int cIndex) {
        int size = this.components.size();
        if (size <= cIndex) {
            return;
        }
        Component c2 = this.components.get(cIndex);
        if (cIndex == 0) {
            c2.offset = 0;
            c2.endOffset = c2.length;
            ++cIndex;
        }
        for (int i2 = cIndex; i2 < size; ++i2) {
            Component prev = this.components.get(i2 - 1);
            Component cur = this.components.get(i2);
            cur.offset = prev.endOffset;
            cur.endOffset = cur.offset + cur.length;
        }
    }

    public CompositeByteBuf removeComponent(int cIndex) {
        this.checkComponentIndex(cIndex);
        this.components.remove(cIndex).freeIfNecessary();
        this.updateComponentOffsets(cIndex);
        return this;
    }

    public CompositeByteBuf removeComponents(int cIndex, int numComponents) {
        this.checkComponentIndex(cIndex, numComponents);
        List<Component> toRemove = this.components.subList(cIndex, cIndex + numComponents);
        for (Component c2 : toRemove) {
            c2.freeIfNecessary();
        }
        toRemove.clear();
        this.updateComponentOffsets(cIndex);
        return this;
    }

    public Iterator<ByteBuf> iterator() {
        this.ensureAccessible();
        ArrayList<ByteBuf> list = new ArrayList<ByteBuf>(this.components.size());
        for (Component c2 : this.components) {
            list.add(c2.buf);
        }
        return list.iterator();
    }

    public List<ByteBuf> decompose(int offset, int length) {
        int readableBytes;
        this.checkIndex(offset, length);
        if (length == 0) {
            return Collections.emptyList();
        }
        int componentId = this.toComponentIndex(offset);
        ArrayList<ByteBuf> slice = new ArrayList<ByteBuf>(this.components.size());
        Component firstC = this.components.get(componentId);
        ByteBuf first = firstC.buf.duplicate();
        first.readerIndex(offset - firstC.offset);
        ByteBuf buf = first;
        int bytesToSlice = length;
        do {
            if (bytesToSlice <= (readableBytes = buf.readableBytes())) {
                buf.writerIndex(buf.readerIndex() + bytesToSlice);
                slice.add(buf);
                break;
            }
            slice.add(buf);
            buf = this.components.get((int)(++componentId)).buf.duplicate();
        } while ((bytesToSlice -= readableBytes) > 0);
        for (int i2 = 0; i2 < slice.size(); ++i2) {
            slice.set(i2, ((ByteBuf)slice.get(i2)).slice());
        }
        return slice;
    }

    @Override
    public boolean isDirect() {
        int size = this.components.size();
        if (size == 0) {
            return false;
        }
        for (int i2 = 0; i2 < size; ++i2) {
            if (this.components.get((int)i2).buf.isDirect()) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean hasArray() {
        if (this.components.size() == 1) {
            return this.components.get((int)0).buf.hasArray();
        }
        return false;
    }

    @Override
    public byte[] array() {
        if (this.components.size() == 1) {
            return this.components.get((int)0).buf.array();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public int arrayOffset() {
        if (this.components.size() == 1) {
            return this.components.get((int)0).buf.arrayOffset();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasMemoryAddress() {
        if (this.components.size() == 1) {
            return this.components.get((int)0).buf.hasMemoryAddress();
        }
        return false;
    }

    @Override
    public long memoryAddress() {
        if (this.components.size() == 1) {
            return this.components.get((int)0).buf.memoryAddress();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public int capacity() {
        if (this.components.isEmpty()) {
            return 0;
        }
        return this.components.get((int)(this.components.size() - 1)).endOffset;
    }

    @Override
    public CompositeByteBuf capacity(int newCapacity) {
        this.ensureAccessible();
        if (newCapacity < 0 || newCapacity > this.maxCapacity()) {
            throw new IllegalArgumentException("newCapacity: " + newCapacity);
        }
        int oldCapacity = this.capacity();
        if (newCapacity > oldCapacity) {
            int paddingLength = newCapacity - oldCapacity;
            int nComponents = this.components.size();
            if (nComponents < this.maxNumComponents) {
                ByteBuf padding = this.allocBuffer(paddingLength);
                padding.setIndex(0, paddingLength);
                this.addComponent0(this.components.size(), padding);
            } else {
                ByteBuf padding = this.allocBuffer(paddingLength);
                padding.setIndex(0, paddingLength);
                this.addComponent0(this.components.size(), padding);
                this.consolidateIfNeeded();
            }
        } else if (newCapacity < oldCapacity) {
            int bytesToTrim = oldCapacity - newCapacity;
            ListIterator<Component> i2 = this.components.listIterator(this.components.size());
            while (i2.hasPrevious()) {
                Component c2 = i2.previous();
                if (bytesToTrim >= c2.length) {
                    bytesToTrim -= c2.length;
                    i2.remove();
                    continue;
                }
                Component newC = new Component(c2.buf.slice(0, c2.length - bytesToTrim));
                newC.offset = c2.offset;
                newC.endOffset = newC.offset + newC.length;
                i2.set(newC);
                break;
            }
            if (this.readerIndex() > newCapacity) {
                this.setIndex(newCapacity, newCapacity);
            } else if (this.writerIndex() > newCapacity) {
                this.writerIndex(newCapacity);
            }
        }
        return this;
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.alloc;
    }

    @Override
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }

    public int numComponents() {
        return this.components.size();
    }

    public int maxNumComponents() {
        return this.maxNumComponents;
    }

    public int toComponentIndex(int offset) {
        this.checkIndex(offset);
        int low = 0;
        int high = this.components.size();
        while (low <= high) {
            int mid = low + high >>> 1;
            Component c2 = this.components.get(mid);
            if (offset >= c2.endOffset) {
                low = mid + 1;
                continue;
            }
            if (offset < c2.offset) {
                high = mid - 1;
                continue;
            }
            return mid;
        }
        throw new Error("should not reach here");
    }

    public int toByteIndex(int cIndex) {
        this.checkComponentIndex(cIndex);
        return this.components.get((int)cIndex).offset;
    }

    @Override
    public byte getByte(int index) {
        return this._getByte(index);
    }

    @Override
    protected byte _getByte(int index) {
        Component c2 = this.findComponent(index);
        return c2.buf.getByte(index - c2.offset);
    }

    @Override
    protected short _getShort(int index) {
        Component c2 = this.findComponent(index);
        if (index + 2 <= c2.endOffset) {
            return c2.buf.getShort(index - c2.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (short)((this._getByte(index) & 0xFF) << 8 | this._getByte(index + 1) & 0xFF);
        }
        return (short)(this._getByte(index) & 0xFF | (this._getByte(index + 1) & 0xFF) << 8);
    }

    @Override
    protected int _getUnsignedMedium(int index) {
        Component c2 = this.findComponent(index);
        if (index + 3 <= c2.endOffset) {
            return c2.buf.getUnsignedMedium(index - c2.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (this._getShort(index) & 0xFFFF) << 8 | this._getByte(index + 2) & 0xFF;
        }
        return this._getShort(index) & 0xFFFF | (this._getByte(index + 2) & 0xFF) << 16;
    }

    @Override
    protected int _getInt(int index) {
        Component c2 = this.findComponent(index);
        if (index + 4 <= c2.endOffset) {
            return c2.buf.getInt(index - c2.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (this._getShort(index) & 0xFFFF) << 16 | this._getShort(index + 2) & 0xFFFF;
        }
        return this._getShort(index) & 0xFFFF | (this._getShort(index + 2) & 0xFFFF) << 16;
    }

    @Override
    protected long _getLong(int index) {
        Component c2 = this.findComponent(index);
        if (index + 8 <= c2.endOffset) {
            return c2.buf.getLong(index - c2.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return ((long)this._getInt(index) & 0xFFFFFFFFL) << 32 | (long)this._getInt(index + 4) & 0xFFFFFFFFL;
        }
        return (long)this._getInt(index) & 0xFFFFFFFFL | ((long)this._getInt(index + 4) & 0xFFFFFFFFL) << 32;
    }

    @Override
    public CompositeByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        this.checkDstIndex(index, length, dstIndex, dst.length);
        if (length == 0) {
            return this;
        }
        int i2 = this.toComponentIndex(index);
        while (length > 0) {
            Component c2 = this.components.get(i2);
            ByteBuf s2 = c2.buf;
            int adjustment = c2.offset;
            int localLength = Math.min(length, s2.capacity() - (index - adjustment));
            s2.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
            length -= localLength;
            ++i2;
        }
        return this;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public CompositeByteBuf getBytes(int index, ByteBuffer dst) {
        int limit = dst.limit();
        int length = dst.remaining();
        this.checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        int i2 = this.toComponentIndex(index);
        try {
            while (length > 0) {
                Component c2 = this.components.get(i2);
                ByteBuf s2 = c2.buf;
                int adjustment = c2.offset;
                int localLength = Math.min(length, s2.capacity() - (index - adjustment));
                dst.limit(dst.position() + localLength);
                s2.getBytes(index - adjustment, dst);
                index += localLength;
                length -= localLength;
                ++i2;
            }
        }
        finally {
            dst.limit(limit);
        }
        return this;
    }

    @Override
    public CompositeByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        this.checkDstIndex(index, length, dstIndex, dst.capacity());
        if (length == 0) {
            return this;
        }
        int i2 = this.toComponentIndex(index);
        while (length > 0) {
            Component c2 = this.components.get(i2);
            ByteBuf s2 = c2.buf;
            int adjustment = c2.offset;
            int localLength = Math.min(length, s2.capacity() - (index - adjustment));
            s2.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
            length -= localLength;
            ++i2;
        }
        return this;
    }

    @Override
    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        int count = this.nioBufferCount();
        if (count == 1) {
            return out.write(this.internalNioBuffer(index, length));
        }
        long writtenBytes = out.write(this.nioBuffers(index, length));
        if (writtenBytes > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int)writtenBytes;
    }

    @Override
    public CompositeByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        int i2 = this.toComponentIndex(index);
        while (length > 0) {
            Component c2 = this.components.get(i2);
            ByteBuf s2 = c2.buf;
            int adjustment = c2.offset;
            int localLength = Math.min(length, s2.capacity() - (index - adjustment));
            s2.getBytes(index - adjustment, out, localLength);
            index += localLength;
            length -= localLength;
            ++i2;
        }
        return this;
    }

    @Override
    public CompositeByteBuf setByte(int index, int value) {
        Component c2 = this.findComponent(index);
        c2.buf.setByte(index - c2.offset, value);
        return this;
    }

    @Override
    protected void _setByte(int index, int value) {
        this.setByte(index, value);
    }

    @Override
    public CompositeByteBuf setShort(int index, int value) {
        return (CompositeByteBuf)super.setShort(index, value);
    }

    @Override
    protected void _setShort(int index, int value) {
        Component c2 = this.findComponent(index);
        if (index + 2 <= c2.endOffset) {
            c2.buf.setShort(index - c2.offset, value);
        } else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setByte(index, (byte)(value >>> 8));
            this._setByte(index + 1, (byte)value);
        } else {
            this._setByte(index, (byte)value);
            this._setByte(index + 1, (byte)(value >>> 8));
        }
    }

    @Override
    public CompositeByteBuf setMedium(int index, int value) {
        return (CompositeByteBuf)super.setMedium(index, value);
    }

    @Override
    protected void _setMedium(int index, int value) {
        Component c2 = this.findComponent(index);
        if (index + 3 <= c2.endOffset) {
            c2.buf.setMedium(index - c2.offset, value);
        } else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setShort(index, (short)(value >> 8));
            this._setByte(index + 2, (byte)value);
        } else {
            this._setShort(index, (short)value);
            this._setByte(index + 2, (byte)(value >>> 16));
        }
    }

    @Override
    public CompositeByteBuf setInt(int index, int value) {
        return (CompositeByteBuf)super.setInt(index, value);
    }

    @Override
    protected void _setInt(int index, int value) {
        Component c2 = this.findComponent(index);
        if (index + 4 <= c2.endOffset) {
            c2.buf.setInt(index - c2.offset, value);
        } else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setShort(index, (short)(value >>> 16));
            this._setShort(index + 2, (short)value);
        } else {
            this._setShort(index, (short)value);
            this._setShort(index + 2, (short)(value >>> 16));
        }
    }

    @Override
    public CompositeByteBuf setLong(int index, long value) {
        return (CompositeByteBuf)super.setLong(index, value);
    }

    @Override
    protected void _setLong(int index, long value) {
        Component c2 = this.findComponent(index);
        if (index + 8 <= c2.endOffset) {
            c2.buf.setLong(index - c2.offset, value);
        } else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setInt(index, (int)(value >>> 32));
            this._setInt(index + 4, (int)value);
        } else {
            this._setInt(index, (int)value);
            this._setInt(index + 4, (int)(value >>> 32));
        }
    }

    @Override
    public CompositeByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        this.checkSrcIndex(index, length, srcIndex, src.length);
        if (length == 0) {
            return this;
        }
        int i2 = this.toComponentIndex(index);
        while (length > 0) {
            Component c2 = this.components.get(i2);
            ByteBuf s2 = c2.buf;
            int adjustment = c2.offset;
            int localLength = Math.min(length, s2.capacity() - (index - adjustment));
            s2.setBytes(index - adjustment, src, srcIndex, localLength);
            index += localLength;
            srcIndex += localLength;
            length -= localLength;
            ++i2;
        }
        return this;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public CompositeByteBuf setBytes(int index, ByteBuffer src) {
        int limit = src.limit();
        int length = src.remaining();
        this.checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        int i2 = this.toComponentIndex(index);
        try {
            while (length > 0) {
                Component c2 = this.components.get(i2);
                ByteBuf s2 = c2.buf;
                int adjustment = c2.offset;
                int localLength = Math.min(length, s2.capacity() - (index - adjustment));
                src.limit(src.position() + localLength);
                s2.setBytes(index - adjustment, src);
                index += localLength;
                length -= localLength;
                ++i2;
            }
        }
        finally {
            src.limit(limit);
        }
        return this;
    }

    @Override
    public CompositeByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        this.checkSrcIndex(index, length, srcIndex, src.capacity());
        if (length == 0) {
            return this;
        }
        int i2 = this.toComponentIndex(index);
        while (length > 0) {
            Component c2 = this.components.get(i2);
            ByteBuf s2 = c2.buf;
            int adjustment = c2.offset;
            int localLength = Math.min(length, s2.capacity() - (index - adjustment));
            s2.setBytes(index - adjustment, src, srcIndex, localLength);
            index += localLength;
            srcIndex += localLength;
            length -= localLength;
            ++i2;
        }
        return this;
    }

    @Override
    public int setBytes(int index, InputStream in, int length) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return in.read(EmptyArrays.EMPTY_BYTES);
        }
        int i2 = this.toComponentIndex(index);
        int readBytes = 0;
        do {
            Component c2 = this.components.get(i2);
            ByteBuf s2 = c2.buf;
            int adjustment = c2.offset;
            int localLength = Math.min(length, s2.capacity() - (index - adjustment));
            int localReadBytes = s2.setBytes(index - adjustment, in, localLength);
            if (localReadBytes < 0) {
                if (readBytes != 0) break;
                return -1;
            }
            if (localReadBytes == localLength) {
                index += localLength;
                length -= localLength;
                readBytes += localLength;
                ++i2;
                continue;
            }
            index += localReadBytes;
            length -= localReadBytes;
            readBytes += localReadBytes;
        } while (length > 0);
        return readBytes;
    }

    @Override
    public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return in.read(FULL_BYTEBUFFER);
        }
        int i2 = this.toComponentIndex(index);
        int readBytes = 0;
        do {
            Component c2 = this.components.get(i2);
            ByteBuf s2 = c2.buf;
            int adjustment = c2.offset;
            int localLength = Math.min(length, s2.capacity() - (index - adjustment));
            int localReadBytes = s2.setBytes(index - adjustment, in, localLength);
            if (localReadBytes == 0) break;
            if (localReadBytes < 0) {
                if (readBytes != 0) break;
                return -1;
            }
            if (localReadBytes == localLength) {
                index += localLength;
                length -= localLength;
                readBytes += localLength;
                ++i2;
                continue;
            }
            index += localReadBytes;
            length -= localReadBytes;
            readBytes += localReadBytes;
        } while (length > 0);
        return readBytes;
    }

    @Override
    public ByteBuf copy(int index, int length) {
        this.checkIndex(index, length);
        ByteBuf dst = Unpooled.buffer(length);
        if (length != 0) {
            this.copyTo(index, length, this.toComponentIndex(index), dst);
        }
        return dst;
    }

    private void copyTo(int index, int length, int componentId, ByteBuf dst) {
        int dstIndex = 0;
        int i2 = componentId;
        while (length > 0) {
            Component c2 = this.components.get(i2);
            ByteBuf s2 = c2.buf;
            int adjustment = c2.offset;
            int localLength = Math.min(length, s2.capacity() - (index - adjustment));
            s2.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
            length -= localLength;
            ++i2;
        }
        dst.writerIndex(dst.capacity());
    }

    public ByteBuf component(int cIndex) {
        return this.internalComponent(cIndex).duplicate();
    }

    public ByteBuf componentAtOffset(int offset) {
        return this.internalComponentAtOffset(offset).duplicate();
    }

    public ByteBuf internalComponent(int cIndex) {
        this.checkComponentIndex(cIndex);
        return this.components.get((int)cIndex).buf;
    }

    public ByteBuf internalComponentAtOffset(int offset) {
        return this.findComponent((int)offset).buf;
    }

    private Component findComponent(int offset) {
        this.checkIndex(offset);
        int low = 0;
        int high = this.components.size();
        while (low <= high) {
            int mid = low + high >>> 1;
            Component c2 = this.components.get(mid);
            if (offset >= c2.endOffset) {
                low = mid + 1;
                continue;
            }
            if (offset < c2.offset) {
                high = mid - 1;
                continue;
            }
            return c2;
        }
        throw new Error("should not reach here");
    }

    @Override
    public int nioBufferCount() {
        if (this.components.size() == 1) {
            return this.components.get((int)0).buf.nioBufferCount();
        }
        int count = 0;
        int componentsCount = this.components.size();
        for (int i2 = 0; i2 < componentsCount; ++i2) {
            Component c2 = this.components.get(i2);
            count += c2.buf.nioBufferCount();
        }
        return count;
    }

    @Override
    public ByteBuffer internalNioBuffer(int index, int length) {
        if (this.components.size() == 1) {
            return this.components.get((int)0).buf.internalNioBuffer(index, length);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteBuffer nioBuffer(int index, int length) {
        ByteBuf buf;
        if (this.components.size() == 1 && (buf = this.components.get((int)0).buf).nioBufferCount() == 1) {
            return this.components.get((int)0).buf.nioBuffer(index, length);
        }
        ByteBuffer merged = ByteBuffer.allocate(length).order(this.order());
        ByteBuffer[] buffers = this.nioBuffers(index, length);
        for (int i2 = 0; i2 < buffers.length; ++i2) {
            merged.put(buffers[i2]);
        }
        merged.flip();
        return merged;
    }

    @Override
    public ByteBuffer[] nioBuffers(int index, int length) {
        this.checkIndex(index, length);
        if (length == 0) {
            return EmptyArrays.EMPTY_BYTE_BUFFERS;
        }
        ArrayList<ByteBuffer> buffers = new ArrayList<ByteBuffer>(this.components.size());
        int i2 = this.toComponentIndex(index);
        while (length > 0) {
            Component c2 = this.components.get(i2);
            ByteBuf s2 = c2.buf;
            int adjustment = c2.offset;
            int localLength = Math.min(length, s2.capacity() - (index - adjustment));
            switch (s2.nioBufferCount()) {
                case 0: {
                    throw new UnsupportedOperationException();
                }
                case 1: {
                    buffers.add(s2.nioBuffer(index - adjustment, localLength));
                    break;
                }
                default: {
                    Collections.addAll(buffers, s2.nioBuffers(index - adjustment, localLength));
                }
            }
            index += localLength;
            length -= localLength;
            ++i2;
        }
        return buffers.toArray(new ByteBuffer[buffers.size()]);
    }

    public CompositeByteBuf consolidate() {
        this.ensureAccessible();
        int numComponents = this.numComponents();
        if (numComponents <= 1) {
            return this;
        }
        Component last = this.components.get(numComponents - 1);
        int capacity = last.endOffset;
        ByteBuf consolidated = this.allocBuffer(capacity);
        for (int i2 = 0; i2 < numComponents; ++i2) {
            Component c2 = this.components.get(i2);
            ByteBuf b2 = c2.buf;
            consolidated.writeBytes(b2);
            c2.freeIfNecessary();
        }
        this.components.clear();
        this.components.add(new Component(consolidated));
        this.updateComponentOffsets(0);
        return this;
    }

    public CompositeByteBuf consolidate(int cIndex, int numComponents) {
        this.checkComponentIndex(cIndex, numComponents);
        if (numComponents <= 1) {
            return this;
        }
        int endCIndex = cIndex + numComponents;
        Component last = this.components.get(endCIndex - 1);
        int capacity = last.endOffset - this.components.get((int)cIndex).offset;
        ByteBuf consolidated = this.allocBuffer(capacity);
        for (int i2 = cIndex; i2 < endCIndex; ++i2) {
            Component c2 = this.components.get(i2);
            ByteBuf b2 = c2.buf;
            consolidated.writeBytes(b2);
            c2.freeIfNecessary();
        }
        this.components.subList(cIndex + 1, endCIndex).clear();
        this.components.set(cIndex, new Component(consolidated));
        this.updateComponentOffsets(cIndex);
        return this;
    }

    public CompositeByteBuf discardReadComponents() {
        this.ensureAccessible();
        int readerIndex = this.readerIndex();
        if (readerIndex == 0) {
            return this;
        }
        int writerIndex = this.writerIndex();
        if (readerIndex == writerIndex && writerIndex == this.capacity()) {
            for (Component c2 : this.components) {
                c2.freeIfNecessary();
            }
            this.components.clear();
            this.setIndex(0, 0);
            this.adjustMarkers(readerIndex);
            return this;
        }
        int firstComponentId = this.toComponentIndex(readerIndex);
        for (int i2 = 0; i2 < firstComponentId; ++i2) {
            this.components.get(i2).freeIfNecessary();
        }
        this.components.subList(0, firstComponentId).clear();
        Component first = this.components.get(0);
        int offset = first.offset;
        this.updateComponentOffsets(0);
        this.setIndex(readerIndex - offset, writerIndex - offset);
        this.adjustMarkers(offset);
        return this;
    }

    @Override
    public CompositeByteBuf discardReadBytes() {
        this.ensureAccessible();
        int readerIndex = this.readerIndex();
        if (readerIndex == 0) {
            return this;
        }
        int writerIndex = this.writerIndex();
        if (readerIndex == writerIndex && writerIndex == this.capacity()) {
            for (Component c2 : this.components) {
                c2.freeIfNecessary();
            }
            this.components.clear();
            this.setIndex(0, 0);
            this.adjustMarkers(readerIndex);
            return this;
        }
        int firstComponentId = this.toComponentIndex(readerIndex);
        for (int i2 = 0; i2 < firstComponentId; ++i2) {
            this.components.get(i2).freeIfNecessary();
        }
        this.components.subList(0, firstComponentId).clear();
        Component c3 = this.components.get(0);
        int adjustment = readerIndex - c3.offset;
        if (adjustment == c3.length) {
            this.components.remove(0);
        } else {
            Component newC = new Component(c3.buf.slice(adjustment, c3.length - adjustment));
            this.components.set(0, newC);
        }
        this.updateComponentOffsets(0);
        this.setIndex(0, writerIndex - readerIndex);
        this.adjustMarkers(readerIndex);
        return this;
    }

    private ByteBuf allocBuffer(int capacity) {
        if (this.direct) {
            return this.alloc().directBuffer(capacity);
        }
        return this.alloc().heapBuffer(capacity);
    }

    @Override
    public String toString() {
        String result = super.toString();
        result = result.substring(0, result.length() - 1);
        return result + ", components=" + this.components.size() + ')';
    }

    @Override
    public CompositeByteBuf readerIndex(int readerIndex) {
        return (CompositeByteBuf)super.readerIndex(readerIndex);
    }

    @Override
    public CompositeByteBuf writerIndex(int writerIndex) {
        return (CompositeByteBuf)super.writerIndex(writerIndex);
    }

    @Override
    public CompositeByteBuf setIndex(int readerIndex, int writerIndex) {
        return (CompositeByteBuf)super.setIndex(readerIndex, writerIndex);
    }

    @Override
    public CompositeByteBuf clear() {
        return (CompositeByteBuf)super.clear();
    }

    @Override
    public CompositeByteBuf markReaderIndex() {
        return (CompositeByteBuf)super.markReaderIndex();
    }

    @Override
    public CompositeByteBuf resetReaderIndex() {
        return (CompositeByteBuf)super.resetReaderIndex();
    }

    @Override
    public CompositeByteBuf markWriterIndex() {
        return (CompositeByteBuf)super.markWriterIndex();
    }

    @Override
    public CompositeByteBuf resetWriterIndex() {
        return (CompositeByteBuf)super.resetWriterIndex();
    }

    @Override
    public CompositeByteBuf ensureWritable(int minWritableBytes) {
        return (CompositeByteBuf)super.ensureWritable(minWritableBytes);
    }

    @Override
    public CompositeByteBuf getBytes(int index, ByteBuf dst) {
        return (CompositeByteBuf)super.getBytes(index, dst);
    }

    @Override
    public CompositeByteBuf getBytes(int index, ByteBuf dst, int length) {
        return (CompositeByteBuf)super.getBytes(index, dst, length);
    }

    @Override
    public CompositeByteBuf getBytes(int index, byte[] dst) {
        return (CompositeByteBuf)super.getBytes(index, dst);
    }

    @Override
    public CompositeByteBuf setBoolean(int index, boolean value) {
        return (CompositeByteBuf)super.setBoolean(index, value);
    }

    @Override
    public CompositeByteBuf setChar(int index, int value) {
        return (CompositeByteBuf)super.setChar(index, value);
    }

    @Override
    public CompositeByteBuf setFloat(int index, float value) {
        return (CompositeByteBuf)super.setFloat(index, value);
    }

    @Override
    public CompositeByteBuf setDouble(int index, double value) {
        return (CompositeByteBuf)super.setDouble(index, value);
    }

    @Override
    public CompositeByteBuf setBytes(int index, ByteBuf src) {
        return (CompositeByteBuf)super.setBytes(index, src);
    }

    @Override
    public CompositeByteBuf setBytes(int index, ByteBuf src, int length) {
        return (CompositeByteBuf)super.setBytes(index, src, length);
    }

    @Override
    public CompositeByteBuf setBytes(int index, byte[] src) {
        return (CompositeByteBuf)super.setBytes(index, src);
    }

    @Override
    public CompositeByteBuf setZero(int index, int length) {
        return (CompositeByteBuf)super.setZero(index, length);
    }

    @Override
    public CompositeByteBuf readBytes(ByteBuf dst) {
        return (CompositeByteBuf)super.readBytes(dst);
    }

    @Override
    public CompositeByteBuf readBytes(ByteBuf dst, int length) {
        return (CompositeByteBuf)super.readBytes(dst, length);
    }

    @Override
    public CompositeByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        return (CompositeByteBuf)super.readBytes(dst, dstIndex, length);
    }

    @Override
    public CompositeByteBuf readBytes(byte[] dst) {
        return (CompositeByteBuf)super.readBytes(dst);
    }

    @Override
    public CompositeByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        return (CompositeByteBuf)super.readBytes(dst, dstIndex, length);
    }

    @Override
    public CompositeByteBuf readBytes(ByteBuffer dst) {
        return (CompositeByteBuf)super.readBytes(dst);
    }

    @Override
    public CompositeByteBuf readBytes(OutputStream out, int length) throws IOException {
        return (CompositeByteBuf)super.readBytes(out, length);
    }

    @Override
    public CompositeByteBuf skipBytes(int length) {
        return (CompositeByteBuf)super.skipBytes(length);
    }

    @Override
    public CompositeByteBuf writeBoolean(boolean value) {
        return (CompositeByteBuf)super.writeBoolean(value);
    }

    @Override
    public CompositeByteBuf writeByte(int value) {
        return (CompositeByteBuf)super.writeByte(value);
    }

    @Override
    public CompositeByteBuf writeShort(int value) {
        return (CompositeByteBuf)super.writeShort(value);
    }

    @Override
    public CompositeByteBuf writeMedium(int value) {
        return (CompositeByteBuf)super.writeMedium(value);
    }

    @Override
    public CompositeByteBuf writeInt(int value) {
        return (CompositeByteBuf)super.writeInt(value);
    }

    @Override
    public CompositeByteBuf writeLong(long value) {
        return (CompositeByteBuf)super.writeLong(value);
    }

    @Override
    public CompositeByteBuf writeChar(int value) {
        return (CompositeByteBuf)super.writeChar(value);
    }

    @Override
    public CompositeByteBuf writeFloat(float value) {
        return (CompositeByteBuf)super.writeFloat(value);
    }

    @Override
    public CompositeByteBuf writeDouble(double value) {
        return (CompositeByteBuf)super.writeDouble(value);
    }

    @Override
    public CompositeByteBuf writeBytes(ByteBuf src) {
        return (CompositeByteBuf)super.writeBytes(src);
    }

    @Override
    public CompositeByteBuf writeBytes(ByteBuf src, int length) {
        return (CompositeByteBuf)super.writeBytes(src, length);
    }

    @Override
    public CompositeByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        return (CompositeByteBuf)super.writeBytes(src, srcIndex, length);
    }

    @Override
    public CompositeByteBuf writeBytes(byte[] src) {
        return (CompositeByteBuf)super.writeBytes(src);
    }

    @Override
    public CompositeByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        return (CompositeByteBuf)super.writeBytes(src, srcIndex, length);
    }

    @Override
    public CompositeByteBuf writeBytes(ByteBuffer src) {
        return (CompositeByteBuf)super.writeBytes(src);
    }

    @Override
    public CompositeByteBuf writeZero(int length) {
        return (CompositeByteBuf)super.writeZero(length);
    }

    @Override
    public CompositeByteBuf retain(int increment) {
        return (CompositeByteBuf)super.retain(increment);
    }

    @Override
    public CompositeByteBuf retain() {
        return (CompositeByteBuf)super.retain();
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return this.nioBuffers(this.readerIndex(), this.readableBytes());
    }

    @Override
    public CompositeByteBuf discardSomeReadBytes() {
        return this.discardReadComponents();
    }

    @Override
    protected void deallocate() {
        if (this.freed) {
            return;
        }
        this.freed = true;
        int size = this.components.size();
        for (int i2 = 0; i2 < size; ++i2) {
            this.components.get(i2).freeIfNecessary();
        }
        if (this.leak != null) {
            this.leak.close();
        }
    }

    @Override
    public ByteBuf unwrap() {
        return null;
    }

    private static final class Component {
        final ByteBuf buf;
        final int length;
        int offset;
        int endOffset;

        Component(ByteBuf buf) {
            this.buf = buf;
            this.length = buf.readableBytes();
        }

        void freeIfNecessary() {
            this.buf.release();
        }
    }
}

