/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.celestial.client.Celestial;
import org.celestial.client.cmd.CommandAbstract;
import org.celestial.client.feature.Feature;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;
import org.lwjgl.input.Keyboard;

public class BindCommand
extends CommandAbstract {
    public BindCommand() {
        super("bind", "bind", "\u00a76.bind" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " add \u00a73<name> \u00a73<key> | \u00a76.bind " + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + "remove \u00a73<name> \u00a73<key> | \u00a76.bind " + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + "none", "\u00a76.bind" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " add \u00a73<name> \u00a73<key> | \u00a76.bind " + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + "remove \u00a73<name> \u00a73<key> | \u00a76.bind " + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + "none", "bind");
    }

    @Override
    public void execute(String ... arguments) {
        try {
            if (arguments.length >= 2) {
                if (arguments[0].equals("bind")) {
                    if (arguments[1].equals("none")) {
                        for (Feature feature2 : Celestial.instance.featureManager.getFeatureList()) {
                            if (feature2 == null || feature2.getBind() == 0) continue;
                            feature2.setBind(0);
                        }
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Features successfully" + (Object)((Object)ChatFormatting.WHITE) + " unbinded");
                        NotificationManager.publicity("Bind Manager", (Object)((Object)ChatFormatting.GREEN) + "Features successfully" + (Object)((Object)ChatFormatting.WHITE) + " unbinded", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("list")) {
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.WHITE) + "Binds:");
                        for (Feature feature1 : Celestial.instance.featureManager.getFeatureList()) {
                            if (feature1.getBind() == 0) continue;
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + feature1.getLabel() + (Object)((Object)ChatFormatting.WHITE) + " : " + (Object)((Object)ChatFormatting.GRAY) + Keyboard.getKeyName(feature1.getBind()));
                        }
                    }
                    String moduleName = arguments[2];
                    String bind = arguments[3].toUpperCase();
                    Feature feature = Celestial.instance.featureManager.getFeatureByLabel(moduleName);
                    if (arguments[1].equals("add")) {
                        feature.setBind(Keyboard.getKeyIndex(bind));
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + feature.getLabel() + (Object)((Object)ChatFormatting.WHITE) + " was set on key " + (Object)((Object)ChatFormatting.RED) + "\"" + bind + "\"");
                        NotificationManager.publicity("Bind Manager", (Object)((Object)ChatFormatting.GREEN) + feature.getLabel() + (Object)((Object)ChatFormatting.WHITE) + " was set on key " + (Object)((Object)ChatFormatting.RED) + "\"" + bind + "\"", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("remove")) {
                        feature.setBind(0);
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + feature.getLabel() + (Object)((Object)ChatFormatting.WHITE) + " bind was deleted from key " + (Object)((Object)ChatFormatting.RED) + "\"" + bind + "\"");
                        NotificationManager.publicity("Bind Manager", (Object)((Object)ChatFormatting.GREEN) + feature.getLabel() + (Object)((Object)ChatFormatting.WHITE) + " bind was deleted from key " + (Object)((Object)ChatFormatting.RED) + "\"" + bind + "\"", 4, NotificationType.SUCCESS);
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

