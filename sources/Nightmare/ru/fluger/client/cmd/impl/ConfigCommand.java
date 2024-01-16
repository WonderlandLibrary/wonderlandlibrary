// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.cmd.impl;

import java.util.Iterator;
import org.lwjgl.Sys;
import java.io.File;
import ru.fluger.client.settings.config.Config;
import ru.fluger.client.settings.config.ConfigManager;
import ru.fluger.client.helpers.misc.ChatHelper;
import ru.fluger.client.Fluger;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.fluger.client.cmd.CommandAbstract;

public class ConfigCommand extends CommandAbstract
{
    public ConfigCommand() {
        super("cfg", "configurations", ChatFormatting.RED + ".cfg" + ChatFormatting.WHITE + " save <name> | load <name> | delete <name> | list | create <name> | dir" + ChatFormatting.RED, new String[] { "<name>", "cfg" });
    }
    
    @Override
    public void execute(final String... args) {
        try {
            if (args.length >= 2) {
                final String upperCase = args[1].toUpperCase();
                if (args.length == 3) {
                    final String s = upperCase;
                    switch (s) {
                        case "LOAD": {
                            if (Fluger.instance.configManager.loadConfig(args[2])) {
                                ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "loaded config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                                break;
                            }
                            ChatHelper.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "load config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                            break;
                        }
                        case "SAVE": {
                            if (Fluger.instance.configManager.saveConfig(args[2])) {
                                Fluger.instance.fileManager.saveFiles();
                                ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                                ConfigManager.getLoadedConfigs().clear();
                                Fluger.instance.configManager.load();
                                break;
                            }
                            ChatHelper.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to save config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                            break;
                        }
                        case "DELETE": {
                            if (Fluger.instance.configManager.deleteConfig(args[2])) {
                                ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "deleted config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                                break;
                            }
                            ChatHelper.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to delete config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                            break;
                        }
                        case "CREATE": {
                            Fluger.instance.configManager.saveConfig(args[2]);
                            ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "created config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                            break;
                        }
                    }
                }
                else if (args.length == 2 && upperCase.equalsIgnoreCase("LIST")) {
                    ChatHelper.addChatMessage(ChatFormatting.GREEN + "Configs:");
                    for (final Config config : Fluger.instance.configManager.getContents()) {
                        ChatHelper.addChatMessage(ChatFormatting.RED + config.getName());
                    }
                }
                else if (args.length == 2 && upperCase.equalsIgnoreCase("DIR")) {
                    final File file = new File("C:\\FlugerClient\\game\\configs", "configs");
                    Sys.openURL(file.getAbsolutePath());
                }
            }
            else {
                ChatHelper.addChatMessage(this.getUsage());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
