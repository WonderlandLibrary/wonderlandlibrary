package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventAttack;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventPacketSend;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;

public class ModuleCriticals extends Module implements EventListener {
   private Property mode = new Property(this, "Mode", ModuleCriticals.Mode.Packets);
   private Property miniJumpHeight = new Property(this, "MiniJumpHeight", Float.valueOf(0.2F));
   private Property fakeCritParticles = new Property(this, "FakeCritParticles", Boolean.valueOf(true));
   private boolean crit;
   private boolean next;

   public ModuleCriticals() {
      super("Criticals", "Do more damage", new String[]{"criticals", "crits"}, Module.Category.Combat, 15718497);
   }

   public void onEvent(Event event) {
      if(event instanceof EventAttack) {
         if(((EventAttack)event).isCancelled()) {
            return;
         }

         EntityPlayerSP player = AC.getMC().getPlayer();
         if(player.isInLiquid() || player.isOnLadder()) {
            return;
         }

         if(this.mode.value == ModuleCriticals.Mode.Jump) {
            if(player.onGround) {
               player.jump();
            }
         } else if(this.mode.value == ModuleCriticals.Mode.MiniJump) {
            if(player.onGround) {
               player.motionY = (double)((Float)this.miniJumpHeight.value).floatValue();
               if(((Boolean)this.fakeCritParticles.value).booleanValue()) {
                  player.fallDistance = 0.1F;
               }
            }
         } else if(this.mode.value == ModuleCriticals.Mode.Packets) {
            if(player.onGround) {
               if(((EventAttack)event).getEntity().hurtResistantTime < 4 && (this.next = !this.next)) {
                  this.crit = true;
                  double posX = AC.getMC().thePlayer.posX;
                  double posY = AC.getMC().thePlayer.posY;
                  double posZ = AC.getMC().thePlayer.posZ;
                  NetHandlerPlayClient sendQueue = AC.getMC().thePlayer.sendQueue;
                  sendQueue.addToSendQueue(new C04PacketPlayerPosition(posX, posY + 0.0625D, posZ, false));
                  sendQueue.addToSendQueue(new C04PacketPlayerPosition(posX, posY, posZ, false));
                  sendQueue.addToSendQueue(new C04PacketPlayerPosition(posX, posY + 0.001D, posZ, false));
                  sendQueue.addToSendQueue(new C04PacketPlayerPosition(posX, posY, posZ, false));
                  this.crit = false;
               }

               if(((Boolean)this.fakeCritParticles.value).booleanValue()) {
                  player.fallDistance = 0.1F;
               }
            }
         } else if(this.mode.value == ModuleCriticals.Mode.Always && player.onGround && ((EventAttack)event).getEntity().hurtResistantTime < 4 && (this.next = !this.next)) {
            player.jump();

            while(player.fallDistance <= 0.0F) {
               player.onUpdate();
            }
         }

         if(((Boolean)this.fakeCritParticles.value).booleanValue()) {
            AC.getMC().getPlayer().onCriticalHit(((EventAttack)event).getEntity());
         }
      } else if(event instanceof EventPacketSend) {
         if(this.mode.value == ModuleCriticals.Mode.Packets && this.crit && ((EventPacketSend)event).getPacket() instanceof C03PacketPlayer) {
            ((C03PacketPlayer)((EventPacketSend)event).getPacket()).onGround = false;
         }
      } else if(event instanceof EventTick) {
         this.setTag(((ModuleCriticals.Mode)this.mode.value).name());
      }

   }

   public static enum Mode {
      Jump,
      MiniJump,
      Packets,
      Always;
   }
}
