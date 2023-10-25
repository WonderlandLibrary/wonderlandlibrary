/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.openal;

import java.util.HashMap;
import org.lwjgl.openal.ALCcontext;

public final class ALCdevice {
    final long device;
    private boolean valid;
    private final HashMap<Long, ALCcontext> contexts = new HashMap();

    ALCdevice(long device) {
        this.device = device;
        this.valid = true;
    }

    public boolean equals(Object device) {
        if (device instanceof ALCdevice) {
            return ((ALCdevice)device).device == this.device;
        }
        return super.equals(device);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void addContext(ALCcontext context) {
        HashMap<Long, ALCcontext> hashMap = this.contexts;
        synchronized (hashMap) {
            this.contexts.put(context.context, context);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void removeContext(ALCcontext context) {
        HashMap<Long, ALCcontext> hashMap = this.contexts;
        synchronized (hashMap) {
            this.contexts.remove(context.context);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void setInvalid() {
        this.valid = false;
        HashMap<Long, ALCcontext> hashMap = this.contexts;
        synchronized (hashMap) {
            for (ALCcontext context : this.contexts.values()) {
                context.setInvalid();
            }
        }
        this.contexts.clear();
    }

    public boolean isValid() {
        return this.valid;
    }
}

