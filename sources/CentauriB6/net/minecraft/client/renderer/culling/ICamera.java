package net.minecraft.client.renderer.culling;

import net.minecraft.util.AxisAlignedBB;

public interface ICamera {
   void setPosition(double var1, double var3, double var5);

   boolean isBoundingBoxInFrustum(AxisAlignedBB var1);
}
