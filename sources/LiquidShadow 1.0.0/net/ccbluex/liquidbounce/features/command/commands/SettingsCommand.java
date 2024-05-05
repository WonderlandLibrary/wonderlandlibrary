package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.special.Setting;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SettingsCommand extends Command {
    public SettingsCommand() {
        super("settings", "setting","config");
    }

    @Override
    public void execute(@NotNull String[] args) {
        if (args.length <= 1) {
            chatSyntax("settings <load/save/list/import>");
            return;
        }

        switch (args[1]) {
            case "load": {
                if (args.length < 3) {
                    chatSyntax("settings load <name>");
                    return;
                }
                Setting setting = LiquidBounce.settingManager.getSetting(args[2]);
                if (setting != null) {
                    chat("loading setting " + args[2] + ",please wait...");
                    setting.load();
                    LiquidBounce.commandManager.getCommand("reload").execute(new String[]{"reload"});
                    chat("loaded setting " + args[2] + " success!");
                } else {
                    chat("this setting is not exist!");
                    return;
                }
                break;
            }
            case "save": {
                if (args.length < 3) {
                    chatSyntax("settings save <name>");
                    return;
                }
                Setting setting = LiquidBounce.settingManager.getSetting(args[2]);
                if (setting != null) {
                    chat("saving setting " + args[2] + ",please wait...");
                    setting.save();
                    chat("saved setting" + args[2] + " success!");
                } else {
                    chat("saving setting " + args[2] + ",please wait...");
                    LiquidBounce.settingManager.addSetting(new Setting(args[2],true));
                    chat("saved setting" + args[2] + " success!");
                }
                break;
            }
            case "list":
                chat("your settings:");
                for (Setting setting : LiquidBounce.settingManager.getSettings()) {
                    chat(setting.getName());
                }
                break;
            case "import":
                if (args.length < 3) {
                    chatSyntax("settings import <name>");
                    return;
                }
                Path settingsDir = Paths.get(LiquidBounce.fileManager.settingsDir.getAbsolutePath(),args[2]);
                File settingsDirFile = settingsDir.toFile();
                if (settingsDirFile.exists()) {
                    LiquidBounce.settingManager.addSetting(new Setting(args[2],false));
                    chat("imported setting " + args[2] + " successful!");
                } else {
                    chat("This settings is not exist!");
                }

        }
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull String[] args) {
        if (args.length == 1) {
            List<String> cmds = Arrays.asList("load","save","list");
            List<String> s = new ArrayList<>();
            for (String cmd : cmds) {
                if (cmd.startsWith(args[0].toLowerCase())) {
                    s.add(cmd);
                }
            }
            return s;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("save")) {
                List<String> settings = new ArrayList<>();
                for (Setting setting : LiquidBounce.settingManager.getSettings()) {
                    settings.add(setting.getName());
                }
                List<String> s = new ArrayList<>();
                for (String setting : settings) {
                    if (setting.startsWith(args[1].toLowerCase())) {
                        s.add(setting);
                    }
                }
                return s;
            }
        }
        return Collections.emptyList();
    }
}
