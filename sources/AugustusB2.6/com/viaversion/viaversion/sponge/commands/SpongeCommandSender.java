// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.sponge.commands;

import org.spongepowered.api.util.Identifiable;
import java.util.UUID;
import org.spongepowered.api.text.serializer.TextSerializers;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.SpongePlugin;
import org.spongepowered.api.command.CommandSource;
import com.viaversion.viaversion.api.command.ViaCommandSender;

public class SpongeCommandSender implements ViaCommandSender
{
    private final CommandSource source;
    
    public SpongeCommandSender(final CommandSource source) {
        this.source = source;
    }
    
    @Override
    public boolean hasPermission(final String permission) {
        return this.source.hasPermission(permission);
    }
    
    @Override
    public void sendMessage(final String msg) {
        final String serialized = SpongePlugin.COMPONENT_SERIALIZER.serialize((Component)SpongePlugin.COMPONENT_SERIALIZER.deserialize(msg));
        this.source.sendMessage(TextSerializers.JSON.deserialize(serialized));
    }
    
    @Override
    public UUID getUUID() {
        if (this.source instanceof Identifiable) {
            return ((Identifiable)this.source).getUniqueId();
        }
        return UUID.fromString(this.getName());
    }
    
    @Override
    public String getName() {
        return this.source.getName();
    }
}
