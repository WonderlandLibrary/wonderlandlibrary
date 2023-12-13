package org.alphacentauri.modules;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventPacketSend;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.modules.ModuleFly;

public class ModuleNoFall extends Module implements EventListener {
   public Property mode = new Property(this, "Mode", ModuleNoFall.Mode.Vanilla);
   private ModuleFly fly;

   public ModuleNoFall() {
      super("NoFall", "FallDamage? Bypassed.", new String[]{"nofall"}, Module.Category.Movement, 12032450);
   }

   public void setBypass(AntiCheat ac) {
      if(ac == AntiCheat.Vanilla) {
         this.mode.value = ModuleNoFall.Mode.Vanilla;
      } else if(ac == AntiCheat.Spartan || ac == AntiCheat.AAC) {
         this.mode.value = ModuleNoFall.Mode.GroundSpoofJump;
      }

   }

   public void onEvent(Event event) {
      if(this.fly == null) {
         this.fly = (ModuleFly)AC.getModuleManager().get(ModuleFly.class);
      }

      if(!this.fly.isEnabled() || this.fly.mode.value != ModuleFly.Mode.AAC1) {
         if(this.mode.value == ModuleNoFall.Mode.Vanilla) {
            if(event instanceof EventPacketSend && ((EventPacketSend)event).getPacket() instanceof C03PacketPlayer) {
               ((C03PacketPlayer)((EventPacketSend)event).getPacket()).onGround = true;
            }
         } else if(this.mode.value == ModuleNoFall.Mode.GroundSpoof) {
            if(event instanceof EventPacketSend && ((EventPacketSend)event).getPacket() instanceof C03PacketPlayer) {
               ((C03PacketPlayer)((EventPacketSend)event).getPacket()).onGround = false;
            }
         } else if(this.mode.value == ModuleNoFall.Mode.GroundSpoofJump) {
            if(event instanceof EventPacketSend) {
               if(((EventPacketSend)event).getPacket() instanceof C03PacketPlayer) {
                  ((C03PacketPlayer)((EventPacketSend)event).getPacket()).onGround = false;
               }
            } else if(event instanceof EventTick && AC.getMC().getPlayer().onGround) {
               AC.getMC().getPlayer().jump();
            }
         }

      }
   }

   public static enum Mode {
      Vanilla,
      GroundSpoof,
      GroundSpoofJump;
   }
}
