package com.ohare.client.command.impl;

import com.ohare.client.Client;
import com.ohare.client.command.Command;
import com.ohare.client.config.Config;
import com.ohare.client.utils.Printer;

import java.io.IOException;

public class ConfigCMD extends Command {

    public ConfigCMD() {
        super("Config", new String[]{"c", "config", "configs"});
    }

    @Override
    public void onRun(final String[] s) {
    	if (s.length == 1) {
    		return;
    	}
        switch (s[1]) {
            case "list":
                if (!Client.INSTANCE.getConfigManager().getConfigs().isEmpty()) {
                    Printer.print("Current Configs:");
                    Client.INSTANCE.getConfigManager().getConfigs().forEach(cfg -> {
                        Printer.print(cfg.getName());
                    });
                } else {
                    Printer.print("You have no saved configs!");
                }
                break;
            case "help":
                Printer.print("config list - shows all configs.");
                Printer.print("config create/save configname - saves a config.");
                Printer.print("config override configname - overrides existing configs.");
                Printer.print("config delete/remove configname - removes a config.");
                break;
            case "create":
            case "save":
                if (!Client.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Client.INSTANCE.getConfigManager().saveConfig(s[2], s.length > 3 && s[3].equalsIgnoreCase("keys"));
                    Client.INSTANCE.getConfigManager().getConfigs().add(new Config(s[2]));
                    Printer.print("Created a config named " + s[2] + (s.length > 3 && s[3].equalsIgnoreCase("keys") ? " with keys included" : "") + "!");
                    Client.INSTANCE.getNotificationManager().addNotification("Created a config named " + s[2] + (s.length > 3 && s[3].equalsIgnoreCase("keys") ? " with keys included" : "") + "!",2000);
                } else {
                    Printer.print(s[2] + " is already a saved config!");
                    Client.INSTANCE.getNotificationManager().addNotification( s[2] + " is already a saved config!",2000);
                }
                break;
            case "delete":
            case "remove":
                if (Client.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Client.INSTANCE.getConfigManager().deleteConfig(s[2]);
                    Printer.print("Deleted the config named " + s[2] + "!");
                    Client.INSTANCE.getNotificationManager().addNotification( "Deleted the config named " + s[2] + "!",2000);
                } else {
                    Printer.print(s[2] + " is not a saved config!");
                    Client.INSTANCE.getNotificationManager().addNotification( s[2] + " is not a saved config!",2000);
                }
                break;
            case "reload":
                Client.INSTANCE.getConfigManager().getConfigs().clear();
                Client.INSTANCE.getConfigManager().load();
                Printer.print("Reloaded all saved configs. Current number of configs: " + Client.INSTANCE.getConfigManager().getConfigs().size() + "!");
                Client.INSTANCE.getNotificationManager().addNotification( "Reloaded all saved configs. Current number of configs: " + Client.INSTANCE.getConfigManager().getConfigs().size() + "!",2000);
                break;
            case "clear":
                try {
                    if (!Client.INSTANCE.getConfigManager().getConfigs().isEmpty()) {
                        Client.INSTANCE.getConfigManager().clear();
                        Client.INSTANCE.getConfigManager().getConfigs().clear();
                        Printer.print("Cleared all saved configs!");
                        Client.INSTANCE.getNotificationManager().addNotification( "Cleared all saved configs!",2000);
                    } else {
                        Printer.print("You have no saved configs!");
                        Client.INSTANCE.getNotificationManager().addNotification( "You have no saved configs!",2000);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "override":
                if (Client.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Client.INSTANCE.getConfigManager().saveConfig(s[2], s.length > 3 && s[3].equalsIgnoreCase("keys"));
                    Printer.print("Overrode the config named " + s[2] + (s.length > 3 && s[3].equalsIgnoreCase("keys") ? " with keys included" : "") + "!");
                    Client.INSTANCE.getNotificationManager().addNotification( "Overrode the config named " + s[2] + (s.length > 3 && s[3].equalsIgnoreCase("keys") ? " with keys included" : "") + "!",2000);
                } else {
                    Printer.print(s[2] + " is not a saved config!");
                    Client.INSTANCE.getNotificationManager().addNotification( s[2] + " is not a saved config!",2000);
                }
                break;
            case "load":
                if (Client.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Client.INSTANCE.getConfigManager().loadConfig(s[2]);
                    Printer.print("Loaded the config named " + s[2] + "!");
                    Client.INSTANCE.getNotificationManager().addNotification( "Loaded the config named " + s[2] + "!",2000);
                } else {
                    Printer.print(s[2] + " is not a saved config!");
                    Client.INSTANCE.getNotificationManager().addNotification( s[2] + " is not a saved config!",2000);
                }
                break;
        }
    }
}
