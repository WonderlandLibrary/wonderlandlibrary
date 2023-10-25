package ru.smertnix.celestial.feature.impl.movement;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;

public class AirJump extends Feature {
    private final ListSetting mode = new ListSetting("Mode", "Matrix", () -> true, "Matrix", "Vanilla");

    public AirJump() {
        super("Air Jump", "Позволяет взбираться на высокие поверхности", FeatureCategory.Movement);
        addSettings(mode);
    }

    @EventTarget
    public void onUpdate(EventPreMotion eventPreMotion) {
    	if (mode.getCurrentMode().equals("Matrix")) {
    		float ex2 = 1f;
            float ex = 1f;
            if ((isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - ex, mc.player.posZ)).getBlock()) ||
                    isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - ex, mc.player.posZ)).getBlock()) ||
                    isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX - ex2, mc.player.posY - ex, mc.player.posZ - ex2)).getBlock()) ||
                    isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX + ex2, mc.player.posY - ex, mc.player.posZ + ex2)).getBlock()) ||
                    isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX - ex2, mc.player.posY - ex, mc.player.posZ + ex2)).getBlock()) ||
                    isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX + ex2, mc.player.posY - ex, mc.player.posZ - ex2)).getBlock()) ||
                    isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX + ex2, mc.player.posY - ex, mc.player.posZ)).getBlock()) ||
                    isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX - ex2, mc.player.posY - ex, mc.player.posZ)).getBlock()) ||
                    isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - ex, mc.player.posZ + ex2)).getBlock()) ||
                    isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - ex, mc.player.posZ - ex2)).getBlock())
            )
                    && mc.player.ticksExisted % 2 == 0) {
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.jumpTicks = 0;
                    mc.player.fallDistance = 0;
                    eventPreMotion.setOnGround(true);
                    mc.player.onGround = true;
                }
            }
    	} else {
    		mc.player.onGround = true;
    	}
    }
    public boolean isBlockValid(Block block) {
        return block != Blocks.AIR && !Arrays.asList(6, 27, 28, 31, 32, 37, 38, 39, 40, 44, 77, 143, 175).contains(Block.getIdFromBlock(block));
    }
}
