package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.command.Command;

public class PrefixCommand extends Command {
    public PrefixCommand() {
        super("prefix");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length <= 1) {
            chatSyntax("prefix <character>");
            return;
        }

        String prefix = args[1];

        if (prefix.length() > 1) {
            chat("§cPrefix can only be one character long!");
            return;
        }

        LiquidSense.commandManager.setPrefix(prefix.charAt(0));
        LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.valuesConfig);

        chat("Successfully changed command prefix to '§8" +prefix+"§3'");
    }
}
