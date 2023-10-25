/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import org.lwjgl.opencl.CLCommandQueue;
import org.lwjgl.opencl.CLContext;
import org.lwjgl.opencl.CLObjectChild;
import org.lwjgl.opencl.CLObjectRegistry;
import org.lwjgl.opencl.CLPlatform;
import org.lwjgl.opencl.InfoUtil;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class CLEvent
extends CLObjectChild<CLContext> {
    private static final CLEventUtil util = (CLEventUtil)CLPlatform.getInfoUtilInstance(CLEvent.class, "CL_EVENT_UTIL");
    private final CLCommandQueue queue;

    CLEvent(long pointer, CLContext context) {
        this(pointer, context, null);
    }

    CLEvent(long pointer, CLCommandQueue queue) {
        this(pointer, (CLContext)queue.getParent(), queue);
    }

    CLEvent(long pointer, CLContext context, CLCommandQueue queue) {
        super(pointer, context);
        if (this.isValid()) {
            this.queue = queue;
            if (queue == null) {
                context.getCLEventRegistry().registerObject(this);
            } else {
                queue.getCLEventRegistry().registerObject(this);
            }
        } else {
            this.queue = null;
        }
    }

    public CLCommandQueue getCLCommandQueue() {
        return this.queue;
    }

    public int getInfoInt(int param_name) {
        return util.getInfoInt(this, param_name);
    }

    public long getProfilingInfoLong(int param_name) {
        return util.getProfilingInfoLong(this, param_name);
    }

    CLObjectRegistry<CLEvent> getParentRegistry() {
        if (this.queue == null) {
            return ((CLContext)this.getParent()).getCLEventRegistry();
        }
        return this.queue.getCLEventRegistry();
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
                if (this.queue == null) {
                    ((CLContext)this.getParent()).getCLEventRegistry().unregisterObject(this);
                } else {
                    this.queue.getCLEventRegistry().unregisterObject(this);
                }
            }
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static interface CLEventUtil
    extends InfoUtil<CLEvent> {
        public long getProfilingInfoLong(CLEvent var1, int var2);
    }
}

