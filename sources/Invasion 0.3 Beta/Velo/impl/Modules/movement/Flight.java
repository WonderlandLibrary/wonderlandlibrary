package Velo.impl.Modules.movement;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import Velo.api.Event.Event;
import Velo.api.Module.Module;
import Velo.api.Util.Other.BlockUtil;
import Velo.api.Util.Other.ChatUtil;
import Velo.api.Util.Other.MathUtils;
import Velo.api.Util.Other.MovementUtil;
import Velo.api.Util.Other.Timer;
import Velo.api.Util.Other.other.PlayerUtil;
import Velo.impl.Event.EventMovement;
import Velo.impl.Event.EventPostMotion;
import Velo.impl.Event.EventPreMotion;
import Velo.impl.Event.EventReceivePacket;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Event.EventStrafe;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Flight extends Module {
	
	boolean damageboosted = false;
	boolean boosted = false;
	public float tickselapsed = 0;
	public int ticks = 0;
	public float ticks2 = 0;
	boolean backWards = false;
	public Timer mTimer = new Timer();
	   private Vec3 lastPos;
	    private boolean back, done;
	    boolean down;
	   private boolean tp;
		public float ticksElapsed = 0;
	public static List<C03PacketPlayer> incomingpackets = new ArrayList<C03PacketPlayer>();
	public double posX = 0, posY = 0, posZ = 0;
	public boolean verusDamaged = false;
	public static NumberSetting hypixelFreecamHorizontalFlySpeed = new NumberSetting("Horizontal Speed", 5.6, 2, 18, 0.2);
	public static NumberSetting hypixelFreecamVerticalFlySpeed = new NumberSetting("Vertical Speed", 0.4, 0.2, 1, 0.01);
	public static ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Redesky", "Mineplex", "SmoothVanilla", "OldNCP Custom", "Funcraft", "Survivaldub", "KeepAlive", "Boat Flight", "Bow Flight", "Verus", "AAC5");
	public static NumberSetting speed = new NumberSetting("Speed", 3, 0.01, 10.1, 0.01);
	public static NumberSetting blinkamount = new NumberSetting("AAC Blink Amount", 15, 1, 20, 1);
	public static BooleanSetting hypixelUseFireball = new BooleanSetting("Fireball disabler", false);
	public static BooleanSetting hypixelUsePearl = new BooleanSetting("Pearl disabler", true);
	public static BooleanSetting ViewBobbing = new BooleanSetting("ViewBobbing", true);
	public static BooleanSetting oldncpboost = new BooleanSetting("OldNCP SpeedBoost", true);
	public static BooleanSetting oldncptimerboost = new BooleanSetting("OldNCP TimerBoost", true);
	public static BooleanSetting oldncpdamage = new BooleanSetting("OldNCP Damage", true);
	public static BooleanSetting oldncpblink = new BooleanSetting("OldNCP Blink", false);
	public static NumberSetting oldncpspeed = new NumberSetting("OldNCP Speed", 1.3, 0.3, 3, 0.1);
	public static NumberSetting oldncptimer = new NumberSetting("OldNCP Timer", 1.0, 1.0, 6.0, 0.05);
	public static NumberSetting oldncpblinkticks = new NumberSetting("OldNCP Blink TicksExisted", 10, 1, 40, 1);
	public static NumberSetting oldncpfricition = new NumberSetting("OldNCP Fricition", 0.01, 0.005, 0.02, 0.001);
	
	public static float mineplexSpeed;
	public static BooleanSetting aacdebug = new BooleanSetting("AACDebug", true);
	public Flight() {
		super("Flight", "Flight", Keyboard.KEY_F, Category.MOVEMENT);
		this.loadSettings(mode, speed, blinkamount, hypixelFreecamHorizontalFlySpeed, hypixelFreecamVerticalFlySpeed, ViewBobbing, aacdebug, oldncpboost,
				oldncptimerboost, oldncpdamage, oldncpblink, oldncpspeed, oldncptimer, oldncpblinkticks, oldncpfricition);
	}
	
	public Timer time = new Timer();
	public void onEnable() {
		
		
		this.tp = false;
		time.reset2();
		mineplexSpeed = speed.getValueFloat();
		mTimer.reset1();
		
		if(mode.equalsIgnorecase("Funcraft")) {
			for(int i = 0; i < 64; i++) {
				//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.049, mc.thePlayer.posZ, false));
				//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
			}
			
			for(int i = 0; i < 3; i++) {
				//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.01, mc.thePlayer.posZ, false));
				//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
			}
			
			//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
			
		}
		if(mode.equalsIgnorecase("Survivaldub")) {
			for(int i = 0; i < 64; i++) {
				//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.049, mc.thePlayer.posZ, false));
				//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
			}
			
			for(int i = 0; i < 3; i++) {
				//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.01, mc.thePlayer.posZ, false));
				//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
			}
			
			//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
			
		}
		
		if(mode.equalsIgnorecase("OldNCP Custom")) {
			ticksElapsed = 0;
			if(oldncpdamage.isEnabled()) {
				for(int i = 0; i < 64; i++) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.049, mc.thePlayer.posZ, false));
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
				}
				
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
			}
		}
		
		if(mode.equalsIgnorecase("Funcraft")) {
			ticks = 0;
			ticksElapsed = 0F;
		}
		
		if(mode.equalsIgnorecase("Redesky")) {
	    	  //  mc.thePlayer.sendQueue.addToSendQueue(new S12PacketEntityVelocity(mc.thePlayer.getEntityId(), 0, 0, 0));
	    //	    mc.thePlayer.sendQueue.addToSendQueue(new S27PacketExplosion());
				//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 1.0E10, mc.thePlayer.posZ, true));
				//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 10, mc.thePlayer.posZ, false));
				//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
		}
		
		posY = mc.thePlayer.posY;
		if(mode.equalsIgnorecase("Vanilla")) {
			
			mc.thePlayer.motionY = 0;
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
            //mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ);
            
			//this.mineplexDamage(mc.thePlayer);
		
			mTimer.reset1();
			
		}
		posY = mc.thePlayer.posY;
		mTimer.reset();
		if(mode.equalsIgnorecase("Mineplex")) {
			
		}
		if(mode.equalsIgnorecase("Bow Flight")) {

	        done = false;
	        back = false;
	        down = false;

			
			 
		}
		
		if(mode.equalsIgnorecase("Verus")) {
	    	 if(mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, 3.0001, 0).expand(0, 0, 0)).isEmpty()) {
	    		 this.verusDamaged = true;
	             mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.45, mc.thePlayer.posZ, false));
	             mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
	             mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
	             this.verusDamaged = false;
	    	 }
	
	    	 
	    	 
	    	 mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ);
			 }
		
	time.reset1();
	}
	
	public void onDisable() {
		mc.timer.ticksPerSecond = 20F;
time.reset();
		if(mode.equalsIgnorecase("Boat Flight")) {
			//mc.thePlayer.setPosition(mc.thePlayer.riddenByEntity.posX, mc.thePlayer.riddenByEntity.posY, mc.thePlayer.riddenByEntity.posZ);
		}
		posX = 0;
		posY = 0;
		posZ = 0;
		mc.timer.timerSpeed = 1.0F;
		boosted = false;
		tickselapsed = 0;
		damageboosted = false;
		mc.thePlayer.speedInAir = 0.02f;;
		mc.thePlayer.speedInAir = 0.02f;
		ticks = 0;
		mc.thePlayer.capabilities.isFlying = false;
		
		if(mode.equalsIgnorecase("Funcraft")) {
			ticksElapsed = 0F;
		}
		
		if(mode.equalsIgnorecase("Mineplex")) {
			ticksElapsed = 0.0F;
			ticks = 1;
			ticks2 = 0;
		}
		
		if(aacdebug.isEnabled()) {
			if(!incomingpackets.isEmpty()) {
				ChatUtil.addChatMessage("C03's: " + incomingpackets.toArray().length);
			}
		}
		
		if(mode.equalsIgnorecase("AAC5")) {
			for(C03PacketPlayer p : incomingpackets) {
				mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
				mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(((C03PacketPlayer) p).getPositionX(), 1.04E208, ((C03PacketPlayer) p).getPositionZ(), true));
				mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
			}
			incomingpackets.clear();
		}
		
		if(mode.equalsIgnorecase("OldNCP Custom")) {
			if(oldncpblink.isEnabled()) {
				for(C03PacketPlayer p : incomingpackets) {
					mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
					mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(((C03PacketPlayer) p).getPositionX(), 1.04E208, ((C03PacketPlayer) p).getPositionZ(), true));
					mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
				}
				incomingpackets.clear();
			}
		}
		
       mc.thePlayer.setSpeed(0);
        mc.timer.timerSpeed = 1.0F;
        
        if(mode.equalsIgnorecase("AAC5")) {
			//mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
			//mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 1.04E250, mc.thePlayer.posZ, true));
			//mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        }
        
		//mc.thePlayer.sendQueue.addToSendQueue(new C00Handshake(47, "eu.loyisa.cn", 25565, EnumConnectionState.LOGIN));
		//mc.thePlayer.sendQueue.addToSendQueue(new C00PacketLoginStart(AltLoginThread.createSession("Miserasu", "myfly7").getProfile()));
	}
	
	
	@Override
	public void onStrafe(EventStrafe event) {
	     if(mode.equalsIgnorecase("Bow Flight")) {
	    	 event.setStrafe(0);
	    	 event.setForward(0);
	     }
		super.onStrafe(event);
	}
	
	
	@Override
	public void onPostMotionUpdate(EventPostMotion event) {
		if(mode.equalsIgnorecase("Vanilla")) {
			
		}
//ChatUtil.addChatMessage3(mTimer.getElapsedTime() + " Milli Seconds Elasped");
		super.onPostMotionUpdate(event);
	}
	
	
	@Override
	public void onPreMotionUpdate(EventPreMotion event) {
		if(ViewBobbing.isEnabled()) {
			mc.thePlayer.cameraYaw = 0.09F;
		}
        if(mode.equalsIgnorecase("Bow Flight")) {
        	event.setPitch(-90);
		
			 
        }
        
		if(mode.equalsIgnorecase("OldNCP Custom")) {
	        double diffX = mc.thePlayer.posX - posX;
	        double diffY = mc.thePlayer.posY - posY;
	        double diffZ = mc.thePlayer.posZ - posZ;
	        
	        double dist = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
			if(mc.thePlayer.onGround && ticks > 2) {
				mc.thePlayer.setSpeed(0);
				this.toggle();
			}
			
			if(mc.thePlayer.hurtTime > 0) {
				boosted = true;
				//ticksElapsed = 0;
			}
			if(mc.thePlayer.isMoving()) {
				ticks++;
				if(mc.thePlayer.onGround) {
					mc.thePlayer.jump();
					ticksElapsed = oldncpspeed.getValueFloat();
				} else {
					if(ticksElapsed > 0.2) {
						ticksElapsed *= 0.993;
					}
					if(oldncptimerboost.isEnabled()) {
						mc.timer.timerSpeed = oldncptimer.getValueFloat();
					}
					mc.thePlayer.motionY = 0;
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.0E-10, mc.thePlayer.posZ);
					
					if(oldncpboost.isEnabled()) {
						mc.thePlayer.setSpeed(ticksElapsed);
					} else {
						mc.thePlayer.setSpeed(mc.thePlayer.getCurrentMotion());
					}
					
					if(oldncpblink.isEnabled()) {
						if(mc.thePlayer.ticksExisted % oldncpblinkticks.getValue() == 0) {
							for(C03PacketPlayer c03 : incomingpackets) {
								mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(c03);
							}
							incomingpackets.clear();
						}
					}
				}
			}
		}
        
		if(mode.equalsIgnorecase("Mineplex")) {
			
	    }
		super.onPreMotionUpdate(event);
	}
	
	
	public void onUpdate(EventUpdate e) {
		this.setDisplayName("Flight " + mode.modes.get(mode.index));
		if(ViewBobbing.isEnabled()) {
			//mc.thePlayer.onGround = true;
			//mc.entityRenderer.setupViewBobbing(0);
		}
		if(mode.equalsIgnorecase("SmoothVanilla")) {
			mc.thePlayer.capabilities.isFlying = true;
		}
		
		
        if(mode.equalsIgnorecase("Redesky")) {
        	mc.thePlayer.jumpMovementFactor = 0.09F;
        	if(ticks < 10) {
        		mc.timer.timerSpeed = 1F;
        		ticks++;
        		mc.thePlayer.motionY = 1F;
        	} else {
        		mc.timer.timerSpeed = 1F;
        	}
        }
		
		if(this.mode.equalsIgnorecase("Verus")) {
      	  mc.thePlayer.motionX = 0;
          mc.thePlayer.motionY = 0;
          mc.thePlayer.motionZ = 0;
          mc.thePlayer.speedInAir = 0.25f;
		}
		
        if(mode.equalsIgnorecase("Bow Flight")) {
        	
		
			 if(mTimer.elapsed(250)) {
	 			 mc.gameSettings.keyBindUseItem.pressed = true;
}
if(mTimer.elapsed(425)) {
	 mc.gameSettings.keyBindUseItem.pressed = false;
	
	 
	
}

if(mTimer.elapsed(3000)) {

}


if(mc.thePlayer.hurtTime == 9f) {
	mc.thePlayer.cameraYaw = 2.5f;
//	mc.thePlayer.jumpMovementFactor = 0.408f;
	//mc.thePlayer.jump();

	mc.thePlayer.motionY += 01.272;
	mc.gameSettings.keyBindForward.pressed = true;
//	mc.thePlayer.sendQueue.addToSendQueue(new C00PacketKeepAlive());
	if(mc.thePlayer.onGround) {
		mc.gameSettings.keyBindForward.pressed = false;
	}else {
		ticksElapsed += 0.01F;
	//	mc.thePlayer.setSpeed(0.9f - ticksElapsed);
	}
	


	//mc.gameSettings.keyBindForward.pressed = true;
	
	//mc.thePlayer.speedInAir = 0.17f;
}

if(mc.thePlayer.motionY >= -.47) {
	mc.thePlayer.motionX *= 1.19;
	mc.thePlayer.motionZ *= 1.19;

	
}

if(mc.thePlayer.hurtTime == 6f) {

//	mc.thePlayer.motionY = 0.872;
	//mc.gameSettings.keyBindForward.pressed = true;

}
if(mc.thePlayer.hurtTime == 5f) {
	
	//mc.thePlayer.motionY = 0.872;
	//mc.gameSettings.keyBindForward.pressed = true;

}
if(mc.thePlayer.hurtTime == 4f) {
	
	//mc.thePlayer.motionY = 0.872;
	//mc.gameSettings.keyBindForward.pressed = true;

}
if(mc.thePlayer.hurtTime <= 2f && mc.thePlayer.hurtTime != 0) {

	//mc.thePlayer.motionY = 0.872;
	//mc.gameSettings.keyBindForward.pressed = true;
	
}
if(mc.thePlayer.hurtTime == 2f) {

//	mc.thePlayer.motionY = 0.872;
	//mc.gameSettings.keyBindForward.pressed = true;
	
}
if(mc.thePlayer.hurtTime == 8f) {

//	mc.thePlayer.motionY += 0.872;
	//mc.gameSettings.keyBindForward.pressed = true;

}
if(mc.thePlayer.hurtTime >= 5f) {
	//mc.thePlayer.motionX *= 1.02;
	//mc.thePlayer.motionZ *= 1.02;

	//mc.thePlayer.jumpMovementFactor = 0.178f;
	//mc.gameSettings.keyBindForward.pressed = true;
	//mc.thePlayer.motionY += 0.02;
//	mc.thePlayer.speedInAir = 0.02f;


}

if(mc.thePlayer.hurtTime == 0.0f) {
	mc.thePlayer.motionZ *= 1;


	mc.thePlayer.motionX *= 1;
	mc.thePlayer.capabilities.isFlying = false;
	mc.timer.timerSpeed = 1;
//	mc.thePlayer.motionY += 0.2;
//	mc.thePlayer.speedInAir = 0.02f;
}
if(mTimer.elapsed(1050)) {
	double x = mc.thePlayer.posX - mc.thePlayer.lastTickPosX;
	double y = mc.thePlayer.posY - mc.thePlayer.lastTickPosY;
	double z = mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ;
//	ChatUtil.addChatMessage3( "Moved X Cords By: " + x + " Moved Y Cords By: " +y +  " Moved Z Cords By: "  +  z  + "");
}
if(mTimer.elapsed(7621)) {
mc.gameSettings.keyBindForward.pressed = false;

	mTimer.reset1();
	this.isToggled = false;
}
        }
		if(mode.equalsIgnorecase("Mineplex")) {
			
		
if(!mc.thePlayer.onGround) {

}else {
	
}
		}
		if(mode.equalsIgnorecase("Vanilla")) {
			mc.timer.timerSpeed = 1F;
			mc.thePlayer.motionY = 0;
			if(mc.gameSettings.keyBindJump.pressed) {
				mc.thePlayer.motionY += speed.getValue();
			}
			if(mc.gameSettings.keyBindSneak.pressed) {
				mc.thePlayer.motionY -= speed.getValue();
			}
			if(mc.thePlayer.isMoving()) {
				mc.thePlayer.setSpeed(speed.getValue());
			} else {
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
			}
		}
		if(mode.equalsIgnorecase("AAC5")) {
			mc.timer.timerSpeed = 1F;
			mc.thePlayer.motionY = 0;
			//mc.thePlayer.onGround = true;
			
			mc.thePlayer.cameraYaw = 0;
			
			if(!incomingpackets.isEmpty()) {
				for(C03PacketPlayer p : incomingpackets) {
					if(mc.thePlayer.ticksExisted % blinkamount.getValue() == 0) {
						mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
						mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.lastTickPosX, 1.04E208, mc.thePlayer.lastTickPosZ, true));
						mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
					}
				}
				if(mc.thePlayer.ticksExisted % blinkamount.getValue() == 0) {
					incomingpackets.clear();
				}
			}
			
			if(mc.gameSettings.keyBindJump.pressed) {
				mc.thePlayer.motionY += speed.getValue();
			}
			if(mc.gameSettings.keyBindSneak.pressed) {
				mc.thePlayer.motionY -= speed.getValue();
			}
			if(mc.thePlayer.isMoving()) {
				mc.thePlayer.setSpeed(speed.getValue());
			} else {
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
			}
		}
		
		if(mode.equalsIgnorecase("Boat Flight")) {
			if(mc.thePlayer.ridingEntity instanceof EntityBoat && mc.thePlayer.isRiding()) {
				
				Entity vehicle = mc.thePlayer.ridingEntity;
		
				
				mc.thePlayer.ridingEntity.motionY = 0;
			//	mc.thePlayer.ridingEntity.motionX *= 5.5;
			//	mc.thePlayer.ridingEntity.motionZ *= 5.5;
			//	mc.thePlayer.ridingEntity.ticksExisted = 100;
			float mtx = (float) (mc.thePlayer.ridingEntity.motionX *= 5.5);
			float mtz = (float) (mc.thePlayer.ridingEntity.motionZ *= 5.5);	
				//mc.thePlayer.ridingEntity.inWater = true;
				
		
			
	
				vehicle.setVelocity(mtx, 0, mtz);
				//mc.thePlayer.ridingEntity.setPosition(mc.thePlayer.ridingEntity.posX *= 1.2, mc.thePlayer.ridingEntity.posY, mc.thePlayer.ridingEntity.posZ *= 1.2);
		//	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.ridingEntity.posX * 1.4, mc.thePlayer.ridingEntity.posY, mc.thePlayer.ridingEntity.posZ * 1.4, false));
				if(mc.gameSettings.keyBindJump.isKeyDown()) {
					mc.thePlayer.ridingEntity.motionY = 0.47f;
				}
			}
		}
		if(mode.equalsIgnorecase("Funcraft")) {
			ticks++;
			if(ticks < 2) {
				mc.thePlayer.jump();
			} else {
	            mc.thePlayer.motionY = 0;
			}
			
			if(mc.thePlayer.isMoving()) {
				//mc.thePlayer.onGround = true;
				if(ticksElapsed < 1.1) {
					ticksElapsed += 0.02F;
				}
				mc.thePlayer.setSpeed(1.3 - ticksElapsed);
			} else {
				mc.thePlayer.setSpeed(0);
			}
			
			mc.timer.timerSpeed = 1.05F;
			//mc.thePlayer.setSpeed(0.5-ticksElapsed);
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.0E-10, mc.thePlayer.posZ);
		}
		if(mode.equalsIgnorecase("Survivaldub")) {
			ticks++;
			if(ticks < 10) {
				ticksElapsed += 0.02F;
				//mc.thePlayer.setSpeed(0.7-ticksElapsed);
			}
			if(ticks < 2) {
				mc.thePlayer.jump();
			} else {
				mc.timer.timerSpeed = 1.05F;
				double direction = mc.thePlayer.getDirection();
				double speed = 1.0;
				if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
					if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
						speed = 1.0 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.015 : 0);
					}
					if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
						speed = 1.0 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.025 : 0);
					}
					if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 2) {
						speed = 1.0 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.03 : 0);
					}
				}
				mc.thePlayer.setSpeed(mc.thePlayer.getCurrentMotion() * speed);
				mc.thePlayer.onGround = false;
	            mc.thePlayer.motionY = 0;
			}
			 mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.0E-10, mc.thePlayer.posZ);
			
			//mc.thePlayer.setSpeed(0.5-ticksElapsed);
		}
		if(mode.equalsIgnorecase("Cubecraft")) {
			//mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(-10)));
			mc.timer.timerSpeed = 0.6F;
			mc.thePlayer.capabilities.isFlying = false;
			mc.thePlayer.motionY = 0;
			mc.thePlayer.prevCameraYaw = 0.09F;
			if(mc.gameSettings.keyBindJump.pressed) {
				mc.thePlayer.motionY += speed.getValue();
			}
			if(mc.gameSettings.keyBindSneak.pressed) {
				mc.thePlayer.motionY -= speed.getValue();
			}
			if(mc.thePlayer.isMoving()) {
				mc.thePlayer.setSpeed(speed.getValue());

			} else {
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
			}
			if((mc.thePlayer.ticksExisted % 15 == 0)) {
				
			}
			
		}
		
		
		if(mode.equalsIgnorecase("KeepAlive")) {
            mc.getNetHandler().addToSendQueue(new C00PacketKeepAlive());
            mc.thePlayer.capabilities.isFlying = false;
            	mc.thePlayer.motionY = 0.0;
            	mc.thePlayer.motionX = 0.0;
            	mc.thePlayer.motionZ = 0.0;
            if (mc.gameSettings.keyBindJump.isKeyDown()) { 
            	mc.thePlayer.motionY += 2f;
            		
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            	mc.thePlayer.motionY -= 2f;
            }
            this.strafe(5f);
		
	}
	}
	
	
	
	public void onMovementUpdate(EventMovement event) {
		if(mode.equalsIgnorecase("Funcraft")) {
			
			

		}
		

		
		
		if(mode.equalsIgnorecase("Boat Flight")) {
			if(mc.thePlayer.ridingEntity instanceof EntityBoat && mc.thePlayer.isRiding()) {
	
			
	

				
				//mc.thePlayer.ridingEntity.setPosition(mc.thePlayer.ridingEntity.posX *= 1.2, mc.thePlayer.ridingEntity.posY, mc.thePlayer.ridingEntity.posZ *= 1.2);

			}
		}
	}
	
	
	@Override
	public void onEventSendPacket(EventSendPacket event) {
		if(mode.equalsIgnorecase("Verus")) {
		  	   if (event.getPacket() instanceof C03PacketPlayer && !this.verusDamaged) {
	               	C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
	                c03.onGround = true;
	               } 
		}
		if(mode.equalsIgnorecase("Funcraft")) {
			if(mc.thePlayer != null) {
				if(event.getPacket() instanceof C03PacketPlayer) {
			  		if(!(mc.thePlayer.ticksExisted % 15 == 0)) {
			  			//event.setCancelled(true);
			  		}
				}
			}
		}
		if(mode.equalsIgnorecase("Vanilla")) {
			if(mc.thePlayer != null) {
				if(event.getPacket() instanceof C03PacketPlayer) {
					
				}
			}
		}
		if(mode.equalsIgnorecase("AAC5")) {
			if(mc.thePlayer != null) {
				if(event.getPacket() instanceof C03PacketPlayer) {
					C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
					c03.onGround = true;
					incomingpackets.add(c03);
					event.setCancelled(true);
				}
			}
		}
		if(mode.equalsIgnorecase("OldNCP Custom")) {
			if(mc.thePlayer != null) {
				if(oldncpblink.isEnabled()) {
					if(boosted) {
						if(event.getPacket() instanceof C03PacketPlayer) {
							C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
							incomingpackets.add(c03);
							event.setCancelled(true);
						}
					}
				}
			}
		}
		if(mode.equalsIgnorecase("Redesky")) {
			if(mc.thePlayer != null) {
				if(event.getPacket() instanceof C03PacketPlayer) {
					
				}
			}
		}
		if(mode.equalsIgnorecase("Cubecraft")) {
			if(mc.thePlayer != null) {
		  		if(!(mc.thePlayer.ticksExisted % 15 == 0)) {
		  			if(event.getPacket() instanceof C03PacketPlayer) {
		  				C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
		  				event.setCancelled(true);
		  			}
		  		}
			}
		}
		
		

		super.onEventSendPacket(event);
	}
	public void onEventReceivePacket(EventReceivePacket event) {
		if(event.getPacket() instanceof S08PacketPlayerPosLook) {
			if(mode.equalsIgnorecase("Funcraft")) {
				boosted = true;
			}
			if(mode.equalsIgnorecase("Redesky")) {
				
			}
			if(mode.equalsIgnorecase("AAC5")) {
				S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook) event.getPacket();
				//s08.x = mc.thePlayer.posX;
				//s08.z = mc.thePlayer.posZ;
			}
		}
		if(mode.equalsIgnorecase("Redesky")) {
			if(mc.thePlayer != null) {
			  	if (!(event.getPacket() instanceof S1CPacketEntityMetadata) && !(event.getPacket() instanceof S34PacketMaps)) {
			  		//ChatUtil.addChatMessage(event.getPacket() + "");
		        }
			  	if ((event.getPacket() instanceof S14PacketEntity.S15PacketEntityRelMove)) {
			  		S14PacketEntity.S15PacketEntityRelMove packet = (S14PacketEntity.S15PacketEntityRelMove) event.getPacket();
			  		event.setCancelled(true);
		        }
			  	if ((event.getPacket() instanceof S08PacketPlayerPosLook)) {
			  		S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
			  		
			  		
		        }
			  	if ((event.getPacket() instanceof S12PacketEntityVelocity)) {
			  		event.setCancelled(true);
		        }
			}
		}
		if(mode.equalsIgnorecase("")) {
			if(mc.thePlayer != null) {
			  	if (!(event.getPacket() instanceof S1CPacketEntityMetadata) && !(event.getPacket() instanceof S34PacketMaps)) {
			  		//ChatUtil.addChatMessage(event.getPacket() + "");
		        }
			  	if ((event.getPacket() instanceof S14PacketEntity.S15PacketEntityRelMove)) {
			  		S14PacketEntity.S15PacketEntityRelMove packet = (S14PacketEntity.S15PacketEntityRelMove) event.getPacket();
			  		event.setCancelled(true);
		        }
			  	if ((event.getPacket() instanceof S08PacketPlayerPosLook)) {
			  		S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
			  		
			  		
		        }
			  	if ((event.getPacket() instanceof S12PacketEntityVelocity)) {
			  		event.setCancelled(true);
		        }
			}
		}
		
		if(mode.equalsIgnorecase("Mineplex")) {
		//	mc.getNetHandler().addToSendQueue(new C18PacketSpectate());
			//2mc.getNetHandler().addToSendQueue(new C00PacketKeepAlive(MathUtils.customRandInt(1, 10000)));
			//mc.getNetHandler().addToSendQueue(new C00PacketKeepAlive(MathUtils.customRandInt(10000, 100000)));
	//		mc.getNetHandler().addToSendQueue(new C00PacketKeepAlive(MathUtils.customRandInt(100000, 10000000)));
	//		mc.getNetHandler().addToSendQueue(new C00PacketKeepAlive(MathUtils.customRandInt(10000000, 1000000000)));
			
			
		}
	}
	
	public boolean isMoving() {
		return  mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0f || mc.thePlayer.movementInput.moveStrafe != 0f);
	}
	
    public void strafe(Float speed) {
        if (!isMoving()) 
        	return;
        		
        Double yaw = this.direction();
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    public Double direction() {

    	double rotationYaw = (double) mc.thePlayer.rotationYaw;
		if (mc.thePlayer.moveForward < 0f)
			rotationYaw += 180f;
		float forward = 1f;
		if (mc.thePlayer.moveForward < 0f) {
			forward = -0.5f;
		} else if (mc.thePlayer.moveForward > 0f) {
			forward = 0.5f;
		}
		if (mc.thePlayer.moveStrafing > 0f)
			rotationYaw -= 90f * forward;
		if (mc.thePlayer.moveStrafing < 0f)
			rotationYaw += 90f * forward;
		return Math.toRadians(rotationYaw);
	}

	private void mineplexDamage(EntityPlayerSP playerRef) {
		if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
				mc.thePlayer.getEntityBoundingBox().offset(0, 3.0001, 0).expand(0, 0, 0)).isEmpty()) {

			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY + 3.45, mc.thePlayer.posZ, false));
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY, mc.thePlayer.posZ, false));
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY, mc.thePlayer.posZ, true));

		}
	}

	public static int airSlot() {
		for (int j = 0; j < 8; ++j) {
			if (Minecraft.getMinecraft().thePlayer.inventory.mainInventory[j] == null) {
				return j;
			}
		}
		// ChatUtil.printChat("Clear a hotbar slot.");
		return -10;
	}

}

