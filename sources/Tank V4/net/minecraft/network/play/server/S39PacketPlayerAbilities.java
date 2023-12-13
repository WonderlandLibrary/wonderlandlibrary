package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S39PacketPlayerAbilities implements Packet {
   private float flySpeed;
   private float walkSpeed;
   private boolean flying;
   private boolean allowFlying;
   private boolean invulnerable;
   private boolean creativeMode;

   public boolean isAllowFlying() {
      return this.allowFlying;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      byte var2 = var1.readByte();
      this.setInvulnerable((var2 & 1) > 0);
      this.setFlying((var2 & 2) > 0);
      this.setAllowFlying((var2 & 4) > 0);
      this.setCreativeMode((var2 & 8) > 0);
      this.setFlySpeed(var1.readFloat());
      this.setWalkSpeed(var1.readFloat());
   }

   public void setFlySpeed(float var1) {
      this.flySpeed = var1;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      byte var2 = 0;
      if (this.isInvulnerable()) {
         var2 = (byte)(var2 | 1);
      }

      if (this.isFlying()) {
         var2 = (byte)(var2 | 2);
      }

      if (this.isAllowFlying()) {
         var2 = (byte)(var2 | 4);
      }

      if (this.isCreativeMode()) {
         var2 = (byte)(var2 | 8);
      }

      var1.writeByte(var2);
      var1.writeFloat(this.flySpeed);
      var1.writeFloat(this.walkSpeed);
   }

   public void setFlying(boolean var1) {
      this.flying = var1;
   }

   public S39PacketPlayerAbilities() {
   }

   public void setWalkSpeed(float var1) {
      this.walkSpeed = var1;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public boolean isInvulnerable() {
      return this.invulnerable;
   }

   public void setInvulnerable(boolean var1) {
      this.invulnerable = var1;
   }

   public void setCreativeMode(boolean var1) {
      this.creativeMode = var1;
   }

   public float getFlySpeed() {
      return this.flySpeed;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handlePlayerAbilities(this);
   }

   public float getWalkSpeed() {
      return this.walkSpeed;
   }

   public boolean isCreativeMode() {
      return this.creativeMode;
   }

   public void setAllowFlying(boolean var1) {
      this.allowFlying = var1;
   }

   public boolean isFlying() {
      return this.flying;
   }

   public S39PacketPlayerAbilities(PlayerCapabilities var1) {
      this.setInvulnerable(var1.disableDamage);
      this.setFlying(var1.isFlying);
      this.setAllowFlying(var1.allowFlying);
      this.setCreativeMode(var1.isCreativeMode);
      this.setFlySpeed(var1.getFlySpeed());
      this.setWalkSpeed(var1.getWalkSpeed());
   }
}
