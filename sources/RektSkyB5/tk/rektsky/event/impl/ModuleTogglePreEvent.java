/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.event.impl;

import tk.rektsky.event.Event;
import tk.rektsky.module.Module;

public class ModuleTogglePreEvent
extends Event {
    private Module module;
    private boolean enabled;
    private boolean canceled;

    public Module getModule() {
        return this.module;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public ModuleTogglePreEvent(Module module, boolean enabled) {
        this.module = module;
        this.enabled = enabled;
    }
}

