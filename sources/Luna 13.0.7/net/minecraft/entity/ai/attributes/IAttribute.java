package net.minecraft.entity.ai.attributes;

public abstract interface IAttribute
{
  public abstract String getAttributeUnlocalizedName();
  
  public abstract double clampValue(double paramDouble);
  
  public abstract double getDefaultValue();
  
  public abstract boolean getShouldWatch();
  
  public abstract IAttribute func_180372_d();
}
