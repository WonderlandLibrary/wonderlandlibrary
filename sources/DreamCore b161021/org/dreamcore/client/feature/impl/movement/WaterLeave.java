package org.dreamcore.client.feature.impl.movement;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.NumberSetting;

public class WaterLeave extends Feature {

    private final NumberSetting leaveMotion;

    public WaterLeave() {
        super("WaterLeave", "Игрок высоко прыгает при погружении в воду", Type.Movement);
        leaveMotion = new NumberSetting("Leave Motion", 10, 0.5F, 20, 0.5F, () -> true);
        addSettings(leaveMotion);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {

        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.95, mc.player.posZ)).getBlock() == Blocks.WATER) {
            mc.player.motionY = leaveMotion.getNumberValue();
            mc.player.onGround = true;
            mc.player.isAirBorne = true;
        }
        if (mc.player.isInWater() || mc.player.isInLava()) {
            mc.player.onGround = true;
        }
        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.0000001, mc.player.posZ)).getBlock() == Blocks.WATER) {
            mc.player.motionY = 0.06f;
        }
    }
}
