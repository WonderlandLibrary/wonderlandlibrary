package net.minecraft.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;

public enum EnumCreatureType {
   MONSTER(IMob.class, 70, Material.air, false, false);

   private final int maxNumberOfCreature;
   private final Class creatureClass;
   private final boolean isAnimal;
   private final boolean isPeacefulCreature;
   WATER_CREATURE(EntityWaterMob.class, 5, Material.water, true, false),
   CREATURE(EntityAnimal.class, 10, Material.air, true, true);

   private static final EnumCreatureType[] ENUM$VALUES = new EnumCreatureType[]{MONSTER, CREATURE, AMBIENT, WATER_CREATURE};
   AMBIENT(EntityAmbientCreature.class, 15, Material.air, true, false);

   private final Material creatureMaterial;

   public boolean getAnimal() {
      return this.isAnimal;
   }

   public int getMaxNumberOfCreature() {
      return this.maxNumberOfCreature;
   }

   public boolean getPeacefulCreature() {
      return this.isPeacefulCreature;
   }

   public Class getCreatureClass() {
      return this.creatureClass;
   }

   private EnumCreatureType(Class var3, int var4, Material var5, boolean var6, boolean var7) {
      this.creatureClass = var3;
      this.maxNumberOfCreature = var4;
      this.creatureMaterial = var5;
      this.isPeacefulCreature = var6;
      this.isAnimal = var7;
   }
}
