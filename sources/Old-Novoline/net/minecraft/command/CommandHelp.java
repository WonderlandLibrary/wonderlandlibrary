package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

import java.util.*;

public class CommandHelp extends CommandBase {

    /**
     * Gets the name of the command
     */
    public String getCommandName() {
        return "help";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 0;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender) {
        return "commands.help.usage";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("?");
    }

    /**
     * Callback when the command is invoked
     */
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        final List<ICommand> list = this.getSortedPossibleCommands(sender);
        final int j = (list.size() - 1) / 7;
        int k;

        try {
            k = args.length == 0 ? 0 : parseInt(args[0], 1, j + 1) - 1;
        } catch (NumberInvalidException e) {
            final Map<String, ICommand> map = this.getCommands();
            final ICommand icommand = map.get(args[0]);

            if (icommand != null) throw new WrongUsageException(icommand.getCommandUsage(sender));
            if (MathHelper.parseIntWithDefault(args[0], -1) != -1) throw e;

            throw new CommandNotFoundException();
        }

        final int l = Math.min((k + 1) * 7, list.size());
        final ChatComponentTranslation header = new ChatComponentTranslation("commands.help.header", k + 1, j + 1);
        header.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
        sender.addChatMessage(header);

        for (int i1 = k * 7; i1 < l; ++i1) {
            final ICommand command = list.get(i1);
            final ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(command.getCommandUsage(sender));

            chatcomponenttranslation.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + command.getCommandName() + " "));
            sender.addChatMessage(chatcomponenttranslation);
        }

        if (k == 0 && sender instanceof EntityPlayer) {
            final ChatComponentTranslation footer = new ChatComponentTranslation("commands.help.footer");

            footer.getChatStyle().setColor(EnumChatFormatting.GREEN);
            sender.addChatMessage(footer);
        }
    }

    protected List<ICommand> getSortedPossibleCommands(ICommandSender p_71534_1_) {
        final List<ICommand> list = MinecraftServer.getServer().getCommandManager().getPossibleCommands(p_71534_1_);

        Collections.sort(list);
        return list;
    }

    protected Map<String, ICommand> getCommands() {
        return MinecraftServer.getServer().getCommandManager().getCommands();
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            final Set<String> set = this.getCommands().keySet();
            return getListOfStringsMatchingLastWord(args, set.toArray(new String[0]));
        } else {
            return null;
        }
    }

}
