/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.SessionService;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.ServerLoginHandler;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.status.PlayerInfo;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.VersionInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoBuilder;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.mc.protocol.packet.handshake.client.HandshakePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.login.client.EncryptionResponsePacket;
import com.github.steveice10.mc.protocol.packet.login.client.LoginStartPacket;
import com.github.steveice10.mc.protocol.packet.login.server.EncryptionRequestPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginSetCompressionPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginSuccessPacket;
import com.github.steveice10.mc.protocol.packet.status.client.StatusPingPacket;
import com.github.steveice10.mc.protocol.packet.status.client.StatusQueryPacket;
import com.github.steveice10.mc.protocol.packet.status.server.StatusPongPacket;
import com.github.steveice10.mc.protocol.packet.status.server.StatusResponsePacket;
import com.github.steveice10.mc.protocol.util.CryptUtil;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import java.math.BigInteger;
import java.net.Proxy;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import javax.crypto.SecretKey;

public class ServerListener
extends SessionAdapter {
    private static final KeyPair KEY_PAIR = CryptUtil.generateKeyPair();
    private byte[] verifyToken = new byte[4];
    private String serverId = "";
    private String username = "";
    private long lastPingTime = 0L;
    private int lastPingId = 0;

    public ServerListener() {
        new Random().nextBytes(this.verifyToken);
    }

    @Override
    public void connected(ConnectedEvent event) {
        event.getSession().setFlag("ping", 0);
    }

    @Override
    public void packetReceived(PacketReceivedEvent event) {
        MinecraftPacket packet;
        MinecraftProtocol protocol = (MinecraftProtocol)event.getSession().getPacketProtocol();
        if (protocol.getSubProtocol() == SubProtocol.HANDSHAKE && event.getPacket() instanceof HandshakePacket) {
            packet = (HandshakePacket)event.getPacket();
            switch (((HandshakePacket)packet).getIntent()) {
                case STATUS: {
                    protocol.setSubProtocol(SubProtocol.STATUS, false, event.getSession());
                    break;
                }
                case LOGIN: {
                    protocol.setSubProtocol(SubProtocol.LOGIN, false, event.getSession());
                    if (((HandshakePacket)packet).getProtocolVersion() > 340) {
                        event.getSession().disconnect("Outdated server! I'm still on 1.12.2.");
                        break;
                    }
                    if (((HandshakePacket)packet).getProtocolVersion() >= 340) break;
                    event.getSession().disconnect("Outdated client! Please use 1.12.2.");
                    break;
                }
                default: {
                    throw new UnsupportedOperationException("Invalid client intent: " + (Object)((Object)((HandshakePacket)packet).getIntent()));
                }
            }
        }
        if (protocol.getSubProtocol() == SubProtocol.LOGIN) {
            if (event.getPacket() instanceof LoginStartPacket) {
                boolean verify;
                this.username = ((LoginStartPacket)event.getPacket()).getUsername();
                boolean bl = verify = event.getSession().hasFlag("verify-users") ? (Boolean)event.getSession().getFlag("verify-users") : true;
                if (verify) {
                    event.getSession().send(new EncryptionRequestPacket(this.serverId, KEY_PAIR.getPublic(), this.verifyToken));
                } else {
                    new Thread(new UserAuthTask(event.getSession(), null)).start();
                }
            } else if (event.getPacket() instanceof EncryptionResponsePacket) {
                PrivateKey privateKey;
                packet = (EncryptionResponsePacket)event.getPacket();
                if (!Arrays.equals(this.verifyToken, ((EncryptionResponsePacket)packet).getVerifyToken(privateKey = KEY_PAIR.getPrivate()))) {
                    event.getSession().disconnect("Invalid nonce!");
                    return;
                }
                SecretKey key = ((EncryptionResponsePacket)packet).getSecretKey(privateKey);
                protocol.enableEncryption(key);
                new Thread(new UserAuthTask(event.getSession(), key)).start();
            }
        }
        if (protocol.getSubProtocol() == SubProtocol.STATUS) {
            if (event.getPacket() instanceof StatusQueryPacket) {
                ServerInfoBuilder builder = (ServerInfoBuilder)event.getSession().getFlag("info-builder");
                if (builder == null) {
                    builder = new ServerInfoBuilder(){

                        @Override
                        public ServerStatusInfo buildInfo(Session session) {
                            return new ServerStatusInfo(VersionInfo.CURRENT, new PlayerInfo(0, 20, new GameProfile[0]), Message.fromString("A Minecraft Server"), null);
                        }
                    };
                }
                ServerStatusInfo info = builder.buildInfo(event.getSession());
                event.getSession().send(new StatusResponsePacket(info));
            } else if (event.getPacket() instanceof StatusPingPacket) {
                event.getSession().send(new StatusPongPacket(((StatusPingPacket)event.getPacket()).getPingTime()));
            }
        }
        if (protocol.getSubProtocol() == SubProtocol.GAME && event.getPacket() instanceof ClientKeepAlivePacket && ((ClientKeepAlivePacket)(packet = (ClientKeepAlivePacket)event.getPacket())).getPingId() == (long)this.lastPingId) {
            long time = System.currentTimeMillis() - this.lastPingTime;
            event.getSession().setFlag("ping", time);
        }
    }

    @Override
    public void disconnecting(DisconnectingEvent event) {
        MinecraftProtocol protocol = (MinecraftProtocol)event.getSession().getPacketProtocol();
        if (protocol.getSubProtocol() == SubProtocol.LOGIN) {
            event.getSession().send(new LoginDisconnectPacket(event.getReason()));
        } else if (protocol.getSubProtocol() == SubProtocol.GAME) {
            event.getSession().send(new ServerDisconnectPacket(event.getReason()));
        }
    }

    private class KeepAliveTask
    implements Runnable {
        private Session session;

        public KeepAliveTask(Session session) {
            this.session = session;
        }

        @Override
        public void run() {
            while (this.session.isConnected()) {
                ServerListener.this.lastPingTime = System.currentTimeMillis();
                ServerListener.this.lastPingId = (int)ServerListener.this.lastPingTime;
                this.session.send(new ServerKeepAlivePacket(ServerListener.this.lastPingId));
                try {
                    Thread.sleep(2000L);
                }
                catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    private class UserAuthTask
    implements Runnable {
        private Session session;
        private SecretKey key;

        public UserAuthTask(Session session, SecretKey key) {
            this.key = key;
            this.session = session;
        }

        @Override
        public void run() {
            boolean verify = this.session.hasFlag("verify-users") ? (Boolean)this.session.getFlag("verify-users") : true;
            GameProfile profile = null;
            if (verify && this.key != null) {
                Proxy proxy = (Proxy)this.session.getFlag("auth-proxy");
                if (proxy == null) {
                    proxy = Proxy.NO_PROXY;
                }
                try {
                    profile = new SessionService(proxy).getProfileByServer(ServerListener.this.username, new BigInteger(CryptUtil.getServerIdHash(ServerListener.this.serverId, KEY_PAIR.getPublic(), this.key)).toString(16));
                }
                catch (RequestException e) {
                    this.session.disconnect("Failed to make session service request.", e);
                    return;
                }
                if (profile == null) {
                    this.session.disconnect("Failed to verify username.");
                }
            } else {
                profile = new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + ServerListener.this.username).getBytes()), ServerListener.this.username);
            }
            int threshold = this.session.hasFlag("compression-threshold") ? (Integer)this.session.getFlag("compression-threshold") : 256;
            this.session.send(new LoginSetCompressionPacket(threshold));
            this.session.setCompressionThreshold(threshold);
            this.session.send(new LoginSuccessPacket(profile));
            this.session.setFlag("profile", profile);
            ((MinecraftProtocol)this.session.getPacketProtocol()).setSubProtocol(SubProtocol.GAME, false, this.session);
            ServerLoginHandler handler = (ServerLoginHandler)this.session.getFlag("login-handler");
            if (handler != null) {
                handler.loggedIn(this.session);
            }
            new Thread(new KeepAliveTask(this.session)).start();
        }
    }
}

