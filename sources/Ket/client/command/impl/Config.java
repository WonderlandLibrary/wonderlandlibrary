package client.command.impl;

import client.Client;
import client.command.Command;
import client.util.ChatUtil;
import client.util.file.config.ConfigFile;
import client.util.file.config.ConfigManager;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public final class Config extends Command {

    public Config() {
        super("Allows you to save/load configuration files", "config");
    }

    @Override
    public void execute(final String[] args) {
        final ConfigManager configManager = Client.INSTANCE.getConfigManager();
        final String command = args[1].toLowerCase();
        switch (args.length) {
            case 3:
                final String name = args[2];
                switch (command) {
                    case "load":
                        configManager.update();
                        final ConfigFile config = configManager.get(name, false);
                        if (config != null) {
                            CompletableFuture.runAsync(() -> {
                                if (config.read()) {
                                    ChatUtil.display("Loaded %s config file!", name);
                                    if (!name.equalsIgnoreCase("latest")) ChatUtil.display("Accidentally loaded this config? Use .config load latest, to load the most recent autosaved config");
                                } else ChatUtil.display("Config file not found!");
                            });
                        } else ChatUtil.display("Config file not found!");
                        break;
                    case "save":
                    case "create":
                        if (name.equalsIgnoreCase("latest")) {
                            ChatUtil.display("This is a reserved file name.");
                            return;
                        }
                        CompletableFuture.runAsync(() -> {
                            configManager.set(name);
                            ChatUtil.display("Saved config file!");
                            ChatUtil.display("Remember if you want to share this config, use .config folder to open the path!");
                        });
                        break;
                    default:
                        ChatUtil.display("Usage: .config save/load/list/folder");
                        break;
                }
                break;
            case 2:
                switch (command) {
                    case "list":
                        ChatUtil.display("Click on the config you want to load.");
                        configManager.update();
                        configManager.forEach(configFile -> {
                            final String configName = configFile.getFile().getName().replace(".json", "");
                            final String configCommand = ".config load " + configName;
                            final EnumChatFormatting color = EnumChatFormatting.AQUA;
                            final ChatComponentText chatText = new ChatComponentText(color + "> " + configName);
                            final ChatComponentText hoverText = new ChatComponentText(String.format("Click to load config %s", configName));
                            chatText.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, configCommand)).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
                            mc.thePlayer.addChatMessage(chatText);
                        });
                        break;
                    case "folder":
                        try {
                            Desktop desktop = Desktop.getDesktop();
                            File dirToOpen = new File(String.valueOf(ConfigManager.CONFIG_DIRECTORY));
                            desktop.open(dirToOpen);
                            ChatUtil.display("Opened config folder");
                        } catch (IllegalArgumentException | IOException iae) {
                            ChatUtil.display("Config file not found!");
                        }
                        break;
                    default:
                        ChatUtil.display("Valid actions are save, load, list, and folder");
                        break;
                }
                break;
            default:
                ChatUtil.display("Valid actions are save, load, list, and folder");
                break;
        }
    }
}
