// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.commands;

import com.viaversion.viaversion.commands.defaultsubs.ReloadSubCmd;
import com.viaversion.viaversion.commands.defaultsubs.HelpSubCmd;
import com.viaversion.viaversion.commands.defaultsubs.AutoTeamSubCmd;
import com.viaversion.viaversion.commands.defaultsubs.DontBugMeSubCmd;
import com.viaversion.viaversion.commands.defaultsubs.DisplayLeaksSubCmd;
import com.viaversion.viaversion.commands.defaultsubs.DumpSubCmd;
import com.viaversion.viaversion.commands.defaultsubs.DebugSubCmd;
import com.viaversion.viaversion.commands.defaultsubs.PPSSubCmd;
import com.viaversion.viaversion.commands.defaultsubs.ListSubCmd;
import java.util.HashSet;
import com.viaversion.viaversion.api.Via;
import java.util.Iterator;
import java.util.Set;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import java.util.Locale;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import com.viaversion.viaversion.api.command.ViaSubCommand;
import java.util.Map;
import com.viaversion.viaversion.api.command.ViaVersionCommand;

public abstract class ViaCommandHandler implements ViaVersionCommand
{
    private final Map<String, ViaSubCommand> commandMap;
    
    protected ViaCommandHandler() {
        this.commandMap = new HashMap<String, ViaSubCommand>();
        this.registerDefaults();
    }
    
    @Override
    public void registerSubCommand(final ViaSubCommand command) {
        Preconditions.checkArgument(command.name().matches("^[a-z0-9_-]{3,15}$"), (Object)(command.name() + " is not a valid sub-command name."));
        Preconditions.checkArgument(!this.hasSubCommand(command.name()), (Object)("ViaSubCommand " + command.name() + " does already exists!"));
        this.commandMap.put(command.name().toLowerCase(Locale.ROOT), command);
    }
    
    @Override
    public boolean hasSubCommand(final String name) {
        return this.commandMap.containsKey(name.toLowerCase(Locale.ROOT));
    }
    
    @Override
    public ViaSubCommand getSubCommand(final String name) {
        return this.commandMap.get(name.toLowerCase(Locale.ROOT));
    }
    
    @Override
    public boolean onCommand(final ViaCommandSender sender, final String[] args) {
        if (args.length == 0) {
            this.showHelp(sender);
            return false;
        }
        if (!this.hasSubCommand(args[0])) {
            sender.sendMessage(ViaSubCommand.color("&cThis command does not exist."));
            this.showHelp(sender);
            return false;
        }
        final ViaSubCommand handler = this.getSubCommand(args[0]);
        if (!this.hasPermission(sender, handler.permission())) {
            sender.sendMessage(ViaSubCommand.color("&cYou are not allowed to use this command!"));
            return false;
        }
        final String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        final boolean result = handler.execute(sender, subArgs);
        if (!result) {
            sender.sendMessage("Usage: /viaversion " + handler.usage());
        }
        return result;
    }
    
    @Override
    public List<String> onTabComplete(final ViaCommandSender sender, final String[] args) {
        final Set<ViaSubCommand> allowed = this.calculateAllowedCommands(sender);
        final List<String> output = new ArrayList<String>();
        if (args.length == 1) {
            if (!args[0].isEmpty()) {
                for (final ViaSubCommand sub : allowed) {
                    if (sub.name().toLowerCase().startsWith(args[0].toLowerCase(Locale.ROOT))) {
                        output.add(sub.name());
                    }
                }
            }
            else {
                for (final ViaSubCommand sub : allowed) {
                    output.add(sub.name());
                }
            }
        }
        else if (args.length >= 2 && this.getSubCommand(args[0]) != null) {
            final ViaSubCommand sub2 = this.getSubCommand(args[0]);
            if (!allowed.contains(sub2)) {
                return output;
            }
            final String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            final List<String> tab = sub2.onTabComplete(sender, subArgs);
            Collections.sort(tab);
            return tab;
        }
        return output;
    }
    
    @Override
    public void showHelp(final ViaCommandSender sender) {
        final Set<ViaSubCommand> allowed = this.calculateAllowedCommands(sender);
        if (allowed.isEmpty()) {
            sender.sendMessage(ViaSubCommand.color("&cYou are not allowed to use these commands!"));
            return;
        }
        sender.sendMessage(ViaSubCommand.color("&aViaVersion &c" + Via.getPlatform().getPluginVersion()));
        sender.sendMessage(ViaSubCommand.color("&6Commands:"));
        for (final ViaSubCommand cmd : allowed) {
            sender.sendMessage(ViaSubCommand.color(String.format("&2/viaversion %s &7- &6%s", cmd.usage(), cmd.description())));
        }
        allowed.clear();
    }
    
    private Set<ViaSubCommand> calculateAllowedCommands(final ViaCommandSender sender) {
        final Set<ViaSubCommand> cmds = new HashSet<ViaSubCommand>();
        for (final ViaSubCommand sub : this.commandMap.values()) {
            if (this.hasPermission(sender, sub.permission())) {
                cmds.add(sub);
            }
        }
        return cmds;
    }
    
    private boolean hasPermission(final ViaCommandSender sender, final String permission) {
        return permission == null || sender.hasPermission(permission);
    }
    
    private void registerDefaults() {
        this.registerSubCommand(new ListSubCmd());
        this.registerSubCommand(new PPSSubCmd());
        this.registerSubCommand(new DebugSubCmd());
        this.registerSubCommand(new DumpSubCmd());
        this.registerSubCommand(new DisplayLeaksSubCmd());
        this.registerSubCommand(new DontBugMeSubCmd());
        this.registerSubCommand(new AutoTeamSubCmd());
        this.registerSubCommand(new HelpSubCmd());
        this.registerSubCommand(new ReloadSubCmd());
    }
}
