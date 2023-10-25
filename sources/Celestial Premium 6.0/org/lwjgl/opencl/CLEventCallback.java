/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import org.lwjgl.PointerWrapperAbstract;
import org.lwjgl.opencl.CLEvent;
import org.lwjgl.opencl.CLObjectRegistry;
import org.lwjgl.opencl.CallbackUtil;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class CLEventCallback
extends PointerWrapperAbstract {
    private CLObjectRegistry<CLEvent> eventRegistry;

    protected CLEventCallback() {
        super(CallbackUtil.getEventCallback());
    }

    void setRegistry(CLObjectRegistry<CLEvent> eventRegistry) {
        this.eventRegistry = eventRegistry;
    }

    private void handleMessage(long event_address, int event_command_exec_status) {
        this.handleMessage(this.eventRegistry.getObject(event_address), event_command_exec_status);
    }

    protected abstract void handleMessage(CLEvent var1, int var2);
}

