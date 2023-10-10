package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.potion.Potion;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventPacketSend;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;

public class ModuleSprint extends Module implements EventListener {
   public Property hunger = new Property(this, "Hunger", Boolean.valueOf(false));
   Property fake = new Property(this, "Fake", Boolean.valueOf(false));
   Property legit = new Property(this, "Legit", Boolean.valueOf(false));
   public Property multi_dir = new Property(this, "MultiDirection", Boolean.valueOf(true));
   public Property multi_jump = new Property(this, "MutliDirectionJump", Boolean.valueOf(true));

   public ModuleSprint() {
      super("Sprint", "Makes you always sprint", new String[]{"sprint"}, Module.Category.Movement, 7640851);
   }

   public void setBypass(AntiCheat ac) {
      super.setBypass(ac);
      if(ac != AntiCheat.NCP && ac != AntiCheat.Reflex) {
         if(ac == AntiCheat.AAC) {
            this.hunger.value = Boolean.valueOf(false);
            this.fake.value = Boolean.valueOf(false);
         } else if(ac == AntiCheat.Spartan) {
            this.hunger.value = Boolean.valueOf(false);
            this.fake.value = Boolean.valueOf(true);
         }
      } else {
         this.hunger.value = Boolean.valueOf(true);
         this.fake.value = Boolean.valueOf(false);
      }

   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         if(player.isCollidedHorizontally || !player.hasMovementInput() || ((Boolean)this.legit.value).booleanValue() && (player.isUsingItem() || player.isPotionActive(Potion.blindness)) || ((Boolean)this.hunger.value).booleanValue() && player.getFoodStats().getFoodLevel() <= 6 || !((Boolean)this.multi_dir.value).booleanValue() && player.moveForward <= 0.0F) {
            player.setSprinting(false);
         } else {
            player.setSprinting(true);
            player.sprintingTicksLeft = 5;
         }
      } else if(event instanceof EventPacketSend && ((Boolean)this.fake.value).booleanValue()) {
         Packet packet = ((EventPacketSend)event).getPacket();
         if(packet instanceof C0BPacketEntityAction && ((C0BPacketEntityAction)packet).getAction() == Action.START_SPRINTING) {
            ((EventPacketSend)event).cancel();
         }
      }

   }
}
