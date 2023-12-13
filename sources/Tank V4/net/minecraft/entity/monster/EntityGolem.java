package net.minecraft.entity.monster;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.world.World;

public abstract class EntityGolem extends EntityCreature implements IAnimals {
   protected String getDeathSound() {
      return "none";
   }

   protected String getLivingSound() {
      return "none";
   }

   public int getTalkInterval() {
      return 120;
   }

   public EntityGolem(World var1) {
      super(var1);
   }

   protected String getHurtSound() {
      return "none";
   }

   protected boolean canDespawn() {
      return false;
   }

   public void fall(float var1, float var2) {
   }
}
