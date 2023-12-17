// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import java.util.Collection;
import java.util.Iterator;
import net.minecraft.network.Packet;
import com.google.common.base.Objects;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import java.util.Collections;
import com.google.common.collect.Sets;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.Set;

public class BossInfoServer extends BossInfo
{
    private final Set<EntityPlayerMP> players;
    private final Set<EntityPlayerMP> readOnlyPlayers;
    private boolean visible;
    
    public BossInfoServer(final ITextComponent nameIn, final Color colorIn, final Overlay overlayIn) {
        super(MathHelper.getRandomUUID(), nameIn, colorIn, overlayIn);
        this.players = (Set<EntityPlayerMP>)Sets.newHashSet();
        this.readOnlyPlayers = Collections.unmodifiableSet((Set<? extends EntityPlayerMP>)this.players);
        this.visible = true;
    }
    
    @Override
    public void setPercent(final float percentIn) {
        if (percentIn != this.percent) {
            super.setPercent(percentIn);
            this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PCT);
        }
    }
    
    @Override
    public void setColor(final Color colorIn) {
        if (colorIn != this.color) {
            super.setColor(colorIn);
            this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_STYLE);
        }
    }
    
    @Override
    public void setOverlay(final Overlay overlayIn) {
        if (overlayIn != this.overlay) {
            super.setOverlay(overlayIn);
            this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_STYLE);
        }
    }
    
    @Override
    public BossInfo setDarkenSky(final boolean darkenSkyIn) {
        if (darkenSkyIn != this.darkenSky) {
            super.setDarkenSky(darkenSkyIn);
            this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PROPERTIES);
        }
        return this;
    }
    
    @Override
    public BossInfo setPlayEndBossMusic(final boolean playEndBossMusicIn) {
        if (playEndBossMusicIn != this.playEndBossMusic) {
            super.setPlayEndBossMusic(playEndBossMusicIn);
            this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PROPERTIES);
        }
        return this;
    }
    
    @Override
    public BossInfo setCreateFog(final boolean createFogIn) {
        if (createFogIn != this.createFog) {
            super.setCreateFog(createFogIn);
            this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PROPERTIES);
        }
        return this;
    }
    
    @Override
    public void setName(final ITextComponent nameIn) {
        if (!Objects.equal((Object)nameIn, (Object)this.name)) {
            super.setName(nameIn);
            this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_NAME);
        }
    }
    
    private void sendUpdate(final SPacketUpdateBossInfo.Operation operationIn) {
        if (this.visible) {
            final SPacketUpdateBossInfo spacketupdatebossinfo = new SPacketUpdateBossInfo(operationIn, this);
            for (final EntityPlayerMP entityplayermp : this.players) {
                entityplayermp.connection.sendPacket(spacketupdatebossinfo);
            }
        }
    }
    
    public void addPlayer(final EntityPlayerMP player) {
        if (this.players.add(player) && this.visible) {
            player.connection.sendPacket(new SPacketUpdateBossInfo(SPacketUpdateBossInfo.Operation.ADD, this));
        }
    }
    
    public void removePlayer(final EntityPlayerMP player) {
        if (this.players.remove(player) && this.visible) {
            player.connection.sendPacket(new SPacketUpdateBossInfo(SPacketUpdateBossInfo.Operation.REMOVE, this));
        }
    }
    
    public void setVisible(final boolean visibleIn) {
        if (visibleIn != this.visible) {
            this.visible = visibleIn;
            for (final EntityPlayerMP entityplayermp : this.players) {
                entityplayermp.connection.sendPacket(new SPacketUpdateBossInfo(visibleIn ? SPacketUpdateBossInfo.Operation.ADD : SPacketUpdateBossInfo.Operation.REMOVE, this));
            }
        }
    }
    
    public Collection<EntityPlayerMP> getPlayers() {
        return this.readOnlyPlayers;
    }
}
