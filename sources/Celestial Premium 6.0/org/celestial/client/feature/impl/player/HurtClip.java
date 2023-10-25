/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  ru.wendoxd.wclassguard.WXFuscator
 */
package org.celestial.client.feature.impl.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.client.CPacketPlayer;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.misc.FreeCam;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;
import ru.wendoxd.wclassguard.WXFuscator;

public class HurtClip
extends Feature {
    private final NumberSetting health = new NumberSetting("Health", 20.0f, 1.0f, 20.0f, 0.5f, () -> true);
    private final BooleanSetting autoFreecam = new BooleanSetting("Auto Freecam", true, () -> true);
    private final BooleanSetting autoDisable = new BooleanSetting("Auto Disable", true, () -> true);
    private final BooleanSetting freezeMotions = new BooleanSetting("Freeze Motions", true, () -> true);

    public HurtClip() {
        super("HurtClip", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043d\u0430\u043d\u043e\u0441\u0438\u0442 \u0443\u0440\u043e\u043d, \u0438 \u043b\u0438\u0432\u0430\u0435\u0442 \u043f\u043e\u0434 \u0431\u0435\u0434\u0440\u043e\u043a", Type.Player);
        this.addSettings(this.health, this.freezeMotions, this.autoFreecam, this.autoDisable);
    }

    @Override
    public void onDisable() {
        HurtClip.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }

    public static void init() {
    }

    @WXFuscator
    @EventTarget
    public void onPreMotion(EventPreMotion eventPreMotion) {
        if (HurtClip.mc.player.getHealth() > this.health.getCurrentValue()) {
            return;
        }
        if (!MovementHelper.airBlockAbove2()) {
            this.toggle();
            EventManager.unregister(this);
            NotificationManager.publicity("Hurt Clip", "Please " + (Object)((Object)ChatFormatting.RED) + "destroy " + (Object)((Object)ChatFormatting.WHITE) + "block above " + (Object)((Object)ChatFormatting.GREEN) + "your head!", 5, NotificationType.ERROR);
            ChatHelper.addChatMessage("Please " + (Object)((Object)ChatFormatting.RED) + "destroy " + (Object)((Object)ChatFormatting.WHITE) + "block above " + (Object)((Object)ChatFormatting.GREEN) + "your head!");
            HurtClip.mc.timer.timerSpeed = 1.0f;
            return;
        }
        if (!MovementHelper.isUnderBedrock()) {
            if (HurtClip.mc.player.hurtTime <= 0) {
                int i;
                if (this.freezeMotions.getCurrentValue()) {
                    HurtClip.mc.player.motionX = 0.0;
                    HurtClip.mc.player.motionZ = 0.0;
                    HurtClip.mc.player.setVelocity(0.0, 0.0, 0.0);
                }
                HurtClip.mc.timer.timerSpeed = 0.3f;
                for (i = 0; i < 8; ++i) {
                    HurtClip.mc.player.connection.sendPacket(new CPacketPlayer.Position(HurtClip.mc.player.posX, HurtClip.mc.player.posY + (double)0.399f, HurtClip.mc.player.posZ, false));
                    HurtClip.mc.player.connection.sendPacket(new CPacketPlayer.Position(HurtClip.mc.player.posX, HurtClip.mc.player.posY, HurtClip.mc.player.posZ, false));
                }
                for (i = 0; i < 8; ++i) {
                    HurtClip.mc.player.connection.sendPacket(new CPacketPlayer.Position(HurtClip.mc.player.posX, HurtClip.mc.player.posY, HurtClip.mc.player.posZ, true));
                }
            }
            if (HurtClip.mc.player.hurtTime > 0) {
                HurtClip.mc.player.setPositionAndUpdate(HurtClip.mc.player.posX, -2.0, HurtClip.mc.player.posZ);
            }
        } else {
            if (this.autoFreecam.getCurrentValue()) {
                Celestial.instance.featureManager.getFeatureByClass(FreeCam.class).toggle();
            }
            if (this.autoDisable.getCurrentValue()) {
                this.toggle();
                EventManager.unregister(this);
            }
        }
    }
}

