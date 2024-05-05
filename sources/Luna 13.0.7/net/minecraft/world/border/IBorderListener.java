package net.minecraft.world.border;

public abstract interface IBorderListener
{
  public abstract void onSizeChanged(WorldBorder paramWorldBorder, double paramDouble);
  
  public abstract void func_177692_a(WorldBorder paramWorldBorder, double paramDouble1, double paramDouble2, long paramLong);
  
  public abstract void onCenterChanged(WorldBorder paramWorldBorder, double paramDouble1, double paramDouble2);
  
  public abstract void onWarningTimeChanged(WorldBorder paramWorldBorder, int paramInt);
  
  public abstract void onWarningDistanceChanged(WorldBorder paramWorldBorder, int paramInt);
  
  public abstract void func_177696_b(WorldBorder paramWorldBorder, double paramDouble);
  
  public abstract void func_177695_c(WorldBorder paramWorldBorder, double paramDouble);
}
