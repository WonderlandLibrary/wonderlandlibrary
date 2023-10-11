package net.arikia.dev.drpc;

import com.sun.jna.*;
import net.arikia.dev.drpc.callbacks.*;
import java.util.*;

public class DiscordEventHandlers extends Structure
{
    public ReadyCallback ready;
    public DisconnectedCallback disconnected;
    public ErroredCallback errored;
    public JoinGameCallback joinGame;
    public SpectateGameCallback spectateGame;
    public JoinRequestCallback joinRequest;
    
    public List<String> getFieldOrder() {
        return Arrays.asList("ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest");
    }
    
    public static class Builder
    {
        DiscordEventHandlers h;
        
        public Builder() {
            this.h = new DiscordEventHandlers();
        }
        
        public Builder setReadyEventHandler(final ReadyCallback r) {
            this.h.ready = r;
            return this;
        }
        
        public Builder setDisconnectedEventHandler(final DisconnectedCallback d) {
            this.h.disconnected = d;
            return this;
        }
        
        public Builder setErroredEventHandler(final ErroredCallback e) {
            this.h.errored = e;
            return this;
        }
        
        public Builder setJoinGameEventHandler(final JoinGameCallback j) {
            this.h.joinGame = j;
            return this;
        }
        
        public Builder setSpectateGameEventHandler(final SpectateGameCallback s) {
            this.h.spectateGame = s;
            return this;
        }
        
        public Builder setJoinRequestEventHandler(final JoinRequestCallback j) {
            this.h.joinRequest = j;
            return this;
        }
        
        public DiscordEventHandlers build() {
            return this.h;
        }
    }
}
