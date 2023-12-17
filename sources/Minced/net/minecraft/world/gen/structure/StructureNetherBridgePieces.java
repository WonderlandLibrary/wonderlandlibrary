// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.tileentity.TileEntityMobSpawner;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStairs;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import java.util.Random;
import java.util.List;

public class StructureNetherBridgePieces
{
    private static final PieceWeight[] PRIMARY_COMPONENTS;
    private static final PieceWeight[] SECONDARY_COMPONENTS;
    
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
    
    private static Piece findAndCreateBridgePieceFactory(final PieceWeight p_175887_0_, final List<StructureComponent> p_175887_1_, final Random p_175887_2_, final int p_175887_3_, final int p_175887_4_, final int p_175887_5_, final EnumFacing p_175887_6_, final int p_175887_7_) {
        final Class<? extends Piece> oclass = p_175887_0_.weightClass;
        Piece structurenetherbridgepieces$piece = null;
        if (oclass == Straight.class) {
            structurenetherbridgepieces$piece = Straight.createPiece(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
        }
        else if (oclass == Crossing3.class) {
            structurenetherbridgepieces$piece = Crossing3.createPiece(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
        }
        else if (oclass == Crossing.class) {
            structurenetherbridgepieces$piece = Crossing.createPiece(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
        }
        else if (oclass == Stairs.class) {
            structurenetherbridgepieces$piece = Stairs.createPiece(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_7_, p_175887_6_);
        }
        else if (oclass == Throne.class) {
            structurenetherbridgepieces$piece = Throne.createPiece(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_7_, p_175887_6_);
        }
        else if (oclass == Entrance.class) {
            structurenetherbridgepieces$piece = Entrance.createPiece(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
        }
        else if (oclass == Corridor5.class) {
            structurenetherbridgepieces$piece = Corridor5.createPiece(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
        }
        else if (oclass == Corridor2.class) {
            structurenetherbridgepieces$piece = Corridor2.createPiece(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
        }
        else if (oclass == Corridor.class) {
            structurenetherbridgepieces$piece = Corridor.createPiece(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
        }
        else if (oclass == Corridor3.class) {
            structurenetherbridgepieces$piece = Corridor3.createPiece(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
        }
        else if (oclass == Corridor4.class) {
            structurenetherbridgepieces$piece = Corridor4.createPiece(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
        }
        else if (oclass == Crossing2.class) {
            structurenetherbridgepieces$piece = Crossing2.createPiece(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
        }
        else if (oclass == NetherStalkRoom.class) {
            structurenetherbridgepieces$piece = NetherStalkRoom.createPiece(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
        }
        return structurenetherbridgepieces$piece;
    }
    
    static {
        PRIMARY_COMPONENTS = new PieceWeight[] { new PieceWeight(Straight.class, 30, 0, true), new PieceWeight(Crossing3.class, 10, 4), new PieceWeight(Crossing.class, 10, 4), new PieceWeight(Stairs.class, 10, 3), new PieceWeight(Throne.class, 5, 2), new PieceWeight(Entrance.class, 5, 1) };
        SECONDARY_COMPONENTS = new PieceWeight[] { new PieceWeight(Corridor5.class, 25, 0, true), new PieceWeight(Crossing2.class, 15, 5), new PieceWeight(Corridor2.class, 5, 10), new PieceWeight(Corridor.class, 5, 10), new PieceWeight(Corridor3.class, 10, 3, true), new PieceWeight(Corridor4.class, 7, 2), new PieceWeight(NetherStalkRoom.class, 5, 2) };
    }
    
    public static class Corridor extends Piece
    {
        private boolean chest;
        
        public Corridor() {
        }
        
        public Corridor(final int p_i45615_1_, final Random p_i45615_2_, final StructureBoundingBox p_i45615_3_, final EnumFacing p_i45615_4_) {
            super(p_i45615_1_);
            this.setCoordBaseMode(p_i45615_4_);
            this.boundingBox = p_i45615_3_;
            this.chest = (p_i45615_2_.nextInt(3) == 0);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.chest = tagCompound.getBoolean("Chest");
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setBoolean("Chest", this.chest);
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            this.getNextComponentX((Start)componentIn, listIn, rand, 0, 1, true);
        }
        
        public static Corridor createPiece(final List<StructureComponent> p_175879_0_, final Random p_175879_1_, final int p_175879_2_, final int p_175879_3_, final int p_175879_4_, final EnumFacing p_175879_5_, final int p_175879_6_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175879_2_, p_175879_3_, p_175879_4_, -1, 0, 0, 5, 7, 5, p_175879_5_);
            return (Piece.isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(p_175879_0_, structureboundingbox) == null) ? new Corridor(p_175879_6_, p_175879_1_, structureboundingbox, p_175879_5_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 4, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 1, 4, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 3, 4, 4, 3, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 3, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 4, 1, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 4, 3, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            if (this.chest && structureBoundingBoxIn.isVecInside(new BlockPos(this.getXWithOffset(3, 3), this.getYWithOffset(2), this.getZWithOffset(3, 3)))) {
                this.chest = false;
                this.generateChest(worldIn, structureBoundingBoxIn, randomIn, 3, 2, 3, LootTableList.CHESTS_NETHER_BRIDGE);
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 4; ++j) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureBoundingBoxIn);
                }
            }
            return true;
        }
    }
    
    public static class Corridor2 extends Piece
    {
        private boolean chest;
        
        public Corridor2() {
        }
        
        public Corridor2(final int p_i45613_1_, final Random p_i45613_2_, final StructureBoundingBox p_i45613_3_, final EnumFacing p_i45613_4_) {
            super(p_i45613_1_);
            this.setCoordBaseMode(p_i45613_4_);
            this.boundingBox = p_i45613_3_;
            this.chest = (p_i45613_2_.nextInt(3) == 0);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.chest = tagCompound.getBoolean("Chest");
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setBoolean("Chest", this.chest);
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            this.getNextComponentZ((Start)componentIn, listIn, rand, 0, 1, true);
        }
        
        public static Corridor2 createPiece(final List<StructureComponent> p_175876_0_, final Random p_175876_1_, final int p_175876_2_, final int p_175876_3_, final int p_175876_4_, final EnumFacing p_175876_5_, final int p_175876_6_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175876_2_, p_175876_3_, p_175876_4_, -1, 0, 0, 5, 7, 5, p_175876_5_);
            return (Piece.isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(p_175876_0_, structureboundingbox) == null) ? new Corridor2(p_175876_6_, p_175876_1_, structureboundingbox, p_175876_5_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 4, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 1, 0, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 3, 0, 4, 3, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 4, 4, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 4, 1, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 4, 3, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            if (this.chest && structureBoundingBoxIn.isVecInside(new BlockPos(this.getXWithOffset(1, 3), this.getYWithOffset(2), this.getZWithOffset(1, 3)))) {
                this.chest = false;
                this.generateChest(worldIn, structureBoundingBoxIn, randomIn, 1, 2, 3, LootTableList.CHESTS_NETHER_BRIDGE);
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 4; ++j) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureBoundingBoxIn);
                }
            }
            return true;
        }
    }
    
    public static class Corridor3 extends Piece
    {
        public Corridor3() {
        }
        
        public Corridor3(final int p_i45619_1_, final Random p_i45619_2_, final StructureBoundingBox p_i45619_3_, final EnumFacing p_i45619_4_) {
            super(p_i45619_1_);
            this.setCoordBaseMode(p_i45619_4_);
            this.boundingBox = p_i45619_3_;
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            this.getNextComponentNormal((Start)componentIn, listIn, rand, 1, 0, true);
        }
        
        public static Corridor3 createPiece(final List<StructureComponent> p_175883_0_, final Random p_175883_1_, final int p_175883_2_, final int p_175883_3_, final int p_175883_4_, final EnumFacing p_175883_5_, final int p_175883_6_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175883_2_, p_175883_3_, p_175883_4_, -1, -7, 0, 5, 14, 10, p_175883_5_);
            return (Piece.isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(p_175883_0_, structureboundingbox) == null) ? new Corridor3(p_175883_6_, p_175883_1_, structureboundingbox, p_175883_5_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            final IBlockState iblockstate = Blocks.NETHER_BRICK_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.SOUTH);
            for (int i = 0; i <= 9; ++i) {
                final int j = Math.max(1, 7 - i);
                final int k = Math.min(Math.max(j + 5, 14 - i), 13);
                final int l = i;
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, i, 4, j, i, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, j + 1, i, 3, k - 1, i, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                if (i <= 6) {
                    this.setBlockState(worldIn, iblockstate, 1, j + 1, i, structureBoundingBoxIn);
                    this.setBlockState(worldIn, iblockstate, 2, j + 1, i, structureBoundingBoxIn);
                    this.setBlockState(worldIn, iblockstate, 3, j + 1, i, structureBoundingBoxIn);
                }
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, k, i, 4, k, i, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, j + 1, i, 0, k - 1, i, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, j + 1, i, 4, k - 1, i, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                if ((i & 0x1) == 0x0) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, j + 2, i, 0, j + 3, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, j + 2, i, 4, j + 3, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                }
                for (int i2 = 0; i2 <= 4; ++i2) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i2, -1, l, structureBoundingBoxIn);
                }
            }
            return true;
        }
    }
    
    public static class Corridor4 extends Piece
    {
        public Corridor4() {
        }
        
        public Corridor4(final int p_i45618_1_, final Random p_i45618_2_, final StructureBoundingBox p_i45618_3_, final EnumFacing p_i45618_4_) {
            super(p_i45618_1_);
            this.setCoordBaseMode(p_i45618_4_);
            this.boundingBox = p_i45618_3_;
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            int i = 1;
            final EnumFacing enumfacing = this.getCoordBaseMode();
            if (enumfacing == EnumFacing.WEST || enumfacing == EnumFacing.NORTH) {
                i = 5;
            }
            this.getNextComponentX((Start)componentIn, listIn, rand, 0, i, rand.nextInt(8) > 0);
            this.getNextComponentZ((Start)componentIn, listIn, rand, 0, i, rand.nextInt(8) > 0);
        }
        
        public static Corridor4 createPiece(final List<StructureComponent> p_175880_0_, final Random p_175880_1_, final int p_175880_2_, final int p_175880_3_, final int p_175880_4_, final EnumFacing p_175880_5_, final int p_175880_6_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175880_2_, p_175880_3_, p_175880_4_, -3, 0, 0, 9, 7, 9, p_175880_5_);
            return (Piece.isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(p_175880_0_, structureboundingbox) == null) ? new Corridor4(p_175880_6_, p_175880_1_, structureboundingbox, p_175880_5_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 8, 1, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 8, 5, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 0, 8, 6, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 2, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 0, 8, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 1, 4, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 0, 7, 4, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 8, 2, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 4, 2, 2, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 4, 7, 2, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 8, 8, 3, 8, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 6, 0, 3, 7, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 3, 6, 8, 3, 7, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 4, 0, 5, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 3, 4, 8, 5, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 5, 2, 5, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 5, 7, 5, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 5, 1, 5, 5, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 5, 7, 5, 5, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            for (int i = 0; i <= 5; ++i) {
                for (int j = 0; j <= 8; ++j) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), j, -1, i, structureBoundingBoxIn);
                }
            }
            return true;
        }
    }
    
    public static class Corridor5 extends Piece
    {
        public Corridor5() {
        }
        
        public Corridor5(final int p_i45614_1_, final Random p_i45614_2_, final StructureBoundingBox p_i45614_3_, final EnumFacing p_i45614_4_) {
            super(p_i45614_1_);
            this.setCoordBaseMode(p_i45614_4_);
            this.boundingBox = p_i45614_3_;
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            this.getNextComponentNormal((Start)componentIn, listIn, rand, 1, 0, true);
        }
        
        public static Corridor5 createPiece(final List<StructureComponent> p_175877_0_, final Random p_175877_1_, final int p_175877_2_, final int p_175877_3_, final int p_175877_4_, final EnumFacing p_175877_5_, final int p_175877_6_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175877_2_, p_175877_3_, p_175877_4_, -1, 0, 0, 5, 7, 5, p_175877_5_);
            return (Piece.isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(p_175877_0_, structureboundingbox) == null) ? new Corridor5(p_175877_6_, p_175877_1_, structureboundingbox, p_175877_5_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 4, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 1, 0, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 3, 0, 4, 3, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 1, 4, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 3, 4, 4, 3, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 4; ++j) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureBoundingBoxIn);
                }
            }
            return true;
        }
    }
    
    public static class Crossing extends Piece
    {
        public Crossing() {
        }
        
        public Crossing(final int p_i45610_1_, final Random p_i45610_2_, final StructureBoundingBox p_i45610_3_, final EnumFacing p_i45610_4_) {
            super(p_i45610_1_);
            this.setCoordBaseMode(p_i45610_4_);
            this.boundingBox = p_i45610_3_;
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            this.getNextComponentNormal((Start)componentIn, listIn, rand, 2, 0, false);
            this.getNextComponentX((Start)componentIn, listIn, rand, 0, 2, false);
            this.getNextComponentZ((Start)componentIn, listIn, rand, 0, 2, false);
        }
        
        public static Crossing createPiece(final List<StructureComponent> p_175873_0_, final Random p_175873_1_, final int p_175873_2_, final int p_175873_3_, final int p_175873_4_, final EnumFacing p_175873_5_, final int p_175873_6_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175873_2_, p_175873_3_, p_175873_4_, -2, 0, 0, 7, 9, 7, p_175873_5_);
            return (Piece.isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(p_175873_0_, structureboundingbox) == null) ? new Crossing(p_175873_6_, p_175873_1_, structureboundingbox, p_175873_5_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 6, 7, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 1, 6, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 6, 1, 6, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 0, 6, 6, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 6, 6, 6, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 6, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 5, 0, 6, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 0, 6, 6, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 5, 6, 6, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 6, 0, 4, 6, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 0, 4, 5, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 6, 6, 4, 6, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 6, 4, 5, 6, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 2, 0, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 2, 0, 5, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 6, 2, 6, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 5, 2, 6, 5, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            for (int i = 0; i <= 6; ++i) {
                for (int j = 0; j <= 6; ++j) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureBoundingBoxIn);
                }
            }
            return true;
        }
    }
    
    public static class Crossing2 extends Piece
    {
        public Crossing2() {
        }
        
        public Crossing2(final int p_i45616_1_, final Random p_i45616_2_, final StructureBoundingBox p_i45616_3_, final EnumFacing p_i45616_4_) {
            super(p_i45616_1_);
            this.setCoordBaseMode(p_i45616_4_);
            this.boundingBox = p_i45616_3_;
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            this.getNextComponentNormal((Start)componentIn, listIn, rand, 1, 0, true);
            this.getNextComponentX((Start)componentIn, listIn, rand, 0, 1, true);
            this.getNextComponentZ((Start)componentIn, listIn, rand, 0, 1, true);
        }
        
        public static Crossing2 createPiece(final List<StructureComponent> p_175878_0_, final Random p_175878_1_, final int p_175878_2_, final int p_175878_3_, final int p_175878_4_, final EnumFacing p_175878_5_, final int p_175878_6_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175878_2_, p_175878_3_, p_175878_4_, -1, 0, 0, 5, 7, 5, p_175878_5_);
            return (Piece.isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(p_175878_0_, structureboundingbox) == null) ? new Crossing2(p_175878_6_, p_175878_1_, structureboundingbox, p_175878_5_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 4, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 0, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 4, 4, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 4; ++j) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureBoundingBoxIn);
                }
            }
            return true;
        }
    }
    
    public static class Crossing3 extends Piece
    {
        public Crossing3() {
        }
        
        public Crossing3(final int p_i45622_1_, final Random p_i45622_2_, final StructureBoundingBox p_i45622_3_, final EnumFacing p_i45622_4_) {
            super(p_i45622_1_);
            this.setCoordBaseMode(p_i45622_4_);
            this.boundingBox = p_i45622_3_;
        }
        
        protected Crossing3(final Random p_i2042_1_, final int p_i2042_2_, final int p_i2042_3_) {
            super(0);
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(p_i2042_1_));
            if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
                this.boundingBox = new StructureBoundingBox(p_i2042_2_, 64, p_i2042_3_, p_i2042_2_ + 19 - 1, 73, p_i2042_3_ + 19 - 1);
            }
            else {
                this.boundingBox = new StructureBoundingBox(p_i2042_2_, 64, p_i2042_3_, p_i2042_2_ + 19 - 1, 73, p_i2042_3_ + 19 - 1);
            }
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            this.getNextComponentNormal((Start)componentIn, listIn, rand, 8, 3, false);
            this.getNextComponentX((Start)componentIn, listIn, rand, 3, 8, false);
            this.getNextComponentZ((Start)componentIn, listIn, rand, 3, 8, false);
        }
        
        public static Crossing3 createPiece(final List<StructureComponent> p_175885_0_, final Random p_175885_1_, final int p_175885_2_, final int p_175885_3_, final int p_175885_4_, final EnumFacing p_175885_5_, final int p_175885_6_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175885_2_, p_175885_3_, p_175885_4_, -8, -3, 0, 19, 10, 19, p_175885_5_);
            return (Piece.isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(p_175885_0_, structureboundingbox) == null) ? new Crossing3(p_175885_6_, p_175885_1_, structureboundingbox, p_175885_5_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 0, 11, 4, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 7, 18, 4, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 0, 10, 7, 18, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 8, 18, 7, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 5, 0, 7, 5, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 5, 11, 7, 5, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 0, 11, 5, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 11, 11, 5, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 7, 7, 5, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 7, 18, 5, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 11, 7, 5, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 11, 18, 5, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 0, 11, 2, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 13, 11, 2, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 0, 11, 1, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 15, 11, 1, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            for (int i = 7; i <= 11; ++i) {
                for (int j = 0; j <= 2; ++j) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i, -1, 18 - j, structureBoundingBoxIn);
                }
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 7, 5, 2, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 2, 7, 18, 2, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 7, 3, 1, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 0, 7, 18, 1, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            for (int k = 0; k <= 2; ++k) {
                for (int l = 7; l <= 11; ++l) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), k, -1, l, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), 18 - k, -1, l, structureBoundingBoxIn);
                }
            }
            return true;
        }
    }
    
    public static class End extends Piece
    {
        private int fillSeed;
        
        public End() {
        }
        
        public End(final int p_i45621_1_, final Random p_i45621_2_, final StructureBoundingBox p_i45621_3_, final EnumFacing p_i45621_4_) {
            super(p_i45621_1_);
            this.setCoordBaseMode(p_i45621_4_);
            this.boundingBox = p_i45621_3_;
            this.fillSeed = p_i45621_2_.nextInt();
        }
        
        public static End createPiece(final List<StructureComponent> p_175884_0_, final Random p_175884_1_, final int p_175884_2_, final int p_175884_3_, final int p_175884_4_, final EnumFacing p_175884_5_, final int p_175884_6_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175884_2_, p_175884_3_, p_175884_4_, -1, -3, 0, 5, 10, 8, p_175884_5_);
            return (Piece.isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(p_175884_0_, structureboundingbox) == null) ? new End(p_175884_6_, p_175884_1_, structureboundingbox, p_175884_5_) : null;
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.fillSeed = tagCompound.getInteger("Seed");
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setInteger("Seed", this.fillSeed);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            final Random random = new Random(this.fillSeed);
            for (int i = 0; i <= 4; ++i) {
                for (int j = 3; j <= 4; ++j) {
                    final int k = random.nextInt(8);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, i, j, 0, i, j, k, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                }
            }
            int l = random.nextInt(8);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 0, 5, l, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            l = random.nextInt(8);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 0, 4, 5, l, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            for (int i2 = 0; i2 <= 4; ++i2) {
                final int k2 = random.nextInt(5);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, i2, 2, 0, i2, 2, k2, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            }
            for (int j2 = 0; j2 <= 4; ++j2) {
                for (int l2 = 0; l2 <= 1; ++l2) {
                    final int i3 = random.nextInt(3);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, j2, l2, 0, j2, l2, i3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                }
            }
            return true;
        }
    }
    
    public static class Entrance extends Piece
    {
        public Entrance() {
        }
        
        public Entrance(final int p_i45617_1_, final Random p_i45617_2_, final StructureBoundingBox p_i45617_3_, final EnumFacing p_i45617_4_) {
            super(p_i45617_1_);
            this.setCoordBaseMode(p_i45617_4_);
            this.boundingBox = p_i45617_3_;
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            this.getNextComponentNormal((Start)componentIn, listIn, rand, 5, 3, true);
        }
        
        public static Entrance createPiece(final List<StructureComponent> p_175881_0_, final Random p_175881_1_, final int p_175881_2_, final int p_175881_3_, final int p_175881_4_, final EnumFacing p_175881_5_, final int p_175881_6_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175881_2_, p_175881_3_, p_175881_4_, -5, -3, 0, 13, 14, 13, p_175881_5_);
            return (Piece.isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(p_175881_0_, structureboundingbox) == null) ? new Entrance(p_175881_6_, p_175881_1_, structureboundingbox, p_175881_5_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 12, 13, 12, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 8, 0, 7, 8, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            for (int i = 1; i <= 11; i += 2) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, i, 10, 0, i, 11, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, i, 10, 12, i, 11, 12, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 10, i, 0, 11, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 10, i, 12, 11, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i, 13, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i, 13, 12, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK.getDefaultState(), 0, 13, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK.getDefaultState(), 12, 13, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), i + 1, 13, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), i + 1, 13, 12, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, i + 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 12, 13, i + 1, structureBoundingBoxIn);
            }
            this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 12, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 12, 13, 0, structureBoundingBoxIn);
            for (int k = 3; k <= 9; k += 2) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, k, 1, 8, k, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 7, k, 11, 8, k, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            for (int l = 4; l <= 8; ++l) {
                for (int j = 0; j <= 2; ++j) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), l, -1, j, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), l, -1, 12 - j, structureBoundingBoxIn);
                }
            }
            for (int i2 = 0; i2 <= 2; ++i2) {
                for (int j2 = 4; j2 <= 8; ++j2) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i2, -1, j2, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), 12 - i2, -1, j2, structureBoundingBoxIn);
                }
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 5, 7, 5, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 6, 6, 4, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.setBlockState(worldIn, Blocks.NETHER_BRICK.getDefaultState(), 6, 0, 6, structureBoundingBoxIn);
            final IBlockState iblockstate = Blocks.FLOWING_LAVA.getDefaultState();
            this.setBlockState(worldIn, iblockstate, 6, 5, 6, structureBoundingBoxIn);
            final BlockPos blockpos = new BlockPos(this.getXWithOffset(6, 6), this.getYWithOffset(5), this.getZWithOffset(6, 6));
            if (structureBoundingBoxIn.isVecInside(blockpos)) {
                worldIn.immediateBlockTick(blockpos, iblockstate, randomIn);
            }
            return true;
        }
    }
    
    public static class NetherStalkRoom extends Piece
    {
        public NetherStalkRoom() {
        }
        
        public NetherStalkRoom(final int p_i45612_1_, final Random p_i45612_2_, final StructureBoundingBox p_i45612_3_, final EnumFacing p_i45612_4_) {
            super(p_i45612_1_);
            this.setCoordBaseMode(p_i45612_4_);
            this.boundingBox = p_i45612_3_;
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            this.getNextComponentNormal((Start)componentIn, listIn, rand, 5, 3, true);
            this.getNextComponentNormal((Start)componentIn, listIn, rand, 5, 11, true);
        }
        
        public static NetherStalkRoom createPiece(final List<StructureComponent> p_175875_0_, final Random p_175875_1_, final int p_175875_2_, final int p_175875_3_, final int p_175875_4_, final EnumFacing p_175875_5_, final int p_175875_6_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175875_2_, p_175875_3_, p_175875_4_, -5, -3, 0, 13, 14, 13, p_175875_5_);
            return (Piece.isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(p_175875_0_, structureboundingbox) == null) ? new NetherStalkRoom(p_175875_6_, p_175875_1_, structureboundingbox, p_175875_5_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 12, 13, 12, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            for (int i = 1; i <= 11; i += 2) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, i, 10, 0, i, 11, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, i, 10, 12, i, 11, 12, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 10, i, 0, 11, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 10, i, 12, 11, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i, 13, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i, 13, 12, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK.getDefaultState(), 0, 13, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK.getDefaultState(), 12, 13, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), i + 1, 13, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), i + 1, 13, 12, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, i + 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 12, 13, i + 1, structureBoundingBoxIn);
            }
            this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 12, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 12, 13, 0, structureBoundingBoxIn);
            for (int j1 = 3; j1 <= 9; j1 += 2) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, j1, 1, 8, j1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 7, j1, 11, 8, j1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            }
            final IBlockState iblockstate = Blocks.NETHER_BRICK_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.NORTH);
            for (int k = 0; k <= 6; ++k) {
                final int l = k + 4;
                for (int m = 5; m <= 7; ++m) {
                    this.setBlockState(worldIn, iblockstate, m, 5 + k, l, structureBoundingBoxIn);
                }
                if (l >= 5 && l <= 8) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, l, 7, k + 4, l, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                }
                else if (l >= 9 && l <= 10) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 8, l, 7, k + 4, l, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                }
                if (k >= 1) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 6 + k, l, 7, 9 + k, l, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }
            }
            for (int k2 = 5; k2 <= 7; ++k2) {
                this.setBlockState(worldIn, iblockstate, k2, 12, 11, structureBoundingBoxIn);
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 6, 7, 5, 7, 7, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 6, 7, 7, 7, 7, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 13, 12, 7, 13, 12, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 2, 3, 5, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 9, 3, 5, 10, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 4, 2, 5, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 5, 2, 10, 5, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 5, 9, 10, 5, 10, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 5, 4, 10, 5, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            final IBlockState iblockstate2 = iblockstate.withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.EAST);
            final IBlockState iblockstate3 = iblockstate.withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.WEST);
            this.setBlockState(worldIn, iblockstate3, 4, 5, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate3, 4, 5, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate3, 4, 5, 9, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate3, 4, 5, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 8, 5, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 8, 5, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 8, 5, 9, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 8, 5, 10, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 4, 4, 4, 8, Blocks.SOUL_SAND.getDefaultState(), Blocks.SOUL_SAND.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 4, 4, 9, 4, 8, Blocks.SOUL_SAND.getDefaultState(), Blocks.SOUL_SAND.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 5, 4, 4, 5, 8, Blocks.NETHER_WART.getDefaultState(), Blocks.NETHER_WART.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 4, 9, 5, 8, Blocks.NETHER_WART.getDefaultState(), Blocks.NETHER_WART.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            for (int l2 = 4; l2 <= 8; ++l2) {
                for (int i2 = 0; i2 <= 2; ++i2) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), l2, -1, i2, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), l2, -1, 12 - i2, structureBoundingBoxIn);
                }
            }
            for (int i3 = 0; i3 <= 2; ++i3) {
                for (int j2 = 4; j2 <= 8; ++j2) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i3, -1, j2, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), 12 - i3, -1, j2, structureBoundingBoxIn);
                }
            }
            return true;
        }
    }
    
    abstract static class Piece extends StructureComponent
    {
        public Piece() {
        }
        
        protected Piece(final int p_i2054_1_) {
            super(p_i2054_1_);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
        }
        
        private int getTotalWeight(final List<PieceWeight> p_74960_1_) {
            boolean flag = false;
            int i = 0;
            for (final PieceWeight structurenetherbridgepieces$pieceweight : p_74960_1_) {
                if (structurenetherbridgepieces$pieceweight.maxPlaceCount > 0 && structurenetherbridgepieces$pieceweight.placeCount < structurenetherbridgepieces$pieceweight.maxPlaceCount) {
                    flag = true;
                }
                i += structurenetherbridgepieces$pieceweight.weight;
            }
            return flag ? i : -1;
        }
        
        private Piece generatePiece(final Start p_175871_1_, final List<PieceWeight> p_175871_2_, final List<StructureComponent> p_175871_3_, final Random p_175871_4_, final int p_175871_5_, final int p_175871_6_, final int p_175871_7_, final EnumFacing p_175871_8_, final int p_175871_9_) {
            final int i = this.getTotalWeight(p_175871_2_);
            final boolean flag = i > 0 && p_175871_9_ <= 30;
            int j = 0;
            while (j < 5 && flag) {
                ++j;
                int k = p_175871_4_.nextInt(i);
                for (final PieceWeight structurenetherbridgepieces$pieceweight : p_175871_2_) {
                    k -= structurenetherbridgepieces$pieceweight.weight;
                    if (k < 0) {
                        if (!structurenetherbridgepieces$pieceweight.doPlace(p_175871_9_)) {
                            break;
                        }
                        if (structurenetherbridgepieces$pieceweight == p_175871_1_.lastPlaced && !structurenetherbridgepieces$pieceweight.allowInRow) {
                            break;
                        }
                        final Piece structurenetherbridgepieces$piece = findAndCreateBridgePieceFactory(structurenetherbridgepieces$pieceweight, p_175871_3_, p_175871_4_, p_175871_5_, p_175871_6_, p_175871_7_, p_175871_8_, p_175871_9_);
                        if (structurenetherbridgepieces$piece != null) {
                            final PieceWeight pieceWeight = structurenetherbridgepieces$pieceweight;
                            ++pieceWeight.placeCount;
                            p_175871_1_.lastPlaced = structurenetherbridgepieces$pieceweight;
                            if (!structurenetherbridgepieces$pieceweight.isValid()) {
                                p_175871_2_.remove(structurenetherbridgepieces$pieceweight);
                            }
                            return structurenetherbridgepieces$piece;
                        }
                        continue;
                    }
                }
            }
            return End.createPiece(p_175871_3_, p_175871_4_, p_175871_5_, p_175871_6_, p_175871_7_, p_175871_8_, p_175871_9_);
        }
        
        private StructureComponent generateAndAddPiece(final Start p_175870_1_, final List<StructureComponent> p_175870_2_, final Random p_175870_3_, final int p_175870_4_, final int p_175870_5_, final int p_175870_6_, @Nullable final EnumFacing p_175870_7_, final int p_175870_8_, final boolean p_175870_9_) {
            if (Math.abs(p_175870_4_ - p_175870_1_.getBoundingBox().minX) <= 112 && Math.abs(p_175870_6_ - p_175870_1_.getBoundingBox().minZ) <= 112) {
                List<PieceWeight> list = p_175870_1_.primaryWeights;
                if (p_175870_9_) {
                    list = p_175870_1_.secondaryWeights;
                }
                final StructureComponent structurecomponent = this.generatePiece(p_175870_1_, list, p_175870_2_, p_175870_3_, p_175870_4_, p_175870_5_, p_175870_6_, p_175870_7_, p_175870_8_ + 1);
                if (structurecomponent != null) {
                    p_175870_2_.add(structurecomponent);
                    p_175870_1_.pendingChildren.add(structurecomponent);
                }
                return structurecomponent;
            }
            return End.createPiece(p_175870_2_, p_175870_3_, p_175870_4_, p_175870_5_, p_175870_6_, p_175870_7_, p_175870_8_);
        }
        
        @Nullable
        protected StructureComponent getNextComponentNormal(final Start p_74963_1_, final List<StructureComponent> p_74963_2_, final Random p_74963_3_, final int p_74963_4_, final int p_74963_5_, final boolean p_74963_6_) {
            final EnumFacing enumfacing = this.getCoordBaseMode();
            if (enumfacing != null) {
                switch (enumfacing) {
                    case NORTH: {
                        return this.generateAndAddPiece(p_74963_1_, p_74963_2_, p_74963_3_, this.boundingBox.minX + p_74963_4_, this.boundingBox.minY + p_74963_5_, this.boundingBox.minZ - 1, enumfacing, this.getComponentType(), p_74963_6_);
                    }
                    case SOUTH: {
                        return this.generateAndAddPiece(p_74963_1_, p_74963_2_, p_74963_3_, this.boundingBox.minX + p_74963_4_, this.boundingBox.minY + p_74963_5_, this.boundingBox.maxZ + 1, enumfacing, this.getComponentType(), p_74963_6_);
                    }
                    case WEST: {
                        return this.generateAndAddPiece(p_74963_1_, p_74963_2_, p_74963_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74963_5_, this.boundingBox.minZ + p_74963_4_, enumfacing, this.getComponentType(), p_74963_6_);
                    }
                    case EAST: {
                        return this.generateAndAddPiece(p_74963_1_, p_74963_2_, p_74963_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74963_5_, this.boundingBox.minZ + p_74963_4_, enumfacing, this.getComponentType(), p_74963_6_);
                    }
                }
            }
            return null;
        }
        
        @Nullable
        protected StructureComponent getNextComponentX(final Start p_74961_1_, final List<StructureComponent> p_74961_2_, final Random p_74961_3_, final int p_74961_4_, final int p_74961_5_, final boolean p_74961_6_) {
            final EnumFacing enumfacing = this.getCoordBaseMode();
            if (enumfacing != null) {
                switch (enumfacing) {
                    case NORTH: {
                        return this.generateAndAddPiece(p_74961_1_, p_74961_2_, p_74961_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74961_4_, this.boundingBox.minZ + p_74961_5_, EnumFacing.WEST, this.getComponentType(), p_74961_6_);
                    }
                    case SOUTH: {
                        return this.generateAndAddPiece(p_74961_1_, p_74961_2_, p_74961_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74961_4_, this.boundingBox.minZ + p_74961_5_, EnumFacing.WEST, this.getComponentType(), p_74961_6_);
                    }
                    case WEST: {
                        return this.generateAndAddPiece(p_74961_1_, p_74961_2_, p_74961_3_, this.boundingBox.minX + p_74961_5_, this.boundingBox.minY + p_74961_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType(), p_74961_6_);
                    }
                    case EAST: {
                        return this.generateAndAddPiece(p_74961_1_, p_74961_2_, p_74961_3_, this.boundingBox.minX + p_74961_5_, this.boundingBox.minY + p_74961_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType(), p_74961_6_);
                    }
                }
            }
            return null;
        }
        
        @Nullable
        protected StructureComponent getNextComponentZ(final Start p_74965_1_, final List<StructureComponent> p_74965_2_, final Random p_74965_3_, final int p_74965_4_, final int p_74965_5_, final boolean p_74965_6_) {
            final EnumFacing enumfacing = this.getCoordBaseMode();
            if (enumfacing != null) {
                switch (enumfacing) {
                    case NORTH: {
                        return this.generateAndAddPiece(p_74965_1_, p_74965_2_, p_74965_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74965_4_, this.boundingBox.minZ + p_74965_5_, EnumFacing.EAST, this.getComponentType(), p_74965_6_);
                    }
                    case SOUTH: {
                        return this.generateAndAddPiece(p_74965_1_, p_74965_2_, p_74965_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74965_4_, this.boundingBox.minZ + p_74965_5_, EnumFacing.EAST, this.getComponentType(), p_74965_6_);
                    }
                    case WEST: {
                        return this.generateAndAddPiece(p_74965_1_, p_74965_2_, p_74965_3_, this.boundingBox.minX + p_74965_5_, this.boundingBox.minY + p_74965_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType(), p_74965_6_);
                    }
                    case EAST: {
                        return this.generateAndAddPiece(p_74965_1_, p_74965_2_, p_74965_3_, this.boundingBox.minX + p_74965_5_, this.boundingBox.minY + p_74965_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType(), p_74965_6_);
                    }
                }
            }
            return null;
        }
        
        protected static boolean isAboveGround(final StructureBoundingBox p_74964_0_) {
            return p_74964_0_ != null && p_74964_0_.minY > 10;
        }
    }
    
    static class PieceWeight
    {
        public Class<? extends Piece> weightClass;
        public final int weight;
        public int placeCount;
        public int maxPlaceCount;
        public boolean allowInRow;
        
        public PieceWeight(final Class<? extends Piece> p_i2055_1_, final int p_i2055_2_, final int p_i2055_3_, final boolean p_i2055_4_) {
            this.weightClass = p_i2055_1_;
            this.weight = p_i2055_2_;
            this.maxPlaceCount = p_i2055_3_;
            this.allowInRow = p_i2055_4_;
        }
        
        public PieceWeight(final Class<? extends Piece> p_i2056_1_, final int p_i2056_2_, final int p_i2056_3_) {
            this(p_i2056_1_, p_i2056_2_, p_i2056_3_, false);
        }
        
        public boolean doPlace(final int p_78822_1_) {
            return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
        }
        
        public boolean isValid() {
            return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
        }
    }
    
    public static class Stairs extends Piece
    {
        public Stairs() {
        }
        
        public Stairs(final int p_i45609_1_, final Random p_i45609_2_, final StructureBoundingBox p_i45609_3_, final EnumFacing p_i45609_4_) {
            super(p_i45609_1_);
            this.setCoordBaseMode(p_i45609_4_);
            this.boundingBox = p_i45609_3_;
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            this.getNextComponentZ((Start)componentIn, listIn, rand, 6, 2, false);
        }
        
        public static Stairs createPiece(final List<StructureComponent> p_175872_0_, final Random p_175872_1_, final int p_175872_2_, final int p_175872_3_, final int p_175872_4_, final int p_175872_5_, final EnumFacing p_175872_6_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175872_2_, p_175872_3_, p_175872_4_, -2, 0, 0, 7, 11, 7, p_175872_6_);
            return (Piece.isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(p_175872_0_, structureboundingbox) == null) ? new Stairs(p_175872_5_, p_175872_1_, structureboundingbox, p_175872_6_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 6, 10, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 1, 8, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 0, 6, 8, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 1, 0, 8, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 1, 6, 8, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 6, 5, 8, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 2, 0, 5, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 2, 6, 5, 2, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 4, 6, 5, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.setBlockState(worldIn, Blocks.NETHER_BRICK.getDefaultState(), 5, 2, 5, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 5, 4, 3, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 5, 3, 4, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 5, 2, 5, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 5, 1, 6, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, 1, 5, 7, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 8, 2, 6, 8, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 6, 0, 4, 8, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 0, 4, 5, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            for (int i = 0; i <= 6; ++i) {
                for (int j = 0; j <= 6; ++j) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureBoundingBoxIn);
                }
            }
            return true;
        }
    }
    
    public static class Start extends Crossing3
    {
        public PieceWeight lastPlaced;
        public List<PieceWeight> primaryWeights;
        public List<PieceWeight> secondaryWeights;
        public List<StructureComponent> pendingChildren;
        
        public Start() {
            this.pendingChildren = (List<StructureComponent>)Lists.newArrayList();
        }
        
        public Start(final Random p_i2059_1_, final int p_i2059_2_, final int p_i2059_3_) {
            super(p_i2059_1_, p_i2059_2_, p_i2059_3_);
            this.pendingChildren = (List<StructureComponent>)Lists.newArrayList();
            this.primaryWeights = (List<PieceWeight>)Lists.newArrayList();
            for (final PieceWeight structurenetherbridgepieces$pieceweight : StructureNetherBridgePieces.PRIMARY_COMPONENTS) {
                structurenetherbridgepieces$pieceweight.placeCount = 0;
                this.primaryWeights.add(structurenetherbridgepieces$pieceweight);
            }
            this.secondaryWeights = (List<PieceWeight>)Lists.newArrayList();
            for (final PieceWeight structurenetherbridgepieces$pieceweight2 : StructureNetherBridgePieces.SECONDARY_COMPONENTS) {
                structurenetherbridgepieces$pieceweight2.placeCount = 0;
                this.secondaryWeights.add(structurenetherbridgepieces$pieceweight2);
            }
        }
    }
    
    public static class Straight extends Piece
    {
        public Straight() {
        }
        
        public Straight(final int p_i45620_1_, final Random p_i45620_2_, final StructureBoundingBox p_i45620_3_, final EnumFacing p_i45620_4_) {
            super(p_i45620_1_);
            this.setCoordBaseMode(p_i45620_4_);
            this.boundingBox = p_i45620_3_;
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            this.getNextComponentNormal((Start)componentIn, listIn, rand, 1, 3, false);
        }
        
        public static Straight createPiece(final List<StructureComponent> p_175882_0_, final Random p_175882_1_, final int p_175882_2_, final int p_175882_3_, final int p_175882_4_, final EnumFacing p_175882_5_, final int p_175882_6_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175882_2_, p_175882_3_, p_175882_4_, -1, -3, 0, 5, 10, 19, p_175882_5_);
            return (Piece.isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(p_175882_0_, structureboundingbox) == null) ? new Straight(p_175882_6_, p_175882_1_, structureboundingbox, p_175882_5_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 4, 4, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 0, 3, 7, 18, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 0, 5, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 0, 4, 5, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 4, 2, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 13, 4, 2, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 1, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 15, 4, 1, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 2; ++j) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i, -1, 18 - j, structureBoundingBoxIn);
                }
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 4, 0, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 14, 0, 4, 14, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 17, 0, 4, 17, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 4, 4, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 14, 4, 4, 14, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 17, 4, 4, 17, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            return true;
        }
    }
    
    public static class Throne extends Piece
    {
        private boolean hasSpawner;
        
        public Throne() {
        }
        
        public Throne(final int p_i45611_1_, final Random p_i45611_2_, final StructureBoundingBox p_i45611_3_, final EnumFacing p_i45611_4_) {
            super(p_i45611_1_);
            this.setCoordBaseMode(p_i45611_4_);
            this.boundingBox = p_i45611_3_;
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.hasSpawner = tagCompound.getBoolean("Mob");
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setBoolean("Mob", this.hasSpawner);
        }
        
        public static Throne createPiece(final List<StructureComponent> p_175874_0_, final Random p_175874_1_, final int p_175874_2_, final int p_175874_3_, final int p_175874_4_, final int p_175874_5_, final EnumFacing p_175874_6_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175874_2_, p_175874_3_, p_175874_4_, -2, 0, 0, 7, 8, 9, p_175874_6_);
            return (Piece.isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(p_175874_0_, structureboundingbox) == null) ? new Throne(p_175874_5_, p_175874_1_, structureboundingbox, p_175874_6_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 6, 7, 7, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 5, 1, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 1, 5, 2, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 2, 5, 3, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 3, 5, 4, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 1, 4, 2, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 0, 5, 4, 2, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 2, 1, 5, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 2, 5, 5, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 3, 0, 5, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 5, 3, 6, 5, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 8, 5, 5, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 1, 6, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 5, 6, 3, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 3, 0, 6, 8, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 6, 3, 6, 6, 8, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 8, 5, 7, 8, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 8, 8, 4, 8, 8, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            if (!this.hasSpawner) {
                final BlockPos blockpos = new BlockPos(this.getXWithOffset(3, 5), this.getYWithOffset(5), this.getZWithOffset(3, 5));
                if (structureBoundingBoxIn.isVecInside(blockpos)) {
                    this.hasSpawner = true;
                    worldIn.setBlockState(blockpos, Blocks.MOB_SPAWNER.getDefaultState(), 2);
                    final TileEntity tileentity = worldIn.getTileEntity(blockpos);
                    if (tileentity instanceof TileEntityMobSpawner) {
                        ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntityBlaze.class));
                    }
                }
            }
            for (int i = 0; i <= 6; ++i) {
                for (int j = 0; j <= 6; ++j) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureBoundingBoxIn);
                }
            }
            return true;
        }
    }
}
