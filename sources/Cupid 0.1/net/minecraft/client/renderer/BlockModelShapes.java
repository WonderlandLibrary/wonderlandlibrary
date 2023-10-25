package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockDirt;
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
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStem;
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
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class BlockModelShapes {
  private final Map<IBlockState, IBakedModel> bakedModelStore = Maps.newIdentityHashMap();
  
  private final BlockStateMapper blockStateMapper = new BlockStateMapper();
  
  private final ModelManager modelManager;
  
  public BlockModelShapes(ModelManager manager) {
    this.modelManager = manager;
    registerAllBlocks();
  }
  
  public BlockStateMapper getBlockStateMapper() {
    return this.blockStateMapper;
  }
  
  public TextureAtlasSprite getTexture(IBlockState state) {
    Block block = state.getBlock();
    IBakedModel ibakedmodel = getModelForState(state);
    if (ibakedmodel == null || ibakedmodel == this.modelManager.getMissingModel()) {
      if (block == Blocks.wall_sign || block == Blocks.standing_sign || block == Blocks.chest || block == Blocks.trapped_chest || block == Blocks.standing_banner || block == Blocks.wall_banner)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/planks_oak"); 
      if (block == Blocks.ender_chest)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/obsidian"); 
      if (block == Blocks.flowing_lava || block == Blocks.lava)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/lava_still"); 
      if (block == Blocks.flowing_water || block == Blocks.water)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/water_still"); 
      if (block == Blocks.skull)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/soul_sand"); 
      if (block == Blocks.barrier)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:items/barrier"); 
    } 
    if (ibakedmodel == null)
      ibakedmodel = this.modelManager.getMissingModel(); 
    return ibakedmodel.getParticleTexture();
  }
  
  public IBakedModel getModelForState(IBlockState state) {
    IBakedModel ibakedmodel = this.bakedModelStore.get(state);
    if (ibakedmodel == null)
      ibakedmodel = this.modelManager.getMissingModel(); 
    return ibakedmodel;
  }
  
  public ModelManager getModelManager() {
    return this.modelManager;
  }
  
  public void reloadModels() {
    this.bakedModelStore.clear();
    for (Map.Entry<IBlockState, ModelResourceLocation> entry : (Iterable<Map.Entry<IBlockState, ModelResourceLocation>>)this.blockStateMapper.putAllStateModelLocations().entrySet())
      this.bakedModelStore.put(entry.getKey(), this.modelManager.getModel(entry.getValue())); 
  }
  
  public void registerBlockWithStateMapper(Block assoc, IStateMapper stateMapper) {
    this.blockStateMapper.registerBlockStateMapper(assoc, stateMapper);
  }
  
  public void registerBuiltInBlocks(Block... builtIns) {
    this.blockStateMapper.registerBuiltInBlocks(builtIns);
  }
  
  private void registerAllBlocks() {
    registerBuiltInBlocks(new Block[] { 
          Blocks.air, (Block)Blocks.flowing_water, (Block)Blocks.water, (Block)Blocks.flowing_lava, (Block)Blocks.lava, (Block)Blocks.piston_extension, (Block)Blocks.chest, Blocks.ender_chest, Blocks.trapped_chest, Blocks.standing_sign, 
          (Block)Blocks.skull, Blocks.end_portal, Blocks.barrier, Blocks.wall_sign, Blocks.wall_banner, Blocks.standing_banner });
    registerBlockWithStateMapper(Blocks.stone, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockStone.VARIANT).build());
    registerBlockWithStateMapper(Blocks.prismarine, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockPrismarine.VARIANT).build());
    registerBlockWithStateMapper((Block)Blocks.leaves, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockOldLeaf.VARIANT).withSuffix("_leaves").ignore(new IProperty[] { (IProperty)BlockLeaves.CHECK_DECAY, (IProperty)BlockLeaves.DECAYABLE }).build());
    registerBlockWithStateMapper((Block)Blocks.leaves2, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockNewLeaf.VARIANT).withSuffix("_leaves").ignore(new IProperty[] { (IProperty)BlockLeaves.CHECK_DECAY, (IProperty)BlockLeaves.DECAYABLE }).build());
    registerBlockWithStateMapper((Block)Blocks.cactus, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockCactus.AGE }).build());
    registerBlockWithStateMapper((Block)Blocks.reeds, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockReed.AGE }).build());
    registerBlockWithStateMapper(Blocks.jukebox, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockJukebox.HAS_RECORD }).build());
    registerBlockWithStateMapper(Blocks.command_block, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockCommandBlock.TRIGGERED }).build());
    registerBlockWithStateMapper(Blocks.cobblestone_wall, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockWall.VARIANT).withSuffix("_wall").build());
    registerBlockWithStateMapper((Block)Blocks.double_plant, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockDoublePlant.VARIANT).ignore(new IProperty[] { (IProperty)BlockDoublePlant.field_181084_N }).build());
    registerBlockWithStateMapper(Blocks.oak_fence_gate, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFenceGate.POWERED }).build());
    registerBlockWithStateMapper(Blocks.spruce_fence_gate, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFenceGate.POWERED }).build());
    registerBlockWithStateMapper(Blocks.birch_fence_gate, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFenceGate.POWERED }).build());
    registerBlockWithStateMapper(Blocks.jungle_fence_gate, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFenceGate.POWERED }).build());
    registerBlockWithStateMapper(Blocks.dark_oak_fence_gate, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFenceGate.POWERED }).build());
    registerBlockWithStateMapper(Blocks.acacia_fence_gate, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFenceGate.POWERED }).build());
    registerBlockWithStateMapper(Blocks.tripwire, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockTripWire.DISARMED, (IProperty)BlockTripWire.POWERED }).build());
    registerBlockWithStateMapper((Block)Blocks.double_wooden_slab, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockPlanks.VARIANT).withSuffix("_double_slab").build());
    registerBlockWithStateMapper((Block)Blocks.wooden_slab, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockPlanks.VARIANT).withSuffix("_slab").build());
    registerBlockWithStateMapper(Blocks.tnt, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockTNT.EXPLODE }).build());
    registerBlockWithStateMapper((Block)Blocks.fire, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFire.AGE }).build());
    registerBlockWithStateMapper((Block)Blocks.redstone_wire, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockRedstoneWire.POWER }).build());
    registerBlockWithStateMapper(Blocks.oak_door, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDoor.POWERED }).build());
    registerBlockWithStateMapper(Blocks.spruce_door, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDoor.POWERED }).build());
    registerBlockWithStateMapper(Blocks.birch_door, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDoor.POWERED }).build());
    registerBlockWithStateMapper(Blocks.jungle_door, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDoor.POWERED }).build());
    registerBlockWithStateMapper(Blocks.acacia_door, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDoor.POWERED }).build());
    registerBlockWithStateMapper(Blocks.dark_oak_door, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDoor.POWERED }).build());
    registerBlockWithStateMapper(Blocks.iron_door, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDoor.POWERED }).build());
    registerBlockWithStateMapper(Blocks.wool, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockColored.COLOR).withSuffix("_wool").build());
    registerBlockWithStateMapper(Blocks.carpet, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockColored.COLOR).withSuffix("_carpet").build());
    registerBlockWithStateMapper(Blocks.stained_hardened_clay, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockColored.COLOR).withSuffix("_stained_hardened_clay").build());
    registerBlockWithStateMapper((Block)Blocks.stained_glass_pane, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockColored.COLOR).withSuffix("_stained_glass_pane").build());
    registerBlockWithStateMapper((Block)Blocks.stained_glass, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockColored.COLOR).withSuffix("_stained_glass").build());
    registerBlockWithStateMapper(Blocks.sandstone, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockSandStone.TYPE).build());
    registerBlockWithStateMapper(Blocks.red_sandstone, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockRedSandstone.TYPE).build());
    registerBlockWithStateMapper((Block)Blocks.tallgrass, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockTallGrass.TYPE).build());
    registerBlockWithStateMapper(Blocks.bed, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockBed.OCCUPIED }).build());
    registerBlockWithStateMapper((Block)Blocks.yellow_flower, (IStateMapper)(new StateMap.Builder()).withName(Blocks.yellow_flower.getTypeProperty()).build());
    registerBlockWithStateMapper((Block)Blocks.red_flower, (IStateMapper)(new StateMap.Builder()).withName(Blocks.red_flower.getTypeProperty()).build());
    registerBlockWithStateMapper((Block)Blocks.stone_slab, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockStoneSlab.VARIANT).withSuffix("_slab").build());
    registerBlockWithStateMapper((Block)Blocks.stone_slab2, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockStoneSlabNew.VARIANT).withSuffix("_slab").build());
    registerBlockWithStateMapper(Blocks.monster_egg, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockSilverfish.VARIANT).withSuffix("_monster_egg").build());
    registerBlockWithStateMapper(Blocks.stonebrick, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockStoneBrick.VARIANT).build());
    registerBlockWithStateMapper(Blocks.dispenser, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDispenser.TRIGGERED }).build());
    registerBlockWithStateMapper(Blocks.dropper, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDropper.TRIGGERED }).build());
    registerBlockWithStateMapper(Blocks.log, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockOldLog.VARIANT).withSuffix("_log").build());
    registerBlockWithStateMapper(Blocks.log2, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockNewLog.VARIANT).withSuffix("_log").build());
    registerBlockWithStateMapper(Blocks.planks, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockPlanks.VARIANT).withSuffix("_planks").build());
    registerBlockWithStateMapper(Blocks.sapling, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockSapling.TYPE).withSuffix("_sapling").build());
    registerBlockWithStateMapper((Block)Blocks.sand, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockSand.VARIANT).build());
    registerBlockWithStateMapper((Block)Blocks.hopper, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockHopper.ENABLED }).build());
    registerBlockWithStateMapper(Blocks.flower_pot, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFlowerPot.LEGACY_DATA }).build());
    registerBlockWithStateMapper(Blocks.quartz_block, (IStateMapper)new StateMapperBase() {
          protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            BlockQuartz.EnumType blockquartz$enumtype = (BlockQuartz.EnumType)state.getValue((IProperty)BlockQuartz.VARIANT);
            switch (blockquartz$enumtype) {
              default:
                return new ModelResourceLocation("quartz_block", "normal");
              case CHISELED:
                return new ModelResourceLocation("chiseled_quartz_block", "normal");
              case LINES_Y:
                return new ModelResourceLocation("quartz_column", "axis=y");
              case LINES_X:
                return new ModelResourceLocation("quartz_column", "axis=x");
              case LINES_Z:
                break;
            } 
            return new ModelResourceLocation("quartz_column", "axis=z");
          }
        });
    registerBlockWithStateMapper((Block)Blocks.deadbush, (IStateMapper)new StateMapperBase() {
          protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            return new ModelResourceLocation("dead_bush", "normal");
          }
        });
    registerBlockWithStateMapper(Blocks.pumpkin_stem, (IStateMapper)new StateMapperBase() {
          protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            Map<IProperty, Comparable> map = Maps.newLinkedHashMap((Map)state.getProperties());
            if (state.getValue((IProperty)BlockStem.FACING) != EnumFacing.UP)
              map.remove(BlockStem.AGE); 
            return new ModelResourceLocation((ResourceLocation)Block.blockRegistry.getNameForObject(state.getBlock()), getPropertyString(map));
          }
        });
    registerBlockWithStateMapper(Blocks.melon_stem, (IStateMapper)new StateMapperBase() {
          protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            Map<IProperty, Comparable> map = Maps.newLinkedHashMap((Map)state.getProperties());
            if (state.getValue((IProperty)BlockStem.FACING) != EnumFacing.UP)
              map.remove(BlockStem.AGE); 
            return new ModelResourceLocation((ResourceLocation)Block.blockRegistry.getNameForObject(state.getBlock()), getPropertyString(map));
          }
        });
    registerBlockWithStateMapper(Blocks.dirt, (IStateMapper)new StateMapperBase() {
          protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            Map<IProperty, Comparable> map = Maps.newLinkedHashMap((Map)state.getProperties());
            String s = BlockDirt.VARIANT.getName((Enum)map.remove(BlockDirt.VARIANT));
            if (BlockDirt.DirtType.PODZOL != state.getValue((IProperty)BlockDirt.VARIANT))
              map.remove(BlockDirt.SNOWY); 
            return new ModelResourceLocation(s, getPropertyString(map));
          }
        });
    registerBlockWithStateMapper((Block)Blocks.double_stone_slab, (IStateMapper)new StateMapperBase() {
          protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            Map<IProperty, Comparable> map = Maps.newLinkedHashMap((Map)state.getProperties());
            String s = BlockStoneSlab.VARIANT.getName((Enum)map.remove(BlockStoneSlab.VARIANT));
            map.remove(BlockStoneSlab.SEAMLESS);
            String s1 = ((Boolean)state.getValue((IProperty)BlockStoneSlab.SEAMLESS)).booleanValue() ? "all" : "normal";
            return new ModelResourceLocation(s + "_double_slab", s1);
          }
        });
    registerBlockWithStateMapper((Block)Blocks.double_stone_slab2, (IStateMapper)new StateMapperBase() {
          protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            Map<IProperty, Comparable> map = Maps.newLinkedHashMap((Map)state.getProperties());
            String s = BlockStoneSlabNew.VARIANT.getName((Enum)map.remove(BlockStoneSlabNew.VARIANT));
            map.remove(BlockStoneSlab.SEAMLESS);
            String s1 = ((Boolean)state.getValue((IProperty)BlockStoneSlabNew.SEAMLESS)).booleanValue() ? "all" : "normal";
            return new ModelResourceLocation(s + "_double_slab", s1);
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\BlockModelShapes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */