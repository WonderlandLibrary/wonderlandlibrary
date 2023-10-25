/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.io.File;
import java.util.Scanner;
import org.apache.commons.lang3.RandomStringUtils;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceiveMessage;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;
import org.lwjgl.Sys;

public class AutoAuth
extends Feature {
    private String password;
    private final ListSetting passwordMode = new ListSetting("Password Mode", "Custom", "Custom", "Random", "Qwerty");
    private final BooleanSetting showPasswordInChat = new BooleanSetting("Show Password In Chat", true, () -> true);

    public AutoAuth() {
        super("AutoAuth", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0440\u0435\u0433\u0435\u0441\u0442\u0440\u0438\u0440\u0443\u0435\u0442\u0441\u044f \u0438 \u043b\u043e\u0433\u0438\u043d\u0438\u0442\u0441\u044f \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440\u0430\u0445", Type.Player);
        this.addSettings(this.passwordMode, this.showPasswordInChat);
    }

    @Override
    public void onEnable() {
        if (this.passwordMode.currentMode.equals("Custom")) {
            try {
                File file = new File(AutoAuth.mc.gameDir + "\\celestial", "AutoAuthPassword.json");
                if (!file.exists()) {
                    file.createNewFile();
                }
                Sys.openURL(file.getAbsolutePath());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        super.onEnable();
    }

    @EventTarget
    public void onReceiveMessage(EventReceiveMessage event) {
        if (this.passwordMode.currentMode.equals("Custom")) {
            try {
                File file = new File(AutoAuth.mc.gameDir + "\\celestial", "AutoAuthPassword.json");
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    this.password = scanner.nextLine();
                }
            }
            catch (Exception file) {}
        } else if (this.passwordMode.currentMode.equals("Qwerty")) {
            this.password = "qwerty123";
        } else if (this.passwordMode.currentMode.equals("Random")) {
            String str1 = RandomStringUtils.randomAlphabetic(5);
            String str2 = RandomStringUtils.randomPrint(5);
            this.password = str1 + str2;
        }
        if (this.passwordMode.currentMode.equals("Custom") && (this.password == null || this.password.isEmpty())) {
            return;
        }
        if (event.getMessage().contains("/reg") || event.getMessage().contains("/register") || event.getMessage().contains("\u0417\u0430\u0440\u0435\u0433\u0435\u0441\u0442\u0440\u0438\u0440\u0443\u0439\u0442\u0435\u0441\u044c")) {
            AutoAuth.mc.player.sendChatMessage("/reg " + this.password + " " + this.password);
            if (this.showPasswordInChat.getCurrentValue()) {
                ChatHelper.addChatMessage("Your password: " + (Object)((Object)ChatFormatting.RED) + this.password);
            }
            NotificationManager.publicity("AutoAuth", "You are successfully registered!", 4, NotificationType.SUCCESS);
        } else if (event.getMessage().contains("\u0410\u0432\u0442\u043e\u0440\u0438\u0437\u0443\u0439\u0442\u0435\u0441\u044c") || event.getMessage().contains("/l")) {
            AutoAuth.mc.player.sendChatMessage("/login " + this.password);
            NotificationManager.publicity("AutoAuth", "You are successfully login!", 4, NotificationType.SUCCESS);
        }
    }
}

