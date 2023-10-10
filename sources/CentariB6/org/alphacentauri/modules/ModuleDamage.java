package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.MathHelper;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventPacketSend;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;

public class ModuleDamage extends Module implements EventListener {
   private Property amount = new Property(this, "Amount", Double.valueOf(1.0D));
   private int jmp = 0;

   public ModuleDamage() {
      super("Damage", "Self-Damage", new String[]{"damage", "dmg"}, Module.Category.Misc, 7808080);
   }

   public void setEnabledSilent(boolean enabled) {
      super.setEnabledSilent(enabled);
      if(enabled) {
         if(this.getBypass() != AntiCheat.AAC) {
            this.dmg(((Double)this.amount.value).doubleValue());
            this.setEnabledSilent(false);
         } else {
            this.jmp = 0;
         }
      }

   }

   public void dmg(double damage) {
      if(AC.getMC().getPlayer() != null) {
         if(this.getBypass() == AntiCheat.Spartan) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(player.isCreative()) {
               return;
            }

            if(damage < 1.0D) {
               damage = 1.0D;
            }

            if(damage > (double)MathHelper.floor_double((double)(player.getMaxHealth() + 2.0F))) {
               damage = (double)MathHelper.floor_double((double)(player.getMaxHealth() + 2.0F));
            }

            double offset = 0.55D;
            if(player.onGround) {
               for(int i = 0; (double)i <= (3.0D + damage) / offset; ++i) {
                  player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY + offset, player.posZ, false));
                  player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY, player.posZ, (double)i == (3.0D + damage) / offset));
               }
            }
         } else if(this.getBypass() == AntiCheat.NCP) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(player.isCreative()) {
               return;
            }

            if(damage < 1.0D) {
               damage = 1.0D;
            }

            if(damage > (double)MathHelper.floor_double((double)player.getMaxHealth())) {
               damage = (double)MathHelper.floor_double((double)player.getMaxHealth());
            }

            double offset = 0.0625D;
            if(player.onGround) {
               for(int i = 0; (double)i <= (3.0D + damage) / offset; ++i) {
                  player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY + offset, player.posZ, false));
                  player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY, player.posZ, (double)i == (3.0D + damage) / offset));
               }
            }
         } else if(this.getBypass() == AntiCheat.AAC) {
            AC.addChat(this.getName(), "Doesn\'t work lol");
         } else if(this.getBypass() == AntiCheat.Vanilla) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(player.isCreative()) {
               return;
            }

            if(damage < 1.0D) {
               damage = 1.0D;
            }

            if(damage > (double)MathHelper.floor_double((double)player.getMaxHealth())) {
               damage = (double)MathHelper.floor_double((double)player.getMaxHealth());
            }

            if(player.onGround) {
               for(int i = 0; (double)i < 3.0D + damage; ++i) {
                  player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY + 1.0D, player.posZ, false));
                  player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY + 0.0D, player.posZ, (double)i == 3.0D + damage));
               }
            }
         }

      }
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         if(this.getBypass() == AntiCheat.AAC) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(player.onGround) {
               player.motionY = 0.42D;
            } else if(player.fallDistance > 0.0F) {
               player.motionY = -2.0D;
               if((double)(++this.jmp) >= 3.0D + ((Double)this.amount.value).doubleValue()) {
                  this.setEnabledSilent(false);
               }
            }
         } else {
            this.setEnabledSilent(false);
         }
      } else if(event instanceof EventPacketSend && this.getBypass() == AntiCheat.AAC && ((EventPacketSend)event).getPacket() instanceof C03PacketPlayer) {
         ((C03PacketPlayer)((EventPacketSend)event).getPacket()).onGround = false;
      }

   }
}
