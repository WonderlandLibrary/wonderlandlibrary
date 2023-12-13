// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.sponge.platform;

import org.spongepowered.api.scheduler.ScheduledTask;
import com.viaversion.viaversion.api.platform.PlatformTask;

public class SpongeViaTask implements PlatformTask<ScheduledTask>
{
    private final ScheduledTask task;
    
    public SpongeViaTask(final ScheduledTask task) {
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
