package net.arikia.dev.drpc.callbacks;

import com.sun.jna.Callback;
import net.arikia.dev.drpc.DiscordJoinRequest;

public abstract interface JoinRequestCallback
  extends Callback
{
  public abstract void apply(DiscordJoinRequest paramDiscordJoinRequest);
}
