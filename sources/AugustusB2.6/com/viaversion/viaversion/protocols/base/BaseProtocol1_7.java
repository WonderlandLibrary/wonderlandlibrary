// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.base;

import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.util.ChatColorUtil;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import java.util.UUID;
import com.google.common.base.Joiner;
import java.util.logging.Level;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.ProtocolPathEntry;
import java.util.List;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.api.protocol.version.ServerProtocolVersion;
import com.viaversion.viaversion.protocol.ServerProtocolVersionSingleton;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocol.ProtocolManagerImpl;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.util.GsonUtil;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public class BaseProtocol1_7 extends AbstractProtocol
{
    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundStatusPackets.STATUS_RESPONSE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ProtocolInfo info = wrapper.user().getProtocolInfo();
                        final String originalStatus = wrapper.get(Type.STRING, 0);
                        try {
                            JsonElement json = GsonUtil.getGson().fromJson(originalStatus, JsonElement.class);
                            int protocolVersion = 0;
                            JsonObject version;
                            if (json.isJsonObject()) {
                                if (json.getAsJsonObject().has("version")) {
                                    version = json.getAsJsonObject().get("version").getAsJsonObject();
                                    if (version.has("protocol")) {
                                        protocolVersion = version.get("protocol").getAsLong().intValue();
                                    }
                                }
                                else {
                                    json.getAsJsonObject().add("version", version = new JsonObject());
                                }
                            }
                            else {
                                json = new JsonObject();
                                json.getAsJsonObject().add("version", version = new JsonObject());
                            }
                            if (Via.getConfig().isSendSupportedVersions()) {
                                version.add("supportedVersions", GsonUtil.getGson().toJsonTree(Via.getAPI().getSupportedVersions()));
                            }
                            if (!Via.getAPI().getServerVersion().isKnown()) {
                                final ProtocolManagerImpl protocolManager = (ProtocolManagerImpl)Via.getManager().getProtocolManager();
                                protocolManager.setServerProtocol(new ServerProtocolVersionSingleton(ProtocolVersion.getProtocol(protocolVersion).getVersion()));
                            }
                            final VersionProvider versionProvider = Via.getManager().getProviders().get(VersionProvider.class);
                            if (versionProvider == null) {
                                wrapper.user().setActive(false);
                                return;
                            }
                            final int closestServerProtocol = versionProvider.getClosestServerProtocol(wrapper.user());
                            List<ProtocolPathEntry> protocols = null;
                            if (info.getProtocolVersion() >= closestServerProtocol || Via.getPlatform().isOldClientsAllowed()) {
                                protocols = Via.getManager().getProtocolManager().getProtocolPath(info.getProtocolVersion(), closestServerProtocol);
                            }
                            if (protocols != null) {
                                if (protocolVersion == closestServerProtocol || protocolVersion == 0) {
                                    final ProtocolVersion prot = ProtocolVersion.getProtocol(info.getProtocolVersion());
                                    version.addProperty("protocol", prot.getOriginalVersion());
                                }
                            }
                            else {
                                wrapper.user().setActive(false);
                            }
                            if (Via.getConfig().blockedProtocolVersions().contains(info.getProtocolVersion())) {
                                version.addProperty("protocol", -1);
                            }
                            wrapper.set(Type.STRING, 0, GsonUtil.getGson().toJson(json));
                        }
                        catch (JsonParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        this.registerClientbound(ClientboundLoginPackets.GAME_PROFILE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ProtocolInfo info = wrapper.user().getProtocolInfo();
                        info.setState(State.PLAY);
                        final UUID uuid = BaseProtocol1_7.this.passthroughLoginUUID(wrapper);
                        info.setUuid(uuid);
                        final String username = wrapper.passthrough(Type.STRING);
                        info.setUsername(username);
                        Via.getManager().getConnectionManager().onLoginSuccess(wrapper.user());
                        if (!info.getPipeline().hasNonBaseProtocols()) {
                            wrapper.user().setActive(false);
                        }
                        if (Via.getManager().isDebug()) {
                            Via.getPlatform().getLogger().log(Level.INFO, "{0} logged in with protocol {1}, Route: {2}", new Object[] { username, info.getProtocolVersion(), Joiner.on(", ").join(info.getPipeline().pipes(), ", ", new Object[0]) });
                        }
                    }
                });
            }
        });
        this.registerServerbound(ServerboundLoginPackets.HELLO, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int protocol = wrapper.user().getProtocolInfo().getProtocolVersion();
                        if (Via.getConfig().blockedProtocolVersions().contains(protocol)) {
                            if (!wrapper.user().getChannel().isOpen()) {
                                return;
                            }
                            if (!wrapper.user().shouldApplyBlockProtocol()) {
                                return;
                            }
                            final PacketWrapper disconnectPacket = PacketWrapper.create(ClientboundLoginPackets.LOGIN_DISCONNECT, wrapper.user());
                            Protocol1_9To1_8.FIX_JSON.write(disconnectPacket, ChatColorUtil.translateAlternateColorCodes(Via.getConfig().getBlockedDisconnectMsg()));
                            wrapper.cancel();
                            final ChannelFuture future = disconnectPacket.sendFuture(BaseProtocol.class);
                            future.addListener(f -> wrapper.user().getChannel().close());
                        }
                    }
                });
            }
        });
    }
    
    @Override
    public boolean isBaseProtocol() {
        return true;
    }
    
    public static String addDashes(final String trimmedUUID) {
        final StringBuilder idBuff = new StringBuilder(trimmedUUID);
        idBuff.insert(20, '-');
        idBuff.insert(16, '-');
        idBuff.insert(12, '-');
        idBuff.insert(8, '-');
        return idBuff.toString();
    }
    
    protected UUID passthroughLoginUUID(final PacketWrapper wrapper) throws Exception {
        String uuidString = wrapper.passthrough(Type.STRING);
        if (uuidString.length() == 32) {
            uuidString = addDashes(uuidString);
        }
        return UUID.fromString(uuidString);
    }
}
