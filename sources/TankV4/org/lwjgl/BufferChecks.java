package org.lwjgl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

public class BufferChecks {
   private BufferChecks() {
   }

   public static void checkFunctionAddress(long var0) {
      if (LWJGLUtil.CHECKS && var0 == 0L) {
         throw new IllegalStateException("Function is not supported");
      }
   }

   public static void checkNullTerminated(ByteBuffer var0) {
      if (LWJGLUtil.CHECKS && var0.get(var0.limit() - 1) != 0) {
         throw new IllegalArgumentException("Missing null termination");
      }
   }

   public static void checkNullTerminated(ByteBuffer var0, int var1) {
      if (LWJGLUtil.CHECKS) {
         int var2 = 0;

         for(int var3 = var0.position(); var3 < var0.limit(); ++var3) {
            if (var0.get(var3) == 0) {
               ++var2;
            }
         }

         if (var2 < var1) {
            throw new IllegalArgumentException("Missing null termination");
         }
      }

   }

   public static void checkNullTerminated(IntBuffer var0) {
      if (LWJGLUtil.CHECKS && var0.get(var0.limit() - 1) != 0) {
         throw new IllegalArgumentException("Missing null termination");
      }
   }

   public static void checkNullTerminated(LongBuffer var0) {
      if (LWJGLUtil.CHECKS && var0.get(var0.limit() - 1) != 0L) {
         throw new IllegalArgumentException("Missing null termination");
      }
   }

   public static void checkNullTerminated(PointerBuffer var0) {
      if (LWJGLUtil.CHECKS && var0.get(var0.limit() - 1) != 0L) {
         throw new IllegalArgumentException("Missing null termination");
      }
   }

   public static void checkNotNull(Object var0) {
      if (LWJGLUtil.CHECKS && var0 == null) {
         throw new IllegalArgumentException("Null argument");
      }
   }

   public static void checkDirect(ByteBuffer var0) {
      if (LWJGLUtil.CHECKS && !var0.isDirect()) {
         throw new IllegalArgumentException("ByteBuffer is not direct");
      }
   }

   public static void checkDirect(ShortBuffer var0) {
      if (LWJGLUtil.CHECKS && !var0.isDirect()) {
         throw new IllegalArgumentException("ShortBuffer is not direct");
      }
   }

   public static void checkDirect(IntBuffer var0) {
      if (LWJGLUtil.CHECKS && !var0.isDirect()) {
         throw new IllegalArgumentException("IntBuffer is not direct");
      }
   }

   public static void checkDirect(LongBuffer var0) {
      if (LWJGLUtil.CHECKS && !var0.isDirect()) {
         throw new IllegalArgumentException("LongBuffer is not direct");
      }
   }

   public static void checkDirect(FloatBuffer var0) {
      if (LWJGLUtil.CHECKS && !var0.isDirect()) {
         throw new IllegalArgumentException("FloatBuffer is not direct");
      }
   }

   public static void checkDirect(DoubleBuffer var0) {
      if (LWJGLUtil.CHECKS && !var0.isDirect()) {
         throw new IllegalArgumentException("DoubleBuffer is not direct");
      }
   }

   public static void checkDirect(PointerBuffer var0) {
   }

   public static void checkArray(Object[] var0) {
      if (LWJGLUtil.CHECKS && (var0 == null || var0.length == 0)) {
         throw new IllegalArgumentException("Invalid array");
      }
   }

   private static void throwBufferSizeException(Buffer var0, int var1) {
      throw new IllegalArgumentException("Number of remaining buffer elements is " + var0.remaining() + ", must be at least " + var1 + ". Because at most " + var1 + " elements can be returned, a buffer with at least " + var1 + " elements is required, regardless of actual returned element count");
   }

   private static void throwBufferSizeException(PointerBuffer var0, int var1) {
      throw new IllegalArgumentException("Number of remaining pointer buffer elements is " + var0.remaining() + ", must be at least " + var1);
   }

   private static void throwArraySizeException(Object[] var0, int var1) {
      throw new IllegalArgumentException("Number of array elements is " + var0.length + ", must be at least " + var1);
   }

   private static void throwArraySizeException(long[] var0, int var1) {
      throw new IllegalArgumentException("Number of array elements is " + var0.length + ", must be at least " + var1);
   }

   public static void checkBufferSize(Buffer var0, int var1) {
      if (LWJGLUtil.CHECKS && var0.remaining() < var1) {
         throwBufferSizeException(var0, var1);
      }

   }

   public static int checkBuffer(Buffer var0, int var1) {
      byte var2;
      if (var0 instanceof ByteBuffer) {
         checkBuffer((ByteBuffer)var0, var1);
         var2 = 0;
      } else if (var0 instanceof ShortBuffer) {
         checkBuffer((ShortBuffer)var0, var1);
         var2 = 1;
      } else if (var0 instanceof IntBuffer) {
         checkBuffer((IntBuffer)var0, var1);
         var2 = 2;
      } else if (var0 instanceof LongBuffer) {
         checkBuffer((LongBuffer)var0, var1);
         var2 = 4;
      } else if (var0 instanceof FloatBuffer) {
         checkBuffer((FloatBuffer)var0, var1);
         var2 = 2;
      } else {
         if (!(var0 instanceof DoubleBuffer)) {
            throw new IllegalArgumentException("Unsupported Buffer type specified: " + var0.getClass());
         }

         checkBuffer((DoubleBuffer)var0, var1);
         var2 = 4;
      }

      return var0.position() << var2;
   }

   public static void checkBuffer(ByteBuffer var0, int var1) {
      if (LWJGLUtil.CHECKS) {
         checkBufferSize(var0, var1);
         checkDirect(var0);
      }

   }

   public static void checkBuffer(ShortBuffer var0, int var1) {
      if (LWJGLUtil.CHECKS) {
         checkBufferSize(var0, var1);
         checkDirect(var0);
      }

   }

   public static void checkBuffer(IntBuffer var0, int var1) {
      if (LWJGLUtil.CHECKS) {
         checkBufferSize(var0, var1);
         checkDirect(var0);
      }

   }

   public static void checkBuffer(LongBuffer var0, int var1) {
      if (LWJGLUtil.CHECKS) {
         checkBufferSize(var0, var1);
         checkDirect(var0);
      }

   }

   public static void checkBuffer(FloatBuffer var0, int var1) {
      if (LWJGLUtil.CHECKS) {
         checkBufferSize(var0, var1);
         checkDirect(var0);
      }

   }

   public static void checkBuffer(DoubleBuffer var0, int var1) {
      if (LWJGLUtil.CHECKS) {
         checkBufferSize(var0, var1);
         checkDirect(var0);
      }

   }

   public static void checkBuffer(PointerBuffer var0, int var1) {
      if (LWJGLUtil.CHECKS && var0.remaining() < var1) {
         throwBufferSizeException(var0, var1);
      }

   }

   public static void checkArray(Object[] var0, int var1) {
      if (LWJGLUtil.CHECKS && var0.length < var1) {
         throwArraySizeException(var0, var1);
      }

   }

   public static void checkArray(long[] var0, int var1) {
      if (LWJGLUtil.CHECKS && var0.length < var1) {
         throwArraySizeException(var0, var1);
      }

   }
}
