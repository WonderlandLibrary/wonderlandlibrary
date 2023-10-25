/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

public class CommandHelp
extends CommandBase {
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        List<ICommand> list = this.getSortedPossibleCommands(iCommandSender);
        int n = 7;
        int n2 = (list.size() - 1) / 7;
        int n3 = 0;
        try {
            n3 = stringArray.length == 0 ? 0 : CommandHelp.parseInt(stringArray[0], 1, n2 + 1) - 1;
        }
        catch (NumberInvalidException numberInvalidException) {
            Map<String, ICommand> map = this.getCommands();
            ICommand iCommand = map.get(stringArray[0]);
            if (iCommand != null) {
                throw new WrongUsageException(iCommand.getCommandUsage(iCommandSender), new Object[0]);
            }
            if (MathHelper.parseIntWithDefault(stringArray[0], -1) != -1) {
                throw numberInvalidException;
            }
            throw new CommandNotFoundException();
        }
        int n4 = Math.min((n3 + 1) * 7, list.size());
        ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("commands.help.header", n3 + 1, n2 + 1);
        chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
        iCommandSender.addChatMessage(chatComponentTranslation);
        int n5 = n3 * 7;
        while (n5 < n4) {
            ICommand iCommand = list.get(n5);
            ChatComponentTranslation chatComponentTranslation2 = new ChatComponentTranslation(iCommand.getCommandUsage(iCommandSender), new Object[0]);
            chatComponentTranslation2.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + iCommand.getCommandName() + " "));
            iCommandSender.addChatMessage(chatComponentTranslation2);
            ++n5;
        }
        if (n3 == 0 && iCommandSender instanceof EntityPlayer) {
            ChatComponentTranslation chatComponentTranslation3 = new ChatComponentTranslation("commands.help.footer", new Object[0]);
            chatComponentTranslation3.getChatStyle().setColor(EnumChatFormatting.GREEN);
            iCommandSender.addChatMessage(chatComponentTranslation3);
        }
    }

    protected Map<String, ICommand> getCommands() {
        return MinecraftServer.getServer().getCommandManager().getCommands();
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        if (stringArray.length == 1) {
            Set<String> set = this.getCommands().keySet();
            return CommandHelp.getListOfStringsMatchingLastWord(stringArray, set.toArray(new String[set.size()]));
        }
        return null;
    }

    protected List<ICommand> getSortedPossibleCommands(ICommandSender iCommandSender) {
        List<ICommand> list = MinecraftServer.getServer().getCommandManager().getPossibleCommands(iCommandSender);
        Collections.sort(list);
        return list;
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("?");
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.help.usage";
    }
}

