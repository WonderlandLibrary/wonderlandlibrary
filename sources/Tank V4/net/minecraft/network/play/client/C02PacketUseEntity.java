package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class C02PacketUseEntity implements Packet {
   private int entityId;
   private Vec3 hitVec;
   private C02PacketUseEntity.Action action;

   public void processPacket(INetHandlerPlayServer var1) {
      var1.processUseEntity(this);
   }

   public C02PacketUseEntity(Entity var1, Vec3 var2) {
      this(var1, C02PacketUseEntity.Action.INTERACT_AT);
      this.hitVec = var2;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayServer)var1);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = var1.readVarIntFromBuffer();
      this.action = (C02PacketUseEntity.Action)var1.readEnumValue(C02PacketUseEntity.Action.class);
      if (this.action == C02PacketUseEntity.Action.INTERACT_AT) {
         this.hitVec = new Vec3((double)var1.readFloat(), (double)var1.readFloat(), (double)var1.readFloat());
      }

   }

   public Vec3 getHitVec() {
      return this.hitVec;
   }

   public Entity getEntityFromWorld(World var1) {
      return var1.getEntityByID(this.entityId);
   }

   public C02PacketUseEntity(Entity var1, C02PacketUseEntity.Action var2) {
      this.entityId = var1.getEntityId();
      this.action = var2;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.entityId);
      var1.writeEnumValue(this.action);
      if (this.action == C02PacketUseEntity.Action.INTERACT_AT) {
         var1.writeFloat((float)this.hitVec.xCoord);
         var1.writeFloat((float)this.hitVec.yCoord);
         var1.writeFloat((float)this.hitVec.zCoord);
      }

   }

   public C02PacketUseEntity() {
   }

   public C02PacketUseEntity.Action getAction() {
      return this.action;
   }

   public static enum Action {
      INTERACT,
      INTERACT_AT,
      ATTACK;

      private static final C02PacketUseEntity.Action[] ENUM$VALUES = new C02PacketUseEntity.Action[]{INTERACT, ATTACK, INTERACT_AT};
   }
}
