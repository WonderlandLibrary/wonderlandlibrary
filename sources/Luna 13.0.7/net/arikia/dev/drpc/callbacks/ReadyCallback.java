package net.arikia.dev.drpc.callbacks;

import com.sun.jna.Callback;

public abstract interface ReadyCallback
  extends Callback
{
  public abstract void apply();
}
