package com.canon.majik.api.utils.autocrystal;

import com.canon.majik.api.utils.player.BlockUtil;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class AutoCrystalThread  {

    public static ArrayList<BlockPos> start(float radius, boolean onePointThirteen) {
        ArrayList<BlockPos> blocks = new ArrayList<>();
        for (BlockPos pos : BlockUtil.getBlocksInRadius(radius)){
            if (!BlockUtil.valid(pos, onePointThirteen)) {
                continue;
            }
            if (!BlockUtil.empty(pos)) {
                continue;
            }
            blocks.add(pos);
        }
        return blocks;
    }
}
