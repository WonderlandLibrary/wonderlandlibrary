package net.minecraft.util;

import com.google.common.util.concurrent.ListenableFuture;

public abstract interface IThreadListener
{
  public abstract ListenableFuture addScheduledTask(Runnable paramRunnable);
  
  public abstract boolean isCallingFromMinecraftThread();
}
