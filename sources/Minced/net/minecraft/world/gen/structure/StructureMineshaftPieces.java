// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTorch;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRail;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.nbt.NBTTagCompound;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import java.util.Random;
import java.util.List;

public class StructureMineshaftPieces
{
    public static void registerStructurePieces() {
        MapGenStructureIO.registerStructureComponent(Corridor.class, "MSCorridor");
        MapGenStructureIO.registerStructureComponent(Cross.class, "MSCrossing");
        MapGenStructureIO.registerStructureComponent(Room.class, "MSRoom");
        MapGenStructureIO.registerStructureComponent(Stairs.class, "MSStairs");
    }
    
    private static Peice createRandomShaftPiece(final List<StructureComponent> p_189940_0_, final Random p_189940_1_, final int p_189940_2_, final int p_189940_3_, final int p_189940_4_, @Nullable final EnumFacing p_189940_5_, final int p_189940_6_, final MapGenMineshaft.Type p_189940_7_) {
        final int i = p_189940_1_.nextInt(100);
        if (i >= 80) {
            final StructureBoundingBox structureboundingbox = Cross.findCrossing(p_189940_0_, p_189940_1_, p_189940_2_, p_189940_3_, p_189940_4_, p_189940_5_);
            if (structureboundingbox != null) {
                return new Cross(p_189940_6_, p_189940_1_, structureboundingbox, p_189940_5_, p_189940_7_);
            }
        }
        else if (i >= 70) {
            final StructureBoundingBox structureboundingbox2 = Stairs.findStairs(p_189940_0_, p_189940_1_, p_189940_2_, p_189940_3_, p_189940_4_, p_189940_5_);
            if (structureboundingbox2 != null) {
                return new Stairs(p_189940_6_, p_189940_1_, structureboundingbox2, p_189940_5_, p_189940_7_);
            }
        }
        else {
            final StructureBoundingBox structureboundingbox3 = Corridor.findCorridorSize(p_189940_0_, p_189940_1_, p_189940_2_, p_189940_3_, p_189940_4_, p_189940_5_);
            if (structureboundingbox3 != null) {
                return new Corridor(p_189940_6_, p_189940_1_, structureboundingbox3, p_189940_5_, p_189940_7_);
            }
        }
        return null;
    }
    
    private static Peice generateAndAddPiece(final StructureComponent p_189938_0_, final List<StructureComponent> p_189938_1_, final Random p_189938_2_, final int p_189938_3_, final int p_189938_4_, final int p_189938_5_, final EnumFacing p_189938_6_, final int p_189938_7_) {
        if (p_189938_7_ > 8) {
            return null;
        }
        if (Math.abs(p_189938_3_ - p_189938_0_.getBoundingBox().minX) <= 80 && Math.abs(p_189938_5_ - p_189938_0_.getBoundingBox().minZ) <= 80) {
            final MapGenMineshaft.Type mapgenmineshaft$type = ((Peice)p_189938_0_).mineShaftType;
            final Peice structuremineshaftpieces$peice = createRandomShaftPiece(p_189938_1_, p_189938_2_, p_189938_3_, p_189938_4_, p_189938_5_, p_189938_6_, p_189938_7_ + 1, mapgenmineshaft$type);
            if (structuremineshaftpieces$peice != null) {
                p_189938_1_.add(structuremineshaftpieces$peice);
                structuremineshaftpieces$peice.buildComponent(p_189938_0_, p_189938_1_, p_189938_2_);
            }
            return structuremineshaftpieces$peice;
        }
        return null;
    }
    
    public static class Corridor extends Peice
    {
        private boolean hasRails;
        private boolean hasSpiders;
        private boolean spawnerPlaced;
        private int sectionCount;
        
        public Corridor() {
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setBoolean("hr", this.hasRails);
            tagCompound.setBoolean("sc", this.hasSpiders);
            tagCompound.setBoolean("hps", this.spawnerPlaced);
            tagCompound.setInteger("Num", this.sectionCount);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.hasRails = tagCompound.getBoolean("hr");
            this.hasSpiders = tagCompound.getBoolean("sc");
            this.spawnerPlaced = tagCompound.getBoolean("hps");
            this.sectionCount = tagCompound.getInteger("Num");
        }
        
        public Corridor(final int p_i47140_1_, final Random p_i47140_2_, final StructureBoundingBox p_i47140_3_, final EnumFacing p_i47140_4_, final MapGenMineshaft.Type p_i47140_5_) {
            super(p_i47140_1_, p_i47140_5_);
            this.setCoordBaseMode(p_i47140_4_);
            this.boundingBox = p_i47140_3_;
            this.hasRails = (p_i47140_2_.nextInt(3) == 0);
            this.hasSpiders = (!this.hasRails && p_i47140_2_.nextInt(23) == 0);
            if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
                this.sectionCount = p_i47140_3_.getZSize() / 5;
            }
            else {
                this.sectionCount = p_i47140_3_.getXSize() / 5;
            }
        }
        
        public static StructureBoundingBox findCorridorSize(final List<StructureComponent> p_175814_0_, final Random rand, final int x, final int y, final int z, final EnumFacing facing) {
            final StructureBoundingBox structureboundingbox = new StructureBoundingBox(x, y, z, x, y + 2, z);
            int i;
            for (i = rand.nextInt(3) + 2; i > 0; --i) {
                final int j = i * 5;
                switch (facing) {
                    default: {
                        structureboundingbox.maxX = x + 2;
                        structureboundingbox.minZ = z - (j - 1);
                        break;
                    }
                    case SOUTH: {
                        structureboundingbox.maxX = x + 2;
                        structureboundingbox.maxZ = z + (j - 1);
                        break;
                    }
                    case WEST: {
                        structureboundingbox.minX = x - (j - 1);
                        structureboundingbox.maxZ = z + 2;
                        break;
                    }
                    case EAST: {
                        structureboundingbox.maxX = x + (j - 1);
                        structureboundingbox.maxZ = z + 2;
                        break;
                    }
                }
                if (StructureComponent.findIntersecting(p_175814_0_, structureboundingbox) == null) {
                    break;
                }
            }
            return (i > 0) ? structureboundingbox : null;
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            final int i = this.getComponentType();
            final int j = rand.nextInt(4);
            final EnumFacing enumfacing = this.getCoordBaseMode();
            if (enumfacing != null) {
                switch (enumfacing) {
                    default: {
                        if (j <= 1) {
                            generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ - 1, enumfacing, i);
                            break;
                        }
                        if (j == 2) {
                            generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, EnumFacing.WEST, i);
                            break;
                        }
                        generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, EnumFacing.EAST, i);
                        break;
                    }
                    case SOUTH: {
                        if (j <= 1) {
                            generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ + 1, enumfacing, i);
                            break;
                        }
                        if (j == 2) {
                            generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.WEST, i);
                            break;
                        }
                        generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.EAST, i);
                        break;
                    }
                    case WEST: {
                        if (j <= 1) {
                            generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, enumfacing, i);
                            break;
                        }
                        if (j == 2) {
                            generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                            break;
                        }
                        generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                        break;
                    }
                    case EAST: {
                        if (j <= 1) {
                            generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, enumfacing, i);
                            break;
                        }
                        if (j == 2) {
                            generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                            break;
                        }
                        generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                        break;
                    }
                }
            }
            if (i < 8) {
                if (enumfacing != EnumFacing.NORTH && enumfacing != EnumFacing.SOUTH) {
                    for (int i2 = this.boundingBox.minX + 3; i2 + 3 <= this.boundingBox.maxX; i2 += 5) {
                        final int j2 = rand.nextInt(5);
                        if (j2 == 0) {
                            generateAndAddPiece(componentIn, listIn, rand, i2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i + 1);
                        }
                        else if (j2 == 1) {
                            generateAndAddPiece(componentIn, listIn, rand, i2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i + 1);
                        }
                    }
                }
                else {
                    for (int k = this.boundingBox.minZ + 3; k + 3 <= this.boundingBox.maxZ; k += 5) {
                        final int l = rand.nextInt(5);
                        if (l == 0) {
                            generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, k, EnumFacing.WEST, i + 1);
                        }
                        else if (l == 1) {
                            generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, k, EnumFacing.EAST, i + 1);
                        }
                    }
                }
            }
        }
        
        @Override
        protected boolean generateChest(final World worldIn, final StructureBoundingBox structurebb, final Random randomIn, final int x, final int y, final int z, final ResourceLocation loot) {
            final BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
            if (structurebb.isVecInside(blockpos) && worldIn.getBlockState(blockpos).getMaterial() == Material.AIR && worldIn.getBlockState(blockpos.down()).getMaterial() != Material.AIR) {
                final IBlockState iblockstate = Blocks.RAIL.getDefaultState().withProperty(BlockRail.SHAPE, randomIn.nextBoolean() ? BlockRailBase.EnumRailDirection.NORTH_SOUTH : BlockRailBase.EnumRailDirection.EAST_WEST);
                this.setBlockState(worldIn, iblockstate, x, y, z, structurebb);
                final EntityMinecartChest entityminecartchest = new EntityMinecartChest(worldIn, blockpos.getX() + 0.5f, blockpos.getY() + 0.5f, blockpos.getZ() + 0.5f);
                entityminecartchest.setLootTable(loot, randomIn.nextLong());
                worldIn.spawnEntity(entityminecartchest);
                return true;
            }
            return false;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
                return false;
            }
            final int i = 0;
            final int j = 2;
            final int k = 0;
            final int l = 2;
            final int i2 = this.sectionCount * 5 - 1;
            final IBlockState iblockstate = this.getPlanksBlock();
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 2, 1, i2, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.generateMaybeBox(worldIn, structureBoundingBoxIn, randomIn, 0.8f, 0, 2, 0, 2, 2, i2, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false, 0);
            if (this.hasSpiders) {
                this.generateMaybeBox(worldIn, structureBoundingBoxIn, randomIn, 0.6f, 0, 0, 0, 2, 1, i2, Blocks.WEB.getDefaultState(), Blocks.AIR.getDefaultState(), false, 8);
            }
            for (int j2 = 0; j2 < this.sectionCount; ++j2) {
                final int k2 = 2 + j2 * 5;
                this.placeSupport(worldIn, structureBoundingBoxIn, 0, 0, k2, 2, 2, randomIn);
                this.placeCobWeb(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 0, 2, k2 - 1);
                this.placeCobWeb(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 2, 2, k2 - 1);
                this.placeCobWeb(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 0, 2, k2 + 1);
                this.placeCobWeb(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 2, 2, k2 + 1);
                this.placeCobWeb(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 0, 2, k2 - 2);
                this.placeCobWeb(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 2, 2, k2 - 2);
                this.placeCobWeb(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 0, 2, k2 + 2);
                this.placeCobWeb(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 2, 2, k2 + 2);
                if (randomIn.nextInt(100) == 0) {
                    this.generateChest(worldIn, structureBoundingBoxIn, randomIn, 2, 0, k2 - 1, LootTableList.CHESTS_ABANDONED_MINESHAFT);
                }
                if (randomIn.nextInt(100) == 0) {
                    this.generateChest(worldIn, structureBoundingBoxIn, randomIn, 0, 0, k2 + 1, LootTableList.CHESTS_ABANDONED_MINESHAFT);
                }
                if (this.hasSpiders && !this.spawnerPlaced) {
                    final int l2 = this.getYWithOffset(0);
                    final int i3 = k2 - 1 + randomIn.nextInt(3);
                    final int j3 = this.getXWithOffset(1, i3);
                    final int k3 = this.getZWithOffset(1, i3);
                    final BlockPos blockpos = new BlockPos(j3, l2, k3);
                    if (structureBoundingBoxIn.isVecInside(blockpos) && this.getSkyBrightness(worldIn, 1, 0, i3, structureBoundingBoxIn) < 8) {
                        this.spawnerPlaced = true;
                        worldIn.setBlockState(blockpos, Blocks.MOB_SPAWNER.getDefaultState(), 2);
                        final TileEntity tileentity = worldIn.getTileEntity(blockpos);
                        if (tileentity instanceof TileEntityMobSpawner) {
                            ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntityCaveSpider.class));
                        }
                    }
                }
            }
            for (int l3 = 0; l3 <= 2; ++l3) {
                for (int i4 = 0; i4 <= i2; ++i4) {
                    final int k4 = -1;
                    final IBlockState iblockstate2 = this.getBlockStateFromPos(worldIn, l3, -1, i4, structureBoundingBoxIn);
                    if (iblockstate2.getMaterial() == Material.AIR && this.getSkyBrightness(worldIn, l3, -1, i4, structureBoundingBoxIn) < 8) {
                        final int l4 = -1;
                        this.setBlockState(worldIn, iblockstate, l3, -1, i4, structureBoundingBoxIn);
                    }
                }
            }
            if (this.hasRails) {
                final IBlockState iblockstate3 = Blocks.RAIL.getDefaultState().withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
                for (int j4 = 0; j4 <= i2; ++j4) {
                    final IBlockState iblockstate4 = this.getBlockStateFromPos(worldIn, 1, -1, j4, structureBoundingBoxIn);
                    if (iblockstate4.getMaterial() != Material.AIR && iblockstate4.isFullBlock()) {
                        final float f = (this.getSkyBrightness(worldIn, 1, 0, j4, structureBoundingBoxIn) > 8) ? 0.9f : 0.7f;
                        this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, f, 1, 0, j4, iblockstate3);
                    }
                }
            }
            return true;
        }
        
        private void placeSupport(final World p_189921_1_, final StructureBoundingBox p_189921_2_, final int p_189921_3_, final int p_189921_4_, final int p_189921_5_, final int p_189921_6_, final int p_189921_7_, final Random p_189921_8_) {
            if (this.isSupportingBox(p_189921_1_, p_189921_2_, p_189921_3_, p_189921_7_, p_189921_6_, p_189921_5_)) {
                final IBlockState iblockstate = this.getPlanksBlock();
                final IBlockState iblockstate2 = this.getFenceBlock();
                final IBlockState iblockstate3 = Blocks.AIR.getDefaultState();
                this.fillWithBlocks(p_189921_1_, p_189921_2_, p_189921_3_, p_189921_4_, p_189921_5_, p_189921_3_, p_189921_6_ - 1, p_189921_5_, iblockstate2, iblockstate3, false);
                this.fillWithBlocks(p_189921_1_, p_189921_2_, p_189921_7_, p_189921_4_, p_189921_5_, p_189921_7_, p_189921_6_ - 1, p_189921_5_, iblockstate2, iblockstate3, false);
                if (p_189921_8_.nextInt(4) == 0) {
                    this.fillWithBlocks(p_189921_1_, p_189921_2_, p_189921_3_, p_189921_6_, p_189921_5_, p_189921_3_, p_189921_6_, p_189921_5_, iblockstate, iblockstate3, false);
                    this.fillWithBlocks(p_189921_1_, p_189921_2_, p_189921_7_, p_189921_6_, p_189921_5_, p_189921_7_, p_189921_6_, p_189921_5_, iblockstate, iblockstate3, false);
                }
                else {
                    this.fillWithBlocks(p_189921_1_, p_189921_2_, p_189921_3_, p_189921_6_, p_189921_5_, p_189921_7_, p_189921_6_, p_189921_5_, iblockstate, iblockstate3, false);
                    this.randomlyPlaceBlock(p_189921_1_, p_189921_2_, p_189921_8_, 0.05f, p_189921_3_ + 1, p_189921_6_, p_189921_5_ - 1, Blocks.TORCH.getDefaultState().withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.NORTH));
                    this.randomlyPlaceBlock(p_189921_1_, p_189921_2_, p_189921_8_, 0.05f, p_189921_3_ + 1, p_189921_6_, p_189921_5_ + 1, Blocks.TORCH.getDefaultState().withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.SOUTH));
                }
            }
        }
        
        private void placeCobWeb(final World p_189922_1_, final StructureBoundingBox p_189922_2_, final Random p_189922_3_, final float p_189922_4_, final int p_189922_5_, final int p_189922_6_, final int p_189922_7_) {
            if (this.getSkyBrightness(p_189922_1_, p_189922_5_, p_189922_6_, p_189922_7_, p_189922_2_) < 8) {
                this.randomlyPlaceBlock(p_189922_1_, p_189922_2_, p_189922_3_, p_189922_4_, p_189922_5_, p_189922_6_, p_189922_7_, Blocks.WEB.getDefaultState());
            }
        }
    }
    
    public static class Cross extends Peice
    {
        private EnumFacing corridorDirection;
        private boolean isMultipleFloors;
        
        public Cross() {
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setBoolean("tf", this.isMultipleFloors);
            tagCompound.setInteger("D", this.corridorDirection.getHorizontalIndex());
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.isMultipleFloors = tagCompound.getBoolean("tf");
            this.corridorDirection = EnumFacing.byHorizontalIndex(tagCompound.getInteger("D"));
        }
        
        public Cross(final int p_i47139_1_, final Random p_i47139_2_, final StructureBoundingBox p_i47139_3_, @Nullable final EnumFacing p_i47139_4_, final MapGenMineshaft.Type p_i47139_5_) {
            super(p_i47139_1_, p_i47139_5_);
            this.corridorDirection = p_i47139_4_;
            this.boundingBox = p_i47139_3_;
            this.isMultipleFloors = (p_i47139_3_.getYSize() > 3);
        }
        
        public static StructureBoundingBox findCrossing(final List<StructureComponent> listIn, final Random rand, final int x, final int y, final int z, final EnumFacing facing) {
            final StructureBoundingBox structureboundingbox = new StructureBoundingBox(x, y, z, x, y + 2, z);
            if (rand.nextInt(4) == 0) {
                final StructureBoundingBox structureBoundingBox = structureboundingbox;
                structureBoundingBox.maxY += 4;
            }
            switch (facing) {
                default: {
                    structureboundingbox.minX = x - 1;
                    structureboundingbox.maxX = x + 3;
                    structureboundingbox.minZ = z - 4;
                    break;
                }
                case SOUTH: {
                    structureboundingbox.minX = x - 1;
                    structureboundingbox.maxX = x + 3;
                    structureboundingbox.maxZ = z + 3 + 1;
                    break;
                }
                case WEST: {
                    structureboundingbox.minX = x - 4;
                    structureboundingbox.minZ = z - 1;
                    structureboundingbox.maxZ = z + 3;
                    break;
                }
                case EAST: {
                    structureboundingbox.maxX = x + 3 + 1;
                    structureboundingbox.minZ = z - 1;
                    structureboundingbox.maxZ = z + 3;
                    break;
                }
            }
            return (StructureComponent.findIntersecting(listIn, structureboundingbox) != null) ? null : structureboundingbox;
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            final int i = this.getComponentType();
            switch (this.corridorDirection) {
                default: {
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
                    break;
                }
                case SOUTH: {
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
                    break;
                }
                case WEST: {
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                    break;
                }
                case EAST: {
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
                    break;
                }
            }
            if (this.isMultipleFloors) {
                if (rand.nextBoolean()) {
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                }
                if (rand.nextBoolean()) {
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                }
                if (rand.nextBoolean()) {
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
                }
                if (rand.nextBoolean()) {
                    generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                }
            }
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
                return false;
            }
            final IBlockState iblockstate = this.getPlanksBlock();
            if (this.isMultipleFloors) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }
            else {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }
            this.placeSupportPillar(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxY);
            this.placeSupportPillar(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxY);
            this.placeSupportPillar(worldIn, structureBoundingBoxIn, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxY);
            this.placeSupportPillar(worldIn, structureBoundingBoxIn, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxY);
            for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
                for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
                    if (this.getBlockStateFromPos(worldIn, i, this.boundingBox.minY - 1, j, structureBoundingBoxIn).getMaterial() == Material.AIR && this.getSkyBrightness(worldIn, i, this.boundingBox.minY - 1, j, structureBoundingBoxIn) < 8) {
                        this.setBlockState(worldIn, iblockstate, i, this.boundingBox.minY - 1, j, structureBoundingBoxIn);
                    }
                }
            }
            return true;
        }
        
        private void placeSupportPillar(final World p_189923_1_, final StructureBoundingBox p_189923_2_, final int p_189923_3_, final int p_189923_4_, final int p_189923_5_, final int p_189923_6_) {
            if (this.getBlockStateFromPos(p_189923_1_, p_189923_3_, p_189923_6_ + 1, p_189923_5_, p_189923_2_).getMaterial() != Material.AIR) {
                this.fillWithBlocks(p_189923_1_, p_189923_2_, p_189923_3_, p_189923_4_, p_189923_5_, p_189923_3_, p_189923_6_, p_189923_5_, this.getPlanksBlock(), Blocks.AIR.getDefaultState(), false);
            }
        }
    }
    
    abstract static class Peice extends StructureComponent
    {
        protected MapGenMineshaft.Type mineShaftType;
        
        public Peice() {
        }
        
        public Peice(final int p_i47138_1_, final MapGenMineshaft.Type p_i47138_2_) {
            super(p_i47138_1_);
            this.mineShaftType = p_i47138_2_;
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            tagCompound.setInteger("MST", this.mineShaftType.ordinal());
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            this.mineShaftType = MapGenMineshaft.Type.byId(tagCompound.getInteger("MST"));
        }
        
        protected IBlockState getPlanksBlock() {
            switch (this.mineShaftType) {
                default: {
                    return Blocks.PLANKS.getDefaultState();
                }
                case MESA: {
                    return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK);
                }
            }
        }
        
        protected IBlockState getFenceBlock() {
            switch (this.mineShaftType) {
                default: {
                    return Blocks.OAK_FENCE.getDefaultState();
                }
                case MESA: {
                    return Blocks.DARK_OAK_FENCE.getDefaultState();
                }
            }
        }
        
        protected boolean isSupportingBox(final World p_189918_1_, final StructureBoundingBox p_189918_2_, final int p_189918_3_, final int p_189918_4_, final int p_189918_5_, final int p_189918_6_) {
            for (int i = p_189918_3_; i <= p_189918_4_; ++i) {
                if (this.getBlockStateFromPos(p_189918_1_, i, p_189918_5_ + 1, p_189918_6_, p_189918_2_).getMaterial() == Material.AIR) {
                    return false;
                }
            }
            return true;
        }
    }
    
    public static class Room extends Peice
    {
        private final List<StructureBoundingBox> connectedRooms;
        
        public Room() {
            this.connectedRooms = (List<StructureBoundingBox>)Lists.newLinkedList();
        }
        
        public Room(final int p_i47137_1_, final Random p_i47137_2_, final int p_i47137_3_, final int p_i47137_4_, final MapGenMineshaft.Type p_i47137_5_) {
            super(p_i47137_1_, p_i47137_5_);
            this.connectedRooms = (List<StructureBoundingBox>)Lists.newLinkedList();
            this.mineShaftType = p_i47137_5_;
            this.boundingBox = new StructureBoundingBox(p_i47137_3_, 50, p_i47137_4_, p_i47137_3_ + 7 + p_i47137_2_.nextInt(6), 54 + p_i47137_2_.nextInt(6), p_i47137_4_ + 7 + p_i47137_2_.nextInt(6));
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            final int i = this.getComponentType();
            int j = this.boundingBox.getYSize() - 3 - 1;
            if (j <= 0) {
                j = 1;
            }
            for (int k = 0; k < this.boundingBox.getXSize(); k += 4) {
                k += rand.nextInt(this.boundingBox.getXSize());
                if (k + 3 > this.boundingBox.getXSize()) {
                    break;
                }
                final Peice structuremineshaftpieces$peice = generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX + k, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                if (structuremineshaftpieces$peice != null) {
                    final StructureBoundingBox structureboundingbox = structuremineshaftpieces$peice.getBoundingBox();
                    this.connectedRooms.add(new StructureBoundingBox(structureboundingbox.minX, structureboundingbox.minY, this.boundingBox.minZ, structureboundingbox.maxX, structureboundingbox.maxY, this.boundingBox.minZ + 1));
                }
            }
            for (int k = 0; k < this.boundingBox.getXSize(); k += 4) {
                k += rand.nextInt(this.boundingBox.getXSize());
                if (k + 3 > this.boundingBox.getXSize()) {
                    break;
                }
                final Peice structuremineshaftpieces$peice2 = generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX + k, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                if (structuremineshaftpieces$peice2 != null) {
                    final StructureBoundingBox structureboundingbox2 = structuremineshaftpieces$peice2.getBoundingBox();
                    this.connectedRooms.add(new StructureBoundingBox(structureboundingbox2.minX, structureboundingbox2.minY, this.boundingBox.maxZ - 1, structureboundingbox2.maxX, structureboundingbox2.maxY, this.boundingBox.maxZ));
                }
            }
            for (int k = 0; k < this.boundingBox.getZSize(); k += 4) {
                k += rand.nextInt(this.boundingBox.getZSize());
                if (k + 3 > this.boundingBox.getZSize()) {
                    break;
                }
                final Peice structuremineshaftpieces$peice3 = generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.minZ + k, EnumFacing.WEST, i);
                if (structuremineshaftpieces$peice3 != null) {
                    final StructureBoundingBox structureboundingbox3 = structuremineshaftpieces$peice3.getBoundingBox();
                    this.connectedRooms.add(new StructureBoundingBox(this.boundingBox.minX, structureboundingbox3.minY, structureboundingbox3.minZ, this.boundingBox.minX + 1, structureboundingbox3.maxY, structureboundingbox3.maxZ));
                }
            }
            for (int k = 0; k < this.boundingBox.getZSize(); k += 4) {
                k += rand.nextInt(this.boundingBox.getZSize());
                if (k + 3 > this.boundingBox.getZSize()) {
                    break;
                }
                final StructureComponent structurecomponent = generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.minZ + k, EnumFacing.EAST, i);
                if (structurecomponent != null) {
                    final StructureBoundingBox structureboundingbox4 = structurecomponent.getBoundingBox();
                    this.connectedRooms.add(new StructureBoundingBox(this.boundingBox.maxX - 1, structureboundingbox4.minY, structureboundingbox4.minZ, this.boundingBox.maxX, structureboundingbox4.maxY, structureboundingbox4.maxZ));
                }
            }
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
                return false;
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, Blocks.DIRT.getDefaultState(), Blocks.AIR.getDefaultState(), true);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ, this.boundingBox.maxX, Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY), this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            for (final StructureBoundingBox structureboundingbox : this.connectedRooms) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, structureboundingbox.minX, structureboundingbox.maxY - 2, structureboundingbox.minZ, structureboundingbox.maxX, structureboundingbox.maxY, structureboundingbox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }
            this.randomlyRareFillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), false);
            return true;
        }
        
        @Override
        public void offset(final int x, final int y, final int z) {
            super.offset(x, y, z);
            for (final StructureBoundingBox structureboundingbox : this.connectedRooms) {
                structureboundingbox.offset(x, y, z);
            }
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            final NBTTagList nbttaglist = new NBTTagList();
            for (final StructureBoundingBox structureboundingbox : this.connectedRooms) {
                nbttaglist.appendTag(structureboundingbox.toNBTTagIntArray());
            }
            tagCompound.setTag("Entrances", nbttaglist);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            final NBTTagList nbttaglist = tagCompound.getTagList("Entrances", 11);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                this.connectedRooms.add(new StructureBoundingBox(nbttaglist.getIntArrayAt(i)));
            }
        }
    }
    
    public static class Stairs extends Peice
    {
        public Stairs() {
        }
        
        public Stairs(final int p_i47136_1_, final Random p_i47136_2_, final StructureBoundingBox p_i47136_3_, final EnumFacing p_i47136_4_, final MapGenMineshaft.Type p_i47136_5_) {
            super(p_i47136_1_, p_i47136_5_);
            this.setCoordBaseMode(p_i47136_4_);
            this.boundingBox = p_i47136_3_;
        }
        
        public static StructureBoundingBox findStairs(final List<StructureComponent> listIn, final Random rand, final int x, final int y, final int z, final EnumFacing facing) {
            final StructureBoundingBox structureboundingbox = new StructureBoundingBox(x, y - 5, z, x, y + 2, z);
            switch (facing) {
                default: {
                    structureboundingbox.maxX = x + 2;
                    structureboundingbox.minZ = z - 8;
                    break;
                }
                case SOUTH: {
                    structureboundingbox.maxX = x + 2;
                    structureboundingbox.maxZ = z + 8;
                    break;
                }
                case WEST: {
                    structureboundingbox.minX = x - 8;
                    structureboundingbox.maxZ = z + 2;
                    break;
                }
                case EAST: {
                    structureboundingbox.maxX = x + 8;
                    structureboundingbox.maxZ = z + 2;
                    break;
                }
            }
            return (StructureComponent.findIntersecting(listIn, structureboundingbox) != null) ? null : structureboundingbox;
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            final int i = this.getComponentType();
            final EnumFacing enumfacing = this.getCoordBaseMode();
            if (enumfacing != null) {
                switch (enumfacing) {
                    default: {
                        generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                        break;
                    }
                    case SOUTH: {
                        generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                        break;
                    }
                    case WEST: {
                        generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, i);
                        break;
                    }
                    case EAST: {
                        generateAndAddPiece(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, i);
                        break;
                    }
                }
            }
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
                return false;
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 2, 7, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 7, 2, 2, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            for (int i = 0; i < 5; ++i) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5 - i - ((i < 4) ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }
            return true;
        }
    }
}
