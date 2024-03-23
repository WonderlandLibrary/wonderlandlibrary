package dev.zprestige.prestige.client.module.impl.movement;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;

public class FastBridge extends Module {

    public boolean bridging;

    public FastBridge() {
        super("Fast Bridge", Category.Movement, "Automatically sneaks on block edges");
    }

    @EventListener
    public void event(TickEvent event) {
        if (getMc().player.getMainHandStack().getItem() instanceof BlockItem || getMc().player.getOffHandStack().getItem() instanceof BlockItem) {
            if (getMc().player.getPitch() < 70) {
                if (bridging) {
                    getMc().options.sneakKey.setPressed(false);
                    bridging = false;
                }
            }
            BlockPos blockPos = BlockPos.ofFloored(getMc().player.getPos()).down();
            ClientWorld clientWorld = getMc().world;
            if (clientWorld.getBlockState(blockPos).isReplaceable() && clientWorld.getBlockState(blockPos.down()).isReplaceable() && clientWorld.getBlockState(blockPos.down().down()).isReplaceable()) {
                getMc().options.sneakKey.setPressed(true);
                bridging = true;
            } else {
                if (bridging) {
                    getMc().options.sneakKey.setPressed(false);
                    bridging = false;
                }
            }
        }
    }
}
