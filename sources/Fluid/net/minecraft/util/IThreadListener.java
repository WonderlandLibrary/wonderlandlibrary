// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import com.google.common.util.concurrent.ListenableFuture;

public interface IThreadListener
{
    ListenableFuture<Object> addScheduledTask(final Runnable p0);
    
    boolean isCallingFromMinecraftThread();
}
