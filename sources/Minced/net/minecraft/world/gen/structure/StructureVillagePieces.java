// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockSandStone;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombieVillager;
import javax.annotation.Nullable;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeTaiga;
import net.minecraft.world.biome.BiomeSavanna;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockCrops;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;

public class StructureVillagePieces
{
    public static void registerVillagePieces() {
        MapGenStructureIO.registerStructureComponent(House1.class, "ViBH");
        MapGenStructureIO.registerStructureComponent(Field1.class, "ViDF");
        MapGenStructureIO.registerStructureComponent(Field2.class, "ViF");
        MapGenStructureIO.registerStructureComponent(Torch.class, "ViL");
        MapGenStructureIO.registerStructureComponent(Hall.class, "ViPH");
        MapGenStructureIO.registerStructureComponent(House4Garden.class, "ViSH");
        MapGenStructureIO.registerStructureComponent(WoodHut.class, "ViSmH");
        MapGenStructureIO.registerStructureComponent(Church.class, "ViST");
        MapGenStructureIO.registerStructureComponent(House2.class, "ViS");
        MapGenStructureIO.registerStructureComponent(Start.class, "ViStart");
        MapGenStructureIO.registerStructureComponent(Path.class, "ViSR");
        MapGenStructureIO.registerStructureComponent(House3.class, "ViTRH");
        MapGenStructureIO.registerStructureComponent(Well.class, "ViW");
    }
    
    public static List<PieceWeight> getStructureVillageWeightedPieceList(final Random random, final int size) {
        final List<PieceWeight> list = (List<PieceWeight>)Lists.newArrayList();
        list.add(new PieceWeight(House4Garden.class, 4, MathHelper.getInt(random, 2 + size, 4 + size * 2)));
        list.add(new PieceWeight(Church.class, 20, MathHelper.getInt(random, 0 + size, 1 + size)));
        list.add(new PieceWeight(House1.class, 20, MathHelper.getInt(random, 0 + size, 2 + size)));
        list.add(new PieceWeight(WoodHut.class, 3, MathHelper.getInt(random, 2 + size, 5 + size * 3)));
        list.add(new PieceWeight(Hall.class, 15, MathHelper.getInt(random, 0 + size, 2 + size)));
        list.add(new PieceWeight(Field1.class, 3, MathHelper.getInt(random, 1 + size, 4 + size)));
        list.add(new PieceWeight(Field2.class, 3, MathHelper.getInt(random, 2 + size, 4 + size * 2)));
        list.add(new PieceWeight(House2.class, 15, MathHelper.getInt(random, 0, 1 + size)));
        list.add(new PieceWeight(House3.class, 8, MathHelper.getInt(random, 0 + size, 3 + size * 2)));
        final Iterator<PieceWeight> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().villagePiecesLimit == 0) {
                iterator.remove();
            }
        }
        return list;
    }
    
    private static int updatePieceWeight(final List<PieceWeight> p_75079_0_) {
        boolean flag = false;
        int i = 0;
        for (final PieceWeight structurevillagepieces$pieceweight : p_75079_0_) {
            if (structurevillagepieces$pieceweight.villagePiecesLimit > 0 && structurevillagepieces$pieceweight.villagePiecesSpawned < structurevillagepieces$pieceweight.villagePiecesLimit) {
                flag = true;
            }
            i += structurevillagepieces$pieceweight.villagePieceWeight;
        }
        return flag ? i : -1;
    }
    
    private static Village findAndCreateComponentFactory(final Start start, final PieceWeight weight, final List<StructureComponent> structureComponents, final Random rand, final int structureMinX, final int structureMinY, final int structureMinZ, final EnumFacing facing, final int componentType) {
        final Class<? extends Village> oclass = weight.villagePieceClass;
        Village structurevillagepieces$village = null;
        if (oclass == House4Garden.class) {
            structurevillagepieces$village = House4Garden.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == Church.class) {
            structurevillagepieces$village = Church.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == House1.class) {
            structurevillagepieces$village = House1.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == WoodHut.class) {
            structurevillagepieces$village = WoodHut.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == Hall.class) {
            structurevillagepieces$village = Hall.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == Field1.class) {
            structurevillagepieces$village = Field1.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == Field2.class) {
            structurevillagepieces$village = Field2.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == House2.class) {
            structurevillagepieces$village = House2.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == House3.class) {
            structurevillagepieces$village = House3.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        return structurevillagepieces$village;
    }
    
    private static Village generateComponent(final Start start, final List<StructureComponent> structureComponents, final Random rand, final int structureMinX, final int structureMinY, final int structureMinZ, final EnumFacing facing, final int componentType) {
        final int i = updatePieceWeight(start.structureVillageWeightedPieceList);
        if (i <= 0) {
            return null;
        }
        int j = 0;
        while (j < 5) {
            ++j;
            int k = rand.nextInt(i);
            for (final PieceWeight structurevillagepieces$pieceweight : start.structureVillageWeightedPieceList) {
                k -= structurevillagepieces$pieceweight.villagePieceWeight;
                if (k < 0) {
                    if (!structurevillagepieces$pieceweight.canSpawnMoreVillagePiecesOfType(componentType)) {
                        break;
                    }
                    if (structurevillagepieces$pieceweight == start.lastPlaced && start.structureVillageWeightedPieceList.size() > 1) {
                        break;
                    }
                    final Village structurevillagepieces$village = findAndCreateComponentFactory(start, structurevillagepieces$pieceweight, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
                    if (structurevillagepieces$village != null) {
                        final PieceWeight pieceWeight = structurevillagepieces$pieceweight;
                        ++pieceWeight.villagePiecesSpawned;
                        start.lastPlaced = structurevillagepieces$pieceweight;
                        if (!structurevillagepieces$pieceweight.canSpawnMoreVillagePieces()) {
                            start.structureVillageWeightedPieceList.remove(structurevillagepieces$pieceweight);
                        }
                        return structurevillagepieces$village;
                    }
                    continue;
                }
            }
        }
        final StructureBoundingBox structureboundingbox = Torch.findPieceBox(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);
        if (structureboundingbox != null) {
            return new Torch(start, componentType, rand, structureboundingbox, facing);
        }
        return null;
    }
    
    private static StructureComponent generateAndAddComponent(final Start start, final List<StructureComponent> structureComponents, final Random rand, final int structureMinX, final int structureMinY, final int structureMinZ, final EnumFacing facing, final int componentType) {
        if (componentType > 50) {
            return null;
        }
        if (Math.abs(structureMinX - start.getBoundingBox().minX) > 112 || Math.abs(structureMinZ - start.getBoundingBox().minZ) > 112) {
            return null;
        }
        final StructureComponent structurecomponent = generateComponent(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType + 1);
        if (structurecomponent != null) {
            structureComponents.add(structurecomponent);
            start.pendingHouses.add(structurecomponent);
            return structurecomponent;
        }
        return null;
    }
    
    private static StructureComponent generateAndAddRoadPiece(final Start start, final List<StructureComponent> p_176069_1_, final Random rand, final int p_176069_3_, final int p_176069_4_, final int p_176069_5_, final EnumFacing facing, final int p_176069_7_) {
        if (p_176069_7_ > 3 + start.terrainType) {
            return null;
        }
        if (Math.abs(p_176069_3_ - start.getBoundingBox().minX) > 112 || Math.abs(p_176069_5_ - start.getBoundingBox().minZ) > 112) {
            return null;
        }
        final StructureBoundingBox structureboundingbox = Path.findPieceBox(start, p_176069_1_, rand, p_176069_3_, p_176069_4_, p_176069_5_, facing);
        if (structureboundingbox != null && structureboundingbox.minY > 10) {
            final StructureComponent structurecomponent = new Path(start, p_176069_7_, rand, structureboundingbox, facing);
            p_176069_1_.add(structurecomponent);
            start.pendingRoads.add(structurecomponent);
            return structurecomponent;
        }
        return null;
    }
    
    public static class Church extends Village
    {
        public Church() {
        }
        
        public Church(final Start start, final int type, final Random rand, final StructureBoundingBox p_i45564_4_, final EnumFacing facing) {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = p_i45564_4_;
        }
        
        public static Church createPiece(final Start start, final List<StructureComponent> p_175854_1_, final Random rand, final int p_175854_3_, final int p_175854_4_, final int p_175854_5_, final EnumFacing facing, final int p_175854_7_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175854_3_, p_175854_4_, p_175854_5_, 0, 0, 0, 5, 12, 9, facing);
            return (Village.canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175854_1_, structureboundingbox) == null) ? new Church(start, p_175854_7_, rand, structureboundingbox, facing) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
                if (this.averageGroundLvl < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 12 - 1, 0);
            }
            final IBlockState iblockstate = Blocks.COBBLESTONE.getDefaultState();
            final IBlockState iblockstate2 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.NORTH));
            final IBlockState iblockstate3 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.WEST));
            final IBlockState iblockstate4 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.EAST));
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 3, 3, 7, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 1, 3, 9, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 3, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 3, 10, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 10, 3, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 10, 3, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 4, 0, 4, 7, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 4, 4, 4, 7, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 8, 3, 4, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 4, 3, 10, 4, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 5, 3, 5, 7, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 9, 0, 4, 9, 4, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 0, 4, 4, 4, iblockstate, iblockstate, false);
            this.setBlockState(worldIn, iblockstate, 0, 11, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 4, 11, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 2, 11, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 2, 11, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 1, 1, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 1, 1, 7, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 2, 1, 7, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 3, 1, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 3, 1, 7, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 1, 1, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 2, 1, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 3, 1, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate3, 1, 2, 7, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate4, 3, 2, 7, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 4, 3, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 6, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 7, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 4, 6, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 4, 7, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 2, 6, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 2, 7, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 2, 6, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 2, 7, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 4, 3, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 2, 3, 8, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.SOUTH, 2, 4, 7, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.EAST, 1, 4, 6, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.WEST, 3, 4, 6, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.NORTH, 2, 4, 5, structureBoundingBoxIn);
            final IBlockState iblockstate5 = Blocks.LADDER.getDefaultState().withProperty((IProperty<Comparable>)BlockLadder.FACING, EnumFacing.WEST);
            for (int i = 1; i <= 9; ++i) {
                this.setBlockState(worldIn, iblockstate5, 3, i, 3, structureBoundingBoxIn);
            }
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 1, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 2, 0, structureBoundingBoxIn);
            this.createVillageDoor(worldIn, structureBoundingBoxIn, randomIn, 2, 1, 0, EnumFacing.NORTH);
            if (this.getBlockStateFromPos(worldIn, 2, 0, -1, structureBoundingBoxIn).getMaterial() == Material.AIR && this.getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getMaterial() != Material.AIR) {
                this.setBlockState(worldIn, iblockstate2, 2, 0, -1, structureBoundingBoxIn);
                if (this.getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getBlock() == Blocks.GRASS_PATH) {
                    this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), 2, -1, -1, structureBoundingBoxIn);
                }
            }
            for (int k = 0; k < 9; ++k) {
                for (int j = 0; j < 5; ++j) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, j, 12, k, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, iblockstate, j, -1, k, structureBoundingBoxIn);
                }
            }
            this.spawnVillagers(worldIn, structureBoundingBoxIn, 2, 1, 2, 1);
            return true;
        }
        
        @Override
        protected int chooseProfession(final int villagersSpawnedIn, final int currentVillagerProfession) {
            return 2;
        }
    }
    
    public static class Field1 extends Village
    {
        private Block cropTypeA;
        private Block cropTypeB;
        private Block cropTypeC;
        private Block cropTypeD;
        
        public Field1() {
        }
        
        public Field1(final Start start, final int type, final Random rand, final StructureBoundingBox p_i45570_4_, final EnumFacing facing) {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = p_i45570_4_;
            this.cropTypeA = this.getRandomCropType(rand);
            this.cropTypeB = this.getRandomCropType(rand);
            this.cropTypeC = this.getRandomCropType(rand);
            this.cropTypeD = this.getRandomCropType(rand);
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setInteger("CA", Block.REGISTRY.getIDForObject(this.cropTypeA));
            tagCompound.setInteger("CB", Block.REGISTRY.getIDForObject(this.cropTypeB));
            tagCompound.setInteger("CC", Block.REGISTRY.getIDForObject(this.cropTypeC));
            tagCompound.setInteger("CD", Block.REGISTRY.getIDForObject(this.cropTypeD));
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.cropTypeA = Block.getBlockById(tagCompound.getInteger("CA"));
            this.cropTypeB = Block.getBlockById(tagCompound.getInteger("CB"));
            this.cropTypeC = Block.getBlockById(tagCompound.getInteger("CC"));
            this.cropTypeD = Block.getBlockById(tagCompound.getInteger("CD"));
            if (!(this.cropTypeA instanceof BlockCrops)) {
                this.cropTypeA = Blocks.WHEAT;
            }
            if (!(this.cropTypeB instanceof BlockCrops)) {
                this.cropTypeB = Blocks.CARROTS;
            }
            if (!(this.cropTypeC instanceof BlockCrops)) {
                this.cropTypeC = Blocks.POTATOES;
            }
            if (!(this.cropTypeD instanceof BlockCrops)) {
                this.cropTypeD = Blocks.BEETROOTS;
            }
        }
        
        private Block getRandomCropType(final Random rand) {
            switch (rand.nextInt(10)) {
                case 0:
                case 1: {
                    return Blocks.CARROTS;
                }
                case 2:
                case 3: {
                    return Blocks.POTATOES;
                }
                case 4: {
                    return Blocks.BEETROOTS;
                }
                default: {
                    return Blocks.WHEAT;
                }
            }
        }
        
        public static Field1 createPiece(final Start start, final List<StructureComponent> p_175851_1_, final Random rand, final int p_175851_3_, final int p_175851_4_, final int p_175851_5_, final EnumFacing facing, final int p_175851_7_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175851_3_, p_175851_4_, p_175851_5_, 0, 0, 0, 13, 4, 9, facing);
            return (Village.canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175851_1_, structureboundingbox) == null) ? new Field1(start, p_175851_7_, rand, structureboundingbox, facing) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
                if (this.averageGroundLvl < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 4 - 1, 0);
            }
            final IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 12, 4, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 1, 5, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 1, 8, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 0, 1, 11, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 0, 6, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 0, 0, 12, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 11, 0, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 8, 11, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 1, 3, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 1, 9, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
            for (int i = 1; i <= 7; ++i) {
                final int j = ((BlockCrops)this.cropTypeA).getMaxAge();
                final int k = j / 3;
                this.setBlockState(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getInt(randomIn, k, j)), 1, 1, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getInt(randomIn, k, j)), 2, 1, i, structureBoundingBoxIn);
                final int l = ((BlockCrops)this.cropTypeB).getMaxAge();
                final int i2 = l / 3;
                this.setBlockState(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getInt(randomIn, i2, l)), 4, 1, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getInt(randomIn, i2, l)), 5, 1, i, structureBoundingBoxIn);
                final int j2 = ((BlockCrops)this.cropTypeC).getMaxAge();
                final int k2 = j2 / 3;
                this.setBlockState(worldIn, this.cropTypeC.getStateFromMeta(MathHelper.getInt(randomIn, k2, j2)), 7, 1, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, this.cropTypeC.getStateFromMeta(MathHelper.getInt(randomIn, k2, j2)), 8, 1, i, structureBoundingBoxIn);
                final int l2 = ((BlockCrops)this.cropTypeD).getMaxAge();
                final int i3 = l2 / 3;
                this.setBlockState(worldIn, this.cropTypeD.getStateFromMeta(MathHelper.getInt(randomIn, i3, l2)), 10, 1, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, this.cropTypeD.getStateFromMeta(MathHelper.getInt(randomIn, i3, l2)), 11, 1, i, structureBoundingBoxIn);
            }
            for (int j3 = 0; j3 < 9; ++j3) {
                for (int k3 = 0; k3 < 13; ++k3) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, k3, 4, j3, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.DIRT.getDefaultState(), k3, -1, j3, structureBoundingBoxIn);
                }
            }
            return true;
        }
    }
    
    public static class Field2 extends Village
    {
        private Block cropTypeA;
        private Block cropTypeB;
        
        public Field2() {
        }
        
        public Field2(final Start start, final int p_i45569_2_, final Random rand, final StructureBoundingBox p_i45569_4_, final EnumFacing facing) {
            super(start, p_i45569_2_);
            this.setCoordBaseMode(facing);
            this.boundingBox = p_i45569_4_;
            this.cropTypeA = this.getRandomCropType(rand);
            this.cropTypeB = this.getRandomCropType(rand);
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setInteger("CA", Block.REGISTRY.getIDForObject(this.cropTypeA));
            tagCompound.setInteger("CB", Block.REGISTRY.getIDForObject(this.cropTypeB));
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.cropTypeA = Block.getBlockById(tagCompound.getInteger("CA"));
            this.cropTypeB = Block.getBlockById(tagCompound.getInteger("CB"));
        }
        
        private Block getRandomCropType(final Random rand) {
            switch (rand.nextInt(10)) {
                case 0:
                case 1: {
                    return Blocks.CARROTS;
                }
                case 2:
                case 3: {
                    return Blocks.POTATOES;
                }
                case 4: {
                    return Blocks.BEETROOTS;
                }
                default: {
                    return Blocks.WHEAT;
                }
            }
        }
        
        public static Field2 createPiece(final Start start, final List<StructureComponent> p_175852_1_, final Random rand, final int p_175852_3_, final int p_175852_4_, final int p_175852_5_, final EnumFacing facing, final int p_175852_7_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175852_3_, p_175852_4_, p_175852_5_, 0, 0, 0, 7, 4, 9, facing);
            return (Village.canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175852_1_, structureboundingbox) == null) ? new Field2(start, p_175852_7_, rand, structureboundingbox, facing) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
                if (this.averageGroundLvl < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 4 - 1, 0);
            }
            final IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 6, 4, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 1, 5, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 0, 6, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 5, 0, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 8, 5, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 1, 3, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
            for (int i = 1; i <= 7; ++i) {
                final int j = ((BlockCrops)this.cropTypeA).getMaxAge();
                final int k = j / 3;
                this.setBlockState(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getInt(randomIn, k, j)), 1, 1, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getInt(randomIn, k, j)), 2, 1, i, structureBoundingBoxIn);
                final int l = ((BlockCrops)this.cropTypeB).getMaxAge();
                final int i2 = l / 3;
                this.setBlockState(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getInt(randomIn, i2, l)), 4, 1, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getInt(randomIn, i2, l)), 5, 1, i, structureBoundingBoxIn);
            }
            for (int j2 = 0; j2 < 9; ++j2) {
                for (int k2 = 0; k2 < 7; ++k2) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, k2, 4, j2, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.DIRT.getDefaultState(), k2, -1, j2, structureBoundingBoxIn);
                }
            }
            return true;
        }
    }
    
    public static class Hall extends Village
    {
        public Hall() {
        }
        
        public Hall(final Start start, final int type, final Random rand, final StructureBoundingBox p_i45567_4_, final EnumFacing facing) {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = p_i45567_4_;
        }
        
        public static Hall createPiece(final Start start, final List<StructureComponent> p_175857_1_, final Random rand, final int p_175857_3_, final int p_175857_4_, final int p_175857_5_, final EnumFacing facing, final int p_175857_7_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175857_3_, p_175857_4_, p_175857_5_, 0, 0, 0, 9, 7, 11, facing);
            return (Village.canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175857_1_, structureboundingbox) == null) ? new Hall(start, p_175857_7_, rand, structureboundingbox, facing) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
                if (this.averageGroundLvl < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 7 - 1, 0);
            }
            final IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
            final IBlockState iblockstate2 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.NORTH));
            final IBlockState iblockstate3 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.SOUTH));
            final IBlockState iblockstate4 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.WEST));
            final IBlockState iblockstate5 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
            final IBlockState iblockstate6 = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
            final IBlockState iblockstate7 = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 7, 4, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 6, 8, 4, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 6, 8, 0, 10, Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState(), false);
            this.setBlockState(worldIn, iblockstate, 6, 0, 6, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 6, 2, 1, 10, iblockstate7, iblockstate7, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 6, 8, 1, 10, iblockstate7, iblockstate7, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 10, 7, 1, 10, iblockstate7, iblockstate7, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 7, 0, 4, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 3, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 0, 0, 8, 3, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 7, 1, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 5, 7, 1, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 7, 3, 0, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 5, 7, 3, 5, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 1, 8, 4, 1, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 4, 8, 4, 4, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 2, 8, 5, 3, iblockstate5, iblockstate5, false);
            this.setBlockState(worldIn, iblockstate5, 0, 4, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate5, 0, 4, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate5, 8, 4, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate5, 8, 4, 3, structureBoundingBoxIn);
            final IBlockState iblockstate8 = iblockstate2;
            final IBlockState iblockstate9 = iblockstate3;
            for (int i = -1; i <= 2; ++i) {
                for (int j = 0; j <= 8; ++j) {
                    this.setBlockState(worldIn, iblockstate8, j, 4 + i, i, structureBoundingBoxIn);
                    this.setBlockState(worldIn, iblockstate9, j, 4 + i, 5 - i, structureBoundingBoxIn);
                }
            }
            this.setBlockState(worldIn, iblockstate6, 0, 2, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate6, 0, 2, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate6, 8, 2, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate6, 8, 2, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 3, 2, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 6, 2, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate7, 2, 1, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 2, 2, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate5, 1, 1, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate8, 2, 1, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate4, 1, 1, 3, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 1, 7, 0, 3, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), Blocks.DOUBLE_STONE_SLAB.getDefaultState(), false);
            this.setBlockState(worldIn, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), 6, 1, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), 6, 1, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 1, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 2, 0, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.NORTH, 2, 3, 1, structureBoundingBoxIn);
            this.createVillageDoor(worldIn, structureBoundingBoxIn, randomIn, 2, 1, 0, EnumFacing.NORTH);
            if (this.getBlockStateFromPos(worldIn, 2, 0, -1, structureBoundingBoxIn).getMaterial() == Material.AIR && this.getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getMaterial() != Material.AIR) {
                this.setBlockState(worldIn, iblockstate8, 2, 0, -1, structureBoundingBoxIn);
                if (this.getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getBlock() == Blocks.GRASS_PATH) {
                    this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), 2, -1, -1, structureBoundingBoxIn);
                }
            }
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 6, 1, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 6, 2, 5, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.SOUTH, 6, 3, 4, structureBoundingBoxIn);
            this.createVillageDoor(worldIn, structureBoundingBoxIn, randomIn, 6, 1, 5, EnumFacing.SOUTH);
            for (int k = 0; k < 5; ++k) {
                for (int l = 0; l < 9; ++l) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, l, 7, k, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, iblockstate, l, -1, k, structureBoundingBoxIn);
                }
            }
            this.spawnVillagers(worldIn, structureBoundingBoxIn, 4, 1, 2, 2);
            return true;
        }
        
        @Override
        protected int chooseProfession(final int villagersSpawnedIn, final int currentVillagerProfession) {
            return (villagersSpawnedIn == 0) ? 4 : super.chooseProfession(villagersSpawnedIn, currentVillagerProfession);
        }
    }
    
    public static class House1 extends Village
    {
        public House1() {
        }
        
        public House1(final Start start, final int type, final Random rand, final StructureBoundingBox p_i45571_4_, final EnumFacing facing) {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = p_i45571_4_;
        }
        
        public static House1 createPiece(final Start start, final List<StructureComponent> p_175850_1_, final Random rand, final int p_175850_3_, final int p_175850_4_, final int p_175850_5_, final EnumFacing facing, final int p_175850_7_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175850_3_, p_175850_4_, p_175850_5_, 0, 0, 0, 9, 9, 6, facing);
            return (Village.canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175850_1_, structureboundingbox) == null) ? new House1(start, p_175850_7_, rand, structureboundingbox, facing) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
                if (this.averageGroundLvl < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 9 - 1, 0);
            }
            final IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
            final IBlockState iblockstate2 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.NORTH));
            final IBlockState iblockstate3 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.SOUTH));
            final IBlockState iblockstate4 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.EAST));
            final IBlockState iblockstate5 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
            final IBlockState iblockstate6 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.NORTH));
            final IBlockState iblockstate7 = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 7, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 8, 0, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 8, 5, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 1, 8, 6, 4, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 7, 2, 8, 7, 3, iblockstate, iblockstate, false);
            for (int i = -1; i <= 2; ++i) {
                for (int j = 0; j <= 8; ++j) {
                    this.setBlockState(worldIn, iblockstate2, j, 6 + i, i, structureBoundingBoxIn);
                    this.setBlockState(worldIn, iblockstate3, j, 6 + i, 5 - i, structureBoundingBoxIn);
                }
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 5, 8, 1, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 0, 8, 1, 4, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 0, 7, 1, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 4, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 5, 0, 4, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 5, 8, 4, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 0, 8, 4, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 1, 0, 4, 4, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 5, 7, 4, 5, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 1, 8, 4, 4, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 7, 4, 0, iblockstate5, iblockstate5, false);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 6, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 4, 3, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 5, 3, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 6, 3, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 8, 3, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 8, 3, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 3, 2, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 6, 2, 5, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 1, 7, 4, 1, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 4, 7, 4, 4, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 4, 7, 3, 4, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
            this.setBlockState(worldIn, iblockstate5, 7, 1, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate4, 7, 1, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 6, 1, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 5, 1, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 4, 1, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 3, 1, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate7, 6, 1, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 6, 2, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate7, 4, 1, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 4, 2, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.CRAFTING_TABLE.getDefaultState(), 7, 1, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 1, 1, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 1, 2, 0, structureBoundingBoxIn);
            this.createVillageDoor(worldIn, structureBoundingBoxIn, randomIn, 1, 1, 0, EnumFacing.NORTH);
            if (this.getBlockStateFromPos(worldIn, 1, 0, -1, structureBoundingBoxIn).getMaterial() == Material.AIR && this.getBlockStateFromPos(worldIn, 1, -1, -1, structureBoundingBoxIn).getMaterial() != Material.AIR) {
                this.setBlockState(worldIn, iblockstate6, 1, 0, -1, structureBoundingBoxIn);
                if (this.getBlockStateFromPos(worldIn, 1, -1, -1, structureBoundingBoxIn).getBlock() == Blocks.GRASS_PATH) {
                    this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), 1, -1, -1, structureBoundingBoxIn);
                }
            }
            for (int l = 0; l < 6; ++l) {
                for (int k = 0; k < 9; ++k) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, k, 9, l, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, iblockstate, k, -1, l, structureBoundingBoxIn);
                }
            }
            this.spawnVillagers(worldIn, structureBoundingBoxIn, 2, 1, 2, 1);
            return true;
        }
        
        @Override
        protected int chooseProfession(final int villagersSpawnedIn, final int currentVillagerProfession) {
            return 1;
        }
    }
    
    public static class House2 extends Village
    {
        private boolean hasMadeChest;
        
        public House2() {
        }
        
        public House2(final Start start, final int type, final Random rand, final StructureBoundingBox p_i45563_4_, final EnumFacing facing) {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = p_i45563_4_;
        }
        
        public static House2 createPiece(final Start start, final List<StructureComponent> p_175855_1_, final Random rand, final int p_175855_3_, final int p_175855_4_, final int p_175855_5_, final EnumFacing facing, final int p_175855_7_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175855_3_, p_175855_4_, p_175855_5_, 0, 0, 0, 10, 6, 7, facing);
            return (Village.canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175855_1_, structureboundingbox) == null) ? new House2(start, p_175855_7_, rand, structureboundingbox, facing) : null;
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setBoolean("Chest", this.hasMadeChest);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.hasMadeChest = tagCompound.getBoolean("Chest");
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
                if (this.averageGroundLvl < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 6 - 1, 0);
            }
            final IBlockState iblockstate = Blocks.COBBLESTONE.getDefaultState();
            final IBlockState iblockstate2 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.NORTH));
            final IBlockState iblockstate3 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.WEST));
            final IBlockState iblockstate4 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
            final IBlockState iblockstate5 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.NORTH));
            final IBlockState iblockstate6 = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
            final IBlockState iblockstate7 = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 9, 4, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 9, 0, 6, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 0, 9, 4, 6, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 9, 5, 6, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 1, 8, 5, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 2, 3, 0, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 4, 0, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 3, 4, 0, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 6, 0, 4, 6, iblockstate6, iblockstate6, false);
            this.setBlockState(worldIn, iblockstate4, 3, 3, 1, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 2, 3, 3, 2, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 3, 5, 3, 3, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 5, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 6, 5, 3, 6, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 0, 5, 3, 0, iblockstate7, iblockstate7, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 0, 9, 3, 0, iblockstate7, iblockstate7, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 4, 9, 4, 6, iblockstate, iblockstate, false);
            this.setBlockState(worldIn, Blocks.FLOWING_LAVA.getDefaultState(), 7, 1, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.FLOWING_LAVA.getDefaultState(), 8, 1, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.IRON_BARS.getDefaultState(), 9, 2, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.IRON_BARS.getDefaultState(), 9, 2, 4, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 4, 8, 2, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.setBlockState(worldIn, iblockstate, 6, 1, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.FURNACE.getDefaultState(), 6, 2, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.FURNACE.getDefaultState(), 6, 3, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), 8, 1, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate7, 2, 1, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 2, 2, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate4, 1, 1, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 2, 1, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate3, 1, 1, 4, structureBoundingBoxIn);
            if (!this.hasMadeChest && structureBoundingBoxIn.isVecInside(new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(1), this.getZWithOffset(5, 5)))) {
                this.hasMadeChest = true;
                this.generateChest(worldIn, structureBoundingBoxIn, randomIn, 5, 1, 5, LootTableList.CHESTS_VILLAGE_BLACKSMITH);
            }
            for (int i = 6; i <= 8; ++i) {
                if (this.getBlockStateFromPos(worldIn, i, 0, -1, structureBoundingBoxIn).getMaterial() == Material.AIR && this.getBlockStateFromPos(worldIn, i, -1, -1, structureBoundingBoxIn).getMaterial() != Material.AIR) {
                    this.setBlockState(worldIn, iblockstate5, i, 0, -1, structureBoundingBoxIn);
                    if (this.getBlockStateFromPos(worldIn, i, -1, -1, structureBoundingBoxIn).getBlock() == Blocks.GRASS_PATH) {
                        this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), i, -1, -1, structureBoundingBoxIn);
                    }
                }
            }
            for (int k = 0; k < 7; ++k) {
                for (int j = 0; j < 10; ++j) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, j, 6, k, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, iblockstate, j, -1, k, structureBoundingBoxIn);
                }
            }
            this.spawnVillagers(worldIn, structureBoundingBoxIn, 7, 1, 1, 1);
            return true;
        }
        
        @Override
        protected int chooseProfession(final int villagersSpawnedIn, final int currentVillagerProfession) {
            return 3;
        }
    }
    
    public static class House3 extends Village
    {
        public House3() {
        }
        
        public House3(final Start start, final int type, final Random rand, final StructureBoundingBox p_i45561_4_, final EnumFacing facing) {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = p_i45561_4_;
        }
        
        public static House3 createPiece(final Start start, final List<StructureComponent> p_175849_1_, final Random rand, final int p_175849_3_, final int p_175849_4_, final int p_175849_5_, final EnumFacing facing, final int p_175849_7_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175849_3_, p_175849_4_, p_175849_5_, 0, 0, 0, 9, 7, 12, facing);
            return (Village.canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175849_1_, structureboundingbox) == null) ? new House3(start, p_175849_7_, rand, structureboundingbox, facing) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
                if (this.averageGroundLvl < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 7 - 1, 0);
            }
            final IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
            final IBlockState iblockstate2 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.NORTH));
            final IBlockState iblockstate3 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.SOUTH));
            final IBlockState iblockstate4 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.EAST));
            final IBlockState iblockstate5 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.WEST));
            final IBlockState iblockstate6 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
            final IBlockState iblockstate7 = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 7, 4, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 6, 8, 4, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 5, 8, 0, 10, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 7, 0, 4, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 3, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 0, 0, 8, 3, 10, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 7, 2, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 5, 2, 1, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 6, 2, 3, 10, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 10, 7, 3, 10, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 7, 3, 0, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 5, 2, 3, 5, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 1, 8, 4, 1, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 4, 3, 4, 4, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 2, 8, 5, 3, iblockstate6, iblockstate6, false);
            this.setBlockState(worldIn, iblockstate6, 0, 4, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate6, 0, 4, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate6, 8, 4, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate6, 8, 4, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate6, 8, 4, 4, structureBoundingBoxIn);
            final IBlockState iblockstate8 = iblockstate2;
            final IBlockState iblockstate9 = iblockstate3;
            final IBlockState iblockstate10 = iblockstate5;
            final IBlockState iblockstate11 = iblockstate4;
            for (int i = -1; i <= 2; ++i) {
                for (int j = 0; j <= 8; ++j) {
                    this.setBlockState(worldIn, iblockstate8, j, 4 + i, i, structureBoundingBoxIn);
                    if ((i > -1 || j <= 1) && (i > 0 || j <= 3) && (i > 1 || j <= 4 || j >= 6)) {
                        this.setBlockState(worldIn, iblockstate9, j, 4 + i, 5 - i, structureBoundingBoxIn);
                    }
                }
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 5, 3, 4, 10, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 2, 7, 4, 10, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 4, 4, 5, 10, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 5, 4, 6, 5, 10, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 6, 3, 5, 6, 10, iblockstate6, iblockstate6, false);
            for (int k = 4; k >= 1; --k) {
                this.setBlockState(worldIn, iblockstate6, k, 2 + k, 7 - k, structureBoundingBoxIn);
                for (int k2 = 8 - k; k2 <= 10; ++k2) {
                    this.setBlockState(worldIn, iblockstate11, k, 2 + k, k2, structureBoundingBoxIn);
                }
            }
            this.setBlockState(worldIn, iblockstate6, 6, 6, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate6, 7, 5, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate5, 6, 6, 4, structureBoundingBoxIn);
            for (int l = 6; l <= 8; ++l) {
                for (int l2 = 5; l2 <= 10; ++l2) {
                    this.setBlockState(worldIn, iblockstate10, l, 12 - l, l2, structureBoundingBoxIn);
                }
            }
            this.setBlockState(worldIn, iblockstate7, 0, 2, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate7, 0, 2, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate7, 4, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate7, 6, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate7, 8, 2, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate7, 8, 2, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate6, 8, 2, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate7, 8, 2, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 7, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate7, 8, 2, 9, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate7, 2, 2, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 7, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate7, 2, 2, 9, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate7, 4, 4, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 5, 4, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate7, 6, 4, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate6, 5, 5, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 1, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 2, 0, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.NORTH, 2, 3, 1, structureBoundingBoxIn);
            this.createVillageDoor(worldIn, structureBoundingBoxIn, randomIn, 2, 1, 0, EnumFacing.NORTH);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, -1, 3, 2, -1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            if (this.getBlockStateFromPos(worldIn, 2, 0, -1, structureBoundingBoxIn).getMaterial() == Material.AIR && this.getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getMaterial() != Material.AIR) {
                this.setBlockState(worldIn, iblockstate8, 2, 0, -1, structureBoundingBoxIn);
                if (this.getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getBlock() == Blocks.GRASS_PATH) {
                    this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), 2, -1, -1, structureBoundingBoxIn);
                }
            }
            for (int i2 = 0; i2 < 5; ++i2) {
                for (int i3 = 0; i3 < 9; ++i3) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, i3, 7, i2, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, iblockstate, i3, -1, i2, structureBoundingBoxIn);
                }
            }
            for (int j2 = 5; j2 < 11; ++j2) {
                for (int j3 = 2; j3 < 9; ++j3) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, j3, 7, j2, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, iblockstate, j3, -1, j2, structureBoundingBoxIn);
                }
            }
            this.spawnVillagers(worldIn, structureBoundingBoxIn, 4, 1, 2, 2);
            return true;
        }
    }
    
    public static class House4Garden extends Village
    {
        private boolean isRoofAccessible;
        
        public House4Garden() {
        }
        
        public House4Garden(final Start start, final int p_i45566_2_, final Random rand, final StructureBoundingBox p_i45566_4_, final EnumFacing facing) {
            super(start, p_i45566_2_);
            this.setCoordBaseMode(facing);
            this.boundingBox = p_i45566_4_;
            this.isRoofAccessible = rand.nextBoolean();
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setBoolean("Terrace", this.isRoofAccessible);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.isRoofAccessible = tagCompound.getBoolean("Terrace");
        }
        
        public static House4Garden createPiece(final Start start, final List<StructureComponent> p_175858_1_, final Random rand, final int p_175858_3_, final int p_175858_4_, final int p_175858_5_, final EnumFacing facing, final int p_175858_7_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175858_3_, p_175858_4_, p_175858_5_, 0, 0, 0, 5, 6, 5, facing);
            return (StructureComponent.findIntersecting(p_175858_1_, structureboundingbox) != null) ? null : new House4Garden(start, p_175858_7_, rand, structureboundingbox, facing);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
                if (this.averageGroundLvl < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 6 - 1, 0);
            }
            final IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
            final IBlockState iblockstate2 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
            final IBlockState iblockstate3 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.NORTH));
            final IBlockState iblockstate4 = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
            final IBlockState iblockstate5 = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 0, 4, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 0, 4, 4, 4, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 1, 3, 4, 3, iblockstate2, iblockstate2, false);
            this.setBlockState(worldIn, iblockstate, 0, 1, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 0, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 0, 3, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 4, 1, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 4, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 4, 3, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 0, 1, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 0, 2, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 0, 3, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 4, 1, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 4, 2, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 4, 3, 4, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 3, iblockstate2, iblockstate2, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 3, 3, iblockstate2, iblockstate2, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 4, 3, 3, 4, iblockstate2, iblockstate2, false);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 1, 1, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 1, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 1, 3, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 2, 3, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 3, 3, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 3, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 3, 1, 0, structureBoundingBoxIn);
            if (this.getBlockStateFromPos(worldIn, 2, 0, -1, structureBoundingBoxIn).getMaterial() == Material.AIR && this.getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getMaterial() != Material.AIR) {
                this.setBlockState(worldIn, iblockstate3, 2, 0, -1, structureBoundingBoxIn);
                if (this.getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getBlock() == Blocks.GRASS_PATH) {
                    this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), 2, -1, -1, structureBoundingBoxIn);
                }
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 3, 3, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            if (this.isRoofAccessible) {
                this.setBlockState(worldIn, iblockstate5, 0, 5, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate5, 1, 5, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate5, 2, 5, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate5, 3, 5, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate5, 4, 5, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate5, 0, 5, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate5, 1, 5, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate5, 2, 5, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate5, 3, 5, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate5, 4, 5, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate5, 4, 5, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate5, 4, 5, 2, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate5, 4, 5, 3, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate5, 0, 5, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate5, 0, 5, 2, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate5, 0, 5, 3, structureBoundingBoxIn);
            }
            if (this.isRoofAccessible) {
                final IBlockState iblockstate6 = Blocks.LADDER.getDefaultState().withProperty((IProperty<Comparable>)BlockLadder.FACING, EnumFacing.SOUTH);
                this.setBlockState(worldIn, iblockstate6, 3, 1, 3, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate6, 3, 2, 3, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate6, 3, 3, 3, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate6, 3, 4, 3, structureBoundingBoxIn);
            }
            this.placeTorch(worldIn, EnumFacing.NORTH, 2, 3, 1, structureBoundingBoxIn);
            for (int j = 0; j < 5; ++j) {
                for (int i = 0; i < 5; ++i) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, i, 6, j, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, iblockstate, i, -1, j, structureBoundingBoxIn);
                }
            }
            this.spawnVillagers(worldIn, structureBoundingBoxIn, 1, 1, 2, 1);
            return true;
        }
    }
    
    public static class Path extends Road
    {
        private int length;
        
        public Path() {
        }
        
        public Path(final Start start, final int p_i45562_2_, final Random rand, final StructureBoundingBox p_i45562_4_, final EnumFacing facing) {
            super(start, p_i45562_2_);
            this.setCoordBaseMode(facing);
            this.boundingBox = p_i45562_4_;
            this.length = Math.max(p_i45562_4_.getXSize(), p_i45562_4_.getZSize());
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setInteger("Length", this.length);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.length = tagCompound.getInteger("Length");
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            boolean flag = false;
            for (int i = rand.nextInt(5); i < this.length - 8; i += 2 + rand.nextInt(5)) {
                final StructureComponent structurecomponent = this.getNextComponentNN((Start)componentIn, listIn, rand, 0, i);
                if (structurecomponent != null) {
                    i += Math.max(structurecomponent.boundingBox.getXSize(), structurecomponent.boundingBox.getZSize());
                    flag = true;
                }
            }
            for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5)) {
                final StructureComponent structurecomponent2 = this.getNextComponentPP((Start)componentIn, listIn, rand, 0, j);
                if (structurecomponent2 != null) {
                    j += Math.max(structurecomponent2.boundingBox.getXSize(), structurecomponent2.boundingBox.getZSize());
                    flag = true;
                }
            }
            final EnumFacing enumfacing = this.getCoordBaseMode();
            if (flag && rand.nextInt(3) > 0 && enumfacing != null) {
                switch (enumfacing) {
                    default: {
                        generateAndAddRoadPiece((Start)componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, this.getComponentType());
                        break;
                    }
                    case SOUTH: {
                        generateAndAddRoadPiece((Start)componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST, this.getComponentType());
                        break;
                    }
                    case WEST: {
                        generateAndAddRoadPiece((Start)componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                        break;
                    }
                    case EAST: {
                        generateAndAddRoadPiece((Start)componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                        break;
                    }
                }
            }
            if (flag && rand.nextInt(3) > 0 && enumfacing != null) {
                switch (enumfacing) {
                    default: {
                        generateAndAddRoadPiece((Start)componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType());
                        break;
                    }
                    case SOUTH: {
                        generateAndAddRoadPiece((Start)componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST, this.getComponentType());
                        break;
                    }
                    case WEST: {
                        generateAndAddRoadPiece((Start)componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                        break;
                    }
                    case EAST: {
                        generateAndAddRoadPiece((Start)componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                        break;
                    }
                }
            }
        }
        
        public static StructureBoundingBox findPieceBox(final Start start, final List<StructureComponent> p_175848_1_, final Random rand, final int p_175848_3_, final int p_175848_4_, final int p_175848_5_, final EnumFacing facing) {
            for (int i = 7 * MathHelper.getInt(rand, 3, 5); i >= 7; i -= 7) {
                final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175848_3_, p_175848_4_, p_175848_5_, 0, 0, 0, 3, 3, i, facing);
                if (StructureComponent.findIntersecting(p_175848_1_, structureboundingbox) == null) {
                    return structureboundingbox;
                }
            }
            return null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            final IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.GRASS_PATH.getDefaultState());
            final IBlockState iblockstate2 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
            final IBlockState iblockstate3 = this.getBiomeSpecificBlockState(Blocks.GRAVEL.getDefaultState());
            final IBlockState iblockstate4 = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
            for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
                for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
                    BlockPos blockpos = new BlockPos(i, 64, j);
                    if (structureBoundingBoxIn.isVecInside(blockpos)) {
                        blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos).down();
                        if (blockpos.getY() < worldIn.getSeaLevel()) {
                            blockpos = new BlockPos(blockpos.getX(), worldIn.getSeaLevel() - 1, blockpos.getZ());
                        }
                        while (blockpos.getY() >= worldIn.getSeaLevel() - 1) {
                            final IBlockState iblockstate5 = worldIn.getBlockState(blockpos);
                            if (iblockstate5.getBlock() == Blocks.GRASS && worldIn.isAirBlock(blockpos.up())) {
                                worldIn.setBlockState(blockpos, iblockstate, 2);
                                break;
                            }
                            if (iblockstate5.getMaterial().isLiquid()) {
                                worldIn.setBlockState(blockpos, iblockstate2, 2);
                                break;
                            }
                            if (iblockstate5.getBlock() == Blocks.SAND || iblockstate5.getBlock() == Blocks.SANDSTONE || iblockstate5.getBlock() == Blocks.RED_SANDSTONE) {
                                worldIn.setBlockState(blockpos, iblockstate3, 2);
                                worldIn.setBlockState(blockpos.down(), iblockstate4, 2);
                                break;
                            }
                            blockpos = blockpos.down();
                        }
                    }
                }
            }
            return true;
        }
    }
    
    public static class PieceWeight
    {
        public Class<? extends Village> villagePieceClass;
        public final int villagePieceWeight;
        public int villagePiecesSpawned;
        public int villagePiecesLimit;
        
        public PieceWeight(final Class<? extends Village> p_i2098_1_, final int p_i2098_2_, final int p_i2098_3_) {
            this.villagePieceClass = p_i2098_1_;
            this.villagePieceWeight = p_i2098_2_;
            this.villagePiecesLimit = p_i2098_3_;
        }
        
        public boolean canSpawnMoreVillagePiecesOfType(final int componentType) {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }
        
        public boolean canSpawnMoreVillagePieces() {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }
    }
    
    public abstract static class Road extends Village
    {
        public Road() {
        }
        
        protected Road(final Start start, final int type) {
            super(start, type);
        }
    }
    
    public static class Start extends Well
    {
        public BiomeProvider biomeProvider;
        public int terrainType;
        public PieceWeight lastPlaced;
        public List<PieceWeight> structureVillageWeightedPieceList;
        public List<StructureComponent> pendingHouses;
        public List<StructureComponent> pendingRoads;
        
        public Start() {
            this.pendingHouses = (List<StructureComponent>)Lists.newArrayList();
            this.pendingRoads = (List<StructureComponent>)Lists.newArrayList();
        }
        
        public Start(final BiomeProvider biomeProviderIn, final int p_i2104_2_, final Random rand, final int p_i2104_4_, final int p_i2104_5_, final List<PieceWeight> p_i2104_6_, final int p_i2104_7_) {
            super(null, 0, rand, p_i2104_4_, p_i2104_5_);
            this.pendingHouses = (List<StructureComponent>)Lists.newArrayList();
            this.pendingRoads = (List<StructureComponent>)Lists.newArrayList();
            this.biomeProvider = biomeProviderIn;
            this.structureVillageWeightedPieceList = p_i2104_6_;
            this.terrainType = p_i2104_7_;
            final Biome biome = biomeProviderIn.getBiome(new BlockPos(p_i2104_4_, 0, p_i2104_5_), Biomes.DEFAULT);
            if (biome instanceof BiomeDesert) {
                this.structureType = 1;
            }
            else if (biome instanceof BiomeSavanna) {
                this.structureType = 2;
            }
            else if (biome instanceof BiomeTaiga) {
                this.structureType = 3;
            }
            this.setStructureType(this.structureType);
            this.isZombieInfested = (rand.nextInt(50) == 0);
        }
    }
    
    public static class Torch extends Village
    {
        public Torch() {
        }
        
        public Torch(final Start start, final int p_i45568_2_, final Random rand, final StructureBoundingBox p_i45568_4_, final EnumFacing facing) {
            super(start, p_i45568_2_);
            this.setCoordBaseMode(facing);
            this.boundingBox = p_i45568_4_;
        }
        
        public static StructureBoundingBox findPieceBox(final Start start, final List<StructureComponent> p_175856_1_, final Random rand, final int p_175856_3_, final int p_175856_4_, final int p_175856_5_, final EnumFacing facing) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175856_3_, p_175856_4_, p_175856_5_, 0, 0, 0, 3, 4, 2, facing);
            return (StructureComponent.findIntersecting(p_175856_1_, structureboundingbox) != null) ? null : structureboundingbox;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
                if (this.averageGroundLvl < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 4 - 1, 0);
            }
            final IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 2, 3, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.setBlockState(worldIn, iblockstate, 1, 0, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 1, 1, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 1, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.WOOL.getStateFromMeta(EnumDyeColor.WHITE.getDyeDamage()), 1, 3, 0, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.EAST, 2, 3, 0, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.NORTH, 1, 3, 1, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.WEST, 0, 3, 0, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.SOUTH, 1, 3, -1, structureBoundingBoxIn);
            return true;
        }
    }
    
    abstract static class Village extends StructureComponent
    {
        protected int averageGroundLvl;
        private int villagersSpawned;
        protected int structureType;
        protected boolean isZombieInfested;
        
        public Village() {
            this.averageGroundLvl = -1;
        }
        
        protected Village(final Start start, final int type) {
            super(type);
            this.averageGroundLvl = -1;
            if (start != null) {
                this.structureType = start.structureType;
                this.isZombieInfested = start.isZombieInfested;
            }
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            tagCompound.setInteger("HPos", this.averageGroundLvl);
            tagCompound.setInteger("VCount", this.villagersSpawned);
            tagCompound.setByte("Type", (byte)this.structureType);
            tagCompound.setBoolean("Zombie", this.isZombieInfested);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            this.averageGroundLvl = tagCompound.getInteger("HPos");
            this.villagersSpawned = tagCompound.getInteger("VCount");
            this.structureType = tagCompound.getByte("Type");
            if (tagCompound.getBoolean("Desert")) {
                this.structureType = 1;
            }
            this.isZombieInfested = tagCompound.getBoolean("Zombie");
        }
        
        @Nullable
        protected StructureComponent getNextComponentNN(final Start start, final List<StructureComponent> structureComponents, final Random rand, final int p_74891_4_, final int p_74891_5_) {
            final EnumFacing enumfacing = this.getCoordBaseMode();
            if (enumfacing == null) {
                return null;
            }
            switch (enumfacing) {
                default: {
                    return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ + p_74891_5_, EnumFacing.WEST, this.getComponentType());
                }
                case SOUTH: {
                    return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ + p_74891_5_, EnumFacing.WEST, this.getComponentType());
                }
                case WEST: {
                    return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74891_5_, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                }
                case EAST: {
                    return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74891_5_, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                }
            }
        }
        
        @Nullable
        protected StructureComponent getNextComponentPP(final Start start, final List<StructureComponent> structureComponents, final Random rand, final int p_74894_4_, final int p_74894_5_) {
            final EnumFacing enumfacing = this.getCoordBaseMode();
            if (enumfacing == null) {
                return null;
            }
            switch (enumfacing) {
                default: {
                    return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74894_4_, this.boundingBox.minZ + p_74894_5_, EnumFacing.EAST, this.getComponentType());
                }
                case SOUTH: {
                    return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74894_4_, this.boundingBox.minZ + p_74894_5_, EnumFacing.EAST, this.getComponentType());
                }
                case WEST: {
                    return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74894_5_, this.boundingBox.minY + p_74894_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                }
                case EAST: {
                    return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74894_5_, this.boundingBox.minY + p_74894_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                }
            }
        }
        
        protected int getAverageGroundLevel(final World worldIn, final StructureBoundingBox structurebb) {
            int i = 0;
            int j = 0;
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k) {
                for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l) {
                    blockpos$mutableblockpos.setPos(l, 64, k);
                    if (structurebb.isVecInside(blockpos$mutableblockpos)) {
                        i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(), worldIn.provider.getAverageGroundLevel() - 1);
                        ++j;
                    }
                }
            }
            if (j == 0) {
                return -1;
            }
            return i / j;
        }
        
        protected static boolean canVillageGoDeeper(final StructureBoundingBox structurebb) {
            return structurebb != null && structurebb.minY > 10;
        }
        
        protected void spawnVillagers(final World worldIn, final StructureBoundingBox structurebb, final int x, final int y, final int z, final int count) {
            if (this.villagersSpawned < count) {
                for (int i = this.villagersSpawned; i < count; ++i) {
                    final int j = this.getXWithOffset(x + i, z);
                    final int k = this.getYWithOffset(y);
                    final int l = this.getZWithOffset(x + i, z);
                    if (!structurebb.isVecInside(new BlockPos(j, k, l))) {
                        break;
                    }
                    ++this.villagersSpawned;
                    if (this.isZombieInfested) {
                        final EntityZombieVillager entityzombievillager = new EntityZombieVillager(worldIn);
                        entityzombievillager.setLocationAndAngles(j + 0.5, k, l + 0.5, 0.0f, 0.0f);
                        entityzombievillager.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityzombievillager)), null);
                        entityzombievillager.setProfession(this.chooseProfession(i, 0));
                        entityzombievillager.enablePersistence();
                        worldIn.spawnEntity(entityzombievillager);
                    }
                    else {
                        final EntityVillager entityvillager = new EntityVillager(worldIn);
                        entityvillager.setLocationAndAngles(j + 0.5, k, l + 0.5, 0.0f, 0.0f);
                        entityvillager.setProfession(this.chooseProfession(i, worldIn.rand.nextInt(6)));
                        entityvillager.finalizeMobSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityvillager)), null, false);
                        worldIn.spawnEntity(entityvillager);
                    }
                }
            }
        }
        
        protected int chooseProfession(final int villagersSpawnedIn, final int currentVillagerProfession) {
            return currentVillagerProfession;
        }
        
        protected IBlockState getBiomeSpecificBlockState(final IBlockState blockstateIn) {
            if (this.structureType == 1) {
                if (blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2) {
                    return Blocks.SANDSTONE.getDefaultState();
                }
                if (blockstateIn.getBlock() == Blocks.COBBLESTONE) {
                    return Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.getMetadata());
                }
                if (blockstateIn.getBlock() == Blocks.PLANKS) {
                    return Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata());
                }
                if (blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
                    return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, (Comparable)blockstateIn.getValue((IProperty<V>)BlockStairs.FACING));
                }
                if (blockstateIn.getBlock() == Blocks.STONE_STAIRS) {
                    return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, (Comparable)blockstateIn.getValue((IProperty<V>)BlockStairs.FACING));
                }
                if (blockstateIn.getBlock() == Blocks.GRAVEL) {
                    return Blocks.SANDSTONE.getDefaultState();
                }
            }
            else if (this.structureType == 3) {
                if (blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2) {
                    return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLog.LOG_AXIS, (Comparable)blockstateIn.getValue((IProperty<V>)BlockLog.LOG_AXIS));
                }
                if (blockstateIn.getBlock() == Blocks.PLANKS) {
                    return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE);
                }
                if (blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
                    return Blocks.SPRUCE_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, (Comparable)blockstateIn.getValue((IProperty<V>)BlockStairs.FACING));
                }
                if (blockstateIn.getBlock() == Blocks.OAK_FENCE) {
                    return Blocks.SPRUCE_FENCE.getDefaultState();
                }
            }
            else if (this.structureType == 2) {
                if (blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2) {
                    return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLog.LOG_AXIS, (Comparable)blockstateIn.getValue((IProperty<V>)BlockLog.LOG_AXIS));
                }
                if (blockstateIn.getBlock() == Blocks.PLANKS) {
                    return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA);
                }
                if (blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
                    return Blocks.ACACIA_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, (Comparable)blockstateIn.getValue((IProperty<V>)BlockStairs.FACING));
                }
                if (blockstateIn.getBlock() == Blocks.COBBLESTONE) {
                    return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
                }
                if (blockstateIn.getBlock() == Blocks.OAK_FENCE) {
                    return Blocks.ACACIA_FENCE.getDefaultState();
                }
            }
            return blockstateIn;
        }
        
        protected BlockDoor biomeDoor() {
            switch (this.structureType) {
                case 2: {
                    return Blocks.ACACIA_DOOR;
                }
                case 3: {
                    return Blocks.SPRUCE_DOOR;
                }
                default: {
                    return Blocks.OAK_DOOR;
                }
            }
        }
        
        protected void createVillageDoor(final World p_189927_1_, final StructureBoundingBox p_189927_2_, final Random p_189927_3_, final int p_189927_4_, final int p_189927_5_, final int p_189927_6_, final EnumFacing p_189927_7_) {
            if (!this.isZombieInfested) {
                this.generateDoor(p_189927_1_, p_189927_2_, p_189927_3_, p_189927_4_, p_189927_5_, p_189927_6_, EnumFacing.NORTH, this.biomeDoor());
            }
        }
        
        protected void placeTorch(final World p_189926_1_, final EnumFacing p_189926_2_, final int p_189926_3_, final int p_189926_4_, final int p_189926_5_, final StructureBoundingBox p_189926_6_) {
            if (!this.isZombieInfested) {
                this.setBlockState(p_189926_1_, Blocks.TORCH.getDefaultState().withProperty((IProperty<Comparable>)BlockTorch.FACING, p_189926_2_), p_189926_3_, p_189926_4_, p_189926_5_, p_189926_6_);
            }
        }
        
        @Override
        protected void replaceAirAndLiquidDownwards(final World worldIn, final IBlockState blockstateIn, final int x, final int y, final int z, final StructureBoundingBox boundingboxIn) {
            final IBlockState iblockstate = this.getBiomeSpecificBlockState(blockstateIn);
            super.replaceAirAndLiquidDownwards(worldIn, iblockstate, x, y, z, boundingboxIn);
        }
        
        protected void setStructureType(final int p_189924_1_) {
            this.structureType = p_189924_1_;
        }
    }
    
    public static class Well extends Village
    {
        public Well() {
        }
        
        public Well(final Start start, final int type, final Random rand, final int x, final int z) {
            super(start, type);
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));
            if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
                this.boundingBox = new StructureBoundingBox(x, 64, z, x + 6 - 1, 78, z + 6 - 1);
            }
            else {
                this.boundingBox = new StructureBoundingBox(x, 64, z, x + 6 - 1, 78, z + 6 - 1);
            }
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            generateAndAddRoadPiece((Start)componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.WEST, this.getComponentType());
            generateAndAddRoadPiece((Start)componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.EAST, this.getComponentType());
            generateAndAddRoadPiece((Start)componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
            generateAndAddRoadPiece((Start)componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
                if (this.averageGroundLvl < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 3, 0);
            }
            final IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
            final IBlockState iblockstate2 = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 4, 12, 4, iblockstate, Blocks.FLOWING_WATER.getDefaultState(), false);
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 12, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 3, 12, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 12, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 3, 12, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 1, 13, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 1, 14, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 4, 13, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 4, 14, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 1, 13, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 1, 14, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 4, 13, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate2, 4, 14, 4, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 15, 1, 4, 15, 4, iblockstate, iblockstate, false);
            for (int i = 0; i <= 5; ++i) {
                for (int j = 0; j <= 5; ++j) {
                    if (j == 0 || j == 5 || i == 0 || i == 5) {
                        this.setBlockState(worldIn, iblockstate, j, 11, i, structureBoundingBoxIn);
                        this.clearCurrentPositionBlocksUpwards(worldIn, j, 12, i, structureBoundingBoxIn);
                    }
                }
            }
            return true;
        }
    }
    
    public static class WoodHut extends Village
    {
        private boolean isTallHouse;
        private int tablePosition;
        
        public WoodHut() {
        }
        
        public WoodHut(final Start start, final int type, final Random rand, final StructureBoundingBox structurebb, final EnumFacing facing) {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = structurebb;
            this.isTallHouse = rand.nextBoolean();
            this.tablePosition = rand.nextInt(3);
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setInteger("T", this.tablePosition);
            tagCompound.setBoolean("C", this.isTallHouse);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.tablePosition = tagCompound.getInteger("T");
            this.isTallHouse = tagCompound.getBoolean("C");
        }
        
        public static WoodHut createPiece(final Start start, final List<StructureComponent> p_175853_1_, final Random rand, final int p_175853_3_, final int p_175853_4_, final int p_175853_5_, final EnumFacing facing, final int p_175853_7_) {
            final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175853_3_, p_175853_4_, p_175853_5_, 0, 0, 0, 4, 6, 5, facing);
            return (Village.canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175853_1_, structureboundingbox) == null) ? new WoodHut(start, p_175853_7_, rand, structureboundingbox, facing) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
                if (this.averageGroundLvl < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 6 - 1, 0);
            }
            final IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
            final IBlockState iblockstate2 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
            final IBlockState iblockstate3 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.NORTH));
            final IBlockState iblockstate4 = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
            final IBlockState iblockstate5 = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 3, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 3, 0, 4, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 0, 3, Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState(), false);
            if (this.isTallHouse) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 1, 2, 4, 3, iblockstate4, iblockstate4, false);
            }
            else {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 1, 2, 5, 3, iblockstate4, iblockstate4, false);
            }
            this.setBlockState(worldIn, iblockstate4, 1, 4, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate4, 2, 4, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate4, 1, 4, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate4, 2, 4, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate4, 0, 4, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate4, 0, 4, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate4, 0, 4, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate4, 3, 4, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate4, 3, 4, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate4, 3, 4, 3, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 3, 0, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 3, 3, 0, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 4, 0, 3, 4, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 4, 3, 3, 4, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 3, iblockstate2, iblockstate2, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 1, 3, 3, 3, iblockstate2, iblockstate2, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 2, 3, 0, iblockstate2, iblockstate2, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 4, 2, 3, 4, iblockstate2, iblockstate2, false);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 3, 2, 2, structureBoundingBoxIn);
            if (this.tablePosition > 0) {
                this.setBlockState(worldIn, iblockstate5, this.tablePosition, 1, 3, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), this.tablePosition, 2, 3, structureBoundingBoxIn);
            }
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 1, 1, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 1, 2, 0, structureBoundingBoxIn);
            this.createVillageDoor(worldIn, structureBoundingBoxIn, randomIn, 1, 1, 0, EnumFacing.NORTH);
            if (this.getBlockStateFromPos(worldIn, 1, 0, -1, structureBoundingBoxIn).getMaterial() == Material.AIR && this.getBlockStateFromPos(worldIn, 1, -1, -1, structureBoundingBoxIn).getMaterial() != Material.AIR) {
                this.setBlockState(worldIn, iblockstate3, 1, 0, -1, structureBoundingBoxIn);
                if (this.getBlockStateFromPos(worldIn, 1, -1, -1, structureBoundingBoxIn).getBlock() == Blocks.GRASS_PATH) {
                    this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), 1, -1, -1, structureBoundingBoxIn);
                }
            }
            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 4; ++j) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, j, 6, i, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, iblockstate, j, -1, i, structureBoundingBoxIn);
                }
            }
            this.spawnVillagers(worldIn, structureBoundingBoxIn, 1, 1, 2, 1);
            return true;
        }
    }
}
