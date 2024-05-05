package net.arikia.dev.drpc.callbacks;

import com.sun.jna.Callback;

public abstract interface DisconnectedCallback
  extends Callback
{
  public abstract void apply(int paramInt, String paramString);
}
