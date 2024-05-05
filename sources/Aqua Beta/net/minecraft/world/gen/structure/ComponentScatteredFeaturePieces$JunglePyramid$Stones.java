package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureComponent;

static class ComponentScatteredFeaturePieces.JunglePyramid.Stones
extends StructureComponent.BlockSelector {
    private ComponentScatteredFeaturePieces.JunglePyramid.Stones() {
    }

    public void selectBlocks(Random rand, int x, int y, int z, boolean p_75062_5_) {
        this.blockstate = rand.nextFloat() < 0.4f ? Blocks.cobblestone.getDefaultState() : Blocks.mossy_cobblestone.getDefaultState();
    }
}
