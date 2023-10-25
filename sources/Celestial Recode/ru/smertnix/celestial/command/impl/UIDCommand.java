package ru.smertnix.celestial.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import ru.smertnix.celestial.command.CommandAbstract;
import ru.smertnix.celestial.utils.other.ChatUtils;
import ru.smertnix.celestial.utils.other.NameUtils;

public class UIDCommand extends CommandAbstract {
    public static String oldName;

    public UIDCommand() {
        super("uid", "uid", ".fakename" + ChatFormatting.WHITE + " set <name> |" + ChatFormatting.WHITE + " reset", ".fakename" + ChatFormatting.WHITE + " set <name> |" + ChatFormatting.WHITE + " reset", "uid", "setuid", "id");
    }

    @Override
    public void execute(String ... arguments) {
        try {
            if (arguments.length >= 2) {
                oldName = "reborn";
                    if (arguments[1].equalsIgnoreCase("reset")) {
                    	NameUtils.setUID(oldName);
                        ChatUtils.addChatMessage("UID сброшен!");
                    } else {
                    	if (arguments[1].length() > 5) {
                    	    ChatUtils.addChatMessage("Кастомный юид был добавлен " + ChatFormatting.RED + "bolshoy");
                    	} else {
                            ChatUtils.addChatMessage("UID был сброшен " + ChatFormatting.GREEN + arguments[1]);
                            if (arguments[1].contains("§")) {
                            	NameUtils.setUID(arguments[1].replaceAll("§", "§"));
                            } else if (arguments[1].contains("§")) {
                            	NameUtils.setUID(arguments[1].replaceAll("§", "§"));
                            } else {
                              	NameUtils.setUID(arguments[1]);
                            }
                            
                    	}
                    }
            } else {
            	ChatUtils.addChatMessage("Список доступных команд:");
            	ChatUtils.addChatMessage("uid <UID> (Поставить кастом юид)");
            	ChatUtils.addChatMessage("uid reset (Сброс юида в клиенте)");
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}
