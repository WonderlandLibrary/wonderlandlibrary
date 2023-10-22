package cn.hanabi.utils.rotation;

import cn.hanabi.events.EventPreMotion;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Rotation {
   private static final Minecraft mc = Minecraft.getMinecraft();
   private float yaw;
   private float pitch;

   public Rotation(float yaw, float pitch) {
      this.yaw = yaw;
      this.pitch = pitch;
   }

   public Rotation(EntityLivingBase entityLivingBase) {
      this.yaw = entityLivingBase.rotationYaw;
      this.pitch = entityLivingBase.rotationPitch;
   }

   public Rotation(C03PacketPlayer packetPlayer) {
      this.yaw = packetPlayer.getYaw();
      this.pitch = packetPlayer.getPitch();
   }

   public Rotation(S08PacketPlayerPosLook posLook) {
      this.yaw = posLook.getYaw();
      this.pitch = posLook.getPitch();
   }

   public void add(float yaw, float pitch) {
      this.setYaw(this.getYaw() + yaw);
      this.setPitch(Math.min(Math.max(this.getPitch() + pitch, -90.0F), 90.0F));
   }

   public void remove(float yaw, float pitch) {
      this.setYaw(this.getYaw() - yaw);
      this.setPitch(this.getPitch() - pitch);
   }

   public void apply() {
      mc.thePlayer.rotationYaw = this.getYaw();
      mc.thePlayer.rotationPitch = this.getPitch();
   }

   public void apply(EventPreMotion event) {
      event.setYaw(this.getYaw());
      event.setPitch(this.getPitch());
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }
}
