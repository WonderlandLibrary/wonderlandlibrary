package org.dreamcore.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.cmd.CommandAbstract;
import org.dreamcore.client.helpers.misc.ChatHelper;
import org.dreamcore.client.macro.Macro;
import org.dreamcore.client.ui.notification.NotificationManager;
import org.dreamcore.client.ui.notification.NotificationType;

public class MacroCommand extends CommandAbstract {

    public MacroCommand() {
        super("macros", "macro", "§6.macro" + ChatFormatting.LIGHT_PURPLE + " add " + "§3<key> /home_home | §6.macro" + ChatFormatting.LIGHT_PURPLE + " remove " + "§3<key> | §6.macro" + ChatFormatting.LIGHT_PURPLE + " clear " + "§3| §6.macro" + ChatFormatting.LIGHT_PURPLE + " list", "§6.macro" + ChatFormatting.LIGHT_PURPLE + " add " + "§3<key> </home_home> | §6.macro" + ChatFormatting.LIGHT_PURPLE + " remove " + "§3<key> | §6.macro" + ChatFormatting.LIGHT_PURPLE + " clear " + "| §6.macro" + ChatFormatting.LIGHT_PURPLE + " list", "macro");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equals("macro")) {
                    if (arguments[1].equals("add")) {
                        StringBuilder command = new StringBuilder();
                        for (int i = 3; i < arguments.length; ++i) {
                            command.append(arguments[i]).append(" ");
                        }
                        dreamcore.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(arguments[2].toUpperCase()), command.toString()));
                        ChatHelper.addChatMessage(ChatFormatting.GREEN + "Added" + " macros for key" + ChatFormatting.RED + " \"" + arguments[2].toUpperCase() + ChatFormatting.RED + "\" " + ChatFormatting.WHITE + "with value " + ChatFormatting.RED + command);
                        NotificationManager.publicity("Macro Manager", ChatFormatting.GREEN + "Added" + " macro for key" + ChatFormatting.RED + " \"" + arguments[2].toUpperCase() + ChatFormatting.RED + "\" " + ChatFormatting.WHITE + "with value " + ChatFormatting.RED + command, 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("clear")) {
                        if (dreamcore.instance.macroManager.getMacros().isEmpty()) {
                            ChatHelper.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
                            NotificationManager.publicity("Macro Manager", "Your macro list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        dreamcore.instance.macroManager.getMacros().clear();
                        ChatHelper.addChatMessage(ChatFormatting.GREEN + "Your macros list " + ChatFormatting.WHITE + " successfully cleared!");
                        NotificationManager.publicity("Macro Manager", ChatFormatting.GREEN + "Your macros list " + ChatFormatting.WHITE + " successfully cleared!", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("remove")) {
                        dreamcore.instance.macroManager.deleteMacroByKey(Keyboard.getKeyIndex(arguments[2].toUpperCase()));
                        ChatHelper.addChatMessage(ChatFormatting.GREEN + "Macro " + ChatFormatting.WHITE + "was deleted from key " + ChatFormatting.RED + "\"" + arguments[2].toUpperCase() + "\"");
                        NotificationManager.publicity("Macro Manager", ChatFormatting.GREEN + "Macro " + ChatFormatting.WHITE + "was deleted from key " + ChatFormatting.RED + "\"" + arguments[2].toUpperCase() + "\"", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("list")) {
                        if (dreamcore.instance.macroManager.getMacros().isEmpty()) {
                            ChatHelper.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
                            NotificationManager.publicity("Macro Manager", "Your macros list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        dreamcore.instance.macroManager.getMacros().forEach(macro -> ChatHelper.addChatMessage(ChatFormatting.GREEN + "Macros list: " + ChatFormatting.WHITE + "Macros Name: " + ChatFormatting.RED + macro.getValue() + ChatFormatting.WHITE + ", Macro Bind: " + ChatFormatting.RED + Keyboard.getKeyName(macro.getKey())));
                    }
                }
            } else {
                ChatHelper.addChatMessage(getUsage());
            }
        } catch (Exception ignored) {

        }
    }
}
