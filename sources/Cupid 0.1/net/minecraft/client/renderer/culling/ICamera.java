package net.minecraft.client.renderer.culling;

import net.minecraft.util.AxisAlignedBB;

public interface ICamera {
  boolean isBoundingBoxInFrustum(AxisAlignedBB paramAxisAlignedBB);
  
  void setPosition(double paramDouble1, double paramDouble2, double paramDouble3);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\culling\ICamera.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */