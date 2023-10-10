// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.platform;

import com.google.common.base.Preconditions;
import org.bukkit.scheduler.BukkitTask;
import com.viaversion.viaversion.api.platform.PlatformTask;

public class BukkitViaTask implements PlatformTask<BukkitTask>
{
    private final BukkitTask task;
    
    public BukkitViaTask(final BukkitTask task) {
        this.task = task;
    }
    
    @Override
    public BukkitTask getObject() {
        return this.task;
    }
    
    @Override
    public void cancel() {
        Preconditions.checkArgument(this.task != null, (Object)"Task cannot be cancelled");
        this.task.cancel();
    }
}
