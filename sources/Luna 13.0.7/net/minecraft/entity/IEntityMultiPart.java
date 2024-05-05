package net.minecraft.entity;

import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract interface IEntityMultiPart
{
  public abstract World func_82194_d();
  
  public abstract boolean attackEntityFromPart(EntityDragonPart paramEntityDragonPart, DamageSource paramDamageSource, float paramFloat);
}
