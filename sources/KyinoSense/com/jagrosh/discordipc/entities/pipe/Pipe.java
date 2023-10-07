/*
 * Decompiled with CFR 0.152.
 */
package com.jagrosh.discordipc.entities.pipe;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.Callback;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import com.jagrosh.discordipc.entities.pipe.UnixPipe;
import com.jagrosh.discordipc.entities.pipe.WindowsPipe;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Pipe {
    private static final Logger LOGGER = LoggerFactory.getLogger(Pipe.class);
    private static final int VERSION = 1;
    PipeStatus status = PipeStatus.CONNECTING;
    IPCListener listener;
    private DiscordBuild build;
    final IPCClient ipcClient;
    private final HashMap<String, Callback> callbacks;
    private static final String[] unixPaths = new String[]{"XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP"};

    Pipe(IPCClient ipcClient, HashMap<String, Callback> callbacks) {
        this.ipcClient = ipcClient;
        this.callbacks = callbacks;
    }

    public static Pipe openPipe(IPCClient ipcClient, long clientId, HashMap<String, Callback> callbacks, DiscordBuild ... preferredOrder) throws NoDiscordClientException {
        int i;
        if (preferredOrder == null || preferredOrder.length == 0) {
            preferredOrder = new DiscordBuild[]{DiscordBuild.ANY};
        }
        Pipe pipe = null;
        Pipe[] open = new Pipe[DiscordBuild.values().length];
        for (i = 0; i < 10; ++i) {
            try {
                String location = Pipe.getPipeLocation(i);
                LOGGER.debug(String.format("Searching for IPC: %s", location));
                pipe = Pipe.createPipe(ipcClient, callbacks, location);
                pipe.send(Packet.OpCode.HANDSHAKE, new JSONObject().put("v", 1).put("client_id", Long.toString(clientId)), null);
                Packet p = pipe.read();
                pipe.build = DiscordBuild.from(p.getJson().getJSONObject("data").getJSONObject("config").getString("api_endpoint"));
                LOGGER.debug(String.format("Found a valid client (%s) with packet: %s", pipe.build.name(), p.toString()));
                if (pipe.build == preferredOrder[0] || DiscordBuild.ANY == preferredOrder[0]) {
                    LOGGER.info(String.format("Found preferred client: %s", pipe.build.name()));
                    break;
                }
                open[pipe.build.ordinal()] = pipe;
                open[DiscordBuild.ANY.ordinal()] = pipe;
                pipe.build = null;
                pipe = null;
                continue;
            }
            catch (IOException | JSONException ex) {
                pipe = null;
            }
        }
        if (pipe == null) {
            for (i = 1; i < preferredOrder.length; ++i) {
                DiscordBuild cb = preferredOrder[i];
                LOGGER.debug(String.format("Looking for client build: %s", cb.name()));
                if (open[cb.ordinal()] == null) continue;
                pipe = open[cb.ordinal()];
                open[cb.ordinal()] = null;
                if (cb == DiscordBuild.ANY) {
                    for (int k = 0; k < open.length; ++k) {
                        if (open[k] != pipe) continue;
                        pipe.build = DiscordBuild.values()[k];
                        open[k] = null;
                    }
                } else {
                    pipe.build = cb;
                }
                LOGGER.info(String.format("Found preferred client: %s", pipe.build.name()));
                break;
            }
            if (pipe == null) {
                throw new NoDiscordClientException();
            }
        }
        for (i = 0; i < open.length; ++i) {
            if (i == DiscordBuild.ANY.ordinal() || open[i] == null) continue;
            try {
                open[i].close();
                continue;
            }
            catch (IOException ex) {
                LOGGER.debug("Failed to close an open IPC pipe!", ex);
            }
        }
        pipe.status = PipeStatus.CONNECTED;
        return pipe;
    }

    private static Pipe createPipe(IPCClient ipcClient, HashMap<String, Callback> callbacks, String location) {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return new WindowsPipe(ipcClient, callbacks, location);
        }
        if (osName.contains("linux") || osName.contains("mac")) {
            try {
                return new UnixPipe(ipcClient, callbacks, location);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Unsupported OS: " + osName);
    }

    public void send(Packet.OpCode op, JSONObject data, Callback callback) {
        try {
            String nonce = Pipe.generateNonce();
            Packet p = new Packet(op, data.put("nonce", nonce));
            if (callback != null && !callback.isEmpty()) {
                this.callbacks.put(nonce, callback);
            }
            this.write(p.toBytes());
            LOGGER.debug(String.format("Sent packet: %s", p.toString()));
            if (this.listener != null) {
                this.listener.onPacketSent(this.ipcClient, p);
            }
        }
        catch (IOException ex) {
            LOGGER.error("Encountered an IOException while sending a packet and disconnected!");
            this.status = PipeStatus.DISCONNECTED;
        }
    }

    public abstract Packet read() throws IOException, JSONException;

    public abstract void write(byte[] var1) throws IOException;

    private static String generateNonce() {
        return UUID.randomUUID().toString();
    }

    public PipeStatus getStatus() {
        return this.status;
    }

    public void setStatus(PipeStatus status) {
        this.status = status;
    }

    public void setListener(IPCListener listener) {
        this.listener = listener;
    }

    public abstract void close() throws IOException;

    public DiscordBuild getDiscordBuild() {
        return this.build;
    }

    private static String getPipeLocation(int i) {
        String str;
        if (System.getProperty("os.name").contains("Win")) {
            return "\\\\?\\pipe\\discord-ipc-" + i;
        }
        String tmppath = null;
        String[] stringArray = unixPaths;
        int n = stringArray.length;
        for (int j = 0; j < n && (tmppath = System.getenv(str = stringArray[j])) == null; ++j) {
        }
        if (tmppath == null) {
            tmppath = "/tmp";
        }
        return tmppath + "/discord-ipc-" + i;
    }
}

