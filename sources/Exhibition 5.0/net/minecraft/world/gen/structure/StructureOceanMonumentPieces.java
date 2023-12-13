// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.structure;

import net.minecraft.block.BlockPrismarine;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.Vec3i;
import net.minecraft.util.BlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Blocks;
import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.util.EnumFacing;

public class StructureOceanMonumentPieces
{
    private static final String __OBFID = "CL_00001994";
    
    public static void func_175970_a() {
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
    
    public static class DoubleXRoom extends Piece
    {
        private static final String __OBFID = "CL_00001983";
        
        public DoubleXRoom() {
        }
        
        public DoubleXRoom(final EnumFacing p_i45597_1_, final RoomDefinition p_i45597_2_, final Random p_i45597_3_) {
            super(1, p_i45597_1_, p_i45597_2_, 2, 1, 1);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            final RoomDefinition var4 = this.field_175830_k.field_175965_b[EnumFacing.EAST.getIndex()];
            final RoomDefinition var5 = this.field_175830_k;
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(worldIn, p_74875_3_, 8, 0, var4.field_175966_c[EnumFacing.DOWN.getIndex()]);
                this.func_175821_a(worldIn, p_74875_3_, 0, 0, var5.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }
            if (var5.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(worldIn, p_74875_3_, 1, 4, 1, 7, 4, 6, DoubleXRoom.field_175828_a);
            }
            if (var4.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(worldIn, p_74875_3_, 8, 4, 1, 14, 4, 6, DoubleXRoom.field_175828_a);
            }
            this.func_175804_a(worldIn, p_74875_3_, 0, 3, 0, 0, 3, 7, DoubleXRoom.field_175826_b, DoubleXRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 15, 3, 0, 15, 3, 7, DoubleXRoom.field_175826_b, DoubleXRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 0, 15, 3, 0, DoubleXRoom.field_175826_b, DoubleXRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 7, 14, 3, 7, DoubleXRoom.field_175826_b, DoubleXRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 2, 0, 0, 2, 7, DoubleXRoom.field_175828_a, DoubleXRoom.field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 15, 2, 0, 15, 2, 7, DoubleXRoom.field_175828_a, DoubleXRoom.field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 0, 15, 2, 0, DoubleXRoom.field_175828_a, DoubleXRoom.field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 7, 14, 2, 7, DoubleXRoom.field_175828_a, DoubleXRoom.field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 0, 1, 7, DoubleXRoom.field_175826_b, DoubleXRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 15, 1, 0, 15, 1, 7, DoubleXRoom.field_175826_b, DoubleXRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 0, 15, 1, 0, DoubleXRoom.field_175826_b, DoubleXRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 7, 14, 1, 7, DoubleXRoom.field_175826_b, DoubleXRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 1, 0, 10, 1, 4, DoubleXRoom.field_175826_b, DoubleXRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 2, 0, 9, 2, 3, DoubleXRoom.field_175828_a, DoubleXRoom.field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 3, 0, 10, 3, 4, DoubleXRoom.field_175826_b, DoubleXRoom.field_175826_b, false);
            this.func_175811_a(worldIn, DoubleXRoom.field_175825_e, 6, 2, 3, p_74875_3_);
            this.func_175811_a(worldIn, DoubleXRoom.field_175825_e, 9, 2, 3, p_74875_3_);
            if (var5.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, DoubleXRoom.field_175822_f, DoubleXRoom.field_175822_f, false);
            }
            if (var5.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 7, 4, 2, 7, DoubleXRoom.field_175822_f, DoubleXRoom.field_175822_f, false);
            }
            if (var5.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 0, 2, 4, DoubleXRoom.field_175822_f, DoubleXRoom.field_175822_f, false);
            }
            if (var4.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 11, 1, 0, 12, 2, 0, DoubleXRoom.field_175822_f, DoubleXRoom.field_175822_f, false);
            }
            if (var4.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 11, 1, 7, 12, 2, 7, DoubleXRoom.field_175822_f, DoubleXRoom.field_175822_f, false);
            }
            if (var4.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 15, 1, 3, 15, 2, 4, DoubleXRoom.field_175822_f, DoubleXRoom.field_175822_f, false);
            }
            return true;
        }
    }
    
    public static class DoubleXYRoom extends Piece
    {
        private static final String __OBFID = "CL_00001982";
        
        public DoubleXYRoom() {
        }
        
        public DoubleXYRoom(final EnumFacing p_i45596_1_, final RoomDefinition p_i45596_2_, final Random p_i45596_3_) {
            super(1, p_i45596_1_, p_i45596_2_, 2, 2, 1);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            final RoomDefinition var4 = this.field_175830_k.field_175965_b[EnumFacing.EAST.getIndex()];
            final RoomDefinition var5 = this.field_175830_k;
            final RoomDefinition var6 = var5.field_175965_b[EnumFacing.UP.getIndex()];
            final RoomDefinition var7 = var4.field_175965_b[EnumFacing.UP.getIndex()];
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(worldIn, p_74875_3_, 8, 0, var4.field_175966_c[EnumFacing.DOWN.getIndex()]);
                this.func_175821_a(worldIn, p_74875_3_, 0, 0, var5.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }
            if (var6.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(worldIn, p_74875_3_, 1, 8, 1, 7, 8, 6, DoubleXYRoom.field_175828_a);
            }
            if (var7.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(worldIn, p_74875_3_, 8, 8, 1, 14, 8, 6, DoubleXYRoom.field_175828_a);
            }
            for (int var8 = 1; var8 <= 7; ++var8) {
                IBlockState var9 = DoubleXYRoom.field_175826_b;
                if (var8 == 2 || var8 == 6) {
                    var9 = DoubleXYRoom.field_175828_a;
                }
                this.func_175804_a(worldIn, p_74875_3_, 0, var8, 0, 0, var8, 7, var9, var9, false);
                this.func_175804_a(worldIn, p_74875_3_, 15, var8, 0, 15, var8, 7, var9, var9, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, var8, 0, 15, var8, 0, var9, var9, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, var8, 7, 14, var8, 7, var9, var9, false);
            }
            this.func_175804_a(worldIn, p_74875_3_, 2, 1, 3, 2, 7, 4, DoubleXYRoom.field_175826_b, DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 2, 4, 7, 2, DoubleXYRoom.field_175826_b, DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 5, 4, 7, 5, DoubleXYRoom.field_175826_b, DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 13, 1, 3, 13, 7, 4, DoubleXYRoom.field_175826_b, DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 11, 1, 2, 12, 7, 2, DoubleXYRoom.field_175826_b, DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 11, 1, 5, 12, 7, 5, DoubleXYRoom.field_175826_b, DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 1, 3, 5, 3, 4, DoubleXYRoom.field_175826_b, DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 1, 3, 10, 3, 4, DoubleXYRoom.field_175826_b, DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 7, 2, 10, 7, 5, DoubleXYRoom.field_175826_b, DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 5, 2, 5, 7, 2, DoubleXYRoom.field_175826_b, DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 5, 2, 10, 7, 2, DoubleXYRoom.field_175826_b, DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 5, 5, 5, 7, 5, DoubleXYRoom.field_175826_b, DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 5, 5, 10, 7, 5, DoubleXYRoom.field_175826_b, DoubleXYRoom.field_175826_b, false);
            this.func_175811_a(worldIn, DoubleXYRoom.field_175826_b, 6, 6, 2, p_74875_3_);
            this.func_175811_a(worldIn, DoubleXYRoom.field_175826_b, 9, 6, 2, p_74875_3_);
            this.func_175811_a(worldIn, DoubleXYRoom.field_175826_b, 6, 6, 5, p_74875_3_);
            this.func_175811_a(worldIn, DoubleXYRoom.field_175826_b, 9, 6, 5, p_74875_3_);
            this.func_175804_a(worldIn, p_74875_3_, 5, 4, 3, 6, 4, 4, DoubleXYRoom.field_175826_b, DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 9, 4, 3, 10, 4, 4, DoubleXYRoom.field_175826_b, DoubleXYRoom.field_175826_b, false);
            this.func_175811_a(worldIn, DoubleXYRoom.field_175825_e, 5, 4, 2, p_74875_3_);
            this.func_175811_a(worldIn, DoubleXYRoom.field_175825_e, 5, 4, 5, p_74875_3_);
            this.func_175811_a(worldIn, DoubleXYRoom.field_175825_e, 10, 4, 2, p_74875_3_);
            this.func_175811_a(worldIn, DoubleXYRoom.field_175825_e, 10, 4, 5, p_74875_3_);
            if (var5.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, DoubleXYRoom.field_175822_f, DoubleXYRoom.field_175822_f, false);
            }
            if (var5.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 7, 4, 2, 7, DoubleXYRoom.field_175822_f, DoubleXYRoom.field_175822_f, false);
            }
            if (var5.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 0, 2, 4, DoubleXYRoom.field_175822_f, DoubleXYRoom.field_175822_f, false);
            }
            if (var4.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 11, 1, 0, 12, 2, 0, DoubleXYRoom.field_175822_f, DoubleXYRoom.field_175822_f, false);
            }
            if (var4.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 11, 1, 7, 12, 2, 7, DoubleXYRoom.field_175822_f, DoubleXYRoom.field_175822_f, false);
            }
            if (var4.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 15, 1, 3, 15, 2, 4, DoubleXYRoom.field_175822_f, DoubleXYRoom.field_175822_f, false);
            }
            if (var6.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 3, 5, 0, 4, 6, 0, DoubleXYRoom.field_175822_f, DoubleXYRoom.field_175822_f, false);
            }
            if (var6.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 3, 5, 7, 4, 6, 7, DoubleXYRoom.field_175822_f, DoubleXYRoom.field_175822_f, false);
            }
            if (var6.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 0, 5, 3, 0, 6, 4, DoubleXYRoom.field_175822_f, DoubleXYRoom.field_175822_f, false);
            }
            if (var7.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 11, 5, 0, 12, 6, 0, DoubleXYRoom.field_175822_f, DoubleXYRoom.field_175822_f, false);
            }
            if (var7.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 11, 5, 7, 12, 6, 7, DoubleXYRoom.field_175822_f, DoubleXYRoom.field_175822_f, false);
            }
            if (var7.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 15, 5, 3, 15, 6, 4, DoubleXYRoom.field_175822_f, DoubleXYRoom.field_175822_f, false);
            }
            return true;
        }
    }
    
    public static class DoubleYRoom extends Piece
    {
        private static final String __OBFID = "CL_00001981";
        
        public DoubleYRoom() {
        }
        
        public DoubleYRoom(final EnumFacing p_i45595_1_, final RoomDefinition p_i45595_2_, final Random p_i45595_3_) {
            super(1, p_i45595_1_, p_i45595_2_, 1, 2, 1);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(worldIn, p_74875_3_, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }
            final RoomDefinition var4 = this.field_175830_k.field_175965_b[EnumFacing.UP.getIndex()];
            if (var4.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(worldIn, p_74875_3_, 1, 8, 1, 6, 8, 6, DoubleYRoom.field_175828_a);
            }
            this.func_175804_a(worldIn, p_74875_3_, 0, 4, 0, 0, 4, 7, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 4, 0, 7, 4, 7, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 4, 0, 6, 4, 0, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 4, 7, 6, 4, 7, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 4, 1, 2, 4, 2, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 4, 2, 1, 4, 2, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 4, 1, 5, 4, 2, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 4, 2, 6, 4, 2, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 4, 5, 2, 4, 6, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 4, 5, 1, 4, 5, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 4, 5, 5, 4, 6, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 4, 5, 6, 4, 5, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
            RoomDefinition var5 = this.field_175830_k;
            for (int var6 = 1; var6 <= 5; var6 += 4) {
                byte var7 = 0;
                if (var5.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, 2, var6, var7, 2, var6 + 2, var7, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 5, var6, var7, 5, var6 + 2, var7, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 3, var6 + 2, var7, 4, var6 + 2, var7, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                }
                else {
                    this.func_175804_a(worldIn, p_74875_3_, 0, var6, var7, 7, var6 + 2, var7, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, var6 + 1, var7, 7, var6 + 1, var7, DoubleYRoom.field_175828_a, DoubleYRoom.field_175828_a, false);
                }
                var7 = 7;
                if (var5.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, 2, var6, var7, 2, var6 + 2, var7, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 5, var6, var7, 5, var6 + 2, var7, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 3, var6 + 2, var7, 4, var6 + 2, var7, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                }
                else {
                    this.func_175804_a(worldIn, p_74875_3_, 0, var6, var7, 7, var6 + 2, var7, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, var6 + 1, var7, 7, var6 + 1, var7, DoubleYRoom.field_175828_a, DoubleYRoom.field_175828_a, false);
                }
                byte var8 = 0;
                if (var5.field_175966_c[EnumFacing.WEST.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6, 2, var8, var6 + 2, 2, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6, 5, var8, var6 + 2, 5, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6 + 2, 3, var8, var6 + 2, 4, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                }
                else {
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6, 0, var8, var6 + 2, 7, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6 + 1, 0, var8, var6 + 1, 7, DoubleYRoom.field_175828_a, DoubleYRoom.field_175828_a, false);
                }
                var8 = 7;
                if (var5.field_175966_c[EnumFacing.EAST.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6, 2, var8, var6 + 2, 2, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6, 5, var8, var6 + 2, 5, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6 + 2, 3, var8, var6 + 2, 4, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                }
                else {
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6, 0, var8, var6 + 2, 7, DoubleYRoom.field_175826_b, DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6 + 1, 0, var8, var6 + 1, 7, DoubleYRoom.field_175828_a, DoubleYRoom.field_175828_a, false);
                }
                var5 = var4;
            }
            return true;
        }
    }
    
    public static class DoubleYZRoom extends Piece
    {
        private static final String __OBFID = "CL_00001980";
        
        public DoubleYZRoom() {
        }
        
        public DoubleYZRoom(final EnumFacing p_i45594_1_, final RoomDefinition p_i45594_2_, final Random p_i45594_3_) {
            super(1, p_i45594_1_, p_i45594_2_, 1, 2, 2);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            final RoomDefinition var4 = this.field_175830_k.field_175965_b[EnumFacing.NORTH.getIndex()];
            final RoomDefinition var5 = this.field_175830_k;
            final RoomDefinition var6 = var4.field_175965_b[EnumFacing.UP.getIndex()];
            final RoomDefinition var7 = var5.field_175965_b[EnumFacing.UP.getIndex()];
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(worldIn, p_74875_3_, 0, 8, var4.field_175966_c[EnumFacing.DOWN.getIndex()]);
                this.func_175821_a(worldIn, p_74875_3_, 0, 0, var5.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }
            if (var7.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(worldIn, p_74875_3_, 1, 8, 1, 6, 8, 7, DoubleYZRoom.field_175828_a);
            }
            if (var6.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(worldIn, p_74875_3_, 1, 8, 8, 6, 8, 14, DoubleYZRoom.field_175828_a);
            }
            for (int var8 = 1; var8 <= 7; ++var8) {
                IBlockState var9 = DoubleYZRoom.field_175826_b;
                if (var8 == 2 || var8 == 6) {
                    var9 = DoubleYZRoom.field_175828_a;
                }
                this.func_175804_a(worldIn, p_74875_3_, 0, var8, 0, 0, var8, 15, var9, var9, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, var8, 0, 7, var8, 15, var9, var9, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, var8, 0, 6, var8, 0, var9, var9, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, var8, 15, 6, var8, 15, var9, var9, false);
            }
            for (int var8 = 1; var8 <= 7; ++var8) {
                IBlockState var9 = DoubleYZRoom.field_175827_c;
                if (var8 == 2 || var8 == 6) {
                    var9 = DoubleYZRoom.field_175825_e;
                }
                this.func_175804_a(worldIn, p_74875_3_, 3, var8, 7, 4, var8, 8, var9, var9, false);
            }
            if (var5.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, DoubleYZRoom.field_175822_f, DoubleYZRoom.field_175822_f, false);
            }
            if (var5.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 3, 7, 2, 4, DoubleYZRoom.field_175822_f, DoubleYZRoom.field_175822_f, false);
            }
            if (var5.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 0, 2, 4, DoubleYZRoom.field_175822_f, DoubleYZRoom.field_175822_f, false);
            }
            if (var4.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 15, 4, 2, 15, DoubleYZRoom.field_175822_f, DoubleYZRoom.field_175822_f, false);
            }
            if (var4.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 11, 0, 2, 12, DoubleYZRoom.field_175822_f, DoubleYZRoom.field_175822_f, false);
            }
            if (var4.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 11, 7, 2, 12, DoubleYZRoom.field_175822_f, DoubleYZRoom.field_175822_f, false);
            }
            if (var7.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 3, 5, 0, 4, 6, 0, DoubleYZRoom.field_175822_f, DoubleYZRoom.field_175822_f, false);
            }
            if (var7.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 7, 5, 3, 7, 6, 4, DoubleYZRoom.field_175822_f, DoubleYZRoom.field_175822_f, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 4, 2, 6, 4, 5, DoubleYZRoom.field_175826_b, DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, 1, 2, 6, 3, 2, DoubleYZRoom.field_175826_b, DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, 1, 5, 6, 3, 5, DoubleYZRoom.field_175826_b, DoubleYZRoom.field_175826_b, false);
            }
            if (var7.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 0, 5, 3, 0, 6, 4, DoubleYZRoom.field_175822_f, DoubleYZRoom.field_175822_f, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 4, 2, 2, 4, 5, DoubleYZRoom.field_175826_b, DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 1, 2, 1, 3, 2, DoubleYZRoom.field_175826_b, DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 1, 5, 1, 3, 5, DoubleYZRoom.field_175826_b, DoubleYZRoom.field_175826_b, false);
            }
            if (var6.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 3, 5, 15, 4, 6, 15, DoubleYZRoom.field_175822_f, DoubleYZRoom.field_175822_f, false);
            }
            if (var6.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 0, 5, 11, 0, 6, 12, DoubleYZRoom.field_175822_f, DoubleYZRoom.field_175822_f, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 4, 10, 2, 4, 13, DoubleYZRoom.field_175826_b, DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 1, 10, 1, 3, 10, DoubleYZRoom.field_175826_b, DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 1, 13, 1, 3, 13, DoubleYZRoom.field_175826_b, DoubleYZRoom.field_175826_b, false);
            }
            if (var6.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 7, 5, 11, 7, 6, 12, DoubleYZRoom.field_175822_f, DoubleYZRoom.field_175822_f, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 4, 10, 6, 4, 13, DoubleYZRoom.field_175826_b, DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, 1, 10, 6, 3, 10, DoubleYZRoom.field_175826_b, DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, 1, 13, 6, 3, 13, DoubleYZRoom.field_175826_b, DoubleYZRoom.field_175826_b, false);
            }
            return true;
        }
    }
    
    public static class DoubleZRoom extends Piece
    {
        private static final String __OBFID = "CL_00001979";
        
        public DoubleZRoom() {
        }
        
        public DoubleZRoom(final EnumFacing p_i45593_1_, final RoomDefinition p_i45593_2_, final Random p_i45593_3_) {
            super(1, p_i45593_1_, p_i45593_2_, 1, 1, 2);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            final RoomDefinition var4 = this.field_175830_k.field_175965_b[EnumFacing.NORTH.getIndex()];
            final RoomDefinition var5 = this.field_175830_k;
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(worldIn, p_74875_3_, 0, 8, var4.field_175966_c[EnumFacing.DOWN.getIndex()]);
                this.func_175821_a(worldIn, p_74875_3_, 0, 0, var5.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }
            if (var5.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(worldIn, p_74875_3_, 1, 4, 1, 6, 4, 7, DoubleZRoom.field_175828_a);
            }
            if (var4.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(worldIn, p_74875_3_, 1, 4, 8, 6, 4, 14, DoubleZRoom.field_175828_a);
            }
            this.func_175804_a(worldIn, p_74875_3_, 0, 3, 0, 0, 3, 15, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 3, 0, 7, 3, 15, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 0, 7, 3, 0, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 15, 6, 3, 15, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 2, 0, 0, 2, 15, DoubleZRoom.field_175828_a, DoubleZRoom.field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 2, 0, 7, 2, 15, DoubleZRoom.field_175828_a, DoubleZRoom.field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 0, 7, 2, 0, DoubleZRoom.field_175828_a, DoubleZRoom.field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 15, 6, 2, 15, DoubleZRoom.field_175828_a, DoubleZRoom.field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 0, 1, 15, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 1, 0, 7, 1, 15, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 0, 7, 1, 0, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 15, 6, 1, 15, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 1, 1, 1, 2, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 1, 1, 6, 1, 2, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 1, 1, 3, 2, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 3, 1, 6, 3, 2, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 13, 1, 1, 14, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 1, 13, 6, 1, 14, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 13, 1, 3, 14, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 3, 13, 6, 3, 14, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 1, 6, 2, 3, 6, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 1, 6, 5, 3, 6, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 1, 9, 2, 3, 9, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 1, 9, 5, 3, 9, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 2, 6, 4, 2, 6, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 2, 9, 4, 2, 9, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 2, 7, 2, 2, 8, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 2, 7, 5, 2, 8, DoubleZRoom.field_175826_b, DoubleZRoom.field_175826_b, false);
            this.func_175811_a(worldIn, DoubleZRoom.field_175825_e, 2, 2, 5, p_74875_3_);
            this.func_175811_a(worldIn, DoubleZRoom.field_175825_e, 5, 2, 5, p_74875_3_);
            this.func_175811_a(worldIn, DoubleZRoom.field_175825_e, 2, 2, 10, p_74875_3_);
            this.func_175811_a(worldIn, DoubleZRoom.field_175825_e, 5, 2, 10, p_74875_3_);
            this.func_175811_a(worldIn, DoubleZRoom.field_175826_b, 2, 3, 5, p_74875_3_);
            this.func_175811_a(worldIn, DoubleZRoom.field_175826_b, 5, 3, 5, p_74875_3_);
            this.func_175811_a(worldIn, DoubleZRoom.field_175826_b, 2, 3, 10, p_74875_3_);
            this.func_175811_a(worldIn, DoubleZRoom.field_175826_b, 5, 3, 10, p_74875_3_);
            if (var5.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, DoubleZRoom.field_175822_f, DoubleZRoom.field_175822_f, false);
            }
            if (var5.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 3, 7, 2, 4, DoubleZRoom.field_175822_f, DoubleZRoom.field_175822_f, false);
            }
            if (var5.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 0, 2, 4, DoubleZRoom.field_175822_f, DoubleZRoom.field_175822_f, false);
            }
            if (var4.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 15, 4, 2, 15, DoubleZRoom.field_175822_f, DoubleZRoom.field_175822_f, false);
            }
            if (var4.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 11, 0, 2, 12, DoubleZRoom.field_175822_f, DoubleZRoom.field_175822_f, false);
            }
            if (var4.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 11, 7, 2, 12, DoubleZRoom.field_175822_f, DoubleZRoom.field_175822_f, false);
            }
            return true;
        }
    }
    
    public static class EntryRoom extends Piece
    {
        private static final String __OBFID = "CL_00001978";
        
        public EntryRoom() {
        }
        
        public EntryRoom(final EnumFacing p_i45592_1_, final RoomDefinition p_i45592_2_) {
            super(1, p_i45592_1_, p_i45592_2_, 1, 1, 1);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            this.func_175804_a(worldIn, p_74875_3_, 0, 3, 0, 2, 3, 7, EntryRoom.field_175826_b, EntryRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 3, 0, 7, 3, 7, EntryRoom.field_175826_b, EntryRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 2, 0, 1, 2, 7, EntryRoom.field_175826_b, EntryRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 2, 0, 7, 2, 7, EntryRoom.field_175826_b, EntryRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 0, 1, 7, EntryRoom.field_175826_b, EntryRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 1, 0, 7, 1, 7, EntryRoom.field_175826_b, EntryRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 7, 7, 3, 7, EntryRoom.field_175826_b, EntryRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 0, 2, 3, 0, EntryRoom.field_175826_b, EntryRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 1, 0, 6, 3, 0, EntryRoom.field_175826_b, EntryRoom.field_175826_b, false);
            if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 7, 4, 2, 7, EntryRoom.field_175822_f, EntryRoom.field_175822_f, false);
            }
            if (this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 1, 2, 4, EntryRoom.field_175822_f, EntryRoom.field_175822_f, false);
            }
            if (this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 6, 1, 3, 7, 2, 4, EntryRoom.field_175822_f, EntryRoom.field_175822_f, false);
            }
            return true;
        }
    }
    
    static class FitSimpleRoomHelper implements MonumentRoomFitHelper
    {
        private static final String __OBFID = "CL_00001987";
        
        private FitSimpleRoomHelper() {
        }
        
        @Override
        public boolean func_175969_a(final RoomDefinition p_175969_1_) {
            return true;
        }
        
        @Override
        public Piece func_175968_a(final EnumFacing p_175968_1_, final RoomDefinition p_175968_2_, final Random p_175968_3_) {
            p_175968_2_.field_175963_d = true;
            return new SimpleRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }
        
        FitSimpleRoomHelper(final SwitchEnumFacing p_i45601_1_) {
            this();
        }
    }
    
    static class FitSimpleRoomTopHelper implements MonumentRoomFitHelper
    {
        private static final String __OBFID = "CL_00001986";
        
        private FitSimpleRoomTopHelper() {
        }
        
        @Override
        public boolean func_175969_a(final RoomDefinition p_175969_1_) {
            return !p_175969_1_.field_175966_c[EnumFacing.WEST.getIndex()] && !p_175969_1_.field_175966_c[EnumFacing.EAST.getIndex()] && !p_175969_1_.field_175966_c[EnumFacing.NORTH.getIndex()] && !p_175969_1_.field_175966_c[EnumFacing.SOUTH.getIndex()] && !p_175969_1_.field_175966_c[EnumFacing.UP.getIndex()];
        }
        
        @Override
        public Piece func_175968_a(final EnumFacing p_175968_1_, final RoomDefinition p_175968_2_, final Random p_175968_3_) {
            p_175968_2_.field_175963_d = true;
            return new SimpleTopRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }
        
        FitSimpleRoomTopHelper(final SwitchEnumFacing p_i45600_1_) {
            this();
        }
    }
    
    public static class MonumentBuilding extends Piece
    {
        private RoomDefinition field_175845_o;
        private RoomDefinition field_175844_p;
        private List field_175843_q;
        private static final String __OBFID = "CL_00001985";
        
        public MonumentBuilding() {
            this.field_175843_q = Lists.newArrayList();
        }
        
        public MonumentBuilding(final Random p_i45599_1_, final int p_i45599_2_, final int p_i45599_3_, final EnumFacing p_i45599_4_) {
            super(0);
            this.field_175843_q = Lists.newArrayList();
            this.coordBaseMode = p_i45599_4_;
            switch (StructureOceanMonumentPieces.SwitchEnumFacing.field_175971_a[this.coordBaseMode.ordinal()]) {
                case 1:
                case 2: {
                    this.boundingBox = new StructureBoundingBox(p_i45599_2_, 39, p_i45599_3_, p_i45599_2_ + 58 - 1, 61, p_i45599_3_ + 58 - 1);
                    break;
                }
                default: {
                    this.boundingBox = new StructureBoundingBox(p_i45599_2_, 39, p_i45599_3_, p_i45599_2_ + 58 - 1, 61, p_i45599_3_ + 58 - 1);
                    break;
                }
            }
            final List var5 = this.func_175836_a(p_i45599_1_);
            this.field_175845_o.field_175963_d = true;
            this.field_175843_q.add(new EntryRoom(this.coordBaseMode, this.field_175845_o));
            this.field_175843_q.add(new MonumentCoreRoom(this.coordBaseMode, this.field_175844_p, p_i45599_1_));
            final ArrayList var6 = Lists.newArrayList();
            var6.add(new XYDoubleRoomFitHelper(null));
            var6.add(new YZDoubleRoomFitHelper(null));
            var6.add(new ZDoubleRoomFitHelper(null));
            var6.add(new XDoubleRoomFitHelper(null));
            var6.add(new YDoubleRoomFitHelper(null));
            var6.add(new FitSimpleRoomTopHelper(null));
            var6.add(new FitSimpleRoomHelper(null));
            for (final RoomDefinition var8 : var5) {
                if (!var8.field_175963_d && !var8.func_175961_b()) {
                    for (final MonumentRoomFitHelper var10 : var6) {
                        if (var10.func_175969_a(var8)) {
                            this.field_175843_q.add(var10.func_175968_a(this.coordBaseMode, var8, p_i45599_1_));
                            break;
                        }
                    }
                }
            }
            final int var11 = this.boundingBox.minY;
            final int var12 = this.getXWithOffset(9, 22);
            final int var13 = this.getZWithOffset(9, 22);
            for (final Piece var15 : this.field_175843_q) {
                var15.getBoundingBox().offset(var12, var11, var13);
            }
            final StructureBoundingBox var16 = StructureBoundingBox.func_175899_a(this.getXWithOffset(1, 1), this.getYWithOffset(1), this.getZWithOffset(1, 1), this.getXWithOffset(23, 21), this.getYWithOffset(8), this.getZWithOffset(23, 21));
            final StructureBoundingBox var17 = StructureBoundingBox.func_175899_a(this.getXWithOffset(34, 1), this.getYWithOffset(1), this.getZWithOffset(34, 1), this.getXWithOffset(56, 21), this.getYWithOffset(8), this.getZWithOffset(56, 21));
            final StructureBoundingBox var18 = StructureBoundingBox.func_175899_a(this.getXWithOffset(22, 22), this.getYWithOffset(13), this.getZWithOffset(22, 22), this.getXWithOffset(35, 35), this.getYWithOffset(17), this.getZWithOffset(35, 35));
            int var19 = p_i45599_1_.nextInt();
            this.field_175843_q.add(new WingRoom(this.coordBaseMode, var16, var19++));
            this.field_175843_q.add(new WingRoom(this.coordBaseMode, var17, var19++));
            this.field_175843_q.add(new Penthouse(this.coordBaseMode, var18));
        }
        
        private List func_175836_a(final Random p_175836_1_) {
            final RoomDefinition[] var2 = new RoomDefinition[75];
            for (int var3 = 0; var3 < 5; ++var3) {
                for (int var4 = 0; var4 < 4; ++var4) {
                    final byte var5 = 0;
                    final int var6 = Piece.func_175820_a(var3, var5, var4);
                    var2[var6] = new RoomDefinition(var6);
                }
            }
            for (int var3 = 0; var3 < 5; ++var3) {
                for (int var4 = 0; var4 < 4; ++var4) {
                    final byte var5 = 1;
                    final int var6 = Piece.func_175820_a(var3, var5, var4);
                    var2[var6] = new RoomDefinition(var6);
                }
            }
            for (int var3 = 1; var3 < 4; ++var3) {
                for (int var4 = 0; var4 < 2; ++var4) {
                    final byte var5 = 2;
                    final int var6 = Piece.func_175820_a(var3, var5, var4);
                    var2[var6] = new RoomDefinition(var6);
                }
            }
            this.field_175845_o = var2[MonumentBuilding.field_175823_g];
            for (int var3 = 0; var3 < 5; ++var3) {
                for (int var4 = 0; var4 < 5; ++var4) {
                    for (int var7 = 0; var7 < 3; ++var7) {
                        final int var6 = Piece.func_175820_a(var3, var7, var4);
                        if (var2[var6] != null) {
                            for (final EnumFacing var11 : EnumFacing.values()) {
                                final int var12 = var3 + var11.getFrontOffsetX();
                                final int var13 = var7 + var11.getFrontOffsetY();
                                final int var14 = var4 + var11.getFrontOffsetZ();
                                if (var12 >= 0 && var12 < 5 && var14 >= 0 && var14 < 5 && var13 >= 0 && var13 < 3) {
                                    final int var15 = Piece.func_175820_a(var12, var13, var14);
                                    if (var2[var15] != null) {
                                        if (var14 != var4) {
                                            var2[var6].func_175957_a(var11.getOpposite(), var2[var15]);
                                        }
                                        else {
                                            var2[var6].func_175957_a(var11, var2[var15]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            final RoomDefinition var16;
            var2[MonumentBuilding.field_175831_h].func_175957_a(EnumFacing.UP, var16 = new RoomDefinition(1003));
            final RoomDefinition var17;
            var2[MonumentBuilding.field_175832_i].func_175957_a(EnumFacing.SOUTH, var17 = new RoomDefinition(1001));
            final RoomDefinition var18;
            var2[MonumentBuilding.field_175829_j].func_175957_a(EnumFacing.SOUTH, var18 = new RoomDefinition(1002));
            var16.field_175963_d = true;
            var17.field_175963_d = true;
            var18.field_175963_d = true;
            this.field_175845_o.field_175964_e = true;
            this.field_175844_p = var2[Piece.func_175820_a(p_175836_1_.nextInt(4), 0, 2)];
            this.field_175844_p.field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.NORTH.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.NORTH.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            final ArrayList var19 = Lists.newArrayList();
            final RoomDefinition[] var20 = var2;
            for (int var9 = var2.length, var10 = 0; var10 < var9; ++var10) {
                final RoomDefinition var21 = var20[var10];
                if (var21 != null) {
                    var21.func_175958_a();
                    var19.add(var21);
                }
            }
            var16.func_175958_a();
            Collections.shuffle(var19, p_175836_1_);
            int var22 = 1;
            for (final RoomDefinition var24 : var19) {
                int var25 = 0;
                int var12 = 0;
                while (var25 < 2 && var12 < 5) {
                    ++var12;
                    final int var13 = p_175836_1_.nextInt(6);
                    if (var24.field_175966_c[var13]) {
                        final int var14 = EnumFacing.getFront(var13).getOpposite().getIndex();
                        var24.field_175966_c[var13] = false;
                        var24.field_175965_b[var13].field_175966_c[var14] = false;
                        if (var24.func_175959_a(var22++) && var24.field_175965_b[var13].func_175959_a(var22++)) {
                            ++var25;
                        }
                        else {
                            var24.field_175966_c[var13] = true;
                            var24.field_175965_b[var13].field_175966_c[var14] = true;
                        }
                    }
                }
            }
            var19.add(var16);
            var19.add(var17);
            var19.add(var18);
            return var19;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            this.func_175840_a(false, 0, worldIn, p_74875_2_, p_74875_3_);
            this.func_175840_a(true, 33, worldIn, p_74875_2_, p_74875_3_);
            this.func_175839_b(worldIn, p_74875_2_, p_74875_3_);
            this.func_175837_c(worldIn, p_74875_2_, p_74875_3_);
            this.func_175841_d(worldIn, p_74875_2_, p_74875_3_);
            this.func_175835_e(worldIn, p_74875_2_, p_74875_3_);
            this.func_175842_f(worldIn, p_74875_2_, p_74875_3_);
            this.func_175838_g(worldIn, p_74875_2_, p_74875_3_);
            for (int var4 = 0; var4 < 7; ++var4) {
                int var5 = 0;
                while (var5 < 7) {
                    if (var5 == 0 && var4 == 3) {
                        var5 = 6;
                    }
                    final int var6 = var4 * 9;
                    final int var7 = var5 * 9;
                    for (int var8 = 0; var8 < 4; ++var8) {
                        for (int var9 = 0; var9 < 4; ++var9) {
                            this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, var6 + var8, 0, var7 + var9, p_74875_3_);
                            this.func_175808_b(worldIn, MonumentBuilding.field_175826_b, var6 + var8, -1, var7 + var9, p_74875_3_);
                        }
                    }
                    if (var4 != 0 && var4 != 6) {
                        var5 += 6;
                    }
                    else {
                        ++var5;
                    }
                }
            }
            for (int var4 = 0; var4 < 5; ++var4) {
                this.func_175804_a(worldIn, p_74875_3_, -1 - var4, 0 + var4 * 2, -1 - var4, -1 - var4, 23, 58 + var4, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_74875_3_, 58 + var4, 0 + var4 * 2, -1 - var4, 58 + var4, 23, 58 + var4, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_74875_3_, 0 - var4, 0 + var4 * 2, -1 - var4, 57 + var4, 23, -1 - var4, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_74875_3_, 0 - var4, 0 + var4 * 2, 58 + var4, 57 + var4, 23, 58 + var4, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
            }
            for (final Piece var11 : this.field_175843_q) {
                if (var11.getBoundingBox().intersectsWith(p_74875_3_)) {
                    var11.addComponentParts(worldIn, p_74875_2_, p_74875_3_);
                }
            }
            return true;
        }
        
        private void func_175840_a(final boolean p_175840_1_, final int p_175840_2_, final World worldIn, final Random p_175840_4_, final StructureBoundingBox p_175840_5_) {
            final boolean var6 = true;
            if (this.func_175818_a(p_175840_5_, p_175840_2_, 0, p_175840_2_ + 23, 20)) {
                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 0, 0, 0, p_175840_2_ + 24, 0, 20, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 0, 1, 0, p_175840_2_ + 24, 10, 20, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                for (int var7 = 0; var7 < 4; ++var7) {
                    this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + var7, var7 + 1, var7, p_175840_2_ + var7, var7 + 1, 20, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + var7 + 7, var7 + 5, var7 + 7, p_175840_2_ + var7 + 7, var7 + 5, 20, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 17 - var7, var7 + 5, var7 + 7, p_175840_2_ + 17 - var7, var7 + 5, 20, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 24 - var7, var7 + 1, var7, p_175840_2_ + 24 - var7, var7 + 1, 20, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + var7 + 1, var7 + 1, var7, p_175840_2_ + 23 - var7, var7 + 1, var7, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + var7 + 8, var7 + 5, var7 + 7, p_175840_2_ + 16 - var7, var7 + 5, var7 + 7, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                }
                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 4, 4, 4, p_175840_2_ + 6, 4, 20, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 7, 4, 4, p_175840_2_ + 17, 4, 6, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 18, 4, 4, p_175840_2_ + 20, 4, 20, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 11, 8, 11, p_175840_2_ + 13, 8, 20, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, p_175840_2_ + 12, 9, 12, p_175840_5_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, p_175840_2_ + 12, 9, 15, p_175840_5_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, p_175840_2_ + 12, 9, 18, p_175840_5_);
                int var7 = p_175840_1_ ? (p_175840_2_ + 19) : (p_175840_2_ + 5);
                final int var8 = p_175840_1_ ? (p_175840_2_ + 5) : (p_175840_2_ + 19);
                for (int var9 = 20; var9 >= 5; var9 -= 3) {
                    this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var7, 5, var9, p_175840_5_);
                }
                for (int var9 = 19; var9 >= 7; var9 -= 3) {
                    this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var8, 5, var9, p_175840_5_);
                }
                for (int var9 = 0; var9 < 4; ++var9) {
                    final int var10 = p_175840_1_ ? (p_175840_2_ + (24 - (17 - var9 * 3))) : (p_175840_2_ + 17 - var9 * 3);
                    this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var10, 5, 5, p_175840_5_);
                }
                this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var8, 5, 5, p_175840_5_);
                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 11, 1, 12, p_175840_2_ + 13, 7, 12, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 12, 1, 11, p_175840_2_ + 12, 7, 13, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
            }
        }
        
        private void func_175839_b(final World worldIn, final Random p_175839_2_, final StructureBoundingBox p_175839_3_) {
            if (this.func_175818_a(p_175839_3_, 22, 5, 35, 17)) {
                this.func_175804_a(worldIn, p_175839_3_, 25, 0, 0, 32, 8, 20, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                for (int var4 = 0; var4 < 4; ++var4) {
                    this.func_175804_a(worldIn, p_175839_3_, 24, 2, 5 + var4 * 4, 24, 4, 5 + var4 * 4, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(worldIn, p_175839_3_, 22, 4, 5 + var4 * 4, 23, 4, 5 + var4 * 4, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                    this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 25, 5, 5 + var4 * 4, p_175839_3_);
                    this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 26, 6, 5 + var4 * 4, p_175839_3_);
                    this.func_175811_a(worldIn, MonumentBuilding.field_175825_e, 26, 5, 5 + var4 * 4, p_175839_3_);
                    this.func_175804_a(worldIn, p_175839_3_, 33, 2, 5 + var4 * 4, 33, 4, 5 + var4 * 4, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(worldIn, p_175839_3_, 34, 4, 5 + var4 * 4, 35, 4, 5 + var4 * 4, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                    this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 32, 5, 5 + var4 * 4, p_175839_3_);
                    this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 31, 6, 5 + var4 * 4, p_175839_3_);
                    this.func_175811_a(worldIn, MonumentBuilding.field_175825_e, 31, 5, 5 + var4 * 4, p_175839_3_);
                    this.func_175804_a(worldIn, p_175839_3_, 27, 6, 5 + var4 * 4, 30, 6, 5 + var4 * 4, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                }
            }
        }
        
        private void func_175837_c(final World worldIn, final Random p_175837_2_, final StructureBoundingBox p_175837_3_) {
            if (this.func_175818_a(p_175837_3_, 15, 20, 42, 21)) {
                this.func_175804_a(worldIn, p_175837_3_, 15, 0, 21, 42, 0, 21, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 26, 1, 21, 31, 3, 21, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 21, 12, 21, 36, 12, 21, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 17, 11, 21, 40, 11, 21, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 16, 10, 21, 41, 10, 21, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 15, 7, 21, 42, 9, 21, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 16, 6, 21, 41, 6, 21, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 17, 5, 21, 40, 5, 21, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 21, 4, 21, 36, 4, 21, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 22, 3, 21, 26, 3, 21, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 31, 3, 21, 35, 3, 21, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 23, 2, 21, 25, 2, 21, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 32, 2, 21, 34, 2, 21, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 28, 4, 20, 29, 4, 21, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 27, 3, 21, p_175837_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 30, 3, 21, p_175837_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 26, 2, 21, p_175837_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 31, 2, 21, p_175837_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 25, 1, 21, p_175837_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 32, 1, 21, p_175837_3_);
                for (int var4 = 0; var4 < 7; ++var4) {
                    this.func_175811_a(worldIn, MonumentBuilding.field_175827_c, 28 - var4, 6 + var4, 21, p_175837_3_);
                    this.func_175811_a(worldIn, MonumentBuilding.field_175827_c, 29 + var4, 6 + var4, 21, p_175837_3_);
                }
                for (int var4 = 0; var4 < 4; ++var4) {
                    this.func_175811_a(worldIn, MonumentBuilding.field_175827_c, 28 - var4, 9 + var4, 21, p_175837_3_);
                    this.func_175811_a(worldIn, MonumentBuilding.field_175827_c, 29 + var4, 9 + var4, 21, p_175837_3_);
                }
                this.func_175811_a(worldIn, MonumentBuilding.field_175827_c, 28, 12, 21, p_175837_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175827_c, 29, 12, 21, p_175837_3_);
                for (int var4 = 0; var4 < 3; ++var4) {
                    this.func_175811_a(worldIn, MonumentBuilding.field_175827_c, 22 - var4 * 2, 8, 21, p_175837_3_);
                    this.func_175811_a(worldIn, MonumentBuilding.field_175827_c, 22 - var4 * 2, 9, 21, p_175837_3_);
                    this.func_175811_a(worldIn, MonumentBuilding.field_175827_c, 35 + var4 * 2, 8, 21, p_175837_3_);
                    this.func_175811_a(worldIn, MonumentBuilding.field_175827_c, 35 + var4 * 2, 9, 21, p_175837_3_);
                }
                this.func_175804_a(worldIn, p_175837_3_, 15, 13, 21, 42, 15, 21, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 15, 1, 21, 15, 6, 21, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 16, 1, 21, 16, 5, 21, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 17, 1, 21, 20, 4, 21, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 21, 1, 21, 21, 3, 21, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 22, 1, 21, 22, 2, 21, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 23, 1, 21, 24, 1, 21, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 42, 1, 21, 42, 6, 21, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 41, 1, 21, 41, 5, 21, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 37, 1, 21, 40, 4, 21, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 36, 1, 21, 36, 3, 21, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 35, 1, 21, 35, 2, 21, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 33, 1, 21, 34, 1, 21, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
            }
        }
        
        private void func_175841_d(final World worldIn, final Random p_175841_2_, final StructureBoundingBox p_175841_3_) {
            if (this.func_175818_a(p_175841_3_, 21, 21, 36, 36)) {
                this.func_175804_a(worldIn, p_175841_3_, 21, 0, 22, 36, 0, 36, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175841_3_, 21, 1, 22, 36, 23, 36, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                for (int var4 = 0; var4 < 4; ++var4) {
                    this.func_175804_a(worldIn, p_175841_3_, 21 + var4, 13 + var4, 21 + var4, 36 - var4, 13 + var4, 21 + var4, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(worldIn, p_175841_3_, 21 + var4, 13 + var4, 36 - var4, 36 - var4, 13 + var4, 36 - var4, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(worldIn, p_175841_3_, 21 + var4, 13 + var4, 22 + var4, 21 + var4, 13 + var4, 35 - var4, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(worldIn, p_175841_3_, 36 - var4, 13 + var4, 22 + var4, 36 - var4, 13 + var4, 35 - var4, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                }
                this.func_175804_a(worldIn, p_175841_3_, 25, 16, 25, 32, 16, 32, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175841_3_, 25, 17, 25, 25, 19, 25, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                this.func_175804_a(worldIn, p_175841_3_, 32, 17, 25, 32, 19, 25, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                this.func_175804_a(worldIn, p_175841_3_, 25, 17, 32, 25, 19, 32, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                this.func_175804_a(worldIn, p_175841_3_, 32, 17, 32, 32, 19, 32, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 26, 20, 26, p_175841_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 27, 21, 27, p_175841_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175825_e, 27, 20, 27, p_175841_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 26, 20, 31, p_175841_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 27, 21, 30, p_175841_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175825_e, 27, 20, 30, p_175841_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 31, 20, 31, p_175841_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 30, 21, 30, p_175841_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175825_e, 30, 20, 30, p_175841_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 31, 20, 26, p_175841_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175826_b, 30, 21, 27, p_175841_3_);
                this.func_175811_a(worldIn, MonumentBuilding.field_175825_e, 30, 20, 27, p_175841_3_);
                this.func_175804_a(worldIn, p_175841_3_, 28, 21, 27, 29, 21, 27, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175841_3_, 27, 21, 28, 27, 21, 29, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175841_3_, 28, 21, 30, 29, 21, 30, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175841_3_, 30, 21, 28, 30, 21, 29, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
            }
        }
        
        private void func_175835_e(final World worldIn, final Random p_175835_2_, final StructureBoundingBox p_175835_3_) {
            if (this.func_175818_a(p_175835_3_, 0, 21, 6, 58)) {
                this.func_175804_a(worldIn, p_175835_3_, 0, 0, 21, 6, 0, 57, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175835_3_, 0, 1, 21, 6, 7, 57, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175835_3_, 4, 4, 21, 6, 4, 53, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                for (int var4 = 0; var4 < 4; ++var4) {
                    this.func_175804_a(worldIn, p_175835_3_, var4, var4 + 1, 21, var4, var4 + 1, 57 - var4, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                }
                for (int var4 = 23; var4 < 53; var4 += 3) {
                    this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, 5, 5, var4, p_175835_3_);
                }
                this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, 5, 5, 52, p_175835_3_);
                for (int var4 = 0; var4 < 4; ++var4) {
                    this.func_175804_a(worldIn, p_175835_3_, var4, var4 + 1, 21, var4, var4 + 1, 57 - var4, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                }
                this.func_175804_a(worldIn, p_175835_3_, 4, 1, 52, 6, 3, 52, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175835_3_, 5, 1, 51, 5, 3, 53, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
            }
            if (this.func_175818_a(p_175835_3_, 51, 21, 58, 58)) {
                this.func_175804_a(worldIn, p_175835_3_, 51, 0, 21, 57, 0, 57, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175835_3_, 51, 1, 21, 57, 7, 57, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175835_3_, 51, 4, 21, 53, 4, 53, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                for (int var4 = 0; var4 < 4; ++var4) {
                    this.func_175804_a(worldIn, p_175835_3_, 57 - var4, var4 + 1, 21, 57 - var4, var4 + 1, 57 - var4, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                }
                for (int var4 = 23; var4 < 53; var4 += 3) {
                    this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, 52, 5, var4, p_175835_3_);
                }
                this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, 52, 5, 52, p_175835_3_);
                this.func_175804_a(worldIn, p_175835_3_, 51, 1, 52, 53, 3, 52, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175835_3_, 52, 1, 51, 52, 3, 53, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
            }
            if (this.func_175818_a(p_175835_3_, 0, 51, 57, 57)) {
                this.func_175804_a(worldIn, p_175835_3_, 7, 0, 51, 50, 0, 57, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175835_3_, 7, 1, 51, 50, 10, 57, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                for (int var4 = 0; var4 < 4; ++var4) {
                    this.func_175804_a(worldIn, p_175835_3_, var4 + 1, var4 + 1, 57 - var4, 56 - var4, var4 + 1, 57 - var4, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                }
            }
        }
        
        private void func_175842_f(final World worldIn, final Random p_175842_2_, final StructureBoundingBox p_175842_3_) {
            if (this.func_175818_a(p_175842_3_, 7, 21, 13, 50)) {
                this.func_175804_a(worldIn, p_175842_3_, 7, 0, 21, 13, 0, 50, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175842_3_, 7, 1, 21, 13, 10, 50, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175842_3_, 11, 8, 21, 13, 8, 53, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                for (int var4 = 0; var4 < 4; ++var4) {
                    this.func_175804_a(worldIn, p_175842_3_, var4 + 7, var4 + 5, 21, var4 + 7, var4 + 5, 54, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                }
                for (int var4 = 21; var4 <= 45; var4 += 3) {
                    this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, 12, 9, var4, p_175842_3_);
                }
            }
            if (this.func_175818_a(p_175842_3_, 44, 21, 50, 54)) {
                this.func_175804_a(worldIn, p_175842_3_, 44, 0, 21, 50, 0, 50, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175842_3_, 44, 1, 21, 50, 10, 50, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175842_3_, 44, 8, 21, 46, 8, 53, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                for (int var4 = 0; var4 < 4; ++var4) {
                    this.func_175804_a(worldIn, p_175842_3_, 50 - var4, var4 + 5, 21, 50 - var4, var4 + 5, 54, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                }
                for (int var4 = 21; var4 <= 45; var4 += 3) {
                    this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, 45, 9, var4, p_175842_3_);
                }
            }
            if (this.func_175818_a(p_175842_3_, 8, 44, 49, 54)) {
                this.func_175804_a(worldIn, p_175842_3_, 14, 0, 44, 43, 0, 50, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175842_3_, 14, 1, 44, 43, 10, 50, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                for (int var4 = 12; var4 <= 45; var4 += 3) {
                    this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var4, 9, 45, p_175842_3_);
                    this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var4, 9, 52, p_175842_3_);
                    if (var4 == 12 || var4 == 18 || var4 == 24 || var4 == 33 || var4 == 39 || var4 == 45) {
                        this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var4, 9, 47, p_175842_3_);
                        this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var4, 9, 50, p_175842_3_);
                        this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var4, 10, 45, p_175842_3_);
                        this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var4, 10, 46, p_175842_3_);
                        this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var4, 10, 51, p_175842_3_);
                        this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var4, 10, 52, p_175842_3_);
                        this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var4, 11, 47, p_175842_3_);
                        this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var4, 11, 50, p_175842_3_);
                        this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var4, 12, 48, p_175842_3_);
                        this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var4, 12, 49, p_175842_3_);
                    }
                }
                for (int var4 = 0; var4 < 3; ++var4) {
                    this.func_175804_a(worldIn, p_175842_3_, 8 + var4, 5 + var4, 54, 49 - var4, 5 + var4, 54, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                }
                this.func_175804_a(worldIn, p_175842_3_, 11, 8, 54, 46, 8, 54, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                this.func_175804_a(worldIn, p_175842_3_, 14, 8, 44, 43, 8, 53, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
            }
        }
        
        private void func_175838_g(final World worldIn, final Random p_175838_2_, final StructureBoundingBox p_175838_3_) {
            if (this.func_175818_a(p_175838_3_, 14, 21, 20, 43)) {
                this.func_175804_a(worldIn, p_175838_3_, 14, 0, 21, 20, 0, 43, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175838_3_, 14, 1, 22, 20, 14, 43, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175838_3_, 18, 12, 22, 20, 12, 39, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175838_3_, 18, 12, 21, 20, 12, 21, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                for (int var4 = 0; var4 < 4; ++var4) {
                    this.func_175804_a(worldIn, p_175838_3_, var4 + 14, var4 + 9, 21, var4 + 14, var4 + 9, 43 - var4, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                }
                for (int var4 = 23; var4 <= 39; var4 += 3) {
                    this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, 19, 13, var4, p_175838_3_);
                }
            }
            if (this.func_175818_a(p_175838_3_, 37, 21, 43, 43)) {
                this.func_175804_a(worldIn, p_175838_3_, 37, 0, 21, 43, 0, 43, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175838_3_, 37, 1, 22, 43, 14, 43, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175838_3_, 37, 12, 22, 39, 12, 39, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175838_3_, 37, 12, 21, 39, 12, 21, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                for (int var4 = 0; var4 < 4; ++var4) {
                    this.func_175804_a(worldIn, p_175838_3_, 43 - var4, var4 + 9, 21, 43 - var4, var4 + 9, 43 - var4, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                }
                for (int var4 = 23; var4 <= 39; var4 += 3) {
                    this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, 38, 13, var4, p_175838_3_);
                }
            }
            if (this.func_175818_a(p_175838_3_, 15, 37, 42, 43)) {
                this.func_175804_a(worldIn, p_175838_3_, 21, 0, 37, 36, 0, 43, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                this.func_175804_a(worldIn, p_175838_3_, 21, 1, 37, 36, 14, 43, MonumentBuilding.field_175822_f, MonumentBuilding.field_175822_f, false);
                this.func_175804_a(worldIn, p_175838_3_, 21, 12, 37, 36, 12, 39, MonumentBuilding.field_175828_a, MonumentBuilding.field_175828_a, false);
                for (int var4 = 0; var4 < 4; ++var4) {
                    this.func_175804_a(worldIn, p_175838_3_, 15 + var4, var4 + 9, 43 - var4, 42 - var4, var4 + 9, 43 - var4, MonumentBuilding.field_175826_b, MonumentBuilding.field_175826_b, false);
                }
                for (int var4 = 21; var4 <= 36; var4 += 3) {
                    this.func_175811_a(worldIn, MonumentBuilding.field_175824_d, var4, 13, 38, p_175838_3_);
                }
            }
        }
    }
    
    public static class MonumentCoreRoom extends Piece
    {
        private static final String __OBFID = "CL_00001984";
        
        public MonumentCoreRoom() {
        }
        
        public MonumentCoreRoom(final EnumFacing p_i45598_1_, final RoomDefinition p_i45598_2_, final Random p_i45598_3_) {
            super(1, p_i45598_1_, p_i45598_2_, 2, 2, 2);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            this.func_175819_a(worldIn, p_74875_3_, 1, 8, 0, 14, 8, 14, MonumentCoreRoom.field_175828_a);
            final byte var4 = 7;
            IBlockState var5 = MonumentCoreRoom.field_175826_b;
            this.func_175804_a(worldIn, p_74875_3_, 0, var4, 0, 0, var4, 15, var5, var5, false);
            this.func_175804_a(worldIn, p_74875_3_, 15, var4, 0, 15, var4, 15, var5, var5, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, var4, 0, 15, var4, 0, var5, var5, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, var4, 15, 14, var4, 15, var5, var5, false);
            for (int var6 = 1; var6 <= 6; ++var6) {
                var5 = MonumentCoreRoom.field_175826_b;
                if (var6 == 2 || var6 == 6) {
                    var5 = MonumentCoreRoom.field_175828_a;
                }
                for (int var7 = 0; var7 <= 15; var7 += 15) {
                    this.func_175804_a(worldIn, p_74875_3_, var7, var6, 0, var7, var6, 1, var5, var5, false);
                    this.func_175804_a(worldIn, p_74875_3_, var7, var6, 6, var7, var6, 9, var5, var5, false);
                    this.func_175804_a(worldIn, p_74875_3_, var7, var6, 14, var7, var6, 15, var5, var5, false);
                }
                this.func_175804_a(worldIn, p_74875_3_, 1, var6, 0, 1, var6, 0, var5, var5, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, var6, 0, 9, var6, 0, var5, var5, false);
                this.func_175804_a(worldIn, p_74875_3_, 14, var6, 0, 14, var6, 0, var5, var5, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, var6, 15, 14, var6, 15, var5, var5, false);
            }
            this.func_175804_a(worldIn, p_74875_3_, 6, 3, 6, 9, 6, 9, MonumentCoreRoom.field_175827_c, MonumentCoreRoom.field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 4, 7, 8, 5, 8, Blocks.gold_block.getDefaultState(), Blocks.gold_block.getDefaultState(), false);
            for (int var6 = 3; var6 <= 6; var6 += 3) {
                for (int var8 = 6; var8 <= 9; var8 += 3) {
                    this.func_175811_a(worldIn, MonumentCoreRoom.field_175825_e, var8, var6, 6, p_74875_3_);
                    this.func_175811_a(worldIn, MonumentCoreRoom.field_175825_e, var8, var6, 9, p_74875_3_);
                }
            }
            this.func_175804_a(worldIn, p_74875_3_, 5, 1, 6, 5, 2, 6, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 1, 9, 5, 2, 9, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 1, 6, 10, 2, 6, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 1, 9, 10, 2, 9, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 1, 5, 6, 2, 5, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 9, 1, 5, 9, 2, 5, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 1, 10, 6, 2, 10, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 9, 1, 10, 9, 2, 10, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 2, 5, 5, 6, 5, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 2, 10, 5, 6, 10, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 2, 5, 10, 6, 5, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 2, 10, 10, 6, 10, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 7, 1, 5, 7, 6, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 7, 1, 10, 7, 6, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 7, 9, 5, 7, 14, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 7, 9, 10, 7, 14, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 7, 5, 6, 7, 5, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 7, 10, 6, 7, 10, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 9, 7, 5, 14, 7, 5, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 9, 7, 10, 14, 7, 10, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 1, 2, 2, 1, 3, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 2, 3, 1, 2, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 13, 1, 2, 13, 1, 3, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 12, 1, 2, 12, 1, 2, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 1, 12, 2, 1, 13, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 13, 3, 1, 13, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 13, 1, 12, 13, 1, 13, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 12, 1, 13, 12, 1, 13, MonumentCoreRoom.field_175826_b, MonumentCoreRoom.field_175826_b, false);
            return true;
        }
    }
    
    public static class Penthouse extends Piece
    {
        private static final String __OBFID = "CL_00001977";
        
        public Penthouse() {
        }
        
        public Penthouse(final EnumFacing p_i45591_1_, final StructureBoundingBox p_i45591_2_) {
            super(p_i45591_1_, p_i45591_2_);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            this.func_175804_a(worldIn, p_74875_3_, 2, -1, 2, 11, -1, 11, Penthouse.field_175826_b, Penthouse.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, -1, 0, 1, -1, 11, Penthouse.field_175828_a, Penthouse.field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 12, -1, 0, 13, -1, 11, Penthouse.field_175828_a, Penthouse.field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, -1, 0, 11, -1, 1, Penthouse.field_175828_a, Penthouse.field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, -1, 12, 11, -1, 13, Penthouse.field_175828_a, Penthouse.field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 0, 0, 0, 0, 13, Penthouse.field_175826_b, Penthouse.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 13, 0, 0, 13, 0, 13, Penthouse.field_175826_b, Penthouse.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 0, 12, 0, 0, Penthouse.field_175826_b, Penthouse.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 13, 12, 0, 13, Penthouse.field_175826_b, Penthouse.field_175826_b, false);
            for (int var4 = 2; var4 <= 11; var4 += 3) {
                this.func_175811_a(worldIn, Penthouse.field_175825_e, 0, 0, var4, p_74875_3_);
                this.func_175811_a(worldIn, Penthouse.field_175825_e, 13, 0, var4, p_74875_3_);
                this.func_175811_a(worldIn, Penthouse.field_175825_e, var4, 0, 0, p_74875_3_);
            }
            this.func_175804_a(worldIn, p_74875_3_, 2, 0, 3, 4, 0, 9, Penthouse.field_175826_b, Penthouse.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 9, 0, 3, 11, 0, 9, Penthouse.field_175826_b, Penthouse.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 4, 0, 9, 9, 0, 11, Penthouse.field_175826_b, Penthouse.field_175826_b, false);
            this.func_175811_a(worldIn, Penthouse.field_175826_b, 5, 0, 8, p_74875_3_);
            this.func_175811_a(worldIn, Penthouse.field_175826_b, 8, 0, 8, p_74875_3_);
            this.func_175811_a(worldIn, Penthouse.field_175826_b, 10, 0, 10, p_74875_3_);
            this.func_175811_a(worldIn, Penthouse.field_175826_b, 3, 0, 10, p_74875_3_);
            this.func_175804_a(worldIn, p_74875_3_, 3, 0, 3, 3, 0, 7, Penthouse.field_175827_c, Penthouse.field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 0, 3, 10, 0, 7, Penthouse.field_175827_c, Penthouse.field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 0, 10, 7, 0, 10, Penthouse.field_175827_c, Penthouse.field_175827_c, false);
            byte var5 = 3;
            for (int var6 = 0; var6 < 2; ++var6) {
                for (int var7 = 2; var7 <= 8; var7 += 3) {
                    this.func_175804_a(worldIn, p_74875_3_, var5, 0, var7, var5, 2, var7, Penthouse.field_175826_b, Penthouse.field_175826_b, false);
                }
                var5 = 10;
            }
            this.func_175804_a(worldIn, p_74875_3_, 5, 0, 10, 5, 2, 10, Penthouse.field_175826_b, Penthouse.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 8, 0, 10, 8, 2, 10, Penthouse.field_175826_b, Penthouse.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, -1, 7, 7, -1, 8, Penthouse.field_175827_c, Penthouse.field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, -1, 3, 7, -1, 4, Penthouse.field_175822_f, Penthouse.field_175822_f, false);
            this.func_175817_a(worldIn, p_74875_3_, 6, 1, 6);
            return true;
        }
    }
    
    public abstract static class Piece extends StructureComponent
    {
        protected static final IBlockState field_175828_a;
        protected static final IBlockState field_175826_b;
        protected static final IBlockState field_175827_c;
        protected static final IBlockState field_175824_d;
        protected static final IBlockState field_175825_e;
        protected static final IBlockState field_175822_f;
        protected static final int field_175823_g;
        protected static final int field_175831_h;
        protected static final int field_175832_i;
        protected static final int field_175829_j;
        protected RoomDefinition field_175830_k;
        private static final String __OBFID = "CL_00001976";
        
        protected static final int func_175820_a(final int p_175820_0_, final int p_175820_1_, final int p_175820_2_) {
            return p_175820_1_ * 25 + p_175820_2_ * 5 + p_175820_0_;
        }
        
        public Piece() {
            super(0);
        }
        
        public Piece(final int p_i45588_1_) {
            super(p_i45588_1_);
        }
        
        public Piece(final EnumFacing p_i45589_1_, final StructureBoundingBox p_i45589_2_) {
            super(1);
            this.coordBaseMode = p_i45589_1_;
            this.boundingBox = p_i45589_2_;
        }
        
        protected Piece(final int p_i45590_1_, final EnumFacing p_i45590_2_, final RoomDefinition p_i45590_3_, final int p_i45590_4_, final int p_i45590_5_, final int p_i45590_6_) {
            super(p_i45590_1_);
            this.coordBaseMode = p_i45590_2_;
            this.field_175830_k = p_i45590_3_;
            final int var7 = p_i45590_3_.field_175967_a;
            final int var8 = var7 % 5;
            final int var9 = var7 / 5 % 5;
            final int var10 = var7 / 25;
            if (p_i45590_2_ != EnumFacing.NORTH && p_i45590_2_ != EnumFacing.SOUTH) {
                this.boundingBox = new StructureBoundingBox(0, 0, 0, p_i45590_6_ * 8 - 1, p_i45590_5_ * 4 - 1, p_i45590_4_ * 8 - 1);
            }
            else {
                this.boundingBox = new StructureBoundingBox(0, 0, 0, p_i45590_4_ * 8 - 1, p_i45590_5_ * 4 - 1, p_i45590_6_ * 8 - 1);
            }
            switch (StructureOceanMonumentPieces.SwitchEnumFacing.field_175971_a[p_i45590_2_.ordinal()]) {
                case 1: {
                    this.boundingBox.offset(var8 * 8, var10 * 4, -(var9 + p_i45590_6_) * 8 + 1);
                    break;
                }
                case 2: {
                    this.boundingBox.offset(var8 * 8, var10 * 4, var9 * 8);
                    break;
                }
                case 3: {
                    this.boundingBox.offset(-(var9 + p_i45590_6_) * 8 + 1, var10 * 4, var8 * 8);
                    break;
                }
                default: {
                    this.boundingBox.offset(var9 * 8, var10 * 4, var8 * 8);
                    break;
                }
            }
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound p_143012_1_) {
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound p_143011_1_) {
        }
        
        protected void func_175821_a(final World worldIn, final StructureBoundingBox p_175821_2_, final int p_175821_3_, final int p_175821_4_, final boolean p_175821_5_) {
            if (p_175821_5_) {
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 0, 0, p_175821_4_ + 0, p_175821_3_ + 2, 0, p_175821_4_ + 8 - 1, Piece.field_175828_a, Piece.field_175828_a, false);
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 5, 0, p_175821_4_ + 0, p_175821_3_ + 8 - 1, 0, p_175821_4_ + 8 - 1, Piece.field_175828_a, Piece.field_175828_a, false);
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 0, p_175821_3_ + 4, 0, p_175821_4_ + 2, Piece.field_175828_a, Piece.field_175828_a, false);
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 5, p_175821_3_ + 4, 0, p_175821_4_ + 8 - 1, Piece.field_175828_a, Piece.field_175828_a, false);
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 2, p_175821_3_ + 4, 0, p_175821_4_ + 2, Piece.field_175826_b, Piece.field_175826_b, false);
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 5, p_175821_3_ + 4, 0, p_175821_4_ + 5, Piece.field_175826_b, Piece.field_175826_b, false);
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 2, 0, p_175821_4_ + 3, p_175821_3_ + 2, 0, p_175821_4_ + 4, Piece.field_175826_b, Piece.field_175826_b, false);
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 5, 0, p_175821_4_ + 3, p_175821_3_ + 5, 0, p_175821_4_ + 4, Piece.field_175826_b, Piece.field_175826_b, false);
            }
            else {
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 0, 0, p_175821_4_ + 0, p_175821_3_ + 8 - 1, 0, p_175821_4_ + 8 - 1, Piece.field_175828_a, Piece.field_175828_a, false);
            }
        }
        
        protected void func_175819_a(final World worldIn, final StructureBoundingBox p_175819_2_, final int p_175819_3_, final int p_175819_4_, final int p_175819_5_, final int p_175819_6_, final int p_175819_7_, final int p_175819_8_, final IBlockState p_175819_9_) {
            for (int var10 = p_175819_4_; var10 <= p_175819_7_; ++var10) {
                for (int var11 = p_175819_3_; var11 <= p_175819_6_; ++var11) {
                    for (int var12 = p_175819_5_; var12 <= p_175819_8_; ++var12) {
                        if (this.func_175807_a(worldIn, var11, var10, var12, p_175819_2_) == Piece.field_175822_f) {
                            this.func_175811_a(worldIn, p_175819_9_, var11, var10, var12, p_175819_2_);
                        }
                    }
                }
            }
        }
        
        protected boolean func_175818_a(final StructureBoundingBox p_175818_1_, final int p_175818_2_, final int p_175818_3_, final int p_175818_4_, final int p_175818_5_) {
            final int var6 = this.getXWithOffset(p_175818_2_, p_175818_3_);
            final int var7 = this.getZWithOffset(p_175818_2_, p_175818_3_);
            final int var8 = this.getXWithOffset(p_175818_4_, p_175818_5_);
            final int var9 = this.getZWithOffset(p_175818_4_, p_175818_5_);
            return p_175818_1_.intersectsWith(Math.min(var6, var8), Math.min(var7, var9), Math.max(var6, var8), Math.max(var7, var9));
        }
        
        protected boolean func_175817_a(final World worldIn, final StructureBoundingBox p_175817_2_, final int p_175817_3_, final int p_175817_4_, final int p_175817_5_) {
            final int var6 = this.getXWithOffset(p_175817_3_, p_175817_5_);
            final int var7 = this.getYWithOffset(p_175817_4_);
            final int var8 = this.getZWithOffset(p_175817_3_, p_175817_5_);
            if (p_175817_2_.func_175898_b(new BlockPos(var6, var7, var8))) {
                final EntityGuardian var9 = new EntityGuardian(worldIn);
                var9.func_175467_a(true);
                var9.heal(var9.getMaxHealth());
                var9.setLocationAndAngles(var6 + 0.5, var7, var8 + 0.5, 0.0f, 0.0f);
                var9.func_180482_a(worldIn.getDifficultyForLocation(new BlockPos(var9)), null);
                worldIn.spawnEntityInWorld(var9);
                return true;
            }
            return false;
        }
        
        static {
            field_175828_a = Blocks.prismarine.getStateFromMeta(BlockPrismarine.ROUGHMETA);
            field_175826_b = Blocks.prismarine.getStateFromMeta(BlockPrismarine.BRICKSMETA);
            field_175827_c = Blocks.prismarine.getStateFromMeta(BlockPrismarine.DARKMETA);
            field_175824_d = Piece.field_175826_b;
            field_175825_e = Blocks.sea_lantern.getDefaultState();
            field_175822_f = Blocks.water.getDefaultState();
            field_175823_g = func_175820_a(2, 0, 0);
            field_175831_h = func_175820_a(2, 2, 0);
            field_175832_i = func_175820_a(0, 1, 0);
            field_175829_j = func_175820_a(4, 1, 0);
        }
    }
    
    static class RoomDefinition
    {
        int field_175967_a;
        RoomDefinition[] field_175965_b;
        boolean[] field_175966_c;
        boolean field_175963_d;
        boolean field_175964_e;
        int field_175962_f;
        private static final String __OBFID = "CL_00001972";
        
        public RoomDefinition(final int p_i45584_1_) {
            this.field_175965_b = new RoomDefinition[6];
            this.field_175966_c = new boolean[6];
            this.field_175967_a = p_i45584_1_;
        }
        
        public void func_175957_a(final EnumFacing p_175957_1_, final RoomDefinition p_175957_2_) {
            this.field_175965_b[p_175957_1_.getIndex()] = p_175957_2_;
            p_175957_2_.field_175965_b[p_175957_1_.getOpposite().getIndex()] = this;
        }
        
        public void func_175958_a() {
            for (int var1 = 0; var1 < 6; ++var1) {
                this.field_175966_c[var1] = (this.field_175965_b[var1] != null);
            }
        }
        
        public boolean func_175959_a(final int p_175959_1_) {
            if (this.field_175964_e) {
                return true;
            }
            this.field_175962_f = p_175959_1_;
            for (int var2 = 0; var2 < 6; ++var2) {
                if (this.field_175965_b[var2] != null && this.field_175966_c[var2] && this.field_175965_b[var2].field_175962_f != p_175959_1_ && this.field_175965_b[var2].func_175959_a(p_175959_1_)) {
                    return true;
                }
            }
            return false;
        }
        
        public boolean func_175961_b() {
            return this.field_175967_a >= 75;
        }
        
        public int func_175960_c() {
            int var1 = 0;
            for (int var2 = 0; var2 < 6; ++var2) {
                if (this.field_175966_c[var2]) {
                    ++var1;
                }
            }
            return var1;
        }
    }
    
    public static class SimpleRoom extends Piece
    {
        private int field_175833_o;
        private static final String __OBFID = "CL_00001975";
        
        public SimpleRoom() {
        }
        
        public SimpleRoom(final EnumFacing p_i45587_1_, final RoomDefinition p_i45587_2_, final Random p_i45587_3_) {
            super(1, p_i45587_1_, p_i45587_2_, 1, 1, 1);
            this.field_175833_o = p_i45587_3_.nextInt(3);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(worldIn, p_74875_3_, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }
            if (this.field_175830_k.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(worldIn, p_74875_3_, 1, 4, 1, 6, 4, 6, SimpleRoom.field_175828_a);
            }
            final boolean var4 = this.field_175833_o != 0 && p_74875_2_.nextBoolean() && !this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()] && !this.field_175830_k.field_175966_c[EnumFacing.UP.getIndex()] && this.field_175830_k.func_175960_c() > 1;
            if (this.field_175833_o == 0) {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 2, 1, 2, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 3, 0, 2, 3, 2, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 2, 0, 0, 2, 2, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 2, 0, 2, 2, 0, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                this.func_175811_a(worldIn, SimpleRoom.field_175825_e, 1, 2, 1, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 5, 1, 0, 7, 1, 2, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 3, 0, 7, 3, 2, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 2, 0, 7, 2, 2, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 2, 0, 6, 2, 0, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                this.func_175811_a(worldIn, SimpleRoom.field_175825_e, 6, 2, 1, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 5, 2, 1, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 3, 5, 2, 3, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 2, 5, 0, 2, 7, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 2, 7, 2, 2, 7, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                this.func_175811_a(worldIn, SimpleRoom.field_175825_e, 1, 2, 6, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 5, 1, 5, 7, 1, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 3, 5, 7, 3, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 2, 5, 7, 2, 7, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 2, 7, 6, 2, 7, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                this.func_175811_a(worldIn, SimpleRoom.field_175825_e, 6, 2, 6, p_74875_3_);
                if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, 3, 3, 0, 4, 3, 0, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                }
                else {
                    this.func_175804_a(worldIn, p_74875_3_, 3, 3, 0, 4, 3, 1, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 3, 2, 0, 4, 2, 0, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 1, 1, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                }
                if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, 3, 3, 7, 4, 3, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                }
                else {
                    this.func_175804_a(worldIn, p_74875_3_, 3, 3, 6, 4, 3, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 3, 2, 7, 4, 2, 7, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 3, 1, 6, 4, 1, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                }
                if (this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, 0, 3, 3, 0, 3, 4, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                }
                else {
                    this.func_175804_a(worldIn, p_74875_3_, 0, 3, 3, 1, 3, 4, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, 2, 3, 0, 2, 4, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 1, 1, 4, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                }
                if (this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, 7, 3, 3, 7, 3, 4, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                }
                else {
                    this.func_175804_a(worldIn, p_74875_3_, 6, 3, 3, 7, 3, 4, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 7, 2, 3, 7, 2, 4, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 6, 1, 3, 7, 1, 4, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                }
            }
            else if (this.field_175833_o == 1) {
                this.func_175804_a(worldIn, p_74875_3_, 2, 1, 2, 2, 3, 2, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 2, 1, 5, 2, 3, 5, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 1, 5, 5, 3, 5, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 1, 2, 5, 3, 2, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175811_a(worldIn, SimpleRoom.field_175825_e, 2, 2, 2, p_74875_3_);
                this.func_175811_a(worldIn, SimpleRoom.field_175825_e, 2, 2, 5, p_74875_3_);
                this.func_175811_a(worldIn, SimpleRoom.field_175825_e, 5, 2, 5, p_74875_3_);
                this.func_175811_a(worldIn, SimpleRoom.field_175825_e, 5, 2, 2, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 1, 3, 0, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 1, 0, 3, 1, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 7, 1, 3, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 6, 0, 3, 6, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, 1, 7, 7, 3, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 6, 7, 3, 6, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, 1, 0, 7, 3, 0, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 1, 7, 3, 1, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175811_a(worldIn, SimpleRoom.field_175828_a, 1, 2, 0, p_74875_3_);
                this.func_175811_a(worldIn, SimpleRoom.field_175828_a, 0, 2, 1, p_74875_3_);
                this.func_175811_a(worldIn, SimpleRoom.field_175828_a, 1, 2, 7, p_74875_3_);
                this.func_175811_a(worldIn, SimpleRoom.field_175828_a, 0, 2, 6, p_74875_3_);
                this.func_175811_a(worldIn, SimpleRoom.field_175828_a, 6, 2, 7, p_74875_3_);
                this.func_175811_a(worldIn, SimpleRoom.field_175828_a, 7, 2, 6, p_74875_3_);
                this.func_175811_a(worldIn, SimpleRoom.field_175828_a, 6, 2, 0, p_74875_3_);
                this.func_175811_a(worldIn, SimpleRoom.field_175828_a, 7, 2, 1, p_74875_3_);
                if (!this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, 1, 3, 0, 6, 3, 0, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 1, 2, 0, 6, 2, 0, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 1, 1, 0, 6, 1, 0, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                }
                if (!this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, 1, 3, 7, 6, 3, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 1, 2, 7, 6, 2, 7, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 1, 1, 7, 6, 1, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                }
                if (!this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, 0, 3, 1, 0, 3, 6, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, 2, 1, 0, 2, 6, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, 1, 1, 0, 1, 6, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                }
                if (!this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, 7, 3, 1, 7, 3, 6, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 7, 2, 1, 7, 2, 6, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 7, 1, 1, 7, 1, 6, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                }
            }
            else if (this.field_175833_o == 2) {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 0, 1, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 0, 7, 1, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 1, 0, 6, 1, 0, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 1, 7, 6, 1, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 2, 0, 0, 2, 7, SimpleRoom.field_175827_c, SimpleRoom.field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 2, 0, 7, 2, 7, SimpleRoom.field_175827_c, SimpleRoom.field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 2, 0, 6, 2, 0, SimpleRoom.field_175827_c, SimpleRoom.field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 2, 7, 6, 2, 7, SimpleRoom.field_175827_c, SimpleRoom.field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 3, 0, 0, 3, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 3, 0, 7, 3, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 3, 0, 6, 3, 0, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 3, 7, 6, 3, 7, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 0, 2, 4, SimpleRoom.field_175827_c, SimpleRoom.field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 3, 7, 2, 4, SimpleRoom.field_175827_c, SimpleRoom.field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, SimpleRoom.field_175827_c, SimpleRoom.field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 7, 4, 2, 7, SimpleRoom.field_175827_c, SimpleRoom.field_175827_c, false);
                if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, SimpleRoom.field_175822_f, SimpleRoom.field_175822_f, false);
                }
                if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, 3, 1, 7, 4, 2, 7, SimpleRoom.field_175822_f, SimpleRoom.field_175822_f, false);
                }
                if (this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 0, 2, 4, SimpleRoom.field_175822_f, SimpleRoom.field_175822_f, false);
                }
                if (this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()]) {
                    this.func_175804_a(worldIn, p_74875_3_, 7, 1, 3, 7, 2, 4, SimpleRoom.field_175822_f, SimpleRoom.field_175822_f, false);
                }
            }
            if (var4) {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 3, 4, 1, 4, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 3, 2, 3, 4, 2, 4, SimpleRoom.field_175828_a, SimpleRoom.field_175828_a, false);
                this.func_175804_a(worldIn, p_74875_3_, 3, 3, 3, 4, 3, 4, SimpleRoom.field_175826_b, SimpleRoom.field_175826_b, false);
            }
            return true;
        }
    }
    
    public static class SimpleTopRoom extends Piece
    {
        private static final String __OBFID = "CL_00001974";
        
        public SimpleTopRoom() {
        }
        
        public SimpleTopRoom(final EnumFacing p_i45586_1_, final RoomDefinition p_i45586_2_, final Random p_i45586_3_) {
            super(1, p_i45586_1_, p_i45586_2_, 1, 1, 1);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(worldIn, p_74875_3_, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }
            if (this.field_175830_k.field_175965_b[EnumFacing.UP.getIndex()] == null) {
                this.func_175819_a(worldIn, p_74875_3_, 1, 4, 1, 6, 4, 6, SimpleTopRoom.field_175828_a);
            }
            for (int var4 = 1; var4 <= 6; ++var4) {
                for (int var5 = 1; var5 <= 6; ++var5) {
                    if (p_74875_2_.nextInt(3) != 0) {
                        final int var6 = 2 + ((p_74875_2_.nextInt(4) != 0) ? 1 : 0);
                        this.func_175804_a(worldIn, p_74875_3_, var4, var6, var5, var4, 3, var5, Blocks.sponge.getStateFromMeta(1), Blocks.sponge.getStateFromMeta(1), false);
                    }
                }
            }
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 0, 1, 7, SimpleTopRoom.field_175826_b, SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 1, 0, 7, 1, 7, SimpleTopRoom.field_175826_b, SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 0, 6, 1, 0, SimpleTopRoom.field_175826_b, SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 7, 6, 1, 7, SimpleTopRoom.field_175826_b, SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 2, 0, 0, 2, 7, SimpleTopRoom.field_175827_c, SimpleTopRoom.field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 2, 0, 7, 2, 7, SimpleTopRoom.field_175827_c, SimpleTopRoom.field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 0, 6, 2, 0, SimpleTopRoom.field_175827_c, SimpleTopRoom.field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 7, 6, 2, 7, SimpleTopRoom.field_175827_c, SimpleTopRoom.field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 3, 0, 0, 3, 7, SimpleTopRoom.field_175826_b, SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 3, 0, 7, 3, 7, SimpleTopRoom.field_175826_b, SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 0, 6, 3, 0, SimpleTopRoom.field_175826_b, SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 7, 6, 3, 7, SimpleTopRoom.field_175826_b, SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 0, 2, 4, SimpleTopRoom.field_175827_c, SimpleTopRoom.field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 1, 3, 7, 2, 4, SimpleTopRoom.field_175827_c, SimpleTopRoom.field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, SimpleTopRoom.field_175827_c, SimpleTopRoom.field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 7, 4, 2, 7, SimpleTopRoom.field_175827_c, SimpleTopRoom.field_175827_c, false);
            if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, SimpleTopRoom.field_175822_f, SimpleTopRoom.field_175822_f, false);
            }
            return true;
        }
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_175971_a;
        private static final String __OBFID = "CL_00001993";
        
        static {
            field_175971_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_175971_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_175971_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_175971_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
        }
    }
    
    public static class WingRoom extends Piece
    {
        private int field_175834_o;
        private static final String __OBFID = "CL_00001973";
        
        public WingRoom() {
        }
        
        public WingRoom(final EnumFacing p_i45585_1_, final StructureBoundingBox p_i45585_2_, final int p_i45585_3_) {
            super(p_i45585_1_, p_i45585_2_);
            this.field_175834_o = (p_i45585_3_ & 0x1);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.field_175834_o == 0) {
                for (int var4 = 0; var4 < 4; ++var4) {
                    this.func_175804_a(worldIn, p_74875_3_, 10 - var4, 3 - var4, 20 - var4, 12 + var4, 3 - var4, 20, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                }
                this.func_175804_a(worldIn, p_74875_3_, 7, 0, 6, 15, 0, 16, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, 0, 6, 6, 3, 20, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 16, 0, 6, 16, 3, 20, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 7, 7, 1, 20, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 15, 1, 7, 15, 1, 20, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 6, 9, 3, 6, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 13, 1, 6, 15, 3, 6, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 8, 1, 7, 9, 1, 7, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 13, 1, 7, 14, 1, 7, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 9, 0, 5, 13, 0, 5, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 10, 0, 7, 12, 0, 7, WingRoom.field_175827_c, WingRoom.field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 8, 0, 10, 8, 0, 12, WingRoom.field_175827_c, WingRoom.field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 14, 0, 10, 14, 0, 12, WingRoom.field_175827_c, WingRoom.field_175827_c, false);
                for (int var4 = 18; var4 >= 7; var4 -= 3) {
                    this.func_175811_a(worldIn, WingRoom.field_175825_e, 6, 3, var4, p_74875_3_);
                    this.func_175811_a(worldIn, WingRoom.field_175825_e, 16, 3, var4, p_74875_3_);
                }
                this.func_175811_a(worldIn, WingRoom.field_175825_e, 10, 0, 10, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175825_e, 12, 0, 10, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175825_e, 10, 0, 12, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175825_e, 12, 0, 12, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175825_e, 8, 3, 6, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175825_e, 14, 3, 6, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175826_b, 4, 2, 4, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175825_e, 4, 1, 4, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175826_b, 4, 0, 4, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175826_b, 18, 2, 4, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175825_e, 18, 1, 4, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175826_b, 18, 0, 4, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175826_b, 4, 2, 18, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175825_e, 4, 1, 18, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175826_b, 4, 0, 18, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175826_b, 18, 2, 18, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175825_e, 18, 1, 18, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175826_b, 18, 0, 18, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175826_b, 9, 7, 20, p_74875_3_);
                this.func_175811_a(worldIn, WingRoom.field_175826_b, 13, 7, 20, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 6, 0, 21, 7, 4, 21, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 15, 0, 21, 16, 4, 21, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                this.func_175817_a(worldIn, p_74875_3_, 11, 2, 16);
            }
            else if (this.field_175834_o == 1) {
                this.func_175804_a(worldIn, p_74875_3_, 9, 3, 18, 13, 3, 20, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 9, 0, 18, 9, 2, 18, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 13, 0, 18, 13, 2, 18, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                byte var5 = 9;
                final byte var6 = 20;
                final byte var7 = 5;
                for (int var8 = 0; var8 < 2; ++var8) {
                    this.func_175811_a(worldIn, WingRoom.field_175826_b, var5, var7 + 1, var6, p_74875_3_);
                    this.func_175811_a(worldIn, WingRoom.field_175825_e, var5, var7, var6, p_74875_3_);
                    this.func_175811_a(worldIn, WingRoom.field_175826_b, var5, var7 - 1, var6, p_74875_3_);
                    var5 = 13;
                }
                this.func_175804_a(worldIn, p_74875_3_, 7, 3, 7, 15, 3, 14, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                var5 = 10;
                for (int var8 = 0; var8 < 2; ++var8) {
                    this.func_175804_a(worldIn, p_74875_3_, var5, 0, 10, var5, 6, 10, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var5, 0, 12, var5, 6, 12, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                    this.func_175811_a(worldIn, WingRoom.field_175825_e, var5, 0, 10, p_74875_3_);
                    this.func_175811_a(worldIn, WingRoom.field_175825_e, var5, 0, 12, p_74875_3_);
                    this.func_175811_a(worldIn, WingRoom.field_175825_e, var5, 4, 10, p_74875_3_);
                    this.func_175811_a(worldIn, WingRoom.field_175825_e, var5, 4, 12, p_74875_3_);
                    var5 = 12;
                }
                var5 = 8;
                for (int var8 = 0; var8 < 2; ++var8) {
                    this.func_175804_a(worldIn, p_74875_3_, var5, 0, 7, var5, 2, 7, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var5, 0, 14, var5, 2, 14, WingRoom.field_175826_b, WingRoom.field_175826_b, false);
                    var5 = 14;
                }
                this.func_175804_a(worldIn, p_74875_3_, 8, 3, 8, 8, 3, 13, WingRoom.field_175827_c, WingRoom.field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 14, 3, 8, 14, 3, 13, WingRoom.field_175827_c, WingRoom.field_175827_c, false);
                this.func_175817_a(worldIn, p_74875_3_, 11, 5, 13);
            }
            return true;
        }
    }
    
    static class XDoubleRoomFitHelper implements MonumentRoomFitHelper
    {
        private static final String __OBFID = "CL_00001992";
        
        private XDoubleRoomFitHelper() {
        }
        
        @Override
        public boolean func_175969_a(final RoomDefinition p_175969_1_) {
            return p_175969_1_.field_175966_c[EnumFacing.EAST.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d;
        }
        
        @Override
        public Piece func_175968_a(final EnumFacing p_175968_1_, final RoomDefinition p_175968_2_, final Random p_175968_3_) {
            p_175968_2_.field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d = true;
            return new DoubleXRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }
        
        XDoubleRoomFitHelper(final SwitchEnumFacing p_i45606_1_) {
            this();
        }
    }
    
    static class XYDoubleRoomFitHelper implements MonumentRoomFitHelper
    {
        private static final String __OBFID = "CL_00001991";
        
        private XYDoubleRoomFitHelper() {
        }
        
        @Override
        public boolean func_175969_a(final RoomDefinition p_175969_1_) {
            if (p_175969_1_.field_175966_c[EnumFacing.EAST.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d && p_175969_1_.field_175966_c[EnumFacing.UP.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d) {
                final RoomDefinition var2 = p_175969_1_.field_175965_b[EnumFacing.EAST.getIndex()];
                return var2.field_175966_c[EnumFacing.UP.getIndex()] && !var2.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d;
            }
            return false;
        }
        
        @Override
        public Piece func_175968_a(final EnumFacing p_175968_1_, final RoomDefinition p_175968_2_, final Random p_175968_3_) {
            p_175968_2_.field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            return new DoubleXYRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }
        
        XYDoubleRoomFitHelper(final SwitchEnumFacing p_i45605_1_) {
            this();
        }
    }
    
    static class YDoubleRoomFitHelper implements MonumentRoomFitHelper
    {
        private static final String __OBFID = "CL_00001990";
        
        private YDoubleRoomFitHelper() {
        }
        
        @Override
        public boolean func_175969_a(final RoomDefinition p_175969_1_) {
            return p_175969_1_.field_175966_c[EnumFacing.UP.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d;
        }
        
        @Override
        public Piece func_175968_a(final EnumFacing p_175968_1_, final RoomDefinition p_175968_2_, final Random p_175968_3_) {
            p_175968_2_.field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            return new DoubleYRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }
        
        YDoubleRoomFitHelper(final SwitchEnumFacing p_i45604_1_) {
            this();
        }
    }
    
    static class YZDoubleRoomFitHelper implements MonumentRoomFitHelper
    {
        private static final String __OBFID = "CL_00001989";
        
        private YZDoubleRoomFitHelper() {
        }
        
        @Override
        public boolean func_175969_a(final RoomDefinition p_175969_1_) {
            if (p_175969_1_.field_175966_c[EnumFacing.NORTH.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d && p_175969_1_.field_175966_c[EnumFacing.UP.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d) {
                final RoomDefinition var2 = p_175969_1_.field_175965_b[EnumFacing.NORTH.getIndex()];
                return var2.field_175966_c[EnumFacing.UP.getIndex()] && !var2.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d;
            }
            return false;
        }
        
        @Override
        public Piece func_175968_a(final EnumFacing p_175968_1_, final RoomDefinition p_175968_2_, final Random p_175968_3_) {
            p_175968_2_.field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            return new DoubleYZRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }
        
        YZDoubleRoomFitHelper(final SwitchEnumFacing p_i45603_1_) {
            this();
        }
    }
    
    static class ZDoubleRoomFitHelper implements MonumentRoomFitHelper
    {
        private static final String __OBFID = "CL_00001988";
        
        private ZDoubleRoomFitHelper() {
        }
        
        @Override
        public boolean func_175969_a(final RoomDefinition p_175969_1_) {
            return p_175969_1_.field_175966_c[EnumFacing.NORTH.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d;
        }
        
        @Override
        public Piece func_175968_a(final EnumFacing p_175968_1_, final RoomDefinition p_175968_2_, final Random p_175968_3_) {
            RoomDefinition var4 = p_175968_2_;
            if (!p_175968_2_.field_175966_c[EnumFacing.NORTH.getIndex()] || p_175968_2_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d) {
                var4 = p_175968_2_.field_175965_b[EnumFacing.SOUTH.getIndex()];
            }
            var4.field_175963_d = true;
            var4.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
            return new DoubleZRoom(p_175968_1_, var4, p_175968_3_);
        }
        
        ZDoubleRoomFitHelper(final SwitchEnumFacing p_i45602_1_) {
            this();
        }
    }
    
    interface MonumentRoomFitHelper
    {
        boolean func_175969_a(final RoomDefinition p0);
        
        Piece func_175968_a(final EnumFacing p0, final RoomDefinition p1, final Random p2);
    }
}
