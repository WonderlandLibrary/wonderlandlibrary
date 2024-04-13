package dev.tenacity.command.impl;

import dev.tenacity.Tenacity;
import dev.tenacity.command.AbstractCommand;
import dev.tenacity.exception.InvalidConfigException;
import dev.tenacity.util.misc.ChatUtil;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author RareHyperIonYT
 * @since 24.12.2023
 */
public class ConfigCommand  extends AbstractCommand {

    public ConfigCommand() {
        super("config", "load or save config", ".config [load/save] [name] | .c [load/save] [name]", 1);
    }

    @Override
    public void onCommand(final String[] arguments) {
        if(arguments.length == 1) {
            /*
                TODO: Possibly implement default config saving that
                 saves to "Config.json" with ConfigHandler::saveDefaultConfig()
            */
            ChatUtil.error("Usage: " + getUsage());
        } else if(arguments.length >= 2) {
            String action = arguments[0];
            String name = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));



            switch (action) {
                case "load": {
                    ChatUtil.print("§aLoading config from \"" + name + "\".");

                    try {
                        Tenacity.getInstance().getConfigHandler().loadConfig(name);
                        ChatUtil.print("§aSuccessfully loaded config.");
                    } catch (InvalidConfigException | IOException e) {
                        ChatUtil.error("There was an error loading that config.");
                    }

                    break;
                }

                case "save": {
                    ChatUtil.print("§aSaving config with name \"" + name + "\".");

                    try {
                        Tenacity.getInstance().getConfigHandler().saveConfig(name);
                        ChatUtil.print("§aSuccessfully saved config.");
                    } catch (IOException e) {
                        e.printStackTrace();
                        ChatUtil.error("There was an error saving the config.");
                    }
                    break;
                }
            }
        }
    }

}
