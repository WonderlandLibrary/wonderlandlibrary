/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultiset
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Multiset
 *  com.google.common.collect.Multisets
 */
package net.minecraft.item;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;

public class ItemMap
extends ItemMapBase {
    public MapData getMapData(ItemStack itemStack, World world) {
        String string = "map_" + itemStack.getMetadata();
        MapData mapData = (MapData)world.loadItemData(MapData.class, string);
        if (mapData == null && !world.isRemote) {
            itemStack.setItemDamage(world.getUniqueDataId("map"));
            string = "map_" + itemStack.getMetadata();
            mapData = new MapData(string);
            mapData.scale = (byte)3;
            mapData.calculateMapCenter(world.getWorldInfo().getSpawnX(), world.getWorldInfo().getSpawnZ(), mapData.scale);
            mapData.dimension = (byte)world.provider.getDimensionId();
            mapData.markDirty();
            world.setItemData(string, mapData);
        }
        return mapData;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean bl) {
        MapData mapData = this.getMapData(itemStack, entityPlayer.worldObj);
        if (bl) {
            if (mapData == null) {
                list.add("Unknown map");
            } else {
                list.add("Scaling at 1:" + (1 << mapData.scale));
                list.add("(Level " + mapData.scale + "/" + 4 + ")");
            }
        }
    }

    @Override
    public Packet createMapDataPacket(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        return this.getMapData(itemStack, world).getMapPacket(itemStack, world, entityPlayer);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int n, boolean bl) {
        if (!world.isRemote) {
            MapData mapData = this.getMapData(itemStack, world);
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer)entity;
                mapData.updateVisiblePlayers(entityPlayer, itemStack);
            }
            if (bl) {
                this.updateMapData(world, entity, mapData);
            }
        }
    }

    public void updateMapData(World world, Entity entity, MapData mapData) {
        if (world.provider.getDimensionId() == mapData.dimension && entity instanceof EntityPlayer) {
            int n = 1 << mapData.scale;
            int n2 = mapData.xCenter;
            int n3 = mapData.zCenter;
            int n4 = MathHelper.floor_double(entity.posX - (double)n2) / n + 64;
            int n5 = MathHelper.floor_double(entity.posZ - (double)n3) / n + 64;
            int n6 = 128 / n;
            if (world.provider.getHasNoSky()) {
                n6 /= 2;
            }
            MapData.MapInfo mapInfo = mapData.getMapInfo((EntityPlayer)entity);
            ++mapInfo.field_82569_d;
            boolean bl = false;
            int n7 = n4 - n6 + 1;
            while (n7 < n4 + n6) {
                if ((n7 & 0xF) == (mapInfo.field_82569_d & 0xF) || bl) {
                    bl = false;
                    double d = 0.0;
                    int n8 = n5 - n6 - 1;
                    while (n8 < n5 + n6) {
                        if (n7 >= 0 && n8 >= -1 && n7 < 128 && n8 < 128) {
                            int n9 = n7 - n4;
                            int n10 = n8 - n5;
                            boolean bl2 = n9 * n9 + n10 * n10 > (n6 - 2) * (n6 - 2);
                            int n11 = (n2 / n + n7 - 64) * n;
                            int n12 = (n3 / n + n8 - 64) * n;
                            HashMultiset hashMultiset = HashMultiset.create();
                            Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(n11, 0, n12));
                            if (!chunk.isEmpty()) {
                                byte by;
                                MapColor mapColor;
                                int n13;
                                int n14;
                                int n15 = n11 & 0xF;
                                int n16 = n12 & 0xF;
                                int n17 = 0;
                                double d2 = 0.0;
                                if (world.provider.getHasNoSky()) {
                                    int n18 = n11 + n12 * 231871;
                                    if (((n18 = n18 * n18 * 31287121 + n18 * 11) >> 20 & 1) == 0) {
                                        hashMultiset.add((Object)Blocks.dirt.getMapColor(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT)), 10);
                                    } else {
                                        hashMultiset.add((Object)Blocks.stone.getMapColor(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE)), 100);
                                    }
                                    d2 = 100.0;
                                } else {
                                    BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                                    int n19 = 0;
                                    while (n19 < n) {
                                        n14 = 0;
                                        while (n14 < n) {
                                            int n20 = chunk.getHeightValue(n19 + n15, n14 + n16) + 1;
                                            IBlockState iBlockState = Blocks.air.getDefaultState();
                                            if (n20 > 1) {
                                                while ((iBlockState = chunk.getBlockState(mutableBlockPos.func_181079_c(n19 + n15, --n20, n14 + n16))).getBlock().getMapColor(iBlockState) == MapColor.airColor && n20 > 0) {
                                                }
                                                if (n20 > 0 && iBlockState.getBlock().getMaterial().isLiquid()) {
                                                    Block block;
                                                    n13 = n20 - 1;
                                                    do {
                                                        block = chunk.getBlock(n19 + n15, n13--, n14 + n16);
                                                        ++n17;
                                                    } while (n13 > 0 && block.getMaterial().isLiquid());
                                                }
                                            }
                                            d2 += (double)n20 / (double)(n * n);
                                            hashMultiset.add((Object)iBlockState.getBlock().getMapColor(iBlockState));
                                            ++n14;
                                        }
                                        ++n19;
                                    }
                                }
                                n17 /= n * n;
                                double d3 = (d2 - d) * 4.0 / (double)(n + 4) + ((double)(n7 + n8 & 1) - 0.5) * 0.4;
                                n14 = 1;
                                if (d3 > 0.6) {
                                    n14 = 2;
                                }
                                if (d3 < -0.6) {
                                    n14 = 0;
                                }
                                if ((mapColor = (MapColor)Iterables.getFirst((Iterable)Multisets.copyHighestCountFirst((Multiset)hashMultiset), (Object)MapColor.airColor)) == MapColor.waterColor) {
                                    d3 = (double)n17 * 0.1 + (double)(n7 + n8 & 1) * 0.2;
                                    n14 = 1;
                                    if (d3 < 0.5) {
                                        n14 = 2;
                                    }
                                    if (d3 > 0.9) {
                                        n14 = 0;
                                    }
                                }
                                d = d2;
                                if (!(n8 < 0 || n9 * n9 + n10 * n10 >= n6 * n6 || bl2 && (n7 + n8 & 1) == 0 || (by = mapData.colors[n7 + n8 * 128]) == (n13 = (int)((byte)(mapColor.colorIndex * 4 + n14))))) {
                                    mapData.colors[n7 + n8 * 128] = n13;
                                    mapData.updateMapData(n7, n8);
                                    bl = true;
                                }
                            }
                        }
                        ++n8;
                    }
                }
                ++n7;
            }
        }
    }

    public static MapData loadMapData(int n, World world) {
        String string = "map_" + n;
        MapData mapData = (MapData)world.loadItemData(MapData.class, string);
        if (mapData == null) {
            mapData = new MapData(string);
            world.setItemData(string, mapData);
        }
        return mapData;
    }

    protected ItemMap() {
        this.setHasSubtypes(true);
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if (itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("map_is_scaling")) {
            MapData mapData = Items.filled_map.getMapData(itemStack, world);
            itemStack.setItemDamage(world.getUniqueDataId("map"));
            MapData mapData2 = new MapData("map_" + itemStack.getMetadata());
            mapData2.scale = (byte)(mapData.scale + 1);
            if (mapData2.scale > 4) {
                mapData2.scale = (byte)4;
            }
            mapData2.calculateMapCenter(mapData.xCenter, mapData.zCenter, mapData2.scale);
            mapData2.dimension = mapData.dimension;
            mapData2.markDirty();
            world.setItemData("map_" + itemStack.getMetadata(), mapData2);
        }
    }
}

