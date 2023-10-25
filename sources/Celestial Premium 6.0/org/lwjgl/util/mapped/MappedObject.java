/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.mapped;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.util.mapped.MappedForeach;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class MappedObject {
    static final boolean CHECKS = LWJGLUtil.getPrivilegedBoolean("org.lwjgl.util.mapped.Checks");
    public long baseAddress;
    public long viewAddress;
    ByteBuffer preventGC;
    public static int SIZEOF = -1;
    public int view;

    protected MappedObject() {
    }

    protected final long getViewAddress(int view) {
        throw new InternalError("type not registered");
    }

    public final void setViewAddress(long address) {
        if (CHECKS) {
            this.checkAddress(address);
        }
        this.viewAddress = address;
    }

    final void checkAddress(long address) {
        long base = MemoryUtil.getAddress0(this.preventGC);
        int offset = (int)(address - base);
        if (address < base || this.preventGC.capacity() < offset + this.getSizeof()) {
            throw new IndexOutOfBoundsException(Integer.toString(offset / this.getSizeof()));
        }
    }

    final void checkRange(int bytes) {
        if (bytes < 0) {
            throw new IllegalArgumentException();
        }
        if ((long)this.preventGC.capacity() < this.viewAddress - MemoryUtil.getAddress0(this.preventGC) + (long)bytes) {
            throw new BufferOverflowException();
        }
    }

    public final int getAlign() {
        throw new InternalError("type not registered");
    }

    public final int getSizeof() {
        throw new InternalError("type not registered");
    }

    public final int capacity() {
        throw new InternalError("type not registered");
    }

    public static <T extends MappedObject> T map(ByteBuffer bb) {
        throw new InternalError("type not registered");
    }

    public static <T extends MappedObject> T map(long address, int capacity) {
        throw new InternalError("type not registered");
    }

    public static <T extends MappedObject> T malloc(int elementCount) {
        throw new InternalError("type not registered");
    }

    public final <T extends MappedObject> T dup() {
        throw new InternalError("type not registered");
    }

    public final <T extends MappedObject> T slice() {
        throw new InternalError("type not registered");
    }

    public final void runViewConstructor() {
        throw new InternalError("type not registered");
    }

    public final void next() {
        throw new InternalError("type not registered");
    }

    public final <T extends MappedObject> void copyTo(T target) {
        throw new InternalError("type not registered");
    }

    public final <T extends MappedObject> void copyRange(T target, int instances) {
        throw new InternalError("type not registered");
    }

    public static <T extends MappedObject> Iterable<T> foreach(T mapped) {
        return MappedObject.foreach(mapped, mapped.capacity());
    }

    public static <T extends MappedObject> Iterable<T> foreach(T mapped, int elementCount) {
        return new MappedForeach<T>(mapped, elementCount);
    }

    public final <T extends MappedObject> T[] asArray() {
        throw new InternalError("type not registered");
    }

    public final ByteBuffer backingByteBuffer() {
        return this.preventGC;
    }
}

