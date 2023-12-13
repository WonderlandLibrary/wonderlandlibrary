/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.player;

import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.DoubleSetting;
import tk.rektsky.module.settings.StringSetting;
import tk.rektsky.rektskyapi.utils.StringUtils;

public class Spammer
extends Module {
    public StringSetting message = new StringSetting("Message", 0, 100, "Buy   RektSky   at  rektsky .  ml");
    public BooleanSetting antiKick = new BooleanSetting("AntiSpam Bypass", true);
    public DoubleSetting delay = new DoubleSetting("Delay", 0.1, 20.0, 5.0);
    private long lastMessageTime = 0L;

    public Spammer() {
        super("Spammer", "Spams the chat (use .spammer command)", Category.PLAYER);
    }

    @Subscribe
    public void onWorldTick(WorldTickEvent event) {
        if ((double)(System.currentTimeMillis() - this.lastMessageTime) > this.delay.getValue() * 1000.0) {
            String message = this.message.getValue();
            if (this.antiKick.getValue().booleanValue()) {
                message = "[" + StringUtils.generateRandomString(6) + "]  " + message + "  [" + StringUtils.generateRandomString(6) + "]";
            }
            this.mc.thePlayer.sendChatMessage(message);
            this.lastMessageTime = System.currentTimeMillis();
        }
    }
}

