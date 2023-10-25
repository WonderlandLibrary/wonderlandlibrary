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
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class StructureMineshaftPieces {
    private static final List<WeightedRandomChestContent> CHEST_CONTENT_WEIGHT_LIST = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.dye, EnumDyeColor.BLUE.getDyeDamage(), 4, 9, 5), new WeightedRandomChestContent(Items.diamond, 0, 1, 2, 3), new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.rail), 0, 4, 8, 1), new WeightedRandomChestContent(Items.melon_seeds, 0, 2, 4, 10), new WeightedRandomChestContent(Items.pumpkin_seeds, 0, 2, 4, 10), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1)});

    private static StructureComponent func_175890_b(StructureComponent structureComponent, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
        if (n4 > 8) {
            return null;
        }
        if (Math.abs(n - structureComponent.getBoundingBox().minX) <= 80 && Math.abs(n3 - structureComponent.getBoundingBox().minZ) <= 80) {
            StructureComponent structureComponent2 = StructureMineshaftPieces.func_175892_a(list, random, n, n2, n3, enumFacing, n4 + 1);
            if (structureComponent2 != null) {
                list.add(structureComponent2);
                structureComponent2.buildComponent(structureComponent, list, random);
            }
            return structureComponent2;
        }
        return null;
    }

    private static StructureComponent func_175892_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
        int n5 = random.nextInt(100);
        if (n5 >= 80) {
            StructureBoundingBox structureBoundingBox = Cross.func_175813_a(list, random, n, n2, n3, enumFacing);
            if (structureBoundingBox != null) {
                return new Cross(n4, random, structureBoundingBox, enumFacing);
            }
        } else if (n5 >= 70) {
            StructureBoundingBox structureBoundingBox = Stairs.func_175812_a(list, random, n, n2, n3, enumFacing);
            if (structureBoundingBox != null) {
                return new Stairs(n4, random, structureBoundingBox, enumFacing);
            }
        } else {
            StructureBoundingBox structureBoundingBox = Corridor.func_175814_a(list, random, n, n2, n3, enumFacing);
            if (structureBoundingBox != null) {
                return new Corridor(n4, random, structureBoundingBox, enumFacing);
            }
        }
        return null;
    }

    public static void registerStructurePieces() {
        MapGenStructureIO.registerStructureComponent(Corridor.class, "MSCorridor");
        MapGenStructureIO.registerStructureComponent(Cross.class, "MSCrossing");
        MapGenStructureIO.registerStructureComponent(Room.class, "MSRoom");
        MapGenStructureIO.registerStructureComponent(Stairs.class, "MSStairs");
    }

    public static class Room
    extends StructureComponent {
        private List<StructureBoundingBox> roomsLinkedToTheRoom = Lists.newLinkedList();

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            NBTTagList nBTTagList = nBTTagCompound.getTagList("Entrances", 11);
            int n = 0;
            while (n < nBTTagList.tagCount()) {
                this.roomsLinkedToTheRoom.add(new StructureBoundingBox(nBTTagList.getIntArrayAt(n)));
                ++n;
            }
        }

        public Room() {
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            StructureBoundingBox structureBoundingBox;
            StructureComponent structureComponent2;
            int n = this.getComponentType();
            int n2 = this.boundingBox.getYSize() - 3 - 1;
            if (n2 <= 0) {
                n2 = 1;
            }
            int n3 = 0;
            while (n3 < this.boundingBox.getXSize()) {
                if ((n3 += random.nextInt(this.boundingBox.getXSize())) + 3 > this.boundingBox.getXSize()) break;
                structureComponent2 = StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX + n3, this.boundingBox.minY + random.nextInt(n2) + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, n);
                if (structureComponent2 != null) {
                    structureBoundingBox = structureComponent2.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(structureBoundingBox.minX, structureBoundingBox.minY, this.boundingBox.minZ, structureBoundingBox.maxX, structureBoundingBox.maxY, this.boundingBox.minZ + 1));
                }
                n3 += 4;
            }
            n3 = 0;
            while (n3 < this.boundingBox.getXSize()) {
                if ((n3 += random.nextInt(this.boundingBox.getXSize())) + 3 > this.boundingBox.getXSize()) break;
                structureComponent2 = StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX + n3, this.boundingBox.minY + random.nextInt(n2) + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, n);
                if (structureComponent2 != null) {
                    structureBoundingBox = structureComponent2.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(structureBoundingBox.minX, structureBoundingBox.minY, this.boundingBox.maxZ - 1, structureBoundingBox.maxX, structureBoundingBox.maxY, this.boundingBox.maxZ));
                }
                n3 += 4;
            }
            n3 = 0;
            while (n3 < this.boundingBox.getZSize()) {
                if ((n3 += random.nextInt(this.boundingBox.getZSize())) + 3 > this.boundingBox.getZSize()) break;
                structureComponent2 = StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + random.nextInt(n2) + 1, this.boundingBox.minZ + n3, EnumFacing.WEST, n);
                if (structureComponent2 != null) {
                    structureBoundingBox = structureComponent2.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.minX, structureBoundingBox.minY, structureBoundingBox.minZ, this.boundingBox.minX + 1, structureBoundingBox.maxY, structureBoundingBox.maxZ));
                }
                n3 += 4;
            }
            n3 = 0;
            while (n3 < this.boundingBox.getZSize()) {
                if ((n3 += random.nextInt(this.boundingBox.getZSize())) + 3 > this.boundingBox.getZSize()) break;
                structureComponent2 = StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + random.nextInt(n2) + 1, this.boundingBox.minZ + n3, EnumFacing.EAST, n);
                if (structureComponent2 != null) {
                    structureBoundingBox = structureComponent2.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.maxX - 1, structureBoundingBox.minY, structureBoundingBox.minZ, this.boundingBox.maxX, structureBoundingBox.maxY, structureBoundingBox.maxZ));
                }
                n3 += 4;
            }
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
                return false;
            }
            this.fillWithBlocks(world, structureBoundingBox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, Blocks.dirt.getDefaultState(), Blocks.air.getDefaultState(), true);
            this.fillWithBlocks(world, structureBoundingBox, this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ, this.boundingBox.maxX, Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY), this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            for (StructureBoundingBox structureBoundingBox2 : this.roomsLinkedToTheRoom) {
                this.fillWithBlocks(world, structureBoundingBox, structureBoundingBox2.minX, structureBoundingBox2.maxY - 2, structureBoundingBox2.minZ, structureBoundingBox2.maxX, structureBoundingBox2.maxY, structureBoundingBox2.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            this.randomlyRareFillWithBlocks(world, structureBoundingBox, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), false);
            return true;
        }

        public Room(int n, Random random, int n2, int n3) {
            super(n);
            this.boundingBox = new StructureBoundingBox(n2, 50, n3, n2 + 7 + random.nextInt(6), 54 + random.nextInt(6), n3 + 7 + random.nextInt(6));
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            NBTTagList nBTTagList = new NBTTagList();
            for (StructureBoundingBox structureBoundingBox : this.roomsLinkedToTheRoom) {
                nBTTagList.appendTag(structureBoundingBox.toNBTTagIntArray());
            }
            nBTTagCompound.setTag("Entrances", nBTTagList);
        }

        @Override
        public void func_181138_a(int n, int n2, int n3) {
            super.func_181138_a(n, n2, n3);
            for (StructureBoundingBox structureBoundingBox : this.roomsLinkedToTheRoom) {
                structureBoundingBox.offset(n, n2, n3);
            }
        }
    }

    public static class Cross
    extends StructureComponent {
        private boolean isMultipleFloors;
        private EnumFacing corridorDirection;

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
                return false;
            }
            if (this.isMultipleFloors) {
                this.fillWithBlocks(world, structureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            } else {
                this.fillWithBlocks(world, structureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            this.fillWithBlocks(world, structureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
            int n = this.boundingBox.minX;
            while (n <= this.boundingBox.maxX) {
                int n2 = this.boundingBox.minZ;
                while (n2 <= this.boundingBox.maxZ) {
                    if (this.getBlockStateFromPos(world, n, this.boundingBox.minY - 1, n2, structureBoundingBox).getBlock().getMaterial() == Material.air) {
                        this.setBlockState(world, Blocks.planks.getDefaultState(), n, this.boundingBox.minY - 1, n2, structureBoundingBox);
                    }
                    ++n2;
                }
                ++n;
            }
            return true;
        }

        public Cross() {
        }

        public Cross(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.corridorDirection = enumFacing;
            this.boundingBox = structureBoundingBox;
            this.isMultipleFloors = structureBoundingBox.getYSize() > 3;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            this.isMultipleFloors = nBTTagCompound.getBoolean("tf");
            this.corridorDirection = EnumFacing.getHorizontal(nBTTagCompound.getInteger("D"));
        }

        public static StructureBoundingBox func_175813_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing) {
            StructureBoundingBox structureBoundingBox = new StructureBoundingBox(n, n2, n3, n, n2 + 2, n3);
            if (random.nextInt(4) == 0) {
                structureBoundingBox.maxY += 4;
            }
            switch (enumFacing) {
                case NORTH: {
                    structureBoundingBox.minX = n - 1;
                    structureBoundingBox.maxX = n + 3;
                    structureBoundingBox.minZ = n3 - 4;
                    break;
                }
                case SOUTH: {
                    structureBoundingBox.minX = n - 1;
                    structureBoundingBox.maxX = n + 3;
                    structureBoundingBox.maxZ = n3 + 4;
                    break;
                }
                case WEST: {
                    structureBoundingBox.minX = n - 4;
                    structureBoundingBox.minZ = n3 - 1;
                    structureBoundingBox.maxZ = n3 + 3;
                    break;
                }
                case EAST: {
                    structureBoundingBox.maxX = n + 4;
                    structureBoundingBox.minZ = n3 - 1;
                    structureBoundingBox.maxZ = n3 + 3;
                }
            }
            return StructureComponent.findIntersecting(list, structureBoundingBox) != null ? null : structureBoundingBox;
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            nBTTagCompound.setBoolean("tf", this.isMultipleFloors);
            nBTTagCompound.setInteger("D", this.corridorDirection.getHorizontalIndex());
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            int n = this.getComponentType();
            switch (this.corridorDirection) {
                case NORTH: {
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, n);
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, n);
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, n);
                    break;
                }
                case SOUTH: {
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, n);
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, n);
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, n);
                    break;
                }
                case WEST: {
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, n);
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, n);
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, n);
                    break;
                }
                case EAST: {
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, n);
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, n);
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, n);
                }
            }
            if (this.isMultipleFloors) {
                if (random.nextBoolean()) {
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, n);
                }
                if (random.nextBoolean()) {
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.WEST, n);
                }
                if (random.nextBoolean()) {
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.EAST, n);
                }
                if (random.nextBoolean()) {
                    StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, n);
                }
            }
        }
    }

    public static class Stairs
    extends StructureComponent {
        public static StructureBoundingBox func_175812_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing) {
            StructureBoundingBox structureBoundingBox = new StructureBoundingBox(n, n2 - 5, n3, n, n2 + 2, n3);
            switch (enumFacing) {
                case NORTH: {
                    structureBoundingBox.maxX = n + 2;
                    structureBoundingBox.minZ = n3 - 8;
                    break;
                }
                case SOUTH: {
                    structureBoundingBox.maxX = n + 2;
                    structureBoundingBox.maxZ = n3 + 8;
                    break;
                }
                case WEST: {
                    structureBoundingBox.minX = n - 8;
                    structureBoundingBox.maxZ = n3 + 2;
                    break;
                }
                case EAST: {
                    structureBoundingBox.maxX = n + 8;
                    structureBoundingBox.maxZ = n3 + 2;
                }
            }
            return StructureComponent.findIntersecting(list, structureBoundingBox) != null ? null : structureBoundingBox;
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            int n = this.getComponentType();
            if (this.coordBaseMode != null) {
                switch (this.coordBaseMode) {
                    case NORTH: {
                        StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, n);
                        break;
                    }
                    case SOUTH: {
                        StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, n);
                        break;
                    }
                    case WEST: {
                        StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, n);
                        break;
                    }
                    case EAST: {
                        StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, n);
                    }
                }
            }
        }

        public Stairs(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        public Stairs() {
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
                return false;
            }
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 0, 2, 7, 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 7, 2, 2, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            int n = 0;
            while (n < 5) {
                this.fillWithBlocks(world, structureBoundingBox, 0, 5 - n - (n < 4 ? 1 : 0), 2 + n, 2, 7 - n, 2 + n, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                ++n;
            }
            return true;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
        }
    }

    public static class Corridor
    extends StructureComponent {
        private int sectionCount;
        private boolean hasSpiders;
        private boolean spawnerPlaced;
        private boolean hasRails;

        public Corridor(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
            this.hasRails = random.nextInt(3) == 0;
            this.hasSpiders = !this.hasRails && random.nextInt(23) == 0;
            this.sectionCount = this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.SOUTH ? structureBoundingBox.getXSize() / 5 : structureBoundingBox.getZSize() / 5;
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            block25: {
                int n = this.getComponentType();
                int n2 = random.nextInt(4);
                if (this.coordBaseMode != null) {
                    switch (this.coordBaseMode) {
                        case NORTH: {
                            if (n2 <= 1) {
                                StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ - 1, this.coordBaseMode, n);
                                break;
                            }
                            if (n2 == 2) {
                                StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, EnumFacing.WEST, n);
                                break;
                            }
                            StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, EnumFacing.EAST, n);
                            break;
                        }
                        case SOUTH: {
                            if (n2 <= 1) {
                                StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ + 1, this.coordBaseMode, n);
                                break;
                            }
                            if (n2 == 2) {
                                StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.WEST, n);
                                break;
                            }
                            StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.EAST, n);
                            break;
                        }
                        case WEST: {
                            if (n2 <= 1) {
                                StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, this.coordBaseMode, n);
                                break;
                            }
                            if (n2 == 2) {
                                StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, n);
                                break;
                            }
                            StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, n);
                            break;
                        }
                        case EAST: {
                            if (n2 <= 1) {
                                StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, this.coordBaseMode, n);
                                break;
                            }
                            if (n2 == 2) {
                                StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, n);
                                break;
                            }
                            StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, n);
                        }
                    }
                }
                if (n >= 8) break block25;
                if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.SOUTH) {
                    int n3 = this.boundingBox.minX + 3;
                    while (n3 + 3 <= this.boundingBox.maxX) {
                        int n4 = random.nextInt(5);
                        if (n4 == 0) {
                            StructureMineshaftPieces.func_175890_b(structureComponent, list, random, n3, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, n + 1);
                        } else if (n4 == 1) {
                            StructureMineshaftPieces.func_175890_b(structureComponent, list, random, n3, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, n + 1);
                        }
                        n3 += 5;
                    }
                } else {
                    int n5 = this.boundingBox.minZ + 3;
                    while (n5 + 3 <= this.boundingBox.maxZ) {
                        int n6 = random.nextInt(5);
                        if (n6 == 0) {
                            StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, n5, EnumFacing.WEST, n + 1);
                        } else if (n6 == 1) {
                            StructureMineshaftPieces.func_175890_b(structureComponent, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, n5, EnumFacing.EAST, n + 1);
                        }
                        n5 += 5;
                    }
                }
            }
        }

        public static StructureBoundingBox func_175814_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing) {
            StructureBoundingBox structureBoundingBox = new StructureBoundingBox(n, n2, n3, n, n2 + 2, n3);
            int n4 = random.nextInt(3) + 2;
            while (n4 > 0) {
                int n5 = n4 * 5;
                switch (enumFacing) {
                    case NORTH: {
                        structureBoundingBox.maxX = n + 2;
                        structureBoundingBox.minZ = n3 - (n5 - 1);
                        break;
                    }
                    case SOUTH: {
                        structureBoundingBox.maxX = n + 2;
                        structureBoundingBox.maxZ = n3 + (n5 - 1);
                        break;
                    }
                    case WEST: {
                        structureBoundingBox.minX = n - (n5 - 1);
                        structureBoundingBox.maxZ = n3 + 2;
                        break;
                    }
                    case EAST: {
                        structureBoundingBox.maxX = n + (n5 - 1);
                        structureBoundingBox.maxZ = n3 + 2;
                    }
                }
                if (StructureComponent.findIntersecting(list, structureBoundingBox) == null) break;
                --n4;
            }
            return n4 > 0 ? structureBoundingBox : null;
        }

        @Override
        protected boolean generateChestContents(World world, StructureBoundingBox structureBoundingBox, Random random, int n, int n2, int n3, List<WeightedRandomChestContent> list, int n4) {
            BlockPos blockPos = new BlockPos(this.getXWithOffset(n, n3), this.getYWithOffset(n2), this.getZWithOffset(n, n3));
            if (structureBoundingBox.isVecInside(blockPos) && world.getBlockState(blockPos).getBlock().getMaterial() == Material.air) {
                int n5 = random.nextBoolean() ? 1 : 0;
                world.setBlockState(blockPos, Blocks.rail.getStateFromMeta(this.getMetadataWithOffset(Blocks.rail, n5)), 2);
                EntityMinecartChest entityMinecartChest = new EntityMinecartChest(world, (float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.5f, (float)blockPos.getZ() + 0.5f);
                WeightedRandomChestContent.generateChestContents(random, list, entityMinecartChest, n4);
                world.spawnEntityInWorld(entityMinecartChest);
                return true;
            }
            return false;
        }

        public Corridor() {
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            nBTTagCompound.setBoolean("hr", this.hasRails);
            nBTTagCompound.setBoolean("sc", this.hasSpiders);
            nBTTagCompound.setBoolean("hps", this.spawnerPlaced);
            nBTTagCompound.setInteger("Num", this.sectionCount);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            this.hasRails = nBTTagCompound.getBoolean("hr");
            this.hasSpiders = nBTTagCompound.getBoolean("sc");
            this.spawnerPlaced = nBTTagCompound.getBoolean("hps");
            this.sectionCount = nBTTagCompound.getInteger("Num");
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n;
            int n2;
            int n3;
            if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
                return false;
            }
            boolean bl = false;
            int n4 = 2;
            boolean bl2 = false;
            int n5 = 2;
            int n6 = this.sectionCount * 5 - 1;
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 2, 1, n6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175805_a(world, structureBoundingBox, random, 0.8f, 0, 2, 0, 2, 2, n6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            if (this.hasSpiders) {
                this.func_175805_a(world, structureBoundingBox, random, 0.6f, 0, 0, 0, 2, 1, n6, Blocks.web.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            int n7 = 0;
            while (n7 < this.sectionCount) {
                n3 = 2 + n7 * 5;
                this.fillWithBlocks(world, structureBoundingBox, 0, 0, n3, 0, 1, n3, Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 2, 0, n3, 2, 1, n3, Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);
                if (random.nextInt(4) == 0) {
                    this.fillWithBlocks(world, structureBoundingBox, 0, 2, n3, 0, 2, n3, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.fillWithBlocks(world, structureBoundingBox, 2, 2, n3, 2, 2, n3, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                } else {
                    this.fillWithBlocks(world, structureBoundingBox, 0, 2, n3, 2, 2, n3, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                }
                this.randomlyPlaceBlock(world, structureBoundingBox, random, 0.1f, 0, 2, n3 - 1, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(world, structureBoundingBox, random, 0.1f, 2, 2, n3 - 1, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(world, structureBoundingBox, random, 0.1f, 0, 2, n3 + 1, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(world, structureBoundingBox, random, 0.1f, 2, 2, n3 + 1, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(world, structureBoundingBox, random, 0.05f, 0, 2, n3 - 2, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(world, structureBoundingBox, random, 0.05f, 2, 2, n3 - 2, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(world, structureBoundingBox, random, 0.05f, 0, 2, n3 + 2, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(world, structureBoundingBox, random, 0.05f, 2, 2, n3 + 2, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(world, structureBoundingBox, random, 0.05f, 1, 2, n3 - 1, Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));
                this.randomlyPlaceBlock(world, structureBoundingBox, random, 0.05f, 1, 2, n3 + 1, Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));
                if (random.nextInt(100) == 0) {
                    this.generateChestContents(world, structureBoundingBox, random, 2, 0, n3 - 1, WeightedRandomChestContent.func_177629_a(CHEST_CONTENT_WEIGHT_LIST, Items.enchanted_book.getRandom(random)), 3 + random.nextInt(4));
                }
                if (random.nextInt(100) == 0) {
                    this.generateChestContents(world, structureBoundingBox, random, 0, 0, n3 + 1, WeightedRandomChestContent.func_177629_a(CHEST_CONTENT_WEIGHT_LIST, Items.enchanted_book.getRandom(random)), 3 + random.nextInt(4));
                }
                if (this.hasSpiders && !this.spawnerPlaced) {
                    n2 = this.getYWithOffset(0);
                    int n8 = n3 - 1 + random.nextInt(3);
                    n = this.getXWithOffset(1, n8);
                    BlockPos blockPos = new BlockPos(n, n2, n8 = this.getZWithOffset(1, n8));
                    if (structureBoundingBox.isVecInside(blockPos)) {
                        this.spawnerPlaced = true;
                        world.setBlockState(blockPos, Blocks.mob_spawner.getDefaultState(), 2);
                        TileEntity tileEntity = world.getTileEntity(blockPos);
                        if (tileEntity instanceof TileEntityMobSpawner) {
                            ((TileEntityMobSpawner)tileEntity).getSpawnerBaseLogic().setEntityName("CaveSpider");
                        }
                    }
                }
                ++n7;
            }
            n7 = 0;
            while (n7 <= 2) {
                n3 = 0;
                while (n3 <= n6) {
                    n2 = -1;
                    IBlockState iBlockState = this.getBlockStateFromPos(world, n7, n2, n3, structureBoundingBox);
                    if (iBlockState.getBlock().getMaterial() == Material.air) {
                        n = -1;
                        this.setBlockState(world, Blocks.planks.getDefaultState(), n7, n, n3, structureBoundingBox);
                    }
                    ++n3;
                }
                ++n7;
            }
            if (this.hasRails) {
                n7 = 0;
                while (n7 <= n6) {
                    IBlockState iBlockState = this.getBlockStateFromPos(world, 1, -1, n7, structureBoundingBox);
                    if (iBlockState.getBlock().getMaterial() != Material.air && iBlockState.getBlock().isFullBlock()) {
                        this.randomlyPlaceBlock(world, structureBoundingBox, random, 0.7f, 1, 0, n7, Blocks.rail.getStateFromMeta(this.getMetadataWithOffset(Blocks.rail, 0)));
                    }
                    ++n7;
                }
            }
            return true;
        }
    }
}

