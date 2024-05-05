/*
 * Decompiled with CFR 0.152.
 */
package com.jagrosh.discordipc;

import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.Callback;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.User;
import com.jagrosh.discordipc.entities.pipe.Pipe;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import java.io.Closeable;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IPCClient
implements Closeable {
    private static final Logger LOGGER = LoggerFactory.getLogger(IPCClient.class);
    private final long clientId;
    private final HashMap<String, Callback> callbacks = new HashMap();
    private volatile Pipe pipe;
    private IPCListener listener = null;
    private Thread readThread = null;

    public IPCClient(long clientId) {
        this.clientId = clientId;
    }

    public void setListener(IPCListener listener) {
        this.listener = listener;
        if (this.pipe != null) {
            this.pipe.setListener(listener);
        }
    }

    public void connect(DiscordBuild ... preferredOrder) throws NoDiscordClientException {
        this.checkConnected(false);
        this.callbacks.clear();
        this.pipe = null;
        this.pipe = Pipe.openPipe(this, this.clientId, this.callbacks, preferredOrder);
        LOGGER.debug("Client is now connected and ready!");
        if (this.listener != null) {
            this.listener.onReady(this);
        }
        this.startReading();
    }

    public void sendRichPresence(RichPresence presence) {
        this.sendRichPresence(presence, null);
    }

    public void sendRichPresence(RichPresence presence, Callback callback) {
        this.checkConnected(true);
        LOGGER.debug("Sending RichPresence to discord: " + (presence == null ? null : presence.toJson().toString()));
        this.pipe.send(Packet.OpCode.FRAME, new JSONObject().put("cmd", "SET_ACTIVITY").put("args", new JSONObject().put("pid", IPCClient.getPID()).put("activity", presence == null ? null : presence.toJson())), callback);
    }

    public void subscribe(Event sub) {
        this.subscribe(sub, null);
    }

    public void subscribe(Event sub, Callback callback) {
        this.checkConnected(true);
        if (!sub.isSubscribable()) {
            throw new IllegalStateException("Cannot subscribe to " + (Object)((Object)sub) + " event!");
        }
        LOGGER.debug(String.format("Subscribing to Event: %s", sub.name()));
        this.pipe.send(Packet.OpCode.FRAME, new JSONObject().put("cmd", "SUBSCRIBE").put("evt", sub.name()), callback);
    }

    public PipeStatus getStatus() {
        if (this.pipe == null) {
            return PipeStatus.UNINITIALIZED;
        }
        return this.pipe.getStatus();
    }

    @Override
    public void close() {
        this.checkConnected(true);
        try {
            this.pipe.close();
        }
        catch (IOException e) {
            LOGGER.debug("Failed to close pipe", e);
        }
    }

    public DiscordBuild getDiscordBuild() {
        if (this.pipe == null) {
            return null;
        }
        return this.pipe.getDiscordBuild();
    }

    private void checkConnected(boolean connected) {
        if (connected && this.getStatus() != PipeStatus.CONNECTED) {
            throw new IllegalStateException(String.format("IPCClient (ID: %d) is not connected!", this.clientId));
        }
        if (!connected && this.getStatus() == PipeStatus.CONNECTED) {
            throw new IllegalStateException(String.format("IPCClient (ID: %d) is already connected!", this.clientId));
        }
    }

    private void startReading() {
        this.readThread = new Thread(this::lambda$startReading$0);
        LOGGER.debug("Starting IPCClient reading thread!");
        this.readThread.start();
    }

    private static int getPID() {
        String pr = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.parseInt(pr.substring(0, pr.indexOf(64)));
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private /* synthetic */ void lambda$startReading$0() {
        try {
            while (true) lbl-1000:
            // 5 sources

            {
                if ((p = this.pipe.read()).getOp() == Packet.OpCode.CLOSE) {
                    this.pipe.setStatus(PipeStatus.DISCONNECTED);
                    if (this.listener == null) return;
                    this.listener.onClose(this, p.getJson());
                    return;
                }
                json = p.getJson();
                event = Event.of(json.optString("evt", null));
                nonce = json.optString("nonce", null);
                switch (1.$SwitchMap$com$jagrosh$discordipc$IPCClient$Event[event.ordinal()]) {
                    case 1: {
                        if (nonce == null || !this.callbacks.containsKey(nonce)) break;
                        this.callbacks.remove(nonce).succeed(p);
                        break;
                    }
                    case 2: {
                        if (nonce == null || !this.callbacks.containsKey(nonce)) break;
                        this.callbacks.remove(nonce).fail(json.getJSONObject("data").optString("message", null));
                        break;
                    }
                    case 3: {
                        IPCClient.LOGGER.debug("Reading thread received a 'join' event.");
                        break;
                    }
                    case 4: {
                        IPCClient.LOGGER.debug("Reading thread received a 'spectate' event.");
                        break;
                    }
                    case 5: {
                        IPCClient.LOGGER.debug("Reading thread received a 'join request' event.");
                        break;
                    }
                    case 6: {
                        IPCClient.LOGGER.debug("Reading thread encountered an event with an unknown type: " + json.getString("evt"));
                        break;
                    }
                }
                if (this.listener == null || !json.has("cmd") || !json.getString("cmd").equals("DISPATCH")) continue;
                try {
                    data = json.getJSONObject("data");
                    switch (1.$SwitchMap$com$jagrosh$discordipc$IPCClient$Event[Event.of(json.getString("evt")).ordinal()]) {
                        case 3: {
                            this.listener.onActivityJoin(this, data.getString("secret"));
                            break;
                        }
                        case 4: {
                            this.listener.onActivitySpectate(this, data.getString("secret"));
                            break;
                        }
                        case 5: {
                            u = data.getJSONObject("user");
                            user = new User(u.getString("username"), u.getString("discriminator"), Long.parseLong(u.getString("id")), u.optString("avatar", null));
                            this.listener.onActivityJoinRequest(this, data.optString("secret", null), user);
                        }
                    }
                }
                catch (Exception e) {
                    IPCClient.LOGGER.error("Exception when handling event: ", e);
                    continue;
                }
                break;
            }
        }
        catch (IOException | JSONException ex) {
            if (ex instanceof IOException) {
                IPCClient.LOGGER.error("Reading thread encountered an IOException", ex);
            } else {
                IPCClient.LOGGER.error("Reading thread encountered an JSONException", ex);
            }
            this.pipe.setStatus(PipeStatus.DISCONNECTED);
            if (this.listener == null) return;
            this.listener.onDisconnect(this, ex);
        }
        ** GOTO lbl-1000
    }

    public static enum Event {
        NULL(false),
        READY(false),
        ERROR(false),
        ACTIVITY_JOIN(true),
        ACTIVITY_SPECTATE(true),
        ACTIVITY_JOIN_REQUEST(true),
        UNKNOWN(false);

        private final boolean subscribable;

        private Event(boolean subscribable) {
            this.subscribable = subscribable;
        }

        public boolean isSubscribable() {
            return this.subscribable;
        }

        static Event of(String str) {
            if (str == null) {
                return NULL;
            }
            for (Event s : Event.values()) {
                if (s == UNKNOWN || !s.name().equalsIgnoreCase(str)) continue;
                return s;
            }
            return UNKNOWN;
        }
    }
}

