package ru.smertnix.celestial.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.command.CommandAbstract;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.ui.config.Config;
import ru.smertnix.celestial.ui.config.ConfigManager;
import ru.smertnix.celestial.utils.other.ChatUtils;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import java.io.File;

public class ConfigCommand extends CommandAbstract {

    public ConfigCommand() {
        super("cfg", "configurations", ChatFormatting.RED + ".cfg" + ChatFormatting.WHITE + " save <name> | load <name> | delete <name> | list | create <name> | dir" + ChatFormatting.RED, "<name>", "cfg");
    }

    @Override
    public void execute(String... args) {
        try {
            if (args.length >= 2) {
                String upperCase = args[1].toUpperCase();
                if (args.length == 3) {
                    switch (upperCase) {
                        case "LOAD":
                            if (Celestial.instance.configManager.loadConfig(args[2])) {
                                ChatUtils.addChatMessage(ChatFormatting.WHITE + "Конфиг " + ChatFormatting.RED + "\'" + args[2] + "\'" + ChatFormatting.WHITE + " был успешно загружен!");
                            } else {
                                ChatUtils.addChatMessage(ChatFormatting.WHITE + "Такого конфига не существует пацан(");
                            }
                            break;
                        case "SAVE":
                            if (Celestial.instance.configManager.saveConfig(args[2])) {
                                Celestial.instance.fileManager.saveFiles();
                                ChatUtils.addChatMessage(ChatFormatting.WHITE + "Конфиг" + ChatFormatting.RED + "\'" + args[2] + "\'" + ChatFormatting.WHITE + " был сохранен!");
                                Celestial.instance.configManager.load();
                            } else {
                            	  ChatUtils.addChatMessage(ChatFormatting.WHITE + "Ошибка");
                            	  }
                            break;
                        case "DELETE":
                            if (Celestial.instance.configManager.deleteConfig(args[2])) {
                            	 ChatUtils.addChatMessage(ChatFormatting.WHITE + "Конфиг " + ChatFormatting.RED + "\'" + args[2] + "\'" + ChatFormatting.WHITE + " был удалён!");
                            	 } else {
                            	  ChatUtils.addChatMessage(ChatFormatting.WHITE + "Конфиг был не найден.");
                            	  }
                            break;
                        case "CREATE":
                            Celestial.instance.configManager.saveConfig(args[2]);
                            ChatUtils.addChatMessage(ChatFormatting.WHITE + "Конфиг " + ChatFormatting.RED + "\'" + args[2] + "\'" + ChatFormatting.WHITE + " был создан!");
                            break;

                    }
                } else if (args.length == 2 && upperCase.equalsIgnoreCase("LIST")) {
                    ChatUtils.addChatMessage(ChatFormatting.GREEN + "Configs:");
                    for (Config config : Celestial.instance.configManager.getContents()) {
                        ChatUtils.addChatMessage(ChatFormatting.RED + config.getName());
                    }
                } else if (args.length == 2 && upperCase.equalsIgnoreCase("DIR")) {
                    File file = new File("C:\\Minced", "configs");
                    Sys.openURL(file.getAbsolutePath());
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}