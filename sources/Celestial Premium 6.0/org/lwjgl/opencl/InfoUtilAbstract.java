/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.PointerWrapperAbstract;
import org.lwjgl.opencl.APIUtil;
import org.lwjgl.opencl.CLObject;
import org.lwjgl.opencl.InfoUtil;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
abstract class InfoUtilAbstract<T extends CLObject>
implements InfoUtil<T> {
    protected InfoUtilAbstract() {
    }

    protected abstract int getInfo(T var1, int var2, ByteBuffer var3, PointerBuffer var4);

    protected int getInfoSizeArraySize(T object, int param_name) {
        throw new UnsupportedOperationException();
    }

    protected PointerBuffer getSizesBuffer(T object, int param_name) {
        int size = this.getInfoSizeArraySize(object, param_name);
        PointerBuffer buffer = APIUtil.getBufferPointer(size);
        buffer.limit(size);
        this.getInfo(object, param_name, buffer.getBuffer(), null);
        return buffer;
    }

    @Override
    public int getInfoInt(T object, int param_name) {
        ((PointerWrapperAbstract)object).checkValid();
        ByteBuffer buffer = APIUtil.getBufferByte(4);
        this.getInfo(object, param_name, buffer, null);
        return buffer.getInt(0);
    }

    @Override
    public long getInfoSize(T object, int param_name) {
        ((PointerWrapperAbstract)object).checkValid();
        PointerBuffer buffer = APIUtil.getBufferPointer();
        this.getInfo(object, param_name, buffer.getBuffer(), null);
        return buffer.get(0);
    }

    @Override
    public long[] getInfoSizeArray(T object, int param_name) {
        ((PointerWrapperAbstract)object).checkValid();
        int size = this.getInfoSizeArraySize(object, param_name);
        PointerBuffer buffer = APIUtil.getBufferPointer(size);
        this.getInfo(object, param_name, buffer.getBuffer(), null);
        long[] array = new long[size];
        for (int i = 0; i < size; ++i) {
            array[i] = buffer.get(i);
        }
        return array;
    }

    @Override
    public long getInfoLong(T object, int param_name) {
        ((PointerWrapperAbstract)object).checkValid();
        ByteBuffer buffer = APIUtil.getBufferByte(8);
        this.getInfo(object, param_name, buffer, null);
        return buffer.getLong(0);
    }

    @Override
    public String getInfoString(T object, int param_name) {
        ((PointerWrapperAbstract)object).checkValid();
        int bytes = this.getSizeRet(object, param_name);
        if (bytes <= 1) {
            return null;
        }
        ByteBuffer buffer = APIUtil.getBufferByte(bytes);
        this.getInfo(object, param_name, buffer, null);
        buffer.limit(bytes - 1);
        return APIUtil.getString(buffer);
    }

    protected final int getSizeRet(T object, int param_name) {
        PointerBuffer bytes = APIUtil.getBufferPointer();
        int errcode = this.getInfo(object, param_name, null, bytes);
        if (errcode != 0) {
            throw new IllegalArgumentException("Invalid parameter specified: " + LWJGLUtil.toHexString(param_name));
        }
        return (int)bytes.get(0);
    }
}

