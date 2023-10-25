/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CLContext;
import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opencl.CLKernel;
import org.lwjgl.opencl.CLObjectChild;
import org.lwjgl.opencl.CLObjectRegistry;
import org.lwjgl.opencl.CLPlatform;
import org.lwjgl.opencl.InfoUtil;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class CLProgram
extends CLObjectChild<CLContext> {
    private static final CLProgramUtil util = (CLProgramUtil)CLPlatform.getInfoUtilInstance(CLProgram.class, "CL_PROGRAM_UTIL");
    private final CLObjectRegistry<CLKernel> clKernels;

    CLProgram(long pointer, CLContext context) {
        super(pointer, context);
        if (this.isValid()) {
            context.getCLProgramRegistry().registerObject(this);
            this.clKernels = new CLObjectRegistry();
        } else {
            this.clKernels = null;
        }
    }

    public CLKernel getCLKernel(long id) {
        return this.clKernels.getObject(id);
    }

    public CLKernel[] createKernelsInProgram() {
        return util.createKernelsInProgram(this);
    }

    public String getInfoString(int param_name) {
        return util.getInfoString(this, param_name);
    }

    public int getInfoInt(int param_name) {
        return util.getInfoInt(this, param_name);
    }

    public long[] getInfoSizeArray(int param_name) {
        return util.getInfoSizeArray(this, param_name);
    }

    public CLDevice[] getInfoDevices() {
        return util.getInfoDevices(this);
    }

    public ByteBuffer getInfoBinaries(ByteBuffer target) {
        return util.getInfoBinaries(this, target);
    }

    public ByteBuffer[] getInfoBinaries(ByteBuffer[] target) {
        return util.getInfoBinaries(this, target);
    }

    public String getBuildInfoString(CLDevice device, int param_name) {
        return util.getBuildInfoString(this, device, param_name);
    }

    public int getBuildInfoInt(CLDevice device, int param_name) {
        return util.getBuildInfoInt(this, device, param_name);
    }

    CLObjectRegistry<CLKernel> getCLKernelRegistry() {
        return this.clKernels;
    }

    void registerCLKernels(PointerBuffer kernels) {
        for (int i = kernels.position(); i < kernels.limit(); ++i) {
            long pointer = kernels.get(i);
            if (pointer == 0L) continue;
            new CLKernel(pointer, this);
        }
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
                ((CLContext)this.getParent()).getCLProgramRegistry().unregisterObject(this);
            }
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static interface CLProgramUtil
    extends InfoUtil<CLProgram> {
        public CLKernel[] createKernelsInProgram(CLProgram var1);

        public CLDevice[] getInfoDevices(CLProgram var1);

        public ByteBuffer getInfoBinaries(CLProgram var1, ByteBuffer var2);

        public ByteBuffer[] getInfoBinaries(CLProgram var1, ByteBuffer[] var2);

        public String getBuildInfoString(CLProgram var1, CLDevice var2, int var3);

        public int getBuildInfoInt(CLProgram var1, CLDevice var2, int var3);
    }
}

