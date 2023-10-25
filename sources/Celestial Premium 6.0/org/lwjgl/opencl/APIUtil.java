/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.PointerWrapper;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CLCommandQueue;
import org.lwjgl.opencl.CLContext;
import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opencl.CLEvent;
import org.lwjgl.opencl.CLKernel;
import org.lwjgl.opencl.CLMem;
import org.lwjgl.opencl.CLObjectChild;
import org.lwjgl.opencl.CLObjectRegistry;
import org.lwjgl.opencl.CLPlatform;
import org.lwjgl.opencl.CLProgram;
import org.lwjgl.opencl.CLSampler;
import org.lwjgl.opencl.EXTDeviceFission;
import org.lwjgl.opencl.FastLongMap;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
final class APIUtil {
    private static final int INITIAL_BUFFER_SIZE = 256;
    private static final int INITIAL_LENGTHS_SIZE = 4;
    private static final int BUFFERS_SIZE = 32;
    private static final ThreadLocal<char[]> arrayTL = new ThreadLocal<char[]>(){

        @Override
        protected char[] initialValue() {
            return new char[256];
        }
    };
    private static final ThreadLocal<ByteBuffer> bufferByteTL = new ThreadLocal<ByteBuffer>(){

        @Override
        protected ByteBuffer initialValue() {
            return BufferUtils.createByteBuffer(256);
        }
    };
    private static final ThreadLocal<PointerBuffer> bufferPointerTL = new ThreadLocal<PointerBuffer>(){

        @Override
        protected PointerBuffer initialValue() {
            return BufferUtils.createPointerBuffer(256);
        }
    };
    private static final ThreadLocal<PointerBuffer> lengthsTL = new ThreadLocal<PointerBuffer>(){

        @Override
        protected PointerBuffer initialValue() {
            return BufferUtils.createPointerBuffer(4);
        }
    };
    private static final ThreadLocal<Buffers> buffersTL = new ThreadLocal<Buffers>(){

        @Override
        protected Buffers initialValue() {
            return new Buffers();
        }
    };
    private static final ObjectDestructor<CLDevice> DESTRUCTOR_CLSubDevice = new ObjectDestructor<CLDevice>(){

        @Override
        public void release(CLDevice object) {
            EXTDeviceFission.clReleaseDeviceEXT(object);
        }
    };
    private static final ObjectDestructor<CLMem> DESTRUCTOR_CLMem = new ObjectDestructor<CLMem>(){

        @Override
        public void release(CLMem object) {
            CL10.clReleaseMemObject(object);
        }
    };
    private static final ObjectDestructor<CLCommandQueue> DESTRUCTOR_CLCommandQueue = new ObjectDestructor<CLCommandQueue>(){

        @Override
        public void release(CLCommandQueue object) {
            CL10.clReleaseCommandQueue(object);
        }
    };
    private static final ObjectDestructor<CLSampler> DESTRUCTOR_CLSampler = new ObjectDestructor<CLSampler>(){

        @Override
        public void release(CLSampler object) {
            CL10.clReleaseSampler(object);
        }
    };
    private static final ObjectDestructor<CLProgram> DESTRUCTOR_CLProgram = new ObjectDestructor<CLProgram>(){

        @Override
        public void release(CLProgram object) {
            CL10.clReleaseProgram(object);
        }
    };
    private static final ObjectDestructor<CLKernel> DESTRUCTOR_CLKernel = new ObjectDestructor<CLKernel>(){

        @Override
        public void release(CLKernel object) {
            CL10.clReleaseKernel(object);
        }
    };
    private static final ObjectDestructor<CLEvent> DESTRUCTOR_CLEvent = new ObjectDestructor<CLEvent>(){

        @Override
        public void release(CLEvent object) {
            CL10.clReleaseEvent(object);
        }
    };

    private APIUtil() {
    }

    private static char[] getArray(int size) {
        char[] array = arrayTL.get();
        if (array.length < size) {
            for (int sizeNew = array.length << 1; sizeNew < size; sizeNew <<= 1) {
            }
            array = new char[size];
            arrayTL.set(array);
        }
        return array;
    }

    static ByteBuffer getBufferByte(int size) {
        ByteBuffer buffer = bufferByteTL.get();
        if (buffer.capacity() < size) {
            for (int sizeNew = buffer.capacity() << 1; sizeNew < size; sizeNew <<= 1) {
            }
            buffer = BufferUtils.createByteBuffer(size);
            bufferByteTL.set(buffer);
        } else {
            buffer.clear();
        }
        return buffer;
    }

    private static ByteBuffer getBufferByteOffset(int size) {
        ByteBuffer buffer = bufferByteTL.get();
        if (buffer.capacity() < size) {
            for (int sizeNew = buffer.capacity() << 1; sizeNew < size; sizeNew <<= 1) {
            }
            ByteBuffer bufferNew = BufferUtils.createByteBuffer(size);
            bufferNew.put(buffer);
            buffer = bufferNew;
            bufferByteTL.set(buffer);
        } else {
            buffer.position(buffer.limit());
            buffer.limit(buffer.capacity());
        }
        return buffer;
    }

    static PointerBuffer getBufferPointer(int size) {
        PointerBuffer buffer = bufferPointerTL.get();
        if (buffer.capacity() < size) {
            for (int sizeNew = buffer.capacity() << 1; sizeNew < size; sizeNew <<= 1) {
            }
            buffer = BufferUtils.createPointerBuffer(size);
            bufferPointerTL.set(buffer);
        } else {
            buffer.clear();
        }
        return buffer;
    }

    static ShortBuffer getBufferShort() {
        return APIUtil.buffersTL.get().shorts;
    }

    static IntBuffer getBufferInt() {
        return APIUtil.buffersTL.get().ints;
    }

    static IntBuffer getBufferIntDebug() {
        return APIUtil.buffersTL.get().intsDebug;
    }

    static LongBuffer getBufferLong() {
        return APIUtil.buffersTL.get().longs;
    }

    static FloatBuffer getBufferFloat() {
        return APIUtil.buffersTL.get().floats;
    }

    static DoubleBuffer getBufferDouble() {
        return APIUtil.buffersTL.get().doubles;
    }

    static PointerBuffer getBufferPointer() {
        return APIUtil.buffersTL.get().pointers;
    }

    static PointerBuffer getLengths() {
        return APIUtil.getLengths(1);
    }

    static PointerBuffer getLengths(int size) {
        PointerBuffer lengths = lengthsTL.get();
        if (lengths.capacity() < size) {
            for (int sizeNew = lengths.capacity(); sizeNew < size; sizeNew <<= 1) {
            }
            lengths = BufferUtils.createPointerBuffer(size);
            lengthsTL.set(lengths);
        } else {
            lengths.clear();
        }
        return lengths;
    }

    private static ByteBuffer encode(ByteBuffer buffer, CharSequence string) {
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (LWJGLUtil.DEBUG && '\u0080' <= c) {
                buffer.put((byte)26);
                continue;
            }
            buffer.put((byte)c);
        }
        return buffer;
    }

    static String getString(ByteBuffer buffer) {
        int length = buffer.remaining();
        char[] charArray = APIUtil.getArray(length);
        for (int i = buffer.position(); i < buffer.limit(); ++i) {
            charArray[i - buffer.position()] = (char)buffer.get(i);
        }
        return new String(charArray, 0, length);
    }

    static long getBuffer(CharSequence string) {
        ByteBuffer buffer = APIUtil.encode(APIUtil.getBufferByte(string.length()), string);
        buffer.flip();
        return MemoryUtil.getAddress0(buffer);
    }

    static long getBuffer(CharSequence string, int offset) {
        ByteBuffer buffer = APIUtil.encode(APIUtil.getBufferByteOffset(offset + string.length()), string);
        buffer.flip();
        return MemoryUtil.getAddress(buffer);
    }

    static long getBufferNT(CharSequence string) {
        ByteBuffer buffer = APIUtil.encode(APIUtil.getBufferByte(string.length() + 1), string);
        buffer.put((byte)0);
        buffer.flip();
        return MemoryUtil.getAddress0(buffer);
    }

    static int getTotalLength(CharSequence[] strings) {
        int length = 0;
        for (CharSequence string : strings) {
            length += string.length();
        }
        return length;
    }

    static long getBuffer(CharSequence[] strings) {
        ByteBuffer buffer = APIUtil.getBufferByte(APIUtil.getTotalLength(strings));
        for (CharSequence string : strings) {
            APIUtil.encode(buffer, string);
        }
        buffer.flip();
        return MemoryUtil.getAddress0(buffer);
    }

    static long getBufferNT(CharSequence[] strings) {
        ByteBuffer buffer = APIUtil.getBufferByte(APIUtil.getTotalLength(strings) + strings.length);
        for (CharSequence string : strings) {
            APIUtil.encode(buffer, string);
            buffer.put((byte)0);
        }
        buffer.flip();
        return MemoryUtil.getAddress0(buffer);
    }

    static long getLengths(CharSequence[] strings) {
        PointerBuffer buffer = APIUtil.getLengths(strings.length);
        for (CharSequence string : strings) {
            buffer.put(string.length());
        }
        buffer.flip();
        return MemoryUtil.getAddress0(buffer);
    }

    static long getLengths(ByteBuffer[] buffers) {
        PointerBuffer lengths = APIUtil.getLengths(buffers.length);
        for (ByteBuffer buffer : buffers) {
            lengths.put(buffer.remaining());
        }
        lengths.flip();
        return MemoryUtil.getAddress0(lengths);
    }

    static int getSize(PointerBuffer lengths) {
        long size = 0L;
        for (int i = lengths.position(); i < lengths.limit(); ++i) {
            size += lengths.get(i);
        }
        return (int)size;
    }

    static long getPointer(PointerWrapper pointer) {
        return MemoryUtil.getAddress0(APIUtil.getBufferPointer().put(0, pointer));
    }

    static long getPointerSafe(PointerWrapper pointer) {
        return MemoryUtil.getAddress0(APIUtil.getBufferPointer().put(0, pointer == null ? 0L : pointer.getPointer()));
    }

    static Set<String> getExtensions(String extensionList) {
        HashSet<String> extensions = new HashSet<String>();
        if (extensionList != null) {
            StringTokenizer tokenizer = new StringTokenizer(extensionList);
            while (tokenizer.hasMoreTokens()) {
                extensions.add(tokenizer.nextToken());
            }
        }
        return extensions;
    }

    static boolean isDevicesParam(int param_name) {
        switch (param_name) {
            case 4225: 
            case 8198: 
            case 8199: 
            case 0x10000002: 
            case 0x10000003: {
                return true;
            }
        }
        return false;
    }

    static CLPlatform getCLPlatform(PointerBuffer properties) {
        long key;
        long platformID = 0L;
        int keys = properties.remaining() / 2;
        for (int k = 0; k < keys && (key = properties.get(k << 1)) != 0L; ++k) {
            if (key != 4228L) continue;
            platformID = properties.get((k << 1) + 1);
            break;
        }
        if (platformID == 0L) {
            throw new IllegalArgumentException("Could not find CL_CONTEXT_PLATFORM in cl_context_properties.");
        }
        CLPlatform platform = CLPlatform.getCLPlatform(platformID);
        if (platform == null) {
            throw new IllegalStateException("Could not find a valid CLPlatform. Make sure clGetPlatformIDs has been used before.");
        }
        return platform;
    }

    static ByteBuffer getNativeKernelArgs(long user_func_ref, CLMem[] clMems, long[] sizes) {
        ByteBuffer args = APIUtil.getBufferByte(12 + (clMems == null ? 0 : clMems.length * (4 + PointerBuffer.getPointerSize())));
        args.putLong(0, user_func_ref);
        if (clMems == null) {
            args.putInt(8, 0);
        } else {
            args.putInt(8, clMems.length);
            int byteIndex = 12;
            for (int i = 0; i < clMems.length; ++i) {
                if (LWJGLUtil.DEBUG && !clMems[i].isValid()) {
                    throw new IllegalArgumentException("An invalid CLMem object was specified.");
                }
                args.putInt(byteIndex, (int)sizes[i]);
                byteIndex += 4 + PointerBuffer.getPointerSize();
            }
        }
        return args;
    }

    static void releaseObjects(CLDevice device) {
        if (!device.isValid() || device.getReferenceCount() > 1) {
            return;
        }
        APIUtil.releaseObjects(device.getSubCLDeviceRegistry(), DESTRUCTOR_CLSubDevice);
    }

    static void releaseObjects(CLContext context) {
        if (!context.isValid() || context.getReferenceCount() > 1) {
            return;
        }
        APIUtil.releaseObjects(context.getCLEventRegistry(), DESTRUCTOR_CLEvent);
        APIUtil.releaseObjects(context.getCLProgramRegistry(), DESTRUCTOR_CLProgram);
        APIUtil.releaseObjects(context.getCLSamplerRegistry(), DESTRUCTOR_CLSampler);
        APIUtil.releaseObjects(context.getCLMemRegistry(), DESTRUCTOR_CLMem);
        APIUtil.releaseObjects(context.getCLCommandQueueRegistry(), DESTRUCTOR_CLCommandQueue);
    }

    static void releaseObjects(CLProgram program) {
        if (!program.isValid() || program.getReferenceCount() > 1) {
            return;
        }
        APIUtil.releaseObjects(program.getCLKernelRegistry(), DESTRUCTOR_CLKernel);
    }

    static void releaseObjects(CLCommandQueue queue) {
        if (!queue.isValid() || queue.getReferenceCount() > 1) {
            return;
        }
        APIUtil.releaseObjects(queue.getCLEventRegistry(), DESTRUCTOR_CLEvent);
    }

    private static <T extends CLObjectChild> void releaseObjects(CLObjectRegistry<T> registry, ObjectDestructor<T> destructor) {
        if (registry.isEmpty()) {
            return;
        }
        for (FastLongMap.Entry<T> entry : registry.getAll()) {
            CLObjectChild object = (CLObjectChild)entry.value;
            while (object.isValid()) {
                destructor.release(object);
            }
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static interface ObjectDestructor<T extends CLObjectChild> {
        public void release(T var1);
    }

    private static class Buffers {
        final ShortBuffer shorts = BufferUtils.createShortBuffer(32);
        final IntBuffer ints = BufferUtils.createIntBuffer(32);
        final IntBuffer intsDebug = BufferUtils.createIntBuffer(1);
        final LongBuffer longs = BufferUtils.createLongBuffer(32);
        final FloatBuffer floats = BufferUtils.createFloatBuffer(32);
        final DoubleBuffer doubles = BufferUtils.createDoubleBuffer(32);
        final PointerBuffer pointers = BufferUtils.createPointerBuffer(32);

        Buffers() {
        }
    }
}

