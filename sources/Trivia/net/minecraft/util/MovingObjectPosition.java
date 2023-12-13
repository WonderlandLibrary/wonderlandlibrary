package net.minecraft.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;

public class MovingObjectPosition {
	private BlockPos blockPos;

	/** What type of ray trace hit was this? 0 = block, 1 = entity */
	public MovingObjectPosition.MovingObjectType typeOfHit;
	public EnumFacing sideHit;

	/** The vector position of the hit */
	public Vec3 hitVec;

	/** The hit entity */
	public Entity entityHit;

	

	public MovingObjectPosition(Vec3 hitVecIn, EnumFacing facing, BlockPos blockPosIn) {
		this(MovingObjectPosition.MovingObjectType.BLOCK, hitVecIn, facing, blockPosIn);
	}

	public MovingObjectPosition(Vec3 p_i45552_1_, EnumFacing facing) {
		this(MovingObjectPosition.MovingObjectType.BLOCK, p_i45552_1_, facing, BlockPos.ORIGIN);
	}
	
	public EntityLivingBase getMouseOverEntity()
	  {
	    EntityLivingBase re = null;
	    if (Minecraft.getMinecraft().objectMouseOver != null) {
	      if (((Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityLivingBase)) && 
	        (!(Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityArmorStand)))
	      {
	        re = (EntityLivingBase)Minecraft.getMinecraft().objectMouseOver.entityHit;
	        if (!check(re)) {
	          re = null;
	        }
	      }
	    }
	    return re;
	  }
	 public boolean check(EntityLivingBase in)
	  {
	   
	    if (in instanceof EntityPlayer) {
	      return true;
	    }
	   
	    
	    return true;
	  }
	
	 
	public MovingObjectPosition(Entity p_i2304_1_) {
		this(p_i2304_1_, new Vec3(p_i2304_1_.posX, p_i2304_1_.posY, p_i2304_1_.posZ));
	}

	public MovingObjectPosition(MovingObjectPosition.MovingObjectType typeOfHitIn, Vec3 hitVecIn, EnumFacing sideHitIn,
			BlockPos blockPosIn) {
		this.typeOfHit = typeOfHitIn;
		this.blockPos = blockPosIn;
		this.sideHit = sideHitIn;
		this.hitVec = new Vec3(hitVecIn.xCoord, hitVecIn.yCoord, hitVecIn.zCoord);
	}

	public MovingObjectPosition(Entity entityHitIn, Vec3 hitVecIn) {
		this.typeOfHit = MovingObjectPosition.MovingObjectType.ENTITY;
		this.entityHit = entityHitIn;
		this.hitVec = hitVecIn;
	}

	public BlockPos getBlockPos() {
		return this.blockPos;
	}

	public String toString() {
		return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.blockPos + ", f=" + this.sideHit + ", pos="
				+ this.hitVec + ", entity=" + this.entityHit + '}';
	}

	public static enum MovingObjectType {
		MISS, BLOCK, ENTITY;
	}
}
