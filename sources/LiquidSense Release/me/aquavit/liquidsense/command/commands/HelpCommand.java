package me.aquavit.liquidsense.command.commands;

import joptsimple.internal.Strings;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.utils.client.ClientUtils;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(final String[] args) {
        int page = 1;
        final double maxPageDouble = LiquidSense.commandManager.getCommands().size() / 8.0;
        final int maxPage = (maxPageDouble > (int)maxPageDouble) ? ((int)maxPageDouble + 1) : ((int)maxPageDouble);
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e) {
                chatSyntaxError();
            }
        }
        if (page <= 0) {
            chat("The number you have entered is too low, it must be over 0");
            return;
        }
        if (page > maxPage) {
            chat("The number you have entered is too big, it must be under " + maxPage + ".");
            return;
        }
        chat("§c§lHelp");
        ClientUtils.displayChatMessage("§7> Page: §8" + page + " / " + maxPage);
        for (int i = 8 * (page - 1); i < 8 * page && i < LiquidSense.commandManager.getCommands().size(); ++i) {
            final Command command = LiquidSense.commandManager.getCommands().get(i);
            ClientUtils.displayChatMessage("§6> §7" + LiquidSense.commandManager.getPrefix() + command.getCommand() + ((command.getAlias() == null) ? "" : (" §7(§8" + Strings.join(command.getAlias(), "§7, §8") + "§7)")));
        }
        ClientUtils.displayChatMessage("§a------------\n§7> §c"+ LiquidSense.commandManager.getPrefix() + "help §8<§7§lpage§8>");
    }
}

