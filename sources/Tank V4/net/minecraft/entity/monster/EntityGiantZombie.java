package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityGiantZombie extends EntityMob {
   public EntityGiantZombie(World var1) {
      super(var1);
      this.setSize(this.width * 6.0F, this.height * 6.0F);
   }

   public float getEyeHeight() {
      return 10.440001F;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(50.0D);
   }

   public float getBlockPathWeight(BlockPos var1) {
      return this.worldObj.getLightBrightness(var1) - 0.5F;
   }
}
