/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import java.nio.IntBuffer;
import java.util.List;
import org.lwjgl.LWJGLException;
import org.lwjgl.opencl.CLCommandQueue;
import org.lwjgl.opencl.CLContextCallback;
import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opencl.CLEvent;
import org.lwjgl.opencl.CLMem;
import org.lwjgl.opencl.CLObjectChild;
import org.lwjgl.opencl.CLObjectRegistry;
import org.lwjgl.opencl.CLPlatform;
import org.lwjgl.opencl.CLProgram;
import org.lwjgl.opencl.CLSampler;
import org.lwjgl.opencl.CallbackUtil;
import org.lwjgl.opencl.InfoUtil;
import org.lwjgl.opencl.api.CLImageFormat;
import org.lwjgl.opencl.api.Filter;
import org.lwjgl.opengl.Drawable;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class CLContext
extends CLObjectChild<CLPlatform> {
    private static final CLContextUtil util = (CLContextUtil)CLPlatform.getInfoUtilInstance(CLContext.class, "CL_CONTEXT_UTIL");
    private final CLObjectRegistry<CLCommandQueue> clCommandQueues;
    private final CLObjectRegistry<CLMem> clMems;
    private final CLObjectRegistry<CLSampler> clSamplers;
    private final CLObjectRegistry<CLProgram> clPrograms;
    private final CLObjectRegistry<CLEvent> clEvents;
    private long contextCallback;
    private long printfCallback;

    CLContext(long pointer, CLPlatform platform) {
        super(pointer, platform);
        if (this.isValid()) {
            this.clCommandQueues = new CLObjectRegistry();
            this.clMems = new CLObjectRegistry();
            this.clSamplers = new CLObjectRegistry();
            this.clPrograms = new CLObjectRegistry();
            this.clEvents = new CLObjectRegistry();
        } else {
            this.clCommandQueues = null;
            this.clMems = null;
            this.clSamplers = null;
            this.clPrograms = null;
            this.clEvents = null;
        }
    }

    public CLCommandQueue getCLCommandQueue(long id) {
        return this.clCommandQueues.getObject(id);
    }

    public CLMem getCLMem(long id) {
        return this.clMems.getObject(id);
    }

    public CLSampler getCLSampler(long id) {
        return this.clSamplers.getObject(id);
    }

    public CLProgram getCLProgram(long id) {
        return this.clPrograms.getObject(id);
    }

    public CLEvent getCLEvent(long id) {
        return this.clEvents.getObject(id);
    }

    public static CLContext create(CLPlatform platform, List<CLDevice> devices, IntBuffer errcode_ret) throws LWJGLException {
        return CLContext.create(platform, devices, null, null, errcode_ret);
    }

    public static CLContext create(CLPlatform platform, List<CLDevice> devices, CLContextCallback pfn_notify, IntBuffer errcode_ret) throws LWJGLException {
        return CLContext.create(platform, devices, pfn_notify, null, errcode_ret);
    }

    public static CLContext create(CLPlatform platform, List<CLDevice> devices, CLContextCallback pfn_notify, Drawable share_drawable, IntBuffer errcode_ret) throws LWJGLException {
        return util.create(platform, devices, pfn_notify, share_drawable, errcode_ret);
    }

    public static CLContext createFromType(CLPlatform platform, long device_type, IntBuffer errcode_ret) throws LWJGLException {
        return util.createFromType(platform, device_type, null, null, errcode_ret);
    }

    public static CLContext createFromType(CLPlatform platform, long device_type, CLContextCallback pfn_notify, IntBuffer errcode_ret) throws LWJGLException {
        return util.createFromType(platform, device_type, pfn_notify, null, errcode_ret);
    }

    public static CLContext createFromType(CLPlatform platform, long device_type, CLContextCallback pfn_notify, Drawable share_drawable, IntBuffer errcode_ret) throws LWJGLException {
        return util.createFromType(platform, device_type, pfn_notify, share_drawable, errcode_ret);
    }

    public int getInfoInt(int param_name) {
        return util.getInfoInt(this, param_name);
    }

    public List<CLDevice> getInfoDevices() {
        return util.getInfoDevices(this);
    }

    public List<CLImageFormat> getSupportedImageFormats(long flags, int image_type) {
        return this.getSupportedImageFormats(flags, image_type, null);
    }

    public List<CLImageFormat> getSupportedImageFormats(long flags, int image_type, Filter<CLImageFormat> filter) {
        return util.getSupportedImageFormats(this, flags, image_type, filter);
    }

    CLObjectRegistry<CLCommandQueue> getCLCommandQueueRegistry() {
        return this.clCommandQueues;
    }

    CLObjectRegistry<CLMem> getCLMemRegistry() {
        return this.clMems;
    }

    CLObjectRegistry<CLSampler> getCLSamplerRegistry() {
        return this.clSamplers;
    }

    CLObjectRegistry<CLProgram> getCLProgramRegistry() {
        return this.clPrograms;
    }

    CLObjectRegistry<CLEvent> getCLEventRegistry() {
        return this.clEvents;
    }

    private boolean checkCallback(long callback, int result) {
        if (result == 0 && (callback == 0L || this.isValid())) {
            return true;
        }
        if (callback != 0L) {
            CallbackUtil.deleteGlobalRef(callback);
        }
        return false;
    }

    void setContextCallback(long callback) {
        if (this.checkCallback(callback, 0)) {
            this.contextCallback = callback;
        }
    }

    void setPrintfCallback(long callback, int result) {
        if (this.checkCallback(callback, result)) {
            this.printfCallback = callback;
        }
    }

    void releaseImpl() {
        if (this.release() > 0) {
            return;
        }
        if (this.contextCallback != 0L) {
            CallbackUtil.deleteGlobalRef(this.contextCallback);
        }
        if (this.printfCallback != 0L) {
            CallbackUtil.deleteGlobalRef(this.printfCallback);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static interface CLContextUtil
    extends InfoUtil<CLContext> {
        public List<CLDevice> getInfoDevices(CLContext var1);

        public CLContext create(CLPlatform var1, List<CLDevice> var2, CLContextCallback var3, Drawable var4, IntBuffer var5) throws LWJGLException;

        public CLContext createFromType(CLPlatform var1, long var2, CLContextCallback var4, Drawable var5, IntBuffer var6) throws LWJGLException;

        public List<CLImageFormat> getSupportedImageFormats(CLContext var1, long var2, int var4, Filter<CLImageFormat> var5);
    }
}

