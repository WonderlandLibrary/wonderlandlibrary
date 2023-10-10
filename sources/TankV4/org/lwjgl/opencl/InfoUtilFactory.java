package org.lwjgl.opencl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.BufferChecks;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.PointerWrapper;
import org.lwjgl.opencl.api.CLBufferRegion;
import org.lwjgl.opencl.api.CLImageFormat;
import org.lwjgl.opencl.api.Filter;
import org.lwjgl.opengl.Drawable;

final class InfoUtilFactory {
   static final InfoUtil CL_COMMAND_QUEUE_UTIL = new InfoUtilAbstract() {
      protected int getInfo(CLCommandQueue var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return CL10.clGetCommandQueueInfo(var1, var2, var3, (PointerBuffer)null);
      }

      protected int getInfo(CLObject var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return this.getInfo((CLCommandQueue)var1, var2, var3, var4);
      }
   };
   static final CLContext.CLContextUtil CL_CONTEXT_UTIL = new InfoUtilFactory.CLContextUtil();
   static final InfoUtil CL_DEVICE_UTIL = new InfoUtilFactory.CLDeviceUtil();
   static final CLEvent.CLEventUtil CL_EVENT_UTIL = new InfoUtilFactory.CLEventUtil();
   static final CLKernel.CLKernelUtil CL_KERNEL_UTIL = new InfoUtilFactory.CLKernelUtil();
   static final CLMem.CLMemUtil CL_MEM_UTIL = new InfoUtilFactory.CLMemUtil();
   static final CLPlatform.CLPlatformUtil CL_PLATFORM_UTIL = new InfoUtilFactory.CLPlatformUtil();
   static final CLProgram.CLProgramUtil CL_PROGRAM_UTIL = new InfoUtilFactory.CLProgramUtil();
   static final InfoUtil CL_SAMPLER_UTIL = new InfoUtilAbstract() {
      protected int getInfo(CLSampler var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return CL10.clGetSamplerInfo(var1, var2, var3, var4);
      }

      protected int getInfo(CLObject var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return this.getInfo((CLSampler)var1, var2, var3, var4);
      }
   };

   private InfoUtilFactory() {
   }

   private static final class CLProgramUtil extends InfoUtilAbstract implements CLProgram.CLProgramUtil {
      private CLProgramUtil() {
      }

      protected int getInfo(CLProgram var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return CL10.clGetProgramInfo(var1, var2, var3, var4);
      }

      protected int getInfoSizeArraySize(CLProgram var1, int var2) {
         switch(var2) {
         case 4453:
            return this.getInfoInt(var1, 4450);
         default:
            throw new IllegalArgumentException("Unsupported parameter: " + LWJGLUtil.toHexString(var2));
         }
      }

      public CLKernel[] createKernelsInProgram(CLProgram var1) {
         IntBuffer var2 = APIUtil.getBufferInt();
         CL10.clCreateKernelsInProgram(var1, (PointerBuffer)null, var2);
         int var3 = var2.get(0);
         if (var3 == 0) {
            return null;
         } else {
            PointerBuffer var4 = APIUtil.getBufferPointer(var3);
            CL10.clCreateKernelsInProgram(var1, var4, (IntBuffer)null);
            CLKernel[] var5 = new CLKernel[var3];

            for(int var6 = 0; var6 < var3; ++var6) {
               var5[var6] = var1.getCLKernel(var4.get(var6));
            }

            return var5;
         }
      }

      public CLDevice[] getInfoDevices(CLProgram var1) {
         var1.checkValid();
         int var2 = this.getInfoInt(var1, 4450);
         PointerBuffer var3 = APIUtil.getBufferPointer(var2);
         CL10.clGetProgramInfo(var1, 4451, var3.getBuffer(), (PointerBuffer)null);
         CLPlatform var4 = (CLPlatform)((CLContext)var1.getParent()).getParent();
         CLDevice[] var5 = new CLDevice[var2];

         for(int var6 = 0; var6 < var2; ++var6) {
            var5[var6] = var4.getCLDevice(var3.get(var6));
         }

         return var5;
      }

      public ByteBuffer getInfoBinaries(CLProgram var1, ByteBuffer var2) {
         var1.checkValid();
         PointerBuffer var3 = this.getSizesBuffer(var1, 4453);
         int var4 = 0;

         for(int var5 = 0; var5 < var3.limit(); ++var5) {
            var4 = (int)((long)var4 + var3.get(var5));
         }

         if (var2 == null) {
            var2 = BufferUtils.createByteBuffer(var4);
         } else if (LWJGLUtil.DEBUG) {
            BufferChecks.checkBuffer(var2, var4);
         }

         CL10.clGetProgramInfo(var1, var3, var2, (PointerBuffer)null);
         return var2;
      }

      public ByteBuffer[] getInfoBinaries(CLProgram var1, ByteBuffer[] var2) {
         var1.checkValid();
         PointerBuffer var3;
         int var4;
         if (var2 == null) {
            var3 = this.getSizesBuffer(var1, 4453);
            var2 = new ByteBuffer[var3.remaining()];

            for(var4 = 0; var4 < var3.remaining(); ++var4) {
               var2[var4] = BufferUtils.createByteBuffer((int)var3.get(var4));
            }
         } else if (LWJGLUtil.DEBUG) {
            var3 = this.getSizesBuffer(var1, 4453);
            if (var2.length < var3.remaining()) {
               throw new IllegalArgumentException("The target array is not big enough: " + var3.remaining() + " buffers are required.");
            }

            for(var4 = 0; var4 < var2.length; ++var4) {
               BufferChecks.checkBuffer(var2[var4], (int)var3.get(var4));
            }
         }

         CL10.clGetProgramInfo(var1, var2, (PointerBuffer)null);
         return var2;
      }

      public String getBuildInfoString(CLProgram var1, CLDevice var2, int var3) {
         var1.checkValid();
         int var4 = getBuildSizeRet(var1, var2, var3);
         if (var4 <= 1) {
            return null;
         } else {
            ByteBuffer var5 = APIUtil.getBufferByte(var4);
            CL10.clGetProgramBuildInfo(var1, var2, var3, var5, (PointerBuffer)null);
            var5.limit(var4 - 1);
            return APIUtil.getString(var5);
         }
      }

      public int getBuildInfoInt(CLProgram var1, CLDevice var2, int var3) {
         var1.checkValid();
         ByteBuffer var4 = APIUtil.getBufferByte(4);
         CL10.clGetProgramBuildInfo(var1, var2, var3, var4, (PointerBuffer)null);
         return var4.getInt(0);
      }

      private static int getBuildSizeRet(CLProgram var0, CLDevice var1, int var2) {
         PointerBuffer var3 = APIUtil.getBufferPointer();
         int var4 = CL10.clGetProgramBuildInfo(var0, var1, var2, (ByteBuffer)null, var3);
         if (var4 != 0) {
            throw new IllegalArgumentException("Invalid parameter specified: " + LWJGLUtil.toHexString(var2));
         } else {
            return (int)var3.get(0);
         }
      }

      protected int getInfoSizeArraySize(CLObject var1, int var2) {
         return this.getInfoSizeArraySize((CLProgram)var1, var2);
      }

      protected int getInfo(CLObject var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return this.getInfo((CLProgram)var1, var2, var3, var4);
      }

      CLProgramUtil(Object var1) {
         this();
      }
   }

   private static final class CLPlatformUtil extends InfoUtilAbstract implements CLPlatform.CLPlatformUtil {
      private CLPlatformUtil() {
      }

      protected int getInfo(CLPlatform var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return CL10.clGetPlatformInfo(var1, var2, var3, var4);
      }

      public List getPlatforms(Filter var1) {
         IntBuffer var2 = APIUtil.getBufferInt();
         CL10.clGetPlatformIDs((PointerBuffer)null, var2);
         int var3 = var2.get(0);
         if (var3 == 0) {
            return null;
         } else {
            PointerBuffer var4 = APIUtil.getBufferPointer(var3);
            CL10.clGetPlatformIDs(var4, (IntBuffer)null);
            ArrayList var5 = new ArrayList(var3);

            for(int var6 = 0; var6 < var3; ++var6) {
               CLPlatform var7 = CLPlatform.getCLPlatform(var4.get(var6));
               if (var1 == null || var1.accept(var7)) {
                  var5.add(var7);
               }
            }

            return var5.size() == 0 ? null : var5;
         }
      }

      public List getDevices(CLPlatform var1, int var2, Filter var3) {
         var1.checkValid();
         IntBuffer var4 = APIUtil.getBufferInt();
         CL10.clGetDeviceIDs(var1, (long)var2, (PointerBuffer)null, var4);
         int var5 = var4.get(0);
         if (var5 == 0) {
            return null;
         } else {
            PointerBuffer var6 = APIUtil.getBufferPointer(var5);
            CL10.clGetDeviceIDs(var1, (long)var2, var6, (IntBuffer)null);
            ArrayList var7 = new ArrayList(var5);

            for(int var8 = 0; var8 < var5; ++var8) {
               CLDevice var9 = var1.getCLDevice(var6.get(var8));
               if (var3 == null || var3.accept(var9)) {
                  var7.add(var9);
               }
            }

            return var7.size() == 0 ? null : var7;
         }
      }

      protected int getInfo(CLObject var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return this.getInfo((CLPlatform)var1, var2, var3, var4);
      }

      CLPlatformUtil(Object var1) {
         this();
      }
   }

   private static final class CLMemUtil extends InfoUtilAbstract implements CLMem.CLMemUtil {
      private CLMemUtil() {
      }

      protected int getInfo(CLMem var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return CL10.clGetMemObjectInfo(var1, var2, var3, var4);
      }

      public CLMem createImage2D(CLContext var1, long var2, CLImageFormat var4, long var5, long var7, long var9, Buffer var11, IntBuffer var12) {
         ByteBuffer var13 = APIUtil.getBufferByte(8);
         var13.putInt(0, var4.getChannelOrder());
         var13.putInt(4, var4.getChannelType());
         long var14 = CLCapabilities.clCreateImage2D;
         BufferChecks.checkFunctionAddress(var14);
         if (var12 != null) {
            BufferChecks.checkBuffer((IntBuffer)var12, 1);
         } else if (LWJGLUtil.DEBUG) {
            var12 = APIUtil.getBufferInt();
         }

         CLMem var16 = new CLMem(CL10.nclCreateImage2D(var1.getPointer(), var2, MemoryUtil.getAddress((ByteBuffer)var13, 0), var5, var7, var9, MemoryUtil.getAddress0Safe(var11) + (long)(var11 != null ? BufferChecks.checkBuffer(var11, CLChecks.calculateImage2DSize(var13, var5, var7, var9)) : 0), MemoryUtil.getAddressSafe(var12), var14), var1);
         if (LWJGLUtil.DEBUG) {
            Util.checkCLError(var12.get(0));
         }

         return var16;
      }

      public CLMem createImage3D(CLContext var1, long var2, CLImageFormat var4, long var5, long var7, long var9, long var11, long var13, Buffer var15, IntBuffer var16) {
         ByteBuffer var17 = APIUtil.getBufferByte(8);
         var17.putInt(0, var4.getChannelOrder());
         var17.putInt(4, var4.getChannelType());
         long var18 = CLCapabilities.clCreateImage3D;
         BufferChecks.checkFunctionAddress(var18);
         if (var16 != null) {
            BufferChecks.checkBuffer((IntBuffer)var16, 1);
         } else if (LWJGLUtil.DEBUG) {
            var16 = APIUtil.getBufferInt();
         }

         CLMem var20 = new CLMem(CL10.nclCreateImage3D(var1.getPointer(), var2, MemoryUtil.getAddress((ByteBuffer)var17, 0), var5, var7, var9, var11, var13, MemoryUtil.getAddress0Safe(var15) + (long)(var15 != null ? BufferChecks.checkBuffer(var15, CLChecks.calculateImage3DSize(var17, var5, var7, var9, var11, var13)) : 0), MemoryUtil.getAddressSafe(var16), var18), var1);
         if (LWJGLUtil.DEBUG) {
            Util.checkCLError(var16.get(0));
         }

         return var20;
      }

      public CLMem createSubBuffer(CLMem var1, long var2, int var4, CLBufferRegion var5, IntBuffer var6) {
         PointerBuffer var7 = APIUtil.getBufferPointer(2);
         var7.put((long)var5.getOrigin());
         var7.put((long)var5.getSize());
         return CL11.clCreateSubBuffer(var1, var2, var4, var7.getBuffer(), var6);
      }

      public ByteBuffer getInfoHostBuffer(CLMem var1) {
         var1.checkValid();
         long var2;
         if (LWJGLUtil.DEBUG) {
            var2 = this.getInfoLong(var1, 4353);
            if ((var2 & 8L) != 8L) {
               throw new IllegalArgumentException("The specified CLMem object does not use host memory.");
            }
         }

         var2 = this.getInfoSize(var1, 4354);
         if (var2 == 0L) {
            return null;
         } else {
            long var4 = this.getInfoSize(var1, 4355);
            return CL.getHostBuffer(var4, (int)var2);
         }
      }

      public long getImageInfoSize(CLMem var1, int var2) {
         var1.checkValid();
         PointerBuffer var3 = APIUtil.getBufferPointer();
         CL10.clGetImageInfo(var1, var2, var3.getBuffer(), (PointerBuffer)null);
         return var3.get(0);
      }

      public CLImageFormat getImageInfoFormat(CLMem var1) {
         var1.checkValid();
         ByteBuffer var2 = APIUtil.getBufferByte(8);
         CL10.clGetImageInfo(var1, 4368, var2, (PointerBuffer)null);
         return new CLImageFormat(var2.getInt(0), var2.getInt(4));
      }

      public int getImageInfoFormat(CLMem var1, int var2) {
         var1.checkValid();
         ByteBuffer var3 = APIUtil.getBufferByte(8);
         CL10.clGetImageInfo(var1, 4368, var3, (PointerBuffer)null);
         return var3.getInt(var2 << 2);
      }

      public int getGLObjectType(CLMem var1) {
         var1.checkValid();
         IntBuffer var2 = APIUtil.getBufferInt();
         CL10GL.clGetGLObjectInfo(var1, var2, (IntBuffer)null);
         return var2.get(0);
      }

      public int getGLObjectName(CLMem var1) {
         var1.checkValid();
         IntBuffer var2 = APIUtil.getBufferInt();
         CL10GL.clGetGLObjectInfo(var1, (IntBuffer)null, var2);
         return var2.get(0);
      }

      public int getGLTextureInfoInt(CLMem var1, int var2) {
         var1.checkValid();
         ByteBuffer var3 = APIUtil.getBufferByte(4);
         CL10GL.clGetGLTextureInfo(var1, var2, var3, (PointerBuffer)null);
         return var3.getInt(0);
      }

      protected int getInfo(CLObject var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return this.getInfo((CLMem)var1, var2, var3, var4);
      }

      CLMemUtil(Object var1) {
         this();
      }
   }

   private static final class CLKernelUtil extends InfoUtilAbstract implements CLKernel.CLKernelUtil {
      private CLKernelUtil() {
      }

      public void setArg(CLKernel var1, int var2, byte var3) {
         CL10.clSetKernelArg(var1, var2, 1L, APIUtil.getBufferByte(1).put(0, var3));
      }

      public void setArg(CLKernel var1, int var2, short var3) {
         CL10.clSetKernelArg(var1, var2, 2L, APIUtil.getBufferShort().put(0, var3));
      }

      public void setArg(CLKernel var1, int var2, int var3) {
         CL10.clSetKernelArg(var1, var2, 4L, APIUtil.getBufferInt().put(0, var3));
      }

      public void setArg(CLKernel var1, int var2, long var3) {
         CL10.clSetKernelArg(var1, var2, 8L, APIUtil.getBufferLong().put(0, var3));
      }

      public void setArg(CLKernel var1, int var2, float var3) {
         CL10.clSetKernelArg(var1, var2, 4L, APIUtil.getBufferFloat().put(0, var3));
      }

      public void setArg(CLKernel var1, int var2, double var3) {
         CL10.clSetKernelArg(var1, var2, 8L, APIUtil.getBufferDouble().put(0, var3));
      }

      public void setArg(CLKernel var1, int var2, CLObject var3) {
         CL10.clSetKernelArg(var1, var2, var3);
      }

      public void setArgSize(CLKernel var1, int var2, long var3) {
         CL10.clSetKernelArg(var1, var2, var3);
      }

      protected int getInfo(CLKernel var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return CL10.clGetKernelInfo(var1, var2, var3, var4);
      }

      public long getWorkGroupInfoSize(CLKernel var1, CLDevice var2, int var3) {
         var2.checkValid();
         PointerBuffer var4 = APIUtil.getBufferPointer();
         CL10.clGetKernelWorkGroupInfo(var1, var2, var3, var4.getBuffer(), (PointerBuffer)null);
         return var4.get(0);
      }

      public long[] getWorkGroupInfoSizeArray(CLKernel var1, CLDevice var2, int var3) {
         var2.checkValid();
         switch(var3) {
         case 4529:
            byte var4 = 3;
            PointerBuffer var5 = APIUtil.getBufferPointer(var4);
            CL10.clGetKernelWorkGroupInfo(var1, var2, var3, var5.getBuffer(), (PointerBuffer)null);
            long[] var6 = new long[var4];

            for(int var7 = 0; var7 < var4; ++var7) {
               var6[var7] = var5.get(var7);
            }

            return var6;
         default:
            throw new IllegalArgumentException("Unsupported parameter: " + LWJGLUtil.toHexString(var3));
         }
      }

      public long getWorkGroupInfoLong(CLKernel var1, CLDevice var2, int var3) {
         var2.checkValid();
         ByteBuffer var4 = APIUtil.getBufferByte(8);
         CL10.clGetKernelWorkGroupInfo(var1, var2, var3, var4, (PointerBuffer)null);
         return var4.getLong(0);
      }

      protected int getInfo(CLObject var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return this.getInfo((CLKernel)var1, var2, var3, var4);
      }

      CLKernelUtil(Object var1) {
         this();
      }
   }

   private static final class CLEventUtil extends InfoUtilAbstract implements CLEvent.CLEventUtil {
      private CLEventUtil() {
      }

      protected int getInfo(CLEvent var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return CL10.clGetEventInfo(var1, var2, var3, var4);
      }

      public long getProfilingInfoLong(CLEvent var1, int var2) {
         var1.checkValid();
         ByteBuffer var3 = APIUtil.getBufferByte(8);
         CL10.clGetEventProfilingInfo(var1, var2, var3, (PointerBuffer)null);
         return var3.getLong(0);
      }

      protected int getInfo(CLObject var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return this.getInfo((CLEvent)var1, var2, var3, var4);
      }

      CLEventUtil(Object var1) {
         this();
      }
   }

   private static final class CLDeviceUtil extends InfoUtilAbstract {
      private CLDeviceUtil() {
      }

      protected int getInfo(CLDevice var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return CL10.clGetDeviceInfo(var1, var2, var3, var4);
      }

      protected int getInfoSizeArraySize(CLDevice var1, int var2) {
         switch(var2) {
         case 4101:
            return this.getInfoInt(var1, 4099);
         default:
            throw new IllegalArgumentException("Unsupported parameter: " + LWJGLUtil.toHexString(var2));
         }
      }

      protected int getInfoSizeArraySize(CLObject var1, int var2) {
         return this.getInfoSizeArraySize((CLDevice)var1, var2);
      }

      protected int getInfo(CLObject var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return this.getInfo((CLDevice)var1, var2, var3, var4);
      }

      CLDeviceUtil(Object var1) {
         this();
      }
   }

   private static final class CLContextUtil extends InfoUtilAbstract implements CLContext.CLContextUtil {
      private CLContextUtil() {
      }

      protected int getInfo(CLContext var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return CL10.clGetContextInfo(var1, var2, var3, var4);
      }

      public List getInfoDevices(CLContext var1) {
         var1.checkValid();
         int var2;
         PointerBuffer var3;
         if (CLCapabilities.getPlatformCapabilities((CLPlatform)var1.getParent()).OpenCL11) {
            var2 = this.getInfoInt(var1, 4227);
         } else {
            var3 = APIUtil.getBufferPointer();
            CL10.clGetContextInfo(var1, 4225, (ByteBuffer)null, var3);
            var2 = (int)(var3.get(0) / (long)PointerBuffer.getPointerSize());
         }

         var3 = APIUtil.getBufferPointer(var2);
         CL10.clGetContextInfo(var1, 4225, var3.getBuffer(), (PointerBuffer)null);
         ArrayList var4 = new ArrayList(var2);

         for(int var5 = 0; var5 < var2; ++var5) {
            var4.add(((CLPlatform)var1.getParent()).getCLDevice(var3.get(var5)));
         }

         return var4.size() == 0 ? null : var4;
      }

      public CLContext create(CLPlatform var1, List var2, CLContextCallback var3, Drawable var4, IntBuffer var5) throws LWJGLException {
         int var6 = 2 + (var4 == null ? 0 : 4) + 1;
         PointerBuffer var7 = APIUtil.getBufferPointer(var6 + var2.size());
         var7.put(4228L).put((PointerWrapper)var1);
         if (var4 != null) {
            var4.setCLSharingProperties(var7);
         }

         var7.put(0L);
         var7.position(var6);
         Iterator var8 = var2.iterator();

         while(var8.hasNext()) {
            CLDevice var9 = (CLDevice)var8.next();
            var7.put((PointerWrapper)var9);
         }

         long var15 = CLCapabilities.clCreateContext;
         BufferChecks.checkFunctionAddress(var15);
         if (var5 != null) {
            BufferChecks.checkBuffer((IntBuffer)var5, 1);
         } else if (LWJGLUtil.DEBUG) {
            var5 = APIUtil.getBufferInt();
         }

         long var10 = var3 != null && !var3.isCustom() ? CallbackUtil.createGlobalRef(var3) : 0L;
         CLContext var12 = null;
         var12 = new CLContext(CL10.nclCreateContext(MemoryUtil.getAddress0((Buffer)var7.getBuffer()), var2.size(), MemoryUtil.getAddress(var7, var6), var3 == null ? 0L : var3.getPointer(), var10, MemoryUtil.getAddressSafe(var5), var15), var1);
         if (LWJGLUtil.DEBUG) {
            Util.checkCLError(var5.get(0));
         }

         if (var12 != null) {
            var12.setContextCallback(var10);
         }

         return var12;
      }

      public CLContext createFromType(CLPlatform var1, long var2, CLContextCallback var4, Drawable var5, IntBuffer var6) throws LWJGLException {
         int var7 = 2 + (var5 == null ? 0 : 4) + 1;
         PointerBuffer var8 = APIUtil.getBufferPointer(var7);
         var8.put(4228L).put((PointerWrapper)var1);
         if (var5 != null) {
            var5.setCLSharingProperties(var8);
         }

         var8.put(0L);
         var8.flip();
         return CL10.clCreateContextFromType(var8, var2, var4, var6);
      }

      public List getSupportedImageFormats(CLContext var1, long var2, int var4, Filter var5) {
         IntBuffer var6 = APIUtil.getBufferInt();
         CL10.clGetSupportedImageFormats(var1, var2, var4, (ByteBuffer)null, var6);
         int var7 = var6.get(0);
         if (var7 == 0) {
            return null;
         } else {
            ByteBuffer var8 = BufferUtils.createByteBuffer(var7 * 8);
            CL10.clGetSupportedImageFormats(var1, var2, var4, var8, (IntBuffer)null);
            ArrayList var9 = new ArrayList(var7);

            for(int var10 = 0; var10 < var7; ++var10) {
               int var11 = var7 * 8;
               CLImageFormat var12 = new CLImageFormat(var8.getInt(var11), var8.getInt(var11 + 4));
               if (var5 == null || var5.accept(var12)) {
                  var9.add(var12);
               }
            }

            return var9.size() == 0 ? null : var9;
         }
      }

      protected int getInfo(CLObject var1, int var2, ByteBuffer var3, PointerBuffer var4) {
         return this.getInfo((CLContext)var1, var2, var3, var4);
      }

      CLContextUtil(Object var1) {
         this();
      }
   }
}
