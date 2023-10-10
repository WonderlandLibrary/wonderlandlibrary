package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.PotionEffect;

public class S1DPacketEntityEffect implements Packet {
   private byte amplifier;
   private int entityId;
   private byte effectId;
   private byte hideParticles;
   private int duration;

   public S1DPacketEntityEffect() {
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.entityId);
      var1.writeByte(this.effectId);
      var1.writeByte(this.amplifier);
      var1.writeVarIntToBuffer(this.duration);
      var1.writeByte(this.hideParticles);
   }

   public byte getAmplifier() {
      return this.amplifier;
   }

   public boolean func_149429_c() {
      return this.duration == 32767;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleEntityEffect(this);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = var1.readVarIntFromBuffer();
      this.effectId = var1.readByte();
      this.amplifier = var1.readByte();
      this.duration = var1.readVarIntFromBuffer();
      this.hideParticles = var1.readByte();
   }

   public int getDuration() {
      return this.duration;
   }

   public byte getEffectId() {
      return this.effectId;
   }

   public boolean func_179707_f() {
      return this.hideParticles != 0;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public int getEntityId() {
      return this.entityId;
   }

   public S1DPacketEntityEffect(int var1, PotionEffect var2) {
      this.entityId = var1;
      this.effectId = (byte)(var2.getPotionID() & 255);
      this.amplifier = (byte)(var2.getAmplifier() & 255);
      if (var2.getDuration() > 32767) {
         this.duration = 32767;
      } else {
         this.duration = var2.getDuration();
      }

      this.hideParticles = (byte)(var2.getIsShowParticles() ? 1 : 0);
   }
}
