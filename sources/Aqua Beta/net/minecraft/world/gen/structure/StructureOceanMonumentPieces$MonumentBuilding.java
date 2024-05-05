package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

public static class StructureOceanMonumentPieces.MonumentBuilding
extends StructureOceanMonumentPieces.Piece {
    private StructureOceanMonumentPieces.RoomDefinition field_175845_o;
    private StructureOceanMonumentPieces.RoomDefinition field_175844_p;
    private List<StructureOceanMonumentPieces.Piece> field_175843_q = Lists.newArrayList();

    public StructureOceanMonumentPieces.MonumentBuilding() {
    }

    public StructureOceanMonumentPieces.MonumentBuilding(Random p_i45599_1_, int p_i45599_2_, int p_i45599_3_, EnumFacing p_i45599_4_) {
        super(0);
        this.coordBaseMode = p_i45599_4_;
        switch (StructureOceanMonumentPieces.1.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
            case 1: 
            case 2: {
                this.boundingBox = new StructureBoundingBox(p_i45599_2_, 39, p_i45599_3_, p_i45599_2_ + 58 - 1, 61, p_i45599_3_ + 58 - 1);
                break;
            }
            default: {
                this.boundingBox = new StructureBoundingBox(p_i45599_2_, 39, p_i45599_3_, p_i45599_2_ + 58 - 1, 61, p_i45599_3_ + 58 - 1);
            }
        }
        List<StructureOceanMonumentPieces.RoomDefinition> list = this.func_175836_a(p_i45599_1_);
        this.field_175845_o.field_175963_d = true;
        this.field_175843_q.add((Object)new StructureOceanMonumentPieces.EntryRoom(this.coordBaseMode, this.field_175845_o));
        this.field_175843_q.add((Object)new StructureOceanMonumentPieces.MonumentCoreRoom(this.coordBaseMode, this.field_175844_p, p_i45599_1_));
        ArrayList list1 = Lists.newArrayList();
        list1.add((Object)new StructureOceanMonumentPieces.XYDoubleRoomFitHelper(null));
        list1.add((Object)new StructureOceanMonumentPieces.YZDoubleRoomFitHelper(null));
        list1.add((Object)new StructureOceanMonumentPieces.ZDoubleRoomFitHelper(null));
        list1.add((Object)new StructureOceanMonumentPieces.XDoubleRoomFitHelper(null));
        list1.add((Object)new StructureOceanMonumentPieces.YDoubleRoomFitHelper(null));
        list1.add((Object)new StructureOceanMonumentPieces.FitSimpleRoomTopHelper(null));
        list1.add((Object)new StructureOceanMonumentPieces.FitSimpleRoomHelper(null));
        block3: for (StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition : list) {
            if (structureoceanmonumentpieces$roomdefinition.field_175963_d || structureoceanmonumentpieces$roomdefinition.func_175961_b()) continue;
            for (Iterator structureoceanmonumentpieces$monumentroomfithelper : list1) {
                if (!structureoceanmonumentpieces$monumentroomfithelper.func_175969_a(structureoceanmonumentpieces$roomdefinition)) continue;
                this.field_175843_q.add((Object)structureoceanmonumentpieces$monumentroomfithelper.func_175968_a(this.coordBaseMode, structureoceanmonumentpieces$roomdefinition, p_i45599_1_));
                continue block3;
            }
        }
        int j = this.boundingBox.minY;
        int k = this.getXWithOffset(9, 22);
        int l = this.getZWithOffset(9, 22);
        for (StructureOceanMonumentPieces.Piece structureoceanmonumentpieces$piece : this.field_175843_q) {
            structureoceanmonumentpieces$piece.getBoundingBox().offset(k, j, l);
        }
        StructureBoundingBox structureboundingbox1 = StructureBoundingBox.func_175899_a((int)this.getXWithOffset(1, 1), (int)this.getYWithOffset(1), (int)this.getZWithOffset(1, 1), (int)this.getXWithOffset(23, 21), (int)this.getYWithOffset(8), (int)this.getZWithOffset(23, 21));
        StructureBoundingBox structureboundingbox2 = StructureBoundingBox.func_175899_a((int)this.getXWithOffset(34, 1), (int)this.getYWithOffset(1), (int)this.getZWithOffset(34, 1), (int)this.getXWithOffset(56, 21), (int)this.getYWithOffset(8), (int)this.getZWithOffset(56, 21));
        StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175899_a((int)this.getXWithOffset(22, 22), (int)this.getYWithOffset(13), (int)this.getZWithOffset(22, 22), (int)this.getXWithOffset(35, 35), (int)this.getYWithOffset(17), (int)this.getZWithOffset(35, 35));
        int i = p_i45599_1_.nextInt();
        this.field_175843_q.add((Object)new StructureOceanMonumentPieces.WingRoom(this.coordBaseMode, structureboundingbox1, i++));
        this.field_175843_q.add((Object)new StructureOceanMonumentPieces.WingRoom(this.coordBaseMode, structureboundingbox2, i++));
        this.field_175843_q.add((Object)new StructureOceanMonumentPieces.Penthouse(this.coordBaseMode, structureboundingbox));
    }

    private List<StructureOceanMonumentPieces.RoomDefinition> func_175836_a(Random p_175836_1_) {
        StructureOceanMonumentPieces.RoomDefinition[] astructureoceanmonumentpieces$roomdefinition = new StructureOceanMonumentPieces.RoomDefinition[75];
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 4; ++j) {
                int k = 0;
                int l = StructureOceanMonumentPieces.MonumentBuilding.func_175820_a((int)i, (int)k, (int)j);
                astructureoceanmonumentpieces$roomdefinition[l] = new StructureOceanMonumentPieces.RoomDefinition(l);
            }
        }
        for (int i2 = 0; i2 < 5; ++i2) {
            for (int l2 = 0; l2 < 4; ++l2) {
                int k3 = 1;
                int j4 = StructureOceanMonumentPieces.MonumentBuilding.func_175820_a((int)i2, (int)k3, (int)l2);
                astructureoceanmonumentpieces$roomdefinition[j4] = new StructureOceanMonumentPieces.RoomDefinition(j4);
            }
        }
        for (int j2 = 1; j2 < 4; ++j2) {
            for (int i3 = 0; i3 < 2; ++i3) {
                int l3 = 2;
                int k4 = StructureOceanMonumentPieces.MonumentBuilding.func_175820_a((int)j2, (int)l3, (int)i3);
                astructureoceanmonumentpieces$roomdefinition[k4] = new StructureOceanMonumentPieces.RoomDefinition(k4);
            }
        }
        this.field_175845_o = astructureoceanmonumentpieces$roomdefinition[field_175823_g];
        for (int k2 = 0; k2 < 5; ++k2) {
            for (int j3 = 0; j3 < 5; ++j3) {
                for (int i4 = 0; i4 < 3; ++i4) {
                    int l4 = StructureOceanMonumentPieces.MonumentBuilding.func_175820_a((int)k2, (int)i4, (int)j3);
                    if (astructureoceanmonumentpieces$roomdefinition[l4] == null) continue;
                    for (EnumFacing enumFacing : EnumFacing.values()) {
                        int l1;
                        int i1 = k2 + enumFacing.getFrontOffsetX();
                        int j1 = i4 + enumFacing.getFrontOffsetY();
                        int k1 = j3 + enumFacing.getFrontOffsetZ();
                        if (i1 < 0 || i1 >= 5 || k1 < 0 || k1 >= 5 || j1 < 0 || j1 >= 3 || astructureoceanmonumentpieces$roomdefinition[l1 = StructureOceanMonumentPieces.MonumentBuilding.func_175820_a((int)i1, (int)j1, (int)k1)] == null) continue;
                        if (k1 != j3) {
                            astructureoceanmonumentpieces$roomdefinition[l4].func_175957_a(enumFacing.getOpposite(), astructureoceanmonumentpieces$roomdefinition[l1]);
                            continue;
                        }
                        astructureoceanmonumentpieces$roomdefinition[l4].func_175957_a(enumFacing, astructureoceanmonumentpieces$roomdefinition[l1]);
                    }
                }
            }
        }
        StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition = new StructureOceanMonumentPieces.RoomDefinition(1003);
        astructureoceanmonumentpieces$roomdefinition[field_175831_h].func_175957_a(EnumFacing.UP, structureoceanmonumentpieces$roomdefinition);
        StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition1 = new StructureOceanMonumentPieces.RoomDefinition(1001);
        astructureoceanmonumentpieces$roomdefinition[field_175832_i].func_175957_a(EnumFacing.SOUTH, structureoceanmonumentpieces$roomdefinition1);
        StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition2 = new StructureOceanMonumentPieces.RoomDefinition(1002);
        astructureoceanmonumentpieces$roomdefinition[field_175829_j].func_175957_a(EnumFacing.SOUTH, structureoceanmonumentpieces$roomdefinition2);
        structureoceanmonumentpieces$roomdefinition.field_175963_d = true;
        structureoceanmonumentpieces$roomdefinition1.field_175963_d = true;
        structureoceanmonumentpieces$roomdefinition2.field_175963_d = true;
        this.field_175845_o.field_175964_e = true;
        this.field_175844_p = astructureoceanmonumentpieces$roomdefinition[StructureOceanMonumentPieces.MonumentBuilding.func_175820_a((int)p_175836_1_.nextInt(4), (int)0, (int)2)];
        this.field_175844_p.field_175963_d = true;
        this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d = true;
        this.field_175844_p.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
        this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
        this.field_175844_p.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
        this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
        this.field_175844_p.field_175965_b[EnumFacing.NORTH.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
        this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.NORTH.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
        ArrayList list = Lists.newArrayList();
        for (EnumFacing enumFacing : astructureoceanmonumentpieces$roomdefinition) {
            if (enumFacing == null) continue;
            enumFacing.func_175958_a();
            list.add((Object)enumFacing);
        }
        structureoceanmonumentpieces$roomdefinition.func_175958_a();
        Collections.shuffle((List)list, (Random)p_175836_1_);
        int i5 = 1;
        for (StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition3 : list) {
            int n = 0;
            for (int k5 = 0; n < 2 && k5 < 5; ++k5) {
                int l5 = p_175836_1_.nextInt(6);
                if (!structureoceanmonumentpieces$roomdefinition3.field_175966_c[l5]) continue;
                int i6 = EnumFacing.getFront((int)l5).getOpposite().getIndex();
                structureoceanmonumentpieces$roomdefinition3.field_175966_c[l5] = false;
                structureoceanmonumentpieces$roomdefinition3.field_175965_b[l5].field_175966_c[i6] = false;
                if (structureoceanmonumentpieces$roomdefinition3.func_175959_a(i5++) && structureoceanmonumentpieces$roomdefinition3.field_175965_b[l5].func_175959_a(i5++)) {
                    ++n;
                    continue;
                }
                structureoceanmonumentpieces$roomdefinition3.field_175966_c[l5] = true;
                structureoceanmonumentpieces$roomdefinition3.field_175965_b[l5].field_175966_c[i6] = true;
            }
        }
        list.add((Object)structureoceanmonumentpieces$roomdefinition);
        list.add((Object)structureoceanmonumentpieces$roomdefinition1);
        list.add((Object)structureoceanmonumentpieces$roomdefinition2);
        return list;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        int i = Math.max((int)worldIn.getSeaLevel(), (int)64) - this.boundingBox.minY;
        this.func_181655_a(worldIn, structureBoundingBoxIn, 0, 0, 0, 58, i, 58, false);
        this.func_175840_a(false, 0, worldIn, randomIn, structureBoundingBoxIn);
        this.func_175840_a(true, 33, worldIn, randomIn, structureBoundingBoxIn);
        this.func_175839_b(worldIn, randomIn, structureBoundingBoxIn);
        this.func_175837_c(worldIn, randomIn, structureBoundingBoxIn);
        this.func_175841_d(worldIn, randomIn, structureBoundingBoxIn);
        this.func_175835_e(worldIn, randomIn, structureBoundingBoxIn);
        this.func_175842_f(worldIn, randomIn, structureBoundingBoxIn);
        this.func_175838_g(worldIn, randomIn, structureBoundingBoxIn);
        for (int j = 0; j < 7; ++j) {
            int k = 0;
            while (k < 7) {
                if (k == 0 && j == 3) {
                    k = 6;
                }
                int l = j * 9;
                int i1 = k * 9;
                for (int j1 = 0; j1 < 4; ++j1) {
                    for (int k1 = 0; k1 < 4; ++k1) {
                        this.setBlockState(worldIn, field_175826_b, l + j1, 0, i1 + k1, structureBoundingBoxIn);
                        this.replaceAirAndLiquidDownwards(worldIn, field_175826_b, l + j1, -1, i1 + k1, structureBoundingBoxIn);
                    }
                }
                if (j != 0 && j != 6) {
                    k += 6;
                    continue;
                }
                ++k;
            }
        }
        for (int l1 = 0; l1 < 5; ++l1) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, -1 - l1, 0 + l1 * 2, -1 - l1, -1 - l1, 23, 58 + l1, false);
            this.func_181655_a(worldIn, structureBoundingBoxIn, 58 + l1, 0 + l1 * 2, -1 - l1, 58 + l1, 23, 58 + l1, false);
            this.func_181655_a(worldIn, structureBoundingBoxIn, 0 - l1, 0 + l1 * 2, -1 - l1, 57 + l1, 23, -1 - l1, false);
            this.func_181655_a(worldIn, structureBoundingBoxIn, 0 - l1, 0 + l1 * 2, 58 + l1, 57 + l1, 23, 58 + l1, false);
        }
        for (StructureOceanMonumentPieces.Piece structureoceanmonumentpieces$piece : this.field_175843_q) {
            if (!structureoceanmonumentpieces$piece.getBoundingBox().intersectsWith(structureBoundingBoxIn)) continue;
            structureoceanmonumentpieces$piece.addComponentParts(worldIn, randomIn, structureBoundingBoxIn);
        }
        return true;
    }

    private void func_175840_a(boolean p_175840_1_, int p_175840_2_, World worldIn, Random p_175840_4_, StructureBoundingBox p_175840_5_) {
        int i = 24;
        if (this.func_175818_a(p_175840_5_, p_175840_2_, 0, p_175840_2_ + 23, 20)) {
            this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 0, 0, 0, p_175840_2_ + 24, 0, 20, field_175828_a, field_175828_a, false);
            this.func_181655_a(worldIn, p_175840_5_, p_175840_2_ + 0, 1, 0, p_175840_2_ + 24, 10, 20, false);
            for (int j = 0; j < 4; ++j) {
                this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + j, j + 1, j, p_175840_2_ + j, j + 1, 20, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + j + 7, j + 5, j + 7, p_175840_2_ + j + 7, j + 5, 20, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 17 - j, j + 5, j + 7, p_175840_2_ + 17 - j, j + 5, 20, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 24 - j, j + 1, j, p_175840_2_ + 24 - j, j + 1, 20, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + j + 1, j + 1, j, p_175840_2_ + 23 - j, j + 1, j, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + j + 8, j + 5, j + 7, p_175840_2_ + 16 - j, j + 5, j + 7, field_175826_b, field_175826_b, false);
            }
            this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 4, 4, 4, p_175840_2_ + 6, 4, 20, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 7, 4, 4, p_175840_2_ + 17, 4, 6, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 18, 4, 4, p_175840_2_ + 20, 4, 20, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 11, 8, 11, p_175840_2_ + 13, 8, 20, field_175828_a, field_175828_a, false);
            this.setBlockState(worldIn, field_175824_d, p_175840_2_ + 12, 9, 12, p_175840_5_);
            this.setBlockState(worldIn, field_175824_d, p_175840_2_ + 12, 9, 15, p_175840_5_);
            this.setBlockState(worldIn, field_175824_d, p_175840_2_ + 12, 9, 18, p_175840_5_);
            int j1 = p_175840_1_ ? p_175840_2_ + 19 : p_175840_2_ + 5;
            int k = p_175840_1_ ? p_175840_2_ + 5 : p_175840_2_ + 19;
            for (int l = 20; l >= 5; l -= 3) {
                this.setBlockState(worldIn, field_175824_d, j1, 5, l, p_175840_5_);
            }
            for (int k1 = 19; k1 >= 7; k1 -= 3) {
                this.setBlockState(worldIn, field_175824_d, k, 5, k1, p_175840_5_);
            }
            for (int l1 = 0; l1 < 4; ++l1) {
                int i1 = p_175840_1_ ? p_175840_2_ + (24 - (17 - l1 * 3)) : p_175840_2_ + 17 - l1 * 3;
                this.setBlockState(worldIn, field_175824_d, i1, 5, 5, p_175840_5_);
            }
            this.setBlockState(worldIn, field_175824_d, k, 5, 5, p_175840_5_);
            this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 11, 1, 12, p_175840_2_ + 13, 7, 12, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 12, 1, 11, p_175840_2_ + 12, 7, 13, field_175828_a, field_175828_a, false);
        }
    }

    private void func_175839_b(World worldIn, Random p_175839_2_, StructureBoundingBox p_175839_3_) {
        if (this.func_175818_a(p_175839_3_, 22, 5, 35, 17)) {
            this.func_181655_a(worldIn, p_175839_3_, 25, 0, 0, 32, 8, 20, false);
            for (int i = 0; i < 4; ++i) {
                this.fillWithBlocks(worldIn, p_175839_3_, 24, 2, 5 + i * 4, 24, 4, 5 + i * 4, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, p_175839_3_, 22, 4, 5 + i * 4, 23, 4, 5 + i * 4, field_175826_b, field_175826_b, false);
                this.setBlockState(worldIn, field_175826_b, 25, 5, 5 + i * 4, p_175839_3_);
                this.setBlockState(worldIn, field_175826_b, 26, 6, 5 + i * 4, p_175839_3_);
                this.setBlockState(worldIn, field_175825_e, 26, 5, 5 + i * 4, p_175839_3_);
                this.fillWithBlocks(worldIn, p_175839_3_, 33, 2, 5 + i * 4, 33, 4, 5 + i * 4, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, p_175839_3_, 34, 4, 5 + i * 4, 35, 4, 5 + i * 4, field_175826_b, field_175826_b, false);
                this.setBlockState(worldIn, field_175826_b, 32, 5, 5 + i * 4, p_175839_3_);
                this.setBlockState(worldIn, field_175826_b, 31, 6, 5 + i * 4, p_175839_3_);
                this.setBlockState(worldIn, field_175825_e, 31, 5, 5 + i * 4, p_175839_3_);
                this.fillWithBlocks(worldIn, p_175839_3_, 27, 6, 5 + i * 4, 30, 6, 5 + i * 4, field_175828_a, field_175828_a, false);
            }
        }
    }

    private void func_175837_c(World worldIn, Random p_175837_2_, StructureBoundingBox p_175837_3_) {
        if (this.func_175818_a(p_175837_3_, 15, 20, 42, 21)) {
            this.fillWithBlocks(worldIn, p_175837_3_, 15, 0, 21, 42, 0, 21, field_175828_a, field_175828_a, false);
            this.func_181655_a(worldIn, p_175837_3_, 26, 1, 21, 31, 3, 21, false);
            this.fillWithBlocks(worldIn, p_175837_3_, 21, 12, 21, 36, 12, 21, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175837_3_, 17, 11, 21, 40, 11, 21, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175837_3_, 16, 10, 21, 41, 10, 21, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175837_3_, 15, 7, 21, 42, 9, 21, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175837_3_, 16, 6, 21, 41, 6, 21, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175837_3_, 17, 5, 21, 40, 5, 21, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175837_3_, 21, 4, 21, 36, 4, 21, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175837_3_, 22, 3, 21, 26, 3, 21, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175837_3_, 31, 3, 21, 35, 3, 21, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175837_3_, 23, 2, 21, 25, 2, 21, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175837_3_, 32, 2, 21, 34, 2, 21, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175837_3_, 28, 4, 20, 29, 4, 21, field_175826_b, field_175826_b, false);
            this.setBlockState(worldIn, field_175826_b, 27, 3, 21, p_175837_3_);
            this.setBlockState(worldIn, field_175826_b, 30, 3, 21, p_175837_3_);
            this.setBlockState(worldIn, field_175826_b, 26, 2, 21, p_175837_3_);
            this.setBlockState(worldIn, field_175826_b, 31, 2, 21, p_175837_3_);
            this.setBlockState(worldIn, field_175826_b, 25, 1, 21, p_175837_3_);
            this.setBlockState(worldIn, field_175826_b, 32, 1, 21, p_175837_3_);
            for (int i = 0; i < 7; ++i) {
                this.setBlockState(worldIn, field_175827_c, 28 - i, 6 + i, 21, p_175837_3_);
                this.setBlockState(worldIn, field_175827_c, 29 + i, 6 + i, 21, p_175837_3_);
            }
            for (int j = 0; j < 4; ++j) {
                this.setBlockState(worldIn, field_175827_c, 28 - j, 9 + j, 21, p_175837_3_);
                this.setBlockState(worldIn, field_175827_c, 29 + j, 9 + j, 21, p_175837_3_);
            }
            this.setBlockState(worldIn, field_175827_c, 28, 12, 21, p_175837_3_);
            this.setBlockState(worldIn, field_175827_c, 29, 12, 21, p_175837_3_);
            for (int k = 0; k < 3; ++k) {
                this.setBlockState(worldIn, field_175827_c, 22 - k * 2, 8, 21, p_175837_3_);
                this.setBlockState(worldIn, field_175827_c, 22 - k * 2, 9, 21, p_175837_3_);
                this.setBlockState(worldIn, field_175827_c, 35 + k * 2, 8, 21, p_175837_3_);
                this.setBlockState(worldIn, field_175827_c, 35 + k * 2, 9, 21, p_175837_3_);
            }
            this.func_181655_a(worldIn, p_175837_3_, 15, 13, 21, 42, 15, 21, false);
            this.func_181655_a(worldIn, p_175837_3_, 15, 1, 21, 15, 6, 21, false);
            this.func_181655_a(worldIn, p_175837_3_, 16, 1, 21, 16, 5, 21, false);
            this.func_181655_a(worldIn, p_175837_3_, 17, 1, 21, 20, 4, 21, false);
            this.func_181655_a(worldIn, p_175837_3_, 21, 1, 21, 21, 3, 21, false);
            this.func_181655_a(worldIn, p_175837_3_, 22, 1, 21, 22, 2, 21, false);
            this.func_181655_a(worldIn, p_175837_3_, 23, 1, 21, 24, 1, 21, false);
            this.func_181655_a(worldIn, p_175837_3_, 42, 1, 21, 42, 6, 21, false);
            this.func_181655_a(worldIn, p_175837_3_, 41, 1, 21, 41, 5, 21, false);
            this.func_181655_a(worldIn, p_175837_3_, 37, 1, 21, 40, 4, 21, false);
            this.func_181655_a(worldIn, p_175837_3_, 36, 1, 21, 36, 3, 21, false);
            this.func_181655_a(worldIn, p_175837_3_, 33, 1, 21, 34, 1, 21, false);
            this.func_181655_a(worldIn, p_175837_3_, 35, 1, 21, 35, 2, 21, false);
        }
    }

    private void func_175841_d(World worldIn, Random p_175841_2_, StructureBoundingBox p_175841_3_) {
        if (this.func_175818_a(p_175841_3_, 21, 21, 36, 36)) {
            this.fillWithBlocks(worldIn, p_175841_3_, 21, 0, 22, 36, 0, 36, field_175828_a, field_175828_a, false);
            this.func_181655_a(worldIn, p_175841_3_, 21, 1, 22, 36, 23, 36, false);
            for (int i = 0; i < 4; ++i) {
                this.fillWithBlocks(worldIn, p_175841_3_, 21 + i, 13 + i, 21 + i, 36 - i, 13 + i, 21 + i, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, p_175841_3_, 21 + i, 13 + i, 36 - i, 36 - i, 13 + i, 36 - i, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, p_175841_3_, 21 + i, 13 + i, 22 + i, 21 + i, 13 + i, 35 - i, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, p_175841_3_, 36 - i, 13 + i, 22 + i, 36 - i, 13 + i, 35 - i, field_175826_b, field_175826_b, false);
            }
            this.fillWithBlocks(worldIn, p_175841_3_, 25, 16, 25, 32, 16, 32, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175841_3_, 25, 17, 25, 25, 19, 25, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, p_175841_3_, 32, 17, 25, 32, 19, 25, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, p_175841_3_, 25, 17, 32, 25, 19, 32, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, p_175841_3_, 32, 17, 32, 32, 19, 32, field_175826_b, field_175826_b, false);
            this.setBlockState(worldIn, field_175826_b, 26, 20, 26, p_175841_3_);
            this.setBlockState(worldIn, field_175826_b, 27, 21, 27, p_175841_3_);
            this.setBlockState(worldIn, field_175825_e, 27, 20, 27, p_175841_3_);
            this.setBlockState(worldIn, field_175826_b, 26, 20, 31, p_175841_3_);
            this.setBlockState(worldIn, field_175826_b, 27, 21, 30, p_175841_3_);
            this.setBlockState(worldIn, field_175825_e, 27, 20, 30, p_175841_3_);
            this.setBlockState(worldIn, field_175826_b, 31, 20, 31, p_175841_3_);
            this.setBlockState(worldIn, field_175826_b, 30, 21, 30, p_175841_3_);
            this.setBlockState(worldIn, field_175825_e, 30, 20, 30, p_175841_3_);
            this.setBlockState(worldIn, field_175826_b, 31, 20, 26, p_175841_3_);
            this.setBlockState(worldIn, field_175826_b, 30, 21, 27, p_175841_3_);
            this.setBlockState(worldIn, field_175825_e, 30, 20, 27, p_175841_3_);
            this.fillWithBlocks(worldIn, p_175841_3_, 28, 21, 27, 29, 21, 27, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175841_3_, 27, 21, 28, 27, 21, 29, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175841_3_, 28, 21, 30, 29, 21, 30, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175841_3_, 30, 21, 28, 30, 21, 29, field_175828_a, field_175828_a, false);
        }
    }

    private void func_175835_e(World worldIn, Random p_175835_2_, StructureBoundingBox p_175835_3_) {
        if (this.func_175818_a(p_175835_3_, 0, 21, 6, 58)) {
            this.fillWithBlocks(worldIn, p_175835_3_, 0, 0, 21, 6, 0, 57, field_175828_a, field_175828_a, false);
            this.func_181655_a(worldIn, p_175835_3_, 0, 1, 21, 6, 7, 57, false);
            this.fillWithBlocks(worldIn, p_175835_3_, 4, 4, 21, 6, 4, 53, field_175828_a, field_175828_a, false);
            for (int i = 0; i < 4; ++i) {
                this.fillWithBlocks(worldIn, p_175835_3_, i, i + 1, 21, i, i + 1, 57 - i, field_175826_b, field_175826_b, false);
            }
            for (int j = 23; j < 53; j += 3) {
                this.setBlockState(worldIn, field_175824_d, 5, 5, j, p_175835_3_);
            }
            this.setBlockState(worldIn, field_175824_d, 5, 5, 52, p_175835_3_);
            for (int k = 0; k < 4; ++k) {
                this.fillWithBlocks(worldIn, p_175835_3_, k, k + 1, 21, k, k + 1, 57 - k, field_175826_b, field_175826_b, false);
            }
            this.fillWithBlocks(worldIn, p_175835_3_, 4, 1, 52, 6, 3, 52, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175835_3_, 5, 1, 51, 5, 3, 53, field_175828_a, field_175828_a, false);
        }
        if (this.func_175818_a(p_175835_3_, 51, 21, 58, 58)) {
            this.fillWithBlocks(worldIn, p_175835_3_, 51, 0, 21, 57, 0, 57, field_175828_a, field_175828_a, false);
            this.func_181655_a(worldIn, p_175835_3_, 51, 1, 21, 57, 7, 57, false);
            this.fillWithBlocks(worldIn, p_175835_3_, 51, 4, 21, 53, 4, 53, field_175828_a, field_175828_a, false);
            for (int l = 0; l < 4; ++l) {
                this.fillWithBlocks(worldIn, p_175835_3_, 57 - l, l + 1, 21, 57 - l, l + 1, 57 - l, field_175826_b, field_175826_b, false);
            }
            for (int i1 = 23; i1 < 53; i1 += 3) {
                this.setBlockState(worldIn, field_175824_d, 52, 5, i1, p_175835_3_);
            }
            this.setBlockState(worldIn, field_175824_d, 52, 5, 52, p_175835_3_);
            this.fillWithBlocks(worldIn, p_175835_3_, 51, 1, 52, 53, 3, 52, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175835_3_, 52, 1, 51, 52, 3, 53, field_175828_a, field_175828_a, false);
        }
        if (this.func_175818_a(p_175835_3_, 0, 51, 57, 57)) {
            this.fillWithBlocks(worldIn, p_175835_3_, 7, 0, 51, 50, 0, 57, field_175828_a, field_175828_a, false);
            this.func_181655_a(worldIn, p_175835_3_, 7, 1, 51, 50, 10, 57, false);
            for (int j1 = 0; j1 < 4; ++j1) {
                this.fillWithBlocks(worldIn, p_175835_3_, j1 + 1, j1 + 1, 57 - j1, 56 - j1, j1 + 1, 57 - j1, field_175826_b, field_175826_b, false);
            }
        }
    }

    private void func_175842_f(World worldIn, Random p_175842_2_, StructureBoundingBox p_175842_3_) {
        if (this.func_175818_a(p_175842_3_, 7, 21, 13, 50)) {
            this.fillWithBlocks(worldIn, p_175842_3_, 7, 0, 21, 13, 0, 50, field_175828_a, field_175828_a, false);
            this.func_181655_a(worldIn, p_175842_3_, 7, 1, 21, 13, 10, 50, false);
            this.fillWithBlocks(worldIn, p_175842_3_, 11, 8, 21, 13, 8, 53, field_175828_a, field_175828_a, false);
            for (int i = 0; i < 4; ++i) {
                this.fillWithBlocks(worldIn, p_175842_3_, i + 7, i + 5, 21, i + 7, i + 5, 54, field_175826_b, field_175826_b, false);
            }
            for (int j = 21; j <= 45; j += 3) {
                this.setBlockState(worldIn, field_175824_d, 12, 9, j, p_175842_3_);
            }
        }
        if (this.func_175818_a(p_175842_3_, 44, 21, 50, 54)) {
            this.fillWithBlocks(worldIn, p_175842_3_, 44, 0, 21, 50, 0, 50, field_175828_a, field_175828_a, false);
            this.func_181655_a(worldIn, p_175842_3_, 44, 1, 21, 50, 10, 50, false);
            this.fillWithBlocks(worldIn, p_175842_3_, 44, 8, 21, 46, 8, 53, field_175828_a, field_175828_a, false);
            for (int k = 0; k < 4; ++k) {
                this.fillWithBlocks(worldIn, p_175842_3_, 50 - k, k + 5, 21, 50 - k, k + 5, 54, field_175826_b, field_175826_b, false);
            }
            for (int l = 21; l <= 45; l += 3) {
                this.setBlockState(worldIn, field_175824_d, 45, 9, l, p_175842_3_);
            }
        }
        if (this.func_175818_a(p_175842_3_, 8, 44, 49, 54)) {
            this.fillWithBlocks(worldIn, p_175842_3_, 14, 0, 44, 43, 0, 50, field_175828_a, field_175828_a, false);
            this.func_181655_a(worldIn, p_175842_3_, 14, 1, 44, 43, 10, 50, false);
            for (int i1 = 12; i1 <= 45; i1 += 3) {
                this.setBlockState(worldIn, field_175824_d, i1, 9, 45, p_175842_3_);
                this.setBlockState(worldIn, field_175824_d, i1, 9, 52, p_175842_3_);
                if (i1 != 12 && i1 != 18 && i1 != 24 && i1 != 33 && i1 != 39 && i1 != 45) continue;
                this.setBlockState(worldIn, field_175824_d, i1, 9, 47, p_175842_3_);
                this.setBlockState(worldIn, field_175824_d, i1, 9, 50, p_175842_3_);
                this.setBlockState(worldIn, field_175824_d, i1, 10, 45, p_175842_3_);
                this.setBlockState(worldIn, field_175824_d, i1, 10, 46, p_175842_3_);
                this.setBlockState(worldIn, field_175824_d, i1, 10, 51, p_175842_3_);
                this.setBlockState(worldIn, field_175824_d, i1, 10, 52, p_175842_3_);
                this.setBlockState(worldIn, field_175824_d, i1, 11, 47, p_175842_3_);
                this.setBlockState(worldIn, field_175824_d, i1, 11, 50, p_175842_3_);
                this.setBlockState(worldIn, field_175824_d, i1, 12, 48, p_175842_3_);
                this.setBlockState(worldIn, field_175824_d, i1, 12, 49, p_175842_3_);
            }
            for (int j1 = 0; j1 < 3; ++j1) {
                this.fillWithBlocks(worldIn, p_175842_3_, 8 + j1, 5 + j1, 54, 49 - j1, 5 + j1, 54, field_175828_a, field_175828_a, false);
            }
            this.fillWithBlocks(worldIn, p_175842_3_, 11, 8, 54, 46, 8, 54, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, p_175842_3_, 14, 8, 44, 43, 8, 53, field_175828_a, field_175828_a, false);
        }
    }

    private void func_175838_g(World worldIn, Random p_175838_2_, StructureBoundingBox p_175838_3_) {
        if (this.func_175818_a(p_175838_3_, 14, 21, 20, 43)) {
            this.fillWithBlocks(worldIn, p_175838_3_, 14, 0, 21, 20, 0, 43, field_175828_a, field_175828_a, false);
            this.func_181655_a(worldIn, p_175838_3_, 14, 1, 22, 20, 14, 43, false);
            this.fillWithBlocks(worldIn, p_175838_3_, 18, 12, 22, 20, 12, 39, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175838_3_, 18, 12, 21, 20, 12, 21, field_175826_b, field_175826_b, false);
            for (int i = 0; i < 4; ++i) {
                this.fillWithBlocks(worldIn, p_175838_3_, i + 14, i + 9, 21, i + 14, i + 9, 43 - i, field_175826_b, field_175826_b, false);
            }
            for (int j = 23; j <= 39; j += 3) {
                this.setBlockState(worldIn, field_175824_d, 19, 13, j, p_175838_3_);
            }
        }
        if (this.func_175818_a(p_175838_3_, 37, 21, 43, 43)) {
            this.fillWithBlocks(worldIn, p_175838_3_, 37, 0, 21, 43, 0, 43, field_175828_a, field_175828_a, false);
            this.func_181655_a(worldIn, p_175838_3_, 37, 1, 22, 43, 14, 43, false);
            this.fillWithBlocks(worldIn, p_175838_3_, 37, 12, 22, 39, 12, 39, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, p_175838_3_, 37, 12, 21, 39, 12, 21, field_175826_b, field_175826_b, false);
            for (int k = 0; k < 4; ++k) {
                this.fillWithBlocks(worldIn, p_175838_3_, 43 - k, k + 9, 21, 43 - k, k + 9, 43 - k, field_175826_b, field_175826_b, false);
            }
            for (int l = 23; l <= 39; l += 3) {
                this.setBlockState(worldIn, field_175824_d, 38, 13, l, p_175838_3_);
            }
        }
        if (this.func_175818_a(p_175838_3_, 15, 37, 42, 43)) {
            this.fillWithBlocks(worldIn, p_175838_3_, 21, 0, 37, 36, 0, 43, field_175828_a, field_175828_a, false);
            this.func_181655_a(worldIn, p_175838_3_, 21, 1, 37, 36, 14, 43, false);
            this.fillWithBlocks(worldIn, p_175838_3_, 21, 12, 37, 36, 12, 39, field_175828_a, field_175828_a, false);
            for (int i1 = 0; i1 < 4; ++i1) {
                this.fillWithBlocks(worldIn, p_175838_3_, 15 + i1, i1 + 9, 43 - i1, 42 - i1, i1 + 9, 43 - i1, field_175826_b, field_175826_b, false);
            }
            for (int j1 = 21; j1 <= 36; j1 += 3) {
                this.setBlockState(worldIn, field_175824_d, j1, 13, 38, p_175838_3_);
            }
        }
    }
}
