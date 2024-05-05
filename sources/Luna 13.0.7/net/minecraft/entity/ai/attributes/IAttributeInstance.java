package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.UUID;

public abstract interface IAttributeInstance
{
  public abstract IAttribute getAttribute();
  
  public abstract double getBaseValue();
  
  public abstract void setBaseValue(double paramDouble);
  
  public abstract Collection getModifiersByOperation(int paramInt);
  
  public abstract Collection func_111122_c();
  
  public abstract boolean func_180374_a(AttributeModifier paramAttributeModifier);
  
  public abstract AttributeModifier getModifier(UUID paramUUID);
  
  public abstract void applyModifier(AttributeModifier paramAttributeModifier);
  
  public abstract void removeModifier(AttributeModifier paramAttributeModifier);
  
  public abstract void removeAllModifiers();
  
  public abstract double getAttributeValue();
}
