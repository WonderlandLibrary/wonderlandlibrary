/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.commands.impl;

import java.io.File;
import net.minecraft.util.ChatComponentText;
import tk.rektsky.commands.Command;
import tk.rektsky.commands.impl.HelpCommand;
import tk.rektsky.config.ConfigManager;
import tk.rektsky.module.impl.render.Notification;
import tk.rektsky.utils.display.ColorUtil;

public class ConfigCommand
extends Command {
    public ConfigCommand() {
        super("config", new String[]{"conf"}, "<save/load/list/delete/del> <Name>", "Lets you save/load/list local configs");
    }

    @Override
    public void onCommand(String label, String[] args) {
        if (args.length < 1) {
            HelpCommand.displayCommandInfomation(this);
            return;
        }
        if (args[0].equals("list")) {
            File[] files;
            StringBuilder names = new StringBuilder();
            File _file = new File("rektsky/configs/");
            if (!_file.exists()) {
                _file.mkdirs();
            }
            for (File f2 : files = _file.listFiles()) {
                names.append("[Config] " + f2.getName().replaceAll(".yml", "")).append("\n");
            }
            String _name = names.toString();
            if (_name.endsWith("\n")) {
                _name = _name.substring(0, _name.length() - 1);
            }
            this.mc.thePlayer.addChatMessage(new ChatComponentText(_name));
            return;
        }
        if (args[0].equals("save")) {
            if (args.length < 2) {
                HelpCommand.displayCommandInfomation(this);
                return;
            }
            ConfigManager.saveConfig(args[1]);
            return;
        }
        if (args[0].equals("load")) {
            if (args.length < 2) {
                HelpCommand.displayCommandInfomation(this);
                return;
            }
            ConfigManager.loadConfig(args[1]);
            return;
        }
        if (args[0].equals("delete") || args[0].equals("del")) {
            if (args.length < 2) {
                HelpCommand.displayCommandInfomation(this);
                return;
            }
            File _file = new File("rektsky/configs/", args[1] + ".yml");
            if (_file.delete()) {
                Notification.displayNotification(new Notification.PopupMessage("Config", "Config: " + args[1] + " deleted successfully!", ColorUtil.NotificationColors.GREEN, 40));
            } else {
                Notification.displayNotification(new Notification.PopupMessage("Config", "Failed to delete: " + args[1], ColorUtil.NotificationColors.RED, 40));
            }
            return;
        }
        HelpCommand.displayCommandInfomation(this);
    }
}

