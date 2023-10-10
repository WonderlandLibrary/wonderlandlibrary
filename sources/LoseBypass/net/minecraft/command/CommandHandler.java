/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandHandler
implements ICommandManager {
    private static final Logger logger = LogManager.getLogger();
    private final Map<String, ICommand> commandMap = Maps.newHashMap();
    private final Set<ICommand> commandSet = Sets.newHashSet();

    @Override
    public int executeCommand(ICommandSender sender, String rawCommand) {
        if ((rawCommand = rawCommand.trim()).startsWith("/")) {
            rawCommand = rawCommand.substring(1);
        }
        String[] astring = rawCommand.split(" ");
        String s = astring[0];
        astring = CommandHandler.dropFirstString(astring);
        ICommand icommand = this.commandMap.get(s);
        int i = this.getUsernameIndex(icommand, astring);
        int j = 0;
        if (icommand == null) {
            ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.notFound", new Object[0]);
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(chatcomponenttranslation);
        } else if (icommand.canCommandSenderUseCommand(sender)) {
            if (i > -1) {
                List<Entity> list = PlayerSelector.matchEntities(sender, astring[i], Entity.class);
                String s1 = astring[i];
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
                for (Entity entity : list) {
                    astring[i] = entity.getUniqueID().toString();
                    if (!this.tryExecute(sender, astring, icommand, rawCommand)) continue;
                    ++j;
                }
                astring[i] = s1;
            } else {
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
                if (this.tryExecute(sender, astring, icommand, rawCommand)) {
                    ++j;
                }
            }
        } else {
            ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.generic.permission", new Object[0]);
            chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(chatcomponenttranslation1);
        }
        sender.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, j);
        return j;
    }

    protected boolean tryExecute(ICommandSender sender, String[] args, ICommand command, String input) {
        try {
            command.processCommand(sender, args);
            return true;
        }
        catch (WrongUsageException wrongusageexception) {
            ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation("commands.generic.usage", new ChatComponentTranslation(wrongusageexception.getMessage(), wrongusageexception.getErrorObjects()));
            chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(chatcomponenttranslation2);
            return false;
        }
        catch (CommandException commandexception) {
            ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(chatcomponenttranslation1);
            return false;
        }
        catch (Throwable var9) {
            ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.exception", new Object[0]);
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(chatcomponenttranslation);
            logger.warn("Couldn't process command: '" + input + "'");
        }
        return false;
    }

    public ICommand registerCommand(ICommand command) {
        this.commandMap.put(command.getCommandName(), command);
        this.commandSet.add(command);
        Iterator<String> iterator = command.getCommandAliases().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            ICommand icommand = this.commandMap.get(s);
            if (icommand != null && icommand.getCommandName().equals(s)) continue;
            this.commandMap.put(s, command);
        }
        return command;
    }

    private static String[] dropFirstString(String[] input) {
        String[] astring = new String[input.length - 1];
        System.arraycopy(input, 1, astring, 0, input.length - 1);
        return astring;
    }

    @Override
    public List<String> getTabCompletionOptions(ICommandSender sender, String input, BlockPos pos) {
        String[] astring = input.split(" ", -1);
        String s = astring[0];
        if (astring.length != 1) {
            if (astring.length <= 1) return null;
            ICommand icommand = this.commandMap.get(s);
            if (icommand == null) return null;
            if (!icommand.canCommandSenderUseCommand(sender)) return null;
            return icommand.addTabCompletionOptions(sender, CommandHandler.dropFirstString(astring), pos);
        }
        ArrayList<String> list = Lists.newArrayList();
        Iterator<Map.Entry<String, ICommand>> iterator = this.commandMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ICommand> entry = iterator.next();
            if (!CommandBase.doesStringStartWith(s, entry.getKey()) || !entry.getValue().canCommandSenderUseCommand(sender)) continue;
            list.add(entry.getKey());
        }
        return list;
    }

    @Override
    public List<ICommand> getPossibleCommands(ICommandSender sender) {
        ArrayList<ICommand> list = Lists.newArrayList();
        Iterator<ICommand> iterator = this.commandSet.iterator();
        while (iterator.hasNext()) {
            ICommand icommand = iterator.next();
            if (!icommand.canCommandSenderUseCommand(sender)) continue;
            list.add(icommand);
        }
        return list;
    }

    @Override
    public Map<String, ICommand> getCommands() {
        return this.commandMap;
    }

    private int getUsernameIndex(ICommand command, String[] args) {
        if (command == null) {
            return -1;
        }
        int i = 0;
        while (i < args.length) {
            if (command.isUsernameIndex(args, i) && PlayerSelector.matchesMultiplePlayers(args[i])) {
                return i;
            }
            ++i;
        }
        return -1;
    }
}

