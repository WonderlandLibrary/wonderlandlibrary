package net.minecraft.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.IAnimals;

public enum EnumCreatureType {
  MONSTER((Class)IMob.class, 70, Material.air, false, false),
  CREATURE((Class)EntityAnimal.class, 10, Material.air, true, true),
  AMBIENT((Class)EntityAmbientCreature.class, 15, Material.air, true, false),
  WATER_CREATURE((Class)EntityWaterMob.class, 5, Material.water, true, false);
  
  private final Class<? extends IAnimals> creatureClass;
  
  private final int maxNumberOfCreature;
  
  private final Material creatureMaterial;
  
  private final boolean isPeacefulCreature;
  
  private final boolean isAnimal;
  
  EnumCreatureType(Class<? extends IAnimals> creatureClassIn, int maxNumberOfCreatureIn, Material creatureMaterialIn, boolean isPeacefulCreatureIn, boolean isAnimalIn) {
    this.creatureClass = creatureClassIn;
    this.maxNumberOfCreature = maxNumberOfCreatureIn;
    this.creatureMaterial = creatureMaterialIn;
    this.isPeacefulCreature = isPeacefulCreatureIn;
    this.isAnimal = isAnimalIn;
  }
  
  public Class<? extends IAnimals> getCreatureClass() {
    return this.creatureClass;
  }
  
  public int getMaxNumberOfCreature() {
    return this.maxNumberOfCreature;
  }
  
  public boolean getPeacefulCreature() {
    return this.isPeacefulCreature;
  }
  
  public boolean getAnimal() {
    return this.isAnimal;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\EnumCreatureType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */