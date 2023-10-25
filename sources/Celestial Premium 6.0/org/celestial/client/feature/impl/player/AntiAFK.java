/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.math.GCDCalcHelper;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class AntiAFK
extends Feature {
    private final BooleanSetting sendMessage;
    private final NumberSetting sendDelay;
    private final BooleanSetting spin = new BooleanSetting("Spin", true, () -> true);
    public float rot = 0.0f;

    public AntiAFK() {
        super("AntiAFK", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0432\u0441\u0442\u0430\u0442\u044c \u0432 \u0430\u0444\u043a \u0440\u0435\u0436\u0438\u043c, \u0431\u0435\u0437 \u0440\u0438\u0441\u043a\u0430 \u043a\u0438\u043a\u043d\u0443\u0442\u044c\u0441\u044f", Type.Player);
        this.sendMessage = new BooleanSetting("Send Message", true, () -> true);
        this.sendDelay = new NumberSetting("Send Delay", 500.0f, 100.0f, 1000.0f, 100.0f, this.sendMessage::getCurrentValue);
        this.addSettings(this.spin, this.sendMessage, this.sendDelay);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (!MovementHelper.isMoving()) {
            if (this.spin.getCurrentValue()) {
                float yaw = GCDCalcHelper.getFixedRotation((float)(Math.floor(this.spinAim(1.0f)) + (double)MathematicHelper.randomizeFloat(-4.0f, 1.0f)));
                event.setYaw(yaw);
                AntiAFK.mc.player.renderYawOffset = yaw;
                AntiAFK.mc.player.rotationPitchHead = 0.0f;
                AntiAFK.mc.player.rotationYawHead = yaw;
            }
            if (AntiAFK.mc.player.ticksExisted % (int)this.sendDelay.getCurrentValue() == 0 && this.sendMessage.getCurrentValue()) {
                AntiAFK.mc.player.sendChatMessage("/homehome");
            }
        }
    }

    public float spinAim(float rots) {
        this.rot += rots;
        return this.rot;
    }
}

