// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import java.util.List;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.Chunk;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.BlockStone;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import com.google.common.collect.HashMultiset;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.World;

public class ItemMap extends ItemMapBase
{
    private static final String __OBFID = "CL_00000047";
    
    protected ItemMap() {
        this.setHasSubtypes(true);
    }
    
    public static MapData loadMapData(final int p_150912_0_, final World worldIn) {
        final String var2 = "map_" + p_150912_0_;
        MapData var3 = (MapData)worldIn.loadItemData(MapData.class, var2);
        if (var3 == null) {
            var3 = new MapData(var2);
            worldIn.setItemData(var2, var3);
        }
        return var3;
    }
    
    public MapData getMapData(final ItemStack p_77873_1_, final World worldIn) {
        String var3 = "map_" + p_77873_1_.getMetadata();
        MapData var4 = (MapData)worldIn.loadItemData(MapData.class, var3);
        if (var4 == null && !worldIn.isRemote) {
            p_77873_1_.setItemDamage(worldIn.getUniqueDataId("map"));
            var3 = "map_" + p_77873_1_.getMetadata();
            var4 = new MapData(var3);
            var4.scale = 3;
            var4.func_176054_a(worldIn.getWorldInfo().getSpawnX(), worldIn.getWorldInfo().getSpawnZ(), var4.scale);
            var4.dimension = (byte)worldIn.provider.getDimensionId();
            var4.markDirty();
            worldIn.setItemData(var3, var4);
        }
        return var4;
    }
    
    public void updateMapData(final World worldIn, final Entity p_77872_2_, final MapData p_77872_3_) {
        if (worldIn.provider.getDimensionId() == p_77872_3_.dimension && p_77872_2_ instanceof EntityPlayer) {
            final int var4 = 1 << p_77872_3_.scale;
            final int var5 = p_77872_3_.xCenter;
            final int var6 = p_77872_3_.zCenter;
            final int var7 = MathHelper.floor_double(p_77872_2_.posX - var5) / var4 + 64;
            final int var8 = MathHelper.floor_double(p_77872_2_.posZ - var6) / var4 + 64;
            int var9 = 128 / var4;
            if (worldIn.provider.getHasNoSky()) {
                var9 /= 2;
            }
            final MapData.MapInfo func_82568_a;
            final MapData.MapInfo var10 = func_82568_a = p_77872_3_.func_82568_a((EntityPlayer)p_77872_2_);
            ++func_82568_a.field_82569_d;
            boolean var11 = false;
            for (int var12 = var7 - var9 + 1; var12 < var7 + var9; ++var12) {
                if ((var12 & 0xF) == (var10.field_82569_d & 0xF) || var11) {
                    var11 = false;
                    double var13 = 0.0;
                    for (int var14 = var8 - var9 - 1; var14 < var8 + var9; ++var14) {
                        if (var12 >= 0 && var14 >= -1 && var12 < 128 && var14 < 128) {
                            final int var15 = var12 - var7;
                            final int var16 = var14 - var8;
                            final boolean var17 = var15 * var15 + var16 * var16 > (var9 - 2) * (var9 - 2);
                            final int var18 = (var5 / var4 + var12 - 64) * var4;
                            final int var19 = (var6 / var4 + var14 - 64) * var4;
                            final HashMultiset var20 = HashMultiset.create();
                            final Chunk var21 = worldIn.getChunkFromBlockCoords(new BlockPos(var18, 0, var19));
                            if (!var21.isEmpty()) {
                                final int var22 = var18 & 0xF;
                                final int var23 = var19 & 0xF;
                                int var24 = 0;
                                double var25 = 0.0;
                                if (worldIn.provider.getHasNoSky()) {
                                    int var26 = var18 + var19 * 231871;
                                    var26 = var26 * var26 * 31287121 + var26 * 11;
                                    if ((var26 >> 20 & 0x1) == 0x0) {
                                        var20.add((Object)Blocks.dirt.getMapColor(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT)), 10);
                                    }
                                    else {
                                        var20.add((Object)Blocks.stone.getMapColor(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT_PROP, BlockStone.EnumType.STONE)), 100);
                                    }
                                    var25 = 100.0;
                                }
                                else {
                                    for (int var26 = 0; var26 < var4; ++var26) {
                                        for (int var27 = 0; var27 < var4; ++var27) {
                                            int var28 = var21.getHeight(var26 + var22, var27 + var23) + 1;
                                            IBlockState var29 = Blocks.air.getDefaultState();
                                            if (var28 > 1) {
                                                do {
                                                    --var28;
                                                    var29 = var21.getBlockState(new BlockPos(var26 + var22, var28, var27 + var23));
                                                } while (var29.getBlock().getMapColor(var29) == MapColor.airColor && var28 > 0);
                                                if (var28 > 0 && var29.getBlock().getMaterial().isLiquid()) {
                                                    int var30 = var28 - 1;
                                                    Block var31;
                                                    do {
                                                        var31 = var21.getBlock(var26 + var22, var30--, var27 + var23);
                                                        ++var24;
                                                    } while (var30 > 0 && var31.getMaterial().isLiquid());
                                                }
                                            }
                                            var25 += var28 / (var4 * var4);
                                            var20.add((Object)var29.getBlock().getMapColor(var29));
                                        }
                                    }
                                }
                                var24 /= var4 * var4;
                                double var32 = (var25 - var13) * 4.0 / (var4 + 4) + ((var12 + var14 & 0x1) - 0.5) * 0.4;
                                byte var33 = 1;
                                if (var32 > 0.6) {
                                    var33 = 2;
                                }
                                if (var32 < -0.6) {
                                    var33 = 0;
                                }
                                final MapColor var34 = (MapColor)Iterables.getFirst((Iterable)Multisets.copyHighestCountFirst((Multiset)var20), (Object)MapColor.airColor);
                                if (var34 == MapColor.waterColor) {
                                    var32 = var24 * 0.1 + (var12 + var14 & 0x1) * 0.2;
                                    var33 = 1;
                                    if (var32 < 0.5) {
                                        var33 = 2;
                                    }
                                    if (var32 > 0.9) {
                                        var33 = 0;
                                    }
                                }
                                var13 = var25;
                                if (var14 >= 0 && var15 * var15 + var16 * var16 < var9 * var9 && (!var17 || (var12 + var14 & 0x1) != 0x0)) {
                                    final byte var35 = p_77872_3_.colors[var12 + var14 * 128];
                                    final byte var36 = (byte)(var34.colorIndex * 4 + var33);
                                    if (var35 != var36) {
                                        p_77872_3_.colors[var12 + var14 * 128] = var36;
                                        p_77872_3_.func_176053_a(var12, var14);
                                        var11 = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void onUpdate(final ItemStack stack, final World worldIn, final Entity entityIn, final int itemSlot, final boolean isSelected) {
        if (!worldIn.isRemote) {
            final MapData var6 = this.getMapData(stack, worldIn);
            if (entityIn instanceof EntityPlayer) {
                final EntityPlayer var7 = (EntityPlayer)entityIn;
                var6.updateVisiblePlayers(var7, stack);
            }
            if (isSelected) {
                this.updateMapData(worldIn, entityIn, var6);
            }
        }
    }
    
    @Override
    public Packet createMapDataPacket(final ItemStack p_150911_1_, final World worldIn, final EntityPlayer p_150911_3_) {
        return this.getMapData(p_150911_1_, worldIn).func_176052_a(p_150911_1_, worldIn, p_150911_3_);
    }
    
    @Override
    public void onCreated(final ItemStack stack, final World worldIn, final EntityPlayer playerIn) {
        if (stack.hasTagCompound() && stack.getTagCompound().getBoolean("map_is_scaling")) {
            final MapData var4 = Items.filled_map.getMapData(stack, worldIn);
            stack.setItemDamage(worldIn.getUniqueDataId("map"));
            final MapData var5 = new MapData("map_" + stack.getMetadata());
            var5.scale = (byte)(var4.scale + 1);
            if (var5.scale > 4) {
                var5.scale = 4;
            }
            var5.func_176054_a(var4.xCenter, var4.zCenter, var5.scale);
            var5.dimension = var4.dimension;
            var5.markDirty();
            worldIn.setItemData("map_" + stack.getMetadata(), var5);
        }
    }
    
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List tooltip, final boolean advanced) {
        final MapData var5 = this.getMapData(stack, playerIn.worldObj);
        if (advanced) {
            if (var5 == null) {
                tooltip.add("Unknown map");
            }
            else {
                tooltip.add("Scaling at 1:" + (1 << var5.scale));
                tooltip.add("(Level " + var5.scale + "/" + 4 + ")");
            }
        }
    }
}
