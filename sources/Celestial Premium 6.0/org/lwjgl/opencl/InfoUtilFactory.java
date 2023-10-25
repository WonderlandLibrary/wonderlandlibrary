/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferChecks;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.APIUtil;
import org.lwjgl.opencl.CL;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CL10GL;
import org.lwjgl.opencl.CL11;
import org.lwjgl.opencl.CLCapabilities;
import org.lwjgl.opencl.CLChecks;
import org.lwjgl.opencl.CLCommandQueue;
import org.lwjgl.opencl.CLContext;
import org.lwjgl.opencl.CLContextCallback;
import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opencl.CLEvent;
import org.lwjgl.opencl.CLKernel;
import org.lwjgl.opencl.CLMem;
import org.lwjgl.opencl.CLObject;
import org.lwjgl.opencl.CLPlatform;
import org.lwjgl.opencl.CLProgram;
import org.lwjgl.opencl.CLSampler;
import org.lwjgl.opencl.CallbackUtil;
import org.lwjgl.opencl.InfoUtil;
import org.lwjgl.opencl.InfoUtilAbstract;
import org.lwjgl.opencl.Util;
import org.lwjgl.opencl.api.CLBufferRegion;
import org.lwjgl.opencl.api.CLImageFormat;
import org.lwjgl.opencl.api.Filter;
import org.lwjgl.opengl.Drawable;

final class InfoUtilFactory {
    static final InfoUtil<CLCommandQueue> CL_COMMAND_QUEUE_UTIL = new InfoUtilAbstract<CLCommandQueue>(){

        @Override
        protected int getInfo(CLCommandQueue object, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
            return CL10.clGetCommandQueueInfo(object, param_name, param_value, null);
        }
    };
    static final CLContext.CLContextUtil CL_CONTEXT_UTIL = new CLContextUtil();
    static final InfoUtil<CLDevice> CL_DEVICE_UTIL = new CLDeviceUtil();
    static final CLEvent.CLEventUtil CL_EVENT_UTIL = new CLEventUtil();
    static final CLKernel.CLKernelUtil CL_KERNEL_UTIL = new CLKernelUtil();
    static final CLMem.CLMemUtil CL_MEM_UTIL = new CLMemUtil();
    static final CLPlatform.CLPlatformUtil CL_PLATFORM_UTIL = new CLPlatformUtil();
    static final CLProgram.CLProgramUtil CL_PROGRAM_UTIL = new CLProgramUtil();
    static final InfoUtil<CLSampler> CL_SAMPLER_UTIL = new InfoUtilAbstract<CLSampler>(){

        @Override
        protected int getInfo(CLSampler sampler, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
            return CL10.clGetSamplerInfo(sampler, param_name, param_value, param_value_size_ret);
        }
    };

    private InfoUtilFactory() {
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static final class CLProgramUtil
    extends InfoUtilAbstract<CLProgram>
    implements CLProgram.CLProgramUtil {
        private CLProgramUtil() {
        }

        @Override
        protected int getInfo(CLProgram program, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
            return CL10.clGetProgramInfo(program, param_name, param_value, param_value_size_ret);
        }

        @Override
        protected int getInfoSizeArraySize(CLProgram program, int param_name) {
            switch (param_name) {
                case 4453: {
                    return this.getInfoInt(program, 4450);
                }
            }
            throw new IllegalArgumentException("Unsupported parameter: " + LWJGLUtil.toHexString(param_name));
        }

        @Override
        public CLKernel[] createKernelsInProgram(CLProgram program) {
            IntBuffer numBuffer = APIUtil.getBufferInt();
            CL10.clCreateKernelsInProgram(program, null, numBuffer);
            int num_kernels = numBuffer.get(0);
            if (num_kernels == 0) {
                return null;
            }
            PointerBuffer kernelIDs = APIUtil.getBufferPointer(num_kernels);
            CL10.clCreateKernelsInProgram(program, kernelIDs, null);
            CLKernel[] kernels = new CLKernel[num_kernels];
            for (int i = 0; i < num_kernels; ++i) {
                kernels[i] = program.getCLKernel(kernelIDs.get(i));
            }
            return kernels;
        }

        @Override
        public CLDevice[] getInfoDevices(CLProgram program) {
            program.checkValid();
            int size = this.getInfoInt(program, 4450);
            PointerBuffer buffer = APIUtil.getBufferPointer(size);
            CL10.clGetProgramInfo(program, 4451, buffer.getBuffer(), null);
            CLPlatform platform = (CLPlatform)((CLContext)program.getParent()).getParent();
            CLDevice[] array = new CLDevice[size];
            for (int i = 0; i < size; ++i) {
                array[i] = platform.getCLDevice(buffer.get(i));
            }
            return array;
        }

        @Override
        public ByteBuffer getInfoBinaries(CLProgram program, ByteBuffer target) {
            program.checkValid();
            PointerBuffer sizes = this.getSizesBuffer(program, 4453);
            int totalSize = 0;
            for (int i = 0; i < sizes.limit(); ++i) {
                totalSize = (int)((long)totalSize + sizes.get(i));
            }
            if (target == null) {
                target = BufferUtils.createByteBuffer(totalSize);
            } else if (LWJGLUtil.DEBUG) {
                BufferChecks.checkBuffer(target, totalSize);
            }
            CL10.clGetProgramInfo(program, sizes, target, null);
            return target;
        }

        @Override
        public ByteBuffer[] getInfoBinaries(CLProgram program, ByteBuffer[] target) {
            program.checkValid();
            if (target == null) {
                PointerBuffer sizes = this.getSizesBuffer(program, 4453);
                target = new ByteBuffer[sizes.remaining()];
                for (int i = 0; i < sizes.remaining(); ++i) {
                    target[i] = BufferUtils.createByteBuffer((int)sizes.get(i));
                }
            } else if (LWJGLUtil.DEBUG) {
                PointerBuffer sizes = this.getSizesBuffer(program, 4453);
                if (target.length < sizes.remaining()) {
                    throw new IllegalArgumentException("The target array is not big enough: " + sizes.remaining() + " buffers are required.");
                }
                for (int i = 0; i < target.length; ++i) {
                    BufferChecks.checkBuffer(target[i], (int)sizes.get(i));
                }
            }
            CL10.clGetProgramInfo(program, target, null);
            return target;
        }

        @Override
        public String getBuildInfoString(CLProgram program, CLDevice device, int param_name) {
            program.checkValid();
            int bytes = CLProgramUtil.getBuildSizeRet(program, device, param_name);
            if (bytes <= 1) {
                return null;
            }
            ByteBuffer buffer = APIUtil.getBufferByte(bytes);
            CL10.clGetProgramBuildInfo(program, device, param_name, buffer, null);
            buffer.limit(bytes - 1);
            return APIUtil.getString(buffer);
        }

        @Override
        public int getBuildInfoInt(CLProgram program, CLDevice device, int param_name) {
            program.checkValid();
            ByteBuffer buffer = APIUtil.getBufferByte(4);
            CL10.clGetProgramBuildInfo(program, device, param_name, buffer, null);
            return buffer.getInt(0);
        }

        private static int getBuildSizeRet(CLProgram program, CLDevice device, int param_name) {
            PointerBuffer bytes = APIUtil.getBufferPointer();
            int errcode = CL10.clGetProgramBuildInfo(program, device, param_name, null, bytes);
            if (errcode != 0) {
                throw new IllegalArgumentException("Invalid parameter specified: " + LWJGLUtil.toHexString(param_name));
            }
            return (int)bytes.get(0);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static final class CLPlatformUtil
    extends InfoUtilAbstract<CLPlatform>
    implements CLPlatform.CLPlatformUtil {
        private CLPlatformUtil() {
        }

        @Override
        protected int getInfo(CLPlatform platform, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
            return CL10.clGetPlatformInfo(platform, param_name, param_value, param_value_size_ret);
        }

        @Override
        public List<CLPlatform> getPlatforms(Filter<CLPlatform> filter) {
            IntBuffer numBuffer = APIUtil.getBufferInt();
            CL10.clGetPlatformIDs(null, numBuffer);
            int num_platforms = numBuffer.get(0);
            if (num_platforms == 0) {
                return null;
            }
            PointerBuffer platformIDs = APIUtil.getBufferPointer(num_platforms);
            CL10.clGetPlatformIDs(platformIDs, null);
            ArrayList<CLPlatform> platforms = new ArrayList<CLPlatform>(num_platforms);
            for (int i = 0; i < num_platforms; ++i) {
                CLPlatform platform = CLPlatform.getCLPlatform(platformIDs.get(i));
                if (filter != null && !filter.accept(platform)) continue;
                platforms.add(platform);
            }
            return platforms.size() == 0 ? null : platforms;
        }

        @Override
        public List<CLDevice> getDevices(CLPlatform platform, int device_type, Filter<CLDevice> filter) {
            platform.checkValid();
            IntBuffer numBuffer = APIUtil.getBufferInt();
            CL10.clGetDeviceIDs(platform, device_type, null, numBuffer);
            int num_devices = numBuffer.get(0);
            if (num_devices == 0) {
                return null;
            }
            PointerBuffer deviceIDs = APIUtil.getBufferPointer(num_devices);
            CL10.clGetDeviceIDs(platform, device_type, deviceIDs, null);
            ArrayList<CLDevice> devices = new ArrayList<CLDevice>(num_devices);
            for (int i = 0; i < num_devices; ++i) {
                CLDevice device = platform.getCLDevice(deviceIDs.get(i));
                if (filter != null && !filter.accept(device)) continue;
                devices.add(device);
            }
            return devices.size() == 0 ? null : devices;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static final class CLMemUtil
    extends InfoUtilAbstract<CLMem>
    implements CLMem.CLMemUtil {
        private CLMemUtil() {
        }

        @Override
        protected int getInfo(CLMem mem, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
            return CL10.clGetMemObjectInfo(mem, param_name, param_value, param_value_size_ret);
        }

        @Override
        public CLMem createImage2D(CLContext context, long flags, CLImageFormat image_format, long image_width, long image_height, long image_row_pitch, Buffer host_ptr, IntBuffer errcode_ret) {
            ByteBuffer formatBuffer = APIUtil.getBufferByte(8);
            formatBuffer.putInt(0, image_format.getChannelOrder());
            formatBuffer.putInt(4, image_format.getChannelType());
            long function_pointer = CLCapabilities.clCreateImage2D;
            BufferChecks.checkFunctionAddress(function_pointer);
            if (errcode_ret != null) {
                BufferChecks.checkBuffer(errcode_ret, 1);
            } else if (LWJGLUtil.DEBUG) {
                errcode_ret = APIUtil.getBufferInt();
            }
            CLMem __result = new CLMem(CL10.nclCreateImage2D(context.getPointer(), flags, MemoryUtil.getAddress(formatBuffer, 0), image_width, image_height, image_row_pitch, MemoryUtil.getAddress0Safe(host_ptr) + (long)(host_ptr != null ? BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage2DSize(formatBuffer, image_width, image_height, image_row_pitch)) : 0), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
            if (LWJGLUtil.DEBUG) {
                Util.checkCLError(errcode_ret.get(0));
            }
            return __result;
        }

        @Override
        public CLMem createImage3D(CLContext context, long flags, CLImageFormat image_format, long image_width, long image_height, long image_depth, long image_row_pitch, long image_slice_pitch, Buffer host_ptr, IntBuffer errcode_ret) {
            ByteBuffer formatBuffer = APIUtil.getBufferByte(8);
            formatBuffer.putInt(0, image_format.getChannelOrder());
            formatBuffer.putInt(4, image_format.getChannelType());
            long function_pointer = CLCapabilities.clCreateImage3D;
            BufferChecks.checkFunctionAddress(function_pointer);
            if (errcode_ret != null) {
                BufferChecks.checkBuffer(errcode_ret, 1);
            } else if (LWJGLUtil.DEBUG) {
                errcode_ret = APIUtil.getBufferInt();
            }
            CLMem __result = new CLMem(CL10.nclCreateImage3D(context.getPointer(), flags, MemoryUtil.getAddress(formatBuffer, 0), image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, MemoryUtil.getAddress0Safe(host_ptr) + (long)(host_ptr != null ? BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage3DSize(formatBuffer, image_width, image_height, image_depth, image_row_pitch, image_slice_pitch)) : 0), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
            if (LWJGLUtil.DEBUG) {
                Util.checkCLError(errcode_ret.get(0));
            }
            return __result;
        }

        @Override
        public CLMem createSubBuffer(CLMem mem, long flags, int buffer_create_type, CLBufferRegion buffer_create_info, IntBuffer errcode_ret) {
            PointerBuffer infoBuffer = APIUtil.getBufferPointer(2);
            infoBuffer.put(buffer_create_info.getOrigin());
            infoBuffer.put(buffer_create_info.getSize());
            return CL11.clCreateSubBuffer(mem, flags, buffer_create_type, infoBuffer.getBuffer(), errcode_ret);
        }

        @Override
        public ByteBuffer getInfoHostBuffer(CLMem mem) {
            long mem_flags;
            mem.checkValid();
            if (LWJGLUtil.DEBUG && ((mem_flags = this.getInfoLong(mem, 4353)) & 8L) != 8L) {
                throw new IllegalArgumentException("The specified CLMem object does not use host memory.");
            }
            long size = this.getInfoSize(mem, 4354);
            if (size == 0L) {
                return null;
            }
            long address = this.getInfoSize(mem, 4355);
            return CL.getHostBuffer(address, (int)size);
        }

        @Override
        public long getImageInfoSize(CLMem mem, int param_name) {
            mem.checkValid();
            PointerBuffer buffer = APIUtil.getBufferPointer();
            CL10.clGetImageInfo(mem, param_name, buffer.getBuffer(), null);
            return buffer.get(0);
        }

        @Override
        public CLImageFormat getImageInfoFormat(CLMem mem) {
            mem.checkValid();
            ByteBuffer format = APIUtil.getBufferByte(8);
            CL10.clGetImageInfo(mem, 4368, format, null);
            return new CLImageFormat(format.getInt(0), format.getInt(4));
        }

        @Override
        public int getImageInfoFormat(CLMem mem, int index) {
            mem.checkValid();
            ByteBuffer format = APIUtil.getBufferByte(8);
            CL10.clGetImageInfo(mem, 4368, format, null);
            return format.getInt(index << 2);
        }

        @Override
        public int getGLObjectType(CLMem mem) {
            mem.checkValid();
            IntBuffer buffer = APIUtil.getBufferInt();
            CL10GL.clGetGLObjectInfo(mem, buffer, null);
            return buffer.get(0);
        }

        @Override
        public int getGLObjectName(CLMem mem) {
            mem.checkValid();
            IntBuffer buffer = APIUtil.getBufferInt();
            CL10GL.clGetGLObjectInfo(mem, null, buffer);
            return buffer.get(0);
        }

        @Override
        public int getGLTextureInfoInt(CLMem mem, int param_name) {
            mem.checkValid();
            ByteBuffer buffer = APIUtil.getBufferByte(4);
            CL10GL.clGetGLTextureInfo(mem, param_name, buffer, null);
            return buffer.getInt(0);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static final class CLKernelUtil
    extends InfoUtilAbstract<CLKernel>
    implements CLKernel.CLKernelUtil {
        private CLKernelUtil() {
        }

        @Override
        public void setArg(CLKernel kernel, int index, byte value) {
            CL10.clSetKernelArg(kernel, index, 1L, APIUtil.getBufferByte(1).put(0, value));
        }

        @Override
        public void setArg(CLKernel kernel, int index, short value) {
            CL10.clSetKernelArg(kernel, index, 2L, APIUtil.getBufferShort().put(0, value));
        }

        @Override
        public void setArg(CLKernel kernel, int index, int value) {
            CL10.clSetKernelArg(kernel, index, 4L, APIUtil.getBufferInt().put(0, value));
        }

        @Override
        public void setArg(CLKernel kernel, int index, long value) {
            CL10.clSetKernelArg(kernel, index, 8L, APIUtil.getBufferLong().put(0, value));
        }

        @Override
        public void setArg(CLKernel kernel, int index, float value) {
            CL10.clSetKernelArg(kernel, index, 4L, APIUtil.getBufferFloat().put(0, value));
        }

        @Override
        public void setArg(CLKernel kernel, int index, double value) {
            CL10.clSetKernelArg(kernel, index, 8L, APIUtil.getBufferDouble().put(0, value));
        }

        @Override
        public void setArg(CLKernel kernel, int index, CLObject value) {
            CL10.clSetKernelArg(kernel, index, value);
        }

        @Override
        public void setArgSize(CLKernel kernel, int index, long size) {
            CL10.clSetKernelArg(kernel, index, size);
        }

        @Override
        protected int getInfo(CLKernel kernel, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
            return CL10.clGetKernelInfo(kernel, param_name, param_value, param_value_size_ret);
        }

        @Override
        public long getWorkGroupInfoSize(CLKernel kernel, CLDevice device, int param_name) {
            device.checkValid();
            PointerBuffer buffer = APIUtil.getBufferPointer();
            CL10.clGetKernelWorkGroupInfo(kernel, device, param_name, buffer.getBuffer(), null);
            return buffer.get(0);
        }

        @Override
        public long[] getWorkGroupInfoSizeArray(CLKernel kernel, CLDevice device, int param_name) {
            int size;
            device.checkValid();
            switch (param_name) {
                case 4529: {
                    size = 3;
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unsupported parameter: " + LWJGLUtil.toHexString(param_name));
                }
            }
            PointerBuffer buffer = APIUtil.getBufferPointer(size);
            CL10.clGetKernelWorkGroupInfo(kernel, device, param_name, buffer.getBuffer(), null);
            long[] array = new long[size];
            for (int i = 0; i < size; ++i) {
                array[i] = buffer.get(i);
            }
            return array;
        }

        @Override
        public long getWorkGroupInfoLong(CLKernel kernel, CLDevice device, int param_name) {
            device.checkValid();
            ByteBuffer buffer = APIUtil.getBufferByte(8);
            CL10.clGetKernelWorkGroupInfo(kernel, device, param_name, buffer, null);
            return buffer.getLong(0);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static final class CLEventUtil
    extends InfoUtilAbstract<CLEvent>
    implements CLEvent.CLEventUtil {
        private CLEventUtil() {
        }

        @Override
        protected int getInfo(CLEvent event, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
            return CL10.clGetEventInfo(event, param_name, param_value, param_value_size_ret);
        }

        @Override
        public long getProfilingInfoLong(CLEvent event, int param_name) {
            event.checkValid();
            ByteBuffer buffer = APIUtil.getBufferByte(8);
            CL10.clGetEventProfilingInfo(event, param_name, buffer, null);
            return buffer.getLong(0);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static final class CLDeviceUtil
    extends InfoUtilAbstract<CLDevice> {
        private CLDeviceUtil() {
        }

        @Override
        protected int getInfo(CLDevice device, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
            return CL10.clGetDeviceInfo(device, param_name, param_value, param_value_size_ret);
        }

        @Override
        protected int getInfoSizeArraySize(CLDevice device, int param_name) {
            switch (param_name) {
                case 4101: {
                    return this.getInfoInt(device, 4099);
                }
            }
            throw new IllegalArgumentException("Unsupported parameter: " + LWJGLUtil.toHexString(param_name));
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static final class CLContextUtil
    extends InfoUtilAbstract<CLContext>
    implements CLContext.CLContextUtil {
        private CLContextUtil() {
        }

        @Override
        protected int getInfo(CLContext context, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
            return CL10.clGetContextInfo(context, param_name, param_value, param_value_size_ret);
        }

        @Override
        public List<CLDevice> getInfoDevices(CLContext context) {
            int num_devices;
            context.checkValid();
            if (CLCapabilities.getPlatformCapabilities((CLPlatform)((CLPlatform)context.getParent())).OpenCL11) {
                num_devices = this.getInfoInt(context, 4227);
            } else {
                PointerBuffer size_ret = APIUtil.getBufferPointer();
                CL10.clGetContextInfo(context, 4225, null, size_ret);
                num_devices = (int)(size_ret.get(0) / (long)PointerBuffer.getPointerSize());
            }
            PointerBuffer deviceIDs = APIUtil.getBufferPointer(num_devices);
            CL10.clGetContextInfo(context, 4225, deviceIDs.getBuffer(), null);
            ArrayList<CLDevice> devices = new ArrayList<CLDevice>(num_devices);
            for (int i = 0; i < num_devices; ++i) {
                devices.add(((CLPlatform)context.getParent()).getCLDevice(deviceIDs.get(i)));
            }
            return devices.size() == 0 ? null : devices;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public CLContext create(CLPlatform platform, List<CLDevice> devices, CLContextCallback pfn_notify, Drawable share_drawable, IntBuffer errcode_ret) throws LWJGLException {
            int propertyCount = 2 + (share_drawable == null ? 0 : 4) + 1;
            PointerBuffer properties = APIUtil.getBufferPointer(propertyCount + devices.size());
            properties.put(4228L).put(platform);
            if (share_drawable != null) {
                share_drawable.setCLSharingProperties(properties);
            }
            properties.put(0L);
            properties.position(propertyCount);
            for (CLDevice device : devices) {
                properties.put(device);
            }
            long function_pointer = CLCapabilities.clCreateContext;
            BufferChecks.checkFunctionAddress(function_pointer);
            if (errcode_ret != null) {
                BufferChecks.checkBuffer(errcode_ret, 1);
            } else if (LWJGLUtil.DEBUG) {
                errcode_ret = APIUtil.getBufferInt();
            }
            long user_data = pfn_notify == null || pfn_notify.isCustom() ? 0L : CallbackUtil.createGlobalRef(pfn_notify);
            CLContext __result = null;
            try {
                __result = new CLContext(CL10.nclCreateContext(MemoryUtil.getAddress0(properties.getBuffer()), devices.size(), MemoryUtil.getAddress(properties, propertyCount), pfn_notify == null ? 0L : pfn_notify.getPointer(), user_data, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), platform);
                if (LWJGLUtil.DEBUG) {
                    Util.checkCLError(errcode_ret.get(0));
                }
                CLContext cLContext = __result;
                return cLContext;
            }
            finally {
                if (__result != null) {
                    __result.setContextCallback(user_data);
                }
            }
        }

        @Override
        public CLContext createFromType(CLPlatform platform, long device_type, CLContextCallback pfn_notify, Drawable share_drawable, IntBuffer errcode_ret) throws LWJGLException {
            int propertyCount = 2 + (share_drawable == null ? 0 : 4) + 1;
            PointerBuffer properties = APIUtil.getBufferPointer(propertyCount);
            properties.put(4228L).put(platform);
            if (share_drawable != null) {
                share_drawable.setCLSharingProperties(properties);
            }
            properties.put(0L);
            properties.flip();
            return CL10.clCreateContextFromType(properties, device_type, pfn_notify, errcode_ret);
        }

        @Override
        public List<CLImageFormat> getSupportedImageFormats(CLContext context, long flags, int image_type, Filter<CLImageFormat> filter) {
            IntBuffer numBuffer = APIUtil.getBufferInt();
            CL10.clGetSupportedImageFormats(context, flags, image_type, null, numBuffer);
            int num_image_formats = numBuffer.get(0);
            if (num_image_formats == 0) {
                return null;
            }
            ByteBuffer formatBuffer = BufferUtils.createByteBuffer(num_image_formats * 8);
            CL10.clGetSupportedImageFormats(context, flags, image_type, formatBuffer, null);
            ArrayList<CLImageFormat> formats = new ArrayList<CLImageFormat>(num_image_formats);
            for (int i = 0; i < num_image_formats; ++i) {
                int offset = num_image_formats * 8;
                CLImageFormat format = new CLImageFormat(formatBuffer.getInt(offset), formatBuffer.getInt(offset + 4));
                if (filter != null && !filter.accept(format)) continue;
                formats.add(format);
            }
            return formats.size() == 0 ? null : formats;
        }
    }
}

