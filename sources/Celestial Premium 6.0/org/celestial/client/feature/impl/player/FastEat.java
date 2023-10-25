/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.ListSetting;

public class FastEat
extends Feature {
    private final ListSetting modeFastEat = new ListSetting("FastEat Mode", "Matrix", () -> true, "Matrix", "Vanilla");

    public FastEat() {
        super("FastEat", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u044b\u0441\u0442\u0440\u043e \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u0435\u0434\u0443", Type.Player);
        this.addSettings(this.modeFastEat);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        String mode = this.modeFastEat.getOptions();
        this.setSuffix(mode);
        if (mode.equalsIgnoreCase("Matrix")) {
            if (FastEat.mc.player.getItemInUseMaxCount() >= 12 && (FastEat.mc.player.isEating() || FastEat.mc.player.isDrinking())) {
                for (int i = 0; i < 10; ++i) {
                    FastEat.mc.player.connection.sendPacket(new CPacketPlayer(FastEat.mc.player.onGround));
                }
                FastEat.mc.player.stopActiveHand();
            }
        } else if (mode.equalsIgnoreCase("Vanilla") && FastEat.mc.player.getItemInUseMaxCount() == 16 && (FastEat.mc.player.isEating() || FastEat.mc.player.isDrinking())) {
            for (int i = 0; i < 21; ++i) {
                FastEat.mc.player.connection.sendPacket(new CPacketPlayer(true));
            }
            FastEat.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }

    @Override
    public void onDisable() {
        FastEat.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}

