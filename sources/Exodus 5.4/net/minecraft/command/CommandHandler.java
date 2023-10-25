/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
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
    private final Set<ICommand> commandSet;
    private final Map<String, ICommand> commandMap = Maps.newHashMap();

    public ICommand registerCommand(ICommand iCommand) {
        this.commandMap.put(iCommand.getCommandName(), iCommand);
        this.commandSet.add(iCommand);
        for (String string : iCommand.getCommandAliases()) {
            ICommand iCommand2 = this.commandMap.get(string);
            if (iCommand2 != null && iCommand2.getCommandName().equals(string)) continue;
            this.commandMap.put(string, iCommand);
        }
        return iCommand;
    }

    private int getUsernameIndex(ICommand iCommand, String[] stringArray) {
        if (iCommand == null) {
            return -1;
        }
        int n = 0;
        while (n < stringArray.length) {
            if (iCommand.isUsernameIndex(stringArray, n) && PlayerSelector.matchesMultiplePlayers(stringArray[n])) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    public CommandHandler() {
        this.commandSet = Sets.newHashSet();
    }

    @Override
    public List<ICommand> getPossibleCommands(ICommandSender iCommandSender) {
        ArrayList arrayList = Lists.newArrayList();
        for (ICommand iCommand : this.commandSet) {
            if (!iCommand.canCommandSenderUseCommand(iCommandSender)) continue;
            arrayList.add(iCommand);
        }
        return arrayList;
    }

    protected boolean tryExecute(ICommandSender iCommandSender, String[] stringArray, ICommand iCommand, String string) {
        try {
            iCommand.processCommand(iCommandSender, stringArray);
            return true;
        }
        catch (WrongUsageException wrongUsageException) {
            ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("commands.generic.usage", new ChatComponentTranslation(wrongUsageException.getMessage(), wrongUsageException.getErrorObjects()));
            chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            iCommandSender.addChatMessage(chatComponentTranslation);
        }
        catch (CommandException commandException) {
            ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation(commandException.getMessage(), commandException.getErrorObjects());
            chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            iCommandSender.addChatMessage(chatComponentTranslation);
        }
        catch (Throwable throwable) {
            ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("commands.generic.exception", new Object[0]);
            chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            iCommandSender.addChatMessage(chatComponentTranslation);
            logger.warn("Couldn't process command: '" + string + "'");
        }
        return false;
    }

    @Override
    public List<String> getTabCompletionOptions(ICommandSender iCommandSender, String string, BlockPos blockPos) {
        ICommand iCommand;
        String[] stringArray = string.split(" ", -1);
        String string2 = stringArray[0];
        if (stringArray.length == 1) {
            ArrayList arrayList = Lists.newArrayList();
            for (Map.Entry<String, ICommand> entry : this.commandMap.entrySet()) {
                if (!CommandBase.doesStringStartWith(string2, entry.getKey()) || !entry.getValue().canCommandSenderUseCommand(iCommandSender)) continue;
                arrayList.add(entry.getKey());
            }
            return arrayList;
        }
        if (stringArray.length > 1 && (iCommand = this.commandMap.get(string2)) != null && iCommand.canCommandSenderUseCommand(iCommandSender)) {
            return iCommand.addTabCompletionOptions(iCommandSender, CommandHandler.dropFirstString(stringArray), blockPos);
        }
        return null;
    }

    @Override
    public int executeCommand(ICommandSender iCommandSender, String string) {
        if ((string = string.trim()).startsWith("/")) {
            string = string.substring(1);
        }
        String[] stringArray = string.split(" ");
        String string2 = stringArray[0];
        stringArray = CommandHandler.dropFirstString(stringArray);
        ICommand iCommand = this.commandMap.get(string2);
        int n = this.getUsernameIndex(iCommand, stringArray);
        int n2 = 0;
        if (iCommand == null) {
            ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("commands.generic.notFound", new Object[0]);
            chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            iCommandSender.addChatMessage(chatComponentTranslation);
        } else if (iCommand.canCommandSenderUseCommand(iCommandSender)) {
            if (n > -1) {
                List<Entity> list = PlayerSelector.matchEntities(iCommandSender, stringArray[n], Entity.class);
                String string3 = stringArray[n];
                iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
                for (Entity entity : list) {
                    stringArray[n] = entity.getUniqueID().toString();
                    if (!this.tryExecute(iCommandSender, stringArray, iCommand, string)) continue;
                    ++n2;
                }
                stringArray[n] = string3;
            } else {
                iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
                if (this.tryExecute(iCommandSender, stringArray, iCommand, string)) {
                    ++n2;
                }
            }
        } else {
            ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("commands.generic.permission", new Object[0]);
            chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            iCommandSender.addChatMessage(chatComponentTranslation);
        }
        iCommandSender.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, n2);
        return n2;
    }

    @Override
    public Map<String, ICommand> getCommands() {
        return this.commandMap;
    }

    private static String[] dropFirstString(String[] stringArray) {
        String[] stringArray2 = new String[stringArray.length - 1];
        System.arraycopy(stringArray, 1, stringArray2, 0, stringArray.length - 1);
        return stringArray2;
    }
}

