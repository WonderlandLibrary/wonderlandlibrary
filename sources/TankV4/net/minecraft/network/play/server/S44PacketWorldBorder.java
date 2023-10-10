package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.border.WorldBorder;

public class S44PacketWorldBorder implements Packet {
   private S44PacketWorldBorder.Action action;
   private int warningDistance;
   private double centerX;
   private double diameter;
   private double centerZ;
   private long timeUntilTarget;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$network$play$server$S44PacketWorldBorder$Action;
   private int warningTime;
   private double targetSize;
   private int size;

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleWorldBorder(this);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeEnumValue(this.action);
      switch($SWITCH_TABLE$net$minecraft$network$play$server$S44PacketWorldBorder$Action()[this.action.ordinal()]) {
      case 1:
         var1.writeDouble(this.targetSize);
         break;
      case 2:
         var1.writeDouble(this.diameter);
         var1.writeDouble(this.targetSize);
         var1.writeVarLong(this.timeUntilTarget);
         break;
      case 3:
         var1.writeDouble(this.centerX);
         var1.writeDouble(this.centerZ);
         break;
      case 4:
         var1.writeDouble(this.centerX);
         var1.writeDouble(this.centerZ);
         var1.writeDouble(this.diameter);
         var1.writeDouble(this.targetSize);
         var1.writeVarLong(this.timeUntilTarget);
         var1.writeVarIntToBuffer(this.size);
         var1.writeVarIntToBuffer(this.warningDistance);
         var1.writeVarIntToBuffer(this.warningTime);
         break;
      case 5:
         var1.writeVarIntToBuffer(this.warningTime);
         break;
      case 6:
         var1.writeVarIntToBuffer(this.warningDistance);
      }

   }

   public void func_179788_a(WorldBorder var1) {
      switch($SWITCH_TABLE$net$minecraft$network$play$server$S44PacketWorldBorder$Action()[this.action.ordinal()]) {
      case 1:
         var1.setTransition(this.targetSize);
         break;
      case 2:
         var1.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
         break;
      case 3:
         var1.setCenter(this.centerX, this.centerZ);
         break;
      case 4:
         var1.setCenter(this.centerX, this.centerZ);
         if (this.timeUntilTarget > 0L) {
            var1.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
         } else {
            var1.setTransition(this.targetSize);
         }

         var1.setSize(this.size);
         var1.setWarningDistance(this.warningDistance);
         var1.setWarningTime(this.warningTime);
         break;
      case 5:
         var1.setWarningTime(this.warningTime);
         break;
      case 6:
         var1.setWarningDistance(this.warningDistance);
      }

   }

   static int[] $SWITCH_TABLE$net$minecraft$network$play$server$S44PacketWorldBorder$Action() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$network$play$server$S44PacketWorldBorder$Action;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[S44PacketWorldBorder.Action.values().length];

         try {
            var0[S44PacketWorldBorder.Action.INITIALIZE.ordinal()] = 4;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[S44PacketWorldBorder.Action.LERP_SIZE.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[S44PacketWorldBorder.Action.SET_CENTER.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[S44PacketWorldBorder.Action.SET_SIZE.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[S44PacketWorldBorder.Action.SET_WARNING_BLOCKS.ordinal()] = 6;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[S44PacketWorldBorder.Action.SET_WARNING_TIME.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$network$play$server$S44PacketWorldBorder$Action = var0;
         return var0;
      }
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.action = (S44PacketWorldBorder.Action)var1.readEnumValue(S44PacketWorldBorder.Action.class);
      switch($SWITCH_TABLE$net$minecraft$network$play$server$S44PacketWorldBorder$Action()[this.action.ordinal()]) {
      case 1:
         this.targetSize = var1.readDouble();
         break;
      case 2:
         this.diameter = var1.readDouble();
         this.targetSize = var1.readDouble();
         this.timeUntilTarget = var1.readVarLong();
         break;
      case 3:
         this.centerX = var1.readDouble();
         this.centerZ = var1.readDouble();
         break;
      case 4:
         this.centerX = var1.readDouble();
         this.centerZ = var1.readDouble();
         this.diameter = var1.readDouble();
         this.targetSize = var1.readDouble();
         this.timeUntilTarget = var1.readVarLong();
         this.size = var1.readVarIntFromBuffer();
         this.warningDistance = var1.readVarIntFromBuffer();
         this.warningTime = var1.readVarIntFromBuffer();
         break;
      case 5:
         this.warningTime = var1.readVarIntFromBuffer();
         break;
      case 6:
         this.warningDistance = var1.readVarIntFromBuffer();
      }

   }

   public S44PacketWorldBorder() {
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public S44PacketWorldBorder(WorldBorder var1, S44PacketWorldBorder.Action var2) {
      this.action = var2;
      this.centerX = var1.getCenterX();
      this.centerZ = var1.getCenterZ();
      this.diameter = var1.getDiameter();
      this.targetSize = var1.getTargetSize();
      this.timeUntilTarget = var1.getTimeUntilTarget();
      this.size = var1.getSize();
      this.warningDistance = var1.getWarningDistance();
      this.warningTime = var1.getWarningTime();
   }

   public static enum Action {
      INITIALIZE,
      SET_SIZE,
      SET_CENTER;

      private static final S44PacketWorldBorder.Action[] ENUM$VALUES = new S44PacketWorldBorder.Action[]{SET_SIZE, LERP_SIZE, SET_CENTER, INITIALIZE, SET_WARNING_TIME, SET_WARNING_BLOCKS};
      SET_WARNING_BLOCKS,
      SET_WARNING_TIME,
      LERP_SIZE;
   }
}
