// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.cmd.impl;

import java.util.Iterator;
import Lavish.utils.misc.Console;
import Lavish.Client;
import java.util.List;
import net.minecraft.client.entity.EntityPlayerSP;
import Lavish.cmd.CommandExecutor;

public class Config implements CommandExecutor
{
    @Override
    public void execute(final EntityPlayerSP sender, final List<String> args) {
        if (args.size() >= 1) {
            final String upperCaseFunction = args.get(0).toUpperCase();
            if (args.size() == 2) {
                final String s;
                switch (s = upperCaseFunction) {
                    case "LOAD": {
                        if (Client.instance.configManager.loadConfig(args.get(1))) {
                            Console.sendChatToPlayerWithPrefix("Successfully loaded config: '" + args.get(1) + "'");
                            break;
                        }
                        Console.sendChatToPlayerWithPrefix("Config not found: '" + args.get(1) + "'");
                        break;
                    }
                    case "SAVE": {
                        if (Client.instance.configManager.saveConfig(args.get(1))) {
                            Console.sendChatToPlayerWithPrefix("Successfully saved config: '" + args.get(1) + "'");
                            break;
                        }
                        Console.sendChatToPlayerWithPrefix("Failed to save config: '" + args.get(1) + "'");
                        break;
                    }
                    case "DELETE": {
                        if (Client.instance.configManager.deleteConfig(args.get(1))) {
                            Console.sendChatToPlayerWithPrefix("Successfully deleted config: '" + args.get(1) + "'");
                            break;
                        }
                        Console.sendChatToPlayerWithPrefix("Failed to delete config: '" + args.get(1) + "'");
                        break;
                    }
                    default:
                        break;
                }
            }
            else if (args.size() == 1 && upperCaseFunction.equalsIgnoreCase("LIST")) {
                if (Client.instance.configManager.getContents().size() == 0) {
                    Console.sendChatToPlayerWithPrefix("No configs available!");
                }
                else {
                    Console.sendChatToPlayerWithPrefix("--Available Configs--");
                    for (final Lavish.config.Config config : Client.instance.configManager.getContents()) {
                        Console.sendChatToPlayer(config.getName());
                    }
                }
            }
        }
    }
}
