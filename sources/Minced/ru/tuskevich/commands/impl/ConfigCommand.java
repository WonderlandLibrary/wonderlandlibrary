// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands.impl;

import org.lwjgl.Sys;
import java.util.Objects;
import ru.tuskevich.util.config.ConfigManager;
import java.io.File;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.Minced;
import ru.tuskevich.commands.Command;
import ru.tuskevich.commands.CommandAbstract;

@Command(name = "cfg", description = "Allows you to manage configs")
public class ConfigCommand extends CommandAbstract
{
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length >= 2) {
            try {
                if (args[1].equals("save")) {
                    Minced.getInstance().configManager.saveConfig(args[2]);
                    this.sendMessage("Config " + ChatFormatting.BLUE + "successfully" + ChatFormatting.WHITE + " saved");
                }
                if (args[1].equals("load")) {
                    if (Minced.getInstance().configManager.loadConfig(args[2])) {
                        this.sendMessage("Config " + ChatFormatting.BLUE + "successfully" + ChatFormatting.WHITE + " loaded");
                    }
                    else {
                        this.sendMessage("Config not founded");
                    }
                }
                if (args[1].equals("delete")) {
                    Minced.getInstance().configManager.deleteConfig(args[2]);
                    this.sendMessage("Config " + ChatFormatting.BLUE + "successfully" + ChatFormatting.WHITE + " removed");
                }
                if (args[1].equals("list")) {
                    final File file = new File("C:\\Minced\\game\\minced\\configs");
                    if (ConfigManager.getLoadedConfigs().isEmpty()) {
                        this.sendMessage("Configs not founded");
                    }
                    for (final File s : Objects.requireNonNull(file.listFiles())) {
                        this.sendMessage(s.getName().replaceAll(".json", ""));
                    }
                }
                if (args[1].equals("dir")) {
                    final File file = new File("C:\\Minced\\game\\minced\\configs");
                    Sys.openURL(file.getAbsolutePath());
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        else {
            this.error();
        }
    }
    
    @Override
    public void error() {
        this.sendMessage(ChatFormatting.GRAY + "Command use" + ChatFormatting.WHITE + ":");
        this.sendMessage(ChatFormatting.WHITE + ".cfg load " + ChatFormatting.GRAY + "<name>");
        this.sendMessage(ChatFormatting.WHITE + ".cfg save " + ChatFormatting.GRAY + "<name>");
        this.sendMessage(ChatFormatting.WHITE + ".cfg delete " + ChatFormatting.GRAY + "<name>");
        this.sendMessage(ChatFormatting.WHITE + ".cfg list");
        this.sendMessage(ChatFormatting.WHITE + ".cfg dir");
    }
}
