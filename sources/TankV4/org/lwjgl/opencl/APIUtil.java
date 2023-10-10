package org.lwjgl.opencl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.PointerWrapper;

final class APIUtil {
   private static final int INITIAL_BUFFER_SIZE = 256;
   private static final int INITIAL_LENGTHS_SIZE = 4;
   private static final int BUFFERS_SIZE = 32;
   private static final ThreadLocal arrayTL = new ThreadLocal() {
      protected char[] initialValue() {
         return new char[256];
      }

      protected Object initialValue() {
         return this.initialValue();
      }
   };
   private static final ThreadLocal bufferByteTL = new ThreadLocal() {
      protected ByteBuffer initialValue() {
         return BufferUtils.createByteBuffer(256);
      }

      protected Object initialValue() {
         return this.initialValue();
      }
   };
   private static final ThreadLocal bufferPointerTL = new ThreadLocal() {
      protected PointerBuffer initialValue() {
         return BufferUtils.createPointerBuffer(256);
      }

      protected Object initialValue() {
         return this.initialValue();
      }
   };
   private static final ThreadLocal lengthsTL = new ThreadLocal() {
      protected PointerBuffer initialValue() {
         return BufferUtils.createPointerBuffer(4);
      }

      protected Object initialValue() {
         return this.initialValue();
      }
   };
   private static final ThreadLocal buffersTL = new ThreadLocal() {
      protected APIUtil.Buffers initialValue() {
         return new APIUtil.Buffers();
      }

      protected Object initialValue() {
         return this.initialValue();
      }
   };
   private static final APIUtil.ObjectDestructor DESTRUCTOR_CLSubDevice = new APIUtil.ObjectDestructor() {
      public void release(CLDevice var1) {
         EXTDeviceFission.clReleaseDeviceEXT(var1);
      }

      public void release(CLObjectChild var1) {
         this.release((CLDevice)var1);
      }
   };
   private static final APIUtil.ObjectDestructor DESTRUCTOR_CLMem = new APIUtil.ObjectDestructor() {
      public void release(CLMem var1) {
         CL10.clReleaseMemObject(var1);
      }

      public void release(CLObjectChild var1) {
         this.release((CLMem)var1);
      }
   };
   private static final APIUtil.ObjectDestructor DESTRUCTOR_CLCommandQueue = new APIUtil.ObjectDestructor() {
      public void release(CLCommandQueue var1) {
         CL10.clReleaseCommandQueue(var1);
      }

      public void release(CLObjectChild var1) {
         this.release((CLCommandQueue)var1);
      }
   };
   private static final APIUtil.ObjectDestructor DESTRUCTOR_CLSampler = new APIUtil.ObjectDestructor() {
      public void release(CLSampler var1) {
         CL10.clReleaseSampler(var1);
      }

      public void release(CLObjectChild var1) {
         this.release((CLSampler)var1);
      }
   };
   private static final APIUtil.ObjectDestructor DESTRUCTOR_CLProgram = new APIUtil.ObjectDestructor() {
      public void release(CLProgram var1) {
         CL10.clReleaseProgram(var1);
      }

      public void release(CLObjectChild var1) {
         this.release((CLProgram)var1);
      }
   };
   private static final APIUtil.ObjectDestructor DESTRUCTOR_CLKernel = new APIUtil.ObjectDestructor() {
      public void release(CLKernel var1) {
         CL10.clReleaseKernel(var1);
      }

      public void release(CLObjectChild var1) {
         this.release((CLKernel)var1);
      }
   };
   private static final APIUtil.ObjectDestructor DESTRUCTOR_CLEvent = new APIUtil.ObjectDestructor() {
      public void release(CLEvent var1) {
         CL10.clReleaseEvent(var1);
      }

      public void release(CLObjectChild var1) {
         this.release((CLEvent)var1);
      }
   };

   private APIUtil() {
   }

   private static char[] getArray(int var0) {
      char[] var1 = (char[])arrayTL.get();
      if (var1.length < var0) {
         for(int var2 = var1.length << 1; var2 < var0; var2 <<= 1) {
         }

         var1 = new char[var0];
         arrayTL.set(var1);
      }

      return var1;
   }

   static ByteBuffer getBufferByte(int var0) {
      ByteBuffer var1 = (ByteBuffer)bufferByteTL.get();
      if (var1.capacity() < var0) {
         for(int var2 = var1.capacity() << 1; var2 < var0; var2 <<= 1) {
         }

         var1 = BufferUtils.createByteBuffer(var0);
         bufferByteTL.set(var1);
      } else {
         var1.clear();
      }

      return var1;
   }

   private static ByteBuffer getBufferByteOffset(int var0) {
      ByteBuffer var1 = (ByteBuffer)bufferByteTL.get();
      if (var1.capacity() < var0) {
         for(int var2 = var1.capacity() << 1; var2 < var0; var2 <<= 1) {
         }

         ByteBuffer var3 = BufferUtils.createByteBuffer(var0);
         var3.put(var1);
         var1 = var3;
         bufferByteTL.set(var3);
      } else {
         var1.position(var1.limit());
         var1.limit(var1.capacity());
      }

      return var1;
   }

   static PointerBuffer getBufferPointer(int var0) {
      PointerBuffer var1 = (PointerBuffer)bufferPointerTL.get();
      if (var1.capacity() < var0) {
         for(int var2 = var1.capacity() << 1; var2 < var0; var2 <<= 1) {
         }

         var1 = BufferUtils.createPointerBuffer(var0);
         bufferPointerTL.set(var1);
      } else {
         var1.clear();
      }

      return var1;
   }

   static ShortBuffer getBufferShort() {
      return ((APIUtil.Buffers)buffersTL.get()).shorts;
   }

   static IntBuffer getBufferInt() {
      return ((APIUtil.Buffers)buffersTL.get()).ints;
   }

   static IntBuffer getBufferIntDebug() {
      return ((APIUtil.Buffers)buffersTL.get()).intsDebug;
   }

   static LongBuffer getBufferLong() {
      return ((APIUtil.Buffers)buffersTL.get()).longs;
   }

   static FloatBuffer getBufferFloat() {
      return ((APIUtil.Buffers)buffersTL.get()).floats;
   }

   static DoubleBuffer getBufferDouble() {
      return ((APIUtil.Buffers)buffersTL.get()).doubles;
   }

   static PointerBuffer getBufferPointer() {
      return ((APIUtil.Buffers)buffersTL.get()).pointers;
   }

   static PointerBuffer getLengths() {
      return getLengths(1);
   }

   static PointerBuffer getLengths(int var0) {
      PointerBuffer var1 = (PointerBuffer)lengthsTL.get();
      if (var1.capacity() < var0) {
         for(int var2 = var1.capacity(); var2 < var0; var2 <<= 1) {
         }

         var1 = BufferUtils.createPointerBuffer(var0);
         lengthsTL.set(var1);
      } else {
         var1.clear();
      }

      return var1;
   }

   private static ByteBuffer encode(ByteBuffer var0, CharSequence var1) {
      for(int var2 = 0; var2 < var1.length(); ++var2) {
         char var3 = var1.charAt(var2);
         if (LWJGLUtil.DEBUG && 128 <= var3) {
            var0.put((byte)26);
         } else {
            var0.put((byte)var3);
         }
      }

      return var0;
   }

   static String getString(ByteBuffer var0) {
      int var1 = var0.remaining();
      char[] var2 = getArray(var1);

      for(int var3 = var0.position(); var3 < var0.limit(); ++var3) {
         var2[var3 - var0.position()] = (char)var0.get(var3);
      }

      return new String(var2, 0, var1);
   }

   static long getBuffer(CharSequence var0) {
      ByteBuffer var1 = encode(getBufferByte(var0.length()), var0);
      var1.flip();
      return MemoryUtil.getAddress0((Buffer)var1);
   }

   static long getBuffer(CharSequence var0, int var1) {
      ByteBuffer var2 = encode(getBufferByteOffset(var1 + var0.length()), var0);
      var2.flip();
      return MemoryUtil.getAddress(var2);
   }

   static long getBufferNT(CharSequence var0) {
      ByteBuffer var1 = encode(getBufferByte(var0.length() + 1), var0);
      var1.put((byte)0);
      var1.flip();
      return MemoryUtil.getAddress0((Buffer)var1);
   }

   static int getTotalLength(CharSequence[] var0) {
      int var1 = 0;
      CharSequence[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         CharSequence var5 = var2[var4];
         var1 += var5.length();
      }

      return var1;
   }

   static long getBuffer(CharSequence[] var0) {
      ByteBuffer var1 = getBufferByte(getTotalLength(var0));
      CharSequence[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         CharSequence var5 = var2[var4];
         encode(var1, var5);
      }

      var1.flip();
      return MemoryUtil.getAddress0((Buffer)var1);
   }

   static long getBufferNT(CharSequence[] var0) {
      ByteBuffer var1 = getBufferByte(getTotalLength(var0) + var0.length);
      CharSequence[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         CharSequence var5 = var2[var4];
         encode(var1, var5);
         var1.put((byte)0);
      }

      var1.flip();
      return MemoryUtil.getAddress0((Buffer)var1);
   }

   static long getLengths(CharSequence[] var0) {
      PointerBuffer var1 = getLengths(var0.length);
      CharSequence[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         CharSequence var5 = var2[var4];
         var1.put((long)var5.length());
      }

      var1.flip();
      return MemoryUtil.getAddress0(var1);
   }

   static long getLengths(ByteBuffer[] var0) {
      PointerBuffer var1 = getLengths(var0.length);
      ByteBuffer[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ByteBuffer var5 = var2[var4];
         var1.put((long)var5.remaining());
      }

      var1.flip();
      return MemoryUtil.getAddress0(var1);
   }

   static int getSize(PointerBuffer var0) {
      long var1 = 0L;

      for(int var3 = var0.position(); var3 < var0.limit(); ++var3) {
         var1 += var0.get(var3);
      }

      return (int)var1;
   }

   static long getPointer(PointerWrapper var0) {
      return MemoryUtil.getAddress0(getBufferPointer().put(0, var0));
   }

   static long getPointerSafe(PointerWrapper var0) {
      return MemoryUtil.getAddress0(getBufferPointer().put(0, var0 == null ? 0L : var0.getPointer()));
   }

   static Set getExtensions(String var0) {
      HashSet var1 = new HashSet();
      if (var0 != null) {
         StringTokenizer var2 = new StringTokenizer(var0);

         while(var2.hasMoreTokens()) {
            var1.add(var2.nextToken());
         }
      }

      return var1;
   }

   static boolean isDevicesParam(int var0) {
      switch(var0) {
      case 4225:
      case 8198:
      case 8199:
      case 268435458:
      case 268435459:
         return true;
      default:
         return false;
      }
   }

   static CLPlatform getCLPlatform(PointerBuffer var0) {
      long var1 = 0L;
      int var3 = var0.remaining() / 2;

      for(int var4 = 0; var4 < var3; ++var4) {
         long var5 = var0.get(var4 << 1);
         if (var5 == 0L) {
            break;
         }

         if (var5 == 4228L) {
            var1 = var0.get((var4 << 1) + 1);
            break;
         }
      }

      if (var1 == 0L) {
         throw new IllegalArgumentException("Could not find CL_CONTEXT_PLATFORM in cl_context_properties.");
      } else {
         CLPlatform var7 = CLPlatform.getCLPlatform(var1);
         if (var7 == null) {
            throw new IllegalStateException("Could not find a valid CLPlatform. Make sure clGetPlatformIDs has been used before.");
         } else {
            return var7;
         }
      }
   }

   static ByteBuffer getNativeKernelArgs(long var0, CLMem[] var2, long[] var3) {
      ByteBuffer var4 = getBufferByte(12 + (var2 == null ? 0 : var2.length * (4 + PointerBuffer.getPointerSize())));
      var4.putLong(0, var0);
      if (var2 == null) {
         var4.putInt(8, 0);
      } else {
         var4.putInt(8, var2.length);
         int var5 = 12;

         for(int var6 = 0; var6 < var2.length; ++var6) {
            if (LWJGLUtil.DEBUG && !var2[var6].isValid()) {
               throw new IllegalArgumentException("An invalid CLMem object was specified.");
            }

            var4.putInt(var5, (int)var3[var6]);
            var5 += 4 + PointerBuffer.getPointerSize();
         }
      }

      return var4;
   }

   static void releaseObjects(CLDevice var0) {
      if (var0.isValid() && var0.getReferenceCount() <= 1) {
         releaseObjects(var0.getSubCLDeviceRegistry(), DESTRUCTOR_CLSubDevice);
      }
   }

   static void releaseObjects(CLContext var0) {
      if (var0.isValid() && var0.getReferenceCount() <= 1) {
         releaseObjects(var0.getCLEventRegistry(), DESTRUCTOR_CLEvent);
         releaseObjects(var0.getCLProgramRegistry(), DESTRUCTOR_CLProgram);
         releaseObjects(var0.getCLSamplerRegistry(), DESTRUCTOR_CLSampler);
         releaseObjects(var0.getCLMemRegistry(), DESTRUCTOR_CLMem);
         releaseObjects(var0.getCLCommandQueueRegistry(), DESTRUCTOR_CLCommandQueue);
      }
   }

   static void releaseObjects(CLProgram var0) {
      if (var0.isValid() && var0.getReferenceCount() <= 1) {
         releaseObjects(var0.getCLKernelRegistry(), DESTRUCTOR_CLKernel);
      }
   }

   static void releaseObjects(CLCommandQueue var0) {
      if (var0.isValid() && var0.getReferenceCount() <= 1) {
         releaseObjects(var0.getCLEventRegistry(), DESTRUCTOR_CLEvent);
      }
   }

   private static void releaseObjects(CLObjectRegistry var0, APIUtil.ObjectDestructor var1) {
      if (!var0.isEmpty()) {
         Iterator var2 = var0.getAll().iterator();

         while(var2.hasNext()) {
            FastLongMap.Entry var3 = (FastLongMap.Entry)var2.next();
            CLObjectChild var4 = (CLObjectChild)var3.value;

            while(var4.isValid()) {
               var1.release(var4);
            }
         }

      }
   }

   private interface ObjectDestructor {
      void release(CLObjectChild var1);
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
