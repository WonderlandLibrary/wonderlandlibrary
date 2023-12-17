// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.text.translation.I18n;
import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.world.biome.Biome;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.Chunk;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.BlockStone;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import com.google.common.collect.HashMultiset;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import javax.annotation.Nullable;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.MapData;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class ItemMap extends ItemMapBase
{
    protected ItemMap() {
        this.setHasSubtypes(true);
    }
    
    public static ItemStack setupNewMap(final World worldIn, final double worldX, final double worldZ, final byte scale, final boolean trackingPosition, final boolean unlimitedTracking) {
        final ItemStack itemstack = new ItemStack(Items.FILLED_MAP, 1, worldIn.getUniqueDataId("map"));
        final String s = "map_" + itemstack.getMetadata();
        final MapData mapdata = new MapData(s);
        worldIn.setData(s, mapdata);
        mapdata.calculateMapCenter(worldX, worldZ, mapdata.scale = scale);
        mapdata.dimension = (byte)worldIn.provider.getDimensionType().getId();
        mapdata.trackingPosition = trackingPosition;
        mapdata.unlimitedTracking = unlimitedTracking;
        mapdata.markDirty();
        return itemstack;
    }
    
    @Nullable
    public static MapData loadMapData(final int mapId, final World worldIn) {
        final String s = "map_" + mapId;
        return (MapData)worldIn.loadData(MapData.class, s);
    }
    
    @Nullable
    public MapData getMapData(final ItemStack stack, final World worldIn) {
        String s = "map_" + stack.getMetadata();
        MapData mapdata = (MapData)worldIn.loadData(MapData.class, s);
        if (mapdata == null && !worldIn.isRemote) {
            stack.setItemDamage(worldIn.getUniqueDataId("map"));
            s = "map_" + stack.getMetadata();
            mapdata = new MapData(s);
            mapdata.scale = 3;
            mapdata.calculateMapCenter(worldIn.getWorldInfo().getSpawnX(), worldIn.getWorldInfo().getSpawnZ(), mapdata.scale);
            mapdata.dimension = (byte)worldIn.provider.getDimensionType().getId();
            mapdata.markDirty();
            worldIn.setData(s, mapdata);
        }
        return mapdata;
    }
    
    public void updateMapData(final World worldIn, final Entity viewer, final MapData data) {
        if (worldIn.provider.getDimensionType().getId() == data.dimension && viewer instanceof EntityPlayer) {
            final int i = 1 << data.scale;
            final int j = data.xCenter;
            final int k = data.zCenter;
            final int l = MathHelper.floor(viewer.posX - j) / i + 64;
            final int i2 = MathHelper.floor(viewer.posZ - k) / i + 64;
            int j2 = 128 / i;
            if (worldIn.provider.isNether()) {
                j2 /= 2;
            }
            final MapData.MapInfo mapInfo;
            final MapData.MapInfo mapdata$mapinfo = mapInfo = data.getMapInfo((EntityPlayer)viewer);
            ++mapInfo.step;
            boolean flag = false;
            for (int k2 = l - j2 + 1; k2 < l + j2; ++k2) {
                if ((k2 & 0xF) == (mapdata$mapinfo.step & 0xF) || flag) {
                    flag = false;
                    double d0 = 0.0;
                    for (int l2 = i2 - j2 - 1; l2 < i2 + j2; ++l2) {
                        if (k2 >= 0 && l2 >= -1 && k2 < 128 && l2 < 128) {
                            final int i3 = k2 - l;
                            final int j3 = l2 - i2;
                            final boolean flag2 = i3 * i3 + j3 * j3 > (j2 - 2) * (j2 - 2);
                            final int k3 = (j / i + k2 - 64) * i;
                            final int l3 = (k / i + l2 - 64) * i;
                            final Multiset<MapColor> multiset = (Multiset<MapColor>)HashMultiset.create();
                            final Chunk chunk = worldIn.getChunk(new BlockPos(k3, 0, l3));
                            if (!chunk.isEmpty()) {
                                final int i4 = k3 & 0xF;
                                final int j4 = l3 & 0xF;
                                int k4 = 0;
                                double d2 = 0.0;
                                if (worldIn.provider.isNether()) {
                                    int l4 = k3 + l3 * 231871;
                                    l4 = l4 * l4 * 31287121 + l4 * 11;
                                    if ((l4 >> 20 & 0x1) == 0x0) {
                                        multiset.add((Object)Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT).getMapColor(worldIn, BlockPos.ORIGIN), 10);
                                    }
                                    else {
                                        multiset.add((Object)Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE).getMapColor(worldIn, BlockPos.ORIGIN), 100);
                                    }
                                    d2 = 100.0;
                                }
                                else {
                                    final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                                    for (int i5 = 0; i5 < i; ++i5) {
                                        for (int j5 = 0; j5 < i; ++j5) {
                                            int k5 = chunk.getHeightValue(i5 + i4, j5 + j4) + 1;
                                            IBlockState iblockstate = Blocks.AIR.getDefaultState();
                                            if (k5 <= 1) {
                                                iblockstate = Blocks.BEDROCK.getDefaultState();
                                            }
                                            else {
                                                do {
                                                    --k5;
                                                    iblockstate = chunk.getBlockState(i5 + i4, k5, j5 + j4);
                                                    blockpos$mutableblockpos.setPos((chunk.x << 4) + i5 + i4, k5, (chunk.z << 4) + j5 + j4);
                                                } while (iblockstate.getMapColor(worldIn, blockpos$mutableblockpos) == MapColor.AIR && k5 > 0);
                                                if (k5 > 0 && iblockstate.getMaterial().isLiquid()) {
                                                    int l5 = k5 - 1;
                                                    IBlockState iblockstate2;
                                                    do {
                                                        iblockstate2 = chunk.getBlockState(i5 + i4, l5--, j5 + j4);
                                                        ++k4;
                                                        if (l5 <= 0) {
                                                            break;
                                                        }
                                                    } while (iblockstate2.getMaterial().isLiquid());
                                                }
                                            }
                                            d2 += k5 / (double)(i * i);
                                            multiset.add((Object)iblockstate.getMapColor(worldIn, blockpos$mutableblockpos));
                                        }
                                    }
                                }
                                k4 /= i * i;
                                double d3 = (d2 - d0) * 4.0 / (i + 4) + ((k2 + l2 & 0x1) - 0.5) * 0.4;
                                int i6 = 1;
                                if (d3 > 0.6) {
                                    i6 = 2;
                                }
                                if (d3 < -0.6) {
                                    i6 = 0;
                                }
                                final MapColor mapcolor = (MapColor)Iterables.getFirst((Iterable)Multisets.copyHighestCountFirst((Multiset)multiset), (Object)MapColor.AIR);
                                if (mapcolor == MapColor.WATER) {
                                    d3 = k4 * 0.1 + (k2 + l2 & 0x1) * 0.2;
                                    i6 = 1;
                                    if (d3 < 0.5) {
                                        i6 = 2;
                                    }
                                    if (d3 > 0.9) {
                                        i6 = 0;
                                    }
                                }
                                d0 = d2;
                                if (l2 >= 0 && i3 * i3 + j3 * j3 < j2 * j2 && (!flag2 || (k2 + l2 & 0x1) != 0x0)) {
                                    final byte b0 = data.colors[k2 + l2 * 128];
                                    final byte b2 = (byte)(mapcolor.colorIndex * 4 + i6);
                                    if (b0 != b2) {
                                        data.colors[k2 + l2 * 128] = b2;
                                        data.updateMapData(k2, l2);
                                        flag = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static void renderBiomePreviewMap(final World worldIn, final ItemStack map) {
        if (map.getItem() == Items.FILLED_MAP) {
            final MapData mapdata = Items.FILLED_MAP.getMapData(map, worldIn);
            if (mapdata != null && worldIn.provider.getDimensionType().getId() == mapdata.dimension) {
                final int i = 1 << mapdata.scale;
                final int j = mapdata.xCenter;
                final int k = mapdata.zCenter;
                final Biome[] abiome = worldIn.getBiomeProvider().getBiomes(null, (j / i - 64) * i, (k / i - 64) * i, 128 * i, 128 * i, false);
                for (int l = 0; l < 128; ++l) {
                    for (int i2 = 0; i2 < 128; ++i2) {
                        final int j2 = l * i;
                        final int k2 = i2 * i;
                        final Biome biome = abiome[j2 + k2 * 128 * i];
                        MapColor mapcolor = MapColor.AIR;
                        int l2 = 3;
                        int i3 = 8;
                        if (l > 0 && i2 > 0 && l < 127 && i2 < 127) {
                            if (abiome[(l - 1) * i + (i2 - 1) * i * 128 * i].getBaseHeight() >= 0.0f) {
                                --i3;
                            }
                            if (abiome[(l - 1) * i + (i2 + 1) * i * 128 * i].getBaseHeight() >= 0.0f) {
                                --i3;
                            }
                            if (abiome[(l - 1) * i + i2 * i * 128 * i].getBaseHeight() >= 0.0f) {
                                --i3;
                            }
                            if (abiome[(l + 1) * i + (i2 - 1) * i * 128 * i].getBaseHeight() >= 0.0f) {
                                --i3;
                            }
                            if (abiome[(l + 1) * i + (i2 + 1) * i * 128 * i].getBaseHeight() >= 0.0f) {
                                --i3;
                            }
                            if (abiome[(l + 1) * i + i2 * i * 128 * i].getBaseHeight() >= 0.0f) {
                                --i3;
                            }
                            if (abiome[l * i + (i2 - 1) * i * 128 * i].getBaseHeight() >= 0.0f) {
                                --i3;
                            }
                            if (abiome[l * i + (i2 + 1) * i * 128 * i].getBaseHeight() >= 0.0f) {
                                --i3;
                            }
                            if (biome.getBaseHeight() < 0.0f) {
                                mapcolor = MapColor.ADOBE;
                                if (i3 > 7 && i2 % 2 == 0) {
                                    l2 = (l + (int)(MathHelper.sin(i2 + 0.0f) * 7.0f)) / 8 % 5;
                                    if (l2 == 3) {
                                        l2 = 1;
                                    }
                                    else if (l2 == 4) {
                                        l2 = 0;
                                    }
                                }
                                else if (i3 > 7) {
                                    mapcolor = MapColor.AIR;
                                }
                                else if (i3 > 5) {
                                    l2 = 1;
                                }
                                else if (i3 > 3) {
                                    l2 = 0;
                                }
                                else if (i3 > 1) {
                                    l2 = 0;
                                }
                            }
                            else if (i3 > 0) {
                                mapcolor = MapColor.BROWN;
                                if (i3 > 3) {
                                    l2 = 1;
                                }
                                else {
                                    l2 = 3;
                                }
                            }
                        }
                        if (mapcolor != MapColor.AIR) {
                            mapdata.colors[l + i2 * 128] = (byte)(mapcolor.colorIndex * 4 + l2);
                            mapdata.updateMapData(l, i2);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void onUpdate(final ItemStack stack, final World worldIn, final Entity entityIn, final int itemSlot, final boolean isSelected) {
        if (!worldIn.isRemote) {
            final MapData mapdata = this.getMapData(stack, worldIn);
            if (entityIn instanceof EntityPlayer) {
                final EntityPlayer entityplayer = (EntityPlayer)entityIn;
                mapdata.updateVisiblePlayers(entityplayer, stack);
            }
            if (isSelected || (entityIn instanceof EntityPlayer && ((EntityPlayer)entityIn).getHeldItemOffhand() == stack)) {
                this.updateMapData(worldIn, entityIn, mapdata);
            }
        }
    }
    
    @Nullable
    @Override
    public Packet<?> createMapDataPacket(final ItemStack stack, final World worldIn, final EntityPlayer player) {
        return this.getMapData(stack, worldIn).getMapPacket(stack, worldIn, player);
    }
    
    @Override
    public void onCreated(final ItemStack stack, final World worldIn, final EntityPlayer playerIn) {
        final NBTTagCompound nbttagcompound = stack.getTagCompound();
        if (nbttagcompound != null) {
            if (nbttagcompound.hasKey("map_scale_direction", 99)) {
                scaleMap(stack, worldIn, nbttagcompound.getInteger("map_scale_direction"));
                nbttagcompound.removeTag("map_scale_direction");
            }
            else if (nbttagcompound.getBoolean("map_tracking_position")) {
                enableMapTracking(stack, worldIn);
                nbttagcompound.removeTag("map_tracking_position");
            }
        }
    }
    
    protected static void scaleMap(final ItemStack p_185063_0_, final World p_185063_1_, final int p_185063_2_) {
        final MapData mapdata = Items.FILLED_MAP.getMapData(p_185063_0_, p_185063_1_);
        p_185063_0_.setItemDamage(p_185063_1_.getUniqueDataId("map"));
        final MapData mapdata2 = new MapData("map_" + p_185063_0_.getMetadata());
        if (mapdata != null) {
            mapdata2.scale = (byte)MathHelper.clamp(mapdata.scale + p_185063_2_, 0, 4);
            mapdata2.trackingPosition = mapdata.trackingPosition;
            mapdata2.calculateMapCenter(mapdata.xCenter, mapdata.zCenter, mapdata2.scale);
            mapdata2.dimension = mapdata.dimension;
            mapdata2.markDirty();
            p_185063_1_.setData("map_" + p_185063_0_.getMetadata(), mapdata2);
        }
    }
    
    protected static void enableMapTracking(final ItemStack p_185064_0_, final World p_185064_1_) {
        final MapData mapdata = Items.FILLED_MAP.getMapData(p_185064_0_, p_185064_1_);
        p_185064_0_.setItemDamage(p_185064_1_.getUniqueDataId("map"));
        final MapData mapdata2 = new MapData("map_" + p_185064_0_.getMetadata());
        mapdata2.trackingPosition = true;
        if (mapdata != null) {
            mapdata2.xCenter = mapdata.xCenter;
            mapdata2.zCenter = mapdata.zCenter;
            mapdata2.scale = mapdata.scale;
            mapdata2.dimension = mapdata.dimension;
            mapdata2.markDirty();
            p_185064_1_.setData("map_" + p_185064_0_.getMetadata(), mapdata2);
        }
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
        if (flagIn.isAdvanced()) {
            final MapData mapdata = (worldIn == null) ? null : this.getMapData(stack, worldIn);
            if (mapdata != null) {
                tooltip.add(I18n.translateToLocalFormatted("filled_map.scale", 1 << mapdata.scale));
                tooltip.add(I18n.translateToLocalFormatted("filled_map.level", mapdata.scale, 4));
            }
            else {
                tooltip.add(I18n.translateToLocal("filled_map.unknown"));
            }
        }
    }
    
    public static int getColor(final ItemStack p_190907_0_) {
        final NBTTagCompound nbttagcompound = p_190907_0_.getSubCompound("display");
        if (nbttagcompound != null && nbttagcompound.hasKey("MapColor", 99)) {
            final int i = nbttagcompound.getInteger("MapColor");
            return 0xFF000000 | (i & 0xFFFFFF);
        }
        return -12173266;
    }
}
