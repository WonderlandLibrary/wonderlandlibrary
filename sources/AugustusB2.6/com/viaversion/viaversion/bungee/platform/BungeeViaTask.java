// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bungee.platform;

import net.md_5.bungee.api.scheduler.ScheduledTask;
import com.viaversion.viaversion.api.platform.PlatformTask;

public class BungeeViaTask implements PlatformTask<ScheduledTask>
{
    private final ScheduledTask task;
    
    public BungeeViaTask(final ScheduledTask task) {
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
