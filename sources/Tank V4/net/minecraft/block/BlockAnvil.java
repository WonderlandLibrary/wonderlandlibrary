package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class BlockAnvil extends BlockFalling {
   public static final PropertyDirection FACING;
   public static final PropertyInteger DAMAGE;

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING, DAMAGE});
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      EnumFacing var3 = (EnumFacing)var1.getBlockState(var2).getValue(FACING);
      if (var3.getAxis() == EnumFacing.Axis.X) {
         this.setBlockBounds(0.0F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
      } else {
         this.setBlockBounds(0.125F, 0.0F, 0.0F, 0.875F, 1.0F, 1.0F);
      }

   }

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      EnumFacing var9 = var8.getHorizontalFacing().rotateY();
      return super.onBlockPlaced(var1, var2, var3, var4, var5, var6, var7, var8).withProperty(FACING, var9).withProperty(DAMAGE, var7 >> 2);
   }

   protected BlockAnvil() {
      super(Material.anvil);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(DAMAGE, 0));
      this.setLightOpacity(0);
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public void onEndFalling(World var1, BlockPos var2) {
      var1.playAuxSFX(1022, var2, 0);
   }

   public boolean isFullCube() {
      return false;
   }

   public boolean shouldSideBeRendered(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      return true;
   }

   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      if (!var1.isRemote) {
         var4.displayGui(new BlockAnvil.Anvil(var1, var2));
      }

      return true;
   }

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      var3.add(new ItemStack(var1, 1, 0));
      var3.add(new ItemStack(var1, 1, 1));
      var3.add(new ItemStack(var1, 1, 2));
   }

   public IBlockState getStateForEntityRender(IBlockState var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
   }

   public int damageDropped(IBlockState var1) {
      return (Integer)var1.getValue(DAMAGE);
   }

   protected void onStartFalling(EntityFallingBlock var1) {
      var1.setHurtEntities(true);
   }

   static {
      FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
      DAMAGE = PropertyInteger.create("damage", 0, 2);
   }

   public int getMetaFromState(IBlockState var1) {
      byte var2 = 0;
      int var4 = var2 | ((EnumFacing)var1.getValue(FACING)).getHorizontalIndex();
      var4 |= (Integer)var1.getValue(DAMAGE) << 2;
      return var4;
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(var1 & 3)).withProperty(DAMAGE, (var1 & 15) >> 2);
   }

   public static class Anvil implements IInteractionObject {
      private final World world;
      private final BlockPos position;

      public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
         return new ContainerRepair(var1, this.world, this.position, var2);
      }

      public boolean hasCustomName() {
         return false;
      }

      public Anvil(World var1, BlockPos var2) {
         this.world = var1;
         this.position = var2;
      }

      public String getName() {
         return "anvil";
      }

      public String getGuiID() {
         return "minecraft:anvil";
      }

      public IChatComponent getDisplayName() {
         return new ChatComponentTranslation(Blocks.anvil.getUnlocalizedName() + ".name", new Object[0]);
      }
   }
}
