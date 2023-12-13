package summer.base.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import summer.Summer;
import summer.base.manager.Command;
import summer.base.manager.config.Config;
import summer.base.utilities.ChatUtils;

public final class ConfigCommand implements Command {

    @Override
    public boolean run(String[] args) {
        if (args.length >= 2) {
            String upperCaseFunction = args[1].toUpperCase();

            if (args.length == 3) {
                switch (upperCaseFunction) {
                    case "LOAD":
                        if (Summer.INSTANCE.configManager.loadConfig(args[2]))
                            ChatUtils.sendMessage("Successfully loaded config: '" + args[2] + "'");
                        else
                            ChatUtils.sendMessage("Failed to load config: '" + args[2] + "'");
                        break;
                    case "SAVE":
                        if (Summer.INSTANCE.configManager.saveConfig(args[2]))
                            ChatUtils.sendMessage("Successfully saved config: '" + args[2] + "'");
                        else
                            ChatUtils.sendMessage("Failed to save config: '" + args[2] + "'");
                        break;
                    case "DELETE":
                        if (Summer.INSTANCE.configManager.deleteConfig(args[2]))
                            ChatUtils.sendMessage("Successfully deleted config: '" + args[2] + "'");
                        else
                            ChatUtils.sendMessage("Failed to delete config: '" + args[2] + "'");
                        break;
                }
                return true;
            } else if (args.length == 2 && upperCaseFunction.equalsIgnoreCase("LIST")) {
                ChatUtils.sendMessage("Available Configs:");
                for (Config config : Summer.INSTANCE.configManager.getContents())
                    ChatUtils.sendMessage(config.getName());
                return true;
            }
        }
        return false;
    }

    @Override
    public String usage() {
        return ChatFormatting.WHITE + "c | config <load/save/delete/list> <config>";
    }
}
