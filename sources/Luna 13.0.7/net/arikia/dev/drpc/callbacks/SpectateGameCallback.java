package net.arikia.dev.drpc.callbacks;

import com.sun.jna.Callback;

public abstract interface SpectateGameCallback
  extends Callback
{
  public abstract void apply(String paramString);
}
