// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.sponge.commands;

import org.jetbrains.annotations.NotNull;
import net.kyori.adventure.text.Component;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import org.spongepowered.api.command.CommandCompletion;
import java.util.List;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.Command;
import com.viaversion.viaversion.commands.ViaCommandHandler;

public class SpongeCommandHandler extends ViaCommandHandler implements Command.Raw
{
    public CommandResult process(final CommandCause cause, final ArgumentReader.Mutable arguments) {
        final String[] args = (arguments.input().length() > 0) ? arguments.input().split(" ") : new String[0];
        this.onCommand(new SpongeCommandSender(cause), args);
        return CommandResult.success();
    }
    
    public List<CommandCompletion> complete(final CommandCause cause, final ArgumentReader.Mutable arguments) {
        final String[] args = arguments.input().split(" ", -1);
        return this.onTabComplete(new SpongeCommandSender(cause), args).stream().map((Function<? super Object, ?>)CommandCompletion::of).collect((Collector<? super Object, ?, List<CommandCompletion>>)Collectors.toList());
    }
    
    public boolean canExecute(final CommandCause cause) {
        return cause.hasPermission("viaversion.admin");
    }
    
    public Optional<Component> shortDescription(final CommandCause cause) {
        return Optional.of((Component)Component.text("Shows ViaVersion Version and more."));
    }
    
    public Optional<Component> extendedDescription(final CommandCause cause) {
        return this.shortDescription(cause);
    }
    
    public Optional<Component> help(@NotNull final CommandCause cause) {
        return Optional.empty();
    }
    
    public Component usage(final CommandCause cause) {
        return (Component)Component.text("Usage /viaversion");
    }
}
