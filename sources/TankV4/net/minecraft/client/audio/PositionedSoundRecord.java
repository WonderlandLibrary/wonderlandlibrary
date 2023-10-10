package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public class PositionedSoundRecord extends PositionedSound {
   public static PositionedSoundRecord create(ResourceLocation var0, float var1) {
      return new PositionedSoundRecord(var0, 0.25F, var1, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
   }

   public PositionedSoundRecord(ResourceLocation var1, float var2, float var3, float var4, float var5, float var6) {
      this(var1, var2, var3, false, 0, ISound.AttenuationType.LINEAR, var4, var5, var6);
   }

   private PositionedSoundRecord(ResourceLocation var1, float var2, float var3, boolean var4, int var5, ISound.AttenuationType var6, float var7, float var8, float var9) {
      super(var1);
      this.volume = var2;
      this.pitch = var3;
      this.xPosF = var7;
      this.yPosF = var8;
      this.zPosF = var9;
      this.repeat = var4;
      this.repeatDelay = var5;
      this.attenuationType = var6;
   }

   public static PositionedSoundRecord create(ResourceLocation var0) {
      return new PositionedSoundRecord(var0, 1.0F, 1.0F, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
   }

   public static PositionedSoundRecord create(ResourceLocation var0, float var1, float var2, float var3) {
      return new PositionedSoundRecord(var0, 4.0F, 1.0F, false, 0, ISound.AttenuationType.LINEAR, var1, var2, var3);
   }
}
