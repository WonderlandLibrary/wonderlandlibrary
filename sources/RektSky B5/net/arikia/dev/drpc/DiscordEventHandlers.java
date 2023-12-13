/*
 * Decompiled with CFR 0.152.
 */
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
extends Structure {
    public ReadyCallback ready;
    public DisconnectedCallback disconnected;
    public ErroredCallback errored;
    public JoinGameCallback joinGame;
    public SpectateGameCallback spectateGame;
    public JoinRequestCallback joinRequest;

    @Override
    public List<String> getFieldOrder() {
        return Arrays.asList("ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest");
    }

    public static class Builder {
        DiscordEventHandlers h = new DiscordEventHandlers();

        public Builder setReadyEventHandler(ReadyCallback r2) {
            this.h.ready = r2;
            return this;
        }

        public Builder setDisconnectedEventHandler(DisconnectedCallback d2) {
            this.h.disconnected = d2;
            return this;
        }

        public Builder setErroredEventHandler(ErroredCallback e2) {
            this.h.errored = e2;
            return this;
        }

        public Builder setJoinGameEventHandler(JoinGameCallback j2) {
            this.h.joinGame = j2;
            return this;
        }

        public Builder setSpectateGameEventHandler(SpectateGameCallback s2) {
            this.h.spectateGame = s2;
            return this;
        }

        public Builder setJoinRequestEventHandler(JoinRequestCallback j2) {
            this.h.joinRequest = j2;
            return this;
        }

        public DiscordEventHandlers build() {
            return this.h;
        }
    }
}

