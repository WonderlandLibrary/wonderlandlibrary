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
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class StructureStrongholdPieces {
    static int totalWeight;
    private static List<PieceWeight> structurePieceList;
    private static final Stones strongholdStones;
    private static final PieceWeight[] pieceWeightArray;
    private static Class<? extends Stronghold> strongComponentType;

    private static StructureComponent func_175953_c(Stairs2 stairs2, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
        if (n4 > 50) {
            return null;
        }
        if (Math.abs(n - stairs2.getBoundingBox().minX) <= 112 && Math.abs(n3 - stairs2.getBoundingBox().minZ) <= 112) {
            Stronghold stronghold = StructureStrongholdPieces.func_175955_b(stairs2, list, random, n, n2, n3, enumFacing, n4 + 1);
            if (stronghold != null) {
                list.add(stronghold);
                stairs2.field_75026_c.add(stronghold);
            }
            return stronghold;
        }
        return null;
    }

    static /* synthetic */ Class access$1() {
        return strongComponentType;
    }

    static {
        pieceWeightArray = new PieceWeight[]{new PieceWeight(Straight.class, 40, 0), new PieceWeight(Prison.class, 5, 5), new PieceWeight(LeftTurn.class, 20, 0), new PieceWeight(RightTurn.class, 20, 0), new PieceWeight(RoomCrossing.class, 10, 6), new PieceWeight(StairsStraight.class, 5, 5), new PieceWeight(Stairs.class, 5, 5), new PieceWeight(Crossing.class, 5, 4), new PieceWeight(ChestCorridor.class, 5, 4), new PieceWeight(Library.class, 10, 2){

            @Override
            public boolean canSpawnMoreStructuresOfType(int n) {
                return super.canSpawnMoreStructuresOfType(n) && n > 4;
            }
        }, new PieceWeight(PortalRoom.class, 20, 1){

            @Override
            public boolean canSpawnMoreStructuresOfType(int n) {
                return super.canSpawnMoreStructuresOfType(n) && n > 5;
            }
        }};
        strongholdStones = new Stones();
    }

    private static boolean canAddStructurePieces() {
        boolean bl = false;
        totalWeight = 0;
        for (PieceWeight pieceWeight : structurePieceList) {
            if (pieceWeight.instancesLimit > 0 && pieceWeight.instancesSpawned < pieceWeight.instancesLimit) {
                bl = true;
            }
            totalWeight += pieceWeight.pieceWeight;
        }
        return bl;
    }

    private static Stronghold func_175954_a(Class<? extends Stronghold> clazz, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
        Stronghold stronghold = null;
        if (clazz == Straight.class) {
            stronghold = Straight.func_175862_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Prison.class) {
            stronghold = Prison.func_175860_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == LeftTurn.class) {
            stronghold = LeftTurn.func_175867_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == RightTurn.class) {
            stronghold = RightTurn.func_175867_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == RoomCrossing.class) {
            stronghold = RoomCrossing.func_175859_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == StairsStraight.class) {
            stronghold = StairsStraight.func_175861_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Stairs.class) {
            stronghold = Stairs.func_175863_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Crossing.class) {
            stronghold = Crossing.func_175866_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == ChestCorridor.class) {
            stronghold = ChestCorridor.func_175868_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Library.class) {
            stronghold = Library.func_175864_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == PortalRoom.class) {
            stronghold = PortalRoom.func_175865_a(list, random, n, n2, n3, enumFacing, n4);
        }
        return stronghold;
    }

    public static void prepareStructurePieces() {
        structurePieceList = Lists.newArrayList();
        PieceWeight[] pieceWeightArray = StructureStrongholdPieces.pieceWeightArray;
        int n = StructureStrongholdPieces.pieceWeightArray.length;
        int n2 = 0;
        while (n2 < n) {
            PieceWeight pieceWeight = pieceWeightArray[n2];
            pieceWeight.instancesSpawned = 0;
            structurePieceList.add(pieceWeight);
            ++n2;
        }
        strongComponentType = null;
    }

    private static Stronghold func_175955_b(Stairs2 stairs2, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
        if (!StructureStrongholdPieces.canAddStructurePieces()) {
            return null;
        }
        if (strongComponentType != null) {
            Stronghold stronghold = StructureStrongholdPieces.func_175954_a(strongComponentType, list, random, n, n2, n3, enumFacing, n4);
            strongComponentType = null;
            if (stronghold != null) {
                return stronghold;
            }
        }
        int n5 = 0;
        block0: while (n5 < 5) {
            ++n5;
            int n6 = random.nextInt(totalWeight);
            for (PieceWeight pieceWeight : structurePieceList) {
                if ((n6 -= pieceWeight.pieceWeight) >= 0) continue;
                if (!pieceWeight.canSpawnMoreStructuresOfType(n4) || pieceWeight == stairs2.strongholdPieceWeight) continue block0;
                Stronghold stronghold = StructureStrongholdPieces.func_175954_a(pieceWeight.pieceClass, list, random, n, n2, n3, enumFacing, n4);
                if (stronghold == null) continue;
                ++pieceWeight.instancesSpawned;
                stairs2.strongholdPieceWeight = pieceWeight;
                if (!pieceWeight.canSpawnMoreStructures()) {
                    structurePieceList.remove(pieceWeight);
                }
                return stronghold;
            }
        }
        StructureBoundingBox structureBoundingBox = Corridor.func_175869_a(list, random, n, n2, n3, enumFacing);
        if (structureBoundingBox != null && structureBoundingBox.minY > 1) {
            return new Corridor(n4, random, structureBoundingBox, enumFacing);
        }
        return null;
    }

    public static void registerStrongholdPieces() {
        MapGenStructureIO.registerStructureComponent(ChestCorridor.class, "SHCC");
        MapGenStructureIO.registerStructureComponent(Corridor.class, "SHFC");
        MapGenStructureIO.registerStructureComponent(Crossing.class, "SH5C");
        MapGenStructureIO.registerStructureComponent(LeftTurn.class, "SHLT");
        MapGenStructureIO.registerStructureComponent(Library.class, "SHLi");
        MapGenStructureIO.registerStructureComponent(PortalRoom.class, "SHPR");
        MapGenStructureIO.registerStructureComponent(Prison.class, "SHPH");
        MapGenStructureIO.registerStructureComponent(RightTurn.class, "SHRT");
        MapGenStructureIO.registerStructureComponent(RoomCrossing.class, "SHRC");
        MapGenStructureIO.registerStructureComponent(Stairs.class, "SHSD");
        MapGenStructureIO.registerStructureComponent(Stairs2.class, "SHStart");
        MapGenStructureIO.registerStructureComponent(Straight.class, "SHS");
        MapGenStructureIO.registerStructureComponent(StairsStraight.class, "SHSSD");
    }

    public static class Stairs2
    extends Stairs {
        public PortalRoom strongholdPortalRoom;
        public PieceWeight strongholdPieceWeight;
        public List<StructureComponent> field_75026_c = Lists.newArrayList();

        public Stairs2(int n, Random random, int n2, int n3) {
            super(0, random, n2, n3);
        }

        public Stairs2() {
        }

        @Override
        public BlockPos getBoundingBoxCenter() {
            return this.strongholdPortalRoom != null ? this.strongholdPortalRoom.getBoundingBoxCenter() : super.getBoundingBoxCenter();
        }
    }

    public static class Stairs
    extends Stronghold {
        private boolean field_75024_a;

        public Stairs() {
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setBoolean("Source", this.field_75024_a);
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
                return false;
            }
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 0, 0, 0, 4, 10, 4, true, random, strongholdStones);
            this.placeDoor(world, random, structureBoundingBox, this.field_143013_d, 1, 7, 0);
            this.placeDoor(world, random, structureBoundingBox, Stronghold.Door.OPENING, 1, 1, 4);
            this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 2, 6, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 5, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 6, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 5, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 4, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 5, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 2, 4, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3, 3, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 4, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3, 3, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3, 2, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 3, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 2, 2, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 1, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 2, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 1, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 1, 3, structureBoundingBox);
            return true;
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            if (this.field_75024_a) {
                strongComponentType = Crossing.class;
            }
            this.getNextComponentNormal((Stairs2)structureComponent, list, random, 1, 1);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.field_75024_a = nBTTagCompound.getBoolean("Source");
        }

        public Stairs(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.field_75024_a = false;
            this.coordBaseMode = enumFacing;
            this.field_143013_d = this.getRandomDoor(random);
            this.boundingBox = structureBoundingBox;
        }

        public Stairs(int n, Random random, int n2, int n3) {
            super(n);
            this.field_75024_a = true;
            this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(random);
            this.field_143013_d = Stronghold.Door.OPENING;
            switch (this.coordBaseMode) {
                case NORTH: 
                case SOUTH: {
                    this.boundingBox = new StructureBoundingBox(n2, 64, n3, n2 + 5 - 1, 74, n3 + 5 - 1);
                    break;
                }
                default: {
                    this.boundingBox = new StructureBoundingBox(n2, 64, n3, n2 + 5 - 1, 74, n3 + 5 - 1);
                }
            }
        }

        public static Stairs func_175863_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, -7, 0, 5, 11, 5, enumFacing);
            return Stairs.canStrongholdGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Stairs(n4, random, structureBoundingBox, enumFacing) : null;
        }
    }

    public static class RightTurn
    extends LeftTurn {
        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
                this.getNextComponentX((Stairs2)structureComponent, list, random, 1, 1);
            } else {
                this.getNextComponentZ((Stairs2)structureComponent, list, random, 1, 1);
            }
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
                return false;
            }
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 0, 0, 0, 4, 4, 4, true, random, strongholdStones);
            this.placeDoor(world, random, structureBoundingBox, this.field_143013_d, 1, 1, 0);
            if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
                this.fillWithBlocks(world, structureBoundingBox, 0, 1, 1, 0, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            } else {
                this.fillWithBlocks(world, structureBoundingBox, 4, 1, 1, 4, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            return true;
        }
    }

    public static class ChestCorridor
    extends Stronghold {
        private static final List<WeightedRandomChestContent> strongholdChestContents = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.ender_pearl, 0, 1, 1, 10), new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_helmet, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_leggings, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_boots, 0, 1, 1, 5), new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 1), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)});
        private boolean hasMadeChest;

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setBoolean("Chest", this.hasMadeChest);
        }

        public ChestCorridor() {
        }

        public ChestCorridor(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.field_143013_d = this.getRandomDoor(random);
            this.boundingBox = structureBoundingBox;
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
                return false;
            }
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 0, 0, 0, 4, 4, 6, true, random, strongholdStones);
            this.placeDoor(world, random, structureBoundingBox, this.field_143013_d, 1, 1, 0);
            this.placeDoor(world, random, structureBoundingBox, Stronghold.Door.OPENING, 1, 1, 6);
            this.fillWithBlocks(world, structureBoundingBox, 3, 1, 2, 3, 1, 4, Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
            this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 1, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 5, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 4, structureBoundingBox);
            int n = 2;
            while (n <= 4) {
                this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 2, 1, n, structureBoundingBox);
                ++n;
            }
            if (!this.hasMadeChest && structureBoundingBox.isVecInside(new BlockPos(this.getXWithOffset(3, 3), this.getYWithOffset(2), this.getZWithOffset(3, 3)))) {
                this.hasMadeChest = true;
                this.generateChestContents(world, structureBoundingBox, random, 3, 2, 3, WeightedRandomChestContent.func_177629_a(strongholdChestContents, Items.enchanted_book.getRandom(random)), 2 + random.nextInt(2));
            }
            return true;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.hasMadeChest = nBTTagCompound.getBoolean("Chest");
        }

        public static ChestCorridor func_175868_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, -1, 0, 5, 5, 7, enumFacing);
            return ChestCorridor.canStrongholdGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new ChestCorridor(n4, random, structureBoundingBox, enumFacing) : null;
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((Stairs2)structureComponent, list, random, 1, 1);
        }
    }

    public static class Crossing
    extends Stronghold {
        private boolean field_74999_h;
        private boolean field_74996_b;
        private boolean field_74995_d;
        private boolean field_74997_c;

        public static Crossing func_175866_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -4, -3, 0, 10, 9, 11, enumFacing);
            return Crossing.canStrongholdGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Crossing(n4, random, structureBoundingBox, enumFacing) : null;
        }

        public Crossing() {
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setBoolean("leftLow", this.field_74996_b);
            nBTTagCompound.setBoolean("leftHigh", this.field_74997_c);
            nBTTagCompound.setBoolean("rightLow", this.field_74995_d);
            nBTTagCompound.setBoolean("rightHigh", this.field_74999_h);
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
                return false;
            }
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 0, 0, 0, 9, 8, 10, true, random, strongholdStones);
            this.placeDoor(world, random, structureBoundingBox, this.field_143013_d, 4, 3, 0);
            if (this.field_74996_b) {
                this.fillWithBlocks(world, structureBoundingBox, 0, 3, 1, 0, 5, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            if (this.field_74995_d) {
                this.fillWithBlocks(world, structureBoundingBox, 9, 3, 1, 9, 5, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            if (this.field_74997_c) {
                this.fillWithBlocks(world, structureBoundingBox, 0, 5, 7, 0, 7, 9, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            if (this.field_74999_h) {
                this.fillWithBlocks(world, structureBoundingBox, 9, 5, 7, 9, 7, 9, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            this.fillWithBlocks(world, structureBoundingBox, 5, 1, 10, 7, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 1, 2, 1, 8, 2, 6, false, random, strongholdStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 4, 1, 5, 4, 4, 9, false, random, strongholdStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 8, 1, 5, 8, 4, 9, false, random, strongholdStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 1, 4, 7, 3, 4, 9, false, random, strongholdStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 1, 3, 5, 3, 3, 6, false, random, strongholdStones);
            this.fillWithBlocks(world, structureBoundingBox, 1, 3, 4, 3, 3, 4, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 4, 6, 3, 4, 6, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 5, 1, 7, 7, 1, 8, false, random, strongholdStones);
            this.fillWithBlocks(world, structureBoundingBox, 5, 1, 9, 7, 1, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 2, 7, 7, 2, 7, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 5, 7, 4, 5, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 5, 7, 8, 5, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 5, 7, 7, 5, 9, Blocks.double_stone_slab.getDefaultState(), Blocks.double_stone_slab.getDefaultState(), false);
            this.setBlockState(world, Blocks.torch.getDefaultState(), 6, 5, 6, structureBoundingBox);
            return true;
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            int n = 3;
            int n2 = 5;
            if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.NORTH) {
                n = 8 - n;
                n2 = 8 - n2;
            }
            this.getNextComponentNormal((Stairs2)structureComponent, list, random, 5, 1);
            if (this.field_74996_b) {
                this.getNextComponentX((Stairs2)structureComponent, list, random, n, 1);
            }
            if (this.field_74997_c) {
                this.getNextComponentX((Stairs2)structureComponent, list, random, n2, 7);
            }
            if (this.field_74995_d) {
                this.getNextComponentZ((Stairs2)structureComponent, list, random, n, 1);
            }
            if (this.field_74999_h) {
                this.getNextComponentZ((Stairs2)structureComponent, list, random, n2, 7);
            }
        }

        public Crossing(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.field_143013_d = this.getRandomDoor(random);
            this.boundingBox = structureBoundingBox;
            this.field_74996_b = random.nextBoolean();
            this.field_74997_c = random.nextBoolean();
            this.field_74995_d = random.nextBoolean();
            this.field_74999_h = random.nextInt(3) > 0;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.field_74996_b = nBTTagCompound.getBoolean("leftLow");
            this.field_74997_c = nBTTagCompound.getBoolean("leftHigh");
            this.field_74995_d = nBTTagCompound.getBoolean("rightLow");
            this.field_74999_h = nBTTagCompound.getBoolean("rightHigh");
        }
    }

    public static class Straight
    extends Stronghold {
        private boolean expandsZ;
        private boolean expandsX;

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setBoolean("Left", this.expandsX);
            nBTTagCompound.setBoolean("Right", this.expandsZ);
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((Stairs2)structureComponent, list, random, 1, 1);
            if (this.expandsX) {
                this.getNextComponentX((Stairs2)structureComponent, list, random, 1, 2);
            }
            if (this.expandsZ) {
                this.getNextComponentZ((Stairs2)structureComponent, list, random, 1, 2);
            }
        }

        public static Straight func_175862_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, -1, 0, 5, 5, 7, enumFacing);
            return Straight.canStrongholdGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Straight(n4, random, structureBoundingBox, enumFacing) : null;
        }

        public Straight() {
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.expandsX = nBTTagCompound.getBoolean("Left");
            this.expandsZ = nBTTagCompound.getBoolean("Right");
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
                return false;
            }
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 0, 0, 0, 4, 4, 6, true, random, strongholdStones);
            this.placeDoor(world, random, structureBoundingBox, this.field_143013_d, 1, 1, 0);
            this.placeDoor(world, random, structureBoundingBox, Stronghold.Door.OPENING, 1, 1, 6);
            this.randomlyPlaceBlock(world, structureBoundingBox, random, 0.1f, 1, 2, 1, Blocks.torch.getDefaultState());
            this.randomlyPlaceBlock(world, structureBoundingBox, random, 0.1f, 3, 2, 1, Blocks.torch.getDefaultState());
            this.randomlyPlaceBlock(world, structureBoundingBox, random, 0.1f, 1, 2, 5, Blocks.torch.getDefaultState());
            this.randomlyPlaceBlock(world, structureBoundingBox, random, 0.1f, 3, 2, 5, Blocks.torch.getDefaultState());
            if (this.expandsX) {
                this.fillWithBlocks(world, structureBoundingBox, 0, 1, 2, 0, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            if (this.expandsZ) {
                this.fillWithBlocks(world, structureBoundingBox, 4, 1, 2, 4, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            return true;
        }

        public Straight(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.field_143013_d = this.getRandomDoor(random);
            this.boundingBox = structureBoundingBox;
            this.expandsX = random.nextInt(2) == 0;
            this.expandsZ = random.nextInt(2) == 0;
        }
    }

    public static class Library
    extends Stronghold {
        private boolean isLargeRoom;
        private static final List<WeightedRandomChestContent> strongholdLibraryChestContents = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.book, 0, 1, 3, 20), new WeightedRandomChestContent(Items.paper, 0, 2, 7, 20), new WeightedRandomChestContent(Items.map, 0, 1, 1, 1), new WeightedRandomChestContent(Items.compass, 0, 1, 1, 1)});

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
                return false;
            }
            int n = 11;
            if (!this.isLargeRoom) {
                n = 6;
            }
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 0, 0, 0, 13, n - 1, 14, true, random, strongholdStones);
            this.placeDoor(world, random, structureBoundingBox, this.field_143013_d, 4, 1, 0);
            this.func_175805_a(world, structureBoundingBox, random, 0.07f, 2, 1, 1, 11, 4, 13, Blocks.web.getDefaultState(), Blocks.web.getDefaultState(), false);
            boolean bl = true;
            int n2 = 12;
            int n3 = 1;
            while (n3 <= 13) {
                if ((n3 - 1) % 4 == 0) {
                    this.fillWithBlocks(world, structureBoundingBox, 1, 1, n3, 1, 4, n3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                    this.fillWithBlocks(world, structureBoundingBox, 12, 1, n3, 12, 4, n3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                    this.setBlockState(world, Blocks.torch.getDefaultState(), 2, 3, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.torch.getDefaultState(), 11, 3, n3, structureBoundingBox);
                    if (this.isLargeRoom) {
                        this.fillWithBlocks(world, structureBoundingBox, 1, 6, n3, 1, 9, n3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                        this.fillWithBlocks(world, structureBoundingBox, 12, 6, n3, 12, 9, n3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                    }
                } else {
                    this.fillWithBlocks(world, structureBoundingBox, 1, 1, n3, 1, 4, n3, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                    this.fillWithBlocks(world, structureBoundingBox, 12, 1, n3, 12, 4, n3, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                    if (this.isLargeRoom) {
                        this.fillWithBlocks(world, structureBoundingBox, 1, 6, n3, 1, 9, n3, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                        this.fillWithBlocks(world, structureBoundingBox, 12, 6, n3, 12, 9, n3, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                    }
                }
                ++n3;
            }
            n3 = 3;
            while (n3 < 12) {
                this.fillWithBlocks(world, structureBoundingBox, 3, 1, n3, 4, 3, n3, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 6, 1, n3, 7, 3, n3, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 9, 1, n3, 10, 3, n3, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                n3 += 2;
            }
            if (this.isLargeRoom) {
                this.fillWithBlocks(world, structureBoundingBox, 1, 5, 1, 3, 5, 13, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 10, 5, 1, 12, 5, 13, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 4, 5, 1, 9, 5, 2, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 4, 5, 12, 9, 5, 13, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                this.setBlockState(world, Blocks.planks.getDefaultState(), 9, 5, 11, structureBoundingBox);
                this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 5, 11, structureBoundingBox);
                this.setBlockState(world, Blocks.planks.getDefaultState(), 9, 5, 10, structureBoundingBox);
                this.fillWithBlocks(world, structureBoundingBox, 3, 6, 2, 3, 6, 12, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 10, 6, 2, 10, 6, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 4, 6, 2, 9, 6, 2, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 4, 6, 12, 8, 6, 12, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 9, 6, 11, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 8, 6, 11, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 9, 6, 10, structureBoundingBox);
                n3 = this.getMetadataWithOffset(Blocks.ladder, 3);
                this.setBlockState(world, Blocks.ladder.getStateFromMeta(n3), 10, 1, 13, structureBoundingBox);
                this.setBlockState(world, Blocks.ladder.getStateFromMeta(n3), 10, 2, 13, structureBoundingBox);
                this.setBlockState(world, Blocks.ladder.getStateFromMeta(n3), 10, 3, 13, structureBoundingBox);
                this.setBlockState(world, Blocks.ladder.getStateFromMeta(n3), 10, 4, 13, structureBoundingBox);
                this.setBlockState(world, Blocks.ladder.getStateFromMeta(n3), 10, 5, 13, structureBoundingBox);
                this.setBlockState(world, Blocks.ladder.getStateFromMeta(n3), 10, 6, 13, structureBoundingBox);
                this.setBlockState(world, Blocks.ladder.getStateFromMeta(n3), 10, 7, 13, structureBoundingBox);
                int n4 = 7;
                int n5 = 7;
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), n4 - 1, 9, n5, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), n4, 9, n5, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), n4 - 1, 8, n5, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), n4, 8, n5, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), n4 - 1, 7, n5, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), n4, 7, n5, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), n4 - 2, 7, n5, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), n4 + 1, 7, n5, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), n4 - 1, 7, n5 - 1, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), n4 - 1, 7, n5 + 1, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), n4, 7, n5 - 1, structureBoundingBox);
                this.setBlockState(world, Blocks.oak_fence.getDefaultState(), n4, 7, n5 + 1, structureBoundingBox);
                this.setBlockState(world, Blocks.torch.getDefaultState(), n4 - 2, 8, n5, structureBoundingBox);
                this.setBlockState(world, Blocks.torch.getDefaultState(), n4 + 1, 8, n5, structureBoundingBox);
                this.setBlockState(world, Blocks.torch.getDefaultState(), n4 - 1, 8, n5 - 1, structureBoundingBox);
                this.setBlockState(world, Blocks.torch.getDefaultState(), n4 - 1, 8, n5 + 1, structureBoundingBox);
                this.setBlockState(world, Blocks.torch.getDefaultState(), n4, 8, n5 - 1, structureBoundingBox);
                this.setBlockState(world, Blocks.torch.getDefaultState(), n4, 8, n5 + 1, structureBoundingBox);
            }
            this.generateChestContents(world, structureBoundingBox, random, 3, 3, 5, WeightedRandomChestContent.func_177629_a(strongholdLibraryChestContents, Items.enchanted_book.getRandom(random, 1, 5, 2)), 1 + random.nextInt(4));
            if (this.isLargeRoom) {
                this.setBlockState(world, Blocks.air.getDefaultState(), 12, 9, 1, structureBoundingBox);
                this.generateChestContents(world, structureBoundingBox, random, 12, 8, 1, WeightedRandomChestContent.func_177629_a(strongholdLibraryChestContents, Items.enchanted_book.getRandom(random, 1, 5, 2)), 1 + random.nextInt(4));
            }
            return true;
        }

        public Library(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.field_143013_d = this.getRandomDoor(random);
            this.boundingBox = structureBoundingBox;
            this.isLargeRoom = structureBoundingBox.getYSize() > 6;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.isLargeRoom = nBTTagCompound.getBoolean("Tall");
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setBoolean("Tall", this.isLargeRoom);
        }

        public static Library func_175864_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -4, -1, 0, 14, 11, 15, enumFacing);
            if (!(Library.canStrongholdGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null || Library.canStrongholdGoDeeper(structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -4, -1, 0, 14, 6, 15, enumFacing)) && StructureComponent.findIntersecting(list, structureBoundingBox) == null)) {
                return null;
            }
            return new Library(n4, random, structureBoundingBox, enumFacing);
        }

        public Library() {
        }
    }

    static abstract class Stronghold
    extends StructureComponent {
        protected Door field_143013_d = Door.OPENING;

        protected StructureComponent getNextComponentZ(Stairs2 stairs2, List<StructureComponent> list, Random random, int n, int n2) {
            if (this.coordBaseMode != null) {
                switch (this.coordBaseMode) {
                    case NORTH: {
                        return StructureStrongholdPieces.func_175953_c(stairs2, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + n, this.boundingBox.minZ + n2, EnumFacing.EAST, this.getComponentType());
                    }
                    case SOUTH: {
                        return StructureStrongholdPieces.func_175953_c(stairs2, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + n, this.boundingBox.minZ + n2, EnumFacing.EAST, this.getComponentType());
                    }
                    case WEST: {
                        return StructureStrongholdPieces.func_175953_c(stairs2, list, random, this.boundingBox.minX + n2, this.boundingBox.minY + n, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                    }
                    case EAST: {
                        return StructureStrongholdPieces.func_175953_c(stairs2, list, random, this.boundingBox.minX + n2, this.boundingBox.minY + n, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                    }
                }
            }
            return null;
        }

        protected Stronghold(int n) {
            super(n);
        }

        protected Door getRandomDoor(Random random) {
            int n = random.nextInt(5);
            switch (n) {
                default: {
                    return Door.OPENING;
                }
                case 2: {
                    return Door.WOOD_DOOR;
                }
                case 3: {
                    return Door.GRATES;
                }
                case 4: 
            }
            return Door.IRON_DOOR;
        }

        protected void placeDoor(World world, Random random, StructureBoundingBox structureBoundingBox, Door door, int n, int n2, int n3) {
            switch (door) {
                default: {
                    this.fillWithBlocks(world, structureBoundingBox, n, n2, n3, n + 3 - 1, n2 + 3 - 1, n3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    break;
                }
                case WOOD_DOOR: {
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), n, n2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), n, n2 + 1, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), n, n2 + 2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), n + 1, n2 + 2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), n + 2, n2 + 2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), n + 2, n2 + 1, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), n + 2, n2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.oak_door.getDefaultState(), n + 1, n2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.oak_door.getStateFromMeta(8), n + 1, n2 + 1, n3, structureBoundingBox);
                    break;
                }
                case GRATES: {
                    this.setBlockState(world, Blocks.air.getDefaultState(), n + 1, n2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.air.getDefaultState(), n + 1, n2 + 1, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.iron_bars.getDefaultState(), n, n2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.iron_bars.getDefaultState(), n, n2 + 1, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.iron_bars.getDefaultState(), n, n2 + 2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.iron_bars.getDefaultState(), n + 1, n2 + 2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.iron_bars.getDefaultState(), n + 2, n2 + 2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.iron_bars.getDefaultState(), n + 2, n2 + 1, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.iron_bars.getDefaultState(), n + 2, n2, n3, structureBoundingBox);
                    break;
                }
                case IRON_DOOR: {
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), n, n2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), n, n2 + 1, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), n, n2 + 2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), n + 1, n2 + 2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), n + 2, n2 + 2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), n + 2, n2 + 1, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), n + 2, n2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.iron_door.getDefaultState(), n + 1, n2, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.iron_door.getStateFromMeta(8), n + 1, n2 + 1, n3, structureBoundingBox);
                    this.setBlockState(world, Blocks.stone_button.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_button, 4)), n + 2, n2 + 1, n3 + 1, structureBoundingBox);
                    this.setBlockState(world, Blocks.stone_button.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_button, 3)), n + 2, n2 + 1, n3 - 1, structureBoundingBox);
                }
            }
        }

        public Stronghold() {
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            nBTTagCompound.setString("EntryDoor", this.field_143013_d.name());
        }

        protected StructureComponent getNextComponentX(Stairs2 stairs2, List<StructureComponent> list, Random random, int n, int n2) {
            if (this.coordBaseMode != null) {
                switch (this.coordBaseMode) {
                    case NORTH: {
                        return StructureStrongholdPieces.func_175953_c(stairs2, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + n, this.boundingBox.minZ + n2, EnumFacing.WEST, this.getComponentType());
                    }
                    case SOUTH: {
                        return StructureStrongholdPieces.func_175953_c(stairs2, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + n, this.boundingBox.minZ + n2, EnumFacing.WEST, this.getComponentType());
                    }
                    case WEST: {
                        return StructureStrongholdPieces.func_175953_c(stairs2, list, random, this.boundingBox.minX + n2, this.boundingBox.minY + n, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    }
                    case EAST: {
                        return StructureStrongholdPieces.func_175953_c(stairs2, list, random, this.boundingBox.minX + n2, this.boundingBox.minY + n, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    }
                }
            }
            return null;
        }

        protected StructureComponent getNextComponentNormal(Stairs2 stairs2, List<StructureComponent> list, Random random, int n, int n2) {
            if (this.coordBaseMode != null) {
                switch (this.coordBaseMode) {
                    case NORTH: {
                        return StructureStrongholdPieces.func_175953_c(stairs2, list, random, this.boundingBox.minX + n, this.boundingBox.minY + n2, this.boundingBox.minZ - 1, this.coordBaseMode, this.getComponentType());
                    }
                    case SOUTH: {
                        return StructureStrongholdPieces.func_175953_c(stairs2, list, random, this.boundingBox.minX + n, this.boundingBox.minY + n2, this.boundingBox.maxZ + 1, this.coordBaseMode, this.getComponentType());
                    }
                    case WEST: {
                        return StructureStrongholdPieces.func_175953_c(stairs2, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + n2, this.boundingBox.minZ + n, this.coordBaseMode, this.getComponentType());
                    }
                    case EAST: {
                        return StructureStrongholdPieces.func_175953_c(stairs2, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + n2, this.boundingBox.minZ + n, this.coordBaseMode, this.getComponentType());
                    }
                }
            }
            return null;
        }

        protected static boolean canStrongholdGoDeeper(StructureBoundingBox structureBoundingBox) {
            return structureBoundingBox != null && structureBoundingBox.minY > 10;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            this.field_143013_d = Door.valueOf(nBTTagCompound.getString("EntryDoor"));
        }

        public static enum Door {
            OPENING,
            WOOD_DOOR,
            GRATES,
            IRON_DOOR;

        }
    }

    public static class RoomCrossing
    extends Stronghold {
        protected int roomType;
        private static final List<WeightedRandomChestContent> strongholdRoomCrossingChestContents = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1)});

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((Stairs2)structureComponent, list, random, 4, 1);
            this.getNextComponentX((Stairs2)structureComponent, list, random, 1, 4);
            this.getNextComponentZ((Stairs2)structureComponent, list, random, 1, 4);
        }

        public static RoomCrossing func_175859_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -4, -1, 0, 11, 7, 11, enumFacing);
            return RoomCrossing.canStrongholdGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new RoomCrossing(n4, random, structureBoundingBox, enumFacing) : null;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.roomType = nBTTagCompound.getInteger("Type");
        }

        public RoomCrossing() {
        }

        public RoomCrossing(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.field_143013_d = this.getRandomDoor(random);
            this.boundingBox = structureBoundingBox;
            this.roomType = random.nextInt(5);
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setInteger("Type", this.roomType);
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
                return false;
            }
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 0, 0, 0, 10, 6, 10, true, random, strongholdStones);
            this.placeDoor(world, random, structureBoundingBox, this.field_143013_d, 4, 1, 0);
            this.fillWithBlocks(world, structureBoundingBox, 4, 1, 10, 6, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 4, 0, 3, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 10, 1, 4, 10, 3, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            switch (this.roomType) {
                case 0: {
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 5, 1, 5, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 5, 2, 5, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 5, 3, 5, structureBoundingBox);
                    this.setBlockState(world, Blocks.torch.getDefaultState(), 4, 3, 5, structureBoundingBox);
                    this.setBlockState(world, Blocks.torch.getDefaultState(), 6, 3, 5, structureBoundingBox);
                    this.setBlockState(world, Blocks.torch.getDefaultState(), 5, 3, 4, structureBoundingBox);
                    this.setBlockState(world, Blocks.torch.getDefaultState(), 5, 3, 6, structureBoundingBox);
                    this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 4, 1, 4, structureBoundingBox);
                    this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 4, 1, 5, structureBoundingBox);
                    this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 4, 1, 6, structureBoundingBox);
                    this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 6, 1, 4, structureBoundingBox);
                    this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 6, 1, 5, structureBoundingBox);
                    this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 6, 1, 6, structureBoundingBox);
                    this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 5, 1, 4, structureBoundingBox);
                    this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 5, 1, 6, structureBoundingBox);
                    break;
                }
                case 1: {
                    int n = 0;
                    while (n < 5) {
                        this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3, 1, 3 + n, structureBoundingBox);
                        this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 7, 1, 3 + n, structureBoundingBox);
                        this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3 + n, 1, 3, structureBoundingBox);
                        this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3 + n, 1, 7, structureBoundingBox);
                        ++n;
                    }
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 5, 1, 5, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 5, 2, 5, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 5, 3, 5, structureBoundingBox);
                    this.setBlockState(world, Blocks.flowing_water.getDefaultState(), 5, 4, 5, structureBoundingBox);
                    break;
                }
                case 2: {
                    int n = 1;
                    while (n <= 9) {
                        this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 1, 3, n, structureBoundingBox);
                        this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 9, 3, n, structureBoundingBox);
                        ++n;
                    }
                    n = 1;
                    while (n <= 9) {
                        this.setBlockState(world, Blocks.cobblestone.getDefaultState(), n, 3, 1, structureBoundingBox);
                        this.setBlockState(world, Blocks.cobblestone.getDefaultState(), n, 3, 9, structureBoundingBox);
                        ++n;
                    }
                    this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 5, 1, 4, structureBoundingBox);
                    this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 5, 1, 6, structureBoundingBox);
                    this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 5, 3, 4, structureBoundingBox);
                    this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 5, 3, 6, structureBoundingBox);
                    this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 1, 5, structureBoundingBox);
                    this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 6, 1, 5, structureBoundingBox);
                    this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 3, 5, structureBoundingBox);
                    this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 6, 3, 5, structureBoundingBox);
                    n = 1;
                    while (n <= 3) {
                        this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, n, 4, structureBoundingBox);
                        this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 6, n, 4, structureBoundingBox);
                        this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, n, 6, structureBoundingBox);
                        this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 6, n, 6, structureBoundingBox);
                        ++n;
                    }
                    this.setBlockState(world, Blocks.torch.getDefaultState(), 5, 3, 5, structureBoundingBox);
                    n = 2;
                    while (n <= 8) {
                        this.setBlockState(world, Blocks.planks.getDefaultState(), 2, 3, n, structureBoundingBox);
                        this.setBlockState(world, Blocks.planks.getDefaultState(), 3, 3, n, structureBoundingBox);
                        if (n <= 3 || n >= 7) {
                            this.setBlockState(world, Blocks.planks.getDefaultState(), 4, 3, n, structureBoundingBox);
                            this.setBlockState(world, Blocks.planks.getDefaultState(), 5, 3, n, structureBoundingBox);
                            this.setBlockState(world, Blocks.planks.getDefaultState(), 6, 3, n, structureBoundingBox);
                        }
                        this.setBlockState(world, Blocks.planks.getDefaultState(), 7, 3, n, structureBoundingBox);
                        this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 3, n, structureBoundingBox);
                        ++n;
                    }
                    this.setBlockState(world, Blocks.ladder.getStateFromMeta(this.getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 1, 3, structureBoundingBox);
                    this.setBlockState(world, Blocks.ladder.getStateFromMeta(this.getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 2, 3, structureBoundingBox);
                    this.setBlockState(world, Blocks.ladder.getStateFromMeta(this.getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 3, 3, structureBoundingBox);
                    this.generateChestContents(world, structureBoundingBox, random, 3, 4, 8, WeightedRandomChestContent.func_177629_a(strongholdRoomCrossingChestContents, Items.enchanted_book.getRandom(random)), 1 + random.nextInt(4));
                }
            }
            return true;
        }
    }

    public static class PortalRoom
    extends Stronghold {
        private boolean hasSpawner;

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 0, 0, 0, 10, 7, 15, false, random, strongholdStones);
            this.placeDoor(world, random, structureBoundingBox, Stronghold.Door.GRATES, 4, 1, 0);
            int n = 6;
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 1, n, 1, 1, n, 14, false, random, strongholdStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 9, n, 1, 9, n, 14, false, random, strongholdStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 2, n, 1, 8, n, 2, false, random, strongholdStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 2, n, 14, 8, n, 14, false, random, strongholdStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 1, 1, 1, 2, 1, 4, false, random, strongholdStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 8, 1, 1, 9, 1, 4, false, random, strongholdStones);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 1, 1, 1, 3, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 9, 1, 1, 9, 1, 3, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 3, 1, 8, 7, 1, 12, false, random, strongholdStones);
            this.fillWithBlocks(world, structureBoundingBox, 4, 1, 9, 6, 1, 11, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
            int n2 = 3;
            while (n2 < 14) {
                this.fillWithBlocks(world, structureBoundingBox, 0, 3, n2, 0, 4, n2, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 10, 3, n2, 10, 4, n2, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
                n2 += 2;
            }
            n2 = 2;
            while (n2 < 9) {
                this.fillWithBlocks(world, structureBoundingBox, n2, 3, 15, n2, 4, 15, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
                n2 += 2;
            }
            n2 = this.getMetadataWithOffset(Blocks.stone_brick_stairs, 3);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 4, 1, 5, 6, 1, 7, false, random, strongholdStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 4, 2, 6, 6, 2, 7, false, random, strongholdStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 4, 3, 7, 6, 3, 7, false, random, strongholdStones);
            int n3 = 4;
            while (n3 <= 6) {
                this.setBlockState(world, Blocks.stone_brick_stairs.getStateFromMeta(n2), n3, 1, 4, structureBoundingBox);
                this.setBlockState(world, Blocks.stone_brick_stairs.getStateFromMeta(n2), n3, 2, 5, structureBoundingBox);
                this.setBlockState(world, Blocks.stone_brick_stairs.getStateFromMeta(n2), n3, 3, 6, structureBoundingBox);
                ++n3;
            }
            n3 = EnumFacing.NORTH.getHorizontalIndex();
            int n4 = EnumFacing.SOUTH.getHorizontalIndex();
            int n5 = EnumFacing.EAST.getHorizontalIndex();
            int n6 = EnumFacing.WEST.getHorizontalIndex();
            if (this.coordBaseMode != null) {
                switch (this.coordBaseMode) {
                    case SOUTH: {
                        n3 = EnumFacing.SOUTH.getHorizontalIndex();
                        n4 = EnumFacing.NORTH.getHorizontalIndex();
                        break;
                    }
                    case WEST: {
                        n3 = EnumFacing.WEST.getHorizontalIndex();
                        n4 = EnumFacing.EAST.getHorizontalIndex();
                        n5 = EnumFacing.SOUTH.getHorizontalIndex();
                        n6 = EnumFacing.NORTH.getHorizontalIndex();
                        break;
                    }
                    case EAST: {
                        n3 = EnumFacing.EAST.getHorizontalIndex();
                        n4 = EnumFacing.WEST.getHorizontalIndex();
                        n5 = EnumFacing.SOUTH.getHorizontalIndex();
                        n6 = EnumFacing.NORTH.getHorizontalIndex();
                    }
                }
            }
            this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(n3).withProperty(BlockEndPortalFrame.EYE, random.nextFloat() > 0.9f), 4, 3, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(n3).withProperty(BlockEndPortalFrame.EYE, random.nextFloat() > 0.9f), 5, 3, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(n3).withProperty(BlockEndPortalFrame.EYE, random.nextFloat() > 0.9f), 6, 3, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(n4).withProperty(BlockEndPortalFrame.EYE, random.nextFloat() > 0.9f), 4, 3, 12, structureBoundingBox);
            this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(n4).withProperty(BlockEndPortalFrame.EYE, random.nextFloat() > 0.9f), 5, 3, 12, structureBoundingBox);
            this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(n4).withProperty(BlockEndPortalFrame.EYE, random.nextFloat() > 0.9f), 6, 3, 12, structureBoundingBox);
            this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(n5).withProperty(BlockEndPortalFrame.EYE, random.nextFloat() > 0.9f), 3, 3, 9, structureBoundingBox);
            this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(n5).withProperty(BlockEndPortalFrame.EYE, random.nextFloat() > 0.9f), 3, 3, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(n5).withProperty(BlockEndPortalFrame.EYE, random.nextFloat() > 0.9f), 3, 3, 11, structureBoundingBox);
            this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(n6).withProperty(BlockEndPortalFrame.EYE, random.nextFloat() > 0.9f), 7, 3, 9, structureBoundingBox);
            this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(n6).withProperty(BlockEndPortalFrame.EYE, random.nextFloat() > 0.9f), 7, 3, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(n6).withProperty(BlockEndPortalFrame.EYE, random.nextFloat() > 0.9f), 7, 3, 11, structureBoundingBox);
            if (!this.hasSpawner) {
                n = this.getYWithOffset(3);
                BlockPos blockPos = new BlockPos(this.getXWithOffset(5, 6), n, this.getZWithOffset(5, 6));
                if (structureBoundingBox.isVecInside(blockPos)) {
                    this.hasSpawner = true;
                    world.setBlockState(blockPos, Blocks.mob_spawner.getDefaultState(), 2);
                    TileEntity tileEntity = world.getTileEntity(blockPos);
                    if (tileEntity instanceof TileEntityMobSpawner) {
                        ((TileEntityMobSpawner)tileEntity).getSpawnerBaseLogic().setEntityName("Silverfish");
                    }
                }
            }
            return true;
        }

        public PortalRoom(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.hasSpawner = nBTTagCompound.getBoolean("Mob");
        }

        public PortalRoom() {
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setBoolean("Mob", this.hasSpawner);
        }

        public static PortalRoom func_175865_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -4, -1, 0, 11, 8, 16, enumFacing);
            return PortalRoom.canStrongholdGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new PortalRoom(n4, random, structureBoundingBox, enumFacing) : null;
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            if (structureComponent != null) {
                ((Stairs2)structureComponent).strongholdPortalRoom = this;
            }
        }
    }

    public static class Corridor
    extends Stronghold {
        private int field_74993_a;

        public Corridor() {
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.field_74993_a = nBTTagCompound.getInteger("Steps");
        }

        public static StructureBoundingBox func_175869_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing) {
            int n4 = 3;
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, -1, 0, 5, 5, 4, enumFacing);
            StructureComponent structureComponent = StructureComponent.findIntersecting(list, structureBoundingBox);
            if (structureComponent == null) {
                return null;
            }
            if (structureComponent.getBoundingBox().minY == structureBoundingBox.minY) {
                int n5 = 3;
                while (n5 >= 1) {
                    structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, -1, 0, 5, 5, n5 - 1, enumFacing);
                    if (!structureComponent.getBoundingBox().intersectsWith(structureBoundingBox)) {
                        return StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, -1, 0, 5, 5, n5, enumFacing);
                    }
                    --n5;
                }
            }
            return null;
        }

        public Corridor(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
            this.field_74993_a = enumFacing != EnumFacing.NORTH && enumFacing != EnumFacing.SOUTH ? structureBoundingBox.getXSize() : structureBoundingBox.getZSize();
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
                return false;
            }
            int n = 0;
            while (n < this.field_74993_a) {
                this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 0, 0, n, structureBoundingBox);
                this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 0, n, structureBoundingBox);
                this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 2, 0, n, structureBoundingBox);
                this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3, 0, n, structureBoundingBox);
                this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 4, 0, n, structureBoundingBox);
                int n2 = 1;
                while (n2 <= 3) {
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 0, n2, n, structureBoundingBox);
                    this.setBlockState(world, Blocks.air.getDefaultState(), 1, n2, n, structureBoundingBox);
                    this.setBlockState(world, Blocks.air.getDefaultState(), 2, n2, n, structureBoundingBox);
                    this.setBlockState(world, Blocks.air.getDefaultState(), 3, n2, n, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 4, n2, n, structureBoundingBox);
                    ++n2;
                }
                this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 0, 4, n, structureBoundingBox);
                this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 4, n, structureBoundingBox);
                this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 2, 4, n, structureBoundingBox);
                this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3, 4, n, structureBoundingBox);
                this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 4, 4, n, structureBoundingBox);
                ++n;
            }
            return true;
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setInteger("Steps", this.field_74993_a);
        }
    }

    public static class LeftTurn
    extends Stronghold {
        public LeftTurn() {
        }

        public static LeftTurn func_175867_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, -1, 0, 5, 5, 5, enumFacing);
            return LeftTurn.canStrongholdGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new LeftTurn(n4, random, structureBoundingBox, enumFacing) : null;
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
                this.getNextComponentZ((Stairs2)structureComponent, list, random, 1, 1);
            } else {
                this.getNextComponentX((Stairs2)structureComponent, list, random, 1, 1);
            }
        }

        public LeftTurn(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.field_143013_d = this.getRandomDoor(random);
            this.boundingBox = structureBoundingBox;
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
                return false;
            }
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 0, 0, 0, 4, 4, 4, true, random, strongholdStones);
            this.placeDoor(world, random, structureBoundingBox, this.field_143013_d, 1, 1, 0);
            if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
                this.fillWithBlocks(world, structureBoundingBox, 4, 1, 1, 4, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            } else {
                this.fillWithBlocks(world, structureBoundingBox, 0, 1, 1, 0, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            return true;
        }
    }

    public static class Prison
    extends Stronghold {
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
                return false;
            }
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 0, 0, 0, 8, 4, 10, true, random, strongholdStones);
            this.placeDoor(world, random, structureBoundingBox, this.field_143013_d, 1, 1, 0);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 10, 3, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 4, 1, 1, 4, 3, 1, false, random, strongholdStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 4, 1, 3, 4, 3, 3, false, random, strongholdStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 4, 1, 7, 4, 3, 7, false, random, strongholdStones);
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 4, 1, 9, 4, 3, 9, false, random, strongholdStones);
            this.fillWithBlocks(world, structureBoundingBox, 4, 1, 4, 4, 3, 6, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 1, 5, 7, 3, 5, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
            this.setBlockState(world, Blocks.iron_bars.getDefaultState(), 4, 3, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.iron_bars.getDefaultState(), 4, 3, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3)), 4, 1, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3) + 8), 4, 2, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3)), 4, 1, 8, structureBoundingBox);
            this.setBlockState(world, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3) + 8), 4, 2, 8, structureBoundingBox);
            return true;
        }

        public Prison() {
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((Stairs2)structureComponent, list, random, 1, 1);
        }

        public Prison(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.field_143013_d = this.getRandomDoor(random);
            this.boundingBox = structureBoundingBox;
        }

        public static Prison func_175860_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, -1, 0, 9, 5, 11, enumFacing);
            return Prison.canStrongholdGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Prison(n4, random, structureBoundingBox, enumFacing) : null;
        }
    }

    static class Stones
    extends StructureComponent.BlockSelector {
        private Stones() {
        }

        @Override
        public void selectBlocks(Random random, int n, int n2, int n3, boolean bl) {
            float f;
            this.blockstate = bl ? ((f = random.nextFloat()) < 0.2f ? Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CRACKED_META) : (f < 0.5f ? Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.MOSSY_META) : (f < 0.55f ? Blocks.monster_egg.getStateFromMeta(BlockSilverfish.EnumType.STONEBRICK.getMetadata()) : Blocks.stonebrick.getDefaultState()))) : Blocks.air.getDefaultState();
        }
    }

    public static class StairsStraight
    extends Stronghold {
        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((Stairs2)structureComponent, list, random, 1, 1);
        }

        public static StairsStraight func_175861_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, -7, 0, 5, 11, 8, enumFacing);
            return StairsStraight.canStrongholdGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new StairsStraight(n4, random, structureBoundingBox, enumFacing) : null;
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
                return false;
            }
            this.fillWithRandomizedBlocks(world, structureBoundingBox, 0, 0, 0, 4, 10, 7, true, random, strongholdStones);
            this.placeDoor(world, random, structureBoundingBox, this.field_143013_d, 1, 7, 0);
            this.placeDoor(world, random, structureBoundingBox, Stronghold.Door.OPENING, 1, 1, 7);
            int n = this.getMetadataWithOffset(Blocks.stone_stairs, 2);
            int n2 = 0;
            while (n2 < 6) {
                this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n), 1, 6 - n2, 1 + n2, structureBoundingBox);
                this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n), 2, 6 - n2, 1 + n2, structureBoundingBox);
                this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(n), 3, 6 - n2, 1 + n2, structureBoundingBox);
                if (n2 < 5) {
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 5 - n2, 1 + n2, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 2, 5 - n2, 1 + n2, structureBoundingBox);
                    this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3, 5 - n2, 1 + n2, structureBoundingBox);
                }
                ++n2;
            }
            return true;
        }

        public StairsStraight(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.field_143013_d = this.getRandomDoor(random);
            this.boundingBox = structureBoundingBox;
        }

        public StairsStraight() {
        }
    }

    static class PieceWeight {
        public int instancesSpawned;
        public Class<? extends Stronghold> pieceClass;
        public int instancesLimit;
        public final int pieceWeight;

        public PieceWeight(Class<? extends Stronghold> clazz, int n, int n2) {
            this.pieceClass = clazz;
            this.pieceWeight = n;
            this.instancesLimit = n2;
        }

        public boolean canSpawnMoreStructures() {
            return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
        }

        public boolean canSpawnMoreStructuresOfType(int n) {
            return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
        }
    }
}

