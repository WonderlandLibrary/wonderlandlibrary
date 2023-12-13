// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.block.material.Material;
import net.minecraft.util.Vec3i;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.util.EnumFacing;
import java.util.Random;
import java.util.List;

public class StructureMineshaftPieces
{
    private static final List field_175893_a;
    private static final String __OBFID = "CL_00000444";
    
    public static void registerStructurePieces() {
        MapGenStructureIO.registerStructureComponent(Corridor.class, "MSCorridor");
        MapGenStructureIO.registerStructureComponent(Cross.class, "MSCrossing");
        MapGenStructureIO.registerStructureComponent(Room.class, "MSRoom");
        MapGenStructureIO.registerStructureComponent(Stairs.class, "MSStairs");
    }
    
    private static StructureComponent func_175892_a(final List p_175892_0_, final Random p_175892_1_, final int p_175892_2_, final int p_175892_3_, final int p_175892_4_, final EnumFacing p_175892_5_, final int p_175892_6_) {
        final int var7 = p_175892_1_.nextInt(100);
        if (var7 >= 80) {
            final StructureBoundingBox var8 = Cross.func_175813_a(p_175892_0_, p_175892_1_, p_175892_2_, p_175892_3_, p_175892_4_, p_175892_5_);
            if (var8 != null) {
                return new Cross(p_175892_6_, p_175892_1_, var8, p_175892_5_);
            }
        }
        else if (var7 >= 70) {
            final StructureBoundingBox var8 = Stairs.func_175812_a(p_175892_0_, p_175892_1_, p_175892_2_, p_175892_3_, p_175892_4_, p_175892_5_);
            if (var8 != null) {
                return new Stairs(p_175892_6_, p_175892_1_, var8, p_175892_5_);
            }
        }
        else {
            final StructureBoundingBox var8 = Corridor.func_175814_a(p_175892_0_, p_175892_1_, p_175892_2_, p_175892_3_, p_175892_4_, p_175892_5_);
            if (var8 != null) {
                return new Corridor(p_175892_6_, p_175892_1_, var8, p_175892_5_);
            }
        }
        return null;
    }
    
    private static StructureComponent func_175890_b(final StructureComponent p_175890_0_, final List p_175890_1_, final Random p_175890_2_, final int p_175890_3_, final int p_175890_4_, final int p_175890_5_, final EnumFacing p_175890_6_, final int p_175890_7_) {
        if (p_175890_7_ > 8) {
            return null;
        }
        if (Math.abs(p_175890_3_ - p_175890_0_.getBoundingBox().minX) <= 80 && Math.abs(p_175890_5_ - p_175890_0_.getBoundingBox().minZ) <= 80) {
            final StructureComponent var8 = func_175892_a(p_175890_1_, p_175890_2_, p_175890_3_, p_175890_4_, p_175890_5_, p_175890_6_, p_175890_7_ + 1);
            if (var8 != null) {
                p_175890_1_.add(var8);
                var8.buildComponent(p_175890_0_, p_175890_1_, p_175890_2_);
            }
            return var8;
        }
        return null;
    }
    
    static {
        field_175893_a = Lists.newArrayList((Object[])new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.dye, EnumDyeColor.BLUE.getDyeColorDamage(), 4, 9, 5), new WeightedRandomChestContent(Items.diamond, 0, 1, 2, 3), new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.rail), 0, 4, 8, 1), new WeightedRandomChestContent(Items.melon_seeds, 0, 2, 4, 10), new WeightedRandomChestContent(Items.pumpkin_seeds, 0, 2, 4, 10), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1) });
    }
    
    public static class Corridor extends StructureComponent
    {
        private boolean hasRails;
        private boolean hasSpiders;
        private boolean spawnerPlaced;
        private int sectionCount;
        private static final String __OBFID = "CL_00000445";
        
        public Corridor() {
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound p_143012_1_) {
            p_143012_1_.setBoolean("hr", this.hasRails);
            p_143012_1_.setBoolean("sc", this.hasSpiders);
            p_143012_1_.setBoolean("hps", this.spawnerPlaced);
            p_143012_1_.setInteger("Num", this.sectionCount);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound p_143011_1_) {
            this.hasRails = p_143011_1_.getBoolean("hr");
            this.hasSpiders = p_143011_1_.getBoolean("sc");
            this.spawnerPlaced = p_143011_1_.getBoolean("hps");
            this.sectionCount = p_143011_1_.getInteger("Num");
        }
        
        public Corridor(final int p_i45625_1_, final Random p_i45625_2_, final StructureBoundingBox p_i45625_3_, final EnumFacing p_i45625_4_) {
            super(p_i45625_1_);
            this.coordBaseMode = p_i45625_4_;
            this.boundingBox = p_i45625_3_;
            this.hasRails = (p_i45625_2_.nextInt(3) == 0);
            this.hasSpiders = (!this.hasRails && p_i45625_2_.nextInt(23) == 0);
            if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.SOUTH) {
                this.sectionCount = p_i45625_3_.getXSize() / 5;
            }
            else {
                this.sectionCount = p_i45625_3_.getZSize() / 5;
            }
        }
        
        public static StructureBoundingBox func_175814_a(final List p_175814_0_, final Random p_175814_1_, final int p_175814_2_, final int p_175814_3_, final int p_175814_4_, final EnumFacing p_175814_5_) {
            final StructureBoundingBox var6 = new StructureBoundingBox(p_175814_2_, p_175814_3_, p_175814_4_, p_175814_2_, p_175814_3_ + 2, p_175814_4_);
            int var7;
            for (var7 = p_175814_1_.nextInt(3) + 2; var7 > 0; --var7) {
                final int var8 = var7 * 5;
                switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[p_175814_5_.ordinal()]) {
                    case 1: {
                        var6.maxX = p_175814_2_ + 2;
                        var6.minZ = p_175814_4_ - (var8 - 1);
                        break;
                    }
                    case 2: {
                        var6.maxX = p_175814_2_ + 2;
                        var6.maxZ = p_175814_4_ + (var8 - 1);
                        break;
                    }
                    case 3: {
                        var6.minX = p_175814_2_ - (var8 - 1);
                        var6.maxZ = p_175814_4_ + 2;
                        break;
                    }
                    case 4: {
                        var6.maxX = p_175814_2_ + (var8 - 1);
                        var6.maxZ = p_175814_4_ + 2;
                        break;
                    }
                }
                if (StructureComponent.findIntersecting(p_175814_0_, var6) == null) {
                    break;
                }
            }
            return (var7 > 0) ? var6 : null;
        }
        
        @Override
        public void buildComponent(final StructureComponent p_74861_1_, final List p_74861_2_, final Random p_74861_3_) {
            final int var4 = this.getComponentType();
            final int var5 = p_74861_3_.nextInt(4);
            if (this.coordBaseMode != null) {
                switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[this.coordBaseMode.ordinal()]) {
                    case 1: {
                        if (var5 <= 1) {
                            func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ - 1, this.coordBaseMode, var4);
                            break;
                        }
                        if (var5 == 2) {
                            func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, EnumFacing.WEST, var4);
                            break;
                        }
                        func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, EnumFacing.EAST, var4);
                        break;
                    }
                    case 2: {
                        if (var5 <= 1) {
                            func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ + 1, this.coordBaseMode, var4);
                            break;
                        }
                        if (var5 == 2) {
                            func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.WEST, var4);
                            break;
                        }
                        func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.EAST, var4);
                        break;
                    }
                    case 3: {
                        if (var5 <= 1) {
                            func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, this.coordBaseMode, var4);
                            break;
                        }
                        if (var5 == 2) {
                            func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);
                            break;
                        }
                        func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);
                        break;
                    }
                    case 4: {
                        if (var5 <= 1) {
                            func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, this.coordBaseMode, var4);
                            break;
                        }
                        if (var5 == 2) {
                            func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);
                            break;
                        }
                        func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);
                        break;
                    }
                }
            }
            if (var4 < 8) {
                if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.SOUTH) {
                    for (int var6 = this.boundingBox.minX + 3; var6 + 3 <= this.boundingBox.maxX; var6 += 5) {
                        final int var7 = p_74861_3_.nextInt(5);
                        if (var7 == 0) {
                            func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, var6, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, var4 + 1);
                        }
                        else if (var7 == 1) {
                            func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, var6, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4 + 1);
                        }
                    }
                }
                else {
                    for (int var6 = this.boundingBox.minZ + 3; var6 + 3 <= this.boundingBox.maxZ; var6 += 5) {
                        final int var7 = p_74861_3_.nextInt(5);
                        if (var7 == 0) {
                            func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, var6, EnumFacing.WEST, var4 + 1);
                        }
                        else if (var7 == 1) {
                            func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, var6, EnumFacing.EAST, var4 + 1);
                        }
                    }
                }
            }
        }
        
        @Override
        protected boolean func_180778_a(final World worldIn, final StructureBoundingBox p_180778_2_, final Random p_180778_3_, final int p_180778_4_, final int p_180778_5_, final int p_180778_6_, final List p_180778_7_, final int p_180778_8_) {
            final BlockPos var9 = new BlockPos(this.getXWithOffset(p_180778_4_, p_180778_6_), this.getYWithOffset(p_180778_5_), this.getZWithOffset(p_180778_4_, p_180778_6_));
            if (p_180778_2_.func_175898_b(var9) && worldIn.getBlockState(var9).getBlock().getMaterial() == Material.air) {
                final int var10 = p_180778_3_.nextBoolean() ? 1 : 0;
                worldIn.setBlockState(var9, Blocks.rail.getStateFromMeta(this.getMetadataWithOffset(Blocks.rail, var10)), 2);
                final EntityMinecartChest var11 = new EntityMinecartChest(worldIn, var9.getX() + 0.5f, var9.getY() + 0.5f, var9.getZ() + 0.5f);
                WeightedRandomChestContent.generateChestContents(p_180778_3_, p_180778_7_, var11, p_180778_8_);
                worldIn.spawnEntityInWorld(var11);
                return true;
            }
            return false;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_)) {
                return false;
            }
            final boolean var4 = false;
            final boolean var5 = true;
            final boolean var6 = false;
            final boolean var7 = true;
            final int var8 = this.sectionCount * 5 - 1;
            this.func_175804_a(worldIn, p_74875_3_, 0, 0, 0, 2, 1, var8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175805_a(worldIn, p_74875_3_, p_74875_2_, 0.8f, 0, 2, 0, 2, 2, var8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            if (this.hasSpiders) {
                this.func_175805_a(worldIn, p_74875_3_, p_74875_2_, 0.6f, 0, 0, 0, 2, 1, var8, Blocks.web.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            for (int var9 = 0; var9 < this.sectionCount; ++var9) {
                final int var10 = 2 + var9 * 5;
                this.func_175804_a(worldIn, p_74875_3_, 0, 0, var10, 0, 1, var10, Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, 2, 0, var10, 2, 1, var10, Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);
                if (p_74875_2_.nextInt(4) == 0) {
                    this.func_175804_a(worldIn, p_74875_3_, 0, 2, var10, 0, 2, var10, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, 2, 2, var10, 2, 2, var10, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                }
                else {
                    this.func_175804_a(worldIn, p_74875_3_, 0, 2, var10, 2, 2, var10, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                }
                this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1f, 0, 2, var10 - 1, Blocks.web.getDefaultState());
                this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1f, 2, 2, var10 - 1, Blocks.web.getDefaultState());
                this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1f, 0, 2, var10 + 1, Blocks.web.getDefaultState());
                this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1f, 2, 2, var10 + 1, Blocks.web.getDefaultState());
                this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05f, 0, 2, var10 - 2, Blocks.web.getDefaultState());
                this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05f, 2, 2, var10 - 2, Blocks.web.getDefaultState());
                this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05f, 0, 2, var10 + 2, Blocks.web.getDefaultState());
                this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05f, 2, 2, var10 + 2, Blocks.web.getDefaultState());
                this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05f, 1, 2, var10 - 1, Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));
                this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05f, 1, 2, var10 + 1, Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));
                if (p_74875_2_.nextInt(100) == 0) {
                    this.func_180778_a(worldIn, p_74875_3_, p_74875_2_, 2, 0, var10 - 1, WeightedRandomChestContent.func_177629_a(StructureMineshaftPieces.field_175893_a, Items.enchanted_book.getRandomEnchantedBook(p_74875_2_)), 3 + p_74875_2_.nextInt(4));
                }
                if (p_74875_2_.nextInt(100) == 0) {
                    this.func_180778_a(worldIn, p_74875_3_, p_74875_2_, 0, 0, var10 + 1, WeightedRandomChestContent.func_177629_a(StructureMineshaftPieces.field_175893_a, Items.enchanted_book.getRandomEnchantedBook(p_74875_2_)), 3 + p_74875_2_.nextInt(4));
                }
                if (this.hasSpiders && !this.spawnerPlaced) {
                    final int var11 = this.getYWithOffset(0);
                    int var12 = var10 - 1 + p_74875_2_.nextInt(3);
                    final int var13 = this.getXWithOffset(1, var12);
                    var12 = this.getZWithOffset(1, var12);
                    final BlockPos var14 = new BlockPos(var13, var11, var12);
                    if (p_74875_3_.func_175898_b(var14)) {
                        this.spawnerPlaced = true;
                        worldIn.setBlockState(var14, Blocks.mob_spawner.getDefaultState(), 2);
                        final TileEntity var15 = worldIn.getTileEntity(var14);
                        if (var15 instanceof TileEntityMobSpawner) {
                            ((TileEntityMobSpawner)var15).getSpawnerBaseLogic().setEntityName("CaveSpider");
                        }
                    }
                }
            }
            for (int var9 = 0; var9 <= 2; ++var9) {
                for (int var10 = 0; var10 <= var8; ++var10) {
                    final byte var16 = -1;
                    final IBlockState var17 = this.func_175807_a(worldIn, var9, var16, var10, p_74875_3_);
                    if (var17.getBlock().getMaterial() == Material.air) {
                        final byte var18 = -1;
                        this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), var9, var18, var10, p_74875_3_);
                    }
                }
            }
            if (this.hasRails) {
                for (int var9 = 0; var9 <= var8; ++var9) {
                    final IBlockState var19 = this.func_175807_a(worldIn, 1, -1, var9, p_74875_3_);
                    if (var19.getBlock().getMaterial() != Material.air && var19.getBlock().isFullBlock()) {
                        this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.7f, 1, 0, var9, Blocks.rail.getStateFromMeta(this.getMetadataWithOffset(Blocks.rail, 0)));
                    }
                }
            }
            return true;
        }
    }
    
    public static class Cross extends StructureComponent
    {
        private EnumFacing corridorDirection;
        private boolean isMultipleFloors;
        private static final String __OBFID = "CL_00000446";
        
        public Cross() {
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound p_143012_1_) {
            p_143012_1_.setBoolean("tf", this.isMultipleFloors);
            p_143012_1_.setInteger("D", this.corridorDirection.getHorizontalIndex());
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound p_143011_1_) {
            this.isMultipleFloors = p_143011_1_.getBoolean("tf");
            this.corridorDirection = EnumFacing.getHorizontal(p_143011_1_.getInteger("D"));
        }
        
        public Cross(final int p_i45624_1_, final Random p_i45624_2_, final StructureBoundingBox p_i45624_3_, final EnumFacing p_i45624_4_) {
            super(p_i45624_1_);
            this.corridorDirection = p_i45624_4_;
            this.boundingBox = p_i45624_3_;
            this.isMultipleFloors = (p_i45624_3_.getYSize() > 3);
        }
        
        public static StructureBoundingBox func_175813_a(final List p_175813_0_, final Random p_175813_1_, final int p_175813_2_, final int p_175813_3_, final int p_175813_4_, final EnumFacing p_175813_5_) {
            final StructureBoundingBox var6 = new StructureBoundingBox(p_175813_2_, p_175813_3_, p_175813_4_, p_175813_2_, p_175813_3_ + 2, p_175813_4_);
            if (p_175813_1_.nextInt(4) == 0) {
                final StructureBoundingBox structureBoundingBox = var6;
                structureBoundingBox.maxY += 4;
            }
            switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[p_175813_5_.ordinal()]) {
                case 1: {
                    var6.minX = p_175813_2_ - 1;
                    var6.maxX = p_175813_2_ + 3;
                    var6.minZ = p_175813_4_ - 4;
                    break;
                }
                case 2: {
                    var6.minX = p_175813_2_ - 1;
                    var6.maxX = p_175813_2_ + 3;
                    var6.maxZ = p_175813_4_ + 4;
                    break;
                }
                case 3: {
                    var6.minX = p_175813_2_ - 4;
                    var6.minZ = p_175813_4_ - 1;
                    var6.maxZ = p_175813_4_ + 3;
                    break;
                }
                case 4: {
                    var6.maxX = p_175813_2_ + 4;
                    var6.minZ = p_175813_4_ - 1;
                    var6.maxZ = p_175813_4_ + 3;
                    break;
                }
            }
            return (StructureComponent.findIntersecting(p_175813_0_, var6) != null) ? null : var6;
        }
        
        @Override
        public void buildComponent(final StructureComponent p_74861_1_, final List p_74861_2_, final Random p_74861_3_) {
            final int var4 = this.getComponentType();
            switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[this.corridorDirection.ordinal()]) {
                case 1: {
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, var4);
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, var4);
                    break;
                }
                case 2: {
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, var4);
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, var4);
                    break;
                }
                case 3: {
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, var4);
                    break;
                }
                case 4: {
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, var4);
                    break;
                }
            }
            if (this.isMultipleFloors) {
                if (p_74861_3_.nextBoolean()) {
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);
                }
                if (p_74861_3_.nextBoolean()) {
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.WEST, var4);
                }
                if (p_74861_3_.nextBoolean()) {
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.EAST, var4);
                }
                if (p_74861_3_.nextBoolean()) {
                    func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);
                }
            }
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_)) {
                return false;
            }
            if (this.isMultipleFloors) {
                this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            else {
                this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
            for (int var4 = this.boundingBox.minX; var4 <= this.boundingBox.maxX; ++var4) {
                for (int var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; ++var5) {
                    if (this.func_175807_a(worldIn, var4, this.boundingBox.minY - 1, var5, p_74875_3_).getBlock().getMaterial() == Material.air) {
                        this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), var4, this.boundingBox.minY - 1, var5, p_74875_3_);
                    }
                }
            }
            return true;
        }
    }
    
    public static class Room extends StructureComponent
    {
        private List roomsLinkedToTheRoom;
        private static final String __OBFID = "CL_00000447";
        
        public Room() {
            this.roomsLinkedToTheRoom = Lists.newLinkedList();
        }
        
        public Room(final int p_i2037_1_, final Random p_i2037_2_, final int p_i2037_3_, final int p_i2037_4_) {
            super(p_i2037_1_);
            this.roomsLinkedToTheRoom = Lists.newLinkedList();
            this.boundingBox = new StructureBoundingBox(p_i2037_3_, 50, p_i2037_4_, p_i2037_3_ + 7 + p_i2037_2_.nextInt(6), 54 + p_i2037_2_.nextInt(6), p_i2037_4_ + 7 + p_i2037_2_.nextInt(6));
        }
        
        @Override
        public void buildComponent(final StructureComponent p_74861_1_, final List p_74861_2_, final Random p_74861_3_) {
            final int var4 = this.getComponentType();
            int var5 = this.boundingBox.getYSize() - 3 - 1;
            if (var5 <= 0) {
                var5 = 1;
            }
            for (int var6 = 0; var6 < this.boundingBox.getXSize(); var6 += 4) {
                var6 += p_74861_3_.nextInt(this.boundingBox.getXSize());
                if (var6 + 3 > this.boundingBox.getXSize()) {
                    break;
                }
                final StructureComponent var7 = func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + var6, this.boundingBox.minY + p_74861_3_.nextInt(var5) + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);
                if (var7 != null) {
                    final StructureBoundingBox var8 = var7.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(var8.minX, var8.minY, this.boundingBox.minZ, var8.maxX, var8.maxY, this.boundingBox.minZ + 1));
                }
            }
            for (int var6 = 0; var6 < this.boundingBox.getXSize(); var6 += 4) {
                var6 += p_74861_3_.nextInt(this.boundingBox.getXSize());
                if (var6 + 3 > this.boundingBox.getXSize()) {
                    break;
                }
                final StructureComponent var7 = func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + var6, this.boundingBox.minY + p_74861_3_.nextInt(var5) + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);
                if (var7 != null) {
                    final StructureBoundingBox var8 = var7.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(var8.minX, var8.minY, this.boundingBox.maxZ - 1, var8.maxX, var8.maxY, this.boundingBox.maxZ));
                }
            }
            for (int var6 = 0; var6 < this.boundingBox.getZSize(); var6 += 4) {
                var6 += p_74861_3_.nextInt(this.boundingBox.getZSize());
                if (var6 + 3 > this.boundingBox.getZSize()) {
                    break;
                }
                final StructureComponent var7 = func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74861_3_.nextInt(var5) + 1, this.boundingBox.minZ + var6, EnumFacing.WEST, var4);
                if (var7 != null) {
                    final StructureBoundingBox var8 = var7.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.minX, var8.minY, var8.minZ, this.boundingBox.minX + 1, var8.maxY, var8.maxZ));
                }
            }
            for (int var6 = 0; var6 < this.boundingBox.getZSize(); var6 += 4) {
                var6 += p_74861_3_.nextInt(this.boundingBox.getZSize());
                if (var6 + 3 > this.boundingBox.getZSize()) {
                    break;
                }
                final StructureComponent var7 = func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74861_3_.nextInt(var5) + 1, this.boundingBox.minZ + var6, EnumFacing.EAST, var4);
                if (var7 != null) {
                    final StructureBoundingBox var8 = var7.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.maxX - 1, var8.minY, var8.minZ, this.boundingBox.maxX, var8.maxY, var8.maxZ));
                }
            }
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_)) {
                return false;
            }
            this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, Blocks.dirt.getDefaultState(), Blocks.air.getDefaultState(), true);
            this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ, this.boundingBox.maxX, Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY), this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            for (final StructureBoundingBox var5 : this.roomsLinkedToTheRoom) {
                this.func_175804_a(worldIn, p_74875_3_, var5.minX, var5.maxY - 2, var5.minZ, var5.maxX, var5.maxY, var5.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            this.func_180777_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), false);
            return true;
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound p_143012_1_) {
            final NBTTagList var2 = new NBTTagList();
            for (final StructureBoundingBox var4 : this.roomsLinkedToTheRoom) {
                var2.appendTag(var4.func_151535_h());
            }
            p_143012_1_.setTag("Entrances", var2);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound p_143011_1_) {
            final NBTTagList var2 = p_143011_1_.getTagList("Entrances", 11);
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                this.roomsLinkedToTheRoom.add(new StructureBoundingBox(var2.getIntArray(var3)));
            }
        }
    }
    
    public static class Stairs extends StructureComponent
    {
        private static final String __OBFID = "CL_00000449";
        
        public Stairs() {
        }
        
        public Stairs(final int p_i45623_1_, final Random p_i45623_2_, final StructureBoundingBox p_i45623_3_, final EnumFacing p_i45623_4_) {
            super(p_i45623_1_);
            this.coordBaseMode = p_i45623_4_;
            this.boundingBox = p_i45623_3_;
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound p_143012_1_) {
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound p_143011_1_) {
        }
        
        public static StructureBoundingBox func_175812_a(final List p_175812_0_, final Random p_175812_1_, final int p_175812_2_, final int p_175812_3_, final int p_175812_4_, final EnumFacing p_175812_5_) {
            final StructureBoundingBox var6 = new StructureBoundingBox(p_175812_2_, p_175812_3_ - 5, p_175812_4_, p_175812_2_, p_175812_3_ + 2, p_175812_4_);
            switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[p_175812_5_.ordinal()]) {
                case 1: {
                    var6.maxX = p_175812_2_ + 2;
                    var6.minZ = p_175812_4_ - 8;
                    break;
                }
                case 2: {
                    var6.maxX = p_175812_2_ + 2;
                    var6.maxZ = p_175812_4_ + 8;
                    break;
                }
                case 3: {
                    var6.minX = p_175812_2_ - 8;
                    var6.maxZ = p_175812_4_ + 2;
                    break;
                }
                case 4: {
                    var6.maxX = p_175812_2_ + 8;
                    var6.maxZ = p_175812_4_ + 2;
                    break;
                }
            }
            return (StructureComponent.findIntersecting(p_175812_0_, var6) != null) ? null : var6;
        }
        
        @Override
        public void buildComponent(final StructureComponent p_74861_1_, final List p_74861_2_, final Random p_74861_3_) {
            final int var4 = this.getComponentType();
            if (this.coordBaseMode != null) {
                switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[this.coordBaseMode.ordinal()]) {
                    case 1: {
                        func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);
                        break;
                    }
                    case 2: {
                        func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);
                        break;
                    }
                    case 3: {
                        func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, var4);
                        break;
                    }
                    case 4: {
                        func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, var4);
                        break;
                    }
                }
            }
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_)) {
                return false;
            }
            this.func_175804_a(worldIn, p_74875_3_, 0, 5, 0, 2, 7, 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 0, 7, 2, 2, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            for (int var4 = 0; var4 < 5; ++var4) {
                this.func_175804_a(worldIn, p_74875_3_, 0, 5 - var4 - ((var4 < 4) ? 1 : 0), 2 + var4, 2, 7 - var4, 2 + var4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            return true;
        }
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_175894_a;
        private static final String __OBFID = "CL_00001998";
        
        static {
            field_175894_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_175894_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_175894_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_175894_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_175894_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
