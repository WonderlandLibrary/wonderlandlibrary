package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class EntityPainting extends EntityHanging {
   public EnumArt art;

   public EntityPainting(World worldIn, BlockPos pos, EnumFacing facing, String title) {
      this(worldIn, pos, facing);

      for(EnumArt entitypainting$enumart : EnumArt.values()) {
         if(entitypainting$enumart.title.equals(title)) {
            this.art = entitypainting$enumart;
            break;
         }
      }

      this.updateFacingWithBoundingBox(facing);
   }

   public EntityPainting(World worldIn, BlockPos pos, EnumFacing facing) {
      super(worldIn, pos);
      List<EnumArt> list = Lists.newArrayList();

      for(EnumArt entitypainting$enumart : EnumArt.values()) {
         this.art = entitypainting$enumart;
         this.updateFacingWithBoundingBox(facing);
         if(this.onValidSurface()) {
            list.add(entitypainting$enumart);
         }
      }

      if(!list.isEmpty()) {
         this.art = (EnumArt)list.get(this.rand.nextInt(list.size()));
      }

      this.updateFacingWithBoundingBox(facing);
   }

   public EntityPainting(World worldIn) {
      super(worldIn);
   }

   public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
      BlockPos blockpos = this.hangingPosition.add(x - this.posX, y - this.posY, z - this.posZ);
      this.setPosition((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
   }

   public void readEntityFromNBT(NBTTagCompound tagCompund) {
      String s = tagCompund.getString("Motive");

      for(EnumArt entitypainting$enumart : EnumArt.values()) {
         if(entitypainting$enumart.title.equals(s)) {
            this.art = entitypainting$enumart;
         }
      }

      if(this.art == null) {
         this.art = EnumArt.KEBAB;
      }

      super.readEntityFromNBT(tagCompund);
   }

   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
      BlockPos blockpos = this.hangingPosition.add(x - this.posX, y - this.posY, z - this.posZ);
      this.setPosition((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
   }

   public void writeEntityToNBT(NBTTagCompound tagCompound) {
      tagCompound.setString("Motive", this.art.title);
      super.writeEntityToNBT(tagCompound);
   }

   public int getHeightPixels() {
      return this.art.sizeY;
   }

   public void onBroken(Entity brokenEntity) {
      if(this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
         if(brokenEntity instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)brokenEntity;
            if(entityplayer.capabilities.isCreativeMode) {
               return;
            }
         }

         this.entityDropItem(new ItemStack(Items.painting), 0.0F);
      }

   }

   public int getWidthPixels() {
      return this.art.sizeX;
   }
}
