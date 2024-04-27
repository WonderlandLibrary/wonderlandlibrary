/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.kyori.adventure.text.Component
 *  org.jetbrains.annotations.NotNull
 *  org.spongepowered.api.command.Command$Raw
 *  org.spongepowered.api.command.CommandCause
 *  org.spongepowered.api.command.CommandCompletion
 *  org.spongepowered.api.command.CommandResult
 *  org.spongepowered.api.command.parameter.ArgumentReader$Mutable
 */
package com.viaversion.viaversion.sponge.commands;

import com.viaversion.viaversion.commands.ViaCommandHandler;
import com.viaversion.viaversion.sponge.commands.SpongeCommandSender;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.ArgumentReader;

public class SpongeCommandHandler
extends ViaCommandHandler
implements Command.Raw {
    public CommandResult process(CommandCause cause, ArgumentReader.Mutable arguments) {
        String[] args2 = arguments.input().length() > 0 ? arguments.input().split(" ") : new String[]{};
        this.onCommand(new SpongeCommandSender(cause), args2);
        return CommandResult.success();
    }

    public List<CommandCompletion> complete(CommandCause cause, ArgumentReader.Mutable arguments) {
        String[] args2 = arguments.input().split(" ", -1);
        return this.onTabComplete(new SpongeCommandSender(cause), args2).stream().map(CommandCompletion::of).collect(Collectors.toList());
    }

    public boolean canExecute(CommandCause cause) {
        return cause.hasPermission("viaversion.admin");
    }

    public Optional<Component> shortDescription(CommandCause cause) {
        return Optional.of(Component.text((String)"Shows ViaVersion Version and more."));
    }

    public Optional<Component> extendedDescription(CommandCause cause) {
        return this.shortDescription(cause);
    }

    public Optional<Component> help(@NotNull CommandCause cause) {
        return Optional.empty();
    }

    public Component usage(CommandCause cause) {
        return Component.text((String)"Usage /viaversion");
    }
}

