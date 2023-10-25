/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.celestial.client.Celestial;
import org.celestial.client.cmd.CommandAbstract;
import org.celestial.client.event.EventManager;
import org.celestial.client.feature.Feature;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class PanicCommand
extends CommandAbstract {
    public PanicCommand() {
        super("panic", "panic", "panic", "panic");
    }

    @Override
    public void execute(String ... args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("panic")) {
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.RED) + "disabled " + (Object)((Object)ChatFormatting.WHITE) + "all features!");
                NotificationManager.publicity("Panic Manager", (Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.RED) + "disabled " + (Object)((Object)ChatFormatting.WHITE) + "all features!", 4, NotificationType.SUCCESS);
                for (Feature feature : Celestial.instance.featureManager.getFeatureList()) {
                    feature.setBind(0);
                    if (!feature.getState()) continue;
                    feature.toggle();
                    EventManager.unregister(feature);
                }
            }
        } else {
            ChatHelper.addChatMessage(this.getUsage());
        }
    }
}

