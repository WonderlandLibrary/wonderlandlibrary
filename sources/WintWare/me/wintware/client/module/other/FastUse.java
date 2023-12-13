/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.other;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class FastUse
extends Module {
    public FastUse() {
        super("FastUse", Category.Player);
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        if (Minecraft.player.getItemInUseCount() == 2) {
            for (int i = 0; i < 30; ++i) {
                Minecraft.player.connection.sendPacket(new CPacketPlayer(true));
            }
            mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            Minecraft.player.stopActiveHand();
        }
    }
}

