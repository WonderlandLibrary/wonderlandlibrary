/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package me.wintware.client.viamcp.viafabric.platform;

import io.netty.buffer.ByteBuf;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import me.wintware.client.viamcp.viafabric.platform.VRBossBar;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.ViaAPI;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;

public class VRViaAPI
implements ViaAPI<UUID> {
    @Override
    public int getPlayerVersion(UUID uuid) {
        UserConnection con = Via.getManager().getConnection(uuid);
        if (con != null) {
            return con.getProtocolInfo().getProtocolVersion();
        }
        try {
            return Via.getManager().getInjector().getServerProtocolVersion();
        }
        catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public boolean isInjected(UUID uuid) {
        return Via.getManager().isClientConnected(uuid);
    }

    @Override
    public String getVersion() {
        return Via.getPlatform().getPluginVersion();
    }

    @Override
    public void sendRawPacket(UUID uuid, ByteBuf byteBuf) throws IllegalArgumentException {
        UserConnection ci = Via.getManager().getConnection(uuid);
        ci.sendRawPacket(byteBuf);
    }

    @Override
    public BossBar<Void> createBossBar(String s, BossColor bossColor, BossStyle bossStyle) {
        return new VRBossBar(s, 1.0f, bossColor, bossStyle);
    }

    @Override
    public BossBar<Void> createBossBar(String s, float v, BossColor bossColor, BossStyle bossStyle) {
        return new VRBossBar(s, v, bossColor, bossStyle);
    }

    @Override
    public SortedSet<Integer> getSupportedVersions() {
        TreeSet<Integer> outputSet = new TreeSet<Integer>(ProtocolRegistry.getSupportedVersions());
        outputSet.removeAll(Via.getPlatform().getConf().getBlockedProtocols());
        return outputSet;
    }
}

