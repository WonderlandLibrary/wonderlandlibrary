package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IS12PacketEntityVelocity;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({S12PacketEntityVelocity.class})
public class MixinS12PacketEntityVelocity implements IS12PacketEntityVelocity {
   @Shadow
   private int motionX;
   @Shadow
   private int motionY;
   @Shadow
   private int motionZ;

   public void setX(int f) {
      this.motionX = f;
   }

   public void setY(int f) {
      this.motionY = f;
   }

   public void setZ(int f) {
      this.motionZ = f;
   }
}
