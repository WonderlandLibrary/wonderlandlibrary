/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.celestial.client.Celestial;
import org.celestial.client.cmd.CommandAbstract;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.settings.config.Config;
import org.celestial.client.settings.config.ConfigManager;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class ConfigCommand
extends CommandAbstract {
    public ConfigCommand() {
        super("config", "configurations", "\u00a76.config" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " save | load | delete \u00a73<name>", "config");
    }

    @Override
    public void execute(String ... args) {
        if (args.length >= 2) {
            String upperCase = args[1].toUpperCase();
            if (args.length == 3) {
                switch (upperCase) {
                    case "LOAD": {
                        if (Celestial.instance.configManager.loadConfig(args[2])) {
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "loaded config: " + (Object)((Object)ChatFormatting.RED) + "\"" + args[2] + "\"");
                            NotificationManager.publicity("Config", (Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "loaded config: " + (Object)((Object)ChatFormatting.RED) + "\"" + args[2] + "\"", 4, NotificationType.SUCCESS);
                            break;
                        }
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Failed " + (Object)((Object)ChatFormatting.WHITE) + "load config: " + (Object)((Object)ChatFormatting.RED) + "\"" + args[2] + "\"");
                        NotificationManager.publicity("Config", (Object)((Object)ChatFormatting.RED) + "Failed " + (Object)((Object)ChatFormatting.WHITE) + "load config: " + (Object)((Object)ChatFormatting.RED) + "\"" + args[2] + "\"", 4, NotificationType.ERROR);
                        break;
                    }
                    case "SAVE": {
                        if (Celestial.instance.configManager.saveConfig(args[2])) {
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "saved config: " + (Object)((Object)ChatFormatting.RED) + "\"" + args[2] + "\"");
                            NotificationManager.publicity("Config", (Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "saved config: " + (Object)((Object)ChatFormatting.RED) + "\"" + args[2] + "\"", 4, NotificationType.SUCCESS);
                            ConfigManager.getLoadedConfigs().clear();
                            Celestial.instance.configManager.load();
                            break;
                        }
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Failed " + (Object)((Object)ChatFormatting.WHITE) + "to save config: " + (Object)((Object)ChatFormatting.RED) + "\"" + args[2] + "\"");
                        NotificationManager.publicity("Config", (Object)((Object)ChatFormatting.RED) + "Failed " + (Object)((Object)ChatFormatting.WHITE) + "to save config: " + (Object)((Object)ChatFormatting.RED) + "\"" + args[2] + "\"", 4, NotificationType.ERROR);
                        break;
                    }
                    case "DELETE": {
                        if (Celestial.instance.configManager.deleteConfig(args[2])) {
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "deleted config: " + (Object)((Object)ChatFormatting.RED) + "\"" + args[2] + "\"");
                            NotificationManager.publicity("Config", (Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "deleted config: " + (Object)((Object)ChatFormatting.RED) + "\"" + args[2] + "\"", 4, NotificationType.SUCCESS);
                            break;
                        }
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Failed " + (Object)((Object)ChatFormatting.WHITE) + "to delete config: " + (Object)((Object)ChatFormatting.RED) + "\"" + args[2] + "\"");
                        NotificationManager.publicity("Config", (Object)((Object)ChatFormatting.RED) + "Failed " + (Object)((Object)ChatFormatting.WHITE) + "to delete config: " + (Object)((Object)ChatFormatting.RED) + "\"" + args[2] + "\"", 4, NotificationType.ERROR);
                    }
                }
            } else if (args.length == 2 && upperCase.equalsIgnoreCase("LIST")) {
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Configs:");
                for (Config config : Celestial.instance.configManager.getContents()) {
                    ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + config.getName());
                }
            }
        } else {
            ChatHelper.addChatMessage(this.getUsage());
        }
    }
}

