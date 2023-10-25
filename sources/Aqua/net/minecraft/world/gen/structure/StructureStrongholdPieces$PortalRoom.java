package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;

/*
 * Exception performing whole class analysis ignored.
 */
public static class StructureStrongholdPieces.PortalRoom
extends StructureStrongholdPieces.Stronghold {
    private boolean hasSpawner;

    public StructureStrongholdPieces.PortalRoom() {
    }

    public StructureStrongholdPieces.PortalRoom(int p_i45577_1_, Random p_i45577_2_, StructureBoundingBox p_i45577_3_, EnumFacing p_i45577_4_) {
        super(p_i45577_1_);
        this.coordBaseMode = p_i45577_4_;
        this.boundingBox = p_i45577_3_;
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setBoolean("Mob", this.hasSpawner);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.hasSpawner = tagCompound.getBoolean("Mob");
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        if (componentIn != null) {
            ((StructureStrongholdPieces.Stairs2)componentIn).strongholdPortalRoom = this;
        }
    }

    public static StructureStrongholdPieces.PortalRoom func_175865_a(List<StructureComponent> p_175865_0_, Random p_175865_1_, int p_175865_2_, int p_175865_3_, int p_175865_4_, EnumFacing p_175865_5_, int p_175865_6_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175865_2_, (int)p_175865_3_, (int)p_175865_4_, (int)-4, (int)-1, (int)0, (int)11, (int)8, (int)16, (EnumFacing)p_175865_5_);
        return StructureStrongholdPieces.PortalRoom.canStrongholdGoDeeper((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175865_0_, (StructureBoundingBox)structureboundingbox) == null ? new StructureStrongholdPieces.PortalRoom(p_175865_6_, p_175865_1_, structureboundingbox, p_175865_5_) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 10, 7, 15, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.placeDoor(worldIn, randomIn, structureBoundingBoxIn, StructureStrongholdPieces.Stronghold.Door.GRATES, 4, 1, 0);
        int i = 6;
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, i, 1, 1, i, 14, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, i, 1, 9, i, 14, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, i, 1, 8, i, 2, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, i, 14, 8, i, 14, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 2, 1, 4, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 8, 1, 1, 9, 1, 4, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 1, 1, 3, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 1, 9, 1, 3, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 3, 1, 8, 7, 1, 12, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 9, 6, 1, 11, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
        for (int j = 3; j < 14; j += 2) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, j, 0, 4, j, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 3, j, 10, 4, j, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
        }
        for (int k1 = 2; k1 < 9; k1 += 2) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, k1, 3, 15, k1, 4, 15, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
        }
        int l1 = this.getMetadataWithOffset(Blocks.stone_brick_stairs, 3);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 5, 6, 1, 7, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 2, 6, 6, 2, 7, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 3, 7, 6, 3, 7, false, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        for (int k = 4; k <= 6; ++k) {
            this.setBlockState(worldIn, Blocks.stone_brick_stairs.getStateFromMeta(l1), k, 1, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_brick_stairs.getStateFromMeta(l1), k, 2, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_brick_stairs.getStateFromMeta(l1), k, 3, 6, structureBoundingBoxIn);
        }
        int i2 = EnumFacing.NORTH.getHorizontalIndex();
        int l = EnumFacing.SOUTH.getHorizontalIndex();
        int i1 = EnumFacing.EAST.getHorizontalIndex();
        int j1 = EnumFacing.WEST.getHorizontalIndex();
        if (this.coordBaseMode != null) {
            switch (StructureStrongholdPieces.3.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
                case 1: {
                    i2 = EnumFacing.SOUTH.getHorizontalIndex();
                    l = EnumFacing.NORTH.getHorizontalIndex();
                    break;
                }
                case 2: {
                    i2 = EnumFacing.WEST.getHorizontalIndex();
                    l = EnumFacing.EAST.getHorizontalIndex();
                    i1 = EnumFacing.SOUTH.getHorizontalIndex();
                    j1 = EnumFacing.NORTH.getHorizontalIndex();
                    break;
                }
                case 3: {
                    i2 = EnumFacing.EAST.getHorizontalIndex();
                    l = EnumFacing.WEST.getHorizontalIndex();
                    i1 = EnumFacing.SOUTH.getHorizontalIndex();
                    j1 = EnumFacing.NORTH.getHorizontalIndex();
                }
            }
        }
        this.setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i2).withProperty((IProperty)BlockEndPortalFrame.EYE, (Comparable)Boolean.valueOf((randomIn.nextFloat() > 0.9f ? 1 : 0) != 0)), 4, 3, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i2).withProperty((IProperty)BlockEndPortalFrame.EYE, (Comparable)Boolean.valueOf((randomIn.nextFloat() > 0.9f ? 1 : 0) != 0)), 5, 3, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i2).withProperty((IProperty)BlockEndPortalFrame.EYE, (Comparable)Boolean.valueOf((randomIn.nextFloat() > 0.9f ? 1 : 0) != 0)), 6, 3, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(l).withProperty((IProperty)BlockEndPortalFrame.EYE, (Comparable)Boolean.valueOf((randomIn.nextFloat() > 0.9f ? 1 : 0) != 0)), 4, 3, 12, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(l).withProperty((IProperty)BlockEndPortalFrame.EYE, (Comparable)Boolean.valueOf((randomIn.nextFloat() > 0.9f ? 1 : 0) != 0)), 5, 3, 12, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(l).withProperty((IProperty)BlockEndPortalFrame.EYE, (Comparable)Boolean.valueOf((randomIn.nextFloat() > 0.9f ? 1 : 0) != 0)), 6, 3, 12, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i1).withProperty((IProperty)BlockEndPortalFrame.EYE, (Comparable)Boolean.valueOf((randomIn.nextFloat() > 0.9f ? 1 : 0) != 0)), 3, 3, 9, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i1).withProperty((IProperty)BlockEndPortalFrame.EYE, (Comparable)Boolean.valueOf((randomIn.nextFloat() > 0.9f ? 1 : 0) != 0)), 3, 3, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i1).withProperty((IProperty)BlockEndPortalFrame.EYE, (Comparable)Boolean.valueOf((randomIn.nextFloat() > 0.9f ? 1 : 0) != 0)), 3, 3, 11, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(j1).withProperty((IProperty)BlockEndPortalFrame.EYE, (Comparable)Boolean.valueOf((randomIn.nextFloat() > 0.9f ? 1 : 0) != 0)), 7, 3, 9, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(j1).withProperty((IProperty)BlockEndPortalFrame.EYE, (Comparable)Boolean.valueOf((randomIn.nextFloat() > 0.9f ? 1 : 0) != 0)), 7, 3, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(j1).withProperty((IProperty)BlockEndPortalFrame.EYE, (Comparable)Boolean.valueOf((randomIn.nextFloat() > 0.9f ? 1 : 0) != 0)), 7, 3, 11, structureBoundingBoxIn);
        if (!this.hasSpawner) {
            i = this.getYWithOffset(3);
            BlockPos blockpos = new BlockPos(this.getXWithOffset(5, 6), i, this.getZWithOffset(5, 6));
            if (structureBoundingBoxIn.isVecInside((Vec3i)blockpos)) {
                this.hasSpawner = true;
                worldIn.setBlockState(blockpos, Blocks.mob_spawner.getDefaultState(), 2);
                TileEntity tileentity = worldIn.getTileEntity(blockpos);
                if (tileentity instanceof TileEntityMobSpawner) {
                    ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityName("Silverfish");
                }
            }
        }
        return true;
    }
}
