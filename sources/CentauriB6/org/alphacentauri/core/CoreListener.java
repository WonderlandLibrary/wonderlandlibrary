package org.alphacentauri.core;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import org.alphacentauri.AC;
import org.alphacentauri.bypass.ArenaCraft;
import org.alphacentauri.core.CloudAPI;
import org.alphacentauri.launcher.api.API;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventEntityHealthUpdate;
import org.alphacentauri.management.events.EventGlobalGameLoop;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventMiddleClick;
import org.alphacentauri.management.events.EventPacketRecv;
import org.alphacentauri.management.events.EventRender2D;
import org.alphacentauri.management.events.EventRenderString;
import org.alphacentauri.management.events.EventServerJoin;
import org.alphacentauri.management.events.EventServerLeave;
import org.alphacentauri.management.events.EventSetback;
import org.alphacentauri.management.events.EventSuccessfulServerJoin;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.events.EventWorldChanged;
import org.alphacentauri.management.exceptions.SkidException;
import org.alphacentauri.management.notifications.Notification;
import org.alphacentauri.management.sexyness.ingame.ArrayListRenderer;
import org.alphacentauri.management.sexyness.ingame.InfoRenderer;
import org.alphacentauri.management.sexyness.ingame.TabGuiRenderer;
import org.alphacentauri.management.sexyness.ingame.WatermarkRenderer;
import tk.alphacentauri.launcher.Main;

public class CoreListener implements EventListener {
   private HashMap lastHealth = new HashMap();
   private EventServerJoin lastServerJoin;
   public static String server;

   public ArrayList getPlayers() {
      try {
         ArrayList<String> ret = (ArrayList)AC.getFriendManager().getAll().stream().map(Entry::getKey).collect(Collectors.toCollection(ArrayList::<init>));
         ret.addAll(CloudAPI.instance.users.keySet());
         return ret;
      } catch (Exception var2) {
         return new ArrayList();
      }
   }

   public void onEvent(Event event) {
      if(event instanceof EventRender2D) {
         ScaledResolution resolution = ((EventRender2D)event).getResolution();
         WatermarkRenderer.render(resolution);
         ArrayListRenderer.render(resolution);
         if(AC.getConfig().isTabGuiEnabled()) {
            TabGuiRenderer.render(resolution);
         }

         InfoRenderer.render(resolution);
         InfoRenderer.renderInfo("§7XYZ: §r" + (int)AC.getMC().getPlayer().posX + " " + (int)AC.getMC().getPlayer().posY + " " + (int)AC.getMC().getPlayer().posZ);
         InfoRenderer.renderInfo("§7User: §r" + API.getUsername());
         InfoRenderer.renderInfo("§7FPS: §r" + Minecraft.getDebugFPS());
         if(AC.getMC().currentScreen == null) {
            AC.getGuiManager().renderPinned();
         }

         AC.getGuiManager().update();
      } else if(event instanceof EventServerJoin) {
         try {
            if(API.isServerBlacklisted(InetAddress.getByName(((EventServerJoin)event).getAddr()).getHostAddress() + ":" + ((EventServerJoin)event).getPort())) {
               ((EventServerJoin)event).cancel();
               return;
            }

            this.lastServerJoin = (EventServerJoin)event;
            AC.getPropertyManager().loadConfig(((EventServerJoin)event).getAddr() + "_" + ((EventServerJoin)event).getPort());
         } catch (Exception var5) {
            ;
         }
      } else if(event instanceof EventGlobalGameLoop) {
         if(!Main.b.isConnected()) {
            try {
               throw new SkidException("Connection lost!");
            } catch (Exception var6) {
               ;
            }
         }
      } else if(event instanceof EventPacketRecv) {
         Packet packet = ((EventPacketRecv)event).getPacket();
         if(packet instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook)packet;
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(player == null) {
               return;
            }

            (new EventSetback(player.posX, player.posY, player.posZ, s08.getX(), s08.getY(), s08.getZ(), player.rotationYaw, player.rotationPitch, s08.getYaw(), s08.getPitch())).fire();
         }
      } else if(event instanceof EventMiddleClick) {
         if(!AC.getConfig().isMidClickFriendsEnabled()) {
            return;
         }

         MovingObjectPosition mouseOver = AC.getMC().objectMouseOver;
         if(mouseOver != null && mouseOver.typeOfHit == MovingObjectType.ENTITY && mouseOver.entityHit instanceof EntityPlayer && !(mouseOver.entityHit instanceof EntityPlayerSP)) {
            String name = mouseOver.entityHit.getName();
            boolean friend = AC.getFriendManager().isFriend(name);
            if(friend) {
               AC.getFriendManager().removeFriend(name);
               AC.getNotificationManager().addNotification(new Notification("Friends", "Removed \'§b" + name + "§r\' from your friends!", 2000));
            } else {
               AC.getFriendManager().addFriend(name, name);
               AC.getNotificationManager().addNotification(new Notification("Friends", "Added \'§b" + name + "§r\' as a friend!", 2000));
            }
         }
      } else if(event instanceof EventServerLeave) {
         ArenaCraft.isArena = false;
      } else if(event instanceof EventTick) {
         AC.getTaskManager().exec();
         HashMap<EntityLivingBase, Float> newHealth = new HashMap();
         AC.getMC().getWorld().loadedEntityList.stream().filter((entity) -> {
            return entity instanceof EntityLivingBase;
         }).forEach((entity) -> {
            if(this.lastHealth.containsKey(entity)) {
               float last = ((Float)this.lastHealth.get(entity)).floatValue();
               float now = ((EntityLivingBase)entity).getHealth() + ((EntityLivingBase)entity).getAbsorptionAmount();
               if(last != now) {
                  (new EventEntityHealthUpdate((EntityLivingBase)entity, now - last, now)).fire();
               }

               newHealth.put((EntityLivingBase)entity, Float.valueOf(now));
            } else {
               newHealth.put((EntityLivingBase)entity, Float.valueOf(((EntityLivingBase)entity).getHealth() + ((EntityLivingBase)entity).getAbsorptionAmount()));
            }

         });
         this.lastHealth = newHealth;
      } else if(event instanceof EventRenderString) {
         EventRenderString render = (EventRenderString)event;

         for(String username : this.getPlayers()) {
            if(render.getText() == null) {
               return;
            }

            render.setText(render.getText().replace(username, AC.getFriendManager().getAlias(username)));
         }
      } else if(event instanceof EventWorldChanged && this.lastServerJoin != null) {
         server = this.lastServerJoin.getAddr();
         (new EventSuccessfulServerJoin(this.lastServerJoin.getAddr(), this.lastServerJoin.getPort())).fire();
         this.lastServerJoin = null;
      }

   }
}
