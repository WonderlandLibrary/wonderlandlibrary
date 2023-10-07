// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import net.minecraft.tileentity.TileEntity;
import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class WorldGenDungeons extends WorldGenerator
{
    private static final Logger field_175918_a;
    private static final String[] SPAWNERTYPES;
    private static final List CHESTCONTENT;
    private static final String __OBFID = "CL_00000425";
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        final boolean var4 = true;
        final int var5 = p_180709_2_.nextInt(2) + 2;
        final int var6 = -var5 - 1;
        final int var7 = var5 + 1;
        final boolean var8 = true;
        final boolean var9 = true;
        final int var10 = p_180709_2_.nextInt(2) + 2;
        final int var11 = -var10 - 1;
        final int var12 = var10 + 1;
        int var13 = 0;
        for (int var14 = var6; var14 <= var7; ++var14) {
            for (int var15 = -1; var15 <= 4; ++var15) {
                for (int var16 = var11; var16 <= var12; ++var16) {
                    final BlockPos var17 = p_180709_3_.add(var14, var15, var16);
                    final Material var18 = worldIn.getBlockState(var17).getBlock().getMaterial();
                    final boolean var19 = var18.isSolid();
                    if (var15 == -1 && !var19) {
                        return false;
                    }
                    if (var15 == 4 && !var19) {
                        return false;
                    }
                    if ((var14 == var6 || var14 == var7 || var16 == var11 || var16 == var12) && var15 == 0 && worldIn.isAirBlock(var17) && worldIn.isAirBlock(var17.offsetUp())) {
                        ++var13;
                    }
                }
            }
        }
        if (var13 >= 1 && var13 <= 5) {
            for (int var14 = var6; var14 <= var7; ++var14) {
                for (int var15 = 3; var15 >= -1; --var15) {
                    for (int var16 = var11; var16 <= var12; ++var16) {
                        final BlockPos var17 = p_180709_3_.add(var14, var15, var16);
                        if (var14 != var6 && var15 != -1 && var16 != var11 && var14 != var7 && var15 != 4 && var16 != var12) {
                            if (worldIn.getBlockState(var17).getBlock() != Blocks.chest) {
                                worldIn.setBlockToAir(var17);
                            }
                        }
                        else if (var17.getY() >= 0 && !worldIn.getBlockState(var17.offsetDown()).getBlock().getMaterial().isSolid()) {
                            worldIn.setBlockToAir(var17);
                        }
                        else if (worldIn.getBlockState(var17).getBlock().getMaterial().isSolid() && worldIn.getBlockState(var17).getBlock() != Blocks.chest) {
                            if (var15 == -1 && p_180709_2_.nextInt(4) != 0) {
                                worldIn.setBlockState(var17, Blocks.mossy_cobblestone.getDefaultState(), 2);
                            }
                            else {
                                worldIn.setBlockState(var17, Blocks.cobblestone.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }
            for (int var14 = 0; var14 < 2; ++var14) {
                for (int var15 = 0; var15 < 3; ++var15) {
                    final int var16 = p_180709_3_.getX() + p_180709_2_.nextInt(var5 * 2 + 1) - var5;
                    final int var20 = p_180709_3_.getY();
                    final int var21 = p_180709_3_.getZ() + p_180709_2_.nextInt(var10 * 2 + 1) - var10;
                    final BlockPos var22 = new BlockPos(var16, var20, var21);
                    if (worldIn.isAirBlock(var22)) {
                        int var23 = 0;
                        for (final EnumFacing var25 : EnumFacing.Plane.HORIZONTAL) {
                            if (worldIn.getBlockState(var22.offset(var25)).getBlock().getMaterial().isSolid()) {
                                ++var23;
                            }
                        }
                        if (var23 == 1) {
                            worldIn.setBlockState(var22, Blocks.chest.func_176458_f(worldIn, var22, Blocks.chest.getDefaultState()), 2);
                            final List var26 = WeightedRandomChestContent.func_177629_a(WorldGenDungeons.CHESTCONTENT, Items.enchanted_book.getRandomEnchantedBook(p_180709_2_));
                            final TileEntity var27 = worldIn.getTileEntity(var22);
                            if (var27 instanceof TileEntityChest) {
                                WeightedRandomChestContent.generateChestContents(p_180709_2_, var26, (IInventory)var27, 8);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            worldIn.setBlockState(p_180709_3_, Blocks.mob_spawner.getDefaultState(), 2);
            final TileEntity var28 = worldIn.getTileEntity(p_180709_3_);
            if (var28 instanceof TileEntityMobSpawner) {
                ((TileEntityMobSpawner)var28).getSpawnerBaseLogic().setEntityName(this.pickMobSpawner(p_180709_2_));
            }
            else {
                WorldGenDungeons.field_175918_a.error("Failed to fetch mob spawner entity at (" + p_180709_3_.getX() + ", " + p_180709_3_.getY() + ", " + p_180709_3_.getZ() + ")");
            }
            return true;
        }
        return false;
    }
    
    private String pickMobSpawner(final Random p_76543_1_) {
        return WorldGenDungeons.SPAWNERTYPES[p_76543_1_.nextInt(WorldGenDungeons.SPAWNERTYPES.length)];
    }
    
    static {
        field_175918_a = LogManager.getLogger();
        SPAWNERTYPES = new String[] { "Skeleton", "Zombie", "Zombie", "Spider" };
        CHESTCONTENT = Lists.newArrayList((Object[])new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 10), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 4, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 1, 10), new WeightedRandomChestContent(Items.wheat, 0, 1, 4, 10), new WeightedRandomChestContent(Items.gunpowder, 0, 1, 4, 10), new WeightedRandomChestContent(Items.string, 0, 1, 4, 10), new WeightedRandomChestContent(Items.bucket, 0, 1, 1, 10), new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1), new WeightedRandomChestContent(Items.redstone, 0, 1, 4, 10), new WeightedRandomChestContent(Items.record_13, 0, 1, 1, 4), new WeightedRandomChestContent(Items.record_cat, 0, 1, 1, 4), new WeightedRandomChestContent(Items.name_tag, 0, 1, 1, 10), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 2), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 5), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1) });
    }
}
