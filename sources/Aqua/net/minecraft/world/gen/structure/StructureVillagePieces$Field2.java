package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public static class StructureVillagePieces.Field2
extends StructureVillagePieces.Village {
    private Block cropTypeA;
    private Block cropTypeB;

    public StructureVillagePieces.Field2() {
    }

    public StructureVillagePieces.Field2(StructureVillagePieces.Start start, int p_i45569_2_, Random rand, StructureBoundingBox p_i45569_4_, EnumFacing facing) {
        super(start, p_i45569_2_);
        this.coordBaseMode = facing;
        this.boundingBox = p_i45569_4_;
        this.cropTypeA = this.func_151560_a(rand);
        this.cropTypeB = this.func_151560_a(rand);
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setInteger("CA", Block.blockRegistry.getIDForObject((Object)this.cropTypeA));
        tagCompound.setInteger("CB", Block.blockRegistry.getIDForObject((Object)this.cropTypeB));
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.cropTypeA = Block.getBlockById((int)tagCompound.getInteger("CA"));
        this.cropTypeB = Block.getBlockById((int)tagCompound.getInteger("CB"));
    }

    private Block func_151560_a(Random rand) {
        switch (rand.nextInt(5)) {
            case 0: {
                return Blocks.carrots;
            }
            case 1: {
                return Blocks.potatoes;
            }
        }
        return Blocks.wheat;
    }

    public static StructureVillagePieces.Field2 func_175852_a(StructureVillagePieces.Start start, List<StructureComponent> p_175852_1_, Random rand, int p_175852_3_, int p_175852_4_, int p_175852_5_, EnumFacing facing, int p_175852_7_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175852_3_, (int)p_175852_4_, (int)p_175852_5_, (int)0, (int)0, (int)0, (int)7, (int)4, (int)9, (EnumFacing)facing);
        return StructureVillagePieces.Field2.canVillageGoDeeper((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175852_1_, (StructureBoundingBox)structureboundingbox) == null ? new StructureVillagePieces.Field2(start, p_175852_7_, rand, structureboundingbox, facing) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
            if (this.field_143015_k < 0) {
                return true;
            }
            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 6, 4, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 1, 5, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 0, 6, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 5, 0, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 8, 5, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 1, 3, 0, 7, Blocks.water.getDefaultState(), Blocks.water.getDefaultState(), false);
        for (int i = 1; i <= 7; ++i) {
            this.setBlockState(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange((Random)randomIn, (int)2, (int)7)), 1, 1, i, structureBoundingBoxIn);
            this.setBlockState(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange((Random)randomIn, (int)2, (int)7)), 2, 1, i, structureBoundingBoxIn);
            this.setBlockState(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange((Random)randomIn, (int)2, (int)7)), 4, 1, i, structureBoundingBoxIn);
            this.setBlockState(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange((Random)randomIn, (int)2, (int)7)), 5, 1, i, structureBoundingBoxIn);
        }
        for (int k = 0; k < 9; ++k) {
            for (int j = 0; j < 7; ++j) {
                this.clearCurrentPositionBlocksUpwards(worldIn, j, 4, k, structureBoundingBoxIn);
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.dirt.getDefaultState(), j, -1, k, structureBoundingBoxIn);
            }
        }
        return true;
    }
}
