package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLUtil;

class GLChecks {
   private GLChecks() {
   }

   static int getBufferObjectSize(ContextCapabilities var0, int var1) {
      return GL15.glGetBufferParameteri(var1, 34660);
   }

   static int getBufferObjectSizeARB(ContextCapabilities var0, int var1) {
      return ARBBufferObject.glGetBufferParameteriARB(var1, 34660);
   }

   static int getBufferObjectSizeATI(ContextCapabilities var0, int var1) {
      return ATIVertexArrayObject.glGetObjectBufferiATI(var1, 34660);
   }

   static int getNamedBufferObjectSize(ContextCapabilities var0, int var1) {
      return EXTDirectStateAccess.glGetNamedBufferParameterEXT(var1, 34660);
   }

   static void ensureArrayVBOdisabled(ContextCapabilities var0) {
      if (LWJGLUtil.CHECKS && StateTracker.getReferences(var0).arrayBuffer != 0) {
         throw new OpenGLException("Cannot use Buffers when Array Buffer Object is enabled");
      }
   }

   static void ensureArrayVBOenabled(ContextCapabilities var0) {
      if (LWJGLUtil.CHECKS && StateTracker.getReferences(var0).arrayBuffer == 0) {
         throw new OpenGLException("Cannot use offsets when Array Buffer Object is disabled");
      }
   }

   static void ensureElementVBOdisabled(ContextCapabilities var0) {
      if (LWJGLUtil.CHECKS && StateTracker.getElementArrayBufferBound(var0) != 0) {
         throw new OpenGLException("Cannot use Buffers when Element Array Buffer Object is enabled");
      }
   }

   static void ensureElementVBOenabled(ContextCapabilities var0) {
      if (LWJGLUtil.CHECKS && StateTracker.getElementArrayBufferBound(var0) == 0) {
         throw new OpenGLException("Cannot use offsets when Element Array Buffer Object is disabled");
      }
   }

   static void ensureIndirectBOdisabled(ContextCapabilities var0) {
      if (LWJGLUtil.CHECKS && StateTracker.getReferences(var0).indirectBuffer != 0) {
         throw new OpenGLException("Cannot use Buffers when Draw Indirect Object is enabled");
      }
   }

   static void ensureIndirectBOenabled(ContextCapabilities var0) {
      if (LWJGLUtil.CHECKS && StateTracker.getReferences(var0).indirectBuffer == 0) {
         throw new OpenGLException("Cannot use offsets when Draw Indirect Object is disabled");
      }
   }

   static void ensurePackPBOdisabled(ContextCapabilities var0) {
      if (LWJGLUtil.CHECKS && StateTracker.getReferences(var0).pixelPackBuffer != 0) {
         throw new OpenGLException("Cannot use Buffers when Pixel Pack Buffer Object is enabled");
      }
   }

   static void ensurePackPBOenabled(ContextCapabilities var0) {
      if (LWJGLUtil.CHECKS && StateTracker.getReferences(var0).pixelPackBuffer == 0) {
         throw new OpenGLException("Cannot use offsets when Pixel Pack Buffer Object is disabled");
      }
   }

   static void ensureUnpackPBOdisabled(ContextCapabilities var0) {
      if (LWJGLUtil.CHECKS && StateTracker.getReferences(var0).pixelUnpackBuffer != 0) {
         throw new OpenGLException("Cannot use Buffers when Pixel Unpack Buffer Object is enabled");
      }
   }

   static void ensureUnpackPBOenabled(ContextCapabilities var0) {
      if (LWJGLUtil.CHECKS && StateTracker.getReferences(var0).pixelUnpackBuffer == 0) {
         throw new OpenGLException("Cannot use offsets when Pixel Unpack Buffer Object is disabled");
      }
   }

   static int calculateImageStorage(Buffer var0, int var1, int var2, int var3, int var4, int var5) {
      return LWJGLUtil.CHECKS ? calculateImageStorage(var1, var2, var3, var4, var5) >> BufferUtils.getElementSizeExponent(var0) : 0;
   }

   static int calculateTexImage1DStorage(Buffer var0, int var1, int var2, int var3) {
      return LWJGLUtil.CHECKS ? calculateTexImage1DStorage(var1, var2, var3) >> BufferUtils.getElementSizeExponent(var0) : 0;
   }

   static int calculateTexImage2DStorage(Buffer var0, int var1, int var2, int var3, int var4) {
      return LWJGLUtil.CHECKS ? calculateTexImage2DStorage(var1, var2, var3, var4) >> BufferUtils.getElementSizeExponent(var0) : 0;
   }

   static int calculateTexImage3DStorage(Buffer var0, int var1, int var2, int var3, int var4, int var5) {
      return LWJGLUtil.CHECKS ? calculateTexImage3DStorage(var1, var2, var3, var4, var5) >> BufferUtils.getElementSizeExponent(var0) : 0;
   }

   private static int calculateImageStorage(int var0, int var1, int var2, int var3, int var4) {
      return calculateBytesPerPixel(var0, var1) * var2 * var3 * var4;
   }

   private static int calculateTexImage1DStorage(int var0, int var1, int var2) {
      return calculateBytesPerPixel(var0, var1) * var2;
   }

   private static int calculateTexImage2DStorage(int var0, int var1, int var2, int var3) {
      return calculateTexImage1DStorage(var0, var1, var2) * var3;
   }

   private static int calculateTexImage3DStorage(int var0, int var1, int var2, int var3, int var4) {
      return calculateTexImage2DStorage(var0, var1, var2, var3) * var4;
   }

   private static int calculateBytesPerPixel(int var0, int var1) {
      byte var2;
      switch(var1) {
      case 5120:
      case 5121:
         var2 = 1;
         break;
      case 5122:
      case 5123:
         var2 = 2;
         break;
      case 5124:
      case 5125:
      case 5126:
         var2 = 4;
         break;
      default:
         return 0;
      }

      byte var3;
      switch(var0) {
      case 6406:
      case 6409:
         var3 = 1;
         break;
      case 6407:
      case 32992:
         var3 = 3;
         break;
      case 6408:
      case 32768:
      case 32993:
         var3 = 4;
         break;
      case 6410:
         var3 = 2;
         break;
      default:
         return 0;
      }

      return var2 * var3;
   }

   static int calculateBytesPerCharCode(int var0) {
      switch(var0) {
      case 5121:
      case 37018:
         return 1;
      case 5123:
      case 5127:
      case 37019:
         return 2;
      case 5128:
         return 3;
      case 5129:
         return 4;
      default:
         throw new IllegalArgumentException("Unsupported charcode type: " + var0);
      }
   }

   static int calculateBytesPerPathName(int var0) {
      switch(var0) {
      case 5120:
      case 5121:
      case 37018:
         return 1;
      case 5122:
      case 5123:
      case 5127:
      case 37019:
         return 2;
      case 5124:
      case 5125:
      case 5126:
      case 5129:
         return 4;
      case 5128:
         return 3;
      default:
         throw new IllegalArgumentException("Unsupported path name type: " + var0);
      }
   }

   static int calculateTransformPathValues(int var0) {
      switch(var0) {
      case 0:
         return 0;
      case 37006:
      case 37007:
         return 1;
      case 37008:
         return 2;
      case 37009:
         return 3;
      case 37010:
      case 37014:
         return 6;
      case 37012:
      case 37016:
         return 12;
      default:
         throw new IllegalArgumentException("Unsupported transform type: " + var0);
      }
   }

   static int calculatePathColorGenCoeffsCount(int var0, int var1) {
      int var2 = calculatePathGenCoeffsPerComponent(var0);
      switch(var1) {
      case 6407:
         return 3 * var2;
      case 6408:
         return 4 * var2;
      default:
         return var2;
      }
   }

   static int calculatePathTextGenCoeffsPerComponent(FloatBuffer var0, int var1) {
      return var1 == 0 ? 0 : var0.remaining() / calculatePathGenCoeffsPerComponent(var1);
   }

   private static int calculatePathGenCoeffsPerComponent(int var0) {
      switch(var0) {
      case 0:
         return 0;
      case 9216:
         return 4;
      case 9217:
      case 37002:
         return 3;
      default:
         throw new IllegalArgumentException("Unsupported gen mode: " + var0);
      }
   }

   static int calculateMetricsSize(int var0, int var1) {
      if (LWJGLUtil.DEBUG && (var1 < 0 || var1 % 4 != 0)) {
         throw new IllegalArgumentException("Invalid stride value: " + var1);
      } else {
         int var2 = Integer.bitCount(var0);
         if (LWJGLUtil.DEBUG && var1 >> 2 < var2) {
            throw new IllegalArgumentException("The queried metrics do not fit in the specified stride: " + var1);
         } else {
            return var1 == 0 ? var2 : var1 >> 2;
         }
      }
   }
}
