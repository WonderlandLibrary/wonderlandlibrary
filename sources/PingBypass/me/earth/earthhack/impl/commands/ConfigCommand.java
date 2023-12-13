package me.earth.earthhack.impl.commands;

import me.earth.earthhack.api.command.Command;
import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.api.util.TextColor;
import me.earth.earthhack.impl.services.config.ConfigManager;
import me.earth.earthhack.impl.util.text.ChatUtil;

public class ConfigCommand extends Command implements Globals
{
    public ConfigCommand()
    {
        super(new String[][]{{"config"}, {"save", "load"}});
    }

    @Override
    public void execute(String[] args)
    {
        if (args.length > 1)
        {
            switch (args[1].toLowerCase())
            {
                case "save":
                    ChatUtil.sendMessage(TextColor.GREEN + "Saving config...");
                    ConfigManager.getInstance().save();
                    return;
                case "load":
                    ChatUtil.sendMessage(TextColor.GREEN + "Loading config...");
                    ConfigManager.getInstance().load();
                    return;
                default:
            }
        }

        ChatUtil.sendMessage(TextColor.RED + "Try: " + this.getFullUsage());
    }
}
