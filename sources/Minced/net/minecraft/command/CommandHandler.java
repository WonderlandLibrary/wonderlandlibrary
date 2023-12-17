// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import org.apache.logging.log4j.LogManager;
import java.util.Collections;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.MinecraftServer;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextComponentTranslation;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.Set;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public abstract class CommandHandler implements ICommandManager
{
    private static final Logger LOGGER;
    private final Map<String, ICommand> commandMap;
    private final Set<ICommand> commandSet;
    
    public CommandHandler() {
        this.commandMap = (Map<String, ICommand>)Maps.newHashMap();
        this.commandSet = (Set<ICommand>)Sets.newHashSet();
    }
    
    @Override
    public int executeCommand(final ICommandSender sender, String rawCommand) {
        rawCommand = rawCommand.trim();
        if (rawCommand.startsWith("/")) {
            rawCommand = rawCommand.substring(1);
        }
        String[] astring = rawCommand.split(" ");
        final String s = astring[0];
        astring = dropFirstString(astring);
        final ICommand icommand = this.commandMap.get(s);
        int i = 0;
        try {
            final int j = this.getUsernameIndex(icommand, astring);
            if (icommand == null) {
                final TextComponentTranslation textcomponenttranslation1 = new TextComponentTranslation("commands.generic.notFound", new Object[0]);
                textcomponenttranslation1.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(textcomponenttranslation1);
            }
            else if (icommand.checkPermission(this.getServer(), sender)) {
                if (j > -1) {
                    final List<Entity> list = EntitySelector.matchEntities(sender, astring[j], (Class<? extends Entity>)Entity.class);
                    final String s2 = astring[j];
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
                    if (list.isEmpty()) {
                        throw new PlayerNotFoundException("commands.generic.selector.notFound", new Object[] { astring[j] });
                    }
                    for (final Entity entity : list) {
                        astring[j] = entity.getCachedUniqueIdString();
                        if (this.tryExecute(sender, astring, icommand, rawCommand)) {
                            ++i;
                        }
                    }
                    astring[j] = s2;
                }
                else {
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
                    if (this.tryExecute(sender, astring, icommand, rawCommand)) {
                        ++i;
                    }
                }
            }
            else {
                final TextComponentTranslation textcomponenttranslation2 = new TextComponentTranslation("commands.generic.permission", new Object[0]);
                textcomponenttranslation2.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(textcomponenttranslation2);
            }
        }
        catch (CommandException commandexception) {
            final TextComponentTranslation textcomponenttranslation3 = new TextComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            textcomponenttranslation3.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(textcomponenttranslation3);
        }
        sender.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, i);
        return i;
    }
    
    protected boolean tryExecute(final ICommandSender sender, final String[] args, final ICommand command, final String input) {
        try {
            command.execute(this.getServer(), sender, args);
            return true;
        }
        catch (WrongUsageException wrongusageexception) {
            final TextComponentTranslation textcomponenttranslation2 = new TextComponentTranslation("commands.generic.usage", new Object[] { new TextComponentTranslation(wrongusageexception.getMessage(), wrongusageexception.getErrorObjects()) });
            textcomponenttranslation2.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(textcomponenttranslation2);
        }
        catch (CommandException commandexception) {
            final TextComponentTranslation textcomponenttranslation3 = new TextComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            textcomponenttranslation3.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(textcomponenttranslation3);
        }
        catch (Throwable throwable) {
            final TextComponentTranslation textcomponenttranslation4 = new TextComponentTranslation("commands.generic.exception", new Object[0]);
            textcomponenttranslation4.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(textcomponenttranslation4);
            CommandHandler.LOGGER.warn("Couldn't process command: " + input, throwable);
        }
        return false;
    }
    
    protected abstract MinecraftServer getServer();
    
    public ICommand registerCommand(final ICommand command) {
        this.commandMap.put(command.getName(), command);
        this.commandSet.add(command);
        for (final String s : command.getAliases()) {
            final ICommand icommand = this.commandMap.get(s);
            if (icommand == null || !icommand.getName().equals(s)) {
                this.commandMap.put(s, command);
            }
        }
        return command;
    }
    
    private static String[] dropFirstString(final String[] input) {
        final String[] astring = new String[input.length - 1];
        System.arraycopy(input, 1, astring, 0, input.length - 1);
        return astring;
    }
    
    @Override
    public List<String> getTabCompletions(final ICommandSender sender, final String input, @Nullable final BlockPos pos) {
        final String[] astring = input.split(" ", -1);
        final String s = astring[0];
        if (astring.length == 1) {
            final List<String> list = (List<String>)Lists.newArrayList();
            for (final Map.Entry<String, ICommand> entry : this.commandMap.entrySet()) {
                if (CommandBase.doesStringStartWith(s, entry.getKey()) && entry.getValue().checkPermission(this.getServer(), sender)) {
                    list.add(entry.getKey());
                }
            }
            return list;
        }
        if (astring.length > 1) {
            final ICommand icommand = this.commandMap.get(s);
            if (icommand != null && icommand.checkPermission(this.getServer(), sender)) {
                return icommand.getTabCompletions(this.getServer(), sender, dropFirstString(astring), pos);
            }
        }
        return Collections.emptyList();
    }
    
    @Override
    public List<ICommand> getPossibleCommands(final ICommandSender sender) {
        final List<ICommand> list = (List<ICommand>)Lists.newArrayList();
        for (final ICommand icommand : this.commandSet) {
            if (icommand.checkPermission(this.getServer(), sender)) {
                list.add(icommand);
            }
        }
        return list;
    }
    
    @Override
    public Map<String, ICommand> getCommands() {
        return this.commandMap;
    }
    
    private int getUsernameIndex(final ICommand command, final String[] args) throws CommandException {
        if (command == null) {
            return -1;
        }
        for (int i = 0; i < args.length; ++i) {
            if (command.isUsernameIndex(args, i) && EntitySelector.matchesMultiplePlayers(args[i])) {
                return i;
            }
        }
        return -1;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
