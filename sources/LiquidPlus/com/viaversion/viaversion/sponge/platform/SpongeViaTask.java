/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.spongepowered.api.scheduler.ScheduledTask
 */
package com.viaversion.viaversion.sponge.platform;

import com.viaversion.viaversion.api.platform.PlatformTask;
import org.spongepowered.api.scheduler.ScheduledTask;

public class SpongeViaTask
implements PlatformTask<ScheduledTask> {
    private final ScheduledTask task;

    public SpongeViaTask(ScheduledTask task) {
        this.task = task;
    }

    @Override
    public ScheduledTask getObject() {
        return this.task;
    }

    @Override
    public void cancel() {
        this.task.cancel();
    }
}

