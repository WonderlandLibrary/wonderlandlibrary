/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class StructureVillagePieces {
    public static void registerVillagePieces() {
        MapGenStructureIO.registerStructureComponent(House1.class, "ViBH");
        MapGenStructureIO.registerStructureComponent(Field1.class, "ViDF");
        MapGenStructureIO.registerStructureComponent(Field2.class, "ViF");
        MapGenStructureIO.registerStructureComponent(Torch.class, "ViL");
        MapGenStructureIO.registerStructureComponent(Hall.class, "ViPH");
        MapGenStructureIO.registerStructureComponent(House4Garden.class, "ViSH");
        MapGenStructureIO.registerStructureComponent(WoodHut.class, "ViSmH");
        MapGenStructureIO.registerStructureComponent(Church.class, "ViST");
        MapGenStructureIO.registerStructureComponent(House2.class, "ViS");
        MapGenStructureIO.registerStructureComponent(Start.class, "ViStart");
        MapGenStructureIO.registerStructureComponent(Path.class, "ViSR");
        MapGenStructureIO.registerStructureComponent(House3.class, "ViTRH");
        MapGenStructureIO.registerStructureComponent(Well.class, "ViW");
    }

    private static Village func_176067_c(Start start, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
        int n5 = StructureVillagePieces.func_75079_a(start.structureVillageWeightedPieceList);
        if (n5 <= 0) {
            return null;
        }
        int n6 = 0;
        block0: while (n6 < 5) {
            ++n6;
            int n7 = random.nextInt(n5);
            for (PieceWeight pieceWeight : start.structureVillageWeightedPieceList) {
                if ((n7 -= pieceWeight.villagePieceWeight) >= 0) continue;
                if (!pieceWeight.canSpawnMoreVillagePiecesOfType(n4) || pieceWeight == start.structVillagePieceWeight && start.structureVillageWeightedPieceList.size() > 1) continue block0;
                Village village = StructureVillagePieces.func_176065_a(start, pieceWeight, list, random, n, n2, n3, enumFacing, n4);
                if (village == null) continue;
                ++pieceWeight.villagePiecesSpawned;
                start.structVillagePieceWeight = pieceWeight;
                if (!pieceWeight.canSpawnMoreVillagePieces()) {
                    start.structureVillageWeightedPieceList.remove(pieceWeight);
                }
                return village;
            }
        }
        StructureBoundingBox structureBoundingBox = Torch.func_175856_a(start, list, random, n, n2, n3, enumFacing);
        if (structureBoundingBox != null) {
            return new Torch(start, n4, random, structureBoundingBox, enumFacing);
        }
        return null;
    }

    private static Village func_176065_a(Start start, PieceWeight pieceWeight, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
        Class<? extends Village> clazz = pieceWeight.villagePieceClass;
        Village village = null;
        if (clazz == House4Garden.class) {
            village = House4Garden.func_175858_a(start, list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Church.class) {
            village = Church.func_175854_a(start, list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == House1.class) {
            village = House1.func_175850_a(start, list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == WoodHut.class) {
            village = WoodHut.func_175853_a(start, list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Hall.class) {
            village = Hall.func_175857_a(start, list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Field1.class) {
            village = Field1.func_175851_a(start, list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Field2.class) {
            village = Field2.func_175852_a(start, list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == House2.class) {
            village = House2.func_175855_a(start, list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == House3.class) {
            village = House3.func_175849_a(start, list, random, n, n2, n3, enumFacing, n4);
        }
        return village;
    }

    private static int func_75079_a(List<PieceWeight> list) {
        boolean bl = false;
        int n = 0;
        for (PieceWeight pieceWeight : list) {
            if (pieceWeight.villagePiecesLimit > 0 && pieceWeight.villagePiecesSpawned < pieceWeight.villagePiecesLimit) {
                bl = true;
            }
            n += pieceWeight.villagePieceWeight;
        }
        return bl ? n : -1;
    }

    private static StructureComponent func_176069_e(Start start, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
        if (n4 > 3 + start.terrainType) {
            return null;
        }
        if (Math.abs(n - start.getBoundingBox().minX) <= 112 && Math.abs(n3 - start.getBoundingBox().minZ) <= 112) {
            StructureBoundingBox structureBoundingBox = Path.func_175848_a(start, list, random, n, n2, n3, enumFacing);
            if (structureBoundingBox != null && structureBoundingBox.minY > 10) {
                int n5;
                Path path = new Path(start, n4, random, structureBoundingBox, enumFacing);
                int n6 = (path.boundingBox.minX + path.boundingBox.maxX) / 2;
                int n7 = (path.boundingBox.minZ + path.boundingBox.maxZ) / 2;
                int n8 = path.boundingBox.maxX - path.boundingBox.minX;
                int n9 = path.boundingBox.maxZ - path.boundingBox.minZ;
                int n10 = n5 = n8 > n9 ? n8 : n9;
                if (start.getWorldChunkManager().areBiomesViable(n6, n7, n5 / 2 + 4, MapGenVillage.villageSpawnBiomes)) {
                    list.add(path);
                    start.field_74930_j.add(path);
                    return path;
                }
            }
            return null;
        }
        return null;
    }

    private static StructureComponent func_176066_d(Start start, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
        if (n4 > 50) {
            return null;
        }
        if (Math.abs(n - start.getBoundingBox().minX) <= 112 && Math.abs(n3 - start.getBoundingBox().minZ) <= 112) {
            Village village = StructureVillagePieces.func_176067_c(start, list, random, n, n2, n3, enumFacing, n4 + 1);
            if (village != null) {
                int n5;
                int n6 = (village.boundingBox.minX + village.boundingBox.maxX) / 2;
                int n7 = (village.boundingBox.minZ + village.boundingBox.maxZ) / 2;
                int n8 = village.boundingBox.maxX - village.boundingBox.minX;
                int n9 = village.boundingBox.maxZ - village.boundingBox.minZ;
                int n10 = n5 = n8 > n9 ? n8 : n9;
                if (start.getWorldChunkManager().areBiomesViable(n6, n7, n5 / 2 + 4, MapGenVillage.villageSpawnBiomes)) {
                    list.add(village);
                    start.field_74932_i.add(village);
                    return village;
                }
            }
            return null;
        }
        return null;
    }

    public static List<PieceWeight> getStructureVillageWeightedPieceList(Random random, int n) {
        ArrayList arrayList = Lists.newArrayList();
        arrayList.add(new PieceWeight(House4Garden.class, 4, MathHelper.getRandomIntegerInRange(random, 2 + n, 4 + n * 2)));
        arrayList.add(new PieceWeight(Church.class, 20, MathHelper.getRandomIntegerInRange(random, 0 + n, 1 + n)));
        arrayList.add(new PieceWeight(House1.class, 20, MathHelper.getRandomIntegerInRange(random, 0 + n, 2 + n)));
        arrayList.add(new PieceWeight(WoodHut.class, 3, MathHelper.getRandomIntegerInRange(random, 2 + n, 5 + n * 3)));
        arrayList.add(new PieceWeight(Hall.class, 15, MathHelper.getRandomIntegerInRange(random, 0 + n, 2 + n)));
        arrayList.add(new PieceWeight(Field1.class, 3, MathHelper.getRandomIntegerInRange(random, 1 + n, 4 + n)));
        arrayList.add(new PieceWeight(Field2.class, 3, MathHelper.getRandomIntegerInRange(random, 2 + n, 4 + n * 2)));
        arrayList.add(new PieceWeight(House2.class, 15, MathHelper.getRandomIntegerInRange(random, 0, 1 + n)));
        arrayList.add(new PieceWeight(House3.class, 8, MathHelper.getRandomIntegerInRange(random, 0 + n, 3 + n * 2)));
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            if (((PieceWeight)iterator.next()).villagePiecesLimit != 0) continue;
            iterator.remove();
        }
        return arrayList;
    }

    public static class House2
    extends Village {
        private boolean hasMadeChest;
        private static final List<WeightedRandomChestContent> villageBlacksmithChestContents = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_helmet, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_leggings, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_boots, 0, 1, 1, 5), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.obsidian), 0, 3, 7, 5), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.sapling), 0, 3, 7, 5), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)});

        @Override
        protected int func_180779_c(int n, int n2) {
            return 3;
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(world, structureBoundingBox);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
            }
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 0, 9, 4, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 9, 0, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 4, 0, 9, 4, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 0, 9, 5, 6, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 5, 1, 8, 5, 5, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 0, 2, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 0, 0, 4, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 1, 0, 3, 4, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 6, 0, 4, 6, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 3, 3, 1, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 3, 1, 2, 3, 3, 2, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 1, 3, 5, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 1, 0, 3, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 6, 5, 3, 6, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 1, 0, 5, 3, 0, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 9, 1, 0, 9, 3, 0, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 1, 4, 9, 4, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.setBlockState(world, Blocks.flowing_lava.getDefaultState(), 7, 1, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.flowing_lava.getDefaultState(), 8, 1, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.iron_bars.getDefaultState(), 9, 2, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.iron_bars.getDefaultState(), 9, 2, 4, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 7, 2, 4, 8, 2, 5, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 6, 1, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.furnace.getDefaultState(), 6, 2, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.furnace.getDefaultState(), 6, 3, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.double_stone_slab.getDefaultState(), 8, 1, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 2, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 2, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 2, 1, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.wooden_pressure_plate.getDefaultState(), 2, 2, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 1, 1, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 3)), 2, 1, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), 1, 1, 4, structureBoundingBox);
            if (!this.hasMadeChest && structureBoundingBox.isVecInside(new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(1), this.getZWithOffset(5, 5)))) {
                this.hasMadeChest = true;
                this.generateChestContents(world, structureBoundingBox, random, 5, 1, 5, villageBlacksmithChestContents, 3 + random.nextInt(6));
            }
            int n = 6;
            while (n <= 8) {
                if (this.getBlockStateFromPos(world, n, 0, -1, structureBoundingBox).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(world, n, -1, -1, structureBoundingBox).getBlock().getMaterial() != Material.air) {
                    this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), n, 0, -1, structureBoundingBox);
                }
                ++n;
            }
            n = 0;
            while (n < 7) {
                int n2 = 0;
                while (n2 < 10) {
                    this.clearCurrentPositionBlocksUpwards(world, n2, 6, n, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), n2, -1, n, structureBoundingBox);
                    ++n2;
                }
                ++n;
            }
            this.spawnVillagers(world, structureBoundingBox, 7, 1, 1, 1);
            return true;
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setBoolean("Chest", this.hasMadeChest);
        }

        public House2(Start start, int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(start, n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.hasMadeChest = nBTTagCompound.getBoolean("Chest");
        }

        public House2() {
        }

        public static House2 func_175855_a(Start start, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, 0, 0, 0, 10, 6, 7, enumFacing);
            return House2.canVillageGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new House2(start, n4, random, structureBoundingBox, enumFacing) : null;
        }
    }

    public static class Path
    extends Road {
        private int length;

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.length = nBTTagCompound.getInteger("Length");
        }

        public Path(Start start, int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(start, n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
            this.length = Math.max(structureBoundingBox.getXSize(), structureBoundingBox.getZSize());
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            IBlockState iBlockState = this.func_175847_a(Blocks.gravel.getDefaultState());
            IBlockState iBlockState2 = this.func_175847_a(Blocks.cobblestone.getDefaultState());
            int n = this.boundingBox.minX;
            while (n <= this.boundingBox.maxX) {
                int n2 = this.boundingBox.minZ;
                while (n2 <= this.boundingBox.maxZ) {
                    BlockPos blockPos = new BlockPos(n, 64, n2);
                    if (structureBoundingBox.isVecInside(blockPos)) {
                        blockPos = world.getTopSolidOrLiquidBlock(blockPos).down();
                        world.setBlockState(blockPos, iBlockState, 2);
                        world.setBlockState(blockPos.down(), iBlockState2, 2);
                    }
                    ++n2;
                }
                ++n;
            }
            return true;
        }

        public Path() {
        }

        public static StructureBoundingBox func_175848_a(Start start, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing) {
            int n4 = 7 * MathHelper.getRandomIntegerInRange(random, 3, 5);
            while (n4 >= 7) {
                StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, 0, 0, 0, 3, 3, n4, enumFacing);
                if (StructureComponent.findIntersecting(list, structureBoundingBox) == null) {
                    return structureBoundingBox;
                }
                n4 -= 7;
            }
            return null;
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            StructureComponent structureComponent2;
            boolean bl = false;
            int n = random.nextInt(5);
            while (n < this.length - 8) {
                structureComponent2 = this.getNextComponentNN((Start)structureComponent, list, random, 0, n);
                if (structureComponent2 != null) {
                    n += Math.max(structureComponent2.boundingBox.getXSize(), structureComponent2.boundingBox.getZSize());
                    bl = true;
                }
                n += 2 + random.nextInt(5);
            }
            n = random.nextInt(5);
            while (n < this.length - 8) {
                structureComponent2 = this.getNextComponentPP((Start)structureComponent, list, random, 0, n);
                if (structureComponent2 != null) {
                    n += Math.max(structureComponent2.boundingBox.getXSize(), structureComponent2.boundingBox.getZSize());
                    bl = true;
                }
                n += 2 + random.nextInt(5);
            }
            if (bl && random.nextInt(3) > 0 && this.coordBaseMode != null) {
                switch (this.coordBaseMode) {
                    case NORTH: {
                        StructureVillagePieces.func_176069_e((Start)structureComponent, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, this.getComponentType());
                        break;
                    }
                    case SOUTH: {
                        StructureVillagePieces.func_176069_e((Start)structureComponent, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST, this.getComponentType());
                        break;
                    }
                    case WEST: {
                        StructureVillagePieces.func_176069_e((Start)structureComponent, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                        break;
                    }
                    case EAST: {
                        StructureVillagePieces.func_176069_e((Start)structureComponent, list, random, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    }
                }
            }
            if (bl && random.nextInt(3) > 0 && this.coordBaseMode != null) {
                switch (this.coordBaseMode) {
                    case NORTH: {
                        StructureVillagePieces.func_176069_e((Start)structureComponent, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType());
                        break;
                    }
                    case SOUTH: {
                        StructureVillagePieces.func_176069_e((Start)structureComponent, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST, this.getComponentType());
                        break;
                    }
                    case WEST: {
                        StructureVillagePieces.func_176069_e((Start)structureComponent, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                        break;
                    }
                    case EAST: {
                        StructureVillagePieces.func_176069_e((Start)structureComponent, list, random, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                    }
                }
            }
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setInteger("Length", this.length);
        }
    }

    public static class Church
    extends Village {
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(world, structureBoundingBox);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 12 - 1, 0);
            }
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 1, 3, 3, 7, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 5, 1, 3, 9, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 0, 3, 0, 8, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 0, 3, 10, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 1, 0, 10, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 1, 1, 4, 10, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 4, 0, 4, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 0, 4, 4, 4, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 8, 3, 4, 8, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 5, 4, 3, 10, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 5, 5, 3, 5, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 9, 0, 4, 9, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 4, 0, 4, 4, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 0, 11, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 11, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 2, 11, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 2, 11, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 1, 1, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 1, 1, 7, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 2, 1, 7, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 3, 1, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 3, 1, 7, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 1, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 1, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 3, 1, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 1)), 1, 2, 7, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 0)), 3, 2, 7, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 3, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 3, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 6, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 7, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 6, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 7, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 6, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 7, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 6, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 7, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 3, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 3, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 3, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.getOpposite()), 2, 4, 7, structureBoundingBox);
            this.setBlockState(world, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateY()), 1, 4, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateYCCW()), 3, 4, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 4, 5, structureBoundingBox);
            int n = this.getMetadataWithOffset(Blocks.ladder, 4);
            int n2 = 1;
            while (n2 <= 9) {
                this.setBlockState(world, Blocks.ladder.getStateFromMeta(n), 3, n2, 3, structureBoundingBox);
                ++n2;
            }
            this.setBlockState(world, Blocks.air.getDefaultState(), 2, 1, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 2, 2, 0, structureBoundingBox);
            this.placeDoorCurrentPosition(world, structureBoundingBox, random, 2, 1, 0, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
            if (this.getBlockStateFromPos(world, 2, 0, -1, structureBoundingBox).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(world, 2, -1, -1, structureBoundingBox).getBlock().getMaterial() != Material.air) {
                this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, structureBoundingBox);
            }
            n2 = 0;
            while (n2 < 9) {
                int n3 = 0;
                while (n3 < 5) {
                    this.clearCurrentPositionBlocksUpwards(world, n3, 12, n2, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), n3, -1, n2, structureBoundingBox);
                    ++n3;
                }
                ++n2;
            }
            this.spawnVillagers(world, structureBoundingBox, 2, 1, 2, 1);
            return true;
        }

        @Override
        protected int func_180779_c(int n, int n2) {
            return 2;
        }

        public Church() {
        }

        public static Church func_175854_a(Start start, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, 0, 0, 0, 5, 12, 9, enumFacing);
            return Church.canVillageGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Church(start, n4, random, structureBoundingBox, enumFacing) : null;
        }

        public Church(Start start, int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(start, n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }
    }

    public static abstract class Road
    extends Village {
        public Road() {
        }

        protected Road(Start start, int n) {
            super(start, n);
        }
    }

    public static class House3
    extends Village {
        public House3(Start start, int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(start, n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        public House3() {
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n;
            int n2;
            int n3;
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(world, structureBoundingBox);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 7 - 1, 0);
            }
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 1, 7, 4, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 1, 6, 8, 4, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 0, 5, 8, 0, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 1, 7, 0, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 0, 3, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 0, 0, 8, 3, 10, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 0, 7, 2, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 5, 2, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 0, 6, 2, 3, 10, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 0, 10, 7, 3, 10, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 0, 7, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 5, 2, 3, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 4, 1, 8, 4, 1, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 4, 4, 3, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 2, 8, 5, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 0, 4, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 0, 4, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 4, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 4, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 4, 4, structureBoundingBox);
            int n4 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
            int n5 = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
            int n6 = -1;
            while (n6 <= 2) {
                n3 = 0;
                while (n3 <= 8) {
                    this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(n4), n3, 4 + n6, n6, structureBoundingBox);
                    if (!(n6 <= -1 && n3 > 1 || n6 <= 0 && n3 > 3 || n6 <= 1 && n3 > 4 && n3 < 6)) {
                        this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(n5), n3, 4 + n6, 5 - n6, structureBoundingBox);
                    }
                    ++n3;
                }
                ++n6;
            }
            this.fillWithBlocks(world, structureBoundingBox, 3, 4, 5, 3, 4, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 4, 2, 7, 4, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 5, 4, 4, 5, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 5, 4, 6, 5, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 6, 3, 5, 6, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            n6 = this.getMetadataWithOffset(Blocks.oak_stairs, 0);
            n3 = 4;
            while (n3 >= 1) {
                this.setBlockState(world, Blocks.planks.getDefaultState(), n3, 2 + n3, 7 - n3, structureBoundingBox);
                n2 = 8 - n3;
                while (n2 <= 10) {
                    this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(n6), n3, 2 + n3, n2, structureBoundingBox);
                    ++n2;
                }
                --n3;
            }
            n3 = this.getMetadataWithOffset(Blocks.oak_stairs, 1);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 6, 6, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 7, 5, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(n3), 6, 6, 4, structureBoundingBox);
            n2 = 6;
            while (n2 <= 8) {
                n = 5;
                while (n <= 10) {
                    this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(n3), n2, 12 - n2, n, structureBoundingBox);
                    ++n;
                }
                ++n2;
            }
            this.setBlockState(world, Blocks.log.getDefaultState(), 0, 2, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 0, 2, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 4, 2, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 5, 2, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 6, 2, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 8, 2, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 8, 2, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 2, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 8, 2, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 7, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 8, 2, 9, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 2, 2, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 2, 7, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 2, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 2, 2, 9, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 4, 4, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 5, 4, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 6, 4, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 5, 5, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 2, 1, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 2, 2, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 3, 1, structureBoundingBox);
            this.placeDoorCurrentPosition(world, structureBoundingBox, random, 2, 1, 0, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, -1, 3, 2, -1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            if (this.getBlockStateFromPos(world, 2, 0, -1, structureBoundingBox).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(world, 2, -1, -1, structureBoundingBox).getBlock().getMaterial() != Material.air) {
                this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, structureBoundingBox);
            }
            n2 = 0;
            while (n2 < 5) {
                n = 0;
                while (n < 9) {
                    this.clearCurrentPositionBlocksUpwards(world, n, 7, n2, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), n, -1, n2, structureBoundingBox);
                    ++n;
                }
                ++n2;
            }
            n2 = 5;
            while (n2 < 11) {
                n = 2;
                while (n < 9) {
                    this.clearCurrentPositionBlocksUpwards(world, n, 7, n2, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), n, -1, n2, structureBoundingBox);
                    ++n;
                }
                ++n2;
            }
            this.spawnVillagers(world, structureBoundingBox, 4, 1, 2, 2);
            return true;
        }

        public static House3 func_175849_a(Start start, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, 0, 0, 0, 9, 7, 12, enumFacing);
            return House3.canVillageGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new House3(start, n4, random, structureBoundingBox, enumFacing) : null;
        }
    }

    public static class Field2
    extends Village {
        private Block cropTypeB;
        private Block cropTypeA;

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.cropTypeA = Block.getBlockById(nBTTagCompound.getInteger("CA"));
            this.cropTypeB = Block.getBlockById(nBTTagCompound.getInteger("CB"));
        }

        public static Field2 func_175852_a(Start start, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, 0, 0, 0, 7, 4, 9, enumFacing);
            return Field2.canVillageGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Field2(start, n4, random, structureBoundingBox, enumFacing) : null;
        }

        private Block func_151560_a(Random random) {
            switch (random.nextInt(5)) {
                case 0: {
                    return Blocks.carrots;
                }
                case 1: {
                    return Blocks.potatoes;
                }
            }
            return Blocks.wheat;
        }

        public Field2(Start start, int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(start, n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
            this.cropTypeA = this.func_151560_a(random);
            this.cropTypeB = this.func_151560_a(random);
        }

        public Field2() {
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setInteger("CA", Block.blockRegistry.getIDForObject(this.cropTypeA));
            nBTTagCompound.setInteger("CB", Block.blockRegistry.getIDForObject(this.cropTypeB));
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(world, structureBoundingBox);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
            }
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 0, 6, 4, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 1, 2, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 0, 1, 5, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 0, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 0, 0, 6, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 0, 5, 0, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 8, 5, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 0, 1, 3, 0, 7, Blocks.water.getDefaultState(), Blocks.water.getDefaultState(), false);
            int n = 1;
            while (n <= 7) {
                this.setBlockState(world, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 1, 1, n, structureBoundingBox);
                this.setBlockState(world, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 2, 1, n, structureBoundingBox);
                this.setBlockState(world, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 4, 1, n, structureBoundingBox);
                this.setBlockState(world, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 5, 1, n, structureBoundingBox);
                ++n;
            }
            n = 0;
            while (n < 9) {
                int n2 = 0;
                while (n2 < 7) {
                    this.clearCurrentPositionBlocksUpwards(world, n2, 4, n, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.dirt.getDefaultState(), n2, -1, n, structureBoundingBox);
                    ++n2;
                }
                ++n;
            }
            return true;
        }
    }

    public static class Well
    extends Village {
        public Well() {
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(world, structureBoundingBox);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 3, 0);
            }
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 1, 4, 12, 4, Blocks.cobblestone.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);
            this.setBlockState(world, Blocks.air.getDefaultState(), 2, 12, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 3, 12, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 2, 12, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 3, 12, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 13, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 14, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 13, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 14, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 13, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 14, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 13, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 14, 4, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 1, 15, 1, 4, 15, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            int n = 0;
            while (n <= 5) {
                int n2 = 0;
                while (n2 <= 5) {
                    if (n2 == 0 || n2 == 5 || n == 0 || n == 5) {
                        this.setBlockState(world, Blocks.gravel.getDefaultState(), n2, 11, n, structureBoundingBox);
                        this.clearCurrentPositionBlocksUpwards(world, n2, 12, n, structureBoundingBox);
                    }
                    ++n2;
                }
                ++n;
            }
            return true;
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            StructureVillagePieces.func_176069_e((Start)structureComponent, list, random, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.WEST, this.getComponentType());
            StructureVillagePieces.func_176069_e((Start)structureComponent, list, random, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.EAST, this.getComponentType());
            StructureVillagePieces.func_176069_e((Start)structureComponent, list, random, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
            StructureVillagePieces.func_176069_e((Start)structureComponent, list, random, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
        }

        public Well(Start start, int n, Random random, int n2, int n3) {
            super(start, n);
            this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(random);
            switch (this.coordBaseMode) {
                case NORTH: 
                case SOUTH: {
                    this.boundingBox = new StructureBoundingBox(n2, 64, n3, n2 + 6 - 1, 78, n3 + 6 - 1);
                    break;
                }
                default: {
                    this.boundingBox = new StructureBoundingBox(n2, 64, n3, n2 + 6 - 1, 78, n3 + 6 - 1);
                }
            }
        }
    }

    public static class PieceWeight {
        public int villagePiecesLimit;
        public int villagePiecesSpawned;
        public final int villagePieceWeight;
        public Class<? extends Village> villagePieceClass;

        public boolean canSpawnMoreVillagePieces() {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }

        public PieceWeight(Class<? extends Village> clazz, int n, int n2) {
            this.villagePieceClass = clazz;
            this.villagePieceWeight = n;
            this.villagePiecesLimit = n2;
        }

        public boolean canSpawnMoreVillagePiecesOfType(int n) {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }
    }

    public static class House1
    extends Village {
        public static House1 func_175850_a(Start start, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, 0, 0, 0, 9, 9, 6, enumFacing);
            return House1.canVillageGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new House1(start, n4, random, structureBoundingBox, enumFacing) : null;
        }

        public House1() {
        }

        public House1(Start start, int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(start, n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        @Override
        protected int func_180779_c(int n, int n2) {
            return 1;
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n;
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(world, structureBoundingBox);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 9 - 1, 0);
            }
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 1, 7, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 8, 0, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 0, 8, 5, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 6, 1, 8, 6, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 7, 2, 8, 7, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            int n2 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
            int n3 = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
            int n4 = -1;
            while (n4 <= 2) {
                n = 0;
                while (n <= 8) {
                    this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(n2), n, 6 + n4, n4, structureBoundingBox);
                    this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(n3), n, 6 + n4, 5 - n4, structureBoundingBox);
                    ++n;
                }
                ++n4;
            }
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 0, 0, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 5, 8, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 1, 0, 8, 1, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 1, 0, 7, 1, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 0, 4, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 5, 0, 4, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 2, 5, 8, 4, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 2, 0, 8, 4, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 1, 0, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 5, 7, 4, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 2, 1, 8, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 0, 7, 4, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 2, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 5, 2, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 6, 2, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 3, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 5, 3, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 6, 3, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 3, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 3, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 3, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 3, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 2, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 3, 2, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 5, 2, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 6, 2, 5, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 1, 4, 1, 7, 4, 1, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 4, 4, 7, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 3, 4, 7, 3, 4, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 7, 1, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), 7, 1, 3, structureBoundingBox);
            n4 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
            this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(n4), 6, 1, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(n4), 5, 1, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(n4), 4, 1, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(n4), 3, 1, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 6, 1, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.wooden_pressure_plate.getDefaultState(), 6, 2, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 1, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.wooden_pressure_plate.getDefaultState(), 4, 2, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.crafting_table.getDefaultState(), 7, 1, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 1, 1, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 1, 2, 0, structureBoundingBox);
            this.placeDoorCurrentPosition(world, structureBoundingBox, random, 1, 1, 0, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
            if (this.getBlockStateFromPos(world, 1, 0, -1, structureBoundingBox).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(world, 1, -1, -1, structureBoundingBox).getBlock().getMaterial() != Material.air) {
                this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 0, -1, structureBoundingBox);
            }
            n = 0;
            while (n < 6) {
                int n5 = 0;
                while (n5 < 9) {
                    this.clearCurrentPositionBlocksUpwards(world, n5, 9, n, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), n5, -1, n, structureBoundingBox);
                    ++n5;
                }
                ++n;
            }
            this.spawnVillagers(world, structureBoundingBox, 2, 1, 2, 1);
            return true;
        }
    }

    public static class Start
    extends Well {
        public List<StructureComponent> field_74932_i = Lists.newArrayList();
        public boolean inDesert;
        public List<PieceWeight> structureVillageWeightedPieceList;
        public WorldChunkManager worldChunkMngr;
        public List<StructureComponent> field_74930_j = Lists.newArrayList();
        public int terrainType;
        public PieceWeight structVillagePieceWeight;

        public Start() {
        }

        public WorldChunkManager getWorldChunkManager() {
            return this.worldChunkMngr;
        }

        public Start(WorldChunkManager worldChunkManager, int n, Random random, int n2, int n3, List<PieceWeight> list, int n4) {
            super(null, 0, random, n2, n3);
            this.worldChunkMngr = worldChunkManager;
            this.structureVillageWeightedPieceList = list;
            this.terrainType = n4;
            BiomeGenBase biomeGenBase = worldChunkManager.getBiomeGenerator(new BlockPos(n2, 0, n3), BiomeGenBase.field_180279_ad);
            this.inDesert = biomeGenBase == BiomeGenBase.desert || biomeGenBase == BiomeGenBase.desertHills;
            this.func_175846_a(this.inDesert);
        }
    }

    static abstract class Village
    extends StructureComponent {
        private int villagersSpawned;
        private boolean isDesertVillage;
        protected int field_143015_k = -1;

        protected static boolean canVillageGoDeeper(StructureBoundingBox structureBoundingBox) {
            return structureBoundingBox != null && structureBoundingBox.minY > 10;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            this.field_143015_k = nBTTagCompound.getInteger("HPos");
            this.villagersSpawned = nBTTagCompound.getInteger("VCount");
            this.isDesertVillage = nBTTagCompound.getBoolean("Desert");
        }

        protected StructureComponent getNextComponentNN(Start start, List<StructureComponent> list, Random random, int n, int n2) {
            if (this.coordBaseMode != null) {
                switch (this.coordBaseMode) {
                    case NORTH: {
                        return StructureVillagePieces.func_176066_d(start, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + n, this.boundingBox.minZ + n2, EnumFacing.WEST, this.getComponentType());
                    }
                    case SOUTH: {
                        return StructureVillagePieces.func_176066_d(start, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + n, this.boundingBox.minZ + n2, EnumFacing.WEST, this.getComponentType());
                    }
                    case WEST: {
                        return StructureVillagePieces.func_176066_d(start, list, random, this.boundingBox.minX + n2, this.boundingBox.minY + n, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    }
                    case EAST: {
                        return StructureVillagePieces.func_176066_d(start, list, random, this.boundingBox.minX + n2, this.boundingBox.minY + n, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    }
                }
            }
            return null;
        }

        protected int func_180779_c(int n, int n2) {
            return n2;
        }

        @Override
        protected void replaceAirAndLiquidDownwards(World world, IBlockState iBlockState, int n, int n2, int n3, StructureBoundingBox structureBoundingBox) {
            IBlockState iBlockState2 = this.func_175847_a(iBlockState);
            super.replaceAirAndLiquidDownwards(world, iBlockState2, n, n2, n3, structureBoundingBox);
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            nBTTagCompound.setInteger("HPos", this.field_143015_k);
            nBTTagCompound.setInteger("VCount", this.villagersSpawned);
            nBTTagCompound.setBoolean("Desert", this.isDesertVillage);
        }

        @Override
        protected void setBlockState(World world, IBlockState iBlockState, int n, int n2, int n3, StructureBoundingBox structureBoundingBox) {
            IBlockState iBlockState2 = this.func_175847_a(iBlockState);
            super.setBlockState(world, iBlockState2, n, n2, n3, structureBoundingBox);
        }

        protected void func_175846_a(boolean bl) {
            this.isDesertVillage = bl;
        }

        public Village() {
        }

        protected IBlockState func_175847_a(IBlockState iBlockState) {
            if (this.isDesertVillage) {
                if (iBlockState.getBlock() == Blocks.log || iBlockState.getBlock() == Blocks.log2) {
                    return Blocks.sandstone.getDefaultState();
                }
                if (iBlockState.getBlock() == Blocks.cobblestone) {
                    return Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.getMetadata());
                }
                if (iBlockState.getBlock() == Blocks.planks) {
                    return Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata());
                }
                if (iBlockState.getBlock() == Blocks.oak_stairs) {
                    return Blocks.sandstone_stairs.getDefaultState().withProperty(BlockStairs.FACING, iBlockState.getValue(BlockStairs.FACING));
                }
                if (iBlockState.getBlock() == Blocks.stone_stairs) {
                    return Blocks.sandstone_stairs.getDefaultState().withProperty(BlockStairs.FACING, iBlockState.getValue(BlockStairs.FACING));
                }
                if (iBlockState.getBlock() == Blocks.gravel) {
                    return Blocks.sandstone.getDefaultState();
                }
            }
            return iBlockState;
        }

        protected void spawnVillagers(World world, StructureBoundingBox structureBoundingBox, int n, int n2, int n3, int n4) {
            if (this.villagersSpawned < n4) {
                int n5 = this.villagersSpawned;
                while (n5 < n4) {
                    int n6;
                    int n7;
                    int n8 = this.getXWithOffset(n + n5, n3);
                    if (!structureBoundingBox.isVecInside(new BlockPos(n8, n7 = this.getYWithOffset(n2), n6 = this.getZWithOffset(n + n5, n3)))) break;
                    ++this.villagersSpawned;
                    EntityVillager entityVillager = new EntityVillager(world);
                    entityVillager.setLocationAndAngles((double)n8 + 0.5, n7, (double)n6 + 0.5, 0.0f, 0.0f);
                    entityVillager.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityVillager)), null);
                    entityVillager.setProfession(this.func_180779_c(n5, entityVillager.getProfession()));
                    world.spawnEntityInWorld(entityVillager);
                    ++n5;
                }
            }
        }

        @Override
        protected void fillWithBlocks(World world, StructureBoundingBox structureBoundingBox, int n, int n2, int n3, int n4, int n5, int n6, IBlockState iBlockState, IBlockState iBlockState2, boolean bl) {
            IBlockState iBlockState3 = this.func_175847_a(iBlockState);
            IBlockState iBlockState4 = this.func_175847_a(iBlockState2);
            super.fillWithBlocks(world, structureBoundingBox, n, n2, n3, n4, n5, n6, iBlockState3, iBlockState4, bl);
        }

        protected Village(Start start, int n) {
            super(n);
            if (start != null) {
                this.isDesertVillage = start.inDesert;
            }
        }

        protected StructureComponent getNextComponentPP(Start start, List<StructureComponent> list, Random random, int n, int n2) {
            if (this.coordBaseMode != null) {
                switch (this.coordBaseMode) {
                    case NORTH: {
                        return StructureVillagePieces.func_176066_d(start, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + n, this.boundingBox.minZ + n2, EnumFacing.EAST, this.getComponentType());
                    }
                    case SOUTH: {
                        return StructureVillagePieces.func_176066_d(start, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + n, this.boundingBox.minZ + n2, EnumFacing.EAST, this.getComponentType());
                    }
                    case WEST: {
                        return StructureVillagePieces.func_176066_d(start, list, random, this.boundingBox.minX + n2, this.boundingBox.minY + n, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                    }
                    case EAST: {
                        return StructureVillagePieces.func_176066_d(start, list, random, this.boundingBox.minX + n2, this.boundingBox.minY + n, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                    }
                }
            }
            return null;
        }

        protected int getAverageGroundLevel(World world, StructureBoundingBox structureBoundingBox) {
            int n = 0;
            int n2 = 0;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            int n3 = this.boundingBox.minZ;
            while (n3 <= this.boundingBox.maxZ) {
                int n4 = this.boundingBox.minX;
                while (n4 <= this.boundingBox.maxX) {
                    mutableBlockPos.func_181079_c(n4, 64, n3);
                    if (structureBoundingBox.isVecInside(mutableBlockPos)) {
                        n += Math.max(world.getTopSolidOrLiquidBlock(mutableBlockPos).getY(), world.provider.getAverageGroundLevel());
                        ++n2;
                    }
                    ++n4;
                }
                ++n3;
            }
            if (n2 == 0) {
                return -1;
            }
            return n / n2;
        }
    }

    public static class Torch
    extends Village {
        public static StructureBoundingBox func_175856_a(Start start, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, 0, 0, 0, 3, 4, 2, enumFacing);
            return StructureComponent.findIntersecting(list, structureBoundingBox) != null ? null : structureBoundingBox;
        }

        public Torch() {
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(world, structureBoundingBox);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
            }
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 2, 3, 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 0, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 1, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 2, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.wool.getStateFromMeta(EnumDyeColor.WHITE.getDyeDamage()), 1, 3, 0, structureBoundingBox);
            boolean bl = this.coordBaseMode == EnumFacing.EAST || this.coordBaseMode == EnumFacing.NORTH;
            this.setBlockState(world, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateY()), bl ? 2 : 0, 3, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 1, 3, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateYCCW()), bl ? 0 : 2, 3, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.getOpposite()), 1, 3, -1, structureBoundingBox);
            return true;
        }

        public Torch(Start start, int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(start, n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }
    }

    public static class House4Garden
    extends Village {
        private boolean isRoofAccessible;

        public static House4Garden func_175858_a(Start start, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, 0, 0, 0, 5, 6, 5, enumFacing);
            return StructureComponent.findIntersecting(list, structureBoundingBox) != null ? null : new House4Garden(start, n4, random, structureBoundingBox, enumFacing);
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n;
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(world, structureBoundingBox);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
            }
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 4, 0, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 4, 0, 4, 4, 4, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 4, 1, 3, 4, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 0, 1, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 0, 2, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 0, 3, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 1, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 2, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 3, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 0, 1, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 0, 2, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 0, 3, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 1, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 2, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 3, 4, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 1, 0, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 1, 1, 4, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 4, 3, 3, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 2, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 1, 1, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 1, 2, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 1, 3, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 2, 3, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 3, 3, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 3, 2, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 3, 1, 0, structureBoundingBox);
            if (this.getBlockStateFromPos(world, 2, 0, -1, structureBoundingBox).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(world, 2, -1, -1, structureBoundingBox).getBlock().getMaterial() != Material.air) {
                this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, structureBoundingBox);
            }
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 1, 3, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            if (this.isRoofAccessible) {
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 0, 5, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 5, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 2, 5, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 3, 5, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 5, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 0, 5, 4, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 5, 4, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 2, 5, 4, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 3, 5, 4, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 5, 4, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 5, 1, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 5, 2, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 5, 3, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 0, 5, 1, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 0, 5, 2, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 0, 5, 3, structureBoundingBox);
            }
            if (this.isRoofAccessible) {
                n = this.getMetadataWithOffset(Blocks.ladder, 3);
                this.setBlockState(world, Blocks.ladder.getStateFromMeta(n), 3, 1, 3, structureBoundingBox);
                this.setBlockState(world, Blocks.ladder.getStateFromMeta(n), 3, 2, 3, structureBoundingBox);
                this.setBlockState(world, Blocks.ladder.getStateFromMeta(n), 3, 3, 3, structureBoundingBox);
                this.setBlockState(world, Blocks.ladder.getStateFromMeta(n), 3, 4, 3, structureBoundingBox);
            }
            this.setBlockState(world, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 3, 1, structureBoundingBox);
            n = 0;
            while (n < 5) {
                int n2 = 0;
                while (n2 < 5) {
                    this.clearCurrentPositionBlocksUpwards(world, n2, 6, n, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), n2, -1, n, structureBoundingBox);
                    ++n2;
                }
                ++n;
            }
            this.spawnVillagers(world, structureBoundingBox, 1, 1, 2, 1);
            return true;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.isRoofAccessible = nBTTagCompound.getBoolean("Terrace");
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setBoolean("Terrace", this.isRoofAccessible);
        }

        public House4Garden(Start start, int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(start, n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
            this.isRoofAccessible = random.nextBoolean();
        }

        public House4Garden() {
        }
    }

    public static class Field1
    extends Village {
        private Block cropTypeA;
        private Block cropTypeB;
        private Block cropTypeD;
        private Block cropTypeC;

        public Field1(Start start, int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(start, n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
            this.cropTypeA = this.func_151559_a(random);
            this.cropTypeB = this.func_151559_a(random);
            this.cropTypeC = this.func_151559_a(random);
            this.cropTypeD = this.func_151559_a(random);
        }

        public Field1() {
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.cropTypeA = Block.getBlockById(nBTTagCompound.getInteger("CA"));
            this.cropTypeB = Block.getBlockById(nBTTagCompound.getInteger("CB"));
            this.cropTypeC = Block.getBlockById(nBTTagCompound.getInteger("CC"));
            this.cropTypeD = Block.getBlockById(nBTTagCompound.getInteger("CD"));
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setInteger("CA", Block.blockRegistry.getIDForObject(this.cropTypeA));
            nBTTagCompound.setInteger("CB", Block.blockRegistry.getIDForObject(this.cropTypeB));
            nBTTagCompound.setInteger("CC", Block.blockRegistry.getIDForObject(this.cropTypeC));
            nBTTagCompound.setInteger("CD", Block.blockRegistry.getIDForObject(this.cropTypeD));
        }

        private Block func_151559_a(Random random) {
            switch (random.nextInt(5)) {
                case 0: {
                    return Blocks.carrots;
                }
                case 1: {
                    return Blocks.potatoes;
                }
            }
            return Blocks.wheat;
        }

        public static Field1 func_175851_a(Start start, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, 0, 0, 0, 13, 4, 9, enumFacing);
            return Field1.canVillageGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Field1(start, n4, random, structureBoundingBox, enumFacing) : null;
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(world, structureBoundingBox);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
            }
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 0, 12, 4, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 1, 2, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 0, 1, 5, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 0, 1, 8, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 10, 0, 1, 11, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 0, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 0, 0, 6, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 12, 0, 0, 12, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 0, 11, 0, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 8, 11, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 0, 1, 3, 0, 7, Blocks.water.getDefaultState(), Blocks.water.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 9, 0, 1, 9, 0, 7, Blocks.water.getDefaultState(), Blocks.water.getDefaultState(), false);
            int n = 1;
            while (n <= 7) {
                this.setBlockState(world, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 1, 1, n, structureBoundingBox);
                this.setBlockState(world, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 2, 1, n, structureBoundingBox);
                this.setBlockState(world, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 4, 1, n, structureBoundingBox);
                this.setBlockState(world, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 5, 1, n, structureBoundingBox);
                this.setBlockState(world, this.cropTypeC.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 7, 1, n, structureBoundingBox);
                this.setBlockState(world, this.cropTypeC.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 8, 1, n, structureBoundingBox);
                this.setBlockState(world, this.cropTypeD.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 10, 1, n, structureBoundingBox);
                this.setBlockState(world, this.cropTypeD.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 11, 1, n, structureBoundingBox);
                ++n;
            }
            n = 0;
            while (n < 9) {
                int n2 = 0;
                while (n2 < 13) {
                    this.clearCurrentPositionBlocksUpwards(world, n2, 4, n, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.dirt.getDefaultState(), n2, -1, n, structureBoundingBox);
                    ++n2;
                }
                ++n;
            }
            return true;
        }
    }

    public static class WoodHut
    extends Village {
        private boolean isTallHouse;
        private int tablePosition;

        public WoodHut() {
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setInteger("T", this.tablePosition);
            nBTTagCompound.setBoolean("C", this.isTallHouse);
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(world, structureBoundingBox);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
            }
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 1, 3, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 3, 0, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 1, 2, 0, 3, Blocks.dirt.getDefaultState(), Blocks.dirt.getDefaultState(), false);
            if (this.isTallHouse) {
                this.fillWithBlocks(world, structureBoundingBox, 1, 4, 1, 2, 4, 3, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            } else {
                this.fillWithBlocks(world, structureBoundingBox, 1, 5, 1, 2, 5, 3, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            }
            this.setBlockState(world, Blocks.log.getDefaultState(), 1, 4, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 2, 4, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 1, 4, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 2, 4, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 0, 4, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 0, 4, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 0, 4, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 3, 4, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 3, 4, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 3, 4, 3, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 0, 0, 3, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 1, 0, 3, 3, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 4, 0, 3, 4, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 1, 4, 3, 3, 4, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 1, 0, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 1, 1, 3, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 0, 2, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 4, 2, 3, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 3, 2, 2, structureBoundingBox);
            if (this.tablePosition > 0) {
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), this.tablePosition, 1, 3, structureBoundingBox);
                this.setBlockState(world, Blocks.wooden_pressure_plate.getDefaultState(), this.tablePosition, 2, 3, structureBoundingBox);
            }
            this.setBlockState(world, Blocks.air.getDefaultState(), 1, 1, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 1, 2, 0, structureBoundingBox);
            this.placeDoorCurrentPosition(world, structureBoundingBox, random, 1, 1, 0, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
            if (this.getBlockStateFromPos(world, 1, 0, -1, structureBoundingBox).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(world, 1, -1, -1, structureBoundingBox).getBlock().getMaterial() != Material.air) {
                this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 0, -1, structureBoundingBox);
            }
            int n = 0;
            while (n < 5) {
                int n2 = 0;
                while (n2 < 4) {
                    this.clearCurrentPositionBlocksUpwards(world, n2, 6, n, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), n2, -1, n, structureBoundingBox);
                    ++n2;
                }
                ++n;
            }
            this.spawnVillagers(world, structureBoundingBox, 1, 1, 2, 1);
            return true;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.tablePosition = nBTTagCompound.getInteger("T");
            this.isTallHouse = nBTTagCompound.getBoolean("C");
        }

        public static WoodHut func_175853_a(Start start, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, 0, 0, 0, 4, 6, 5, enumFacing);
            return WoodHut.canVillageGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new WoodHut(start, n4, random, structureBoundingBox, enumFacing) : null;
        }

        public WoodHut(Start start, int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(start, n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
            this.isTallHouse = random.nextBoolean();
            this.tablePosition = random.nextInt(3);
        }
    }

    public static class Hall
    extends Village {
        public Hall() {
        }

        public Hall(Start start, int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(start, n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        @Override
        protected int func_180779_c(int n, int n2) {
            return n == 0 ? 4 : super.func_180779_c(n, n2);
        }

        public static Hall func_175857_a(Start start, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, 0, 0, 0, 9, 7, 11, enumFacing);
            return Hall.canVillageGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Hall(start, n4, random, structureBoundingBox, enumFacing) : null;
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n;
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(world, structureBoundingBox);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 7 - 1, 0);
            }
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 1, 7, 4, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 1, 6, 8, 4, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 0, 6, 8, 0, 10, Blocks.dirt.getDefaultState(), Blocks.dirt.getDefaultState(), false);
            this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 6, 0, 6, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 2, 1, 6, 2, 1, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 1, 6, 8, 1, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 1, 10, 7, 1, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 1, 7, 0, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 0, 3, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 0, 0, 8, 3, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 0, 7, 1, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 5, 7, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 0, 7, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 5, 7, 3, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 4, 1, 8, 4, 1, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 4, 4, 8, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 2, 8, 5, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 0, 4, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 0, 4, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 4, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 4, 3, structureBoundingBox);
            int n2 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
            int n3 = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
            int n4 = -1;
            while (n4 <= 2) {
                n = 0;
                while (n <= 8) {
                    this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(n2), n, 4 + n4, n4, structureBoundingBox);
                    this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(n3), n, 4 + n4, 5 - n4, structureBoundingBox);
                    ++n;
                }
                ++n4;
            }
            this.setBlockState(world, Blocks.log.getDefaultState(), 0, 2, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 0, 2, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 8, 2, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.log.getDefaultState(), 8, 2, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 2, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 3, 2, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 5, 2, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 6, 2, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 2, 1, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.wooden_pressure_plate.getDefaultState(), 2, 2, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.planks.getDefaultState(), 1, 1, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 3)), 2, 1, 4, structureBoundingBox);
            this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), 1, 1, 3, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 5, 0, 1, 7, 0, 3, Blocks.double_stone_slab.getDefaultState(), Blocks.double_stone_slab.getDefaultState(), false);
            this.setBlockState(world, Blocks.double_stone_slab.getDefaultState(), 6, 1, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.double_stone_slab.getDefaultState(), 6, 1, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 2, 1, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 2, 2, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 3, 1, structureBoundingBox);
            this.placeDoorCurrentPosition(world, structureBoundingBox, random, 2, 1, 0, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
            if (this.getBlockStateFromPos(world, 2, 0, -1, structureBoundingBox).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(world, 2, -1, -1, structureBoundingBox).getBlock().getMaterial() != Material.air) {
                this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, structureBoundingBox);
            }
            this.setBlockState(world, Blocks.air.getDefaultState(), 6, 1, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.air.getDefaultState(), 6, 2, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.getOpposite()), 6, 3, 4, structureBoundingBox);
            this.placeDoorCurrentPosition(world, structureBoundingBox, random, 6, 1, 5, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
            n4 = 0;
            while (n4 < 5) {
                n = 0;
                while (n < 9) {
                    this.clearCurrentPositionBlocksUpwards(world, n, 7, n4, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), n, -1, n4, structureBoundingBox);
                    ++n;
                }
                ++n4;
            }
            this.spawnVillagers(world, structureBoundingBox, 4, 1, 2, 2);
            return true;
        }
    }
}

