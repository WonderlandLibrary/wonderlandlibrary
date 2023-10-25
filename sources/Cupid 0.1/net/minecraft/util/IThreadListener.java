package net.minecraft.util;

import com.google.common.util.concurrent.ListenableFuture;

public interface IThreadListener {
  ListenableFuture<Object> addScheduledTask(Runnable paramRunnable);
  
  boolean isCallingFromMinecraftThread();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\IThreadListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */