/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.misc;

import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventPacket;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.ui.Notification.Notification;
import me.Tengoku.Terror.ui.Notification.NotificationManager;
import me.Tengoku.Terror.ui.Notification.NotificationType;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class AntiFlag
extends Module {
    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        if (eventPacket.getPacket() instanceof S08PacketPlayerPosLook && Exodus.INSTANCE.moduleManager.getModuleByName("Speed").isToggled()) {
            Exodus.INSTANCE.moduleManager.getModuleByName("Speed").toggle();
            NotificationManager.show(new Notification(NotificationType.WARNING, "Flag Warning", "You got flagged!", 1));
        }
    }

    public AntiFlag() {
        super("AntiFlag", 0, Category.MISC, "Prevents you from further flagging the anticheat.");
    }
}

