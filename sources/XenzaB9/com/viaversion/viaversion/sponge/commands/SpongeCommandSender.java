// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.sponge.commands;

import org.spongepowered.api.util.Identifiable;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import com.viaversion.viaversion.SpongePlugin;
import net.kyori.adventure.identity.Identity;
import org.spongepowered.api.command.CommandCause;
import com.viaversion.viaversion.api.command.ViaCommandSender;

public class SpongeCommandSender implements ViaCommandSender
{
    private final CommandCause source;
    
    public SpongeCommandSender(final CommandCause source) {
        this.source = source;
    }
    
    @Override
    public boolean hasPermission(final String permission) {
        return this.source.hasPermission(permission);
    }
    
    @Override
    public void sendMessage(final String msg) {
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
