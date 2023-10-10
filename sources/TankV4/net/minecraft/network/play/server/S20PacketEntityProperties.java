package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S20PacketEntityProperties implements Packet {
   private int entityId;
   private final List field_149444_b = Lists.newArrayList();

   public List func_149441_d() {
      return this.field_149444_b;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.entityId);
      var1.writeInt(this.field_149444_b.size());
      Iterator var3 = this.field_149444_b.iterator();

      while(var3.hasNext()) {
         S20PacketEntityProperties.Snapshot var2 = (S20PacketEntityProperties.Snapshot)var3.next();
         var1.writeString(var2.func_151409_a());
         var1.writeDouble(var2.func_151410_b());
         var1.writeVarIntToBuffer(var2.func_151408_c().size());
         Iterator var5 = var2.func_151408_c().iterator();

         while(var5.hasNext()) {
            AttributeModifier var4 = (AttributeModifier)var5.next();
            var1.writeUuid(var4.getID());
            var1.writeDouble(var4.getAmount());
            var1.writeByte(var4.getOperation());
         }
      }

   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleEntityProperties(this);
   }

   public S20PacketEntityProperties(int var1, Collection var2) {
      this.entityId = var1;
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         IAttributeInstance var3 = (IAttributeInstance)var4.next();
         this.field_149444_b.add(new S20PacketEntityProperties.Snapshot(this, var3.getAttribute().getAttributeUnlocalizedName(), var3.getBaseValue(), var3.func_111122_c()));
      }

   }

   public int getEntityId() {
      return this.entityId;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = var1.readVarIntFromBuffer();
      int var2 = var1.readInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1.readStringFromBuffer(64);
         double var5 = var1.readDouble();
         ArrayList var7 = Lists.newArrayList();
         int var8 = var1.readVarIntFromBuffer();

         for(int var9 = 0; var9 < var8; ++var9) {
            UUID var10 = var1.readUuid();
            var7.add(new AttributeModifier(var10, "Unknown synced attribute modifier", var1.readDouble(), var1.readByte()));
         }

         this.field_149444_b.add(new S20PacketEntityProperties.Snapshot(this, var4, var5, var7));
      }

   }

   public S20PacketEntityProperties() {
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public class Snapshot {
      final S20PacketEntityProperties this$0;
      private final double field_151413_c;
      private final String field_151412_b;
      private final Collection field_151411_d;

      public double func_151410_b() {
         return this.field_151413_c;
      }

      public Collection func_151408_c() {
         return this.field_151411_d;
      }

      public String func_151409_a() {
         return this.field_151412_b;
      }

      public Snapshot(S20PacketEntityProperties var1, String var2, double var3, Collection var5) {
         this.this$0 = var1;
         this.field_151412_b = var2;
         this.field_151413_c = var3;
         this.field_151411_d = var5;
      }
   }
}
