package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IS27PacketExplosion;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({S27PacketExplosion.class})
public class MixinS27PacketExplosion implements IS27PacketExplosion {
   @Shadow
   private float field_149152_f;
   @Shadow
   private float field_149153_g;
   @Shadow
   private float field_149159_h;

   public void setX(float f) {
      this.field_149152_f = f;
   }

   public void setY(float f) {
      this.field_149153_g = f;
   }

   public void setZ(float f) {
      this.field_149159_h = f;
   }
}
