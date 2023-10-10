package net.minecraft.client.renderer;

import net.minecraft.client.renderer.texture.Stitcher;

public class StitcherException extends RuntimeException {
   private final Stitcher.Holder holder;

   public StitcherException(Stitcher.Holder var1, String var2) {
      super(var2);
      this.holder = var1;
   }
}
