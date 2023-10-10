package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
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
   private final BlockStateMapper blockStateMapper = new BlockStateMapper();
   private final Map bakedModelStore = Maps.newIdentityHashMap();
   private final ModelManager modelManager;

   public TextureAtlasSprite getTexture(IBlockState var1) {
      Block var2 = var1.getBlock();
      IBakedModel var3 = this.getModelForState(var1);
      if (var3 == null || var3 == this.modelManager.getMissingModel()) {
         if (var2 == Blocks.wall_sign || var2 == Blocks.standing_sign || var2 == Blocks.chest || var2 == Blocks.trapped_chest || var2 == Blocks.standing_banner || var2 == Blocks.wall_banner) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/planks_oak");
         }

         if (var2 == Blocks.ender_chest) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/obsidian");
         }

         if (var2 == Blocks.flowing_lava || var2 == Blocks.lava) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/lava_still");
         }

         if (var2 == Blocks.flowing_water || var2 == Blocks.water) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/water_still");
         }

         if (var2 == Blocks.skull) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/soul_sand");
         }

         if (var2 == Blocks.barrier) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:items/barrier");
         }
      }

      if (var3 == null) {
         var3 = this.modelManager.getMissingModel();
      }

      return var3.getParticleTexture();
   }

   public void registerBuiltInBlocks(Block... var1) {
      this.blockStateMapper.registerBuiltInBlocks(var1);
   }

   public void registerBlockWithStateMapper(Block var1, IStateMapper var2) {
      this.blockStateMapper.registerBlockStateMapper(var1, var2);
   }

   private void registerAllBlocks() {
      this.registerBuiltInBlocks(Blocks.air, Blocks.flowing_water, Blocks.water, Blocks.flowing_lava, Blocks.lava, Blocks.piston_extension, Blocks.chest, Blocks.ender_chest, Blocks.trapped_chest, Blocks.standing_sign, Blocks.skull, Blocks.end_portal, Blocks.barrier, Blocks.wall_sign, Blocks.wall_banner, Blocks.standing_banner);
      this.registerBlockWithStateMapper(Blocks.stone, (new StateMap.Builder()).withName(BlockStone.VARIANT).build());
      this.registerBlockWithStateMapper(Blocks.prismarine, (new StateMap.Builder()).withName(BlockPrismarine.VARIANT).build());
      this.registerBlockWithStateMapper(Blocks.leaves, (new StateMap.Builder()).withName(BlockOldLeaf.VARIANT).withSuffix("_leaves").ignore(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE).build());
      this.registerBlockWithStateMapper(Blocks.leaves2, (new StateMap.Builder()).withName(BlockNewLeaf.VARIANT).withSuffix("_leaves").ignore(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE).build());
      this.registerBlockWithStateMapper(Blocks.cactus, (new StateMap.Builder()).ignore(BlockCactus.AGE).build());
      this.registerBlockWithStateMapper(Blocks.reeds, (new StateMap.Builder()).ignore(BlockReed.AGE).build());
      this.registerBlockWithStateMapper(Blocks.jukebox, (new StateMap.Builder()).ignore(BlockJukebox.HAS_RECORD).build());
      this.registerBlockWithStateMapper(Blocks.command_block, (new StateMap.Builder()).ignore(BlockCommandBlock.TRIGGERED).build());
      this.registerBlockWithStateMapper(Blocks.cobblestone_wall, (new StateMap.Builder()).withName(BlockWall.VARIANT).withSuffix("_wall").build());
      this.registerBlockWithStateMapper(Blocks.double_plant, (new StateMap.Builder()).withName(BlockDoublePlant.VARIANT).ignore(BlockDoublePlant.field_181084_N).build());
      this.registerBlockWithStateMapper(Blocks.oak_fence_gate, (new StateMap.Builder()).ignore(BlockFenceGate.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.spruce_fence_gate, (new StateMap.Builder()).ignore(BlockFenceGate.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.birch_fence_gate, (new StateMap.Builder()).ignore(BlockFenceGate.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.jungle_fence_gate, (new StateMap.Builder()).ignore(BlockFenceGate.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.dark_oak_fence_gate, (new StateMap.Builder()).ignore(BlockFenceGate.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.acacia_fence_gate, (new StateMap.Builder()).ignore(BlockFenceGate.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.tripwire, (new StateMap.Builder()).ignore(BlockTripWire.DISARMED, BlockTripWire.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.double_wooden_slab, (new StateMap.Builder()).withName(BlockPlanks.VARIANT).withSuffix("_double_slab").build());
      this.registerBlockWithStateMapper(Blocks.wooden_slab, (new StateMap.Builder()).withName(BlockPlanks.VARIANT).withSuffix("_slab").build());
      this.registerBlockWithStateMapper(Blocks.tnt, (new StateMap.Builder()).ignore(BlockTNT.EXPLODE).build());
      this.registerBlockWithStateMapper(Blocks.fire, (new StateMap.Builder()).ignore(BlockFire.AGE).build());
      this.registerBlockWithStateMapper(Blocks.redstone_wire, (new StateMap.Builder()).ignore(BlockRedstoneWire.POWER).build());
      this.registerBlockWithStateMapper(Blocks.oak_door, (new StateMap.Builder()).ignore(BlockDoor.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.spruce_door, (new StateMap.Builder()).ignore(BlockDoor.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.birch_door, (new StateMap.Builder()).ignore(BlockDoor.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.jungle_door, (new StateMap.Builder()).ignore(BlockDoor.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.acacia_door, (new StateMap.Builder()).ignore(BlockDoor.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.dark_oak_door, (new StateMap.Builder()).ignore(BlockDoor.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.iron_door, (new StateMap.Builder()).ignore(BlockDoor.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.wool, (new StateMap.Builder()).withName(BlockColored.COLOR).withSuffix("_wool").build());
      this.registerBlockWithStateMapper(Blocks.carpet, (new StateMap.Builder()).withName(BlockColored.COLOR).withSuffix("_carpet").build());
      this.registerBlockWithStateMapper(Blocks.stained_hardened_clay, (new StateMap.Builder()).withName(BlockColored.COLOR).withSuffix("_stained_hardened_clay").build());
      this.registerBlockWithStateMapper(Blocks.stained_glass_pane, (new StateMap.Builder()).withName(BlockColored.COLOR).withSuffix("_stained_glass_pane").build());
      this.registerBlockWithStateMapper(Blocks.stained_glass, (new StateMap.Builder()).withName(BlockColored.COLOR).withSuffix("_stained_glass").build());
      this.registerBlockWithStateMapper(Blocks.sandstone, (new StateMap.Builder()).withName(BlockSandStone.TYPE).build());
      this.registerBlockWithStateMapper(Blocks.red_sandstone, (new StateMap.Builder()).withName(BlockRedSandstone.TYPE).build());
      this.registerBlockWithStateMapper(Blocks.tallgrass, (new StateMap.Builder()).withName(BlockTallGrass.TYPE).build());
      this.registerBlockWithStateMapper(Blocks.bed, (new StateMap.Builder()).ignore(BlockBed.OCCUPIED).build());
      this.registerBlockWithStateMapper(Blocks.yellow_flower, (new StateMap.Builder()).withName(Blocks.yellow_flower.getTypeProperty()).build());
      this.registerBlockWithStateMapper(Blocks.red_flower, (new StateMap.Builder()).withName(Blocks.red_flower.getTypeProperty()).build());
      this.registerBlockWithStateMapper(Blocks.stone_slab, (new StateMap.Builder()).withName(BlockStoneSlab.VARIANT).withSuffix("_slab").build());
      this.registerBlockWithStateMapper(Blocks.stone_slab2, (new StateMap.Builder()).withName(BlockStoneSlabNew.VARIANT).withSuffix("_slab").build());
      this.registerBlockWithStateMapper(Blocks.monster_egg, (new StateMap.Builder()).withName(BlockSilverfish.VARIANT).withSuffix("_monster_egg").build());
      this.registerBlockWithStateMapper(Blocks.stonebrick, (new StateMap.Builder()).withName(BlockStoneBrick.VARIANT).build());
      this.registerBlockWithStateMapper(Blocks.dispenser, (new StateMap.Builder()).ignore(BlockDispenser.TRIGGERED).build());
      this.registerBlockWithStateMapper(Blocks.dropper, (new StateMap.Builder()).ignore(BlockDropper.TRIGGERED).build());
      this.registerBlockWithStateMapper(Blocks.log, (new StateMap.Builder()).withName(BlockOldLog.VARIANT).withSuffix("_log").build());
      this.registerBlockWithStateMapper(Blocks.log2, (new StateMap.Builder()).withName(BlockNewLog.VARIANT).withSuffix("_log").build());
      this.registerBlockWithStateMapper(Blocks.planks, (new StateMap.Builder()).withName(BlockPlanks.VARIANT).withSuffix("_planks").build());
      this.registerBlockWithStateMapper(Blocks.sapling, (new StateMap.Builder()).withName(BlockSapling.TYPE).withSuffix("_sapling").build());
      this.registerBlockWithStateMapper(Blocks.sand, (new StateMap.Builder()).withName(BlockSand.VARIANT).build());
      this.registerBlockWithStateMapper(Blocks.hopper, (new StateMap.Builder()).ignore(BlockHopper.ENABLED).build());
      this.registerBlockWithStateMapper(Blocks.flower_pot, (new StateMap.Builder()).ignore(BlockFlowerPot.LEGACY_DATA).build());
      this.registerBlockWithStateMapper(Blocks.quartz_block, new StateMapperBase(this) {
         final BlockModelShapes this$0;
         private static volatile int[] $SWITCH_TABLE$net$minecraft$block$BlockQuartz$EnumType;

         {
            this.this$0 = var1;
         }

         protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
            BlockQuartz.EnumType var2 = (BlockQuartz.EnumType)var1.getValue(BlockQuartz.VARIANT);
            switch($SWITCH_TABLE$net$minecraft$block$BlockQuartz$EnumType()[var2.ordinal()]) {
            case 1:
            default:
               return new ModelResourceLocation("quartz_block", "normal");
            case 2:
               return new ModelResourceLocation("chiseled_quartz_block", "normal");
            case 3:
               return new ModelResourceLocation("quartz_column", "axis=y");
            case 4:
               return new ModelResourceLocation("quartz_column", "axis=x");
            case 5:
               return new ModelResourceLocation("quartz_column", "axis=z");
            }
         }

         static int[] $SWITCH_TABLE$net$minecraft$block$BlockQuartz$EnumType() {
            int[] var10000 = $SWITCH_TABLE$net$minecraft$block$BlockQuartz$EnumType;
            if (var10000 != null) {
               return var10000;
            } else {
               int[] var0 = new int[BlockQuartz.EnumType.values().length];

               try {
                  var0[BlockQuartz.EnumType.CHISELED.ordinal()] = 2;
               } catch (NoSuchFieldError var5) {
               }

               try {
                  var0[BlockQuartz.EnumType.DEFAULT.ordinal()] = 1;
               } catch (NoSuchFieldError var4) {
               }

               try {
                  var0[BlockQuartz.EnumType.LINES_X.ordinal()] = 4;
               } catch (NoSuchFieldError var3) {
               }

               try {
                  var0[BlockQuartz.EnumType.LINES_Y.ordinal()] = 3;
               } catch (NoSuchFieldError var2) {
               }

               try {
                  var0[BlockQuartz.EnumType.LINES_Z.ordinal()] = 5;
               } catch (NoSuchFieldError var1) {
               }

               $SWITCH_TABLE$net$minecraft$block$BlockQuartz$EnumType = var0;
               return var0;
            }
         }
      });
      this.registerBlockWithStateMapper(Blocks.deadbush, new StateMapperBase(this) {
         final BlockModelShapes this$0;

         protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
            return new ModelResourceLocation("dead_bush", "normal");
         }

         {
            this.this$0 = var1;
         }
      });
      this.registerBlockWithStateMapper(Blocks.pumpkin_stem, new StateMapperBase(this) {
         final BlockModelShapes this$0;

         protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
            LinkedHashMap var2 = Maps.newLinkedHashMap(var1.getProperties());
            if (var1.getValue(BlockStem.FACING) != EnumFacing.UP) {
               var2.remove(BlockStem.AGE);
            }

            return new ModelResourceLocation((ResourceLocation)Block.blockRegistry.getNameForObject(var1.getBlock()), this.getPropertyString(var2));
         }

         {
            this.this$0 = var1;
         }
      });
      this.registerBlockWithStateMapper(Blocks.melon_stem, new StateMapperBase(this) {
         final BlockModelShapes this$0;

         {
            this.this$0 = var1;
         }

         protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
            LinkedHashMap var2 = Maps.newLinkedHashMap(var1.getProperties());
            if (var1.getValue(BlockStem.FACING) != EnumFacing.UP) {
               var2.remove(BlockStem.AGE);
            }

            return new ModelResourceLocation((ResourceLocation)Block.blockRegistry.getNameForObject(var1.getBlock()), this.getPropertyString(var2));
         }
      });
      this.registerBlockWithStateMapper(Blocks.dirt, new StateMapperBase(this) {
         final BlockModelShapes this$0;

         {
            this.this$0 = var1;
         }

         protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
            LinkedHashMap var2 = Maps.newLinkedHashMap(var1.getProperties());
            String var3 = BlockDirt.VARIANT.getName((Enum)((BlockDirt.DirtType)var2.remove(BlockDirt.VARIANT)));
            if (BlockDirt.DirtType.PODZOL != var1.getValue(BlockDirt.VARIANT)) {
               var2.remove(BlockDirt.SNOWY);
            }

            return new ModelResourceLocation(var3, this.getPropertyString(var2));
         }
      });
      this.registerBlockWithStateMapper(Blocks.double_stone_slab, new StateMapperBase(this) {
         final BlockModelShapes this$0;

         protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
            LinkedHashMap var2 = Maps.newLinkedHashMap(var1.getProperties());
            String var3 = BlockStoneSlab.VARIANT.getName((Enum)((BlockStoneSlab.EnumType)var2.remove(BlockStoneSlab.VARIANT)));
            var2.remove(BlockStoneSlab.SEAMLESS);
            String var4 = (Boolean)var1.getValue(BlockStoneSlab.SEAMLESS) ? "all" : "normal";
            return new ModelResourceLocation(var3 + "_double_slab", var4);
         }

         {
            this.this$0 = var1;
         }
      });
      this.registerBlockWithStateMapper(Blocks.double_stone_slab2, new StateMapperBase(this) {
         final BlockModelShapes this$0;

         protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
            LinkedHashMap var2 = Maps.newLinkedHashMap(var1.getProperties());
            String var3 = BlockStoneSlabNew.VARIANT.getName((Enum)((BlockStoneSlabNew.EnumType)var2.remove(BlockStoneSlabNew.VARIANT)));
            var2.remove(BlockStoneSlab.SEAMLESS);
            String var4 = (Boolean)var1.getValue(BlockStoneSlabNew.SEAMLESS) ? "all" : "normal";
            return new ModelResourceLocation(var3 + "_double_slab", var4);
         }

         {
            this.this$0 = var1;
         }
      });
   }

   public IBakedModel getModelForState(IBlockState var1) {
      IBakedModel var2 = (IBakedModel)this.bakedModelStore.get(var1);
      if (var2 == null) {
         var2 = this.modelManager.getMissingModel();
      }

      return var2;
   }

   public BlockModelShapes(ModelManager var1) {
      this.modelManager = var1;
      this.registerAllBlocks();
   }

   public BlockStateMapper getBlockStateMapper() {
      return this.blockStateMapper;
   }

   public ModelManager getModelManager() {
      return this.modelManager;
   }

   public void reloadModels() {
      this.bakedModelStore.clear();
      Iterator var2 = this.blockStateMapper.putAllStateModelLocations().entrySet().iterator();

      while(var2.hasNext()) {
         Entry var1 = (Entry)var2.next();
         this.bakedModelStore.put((IBlockState)var1.getKey(), this.modelManager.getModel((ModelResourceLocation)var1.getValue()));
      }

   }
}
