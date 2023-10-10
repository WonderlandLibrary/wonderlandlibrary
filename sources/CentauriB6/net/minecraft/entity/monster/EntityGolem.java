package net.minecraft.entity.monster;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.world.World;

public abstract class EntityGolem extends EntityCreature implements IAnimals {
   public EntityGolem(World worldIn) {
      super(worldIn);
   }

   public void fall(float distance, float damageMultiplier) {
   }

   protected String getDeathSound() {
      return "none";
   }

   protected String getHurtSound() {
      return "none";
   }

   protected boolean canDespawn() {
      return false;
   }

   public int getTalkInterval() {
      return 120;
   }

   protected String getLivingSound() {
      return "none";
   }
}
