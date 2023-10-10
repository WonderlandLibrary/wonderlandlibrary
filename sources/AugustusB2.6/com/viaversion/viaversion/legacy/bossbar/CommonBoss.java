// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.legacy.bossbar;

import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import com.viaversion.viaversion.api.Via;
import java.util.HashSet;
import com.google.common.collect.MapMaker;
import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.legacy.bossbar.BossStyle;
import com.viaversion.viaversion.api.legacy.bossbar.BossColor;
import com.viaversion.viaversion.api.legacy.bossbar.BossFlag;
import java.util.Set;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Map;
import java.util.UUID;
import com.viaversion.viaversion.api.legacy.bossbar.BossBar;

public class CommonBoss implements BossBar
{
    private final UUID uuid;
    private final Map<UUID, UserConnection> connections;
    private final Set<BossFlag> flags;
    private String title;
    private float health;
    private BossColor color;
    private BossStyle style;
    private boolean visible;
    
    public CommonBoss(final String title, final float health, final BossColor color, final BossStyle style) {
        Preconditions.checkNotNull(title, (Object)"Title cannot be null");
        Preconditions.checkArgument(health >= 0.0f && health <= 1.0f, (Object)("Health must be between 0 and 1. Input: " + health));
        this.uuid = UUID.randomUUID();
        this.title = title;
        this.health = health;
        this.color = ((color == null) ? BossColor.PURPLE : color);
        this.style = ((style == null) ? BossStyle.SOLID : style);
        this.connections = (Map<UUID, UserConnection>)new MapMaker().weakValues().makeMap();
        this.flags = new HashSet<BossFlag>();
        this.visible = true;
    }
    
    @Override
    public BossBar setTitle(final String title) {
        Preconditions.checkNotNull(title);
        this.title = title;
        this.sendPacket(UpdateAction.UPDATE_TITLE);
        return this;
    }
    
    @Override
    public BossBar setHealth(final float health) {
        Preconditions.checkArgument(health >= 0.0f && health <= 1.0f, (Object)("Health must be between 0 and 1. Input: " + health));
        this.health = health;
        this.sendPacket(UpdateAction.UPDATE_HEALTH);
        return this;
    }
    
    @Override
    public BossColor getColor() {
        return this.color;
    }
    
    @Override
    public BossBar setColor(final BossColor color) {
        Preconditions.checkNotNull(color);
        this.color = color;
        this.sendPacket(UpdateAction.UPDATE_STYLE);
        return this;
    }
    
    @Override
    public BossBar setStyle(final BossStyle style) {
        Preconditions.checkNotNull(style);
        this.style = style;
        this.sendPacket(UpdateAction.UPDATE_STYLE);
        return this;
    }
    
    @Override
    public BossBar addPlayer(final UUID player) {
        final UserConnection client = Via.getManager().getConnectionManager().getConnectedClient(player);
        if (client != null) {
            this.addConnection(client);
        }
        return this;
    }
    
    @Override
    public BossBar addConnection(final UserConnection conn) {
        if (this.connections.put(conn.getProtocolInfo().getUuid(), conn) == null && this.visible) {
            this.sendPacketConnection(conn, this.getPacket(UpdateAction.ADD, conn));
        }
        return this;
    }
    
    @Override
    public BossBar removePlayer(final UUID uuid) {
        final UserConnection client = this.connections.remove(uuid);
        if (client != null) {
            this.sendPacketConnection(client, this.getPacket(UpdateAction.REMOVE, client));
        }
        return this;
    }
    
    @Override
    public BossBar removeConnection(final UserConnection conn) {
        this.removePlayer(conn.getProtocolInfo().getUuid());
        return this;
    }
    
    @Override
    public BossBar addFlag(final BossFlag flag) {
        Preconditions.checkNotNull(flag);
        if (!this.hasFlag(flag)) {
            this.flags.add(flag);
        }
        this.sendPacket(UpdateAction.UPDATE_FLAGS);
        return this;
    }
    
    @Override
    public BossBar removeFlag(final BossFlag flag) {
        Preconditions.checkNotNull(flag);
        if (this.hasFlag(flag)) {
            this.flags.remove(flag);
        }
        this.sendPacket(UpdateAction.UPDATE_FLAGS);
        return this;
    }
    
    @Override
    public boolean hasFlag(final BossFlag flag) {
        Preconditions.checkNotNull(flag);
        return this.flags.contains(flag);
    }
    
    @Override
    public Set<UUID> getPlayers() {
        return Collections.unmodifiableSet((Set<? extends UUID>)this.connections.keySet());
    }
    
    @Override
    public Set<UserConnection> getConnections() {
        return Collections.unmodifiableSet((Set<? extends UserConnection>)new HashSet<UserConnection>(this.connections.values()));
    }
    
    @Override
    public BossBar show() {
        this.setVisible(true);
        return this;
    }
    
    @Override
    public BossBar hide() {
        this.setVisible(false);
        return this;
    }
    
    @Override
    public boolean isVisible() {
        return this.visible;
    }
    
    private void setVisible(final boolean value) {
        if (this.visible != value) {
            this.visible = value;
            this.sendPacket(value ? UpdateAction.ADD : UpdateAction.REMOVE);
        }
    }
    
    @Override
    public UUID getId() {
        return this.uuid;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    @Override
    public String getTitle() {
        return this.title;
    }
    
    @Override
    public float getHealth() {
        return this.health;
    }
    
    @Override
    public BossStyle getStyle() {
        return this.style;
    }
    
    public Set<BossFlag> getFlags() {
        return this.flags;
    }
    
    private void sendPacket(final UpdateAction action) {
        for (final UserConnection conn : new ArrayList<UserConnection>(this.connections.values())) {
            final PacketWrapper wrapper = this.getPacket(action, conn);
            this.sendPacketConnection(conn, wrapper);
        }
    }
    
    private void sendPacketConnection(final UserConnection conn, final PacketWrapper wrapper) {
        if (conn.getProtocolInfo() == null || !conn.getProtocolInfo().getPipeline().contains(Protocol1_9To1_8.class)) {
            this.connections.remove(conn.getProtocolInfo().getUuid());
            return;
        }
        try {
            wrapper.scheduleSend(Protocol1_9To1_8.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private PacketWrapper getPacket(final UpdateAction action, final UserConnection connection) {
        try {
            final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9.BOSSBAR, null, connection);
            wrapper.write(Type.UUID, this.uuid);
            wrapper.write(Type.VAR_INT, action.getId());
            switch (action) {
                case ADD: {
                    Protocol1_9To1_8.FIX_JSON.write(wrapper, this.title);
                    wrapper.write(Type.FLOAT, this.health);
                    wrapper.write(Type.VAR_INT, this.color.getId());
                    wrapper.write(Type.VAR_INT, this.style.getId());
                    wrapper.write(Type.BYTE, (byte)this.flagToBytes());
                }
                case UPDATE_HEALTH: {
                    wrapper.write(Type.FLOAT, this.health);
                    break;
                }
                case UPDATE_TITLE: {
                    Protocol1_9To1_8.FIX_JSON.write(wrapper, this.title);
                    break;
                }
                case UPDATE_STYLE: {
                    wrapper.write(Type.VAR_INT, this.color.getId());
                    wrapper.write(Type.VAR_INT, this.style.getId());
                    break;
                }
                case UPDATE_FLAGS: {
                    wrapper.write(Type.BYTE, (byte)this.flagToBytes());
                    break;
                }
            }
            return wrapper;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private int flagToBytes() {
        int bitmask = 0;
        for (final BossFlag flag : this.flags) {
            bitmask |= flag.getId();
        }
        return bitmask;
    }
    
    private enum UpdateAction
    {
        ADD(0), 
        REMOVE(1), 
        UPDATE_HEALTH(2), 
        UPDATE_TITLE(3), 
        UPDATE_STYLE(4), 
        UPDATE_FLAGS(5);
        
        private final int id;
        
        private UpdateAction(final int id) {
            this.id = id;
        }
        
        public int getId() {
            return this.id;
        }
    }
}
