package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;

public class S28PacketEffect implements Packet {
   private BlockPos soundPos;
   private int soundType;
   private boolean serverWide;
   private int soundData;

   public S28PacketEffect() {
   }

   public int getSoundData() {
      return this.soundData;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeInt(this.soundType);
      var1.writeBlockPos(this.soundPos);
      var1.writeInt(this.soundData);
      var1.writeBoolean(this.serverWide);
   }

   public boolean isSoundServerwide() {
      return this.serverWide;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleEffect(this);
   }

   public S28PacketEffect(int var1, BlockPos var2, int var3, boolean var4) {
      this.soundType = var1;
      this.soundPos = var2;
      this.soundData = var3;
      this.serverWide = var4;
   }

   public BlockPos getSoundPos() {
      return this.soundPos;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.soundType = var1.readInt();
      this.soundPos = var1.readBlockPos();
      this.soundData = var1.readInt();
      this.serverWide = var1.readBoolean();
   }

   public int getSoundType() {
      return this.soundType;
   }
}
