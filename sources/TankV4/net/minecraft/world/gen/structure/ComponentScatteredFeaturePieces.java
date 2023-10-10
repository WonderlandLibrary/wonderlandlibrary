package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Iterator;
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
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

public class ComponentScatteredFeaturePieces {
   public static void registerScatteredFeaturePieces() {
      MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.DesertPyramid.class, "TeDP");
      MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.JunglePyramid.class, "TeJP");
      MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.SwampHut.class, "TeSH");
   }

   public static class JunglePyramid extends ComponentScatteredFeaturePieces.Feature {
      private boolean field_74945_j;
      private boolean field_74947_h;
      private boolean field_74948_i;
      private static final List field_175815_j;
      private static final List field_175816_i;
      private static ComponentScatteredFeaturePieces.JunglePyramid.Stones junglePyramidsRandomScatteredStones;
      private boolean field_74946_k;

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.field_74947_h = var1.getBoolean("placedMainChest");
         this.field_74948_i = var1.getBoolean("placedHiddenChest");
         this.field_74945_j = var1.getBoolean("placedTrap1");
         this.field_74946_k = var1.getBoolean("placedTrap2");
      }

      public JunglePyramid(Random var1, int var2, int var3) {
         super(var1, var2, 64, var3, 12, 10, 15);
      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setBoolean("placedMainChest", this.field_74947_h);
         var1.setBoolean("placedHiddenChest", this.field_74948_i);
         var1.setBoolean("placedTrap1", this.field_74945_j);
         var1.setBoolean("placedTrap2", this.field_74946_k);
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (!this.func_74935_a(var1, var3, 0)) {
            return false;
         } else {
            int var4 = this.getMetadataWithOffset(Blocks.stone_stairs, 3);
            int var5 = this.getMetadataWithOffset(Blocks.stone_stairs, 2);
            int var6 = this.getMetadataWithOffset(Blocks.stone_stairs, 0);
            int var7 = this.getMetadataWithOffset(Blocks.stone_stairs, 1);
            this.fillWithRandomizedBlocks(var1, var3, 0, -4, 0, this.scatteredFeatureSizeX - 1, 0, this.scatteredFeatureSizeZ - 1, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 2, 1, 2, 9, 2, 2, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 2, 1, 12, 9, 2, 12, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 2, 1, 3, 2, 2, 11, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 9, 1, 3, 9, 2, 11, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 1, 3, 1, 10, 6, 1, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 1, 3, 13, 10, 6, 13, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 1, 3, 2, 1, 6, 12, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 10, 3, 2, 10, 6, 12, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 2, 3, 2, 9, 3, 12, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 2, 6, 2, 9, 6, 12, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 3, 7, 3, 8, 7, 11, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 4, 8, 4, 7, 8, 10, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithAir(var1, var3, 3, 1, 3, 8, 2, 11);
            this.fillWithAir(var1, var3, 4, 3, 6, 7, 3, 9);
            this.fillWithAir(var1, var3, 2, 4, 2, 9, 5, 12);
            this.fillWithAir(var1, var3, 4, 6, 5, 7, 6, 9);
            this.fillWithAir(var1, var3, 5, 7, 6, 6, 7, 8);
            this.fillWithAir(var1, var3, 5, 1, 2, 6, 2, 2);
            this.fillWithAir(var1, var3, 5, 2, 12, 6, 2, 12);
            this.fillWithAir(var1, var3, 5, 5, 1, 6, 5, 1);
            this.fillWithAir(var1, var3, 5, 5, 13, 6, 5, 13);
            this.setBlockState(var1, Blocks.air.getDefaultState(), 1, 5, 5, var3);
            this.setBlockState(var1, Blocks.air.getDefaultState(), 10, 5, 5, var3);
            this.setBlockState(var1, Blocks.air.getDefaultState(), 1, 5, 9, var3);
            this.setBlockState(var1, Blocks.air.getDefaultState(), 10, 5, 9, var3);

            int var8;
            for(var8 = 0; var8 <= 14; var8 += 14) {
               this.fillWithRandomizedBlocks(var1, var3, 2, 4, var8, 2, 5, var8, false, var2, junglePyramidsRandomScatteredStones);
               this.fillWithRandomizedBlocks(var1, var3, 4, 4, var8, 4, 5, var8, false, var2, junglePyramidsRandomScatteredStones);
               this.fillWithRandomizedBlocks(var1, var3, 7, 4, var8, 7, 5, var8, false, var2, junglePyramidsRandomScatteredStones);
               this.fillWithRandomizedBlocks(var1, var3, 9, 4, var8, 9, 5, var8, false, var2, junglePyramidsRandomScatteredStones);
            }

            this.fillWithRandomizedBlocks(var1, var3, 5, 6, 0, 6, 6, 0, false, var2, junglePyramidsRandomScatteredStones);

            for(var8 = 0; var8 <= 11; var8 += 11) {
               for(int var9 = 2; var9 <= 12; var9 += 2) {
                  this.fillWithRandomizedBlocks(var1, var3, var8, 4, var9, var8, 5, var9, false, var2, junglePyramidsRandomScatteredStones);
               }

               this.fillWithRandomizedBlocks(var1, var3, var8, 6, 5, var8, 6, 5, false, var2, junglePyramidsRandomScatteredStones);
               this.fillWithRandomizedBlocks(var1, var3, var8, 6, 9, var8, 6, 9, false, var2, junglePyramidsRandomScatteredStones);
            }

            this.fillWithRandomizedBlocks(var1, var3, 2, 7, 2, 2, 9, 2, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 9, 7, 2, 9, 9, 2, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 2, 7, 12, 2, 9, 12, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 9, 7, 12, 9, 9, 12, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 4, 9, 4, 4, 9, 4, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 7, 9, 4, 7, 9, 4, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 4, 9, 10, 4, 9, 10, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 7, 9, 10, 7, 9, 10, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 5, 9, 7, 6, 9, 7, false, var2, junglePyramidsRandomScatteredStones);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var4), 5, 9, 6, var3);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var4), 6, 9, 6, var3);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var5), 5, 9, 8, var3);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var5), 6, 9, 8, var3);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var4), 4, 0, 0, var3);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var4), 5, 0, 0, var3);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var4), 6, 0, 0, var3);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var4), 7, 0, 0, var3);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var4), 4, 1, 8, var3);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var4), 4, 2, 9, var3);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var4), 4, 3, 10, var3);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var4), 7, 1, 8, var3);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var4), 7, 2, 9, var3);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var4), 7, 3, 10, var3);
            this.fillWithRandomizedBlocks(var1, var3, 4, 1, 9, 4, 1, 9, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 7, 1, 9, 7, 1, 9, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 4, 1, 10, 7, 2, 10, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 5, 4, 5, 6, 4, 5, false, var2, junglePyramidsRandomScatteredStones);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var6), 4, 4, 5, var3);
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var7), 7, 4, 5, var3);

            for(var8 = 0; var8 < 4; ++var8) {
               this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var5), 5, 0 - var8, 6 + var8, var3);
               this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var5), 6, 0 - var8, 6 + var8, var3);
               this.fillWithAir(var1, var3, 5, 0 - var8, 7 + var8, 6, 0 - var8, 9 + var8);
            }

            this.fillWithAir(var1, var3, 1, -3, 12, 10, -1, 13);
            this.fillWithAir(var1, var3, 1, -3, 1, 3, -1, 13);
            this.fillWithAir(var1, var3, 1, -3, 1, 9, -1, 5);

            for(var8 = 1; var8 <= 13; var8 += 2) {
               this.fillWithRandomizedBlocks(var1, var3, 1, -3, var8, 1, -2, var8, false, var2, junglePyramidsRandomScatteredStones);
            }

            for(var8 = 2; var8 <= 12; var8 += 2) {
               this.fillWithRandomizedBlocks(var1, var3, 1, -1, var8, 3, -1, var8, false, var2, junglePyramidsRandomScatteredStones);
            }

            this.fillWithRandomizedBlocks(var1, var3, 2, -2, 1, 5, -2, 1, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 7, -2, 1, 9, -2, 1, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 6, -3, 1, 6, -3, 1, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 6, -1, 1, 6, -1, 1, false, var2, junglePyramidsRandomScatteredStones);
            this.setBlockState(var1, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.EAST.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, true), 1, -3, 8, var3);
            this.setBlockState(var1, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.WEST.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, true), 4, -3, 8, var3);
            this.setBlockState(var1, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, true), 2, -3, 8, var3);
            this.setBlockState(var1, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, true), 3, -3, 8, var3);
            this.setBlockState(var1, Blocks.redstone_wire.getDefaultState(), 5, -3, 7, var3);
            this.setBlockState(var1, Blocks.redstone_wire.getDefaultState(), 5, -3, 6, var3);
            this.setBlockState(var1, Blocks.redstone_wire.getDefaultState(), 5, -3, 5, var3);
            this.setBlockState(var1, Blocks.redstone_wire.getDefaultState(), 5, -3, 4, var3);
            this.setBlockState(var1, Blocks.redstone_wire.getDefaultState(), 5, -3, 3, var3);
            this.setBlockState(var1, Blocks.redstone_wire.getDefaultState(), 5, -3, 2, var3);
            this.setBlockState(var1, Blocks.redstone_wire.getDefaultState(), 5, -3, 1, var3);
            this.setBlockState(var1, Blocks.redstone_wire.getDefaultState(), 4, -3, 1, var3);
            this.setBlockState(var1, Blocks.mossy_cobblestone.getDefaultState(), 3, -3, 1, var3);
            if (!this.field_74945_j) {
               this.field_74945_j = this.generateDispenserContents(var1, var3, var2, 3, -2, 1, EnumFacing.NORTH.getIndex(), field_175815_j, 2);
            }

            this.setBlockState(var1, Blocks.vine.getStateFromMeta(15), 3, -2, 2, var3);
            this.setBlockState(var1, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.NORTH.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, true), 7, -3, 1, var3);
            this.setBlockState(var1, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.SOUTH.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, true), 7, -3, 5, var3);
            this.setBlockState(var1, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, true), 7, -3, 2, var3);
            this.setBlockState(var1, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, true), 7, -3, 3, var3);
            this.setBlockState(var1, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, true), 7, -3, 4, var3);
            this.setBlockState(var1, Blocks.redstone_wire.getDefaultState(), 8, -3, 6, var3);
            this.setBlockState(var1, Blocks.redstone_wire.getDefaultState(), 9, -3, 6, var3);
            this.setBlockState(var1, Blocks.redstone_wire.getDefaultState(), 9, -3, 5, var3);
            this.setBlockState(var1, Blocks.mossy_cobblestone.getDefaultState(), 9, -3, 4, var3);
            this.setBlockState(var1, Blocks.redstone_wire.getDefaultState(), 9, -2, 4, var3);
            if (!this.field_74946_k) {
               this.field_74946_k = this.generateDispenserContents(var1, var3, var2, 9, -2, 3, EnumFacing.WEST.getIndex(), field_175815_j, 2);
            }

            this.setBlockState(var1, Blocks.vine.getStateFromMeta(15), 8, -1, 3, var3);
            this.setBlockState(var1, Blocks.vine.getStateFromMeta(15), 8, -2, 3, var3);
            if (!this.field_74947_h) {
               this.field_74947_h = this.generateChestContents(var1, var3, var2, 8, -3, 3, WeightedRandomChestContent.func_177629_a(field_175816_i, Items.enchanted_book.getRandom(var2)), 2 + var2.nextInt(5));
            }

            this.setBlockState(var1, Blocks.mossy_cobblestone.getDefaultState(), 9, -3, 2, var3);
            this.setBlockState(var1, Blocks.mossy_cobblestone.getDefaultState(), 8, -3, 1, var3);
            this.setBlockState(var1, Blocks.mossy_cobblestone.getDefaultState(), 4, -3, 5, var3);
            this.setBlockState(var1, Blocks.mossy_cobblestone.getDefaultState(), 5, -2, 5, var3);
            this.setBlockState(var1, Blocks.mossy_cobblestone.getDefaultState(), 5, -1, 5, var3);
            this.setBlockState(var1, Blocks.mossy_cobblestone.getDefaultState(), 6, -3, 5, var3);
            this.setBlockState(var1, Blocks.mossy_cobblestone.getDefaultState(), 7, -2, 5, var3);
            this.setBlockState(var1, Blocks.mossy_cobblestone.getDefaultState(), 7, -1, 5, var3);
            this.setBlockState(var1, Blocks.mossy_cobblestone.getDefaultState(), 8, -3, 5, var3);
            this.fillWithRandomizedBlocks(var1, var3, 9, -1, 1, 9, -1, 5, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithAir(var1, var3, 8, -3, 8, 10, -1, 10);
            this.setBlockState(var1, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 8, -2, 11, var3);
            this.setBlockState(var1, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 9, -2, 11, var3);
            this.setBlockState(var1, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 10, -2, 11, var3);
            this.setBlockState(var1, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing.getFront(this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 8, -2, 12, var3);
            this.setBlockState(var1, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing.getFront(this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 9, -2, 12, var3);
            this.setBlockState(var1, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing.getFront(this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 10, -2, 12, var3);
            this.fillWithRandomizedBlocks(var1, var3, 8, -3, 8, 8, -3, 10, false, var2, junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(var1, var3, 10, -3, 8, 10, -3, 10, false, var2, junglePyramidsRandomScatteredStones);
            this.setBlockState(var1, Blocks.mossy_cobblestone.getDefaultState(), 10, -2, 9, var3);
            this.setBlockState(var1, Blocks.redstone_wire.getDefaultState(), 8, -2, 9, var3);
            this.setBlockState(var1, Blocks.redstone_wire.getDefaultState(), 8, -2, 10, var3);
            this.setBlockState(var1, Blocks.redstone_wire.getDefaultState(), 10, -1, 9, var3);
            this.setBlockState(var1, Blocks.sticky_piston.getStateFromMeta(EnumFacing.UP.getIndex()), 9, -2, 8, var3);
            this.setBlockState(var1, Blocks.sticky_piston.getStateFromMeta(this.getMetadataWithOffset(Blocks.sticky_piston, EnumFacing.WEST.getIndex())), 10, -2, 8, var3);
            this.setBlockState(var1, Blocks.sticky_piston.getStateFromMeta(this.getMetadataWithOffset(Blocks.sticky_piston, EnumFacing.WEST.getIndex())), 10, -1, 8, var3);
            this.setBlockState(var1, Blocks.unpowered_repeater.getStateFromMeta(this.getMetadataWithOffset(Blocks.unpowered_repeater, EnumFacing.NORTH.getHorizontalIndex())), 10, -2, 10, var3);
            if (!this.field_74948_i) {
               this.field_74948_i = this.generateChestContents(var1, var3, var2, 9, -3, 10, WeightedRandomChestContent.func_177629_a(field_175816_i, Items.enchanted_book.getRandom(var2)), 2 + var2.nextInt(5));
            }

            return true;
         }
      }

      public JunglePyramid() {
      }

      static {
         field_175816_i = Lists.newArrayList((Object[])(new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15), new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2), new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20), new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)));
         field_175815_j = Lists.newArrayList((Object[])(new WeightedRandomChestContent(Items.arrow, 0, 2, 7, 30)));
         junglePyramidsRandomScatteredStones = new ComponentScatteredFeaturePieces.JunglePyramid.Stones((ComponentScatteredFeaturePieces.JunglePyramid.Stones)null);
      }

      static class Stones extends StructureComponent.BlockSelector {
         public void selectBlocks(Random var1, int var2, int var3, int var4, boolean var5) {
            if (var1.nextFloat() < 0.4F) {
               this.blockstate = Blocks.cobblestone.getDefaultState();
            } else {
               this.blockstate = Blocks.mossy_cobblestone.getDefaultState();
            }

         }

         Stones(ComponentScatteredFeaturePieces.JunglePyramid.Stones var1) {
            this();
         }

         private Stones() {
         }
      }
   }

   public static class SwampHut extends ComponentScatteredFeaturePieces.Feature {
      private boolean hasWitch;

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setBoolean("Witch", this.hasWitch);
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.hasWitch = var1.getBoolean("Witch");
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (!this.func_74935_a(var1, var3, 0)) {
            return false;
         } else {
            this.fillWithBlocks(var1, var3, 1, 1, 1, 5, 1, 7, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(var1, var3, 1, 4, 2, 5, 4, 7, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(var1, var3, 2, 1, 0, 4, 1, 0, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(var1, var3, 2, 2, 2, 3, 3, 2, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(var1, var3, 1, 2, 3, 1, 3, 6, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(var1, var3, 5, 2, 3, 5, 3, 6, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(var1, var3, 2, 2, 7, 4, 3, 7, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(var1, var3, 1, 0, 2, 1, 3, 2, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(var1, var3, 5, 0, 2, 5, 3, 2, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(var1, var3, 1, 0, 7, 1, 3, 7, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(var1, var3, 5, 0, 7, 5, 3, 7, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 2, 3, 2, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 3, 3, 7, var3);
            this.setBlockState(var1, Blocks.air.getDefaultState(), 1, 3, 4, var3);
            this.setBlockState(var1, Blocks.air.getDefaultState(), 5, 3, 4, var3);
            this.setBlockState(var1, Blocks.air.getDefaultState(), 5, 3, 5, var3);
            this.setBlockState(var1, Blocks.flower_pot.getDefaultState().withProperty(BlockFlowerPot.CONTENTS, BlockFlowerPot.EnumFlowerType.MUSHROOM_RED), 1, 3, 5, var3);
            this.setBlockState(var1, Blocks.crafting_table.getDefaultState(), 3, 2, 6, var3);
            this.setBlockState(var1, Blocks.cauldron.getDefaultState(), 4, 2, 6, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 1, 2, 1, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 5, 2, 1, var3);
            int var4 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
            int var5 = this.getMetadataWithOffset(Blocks.oak_stairs, 1);
            int var6 = this.getMetadataWithOffset(Blocks.oak_stairs, 0);
            int var7 = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
            this.fillWithBlocks(var1, var3, 0, 4, 1, 6, 4, 1, Blocks.spruce_stairs.getStateFromMeta(var4), Blocks.spruce_stairs.getStateFromMeta(var4), false);
            this.fillWithBlocks(var1, var3, 0, 4, 2, 0, 4, 7, Blocks.spruce_stairs.getStateFromMeta(var6), Blocks.spruce_stairs.getStateFromMeta(var6), false);
            this.fillWithBlocks(var1, var3, 6, 4, 2, 6, 4, 7, Blocks.spruce_stairs.getStateFromMeta(var5), Blocks.spruce_stairs.getStateFromMeta(var5), false);
            this.fillWithBlocks(var1, var3, 0, 4, 8, 6, 4, 8, Blocks.spruce_stairs.getStateFromMeta(var7), Blocks.spruce_stairs.getStateFromMeta(var7), false);

            int var8;
            int var9;
            for(var8 = 2; var8 <= 7; var8 += 5) {
               for(var9 = 1; var9 <= 5; var9 += 4) {
                  this.replaceAirAndLiquidDownwards(var1, Blocks.log.getDefaultState(), var9, -1, var8, var3);
               }
            }

            if (!this.hasWitch) {
               var8 = this.getXWithOffset(2, 5);
               var9 = this.getYWithOffset(2);
               int var10 = this.getZWithOffset(2, 5);
               if (var3.isVecInside(new BlockPos(var8, var9, var10))) {
                  this.hasWitch = true;
                  EntityWitch var11 = new EntityWitch(var1);
                  var11.setLocationAndAngles((double)var8 + 0.5D, (double)var9, (double)var10 + 0.5D, 0.0F, 0.0F);
                  var11.onInitialSpawn(var1.getDifficultyForLocation(new BlockPos(var8, var9, var10)), (IEntityLivingData)null);
                  var1.spawnEntityInWorld(var11);
               }
            }

            return true;
         }
      }

      public SwampHut() {
      }

      public SwampHut(Random var1, int var2, int var3) {
         super(var1, var2, 64, var3, 7, 7, 9);
      }
   }

   public static class DesertPyramid extends ComponentScatteredFeaturePieces.Feature {
      private static final List itemsToGenerateInTemple;
      private boolean[] field_74940_h = new boolean[4];

      public DesertPyramid() {
      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setBoolean("hasPlacedChest0", this.field_74940_h[0]);
         var1.setBoolean("hasPlacedChest1", this.field_74940_h[1]);
         var1.setBoolean("hasPlacedChest2", this.field_74940_h[2]);
         var1.setBoolean("hasPlacedChest3", this.field_74940_h[3]);
      }

      public DesertPyramid(Random var1, int var2, int var3) {
         super(var1, var2, 64, var3, 21, 15, 21);
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(var1, var3, 0, -4, 0, this.scatteredFeatureSizeX - 1, 0, this.scatteredFeatureSizeZ - 1, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);

         int var4;
         for(var4 = 1; var4 <= 9; ++var4) {
            this.fillWithBlocks(var1, var3, var4, var4, var4, this.scatteredFeatureSizeX - 1 - var4, var4, this.scatteredFeatureSizeZ - 1 - var4, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(var1, var3, var4 + 1, var4, var4 + 1, this.scatteredFeatureSizeX - 2 - var4, var4, this.scatteredFeatureSizeZ - 2 - var4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         }

         int var5;
         for(var4 = 0; var4 < this.scatteredFeatureSizeX; ++var4) {
            for(var5 = 0; var5 < this.scatteredFeatureSizeZ; ++var5) {
               byte var6 = -5;
               this.replaceAirAndLiquidDownwards(var1, Blocks.sandstone.getDefaultState(), var4, var6, var5, var3);
            }
         }

         var4 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 3);
         var5 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 2);
         int var15 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 0);
         int var7 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 1);
         int var8 = ~EnumDyeColor.ORANGE.getDyeDamage() & 15;
         int var9 = ~EnumDyeColor.BLUE.getDyeDamage() & 15;
         this.fillWithBlocks(var1, var3, 0, 0, 0, 4, 9, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 10, 1, 3, 10, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
         this.setBlockState(var1, Blocks.sandstone_stairs.getStateFromMeta(var4), 2, 10, 0, var3);
         this.setBlockState(var1, Blocks.sandstone_stairs.getStateFromMeta(var5), 2, 10, 4, var3);
         this.setBlockState(var1, Blocks.sandstone_stairs.getStateFromMeta(var15), 0, 10, 2, var3);
         this.setBlockState(var1, Blocks.sandstone_stairs.getStateFromMeta(var7), 4, 10, 2, var3);
         this.fillWithBlocks(var1, var3, this.scatteredFeatureSizeX - 5, 0, 0, this.scatteredFeatureSizeX - 1, 9, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, this.scatteredFeatureSizeX - 4, 10, 1, this.scatteredFeatureSizeX - 2, 10, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
         this.setBlockState(var1, Blocks.sandstone_stairs.getStateFromMeta(var4), this.scatteredFeatureSizeX - 3, 10, 0, var3);
         this.setBlockState(var1, Blocks.sandstone_stairs.getStateFromMeta(var5), this.scatteredFeatureSizeX - 3, 10, 4, var3);
         this.setBlockState(var1, Blocks.sandstone_stairs.getStateFromMeta(var15), this.scatteredFeatureSizeX - 5, 10, 2, var3);
         this.setBlockState(var1, Blocks.sandstone_stairs.getStateFromMeta(var7), this.scatteredFeatureSizeX - 1, 10, 2, var3);
         this.fillWithBlocks(var1, var3, 8, 0, 0, 12, 4, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 9, 1, 0, 11, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 1, 1, var3);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 2, 1, var3);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 3, 1, var3);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, 3, 1, var3);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 3, 1, var3);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 2, 1, var3);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 1, 1, var3);
         this.fillWithBlocks(var1, var3, 4, 1, 1, 8, 3, 3, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 4, 1, 2, 8, 2, 2, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 12, 1, 1, 16, 3, 3, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 12, 1, 2, 16, 2, 2, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 5, 4, 5, this.scatteredFeatureSizeX - 6, 4, this.scatteredFeatureSizeZ - 6, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 9, 4, 9, 11, 4, 11, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 8, 1, 8, 8, 3, 8, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
         this.fillWithBlocks(var1, var3, 12, 1, 8, 12, 3, 8, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
         this.fillWithBlocks(var1, var3, 8, 1, 12, 8, 3, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
         this.fillWithBlocks(var1, var3, 12, 1, 12, 12, 3, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
         this.fillWithBlocks(var1, var3, 1, 1, 5, 4, 4, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, this.scatteredFeatureSizeX - 5, 1, 5, this.scatteredFeatureSizeX - 2, 4, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 6, 7, 9, 6, 7, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, this.scatteredFeatureSizeX - 7, 7, 9, this.scatteredFeatureSizeX - 7, 7, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 5, 5, 9, 5, 7, 11, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
         this.fillWithBlocks(var1, var3, this.scatteredFeatureSizeX - 6, 5, 9, this.scatteredFeatureSizeX - 6, 7, 11, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 5, 5, 10, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 5, 6, 10, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 6, 6, 10, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 6, 5, 10, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 6, 6, 10, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 7, 6, 10, var3);
         this.fillWithBlocks(var1, var3, 2, 4, 4, 2, 6, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, this.scatteredFeatureSizeX - 3, 4, 4, this.scatteredFeatureSizeX - 3, 6, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.setBlockState(var1, Blocks.sandstone_stairs.getStateFromMeta(var4), 2, 4, 5, var3);
         this.setBlockState(var1, Blocks.sandstone_stairs.getStateFromMeta(var4), 2, 3, 4, var3);
         this.setBlockState(var1, Blocks.sandstone_stairs.getStateFromMeta(var4), this.scatteredFeatureSizeX - 3, 4, 5, var3);
         this.setBlockState(var1, Blocks.sandstone_stairs.getStateFromMeta(var4), this.scatteredFeatureSizeX - 3, 3, 4, var3);
         this.fillWithBlocks(var1, var3, 1, 1, 3, 2, 2, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, this.scatteredFeatureSizeX - 3, 1, 3, this.scatteredFeatureSizeX - 2, 2, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
         this.setBlockState(var1, Blocks.sandstone_stairs.getDefaultState(), 1, 1, 2, var3);
         this.setBlockState(var1, Blocks.sandstone_stairs.getDefaultState(), this.scatteredFeatureSizeX - 2, 1, 2, var3);
         this.setBlockState(var1, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), 1, 2, 2, var3);
         this.setBlockState(var1, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), this.scatteredFeatureSizeX - 2, 2, 2, var3);
         this.setBlockState(var1, Blocks.sandstone_stairs.getStateFromMeta(var7), 2, 1, 2, var3);
         this.setBlockState(var1, Blocks.sandstone_stairs.getStateFromMeta(var15), this.scatteredFeatureSizeX - 3, 1, 2, var3);
         this.fillWithBlocks(var1, var3, 4, 3, 5, 4, 3, 18, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, this.scatteredFeatureSizeX - 5, 3, 5, this.scatteredFeatureSizeX - 5, 3, 17, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 3, 1, 5, 4, 2, 16, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, this.scatteredFeatureSizeX - 6, 1, 5, this.scatteredFeatureSizeX - 5, 2, 16, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);

         int var10;
         for(var10 = 5; var10 <= 17; var10 += 2) {
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 4, 1, var10, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 4, 2, var10, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), this.scatteredFeatureSizeX - 5, 1, var10, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), this.scatteredFeatureSizeX - 5, 2, var10, var3);
         }

         this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), 10, 0, 7, var3);
         this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), 10, 0, 8, var3);
         this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), 9, 0, 9, var3);
         this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), 11, 0, 9, var3);
         this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), 8, 0, 10, var3);
         this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), 12, 0, 10, var3);
         this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), 7, 0, 10, var3);
         this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), 13, 0, 10, var3);
         this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), 9, 0, 11, var3);
         this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), 11, 0, 11, var3);
         this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), 10, 0, 12, var3);
         this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), 10, 0, 13, var3);
         this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var9), 10, 0, 10, var3);

         for(var10 = 0; var10 <= this.scatteredFeatureSizeX - 1; var10 += this.scatteredFeatureSizeX - 1) {
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10, 2, 1, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10, 2, 2, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10, 2, 3, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10, 3, 1, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10, 3, 2, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10, 3, 3, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10, 4, 1, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), var10, 4, 2, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10, 4, 3, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10, 5, 1, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10, 5, 2, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10, 5, 3, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10, 6, 1, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), var10, 6, 2, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10, 6, 3, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10, 7, 1, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10, 7, 2, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10, 7, 3, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10, 8, 1, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10, 8, 2, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10, 8, 3, var3);
         }

         for(var10 = 2; var10 <= this.scatteredFeatureSizeX - 3; var10 += this.scatteredFeatureSizeX - 3 - 2) {
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10 - 1, 2, 0, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10, 2, 0, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10 + 1, 2, 0, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10 - 1, 3, 0, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10, 3, 0, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10 + 1, 3, 0, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10 - 1, 4, 0, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), var10, 4, 0, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10 + 1, 4, 0, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10 - 1, 5, 0, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10, 5, 0, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10 + 1, 5, 0, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10 - 1, 6, 0, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), var10, 6, 0, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10 + 1, 6, 0, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10 - 1, 7, 0, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10, 7, 0, var3);
            this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), var10 + 1, 7, 0, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10 - 1, 8, 0, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10, 8, 0, var3);
            this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), var10 + 1, 8, 0, var3);
         }

         this.fillWithBlocks(var1, var3, 8, 4, 0, 12, 6, 0, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 8, 6, 0, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 12, 6, 0, var3);
         this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), 9, 5, 0, var3);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, 5, 0, var3);
         this.setBlockState(var1, Blocks.stained_hardened_clay.getStateFromMeta(var8), 11, 5, 0, var3);
         this.fillWithBlocks(var1, var3, 8, -14, 8, 12, -11, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
         this.fillWithBlocks(var1, var3, 8, -10, 8, 12, -10, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), false);
         this.fillWithBlocks(var1, var3, 8, -9, 8, 12, -9, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
         this.fillWithBlocks(var1, var3, 8, -8, 8, 12, -1, 12, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 9, -11, 9, 11, -1, 11, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.setBlockState(var1, Blocks.stone_pressure_plate.getDefaultState(), 10, -11, 10, var3);
         this.fillWithBlocks(var1, var3, 9, -13, 9, 11, -13, 11, Blocks.tnt.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 8, -11, 10, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 8, -10, 10, var3);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 7, -10, 10, var3);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 7, -11, 10, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 12, -11, 10, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 12, -10, 10, var3);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 13, -10, 10, var3);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 13, -11, 10, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 10, -11, 8, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 10, -10, 8, var3);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 7, var3);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 7, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 10, -11, 12, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 10, -10, 12, var3);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 13, var3);
         this.setBlockState(var1, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 13, var3);
         Iterator var11 = EnumFacing.Plane.HORIZONTAL.iterator();

         while(var11.hasNext()) {
            Object var16 = var11.next();
            EnumFacing var12 = (EnumFacing)var16;
            if (!this.field_74940_h[var12.getHorizontalIndex()]) {
               int var13 = var12.getFrontOffsetX() * 2;
               int var14 = var12.getFrontOffsetZ() * 2;
               this.field_74940_h[var12.getHorizontalIndex()] = this.generateChestContents(var1, var3, var2, 10 + var13, -11, 10 + var14, WeightedRandomChestContent.func_177629_a(itemsToGenerateInTemple, Items.enchanted_book.getRandom(var2)), 2 + var2.nextInt(5));
            }
         }

         return true;
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.field_74940_h[0] = var1.getBoolean("hasPlacedChest0");
         this.field_74940_h[1] = var1.getBoolean("hasPlacedChest1");
         this.field_74940_h[2] = var1.getBoolean("hasPlacedChest2");
         this.field_74940_h[3] = var1.getBoolean("hasPlacedChest3");
      }

      static {
         itemsToGenerateInTemple = Lists.newArrayList((Object[])(new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15), new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2), new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20), new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)));
      }
   }

   abstract static class Feature extends StructureComponent {
      protected int scatteredFeatureSizeZ;
      protected int scatteredFeatureSizeX;
      private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
      protected int scatteredFeatureSizeY;
      protected int field_74936_d = -1;

      protected boolean func_74935_a(World var1, StructureBoundingBox var2, int var3) {
         if (this.field_74936_d >= 0) {
            return true;
         } else {
            int var4 = 0;
            int var5 = 0;
            BlockPos.MutableBlockPos var6 = new BlockPos.MutableBlockPos();

            for(int var7 = this.boundingBox.minZ; var7 <= this.boundingBox.maxZ; ++var7) {
               for(int var8 = this.boundingBox.minX; var8 <= this.boundingBox.maxX; ++var8) {
                  var6.func_181079_c(var8, 64, var7);
                  if (var2.isVecInside(var6)) {
                     var4 += Math.max(var1.getTopSolidOrLiquidBlock(var6).getY(), var1.provider.getAverageGroundLevel());
                     ++var5;
                  }
               }
            }

            if (var5 == 0) {
               return false;
            } else {
               this.field_74936_d = var4 / var5;
               this.boundingBox.offset(0, this.field_74936_d - this.boundingBox.minY + var3, 0);
               return true;
            }
         }
      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         var1.setInteger("Width", this.scatteredFeatureSizeX);
         var1.setInteger("Height", this.scatteredFeatureSizeY);
         var1.setInteger("Depth", this.scatteredFeatureSizeZ);
         var1.setInteger("HPos", this.field_74936_d);
      }

      public Feature() {
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         this.scatteredFeatureSizeX = var1.getInteger("Width");
         this.scatteredFeatureSizeY = var1.getInteger("Height");
         this.scatteredFeatureSizeZ = var1.getInteger("Depth");
         this.field_74936_d = var1.getInteger("HPos");
      }

      protected Feature(Random var1, int var2, int var3, int var4, int var5, int var6, int var7) {
         super(0);
         this.scatteredFeatureSizeX = var5;
         this.scatteredFeatureSizeY = var6;
         this.scatteredFeatureSizeZ = var7;
         this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(var1);
         switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[this.coordBaseMode.ordinal()]) {
         case 3:
         case 4:
            this.boundingBox = new StructureBoundingBox(var2, var3, var4, var2 + var5 - 1, var3 + var6 - 1, var4 + var7 - 1);
            break;
         default:
            this.boundingBox = new StructureBoundingBox(var2, var3, var4, var2 + var7 - 1, var3 + var6 - 1, var4 + var5 - 1);
         }

      }

      static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing() {
         int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing;
         if (var10000 != null) {
            return var10000;
         } else {
            int[] var0 = new int[EnumFacing.values().length];

            try {
               var0[EnumFacing.DOWN.ordinal()] = 1;
            } catch (NoSuchFieldError var6) {
            }

            try {
               var0[EnumFacing.EAST.ordinal()] = 6;
            } catch (NoSuchFieldError var5) {
            }

            try {
               var0[EnumFacing.NORTH.ordinal()] = 3;
            } catch (NoSuchFieldError var4) {
            }

            try {
               var0[EnumFacing.SOUTH.ordinal()] = 4;
            } catch (NoSuchFieldError var3) {
            }

            try {
               var0[EnumFacing.UP.ordinal()] = 2;
            } catch (NoSuchFieldError var2) {
            }

            try {
               var0[EnumFacing.WEST.ordinal()] = 5;
            } catch (NoSuchFieldError var1) {
            }

            $SWITCH_TABLE$net$minecraft$util$EnumFacing = var0;
            return var0;
         }
      }
   }
}
