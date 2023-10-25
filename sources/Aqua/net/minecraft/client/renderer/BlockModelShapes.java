package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockWall;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;

public class BlockModelShapes {
    private final Map<IBlockState, IBakedModel> bakedModelStore = Maps.newIdentityHashMap();
    private final BlockStateMapper blockStateMapper = new BlockStateMapper();
    private final ModelManager modelManager;

    public BlockModelShapes(ModelManager manager) {
        this.modelManager = manager;
        this.registerAllBlocks();
    }

    public BlockStateMapper getBlockStateMapper() {
        return this.blockStateMapper;
    }

    public TextureAtlasSprite getTexture(IBlockState state) {
        Block block = state.getBlock();
        IBakedModel ibakedmodel = this.getModelForState(state);
        if (ibakedmodel == null || ibakedmodel == this.modelManager.getMissingModel()) {
            if (block == Blocks.wall_sign || block == Blocks.standing_sign || block == Blocks.chest || block == Blocks.trapped_chest || block == Blocks.standing_banner || block == Blocks.wall_banner) {
                return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/planks_oak");
            }
            if (block == Blocks.ender_chest) {
                return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/obsidian");
            }
            if (block == Blocks.flowing_lava || block == Blocks.lava) {
                return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/lava_still");
            }
            if (block == Blocks.flowing_water || block == Blocks.water) {
                return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/water_still");
            }
            if (block == Blocks.skull) {
                return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/soul_sand");
            }
            if (block == Blocks.barrier) {
                return this.modelManager.getTextureMap().getAtlasSprite("minecraft:items/barrier");
            }
        }
        if (ibakedmodel == null) {
            ibakedmodel = this.modelManager.getMissingModel();
        }
        return ibakedmodel.getParticleTexture();
    }

    public IBakedModel getModelForState(IBlockState state) {
        IBakedModel ibakedmodel = (IBakedModel)this.bakedModelStore.get((Object)state);
        if (ibakedmodel == null) {
            ibakedmodel = this.modelManager.getMissingModel();
        }
        return ibakedmodel;
    }

    public ModelManager getModelManager() {
        return this.modelManager;
    }

    public void reloadModels() {
        this.bakedModelStore.clear();
        for (Map.Entry entry : this.blockStateMapper.putAllStateModelLocations().entrySet()) {
            this.bakedModelStore.put(entry.getKey(), (Object)this.modelManager.getModel((ModelResourceLocation)entry.getValue()));
        }
    }

    public void registerBlockWithStateMapper(Block assoc, IStateMapper stateMapper) {
        this.blockStateMapper.registerBlockStateMapper(assoc, stateMapper);
    }

    public void registerBuiltInBlocks(Block ... builtIns) {
        this.blockStateMapper.registerBuiltInBlocks(builtIns);
    }

    private void registerAllBlocks() {
        this.registerBuiltInBlocks(new Block[]{Blocks.air, Blocks.flowing_water, Blocks.water, Blocks.flowing_lava, Blocks.lava, Blocks.piston_extension, Blocks.chest, Blocks.ender_chest, Blocks.trapped_chest, Blocks.standing_sign, Blocks.skull, Blocks.end_portal, Blocks.barrier, Blocks.wall_sign, Blocks.wall_banner, Blocks.standing_banner});
        this.registerBlockWithStateMapper(Blocks.stone, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockStone.VARIANT).build());
        this.registerBlockWithStateMapper(Blocks.prismarine, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockPrismarine.VARIANT).build());
        this.registerBlockWithStateMapper((Block)Blocks.leaves, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockOldLeaf.VARIANT).withSuffix("_leaves").ignore(new IProperty[]{BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE}).build());
        this.registerBlockWithStateMapper((Block)Blocks.leaves2, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockNewLeaf.VARIANT).withSuffix("_leaves").ignore(new IProperty[]{BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE}).build());
        this.registerBlockWithStateMapper((Block)Blocks.cactus, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockCactus.AGE}).build());
        this.registerBlockWithStateMapper((Block)Blocks.reeds, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockReed.AGE}).build());
        this.registerBlockWithStateMapper(Blocks.jukebox, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockJukebox.HAS_RECORD}).build());
        this.registerBlockWithStateMapper(Blocks.command_block, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockCommandBlock.TRIGGERED}).build());
        this.registerBlockWithStateMapper(Blocks.cobblestone_wall, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockWall.VARIANT).withSuffix("_wall").build());
        this.registerBlockWithStateMapper((Block)Blocks.double_plant, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockDoublePlant.VARIANT).ignore(new IProperty[]{BlockDoublePlant.FACING}).build());
        this.registerBlockWithStateMapper(Blocks.oak_fence_gate, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockFenceGate.POWERED}).build());
        this.registerBlockWithStateMapper(Blocks.spruce_fence_gate, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockFenceGate.POWERED}).build());
        this.registerBlockWithStateMapper(Blocks.birch_fence_gate, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockFenceGate.POWERED}).build());
        this.registerBlockWithStateMapper(Blocks.jungle_fence_gate, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockFenceGate.POWERED}).build());
        this.registerBlockWithStateMapper(Blocks.dark_oak_fence_gate, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockFenceGate.POWERED}).build());
        this.registerBlockWithStateMapper(Blocks.acacia_fence_gate, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockFenceGate.POWERED}).build());
        this.registerBlockWithStateMapper(Blocks.tripwire, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockTripWire.DISARMED, BlockTripWire.POWERED}).build());
        this.registerBlockWithStateMapper((Block)Blocks.double_wooden_slab, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockPlanks.VARIANT).withSuffix("_double_slab").build());
        this.registerBlockWithStateMapper((Block)Blocks.wooden_slab, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockPlanks.VARIANT).withSuffix("_slab").build());
        this.registerBlockWithStateMapper(Blocks.tnt, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockTNT.EXPLODE}).build());
        this.registerBlockWithStateMapper((Block)Blocks.fire, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockFire.AGE}).build());
        this.registerBlockWithStateMapper((Block)Blocks.redstone_wire, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockRedstoneWire.POWER}).build());
        this.registerBlockWithStateMapper(Blocks.oak_door, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockDoor.POWERED}).build());
        this.registerBlockWithStateMapper(Blocks.spruce_door, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockDoor.POWERED}).build());
        this.registerBlockWithStateMapper(Blocks.birch_door, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockDoor.POWERED}).build());
        this.registerBlockWithStateMapper(Blocks.jungle_door, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockDoor.POWERED}).build());
        this.registerBlockWithStateMapper(Blocks.acacia_door, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockDoor.POWERED}).build());
        this.registerBlockWithStateMapper(Blocks.dark_oak_door, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockDoor.POWERED}).build());
        this.registerBlockWithStateMapper(Blocks.iron_door, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockDoor.POWERED}).build());
        this.registerBlockWithStateMapper(Blocks.wool, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockColored.COLOR).withSuffix("_wool").build());
        this.registerBlockWithStateMapper(Blocks.carpet, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockColored.COLOR).withSuffix("_carpet").build());
        this.registerBlockWithStateMapper(Blocks.stained_hardened_clay, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockColored.COLOR).withSuffix("_stained_hardened_clay").build());
        this.registerBlockWithStateMapper((Block)Blocks.stained_glass_pane, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockColored.COLOR).withSuffix("_stained_glass_pane").build());
        this.registerBlockWithStateMapper((Block)Blocks.stained_glass, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockColored.COLOR).withSuffix("_stained_glass").build());
        this.registerBlockWithStateMapper(Blocks.sandstone, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockSandStone.TYPE).build());
        this.registerBlockWithStateMapper(Blocks.red_sandstone, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockRedSandstone.TYPE).build());
        this.registerBlockWithStateMapper((Block)Blocks.tallgrass, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockTallGrass.TYPE).build());
        this.registerBlockWithStateMapper(Blocks.bed, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockBed.OCCUPIED}).build());
        this.registerBlockWithStateMapper((Block)Blocks.yellow_flower, (IStateMapper)new StateMap.Builder().withName(Blocks.yellow_flower.getTypeProperty()).build());
        this.registerBlockWithStateMapper((Block)Blocks.red_flower, (IStateMapper)new StateMap.Builder().withName(Blocks.red_flower.getTypeProperty()).build());
        this.registerBlockWithStateMapper((Block)Blocks.stone_slab, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockStoneSlab.VARIANT).withSuffix("_slab").build());
        this.registerBlockWithStateMapper((Block)Blocks.stone_slab2, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockStoneSlabNew.VARIANT).withSuffix("_slab").build());
        this.registerBlockWithStateMapper(Blocks.monster_egg, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockSilverfish.VARIANT).withSuffix("_monster_egg").build());
        this.registerBlockWithStateMapper(Blocks.stonebrick, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockStoneBrick.VARIANT).build());
        this.registerBlockWithStateMapper(Blocks.dispenser, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockDispenser.TRIGGERED}).build());
        this.registerBlockWithStateMapper(Blocks.dropper, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockDropper.TRIGGERED}).build());
        this.registerBlockWithStateMapper(Blocks.log, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockOldLog.VARIANT).withSuffix("_log").build());
        this.registerBlockWithStateMapper(Blocks.log2, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockNewLog.VARIANT).withSuffix("_log").build());
        this.registerBlockWithStateMapper(Blocks.planks, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockPlanks.VARIANT).withSuffix("_planks").build());
        this.registerBlockWithStateMapper(Blocks.sapling, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockSapling.TYPE).withSuffix("_sapling").build());
        this.registerBlockWithStateMapper((Block)Blocks.sand, (IStateMapper)new StateMap.Builder().withName((IProperty)BlockSand.VARIANT).build());
        this.registerBlockWithStateMapper((Block)Blocks.hopper, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockHopper.ENABLED}).build());
        this.registerBlockWithStateMapper(Blocks.flower_pot, (IStateMapper)new StateMap.Builder().ignore(new IProperty[]{BlockFlowerPot.LEGACY_DATA}).build());
        this.registerBlockWithStateMapper(Blocks.quartz_block, (IStateMapper)new /* Unavailable Anonymous Inner Class!! */);
        this.registerBlockWithStateMapper((Block)Blocks.deadbush, (IStateMapper)new /* Unavailable Anonymous Inner Class!! */);
        this.registerBlockWithStateMapper(Blocks.pumpkin_stem, (IStateMapper)new /* Unavailable Anonymous Inner Class!! */);
        this.registerBlockWithStateMapper(Blocks.melon_stem, (IStateMapper)new /* Unavailable Anonymous Inner Class!! */);
        this.registerBlockWithStateMapper(Blocks.dirt, (IStateMapper)new /* Unavailable Anonymous Inner Class!! */);
        this.registerBlockWithStateMapper((Block)Blocks.double_stone_slab, (IStateMapper)new /* Unavailable Anonymous Inner Class!! */);
        this.registerBlockWithStateMapper((Block)Blocks.double_stone_slab2, (IStateMapper)new /* Unavailable Anonymous Inner Class!! */);
    }
}
