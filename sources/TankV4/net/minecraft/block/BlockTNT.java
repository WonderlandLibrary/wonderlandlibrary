package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTNT extends Block {
   public static final PropertyBool EXPLODE = PropertyBool.create("explode");

   public void explode(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4) {
      if (!var1.isRemote && (Boolean)var3.getValue(EXPLODE)) {
         EntityTNTPrimed var5 = new EntityTNTPrimed(var1, (double)((float)var2.getX() + 0.5F), (double)var2.getY(), (double)((float)var2.getZ() + 0.5F), var4);
         var1.spawnEntityInWorld(var5);
         var1.playSoundAtEntity(var5, "game.tnt.primed", 1.0F, 1.0F);
      }

   }

   public void onEntityCollidedWithBlock(World var1, BlockPos var2, IBlockState var3, Entity var4) {
      if (!var1.isRemote && var4 instanceof EntityArrow) {
         EntityArrow var5 = (EntityArrow)var4;
         if (var5.isBurning()) {
            this.explode(var1, var2, var1.getBlockState(var2).withProperty(EXPLODE, true), var5.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)var5.shootingEntity : null);
            var1.setBlockToAir(var2);
         }
      }

   }

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      super.onBlockAdded(var1, var2, var3);
      if (var1.isBlockPowered(var2)) {
         this.onBlockDestroyedByPlayer(var1, var2, var3.withProperty(EXPLODE, true));
         var1.setBlockToAir(var2);
      }

   }

   public BlockTNT() {
      super(Material.tnt);
      this.setDefaultState(this.blockState.getBaseState().withProperty(EXPLODE, false));
      this.setCreativeTab(CreativeTabs.tabRedstone);
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (var1.isBlockPowered(var2)) {
         this.onBlockDestroyedByPlayer(var1, var2, var3.withProperty(EXPLODE, true));
         var1.setBlockToAir(var2);
      }

   }

   public int getMetaFromState(IBlockState var1) {
      return (Boolean)var1.getValue(EXPLODE) ? 1 : 0;
   }

   public void onBlockDestroyedByPlayer(World var1, BlockPos var2, IBlockState var3) {
      this.explode(var1, var2, var3, (EntityLivingBase)null);
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(EXPLODE, (var1 & 1) > 0);
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{EXPLODE});
   }

   public boolean canDropFromExplosion(Explosion var1) {
      return false;
   }

   public void onBlockDestroyedByExplosion(World var1, BlockPos var2, Explosion var3) {
      if (!var1.isRemote) {
         EntityTNTPrimed var4 = new EntityTNTPrimed(var1, (double)((float)var2.getX() + 0.5F), (double)var2.getY(), (double)((float)var2.getZ() + 0.5F), var3.getExplosivePlacedBy());
         var4.fuse = var1.rand.nextInt(var4.fuse / 4) + var4.fuse / 8;
         var1.spawnEntityInWorld(var4);
      }

   }

   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var4.getCurrentEquippedItem() != null) {
         Item var9 = var4.getCurrentEquippedItem().getItem();
         if (var9 == Items.flint_and_steel || var9 == Items.fire_charge) {
            this.explode(var1, var2, var3.withProperty(EXPLODE, true), var4);
            var1.setBlockToAir(var2);
            if (var9 == Items.flint_and_steel) {
               var4.getCurrentEquippedItem().damageItem(1, var4);
            } else if (!var4.capabilities.isCreativeMode) {
               --var4.getCurrentEquippedItem().stackSize;
            }

            return true;
         }
      }

      return super.onBlockActivated(var1, var2, var3, var4, var5, var6, var7, var8);
   }
}
