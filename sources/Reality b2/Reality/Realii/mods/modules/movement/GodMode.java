package Reality.Realii.mods.modules.movement;

import java.util.Map;
import java.util.Queue;
import java.util.ArrayDeque;

import java.util.HashMap;
import Reality.Realii.Client;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.events.world.EventTick;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.guis.notification.Notification;
import Reality.Realii.guis.notification.NotificationsManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.player.InventoryUtils;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.world.TimerUtil;
import Reality.Realii.utils.math.MathUtil;
import Reality.Realii.utils.math.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S1BPacketEntityAttach;

public class GodMode extends Module{
	 private Map<Integer, Queue<Packet<?>>> queue;
   
	    public int currTick;
	    public int leftTick;
	    private boolean waitingForReset;
	    public int keepAliveTick;
	    public long displayTime;
	    public boolean last;
	    
	public GodMode(){
		super("GodMode", ModuleType.Movement);
		 this.queue = new HashMap<Integer, Queue<Packet<?>>>();
		  this.currTick = 0;
	     this.leftTick = 0;
	        this.waitingForReset = false;
	        this.keepAliveTick = 0;
	        this.displayTime = 0L;
	        this.last = false;
	}

    @Override
    public void onEnable() {
    	 this.currTick = 0;
         this.leftTick = 0;
         this.waitingForReset = false;
         this.waitingForReset = false;
         this.queue.clear();
    	
     NotificationsManager.addNotification(new Notification("Boat Disaballer Place two Boats then right click on them", Notification.Type.Alert,9));
        super.onEnable();

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
       
    }
    
    @EventHandler
    public void Worldtick(EventTick event) {
    	
    	  ++this.currTick;
          if (this.mc.thePlayer.ridingEntity != null) {
              for (final Entity entity : this.mc.theWorld.getLoadedEntityList()) {
                  if (entity instanceof EntityBoat) {
                      final double deltaX = entity.posX - this.mc.thePlayer.posX;
                      final double deltaY = entity.posY - this.mc.thePlayer.posY;
                      final double deltaZ = entity.posZ - this.mc.thePlayer.posZ;
                      if (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) >= 5.0) {
                          continue;
                      }
                      this.mc.timer.timerSpeed = 0.5f;
                      //   this.mc.timer.timerSpeed = 0.5f;
                      
                      if (entity == this.mc.thePlayer.ridingEntity) {
                          continue;
                      }
                      int item = -1;
                      double highest = 0.0;
                      for (int i = 36; i < 45; ++i) {
                          if (this.mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null && InventoryUtils.getItemDamage(this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > highest) {
                              highest = InventoryUtils.getItemDamage(this.mc.thePlayer.inventoryContainer.getSlot(i).getStack());
                              item = i - 36;
                          }
                      }
                      if (highest == 0.0) {
                          item = -1;
                      }
                      if (item == -1) {
                      	 NotificationsManager.addNotification(new Notification("You need to hold a better weapon", Notification.Type.Alert,3));
                         
                          return;
                      }
                      if (item != this.mc.thePlayer.inventory.currentItem) {
                          this.mc.thePlayer.inventory.currentItem = item;
                          return;
                      }
                      if (this.mc.thePlayer.getCurrentEquippedItem() == null || InventoryUtils.getItemDamage(this.mc.thePlayer.getCurrentEquippedItem()) < 4.0) {
                      	 NotificationsManager.addNotification(new Notification("You need to hold a better weapon", Notification.Type.Alert,3));
                          return;
                      }
                      RotationUtil.pitch(90.0f);
                      this.mc.thePlayer.swingItem();
                      this.mc.playerController.attackEntity((EntityPlayer)this.mc.thePlayer, this.mc.thePlayer.ridingEntity);
                      this.mc.thePlayer.swingItem();
                      this.mc.playerController.attackEntity((EntityPlayer)this.mc.thePlayer, entity);
                  }
              }
          }
          
          //else {
            // if (!((Timer)ModulesManager.getModuleByClass((Class)Timer.class)).isToggled()) {
               //   this.mc.timer.timerSpeed = 1.0;
              //}
             // else {
            //      this.mc.timer.timerSpeed = ((Timer)ModulesManager.getModuleByClass((Class)Timer.class)).timerSpeed.getValue();
            //  }
         // }
      
    }
    
    @EventHandler
    public void onMove(EventMove e) {
      
    }
    
    @EventHandler
    public void onWorldChange(EventPacketRecieve event) {
        if (event.getPacket() instanceof S07PacketRespawn || event.getPacket() instanceof S01PacketJoinGame) {
        	  this.queue.clear();
            this.currTick = 0;
            this.waitingForReset = false;
        }
        if (event.getPacket() instanceof S13PacketDestroyEntities) {
            if (((S13PacketDestroyEntities)event.getPacket()).getEntityIDs().length != 1) {
                for (final int entityID : ((S13PacketDestroyEntities)event.getPacket()).getEntityIDs()) {
                    if (entityID == this.mc.thePlayer.ridingEntity.getEntityId()) {
                     this.mc.timer.timerSpeed = 1.0f;
                        NotificationsManager.addNotification(new Notification("Final GrandMom, \"Final GrandPa Activated! (GodMode + Semi Disabler)", Notification.Type.Alert,3));
                        this.waitingForReset = true;
                    }
                }
            }
            else {
                for (final int entityID : ((S13PacketDestroyEntities)event.getPacket()).getEntityIDs()) {
                    if (entityID == this.mc.thePlayer.ridingEntity.getEntityId()) {
                  
                        NotificationsManager.addNotification(new Notification("Final GrandPaMom, \"Activation of Final GrandPa has Failed!!", Notification.Type.Alert,3));
                        this.displayTime = System.currentTimeMillis();
                        for (int i = 0; i < 20; ++i) {
                           

                            NotificationsManager.addNotification(new Notification("YOU HAVEN'T ACTIVATED FINAL GRANDMom SUCCESSFULLY!", Notification.Type.Alert,3));
                        }
                    }
                }
            }
        }
        if (event.getPacket() instanceof S1BPacketEntityAttach && ((S1BPacketEntityAttach)event.getPacket()).getEntityId() == this.mc.thePlayer.getEntityId() && ((S1BPacketEntityAttach)event.getPacket()).getVehicleEntityId() > 0) {
            this.currTick = 0;
        }
    }
    
    
    @EventHandler
    public void onPacket(EventPacketSend event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            if (this.mc.thePlayer.isRiding()) {
                ((C03PacketPlayer)event.getPacket()).onGround = false;
            }
            if (this.waitingForReset) {
                ((C03PacketPlayer)event.getPacket()).onGround = false;
            }
            if (!this.mc.thePlayer.isRiding() || this.mc.thePlayer.ridingEntity == null || this.currTick <= 50) {}
            if (this.mc.thePlayer.getHealth() >= 19.0f || !this.waitingForReset || this.mc.thePlayer.isDead || this.mc.thePlayer.onGround) {}
        }
    }
    
    
    public void addToQueue(final Packet<?> packet) {
        Queue<Packet<?>> packets = null;
        if (this.queue.containsKey(this.currTick + 50)) {
            packets = this.queue.get(this.currTick + 50);
        }
        else {
            packets = new ArrayDeque<Packet<?>>();
            this.queue.put(this.currTick + 50, packets);
        }
        packets.add(packet);
    }
    
    
   
    
    

}
