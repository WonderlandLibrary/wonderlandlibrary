/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import net.minecraft.network.play.client.CPacketEntityAction;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class HighJump
extends Feature {
    private final ListSetting mode;
    private final NumberSetting motionValue;
    private final TimerHelper sendTimer = new TimerHelper();

    public HighJump() {
        super("HighJump", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043f\u0440\u044b\u0433\u0430\u0442\u044c \u043d\u0430 \u0431\u043e\u043b\u044c\u0448\u0443\u044e \u0432\u044b\u0441\u043e\u0442\u0443", Type.Movement);
        this.mode = new ListSetting("LongJump Mode", "Matrix", () -> true, "Matrix", "Vanilla");
        this.motionValue = new NumberSetting("Motion Value", 5.0f, 0.1f, 10.0f, 0.5f, () -> true);
        this.addSettings(this.mode, this.motionValue);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(this.mode.currentMode);
        if (this.mode.currentMode.equals("Matrix")) {
            if (this.sendTimer.hasReached(350.0)) {
                HighJump.mc.player.connection.sendPacket(new CPacketEntityAction(HighJump.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                this.sendTimer.reset();
            }
            HighJump.mc.player.motionY += (double)0.15f;
            HighJump.mc.player.motionY = this.motionValue.getCurrentValue();
        } else if (this.mode.currentMode.equals("Vanilla")) {
            HighJump.mc.player.motionY = this.motionValue.getCurrentValue();
        }
    }
}

