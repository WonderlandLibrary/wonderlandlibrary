/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  ru.wendoxd.wclassguard.WXFuscator
 */
package org.celestial.client.feature.impl.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;
import ru.wendoxd.wclassguard.WXFuscator;

public class SelfDamage
extends Feature {
    private final BooleanSetting freezeMotions = new BooleanSetting("Freeze Motions", false, () -> true);
    private final BooleanSetting disableOnDamage = new BooleanSetting("Disable on damage", true, () -> true);

    public SelfDamage() {
        super("SelfDamage", "\u041d\u0430\u043d\u043e\u0441\u0438\u0442 \u0441\u0430\u043c \u0441\u0435\u0431\u0435 \u0443\u0440\u043e\u043d", Type.Player);
        this.addSettings(this.freezeMotions, this.disableOnDamage);
    }

    @Override
    public void onDisable() {
        SelfDamage.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }

    public static void init() {
    }

    @WXFuscator
    @EventTarget
    public void onPreMotion(EventPreMotion eventPreMotion) {
        if (this.disableOnDamage.getCurrentValue() && SelfDamage.mc.player.hurtTime > 0) {
            this.toggle();
            EventManager.unregister(this);
        }
        if (this.getState()) {
            int i;
            if (this.freezeMotions.getCurrentValue()) {
                SelfDamage.mc.player.motionX = 0.0;
                SelfDamage.mc.player.motionZ = 0.0;
                SelfDamage.mc.player.setVelocity(0.0, 0.0, 0.0);
            }
            SelfDamage.mc.timer.timerSpeed = 0.3f;
            for (i = 0; i < 8; ++i) {
                SelfDamage.mc.player.connection.sendPacket(new CPacketPlayer.Position(SelfDamage.mc.player.posX, SelfDamage.mc.player.posY + (double)0.399f, SelfDamage.mc.player.posZ, false));
                SelfDamage.mc.player.connection.sendPacket(new CPacketPlayer.Position(SelfDamage.mc.player.posX, SelfDamage.mc.player.posY, SelfDamage.mc.player.posZ, false));
            }
            for (i = 0; i < 8; ++i) {
                SelfDamage.mc.player.connection.sendPacket(new CPacketPlayer.Position(SelfDamage.mc.player.posX, SelfDamage.mc.player.posY, SelfDamage.mc.player.posZ, true));
            }
        } else {
            SelfDamage.mc.timer.timerSpeed = 1.0f;
        }
    }
}

