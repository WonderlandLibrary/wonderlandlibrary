package org.alphacentauri.modules;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventAttack;
import org.alphacentauri.management.events.EventIsBot;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventSetback;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;

public class ModuleAntiBots extends Module implements EventListener {
   private Property semi_invisible = new Property(this, "SemiInvisible", Integer.valueOf(20));
   private Property min_dist = new Property(this, "MinDist", Float.valueOf(10.0F));
   private Property no_armor = new Property(this, "NoArmor", Boolean.valueOf(false));
   private Property tablist = new Property(this, "Tablist", Boolean.valueOf(false));
   private Property only_players = new Property(this, "OnlyPlayers", Boolean.valueOf(true));
   private Property teleportCooldown = new Property(this, "TeleportCooldown", Integer.valueOf(10));
   private Property needSwing = new Property(this, "Swing", Boolean.valueOf(false));
   private Property entityID = new Property(this, "EntityID", Integer.valueOf(1000000000));
   private boolean isCalcing = false;
   private int setbackTimer = 128;

   public ModuleAntiBots() {
      super("AntiBots", "Bot\'s? No Problem!", new String[]{"antibots", "antinpcs"}, Module.Category.Misc, 12726638);
   }

   public ArrayList autocomplete(Command cmd) {
      String[] args = cmd.getArgs();
      ArrayList<String> ret = new ArrayList();
      if(args.length == 1 && "reset".startsWith(args[0])) {
         ret.add("reset");
      }

      ret.addAll(super.autocomplete(cmd));
      return ret;
   }

   public boolean onCommand(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 1 && args[0].equalsIgnoreCase("reset")) {
         for(EntityLivingBase livingEntity : (ArrayList)AC.getMC().getWorld().loadedEntityList.stream().filter((entity) -> {
            return entity instanceof EntityLivingBase;
         }).map((entity) -> {
            return (EntityLivingBase)entity;
         }).collect(Collectors.toCollection(ArrayList::<init>))) {
            livingEntity.maxDist = 0.0D;
            livingEntity.hasSwung = false;
         }

         AC.addChat(this.getName(), "Resetted AntiBots!");
         return true;
      } else {
         return super.onCommand(cmd);
      }
   }

   private boolean isInTablist(EntityPlayer player) {
      if(AC.getMC().isSingleplayer()) {
         return false;
      } else {
         for(NetworkPlayerInfo playerInfo : AC.getMC().getNetHandler().getPlayerInfoMap()) {
            if(playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName())) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean hasArmor(EntityPlayer player) {
      return player.getTotalArmorValue() > 0;
   }

   private void calcDists() {
      if(!this.isCalcing) {
         this.isCalcing = true;
         AC.getThreadPool().execute(() -> {
            try {
               ArrayList<Entity> entities = new ArrayList();
               entities.addAll(AC.getMC().getWorld().loadedEntityList);
               ArrayList<EntityLivingBase> livingEntities = (ArrayList)entities.stream().filter((entity) -> {
                  return entity instanceof EntityLivingBase;
               }).map((entity) -> {
                  return (EntityLivingBase)entity;
               }).collect(Collectors.toCollection(ArrayList::<init>));
               EntityPlayerSP player = AC.getMC().getPlayer();

               for(EntityLivingBase livingEntity : livingEntities) {
                  if(livingEntity.maxDist <= 524300.0D) {
                     double dist = livingEntity.getDistanceSqToEntity(player);
                     if(livingEntity.maxDist < dist) {
                        livingEntity.maxDist = dist;
                     }
                  }
               }
            } catch (Exception var8) {
               if(AC.isDebug()) {
                  var8.printStackTrace();
               }
            }

            this.isCalcing = false;
         });
      }
   }

   public void onEvent(Event event) {
      if(event instanceof EventIsBot) {
         EntityLivingBase entity = ((EventIsBot)event).getEntity();
         if(((Boolean)this.only_players.value).booleanValue() && !(entity instanceof EntityPlayer)) {
            return;
         }

         if(((Boolean)this.needSwing.value).booleanValue() && !entity.hasSwung) {
            ((EventIsBot)event).setIsBot(true);
         }

         if(entity.getEntityId() > ((Integer)this.entityID.value).intValue()) {
            ((EventIsBot)event).setIsBot(true);
            return;
         }

         if((float)entity.ticksExisted <= (float)((Integer)this.semi_invisible.value).intValue() * AC.getMC().timer.timerSpeed) {
            ((EventIsBot)event).setIsBot(true);
            return;
         }

         if(entity.maxDist < (double)(((Float)this.min_dist.value).floatValue() * ((Float)this.min_dist.value).floatValue())) {
            ((EventIsBot)event).setIsBot(true);
            return;
         }

         if(entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            if(((Boolean)this.tablist.value).booleanValue() && !this.isInTablist(player)) {
               ((EventIsBot)event).setIsBot(true);
               return;
            }

            if(((Boolean)this.no_armor.value).booleanValue() && !this.hasArmor(player)) {
               ((EventIsBot)event).setIsBot(true);
            }
         }
      } else if(event instanceof EventTick) {
         if(this.setbackTimer > ((Integer)this.teleportCooldown.value).intValue()) {
            this.calcDists();
         }

         ++this.setbackTimer;
      } else if(event instanceof EventAttack) {
         if(((EventAttack)event).isManual() && ((EventAttack)event).getEntity() instanceof EntityLivingBase) {
            ((EntityLivingBase)((EventAttack)event).getEntity()).maxDist = Double.MAX_VALUE;
            ((EntityLivingBase)((EventAttack)event).getEntity()).hasSwung = true;
            if(((EventAttack)event).getEntity().ticksExisted < ((Integer)this.semi_invisible.value).intValue()) {
               ((EventAttack)event).getEntity().ticksExisted = ((Integer)this.semi_invisible.value).intValue() + 1;
            }
         }
      } else if(event instanceof EventSetback) {
         this.setbackTimer = 0;
      }

   }
}
