package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;

public interface IImageBuffer {
   void skinAvailable();

   BufferedImage parseUserSkin(BufferedImage var1);
}
