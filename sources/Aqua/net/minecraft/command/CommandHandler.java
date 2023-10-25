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
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandHandler
implements ICommandManager {
    private static final Logger logger = LogManager.getLogger();
    private final Map<String, ICommand> commandMap = Maps.newHashMap();
    private final Set<ICommand> commandSet = Sets.newHashSet();

    public int executeCommand(ICommandSender sender, String rawCommand) {
        if ((rawCommand = rawCommand.trim()).startsWith("/")) {
            rawCommand = rawCommand.substring(1);
        }
        String[] astring = rawCommand.split(" ");
        String s = astring[0];
        astring = CommandHandler.dropFirstString(astring);
        ICommand icommand = (ICommand)this.commandMap.get((Object)s);
        int i = this.getUsernameIndex(icommand, astring);
        int j = 0;
        if (icommand == null) {
            ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.notFound", new Object[0]);
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage((IChatComponent)chatcomponenttranslation);
        } else if (icommand.canCommandSenderUseCommand(sender)) {
            if (i > -1) {
                List list = PlayerSelector.matchEntities((ICommandSender)sender, (String)astring[i], Entity.class);
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
            sender.addChatMessage((IChatComponent)chatcomponenttranslation1);
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
            ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation("commands.generic.usage", new Object[]{new ChatComponentTranslation(wrongusageexception.getMessage(), wrongusageexception.getErrorObjects())});
            chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage((IChatComponent)chatcomponenttranslation2);
        }
        catch (CommandException commandexception) {
            ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage((IChatComponent)chatcomponenttranslation1);
        }
        catch (Throwable var9) {
            ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.exception", new Object[0]);
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage((IChatComponent)chatcomponenttranslation);
            logger.warn("Couldn't process command: '" + input + "'");
        }
        return false;
    }

    public ICommand registerCommand(ICommand command) {
        this.commandMap.put((Object)command.getCommandName(), (Object)command);
        this.commandSet.add((Object)command);
        for (String s : command.getCommandAliases()) {
            ICommand icommand = (ICommand)this.commandMap.get((Object)s);
            if (icommand != null && icommand.getCommandName().equals((Object)s)) continue;
            this.commandMap.put((Object)s, (Object)command);
        }
        return command;
    }

    private static String[] dropFirstString(String[] input) {
        String[] astring = new String[input.length - 1];
        System.arraycopy((Object)input, (int)1, (Object)astring, (int)0, (int)(input.length - 1));
        return astring;
    }

    public List<String> getTabCompletionOptions(ICommandSender sender, String input, BlockPos pos) {
        ICommand icommand;
        String[] astring = input.split(" ", -1);
        String s = astring[0];
        if (astring.length == 1) {
            ArrayList list = Lists.newArrayList();
            for (Map.Entry entry : this.commandMap.entrySet()) {
                if (!CommandBase.doesStringStartWith((String)s, (String)((String)entry.getKey())) || !((ICommand)entry.getValue()).canCommandSenderUseCommand(sender)) continue;
                list.add(entry.getKey());
            }
            return list;
        }
        if (astring.length > 1 && (icommand = (ICommand)this.commandMap.get((Object)s)) != null && icommand.canCommandSenderUseCommand(sender)) {
            return icommand.addTabCompletionOptions(sender, CommandHandler.dropFirstString(astring), pos);
        }
        return null;
    }

    public List<ICommand> getPossibleCommands(ICommandSender sender) {
        ArrayList list = Lists.newArrayList();
        for (ICommand icommand : this.commandSet) {
            if (!icommand.canCommandSenderUseCommand(sender)) continue;
            list.add((Object)icommand);
        }
        return list;
    }

    public Map<String, ICommand> getCommands() {
        return this.commandMap;
    }

    private int getUsernameIndex(ICommand command, String[] args) {
        if (command == null) {
            return -1;
        }
        for (int i = 0; i < args.length; ++i) {
            if (!command.isUsernameIndex(args, i) || !PlayerSelector.matchesMultiplePlayers((String)args[i])) continue;
            return i;
        }
        return -1;
    }
}
