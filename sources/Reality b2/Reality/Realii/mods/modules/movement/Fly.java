package Reality.Realii.mods.modules.movement;

import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import java.security.SecureRandom;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import Reality.Realii.Client;
import net.minecraft.entity.player.PlayerCapabilities;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.misc.EventCollideWithBlock;
import Reality.Realii.event.events.rendering.EventRender2D;
import Reality.Realii.event.events.world.EventMove;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.guis.font.CFontRenderer;
import Reality.Realii.guis.font.FontLoaders;
import Reality.Realii.guis.notification.Notification;
import Reality.Realii.guis.notification.NotificationsManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.render.ArrayList2;
import Reality.Realii.utils.cheats.player.Helper;
import Reality.Realii.utils.cheats.player.Motion;
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
import libraries.optifine.MathUtils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;
import org.newdawn.slick.util.MaskUtil;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;

public class Fly extends Module{
	private Mode mode = new Mode("Mode", "mode", new String[]{"Motion","Sentinel","VulcanMotion","VerusLatest","VulcanTimer","Invaded","BaldoGrim","GigaNigga","Morgan","OldNcp","OldNcp2","ZoneCraft","Funcraft","VerusCustom", "Glide","VerusFast","VulcanY","Grim","FunFly","BlinkDamage","VulcanJump","VulcanFast", "AirWalk","NoRule","VulcanNew","Verus","VerusFaster","CubeCraft","HycraftTnt","Clip","IntaveBoat","Vulcan","Negativity","MushMc","Watchdog","Hylex","Mmc"}, "Motion");
	public Option<Boolean> FakeDamage = new Option<>("FakeDamage", false);
	 public static Numbers<Number>  speed = new Numbers<>("MotionFlySpeed", 0.5f, 0.5f, 30f, 10f);
	  private double moveSpeed;
	    private int stage, ticks;
	    int ticks2;
	    int Flags;
	private boolean HasDamage;
    private boolean canflyh;
    private double movementFly;
    private double distance;
    private boolean bool;
    public boolean shift;
    private TimerUtil timer = new TimerUtil();
    private TimerUtil Sugi = new TimerUtil();
    private List<Packet> packetList = new ArrayList<Packet>();
    
    public Fly() {
        super("Fly", ModuleType.Movement);
        this.addValues(this.mode,  speed,FakeDamage);
        
	
		
	}
    
    private boolean canJump() {
        if (this.mc.thePlayer.moving() && mc.thePlayer.onGround) {
            return true;
        }
        return false;
    }
   

    
    @Override
    public void onEnable() {
		if (this.mode.getValue().equals("Invaded")) {
			mc.thePlayer.setPosition(mc.thePlayer.posX,mc.thePlayer.posY + 1,mc.thePlayer.posZ);

		}
    	  Sugi.reset();
    	  Flags = 0;
    	  shift = false;
    	  canflyh = false;
    	  HasDamage = false;
          bool = false;
          if((boolean)FakeDamage.getValue() && !this.mode.getValue().equals("VerusLatest")) {
        	  PlayerUtils.fakeDamage();
          }
          
      	if(mode.getValue().equals("VulcanTimer")) {
      		mc.timer.timerSpeed = 1.0f;
      		
      	}
       	if(mode.getValue().equals("Funcraft")) {
          moveSpeed = 0;
          stage = mc.thePlayer.onGround ? 0 : -1;
          ticks = 0;
       	}
		if(mode.getValue().equals("VerusLatest")) {
			mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
		}

          
       	
       	
       	
      	if(mode.getValue().equals("OldNcp")) {
         mc.timer.timerSpeed = 1.6f;
          //mc.thePlayer.motionY = 0.30;
      		mc.thePlayer.motionY = 0.42;
      		
      		if (!mc.thePlayer.onGround) {
      		
    			  NotificationsManager.addNotification(new Notification("You must be on the ground to toggle Fly", Notification.Type.Alert,1));
    	     
    	            
    	        }
      		
          //1.6
      	}
      	
      	
      	if(mode.getValue().equals("OldNcp2")) {
            mc.timer.timerSpeed = 1.8f;
            //1.6
             //mc.thePlayer.motionY = 0.30;
         		mc.thePlayer.motionY = 0.417;
         		
         		if (!mc.thePlayer.onGround) {
         		
       			  NotificationsManager.addNotification(new Notification("You must be on the ground to toggle Fly", Notification.Type.Alert,1));
       	     
       	            
       	        }
         		
             //1.6
         	}
        	if(mode.getValue().equals("ZoneCraft")) {
         		 mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
                mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
         	}
        	
        
      	if(mode.getValue().equals("VerusCustom")) {
      		 mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
             mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
             mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
      	}
    	if(mode.getValue().equals("Mmc")) {
    		mc.thePlayer.sendQueue.addToSendQueue(new S27PacketExplosion());
    		mc.timer.timerSpeed = 0.5f;
    		
    	}
    	
    	if(mode.getValue().equals("VulcanNew")) {
    	
    			mc.thePlayer.motionY = 9.99;
    	   
    		
    		mc.timer.timerSpeed = 1.0f;
    	}
    	if (this.mode.getValue().equals("VerusFast")) {
    	mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(null));
    	Helper.sendMessage("wait until mesagge to fly");
    	
    	Helper.sendMessage("Some Verus checks are now a sleep");
    	 mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
         mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
         mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
      
    	}
    	
    	if (this.mode.getValue().equals("Hylex")) {
        	 mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
             mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
             mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
          
        	}
    	if (this.mode.getValue().equals("VulcanFast")) {
    		
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ,
    	    mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
          
        	}
    	
    	
    	
	if (this.mode.getValue().equals("FunFly")) {
		
		   NotificationsManager.addNotification(new Notification("Damaged YourSelf", Notification.Type.Alert,1));
		
		
		
		 mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.01, mc.thePlayer.posZ, false));
       mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
        mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    //   mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ,
      // mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
     
    		//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ,
    	    //mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
    		
    		//OR
    	    //mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ,
    	   
          
        	}
    	
    	
    	
    	
    	if (this.mode.getValue().equals("NoRule")) {
    		  mc.timer.timerSpeed = 1.0f;
  		  
  		 mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
           mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
           mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
           
      	
      	
      	}
    	
    	if (this.mode.getValue().equals("VerusFaster")) {
    		mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(null));
    	//mc.thePlayer.jump();
    //	 NotificationsManager.addNotification(new Notification("Damaged YourSelf", Notification.Type.Alert,1));
    //	if(mc.thePlayer.onGround) {
			mc.thePlayer.jump();
    	 mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
         mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
         mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    	//}
    	//maybe dont use jump / if ground
      
    	}
    	
    	
    	
    	
    	if (this.mode.getValue().equals("Negativity")) {
    		mc.thePlayer.motionY = 0.5;
    		mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(null));
    	 mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
         mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
         mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
      
    	}
    	if (this.mode.getValue().equals("IntaveBoat")) {
    		Helper.sendMessage("You Must be in a boat to fly otherwise it will not work just saying");
    		
    	}
    	
    	if (this.mode.getValue().equals("MushMc")) {
    		mc.thePlayer.motionY = 0.5;
      
    	}
    	
    	
        super.onEnable();
      
        

        	
        }        
        
       
        
        	
    

    @Override
    public void onDisable() {
    	shift = false;
    	Flags = 0;
     	if(mode.getValue().equals("VulcanTimer")) {
      		mc.timer.timerSpeed = 1.0f;
      		
      	}
    	if (this.mode.getValue().equals("GigaNigga")) {
    	ticks = 0;
		mc.thePlayer.speedInAir = 0.02f;
    	}
    	if (this.mode.getValue().equals("OldNcp")) {
    	//	 NotificationsManager.addNotification(new Notification("Do Not Use The Fly Again Untill This Closses", Notification.Type.Alert,3));
        		mc.thePlayer.capabilities.allowFlying = false;
        		mc.thePlayer.capabilities.isFlying = false;
    		mc.timer.timerSpeed = 1.0f;
    	}
    	
    	if (this.mode.getValue().equals("OldNcp2")) {
   		
       		mc.thePlayer.capabilities.allowFlying = false;
       		mc.thePlayer.capabilities.isFlying = false;
   		mc.timer.timerSpeed = 1.0f;
   	}
    	 if (this.mode.getValue().equals("NoRule")) {
         	mc.timer.timerSpeed = 1.0f;
         }
    	
    	if(mode.getValue().equals("Grim")) {
    		mc.timer.timerSpeed = 1.0f;
    	}
    	
    	if(mode.getValue().equals("FunFly")) {
    		mc.timer.timerSpeed = 1.0f;
    	}
    	if(mode.getValue().equals("VulcanNew")) {
    		mc.timer.timerSpeed = 1.0f;
    	}
    	
      
    	canflyh = false;
    	if (this.mode.getValue().equals("VulcanY")) {
    		   for (Packet packet : this.packetList) {
    	            this.mc.getNetHandler().addToSendQueue(packet);
    	        }
    	        this.packetList.clear();
    	}
    	if (this.mode.getValue().equals("CubeCraft")) {
    	
        for (Packet packet : this.packetList) {
            this.mc.getNetHandler().addToSendQueue(packet);
        }
        this.packetList.clear();
    	}
		if(mode.getValue().equals("VerusLatest")) {
			mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
		}
    	
    	if (this.mode.getValue().equals("BlinkDamage")) {
    		 mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
             mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
             mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        	mc.timer.timerSpeed = 1.0f;
            for (Packet packet : this.packetList) {
                this.mc.getNetHandler().addToSendQueue(packet);
            }
            this.packetList.clear();
        	}
    	if (this.mode.getValue().equals("Hylex")) {
    		mc.timer.timerSpeed = 1.0f;
    		
          
        	}
    	
    	if(mode.getValue().equals("Mmc")) {
    		mc.timer.timerSpeed = 1.0f;
    		
    		
    		for (Packet packet : this.packetList) {
                this.mc.getNetHandler().addToSendQueue(packet);
            }
            this.packetList.clear();
    		
    	}
    			
    	
    	
    	if (this.mode.getValue().equals("HycraftTnt")) {
        	
            for (Packet packet : this.packetList) {
                this.mc.getNetHandler().addToSendQueue(packet);
            }
            this.packetList.clear();
        	}
    	
        if (this.mode.getValue().equals("IntaveBoat")) {
        	mc.timer.timerSpeed = 1.4f;
        	
        	
        }
        
        
        
        if (this.mode.getValue().equals("VerusFaster")) {
        	mc.timer.timerSpeed = 1.0f;
        	
        }
        if (this.mode.getValue().equals("Negativity")) {
        	mc.timer.timerSpeed = 1.0f;
        	
        }
        if (this.mode.getValue().equals("MushMc")) {
        	mc.timer.timerSpeed = 1.0f;
        	
        }
        if (this.mode.getValue().equals("VerusCustom")) {
        	mc.timer.timerSpeed = 1.0f;
        	
        }
        
        //Negativity
        
    
        super.onDisable();

        
    }
    
    
    private boolean canJeboos() {
        if (!(this.mc.thePlayer.fallDistance >= 3.0f || this.mc.gameSettings.keyBindJump.isPressed() || BlockHelper.isInLiquid() || this.mc.thePlayer.isSneaking())) {
            return true;
        }
        return false;
    }
    public static double range(double min, double max) {
        return min + (new Random().nextDouble() * (max - min));
    }


    @EventHandler
    public void onPre(EventPreUpdate e) {
    	if(mode.getValue().equals("AirWalk")) {
        if (BlockHelper.isInLiquid() && !this.mc.thePlayer.isSneaking() && !this.mc.gameSettings.keyBindJump.isPressed()) {
            this.mc.thePlayer.motionY = 0.05;
            this.mc.thePlayer.onGround = true;
        }
    	}

		if(mode.getValue().equals("Invaded")) {
			if (BlockHelper.isInLiquid() && !this.mc.thePlayer.isSneaking() && !this.mc.gameSettings.keyBindJump.isPressed()) {
				this.mc.thePlayer.motionY = 0.05;
				this.mc.thePlayer.onGround = true;
			}
		}
    	
    	if(mode.getValue().equals("VerusCustom")) {
            if (BlockHelper.isInLiquid() && !this.mc.thePlayer.isSneaking() && !this.mc.gameSettings.keyBindJump.isPressed()) {
                this.mc.thePlayer.motionY = 0.05;
                this.mc.thePlayer.onGround = true;
            }
        	}
    	if(mode.getValue().equals("VulcanTimer")) {
            if (BlockHelper.isInLiquid() && !this.mc.thePlayer.isSneaking() && !this.mc.gameSettings.keyBindJump.isPressed()) {
                this.mc.thePlayer.motionY = 0.05;
                this.mc.thePlayer.onGround = true;
            }
        	}
    	
    	if(mode.getValue().equals("VulcanY")) {
            if (BlockHelper.isInLiquid() && !this.mc.thePlayer.isSneaking() && !this.mc.gameSettings.keyBindJump.isPressed()) {
                this.mc.thePlayer.motionY = 0.05;
                this.mc.thePlayer.onGround = true;
            }
        	}
    	
    	
    
    	
    	
    	
    	if(mode.getValue().equals("Sentinel")) {
            if (BlockHelper.isInLiquid() && !this.mc.thePlayer.isSneaking() && !this.mc.gameSettings.keyBindJump.isPressed()) {
                this.mc.thePlayer.motionY = 0.05;
                this.mc.thePlayer.onGround = true;
            }
        	}
    	
    	if(mode.getValue().equals("Hylex")) {
            if (BlockHelper.isInLiquid() && !this.mc.thePlayer.isSneaking() && !this.mc.gameSettings.keyBindJump.isPressed()) {
                this.mc.thePlayer.motionY = 0.05;
                this.mc.thePlayer.onGround = true;
            }
        	}
    	
    	
    }

    @EventHandler
    public void onPacket(EventPacketSend e) {
    	if(mode.getValue().equals("AirWalk")) {
        if (e.getPacket() instanceof C03PacketPlayer && this.canJeboos() && BlockHelper.isOnLiquid()) {
            C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
            packet.y = this.mc.thePlayer.ticksExisted % 2 == 0 ? packet.y + 2 : packet.y - 2;
        }
    	}

		if(mode.getValue().equals("Invaded")) {
			if (e.getPacket() instanceof C03PacketPlayer && this.canJeboos() && BlockHelper.isOnLiquid()) {
				C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
				packet.y = this.mc.thePlayer.ticksExisted % 2 == 0 ? packet.y + 2 : packet.y - 2;
			}
		}
    	
    	if(mode.getValue().equals("VulcanTimer")) {
    		 if (e.getPacket() instanceof C03PacketPlayer && this.canJeboos() && BlockHelper.isOnLiquid()) {
                 C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
                 packet.y = this.mc.thePlayer.ticksExisted % 2 == 0 ? packet.y + 2 : packet.y - 2;
             }
    		
    		
    	}
    	
    	if(mode.getValue().equals("VerusCustom")) {
            if (e.getPacket() instanceof C03PacketPlayer && this.canJeboos() && BlockHelper.isOnLiquid()) {
                C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
                packet.y = this.mc.thePlayer.ticksExisted % 2 == 0 ? packet.y + 2 : packet.y - 2;
            }
        	}
    	
    	if(mode.getValue().equals("VulcanY")) {
            if (e.getPacket() instanceof C03PacketPlayer && this.canJeboos() && BlockHelper.isOnLiquid()) {
                C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
                packet.y = this.mc.thePlayer.ticksExisted % 2 == 0 ? packet.y + 2 : packet.y - 2;
            }
        	}
    	
    	
    	
    	if(mode.getValue().equals("Sentinel")) {
            if (e.getPacket() instanceof C03PacketPlayer && this.canJeboos() && BlockHelper.isOnLiquid()) {
                C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
                packet.y = this.mc.thePlayer.ticksExisted % 2 == 0 ? packet.y + 2 : packet.y - 2;
            }
        	}
    	
    	if(mode.getValue().equals("Hylex")) {
            if (e.getPacket() instanceof C03PacketPlayer && this.canJeboos() && BlockHelper.isOnLiquid()) {
                C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
                packet.y = this.mc.thePlayer.ticksExisted % 2 == 0 ? packet.y + 2 : packet.y - 2;
            }
        	}
    	
    	
    }

    @EventHandler
    public void onBB(EventCollideWithBlock e) {
    	  final double x = e.getPos().getX(), y = e.getPos().getY(), z = e.getPos().getZ();
    	if(mode.getValue().equals("AirWalk")) {
        if (e.getBlock() instanceof BlockAir && this.canJeboos()) {
            
            e.setBoundingBox(AxisAlignedBB.fromBounds(-15, -1, -15, 15, 1, 15).offset(x, y, z));
        }
    	}

		if(mode.getValue().equals("Invaded")) {
			if (e.getBlock() instanceof BlockAir && this.canJeboos()) {

				e.setBoundingBox(AxisAlignedBB.fromBounds(-15, -1, -15, 15, 1, 15).offset(x, y, z));
			}
		}
    	if(mode.getValue().equals("Hylex")) {
            if (e.getBlock() instanceof BlockAir && this.canJeboos()) {
                
                e.setBoundingBox(AxisAlignedBB.fromBounds(-15, -1, -15, 15, 1, 15).offset(x, y, z));
            }
        	}
    	
    	if(mode.getValue().equals("VulcanTimer")) {
            if (e.getBlock() instanceof BlockAir && this.canJeboos()) {
                
                e.setBoundingBox(AxisAlignedBB.fromBounds(-15, -1, -15, 15, 1, 15).offset(x, y, z));
            }
        	}
    	
    	if(mode.getValue().equals("VulcanY")) {
            if (e.getBlock() instanceof BlockAir && this.canJeboos()) {
                
                e.setBoundingBox(AxisAlignedBB.fromBounds(-15, -1, -15, 15, 1, 15).offset(x, y, z));
            }
        	}
    
    	if(mode.getValue().equals("Sentinel")) {
            if (e.getBlock() instanceof BlockAir && this.canJeboos()) {
                
                e.setBoundingBox(AxisAlignedBB.fromBounds(-15, -1, -15, 15, 1, 15).offset(x, y, z));
            }
        	}
    	
    	if(mode.getValue().equals("VerusCustom")) {
            if (e.getBlock() instanceof BlockAir && this.canJeboos()) {
                
                e.setBoundingBox(AxisAlignedBB.fromBounds(-15, -1, -15, 15, 1, 15).offset(x, y, z));
            }
        	}
    	
    	
    	
    	
    }
    //  if (this.mode.getValue().equals("Hylex")) {


    @EventHandler
    public void onUpdate(EventPreUpdate e) {

    	if (this.mode.getValue().equals("BaldoGrim")) {
    		SecureRandom random = new SecureRandom();
    		float yaw = (float) Math.toRadians(mc.thePlayer.rotationYaw);
    		float pitch = (float) Math.toRadians(mc.thePlayer.rotationPitch);
    		e.setYaw((float) (yaw + MathHelper.getRandomDoubleInRange(random, 20, 20)));
    		e.setX(Math.sin(yaw * 10000));
    		
    		
    	}
    	if (this.mode.getValue().equals("Funcraft")) {
    		
    	
    	mc.thePlayer.motionY = 0.0;
    	
    		  if(!PlayerUtils.isMoving() || mc.thePlayer.isCollidedHorizontally) {
    	            stage = -1;
    	        }


    	       
    	        

    	        mc.thePlayer.jumpMovementFactor = 0F;
    	      
    	        stage++;
    		
    	}
    	if (this.mode.getValue().equals("GigaNigga")) {
    	mc.gameSettings.keyBindJump.pressed = mc.thePlayer.onGround;
		if (ticks == 1) {
			if (mc.thePlayer.hurtTime != 0) {
				e.y -= 100;
				e.x += 15;
				e.z += 15;
				mc.thePlayer.motionY = 2.5;
				mc.thePlayer.motionX *= 10;
				mc.thePlayer.motionZ *= 10;
				e.setOnground(false);
			}
			ticks = 0;
		}
    	}
    	if (this.mode.getValue().equals("Sentinel")) {
    		if (mc.gameSettings.keyBindJump.isKeyDown()) {
    			 mc.thePlayer.motionY = 2;
    		}
    		
    		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
   			 mc.thePlayer.motionY = -2;
   			mc.thePlayer.motionY = -0.2;
   			 
   					 
   		}
     		// if (mc.gameSettings.keyBindJump.isKeyDown()) {
        	//		// mc.timer.timerSpeed = 1.0f;
   	           // mc.thePlayer.motionY = 0.2;
   	      //  } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
   	        //	mc.timer.timerSpeed = 1.0f;
   	          //  mc.thePlayer.motionY = -0.2;
   	        //}
    		if (!mc.gameSettings.keyBindSneak.isKeyDown()) {
     		if (mc.thePlayer.motionY < 0.1) {
   			 mc.thePlayer.motionY = -0.2;
   	}
   		if (mc.thePlayer.ticksExisted % 2 == 0) {
   		mc.thePlayer.motionY = 0.1;
   		}
    		}
    		
    	
    		  
    		//if(mc.thePlayer.onGround) {
    		
    			//mc.thePlayer.motionY = 0.1;
    		//}
    	   
    	}
    	if (this.mode.getValue().equals("OldNcp")) {
    		e.setOnground(true);
    		mc.thePlayer.capabilities.allowFlying = true;
    	//	mc.thePlayer.capabilities.isFlying = true;
    		mc.thePlayer.capabilities.flySpeed = 0.0f;
    		 mc.thePlayer.sendQueue.addToSendQueue(new C13PacketPlayerAbilities());
    		 e.setX(e.getPitch() + (Math.random() - 0.5) / 100);
             e.setZ(e.getPitch() + (Math.random() - 0.5) / 100);
    		if(Sugi.hasReached(1000)) {
    			mc.timer.timerSpeed = 1.2f;
    		 }
    		
    		if(Sugi.hasReached(1500)) {
    			mc.timer.timerSpeed = 1.0f;
    		}
    		
    		
    		mc.thePlayer.motionY = 0;
    		//e.setY(e.getY() + (mc.thePlayer.ticksExisted % 2 == 0 ? RandomUtils.nextDouble(1E-10, 1E-5) : -RandomUtils.nextDouble(1E-10, 1E-5)));
           
    	       
    	}
    	
    	
    	if (this.mode.getValue().equals("OldNcp2")) {
    		PlayerCapabilities playerCapabilities = new PlayerCapabilities();
    		   if (mc.thePlayer.ticksExisted % 30 == 0) {
                   if (mc.thePlayer.isSpectator()) {
                  
                       playerCapabilities.isFlying = true;
                       playerCapabilities.allowFlying = true;
                       playerCapabilities.setFlySpeed((float) range(8.85343425, 9.85343425));
                       playerCapabilities.setPlayerWalkSpeed((float) range(9, 9.8));
                       mc.thePlayer.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(playerCapabilities));
                   }
    		   }
    		   
    		e.setOnground(true);
    		mc.thePlayer.capabilities.allowFlying = true;
    	//	mc.thePlayer.capabilities.isFlying = true;
    		//mc.thePlayer.capabilities.flySpeed = 0.0f;
    		 mc.thePlayer.sendQueue.addToSendQueue(new C13PacketPlayerAbilities());
    		   e.setY(e.getY() + 1.67346739E-7);
    		//300
    		if(Sugi.hasReached(300)) {
    			mc.timer.timerSpeed = 1.5f;
    		}
    		if(Sugi.hasReached(700)) {
    			mc.timer.timerSpeed = 1.0f;
    		}
    		
    		
    		
    		mc.thePlayer.motionY = 0;
    		//e.setY(e.getY() + (mc.thePlayer.ticksExisted % 2 == 0 ? RandomUtils.nextDouble(1E-10, 1E-5) : -RandomUtils.nextDouble(1E-10, 1E-5)));
           
    	       
    	}
    	
    	   this.mc.thePlayer.cameraYaw = (float)(0.09090908616781235 * 1.0f);
    	if(mode.getValue().equals("VulcanFast")) {
    		
   		 mc.thePlayer.motionY = 0.0;
   		
   		 
   	    
   	}
    	
    	if(mode.getValue().equals("BlinkDamage")) {
    		 mc.thePlayer.motionY = 0.0;
    		 if (mc.gameSettings.keyBindJump.isKeyDown()) {
       			// mc.timer.timerSpeed = 1.0f;
  	            mc.thePlayer.motionY = 2.0;
  	        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
  	        //	mc.timer.timerSpeed = 1.0f;
  	            mc.thePlayer.motionY = -2.0;
  	        }
    	}
    	
    	if(mode.getValue().equals("FunFly")) {
    	//	if (mc.thePlayer.ticksExisted % 20 == 0) {
    		//	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ,
    		//    mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
    		//}
    		//if (mc.thePlayer.ticksExisted % 58 == 0) {
       		 //mc.thePlayer.motionY = 9.0;
       	 	//}
    		
    		if(mc.thePlayer.hurtTime > -1) {
    		if (mc.thePlayer.ticksExisted % 1 == 0) {
      		 mc.thePlayer.motionY = 0.0;
    		}
    		if (mc.thePlayer.motionY < 0.20) {
    			 mc.thePlayer.motionY = -0.20;
    		}
    		if (mc.thePlayer.ticksExisted % 2 == 0) {
    			mc.thePlayer.motionY = 0.20;
    		}
      		 if (mc.gameSettings.keyBindJump.isKeyDown()) {
      			// mc.timer.timerSpeed = 1.0f;
 	            mc.thePlayer.motionY = 0.3;
 	        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
 	        //	mc.timer.timerSpeed = 1.0f;
 	            mc.thePlayer.motionY = -0.3;
 	        }
    		}
      		
      		 
      	    
      	}
    
    		
    	
 
    	if (ArrayList2.Sufix.getValue().equals("On")) {
        	
            
    		this.setSuffix(mode.getModeAsString());
    	}
    		
    	 
    	 if (ArrayList2.Sufix.getValue().equals("Off")) {
         	
    	        
     		this.setSuffix("");
     	}
    	if (this.mode.getValue().equals("Motion")) {
    	mc.thePlayer.motionY = 0.0;
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.thePlayer.motionY = speed.getValue().doubleValue();
            mc.thePlayer.setMoveSpeedpre(e, speed.getValue().doubleValue());
        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
        	 mc.thePlayer.setMoveSpeedpre(e, speed.getValue().doubleValue());
            mc.thePlayer.motionY = -speed.getValue().doubleValue();
        }
        
        }
        if (this.mode.getValue().equals("CubeCraft")) {
        	mc.thePlayer.motionY = 0.0;
        	
    	}
        
       
        
    
    if (this.mode.getValue().equals("HycraftTnt")) {
    	mc.thePlayer.motionY = 0.0;
    	
	}
    
    if(mode.getValue().equals("Vulcan")) {
        if (mc.thePlayer.fallDistance > 2) {
            
            mc.thePlayer.fallDistance = 0;
        }
        if (mc.thePlayer.ticksExisted % 3 != 0) {
            mc.thePlayer.motionY = -0.0972;
        } else {
            mc.thePlayer.motionY += 0.026;
        }
      
    }
    
    
    if(mode.getValue().equals("Grim")) {
    	  if(mc.thePlayer.hurtTime > 1) {

    	mc.thePlayer.motionY = 0.0;
    	  }
    }
    if(mode.getValue().equals("VulcanNew")) {
    	
        if (mc.thePlayer.fallDistance > 2) {
            
            mc.thePlayer.fallDistance = 0;
        }
        if (mc.thePlayer.ticksExisted % 3 != 0) {
        	
            mc.thePlayer.motionY = -0.0972;
        	
        }
        else {
            mc.thePlayer.motionY += 0.026;
        }
        if (mc.gameSettings.keyBindJump.isPressed()) {
        	 mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 4.0, mc.thePlayer.posZ);
        }
    	 
        
        
        
       
        if (mc.gameSettings.keyBindSneak.isPressed()) {
       	
       	mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + -2.0, mc.thePlayer.posZ);
        	
       }
        
      
    }
    



    
    	
    	
    	
    	if (this.mode.getValue().equals("VerusFast")) {
    		if (mc.thePlayer.hurtTime > -1) {
    			
    			if(mc.thePlayer.motionY < 0.4) {
    				mc.thePlayer.motionY = 0.0;
    				
    			}
    			
    			if(mc.thePlayer.motionY < 0.0) {
    			
    				mc.thePlayer.onGround = true;
    				mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(null));
    				
    				
    					
    				
    			}
    			if(mc.thePlayer.motionY < -0.4) {
    				
    				mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(null));
    				mc.thePlayer.motionY = 0.0;
    				
    			}
    		

    	        
    	        if (mc.gameSettings.keyBindJump.isKeyDown()) {
    	            mc.thePlayer.motionY = 0.5;
    	        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
    	            mc.thePlayer.motionY = -0.5;
    	        }
    			
    		}
    		
    		
    		
    		
    		
    		
    		
    		
      		
    		
    		
    		 
    		 }
    	
    	if (this.mode.getValue().equals("Hylex")) {
    		
    	

    	        
    	    
    			
    		}
    	
    	if (this.mode.getValue().equals("NoRule")) {
        	
			mc.thePlayer.onGround = true;
			mc.thePlayer.motionY = 0.0;

	        
	        if (mc.gameSettings.keyBindJump.isKeyDown()) {
	            mc.thePlayer.motionY = 0.1;
	        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
	            mc.thePlayer.motionY = -0.1;
	        }
			
		}
    
    	
    	if (this.mode.getValue().equals("VerusFaster")) {
    		
    			if(mc.thePlayer.hurtTime>1) {
    				HasDamage = true;
    			}
    			if(HasDamage) {
    				mc.thePlayer.motionY = 0.0;
    			

				mc.timer.timerSpeed = 0.5f;
    	        
    	        if (mc.gameSettings.keyBindJump.isKeyDown()) {
    	            mc.thePlayer.motionY = 2;
    	        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
    	            mc.thePlayer.motionY = -2;
    	        }
    			
    		}
    	}
    		
    		
    		 
    		 
    	
    	if (this.mode.getValue().equals("Watchdog")) {
    		if (mc.thePlayer.hurtTime > -1) {
    			
    			if(mc.thePlayer.motionY < 0.4) {
    				  
    				
    			}
    			
    			if(mc.thePlayer.motionY < 0.0) {
    				this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.47, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
  	                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + -0.30, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
  	              this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.47, this.mc.thePlayer.posZ);
                  this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + -0.30, this.mc.thePlayer.posZ);
    				
    				mc.thePlayer.onGround = true;
    				
    				
    				
    	              
    	               
    	               
    					
    				
    			}
    			if(mc.thePlayer.motionY < -0.4) {
    				
    				mc.thePlayer.jump();
    				
    			}
    		

    	        
    
    			
    		}
    		
    		
    		
    		
      		
    		
    		
    		 
    		 }
    	
    	if (this.mode.getValue().equals("Negativity")) {
    		if (mc.thePlayer.hurtTime > -1) {
    			
    			if(mc.thePlayer.motionY < 0.4) {
    				mc.thePlayer.motionY = -0.007;
    				
    			}
    			
    			if(mc.thePlayer.motionY < 0.0) {
    			
    				mc.thePlayer.onGround = true;
    				mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(null));
    				
    				
    					
    				
    			}
    			if(mc.thePlayer.motionY < -0.4) {
    				
    				mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(null));
    				mc.thePlayer.motionY = -0.007;
    				
    			}
    		

    	        
    	        if (mc.gameSettings.keyBindJump.isKeyDown()) {
    	            mc.thePlayer.motionY = 0.2;
    	        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
    	            mc.thePlayer.motionY = -0.2;
    	        }
    			
    		}
    	}
      
    	
    	if (this.mode.getValue().equals("MushMc")) {
    		mc.thePlayer.motionY = 0;
    	
    			
    			if(mc.thePlayer.motionY < 0.4) {
    				mc.thePlayer.onGround = true;
    				
    				
    			}
    			
    			if(mc.thePlayer.motionY < 0.0) {
    				
    			
    				mc.thePlayer.onGround = true;
    				mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(null));
    				
    				
    					
    				
    			}
    			if(mc.thePlayer.motionY < -0.4) {
    				mc.thePlayer.onGround = true;
    				
    				mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(null));
    				
    				
    			}
    		

    	        
    	        if (mc.gameSettings.keyBindJump.isKeyDown()) {
    	            mc.thePlayer.motionY = 0.7;
    	        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
    	            mc.thePlayer.motionY = -0.7;
    	        }
    			
    		}
    	
    	
    	
    	
    	if (this.mode.getValue().equals("IntaveBoat")) {
    		mc.thePlayer.motionY = 0.31;
    		mc.timer.timerSpeed = 0.6f;
    		
    		
    		
    	}
    	
    	
    	
    	if (this.mode.getValue().equals("Clip")) {
    	    //double x = mc.thePlayer.posX;
           // double y = mc.thePlayer.posY;
            //double z = mc.thePlayer.posZ;
    		mc.thePlayer.motionY = 0.05;
    		 //if (mc.thePlayer.ticksExisted % 9 == 0) {
    			// x += 5;
    		   
    			  //mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    			//  mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 5, z, true));
    			  
    		 //}
    		// mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX + 7, mc.thePlayer.posY, mc.thePlayer.posZ));
    		  //c.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    		if (mc.thePlayer.ticksExisted % 10 == 0) {
    			
    			this.mc.thePlayer.setPosition(mc.thePlayer.posX + 5, mc.thePlayer.posY, mc.thePlayer.posZ);
    			
    			// mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX+ 99.0001, mc.thePlayer.posY, mc.thePlayer.posZ, false));
    	         //mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
    	        // mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
   			 
   		  // this.mc.thePlayer.setPosition(mc.thePlayer.posX + 7, mc.thePlayer.posY, mc.thePlayer.posZ);
   			  //mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
   			  
   			  
   		 }
    		  
    		
    		
    	}
    	
    	
    	
 
    
  

    	
    	    	
    	
    			
    			
    		    
    		
    	}
    
    
    
   // public void onEvent(EventMove e) {
      //  if(e instanceof EventMove) {
          //  mc.thePlayer.onGround = true;
       // }
  //  }
    

    

   
    
    @EventHandler
    private void onPacketSend(EventPacketSend event) {
    	if (this.mode.getValue().equals("GigaNigga")) {
    	if (event.getPacket() instanceof S12PacketEntityVelocity) {
			if (((S12PacketEntityVelocity) event.getPacket()).getEntityID() == mc.thePlayer.getEntityId()) {
				ticks = 1;
				mc.thePlayer.motionY += (((S12PacketEntityVelocity) event.getPacket()).getMotionY()/8000) * 2000;
				mc.thePlayer.motionX += (((S12PacketEntityVelocity) event.getPacket()).getMotionX()/8000) * 2000;
				mc.thePlayer.motionZ += (((S12PacketEntityVelocity) event.getPacket()).getMotionZ()/8000) * 2000;
			}
		}
    	}
    
    	
    	if (this.mode.getValue().equals("OldNcp")) {
    		
    	  if (event.getPacket() instanceof S32PacketConfirmTransaction) {
    		  final S32PacketConfirmTransaction S32 = (S32PacketConfirmTransaction) event.getPacket();
              if (S32.getActionNumber() < 0) {
                  event.setCancelled(true);
              }
    	  }
    	  }
    	
    	
          
      	  
    	
    	if (this.mode.getValue().equals("Hylex")) {
    		 if (event.getPacket() instanceof C00PacketKeepAlive && this.mc.thePlayer.isEntityAlive()) {
   	            this.packetList.add(event.getPacket());
   	            event.setCancelled(true);
   	        }
   	        if (this.timer.hasReached(750.0)) {
   	            if (!this.packetList.isEmpty()) {
   	                int i = 0;
   	                double totalPackets = MathUtils.getIncremental(Math.random() * 10.0, 1.0);
   	                for (Packet packet : this.packetList) {
   	                    if ((double)i >= totalPackets) continue;
   	                    ++i;
   	                    this.mc.getNetHandler().getNetworkManager().sendPacket(packet);
   	                    this.packetList.remove(packet);
   	                }
   	            }
   	            this.mc.getNetHandler().getNetworkManager().sendPacket(new C00PacketKeepAlive(19000));
   	            this.timer.reset();
   	        }
    		
    	}
    	if (this.mode.getValue().equals("CubeCraft")) {
        if (event.getPacket() instanceof C04PacketPlayerPosition || event.getPacket() instanceof C06PacketPlayerPosLook || event.getPacket() instanceof  C0BPacketEntityAction || event.getPacket() instanceof C02PacketUseEntity|| event.getPacket() instanceof S08PacketPlayerPosLook ) {
            this.packetList.add(event.getPacket());
            event.setCancelled(true);
            
            
        }
        
    	}
    	
    	if (this.mode.getValue().equals("BlinkDamage")) {
            if (event.getPacket() instanceof C04PacketPlayerPosition || event.getPacket() instanceof C06PacketPlayerPosLook || event.getPacket() instanceof  C0BPacketEntityAction || event.getPacket() instanceof C02PacketUseEntity|| event.getPacket() instanceof S08PacketPlayerPosLook ) {
                this.packetList.add(event.getPacket());
                event.setCancelled(true);
                
                
            }
            
        	}
    	
    	
    	if (this.mode.getValue().equals("VulcanY")) {
            if (event.getPacket() instanceof C04PacketPlayerPosition || event.getPacket() instanceof C06PacketPlayerPosLook || event.getPacket() instanceof  C0BPacketEntityAction || event.getPacket() instanceof C02PacketUseEntity|| event.getPacket() instanceof S08PacketPlayerPosLook ) {
                this.packetList.add(event.getPacket());
                event.setCancelled(true);
                
                
            }
            
        	}
    	
    	
    	
    	if (this.mode.getValue().equals("HycraftTnt")) {
            if (event.getPacket() instanceof C04PacketPlayerPosition || event.getPacket() instanceof C06PacketPlayerPosLook || event.getPacket() instanceof  C0BPacketEntityAction || event.getPacket() instanceof C02PacketUseEntity|| event.getPacket() instanceof S08PacketPlayerPosLook ) {
                this.packetList.add(event.getPacket());
                
                event.setCancelled(true);
                
                
            }
            
        	}
    	
        if (this.mode.getValue().equals("FunFly")) {
        	//if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
	        //    event.setCancelled(true);
	           // Helper.sendMessage("Cancelled C0F");
	       //}
        }
    	
    
    	if (this.mode.getValue().equals("Mmc")) {
            if (event.getPacket() instanceof C04PacketPlayerPosition || event.getPacket() instanceof C06PacketPlayerPosLook || event.getPacket() instanceof  C0BPacketEntityAction || event.getPacket() instanceof C02PacketUseEntity|| event.getPacket() instanceof S08PacketPlayerPosLook ) {
                this.packetList.add(event.getPacket());
                event.setCancelled(true);
                
                
            }
            
        	}
    	
    	
    }
    


    	
    	
    

    @EventHandler
    public void onMove(EventMove e) {
		if (this.mode.getValue().equals("Invaded")) {
			if(Flags>3) {
					mc.thePlayer.setMoveSpeed(e,1);
			}

		}
    	if (this.mode.getValue().equals("VulcanTimer")) {
    		
    		if(mc.gameSettings.keyBindSneak.isPressed()) {
      			shift = true;
      		}
      		if(Flags>3 && PlayerUtils.isMoving() && !shift) {
      			
      			mc.timer.timerSpeed = 5.0f;
      		}
      		
      		if(shift) {
      			mc.timer.timerSpeed = 1.0f;
      		}
      	
      		
      	}
    	
    	if(mode.getValue().equals("Funcraft")) {
    		switch(stage) {
            case -1:
                mc.thePlayer.motionY = 0;
                e.setY(-0.00001);
                return;
            case 0:
                moveSpeed = 0.3;
                break;
            case 1:
                if(mc.thePlayer.onGround) {
                    e.setY(mc.thePlayer.motionY = 0.3999);
                    moveSpeed *= 2.14;
                }
                break;
            case 2:
                moveSpeed = 1;
                break;
            default:
                moveSpeed -= moveSpeed / 159;
                mc.thePlayer.motionY = 0;
                
                e.setY(-0.00001);
                break;
        }
    	  mc.thePlayer.setMoveSpeed(e, Math.max(moveSpeed, Motion.getAllowedHorizontalDistance()));
    	}
    	if(mode.getValue().equals("OldNcp")) {
    	if(Sugi.hasReached(10)) {
     		
    		mc.thePlayer.setMoveSpeed(e, 0.50);	
 			
 		}
 		
 		if(Sugi.hasReached(550)) {
 			mc.thePlayer.setMoveSpeed(e, 0.35);	
 		}
 		if(Sugi.hasReached(1400)) {
 		//	mc.timer.timerSpeed = 0.8f;
 			
 		mc.thePlayer.setMoveSpeed(e, 0.35);	
 		}
 		 
 		
 		
    	}
    	
    	if(mode.getValue().equals("OldNcp2")) {
        	if(Sugi.hasReached(10)) {
         		
        		mc.thePlayer.setMoveSpeed(e,RandomUtils.nextDouble(0.50, 0.70));	
        		//50
        		//55
        		//60
        		
     			
     		}
     		//700
     		if(Sugi.hasReached(300)) {
     			//300
     			//400
     			
     			mc.thePlayer.setMoveSpeed(e,RandomUtils.nextDouble(0.31, 0.34));	
     			//28
     			//32
     			//30
     			//37
     			//35
     		}
     		
     		
     		
     		
     		
     		
 	//if(Sugi.hasReached(10)) {
         		
        	//	mc.thePlayer.setMoveSpeed(e,RandomUtils.nextDouble(0.50, 0.70));	
        		//50
        		//55
        		//60
        		
     			
     		//}
     		//700
     		//if(Sugi.hasReached(500)) {
     			//300
     			//400
     			
     		//	mc.thePlayer.setMoveSpeed(e,RandomUtils.nextDouble(0.30, 0.34));	
     			//28
     			//32
     			//30
     			//37
     			//35
     		//}
     		
     		 
     		
     		
        	}
    	
    	if(mode.getValue().equals("VulcanFast")) {
    		
    		mc.thePlayer.setMoveSpeed(e, Math.random() / 2000);	
    		// if (mc.thePlayer.getDistance(mc.thePlayer.lastReportedPosX, mc.thePlayer.lastReportedPosY, mc.thePlayer.lastReportedPosZ) <= 10 - speed - 0.15) {
    			 
    	//	 }
      		 
      	    
      	}
       
    
        if (this.mode.getValue().equals("Clip")) {
        	
        	 
        	
        }
    	if (this.mode.getValue().equals("Motion")) {
        if (PlayerUtils.isMoving()) {
        mc.thePlayer.motionY = 0.0;
            mc.thePlayer.setMoveSpeed(e, speed.getValue().doubleValue());
        }
        
    	}
    	
    	if (this.mode.getValue().equals("BlinkDamage")) {
           
            mc.thePlayer.motionY = 0.0;
            mc.timer.timerSpeed = 0.7f;
                mc.thePlayer.setMoveSpeed(e, 8);
            
            
        	}
    	if (this.mode.getValue().equals("Sentinel")) {
    	     mc.thePlayer.setMoveSpeed(e, 2);
    	     //0.55
    	 	//if(Sugi.hasReached(1200)) {
    	 		//mc.thePlayer.setMoveSpeed(e, 0.45);
    	 //	}
    		
    	 	
    	}
    	if (this.mode.getValue().equals("VulcanY")) {
            
    		
    		 mc.thePlayer.setMoveSpeed(e, 0.33);
            
        	}
        
        if (this.mode.getValue().equals("VerusFast")) {
        	 if (mc.thePlayer.hurtTime > 6) {
        		 if (mc.gameSettings.keyBindJump.isKeyDown()) {
        	            mc.thePlayer.motionY = 3;
        	            
        	    } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
        	    	mc.thePlayer.motionY = -3;
        	        }
        		 	mc.thePlayer.motionY = 0.0;
        		 
        		 	mc.timer.timerSpeed = 0.3f;
        		 	mc.thePlayer.setMoveSpeed(e, 8);
        		 	Helper.sendMessage("Funny Packets Boost =) =) ");
        	 }
        	 
        	 else if (mc.thePlayer.hurtTime > -1) {
        		 mc.timer.timerSpeed = 1.0f;
        		 mc.thePlayer.setMoveSpeed(e, 0.4);

    		 }
        	
        }
        
        
        if (this.mode.getValue().equals("VerusCustom")) {
       	 if (mc.thePlayer.hurtTime > 1) {
       	
       	            
       	  
       		 
       		 	mc.timer.timerSpeed = 0.5f;
       		 	mc.thePlayer.setMoveSpeed(e, 2);
       		 	
       	 } else {
       		 mc.timer.timerSpeed = 1.0f;
       		mc.thePlayer.setMoveSpeed(e, 0.40);
       	 }
       	 
       	
       	
       }
        
       // if (this.mode.getValue().equals("Hylex")) {
        	
       		 //if(mc.thePlayer.ticksExisted % 10 == 0) {
       		//	 mc.thePlayer.setMoveSpeed(e, 0.60);
       			
       			 
       		// }
       		 
       		
       	 
       	
      // }
        
        
       
        if (this.mode.getValue().equals("NoRule")) {
        	 if (mc.thePlayer.hurtTime > 2) {
        		 mc.timer.timerSpeed = 1.3f;
        	 }
        	 
        	 
            			mc.timer.timerSpeed = 1.2f;
            			//32
            		  
        	
       	 if (mc.thePlayer.hurtTime > -1) {
      	
      		 	mc.thePlayer.motionY = 0.0;
      		 	
      		    
      	 
      		
      		  //mc.timer.timerSpeed = 1.0f;
      		 mc.thePlayer.setMoveSpeed(e, 0.49);

  		 
       	 }
       	 
       	// if (mc.thePlayer.hurtTime > 2) {
       		//  mc.timer.timerSpeed = 1.0f;
       		
       		// mc.thePlayer.setMoveSpeed(e, 0.60);
       		 
       	 //}
      }
        
        
        if (this.mode.getValue().equals("VerusFaster")) {
      
       		
       		 
       		 
       		 	if(HasDamage) {
       		 	//mc.timer.timerSpeed = 0.5f;
       		 	mc.thePlayer.setMoveSpeed(e, 8);
       		 	} else {
       		 	mc.thePlayer.setMoveSpeed(e, 0);
       		 	}
       		 	
       	 }
        
        if (this.mode.getValue().equals("FunFly")) {
        	if(mc.thePlayer.hurtTime > 1) {
        	 // if (mc.thePlayer.ticksExisted % 60 == 0) {
        		 
        		  //mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.01, mc.thePlayer.posZ, false));
        	      // mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
        	      //  mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        	        
        	    
        	 // }
        	 
        	 //mc.timer.timerSpeed = 0.4f;
        	 
        	  
      		// if(mc.thePlayer.ticksExisted > 1)
   		
   		 	//OR
   		 	//mc.timer.timerSpeed = 0.8f;
   		 	//	mc.thePlayer.setMoveSpeed(e, 7);
        	
        	  mc.thePlayer.setMoveSpeed(e, 3);
        	}
        	  
   		 
   		 	
   	 }
        
        if (this.mode.getValue().equals("Mmc")) {
        	mc.timer.timerSpeed = 0.5f;
        	mc.thePlayer.setMoveSpeed(e, 2.5);
        	e.setY(mc.thePlayer.motionY = 0.17F);
        	 
            
       		
   	 }
       	 
      
       		

   		 
       	
       
        
        if (this.mode.getValue().equals("Negativity")) {
       	 if (mc.thePlayer.hurtTime > 6) {
       		 if (mc.gameSettings.keyBindJump.isKeyDown()) {
       	            mc.thePlayer.motionY = 3;
       	            
       	    } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
       	    	mc.thePlayer.motionY = -3;
       	        }
       		 	mc.thePlayer.motionY = -0.007;
       		 
       		 	mc.timer.timerSpeed = 0.7f;
       		 	mc.thePlayer.setMoveSpeed(e, 7);
       		 	
       	 }
       	 
       	 else if (mc.thePlayer.hurtTime > -1) {
       		 mc.timer.timerSpeed = 1.3f;
       		 mc.thePlayer.setMoveSpeed(e, 0.45);

   		 }
       	
       }
        
        if (this.mode.getValue().equals("MushMc")) {
          	
          		
          	            
          	            
          	  
          	    	
          	        
          		 	
          		 
        	mc.thePlayer.onGround = true;
        	mc.timer.timerSpeed = 1.4f;
     		 mc.thePlayer.setMoveSpeed(e, 2.4);	 
          		 	
          	 }
          	 
          	 
          		 
        if (this.mode.getValue().equals("Grim")) {
      	  if(mc.thePlayer.hurtTime > 1) {
      	  mc.thePlayer.setMoveSpeed(e, 20);
      	  mc.timer.timerSpeed = 0.1f;
      	  }
  		
  	}
      		 
          	
          
        
        //Negativity
        
          if (this.mode.getValue().equals("CubeCraft")) {
        	  
        	  mc.thePlayer.setMoveSpeed(e, 4);
        	  mc.timer.timerSpeed = 1.03f;
    		
    	}
          
          if (this.mode.getValue().equals("HycraftTnt")) {
        	  
        	  mc.thePlayer.setMoveSpeed(e, 6);
        	  mc.timer.timerSpeed = 1.03f;
    		
    	}
          
          if (this.mode.getValue().equals("Hylex")) {
        	  
        	 
        	  if(mc.thePlayer.hurtTime > 1) {
        		  mc.thePlayer.setMoveSpeed(e, 30);
        	  mc.thePlayer.motionY = -0.5;
        	  }
        	  
        	  if (mc.thePlayer.ticksExisted % 100 == 0) {
        		  mc.timer.timerSpeed = 1.0f;
        	  }
        	 
        	  
              
        	
        	  
        	 
              
        	 
    		
    	}
          
      	if (this.mode.getValue().equals("IntaveBoat")) {
      		 mc.thePlayer.setMoveSpeed(e, 0.4);
      		
      	}
      	
   
    
    
    
   ((TargetStrafe) ModuleManager.getModuleByClass(TargetStrafe.class)).strafe(e, 2);
    
  
    
    }
    
    
    @EventHandler
    public void onPacket(EventPacketRecieve e) {
    	
    	if (this.mode.getValue().equals("VulcanTimer")) {
    		
    		if(e.getPacket() instanceof S08PacketPlayerPosLook) {
    			Flags++;
    			
    			e.setCancelled(true);
    		}
    		
    		
    		
    		
    	}


		if (this.mode.getValue().equals("Invaded")) {

			if(e.getPacket() instanceof S08PacketPlayerPosLook) {
				Flags++;

				e.setCancelled(true);
			}




		}
    	if (this.mode.getValue().equals("OldNcp2")) {
    		
   		 if (e.getPacket() instanceof C00PacketKeepAlive) {
   			 mc.thePlayer.sendQueue.addToSendQueue(new C00PacketKeepAlive(Integer.MIN_VALUE + new Random().nextInt(100)));
                e.setCancelled(true);
            }else if (e.getPacket() instanceof S32PacketConfirmTransaction) {
                S32PacketConfirmTransaction packet = (S32PacketConfirmTransaction) e.getPacket();
                if (packet.getActionNumber() < 0) {
                    e.setCancelled(true);
                }
            }
        } 
		if (e.getPacket() instanceof S08PacketPlayerPosLook && this.mode.getValue().equals("OldNcp")) {
        	  this.setEnabled(false);
        		 NotificationsManager.addNotification(new Notification("Disabled do to lagback", Notification.Type.Alert,3));
        }
		
		if (e.getPacket() instanceof S08PacketPlayerPosLook && this.mode.getValue().equals("OldNcp2")) {
	      	  this.setEnabled(false);
	      		 NotificationsManager.addNotification(new Notification("Disabled do to lagback", Notification.Type.Alert,3));
	      }
        
   
    }
    
    
   
    
    

}
