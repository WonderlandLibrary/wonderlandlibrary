package org.dreamcore.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.cmd.CommandAbstract;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.helpers.misc.ChatHelper;
import org.dreamcore.client.ui.notification.NotificationManager;
import org.dreamcore.client.ui.notification.NotificationType;

public class BindCommand extends CommandAbstract {

    public BindCommand() {
        super("bind", "bind", "§6.bind" + ChatFormatting.LIGHT_PURPLE + " add " + "§3<name> §3<key> | §6.bind " + ChatFormatting.LIGHT_PURPLE + "remove " + "§3<name> §3<key>", "§6.bind" + ChatFormatting.LIGHT_PURPLE + " add " + "§3<name> §3<key> | §6.bind" + ChatFormatting.LIGHT_PURPLE + "remove " + "§3<name> <key> | §6.bind" + ChatFormatting.LIGHT_PURPLE + "clear", "bind");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length == 4) {
                String moduleName = arguments[2];
                String bind = arguments[3].toUpperCase();
                Feature feature = dreamcore.instance.featureManager.getFeatureByLabel(moduleName);
                if (arguments[0].equals("bind")) {
                    switch (arguments[1]) {
                        case "add":
                            feature.setBind(Keyboard.getKeyIndex(bind));
                            ChatHelper.addChatMessage(ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"");
                            NotificationManager.publicity("Bind Manager", ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"", 4, NotificationType.SUCCESS);
                            break;
                        case "remove":
                            feature.setBind(0);
                            ChatHelper.addChatMessage(ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"");
                            NotificationManager.publicity("Bind Manager", ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"", 4, NotificationType.SUCCESS);
                            break;
                        case "clear":
                            if (!feature.getLabel().equals("ClickGui")) {
                                feature.setBind(0);
                            }
                            break;
                        case "list":
                            if (feature.getBind() == 0) {
                                ChatHelper.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
                                NotificationManager.publicity("Macro Manager", "Your macros list is empty!", 4, NotificationType.ERROR);
                                return;
                            }
                            dreamcore.instance.featureManager.getFeatureList().forEach(feature1 -> ChatHelper.addChatMessage(ChatFormatting.GREEN + "Binds list: " + ChatFormatting.WHITE + "Binds Name: " + ChatFormatting.RED + feature1.getBind() + ChatFormatting.WHITE + ", Macro Bind: " + ChatFormatting.RED + Keyboard.getKeyName(feature1.getBind())));
                    }
                }
            } else {
                ChatHelper.addChatMessage(this.getUsage());
            }
        } catch (Exception ignored) {

        }
    }
}
