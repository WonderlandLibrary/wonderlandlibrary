package net.arikia.dev.drpc;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
import net.arikia.dev.drpc.callbacks.DisconnectedCallback;
import net.arikia.dev.drpc.callbacks.ErroredCallback;
import net.arikia.dev.drpc.callbacks.JoinGameCallback;
import net.arikia.dev.drpc.callbacks.JoinRequestCallback;
import net.arikia.dev.drpc.callbacks.ReadyCallback;
import net.arikia.dev.drpc.callbacks.SpectateGameCallback;

public class DiscordEventHandlers
  extends Structure
{
  public ReadyCallback ready;
  public DisconnectedCallback disconnected;
  public ErroredCallback errored;
  public JoinGameCallback joinGame;
  public SpectateGameCallback spectateGame;
  public JoinRequestCallback joinRequest;
  
  public DiscordEventHandlers() {}
  
  public List<String> getFieldOrder()
  {
    return Arrays.asList(new String[] { "ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest" });
  }
}
