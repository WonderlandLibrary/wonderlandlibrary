package org.alphacentauri.modules;

import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventIsBot;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.RaycastUtils;
import org.alphacentauri.management.util.Timer;
import org.alphacentauri.modules.ModuleTeam;

public class ModuleTriggerBot extends Module implements EventListener {
   private Property minCPS = new Property(this, "MinimalCPS", Integer.valueOf(9));
   private Property maxCPS = new Property(this, "MaximalCPS", Integer.valueOf(12));
   private Property reach = new Property(this, "Reach", Float.valueOf(3.8F));
   private Timer timer = new Timer();

   public ModuleTriggerBot() {
      super("TriggerBot", "Clicks automatically at set speed when you can hit an entity", new String[]{"triggerbot"}, Module.Category.Combat, 2898216);
   }

   private boolean isFriendOrTeamOrBot(Entity entity) {
      if(!(entity instanceof EntityLivingBase)) {
         return true;
      } else if(((EventIsBot)(new EventIsBot((EntityLivingBase)entity)).fire()).isBot()) {
         return true;
      } else {
         if(entity instanceof EntityPlayer) {
            if(entity instanceof EntityPlayerSP) {
               return true;
            }

            if(ModuleTeam.isinTeam((EntityPlayer)entity)) {
               return true;
            }

            if(AC.getFriendManager().isFriend(entity.getName())) {
               return true;
            }
         }

         return false;
      }
   }

   public void setEnabledSilent(boolean enabled) {
      this.timer.reset();
      super.setEnabledSilent(enabled);
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         if(((Integer)this.minCPS.value).intValue() > ((Integer)this.maxCPS.value).intValue()) {
            this.minCPS.value = this.maxCPS.value;
         }

         int cps;
         if(Objects.equals(this.maxCPS.value, this.minCPS.value)) {
            cps = ((Integer)this.maxCPS.value).intValue();
         } else {
            cps = AC.getRandom().nextInt(((Integer)this.maxCPS.value).intValue() - ((Integer)this.minCPS.value).intValue()) + ((Integer)this.minCPS.value).intValue();
         }

         if(cps > 999) {
            cps = 999;
         }

         int ms = 1000 / cps;
         EntityPlayerSP player = AC.getMC().getPlayer();
         Vec3 lookVec = player.getLookVec().multiply((double)((Float)this.reach.value).floatValue());

         for(MovingObjectPosition rayCast = RaycastUtils.rayCast(player, player.posX + lookVec.xCoord, player.posY + (double)player.getEyeHeight() + lookVec.yCoord, player.posZ + lookVec.zCoord); this.timer.hasMSPassed((long)ms); this.timer.subtract(ms)) {
            Minecraft mc = AC.getMC();
            if(rayCast != null && rayCast.entityHit != null) {
               if(this.isFriendOrTeamOrBot(rayCast.entityHit)) {
                  return;
               }

               mc.getPlayer().sendQueue.addToSendQueue(new C02PacketUseEntity(rayCast.entityHit, Action.ATTACK));
               mc.getPlayer().swingItem();
            }
         }
      }

   }
}
