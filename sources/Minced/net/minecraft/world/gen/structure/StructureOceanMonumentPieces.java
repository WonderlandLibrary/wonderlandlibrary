// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.block.BlockPrismarine;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Blocks;
import java.util.Collections;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.util.EnumFacing;

public class StructureOceanMonumentPieces
{
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
    
    public static class DoubleXRoom extends Piece
    {
        public DoubleXRoom() {
        }
        
        public DoubleXRoom(final EnumFacing p_i45597_1_, final RoomDefinition p_i45597_2_, final Random p_i45597_3_) {
            super(1, p_i45597_1_, p_i45597_2_, 2, 1, 1);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            final RoomDefinition structureoceanmonumentpieces$roomdefinition = this.roomDefinition.connections[EnumFacing.EAST.getIndex()];
            final RoomDefinition structureoceanmonumentpieces$roomdefinition2 = this.roomDefinition;
            if (this.roomDefinition.index / 25 > 0) {
                this.generateDefaultFloor(worldIn, structureBoundingBoxIn, 8, 0, structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.DOWN.getIndex()]);
                this.generateDefaultFloor(worldIn, structureBoundingBoxIn, 0, 0, structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.DOWN.getIndex()]);
            }
            if (structureoceanmonumentpieces$roomdefinition2.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(worldIn, structureBoundingBoxIn, 1, 4, 1, 7, 4, 6, DoubleXRoom.ROUGH_PRISMARINE);
            }
            if (structureoceanmonumentpieces$roomdefinition.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(worldIn, structureBoundingBoxIn, 8, 4, 1, 14, 4, 6, DoubleXRoom.ROUGH_PRISMARINE);
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 0, 3, 7, DoubleXRoom.BRICKS_PRISMARINE, DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 3, 0, 15, 3, 7, DoubleXRoom.BRICKS_PRISMARINE, DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 15, 3, 0, DoubleXRoom.BRICKS_PRISMARINE, DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 7, 14, 3, 7, DoubleXRoom.BRICKS_PRISMARINE, DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 2, 7, DoubleXRoom.ROUGH_PRISMARINE, DoubleXRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 2, 0, 15, 2, 7, DoubleXRoom.ROUGH_PRISMARINE, DoubleXRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 15, 2, 0, DoubleXRoom.ROUGH_PRISMARINE, DoubleXRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 7, 14, 2, 7, DoubleXRoom.ROUGH_PRISMARINE, DoubleXRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 7, DoubleXRoom.BRICKS_PRISMARINE, DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 1, 0, 15, 1, 7, DoubleXRoom.BRICKS_PRISMARINE, DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 15, 1, 0, DoubleXRoom.BRICKS_PRISMARINE, DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 14, 1, 7, DoubleXRoom.BRICKS_PRISMARINE, DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 0, 10, 1, 4, DoubleXRoom.BRICKS_PRISMARINE, DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 0, 9, 2, 3, DoubleXRoom.ROUGH_PRISMARINE, DoubleXRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 3, 0, 10, 3, 4, DoubleXRoom.BRICKS_PRISMARINE, DoubleXRoom.BRICKS_PRISMARINE, false);
            this.setBlockState(worldIn, DoubleXRoom.SEA_LANTERN, 6, 2, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, DoubleXRoom.SEA_LANTERN, 9, 2, 3, structureBoundingBoxIn);
            if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, false);
            }
            if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7, false);
            }
            if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, false);
            }
            if (structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 11, 1, 0, 12, 2, 0, false);
            }
            if (structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 11, 1, 7, 12, 2, 7, false);
            }
            if (structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 15, 1, 3, 15, 2, 4, false);
            }
            return true;
        }
    }
    
    public static class DoubleXYRoom extends Piece
    {
        public DoubleXYRoom() {
        }
        
        public DoubleXYRoom(final EnumFacing p_i45596_1_, final RoomDefinition p_i45596_2_, final Random p_i45596_3_) {
            super(1, p_i45596_1_, p_i45596_2_, 2, 2, 1);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            final RoomDefinition structureoceanmonumentpieces$roomdefinition = this.roomDefinition.connections[EnumFacing.EAST.getIndex()];
            final RoomDefinition structureoceanmonumentpieces$roomdefinition2 = this.roomDefinition;
            final RoomDefinition structureoceanmonumentpieces$roomdefinition3 = structureoceanmonumentpieces$roomdefinition2.connections[EnumFacing.UP.getIndex()];
            final RoomDefinition structureoceanmonumentpieces$roomdefinition4 = structureoceanmonumentpieces$roomdefinition.connections[EnumFacing.UP.getIndex()];
            if (this.roomDefinition.index / 25 > 0) {
                this.generateDefaultFloor(worldIn, structureBoundingBoxIn, 8, 0, structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.DOWN.getIndex()]);
                this.generateDefaultFloor(worldIn, structureBoundingBoxIn, 0, 0, structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.DOWN.getIndex()]);
            }
            if (structureoceanmonumentpieces$roomdefinition3.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(worldIn, structureBoundingBoxIn, 1, 8, 1, 7, 8, 6, DoubleXYRoom.ROUGH_PRISMARINE);
            }
            if (structureoceanmonumentpieces$roomdefinition4.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(worldIn, structureBoundingBoxIn, 8, 8, 1, 14, 8, 6, DoubleXYRoom.ROUGH_PRISMARINE);
            }
            for (int i = 1; i <= 7; ++i) {
                IBlockState iblockstate = DoubleXYRoom.BRICKS_PRISMARINE;
                if (i == 2 || i == 6) {
                    iblockstate = DoubleXYRoom.ROUGH_PRISMARINE;
                }
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i, 0, 0, i, 7, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, i, 0, 15, i, 7, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 0, 15, i, 0, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 7, 14, i, 7, iblockstate, iblockstate, false);
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 3, 2, 7, 4, DoubleXYRoom.BRICKS_PRISMARINE, DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 2, 4, 7, 2, DoubleXYRoom.BRICKS_PRISMARINE, DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 5, 4, 7, 5, DoubleXYRoom.BRICKS_PRISMARINE, DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 1, 3, 13, 7, 4, DoubleXYRoom.BRICKS_PRISMARINE, DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 1, 2, 12, 7, 2, DoubleXYRoom.BRICKS_PRISMARINE, DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 1, 5, 12, 7, 5, DoubleXYRoom.BRICKS_PRISMARINE, DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 3, 5, 3, 4, DoubleXYRoom.BRICKS_PRISMARINE, DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 3, 10, 3, 4, DoubleXYRoom.BRICKS_PRISMARINE, DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 7, 2, 10, 7, 5, DoubleXYRoom.BRICKS_PRISMARINE, DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 2, 5, 7, 2, DoubleXYRoom.BRICKS_PRISMARINE, DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 5, 2, 10, 7, 2, DoubleXYRoom.BRICKS_PRISMARINE, DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 5, 5, 7, 5, DoubleXYRoom.BRICKS_PRISMARINE, DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 5, 5, 10, 7, 5, DoubleXYRoom.BRICKS_PRISMARINE, DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.setBlockState(worldIn, DoubleXYRoom.BRICKS_PRISMARINE, 6, 6, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, DoubleXYRoom.BRICKS_PRISMARINE, 9, 6, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, DoubleXYRoom.BRICKS_PRISMARINE, 6, 6, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, DoubleXYRoom.BRICKS_PRISMARINE, 9, 6, 5, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 3, 6, 4, 4, DoubleXYRoom.BRICKS_PRISMARINE, DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 4, 3, 10, 4, 4, DoubleXYRoom.BRICKS_PRISMARINE, DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.setBlockState(worldIn, DoubleXYRoom.SEA_LANTERN, 5, 4, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, DoubleXYRoom.SEA_LANTERN, 5, 4, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, DoubleXYRoom.SEA_LANTERN, 10, 4, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, DoubleXYRoom.SEA_LANTERN, 10, 4, 5, structureBoundingBoxIn);
            if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, false);
            }
            if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7, false);
            }
            if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, false);
            }
            if (structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 11, 1, 0, 12, 2, 0, false);
            }
            if (structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 11, 1, 7, 12, 2, 7, false);
            }
            if (structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 15, 1, 3, 15, 2, 4, false);
            }
            if (structureoceanmonumentpieces$roomdefinition3.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 5, 0, 4, 6, 0, false);
            }
            if (structureoceanmonumentpieces$roomdefinition3.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 5, 7, 4, 6, 7, false);
            }
            if (structureoceanmonumentpieces$roomdefinition3.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 0, 5, 3, 0, 6, 4, false);
            }
            if (structureoceanmonumentpieces$roomdefinition4.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 11, 5, 0, 12, 6, 0, false);
            }
            if (structureoceanmonumentpieces$roomdefinition4.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 11, 5, 7, 12, 6, 7, false);
            }
            if (structureoceanmonumentpieces$roomdefinition4.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 15, 5, 3, 15, 6, 4, false);
            }
            return true;
        }
    }
    
    public static class DoubleYRoom extends Piece
    {
        public DoubleYRoom() {
        }
        
        public DoubleYRoom(final EnumFacing p_i45595_1_, final RoomDefinition p_i45595_2_, final Random p_i45595_3_) {
            super(1, p_i45595_1_, p_i45595_2_, 1, 2, 1);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.roomDefinition.index / 25 > 0) {
                this.generateDefaultFloor(worldIn, structureBoundingBoxIn, 0, 0, this.roomDefinition.hasOpening[EnumFacing.DOWN.getIndex()]);
            }
            final RoomDefinition structureoceanmonumentpieces$roomdefinition = this.roomDefinition.connections[EnumFacing.UP.getIndex()];
            if (structureoceanmonumentpieces$roomdefinition.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(worldIn, structureBoundingBoxIn, 1, 8, 1, 6, 8, 6, DoubleYRoom.ROUGH_PRISMARINE);
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 0, 0, 4, 7, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 0, 7, 4, 7, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 0, 6, 4, 0, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 7, 6, 4, 7, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 1, 2, 4, 2, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 2, 1, 4, 2, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 1, 5, 4, 2, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 2, 6, 4, 2, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 5, 2, 4, 6, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 5, 1, 4, 5, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 5, 5, 4, 6, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 5, 6, 4, 5, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
            RoomDefinition structureoceanmonumentpieces$roomdefinition2 = this.roomDefinition;
            for (int i = 1; i <= 5; i += 4) {
                int j = 0;
                if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, i, j, 2, i + 2, j, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, i, j, 5, i + 2, j, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, i + 2, j, 4, i + 2, j, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                }
                else {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i, j, 7, i + 2, j, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i + 1, j, 7, i + 1, j, DoubleYRoom.ROUGH_PRISMARINE, DoubleYRoom.ROUGH_PRISMARINE, false);
                }
                j = 7;
                if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.NORTH.getIndex()]) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, i, j, 2, i + 2, j, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, i, j, 5, i + 2, j, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, i + 2, j, 4, i + 2, j, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                }
                else {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i, j, 7, i + 2, j, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i + 1, j, 7, i + 1, j, DoubleYRoom.ROUGH_PRISMARINE, DoubleYRoom.ROUGH_PRISMARINE, false);
                }
                int k = 0;
                if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.WEST.getIndex()]) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 2, k, i + 2, 2, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 5, k, i + 2, 5, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i + 2, 3, k, i + 2, 4, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                }
                else {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 0, k, i + 2, 7, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i + 1, 0, k, i + 1, 7, DoubleYRoom.ROUGH_PRISMARINE, DoubleYRoom.ROUGH_PRISMARINE, false);
                }
                k = 7;
                if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.EAST.getIndex()]) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 2, k, i + 2, 2, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 5, k, i + 2, 5, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i + 2, 3, k, i + 2, 4, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                }
                else {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 0, k, i + 2, 7, DoubleYRoom.BRICKS_PRISMARINE, DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i + 1, 0, k, i + 1, 7, DoubleYRoom.ROUGH_PRISMARINE, DoubleYRoom.ROUGH_PRISMARINE, false);
                }
                structureoceanmonumentpieces$roomdefinition2 = structureoceanmonumentpieces$roomdefinition;
            }
            return true;
        }
    }
    
    public static class DoubleYZRoom extends Piece
    {
        public DoubleYZRoom() {
        }
        
        public DoubleYZRoom(final EnumFacing p_i45594_1_, final RoomDefinition p_i45594_2_, final Random p_i45594_3_) {
            super(1, p_i45594_1_, p_i45594_2_, 1, 2, 2);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            final RoomDefinition structureoceanmonumentpieces$roomdefinition = this.roomDefinition.connections[EnumFacing.NORTH.getIndex()];
            final RoomDefinition structureoceanmonumentpieces$roomdefinition2 = this.roomDefinition;
            final RoomDefinition structureoceanmonumentpieces$roomdefinition3 = structureoceanmonumentpieces$roomdefinition.connections[EnumFacing.UP.getIndex()];
            final RoomDefinition structureoceanmonumentpieces$roomdefinition4 = structureoceanmonumentpieces$roomdefinition2.connections[EnumFacing.UP.getIndex()];
            if (this.roomDefinition.index / 25 > 0) {
                this.generateDefaultFloor(worldIn, structureBoundingBoxIn, 0, 8, structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.DOWN.getIndex()]);
                this.generateDefaultFloor(worldIn, structureBoundingBoxIn, 0, 0, structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.DOWN.getIndex()]);
            }
            if (structureoceanmonumentpieces$roomdefinition4.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(worldIn, structureBoundingBoxIn, 1, 8, 1, 6, 8, 7, DoubleYZRoom.ROUGH_PRISMARINE);
            }
            if (structureoceanmonumentpieces$roomdefinition3.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(worldIn, structureBoundingBoxIn, 1, 8, 8, 6, 8, 14, DoubleYZRoom.ROUGH_PRISMARINE);
            }
            for (int i = 1; i <= 7; ++i) {
                IBlockState iblockstate = DoubleYZRoom.BRICKS_PRISMARINE;
                if (i == 2 || i == 6) {
                    iblockstate = DoubleYZRoom.ROUGH_PRISMARINE;
                }
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i, 0, 0, i, 15, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, i, 0, 7, i, 15, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 0, 6, i, 0, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 15, 6, i, 15, iblockstate, iblockstate, false);
            }
            for (int j = 1; j <= 7; ++j) {
                IBlockState iblockstate2 = DoubleYZRoom.DARK_PRISMARINE;
                if (j == 2 || j == 6) {
                    iblockstate2 = DoubleYZRoom.SEA_LANTERN;
                }
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, j, 7, 4, j, 8, iblockstate2, iblockstate2, false);
            }
            if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, false);
            }
            if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 7, 1, 3, 7, 2, 4, false);
            }
            if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, false);
            }
            if (structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 1, 15, 4, 2, 15, false);
            }
            if (structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 0, 1, 11, 0, 2, 12, false);
            }
            if (structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 7, 1, 11, 7, 2, 12, false);
            }
            if (structureoceanmonumentpieces$roomdefinition4.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 5, 0, 4, 6, 0, false);
            }
            if (structureoceanmonumentpieces$roomdefinition4.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 7, 5, 3, 7, 6, 4, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 2, 6, 4, 5, DoubleYZRoom.BRICKS_PRISMARINE, DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 2, 6, 3, 2, DoubleYZRoom.BRICKS_PRISMARINE, DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 5, 6, 3, 5, DoubleYZRoom.BRICKS_PRISMARINE, DoubleYZRoom.BRICKS_PRISMARINE, false);
            }
            if (structureoceanmonumentpieces$roomdefinition4.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 0, 5, 3, 0, 6, 4, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 2, 2, 4, 5, DoubleYZRoom.BRICKS_PRISMARINE, DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 2, 1, 3, 2, DoubleYZRoom.BRICKS_PRISMARINE, DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 5, 1, 3, 5, DoubleYZRoom.BRICKS_PRISMARINE, DoubleYZRoom.BRICKS_PRISMARINE, false);
            }
            if (structureoceanmonumentpieces$roomdefinition3.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 5, 15, 4, 6, 15, false);
            }
            if (structureoceanmonumentpieces$roomdefinition3.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 0, 5, 11, 0, 6, 12, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 10, 2, 4, 13, DoubleYZRoom.BRICKS_PRISMARINE, DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 10, 1, 3, 10, DoubleYZRoom.BRICKS_PRISMARINE, DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 13, 1, 3, 13, DoubleYZRoom.BRICKS_PRISMARINE, DoubleYZRoom.BRICKS_PRISMARINE, false);
            }
            if (structureoceanmonumentpieces$roomdefinition3.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 7, 5, 11, 7, 6, 12, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 10, 6, 4, 13, DoubleYZRoom.BRICKS_PRISMARINE, DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 10, 6, 3, 10, DoubleYZRoom.BRICKS_PRISMARINE, DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 13, 6, 3, 13, DoubleYZRoom.BRICKS_PRISMARINE, DoubleYZRoom.BRICKS_PRISMARINE, false);
            }
            return true;
        }
    }
    
    public static class DoubleZRoom extends Piece
    {
        public DoubleZRoom() {
        }
        
        public DoubleZRoom(final EnumFacing p_i45593_1_, final RoomDefinition p_i45593_2_, final Random p_i45593_3_) {
            super(1, p_i45593_1_, p_i45593_2_, 1, 1, 2);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            final RoomDefinition structureoceanmonumentpieces$roomdefinition = this.roomDefinition.connections[EnumFacing.NORTH.getIndex()];
            final RoomDefinition structureoceanmonumentpieces$roomdefinition2 = this.roomDefinition;
            if (this.roomDefinition.index / 25 > 0) {
                this.generateDefaultFloor(worldIn, structureBoundingBoxIn, 0, 8, structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.DOWN.getIndex()]);
                this.generateDefaultFloor(worldIn, structureBoundingBoxIn, 0, 0, structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.DOWN.getIndex()]);
            }
            if (structureoceanmonumentpieces$roomdefinition2.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(worldIn, structureBoundingBoxIn, 1, 4, 1, 6, 4, 7, DoubleZRoom.ROUGH_PRISMARINE);
            }
            if (structureoceanmonumentpieces$roomdefinition.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(worldIn, structureBoundingBoxIn, 1, 4, 8, 6, 4, 14, DoubleZRoom.ROUGH_PRISMARINE);
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 0, 3, 15, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 0, 7, 3, 15, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 7, 3, 0, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 15, 6, 3, 15, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 2, 15, DoubleZRoom.ROUGH_PRISMARINE, DoubleZRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 0, 7, 2, 15, DoubleZRoom.ROUGH_PRISMARINE, DoubleZRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 7, 2, 0, DoubleZRoom.ROUGH_PRISMARINE, DoubleZRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 15, 6, 2, 15, DoubleZRoom.ROUGH_PRISMARINE, DoubleZRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 15, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 0, 7, 1, 15, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 7, 1, 0, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 15, 6, 1, 15, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 1, 1, 2, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 1, 6, 1, 2, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 1, 1, 3, 2, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 1, 6, 3, 2, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 13, 1, 1, 14, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 13, 6, 1, 14, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 13, 1, 3, 14, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 13, 6, 3, 14, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 6, 2, 3, 6, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 6, 5, 3, 6, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 9, 2, 3, 9, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 9, 5, 3, 9, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 6, 4, 2, 6, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 9, 4, 2, 9, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 7, 2, 2, 8, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 7, 5, 2, 8, DoubleZRoom.BRICKS_PRISMARINE, DoubleZRoom.BRICKS_PRISMARINE, false);
            this.setBlockState(worldIn, DoubleZRoom.SEA_LANTERN, 2, 2, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, DoubleZRoom.SEA_LANTERN, 5, 2, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, DoubleZRoom.SEA_LANTERN, 2, 2, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, DoubleZRoom.SEA_LANTERN, 5, 2, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, DoubleZRoom.BRICKS_PRISMARINE, 2, 3, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, DoubleZRoom.BRICKS_PRISMARINE, 5, 3, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, DoubleZRoom.BRICKS_PRISMARINE, 2, 3, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, DoubleZRoom.BRICKS_PRISMARINE, 5, 3, 10, structureBoundingBoxIn);
            if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, false);
            }
            if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 7, 1, 3, 7, 2, 4, false);
            }
            if (structureoceanmonumentpieces$roomdefinition2.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, false);
            }
            if (structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 1, 15, 4, 2, 15, false);
            }
            if (structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 0, 1, 11, 0, 2, 12, false);
            }
            if (structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 7, 1, 11, 7, 2, 12, false);
            }
            return true;
        }
    }
    
    public static class EntryRoom extends Piece
    {
        public EntryRoom() {
        }
        
        public EntryRoom(final EnumFacing p_i45592_1_, final RoomDefinition p_i45592_2_) {
            super(1, p_i45592_1_, p_i45592_2_, 1, 1, 1);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 2, 3, 7, EntryRoom.BRICKS_PRISMARINE, EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 3, 0, 7, 3, 7, EntryRoom.BRICKS_PRISMARINE, EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 1, 2, 7, EntryRoom.BRICKS_PRISMARINE, EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 0, 7, 2, 7, EntryRoom.BRICKS_PRISMARINE, EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 7, EntryRoom.BRICKS_PRISMARINE, EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 0, 7, 1, 7, EntryRoom.BRICKS_PRISMARINE, EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 7, 7, 3, 7, EntryRoom.BRICKS_PRISMARINE, EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 2, 3, 0, EntryRoom.BRICKS_PRISMARINE, EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 0, 6, 3, 0, EntryRoom.BRICKS_PRISMARINE, EntryRoom.BRICKS_PRISMARINE, false);
            if (this.roomDefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7, false);
            }
            if (this.roomDefinition.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 0, 1, 3, 1, 2, 4, false);
            }
            if (this.roomDefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 6, 1, 3, 7, 2, 4, false);
            }
            return true;
        }
    }
    
    static class FitSimpleRoomHelper implements MonumentRoomFitHelper
    {
        private FitSimpleRoomHelper() {
        }
        
        @Override
        public boolean fits(final RoomDefinition definition) {
            return true;
        }
        
        @Override
        public Piece create(final EnumFacing p_175968_1_, final RoomDefinition p_175968_2_, final Random p_175968_3_) {
            p_175968_2_.claimed = true;
            return new SimpleRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }
    }
    
    static class FitSimpleRoomTopHelper implements MonumentRoomFitHelper
    {
        private FitSimpleRoomTopHelper() {
        }
        
        @Override
        public boolean fits(final RoomDefinition definition) {
            return !definition.hasOpening[EnumFacing.WEST.getIndex()] && !definition.hasOpening[EnumFacing.EAST.getIndex()] && !definition.hasOpening[EnumFacing.NORTH.getIndex()] && !definition.hasOpening[EnumFacing.SOUTH.getIndex()] && !definition.hasOpening[EnumFacing.UP.getIndex()];
        }
        
        @Override
        public Piece create(final EnumFacing p_175968_1_, final RoomDefinition p_175968_2_, final Random p_175968_3_) {
            p_175968_2_.claimed = true;
            return new SimpleTopRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }
    }
    
    public static class MonumentBuilding extends Piece
    {
        private RoomDefinition sourceRoom;
        private RoomDefinition coreRoom;
        private final List<Piece> childPieces;
        
        public MonumentBuilding() {
            this.childPieces = (List<Piece>)Lists.newArrayList();
        }
        
        public MonumentBuilding(final Random p_i45599_1_, final int p_i45599_2_, final int p_i45599_3_, final EnumFacing p_i45599_4_) {
            super(0);
            this.childPieces = (List<Piece>)Lists.newArrayList();
            this.setCoordBaseMode(p_i45599_4_);
            final EnumFacing enumfacing = this.getCoordBaseMode();
            if (enumfacing.getAxis() == EnumFacing.Axis.Z) {
                this.boundingBox = new StructureBoundingBox(p_i45599_2_, 39, p_i45599_3_, p_i45599_2_ + 58 - 1, 61, p_i45599_3_ + 58 - 1);
            }
            else {
                this.boundingBox = new StructureBoundingBox(p_i45599_2_, 39, p_i45599_3_, p_i45599_2_ + 58 - 1, 61, p_i45599_3_ + 58 - 1);
            }
            final List<RoomDefinition> list = this.generateRoomGraph(p_i45599_1_);
            this.sourceRoom.claimed = true;
            this.childPieces.add(new EntryRoom(enumfacing, this.sourceRoom));
            this.childPieces.add(new MonumentCoreRoom(enumfacing, this.coreRoom, p_i45599_1_));
            final List<MonumentRoomFitHelper> list2 = (List<MonumentRoomFitHelper>)Lists.newArrayList();
            list2.add(new XYDoubleRoomFitHelper());
            list2.add(new YZDoubleRoomFitHelper());
            list2.add(new ZDoubleRoomFitHelper());
            list2.add(new XDoubleRoomFitHelper());
            list2.add(new YDoubleRoomFitHelper());
            list2.add(new FitSimpleRoomTopHelper());
            list2.add(new FitSimpleRoomHelper());
            for (final RoomDefinition structureoceanmonumentpieces$roomdefinition : list) {
                if (!structureoceanmonumentpieces$roomdefinition.claimed && !structureoceanmonumentpieces$roomdefinition.isSpecial()) {
                    for (final MonumentRoomFitHelper structureoceanmonumentpieces$monumentroomfithelper : list2) {
                        if (structureoceanmonumentpieces$monumentroomfithelper.fits(structureoceanmonumentpieces$roomdefinition)) {
                            this.childPieces.add(structureoceanmonumentpieces$monumentroomfithelper.create(enumfacing, structureoceanmonumentpieces$roomdefinition, p_i45599_1_));
                            break;
                        }
                    }
                }
            }
            final int j = this.boundingBox.minY;
            final int k = this.getXWithOffset(9, 22);
            final int l = this.getZWithOffset(9, 22);
            for (final Piece structureoceanmonumentpieces$piece : this.childPieces) {
                structureoceanmonumentpieces$piece.getBoundingBox().offset(k, j, l);
            }
            final StructureBoundingBox structureboundingbox1 = StructureBoundingBox.createProper(this.getXWithOffset(1, 1), this.getYWithOffset(1), this.getZWithOffset(1, 1), this.getXWithOffset(23, 21), this.getYWithOffset(8), this.getZWithOffset(23, 21));
            final StructureBoundingBox structureboundingbox2 = StructureBoundingBox.createProper(this.getXWithOffset(34, 1), this.getYWithOffset(1), this.getZWithOffset(34, 1), this.getXWithOffset(56, 21), this.getYWithOffset(8), this.getZWithOffset(56, 21));
            final StructureBoundingBox structureboundingbox3 = StructureBoundingBox.createProper(this.getXWithOffset(22, 22), this.getYWithOffset(13), this.getZWithOffset(22, 22), this.getXWithOffset(35, 35), this.getYWithOffset(17), this.getZWithOffset(35, 35));
            int i = p_i45599_1_.nextInt();
            this.childPieces.add(new WingRoom(enumfacing, structureboundingbox1, i++));
            this.childPieces.add(new WingRoom(enumfacing, structureboundingbox2, i++));
            this.childPieces.add(new Penthouse(enumfacing, structureboundingbox3));
        }
        
        private List<RoomDefinition> generateRoomGraph(final Random p_175836_1_) {
            final RoomDefinition[] astructureoceanmonumentpieces$roomdefinition = new RoomDefinition[75];
            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 4; ++j) {
                    final int k = 0;
                    final int l = Piece.getRoomIndex(i, 0, j);
                    astructureoceanmonumentpieces$roomdefinition[l] = new RoomDefinition(l);
                }
            }
            for (int i2 = 0; i2 < 5; ++i2) {
                for (int l2 = 0; l2 < 4; ++l2) {
                    final int k2 = 1;
                    final int j2 = Piece.getRoomIndex(i2, 1, l2);
                    astructureoceanmonumentpieces$roomdefinition[j2] = new RoomDefinition(j2);
                }
            }
            for (int j3 = 1; j3 < 4; ++j3) {
                for (int i3 = 0; i3 < 2; ++i3) {
                    final int l3 = 2;
                    final int k3 = Piece.getRoomIndex(j3, 2, i3);
                    astructureoceanmonumentpieces$roomdefinition[k3] = new RoomDefinition(k3);
                }
            }
            this.sourceRoom = astructureoceanmonumentpieces$roomdefinition[MonumentBuilding.GRIDROOM_SOURCE_INDEX];
            for (int k4 = 0; k4 < 5; ++k4) {
                for (int j4 = 0; j4 < 5; ++j4) {
                    for (int i4 = 0; i4 < 3; ++i4) {
                        final int l4 = Piece.getRoomIndex(k4, i4, j4);
                        if (astructureoceanmonumentpieces$roomdefinition[l4] != null) {
                            for (final EnumFacing enumfacing : EnumFacing.values()) {
                                final int i5 = k4 + enumfacing.getXOffset();
                                final int j5 = i4 + enumfacing.getYOffset();
                                final int k5 = j4 + enumfacing.getZOffset();
                                if (i5 >= 0 && i5 < 5 && k5 >= 0 && k5 < 5 && j5 >= 0 && j5 < 3) {
                                    final int l5 = Piece.getRoomIndex(i5, j5, k5);
                                    if (astructureoceanmonumentpieces$roomdefinition[l5] != null) {
                                        if (k5 == j4) {
                                            astructureoceanmonumentpieces$roomdefinition[l4].setConnection(enumfacing, astructureoceanmonumentpieces$roomdefinition[l5]);
                                        }
                                        else {
                                            astructureoceanmonumentpieces$roomdefinition[l4].setConnection(enumfacing.getOpposite(), astructureoceanmonumentpieces$roomdefinition[l5]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            final RoomDefinition structureoceanmonumentpieces$roomdefinition = new RoomDefinition(1003);
            final RoomDefinition structureoceanmonumentpieces$roomdefinition2 = new RoomDefinition(1001);
            final RoomDefinition structureoceanmonumentpieces$roomdefinition3 = new RoomDefinition(1002);
            astructureoceanmonumentpieces$roomdefinition[MonumentBuilding.GRIDROOM_TOP_CONNECT_INDEX].setConnection(EnumFacing.UP, structureoceanmonumentpieces$roomdefinition);
            astructureoceanmonumentpieces$roomdefinition[MonumentBuilding.GRIDROOM_LEFTWING_CONNECT_INDEX].setConnection(EnumFacing.SOUTH, structureoceanmonumentpieces$roomdefinition2);
            astructureoceanmonumentpieces$roomdefinition[MonumentBuilding.GRIDROOM_RIGHTWING_CONNECT_INDEX].setConnection(EnumFacing.SOUTH, structureoceanmonumentpieces$roomdefinition3);
            structureoceanmonumentpieces$roomdefinition.claimed = true;
            structureoceanmonumentpieces$roomdefinition2.claimed = true;
            structureoceanmonumentpieces$roomdefinition3.claimed = true;
            this.sourceRoom.isSource = true;
            this.coreRoom = astructureoceanmonumentpieces$roomdefinition[Piece.getRoomIndex(p_175836_1_.nextInt(4), 0, 2)];
            this.coreRoom.claimed = true;
            this.coreRoom.connections[EnumFacing.EAST.getIndex()].claimed = true;
            this.coreRoom.connections[EnumFacing.NORTH.getIndex()].claimed = true;
            this.coreRoom.connections[EnumFacing.EAST.getIndex()].connections[EnumFacing.NORTH.getIndex()].claimed = true;
            this.coreRoom.connections[EnumFacing.UP.getIndex()].claimed = true;
            this.coreRoom.connections[EnumFacing.EAST.getIndex()].connections[EnumFacing.UP.getIndex()].claimed = true;
            this.coreRoom.connections[EnumFacing.NORTH.getIndex()].connections[EnumFacing.UP.getIndex()].claimed = true;
            this.coreRoom.connections[EnumFacing.EAST.getIndex()].connections[EnumFacing.NORTH.getIndex()].connections[EnumFacing.UP.getIndex()].claimed = true;
            final List<RoomDefinition> list = (List<RoomDefinition>)Lists.newArrayList();
            for (final RoomDefinition structureoceanmonumentpieces$roomdefinition4 : astructureoceanmonumentpieces$roomdefinition) {
                if (structureoceanmonumentpieces$roomdefinition4 != null) {
                    structureoceanmonumentpieces$roomdefinition4.updateOpenings();
                    list.add(structureoceanmonumentpieces$roomdefinition4);
                }
            }
            structureoceanmonumentpieces$roomdefinition.updateOpenings();
            Collections.shuffle(list, p_175836_1_);
            int i6 = 1;
            for (final RoomDefinition structureoceanmonumentpieces$roomdefinition5 : list) {
                int j6 = 0;
                int k6 = 0;
                while (j6 < 2 && k6 < 5) {
                    ++k6;
                    final int l6 = p_175836_1_.nextInt(6);
                    if (structureoceanmonumentpieces$roomdefinition5.hasOpening[l6]) {
                        final int i7 = EnumFacing.byIndex(l6).getOpposite().getIndex();
                        structureoceanmonumentpieces$roomdefinition5.hasOpening[l6] = false;
                        structureoceanmonumentpieces$roomdefinition5.connections[l6].hasOpening[i7] = false;
                        if (structureoceanmonumentpieces$roomdefinition5.findSource(i6++) && structureoceanmonumentpieces$roomdefinition5.connections[l6].findSource(i6++)) {
                            ++j6;
                        }
                        else {
                            structureoceanmonumentpieces$roomdefinition5.hasOpening[l6] = true;
                            structureoceanmonumentpieces$roomdefinition5.connections[l6].hasOpening[i7] = true;
                        }
                    }
                }
            }
            list.add(structureoceanmonumentpieces$roomdefinition);
            list.add(structureoceanmonumentpieces$roomdefinition2);
            list.add(structureoceanmonumentpieces$roomdefinition3);
            return list;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            final int i = Math.max(worldIn.getSeaLevel(), 64) - this.boundingBox.minY;
            this.generateWaterBox(worldIn, structureBoundingBoxIn, 0, 0, 0, 58, i, 58, false);
            this.generateWing(false, 0, worldIn, randomIn, structureBoundingBoxIn);
            this.generateWing(true, 33, worldIn, randomIn, structureBoundingBoxIn);
            this.generateEntranceArchs(worldIn, randomIn, structureBoundingBoxIn);
            this.generateEntranceWall(worldIn, randomIn, structureBoundingBoxIn);
            this.generateRoofPiece(worldIn, randomIn, structureBoundingBoxIn);
            this.generateLowerWall(worldIn, randomIn, structureBoundingBoxIn);
            this.generateMiddleWall(worldIn, randomIn, structureBoundingBoxIn);
            this.generateUpperWall(worldIn, randomIn, structureBoundingBoxIn);
            for (int j = 0; j < 7; ++j) {
                int k = 0;
                while (k < 7) {
                    if (k == 0 && j == 3) {
                        k = 6;
                    }
                    final int l = j * 9;
                    final int i2 = k * 9;
                    for (int j2 = 0; j2 < 4; ++j2) {
                        for (int k2 = 0; k2 < 4; ++k2) {
                            this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, l + j2, 0, i2 + k2, structureBoundingBoxIn);
                            this.replaceAirAndLiquidDownwards(worldIn, MonumentBuilding.BRICKS_PRISMARINE, l + j2, -1, i2 + k2, structureBoundingBoxIn);
                        }
                    }
                    if (j != 0 && j != 6) {
                        k += 6;
                    }
                    else {
                        ++k;
                    }
                }
            }
            for (int l2 = 0; l2 < 5; ++l2) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, -1 - l2, 0 + l2 * 2, -1 - l2, -1 - l2, 23, 58 + l2, false);
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 58 + l2, 0 + l2 * 2, -1 - l2, 58 + l2, 23, 58 + l2, false);
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 0 - l2, 0 + l2 * 2, -1 - l2, 57 + l2, 23, -1 - l2, false);
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 0 - l2, 0 + l2 * 2, 58 + l2, 57 + l2, 23, 58 + l2, false);
            }
            for (final Piece structureoceanmonumentpieces$piece : this.childPieces) {
                if (structureoceanmonumentpieces$piece.getBoundingBox().intersectsWith(structureBoundingBoxIn)) {
                    structureoceanmonumentpieces$piece.addComponentParts(worldIn, randomIn, structureBoundingBoxIn);
                }
            }
            return true;
        }
        
        private void generateWing(final boolean p_175840_1_, final int p_175840_2_, final World worldIn, final Random p_175840_4_, final StructureBoundingBox p_175840_5_) {
            final int i = 24;
            if (this.doesChunkIntersect(p_175840_5_, p_175840_2_, 0, p_175840_2_ + 23, 20)) {
                this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 0, 0, 0, p_175840_2_ + 24, 0, 20, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(worldIn, p_175840_5_, p_175840_2_ + 0, 1, 0, p_175840_2_ + 24, 10, 20, false);
                for (int j = 0; j < 4; ++j) {
                    this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + j, j + 1, j, p_175840_2_ + j, j + 1, 20, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + j + 7, j + 5, j + 7, p_175840_2_ + j + 7, j + 5, 20, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 17 - j, j + 5, j + 7, p_175840_2_ + 17 - j, j + 5, 20, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 24 - j, j + 1, j, p_175840_2_ + 24 - j, j + 1, 20, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + j + 1, j + 1, j, p_175840_2_ + 23 - j, j + 1, j, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + j + 8, j + 5, j + 7, p_175840_2_ + 16 - j, j + 5, j + 7, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                }
                this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 4, 4, 4, p_175840_2_ + 6, 4, 20, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 7, 4, 4, p_175840_2_ + 17, 4, 6, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 18, 4, 4, p_175840_2_ + 20, 4, 20, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 11, 8, 11, p_175840_2_ + 13, 8, 20, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, p_175840_2_ + 12, 9, 12, p_175840_5_);
                this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, p_175840_2_ + 12, 9, 15, p_175840_5_);
                this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, p_175840_2_ + 12, 9, 18, p_175840_5_);
                final int j2 = p_175840_2_ + (p_175840_1_ ? 19 : 5);
                final int k = p_175840_2_ + (p_175840_1_ ? 5 : 19);
                for (int l = 20; l >= 5; l -= 3) {
                    this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, j2, 5, l, p_175840_5_);
                }
                for (int k2 = 19; k2 >= 7; k2 -= 3) {
                    this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, k, 5, k2, p_175840_5_);
                }
                for (int l2 = 0; l2 < 4; ++l2) {
                    final int i2 = p_175840_1_ ? (p_175840_2_ + (24 - (17 - l2 * 3))) : (p_175840_2_ + 17 - l2 * 3);
                    this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, i2, 5, 5, p_175840_5_);
                }
                this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, k, 5, 5, p_175840_5_);
                this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 11, 1, 12, p_175840_2_ + 13, 7, 12, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 12, 1, 11, p_175840_2_ + 12, 7, 13, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
            }
        }
        
        private void generateEntranceArchs(final World worldIn, final Random p_175839_2_, final StructureBoundingBox p_175839_3_) {
            if (this.doesChunkIntersect(p_175839_3_, 22, 5, 35, 17)) {
                this.generateWaterBox(worldIn, p_175839_3_, 25, 0, 0, 32, 8, 20, false);
                for (int i = 0; i < 4; ++i) {
                    this.fillWithBlocks(worldIn, p_175839_3_, 24, 2, 5 + i * 4, 24, 4, 5 + i * 4, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, p_175839_3_, 22, 4, 5 + i * 4, 23, 4, 5 + i * 4, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 25, 5, 5 + i * 4, p_175839_3_);
                    this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 26, 6, 5 + i * 4, p_175839_3_);
                    this.setBlockState(worldIn, MonumentBuilding.SEA_LANTERN, 26, 5, 5 + i * 4, p_175839_3_);
                    this.fillWithBlocks(worldIn, p_175839_3_, 33, 2, 5 + i * 4, 33, 4, 5 + i * 4, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, p_175839_3_, 34, 4, 5 + i * 4, 35, 4, 5 + i * 4, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 32, 5, 5 + i * 4, p_175839_3_);
                    this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 31, 6, 5 + i * 4, p_175839_3_);
                    this.setBlockState(worldIn, MonumentBuilding.SEA_LANTERN, 31, 5, 5 + i * 4, p_175839_3_);
                    this.fillWithBlocks(worldIn, p_175839_3_, 27, 6, 5 + i * 4, 30, 6, 5 + i * 4, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                }
            }
        }
        
        private void generateEntranceWall(final World worldIn, final Random p_175837_2_, final StructureBoundingBox p_175837_3_) {
            if (this.doesChunkIntersect(p_175837_3_, 15, 20, 42, 21)) {
                this.fillWithBlocks(worldIn, p_175837_3_, 15, 0, 21, 42, 0, 21, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(worldIn, p_175837_3_, 26, 1, 21, 31, 3, 21, false);
                this.fillWithBlocks(worldIn, p_175837_3_, 21, 12, 21, 36, 12, 21, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175837_3_, 17, 11, 21, 40, 11, 21, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175837_3_, 16, 10, 21, 41, 10, 21, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175837_3_, 15, 7, 21, 42, 9, 21, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175837_3_, 16, 6, 21, 41, 6, 21, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175837_3_, 17, 5, 21, 40, 5, 21, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175837_3_, 21, 4, 21, 36, 4, 21, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175837_3_, 22, 3, 21, 26, 3, 21, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175837_3_, 31, 3, 21, 35, 3, 21, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175837_3_, 23, 2, 21, 25, 2, 21, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175837_3_, 32, 2, 21, 34, 2, 21, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175837_3_, 28, 4, 20, 29, 4, 21, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 27, 3, 21, p_175837_3_);
                this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 30, 3, 21, p_175837_3_);
                this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 26, 2, 21, p_175837_3_);
                this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 31, 2, 21, p_175837_3_);
                this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 25, 1, 21, p_175837_3_);
                this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 32, 1, 21, p_175837_3_);
                for (int i = 0; i < 7; ++i) {
                    this.setBlockState(worldIn, MonumentBuilding.DARK_PRISMARINE, 28 - i, 6 + i, 21, p_175837_3_);
                    this.setBlockState(worldIn, MonumentBuilding.DARK_PRISMARINE, 29 + i, 6 + i, 21, p_175837_3_);
                }
                for (int j = 0; j < 4; ++j) {
                    this.setBlockState(worldIn, MonumentBuilding.DARK_PRISMARINE, 28 - j, 9 + j, 21, p_175837_3_);
                    this.setBlockState(worldIn, MonumentBuilding.DARK_PRISMARINE, 29 + j, 9 + j, 21, p_175837_3_);
                }
                this.setBlockState(worldIn, MonumentBuilding.DARK_PRISMARINE, 28, 12, 21, p_175837_3_);
                this.setBlockState(worldIn, MonumentBuilding.DARK_PRISMARINE, 29, 12, 21, p_175837_3_);
                for (int k = 0; k < 3; ++k) {
                    this.setBlockState(worldIn, MonumentBuilding.DARK_PRISMARINE, 22 - k * 2, 8, 21, p_175837_3_);
                    this.setBlockState(worldIn, MonumentBuilding.DARK_PRISMARINE, 22 - k * 2, 9, 21, p_175837_3_);
                    this.setBlockState(worldIn, MonumentBuilding.DARK_PRISMARINE, 35 + k * 2, 8, 21, p_175837_3_);
                    this.setBlockState(worldIn, MonumentBuilding.DARK_PRISMARINE, 35 + k * 2, 9, 21, p_175837_3_);
                }
                this.generateWaterBox(worldIn, p_175837_3_, 15, 13, 21, 42, 15, 21, false);
                this.generateWaterBox(worldIn, p_175837_3_, 15, 1, 21, 15, 6, 21, false);
                this.generateWaterBox(worldIn, p_175837_3_, 16, 1, 21, 16, 5, 21, false);
                this.generateWaterBox(worldIn, p_175837_3_, 17, 1, 21, 20, 4, 21, false);
                this.generateWaterBox(worldIn, p_175837_3_, 21, 1, 21, 21, 3, 21, false);
                this.generateWaterBox(worldIn, p_175837_3_, 22, 1, 21, 22, 2, 21, false);
                this.generateWaterBox(worldIn, p_175837_3_, 23, 1, 21, 24, 1, 21, false);
                this.generateWaterBox(worldIn, p_175837_3_, 42, 1, 21, 42, 6, 21, false);
                this.generateWaterBox(worldIn, p_175837_3_, 41, 1, 21, 41, 5, 21, false);
                this.generateWaterBox(worldIn, p_175837_3_, 37, 1, 21, 40, 4, 21, false);
                this.generateWaterBox(worldIn, p_175837_3_, 36, 1, 21, 36, 3, 21, false);
                this.generateWaterBox(worldIn, p_175837_3_, 33, 1, 21, 34, 1, 21, false);
                this.generateWaterBox(worldIn, p_175837_3_, 35, 1, 21, 35, 2, 21, false);
            }
        }
        
        private void generateRoofPiece(final World worldIn, final Random p_175841_2_, final StructureBoundingBox p_175841_3_) {
            if (this.doesChunkIntersect(p_175841_3_, 21, 21, 36, 36)) {
                this.fillWithBlocks(worldIn, p_175841_3_, 21, 0, 22, 36, 0, 36, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(worldIn, p_175841_3_, 21, 1, 22, 36, 23, 36, false);
                for (int i = 0; i < 4; ++i) {
                    this.fillWithBlocks(worldIn, p_175841_3_, 21 + i, 13 + i, 21 + i, 36 - i, 13 + i, 21 + i, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, p_175841_3_, 21 + i, 13 + i, 36 - i, 36 - i, 13 + i, 36 - i, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, p_175841_3_, 21 + i, 13 + i, 22 + i, 21 + i, 13 + i, 35 - i, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, p_175841_3_, 36 - i, 13 + i, 22 + i, 36 - i, 13 + i, 35 - i, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                }
                this.fillWithBlocks(worldIn, p_175841_3_, 25, 16, 25, 32, 16, 32, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175841_3_, 25, 17, 25, 25, 19, 25, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175841_3_, 32, 17, 25, 32, 19, 25, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175841_3_, 25, 17, 32, 25, 19, 32, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175841_3_, 32, 17, 32, 32, 19, 32, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 26, 20, 26, p_175841_3_);
                this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 27, 21, 27, p_175841_3_);
                this.setBlockState(worldIn, MonumentBuilding.SEA_LANTERN, 27, 20, 27, p_175841_3_);
                this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 26, 20, 31, p_175841_3_);
                this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 27, 21, 30, p_175841_3_);
                this.setBlockState(worldIn, MonumentBuilding.SEA_LANTERN, 27, 20, 30, p_175841_3_);
                this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 31, 20, 31, p_175841_3_);
                this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 30, 21, 30, p_175841_3_);
                this.setBlockState(worldIn, MonumentBuilding.SEA_LANTERN, 30, 20, 30, p_175841_3_);
                this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 31, 20, 26, p_175841_3_);
                this.setBlockState(worldIn, MonumentBuilding.BRICKS_PRISMARINE, 30, 21, 27, p_175841_3_);
                this.setBlockState(worldIn, MonumentBuilding.SEA_LANTERN, 30, 20, 27, p_175841_3_);
                this.fillWithBlocks(worldIn, p_175841_3_, 28, 21, 27, 29, 21, 27, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175841_3_, 27, 21, 28, 27, 21, 29, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175841_3_, 28, 21, 30, 29, 21, 30, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175841_3_, 30, 21, 28, 30, 21, 29, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
            }
        }
        
        private void generateLowerWall(final World worldIn, final Random p_175835_2_, final StructureBoundingBox p_175835_3_) {
            if (this.doesChunkIntersect(p_175835_3_, 0, 21, 6, 58)) {
                this.fillWithBlocks(worldIn, p_175835_3_, 0, 0, 21, 6, 0, 57, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(worldIn, p_175835_3_, 0, 1, 21, 6, 7, 57, false);
                this.fillWithBlocks(worldIn, p_175835_3_, 4, 4, 21, 6, 4, 53, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                for (int i = 0; i < 4; ++i) {
                    this.fillWithBlocks(worldIn, p_175835_3_, i, i + 1, 21, i, i + 1, 57 - i, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                }
                for (int j = 23; j < 53; j += 3) {
                    this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, 5, 5, j, p_175835_3_);
                }
                this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, 5, 5, 52, p_175835_3_);
                for (int k = 0; k < 4; ++k) {
                    this.fillWithBlocks(worldIn, p_175835_3_, k, k + 1, 21, k, k + 1, 57 - k, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                }
                this.fillWithBlocks(worldIn, p_175835_3_, 4, 1, 52, 6, 3, 52, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175835_3_, 5, 1, 51, 5, 3, 53, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
            }
            if (this.doesChunkIntersect(p_175835_3_, 51, 21, 58, 58)) {
                this.fillWithBlocks(worldIn, p_175835_3_, 51, 0, 21, 57, 0, 57, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(worldIn, p_175835_3_, 51, 1, 21, 57, 7, 57, false);
                this.fillWithBlocks(worldIn, p_175835_3_, 51, 4, 21, 53, 4, 53, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                for (int l = 0; l < 4; ++l) {
                    this.fillWithBlocks(worldIn, p_175835_3_, 57 - l, l + 1, 21, 57 - l, l + 1, 57 - l, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                }
                for (int i2 = 23; i2 < 53; i2 += 3) {
                    this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, 52, 5, i2, p_175835_3_);
                }
                this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, 52, 5, 52, p_175835_3_);
                this.fillWithBlocks(worldIn, p_175835_3_, 51, 1, 52, 53, 3, 52, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175835_3_, 52, 1, 51, 52, 3, 53, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
            }
            if (this.doesChunkIntersect(p_175835_3_, 0, 51, 57, 57)) {
                this.fillWithBlocks(worldIn, p_175835_3_, 7, 0, 51, 50, 0, 57, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(worldIn, p_175835_3_, 7, 1, 51, 50, 10, 57, false);
                for (int j2 = 0; j2 < 4; ++j2) {
                    this.fillWithBlocks(worldIn, p_175835_3_, j2 + 1, j2 + 1, 57 - j2, 56 - j2, j2 + 1, 57 - j2, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                }
            }
        }
        
        private void generateMiddleWall(final World worldIn, final Random p_175842_2_, final StructureBoundingBox p_175842_3_) {
            if (this.doesChunkIntersect(p_175842_3_, 7, 21, 13, 50)) {
                this.fillWithBlocks(worldIn, p_175842_3_, 7, 0, 21, 13, 0, 50, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(worldIn, p_175842_3_, 7, 1, 21, 13, 10, 50, false);
                this.fillWithBlocks(worldIn, p_175842_3_, 11, 8, 21, 13, 8, 53, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                for (int i = 0; i < 4; ++i) {
                    this.fillWithBlocks(worldIn, p_175842_3_, i + 7, i + 5, 21, i + 7, i + 5, 54, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                }
                for (int j = 21; j <= 45; j += 3) {
                    this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, 12, 9, j, p_175842_3_);
                }
            }
            if (this.doesChunkIntersect(p_175842_3_, 44, 21, 50, 54)) {
                this.fillWithBlocks(worldIn, p_175842_3_, 44, 0, 21, 50, 0, 50, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(worldIn, p_175842_3_, 44, 1, 21, 50, 10, 50, false);
                this.fillWithBlocks(worldIn, p_175842_3_, 44, 8, 21, 46, 8, 53, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                for (int k = 0; k < 4; ++k) {
                    this.fillWithBlocks(worldIn, p_175842_3_, 50 - k, k + 5, 21, 50 - k, k + 5, 54, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                }
                for (int l = 21; l <= 45; l += 3) {
                    this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, 45, 9, l, p_175842_3_);
                }
            }
            if (this.doesChunkIntersect(p_175842_3_, 8, 44, 49, 54)) {
                this.fillWithBlocks(worldIn, p_175842_3_, 14, 0, 44, 43, 0, 50, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(worldIn, p_175842_3_, 14, 1, 44, 43, 10, 50, false);
                for (int i2 = 12; i2 <= 45; i2 += 3) {
                    this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, i2, 9, 45, p_175842_3_);
                    this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, i2, 9, 52, p_175842_3_);
                    if (i2 == 12 || i2 == 18 || i2 == 24 || i2 == 33 || i2 == 39 || i2 == 45) {
                        this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, i2, 9, 47, p_175842_3_);
                        this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, i2, 9, 50, p_175842_3_);
                        this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, i2, 10, 45, p_175842_3_);
                        this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, i2, 10, 46, p_175842_3_);
                        this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, i2, 10, 51, p_175842_3_);
                        this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, i2, 10, 52, p_175842_3_);
                        this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, i2, 11, 47, p_175842_3_);
                        this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, i2, 11, 50, p_175842_3_);
                        this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, i2, 12, 48, p_175842_3_);
                        this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, i2, 12, 49, p_175842_3_);
                    }
                }
                for (int j2 = 0; j2 < 3; ++j2) {
                    this.fillWithBlocks(worldIn, p_175842_3_, 8 + j2, 5 + j2, 54, 49 - j2, 5 + j2, 54, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                }
                this.fillWithBlocks(worldIn, p_175842_3_, 11, 8, 54, 46, 8, 54, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175842_3_, 14, 8, 44, 43, 8, 53, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
            }
        }
        
        private void generateUpperWall(final World worldIn, final Random p_175838_2_, final StructureBoundingBox p_175838_3_) {
            if (this.doesChunkIntersect(p_175838_3_, 14, 21, 20, 43)) {
                this.fillWithBlocks(worldIn, p_175838_3_, 14, 0, 21, 20, 0, 43, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(worldIn, p_175838_3_, 14, 1, 22, 20, 14, 43, false);
                this.fillWithBlocks(worldIn, p_175838_3_, 18, 12, 22, 20, 12, 39, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175838_3_, 18, 12, 21, 20, 12, 21, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                for (int i = 0; i < 4; ++i) {
                    this.fillWithBlocks(worldIn, p_175838_3_, i + 14, i + 9, 21, i + 14, i + 9, 43 - i, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                }
                for (int j = 23; j <= 39; j += 3) {
                    this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, 19, 13, j, p_175838_3_);
                }
            }
            if (this.doesChunkIntersect(p_175838_3_, 37, 21, 43, 43)) {
                this.fillWithBlocks(worldIn, p_175838_3_, 37, 0, 21, 43, 0, 43, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(worldIn, p_175838_3_, 37, 1, 22, 43, 14, 43, false);
                this.fillWithBlocks(worldIn, p_175838_3_, 37, 12, 22, 39, 12, 39, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175838_3_, 37, 12, 21, 39, 12, 21, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                for (int k = 0; k < 4; ++k) {
                    this.fillWithBlocks(worldIn, p_175838_3_, 43 - k, k + 9, 21, 43 - k, k + 9, 43 - k, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                }
                for (int l = 23; l <= 39; l += 3) {
                    this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, 38, 13, l, p_175838_3_);
                }
            }
            if (this.doesChunkIntersect(p_175838_3_, 15, 37, 42, 43)) {
                this.fillWithBlocks(worldIn, p_175838_3_, 21, 0, 37, 36, 0, 43, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(worldIn, p_175838_3_, 21, 1, 37, 36, 14, 43, false);
                this.fillWithBlocks(worldIn, p_175838_3_, 21, 12, 37, 36, 12, 39, MonumentBuilding.ROUGH_PRISMARINE, MonumentBuilding.ROUGH_PRISMARINE, false);
                for (int i2 = 0; i2 < 4; ++i2) {
                    this.fillWithBlocks(worldIn, p_175838_3_, 15 + i2, i2 + 9, 43 - i2, 42 - i2, i2 + 9, 43 - i2, MonumentBuilding.BRICKS_PRISMARINE, MonumentBuilding.BRICKS_PRISMARINE, false);
                }
                for (int j2 = 21; j2 <= 36; j2 += 3) {
                    this.setBlockState(worldIn, MonumentBuilding.DOT_DECO_DATA, j2, 13, 38, p_175838_3_);
                }
            }
        }
    }
    
    public static class MonumentCoreRoom extends Piece
    {
        public MonumentCoreRoom() {
        }
        
        public MonumentCoreRoom(final EnumFacing p_i45598_1_, final RoomDefinition p_i45598_2_, final Random p_i45598_3_) {
            super(1, p_i45598_1_, p_i45598_2_, 2, 2, 2);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.generateBoxOnFillOnly(worldIn, structureBoundingBoxIn, 1, 8, 0, 14, 8, 14, MonumentCoreRoom.ROUGH_PRISMARINE);
            final int i = 7;
            IBlockState iblockstate = MonumentCoreRoom.BRICKS_PRISMARINE;
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 7, 0, 0, 7, 15, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 7, 0, 15, 7, 15, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, 0, 15, 7, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, 15, 14, 7, 15, iblockstate, iblockstate, false);
            for (int k = 1; k <= 6; ++k) {
                iblockstate = MonumentCoreRoom.BRICKS_PRISMARINE;
                if (k == 2 || k == 6) {
                    iblockstate = MonumentCoreRoom.ROUGH_PRISMARINE;
                }
                for (int j = 0; j <= 15; j += 15) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, j, k, 0, j, k, 1, iblockstate, iblockstate, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, j, k, 6, j, k, 9, iblockstate, iblockstate, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, j, k, 14, j, k, 15, iblockstate, iblockstate, false);
                }
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, k, 0, 1, k, 0, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, k, 0, 9, k, 0, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 14, k, 0, 14, k, 0, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, k, 15, 14, k, 15, iblockstate, iblockstate, false);
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 6, 9, 6, 9, MonumentCoreRoom.DARK_PRISMARINE, MonumentCoreRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 7, 8, 5, 8, Blocks.GOLD_BLOCK.getDefaultState(), Blocks.GOLD_BLOCK.getDefaultState(), false);
            for (int l = 3; l <= 6; l += 3) {
                for (int i2 = 6; i2 <= 9; i2 += 3) {
                    this.setBlockState(worldIn, MonumentCoreRoom.SEA_LANTERN, i2, l, 6, structureBoundingBoxIn);
                    this.setBlockState(worldIn, MonumentCoreRoom.SEA_LANTERN, i2, l, 9, structureBoundingBoxIn);
                }
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 6, 5, 2, 6, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 9, 5, 2, 9, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 6, 10, 2, 6, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 9, 10, 2, 9, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 5, 6, 2, 5, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 5, 9, 2, 5, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 10, 6, 2, 10, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 10, 9, 2, 10, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 5, 5, 6, 5, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 10, 5, 6, 10, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 2, 5, 10, 6, 5, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 2, 10, 10, 6, 10, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 7, 1, 5, 7, 6, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 7, 1, 10, 7, 6, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 7, 9, 5, 7, 14, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 7, 9, 10, 7, 14, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, 5, 6, 7, 5, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, 10, 6, 7, 10, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 7, 5, 14, 7, 5, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 7, 10, 14, 7, 10, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 2, 2, 1, 3, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 2, 3, 1, 2, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 1, 2, 13, 1, 3, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 2, 12, 1, 2, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 12, 2, 1, 13, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 13, 3, 1, 13, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 1, 12, 13, 1, 13, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 13, 12, 1, 13, MonumentCoreRoom.BRICKS_PRISMARINE, MonumentCoreRoom.BRICKS_PRISMARINE, false);
            return true;
        }
    }
    
    public static class Penthouse extends Piece
    {
        public Penthouse() {
        }
        
        public Penthouse(final EnumFacing p_i45591_1_, final StructureBoundingBox p_i45591_2_) {
            super(p_i45591_1_, p_i45591_2_);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 2, 11, -1, 11, Penthouse.BRICKS_PRISMARINE, Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, -1, 0, 1, -1, 11, Penthouse.ROUGH_PRISMARINE, Penthouse.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, -1, 0, 13, -1, 11, Penthouse.ROUGH_PRISMARINE, Penthouse.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 0, 11, -1, 1, Penthouse.ROUGH_PRISMARINE, Penthouse.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 12, 11, -1, 13, Penthouse.ROUGH_PRISMARINE, Penthouse.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 0, 13, Penthouse.BRICKS_PRISMARINE, Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 0, 0, 13, 0, 13, Penthouse.BRICKS_PRISMARINE, Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 12, 0, 0, Penthouse.BRICKS_PRISMARINE, Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 13, 12, 0, 13, Penthouse.BRICKS_PRISMARINE, Penthouse.BRICKS_PRISMARINE, false);
            for (int i = 2; i <= 11; i += 3) {
                this.setBlockState(worldIn, Penthouse.SEA_LANTERN, 0, 0, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, Penthouse.SEA_LANTERN, 13, 0, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, Penthouse.SEA_LANTERN, i, 0, 0, structureBoundingBoxIn);
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 3, 4, 0, 9, Penthouse.BRICKS_PRISMARINE, Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 3, 11, 0, 9, Penthouse.BRICKS_PRISMARINE, Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 9, 9, 0, 11, Penthouse.BRICKS_PRISMARINE, Penthouse.BRICKS_PRISMARINE, false);
            this.setBlockState(worldIn, Penthouse.BRICKS_PRISMARINE, 5, 0, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Penthouse.BRICKS_PRISMARINE, 8, 0, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Penthouse.BRICKS_PRISMARINE, 10, 0, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Penthouse.BRICKS_PRISMARINE, 3, 0, 10, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 3, 3, 0, 7, Penthouse.DARK_PRISMARINE, Penthouse.DARK_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 0, 3, 10, 0, 7, Penthouse.DARK_PRISMARINE, Penthouse.DARK_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 10, 7, 0, 10, Penthouse.DARK_PRISMARINE, Penthouse.DARK_PRISMARINE, false);
            int l = 3;
            for (int j = 0; j < 2; ++j) {
                for (int k = 2; k <= 8; k += 3) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, l, 0, k, l, 2, k, Penthouse.BRICKS_PRISMARINE, Penthouse.BRICKS_PRISMARINE, false);
                }
                l = 10;
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 10, 5, 2, 10, Penthouse.BRICKS_PRISMARINE, Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 0, 10, 8, 2, 10, Penthouse.BRICKS_PRISMARINE, Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, -1, 7, 7, -1, 8, Penthouse.DARK_PRISMARINE, Penthouse.DARK_PRISMARINE, false);
            this.generateWaterBox(worldIn, structureBoundingBoxIn, 6, -1, 3, 7, -1, 4, false);
            this.spawnElder(worldIn, structureBoundingBoxIn, 6, 1, 6);
            return true;
        }
    }
    
    public abstract static class Piece extends StructureComponent
    {
        protected static final IBlockState ROUGH_PRISMARINE;
        protected static final IBlockState BRICKS_PRISMARINE;
        protected static final IBlockState DARK_PRISMARINE;
        protected static final IBlockState DOT_DECO_DATA;
        protected static final IBlockState SEA_LANTERN;
        protected static final IBlockState WATER;
        protected static final int GRIDROOM_SOURCE_INDEX;
        protected static final int GRIDROOM_TOP_CONNECT_INDEX;
        protected static final int GRIDROOM_LEFTWING_CONNECT_INDEX;
        protected static final int GRIDROOM_RIGHTWING_CONNECT_INDEX;
        protected RoomDefinition roomDefinition;
        
        protected static final int getRoomIndex(final int p_175820_0_, final int p_175820_1_, final int p_175820_2_) {
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
            this.setCoordBaseMode(p_i45589_1_);
            this.boundingBox = p_i45589_2_;
        }
        
        protected Piece(final int p_i45590_1_, final EnumFacing p_i45590_2_, final RoomDefinition p_i45590_3_, final int p_i45590_4_, final int p_i45590_5_, final int p_i45590_6_) {
            super(p_i45590_1_);
            this.setCoordBaseMode(p_i45590_2_);
            this.roomDefinition = p_i45590_3_;
            final int i = p_i45590_3_.index;
            final int j = i % 5;
            final int k = i / 5 % 5;
            final int l = i / 25;
            if (p_i45590_2_ != EnumFacing.NORTH && p_i45590_2_ != EnumFacing.SOUTH) {
                this.boundingBox = new StructureBoundingBox(0, 0, 0, p_i45590_6_ * 8 - 1, p_i45590_5_ * 4 - 1, p_i45590_4_ * 8 - 1);
            }
            else {
                this.boundingBox = new StructureBoundingBox(0, 0, 0, p_i45590_4_ * 8 - 1, p_i45590_5_ * 4 - 1, p_i45590_6_ * 8 - 1);
            }
            switch (p_i45590_2_) {
                case NORTH: {
                    this.boundingBox.offset(j * 8, l * 4, -(k + p_i45590_6_) * 8 + 1);
                    break;
                }
                case SOUTH: {
                    this.boundingBox.offset(j * 8, l * 4, k * 8);
                    break;
                }
                case WEST: {
                    this.boundingBox.offset(-(k + p_i45590_6_) * 8 + 1, l * 4, j * 8);
                    break;
                }
                default: {
                    this.boundingBox.offset(k * 8, l * 4, j * 8);
                    break;
                }
            }
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
        }
        
        protected void generateWaterBox(final World p_181655_1_, final StructureBoundingBox p_181655_2_, final int p_181655_3_, final int p_181655_4_, final int p_181655_5_, final int p_181655_6_, final int p_181655_7_, final int p_181655_8_, final boolean p_181655_9_) {
            for (int i = p_181655_4_; i <= p_181655_7_; ++i) {
                for (int j = p_181655_3_; j <= p_181655_6_; ++j) {
                    for (int k = p_181655_5_; k <= p_181655_8_; ++k) {
                        if (!p_181655_9_ || this.getBlockStateFromPos(p_181655_1_, j, i, k, p_181655_2_).getMaterial() != Material.AIR) {
                            if (this.getYWithOffset(i) >= p_181655_1_.getSeaLevel()) {
                                this.setBlockState(p_181655_1_, Blocks.AIR.getDefaultState(), j, i, k, p_181655_2_);
                            }
                            else {
                                this.setBlockState(p_181655_1_, Piece.WATER, j, i, k, p_181655_2_);
                            }
                        }
                    }
                }
            }
        }
        
        protected void generateDefaultFloor(final World worldIn, final StructureBoundingBox p_175821_2_, final int p_175821_3_, final int p_175821_4_, final boolean p_175821_5_) {
            if (p_175821_5_) {
                this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 0, 0, p_175821_4_ + 0, p_175821_3_ + 2, 0, p_175821_4_ + 8 - 1, Piece.ROUGH_PRISMARINE, Piece.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 5, 0, p_175821_4_ + 0, p_175821_3_ + 8 - 1, 0, p_175821_4_ + 8 - 1, Piece.ROUGH_PRISMARINE, Piece.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 0, p_175821_3_ + 4, 0, p_175821_4_ + 2, Piece.ROUGH_PRISMARINE, Piece.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 5, p_175821_3_ + 4, 0, p_175821_4_ + 8 - 1, Piece.ROUGH_PRISMARINE, Piece.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 2, p_175821_3_ + 4, 0, p_175821_4_ + 2, Piece.BRICKS_PRISMARINE, Piece.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 5, p_175821_3_ + 4, 0, p_175821_4_ + 5, Piece.BRICKS_PRISMARINE, Piece.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 2, 0, p_175821_4_ + 3, p_175821_3_ + 2, 0, p_175821_4_ + 4, Piece.BRICKS_PRISMARINE, Piece.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 5, 0, p_175821_4_ + 3, p_175821_3_ + 5, 0, p_175821_4_ + 4, Piece.BRICKS_PRISMARINE, Piece.BRICKS_PRISMARINE, false);
            }
            else {
                this.fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 0, 0, p_175821_4_ + 0, p_175821_3_ + 8 - 1, 0, p_175821_4_ + 8 - 1, Piece.ROUGH_PRISMARINE, Piece.ROUGH_PRISMARINE, false);
            }
        }
        
        protected void generateBoxOnFillOnly(final World worldIn, final StructureBoundingBox p_175819_2_, final int p_175819_3_, final int p_175819_4_, final int p_175819_5_, final int p_175819_6_, final int p_175819_7_, final int p_175819_8_, final IBlockState p_175819_9_) {
            for (int i = p_175819_4_; i <= p_175819_7_; ++i) {
                for (int j = p_175819_3_; j <= p_175819_6_; ++j) {
                    for (int k = p_175819_5_; k <= p_175819_8_; ++k) {
                        if (this.getBlockStateFromPos(worldIn, j, i, k, p_175819_2_) == Piece.WATER) {
                            this.setBlockState(worldIn, p_175819_9_, j, i, k, p_175819_2_);
                        }
                    }
                }
            }
        }
        
        protected boolean doesChunkIntersect(final StructureBoundingBox p_175818_1_, final int p_175818_2_, final int p_175818_3_, final int p_175818_4_, final int p_175818_5_) {
            final int i = this.getXWithOffset(p_175818_2_, p_175818_3_);
            final int j = this.getZWithOffset(p_175818_2_, p_175818_3_);
            final int k = this.getXWithOffset(p_175818_4_, p_175818_5_);
            final int l = this.getZWithOffset(p_175818_4_, p_175818_5_);
            return p_175818_1_.intersectsWith(Math.min(i, k), Math.min(j, l), Math.max(i, k), Math.max(j, l));
        }
        
        protected boolean spawnElder(final World worldIn, final StructureBoundingBox p_175817_2_, final int p_175817_3_, final int p_175817_4_, final int p_175817_5_) {
            final int i = this.getXWithOffset(p_175817_3_, p_175817_5_);
            final int j = this.getYWithOffset(p_175817_4_);
            final int k = this.getZWithOffset(p_175817_3_, p_175817_5_);
            if (p_175817_2_.isVecInside(new BlockPos(i, j, k))) {
                final EntityElderGuardian entityelderguardian = new EntityElderGuardian(worldIn);
                entityelderguardian.heal(entityelderguardian.getMaxHealth());
                entityelderguardian.setLocationAndAngles(i + 0.5, j, k + 0.5, 0.0f, 0.0f);
                entityelderguardian.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityelderguardian)), null);
                worldIn.spawnEntity(entityelderguardian);
                return true;
            }
            return false;
        }
        
        static {
            ROUGH_PRISMARINE = Blocks.PRISMARINE.getStateFromMeta(BlockPrismarine.ROUGH_META);
            BRICKS_PRISMARINE = Blocks.PRISMARINE.getStateFromMeta(BlockPrismarine.BRICKS_META);
            DARK_PRISMARINE = Blocks.PRISMARINE.getStateFromMeta(BlockPrismarine.DARK_META);
            DOT_DECO_DATA = Piece.BRICKS_PRISMARINE;
            SEA_LANTERN = Blocks.SEA_LANTERN.getDefaultState();
            WATER = Blocks.WATER.getDefaultState();
            GRIDROOM_SOURCE_INDEX = getRoomIndex(2, 0, 0);
            GRIDROOM_TOP_CONNECT_INDEX = getRoomIndex(2, 2, 0);
            GRIDROOM_LEFTWING_CONNECT_INDEX = getRoomIndex(0, 1, 0);
            GRIDROOM_RIGHTWING_CONNECT_INDEX = getRoomIndex(4, 1, 0);
        }
    }
    
    static class RoomDefinition
    {
        int index;
        RoomDefinition[] connections;
        boolean[] hasOpening;
        boolean claimed;
        boolean isSource;
        int scanIndex;
        
        public RoomDefinition(final int p_i45584_1_) {
            this.connections = new RoomDefinition[6];
            this.hasOpening = new boolean[6];
            this.index = p_i45584_1_;
        }
        
        public void setConnection(final EnumFacing p_175957_1_, final RoomDefinition p_175957_2_) {
            this.connections[p_175957_1_.getIndex()] = p_175957_2_;
            p_175957_2_.connections[p_175957_1_.getOpposite().getIndex()] = this;
        }
        
        public void updateOpenings() {
            for (int i = 0; i < 6; ++i) {
                this.hasOpening[i] = (this.connections[i] != null);
            }
        }
        
        public boolean findSource(final int p_175959_1_) {
            if (this.isSource) {
                return true;
            }
            this.scanIndex = p_175959_1_;
            for (int i = 0; i < 6; ++i) {
                if (this.connections[i] != null && this.hasOpening[i] && this.connections[i].scanIndex != p_175959_1_ && this.connections[i].findSource(p_175959_1_)) {
                    return true;
                }
            }
            return false;
        }
        
        public boolean isSpecial() {
            return this.index >= 75;
        }
        
        public int countOpenings() {
            int i = 0;
            for (int j = 0; j < 6; ++j) {
                if (this.hasOpening[j]) {
                    ++i;
                }
            }
            return i;
        }
    }
    
    public static class SimpleRoom extends Piece
    {
        private int mainDesign;
        
        public SimpleRoom() {
        }
        
        public SimpleRoom(final EnumFacing p_i45587_1_, final RoomDefinition p_i45587_2_, final Random p_i45587_3_) {
            super(1, p_i45587_1_, p_i45587_2_, 1, 1, 1);
            this.mainDesign = p_i45587_3_.nextInt(3);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.roomDefinition.index / 25 > 0) {
                this.generateDefaultFloor(worldIn, structureBoundingBoxIn, 0, 0, this.roomDefinition.hasOpening[EnumFacing.DOWN.getIndex()]);
            }
            if (this.roomDefinition.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(worldIn, structureBoundingBoxIn, 1, 4, 1, 6, 4, 6, SimpleRoom.ROUGH_PRISMARINE);
            }
            final boolean flag = this.mainDesign != 0 && randomIn.nextBoolean() && !this.roomDefinition.hasOpening[EnumFacing.DOWN.getIndex()] && !this.roomDefinition.hasOpening[EnumFacing.UP.getIndex()] && this.roomDefinition.countOpenings() > 1;
            if (this.mainDesign == 0) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 2, 1, 2, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 2, 3, 2, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 2, 2, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 2, 2, 0, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                this.setBlockState(worldIn, SimpleRoom.SEA_LANTERN, 1, 2, 1, structureBoundingBoxIn);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 0, 7, 1, 2, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 3, 0, 7, 3, 2, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 0, 7, 2, 2, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 0, 6, 2, 0, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                this.setBlockState(worldIn, SimpleRoom.SEA_LANTERN, 6, 2, 1, structureBoundingBoxIn);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 5, 2, 1, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 5, 2, 3, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 5, 0, 2, 7, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 7, 2, 2, 7, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                this.setBlockState(worldIn, SimpleRoom.SEA_LANTERN, 1, 2, 6, structureBoundingBoxIn);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 5, 7, 1, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 3, 5, 7, 3, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 5, 7, 2, 7, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 7, 6, 2, 7, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                this.setBlockState(worldIn, SimpleRoom.SEA_LANTERN, 6, 2, 6, structureBoundingBoxIn);
                if (this.roomDefinition.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 0, 4, 3, 0, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                }
                else {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 0, 4, 3, 1, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 0, 4, 2, 0, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 1, 1, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                }
                if (this.roomDefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 7, 4, 3, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                }
                else {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 6, 4, 3, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 7, 4, 2, 7, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 6, 4, 1, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                }
                if (this.roomDefinition.hasOpening[EnumFacing.WEST.getIndex()]) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 3, 0, 3, 4, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                }
                else {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 3, 1, 3, 4, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 3, 0, 2, 4, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 3, 1, 1, 4, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                }
                if (this.roomDefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 3, 7, 3, 4, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                }
                else {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 3, 7, 3, 4, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 3, 7, 2, 4, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 3, 7, 1, 4, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                }
            }
            else if (this.mainDesign == 1) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 2, 2, 3, 2, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 5, 2, 3, 5, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 5, 5, 3, 5, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 2, 5, 3, 2, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.setBlockState(worldIn, SimpleRoom.SEA_LANTERN, 2, 2, 2, structureBoundingBoxIn);
                this.setBlockState(worldIn, SimpleRoom.SEA_LANTERN, 2, 2, 5, structureBoundingBoxIn);
                this.setBlockState(worldIn, SimpleRoom.SEA_LANTERN, 5, 2, 5, structureBoundingBoxIn);
                this.setBlockState(worldIn, SimpleRoom.SEA_LANTERN, 5, 2, 2, structureBoundingBoxIn);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 1, 3, 0, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 1, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 7, 1, 3, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 6, 0, 3, 6, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 7, 7, 3, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 6, 7, 3, 6, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 0, 7, 3, 0, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 1, 7, 3, 1, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.setBlockState(worldIn, SimpleRoom.ROUGH_PRISMARINE, 1, 2, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, SimpleRoom.ROUGH_PRISMARINE, 0, 2, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, SimpleRoom.ROUGH_PRISMARINE, 1, 2, 7, structureBoundingBoxIn);
                this.setBlockState(worldIn, SimpleRoom.ROUGH_PRISMARINE, 0, 2, 6, structureBoundingBoxIn);
                this.setBlockState(worldIn, SimpleRoom.ROUGH_PRISMARINE, 6, 2, 7, structureBoundingBoxIn);
                this.setBlockState(worldIn, SimpleRoom.ROUGH_PRISMARINE, 7, 2, 6, structureBoundingBoxIn);
                this.setBlockState(worldIn, SimpleRoom.ROUGH_PRISMARINE, 6, 2, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, SimpleRoom.ROUGH_PRISMARINE, 7, 2, 1, structureBoundingBoxIn);
                if (!this.roomDefinition.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 6, 3, 0, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 6, 2, 0, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 6, 1, 0, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                }
                if (!this.roomDefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 7, 6, 3, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 7, 6, 2, 7, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 6, 1, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                }
                if (!this.roomDefinition.hasOpening[EnumFacing.WEST.getIndex()]) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 1, 0, 3, 6, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 1, 0, 2, 6, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 1, 6, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                }
                if (!this.roomDefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 1, 7, 3, 6, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 1, 7, 2, 6, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 1, 7, 1, 6, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                }
            }
            else if (this.mainDesign == 2) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 0, 7, 1, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 6, 1, 0, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 6, 1, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 2, 7, SimpleRoom.DARK_PRISMARINE, SimpleRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 0, 7, 2, 7, SimpleRoom.DARK_PRISMARINE, SimpleRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 6, 2, 0, SimpleRoom.DARK_PRISMARINE, SimpleRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 7, 6, 2, 7, SimpleRoom.DARK_PRISMARINE, SimpleRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 0, 3, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 0, 7, 3, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 6, 3, 0, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 7, 6, 3, 7, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, SimpleRoom.DARK_PRISMARINE, SimpleRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 3, 7, 2, 4, SimpleRoom.DARK_PRISMARINE, SimpleRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, SimpleRoom.DARK_PRISMARINE, SimpleRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7, SimpleRoom.DARK_PRISMARINE, SimpleRoom.DARK_PRISMARINE, false);
                if (this.roomDefinition.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                    this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, false);
                }
                if (this.roomDefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
                    this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7, false);
                }
                if (this.roomDefinition.hasOpening[EnumFacing.WEST.getIndex()]) {
                    this.generateWaterBox(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, false);
                }
                if (this.roomDefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
                    this.generateWaterBox(worldIn, structureBoundingBoxIn, 7, 1, 3, 7, 2, 4, false);
                }
            }
            if (flag) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 3, 4, 1, 4, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 3, 4, 2, 4, SimpleRoom.ROUGH_PRISMARINE, SimpleRoom.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 3, 4, 3, 4, SimpleRoom.BRICKS_PRISMARINE, SimpleRoom.BRICKS_PRISMARINE, false);
            }
            return true;
        }
    }
    
    public static class SimpleTopRoom extends Piece
    {
        public SimpleTopRoom() {
        }
        
        public SimpleTopRoom(final EnumFacing p_i45586_1_, final RoomDefinition p_i45586_2_, final Random p_i45586_3_) {
            super(1, p_i45586_1_, p_i45586_2_, 1, 1, 1);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.roomDefinition.index / 25 > 0) {
                this.generateDefaultFloor(worldIn, structureBoundingBoxIn, 0, 0, this.roomDefinition.hasOpening[EnumFacing.DOWN.getIndex()]);
            }
            if (this.roomDefinition.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(worldIn, structureBoundingBoxIn, 1, 4, 1, 6, 4, 6, SimpleTopRoom.ROUGH_PRISMARINE);
            }
            for (int i = 1; i <= 6; ++i) {
                for (int j = 1; j <= 6; ++j) {
                    if (randomIn.nextInt(3) != 0) {
                        final int k = 2 + ((randomIn.nextInt(4) != 0) ? 1 : 0);
                        this.fillWithBlocks(worldIn, structureBoundingBoxIn, i, k, j, i, 3, j, Blocks.SPONGE.getStateFromMeta(1), Blocks.SPONGE.getStateFromMeta(1), false);
                    }
                }
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 7, SimpleTopRoom.BRICKS_PRISMARINE, SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 0, 7, 1, 7, SimpleTopRoom.BRICKS_PRISMARINE, SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 6, 1, 0, SimpleTopRoom.BRICKS_PRISMARINE, SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 6, 1, 7, SimpleTopRoom.BRICKS_PRISMARINE, SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 2, 7, SimpleTopRoom.DARK_PRISMARINE, SimpleTopRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 0, 7, 2, 7, SimpleTopRoom.DARK_PRISMARINE, SimpleTopRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 6, 2, 0, SimpleTopRoom.DARK_PRISMARINE, SimpleTopRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 7, 6, 2, 7, SimpleTopRoom.DARK_PRISMARINE, SimpleTopRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 0, 3, 7, SimpleTopRoom.BRICKS_PRISMARINE, SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 0, 7, 3, 7, SimpleTopRoom.BRICKS_PRISMARINE, SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 6, 3, 0, SimpleTopRoom.BRICKS_PRISMARINE, SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 7, 6, 3, 7, SimpleTopRoom.BRICKS_PRISMARINE, SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, SimpleTopRoom.DARK_PRISMARINE, SimpleTopRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 3, 7, 2, 4, SimpleTopRoom.DARK_PRISMARINE, SimpleTopRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, SimpleTopRoom.DARK_PRISMARINE, SimpleTopRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7, SimpleTopRoom.DARK_PRISMARINE, SimpleTopRoom.DARK_PRISMARINE, false);
            if (this.roomDefinition.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, false);
            }
            return true;
        }
    }
    
    public static class WingRoom extends Piece
    {
        private int mainDesign;
        
        public WingRoom() {
        }
        
        public WingRoom(final EnumFacing p_i45585_1_, final StructureBoundingBox p_i45585_2_, final int p_i45585_3_) {
            super(p_i45585_1_, p_i45585_2_);
            this.mainDesign = (p_i45585_3_ & 0x1);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.mainDesign == 0) {
                for (int i = 0; i < 4; ++i) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10 - i, 3 - i, 20 - i, 12 + i, 3 - i, 20, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                }
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 6, 15, 0, 16, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 6, 6, 3, 20, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 16, 0, 6, 16, 3, 20, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 7, 7, 1, 20, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 1, 7, 15, 1, 20, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 6, 9, 3, 6, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 1, 6, 15, 3, 6, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 7, 9, 1, 7, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 1, 7, 14, 1, 7, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 5, 13, 0, 5, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 0, 7, 12, 0, 7, WingRoom.DARK_PRISMARINE, WingRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 0, 10, 8, 0, 12, WingRoom.DARK_PRISMARINE, WingRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 14, 0, 10, 14, 0, 12, WingRoom.DARK_PRISMARINE, WingRoom.DARK_PRISMARINE, false);
                for (int i2 = 18; i2 >= 7; i2 -= 3) {
                    this.setBlockState(worldIn, WingRoom.SEA_LANTERN, 6, 3, i2, structureBoundingBoxIn);
                    this.setBlockState(worldIn, WingRoom.SEA_LANTERN, 16, 3, i2, structureBoundingBoxIn);
                }
                this.setBlockState(worldIn, WingRoom.SEA_LANTERN, 10, 0, 10, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.SEA_LANTERN, 12, 0, 10, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.SEA_LANTERN, 10, 0, 12, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.SEA_LANTERN, 12, 0, 12, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.SEA_LANTERN, 8, 3, 6, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.SEA_LANTERN, 14, 3, 6, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.BRICKS_PRISMARINE, 4, 2, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.SEA_LANTERN, 4, 1, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.BRICKS_PRISMARINE, 4, 0, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.BRICKS_PRISMARINE, 18, 2, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.SEA_LANTERN, 18, 1, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.BRICKS_PRISMARINE, 18, 0, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.BRICKS_PRISMARINE, 4, 2, 18, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.SEA_LANTERN, 4, 1, 18, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.BRICKS_PRISMARINE, 4, 0, 18, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.BRICKS_PRISMARINE, 18, 2, 18, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.SEA_LANTERN, 18, 1, 18, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.BRICKS_PRISMARINE, 18, 0, 18, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.BRICKS_PRISMARINE, 9, 7, 20, structureBoundingBoxIn);
                this.setBlockState(worldIn, WingRoom.BRICKS_PRISMARINE, 13, 7, 20, structureBoundingBoxIn);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 21, 7, 4, 21, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 0, 21, 16, 4, 21, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                this.spawnElder(worldIn, structureBoundingBoxIn, 11, 2, 16);
            }
            else if (this.mainDesign == 1) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 3, 18, 13, 3, 20, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 18, 9, 2, 18, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 0, 18, 13, 2, 18, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                int j1 = 9;
                final int k = 20;
                final int l = 5;
                for (int m = 0; m < 2; ++m) {
                    this.setBlockState(worldIn, WingRoom.BRICKS_PRISMARINE, j1, 6, 20, structureBoundingBoxIn);
                    this.setBlockState(worldIn, WingRoom.SEA_LANTERN, j1, 5, 20, structureBoundingBoxIn);
                    this.setBlockState(worldIn, WingRoom.BRICKS_PRISMARINE, j1, 4, 20, structureBoundingBoxIn);
                    j1 = 13;
                }
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 7, 15, 3, 14, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                j1 = 10;
                for (int k2 = 0; k2 < 2; ++k2) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, j1, 0, 10, j1, 6, 10, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, j1, 0, 12, j1, 6, 12, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                    this.setBlockState(worldIn, WingRoom.SEA_LANTERN, j1, 0, 10, structureBoundingBoxIn);
                    this.setBlockState(worldIn, WingRoom.SEA_LANTERN, j1, 0, 12, structureBoundingBoxIn);
                    this.setBlockState(worldIn, WingRoom.SEA_LANTERN, j1, 4, 10, structureBoundingBoxIn);
                    this.setBlockState(worldIn, WingRoom.SEA_LANTERN, j1, 4, 12, structureBoundingBoxIn);
                    j1 = 12;
                }
                j1 = 8;
                for (int l2 = 0; l2 < 2; ++l2) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, j1, 0, 7, j1, 2, 7, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, j1, 0, 14, j1, 2, 14, WingRoom.BRICKS_PRISMARINE, WingRoom.BRICKS_PRISMARINE, false);
                    j1 = 14;
                }
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 3, 8, 8, 3, 13, WingRoom.DARK_PRISMARINE, WingRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 14, 3, 8, 14, 3, 13, WingRoom.DARK_PRISMARINE, WingRoom.DARK_PRISMARINE, false);
                this.spawnElder(worldIn, structureBoundingBoxIn, 11, 5, 13);
            }
            return true;
        }
    }
    
    static class XDoubleRoomFitHelper implements MonumentRoomFitHelper
    {
        private XDoubleRoomFitHelper() {
        }
        
        @Override
        public boolean fits(final RoomDefinition definition) {
            return definition.hasOpening[EnumFacing.EAST.getIndex()] && !definition.connections[EnumFacing.EAST.getIndex()].claimed;
        }
        
        @Override
        public Piece create(final EnumFacing p_175968_1_, final RoomDefinition p_175968_2_, final Random p_175968_3_) {
            p_175968_2_.claimed = true;
            p_175968_2_.connections[EnumFacing.EAST.getIndex()].claimed = true;
            return new DoubleXRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }
    }
    
    static class XYDoubleRoomFitHelper implements MonumentRoomFitHelper
    {
        private XYDoubleRoomFitHelper() {
        }
        
        @Override
        public boolean fits(final RoomDefinition definition) {
            if (definition.hasOpening[EnumFacing.EAST.getIndex()] && !definition.connections[EnumFacing.EAST.getIndex()].claimed && definition.hasOpening[EnumFacing.UP.getIndex()] && !definition.connections[EnumFacing.UP.getIndex()].claimed) {
                final RoomDefinition structureoceanmonumentpieces$roomdefinition = definition.connections[EnumFacing.EAST.getIndex()];
                return structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.UP.getIndex()] && !structureoceanmonumentpieces$roomdefinition.connections[EnumFacing.UP.getIndex()].claimed;
            }
            return false;
        }
        
        @Override
        public Piece create(final EnumFacing p_175968_1_, final RoomDefinition p_175968_2_, final Random p_175968_3_) {
            p_175968_2_.claimed = true;
            p_175968_2_.connections[EnumFacing.EAST.getIndex()].claimed = true;
            p_175968_2_.connections[EnumFacing.UP.getIndex()].claimed = true;
            p_175968_2_.connections[EnumFacing.EAST.getIndex()].connections[EnumFacing.UP.getIndex()].claimed = true;
            return new DoubleXYRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }
    }
    
    static class YDoubleRoomFitHelper implements MonumentRoomFitHelper
    {
        private YDoubleRoomFitHelper() {
        }
        
        @Override
        public boolean fits(final RoomDefinition definition) {
            return definition.hasOpening[EnumFacing.UP.getIndex()] && !definition.connections[EnumFacing.UP.getIndex()].claimed;
        }
        
        @Override
        public Piece create(final EnumFacing p_175968_1_, final RoomDefinition p_175968_2_, final Random p_175968_3_) {
            p_175968_2_.claimed = true;
            p_175968_2_.connections[EnumFacing.UP.getIndex()].claimed = true;
            return new DoubleYRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }
    }
    
    static class YZDoubleRoomFitHelper implements MonumentRoomFitHelper
    {
        private YZDoubleRoomFitHelper() {
        }
        
        @Override
        public boolean fits(final RoomDefinition definition) {
            if (definition.hasOpening[EnumFacing.NORTH.getIndex()] && !definition.connections[EnumFacing.NORTH.getIndex()].claimed && definition.hasOpening[EnumFacing.UP.getIndex()] && !definition.connections[EnumFacing.UP.getIndex()].claimed) {
                final RoomDefinition structureoceanmonumentpieces$roomdefinition = definition.connections[EnumFacing.NORTH.getIndex()];
                return structureoceanmonumentpieces$roomdefinition.hasOpening[EnumFacing.UP.getIndex()] && !structureoceanmonumentpieces$roomdefinition.connections[EnumFacing.UP.getIndex()].claimed;
            }
            return false;
        }
        
        @Override
        public Piece create(final EnumFacing p_175968_1_, final RoomDefinition p_175968_2_, final Random p_175968_3_) {
            p_175968_2_.claimed = true;
            p_175968_2_.connections[EnumFacing.NORTH.getIndex()].claimed = true;
            p_175968_2_.connections[EnumFacing.UP.getIndex()].claimed = true;
            p_175968_2_.connections[EnumFacing.NORTH.getIndex()].connections[EnumFacing.UP.getIndex()].claimed = true;
            return new DoubleYZRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }
    }
    
    static class ZDoubleRoomFitHelper implements MonumentRoomFitHelper
    {
        private ZDoubleRoomFitHelper() {
        }
        
        @Override
        public boolean fits(final RoomDefinition definition) {
            return definition.hasOpening[EnumFacing.NORTH.getIndex()] && !definition.connections[EnumFacing.NORTH.getIndex()].claimed;
        }
        
        @Override
        public Piece create(final EnumFacing p_175968_1_, final RoomDefinition p_175968_2_, final Random p_175968_3_) {
            RoomDefinition structureoceanmonumentpieces$roomdefinition = p_175968_2_;
            if (!p_175968_2_.hasOpening[EnumFacing.NORTH.getIndex()] || p_175968_2_.connections[EnumFacing.NORTH.getIndex()].claimed) {
                structureoceanmonumentpieces$roomdefinition = p_175968_2_.connections[EnumFacing.SOUTH.getIndex()];
            }
            structureoceanmonumentpieces$roomdefinition.claimed = true;
            structureoceanmonumentpieces$roomdefinition.connections[EnumFacing.NORTH.getIndex()].claimed = true;
            return new DoubleZRoom(p_175968_1_, structureoceanmonumentpieces$roomdefinition, p_175968_3_);
        }
    }
    
    interface MonumentRoomFitHelper
    {
        boolean fits(final RoomDefinition p0);
        
        Piece create(final EnumFacing p0, final RoomDefinition p1, final Random p2);
    }
}
