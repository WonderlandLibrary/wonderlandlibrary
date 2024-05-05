package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;

public abstract interface IImageBuffer
{
  public abstract BufferedImage parseUserSkin(BufferedImage paramBufferedImage);
  
  public abstract void func_152634_a();
}
