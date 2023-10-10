// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.sponge.platform;

import org.spongepowered.api.scheduler.Task;
import com.viaversion.viaversion.api.platform.PlatformTask;

public class SpongeViaTask implements PlatformTask<Task>
{
    private final Task task;
    
    public SpongeViaTask(final Task task) {
        this.task = task;
    }
    
    @Override
    public Task getObject() {
        return this.task;
    }
    
    @Override
    public void cancel() {
        this.task.cancel();
    }
}
