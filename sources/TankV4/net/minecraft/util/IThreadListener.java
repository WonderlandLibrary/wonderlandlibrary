package net.minecraft.util;

import com.google.common.util.concurrent.ListenableFuture;

public interface IThreadListener {
   boolean isCallingFromMinecraftThread();

   ListenableFuture addScheduledTask(Runnable var1);
}
