/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.kyori.adventure.text.Component
 *  org.spongepowered.api.entity.living.player.server.ServerPlayer
 */
package com.viaversion.viaversion.sponge.commands;

import com.viaversion.viaversion.SpongePlugin;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class SpongePlayer
implements ViaCommandSender {
    private final ServerPlayer player;

    public SpongePlayer(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission);
    }

    @Override
    public void sendMessage(String msg) {
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

