/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class StructureOceanMonumentPieces {
    public static void registerOceanMonumentPieces() {
        MapGenStructureIO.registerStructureComponent(MonumentBuilding.class, "OMB");
        MapGenStructureIO.registerStructureComponent(MonumentCoreRoom.class, "OMCR");
        MapGenStructureIO.registerStructureComponent(DoubleXRoom.class, "OMDXR");
        MapGenStructureIO.registerStructureComponent(DoubleXYRoom.class, "OMDXYR");
        MapGenStructureIO.registerStructureComponent(DoubleYRoom.class, "OMDYR");
        MapGenStructureIO.registerStructureComponent(DoubleYZRoom.class, "OMDYZR");
        MapGenStructureIO.registerStructureComponent(DoubleZRoom.class, "OMDZR");
        MapGenStructureIO.registerStructureComponent(EntryRoom.class, "OMEntry");
        MapGenStructureIO.registerStructureComponent(Penthouse.class, "OMPenthouse");
        MapGenStructureIO.registerStructureComponent(SimpleRoom.class, "OMSimple");
        MapGenStructureIO.registerStructureComponent(SimpleTopRoom.class, "OMSimpleT");
    }

    static class YDoubleRoomFitHelper
    implements MonumentRoomFitHelper {
        @Override
        public boolean func_175969_a(RoomDefinition roomDefinition) {
            return roomDefinition.field_175966_c[EnumFacing.UP.getIndex()] && !roomDefinition.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d;
        }

        @Override
        public Piece func_175968_a(EnumFacing enumFacing, RoomDefinition roomDefinition, Random random) {
            roomDefinition.field_175963_d = true;
            roomDefinition.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            return new DoubleYRoom(enumFacing, roomDefinition, random);
        }

        private YDoubleRoomFitHelper() {
        }
    }

    public static class DoubleZRoom
    extends Piece {
        public DoubleZRoom() {
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            RoomDefinition roomDefinition = this.field_175830_k.field_175965_b[EnumFacing.NORTH.getIndex()];
            RoomDefinition roomDefinition2 = this.field_175830_k;
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(world, structureBoundingBox, 0, 8, roomDefinition.field_175966_c[EnumFacing.DOWN.getIndex()]);
                this.func_175821_a(world, structureBoundingBox, 0, 0, roomDefinition2.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }
            if (roomDefinition2.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(world, structureBoundingBox, 1, 4, 1, 6, 4, 7, field_175828_a);
            }
            if (roomDefinition.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(world, structureBoundingBox, 1, 4, 8, 6, 4, 14, field_175828_a);
            }
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 0, 0, 3, 15, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 3, 0, 7, 3, 15, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 3, 0, 7, 3, 0, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 3, 15, 6, 3, 15, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 0, 2, 15, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 2, 0, 7, 2, 15, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 0, 7, 2, 0, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 15, 6, 2, 15, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 0, 0, 1, 15, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 1, 0, 7, 1, 15, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 0, 7, 1, 0, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 15, 6, 1, 15, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 1, 1, 1, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 1, 1, 6, 1, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 3, 1, 1, 3, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 3, 1, 6, 3, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 13, 1, 1, 14, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 1, 13, 6, 1, 14, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 3, 13, 1, 3, 14, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 3, 13, 6, 3, 14, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 1, 6, 2, 3, 6, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 1, 6, 5, 3, 6, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 1, 9, 2, 3, 9, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 1, 9, 5, 3, 9, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 2, 6, 4, 2, 6, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 2, 9, 4, 2, 9, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 2, 7, 2, 2, 8, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 2, 7, 5, 2, 8, field_175826_b, field_175826_b, false);
            this.setBlockState(world, field_175825_e, 2, 2, 5, structureBoundingBox);
            this.setBlockState(world, field_175825_e, 5, 2, 5, structureBoundingBox);
            this.setBlockState(world, field_175825_e, 2, 2, 10, structureBoundingBox);
            this.setBlockState(world, field_175825_e, 5, 2, 10, structureBoundingBox);
            this.setBlockState(world, field_175826_b, 2, 3, 5, structureBoundingBox);
            this.setBlockState(world, field_175826_b, 5, 3, 5, structureBoundingBox);
            this.setBlockState(world, field_175826_b, 2, 3, 10, structureBoundingBox);
            this.setBlockState(world, field_175826_b, 5, 3, 10, structureBoundingBox);
            if (roomDefinition2.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 3, 1, 0, 4, 2, 0, false);
            }
            if (roomDefinition2.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 7, 1, 3, 7, 2, 4, false);
            }
            if (roomDefinition2.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 0, 1, 3, 0, 2, 4, false);
            }
            if (roomDefinition.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 3, 1, 15, 4, 2, 15, false);
            }
            if (roomDefinition.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 0, 1, 11, 0, 2, 12, false);
            }
            if (roomDefinition.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 7, 1, 11, 7, 2, 12, false);
            }
            return true;
        }

        public DoubleZRoom(EnumFacing enumFacing, RoomDefinition roomDefinition, Random random) {
            super(1, enumFacing, roomDefinition, 1, 1, 2);
        }
    }

    static class FitSimpleRoomHelper
    implements MonumentRoomFitHelper {
        @Override
        public Piece func_175968_a(EnumFacing enumFacing, RoomDefinition roomDefinition, Random random) {
            roomDefinition.field_175963_d = true;
            return new SimpleRoom(enumFacing, roomDefinition, random);
        }

        @Override
        public boolean func_175969_a(RoomDefinition roomDefinition) {
            return true;
        }

        private FitSimpleRoomHelper() {
        }
    }

    public static class DoubleYZRoom
    extends Piece {
        public DoubleYZRoom() {
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            IBlockState iBlockState;
            RoomDefinition roomDefinition = this.field_175830_k.field_175965_b[EnumFacing.NORTH.getIndex()];
            RoomDefinition roomDefinition2 = this.field_175830_k;
            RoomDefinition roomDefinition3 = roomDefinition.field_175965_b[EnumFacing.UP.getIndex()];
            RoomDefinition roomDefinition4 = roomDefinition2.field_175965_b[EnumFacing.UP.getIndex()];
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(world, structureBoundingBox, 0, 8, roomDefinition.field_175966_c[EnumFacing.DOWN.getIndex()]);
                this.func_175821_a(world, structureBoundingBox, 0, 0, roomDefinition2.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }
            if (roomDefinition4.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(world, structureBoundingBox, 1, 8, 1, 6, 8, 7, field_175828_a);
            }
            if (roomDefinition3.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(world, structureBoundingBox, 1, 8, 8, 6, 8, 14, field_175828_a);
            }
            int n = 1;
            while (n <= 7) {
                iBlockState = field_175826_b;
                if (n == 2 || n == 6) {
                    iBlockState = field_175828_a;
                }
                this.fillWithBlocks(world, structureBoundingBox, 0, n, 0, 0, n, 15, iBlockState, iBlockState, false);
                this.fillWithBlocks(world, structureBoundingBox, 7, n, 0, 7, n, 15, iBlockState, iBlockState, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, n, 0, 6, n, 0, iBlockState, iBlockState, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, n, 15, 6, n, 15, iBlockState, iBlockState, false);
                ++n;
            }
            n = 1;
            while (n <= 7) {
                iBlockState = field_175827_c;
                if (n == 2 || n == 6) {
                    iBlockState = field_175825_e;
                }
                this.fillWithBlocks(world, structureBoundingBox, 3, n, 7, 4, n, 8, iBlockState, iBlockState, false);
                ++n;
            }
            if (roomDefinition2.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 3, 1, 0, 4, 2, 0, false);
            }
            if (roomDefinition2.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 7, 1, 3, 7, 2, 4, false);
            }
            if (roomDefinition2.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 0, 1, 3, 0, 2, 4, false);
            }
            if (roomDefinition.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 3, 1, 15, 4, 2, 15, false);
            }
            if (roomDefinition.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 0, 1, 11, 0, 2, 12, false);
            }
            if (roomDefinition.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 7, 1, 11, 7, 2, 12, false);
            }
            if (roomDefinition4.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 3, 5, 0, 4, 6, 0, false);
            }
            if (roomDefinition4.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 7, 5, 3, 7, 6, 4, false);
                this.fillWithBlocks(world, structureBoundingBox, 5, 4, 2, 6, 4, 5, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 6, 1, 2, 6, 3, 2, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 6, 1, 5, 6, 3, 5, field_175826_b, field_175826_b, false);
            }
            if (roomDefinition4.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 0, 5, 3, 0, 6, 4, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, 4, 2, 2, 4, 5, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, 1, 2, 1, 3, 2, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, 1, 5, 1, 3, 5, field_175826_b, field_175826_b, false);
            }
            if (roomDefinition3.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 3, 5, 15, 4, 6, 15, false);
            }
            if (roomDefinition3.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 0, 5, 11, 0, 6, 12, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, 4, 10, 2, 4, 13, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, 1, 10, 1, 3, 10, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, 1, 13, 1, 3, 13, field_175826_b, field_175826_b, false);
            }
            if (roomDefinition3.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 7, 5, 11, 7, 6, 12, false);
                this.fillWithBlocks(world, structureBoundingBox, 5, 4, 10, 6, 4, 13, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 6, 1, 10, 6, 3, 10, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 6, 1, 13, 6, 3, 13, field_175826_b, field_175826_b, false);
            }
            return true;
        }

        public DoubleYZRoom(EnumFacing enumFacing, RoomDefinition roomDefinition, Random random) {
            super(1, enumFacing, roomDefinition, 1, 2, 2);
        }
    }

    public static class SimpleTopRoom
    extends Piece {
        public SimpleTopRoom(EnumFacing enumFacing, RoomDefinition roomDefinition, Random random) {
            super(1, enumFacing, roomDefinition, 1, 1, 1);
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(world, structureBoundingBox, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }
            if (this.field_175830_k.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(world, structureBoundingBox, 1, 4, 1, 6, 4, 6, field_175828_a);
            }
            int n = 1;
            while (n <= 6) {
                int n2 = 1;
                while (n2 <= 6) {
                    if (random.nextInt(3) != 0) {
                        int n3 = 2 + (random.nextInt(4) == 0 ? 0 : 1);
                        this.fillWithBlocks(world, structureBoundingBox, n, n3, n2, n, 3, n2, Blocks.sponge.getStateFromMeta(1), Blocks.sponge.getStateFromMeta(1), false);
                    }
                    ++n2;
                }
                ++n;
            }
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 0, 0, 1, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 1, 0, 7, 1, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 0, 6, 1, 0, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 7, 6, 1, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 0, 2, 7, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 2, 0, 7, 2, 7, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 0, 6, 2, 0, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 7, 6, 2, 7, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 0, 0, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 3, 0, 7, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 3, 0, 6, 3, 0, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 3, 7, 6, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 3, 0, 2, 4, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 1, 3, 7, 2, 4, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 1, 0, 4, 2, 0, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 1, 7, 4, 2, 7, field_175827_c, field_175827_c, false);
            if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 3, 1, 0, 4, 2, 0, false);
            }
            return true;
        }

        public SimpleTopRoom() {
        }
    }

    static class XYDoubleRoomFitHelper
    implements MonumentRoomFitHelper {
        private XYDoubleRoomFitHelper() {
        }

        @Override
        public Piece func_175968_a(EnumFacing enumFacing, RoomDefinition roomDefinition, Random random) {
            roomDefinition.field_175963_d = true;
            roomDefinition.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d = true;
            roomDefinition.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            roomDefinition.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            return new DoubleXYRoom(enumFacing, roomDefinition, random);
        }

        @Override
        public boolean func_175969_a(RoomDefinition roomDefinition) {
            if (roomDefinition.field_175966_c[EnumFacing.EAST.getIndex()] && !roomDefinition.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d && roomDefinition.field_175966_c[EnumFacing.UP.getIndex()] && !roomDefinition.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d) {
                RoomDefinition roomDefinition2 = roomDefinition.field_175965_b[EnumFacing.EAST.getIndex()];
                return roomDefinition2.field_175966_c[EnumFacing.UP.getIndex()] && !roomDefinition2.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d;
            }
            return false;
        }
    }

    static interface MonumentRoomFitHelper {
        public boolean func_175969_a(RoomDefinition var1);

        public Piece func_175968_a(EnumFacing var1, RoomDefinition var2, Random var3);
    }

    public static class MonumentBuilding
    extends Piece {
        private RoomDefinition field_175845_o;
        private List<Piece> field_175843_q = Lists.newArrayList();
        private RoomDefinition field_175844_p;

        private void func_175842_f(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n;
            if (this.func_175818_a(structureBoundingBox, 7, 21, 13, 50)) {
                this.fillWithBlocks(world, structureBoundingBox, 7, 0, 21, 13, 0, 50, field_175828_a, field_175828_a, false);
                this.func_181655_a(world, structureBoundingBox, 7, 1, 21, 13, 10, 50, false);
                this.fillWithBlocks(world, structureBoundingBox, 11, 8, 21, 13, 8, 53, field_175828_a, field_175828_a, false);
                n = 0;
                while (n < 4) {
                    this.fillWithBlocks(world, structureBoundingBox, n + 7, n + 5, 21, n + 7, n + 5, 54, field_175826_b, field_175826_b, false);
                    ++n;
                }
                n = 21;
                while (n <= 45) {
                    this.setBlockState(world, field_175824_d, 12, 9, n, structureBoundingBox);
                    n += 3;
                }
            }
            if (this.func_175818_a(structureBoundingBox, 44, 21, 50, 54)) {
                this.fillWithBlocks(world, structureBoundingBox, 44, 0, 21, 50, 0, 50, field_175828_a, field_175828_a, false);
                this.func_181655_a(world, structureBoundingBox, 44, 1, 21, 50, 10, 50, false);
                this.fillWithBlocks(world, structureBoundingBox, 44, 8, 21, 46, 8, 53, field_175828_a, field_175828_a, false);
                n = 0;
                while (n < 4) {
                    this.fillWithBlocks(world, structureBoundingBox, 50 - n, n + 5, 21, 50 - n, n + 5, 54, field_175826_b, field_175826_b, false);
                    ++n;
                }
                n = 21;
                while (n <= 45) {
                    this.setBlockState(world, field_175824_d, 45, 9, n, structureBoundingBox);
                    n += 3;
                }
            }
            if (this.func_175818_a(structureBoundingBox, 8, 44, 49, 54)) {
                this.fillWithBlocks(world, structureBoundingBox, 14, 0, 44, 43, 0, 50, field_175828_a, field_175828_a, false);
                this.func_181655_a(world, structureBoundingBox, 14, 1, 44, 43, 10, 50, false);
                n = 12;
                while (n <= 45) {
                    this.setBlockState(world, field_175824_d, n, 9, 45, structureBoundingBox);
                    this.setBlockState(world, field_175824_d, n, 9, 52, structureBoundingBox);
                    if (n == 12 || n == 18 || n == 24 || n == 33 || n == 39 || n == 45) {
                        this.setBlockState(world, field_175824_d, n, 9, 47, structureBoundingBox);
                        this.setBlockState(world, field_175824_d, n, 9, 50, structureBoundingBox);
                        this.setBlockState(world, field_175824_d, n, 10, 45, structureBoundingBox);
                        this.setBlockState(world, field_175824_d, n, 10, 46, structureBoundingBox);
                        this.setBlockState(world, field_175824_d, n, 10, 51, structureBoundingBox);
                        this.setBlockState(world, field_175824_d, n, 10, 52, structureBoundingBox);
                        this.setBlockState(world, field_175824_d, n, 11, 47, structureBoundingBox);
                        this.setBlockState(world, field_175824_d, n, 11, 50, structureBoundingBox);
                        this.setBlockState(world, field_175824_d, n, 12, 48, structureBoundingBox);
                        this.setBlockState(world, field_175824_d, n, 12, 49, structureBoundingBox);
                    }
                    n += 3;
                }
                n = 0;
                while (n < 3) {
                    this.fillWithBlocks(world, structureBoundingBox, 8 + n, 5 + n, 54, 49 - n, 5 + n, 54, field_175828_a, field_175828_a, false);
                    ++n;
                }
                this.fillWithBlocks(world, structureBoundingBox, 11, 8, 54, 46, 8, 54, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 14, 8, 44, 43, 8, 53, field_175828_a, field_175828_a, false);
            }
        }

        private void func_175837_c(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.func_175818_a(structureBoundingBox, 15, 20, 42, 21)) {
                this.fillWithBlocks(world, structureBoundingBox, 15, 0, 21, 42, 0, 21, field_175828_a, field_175828_a, false);
                this.func_181655_a(world, structureBoundingBox, 26, 1, 21, 31, 3, 21, false);
                this.fillWithBlocks(world, structureBoundingBox, 21, 12, 21, 36, 12, 21, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 17, 11, 21, 40, 11, 21, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 16, 10, 21, 41, 10, 21, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 15, 7, 21, 42, 9, 21, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 16, 6, 21, 41, 6, 21, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 17, 5, 21, 40, 5, 21, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 21, 4, 21, 36, 4, 21, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 22, 3, 21, 26, 3, 21, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 31, 3, 21, 35, 3, 21, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 23, 2, 21, 25, 2, 21, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 32, 2, 21, 34, 2, 21, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 28, 4, 20, 29, 4, 21, field_175826_b, field_175826_b, false);
                this.setBlockState(world, field_175826_b, 27, 3, 21, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 30, 3, 21, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 26, 2, 21, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 31, 2, 21, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 25, 1, 21, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 32, 1, 21, structureBoundingBox);
                int n = 0;
                while (n < 7) {
                    this.setBlockState(world, field_175827_c, 28 - n, 6 + n, 21, structureBoundingBox);
                    this.setBlockState(world, field_175827_c, 29 + n, 6 + n, 21, structureBoundingBox);
                    ++n;
                }
                n = 0;
                while (n < 4) {
                    this.setBlockState(world, field_175827_c, 28 - n, 9 + n, 21, structureBoundingBox);
                    this.setBlockState(world, field_175827_c, 29 + n, 9 + n, 21, structureBoundingBox);
                    ++n;
                }
                this.setBlockState(world, field_175827_c, 28, 12, 21, structureBoundingBox);
                this.setBlockState(world, field_175827_c, 29, 12, 21, structureBoundingBox);
                n = 0;
                while (n < 3) {
                    this.setBlockState(world, field_175827_c, 22 - n * 2, 8, 21, structureBoundingBox);
                    this.setBlockState(world, field_175827_c, 22 - n * 2, 9, 21, structureBoundingBox);
                    this.setBlockState(world, field_175827_c, 35 + n * 2, 8, 21, structureBoundingBox);
                    this.setBlockState(world, field_175827_c, 35 + n * 2, 9, 21, structureBoundingBox);
                    ++n;
                }
                this.func_181655_a(world, structureBoundingBox, 15, 13, 21, 42, 15, 21, false);
                this.func_181655_a(world, structureBoundingBox, 15, 1, 21, 15, 6, 21, false);
                this.func_181655_a(world, structureBoundingBox, 16, 1, 21, 16, 5, 21, false);
                this.func_181655_a(world, structureBoundingBox, 17, 1, 21, 20, 4, 21, false);
                this.func_181655_a(world, structureBoundingBox, 21, 1, 21, 21, 3, 21, false);
                this.func_181655_a(world, structureBoundingBox, 22, 1, 21, 22, 2, 21, false);
                this.func_181655_a(world, structureBoundingBox, 23, 1, 21, 24, 1, 21, false);
                this.func_181655_a(world, structureBoundingBox, 42, 1, 21, 42, 6, 21, false);
                this.func_181655_a(world, structureBoundingBox, 41, 1, 21, 41, 5, 21, false);
                this.func_181655_a(world, structureBoundingBox, 37, 1, 21, 40, 4, 21, false);
                this.func_181655_a(world, structureBoundingBox, 36, 1, 21, 36, 3, 21, false);
                this.func_181655_a(world, structureBoundingBox, 33, 1, 21, 34, 1, 21, false);
                this.func_181655_a(world, structureBoundingBox, 35, 1, 21, 35, 2, 21, false);
            }
        }

        private void func_175840_a(boolean bl, int n, World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n2 = 24;
            if (this.func_175818_a(structureBoundingBox, n, 0, n + 23, 20)) {
                this.fillWithBlocks(world, structureBoundingBox, n + 0, 0, 0, n + 24, 0, 20, field_175828_a, field_175828_a, false);
                this.func_181655_a(world, structureBoundingBox, n + 0, 1, 0, n + 24, 10, 20, false);
                int n3 = 0;
                while (n3 < 4) {
                    this.fillWithBlocks(world, structureBoundingBox, n + n3, n3 + 1, n3, n + n3, n3 + 1, 20, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, n + n3 + 7, n3 + 5, n3 + 7, n + n3 + 7, n3 + 5, 20, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, n + 17 - n3, n3 + 5, n3 + 7, n + 17 - n3, n3 + 5, 20, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, n + 24 - n3, n3 + 1, n3, n + 24 - n3, n3 + 1, 20, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, n + n3 + 1, n3 + 1, n3, n + 23 - n3, n3 + 1, n3, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, n + n3 + 8, n3 + 5, n3 + 7, n + 16 - n3, n3 + 5, n3 + 7, field_175826_b, field_175826_b, false);
                    ++n3;
                }
                this.fillWithBlocks(world, structureBoundingBox, n + 4, 4, 4, n + 6, 4, 20, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, n + 7, 4, 4, n + 17, 4, 6, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, n + 18, 4, 4, n + 20, 4, 20, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, n + 11, 8, 11, n + 13, 8, 20, field_175828_a, field_175828_a, false);
                this.setBlockState(world, field_175824_d, n + 12, 9, 12, structureBoundingBox);
                this.setBlockState(world, field_175824_d, n + 12, 9, 15, structureBoundingBox);
                this.setBlockState(world, field_175824_d, n + 12, 9, 18, structureBoundingBox);
                n3 = bl ? n + 19 : n + 5;
                int n4 = bl ? n + 5 : n + 19;
                int n5 = 20;
                while (n5 >= 5) {
                    this.setBlockState(world, field_175824_d, n3, 5, n5, structureBoundingBox);
                    n5 -= 3;
                }
                n5 = 19;
                while (n5 >= 7) {
                    this.setBlockState(world, field_175824_d, n4, 5, n5, structureBoundingBox);
                    n5 -= 3;
                }
                n5 = 0;
                while (n5 < 4) {
                    int n6 = bl ? n + (24 - (17 - n5 * 3)) : n + 17 - n5 * 3;
                    this.setBlockState(world, field_175824_d, n6, 5, 5, structureBoundingBox);
                    ++n5;
                }
                this.setBlockState(world, field_175824_d, n4, 5, 5, structureBoundingBox);
                this.fillWithBlocks(world, structureBoundingBox, n + 11, 1, 12, n + 13, 7, 12, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, n + 12, 1, 11, n + 12, 7, 13, field_175828_a, field_175828_a, false);
            }
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n = Math.max(world.func_181545_F(), 64) - this.boundingBox.minY;
            this.func_181655_a(world, structureBoundingBox, 0, 0, 0, 58, n, 58, false);
            this.func_175840_a(false, 0, world, random, structureBoundingBox);
            this.func_175840_a(true, 33, world, random, structureBoundingBox);
            this.func_175839_b(world, random, structureBoundingBox);
            this.func_175837_c(world, random, structureBoundingBox);
            this.func_175841_d(world, random, structureBoundingBox);
            this.func_175835_e(world, random, structureBoundingBox);
            this.func_175842_f(world, random, structureBoundingBox);
            this.func_175838_g(world, random, structureBoundingBox);
            int n2 = 0;
            while (n2 < 7) {
                int n3 = 0;
                while (n3 < 7) {
                    if (n3 == 0 && n2 == 3) {
                        n3 = 6;
                    }
                    int n4 = n2 * 9;
                    int n5 = n3 * 9;
                    int n6 = 0;
                    while (n6 < 4) {
                        int n7 = 0;
                        while (n7 < 4) {
                            this.setBlockState(world, field_175826_b, n4 + n6, 0, n5 + n7, structureBoundingBox);
                            this.replaceAirAndLiquidDownwards(world, field_175826_b, n4 + n6, -1, n5 + n7, structureBoundingBox);
                            ++n7;
                        }
                        ++n6;
                    }
                    if (n2 != 0 && n2 != 6) {
                        n3 += 6;
                        continue;
                    }
                    ++n3;
                }
                ++n2;
            }
            n2 = 0;
            while (n2 < 5) {
                this.func_181655_a(world, structureBoundingBox, -1 - n2, 0 + n2 * 2, -1 - n2, -1 - n2, 23, 58 + n2, false);
                this.func_181655_a(world, structureBoundingBox, 58 + n2, 0 + n2 * 2, -1 - n2, 58 + n2, 23, 58 + n2, false);
                this.func_181655_a(world, structureBoundingBox, 0 - n2, 0 + n2 * 2, -1 - n2, 57 + n2, 23, -1 - n2, false);
                this.func_181655_a(world, structureBoundingBox, 0 - n2, 0 + n2 * 2, 58 + n2, 57 + n2, 23, 58 + n2, false);
                ++n2;
            }
            for (Piece piece : this.field_175843_q) {
                if (!piece.getBoundingBox().intersectsWith(structureBoundingBox)) continue;
                piece.addComponentParts(world, random, structureBoundingBox);
            }
            return true;
        }

        public MonumentBuilding(Random random, int n, int n2, EnumFacing enumFacing) {
            super(0);
            this.coordBaseMode = enumFacing;
            switch (this.coordBaseMode) {
                case NORTH: 
                case SOUTH: {
                    this.boundingBox = new StructureBoundingBox(n, 39, n2, n + 58 - 1, 61, n2 + 58 - 1);
                    break;
                }
                default: {
                    this.boundingBox = new StructureBoundingBox(n, 39, n2, n + 58 - 1, 61, n2 + 58 - 1);
                }
            }
            List<RoomDefinition> list = this.func_175836_a(random);
            this.field_175845_o.field_175963_d = true;
            this.field_175843_q.add(new EntryRoom(this.coordBaseMode, this.field_175845_o));
            this.field_175843_q.add(new MonumentCoreRoom(this.coordBaseMode, this.field_175844_p, random));
            ArrayList arrayList = Lists.newArrayList();
            arrayList.add(new XYDoubleRoomFitHelper());
            arrayList.add(new YZDoubleRoomFitHelper());
            arrayList.add(new ZDoubleRoomFitHelper());
            arrayList.add(new XDoubleRoomFitHelper());
            arrayList.add(new YDoubleRoomFitHelper());
            arrayList.add(new FitSimpleRoomTopHelper());
            arrayList.add(new FitSimpleRoomHelper());
            block3: for (RoomDefinition roomDefinition : list) {
                if (roomDefinition.field_175963_d || roomDefinition.func_175961_b()) continue;
                for (MonumentRoomFitHelper object2 : arrayList) {
                    if (!object2.func_175969_a(roomDefinition)) continue;
                    this.field_175843_q.add(object2.func_175968_a(this.coordBaseMode, roomDefinition, random));
                    continue block3;
                }
            }
            int n3 = this.boundingBox.minY;
            int n4 = this.getXWithOffset(9, 22);
            int n5 = this.getZWithOffset(9, 22);
            for (Piece piece : this.field_175843_q) {
                piece.getBoundingBox().offset(n4, n3, n5);
            }
            StructureBoundingBox structureBoundingBox = StructureBoundingBox.func_175899_a(this.getXWithOffset(1, 1), this.getYWithOffset(1), this.getZWithOffset(1, 1), this.getXWithOffset(23, 21), this.getYWithOffset(8), this.getZWithOffset(23, 21));
            StructureBoundingBox structureBoundingBox2 = StructureBoundingBox.func_175899_a(this.getXWithOffset(34, 1), this.getYWithOffset(1), this.getZWithOffset(34, 1), this.getXWithOffset(56, 21), this.getYWithOffset(8), this.getZWithOffset(56, 21));
            StructureBoundingBox structureBoundingBox3 = StructureBoundingBox.func_175899_a(this.getXWithOffset(22, 22), this.getYWithOffset(13), this.getZWithOffset(22, 22), this.getXWithOffset(35, 35), this.getYWithOffset(17), this.getZWithOffset(35, 35));
            int n6 = random.nextInt();
            this.field_175843_q.add(new WingRoom(this.coordBaseMode, structureBoundingBox, n6++));
            this.field_175843_q.add(new WingRoom(this.coordBaseMode, structureBoundingBox2, n6++));
            this.field_175843_q.add(new Penthouse(this.coordBaseMode, structureBoundingBox3));
        }

        public MonumentBuilding() {
        }

        private void func_175838_g(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n;
            if (this.func_175818_a(structureBoundingBox, 14, 21, 20, 43)) {
                this.fillWithBlocks(world, structureBoundingBox, 14, 0, 21, 20, 0, 43, field_175828_a, field_175828_a, false);
                this.func_181655_a(world, structureBoundingBox, 14, 1, 22, 20, 14, 43, false);
                this.fillWithBlocks(world, structureBoundingBox, 18, 12, 22, 20, 12, 39, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 18, 12, 21, 20, 12, 21, field_175826_b, field_175826_b, false);
                n = 0;
                while (n < 4) {
                    this.fillWithBlocks(world, structureBoundingBox, n + 14, n + 9, 21, n + 14, n + 9, 43 - n, field_175826_b, field_175826_b, false);
                    ++n;
                }
                n = 23;
                while (n <= 39) {
                    this.setBlockState(world, field_175824_d, 19, 13, n, structureBoundingBox);
                    n += 3;
                }
            }
            if (this.func_175818_a(structureBoundingBox, 37, 21, 43, 43)) {
                this.fillWithBlocks(world, structureBoundingBox, 37, 0, 21, 43, 0, 43, field_175828_a, field_175828_a, false);
                this.func_181655_a(world, structureBoundingBox, 37, 1, 22, 43, 14, 43, false);
                this.fillWithBlocks(world, structureBoundingBox, 37, 12, 22, 39, 12, 39, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 37, 12, 21, 39, 12, 21, field_175826_b, field_175826_b, false);
                n = 0;
                while (n < 4) {
                    this.fillWithBlocks(world, structureBoundingBox, 43 - n, n + 9, 21, 43 - n, n + 9, 43 - n, field_175826_b, field_175826_b, false);
                    ++n;
                }
                n = 23;
                while (n <= 39) {
                    this.setBlockState(world, field_175824_d, 38, 13, n, structureBoundingBox);
                    n += 3;
                }
            }
            if (this.func_175818_a(structureBoundingBox, 15, 37, 42, 43)) {
                this.fillWithBlocks(world, structureBoundingBox, 21, 0, 37, 36, 0, 43, field_175828_a, field_175828_a, false);
                this.func_181655_a(world, structureBoundingBox, 21, 1, 37, 36, 14, 43, false);
                this.fillWithBlocks(world, structureBoundingBox, 21, 12, 37, 36, 12, 39, field_175828_a, field_175828_a, false);
                n = 0;
                while (n < 4) {
                    this.fillWithBlocks(world, structureBoundingBox, 15 + n, n + 9, 43 - n, 42 - n, n + 9, 43 - n, field_175826_b, field_175826_b, false);
                    ++n;
                }
                n = 21;
                while (n <= 36) {
                    this.setBlockState(world, field_175824_d, n, 13, 38, structureBoundingBox);
                    n += 3;
                }
            }
        }

        private void func_175839_b(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.func_175818_a(structureBoundingBox, 22, 5, 35, 17)) {
                this.func_181655_a(world, structureBoundingBox, 25, 0, 0, 32, 8, 20, false);
                int n = 0;
                while (n < 4) {
                    this.fillWithBlocks(world, structureBoundingBox, 24, 2, 5 + n * 4, 24, 4, 5 + n * 4, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 22, 4, 5 + n * 4, 23, 4, 5 + n * 4, field_175826_b, field_175826_b, false);
                    this.setBlockState(world, field_175826_b, 25, 5, 5 + n * 4, structureBoundingBox);
                    this.setBlockState(world, field_175826_b, 26, 6, 5 + n * 4, structureBoundingBox);
                    this.setBlockState(world, field_175825_e, 26, 5, 5 + n * 4, structureBoundingBox);
                    this.fillWithBlocks(world, structureBoundingBox, 33, 2, 5 + n * 4, 33, 4, 5 + n * 4, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 34, 4, 5 + n * 4, 35, 4, 5 + n * 4, field_175826_b, field_175826_b, false);
                    this.setBlockState(world, field_175826_b, 32, 5, 5 + n * 4, structureBoundingBox);
                    this.setBlockState(world, field_175826_b, 31, 6, 5 + n * 4, structureBoundingBox);
                    this.setBlockState(world, field_175825_e, 31, 5, 5 + n * 4, structureBoundingBox);
                    this.fillWithBlocks(world, structureBoundingBox, 27, 6, 5 + n * 4, 30, 6, 5 + n * 4, field_175828_a, field_175828_a, false);
                    ++n;
                }
            }
        }

        private void func_175835_e(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n;
            if (this.func_175818_a(structureBoundingBox, 0, 21, 6, 58)) {
                this.fillWithBlocks(world, structureBoundingBox, 0, 0, 21, 6, 0, 57, field_175828_a, field_175828_a, false);
                this.func_181655_a(world, structureBoundingBox, 0, 1, 21, 6, 7, 57, false);
                this.fillWithBlocks(world, structureBoundingBox, 4, 4, 21, 6, 4, 53, field_175828_a, field_175828_a, false);
                n = 0;
                while (n < 4) {
                    this.fillWithBlocks(world, structureBoundingBox, n, n + 1, 21, n, n + 1, 57 - n, field_175826_b, field_175826_b, false);
                    ++n;
                }
                n = 23;
                while (n < 53) {
                    this.setBlockState(world, field_175824_d, 5, 5, n, structureBoundingBox);
                    n += 3;
                }
                this.setBlockState(world, field_175824_d, 5, 5, 52, structureBoundingBox);
                n = 0;
                while (n < 4) {
                    this.fillWithBlocks(world, structureBoundingBox, n, n + 1, 21, n, n + 1, 57 - n, field_175826_b, field_175826_b, false);
                    ++n;
                }
                this.fillWithBlocks(world, structureBoundingBox, 4, 1, 52, 6, 3, 52, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 5, 1, 51, 5, 3, 53, field_175828_a, field_175828_a, false);
            }
            if (this.func_175818_a(structureBoundingBox, 51, 21, 58, 58)) {
                this.fillWithBlocks(world, structureBoundingBox, 51, 0, 21, 57, 0, 57, field_175828_a, field_175828_a, false);
                this.func_181655_a(world, structureBoundingBox, 51, 1, 21, 57, 7, 57, false);
                this.fillWithBlocks(world, structureBoundingBox, 51, 4, 21, 53, 4, 53, field_175828_a, field_175828_a, false);
                n = 0;
                while (n < 4) {
                    this.fillWithBlocks(world, structureBoundingBox, 57 - n, n + 1, 21, 57 - n, n + 1, 57 - n, field_175826_b, field_175826_b, false);
                    ++n;
                }
                n = 23;
                while (n < 53) {
                    this.setBlockState(world, field_175824_d, 52, 5, n, structureBoundingBox);
                    n += 3;
                }
                this.setBlockState(world, field_175824_d, 52, 5, 52, structureBoundingBox);
                this.fillWithBlocks(world, structureBoundingBox, 51, 1, 52, 53, 3, 52, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 52, 1, 51, 52, 3, 53, field_175828_a, field_175828_a, false);
            }
            if (this.func_175818_a(structureBoundingBox, 0, 51, 57, 57)) {
                this.fillWithBlocks(world, structureBoundingBox, 7, 0, 51, 50, 0, 57, field_175828_a, field_175828_a, false);
                this.func_181655_a(world, structureBoundingBox, 7, 1, 51, 50, 10, 57, false);
                n = 0;
                while (n < 4) {
                    this.fillWithBlocks(world, structureBoundingBox, n + 1, n + 1, 57 - n, 56 - n, n + 1, 57 - n, field_175826_b, field_175826_b, false);
                    ++n;
                }
            }
        }

        private List<RoomDefinition> func_175836_a(Random random) {
            int n;
            int n2;
            int n3;
            Object object;
            int n4;
            int n5;
            Object[] objectArray;
            int n6;
            int n7;
            int n8;
            RoomDefinition[] roomDefinitionArray = new RoomDefinition[75];
            int n9 = 0;
            while (n9 < 5) {
                n8 = 0;
                while (n8 < 4) {
                    n7 = 0;
                    n6 = MonumentBuilding.func_175820_a(n9, n7, n8);
                    roomDefinitionArray[n6] = new RoomDefinition(n6);
                    ++n8;
                }
                ++n9;
            }
            n9 = 0;
            while (n9 < 5) {
                n8 = 0;
                while (n8 < 4) {
                    n7 = 1;
                    n6 = MonumentBuilding.func_175820_a(n9, n7, n8);
                    roomDefinitionArray[n6] = new RoomDefinition(n6);
                    ++n8;
                }
                ++n9;
            }
            n9 = 1;
            while (n9 < 4) {
                n8 = 0;
                while (n8 < 2) {
                    n7 = 2;
                    n6 = MonumentBuilding.func_175820_a(n9, n7, n8);
                    roomDefinitionArray[n6] = new RoomDefinition(n6);
                    ++n8;
                }
                ++n9;
            }
            this.field_175845_o = roomDefinitionArray[field_175823_g];
            n9 = 0;
            while (n9 < 5) {
                n8 = 0;
                while (n8 < 5) {
                    n7 = 0;
                    while (n7 < 3) {
                        n6 = MonumentBuilding.func_175820_a(n9, n7, n8);
                        if (roomDefinitionArray[n6] != null) {
                            objectArray = EnumFacing.values();
                            n5 = objectArray.length;
                            n4 = 0;
                            while (n4 < n5) {
                                int n10;
                                object = objectArray[n4];
                                n3 = n9 + ((EnumFacing)object).getFrontOffsetX();
                                n2 = n7 + ((EnumFacing)object).getFrontOffsetY();
                                n = n8 + ((EnumFacing)object).getFrontOffsetZ();
                                if (n3 >= 0 && n3 < 5 && n >= 0 && n < 5 && n2 >= 0 && n2 < 3 && roomDefinitionArray[n10 = MonumentBuilding.func_175820_a(n3, n2, n)] != null) {
                                    if (n != n8) {
                                        roomDefinitionArray[n6].func_175957_a(((EnumFacing)object).getOpposite(), roomDefinitionArray[n10]);
                                    } else {
                                        roomDefinitionArray[n6].func_175957_a((EnumFacing)object, roomDefinitionArray[n10]);
                                    }
                                }
                                ++n4;
                            }
                        }
                        ++n7;
                    }
                    ++n8;
                }
                ++n9;
            }
            RoomDefinition roomDefinition = new RoomDefinition(1003);
            roomDefinitionArray[field_175831_h].func_175957_a(EnumFacing.UP, roomDefinition);
            RoomDefinition roomDefinition2 = new RoomDefinition(1001);
            roomDefinitionArray[field_175832_i].func_175957_a(EnumFacing.SOUTH, roomDefinition2);
            RoomDefinition roomDefinition3 = new RoomDefinition(1002);
            roomDefinitionArray[field_175829_j].func_175957_a(EnumFacing.SOUTH, roomDefinition3);
            roomDefinition.field_175963_d = true;
            roomDefinition2.field_175963_d = true;
            roomDefinition3.field_175963_d = true;
            this.field_175845_o.field_175964_e = true;
            this.field_175844_p = roomDefinitionArray[MonumentBuilding.func_175820_a(random.nextInt(4), 0, 2)];
            this.field_175844_p.field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.NORTH.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.NORTH.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            ArrayList arrayList = Lists.newArrayList();
            objectArray = roomDefinitionArray;
            n5 = roomDefinitionArray.length;
            n4 = 0;
            while (n4 < n5) {
                object = objectArray[n4];
                if (object != null) {
                    ((RoomDefinition)object).func_175958_a();
                    arrayList.add(object);
                }
                ++n4;
            }
            roomDefinition.func_175958_a();
            Collections.shuffle(arrayList, random);
            int n11 = 1;
            for (RoomDefinition roomDefinition4 : arrayList) {
                int n12 = 0;
                n3 = 0;
                while (n12 < 2 && n3 < 5) {
                    ++n3;
                    n2 = random.nextInt(6);
                    if (!roomDefinition4.field_175966_c[n2]) continue;
                    n = EnumFacing.getFront(n2).getOpposite().getIndex();
                    roomDefinition4.field_175966_c[n2] = false;
                    roomDefinition4.field_175965_b[n2].field_175966_c[n] = false;
                    if (roomDefinition4.func_175959_a(n11++) && roomDefinition4.field_175965_b[n2].func_175959_a(n11++)) {
                        ++n12;
                        continue;
                    }
                    roomDefinition4.field_175966_c[n2] = true;
                    roomDefinition4.field_175965_b[n2].field_175966_c[n] = true;
                }
            }
            arrayList.add(roomDefinition);
            arrayList.add(roomDefinition2);
            arrayList.add(roomDefinition3);
            return arrayList;
        }

        private void func_175841_d(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.func_175818_a(structureBoundingBox, 21, 21, 36, 36)) {
                this.fillWithBlocks(world, structureBoundingBox, 21, 0, 22, 36, 0, 36, field_175828_a, field_175828_a, false);
                this.func_181655_a(world, structureBoundingBox, 21, 1, 22, 36, 23, 36, false);
                int n = 0;
                while (n < 4) {
                    this.fillWithBlocks(world, structureBoundingBox, 21 + n, 13 + n, 21 + n, 36 - n, 13 + n, 21 + n, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 21 + n, 13 + n, 36 - n, 36 - n, 13 + n, 36 - n, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 21 + n, 13 + n, 22 + n, 21 + n, 13 + n, 35 - n, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 36 - n, 13 + n, 22 + n, 36 - n, 13 + n, 35 - n, field_175826_b, field_175826_b, false);
                    ++n;
                }
                this.fillWithBlocks(world, structureBoundingBox, 25, 16, 25, 32, 16, 32, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 25, 17, 25, 25, 19, 25, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 32, 17, 25, 32, 19, 25, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 25, 17, 32, 25, 19, 32, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 32, 17, 32, 32, 19, 32, field_175826_b, field_175826_b, false);
                this.setBlockState(world, field_175826_b, 26, 20, 26, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 27, 21, 27, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 27, 20, 27, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 26, 20, 31, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 27, 21, 30, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 27, 20, 30, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 31, 20, 31, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 30, 21, 30, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 30, 20, 30, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 31, 20, 26, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 30, 21, 27, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 30, 20, 27, structureBoundingBox);
                this.fillWithBlocks(world, structureBoundingBox, 28, 21, 27, 29, 21, 27, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 27, 21, 28, 27, 21, 29, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 28, 21, 30, 29, 21, 30, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 30, 21, 28, 30, 21, 29, field_175828_a, field_175828_a, false);
            }
        }
    }

    static class XDoubleRoomFitHelper
    implements MonumentRoomFitHelper {
        @Override
        public Piece func_175968_a(EnumFacing enumFacing, RoomDefinition roomDefinition, Random random) {
            roomDefinition.field_175963_d = true;
            roomDefinition.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d = true;
            return new DoubleXRoom(enumFacing, roomDefinition, random);
        }

        private XDoubleRoomFitHelper() {
        }

        @Override
        public boolean func_175969_a(RoomDefinition roomDefinition) {
            return roomDefinition.field_175966_c[EnumFacing.EAST.getIndex()] && !roomDefinition.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d;
        }
    }

    public static class EntryRoom
    extends Piece {
        public EntryRoom(EnumFacing enumFacing, RoomDefinition roomDefinition) {
            super(1, enumFacing, roomDefinition, 1, 1, 1);
        }

        public EntryRoom() {
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 0, 2, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 3, 0, 7, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 1, 2, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 2, 0, 7, 2, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 0, 0, 1, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 1, 0, 7, 1, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 7, 7, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 0, 2, 3, 0, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 1, 0, 6, 3, 0, field_175826_b, field_175826_b, false);
            if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 3, 1, 7, 4, 2, 7, false);
            }
            if (this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 0, 1, 3, 1, 2, 4, false);
            }
            if (this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 6, 1, 3, 7, 2, 4, false);
            }
            return true;
        }
    }

    static class FitSimpleRoomTopHelper
    implements MonumentRoomFitHelper {
        private FitSimpleRoomTopHelper() {
        }

        @Override
        public boolean func_175969_a(RoomDefinition roomDefinition) {
            return !roomDefinition.field_175966_c[EnumFacing.WEST.getIndex()] && !roomDefinition.field_175966_c[EnumFacing.EAST.getIndex()] && !roomDefinition.field_175966_c[EnumFacing.NORTH.getIndex()] && !roomDefinition.field_175966_c[EnumFacing.SOUTH.getIndex()] && !roomDefinition.field_175966_c[EnumFacing.UP.getIndex()];
        }

        @Override
        public Piece func_175968_a(EnumFacing enumFacing, RoomDefinition roomDefinition, Random random) {
            roomDefinition.field_175963_d = true;
            return new SimpleTopRoom(enumFacing, roomDefinition, random);
        }
    }

    public static class DoubleYRoom
    extends Piece {
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(world, structureBoundingBox, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }
            RoomDefinition roomDefinition = this.field_175830_k.field_175965_b[EnumFacing.UP.getIndex()];
            if (roomDefinition.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(world, structureBoundingBox, 1, 8, 1, 6, 8, 6, field_175828_a);
            }
            this.fillWithBlocks(world, structureBoundingBox, 0, 4, 0, 0, 4, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 4, 0, 7, 4, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 4, 0, 6, 4, 0, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 4, 7, 6, 4, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 4, 1, 2, 4, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 4, 2, 1, 4, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 4, 1, 5, 4, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 4, 2, 6, 4, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 4, 5, 2, 4, 6, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 4, 5, 1, 4, 5, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 4, 5, 5, 4, 6, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 4, 5, 6, 4, 5, field_175826_b, field_175826_b, false);
            RoomDefinition roomDefinition2 = this.field_175830_k;
            int n = 1;
            while (n <= 5) {
                int n2 = 0;
                if (roomDefinition2.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                    this.fillWithBlocks(world, structureBoundingBox, 2, n, n2, 2, n + 2, n2, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 5, n, n2, 5, n + 2, n2, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 3, n + 2, n2, 4, n + 2, n2, field_175826_b, field_175826_b, false);
                } else {
                    this.fillWithBlocks(world, structureBoundingBox, 0, n, n2, 7, n + 2, n2, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 0, n + 1, n2, 7, n + 1, n2, field_175828_a, field_175828_a, false);
                }
                n2 = 7;
                if (roomDefinition2.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                    this.fillWithBlocks(world, structureBoundingBox, 2, n, n2, 2, n + 2, n2, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 5, n, n2, 5, n + 2, n2, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 3, n + 2, n2, 4, n + 2, n2, field_175826_b, field_175826_b, false);
                } else {
                    this.fillWithBlocks(world, structureBoundingBox, 0, n, n2, 7, n + 2, n2, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 0, n + 1, n2, 7, n + 1, n2, field_175828_a, field_175828_a, false);
                }
                int n3 = 0;
                if (roomDefinition2.field_175966_c[EnumFacing.WEST.getIndex()]) {
                    this.fillWithBlocks(world, structureBoundingBox, n3, n, 2, n3, n + 2, 2, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, n3, n, 5, n3, n + 2, 5, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, n3, n + 2, 3, n3, n + 2, 4, field_175826_b, field_175826_b, false);
                } else {
                    this.fillWithBlocks(world, structureBoundingBox, n3, n, 0, n3, n + 2, 7, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, n3, n + 1, 0, n3, n + 1, 7, field_175828_a, field_175828_a, false);
                }
                n3 = 7;
                if (roomDefinition2.field_175966_c[EnumFacing.EAST.getIndex()]) {
                    this.fillWithBlocks(world, structureBoundingBox, n3, n, 2, n3, n + 2, 2, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, n3, n, 5, n3, n + 2, 5, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, n3, n + 2, 3, n3, n + 2, 4, field_175826_b, field_175826_b, false);
                } else {
                    this.fillWithBlocks(world, structureBoundingBox, n3, n, 0, n3, n + 2, 7, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, n3, n + 1, 0, n3, n + 1, 7, field_175828_a, field_175828_a, false);
                }
                roomDefinition2 = roomDefinition;
                n += 4;
            }
            return true;
        }

        public DoubleYRoom(EnumFacing enumFacing, RoomDefinition roomDefinition, Random random) {
            super(1, enumFacing, roomDefinition, 1, 2, 1);
        }

        public DoubleYRoom() {
        }
    }

    public static class SimpleRoom
    extends Piece {
        private int field_175833_o;

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            boolean bl;
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(world, structureBoundingBox, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }
            if (this.field_175830_k.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(world, structureBoundingBox, 1, 4, 1, 6, 4, 6, field_175828_a);
            }
            boolean bl2 = bl = this.field_175833_o != 0 && random.nextBoolean() && !this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()] && !this.field_175830_k.field_175966_c[EnumFacing.UP.getIndex()] && this.field_175830_k.func_175960_c() > 1;
            if (this.field_175833_o == 0) {
                this.fillWithBlocks(world, structureBoundingBox, 0, 1, 0, 2, 1, 2, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 0, 3, 0, 2, 3, 2, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 0, 2, 2, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, 2, 0, 2, 2, 0, field_175828_a, field_175828_a, false);
                this.setBlockState(world, field_175825_e, 1, 2, 1, structureBoundingBox);
                this.fillWithBlocks(world, structureBoundingBox, 5, 1, 0, 7, 1, 2, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 5, 3, 0, 7, 3, 2, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 7, 2, 0, 7, 2, 2, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 5, 2, 0, 6, 2, 0, field_175828_a, field_175828_a, false);
                this.setBlockState(world, field_175825_e, 6, 2, 1, structureBoundingBox);
                this.fillWithBlocks(world, structureBoundingBox, 0, 1, 5, 2, 1, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 0, 3, 5, 2, 3, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 0, 2, 5, 0, 2, 7, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, 2, 7, 2, 2, 7, field_175828_a, field_175828_a, false);
                this.setBlockState(world, field_175825_e, 1, 2, 6, structureBoundingBox);
                this.fillWithBlocks(world, structureBoundingBox, 5, 1, 5, 7, 1, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 5, 3, 5, 7, 3, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 7, 2, 5, 7, 2, 7, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 5, 2, 7, 6, 2, 7, field_175828_a, field_175828_a, false);
                this.setBlockState(world, field_175825_e, 6, 2, 6, structureBoundingBox);
                if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                    this.fillWithBlocks(world, structureBoundingBox, 3, 3, 0, 4, 3, 0, field_175826_b, field_175826_b, false);
                } else {
                    this.fillWithBlocks(world, structureBoundingBox, 3, 3, 0, 4, 3, 1, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 3, 2, 0, 4, 2, 0, field_175828_a, field_175828_a, false);
                    this.fillWithBlocks(world, structureBoundingBox, 3, 1, 0, 4, 1, 1, field_175826_b, field_175826_b, false);
                }
                if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                    this.fillWithBlocks(world, structureBoundingBox, 3, 3, 7, 4, 3, 7, field_175826_b, field_175826_b, false);
                } else {
                    this.fillWithBlocks(world, structureBoundingBox, 3, 3, 6, 4, 3, 7, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 3, 2, 7, 4, 2, 7, field_175828_a, field_175828_a, false);
                    this.fillWithBlocks(world, structureBoundingBox, 3, 1, 6, 4, 1, 7, field_175826_b, field_175826_b, false);
                }
                if (this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()]) {
                    this.fillWithBlocks(world, structureBoundingBox, 0, 3, 3, 0, 3, 4, field_175826_b, field_175826_b, false);
                } else {
                    this.fillWithBlocks(world, structureBoundingBox, 0, 3, 3, 1, 3, 4, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 0, 2, 3, 0, 2, 4, field_175828_a, field_175828_a, false);
                    this.fillWithBlocks(world, structureBoundingBox, 0, 1, 3, 1, 1, 4, field_175826_b, field_175826_b, false);
                }
                if (this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()]) {
                    this.fillWithBlocks(world, structureBoundingBox, 7, 3, 3, 7, 3, 4, field_175826_b, field_175826_b, false);
                } else {
                    this.fillWithBlocks(world, structureBoundingBox, 6, 3, 3, 7, 3, 4, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 7, 2, 3, 7, 2, 4, field_175828_a, field_175828_a, false);
                    this.fillWithBlocks(world, structureBoundingBox, 6, 1, 3, 7, 1, 4, field_175826_b, field_175826_b, false);
                }
            } else if (this.field_175833_o == 1) {
                this.fillWithBlocks(world, structureBoundingBox, 2, 1, 2, 2, 3, 2, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 2, 1, 5, 2, 3, 5, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 5, 1, 5, 5, 3, 5, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 5, 1, 2, 5, 3, 2, field_175826_b, field_175826_b, false);
                this.setBlockState(world, field_175825_e, 2, 2, 2, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 2, 2, 5, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 5, 2, 5, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 5, 2, 2, structureBoundingBox);
                this.fillWithBlocks(world, structureBoundingBox, 0, 1, 0, 1, 3, 0, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 0, 1, 1, 0, 3, 1, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 0, 1, 7, 1, 3, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 0, 1, 6, 0, 3, 6, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 6, 1, 7, 7, 3, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 7, 1, 6, 7, 3, 6, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 6, 1, 0, 7, 3, 0, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 7, 1, 1, 7, 3, 1, field_175826_b, field_175826_b, false);
                this.setBlockState(world, field_175828_a, 1, 2, 0, structureBoundingBox);
                this.setBlockState(world, field_175828_a, 0, 2, 1, structureBoundingBox);
                this.setBlockState(world, field_175828_a, 1, 2, 7, structureBoundingBox);
                this.setBlockState(world, field_175828_a, 0, 2, 6, structureBoundingBox);
                this.setBlockState(world, field_175828_a, 6, 2, 7, structureBoundingBox);
                this.setBlockState(world, field_175828_a, 7, 2, 6, structureBoundingBox);
                this.setBlockState(world, field_175828_a, 6, 2, 0, structureBoundingBox);
                this.setBlockState(world, field_175828_a, 7, 2, 1, structureBoundingBox);
                if (!this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                    this.fillWithBlocks(world, structureBoundingBox, 1, 3, 0, 6, 3, 0, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 1, 2, 0, 6, 2, 0, field_175828_a, field_175828_a, false);
                    this.fillWithBlocks(world, structureBoundingBox, 1, 1, 0, 6, 1, 0, field_175826_b, field_175826_b, false);
                }
                if (!this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                    this.fillWithBlocks(world, structureBoundingBox, 1, 3, 7, 6, 3, 7, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 1, 2, 7, 6, 2, 7, field_175828_a, field_175828_a, false);
                    this.fillWithBlocks(world, structureBoundingBox, 1, 1, 7, 6, 1, 7, field_175826_b, field_175826_b, false);
                }
                if (!this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()]) {
                    this.fillWithBlocks(world, structureBoundingBox, 0, 3, 1, 0, 3, 6, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 0, 2, 1, 0, 2, 6, field_175828_a, field_175828_a, false);
                    this.fillWithBlocks(world, structureBoundingBox, 0, 1, 1, 0, 1, 6, field_175826_b, field_175826_b, false);
                }
                if (!this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()]) {
                    this.fillWithBlocks(world, structureBoundingBox, 7, 3, 1, 7, 3, 6, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, 7, 2, 1, 7, 2, 6, field_175828_a, field_175828_a, false);
                    this.fillWithBlocks(world, structureBoundingBox, 7, 1, 1, 7, 1, 6, field_175826_b, field_175826_b, false);
                }
            } else if (this.field_175833_o == 2) {
                this.fillWithBlocks(world, structureBoundingBox, 0, 1, 0, 0, 1, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 7, 1, 0, 7, 1, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, 1, 0, 6, 1, 0, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, 1, 7, 6, 1, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 0, 2, 7, field_175827_c, field_175827_c, false);
                this.fillWithBlocks(world, structureBoundingBox, 7, 2, 0, 7, 2, 7, field_175827_c, field_175827_c, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, 2, 0, 6, 2, 0, field_175827_c, field_175827_c, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, 2, 7, 6, 2, 7, field_175827_c, field_175827_c, false);
                this.fillWithBlocks(world, structureBoundingBox, 0, 3, 0, 0, 3, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 7, 3, 0, 7, 3, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, 3, 0, 6, 3, 0, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, 3, 7, 6, 3, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 0, 1, 3, 0, 2, 4, field_175827_c, field_175827_c, false);
                this.fillWithBlocks(world, structureBoundingBox, 7, 1, 3, 7, 2, 4, field_175827_c, field_175827_c, false);
                this.fillWithBlocks(world, structureBoundingBox, 3, 1, 0, 4, 2, 0, field_175827_c, field_175827_c, false);
                this.fillWithBlocks(world, structureBoundingBox, 3, 1, 7, 4, 2, 7, field_175827_c, field_175827_c, false);
                if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                    this.func_181655_a(world, structureBoundingBox, 3, 1, 0, 4, 2, 0, false);
                }
                if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                    this.func_181655_a(world, structureBoundingBox, 3, 1, 7, 4, 2, 7, false);
                }
                if (this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()]) {
                    this.func_181655_a(world, structureBoundingBox, 0, 1, 3, 0, 2, 4, false);
                }
                if (this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()]) {
                    this.func_181655_a(world, structureBoundingBox, 7, 1, 3, 7, 2, 4, false);
                }
            }
            if (bl) {
                this.fillWithBlocks(world, structureBoundingBox, 3, 1, 3, 4, 1, 4, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 3, 2, 3, 4, 2, 4, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, 3, 3, 3, 4, 3, 4, field_175826_b, field_175826_b, false);
            }
            return true;
        }

        public SimpleRoom() {
        }

        public SimpleRoom(EnumFacing enumFacing, RoomDefinition roomDefinition, Random random) {
            super(1, enumFacing, roomDefinition, 1, 1, 1);
            this.field_175833_o = random.nextInt(3);
        }
    }

    public static class WingRoom
    extends Piece {
        private int field_175834_o;

        public WingRoom() {
        }

        public WingRoom(EnumFacing enumFacing, StructureBoundingBox structureBoundingBox, int n) {
            super(enumFacing, structureBoundingBox);
            this.field_175834_o = n & 1;
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (this.field_175834_o == 0) {
                int n = 0;
                while (n < 4) {
                    this.fillWithBlocks(world, structureBoundingBox, 10 - n, 3 - n, 20 - n, 12 + n, 3 - n, 20, field_175826_b, field_175826_b, false);
                    ++n;
                }
                this.fillWithBlocks(world, structureBoundingBox, 7, 0, 6, 15, 0, 16, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 6, 0, 6, 6, 3, 20, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 16, 0, 6, 16, 3, 20, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 7, 1, 7, 7, 1, 20, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 15, 1, 7, 15, 1, 20, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 7, 1, 6, 9, 3, 6, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 13, 1, 6, 15, 3, 6, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 8, 1, 7, 9, 1, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 13, 1, 7, 14, 1, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 9, 0, 5, 13, 0, 5, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 10, 0, 7, 12, 0, 7, field_175827_c, field_175827_c, false);
                this.fillWithBlocks(world, structureBoundingBox, 8, 0, 10, 8, 0, 12, field_175827_c, field_175827_c, false);
                this.fillWithBlocks(world, structureBoundingBox, 14, 0, 10, 14, 0, 12, field_175827_c, field_175827_c, false);
                n = 18;
                while (n >= 7) {
                    this.setBlockState(world, field_175825_e, 6, 3, n, structureBoundingBox);
                    this.setBlockState(world, field_175825_e, 16, 3, n, structureBoundingBox);
                    n -= 3;
                }
                this.setBlockState(world, field_175825_e, 10, 0, 10, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 12, 0, 10, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 10, 0, 12, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 12, 0, 12, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 8, 3, 6, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 14, 3, 6, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 4, 2, 4, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 4, 1, 4, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 4, 0, 4, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 18, 2, 4, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 18, 1, 4, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 18, 0, 4, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 4, 2, 18, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 4, 1, 18, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 4, 0, 18, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 18, 2, 18, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 18, 1, 18, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 18, 0, 18, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 9, 7, 20, structureBoundingBox);
                this.setBlockState(world, field_175826_b, 13, 7, 20, structureBoundingBox);
                this.fillWithBlocks(world, structureBoundingBox, 6, 0, 21, 7, 4, 21, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 15, 0, 21, 16, 4, 21, field_175826_b, field_175826_b, false);
                this.func_175817_a(world, structureBoundingBox, 11, 2, 16);
            } else if (this.field_175834_o == 1) {
                this.fillWithBlocks(world, structureBoundingBox, 9, 3, 18, 13, 3, 20, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 9, 0, 18, 9, 2, 18, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, 13, 0, 18, 13, 2, 18, field_175826_b, field_175826_b, false);
                int n = 9;
                int n2 = 20;
                int n3 = 5;
                int n4 = 0;
                while (n4 < 2) {
                    this.setBlockState(world, field_175826_b, n, n3 + 1, n2, structureBoundingBox);
                    this.setBlockState(world, field_175825_e, n, n3, n2, structureBoundingBox);
                    this.setBlockState(world, field_175826_b, n, n3 - 1, n2, structureBoundingBox);
                    n = 13;
                    ++n4;
                }
                this.fillWithBlocks(world, structureBoundingBox, 7, 3, 7, 15, 3, 14, field_175826_b, field_175826_b, false);
                n = 10;
                n4 = 0;
                while (n4 < 2) {
                    this.fillWithBlocks(world, structureBoundingBox, n, 0, 10, n, 6, 10, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, n, 0, 12, n, 6, 12, field_175826_b, field_175826_b, false);
                    this.setBlockState(world, field_175825_e, n, 0, 10, structureBoundingBox);
                    this.setBlockState(world, field_175825_e, n, 0, 12, structureBoundingBox);
                    this.setBlockState(world, field_175825_e, n, 4, 10, structureBoundingBox);
                    this.setBlockState(world, field_175825_e, n, 4, 12, structureBoundingBox);
                    n = 12;
                    ++n4;
                }
                n = 8;
                n4 = 0;
                while (n4 < 2) {
                    this.fillWithBlocks(world, structureBoundingBox, n, 0, 7, n, 2, 7, field_175826_b, field_175826_b, false);
                    this.fillWithBlocks(world, structureBoundingBox, n, 0, 14, n, 2, 14, field_175826_b, field_175826_b, false);
                    n = 14;
                    ++n4;
                }
                this.fillWithBlocks(world, structureBoundingBox, 8, 3, 8, 8, 3, 13, field_175827_c, field_175827_c, false);
                this.fillWithBlocks(world, structureBoundingBox, 14, 3, 8, 14, 3, 13, field_175827_c, field_175827_c, false);
                this.func_175817_a(world, structureBoundingBox, 11, 5, 13);
            }
            return true;
        }
    }

    static class YZDoubleRoomFitHelper
    implements MonumentRoomFitHelper {
        @Override
        public Piece func_175968_a(EnumFacing enumFacing, RoomDefinition roomDefinition, Random random) {
            roomDefinition.field_175963_d = true;
            roomDefinition.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
            roomDefinition.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            roomDefinition.field_175965_b[EnumFacing.NORTH.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            return new DoubleYZRoom(enumFacing, roomDefinition, random);
        }

        @Override
        public boolean func_175969_a(RoomDefinition roomDefinition) {
            if (roomDefinition.field_175966_c[EnumFacing.NORTH.getIndex()] && !roomDefinition.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d && roomDefinition.field_175966_c[EnumFacing.UP.getIndex()] && !roomDefinition.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d) {
                RoomDefinition roomDefinition2 = roomDefinition.field_175965_b[EnumFacing.NORTH.getIndex()];
                return roomDefinition2.field_175966_c[EnumFacing.UP.getIndex()] && !roomDefinition2.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d;
            }
            return false;
        }

        private YZDoubleRoomFitHelper() {
        }
    }

    public static class DoubleXRoom
    extends Piece {
        public DoubleXRoom(EnumFacing enumFacing, RoomDefinition roomDefinition, Random random) {
            super(1, enumFacing, roomDefinition, 2, 1, 1);
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            RoomDefinition roomDefinition = this.field_175830_k.field_175965_b[EnumFacing.EAST.getIndex()];
            RoomDefinition roomDefinition2 = this.field_175830_k;
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(world, structureBoundingBox, 8, 0, roomDefinition.field_175966_c[EnumFacing.DOWN.getIndex()]);
                this.func_175821_a(world, structureBoundingBox, 0, 0, roomDefinition2.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }
            if (roomDefinition2.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(world, structureBoundingBox, 1, 4, 1, 7, 4, 6, field_175828_a);
            }
            if (roomDefinition.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(world, structureBoundingBox, 8, 4, 1, 14, 4, 6, field_175828_a);
            }
            this.fillWithBlocks(world, structureBoundingBox, 0, 3, 0, 0, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 15, 3, 0, 15, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 3, 0, 15, 3, 0, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 3, 7, 14, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 2, 0, 0, 2, 7, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(world, structureBoundingBox, 15, 2, 0, 15, 2, 7, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 0, 15, 2, 0, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 2, 7, 14, 2, 7, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 1, 0, 0, 1, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 15, 1, 0, 15, 1, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 0, 15, 1, 0, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 1, 7, 14, 1, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 1, 0, 10, 1, 4, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 2, 0, 9, 2, 3, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 3, 0, 10, 3, 4, field_175826_b, field_175826_b, false);
            this.setBlockState(world, field_175825_e, 6, 2, 3, structureBoundingBox);
            this.setBlockState(world, field_175825_e, 9, 2, 3, structureBoundingBox);
            if (roomDefinition2.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 3, 1, 0, 4, 2, 0, false);
            }
            if (roomDefinition2.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 3, 1, 7, 4, 2, 7, false);
            }
            if (roomDefinition2.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 0, 1, 3, 0, 2, 4, false);
            }
            if (roomDefinition.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 11, 1, 0, 12, 2, 0, false);
            }
            if (roomDefinition.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 11, 1, 7, 12, 2, 7, false);
            }
            if (roomDefinition.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 15, 1, 3, 15, 2, 4, false);
            }
            return true;
        }

        public DoubleXRoom() {
        }
    }

    public static class Penthouse
    extends Piece {
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            this.fillWithBlocks(world, structureBoundingBox, 2, -1, 2, 11, -1, 11, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 0, -1, 0, 1, -1, 11, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(world, structureBoundingBox, 12, -1, 0, 13, -1, 11, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(world, structureBoundingBox, 2, -1, 0, 11, -1, 1, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(world, structureBoundingBox, 2, -1, 12, 11, -1, 13, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 0, 0, 13, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 13, 0, 0, 13, 0, 13, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 0, 12, 0, 0, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 0, 13, 12, 0, 13, field_175826_b, field_175826_b, false);
            int n = 2;
            while (n <= 11) {
                this.setBlockState(world, field_175825_e, 0, 0, n, structureBoundingBox);
                this.setBlockState(world, field_175825_e, 13, 0, n, structureBoundingBox);
                this.setBlockState(world, field_175825_e, n, 0, 0, structureBoundingBox);
                n += 3;
            }
            this.fillWithBlocks(world, structureBoundingBox, 2, 0, 3, 4, 0, 9, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 9, 0, 3, 11, 0, 9, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 4, 0, 9, 9, 0, 11, field_175826_b, field_175826_b, false);
            this.setBlockState(world, field_175826_b, 5, 0, 8, structureBoundingBox);
            this.setBlockState(world, field_175826_b, 8, 0, 8, structureBoundingBox);
            this.setBlockState(world, field_175826_b, 10, 0, 10, structureBoundingBox);
            this.setBlockState(world, field_175826_b, 3, 0, 10, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 3, 0, 3, 3, 0, 7, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(world, structureBoundingBox, 10, 0, 3, 10, 0, 7, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 0, 10, 7, 0, 10, field_175827_c, field_175827_c, false);
            n = 3;
            int n2 = 0;
            while (n2 < 2) {
                int n3 = 2;
                while (n3 <= 8) {
                    this.fillWithBlocks(world, structureBoundingBox, n, 0, n3, n, 2, n3, field_175826_b, field_175826_b, false);
                    n3 += 3;
                }
                n = 10;
                ++n2;
            }
            this.fillWithBlocks(world, structureBoundingBox, 5, 0, 10, 5, 2, 10, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 8, 0, 10, 8, 2, 10, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 6, -1, 7, 7, -1, 8, field_175827_c, field_175827_c, false);
            this.func_181655_a(world, structureBoundingBox, 6, -1, 3, 7, -1, 4, false);
            this.func_175817_a(world, structureBoundingBox, 6, 1, 6);
            return true;
        }

        public Penthouse(EnumFacing enumFacing, StructureBoundingBox structureBoundingBox) {
            super(enumFacing, structureBoundingBox);
        }

        public Penthouse() {
        }
    }

    static class ZDoubleRoomFitHelper
    implements MonumentRoomFitHelper {
        @Override
        public Piece func_175968_a(EnumFacing enumFacing, RoomDefinition roomDefinition, Random random) {
            RoomDefinition roomDefinition2 = roomDefinition;
            if (!roomDefinition.field_175966_c[EnumFacing.NORTH.getIndex()] || roomDefinition.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d) {
                roomDefinition2 = roomDefinition.field_175965_b[EnumFacing.SOUTH.getIndex()];
            }
            roomDefinition2.field_175963_d = true;
            roomDefinition2.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
            return new DoubleZRoom(enumFacing, roomDefinition2, random);
        }

        private ZDoubleRoomFitHelper() {
        }

        @Override
        public boolean func_175969_a(RoomDefinition roomDefinition) {
            return roomDefinition.field_175966_c[EnumFacing.NORTH.getIndex()] && !roomDefinition.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d;
        }
    }

    public static class MonumentCoreRoom
    extends Piece {
        public MonumentCoreRoom(EnumFacing enumFacing, RoomDefinition roomDefinition, Random random) {
            super(1, enumFacing, roomDefinition, 2, 2, 2);
        }

        public MonumentCoreRoom() {
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            int n;
            this.func_175819_a(world, structureBoundingBox, 1, 8, 0, 14, 8, 14, field_175828_a);
            int n2 = 7;
            IBlockState iBlockState = field_175826_b;
            this.fillWithBlocks(world, structureBoundingBox, 0, n2, 0, 0, n2, 15, iBlockState, iBlockState, false);
            this.fillWithBlocks(world, structureBoundingBox, 15, n2, 0, 15, n2, 15, iBlockState, iBlockState, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, n2, 0, 15, n2, 0, iBlockState, iBlockState, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, n2, 15, 14, n2, 15, iBlockState, iBlockState, false);
            n2 = 1;
            while (n2 <= 6) {
                iBlockState = field_175826_b;
                if (n2 == 2 || n2 == 6) {
                    iBlockState = field_175828_a;
                }
                n = 0;
                while (n <= 15) {
                    this.fillWithBlocks(world, structureBoundingBox, n, n2, 0, n, n2, 1, iBlockState, iBlockState, false);
                    this.fillWithBlocks(world, structureBoundingBox, n, n2, 6, n, n2, 9, iBlockState, iBlockState, false);
                    this.fillWithBlocks(world, structureBoundingBox, n, n2, 14, n, n2, 15, iBlockState, iBlockState, false);
                    n += 15;
                }
                this.fillWithBlocks(world, structureBoundingBox, 1, n2, 0, 1, n2, 0, iBlockState, iBlockState, false);
                this.fillWithBlocks(world, structureBoundingBox, 6, n2, 0, 9, n2, 0, iBlockState, iBlockState, false);
                this.fillWithBlocks(world, structureBoundingBox, 14, n2, 0, 14, n2, 0, iBlockState, iBlockState, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, n2, 15, 14, n2, 15, iBlockState, iBlockState, false);
                ++n2;
            }
            this.fillWithBlocks(world, structureBoundingBox, 6, 3, 6, 9, 6, 9, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(world, structureBoundingBox, 7, 4, 7, 8, 5, 8, Blocks.gold_block.getDefaultState(), Blocks.gold_block.getDefaultState(), false);
            n2 = 3;
            while (n2 <= 6) {
                n = 6;
                while (n <= 9) {
                    this.setBlockState(world, field_175825_e, n, n2, 6, structureBoundingBox);
                    this.setBlockState(world, field_175825_e, n, n2, 9, structureBoundingBox);
                    n += 3;
                }
                n2 += 3;
            }
            this.fillWithBlocks(world, structureBoundingBox, 5, 1, 6, 5, 2, 6, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 1, 9, 5, 2, 9, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 10, 1, 6, 10, 2, 6, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 10, 1, 9, 10, 2, 9, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 1, 5, 6, 2, 5, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 9, 1, 5, 9, 2, 5, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 6, 1, 10, 6, 2, 10, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 9, 1, 10, 9, 2, 10, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 2, 5, 5, 6, 5, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 2, 10, 5, 6, 10, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 10, 2, 5, 10, 6, 5, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 10, 2, 10, 10, 6, 10, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 7, 1, 5, 7, 6, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 10, 7, 1, 10, 7, 6, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 7, 9, 5, 7, 14, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 10, 7, 9, 10, 7, 14, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 7, 5, 6, 7, 5, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 1, 7, 10, 6, 7, 10, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 9, 7, 5, 14, 7, 5, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 9, 7, 10, 14, 7, 10, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 1, 2, 2, 1, 3, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 1, 2, 3, 1, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 13, 1, 2, 13, 1, 3, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 12, 1, 2, 12, 1, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 2, 1, 12, 2, 1, 13, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 1, 13, 3, 1, 13, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 13, 1, 12, 13, 1, 13, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 12, 1, 13, 12, 1, 13, field_175826_b, field_175826_b, false);
            return true;
        }
    }

    static class RoomDefinition {
        int field_175962_f;
        boolean field_175963_d;
        RoomDefinition[] field_175965_b = new RoomDefinition[6];
        boolean[] field_175966_c = new boolean[6];
        int field_175967_a;
        boolean field_175964_e;

        public RoomDefinition(int n) {
            this.field_175967_a = n;
        }

        public boolean func_175959_a(int n) {
            if (this.field_175964_e) {
                return true;
            }
            this.field_175962_f = n;
            int n2 = 0;
            while (n2 < 6) {
                if (this.field_175965_b[n2] != null && this.field_175966_c[n2] && this.field_175965_b[n2].field_175962_f != n && this.field_175965_b[n2].func_175959_a(n)) {
                    return true;
                }
                ++n2;
            }
            return false;
        }

        public void func_175957_a(EnumFacing enumFacing, RoomDefinition roomDefinition) {
            this.field_175965_b[enumFacing.getIndex()] = roomDefinition;
            roomDefinition.field_175965_b[enumFacing.getOpposite().getIndex()] = this;
        }

        public void func_175958_a() {
            int n = 0;
            while (n < 6) {
                this.field_175966_c[n] = this.field_175965_b[n] != null;
                ++n;
            }
        }

        public int func_175960_c() {
            int n = 0;
            int n2 = 0;
            while (n2 < 6) {
                if (this.field_175966_c[n2]) {
                    ++n;
                }
                ++n2;
            }
            return n;
        }

        public boolean func_175961_b() {
            return this.field_175967_a >= 75;
        }
    }

    public static abstract class Piece
    extends StructureComponent {
        protected static final int field_175831_h;
        protected static final int field_175832_i;
        protected static final IBlockState field_175828_a;
        protected static final IBlockState field_175825_e;
        protected static final int field_175823_g;
        protected RoomDefinition field_175830_k;
        protected static final IBlockState field_175827_c;
        protected static final IBlockState field_175824_d;
        protected static final IBlockState field_175826_b;
        protected static final IBlockState field_175822_f;
        protected static final int field_175829_j;

        public Piece(EnumFacing enumFacing, StructureBoundingBox structureBoundingBox) {
            super(1);
            this.coordBaseMode = enumFacing;
            this.boundingBox = structureBoundingBox;
        }

        protected boolean func_175818_a(StructureBoundingBox structureBoundingBox, int n, int n2, int n3, int n4) {
            int n5 = this.getXWithOffset(n, n2);
            int n6 = this.getZWithOffset(n, n2);
            int n7 = this.getXWithOffset(n3, n4);
            int n8 = this.getZWithOffset(n3, n4);
            return structureBoundingBox.intersectsWith(Math.min(n5, n7), Math.min(n6, n8), Math.max(n5, n7), Math.max(n6, n8));
        }

        protected void func_181655_a(World world, StructureBoundingBox structureBoundingBox, int n, int n2, int n3, int n4, int n5, int n6, boolean bl) {
            int n7 = n2;
            while (n7 <= n5) {
                int n8 = n;
                while (n8 <= n4) {
                    int n9 = n3;
                    while (n9 <= n6) {
                        if (!bl || this.getBlockStateFromPos(world, n8, n7, n9, structureBoundingBox).getBlock().getMaterial() != Material.air) {
                            if (this.getYWithOffset(n7) >= world.func_181545_F()) {
                                this.setBlockState(world, Blocks.air.getDefaultState(), n8, n7, n9, structureBoundingBox);
                            } else {
                                this.setBlockState(world, field_175822_f, n8, n7, n9, structureBoundingBox);
                            }
                        }
                        ++n9;
                    }
                    ++n8;
                }
                ++n7;
            }
        }

        protected void func_175819_a(World world, StructureBoundingBox structureBoundingBox, int n, int n2, int n3, int n4, int n5, int n6, IBlockState iBlockState) {
            int n7 = n2;
            while (n7 <= n5) {
                int n8 = n;
                while (n8 <= n4) {
                    int n9 = n3;
                    while (n9 <= n6) {
                        if (this.getBlockStateFromPos(world, n8, n7, n9, structureBoundingBox) == field_175822_f) {
                            this.setBlockState(world, iBlockState, n8, n7, n9, structureBoundingBox);
                        }
                        ++n9;
                    }
                    ++n8;
                }
                ++n7;
            }
        }

        public Piece(int n) {
            super(n);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nBTTagCompound) {
        }

        static {
            field_175828_a = Blocks.prismarine.getStateFromMeta(BlockPrismarine.ROUGH_META);
            field_175826_b = Blocks.prismarine.getStateFromMeta(BlockPrismarine.BRICKS_META);
            field_175827_c = Blocks.prismarine.getStateFromMeta(BlockPrismarine.DARK_META);
            field_175824_d = field_175826_b;
            field_175825_e = Blocks.sea_lantern.getDefaultState();
            field_175822_f = Blocks.water.getDefaultState();
            field_175823_g = Piece.func_175820_a(2, 0, 0);
            field_175831_h = Piece.func_175820_a(2, 2, 0);
            field_175832_i = Piece.func_175820_a(0, 1, 0);
            field_175829_j = Piece.func_175820_a(4, 1, 0);
        }

        protected static final int func_175820_a(int n, int n2, int n3) {
            return n2 * 25 + n3 * 5 + n;
        }

        protected boolean func_175817_a(World world, StructureBoundingBox structureBoundingBox, int n, int n2, int n3) {
            int n4;
            int n5;
            int n6 = this.getXWithOffset(n, n3);
            if (structureBoundingBox.isVecInside(new BlockPos(n6, n5 = this.getYWithOffset(n2), n4 = this.getZWithOffset(n, n3)))) {
                EntityGuardian entityGuardian = new EntityGuardian(world);
                entityGuardian.setElder(true);
                entityGuardian.heal(entityGuardian.getMaxHealth());
                entityGuardian.setLocationAndAngles((double)n6 + 0.5, n5, (double)n4 + 0.5, 0.0f, 0.0f);
                entityGuardian.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityGuardian)), null);
                world.spawnEntityInWorld(entityGuardian);
                return true;
            }
            return false;
        }

        protected Piece(int n, EnumFacing enumFacing, RoomDefinition roomDefinition, int n2, int n3, int n4) {
            super(n);
            this.coordBaseMode = enumFacing;
            this.field_175830_k = roomDefinition;
            int n5 = roomDefinition.field_175967_a;
            int n6 = n5 % 5;
            int n7 = n5 / 5 % 5;
            int n8 = n5 / 25;
            this.boundingBox = enumFacing != EnumFacing.NORTH && enumFacing != EnumFacing.SOUTH ? new StructureBoundingBox(0, 0, 0, n4 * 8 - 1, n3 * 4 - 1, n2 * 8 - 1) : new StructureBoundingBox(0, 0, 0, n2 * 8 - 1, n3 * 4 - 1, n4 * 8 - 1);
            switch (enumFacing) {
                case NORTH: {
                    this.boundingBox.offset(n6 * 8, n8 * 4, -(n7 + n4) * 8 + 1);
                    break;
                }
                case SOUTH: {
                    this.boundingBox.offset(n6 * 8, n8 * 4, n7 * 8);
                    break;
                }
                case WEST: {
                    this.boundingBox.offset(-(n7 + n4) * 8 + 1, n8 * 4, n6 * 8);
                    break;
                }
                default: {
                    this.boundingBox.offset(n7 * 8, n8 * 4, n6 * 8);
                }
            }
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nBTTagCompound) {
        }

        protected void func_175821_a(World world, StructureBoundingBox structureBoundingBox, int n, int n2, boolean bl) {
            if (bl) {
                this.fillWithBlocks(world, structureBoundingBox, n + 0, 0, n2 + 0, n + 2, 0, n2 + 8 - 1, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, n + 5, 0, n2 + 0, n + 8 - 1, 0, n2 + 8 - 1, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, n + 3, 0, n2 + 0, n + 4, 0, n2 + 2, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, n + 3, 0, n2 + 5, n + 4, 0, n2 + 8 - 1, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(world, structureBoundingBox, n + 3, 0, n2 + 2, n + 4, 0, n2 + 2, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, n + 3, 0, n2 + 5, n + 4, 0, n2 + 5, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, n + 2, 0, n2 + 3, n + 2, 0, n2 + 4, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(world, structureBoundingBox, n + 5, 0, n2 + 3, n + 5, 0, n2 + 4, field_175826_b, field_175826_b, false);
            } else {
                this.fillWithBlocks(world, structureBoundingBox, n + 0, 0, n2 + 0, n + 8 - 1, 0, n2 + 8 - 1, field_175828_a, field_175828_a, false);
            }
        }

        public Piece() {
            super(0);
        }
    }

    public static class DoubleXYRoom
    extends Piece {
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            RoomDefinition roomDefinition = this.field_175830_k.field_175965_b[EnumFacing.EAST.getIndex()];
            RoomDefinition roomDefinition2 = this.field_175830_k;
            RoomDefinition roomDefinition3 = roomDefinition2.field_175965_b[EnumFacing.UP.getIndex()];
            RoomDefinition roomDefinition4 = roomDefinition.field_175965_b[EnumFacing.UP.getIndex()];
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(world, structureBoundingBox, 8, 0, roomDefinition.field_175966_c[EnumFacing.DOWN.getIndex()]);
                this.func_175821_a(world, structureBoundingBox, 0, 0, roomDefinition2.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }
            if (roomDefinition3.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(world, structureBoundingBox, 1, 8, 1, 7, 8, 6, field_175828_a);
            }
            if (roomDefinition4.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(world, structureBoundingBox, 8, 8, 1, 14, 8, 6, field_175828_a);
            }
            int n = 1;
            while (n <= 7) {
                IBlockState iBlockState = field_175826_b;
                if (n == 2 || n == 6) {
                    iBlockState = field_175828_a;
                }
                this.fillWithBlocks(world, structureBoundingBox, 0, n, 0, 0, n, 7, iBlockState, iBlockState, false);
                this.fillWithBlocks(world, structureBoundingBox, 15, n, 0, 15, n, 7, iBlockState, iBlockState, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, n, 0, 15, n, 0, iBlockState, iBlockState, false);
                this.fillWithBlocks(world, structureBoundingBox, 1, n, 7, 14, n, 7, iBlockState, iBlockState, false);
                ++n;
            }
            this.fillWithBlocks(world, structureBoundingBox, 2, 1, 3, 2, 7, 4, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 1, 2, 4, 7, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 3, 1, 5, 4, 7, 5, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 13, 1, 3, 13, 7, 4, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 11, 1, 2, 12, 7, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 11, 1, 5, 12, 7, 5, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 1, 3, 5, 3, 4, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 10, 1, 3, 10, 3, 4, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 7, 2, 10, 7, 5, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 5, 2, 5, 7, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 10, 5, 2, 10, 7, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 5, 5, 5, 5, 7, 5, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 10, 5, 5, 10, 7, 5, field_175826_b, field_175826_b, false);
            this.setBlockState(world, field_175826_b, 6, 6, 2, structureBoundingBox);
            this.setBlockState(world, field_175826_b, 9, 6, 2, structureBoundingBox);
            this.setBlockState(world, field_175826_b, 6, 6, 5, structureBoundingBox);
            this.setBlockState(world, field_175826_b, 9, 6, 5, structureBoundingBox);
            this.fillWithBlocks(world, structureBoundingBox, 5, 4, 3, 6, 4, 4, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(world, structureBoundingBox, 9, 4, 3, 10, 4, 4, field_175826_b, field_175826_b, false);
            this.setBlockState(world, field_175825_e, 5, 4, 2, structureBoundingBox);
            this.setBlockState(world, field_175825_e, 5, 4, 5, structureBoundingBox);
            this.setBlockState(world, field_175825_e, 10, 4, 2, structureBoundingBox);
            this.setBlockState(world, field_175825_e, 10, 4, 5, structureBoundingBox);
            if (roomDefinition2.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 3, 1, 0, 4, 2, 0, false);
            }
            if (roomDefinition2.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 3, 1, 7, 4, 2, 7, false);
            }
            if (roomDefinition2.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 0, 1, 3, 0, 2, 4, false);
            }
            if (roomDefinition.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 11, 1, 0, 12, 2, 0, false);
            }
            if (roomDefinition.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 11, 1, 7, 12, 2, 7, false);
            }
            if (roomDefinition.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 15, 1, 3, 15, 2, 4, false);
            }
            if (roomDefinition3.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 3, 5, 0, 4, 6, 0, false);
            }
            if (roomDefinition3.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 3, 5, 7, 4, 6, 7, false);
            }
            if (roomDefinition3.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 0, 5, 3, 0, 6, 4, false);
            }
            if (roomDefinition4.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 11, 5, 0, 12, 6, 0, false);
            }
            if (roomDefinition4.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 11, 5, 7, 12, 6, 7, false);
            }
            if (roomDefinition4.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_181655_a(world, structureBoundingBox, 15, 5, 3, 15, 6, 4, false);
            }
            return true;
        }

        public DoubleXYRoom(EnumFacing enumFacing, RoomDefinition roomDefinition, Random random) {
            super(1, enumFacing, roomDefinition, 2, 2, 1);
        }

        public DoubleXYRoom() {
        }
    }
}

