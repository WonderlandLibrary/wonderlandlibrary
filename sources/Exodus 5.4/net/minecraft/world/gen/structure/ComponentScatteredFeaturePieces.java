/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentScatteredFeaturePieces {
    public static void registerScatteredFeaturePieces() {
        MapGenStructureIO.registerStructureComponent(DesertPyramid.class, "TeDP");
        MapGenStructureIO.registerStructureComponent(JunglePyramid.class, "TeJP");
        MapGenStructureIO.registerStructureComponent(SwampHut.class, "TeSH");
    }

    static abstract class Feature
    extends StructureComponent {
        protected int scatteredFeatureSizeY;
        protected int scatteredFeatureSizeX;
        protected int field_74936_d = -1;
        protected int scatteredFeatureSizeZ;

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            nBTTagCompound.setInteger("Width", this.scatteredFeatureSizeX);
            nBTTagCompound.setInteger("Height", this.scatteredFeatureSizeY);
            nBTTagCompound.setInteger("Depth", this.scatteredFeatureSizeZ);
            nBTTagCompound.setInteger("HPos", this.field_74936_d);
        }

        public Feature() {
        }

        protected Feature(Random random, int n, int n2, int n3, int n4, int n5, int n6) {
            super(0);
            this.scatteredFeatureSizeX = n4;
            this.scatteredFeatureSizeY = n5;
            this.scatteredFeatureSizeZ = n6;
            this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(random);
            switch (this.coordBaseMode) {
                case NORTH: 
                case SOUTH: {
                    this.boundingBox = new StructureBoundingBox(n, n2, n3, n + n4 - 1, n2 + n5 - 1, n3 + n6 - 1);
                    break;
                }
                default: {
                    this.boundingBox = new StructureBoundingBox(n, n2, n3, n + n6 - 1, n2 + n5 - 1, n3 + n4 - 1);
                }
            }
        }

        protected boolean func_74935_a(World world, StructureBoundingBox structureBoundingBox, int n) {
            if (this.field_74936_d >= 0) {
                return true;
            }
            int n2 = 0;
            int n3 = 0;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            int n4 = this.boundingBox.minZ;
            while (n4 <= this.boundingBox.maxZ) {
                int n5 = this.boundingBox.minX;
                while (n5 <= this.boundingBox.maxX) {
                    mutableBlockPos.func_181079_c(n5, 64, n4);
                    if (structureBoundingBox.isVecInside(mutableBlockPos)) {
                        n2 += Math.max(world.getTopSolidOrLiquidBlock(mutableBlockPos).getY(), world.provider.getAverageGroundLevel());
                        ++n3;
                    }
                    ++n5;
                }
                ++n4;
            }
            if (n3 == 0) {
                return false;
            }
            this.field_74936_d = n2 / n3;
            this.boundingBox.offset(0, this.field_74936_d - this.boundingBox.minY + n, 0);
            return true;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            this.scatteredFeatureSizeX = nBTTagCompound.getInteger("Width");
            this.scatteredFeatureSizeY = nBTTagCompound.getInteger("Height");
            this.scatteredFeatureSizeZ = nBTTagCompound.getInteger("Depth");
            this.field_74936_d = nBTTagCompound.getInteger("HPos");
        }
    }

    public static class JunglePyramid
    extends Feature {
        private boolean field_74945_j;
        private static final List<WeightedRandomChestContent> field_175816_i = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15), new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2), new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20), new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)});
        private static final List<WeightedRandomChestContent> field_175815_j = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.arrow, 0, 2, 7, 30)});
        private boolean field_74946_k;
        private boolean field_74947_h;
        private static Stones junglePyramidsRandomScatteredStones = new Stones();
        private boolean field_74948_i;

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.field_74947_h = nBTTagCompound.getBoolean("placedMainChest");
            this.field_74948_i = nBTTagCompound.getBoolean("placedHiddenChest");
            this.field_74945_j = nBTTagCompound.getBoolean("placedTrap1");
            this.field_74946_k = nBTTagCompound.getBoolean("placedTrap2");
        }

        public JunglePyramid(Random random, int n, int n2) {
            super(random, n, 64, n2, 12, 10, 15);
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setBoolean("placedMainChest", this.field_74947_h);
            nBTTagCompound.setBoolean("placedHiddenChest", this.field_74948_i);
            nBTTagCompound.setBoolean("placedTrap1", this.field_74945_j);
            nBTTagCompound.setBoolean("placedTrap2", this.field_74946_k);
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (!this.func_74935_a(world, structureBoundingBox, 0)) {
                return false;
            }
            int n = this.getMetadataWithOffset(Blocks.stone_stairs, 3);
            int n2 = this.getMetadataWithOffset(Blocks.stone_stairs, 2);
            int n3 = this.getMetadataWithOffset(Blocks.stone_stairs, 0);
            int n4 = this.getMetadataWithOffset(Blocks.stone_stairs, 1);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 0, -4, 0, this.scatteredFeatureSizeX - 1, 0, this.scatteredFeatureSizeZ - 1, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 2, 1, 2, 9, 2, 2, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 2, 1, 12, 9, 2, 12, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 2, 1, 3, 2, 2, 11, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 9, 1, 3, 9, 2, 11, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 1, 3, 1, 10, 6, 1, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 1, 3, 13, 10, 6, 13, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 1, 3, 2, 1, 6, 12, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 10, 3, 2, 10, 6, 12, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 2, 3, 2, 9, 3, 12, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 2, 6, 2, 9, 6, 12, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 3, 7, 3, 8, 7, 11, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 4, 8, 4, 7, 8, 10, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithAir(world, structureBoundingBox, 3, 1, 3, 8, 2, 11);
            this.fillWithAir(world, structureBoundingBox, 4, 3, 6, 7, 3, 9);
            this.fillWithAir(world, structureBoundingBox, 2, 4, 2, 9, 5, 12);
            this.fillWithAir(world, structureBoundingBox, 4, 6, 5, 7, 6, 9);
            this.fillWithAir(world, structureBoundingBox, 5, 7, 6, 6, 7, 8);
            this.fillWithAir(world, structureBoundingBox, 5, 1, 2, 6, 2, 2);
            this.fillWithAir(world, structureBoundingBox, 5, 2, 12, 6, 2, 12);
            this.fillWithAir(world, structureBoundingBox, 5, 5, 1, 6, 5, 1);
            this.fillWithAir(world, structureBoundingBox, 5, 5, 13, 6, 5, 13);
            this.setBlockState(world, Blocks.air.getDefaultState(), 1, 5, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 10, 5, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 1, 5, 9, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 10, 5, 9, structureBoundingBox);
            int n5 = 0;
            while (n5 <= 14) {
                this.fillWithRandomizedBlocks(world, structureBoundingBox, 2, 4, n5, 2, 5, n5, false, random, junglePyramidsRandomScatteredStones);
                this.fillWithRandomizedBlocks(world, structureBoundingBox, 4, 4, n5, 4, 5, n5, false, random, junglePyramidsRandomScatteredStones);
                this.fillWithRandomizedBlocks(world, structureBoundingBox, 7, 4, n5, 7, 5, n5, false, random, junglePyramidsRandomScatteredStones);
                this.fillWithRandomizedBlocks(world, structureBoundingBox, 9, 4, n5, 9, 5, n5, false, random, junglePyramidsRandomScatteredStones);
                n5 += 14;
            }
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 5, 6, 0, 6, 6, 0, false, random, junglePyramidsRandomScatteredStones);
            n5 = 0;
            while (n5 <= 11) {
                int n6 = 2;
                while (n6 <= 12) {
                    this.fillWithRandomizedBlocks(world, structureBoundingBox, n5, 4, n6, n5, 5, n6, false, random, junglePyramidsRandomScatteredStones);
                    n6 += 2;
                }
                this.fillWithRandomizedBlocks(world, structureBoundingBox, n5, 6, 5, n5, 6, 5, false, random, junglePyramidsRandomScatteredStones);
                this.fillWithRandomizedBlocks(world, structureBoundingBox, n5, 6, 9, n5, 6, 9, false, random, junglePyramidsRandomScatteredStones);
                n5 += 11;
            }
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 2, 7, 2, 2, 9, 2, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 9, 7, 2, 9, 9, 2, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 2, 7, 12, 2, 9, 12, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 9, 7, 12, 9, 9, 12, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 4, 9, 4, 4, 9, 4, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 7, 9, 4, 7, 9, 4, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 4, 9, 10, 4, 9, 10, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 7, 9, 10, 7, 9, 10, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 5, 9, 7, 6, 9, 7, false, random, junglePyramidsRandomScatteredStones);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n), 5, 9, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n), 6, 9, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n2), 5, 9, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n2), 6, 9, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n), 4, 0, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n), 5, 0, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n), 6, 0, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n), 7, 0, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n), 4, 1, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n), 4, 2, 9, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n), 4, 3, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n), 7, 1, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n), 7, 2, 9, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n), 7, 3, 10, structureBoundingBox);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 4, 1, 9, 4, 1, 9, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 7, 1, 9, 7, 1, 9, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 4, 1, 10, 7, 2, 10, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 5, 4, 5, 6, 4, 5, false, random, junglePyramidsRandomScatteredStones);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n3), 4, 4, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n4), 7, 4, 5, structureBoundingBox);
            n5 = 0;
            while (n5 < 4) {
                this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n2), 5, 0 - n5, 6 + n5, structureBoundingBox);
                this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n2), 6, 0 - n5, 6 + n5, structureBoundingBox);
                this.fillWithAir(world, structureBoundingBox, 5, 0 - n5, 7 + n5, 6, 0 - n5, 9 + n5);
                ++n5;
            }
            this.fillWithAir(world, structureBoundingBox, 1, -3, 12, 10, -1, 13);
            this.fillWithAir(world, structureBoundingBox, 1, -3, 1, 3, -1, 13);
            this.fillWithAir(world, structureBoundingBox, 1, -3, 1, 9, -1, 5);
            n5 = 1;
            while (n5 <= 13) {
                this.fillWithRandomizedBlocks(world, structureBoundingBox, 1, -3, n5, 1, -2, n5, false, random, junglePyramidsRandomScatteredStones);
                n5 += 2;
            }
            n5 = 2;
            while (n5 <= 12) {
                this.fillWithRandomizedBlocks(world, structureBoundingBox, 1, -1, n5, 3, -1, n5, false, random, junglePyramidsRandomScatteredStones);
                n5 += 2;
            }
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 2, -2, 1, 5, -2, 1, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 7, -2, 1, 9, -2, 1, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 6, -3, 1, 6, -3, 1, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 6, -1, 1, 6, -1, 1, false, random, junglePyramidsRandomScatteredStones);
            this.setBlockState(world, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.EAST.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, true), 1, -3, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.WEST.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, true), 4, -3, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, true), 2, -3, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, true), 3, -3, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 5, -3, 7, structureBoundingBox);
            this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 5, -3, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 5, -3, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 5, -3, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 5, -3, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 5, -3, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 5, -3, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 4, -3, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 3, -3, 1, structureBoundingBox);
            if (!this.field_74945_j) {
                this.field_74945_j = this.generateDispenserContents(world, structureBoundingBox, random, 3, -2, 1, EnumFacing.NORTH.getIndex(), field_175815_j, 2);
            }
            this.setBlockState(world, Blocks.vine.getStateFromMeta(15), 3, -2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.NORTH.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, true), 7, -3, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.SOUTH.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, true), 7, -3, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, true), 7, -3, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, true), 7, -3, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, true), 7, -3, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 8, -3, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 9, -3, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 9, -3, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 9, -3, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 9, -2, 4, structureBoundingBox);
            if (!this.field_74946_k) {
                this.field_74946_k = this.generateDispenserContents(world, structureBoundingBox, random, 9, -2, 3, EnumFacing.WEST.getIndex(), field_175815_j, 2);
            }
            this.setBlockState(world, Blocks.vine.getStateFromMeta(15), 8, -1, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.vine.getStateFromMeta(15), 8, -2, 3, structureBoundingBox);
            if (!this.field_74947_h) {
                this.field_74947_h = this.generateChestContents(world, structureBoundingBox, random, 8, -3, 3, WeightedRandomChestContent.func_177629_a(field_175816_i, Items.enchanted_book.getRandom(random)), 2 + random.nextInt(5));
            }
            this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 9, -3, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 8, -3, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 4, -3, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 5, -2, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 5, -1, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 6, -3, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 7, -2, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 7, -1, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 8, -3, 5, structureBoundingBox);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 9, -1, 1, 9, -1, 5, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithAir(world, structureBoundingBox, 8, -3, 8, 10, -1, 10);
            this.setBlockState(world, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 8, -2, 11, structureBoundingBox);
            this.setBlockState(world, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 9, -2, 11, structureBoundingBox);
            this.setBlockState(world, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 10, -2, 11, structureBoundingBox);
            this.setBlockState(world, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing.getFront(this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 8, -2, 12, structureBoundingBox);
            this.setBlockState(world, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing.getFront(this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 9, -2, 12, structureBoundingBox);
            this.setBlockState(world, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing.getFront(this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 10, -2, 12, structureBoundingBox);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 8, -3, 8, 8, -3, 10, false, random, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 10, -3, 8, 10, -3, 10, false, random, junglePyramidsRandomScatteredStones);
            this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 10, -2, 9, structureBoundingBox);
            this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 8, -2, 9, structureBoundingBox);
            this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 8, -2, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 10, -1, 9, structureBoundingBox);
            this.setBlockState(world, Blocks.sticky_piston.getStateFromMeta(EnumFacing.UP.getIndex()), 9, -2, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.sticky_piston.getStateFromMeta(this.getMetadataWithOffset(Blocks.sticky_piston, EnumFacing.WEST.getIndex())), 10, -2, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.sticky_piston.getStateFromMeta(this.getMetadataWithOffset(Blocks.sticky_piston, EnumFacing.WEST.getIndex())), 10, -1, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.unpowered_repeater.getStateFromMeta(this.getMetadataWithOffset(Blocks.unpowered_repeater, EnumFacing.NORTH.getHorizontalIndex())), 10, -2, 10, structureBoundingBox);
            if (!this.field_74948_i) {
                this.field_74948_i = this.generateChestContents(world, structureBoundingBox, random, 9, -3, 10, WeightedRandomChestContent.func_177629_a(field_175816_i, Items.enchanted_book.getRandom(random)), 2 + random.nextInt(5));
            }
            return true;
        }

        public JunglePyramid() {
        }

        static class Stones
        extends StructureComponent.BlockSelector {
            @Override
            public void selectBlocks(Random random, int n, int n2, int n3, boolean bl) {
                this.blockstate = random.nextFloat() < 0.4f ? Blocks.cobblestone.getDefaultState() : Blocks.mossy_cobblestone.getDefaultState();
            }

            private Stones() {
            }
        }
    }

    public static class SwampHut
    extends Feature {
        private boolean hasWitch;

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.hasWitch = nBTTagCompound.getBoolean("Witch");
        }

        public SwampHut() {
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n;
            int n2;
            if (!this.func_74935_a(world, structureBoundingBox, 0)) {
                return false;
            }
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 1, 5, 1, 7, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 4, 2, 5, 4, 7, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 1, 0, 4, 1, 0, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 2, 2, 3, 3, 2, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 3, 1, 3, 6, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 2, 3, 5, 3, 6, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 2, 7, 4, 3, 7, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 2, 1, 3, 2, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 0, 2, 5, 3, 2, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 7, 1, 3, 7, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 0, 7, 5, 3, 7, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 2, 3, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 3, 3, 7, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 1, 3, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 5, 3, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 5, 3, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.flower_pot.getDefaultState().withProperty(BlockFlowerPot.CONTENTS, BlockFlowerPot.EnumFlowerType.MUSHROOM_RED), 1, 3, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.crafting_table.getDefaultState(), 3, 2, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.cauldron.getDefaultState(), 4, 2, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 2, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 5, 2, 1, structureBoundingBox);
            int n3 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
            int n4 = this.getMetadataWithOffset(Blocks.oak_stairs, 1);
            int n5 = this.getMetadataWithOffset(Blocks.oak_stairs, 0);
            int n6 = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
            this.fillWithBlocks(world, structureBoundingBox, 0, 4, 1, 6, 4, 1, Blocks.spruce_stairs.getStateFromMeta(n3), Blocks.spruce_stairs.getStateFromMeta(n3), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 4, 2, 0, 4, 7, Blocks.spruce_stairs.getStateFromMeta(n5), Blocks.spruce_stairs.getStateFromMeta(n5), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 4, 2, 6, 4, 7, Blocks.spruce_stairs.getStateFromMeta(n4), Blocks.spruce_stairs.getStateFromMeta(n4), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 4, 8, 6, 4, 8, Blocks.spruce_stairs.getStateFromMeta(n6), Blocks.spruce_stairs.getStateFromMeta(n6), false);
            int n7 = 2;
            while (n7 <= 7) {
                n2 = 1;
                while (n2 <= 5) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.log.getDefaultState(), n2, -1, n7, structureBoundingBox);
                    n2 += 4;
                }
                n7 += 5;
            }
            if (!this.hasWitch && structureBoundingBox.isVecInside(new BlockPos(n7 = this.getXWithOffset(2, 5), n2 = this.getYWithOffset(2), n = this.getZWithOffset(2, 5)))) {
                this.hasWitch = true;
                EntityWitch entityWitch = new EntityWitch(world);
                entityWitch.setLocationAndAngles((double)n7 + 0.5, n2, (double)n + 0.5, 0.0f, 0.0f);
                entityWitch.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(n7, n2, n)), null);
                world.spawnEntityInWorld(entityWitch);
            }
            return true;
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setBoolean("Witch", this.hasWitch);
        }

        public SwampHut(Random random, int n, int n2) {
            super(random, n, 64, n2, 7, 7, 9);
        }
    }

    public static class DesertPyramid
    extends Feature {
        private boolean[] field_74940_h = new boolean[4];
        private static final List<WeightedRandomChestContent> itemsToGenerateInTemple = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15), new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2), new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20), new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)});

        public DesertPyramid() {
        }

        public DesertPyramid(Random random, int n, int n2) {
            super(random, n, 64, n2, 21, 15, 21);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.field_74940_h[0] = nBTTagCompound.getBoolean("hasPlacedChest0");
            this.field_74940_h[1] = nBTTagCompound.getBoolean("hasPlacedChest1");
            this.field_74940_h[2] = nBTTagCompound.getBoolean("hasPlacedChest2");
            this.field_74940_h[3] = nBTTagCompound.getBoolean("hasPlacedChest3");
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n;
            int n2;
            this.fillWithBlocks(world, structureBoundingBox, 0, -4, 0, this.scatteredFeatureSizeX - 1, 0, this.scatteredFeatureSizeZ - 1, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            int n3 = 1;
            while (n3 <= 9) {
                this.fillWithBlocks(world, structureBoundingBox, n3, n3, n3, this.scatteredFeatureSizeX - 1 - n3, n3, this.scatteredFeatureSizeZ - 1 - n3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, n3 + 1, n3, n3 + 1, this.scatteredFeatureSizeX - 2 - n3, n3, this.scatteredFeatureSizeZ - 2 - n3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                ++n3;
            }
            n3 = 0;
            while (n3 < this.scatteredFeatureSizeX) {
                n2 = 0;
                while (n2 < this.scatteredFeatureSizeZ) {
                    n = -5;
                    this.replaceAirAndLiquidDownwards(world, Blocks.sandstone.getDefaultState(), n3, n, n2, structureBoundingBox);
                    ++n2;
                }
                ++n3;
            }
            n3 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 3);
            n2 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 2);
            n = this.getMetadataWithOffset(Blocks.sandstone_stairs, 0);
            int n4 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 1);
            int n5 = ~EnumDyeColor.ORANGE.getDyeDamage() & 0xF;
            int n6 = ~EnumDyeColor.BLUE.getDyeDamage() & 0xF;
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 4, 9, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 10, 1, 3, 10, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(n3), 2, 10, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(n2), 2, 10, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(n), 0, 10, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(n4), 4, 10, 2, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, this.scatteredFeatureSizeX - 5, 0, 0, this.scatteredFeatureSizeX - 1, 9, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, this.scatteredFeatureSizeX - 4, 10, 1, this.scatteredFeatureSizeX - 2, 10, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(n3), this.scatteredFeatureSizeX - 3, 10, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(n2), this.scatteredFeatureSizeX - 3, 10, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(n), this.scatteredFeatureSizeX - 5, 10, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(n4), this.scatteredFeatureSizeX - 1, 10, 2, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 8, 0, 0, 12, 4, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 9, 1, 0, 11, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 1, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 2, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 3, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, 3, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 3, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 2, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 1, 1, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 4, 1, 1, 8, 3, 3, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 1, 2, 8, 2, 2, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 12, 1, 1, 16, 3, 3, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 12, 1, 2, 16, 2, 2, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 4, 5, this.scatteredFeatureSizeX - 6, 4, this.scatteredFeatureSizeZ - 6, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 9, 4, 9, 11, 4, 11, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 1, 8, 8, 3, 8, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.fillWithBlocks(world, structureBoundingBox, 12, 1, 8, 12, 3, 8, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 1, 12, 8, 3, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.fillWithBlocks(world, structureBoundingBox, 12, 1, 12, 12, 3, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 5, 4, 4, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, this.scatteredFeatureSizeX - 5, 1, 5, this.scatteredFeatureSizeX - 2, 4, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 7, 9, 6, 7, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, this.scatteredFeatureSizeX - 7, 7, 9, this.scatteredFeatureSizeX - 7, 7, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 5, 9, 5, 7, 11, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.fillWithBlocks(world, structureBoundingBox, this.scatteredFeatureSizeX - 6, 5, 9, this.scatteredFeatureSizeX - 6, 7, 11, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.setBlockState(world, Blocks.air.getDefaultState(), 5, 5, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 5, 6, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 6, 6, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 6, 5, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 6, 6, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 7, 6, 10, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 2, 4, 4, 2, 6, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, this.scatteredFeatureSizeX - 3, 4, 4, this.scatteredFeatureSizeX - 3, 6, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(n3), 2, 4, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(n3), 2, 3, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(n3), this.scatteredFeatureSizeX - 3, 4, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(n3), this.scatteredFeatureSizeX - 3, 3, 4, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 3, 2, 2, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, this.scatteredFeatureSizeX - 3, 1, 3, this.scatteredFeatureSizeX - 2, 2, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.setBlockState(world, Blocks.sandstone_stairs.getDefaultState(), 1, 1, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone_stairs.getDefaultState(), this.scatteredFeatureSizeX - 2, 1, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), 1, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), this.scatteredFeatureSizeX - 2, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(n4), 2, 1, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(n), this.scatteredFeatureSizeX - 3, 1, 2, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 4, 3, 5, 4, 3, 18, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, this.scatteredFeatureSizeX - 5, 3, 5, this.scatteredFeatureSizeX - 5, 3, 17, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 1, 5, 4, 2, 16, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, this.scatteredFeatureSizeX - 6, 1, 5, this.scatteredFeatureSizeX - 5, 2, 16, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            int n7 = 5;
            while (n7 <= 17) {
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 4, 1, n7, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 4, 2, n7, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), this.scatteredFeatureSizeX - 5, 1, n7, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), this.scatteredFeatureSizeX - 5, 2, n7, structureBoundingBox);
                n7 += 2;
            }
            this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), 10, 0, 7, structureBoundingBox);
            this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), 10, 0, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), 9, 0, 9, structureBoundingBox);
            this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), 11, 0, 9, structureBoundingBox);
            this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), 8, 0, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), 12, 0, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), 7, 0, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), 13, 0, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), 9, 0, 11, structureBoundingBox);
            this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), 11, 0, 11, structureBoundingBox);
            this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), 10, 0, 12, structureBoundingBox);
            this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), 10, 0, 13, structureBoundingBox);
            this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n6), 10, 0, 10, structureBoundingBox);
            n7 = 0;
            while (n7 <= this.scatteredFeatureSizeX - 1) {
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7, 2, 1, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7, 2, 2, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7, 2, 3, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7, 3, 1, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7, 3, 2, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7, 3, 3, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7, 4, 1, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), n7, 4, 2, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7, 4, 3, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7, 5, 1, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7, 5, 2, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7, 5, 3, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7, 6, 1, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), n7, 6, 2, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7, 6, 3, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7, 7, 1, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7, 7, 2, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7, 7, 3, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7, 8, 1, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7, 8, 2, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7, 8, 3, structureBoundingBox);
                n7 += this.scatteredFeatureSizeX - 1;
            }
            n7 = 2;
            while (n7 <= this.scatteredFeatureSizeX - 3) {
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7 - 1, 2, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7, 2, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7 + 1, 2, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7 - 1, 3, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7, 3, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7 + 1, 3, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7 - 1, 4, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), n7, 4, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7 + 1, 4, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7 - 1, 5, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7, 5, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7 + 1, 5, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7 - 1, 6, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), n7, 6, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7 + 1, 6, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7 - 1, 7, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7, 7, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), n7 + 1, 7, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7 - 1, 8, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7, 8, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), n7 + 1, 8, 0, structureBoundingBox);
                n7 += this.scatteredFeatureSizeX - 3 - 2;
            }
            this.fillWithBlocks(world, structureBoundingBox, 8, 4, 0, 12, 6, 0, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.setBlockState(world, Blocks.air.getDefaultState(), 8, 6, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 12, 6, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), 9, 5, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, 5, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(n5), 11, 5, 0, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 8, -14, 8, 12, -11, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, -10, 8, 12, -10, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, -9, 8, 12, -9, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, -8, 8, 12, -1, 12, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 9, -11, 9, 11, -1, 11, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.setBlockState(world, Blocks.stone_pressure_plate.getDefaultState(), 10, -11, 10, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 9, -13, 9, 11, -13, 11, Blocks.tnt.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.setBlockState(world, Blocks.air.getDefaultState(), 8, -11, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 8, -10, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 7, -10, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 7, -11, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 12, -11, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 12, -10, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 13, -10, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 13, -11, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 10, -11, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 10, -10, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 7, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 7, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 10, -11, 12, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 10, -10, 12, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 13, structureBoundingBox);
            this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 13, structureBoundingBox);
            for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
                if (this.field_74940_h[enumFacing.getHorizontalIndex()]) continue;
                int n8 = enumFacing.getFrontOffsetX() * 2;
                int n9 = enumFacing.getFrontOffsetZ() * 2;
                this.field_74940_h[enumFacing.getHorizontalIndex()] = this.generateChestContents(world, structureBoundingBox, random, 10 + n8, -11, 10 + n9, WeightedRandomChestContent.func_177629_a(itemsToGenerateInTemple, Items.enchanted_book.getRandom(random)), 2 + random.nextInt(5));
            }
            return true;
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setBoolean("hasPlacedChest0", this.field_74940_h[0]);
            nBTTagCompound.setBoolean("hasPlacedChest1", this.field_74940_h[1]);
            nBTTagCompound.setBoolean("hasPlacedChest2", this.field_74940_h[2]);
            nBTTagCompound.setBoolean("hasPlacedChest3", this.field_74940_h[3]);
        }
    }
}

