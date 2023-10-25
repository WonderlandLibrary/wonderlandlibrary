package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityCaveSpider extends EntitySpider {
  public EntityCaveSpider(World worldIn) {
    super(worldIn);
    setSize(0.7F, 0.5F);
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12.0D);
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    if (super.attackEntityAsMob(entityIn)) {
      if (entityIn instanceof EntityLivingBase) {
        int i = 0;
        if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) {
          i = 7;
        } else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
          i = 15;
        } 
        if (i > 0)
          ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(Potion.poison.id, i * 20, 0)); 
      } 
      return true;
    } 
    return false;
  }
  
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
    return livingdata;
  }
  
  public float getEyeHeight() {
    return 0.45F;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\monster\EntityCaveSpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */