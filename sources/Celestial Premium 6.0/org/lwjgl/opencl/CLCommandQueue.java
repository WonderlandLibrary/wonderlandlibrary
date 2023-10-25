/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CLContext;
import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opencl.CLEvent;
import org.lwjgl.opencl.CLObjectChild;
import org.lwjgl.opencl.CLObjectRegistry;
import org.lwjgl.opencl.CLPlatform;
import org.lwjgl.opencl.InfoUtil;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class CLCommandQueue
extends CLObjectChild<CLContext> {
    private static final InfoUtil<CLCommandQueue> util = CLPlatform.getInfoUtilInstance(CLCommandQueue.class, "CL_COMMAND_QUEUE_UTIL");
    private final CLDevice device;
    private final CLObjectRegistry<CLEvent> clEvents;

    CLCommandQueue(long pointer, CLContext context, CLDevice device) {
        super(pointer, context);
        if (this.isValid()) {
            this.device = device;
            this.clEvents = new CLObjectRegistry();
            context.getCLCommandQueueRegistry().registerObject(this);
        } else {
            this.device = null;
            this.clEvents = null;
        }
    }

    public CLDevice getCLDevice() {
        return this.device;
    }

    public CLEvent getCLEvent(long id) {
        return this.clEvents.getObject(id);
    }

    public int getInfoInt(int param_name) {
        return util.getInfoInt(this, param_name);
    }

    CLObjectRegistry<CLEvent> getCLEventRegistry() {
        return this.clEvents;
    }

    void registerCLEvent(PointerBuffer event) {
        if (event != null) {
            new CLEvent(event.get(event.position()), this);
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
                ((CLContext)this.getParent()).getCLCommandQueueRegistry().unregisterObject(this);
            }
        }
    }
}

