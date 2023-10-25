/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opencl.CLContext;
import org.lwjgl.opencl.CLObjectChild;
import org.lwjgl.opencl.CLPlatform;
import org.lwjgl.opencl.InfoUtil;
import org.lwjgl.opencl.api.CLBufferRegion;
import org.lwjgl.opencl.api.CLImageFormat;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class CLMem
extends CLObjectChild<CLContext> {
    private static final CLMemUtil util = (CLMemUtil)CLPlatform.getInfoUtilInstance(CLMem.class, "CL_MEM_UTIL");

    CLMem(long pointer, CLContext context) {
        super(pointer, context);
        if (this.isValid()) {
            context.getCLMemRegistry().registerObject(this);
        }
    }

    public static CLMem createImage2D(CLContext context, long flags, CLImageFormat image_format, long image_width, long image_height, long image_row_pitch, Buffer host_ptr, IntBuffer errcode_ret) {
        return util.createImage2D(context, flags, image_format, image_width, image_height, image_row_pitch, host_ptr, errcode_ret);
    }

    public static CLMem createImage3D(CLContext context, long flags, CLImageFormat image_format, long image_width, long image_height, long image_depth, long image_row_pitch, long image_slice_pitch, Buffer host_ptr, IntBuffer errcode_ret) {
        return util.createImage3D(context, flags, image_format, image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, host_ptr, errcode_ret);
    }

    public CLMem createSubBuffer(long flags, int buffer_create_type, CLBufferRegion buffer_create_info, IntBuffer errcode_ret) {
        return util.createSubBuffer(this, flags, buffer_create_type, buffer_create_info, errcode_ret);
    }

    public int getInfoInt(int param_name) {
        return util.getInfoInt(this, param_name);
    }

    public long getInfoSize(int param_name) {
        return util.getInfoSize(this, param_name);
    }

    public long getInfoLong(int param_name) {
        return util.getInfoLong(this, param_name);
    }

    public ByteBuffer getInfoHostBuffer() {
        return util.getInfoHostBuffer(this);
    }

    public long getImageInfoSize(int param_name) {
        return util.getImageInfoSize(this, param_name);
    }

    public CLImageFormat getImageFormat() {
        return util.getImageInfoFormat(this);
    }

    public int getImageChannelOrder() {
        return util.getImageInfoFormat(this, 0);
    }

    public int getImageChannelType() {
        return util.getImageInfoFormat(this, 1);
    }

    public int getGLObjectType() {
        return util.getGLObjectType(this);
    }

    public int getGLObjectName() {
        return util.getGLObjectName(this);
    }

    public int getGLTextureInfoInt(int param_name) {
        return util.getGLTextureInfoInt(this, param_name);
    }

    static CLMem create(long pointer, CLContext context) {
        CLMem clMem = context.getCLMemRegistry().getObject(pointer);
        if (clMem == null) {
            clMem = new CLMem(pointer, context);
        } else {
            clMem.retain();
        }
        return clMem;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    int release() {
        try {
            int n = super.release();
            return n;
        }
        finally {
            if (!this.isValid()) {
                ((CLContext)this.getParent()).getCLMemRegistry().unregisterObject(this);
            }
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static interface CLMemUtil
    extends InfoUtil<CLMem> {
        public CLMem createImage2D(CLContext var1, long var2, CLImageFormat var4, long var5, long var7, long var9, Buffer var11, IntBuffer var12);

        public CLMem createImage3D(CLContext var1, long var2, CLImageFormat var4, long var5, long var7, long var9, long var11, long var13, Buffer var15, IntBuffer var16);

        public CLMem createSubBuffer(CLMem var1, long var2, int var4, CLBufferRegion var5, IntBuffer var6);

        public ByteBuffer getInfoHostBuffer(CLMem var1);

        public long getImageInfoSize(CLMem var1, int var2);

        public CLImageFormat getImageInfoFormat(CLMem var1);

        public int getImageInfoFormat(CLMem var1, int var2);

        public int getGLObjectType(CLMem var1);

        public int getGLObjectName(CLMem var1);

        public int getGLTextureInfoInt(CLMem var1, int var2);
    }
}

