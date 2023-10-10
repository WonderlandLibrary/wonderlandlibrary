package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public class BlockSilverfish extends Block {
   private static volatile int[] $SWITCH_TABLE$net$minecraft$block$BlockSilverfish$EnumType;
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockSilverfish.EnumType.class);

   public int quantityDropped(Random var1) {
      return 0;
   }

   static int[] $SWITCH_TABLE$net$minecraft$block$BlockSilverfish$EnumType() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$block$BlockSilverfish$EnumType;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[BlockSilverfish.EnumType.values().length];

         try {
            var0[BlockSilverfish.EnumType.CHISELED_STONEBRICK.ordinal()] = 6;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[BlockSilverfish.EnumType.COBBLESTONE.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[BlockSilverfish.EnumType.CRACKED_STONEBRICK.ordinal()] = 5;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[BlockSilverfish.EnumType.MOSSY_STONEBRICK.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[BlockSilverfish.EnumType.STONE.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[BlockSilverfish.EnumType.STONEBRICK.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$block$BlockSilverfish$EnumType = var0;
         return var0;
      }
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT});
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, BlockSilverfish.EnumType.byMetadata(var1));
   }

   public int getMetaFromState(IBlockState var1) {
      return ((BlockSilverfish.EnumType)var1.getValue(VARIANT)).getMetadata();
   }

   protected ItemStack createStackedBlock(IBlockState var1) {
      switch($SWITCH_TABLE$net$minecraft$block$BlockSilverfish$EnumType()[((BlockSilverfish.EnumType)var1.getValue(VARIANT)).ordinal()]) {
      case 2:
         return new ItemStack(Blocks.cobblestone);
      case 3:
         return new ItemStack(Blocks.stonebrick);
      case 4:
         return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.MOSSY.getMetadata());
      case 5:
         return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CRACKED.getMetadata());
      case 6:
         return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CHISELED.getMetadata());
      default:
         return new ItemStack(Blocks.stone);
      }
   }

   public BlockSilverfish() {
      super(Material.clay);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockSilverfish.EnumType.STONE));
      this.setHardness(0.0F);
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      BlockSilverfish.EnumType[] var7;
      int var6 = (var7 = BlockSilverfish.EnumType.values()).length;

      for(int var5 = 0; var5 < var6; ++var5) {
         BlockSilverfish.EnumType var4 = var7[var5];
         var3.add(new ItemStack(var1, 1, var4.getMetadata()));
      }

   }

   public int getDamageValue(World var1, BlockPos var2) {
      IBlockState var3 = var1.getBlockState(var2);
      return var3.getBlock().getMetaFromState(var3);
   }

   public static boolean canContainSilverfish(IBlockState var0) {
      Block var1 = var0.getBlock();
      return var0 == Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE) || var1 == Blocks.cobblestone || var1 == Blocks.stonebrick;
   }

   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      if (!var1.isRemote && var1.getGameRules().getBoolean("doTileDrops")) {
         EntitySilverfish var6 = new EntitySilverfish(var1);
         var6.setLocationAndAngles((double)var2.getX() + 0.5D, (double)var2.getY(), (double)var2.getZ() + 0.5D, 0.0F, 0.0F);
         var1.spawnEntityInWorld(var6);
         var6.spawnExplosionParticle();
      }

   }

   public static enum EnumType implements IStringSerializable {
      COBBLESTONE(1, "cobblestone", "cobble") {
         public IBlockState getModelBlock() {
            return Blocks.cobblestone.getDefaultState();
         }
      },
      STONEBRICK(2, "stone_brick", "brick") {
         public IBlockState getModelBlock() {
            return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT);
         }
      };

      private static final BlockSilverfish.EnumType[] ENUM$VALUES = new BlockSilverfish.EnumType[]{STONE, COBBLESTONE, STONEBRICK, MOSSY_STONEBRICK, CRACKED_STONEBRICK, CHISELED_STONEBRICK};
      private static final BlockSilverfish.EnumType[] META_LOOKUP = new BlockSilverfish.EnumType[values().length];
      STONE(0, "stone") {
         public IBlockState getModelBlock() {
            return Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE);
         }
      };

      private final String name;
      CRACKED_STONEBRICK(4, "cracked_brick", "crackedbrick") {
         public IBlockState getModelBlock() {
            return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED);
         }
      };

      private final int meta;
      CHISELED_STONEBRICK(5, "chiseled_brick", "chiseledbrick") {
         public IBlockState getModelBlock() {
            return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
         }
      };

      private final String unlocalizedName;
      MOSSY_STONEBRICK(3, "mossy_brick", "mossybrick") {
         public IBlockState getModelBlock() {
            return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
         }
      };

      EnumType(int var3, String var4, String var5, BlockSilverfish.EnumType var6) {
         this(var3, var4, var5);
      }

      static {
         BlockSilverfish.EnumType[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            BlockSilverfish.EnumType var0 = var3[var1];
            META_LOOKUP[var0.getMetadata()] = var0;
         }

      }

      public static BlockSilverfish.EnumType byMetadata(int var0) {
         if (var0 < 0 || var0 >= META_LOOKUP.length) {
            var0 = 0;
         }

         return META_LOOKUP[var0];
      }

      public String getName() {
         return this.name;
      }

      EnumType(int var3, String var4, BlockSilverfish.EnumType var5) {
         this(var3, var4);
      }

      public int getMetadata() {
         return this.meta;
      }

      private EnumType(int var3, String var4, String var5) {
         this.meta = var3;
         this.name = var4;
         this.unlocalizedName = var5;
      }

      public static BlockSilverfish.EnumType forModelBlock(IBlockState var0) {
         BlockSilverfish.EnumType[] var4;
         int var3 = (var4 = values()).length;

         for(int var2 = 0; var2 < var3; ++var2) {
            BlockSilverfish.EnumType var1 = var4[var2];
            if (var0 == var1.getModelBlock()) {
               return var1;
            }
         }

         return STONE;
      }

      public abstract IBlockState getModelBlock();

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }

      public String toString() {
         return this.name;
      }

      private EnumType(int var3, String var4) {
         this(var3, var4, var4);
      }
   }
}
