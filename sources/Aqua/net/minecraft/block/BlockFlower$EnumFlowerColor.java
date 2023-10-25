package net.minecraft.block;

import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;

public static enum BlockFlower.EnumFlowerColor {
    YELLOW,
    RED;


    public BlockFlower getBlock() {
        return this == YELLOW ? Blocks.yellow_flower : Blocks.red_flower;
    }
}
