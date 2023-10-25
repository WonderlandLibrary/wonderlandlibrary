/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opencl.CLObject;
import org.lwjgl.opencl.CLObjectChild;
import org.lwjgl.opencl.CLPlatform;
import org.lwjgl.opencl.CLProgram;
import org.lwjgl.opencl.InfoUtil;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class CLKernel
extends CLObjectChild<CLProgram> {
    private static final CLKernelUtil util = (CLKernelUtil)CLPlatform.getInfoUtilInstance(CLKernel.class, "CL_KERNEL_UTIL");

    CLKernel(long pointer, CLProgram program) {
        super(pointer, program);
        if (this.isValid()) {
            program.getCLKernelRegistry().registerObject(this);
        }
    }

    public CLKernel setArg(int index, byte value) {
        util.setArg(this, index, value);
        return this;
    }

    public CLKernel setArg(int index, short value) {
        util.setArg(this, index, value);
        return this;
    }

    public CLKernel setArg(int index, int value) {
        util.setArg(this, index, value);
        return this;
    }

    public CLKernel setArg(int index, long value) {
        util.setArg(this, index, value);
        return this;
    }

    public CLKernel setArg(int index, float value) {
        util.setArg(this, index, value);
        return this;
    }

    public CLKernel setArg(int index, double value) {
        util.setArg(this, index, value);
        return this;
    }

    public CLKernel setArg(int index, CLObject value) {
        util.setArg(this, index, value);
        return this;
    }

    public CLKernel setArgSize(int index, long size) {
        util.setArgSize(this, index, size);
        return this;
    }

    public String getInfoString(int param_name) {
        return util.getInfoString(this, param_name);
    }

    public int getInfoInt(int param_name) {
        return util.getInfoInt(this, param_name);
    }

    public long getWorkGroupInfoSize(CLDevice device, int param_name) {
        return util.getWorkGroupInfoSize(this, device, param_name);
    }

    public long[] getWorkGroupInfoSizeArray(CLDevice device, int param_name) {
        return util.getWorkGroupInfoSizeArray(this, device, param_name);
    }

    public long getWorkGroupInfoLong(CLDevice device, int param_name) {
        return util.getWorkGroupInfoLong(this, device, param_name);
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
                ((CLProgram)this.getParent()).getCLKernelRegistry().unregisterObject(this);
            }
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static interface CLKernelUtil
    extends InfoUtil<CLKernel> {
        public void setArg(CLKernel var1, int var2, byte var3);

        public void setArg(CLKernel var1, int var2, short var3);

        public void setArg(CLKernel var1, int var2, int var3);

        public void setArg(CLKernel var1, int var2, long var3);

        public void setArg(CLKernel var1, int var2, float var3);

        public void setArg(CLKernel var1, int var2, double var3);

        public void setArg(CLKernel var1, int var2, CLObject var3);

        public void setArgSize(CLKernel var1, int var2, long var3);

        public long getWorkGroupInfoSize(CLKernel var1, CLDevice var2, int var3);

        public long[] getWorkGroupInfoSizeArray(CLKernel var1, CLDevice var2, int var3);

        public long getWorkGroupInfoLong(CLKernel var1, CLDevice var2, int var3);
    }
}

