/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.kyori.adventure.identity.Identity
 *  net.kyori.adventure.text.Component
 *  org.spongepowered.api.command.CommandCause
 *  org.spongepowered.api.util.Identifiable
 */
package com.viaversion.viaversion.sponge.commands;

import com.viaversion.viaversion.SpongePlugin;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import java.util.UUID;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.util.Identifiable;

public class SpongeCommandSender
implements ViaCommandSender {
    private final CommandCause source;

    public SpongeCommandSender(CommandCause source) {
        this.source = source;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.source.hasPermission(permission);
    }

    @Override
    public void sendMessage(String msg) {
        this.source.sendMessage(Identity.nil(), (Component)SpongePlugin.LEGACY_SERIALIZER.deserialize(msg));
    }

    @Override
    public UUID getUUID() {
        if (this.source instanceof Identifiable) {
            return ((Identifiable)this.source).uniqueId();
        }
        return UUID.fromString(this.getName());
    }

    @Override
    public String getName() {
        return this.source.friendlyIdentifier().orElse(this.source.identifier());
    }
}

