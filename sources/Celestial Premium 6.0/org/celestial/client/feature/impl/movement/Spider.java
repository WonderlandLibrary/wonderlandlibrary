/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import net.minecraft.network.play.client.CPacketEntityAction;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class Spider
extends Feature {
    public static NumberSetting climbTicks;
    private final TimerHelper timerHelper = new TimerHelper();
    private final TimerHelper sendTimer = new TimerHelper();
    private final BooleanSetting shiftCheck;

    public Spider() {
        super("Spider", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0432\u0437\u0431\u0435\u0440\u0430\u0435\u0442\u0441\u044f \u043d\u0430 \u0441\u0442\u0435\u043d\u044b", Type.Movement);
        climbTicks = new NumberSetting("Climb Ticks", 2.0f, 0.0f, 5.0f, 0.1f, () -> true);
        this.shiftCheck = new BooleanSetting("Shift Check", false, () -> true);
        this.addSettings(this.shiftCheck, climbTicks);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        this.setSuffix("" + climbTicks.getCurrentValue());
        if (this.shiftCheck.getCurrentValue() && !Spider.mc.gameSettings.keyBindSneak.isKeyDown()) {
            return;
        }
        if (this.sendTimer.hasReached(350.0)) {
            Spider.mc.player.connection.sendPacket(new CPacketEntityAction(Spider.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            this.sendTimer.reset();
        }
        if (MovementHelper.isMoving() && Spider.mc.player.isCollidedHorizontally && this.timerHelper.hasReached(climbTicks.getCurrentValue() * 100.0f)) {
            event.setOnGround(true);
            Spider.mc.player.jump();
            Spider.mc.player.motionY += (double)0.01f;
            this.timerHelper.reset();
        }
    }
}

