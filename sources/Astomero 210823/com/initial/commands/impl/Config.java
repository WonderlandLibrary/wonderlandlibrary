package com.initial.commands.impl;

import com.initial.commands.*;
import net.minecraft.util.*;
import com.initial.*;
import com.initial.modules.*;
import java.io.*;
import java.util.*;

public class Config extends Command
{
    public Config() {
        super("Config", "Loads / Saves a config.", "config", new String[0]);
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length > 1) {
            final String type = args[0];
            final String name = args[1];
            final String s = type;
            switch (s) {
                case "save": {
                    Astomero.addChatMessage("Saved the config " + EnumChatFormatting.RED + name + EnumChatFormatting.WHITE + ".");
                    try {
                        Astomero.instance.configManager.save(name);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "load": {
                    Astomero.addChatMessage("Loaded the config " + EnumChatFormatting.RED + name + EnumChatFormatting.WHITE + ".");
                    for (final Module m : Astomero.instance.moduleManager.getModules()) {
                        m.setToggled(false);
                    }
                    try {
                        Astomero.instance.configManager.load(name);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "delete": {
                    try {
                        Astomero.instance.configManager.delete(name);
                    }
                    catch (Exception e) {
                        Astomero.addChatMessage("Java just killed itself, contact da owner");
                    }
                    finally {
                        Astomero.addChatMessage("Deleted config " + name + "!");
                    }
                    break;
                }
            }
        }
        else {
            try {
                final String type = args[0];
                if (type.equalsIgnoreCase("list")) {
                    Astomero.addChatMessage("Config List:");
                    try {
                        for (final File archivogay : Objects.requireNonNull(Objects.requireNonNull(Astomero.instance.dir).listFiles())) {
                            final String fileName = archivogay.getName().substring(0, archivogay.getName().length() - 5);
                            Astomero.addChatMessage(fileName);
                        }
                    }
                    catch (NullPointerException exc) {
                        Astomero.addChatMessage("No configs saved.");
                    }
                    Astomero.addChatMessage("");
                }
            }
            catch (Exception e2) {
                Astomero.addChatMessage("Exception.");
            }
        }
    }
}
