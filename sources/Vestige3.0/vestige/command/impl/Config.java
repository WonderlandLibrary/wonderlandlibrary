package vestige.command.impl;

import vestige.Vestige;
import vestige.command.Command;
import vestige.util.misc.LogUtil;

public class Config extends Command {

    public Config() {
        super("Config", "Loads or saves a config.");
    }

    @Override
    public void onCommand(String[] args) {
        if(args.length >= 3) {
            String action = args[1].toLowerCase();
            String configName = args[2];

            switch (action) {
                case "load":
                    boolean success = Vestige.instance.getFileSystem().loadConfig(configName, false);

                    if(success) {
                        LogUtil.addChatMessage("Loaded config " + configName);
                    } else {
                        LogUtil.addChatMessage("Config not found.");
                    }
                    break;
                case "save":
                    Vestige.instance.getFileSystem().saveConfig(configName);

                    LogUtil.addChatMessage("Saved config " + configName);
                    break;
            }
        }
    }
}
