// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.sponge.commands;

import java.util.UUID;
import net.kyori.adventure.text.Component;
import com.viaversion.viaversion.SpongePlugin;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import com.viaversion.viaversion.api.command.ViaCommandSender;

public class SpongePlayer implements ViaCommandSender
{
    private final ServerPlayer player;
    
    public SpongePlayer(final ServerPlayer player) {
        this.player = player;
    }
    
    @Override
    public boolean hasPermission(final String permission) {
        return this.player.hasPermission(permission);
    }
    
    @Override
    public void sendMessage(final String msg) {
        this.player.sendMessage((Component)SpongePlugin.LEGACY_SERIALIZER.deserialize(msg));
    }
    
    @Override
    public UUID getUUID() {
        return this.player.uniqueId();
    }
    
    @Override
    public String getName() {
        return this.player.friendlyIdentifier().orElse(this.player.identifier());
    }
}
