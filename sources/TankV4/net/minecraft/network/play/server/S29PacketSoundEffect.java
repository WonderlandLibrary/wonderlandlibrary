package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.Validate;

public class S29PacketSoundEffect implements Packet {
   private int posZ;
   private int soundPitch;
   private int posY = Integer.MAX_VALUE;
   private String soundName;
   private float soundVolume;
   private int posX;

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.soundName = var1.readStringFromBuffer(256);
      this.posX = var1.readInt();
      this.posY = var1.readInt();
      this.posZ = var1.readInt();
      this.soundVolume = var1.readFloat();
      this.soundPitch = var1.readUnsignedByte();
   }

   public S29PacketSoundEffect(String var1, double var2, double var4, double var6, float var8, float var9) {
      Validate.notNull(var1, "name");
      this.soundName = var1;
      this.posX = (int)(var2 * 8.0D);
      this.posY = (int)(var4 * 8.0D);
      this.posZ = (int)(var6 * 8.0D);
      this.soundVolume = var8;
      this.soundPitch = (int)(var9 * 63.0F);
      var9 = MathHelper.clamp_float(var9, 0.0F, 255.0F);
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public double getX() {
      return (double)((float)this.posX / 8.0F);
   }

   public double getY() {
      return (double)((float)this.posY / 8.0F);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeString(this.soundName);
      var1.writeInt(this.posX);
      var1.writeInt(this.posY);
      var1.writeInt(this.posZ);
      var1.writeFloat(this.soundVolume);
      var1.writeByte(this.soundPitch);
   }

   public String getSoundName() {
      return this.soundName;
   }

   public double getZ() {
      return (double)((float)this.posZ / 8.0F);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleSoundEffect(this);
   }

   public float getVolume() {
      return this.soundVolume;
   }

   public S29PacketSoundEffect() {
   }

   public float getPitch() {
      return (float)this.soundPitch / 63.0F;
   }
}
