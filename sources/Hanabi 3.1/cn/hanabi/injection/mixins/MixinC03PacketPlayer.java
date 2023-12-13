package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IC03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({C03PacketPlayer.class})
public class MixinC03PacketPlayer implements IC03PacketPlayer {
   @Shadow
   protected boolean onGround;
   @Shadow
   protected double x;
   @Shadow
   protected double y;
   @Shadow
   protected double z;
   @Shadow
   protected float yaw;
   @Shadow
   protected float pitch;
   @Shadow
   protected boolean rotating;
   @Shadow
   protected boolean moving;

   public boolean ismoving() {
      return this.moving;
   }

   public void setmoving(boolean b) {
      this.moving = b;
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public void setOnGround(boolean b) {
      this.onGround = b;
   }

   public double getPosX() {
      return this.x;
   }

   public void setPosX(double x) {
      this.x = x;
   }

   public double getPosY() {
      return this.y;
   }

   public void setPosY(double y) {
      this.y = y;
   }

   public double getPosZ() {
      return this.z;
   }

   public void setPosZ(double z) {
      this.z = z;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float f) {
      this.yaw = f;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float f) {
      this.pitch = f;
   }

   public boolean getRotate() {
      return this.rotating;
   }

   public void setRotate(boolean b) {
      this.rotating = b;
   }
}
