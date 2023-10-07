package koks.command;

import koks.Koks;
import koks.api.ConfigSystem;
import koks.api.registry.command.Command;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.io.IOException;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Command.Info(name = "config", aliases = {"cfg"}, description = "load configs")
public class Config extends Command {

    @Override
    public boolean execute(String[] args) {
        final ConfigSystem configSystem = Koks.getKoks().configSystem;
        if (args.length >= 1) {

            String name = "";
            for (int i = 1; i < args.length; i++) {
                name += args[i] + " ";
            }
            if (args.length >= 2) {
                name = name.substring(0, name.length() - 1);
            }

            switch (args[0].toLowerCase()) {
                case "online":
                    configSystem.loadOnline(name);
                    break;
                case "list":
                    new Thread(() -> {
                        for (int i = 0; i < 255; i++)
                            sendMessage("", false);

                        if (args.length < 2 || args[1].equalsIgnoreCase("online")) {
                            sendMessage("§7----§c§lOnline§7----");
                            for (String s : configSystem.configList()) {
                                sendMessage("§7" + s);
                            }
                        }
                        final File[] files = ConfigSystem.DIR.listFiles();
                        if (files != null && (args.length < 2 || args[1].equalsIgnoreCase("local"))) {
                            if (args.length < 2)
                                sendMessage("", false);
                            sendMessage("§7----§a§lLocal§7----");
                            for (File file : files) {
                                if (file.getName().contains(".koks"))
                                    sendMessage("§7" + file.getName().replace(".koks", ""));
                            }
                        }
                    }).start();
                    break;
                case "load":
                case "local":
                    configSystem.loadConfig(name);
                    break;
                case "create":
                case "save":
                    try {
                        configSystem.createConfig(name, Koks.clName);
                        sendMessage("Created §e" + name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    sendHelp(this, "create [Name]", "local [Name]", "online [Name]", "list {local:online}");
                    break;
            }
        } else {
            sendHelp(this, "create [Name]", "local [Name]", "online [Name]", "list {local:online}");
        }
        return true;
    }
}
