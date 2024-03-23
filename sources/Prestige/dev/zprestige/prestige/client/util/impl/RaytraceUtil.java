package dev.zprestige.prestige.client.util.impl;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class RaytraceUtil {

    public static MinecraftClient mc = MinecraftClient.getInstance();

    public static boolean isBlockAtPosition(BlockPos blockPos, Block block) {
        return mc.world.getBlockState(blockPos).getBlock() == block;
    }
}