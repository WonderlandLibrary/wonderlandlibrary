package net.minecraft.client.renderer.culling;

import net.minecraft.util.AxisAlignedBB;

public abstract interface ICamera
{
  public abstract boolean isBoundingBoxInFrustum(AxisAlignedBB paramAxisAlignedBB);
  
  public abstract void setPosition(double paramDouble1, double paramDouble2, double paramDouble3);
}
