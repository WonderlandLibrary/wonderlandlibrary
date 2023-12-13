package cn.hanabi.injection.interfaces;

import net.minecraft.util.ResourceLocation;

public interface IEntityRenderer {
   void runSetupCameraTransform(float var1, int var2);

   void setupCameraTransform(float var1, int var2);

   void loadShader2(ResourceLocation var1);
}
