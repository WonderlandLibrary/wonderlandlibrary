// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.sponge.commands;

import org.spongepowered.api.text.Text;
import java.util.Optional;
import java.util.List;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.command.CommandException;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.CommandCallable;
import com.viaversion.viaversion.commands.ViaCommandHandler;

public class SpongeCommandHandler extends ViaCommandHandler implements CommandCallable
{
    public CommandResult process(final CommandSource source, final String arguments) throws CommandException {
        final String[] args = (arguments.length() > 0) ? arguments.split(" ") : new String[0];
        this.onCommand(new SpongeCommandSender(source), args);
        return CommandResult.success();
    }
    
    public List<String> getSuggestions(final CommandSource commandSource, final String s, final Location<World> location) throws CommandException {
        return this.getSuggestions(commandSource, s);
    }
    
    public List<String> getSuggestions(final CommandSource source, final String arguments) throws CommandException {
        final String[] args = arguments.split(" ", -1);
        return this.onTabComplete(new SpongeCommandSender(source), args);
    }
    
    public boolean testPermission(final CommandSource source) {
        return source.hasPermission("viaversion.admin");
    }
    
    public Optional<Text> getShortDescription(final CommandSource source) {
        return Optional.of((Text)Text.of("Shows ViaVersion Version and more."));
    }
    
    public Optional<Text> getHelp(final CommandSource source) {
        return Optional.empty();
    }
    
    public Text getUsage(final CommandSource source) {
        return (Text)Text.of("Usage /viaversion");
    }
}
