package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureComponent;

static class StructureStrongholdPieces.Stones
extends StructureComponent.BlockSelector {
    private StructureStrongholdPieces.Stones() {
    }

    public void selectBlocks(Random rand, int x, int y, int z, boolean p_75062_5_) {
        float f;
        this.blockstate = p_75062_5_ ? ((f = rand.nextFloat()) < 0.2f ? Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CRACKED_META) : (f < 0.5f ? Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.MOSSY_META) : (f < 0.55f ? Blocks.monster_egg.getStateFromMeta(BlockSilverfish.EnumType.STONEBRICK.getMetadata()) : Blocks.stonebrick.getDefaultState()))) : Blocks.air.getDefaultState();
    }
}
