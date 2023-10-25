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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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

public class StructureNetherBridgePieces {
    private static final PieceWeight[] secondaryComponents;
    private static final PieceWeight[] primaryComponents;

    public static void registerNetherFortressPieces() {
        MapGenStructureIO.registerStructureComponent(Crossing3.class, "NeBCr");
        MapGenStructureIO.registerStructureComponent(End.class, "NeBEF");
        MapGenStructureIO.registerStructureComponent(Straight.class, "NeBS");
        MapGenStructureIO.registerStructureComponent(Corridor3.class, "NeCCS");
        MapGenStructureIO.registerStructureComponent(Corridor4.class, "NeCTB");
        MapGenStructureIO.registerStructureComponent(Entrance.class, "NeCE");
        MapGenStructureIO.registerStructureComponent(Crossing2.class, "NeSCSC");
        MapGenStructureIO.registerStructureComponent(Corridor.class, "NeSCLT");
        MapGenStructureIO.registerStructureComponent(Corridor5.class, "NeSC");
        MapGenStructureIO.registerStructureComponent(Corridor2.class, "NeSCRT");
        MapGenStructureIO.registerStructureComponent(NetherStalkRoom.class, "NeCSR");
        MapGenStructureIO.registerStructureComponent(Throne.class, "NeMT");
        MapGenStructureIO.registerStructureComponent(Crossing.class, "NeRC");
        MapGenStructureIO.registerStructureComponent(Stairs.class, "NeSR");
        MapGenStructureIO.registerStructureComponent(Start.class, "NeStart");
    }

    static {
        primaryComponents = new PieceWeight[]{new PieceWeight(Straight.class, 30, 0, true), new PieceWeight(Crossing3.class, 10, 4), new PieceWeight(Crossing.class, 10, 4), new PieceWeight(Stairs.class, 10, 3), new PieceWeight(Throne.class, 5, 2), new PieceWeight(Entrance.class, 5, 1)};
        secondaryComponents = new PieceWeight[]{new PieceWeight(Corridor5.class, 25, 0, true), new PieceWeight(Crossing2.class, 15, 5), new PieceWeight(Corridor2.class, 5, 10), new PieceWeight(Corridor.class, 5, 10), new PieceWeight(Corridor3.class, 10, 3, true), new PieceWeight(Corridor4.class, 7, 2), new PieceWeight(NetherStalkRoom.class, 5, 2)};
    }

    private static Piece func_175887_b(PieceWeight pieceWeight, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
        Class<? extends Piece> clazz = pieceWeight.weightClass;
        Piece piece = null;
        if (clazz == Straight.class) {
            piece = Straight.func_175882_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Crossing3.class) {
            piece = Crossing3.func_175885_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Crossing.class) {
            piece = Crossing.func_175873_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Stairs.class) {
            piece = Stairs.func_175872_a(list, random, n, n2, n3, n4, enumFacing);
        } else if (clazz == Throne.class) {
            piece = Throne.func_175874_a(list, random, n, n2, n3, n4, enumFacing);
        } else if (clazz == Entrance.class) {
            piece = Entrance.func_175881_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Corridor5.class) {
            piece = Corridor5.func_175877_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Corridor2.class) {
            piece = Corridor2.func_175876_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Corridor.class) {
            piece = Corridor.func_175879_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Corridor3.class) {
            piece = Corridor3.func_175883_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Corridor4.class) {
            piece = Corridor4.func_175880_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == Crossing2.class) {
            piece = Crossing2.func_175878_a(list, random, n, n2, n3, enumFacing, n4);
        } else if (clazz == NetherStalkRoom.class) {
            piece = NetherStalkRoom.func_175875_a(list, random, n, n2, n3, enumFacing, n4);
        }
        return piece;
    }

    public static class Corridor
    extends Piece {
        private boolean field_111021_b;

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentX((Start)structureComponent, list, random, 0, 1, true);
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 4, 1, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 4, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 2, 0, 4, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 3, 1, 4, 4, 1, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 3, 3, 4, 4, 3, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 0, 5, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 4, 3, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 3, 4, 1, 4, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 3, 4, 3, 4, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            if (this.field_111021_b && structureBoundingBox.isVecInside(new BlockPos(this.getXWithOffset(3, 3), this.getYWithOffset(2), this.getZWithOffset(3, 3)))) {
                this.field_111021_b = false;
                this.generateChestContents(world, structureBoundingBox, random, 3, 2, 3, field_111019_a, 2 + random.nextInt(4));
            }
            this.fillWithBlocks(world, structureBoundingBox, 0, 6, 0, 4, 6, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            int n = 0;
            while (n <= 4) {
                int n2 = 0;
                while (n2 <= 4) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n, -1, n2, structureBoundingBox);
                    ++n2;
                }
                ++n;
            }
            return true;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.field_111021_b = nBTTagCompound.getBoolean("Chest");
        }

        public Corridor(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
            this.field_111021_b = random.nextInt(3) == 0;
        }

        public Corridor() {
        }

        public static Corridor func_175879_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, 0, 0, 5, 7, 5, enumFacing);
            return Corridor.isAboveGround(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Corridor(n4, random, structureBoundingBox, enumFacing) : null;
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setBoolean("Chest", this.field_111021_b);
        }
    }

    public static class Crossing3
    extends Piece {
        public Crossing3(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        protected Crossing3(Random random, int n, int n2) {
            super(0);
            this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(random);
            switch (this.coordBaseMode) {
                case NORTH: 
                case SOUTH: {
                    this.boundingBox = new StructureBoundingBox(n, 64, n2, n + 19 - 1, 73, n2 + 19 - 1);
                    break;
                }
                default: {
                    this.boundingBox = new StructureBoundingBox(n, 64, n2, n + 19 - 1, 73, n2 + 19 - 1);
                }
            }
        }

        public static Crossing3 func_175885_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -8, -3, 0, 19, 10, 19, enumFacing);
            return Crossing3.isAboveGround(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Crossing3(n4, random, structureBoundingBox, enumFacing) : null;
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((Start)structureComponent, list, random, 8, 3, false);
            this.getNextComponentX((Start)structureComponent, list, random, 3, 8, false);
            this.getNextComponentZ((Start)structureComponent, list, random, 3, 8, false);
        }

        public Crossing3() {
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n;
            this.fillWithBlocks(world, structureBoundingBox, 7, 3, 0, 11, 4, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 7, 18, 4, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 5, 0, 10, 7, 18, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 8, 18, 7, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 5, 0, 7, 5, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 5, 11, 7, 5, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 11, 5, 0, 11, 5, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 11, 5, 11, 11, 5, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 7, 7, 5, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 11, 5, 7, 18, 5, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 11, 7, 5, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 11, 5, 11, 18, 5, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 2, 0, 11, 2, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 2, 13, 11, 2, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 0, 0, 11, 1, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 0, 15, 11, 1, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            int n2 = 7;
            while (n2 <= 11) {
                n = 0;
                while (n <= 2) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n2, -1, n, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n2, -1, 18 - n, structureBoundingBox);
                    ++n;
                }
                ++n2;
            }
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 7, 5, 2, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 13, 2, 7, 18, 2, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 7, 3, 1, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 15, 0, 7, 18, 1, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            n2 = 0;
            while (n2 <= 2) {
                n = 7;
                while (n <= 11) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n2, -1, n, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), 18 - n2, -1, n, structureBoundingBox);
                    ++n;
                }
                ++n2;
            }
            return true;
        }
    }

    public static class End
    extends Piece {
        private int fillSeed;

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n;
            int n2;
            Random random2 = new Random(this.fillSeed);
            int n3 = 0;
            while (n3 <= 4) {
                n2 = 3;
                while (n2 <= 4) {
                    n = random2.nextInt(8);
                    this.fillWithBlocks(world, structureBoundingBox, n3, n2, 0, n3, n2, n, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
                    ++n2;
                }
                ++n3;
            }
            n3 = random2.nextInt(8);
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 0, 0, 5, n3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            n3 = random2.nextInt(8);
            this.fillWithBlocks(world, structureBoundingBox, 4, 5, 0, 4, 5, n3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            n3 = 0;
            while (n3 <= 4) {
                n2 = random2.nextInt(5);
                this.fillWithBlocks(world, structureBoundingBox, n3, 2, 0, n3, 2, n2, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
                ++n3;
            }
            n3 = 0;
            while (n3 <= 4) {
                n2 = 0;
                while (n2 <= 1) {
                    n = random2.nextInt(3);
                    this.fillWithBlocks(world, structureBoundingBox, n3, n2, 0, n3, n2, n, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
                    ++n2;
                }
                ++n3;
            }
            return true;
        }

        public End() {
        }

        public End(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
            this.fillSeed = random.nextInt();
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.fillSeed = nBTTagCompound.getInteger("Seed");
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setInteger("Seed", this.fillSeed);
        }

        public static End func_175884_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, -3, 0, 5, 10, 8, enumFacing);
            return End.isAboveGround(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new End(n4, random, structureBoundingBox, enumFacing) : null;
        }
    }

    public static class Throne
    extends Piece {
        private boolean hasSpawner;

        public static Throne func_175874_a(List<StructureComponent> list, Random random, int n, int n2, int n3, int n4, EnumFacing enumFacing) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -2, 0, 0, 7, 8, 9, enumFacing);
            return Throne.isAboveGround(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Throne(n4, random, structureBoundingBox, enumFacing) : null;
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setBoolean("Mob", this.hasSpawner);
        }

        public Throne() {
        }

        public Throne(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.hasSpawner = nBTTagCompound.getBoolean("Mob");
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            BlockPos blockPos;
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 6, 7, 7, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 0, 5, 1, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 1, 5, 2, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 3, 2, 5, 3, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 4, 3, 5, 4, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 0, 1, 4, 2, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 2, 0, 5, 4, 2, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 5, 2, 1, 5, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 5, 2, 5, 5, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 3, 0, 5, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 5, 3, 6, 5, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 5, 8, 5, 5, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 1, 6, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 5, 6, 3, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 0, 6, 3, 0, 6, 8, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 6, 3, 6, 6, 8, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 6, 8, 5, 7, 8, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 8, 8, 4, 8, 8, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            if (!this.hasSpawner && structureBoundingBox.isVecInside(blockPos = new BlockPos(this.getXWithOffset(3, 5), this.getYWithOffset(5), this.getZWithOffset(3, 5)))) {
                this.hasSpawner = true;
                world.setBlockState(blockPos, Blocks.mob_spawner.getDefaultState(), 2);
                TileEntity tileEntity = world.getTileEntity(blockPos);
                if (tileEntity instanceof TileEntityMobSpawner) {
                    ((TileEntityMobSpawner)tileEntity).getSpawnerBaseLogic().setEntityName("Blaze");
                }
            }
            int n = 0;
            while (n <= 6) {
                int n2 = 0;
                while (n2 <= 6) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n, -1, n2, structureBoundingBox);
                    ++n2;
                }
                ++n;
            }
            return true;
        }
    }

    public static class NetherStalkRoom
    extends Piece {
        public NetherStalkRoom() {
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n;
            int n2;
            int n3;
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 0, 12, 4, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 0, 12, 13, 12, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 0, 1, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 11, 5, 0, 12, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 5, 11, 4, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 5, 11, 10, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 9, 11, 7, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 5, 0, 4, 12, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 5, 0, 10, 12, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 9, 0, 7, 12, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 11, 2, 10, 12, 10, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            int n4 = 1;
            while (n4 <= 11) {
                this.fillWithBlocks(world, structureBoundingBox, n4, 10, 0, n4, 11, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, n4, 10, 12, n4, 11, 12, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 0, 10, n4, 0, 11, n4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 12, 10, n4, 12, 11, n4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
                this.setBlockState(world, Blocks.nether_brick.getDefaultState(), n4, 13, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.nether_brick.getDefaultState(), n4, 13, 12, structureBoundingBox);
                this.setBlockState(world, Blocks.nether_brick.getDefaultState(), 0, 13, n4, structureBoundingBox);
                this.setBlockState(world, Blocks.nether_brick.getDefaultState(), 12, 13, n4, structureBoundingBox);
                this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), n4 + 1, 13, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), n4 + 1, 13, 12, structureBoundingBox);
                this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, n4 + 1, structureBoundingBox);
                this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 12, 13, n4 + 1, structureBoundingBox);
                n4 += 2;
            }
            this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 12, structureBoundingBox);
            this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 12, 13, 0, structureBoundingBox);
            n4 = 3;
            while (n4 <= 9) {
                this.fillWithBlocks(world, structureBoundingBox, 1, 7, n4, 1, 8, n4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 11, 7, n4, 11, 8, n4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
                n4 += 2;
            }
            n4 = this.getMetadataWithOffset(Blocks.nether_brick_stairs, 3);
            int n5 = 0;
            while (n5 <= 6) {
                n3 = n5 + 4;
                n2 = 5;
                while (n2 <= 7) {
                    this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(n4), n2, 5 + n5, n3, structureBoundingBox);
                    ++n2;
                }
                if (n3 >= 5 && n3 <= 8) {
                    this.fillWithBlocks(world, structureBoundingBox, 5, 5, n3, 7, n5 + 4, n3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
                } else if (n3 >= 9 && n3 <= 10) {
                    this.fillWithBlocks(world, structureBoundingBox, 5, 8, n3, 7, n5 + 4, n3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
                }
                if (n5 >= 1) {
                    this.fillWithBlocks(world, structureBoundingBox, 5, 6 + n5, n3, 7, 9 + n5, n3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                }
                ++n5;
            }
            n5 = 5;
            while (n5 <= 7) {
                this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(n4), n5, 12, 11, structureBoundingBox);
                ++n5;
            }
            this.fillWithBlocks(world, structureBoundingBox, 5, 6, 7, 5, 7, 7, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 6, 7, 7, 7, 7, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 13, 12, 7, 13, 12, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 5, 2, 3, 5, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 5, 9, 3, 5, 10, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 5, 4, 2, 5, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 9, 5, 2, 10, 5, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 9, 5, 9, 10, 5, 10, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 10, 5, 4, 10, 5, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            n5 = this.getMetadataWithOffset(Blocks.nether_brick_stairs, 0);
            n3 = this.getMetadataWithOffset(Blocks.nether_brick_stairs, 1);
            this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(n3), 4, 5, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(n3), 4, 5, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(n3), 4, 5, 9, structureBoundingBox);
            this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(n3), 4, 5, 10, structureBoundingBox);
            this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(n5), 8, 5, 2, structureBoundingBox);
            this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(n5), 8, 5, 3, structureBoundingBox);
            this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(n5), 8, 5, 9, structureBoundingBox);
            this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(n5), 8, 5, 10, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 3, 4, 4, 4, 4, 8, Blocks.soul_sand.getDefaultState(), Blocks.soul_sand.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 4, 4, 9, 4, 8, Blocks.soul_sand.getDefaultState(), Blocks.soul_sand.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 5, 4, 4, 5, 8, Blocks.nether_wart.getDefaultState(), Blocks.nether_wart.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 5, 4, 9, 5, 8, Blocks.nether_wart.getDefaultState(), Blocks.nether_wart.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 2, 0, 8, 2, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 4, 12, 2, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 0, 0, 8, 1, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 0, 9, 8, 1, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 4, 3, 1, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 9, 0, 4, 12, 1, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            n2 = 4;
            while (n2 <= 8) {
                n = 0;
                while (n <= 2) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n2, -1, n, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n2, -1, 12 - n, structureBoundingBox);
                    ++n;
                }
                ++n2;
            }
            n2 = 0;
            while (n2 <= 2) {
                n = 4;
                while (n <= 8) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n2, -1, n, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), 12 - n2, -1, n, structureBoundingBox);
                    ++n;
                }
                ++n2;
            }
            return true;
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((Start)structureComponent, list, random, 5, 3, true);
            this.getNextComponentNormal((Start)structureComponent, list, random, 5, 11, true);
        }

        public NetherStalkRoom(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        public static NetherStalkRoom func_175875_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -5, -3, 0, 13, 14, 13, enumFacing);
            return NetherStalkRoom.isAboveGround(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new NetherStalkRoom(n4, random, structureBoundingBox, enumFacing) : null;
        }
    }

    static class PieceWeight {
        public final int field_78826_b;
        public boolean field_78825_e;
        public int field_78827_c;
        public int field_78824_d;
        public Class<? extends Piece> weightClass;

        public PieceWeight(Class<? extends Piece> clazz, int n, int n2) {
            this(clazz, n, n2, false);
        }

        public PieceWeight(Class<? extends Piece> clazz, int n, int n2, boolean bl) {
            this.weightClass = clazz;
            this.field_78826_b = n;
            this.field_78824_d = n2;
            this.field_78825_e = bl;
        }

        public boolean func_78822_a(int n) {
            return this.field_78824_d == 0 || this.field_78827_c < this.field_78824_d;
        }

        public boolean func_78823_a() {
            return this.field_78824_d == 0 || this.field_78827_c < this.field_78824_d;
        }
    }

    public static class Start
    extends Crossing3 {
        public List<PieceWeight> primaryWeights;
        public List<PieceWeight> secondaryWeights;
        public PieceWeight theNetherBridgePieceWeight;
        public List<StructureComponent> field_74967_d = Lists.newArrayList();

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
        }

        public Start() {
        }

        public Start(Random random, int n, int n2) {
            super(random, n, n2);
            PieceWeight pieceWeight;
            this.primaryWeights = Lists.newArrayList();
            PieceWeight[] pieceWeightArray = primaryComponents;
            int n3 = pieceWeightArray.length;
            int n4 = 0;
            while (n4 < n3) {
                pieceWeight = pieceWeightArray[n4];
                pieceWeight.field_78827_c = 0;
                this.primaryWeights.add(pieceWeight);
                ++n4;
            }
            this.secondaryWeights = Lists.newArrayList();
            pieceWeightArray = secondaryComponents;
            n3 = pieceWeightArray.length;
            n4 = 0;
            while (n4 < n3) {
                pieceWeight = pieceWeightArray[n4];
                pieceWeight.field_78827_c = 0;
                this.secondaryWeights.add(pieceWeight);
                ++n4;
            }
        }
    }

    public static class Entrance
    extends Piece {
        public Entrance() {
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((Start)structureComponent, list, random, 5, 3, true);
        }

        public static Entrance func_175881_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -5, -3, 0, 13, 14, 13, enumFacing);
            return Entrance.isAboveGround(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Entrance(n4, random, structureBoundingBox, enumFacing) : null;
        }

        public Entrance(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n;
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 0, 12, 4, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 0, 12, 13, 12, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 0, 1, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 11, 5, 0, 12, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 5, 11, 4, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 5, 11, 10, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 9, 11, 7, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 5, 0, 4, 12, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 5, 0, 10, 12, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 9, 0, 7, 12, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 11, 2, 10, 12, 10, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 8, 0, 7, 8, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            int n2 = 1;
            while (n2 <= 11) {
                this.fillWithBlocks(world, structureBoundingBox, n2, 10, 0, n2, 11, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, n2, 10, 12, n2, 11, 12, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 0, 10, n2, 0, 11, n2, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 12, 10, n2, 12, 11, n2, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
                this.setBlockState(world, Blocks.nether_brick.getDefaultState(), n2, 13, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.nether_brick.getDefaultState(), n2, 13, 12, structureBoundingBox);
                this.setBlockState(world, Blocks.nether_brick.getDefaultState(), 0, 13, n2, structureBoundingBox);
                this.setBlockState(world, Blocks.nether_brick.getDefaultState(), 12, 13, n2, structureBoundingBox);
                this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), n2 + 1, 13, 0, structureBoundingBox);
                this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), n2 + 1, 13, 12, structureBoundingBox);
                this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, n2 + 1, structureBoundingBox);
                this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 12, 13, n2 + 1, structureBoundingBox);
                n2 += 2;
            }
            this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 12, structureBoundingBox);
            this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 0, structureBoundingBox);
            this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 12, 13, 0, structureBoundingBox);
            n2 = 3;
            while (n2 <= 9) {
                this.fillWithBlocks(world, structureBoundingBox, 1, 7, n2, 1, 8, n2, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 11, 7, n2, 11, 8, n2, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
                n2 += 2;
            }
            this.fillWithBlocks(world, structureBoundingBox, 4, 2, 0, 8, 2, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 4, 12, 2, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 0, 0, 8, 1, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 0, 9, 8, 1, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 4, 3, 1, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 9, 0, 4, 12, 1, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            n2 = 4;
            while (n2 <= 8) {
                n = 0;
                while (n <= 2) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n2, -1, n, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n2, -1, 12 - n, structureBoundingBox);
                    ++n;
                }
                ++n2;
            }
            n2 = 0;
            while (n2 <= 2) {
                n = 4;
                while (n <= 8) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n2, -1, n, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), 12 - n2, -1, n, structureBoundingBox);
                    ++n;
                }
                ++n2;
            }
            this.fillWithBlocks(world, structureBoundingBox, 5, 5, 5, 7, 5, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 1, 6, 6, 4, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.setBlockState(world, Blocks.nether_brick.getDefaultState(), 6, 0, 6, structureBoundingBox);
            this.setBlockState(world, Blocks.flowing_lava.getDefaultState(), 6, 5, 6, structureBoundingBox);
            BlockPos blockPos = new BlockPos(this.getXWithOffset(6, 6), this.getYWithOffset(5), this.getZWithOffset(6, 6));
            if (structureBoundingBox.isVecInside(blockPos)) {
                world.forceBlockUpdateTick(Blocks.flowing_lava, blockPos, random);
            }
            return true;
        }
    }

    public static class Stairs
    extends Piece {
        public Stairs(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        public static Stairs func_175872_a(List<StructureComponent> list, Random random, int n, int n2, int n3, int n4, EnumFacing enumFacing) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -2, 0, 0, 7, 11, 7, enumFacing);
            return Stairs.isAboveGround(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Stairs(n4, random, structureBoundingBox, enumFacing) : null;
        }

        public Stairs() {
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentZ((Start)structureComponent, list, random, 6, 2, false);
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 6, 1, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 6, 10, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 1, 8, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 2, 0, 6, 8, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 1, 0, 8, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 2, 1, 6, 8, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 6, 5, 8, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 2, 0, 5, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 3, 2, 6, 5, 2, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 3, 4, 6, 5, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.setBlockState(world, Blocks.nether_brick.getDefaultState(), 5, 2, 5, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 4, 2, 5, 4, 3, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 2, 5, 3, 4, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 2, 5, 2, 5, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 5, 1, 6, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 7, 1, 5, 7, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 8, 2, 6, 8, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 6, 0, 4, 8, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 5, 0, 4, 5, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            int n = 0;
            while (n <= 6) {
                int n2 = 0;
                while (n2 <= 6) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n, -1, n2, structureBoundingBox);
                    ++n2;
                }
                ++n;
            }
            return true;
        }
    }

    public static class Corridor2
    extends Piece {
        private boolean field_111020_b;

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentZ((Start)structureComponent, list, random, 0, 1, true);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
            super.readStructureFromNBT(nBTTagCompound);
            this.field_111020_b = nBTTagCompound.getBoolean("Chest");
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
            super.writeStructureToNBT(nBTTagCompound);
            nBTTagCompound.setBoolean("Chest", this.field_111020_b);
        }

        public Corridor2() {
        }

        public static Corridor2 func_175876_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, 0, 0, 5, 7, 5, enumFacing);
            return Corridor2.isAboveGround(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Corridor2(n4, random, structureBoundingBox, enumFacing) : null;
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 4, 1, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 4, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 0, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 1, 0, 4, 1, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 3, 0, 4, 3, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 2, 0, 4, 5, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 4, 4, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 3, 4, 1, 4, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 3, 4, 3, 4, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            if (this.field_111020_b && structureBoundingBox.isVecInside(new BlockPos(this.getXWithOffset(1, 3), this.getYWithOffset(2), this.getZWithOffset(1, 3)))) {
                this.field_111020_b = false;
                this.generateChestContents(world, structureBoundingBox, random, 1, 2, 3, field_111019_a, 2 + random.nextInt(4));
            }
            this.fillWithBlocks(world, structureBoundingBox, 0, 6, 0, 4, 6, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            int n = 0;
            while (n <= 4) {
                int n2 = 0;
                while (n2 <= 4) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n, -1, n2, structureBoundingBox);
                    ++n2;
                }
                ++n;
            }
            return true;
        }

        public Corridor2(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
            this.field_111020_b = random.nextInt(3) == 0;
        }
    }

    public static class Corridor3
    extends Piece {
        public static Corridor3 func_175883_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, -7, 0, 5, 14, 10, enumFacing);
            return Corridor3.isAboveGround(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Corridor3(n4, random, structureBoundingBox, enumFacing) : null;
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n = this.getMetadataWithOffset(Blocks.nether_brick_stairs, 2);
            int n2 = 0;
            while (n2 <= 9) {
                int n3 = Math.max(1, 7 - n2);
                int n4 = Math.min(Math.max(n3 + 5, 14 - n2), 13);
                int n5 = n2;
                this.fillWithBlocks(world, structureBoundingBox, 0, 0, n2, 4, n3, n2, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 1, n3 + 1, n2, 3, n4 - 1, n2, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                if (n2 <= 6) {
                    this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(n), 1, n3 + 1, n2, structureBoundingBox);
                    this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(n), 2, n3 + 1, n2, structureBoundingBox);
                    this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(n), 3, n3 + 1, n2, structureBoundingBox);
                }
                this.fillWithBlocks(world, structureBoundingBox, 0, n4, n2, 4, n4, n2, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 0, n3 + 1, n2, 0, n4 - 1, n2, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
                this.fillWithBlocks(world, structureBoundingBox, 4, n3 + 1, n2, 4, n4 - 1, n2, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
                if ((n2 & 1) == 0) {
                    this.fillWithBlocks(world, structureBoundingBox, 0, n3 + 2, n2, 0, n3 + 3, n2, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
                    this.fillWithBlocks(world, structureBoundingBox, 4, n3 + 2, n2, 4, n3 + 3, n2, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
                }
                int n6 = 0;
                while (n6 <= 4) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n6, -1, n5, structureBoundingBox);
                    ++n6;
                }
                ++n2;
            }
            return true;
        }

        public Corridor3() {
        }

        public Corridor3(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((Start)structureComponent, list, random, 1, 0, true);
        }
    }

    public static class Straight
    extends Piece {
        public static Straight func_175882_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, -3, 0, 5, 10, 19, enumFacing);
            return Straight.isAboveGround(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Straight(n4, random, structureBoundingBox, enumFacing) : null;
        }

        public Straight() {
        }

        public Straight(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((Start)structureComponent, list, random, 1, 3, false);
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 0, 4, 4, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 5, 0, 3, 7, 18, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 0, 0, 5, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 5, 0, 4, 5, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 4, 2, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 13, 4, 2, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 4, 1, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 15, 4, 1, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            int n = 0;
            while (n <= 4) {
                int n2 = 0;
                while (n2 <= 2) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n, -1, n2, structureBoundingBox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n, -1, 18 - n2, structureBoundingBox);
                    ++n2;
                }
                ++n;
            }
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 1, 0, 4, 1, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 4, 0, 4, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 14, 0, 4, 14, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 17, 0, 4, 17, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 1, 1, 4, 4, 1, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 3, 4, 4, 4, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 3, 14, 4, 4, 14, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 1, 17, 4, 4, 17, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            return true;
        }
    }

    public static class Corridor5
    extends Piece {
        public Corridor5() {
        }

        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((Start)structureComponent, list, random, 1, 0, true);
        }

        public Corridor5(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        public static Corridor5 func_175877_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, 0, 0, 5, 7, 5, enumFacing);
            return Corridor5.isAboveGround(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Corridor5(n4, random, structureBoundingBox, enumFacing) : null;
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 4, 1, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 4, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 0, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 2, 0, 4, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 1, 0, 4, 1, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 3, 0, 4, 3, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 3, 1, 4, 4, 1, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 3, 3, 4, 4, 3, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 6, 0, 4, 6, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            int n = 0;
            while (n <= 4) {
                int n2 = 0;
                while (n2 <= 4) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n, -1, n2, structureBoundingBox);
                    ++n2;
                }
                ++n;
            }
            return true;
        }
    }

    static abstract class Piece
    extends StructureComponent {
        protected static final List<WeightedRandomChestContent> field_111019_a = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 5), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 5), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 15), new WeightedRandomChestContent(Items.golden_sword, 0, 1, 1, 5), new WeightedRandomChestContent(Items.golden_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent(Items.flint_and_steel, 0, 1, 1, 5), new WeightedRandomChestContent(Items.nether_wart, 0, 3, 7, 5), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 10), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 8), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 5), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 3), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.obsidian), 0, 2, 4, 2)});

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
        }

        public Piece() {
        }

        protected StructureComponent getNextComponentX(Start start, List<StructureComponent> list, Random random, int n, int n2, boolean bl) {
            if (this.coordBaseMode != null) {
                switch (this.coordBaseMode) {
                    case NORTH: {
                        return this.func_175870_a(start, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + n, this.boundingBox.minZ + n2, EnumFacing.WEST, this.getComponentType(), bl);
                    }
                    case SOUTH: {
                        return this.func_175870_a(start, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + n, this.boundingBox.minZ + n2, EnumFacing.WEST, this.getComponentType(), bl);
                    }
                    case WEST: {
                        return this.func_175870_a(start, list, random, this.boundingBox.minX + n2, this.boundingBox.minY + n, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType(), bl);
                    }
                    case EAST: {
                        return this.func_175870_a(start, list, random, this.boundingBox.minX + n2, this.boundingBox.minY + n, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType(), bl);
                    }
                }
            }
            return null;
        }

        protected Piece(int n) {
            super(n);
        }

        private Piece func_175871_a(Start start, List<PieceWeight> list, List<StructureComponent> list2, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            int n5 = this.getTotalWeight(list);
            boolean bl = n5 > 0 && n4 <= 30;
            int n6 = 0;
            block0: while (n6 < 5 && bl) {
                ++n6;
                int n7 = random.nextInt(n5);
                for (PieceWeight pieceWeight : list) {
                    if ((n7 -= pieceWeight.field_78826_b) >= 0) continue;
                    if (!pieceWeight.func_78822_a(n4) || pieceWeight == start.theNetherBridgePieceWeight && !pieceWeight.field_78825_e) continue block0;
                    Piece piece = StructureNetherBridgePieces.func_175887_b(pieceWeight, list2, random, n, n2, n3, enumFacing, n4);
                    if (piece == null) continue;
                    ++pieceWeight.field_78827_c;
                    start.theNetherBridgePieceWeight = pieceWeight;
                    if (!pieceWeight.func_78823_a()) {
                        list.remove(pieceWeight);
                    }
                    return piece;
                }
            }
            return End.func_175884_a(list2, random, n, n2, n3, enumFacing, n4);
        }

        protected StructureComponent getNextComponentNormal(Start start, List<StructureComponent> list, Random random, int n, int n2, boolean bl) {
            if (this.coordBaseMode != null) {
                switch (this.coordBaseMode) {
                    case NORTH: {
                        return this.func_175870_a(start, list, random, this.boundingBox.minX + n, this.boundingBox.minY + n2, this.boundingBox.minZ - 1, this.coordBaseMode, this.getComponentType(), bl);
                    }
                    case SOUTH: {
                        return this.func_175870_a(start, list, random, this.boundingBox.minX + n, this.boundingBox.minY + n2, this.boundingBox.maxZ + 1, this.coordBaseMode, this.getComponentType(), bl);
                    }
                    case WEST: {
                        return this.func_175870_a(start, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + n2, this.boundingBox.minZ + n, this.coordBaseMode, this.getComponentType(), bl);
                    }
                    case EAST: {
                        return this.func_175870_a(start, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + n2, this.boundingBox.minZ + n, this.coordBaseMode, this.getComponentType(), bl);
                    }
                }
            }
            return null;
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
        }

        protected static boolean isAboveGround(StructureBoundingBox structureBoundingBox) {
            return structureBoundingBox != null && structureBoundingBox.minY > 10;
        }

        private int getTotalWeight(List<PieceWeight> list) {
            boolean bl = false;
            int n = 0;
            for (PieceWeight pieceWeight : list) {
                if (pieceWeight.field_78824_d > 0 && pieceWeight.field_78827_c < pieceWeight.field_78824_d) {
                    bl = true;
                }
                n += pieceWeight.field_78826_b;
            }
            return bl ? n : -1;
        }

        protected StructureComponent getNextComponentZ(Start start, List<StructureComponent> list, Random random, int n, int n2, boolean bl) {
            if (this.coordBaseMode != null) {
                switch (this.coordBaseMode) {
                    case NORTH: {
                        return this.func_175870_a(start, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + n, this.boundingBox.minZ + n2, EnumFacing.EAST, this.getComponentType(), bl);
                    }
                    case SOUTH: {
                        return this.func_175870_a(start, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + n, this.boundingBox.minZ + n2, EnumFacing.EAST, this.getComponentType(), bl);
                    }
                    case WEST: {
                        return this.func_175870_a(start, list, random, this.boundingBox.minX + n2, this.boundingBox.minY + n, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType(), bl);
                    }
                    case EAST: {
                        return this.func_175870_a(start, list, random, this.boundingBox.minX + n2, this.boundingBox.minY + n, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType(), bl);
                    }
                }
            }
            return null;
        }

        private StructureComponent func_175870_a(Start start, List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4, boolean bl) {
            if (Math.abs(n - start.getBoundingBox().minX) <= 112 && Math.abs(n3 - start.getBoundingBox().minZ) <= 112) {
                Piece piece;
                List<PieceWeight> list2 = start.primaryWeights;
                if (bl) {
                    list2 = start.secondaryWeights;
                }
                if ((piece = this.func_175871_a(start, list2, list, random, n, n2, n3, enumFacing, n4 + 1)) != null) {
                    list.add(piece);
                    start.field_74967_d.add(piece);
                }
                return piece;
            }
            return End.func_175884_a(list, random, n, n2, n3, enumFacing, n4);
        }
    }

    public static class Crossing2
    extends Piece {
        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((Start)structureComponent, list, random, 1, 0, true);
            this.getNextComponentX((Start)structureComponent, list, random, 0, 1, true);
            this.getNextComponentZ((Start)structureComponent, list, random, 0, 1, true);
        }

        public Crossing2() {
        }

        public Crossing2(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        public static Crossing2 func_175878_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -1, 0, 0, 5, 7, 5, enumFacing);
            return Crossing2.isAboveGround(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Crossing2(n4, random, structureBoundingBox, enumFacing) : null;
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 4, 1, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 4, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 0, 5, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 2, 0, 4, 5, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 4, 0, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 2, 4, 4, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 6, 0, 4, 6, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            int n = 0;
            while (n <= 4) {
                int n2 = 0;
                while (n2 <= 4) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n, -1, n2, structureBoundingBox);
                    ++n2;
                }
                ++n;
            }
            return true;
        }
    }

    public static class Crossing
    extends Piece {
        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((Start)structureComponent, list, random, 2, 0, false);
            this.getNextComponentX((Start)structureComponent, list, random, 0, 2, false);
            this.getNextComponentZ((Start)structureComponent, list, random, 0, 2, false);
        }

        public Crossing(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 6, 1, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 6, 7, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 1, 6, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 6, 1, 6, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 2, 0, 6, 6, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 2, 6, 6, 6, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 0, 6, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 5, 0, 6, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 2, 0, 6, 6, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 2, 5, 6, 6, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 6, 0, 4, 6, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 5, 0, 4, 5, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 6, 6, 4, 6, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 5, 6, 4, 5, 6, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 6, 2, 0, 6, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 5, 2, 0, 5, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 6, 2, 6, 6, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 5, 2, 6, 5, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            int n = 0;
            while (n <= 6) {
                int n2 = 0;
                while (n2 <= 6) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n, -1, n2, structureBoundingBox);
                    ++n2;
                }
                ++n;
            }
            return true;
        }

        public Crossing() {
        }

        public static Crossing func_175873_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -2, 0, 0, 7, 9, 7, enumFacing);
            return Crossing.isAboveGround(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Crossing(n4, random, structureBoundingBox, enumFacing) : null;
        }
    }

    public static class Corridor4
    extends Piece {
        @Override
        public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
            int n = 1;
            if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.NORTH) {
                n = 5;
            }
            this.getNextComponentX((Start)structureComponent, list, random, 0, n, random.nextInt(8) > 0);
            this.getNextComponentZ((Start)structureComponent, list, random, 0, n, random.nextInt(8) > 0);
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 8, 1, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 8, 5, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 6, 0, 8, 6, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 2, 5, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 2, 0, 8, 5, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 3, 0, 1, 4, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 3, 0, 7, 4, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 4, 8, 2, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 4, 2, 2, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 1, 4, 7, 2, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 8, 8, 3, 8, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 6, 0, 3, 7, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 3, 6, 8, 3, 7, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 4, 0, 5, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 3, 4, 8, 5, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 3, 5, 2, 5, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 3, 5, 7, 5, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 4, 5, 1, 5, 5, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 4, 5, 7, 5, 5, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            int n = 0;
            while (n <= 5) {
                int n2 = 0;
                while (n2 <= 8) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), n2, -1, n, structureBoundingBox);
                    ++n2;
                }
                ++n;
            }
            return true;
        }

        public Corridor4(int n, Random random, StructureBoundingBox structureBoundingBox, EnumFacing enumFacing) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        public static Corridor4 func_175880_a(List<StructureComponent> list, Random random, int n, int n2, int n3, EnumFacing enumFacing, int n4) {
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(n, n2, n3, -3, 0, 0, 9, 7, 9, enumFacing);
            return Corridor4.isAboveGround(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new Corridor4(n4, random, structureBoundingBox, enumFacing) : null;
        }

        public Corridor4() {
        }
    }
}

