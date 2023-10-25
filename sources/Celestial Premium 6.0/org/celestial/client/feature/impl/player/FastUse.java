/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class FastUse
extends Feature {
    private final TimerHelper timerHelper = new TimerHelper();
    private final ListSetting modeFastEat = new ListSetting("FastEat Mode", "Stop", () -> true, "Stop", "NCP", "Custom");
    private final NumberSetting customDelay = new NumberSetting("Custom Delay", 50.0f, 0.0f, 500.0f, 10.0f, () -> this.modeFastEat.currentMode.equals("Custom"));

    public FastUse() {
        super("FastUse", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u044b\u0441\u0442\u0440\u043e \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u0435\u0434\u0443", Type.Player);
        this.addSettings(this.modeFastEat, this.customDelay);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = this.modeFastEat.getOptions();
        this.setSuffix(mode);
        if (mode.equalsIgnoreCase("Stop")) {
            if (FastUse.mc.player.getItemInUseDuration() >= 12 && (FastUse.mc.player.isEating() || FastUse.mc.player.isDrinking())) {
                for (int i = 0; i < 10; ++i) {
                    FastUse.mc.player.connection.sendPacket(new CPacketPlayer(FastUse.mc.player.onGround));
                }
                FastUse.mc.player.stopActiveHand();
            }
        } else if (mode.equalsIgnoreCase("NCP")) {
            if (FastUse.mc.player.getItemInUseDuration() == 16 && (FastUse.mc.player.isEating() || FastUse.mc.player.isDrinking())) {
                for (int i = 0; i < 21; ++i) {
                    FastUse.mc.player.connection.sendPacket(new CPacketPlayer(true));
                }
                FastUse.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        } else if (mode.equalsIgnoreCase("Custom") && (FastUse.mc.player.isEating() || FastUse.mc.player.isDrinking()) && this.timerHelper.hasReached(this.customDelay.getCurrentValue())) {
            FastUse.mc.player.connection.sendPacket(new CPacketPlayer(FastUse.mc.player.onGround));
            this.timerHelper.reset();
        }
    }

    @Override
    public void onDisable() {
        FastUse.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}

