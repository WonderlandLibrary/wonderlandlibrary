package org.dreamcore.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.cmd.CommandAbstract;
import org.dreamcore.client.helpers.misc.ChatHelper;
import org.dreamcore.client.settings.config.Config;
import org.dreamcore.client.settings.config.ConfigManager;
import org.dreamcore.client.ui.notification.NotificationManager;
import org.dreamcore.client.ui.notification.NotificationType;

public class ConfigCommand extends CommandAbstract {

    public ConfigCommand() {
        super("config", "configurations", "§6.config" + ChatFormatting.LIGHT_PURPLE + " save | load | delete " + "§3<name>", "config");
    }

    @Override
    public void execute(String... args) {
        try {
            if (args.length >= 2) {
                String upperCase = args[1].toUpperCase();
                if (args.length == 3) {
                    switch (upperCase) {
                        case "LOAD":
                            if (dreamcore.instance.configManager.loadConfig(args[2])) {
                                ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "loaded config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                                NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "loaded config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.SUCCESS);
                            } else {
                                ChatHelper.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "load config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                                NotificationManager.publicity("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "load config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.ERROR);
                            }
                            break;
                        case "SAVE":
                            if (dreamcore.instance.configManager.saveConfig(args[2])) {
                                ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                                NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.SUCCESS);
                                ConfigManager.getLoadedConfigs().clear();
                                dreamcore.instance.configManager.load();
                            } else {
                                ChatHelper.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to save config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                                NotificationManager.publicity("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to save config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.ERROR);
                            }
                            break;
                        case "DELETE":
                            if (dreamcore.instance.configManager.deleteConfig(args[2])) {
                                ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "deleted config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                                NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "deleted config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.SUCCESS);
                            } else {
                                ChatHelper.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to delete config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                                NotificationManager.publicity("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to delete config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.ERROR);
                            }
                            break;
                    }
                } else if (args.length == 2 && upperCase.equalsIgnoreCase("LIST")) {
                    ChatHelper.addChatMessage(ChatFormatting.GREEN + "Configs:");
                    for (Config config : dreamcore.instance.configManager.getContents()) {
                        ChatHelper.addChatMessage(ChatFormatting.RED + config.getName());
                    }
                }
            } else {
                ChatHelper.addChatMessage(this.getUsage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}