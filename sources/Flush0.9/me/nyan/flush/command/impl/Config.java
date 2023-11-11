package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.utils.other.ChatUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class Config extends Command {
    public Config() {
        super("Config", "Configurations Manager", "config load <name> | config save <name> | config delete <name> | config list", "setting");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length == 0) {
            sendSyntaxHelpMessage();
            return;
        }

        switch (args[0].toLowerCase()) {
            case "list":
                if (flush.getConfigManager().getPath().listFiles().length > 0) {
                    ChatUtils.println("§9These configs are in your config folder:");
                    for (File file : flush.getConfigManager().getPath().listFiles()) {
                        if (!file.getName().toLowerCase().endsWith(".json")) {
                            continue;
                        }
                        ChatUtils.println(StringUtils.removeEndIgnoreCase(file.getName(), ".json"));
                    }
                    break;
                }

                ChatUtils.println("§9There are no configs in your config folder.");
                break;

            case "save":
                if (args.length < 2) {
                    sendSyntaxHelpMessage();
                    break;
                }

                ChatUtils.println(flush.getConfigManager().save(args[1]) ?
                        String.format("§9Saved config \"%s\".", args[1]) : "§4Failed to save config.");
                break;

            case "load":
                if (args.length < 2) {
                    sendSyntaxHelpMessage();
                    break;
                }

                ChatUtils.println(flush.getConfigManager().load(args[1]) ?
                        String.format("§9Loaded config \"%s\".", args[1]) : "§4Failed to load config.");
                break;

            case "del":
            case "remove":
            case "delete":
                if (args.length < 2) {
                    sendSyntaxHelpMessage();
                    break;
                }

                ChatUtils.println(flush.getConfigManager().delete(args[1]) ?
                        String.format("§9Deleted config \"%s\".", args[1]) : "§4Failed to delete config.");
                break;

            default:
                sendSyntaxHelpMessage();
                break;
        }
    }
}