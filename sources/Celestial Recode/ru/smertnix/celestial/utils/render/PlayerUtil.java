package ru.smertnix.celestial.utils.render;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import ru.smertnix.celestial.utils.Helper;

public class PlayerUtil implements Helper {
    public static Block getBlock(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.world.getBlockState(new BlockPos(offsetX, offsetY, offsetZ)).getBlock();
    }


}
