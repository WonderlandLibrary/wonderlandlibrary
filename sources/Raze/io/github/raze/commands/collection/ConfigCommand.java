package io.github.raze.commands.collection;

import io.github.raze.commands.system.Command;
import io.github.raze.configuration.impl.DeleteConfig;
import io.github.raze.configuration.impl.ListConfig;
import io.github.raze.configuration.impl.LoadConfig;
import io.github.raze.configuration.impl.SaveConfig;

public class ConfigCommand extends Command {

    public ConfigCommand() {
        super("Config", "Create or delete configs", "config save <Name> | config load <Name> | config delete <Name> | config list", "cfg");
    }

    public String onCommand(String[] arguments, String command) {
        if (arguments.length < 2) {
            return "Usage: " + getSyntax();
        }

        String subCommand = arguments[1].toLowerCase();

        String configName;
        if(!subCommand.equals("list")) {
            configName = arguments[2] + ".json";
        } else {
            configName = "";
        }

        SaveConfig saveConfig = new SaveConfig();
        LoadConfig loadConfig = new LoadConfig();
        ListConfig listConfig = new ListConfig();
        DeleteConfig deleteConfig = new DeleteConfig();

        switch (subCommand) {
            case "save":
                saveConfig.saveConfig("raze/configs/", configName);
                if(!saveConfig.getError().equals("File name is empty!"))
                    return "Config saved: " + configName;
                else
                    return "Error: " + saveConfig.getError();

            case "load":
                loadConfig.loadConfig("raze/configs/",configName);
                if(!loadConfig.getError().equals("Incompatible version found!"))
                    return "Config loaded: " + configName;
                else
                    return "Error: " + loadConfig.getError();

            case "delete":
                if(deleteConfig.doesConfigExist(configName)) {
                    deleteConfig.deleteConfig(configName);
                    return "Config deleted: " + configName;
                } else {
                    return "Config not found.";
                }

            case "list":
                return listConfig.listConfigs("raze/configs");

        }
        return "";
    }

}
