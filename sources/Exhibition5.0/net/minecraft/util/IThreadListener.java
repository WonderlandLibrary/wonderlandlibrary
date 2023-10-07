// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import com.google.common.util.concurrent.ListenableFuture;

public interface IThreadListener
{
    ListenableFuture addScheduledTask(final Runnable p0);
    
    boolean isCallingFromMinecraftThread();
}
