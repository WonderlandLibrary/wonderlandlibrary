package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventPacketSend;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.MoveUtils;

public class ModuleFreecam extends Module implements EventListener {
   private Property ySpeed = new Property(this, "YSpeed", Float.valueOf(0.3F));
   private Property fwdSpeed = new Property(this, "XZSpeed", Float.valueOf(0.5F));
   private EntityOtherPlayerMP fakePlayer = null;

   public ModuleFreecam() {
      super("Freecam", "Go outside of your body", new String[]{"freecam"}, Module.Category.Movement, 6478924);
   }

   public void setEnabledSilent(boolean enabled) {
      EntityPlayerSP player = AC.getMC().getPlayer();
      if(enabled) {
         this.fakePlayer = new EntityOtherPlayerMP(AC.getMC().getWorld(), player.getGameProfile());
         this.fakePlayer.clonePlayer(player, true);
         this.fakePlayer.copyLocationAndAnglesFrom(player);
         this.fakePlayer.motionX = player.motionX;
         this.fakePlayer.motionY = player.motionY;
         this.fakePlayer.motionZ = player.motionZ;
         this.fakePlayer.rotationYawHead = player.rotationYawHead;
         AC.getMC().getWorld().addEntityToWorld(-1044170, this.fakePlayer);
         player.noClip = true;
      } else if(this.fakePlayer != null) {
         player.setPositionAndRotation(this.fakePlayer.posX, this.fakePlayer.posY, this.fakePlayer.posZ, player.rotationYaw, player.rotationPitch);
         AC.getMC().getWorld().removeEntityFromWorld(-1044170);
         player.noClip = false;
         player.motionX = this.fakePlayer.motionX;
         player.motionY = this.fakePlayer.motionY;
         player.motionZ = this.fakePlayer.motionZ;
         this.fakePlayer = null;
      }

      super.setEnabledSilent(enabled);
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         AC.getMC().getPlayer().noClip = true;
         GameSettings gameSettings = AC.getMC().gameSettings;
         float y = 0.0F;
         y = y + (gameSettings.keyBindJump.isKeyDown()?((Float)this.ySpeed.value).floatValue():0.0F);
         y = y - (gameSettings.keyBindSneak.isKeyDown()?((Float)this.ySpeed.value).floatValue():0.0F);
         AC.getMC().getPlayer().motionY = (double)y;
         MoveUtils.setSpeed((double)((Float)this.fwdSpeed.value).floatValue());
      } else if(event instanceof EventPacketSend && ((EventPacketSend)event).getPacket() instanceof C03PacketPlayer) {
         ((EventPacketSend)event).cancel();
      }

   }
}
