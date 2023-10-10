package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.CombatTracker;

public class S42PacketCombatEvent implements Packet {
   public S42PacketCombatEvent.Event eventType;
   public String deathMessage;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$network$play$server$S42PacketCombatEvent$Event;
   public int field_179775_c;
   public int field_179772_d;
   public int field_179774_b;

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeEnumValue(this.eventType);
      if (this.eventType == S42PacketCombatEvent.Event.END_COMBAT) {
         var1.writeVarIntToBuffer(this.field_179772_d);
         var1.writeInt(this.field_179775_c);
      } else if (this.eventType == S42PacketCombatEvent.Event.ENTITY_DIED) {
         var1.writeVarIntToBuffer(this.field_179774_b);
         var1.writeInt(this.field_179775_c);
         var1.writeString(this.deathMessage);
      }

   }

   public S42PacketCombatEvent() {
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleCombatEvent(this);
   }

   static int[] $SWITCH_TABLE$net$minecraft$network$play$server$S42PacketCombatEvent$Event() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$network$play$server$S42PacketCombatEvent$Event;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[S42PacketCombatEvent.Event.values().length];

         try {
            var0[S42PacketCombatEvent.Event.END_COMBAT.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[S42PacketCombatEvent.Event.ENTER_COMBAT.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[S42PacketCombatEvent.Event.ENTITY_DIED.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$network$play$server$S42PacketCombatEvent$Event = var0;
         return var0;
      }
   }

   public S42PacketCombatEvent(CombatTracker var1, S42PacketCombatEvent.Event var2) {
      this.eventType = var2;
      EntityLivingBase var3 = var1.func_94550_c();
      switch($SWITCH_TABLE$net$minecraft$network$play$server$S42PacketCombatEvent$Event()[var2.ordinal()]) {
      case 2:
         this.field_179772_d = var1.func_180134_f();
         this.field_179775_c = var3 == null ? -1 : var3.getEntityId();
         break;
      case 3:
         this.field_179774_b = var1.getFighter().getEntityId();
         this.field_179775_c = var3 == null ? -1 : var3.getEntityId();
         this.deathMessage = var1.getDeathMessage().getUnformattedText();
      }

   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.eventType = (S42PacketCombatEvent.Event)var1.readEnumValue(S42PacketCombatEvent.Event.class);
      if (this.eventType == S42PacketCombatEvent.Event.END_COMBAT) {
         this.field_179772_d = var1.readVarIntFromBuffer();
         this.field_179775_c = var1.readInt();
      } else if (this.eventType == S42PacketCombatEvent.Event.ENTITY_DIED) {
         this.field_179774_b = var1.readVarIntFromBuffer();
         this.field_179775_c = var1.readInt();
         this.deathMessage = var1.readStringFromBuffer(32767);
      }

   }

   public static enum Event {
      ENTER_COMBAT,
      ENTITY_DIED,
      END_COMBAT;

      private static final S42PacketCombatEvent.Event[] ENUM$VALUES = new S42PacketCombatEvent.Event[]{ENTER_COMBAT, END_COMBAT, ENTITY_DIED};
   }
}
