package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;

/*
 * Exception performing whole class analysis ignored.
 */
public static class StructureStrongholdPieces.RoomCrossing
extends StructureStrongholdPieces.Stronghold {
    private static final List<WeightedRandomChestContent> strongholdRoomCrossingChestContents = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1)});
    protected int roomType;

    public StructureStrongholdPieces.RoomCrossing() {
    }

    public StructureStrongholdPieces.RoomCrossing(int p_i45575_1_, Random p_i45575_2_, StructureBoundingBox p_i45575_3_, EnumFacing p_i45575_4_) {
        super(p_i45575_1_);
        this.coordBaseMode = p_i45575_4_;
        this.field_143013_d = this.getRandomDoor(p_i45575_2_);
        this.boundingBox = p_i45575_3_;
        this.roomType = p_i45575_2_.nextInt(5);
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setInteger("Type", this.roomType);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.roomType = tagCompound.getInteger("Type");
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 4, 1);
        this.getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 4);
        this.getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 4);
    }

    public static StructureStrongholdPieces.RoomCrossing func_175859_a(List<StructureComponent> p_175859_0_, Random p_175859_1_, int p_175859_2_, int p_175859_3_, int p_175859_4_, EnumFacing p_175859_5_, int p_175859_6_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175859_2_, (int)p_175859_3_, (int)p_175859_4_, (int)-4, (int)-1, (int)0, (int)11, (int)7, (int)11, (EnumFacing)p_175859_5_);
        return StructureStrongholdPieces.RoomCrossing.canStrongholdGoDeeper((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175859_0_, (StructureBoundingBox)structureboundingbox) == null ? new StructureStrongholdPieces.RoomCrossing(p_175859_6_, p_175859_1_, structureboundingbox, p_175859_5_) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
            return false;
        }
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 10, 6, 10, true, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 4, 1, 0);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 10, 6, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 4, 0, 3, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 4, 10, 3, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        switch (this.roomType) {
            case 0: {
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 1, 5, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 2, 5, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 3, 5, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.torch.getDefaultState(), 4, 3, 5, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.torch.getDefaultState(), 6, 3, 5, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.torch.getDefaultState(), 5, 3, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.torch.getDefaultState(), 5, 3, 6, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 4, 1, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 4, 1, 5, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 4, 1, 6, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 6, 1, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 6, 1, 5, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 6, 1, 6, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 5, 1, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 5, 1, 6, structureBoundingBoxIn);
                break;
            }
            case 1: {
                for (int i1 = 0; i1 < 5; ++i1) {
                    this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 1, 3 + i1, structureBoundingBoxIn);
                    this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 7, 1, 3 + i1, structureBoundingBoxIn);
                    this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3 + i1, 1, 3, structureBoundingBoxIn);
                    this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3 + i1, 1, 7, structureBoundingBoxIn);
                }
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 1, 5, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 2, 5, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 3, 5, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.flowing_water.getDefaultState(), 5, 4, 5, structureBoundingBoxIn);
                break;
            }
            case 2: {
                for (int i = 1; i <= 9; ++i) {
                    this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 1, 3, i, structureBoundingBoxIn);
                    this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 9, 3, i, structureBoundingBoxIn);
                }
                for (int j = 1; j <= 9; ++j) {
                    this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), j, 3, 1, structureBoundingBoxIn);
                    this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), j, 3, 9, structureBoundingBoxIn);
                }
                this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 5, 1, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 5, 1, 6, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 5, 3, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 5, 3, 6, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 1, 5, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 6, 1, 5, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 3, 5, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 6, 3, 5, structureBoundingBoxIn);
                for (int k = 1; k <= 3; ++k) {
                    this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, k, 4, structureBoundingBoxIn);
                    this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 6, k, 4, structureBoundingBoxIn);
                    this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, k, 6, structureBoundingBoxIn);
                    this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 6, k, 6, structureBoundingBoxIn);
                }
                this.setBlockState(worldIn, Blocks.torch.getDefaultState(), 5, 3, 5, structureBoundingBoxIn);
                for (int l = 2; l <= 8; ++l) {
                    this.setBlockState(worldIn, Blocks.planks.getDefaultState(), 2, 3, l, structureBoundingBoxIn);
                    this.setBlockState(worldIn, Blocks.planks.getDefaultState(), 3, 3, l, structureBoundingBoxIn);
                    if (l <= 3 || l >= 7) {
                        this.setBlockState(worldIn, Blocks.planks.getDefaultState(), 4, 3, l, structureBoundingBoxIn);
                        this.setBlockState(worldIn, Blocks.planks.getDefaultState(), 5, 3, l, structureBoundingBoxIn);
                        this.setBlockState(worldIn, Blocks.planks.getDefaultState(), 6, 3, l, structureBoundingBoxIn);
                    }
                    this.setBlockState(worldIn, Blocks.planks.getDefaultState(), 7, 3, l, structureBoundingBoxIn);
                    this.setBlockState(worldIn, Blocks.planks.getDefaultState(), 8, 3, l, structureBoundingBoxIn);
                }
                this.setBlockState(worldIn, Blocks.ladder.getStateFromMeta(this.getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 1, 3, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.ladder.getStateFromMeta(this.getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 2, 3, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.ladder.getStateFromMeta(this.getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 3, 3, structureBoundingBoxIn);
                this.generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 3, 4, 8, WeightedRandomChestContent.func_177629_a(strongholdRoomCrossingChestContents, (WeightedRandomChestContent[])new WeightedRandomChestContent[]{Items.enchanted_book.getRandom(randomIn)}), 1 + randomIn.nextInt(4));
            }
        }
        return true;
    }
}
