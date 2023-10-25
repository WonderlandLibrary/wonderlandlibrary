/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.world.BlockHelper;
import org.celestial.client.settings.impl.NumberSetting;

public class WaterLeave
extends Feature {
    private final NumberSetting leaveMotion = new NumberSetting("Leave Motion", 10.0f, 0.5f, 10.0f, 0.1f);

    public WaterLeave() {
        super("WaterLeave", "\u0418\u0433\u0440\u043e\u043a \u0432\u044b\u0441\u043e\u043a\u043e \u043f\u0440\u044b\u0433\u0430\u0435\u0442 \u043f\u0440\u0438 \u043f\u043e\u0433\u0440\u0443\u0436\u0435\u043d\u0438\u0438 \u0432 \u0432\u043e\u0434\u0443", Type.Movement);
        this.addSettings(this.leaveMotion);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + this.leaveMotion.getCurrentValue());
        if (WaterLeave.mc.world.getBlockState(new BlockPos(WaterLeave.mc.player.posX, WaterLeave.mc.player.posY - 0.1, WaterLeave.mc.player.posZ)).getBlock() instanceof BlockLiquid || WaterLeave.mc.world.getBlockState(new BlockPos(WaterLeave.mc.player.posX, WaterLeave.mc.player.posY + 0.1, WaterLeave.mc.player.posZ)).getBlock() instanceof BlockLiquid) {
            if (BlockHelper.collideBlock(WaterLeave.mc.player.getEntityBoundingBox(), 0.0f, Block::isLiquid) && WaterLeave.mc.player.isInsideOfMaterial(Material.AIR)) {
                WaterLeave.mc.player.motionY = 0.08;
            }
            if (!WaterLeave.mc.player.isInLiquid() && WaterLeave.mc.player.fallDistance > 0.0f && WaterLeave.mc.player.motionY < 0.08) {
                WaterLeave.mc.player.motionY += (double)this.leaveMotion.getCurrentValue();
            }
        }
    }
}

