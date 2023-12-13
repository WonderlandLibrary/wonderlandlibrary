package cn.hanabi.modules.modules.world;

import cn.hanabi.Wrapper;
import cn.hanabi.events.EventPacket;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class Spoofer extends Mod {
   public static Value modes = (new Value("Spoofer", "Mode", 0)).LoadValue(new String[]{"Forge", "Lunar", "LabyMod", "PVP-L", "C-B", "Geyser"});

   public Spoofer() {
      super("Spoofer", Category.WORLD);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onPacket(EventPacket event) {
      if (event.getPacket() instanceof C17PacketCustomPayload) {
         if (((C17PacketCustomPayload)event.getPacket()).getChannelName().equalsIgnoreCase("MC|Brand")) {
            if (modes.isCurrentMode("Forge")) {
               Wrapper.sendPacketNoEvent(new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString("FML")));
            } else if (modes.isCurrentMode("Lunar")) {
               Wrapper.sendPacketNoEvent(new C17PacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer())).writeString("Lunar-Client")));
            } else if (modes.isCurrentMode("LabyMod")) {
               Wrapper.sendPacketNoEvent(new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString("LMC")));
            } else if (modes.isCurrentMode("PVP-L")) {
               Wrapper.sendPacketNoEvent(new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString("PLC18")));
            } else if (modes.isCurrentMode("C-B")) {
               Wrapper.sendPacketNoEvent(new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString("CB")));
            } else if (modes.isCurrentMode("Geyser")) {
               Wrapper.sendPacketNoEvent(new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString("Geyser")));
            }
         }

         event.setCancelled(true);
      }

   }
}
