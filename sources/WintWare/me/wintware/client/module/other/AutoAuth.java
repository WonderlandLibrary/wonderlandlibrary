/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.other;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventChatMessage;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.ui.notification.NotificationPublisher;
import me.wintware.client.ui.notification.NotificationType;
import me.wintware.client.utils.other.ChatUtils;
import net.minecraft.client.Minecraft;

public class AutoAuth
extends Module {
    public AutoAuth() {
        super("AutoAuth", Category.Player);
    }

    @EventTarget
    public void onReceiveChat(EventChatMessage event) {
        if (event.getMessage().contains("/reg") || event.getMessage().contains("/register") || event.getMessage().contains("\u0417\u0430\u0440\u0435\u0433\u0435\u0441\u0442\u0440\u0438\u0440\u0443\u0439\u0442\u0435\u0441\u044c")) {
            Minecraft.player.sendChatMessage("/reg 13371337 13371337");
            ChatUtils.addChatMessage("Your password: 13371337");
            NotificationPublisher.queue("AutoAuth", "Your password: 13371337", NotificationType.INFO);
        } else if (event.getMessage().contains("\u0410\u0432\u0442\u043e\u0440\u0438\u0437\u0443\u0439\u0442\u0435\u0441\u044c") || event.getMessage().contains("/l")) {
            Minecraft.player.sendChatMessage("/login 13371337");
        }
    }
}

