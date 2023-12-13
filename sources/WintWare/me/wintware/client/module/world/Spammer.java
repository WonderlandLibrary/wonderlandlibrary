/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.world;

import java.security.SecureRandom;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventTick;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.other.TimerUtils;
import net.minecraft.client.Minecraft;

public class Spammer
extends Module {
    String lastMessage = "";
    Setting delay;
    TimerUtils timer = new TimerUtils();
    private static String alphabet = "ABCDEFG!()1234%$#@!^%&*()_567890~]''/.,[HIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom secureRandom = new SecureRandom();

    public Spammer() {
        super("Spammer", Category.World);
        this.delay = new Setting("Delay", this, 3000.0, 1.0, 20000.0, false);
        Main.instance.setmgr.rSetting(this.delay);
    }

    public static String randomString(int strLength) {
        StringBuilder stringBuilder = new StringBuilder(strLength);
        for (int i = 0; i < strLength; ++i) {
            stringBuilder.append(alphabet.charAt(secureRandom.nextInt(alphabet.length())));
        }
        return stringBuilder.toString();
    }

    @EventTarget
    public void onTickUpdate(EventTick event) {
        if (!this.getState()) {
            return;
        }
        String[] messages = new String[]{"\u041a\u0423\u041f\u0418 AKRIEN CLIENT vk.com/akrienclient", "\u041a\u0423\u041f\u0418 AKRIEN CLIENT vk.com/akrienclient", "\u041a\u0423\u041f\u0418 AKRIEN CLIENT vk.com/akrienclient", "\u041a\u0423\u041f\u0418 AKRIEN CLIENT vk.com/akrienclient", "\u041a\u0423\u041f\u0418 AKRIEN CLIENT vk.com/akrienclient", "\u041a\u0423\u041f\u0418 AKRIEN CLIENT vk.com/akrienclient", "\u041a\u0423\u041f\u0418 AKRIEN CLIENT vk.com/akrienclient", "\u041a\u0423\u041f\u0418 AKRIEN CLIENT vk.com/akrienclient", "\u041a\u0423\u041f\u0418 AKRIEN CLIENT vk.com/akrienclient", "\u041a\u0423\u041f\u0418 AKRIEN CLIENT vk.com/akrienclient", "\u041a\u0423\u041f\u0418 AKRIEN CLIENT vk.com/akrienclient", "\u041a\u0423\u041f\u0418 AKRIEN CLIENT vk.com/akrienclient"};
        String finalText = messages[secureRandom.nextInt(messages.length)];
        if (this.timer.hasReached(this.delay.getValFloat()) && !this.lastMessage.equals(finalText) || this.lastMessage == null) {
            Minecraft.player.sendChatMessage("![" + Spammer.randomString(10) + "] " + finalText + "\u041a\u0423\u041f\u0418 AKRIEN CLIENT vk.com/akrienclient" + Spammer.randomString(10) + "]");
            this.lastMessage = finalText;
            this.timer.reset();
        }
    }

    static {
        alphabet = alphabet + alphabet.toLowerCase();
    }
}

