/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multisets;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;

public class ItemMap
extends ItemMapBase {
    protected ItemMap() {
        this.setHasSubtypes(true);
    }

    public static ItemStack func_190906_a(World p_190906_0_, double p_190906_1_, double p_190906_3_, byte p_190906_5_, boolean p_190906_6_, boolean p_190906_7_) {
        ItemStack itemstack = new ItemStack(Items.FILLED_MAP, 1, p_190906_0_.getUniqueDataId("map"));
        String s = "map_" + itemstack.getMetadata();
        MapData mapdata = new MapData(s);
        p_190906_0_.setItemData(s, mapdata);
        mapdata.scale = p_190906_5_;
        mapdata.calculateMapCenter(p_190906_1_, p_190906_3_, mapdata.scale);
        mapdata.dimension = (byte)p_190906_0_.provider.getDimensionType().getId();
        mapdata.trackingPosition = p_190906_6_;
        mapdata.field_191096_f = p_190906_7_;
        mapdata.markDirty();
        return itemstack;
    }

    @Nullable
    public static MapData loadMapData(int mapId, World worldIn) {
        String s = "map_" + mapId;
        return (MapData)worldIn.loadItemData(MapData.class, s);
    }

    @Nullable
    public MapData getMapData(ItemStack stack, World worldIn) {
        String s = "map_" + stack.getMetadata();
        MapData mapdata = (MapData)worldIn.loadItemData(MapData.class, s);
        if (mapdata == null && !worldIn.isRemote) {
            stack.setItemDamage(worldIn.getUniqueDataId("map"));
            s = "map_" + stack.getMetadata();
            mapdata = new MapData(s);
            mapdata.scale = (byte)3;
            mapdata.calculateMapCenter(worldIn.getWorldInfo().getSpawnX(), worldIn.getWorldInfo().getSpawnZ(), mapdata.scale);
            mapdata.dimension = (byte)worldIn.provider.getDimensionType().getId();
            mapdata.markDirty();
            worldIn.setItemData(s, mapdata);
        }
        return mapdata;
    }

    public void updateMapData(World worldIn, Entity viewer, MapData data) {
        if (worldIn.provider.getDimensionType().getId() == data.dimension && viewer instanceof EntityPlayer) {
            int i = 1 << data.scale;
            int j = data.xCenter;
            int k = data.zCenter;
            int l = MathHelper.floor(viewer.posX - (double)j) / i + 64;
            int i1 = MathHelper.floor(viewer.posZ - (double)k) / i + 64;
            int j1 = 128 / i;
            if (worldIn.provider.getHasNoSky()) {
                j1 /= 2;
            }
            MapData.MapInfo mapdata$mapinfo = data.getMapInfo((EntityPlayer)viewer);
            ++mapdata$mapinfo.step;
            boolean flag = false;
            for (int k1 = l - j1 + 1; k1 < l + j1; ++k1) {
                if ((k1 & 0xF) != (mapdata$mapinfo.step & 0xF) && !flag) continue;
                flag = false;
                double d0 = 0.0;
                for (int l1 = i1 - j1 - 1; l1 < i1 + j1; ++l1) {
                    byte b1;
                    byte b0;
                    MapColor mapcolor;
                    if (k1 < 0 || l1 < -1 || k1 >= 128 || l1 >= 128) continue;
                    int i2 = k1 - l;
                    int j2 = l1 - i1;
                    boolean flag1 = i2 * i2 + j2 * j2 > (j1 - 2) * (j1 - 2);
                    int k2 = (j / i + k1 - 64) * i;
                    int l2 = (k / i + l1 - 64) * i;
                    HashMultiset<MapColor> multiset = HashMultiset.create();
                    Chunk chunk = worldIn.getChunk(new BlockPos(k2, 0, l2));
                    if (chunk.isEmpty()) continue;
                    int i3 = k2 & 0xF;
                    int j3 = l2 & 0xF;
                    int k3 = 0;
                    double d1 = 0.0;
                    if (worldIn.provider.getHasNoSky()) {
                        int l3 = k2 + l2 * 231871;
                        if (((l3 = l3 * l3 * 31287121 + l3 * 11) >> 20 & 1) == 0) {
                            multiset.add(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT).getMapColor(worldIn, BlockPos.ORIGIN), 10);
                        } else {
                            multiset.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE).getMapColor(worldIn, BlockPos.ORIGIN), 100);
                        }
                        d1 = 100.0;
                    } else {
                        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                        for (int i4 = 0; i4 < i; ++i4) {
                            for (int j4 = 0; j4 < i; ++j4) {
                                int k4 = chunk.getHeightValue(i4 + i3, j4 + j3) + 1;
                                IBlockState iblockstate = Blocks.AIR.getDefaultState();
                                if (k4 <= 1) {
                                    iblockstate = Blocks.BEDROCK.getDefaultState();
                                } else {
                                    do {
                                        iblockstate = chunk.getBlockState(i4 + i3, --k4, j4 + j3);
                                        blockpos$mutableblockpos.setPos((chunk.x << 4) + i4 + i3, k4, (chunk.z << 4) + j4 + j3);
                                    } while (iblockstate.getMapColor(worldIn, blockpos$mutableblockpos) == MapColor.AIR && k4 > 0);
                                    if (k4 > 0 && iblockstate.getMaterial().isLiquid()) {
                                        IBlockState iblockstate1;
                                        int l4 = k4 - 1;
                                        do {
                                            iblockstate1 = chunk.getBlockState(i4 + i3, l4--, j4 + j3);
                                            ++k3;
                                        } while (l4 > 0 && iblockstate1.getMaterial().isLiquid());
                                    }
                                }
                                d1 += (double)k4 / (double)(i * i);
                                multiset.add(iblockstate.getMapColor(worldIn, blockpos$mutableblockpos));
                            }
                        }
                    }
                    k3 /= i * i;
                    double d2 = (d1 - d0) * 4.0 / (double)(i + 4) + ((double)(k1 + l1 & 1) - 0.5) * 0.4;
                    int i5 = 1;
                    if (d2 > 0.6) {
                        i5 = 2;
                    }
                    if (d2 < -0.6) {
                        i5 = 0;
                    }
                    if ((mapcolor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.AIR)) == MapColor.WATER) {
                        d2 = (double)k3 * 0.1 + (double)(k1 + l1 & 1) * 0.2;
                        i5 = 1;
                        if (d2 < 0.5) {
                            i5 = 2;
                        }
                        if (d2 > 0.9) {
                            i5 = 0;
                        }
                    }
                    d0 = d1;
                    if (l1 < 0 || i2 * i2 + j2 * j2 >= j1 * j1 || flag1 && (k1 + l1 & 1) == 0 || (b0 = data.colors[k1 + l1 * 128]) == (b1 = (byte)(mapcolor.colorIndex * 4 + i5))) continue;
                    data.colors[k1 + l1 * 128] = b1;
                    data.updateMapData(k1, l1);
                    flag = true;
                }
            }
        }
    }

    public static void func_190905_a(World p_190905_0_, ItemStack p_190905_1_) {
        MapData mapdata;
        if (p_190905_1_.getItem() == Items.FILLED_MAP && (mapdata = Items.FILLED_MAP.getMapData(p_190905_1_, p_190905_0_)) != null && p_190905_0_.provider.getDimensionType().getId() == mapdata.dimension) {
            int i = 1 << mapdata.scale;
            int j = mapdata.xCenter;
            int k = mapdata.zCenter;
            Biome[] abiome = p_190905_0_.getBiomeProvider().getBiomes(null, (j / i - 64) * i, (k / i - 64) * i, 128 * i, 128 * i, false);
            for (int l = 0; l < 128; ++l) {
                for (int i1 = 0; i1 < 128; ++i1) {
                    int j1 = l * i;
                    int k1 = i1 * i;
                    Biome biome = abiome[j1 + k1 * 128 * i];
                    MapColor mapcolor = MapColor.AIR;
                    int l1 = 3;
                    int i2 = 8;
                    if (l > 0 && i1 > 0 && l < 127 && i1 < 127) {
                        if (abiome[(l - 1) * i + (i1 - 1) * i * 128 * i].getBaseHeight() >= 0.0f) {
                            --i2;
                        }
                        if (abiome[(l - 1) * i + (i1 + 1) * i * 128 * i].getBaseHeight() >= 0.0f) {
                            --i2;
                        }
                        if (abiome[(l - 1) * i + i1 * i * 128 * i].getBaseHeight() >= 0.0f) {
                            --i2;
                        }
                        if (abiome[(l + 1) * i + (i1 - 1) * i * 128 * i].getBaseHeight() >= 0.0f) {
                            --i2;
                        }
                        if (abiome[(l + 1) * i + (i1 + 1) * i * 128 * i].getBaseHeight() >= 0.0f) {
                            --i2;
                        }
                        if (abiome[(l + 1) * i + i1 * i * 128 * i].getBaseHeight() >= 0.0f) {
                            --i2;
                        }
                        if (abiome[l * i + (i1 - 1) * i * 128 * i].getBaseHeight() >= 0.0f) {
                            --i2;
                        }
                        if (abiome[l * i + (i1 + 1) * i * 128 * i].getBaseHeight() >= 0.0f) {
                            --i2;
                        }
                        if (biome.getBaseHeight() < 0.0f) {
                            mapcolor = MapColor.ADOBE;
                            if (i2 > 7 && i1 % 2 == 0) {
                                l1 = (l + (int)(MathHelper.sin((float)i1 + 0.0f) * 7.0f)) / 8 % 5;
                                if (l1 == 3) {
                                    l1 = 1;
                                } else if (l1 == 4) {
                                    l1 = 0;
                                }
                            } else if (i2 > 7) {
                                mapcolor = MapColor.AIR;
                            } else if (i2 > 5) {
                                l1 = 1;
                            } else if (i2 > 3) {
                                l1 = 0;
                            } else if (i2 > 1) {
                                l1 = 0;
                            }
                        } else if (i2 > 0) {
                            mapcolor = MapColor.BROWN;
                            l1 = i2 > 3 ? 1 : 3;
                        }
                    }
                    if (mapcolor == MapColor.AIR) continue;
                    mapdata.colors[l + i1 * 128] = (byte)(mapcolor.colorIndex * 4 + l1);
                    mapdata.updateMapData(l, i1);
                }
            }
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote) {
            MapData mapdata = this.getMapData(stack, worldIn);
            if (entityIn instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                mapdata.updateVisiblePlayers(entityplayer, stack);
            }
            if (isSelected || entityIn instanceof EntityPlayer && ((EntityPlayer)entityIn).getHeldItemOffhand() == stack) {
                this.updateMapData(worldIn, entityIn, mapdata);
            }
        }
    }

    @Override
    @Nullable
    public Packet<?> createMapDataPacket(ItemStack stack, World worldIn, EntityPlayer player) {
        return this.getMapData(stack, worldIn).getMapPacket(stack, worldIn, player);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        NBTTagCompound nbttagcompound = stack.getTagCompound();
        if (nbttagcompound != null) {
            if (nbttagcompound.hasKey("map_scale_direction", 99)) {
                ItemMap.scaleMap(stack, worldIn, nbttagcompound.getInteger("map_scale_direction"));
                nbttagcompound.removeTag("map_scale_direction");
            } else if (nbttagcompound.getBoolean("map_tracking_position")) {
                ItemMap.enableMapTracking(stack, worldIn);
                nbttagcompound.removeTag("map_tracking_position");
            }
        }
    }

    protected static void scaleMap(ItemStack p_185063_0_, World p_185063_1_, int p_185063_2_) {
        MapData mapdata = Items.FILLED_MAP.getMapData(p_185063_0_, p_185063_1_);
        p_185063_0_.setItemDamage(p_185063_1_.getUniqueDataId("map"));
        MapData mapdata1 = new MapData("map_" + p_185063_0_.getMetadata());
        if (mapdata != null) {
            mapdata1.scale = (byte)MathHelper.clamp(mapdata.scale + p_185063_2_, 0, 4);
            mapdata1.trackingPosition = mapdata.trackingPosition;
            mapdata1.calculateMapCenter(mapdata.xCenter, mapdata.zCenter, mapdata1.scale);
            mapdata1.dimension = mapdata.dimension;
            mapdata1.markDirty();
            p_185063_1_.setItemData("map_" + p_185063_0_.getMetadata(), mapdata1);
        }
    }

    protected static void enableMapTracking(ItemStack p_185064_0_, World p_185064_1_) {
        MapData mapdata = Items.FILLED_MAP.getMapData(p_185064_0_, p_185064_1_);
        p_185064_0_.setItemDamage(p_185064_1_.getUniqueDataId("map"));
        MapData mapdata1 = new MapData("map_" + p_185064_0_.getMetadata());
        mapdata1.trackingPosition = true;
        if (mapdata != null) {
            mapdata1.xCenter = mapdata.xCenter;
            mapdata1.zCenter = mapdata.zCenter;
            mapdata1.scale = mapdata.scale;
            mapdata1.dimension = mapdata.dimension;
            mapdata1.markDirty();
            p_185064_1_.setItemData("map_" + p_185064_0_.getMetadata(), mapdata1);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        if (advanced.func_194127_a()) {
            MapData mapdata;
            MapData mapData = mapdata = playerIn == null ? null : this.getMapData(stack, playerIn);
            if (mapdata != null) {
                tooltip.add(I18n.translateToLocalFormatted("filled_map.scale", 1 << mapdata.scale));
                tooltip.add(I18n.translateToLocalFormatted("filled_map.level", mapdata.scale, 4));
            } else {
                tooltip.add(I18n.translateToLocal("filled_map.unknown"));
            }
        }
    }

    public static int func_190907_h(ItemStack p_190907_0_) {
        NBTTagCompound nbttagcompound = p_190907_0_.getSubCompound("display");
        if (nbttagcompound != null && nbttagcompound.hasKey("MapColor", 99)) {
            int i = nbttagcompound.getInteger("MapColor");
            return 0xFF000000 | i & 0xFFFFFF;
        }
        return -12173266;
    }
}

