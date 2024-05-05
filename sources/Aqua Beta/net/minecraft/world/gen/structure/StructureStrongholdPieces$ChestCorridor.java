package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;

/*
 * Exception performing whole class analysis ignored.
 */
public static class StructureStrongholdPieces.ChestCorridor
extends StructureStrongholdPieces.Stronghold {
    private static final List<WeightedRandomChestContent> strongholdChestContents = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.ender_pearl, 0, 1, 1, 10), new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5), new WeightedRandomChestContent((Item)Items.iron_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent((Item)Items.iron_helmet, 0, 1, 1, 5), new WeightedRandomChestContent((Item)Items.iron_leggings, 0, 1, 1, 5), new WeightedRandomChestContent((Item)Items.iron_boots, 0, 1, 1, 5), new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 1), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)});
    private boolean hasMadeChest;

    public StructureStrongholdPieces.ChestCorridor() {
    }

    public StructureStrongholdPieces.ChestCorridor(int p_i45582_1_, Random p_i45582_2_, StructureBoundingBox p_i45582_3_, EnumFacing p_i45582_4_) {
        super(p_i45582_1_);
        this.coordBaseMode = p_i45582_4_;
        this.field_143013_d = this.getRandomDoor(p_i45582_2_);
        this.boundingBox = p_i45582_3_;
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setBoolean("Chest", this.hasMadeChest);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.hasMadeChest = tagCompound.getBoolean("Chest");
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
    }

    public static StructureStrongholdPieces.ChestCorridor func_175868_a(List<StructureComponent> p_175868_0_, Random p_175868_1_, int p_175868_2_, int p_175868_3_, int p_175868_4_, EnumFacing p_175868_5_, int p_175868_6_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175868_2_, (int)p_175868_3_, (int)p_175868_4_, (int)-1, (int)-1, (int)0, (int)5, (int)5, (int)7, (EnumFacing)p_175868_5_);
        return StructureStrongholdPieces.ChestCorridor.canStrongholdGoDeeper((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175868_0_, (StructureBoundingBox)structureboundingbox) == null ? new StructureStrongholdPieces.ChestCorridor(p_175868_6_, p_175868_1_, structureboundingbox, p_175868_5_) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
            return false;
        }
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 4, 6, true, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 1, 0);
        this.placeDoor(worldIn, randomIn, structureBoundingBoxIn, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 2, 3, 1, 4, Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 4, structureBoundingBoxIn);
        for (int i = 2; i <= 4; ++i) {
            this.setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 2, 1, i, structureBoundingBoxIn);
        }
        if (!this.hasMadeChest && structureBoundingBoxIn.isVecInside((Vec3i)new BlockPos(this.getXWithOffset(3, 3), this.getYWithOffset(2), this.getZWithOffset(3, 3)))) {
            this.hasMadeChest = true;
            this.generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 3, 2, 3, WeightedRandomChestContent.func_177629_a(strongholdChestContents, (WeightedRandomChestContent[])new WeightedRandomChestContent[]{Items.enchanted_book.getRandom(randomIn)}), 2 + randomIn.nextInt(2));
        }
        return true;
    }
}
