/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.celestial.client.Celestial;
import org.celestial.client.cmd.CommandAbstract;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.macro.Macro;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;
import org.lwjgl.input.Keyboard;

public class MacroCommand
extends CommandAbstract {
    public MacroCommand() {
        super("macros", "macro", "\u00a76.macro" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " add \u00a73<key> /home_home | \u00a76.macro" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " remove \u00a73<key> | \u00a76.macro" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " clear \u00a73| \u00a76.macro" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " list", "\u00a76.macro" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " add \u00a73<key> </home_home> | \u00a76.macro" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " remove \u00a73<key> | \u00a76.macro" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " clear | \u00a76.macro" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " list", "macro");
    }

    @Override
    public void execute(String ... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equals("macro")) {
                    if (arguments[1].equals("add")) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 3; i < arguments.length; ++i) {
                            stringBuilder.append(arguments[i]);
                            if (i == arguments.length - 1) continue;
                            stringBuilder.append(" ");
                        }
                        String number = stringBuilder.toString();
                        Celestial.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(arguments[2].toUpperCase()), number));
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Added macros for key" + (Object)((Object)ChatFormatting.RED) + " \"" + arguments[2].toUpperCase() + (Object)((Object)ChatFormatting.RED) + "\" " + (Object)((Object)ChatFormatting.WHITE) + "with value " + (Object)((Object)ChatFormatting.RED) + "\"" + number + "\"");
                        NotificationManager.publicity("Macro Manager", (Object)((Object)ChatFormatting.GREEN) + "Added macro for key" + (Object)((Object)ChatFormatting.RED) + " \"" + arguments[2].toUpperCase() + (Object)((Object)ChatFormatting.RED) + "\" " + (Object)((Object)ChatFormatting.WHITE) + "with value " + (Object)((Object)ChatFormatting.RED) + "\"" + number + "\"", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("clear")) {
                        if (Celestial.instance.macroManager.getMacros().isEmpty()) {
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Your macros list is empty!");
                            NotificationManager.publicity("Macro Manager", "Your macro list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        Celestial.instance.macroManager.getMacros().clear();
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Your macros list" + (Object)((Object)ChatFormatting.WHITE) + " successfully cleared!");
                        NotificationManager.publicity("Macro Manager", (Object)((Object)ChatFormatting.GREEN) + "Your macros list" + (Object)((Object)ChatFormatting.WHITE) + " successfully cleared!", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("remove")) {
                        Celestial.instance.macroManager.deleteMacroByKey(Keyboard.getKeyIndex(arguments[2].toUpperCase()));
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Macro " + (Object)((Object)ChatFormatting.WHITE) + "was deleted from key " + (Object)((Object)ChatFormatting.RED) + "\"" + arguments[2].toUpperCase() + "\"");
                        NotificationManager.publicity("Macro Manager", (Object)((Object)ChatFormatting.GREEN) + "Macro " + (Object)((Object)ChatFormatting.WHITE) + "was deleted from key " + (Object)((Object)ChatFormatting.RED) + "\"" + arguments[2].toUpperCase() + "\"", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("list")) {
                        if (Celestial.instance.macroManager.getMacros().isEmpty()) {
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Your macros list is empty!");
                            NotificationManager.publicity("Macro Manager", "Your macros list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        Celestial.instance.macroManager.getMacros().forEach(macro -> ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Macros list: " + (Object)((Object)ChatFormatting.WHITE) + "Macros Name: " + (Object)((Object)ChatFormatting.RED) + macro.getValue() + (Object)((Object)ChatFormatting.WHITE) + ", Macro Bind: " + (Object)((Object)ChatFormatting.RED) + Keyboard.getKeyName(macro.getKey())));
                    }
                }
            } else {
                ChatHelper.addChatMessage(this.getUsage());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

