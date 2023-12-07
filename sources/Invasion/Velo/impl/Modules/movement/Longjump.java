package Velo.impl.Modules.movement;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Module.ModuleManager;
import Velo.api.Util.Other.ChatUtil;
import Velo.api.Util.Other.MovementUtil;
import Velo.api.Util.Other.Timer;
import Velo.api.Util.Other.other.PlayerUtil;
import Velo.impl.Event.EventMovement;
import Velo.impl.Event.EventPostMotion;
import Velo.impl.Event.EventPreMotion;
import Velo.impl.Event.EventReceivePacket;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.ModeSetting;
import Velo.modules.impl.movement.Ninja;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;

public class Longjump extends Module {
	public static boolean jumped = false;
	public static ModeSetting mode = new ModeSetting("Mode", "RedeskyFar", "RedeskyFar", "Hypixel" ,"Mineplex", "Redesky", "DamageNcp", "Ncp", "Dev", "AAC5");
	public static BooleanSetting aacdebug = new BooleanSetting("AACDebug", true);
    public int stage, groundTicks, slot;
    public double moveSpeed, air, motionY;
    public boolean done, back, collided, half, damaged, shoot;
	double speed = 0, currentDistance = 0, lastDistance = 0;
	boolean prevOnGround = false;
	float ticks = 0;
	float ticks2 = 0;
	public double posX = 0, posY = 0, posZ = 0;
	public float ticksElapsed = 0;
	public Timer timer = new Timer();
	
	public static List<C03PacketPlayer> incomingpackets = new ArrayList<C03PacketPlayer>();
	
	public Longjump() {
		super("Longjump", "Longjump", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.loadSettings(mode);
	}
	
	public void onEnable() {
		//mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(-1));
        this.moveSpeed = MovementUtil.getBaseMoveSpeed() * (mc.thePlayer.isPotionActive( Potion.moveSpeed) ? 1.0D : 1.34D);
        stage = 0;
        done = false;
        back = false;
        damaged = false;
        shoot = false;
		if(mode.equalsIgnorecase("DamageNcp")) {
			for(int i = 0; i < 64; i++) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.049, mc.thePlayer.posZ, false));
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
			}
			
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
		}
		if(mode.equalsIgnorecase("AAC5")) {
			
		}
		ticks = 0;
	ticks2 = 0;
		timer.reset();
		jumped = false;
		posX = mc.thePlayer.posX;
		posY = mc.thePlayer.posY;
		posZ = mc.thePlayer.posZ;
	}
	
	public void onDisable() {
		
		   stage = 0;
	        this.groundTicks = 0;
	        this.stage = 0;
	        this.motionY = 0.0D;
	        damaged = false;
	        
		if(mode.equalsIgnorecase("Ncp")) {
			//mc.thePlayer.setSpeed(0);
		}
		if(mode.equalsIgnorecase("AAC5")) {
			speed = 0;
			for(C03PacketPlayer p : incomingpackets) {
				mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
				mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(((C03PacketPlayer) p).getPositionX(), 1.04E208, ((C03PacketPlayer) p).getPositionZ(), true));
				mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
			}
			incomingpackets.clear();
		}
		ticksElapsed = 0;
		posX = 0;
		posY = 0;
		posZ = 0;
		if(mode.equalsIgnorecase("Redesky")) {
			mc.thePlayer.setSpeed(0);
		}
		ticks = 0;
		if(mode.equalsIgnorecase("Mineplex")) {
			ticks = 0.4F;
		}
		ticks2 = 0;
		mc.timer.timerSpeed = 1.0F;
		mc.thePlayer.speedInAir = 0.02f;
	}
	
	public void onUpdate(EventUpdate event) {
		this.setDisplayName("Longjump " + mode.modes.get(mode.index));
		if(mode.equalsIgnorecase("RedeskyFar")) {
			if(mc.thePlayer.isInWater() || mc.thePlayer.isOnLadder() && mc.thePlayer != null) {
				mc.thePlayer.motionY = 1F;
				//mc.thePlayer.setSpeed(3);
				if(!mc.thePlayer.onGround) {
					//mc.thePlayer.motionY = 9F;
				}
			}
		}
		if(mode.equalsIgnorecase("Redesky")) {
			mc.timer.timerSpeed = 1F;
			mc.thePlayer.capabilities.isFlying = false;
			mc.thePlayer.motionY = 0;
			if(mc.thePlayer.ticksExisted % 3 == 0) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + -Math.sin(mc.thePlayer.getDirection()) * 1, 1.0E-10, mc.thePlayer.posZ + Math.cos(mc.thePlayer.getDirection()) * 1, false));
			}
			if(mc.gameSettings.keyBindJump.pressed) {
				mc.thePlayer.motionY += 7F;
			}
			if(mc.gameSettings.keyBindSneak.pressed) {
				mc.thePlayer.motionY -= 7F;
			}
			if(mc.thePlayer.isMoving()) {
				mc.thePlayer.setSpeed(3);
			}
		}
		if(mode.equalsIgnorecase("Mineplex")) {
			if(mc.thePlayer.isMoving()) {
				if(mc.thePlayer.onGround) {
					mc.timer.timerSpeed = 5;
					prevOnGround = true;
					//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.posY, mc.thePlayer.lastTickPosZ, true));
					mc.thePlayer.setSpeed(0);
					mc.thePlayer.motionY = 0.42F;
					if(ticks < 1.25) {
						//mc.thePlayer.motionY = 0.42F;
					} else {
						//mc.thePlayer.motionY = 1.5F;
					}
					ticks2 = 0.0F;
				} else {
					ticks2 += 0.02F;
					if(prevOnGround) {
						speed = 0;
						if(ticks < 2.0) {
							//mc.thePlayer.motionY -= 0.05F;
							ticks += 0.25;
						} else {
							
						}
						prevOnGround = false;
					}
					if(speed < 10) {
						speed += 1;
						//mc.thePlayer.motionY = -0;
					}
					
					if(ticks > 2.01) {
						//mc.thePlayer.motionY -= 0.05F;
						ticks = 2.01F;
					}
					
					mc.timer.timerSpeed = 1F;
					
					if(mc.thePlayer.ticksExisted % 1 == 0) {
						back = !back;
					}
					
					if(ticks < 2.0) {
						mc.thePlayer.motionY -= 0.08000000000001D;
						mc.timer.timerSpeed = 1.4F;
						mc.thePlayer.setSpeed(back ? (ticks-ticks2) : ((-ticks-0.06)+ticks2));
					} else {
						mc.timer.timerSpeed = 1.0F;
						if(mc.thePlayer.fallDistance < 0.4) {
							mc.thePlayer.motionY += 0.035F;
						}
						mc.thePlayer.motionY = 0.1F;
						//mc.thePlayer.motionY = 0;
						mc.thePlayer.setSpeed(ticks*0.979-ticks2);
					}
				}
			} else {
				ticks = 0.4F;
			}
		}
		
		if(mode.equalsIgnorecase("AAC5")) {
			speed += 1;
			mc.timer.timerSpeed = 1F;
			if(speed > 1) {
				mc.thePlayer.motionY = 0F;
			}
			if(aacdebug.isEnabled()) {
				if(!incomingpackets.isEmpty()) {
					ChatUtil.addChatMessage("C03's: " + incomingpackets.toArray().length);
				}
			}
			//mc.thePlayer.onGround = true;
			
			mc.thePlayer.cameraYaw = 0;
			
			if(!incomingpackets.isEmpty()) {
				for(C03PacketPlayer p : incomingpackets) {
					if(mc.thePlayer.ticksExisted % 10 == 0) {
						mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
						mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.lastTickPosX, 1.04E208, mc.thePlayer.lastTickPosZ, true));
						mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
					}
				}
				if(mc.thePlayer.ticksExisted % 10 == 0) {
					incomingpackets.clear();
				}
			}
			if(mc.thePlayer.isMoving()) {
				mc.thePlayer.setSpeed(2.5);
			} else {
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
			}
		}
		
		if(mode.equalsIgnorecase("Mineplex")) {
			
		}
		
		if(mode.equalsIgnorecase("Redesky")) {
	
		}
		
		if(mode.equalsIgnorecase("Hypixel")) {
            if (this.mc.thePlayer.onGround && mc.thePlayer.isMoving()) {
                this.mc.thePlayer.jump();
                this.mc.thePlayer.motionY += 0.1D;
                this.speed = 0.65D;
            } else {
           
                this.speed *= 0.992D;
            }
            MovementUtil.actualSetSpeed(this.speed + 2.5f);
            if (timer.hasTimedElapsed(98l, true)) {
            	this.speed = 2.5f;;
                this.mc.thePlayer.motionY = -0.272D;
            }else {
                this.mc.thePlayer.motionY = 0.222D;
            	speed = 0;
            }
            if (timer.hasTimedElapsed(25l, true)) {
            	this.speed = -2.5f;;
               // this.mc.thePlayer.motionY = -0.272D;
            }
            
            
            
            if(timer.hasTimedElapsed(50l, true)){
            	  this.mc.thePlayer.motionY = -0.272D;
               	this.speed = 2.5f;;
            }
  
            if (this.mc.thePlayer.onGround) {
            	MovementUtil.actualSetSpeed(0.0D);
            }
		}
	}
	
	public void onPreMotionUpdate(EventPreMotion event) {
		
		
		if(mode.equalsIgnorecase("Dev")) {
			   if (!mc.thePlayer.onGround && air > 0.0F) {
                   air++;
                   if (mc.thePlayer.isCollidedVertically)
                       air = 0.0F;
                   if (mc.thePlayer.isCollidedHorizontally && !collided)
                       collided = !collided;
                   double speed = half ? (0.5D - (air / 100.0F)) : (0.658D - (air / 100.0F));
                   mc.thePlayer.setSpeed(0);
                   motionY -= 0.04200000000001D;
                   if (air > 24.0F)
                       motionY -= 0.03D;
                   if (air == 12.0F)
                       motionY = -0.007D;
                   if (speed >= 1.8)
                       speed = 1;
                   if (collided)
                       speed += 0.2873D;
                   event.setY(mc.thePlayer.motionY = motionY);
                 mc.thePlayer.setSpeed(speed *= 0.98);
               } else if (air > 0.0F) {
                   air = 0.0F;
               }
               if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                   double groundspeed = 2.8D;
                   collided = mc.thePlayer.isCollidedHorizontally;
                   groundTicks++;
                   mc.thePlayer.motionX *= groundspeed;
                   mc.thePlayer.motionZ *= groundspeed;
                   half = (mc.thePlayer.posY != (int) mc.thePlayer.posY);
                   event.setY(mc.thePlayer.motionY = 0.4399999D);
                   air = 1.0F;
                   motionY = mc.thePlayer.motionY;
               }
		
		}
		if(mode.equalsIgnorecase("Hypixel")) {
			
			if (!jumped && MovementUtil.isOnGround(0.00001)) {
		
				jumped = true;
		
			}
			
			if (jumped && mc.thePlayer.hurtResistantTime == 19) {
				mc.thePlayer.motionY += 0.2;
				mc.thePlayer.setSpeed(3);
				MovementUtil.setMotion(44.95);
			}
			else if (jumped) {

				MovementUtil.strafe();
			}
			
			if (jumped && mc.thePlayer.hurtResistantTime == 0 && MovementUtil.isOnGround(0.0001) && timer.hasTimedElapsed(1000, true)) {
				toggle();
			
			}
		}
		if(mode.equalsIgnorecase("DamageNcp")) {
	        double diffX = mc.thePlayer.posX - posX;
	        double diffY = mc.thePlayer.posY - posY;
	        double diffZ = mc.thePlayer.posZ - posZ;
	        
	        double dist = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
			if(mc.thePlayer.onGround && ticks > 2) {
				mc.thePlayer.setSpeed(0);
				this.toggle();
			}
			
			if(mc.thePlayer.hurtTime > 0) {
				//ticksElapsed = 0;
			}
			
			if(mc.thePlayer.isMoving()) {
				ticks++;
				if(mc.thePlayer.onGround) {
					mc.thePlayer.jump();
				} else {
					if(ticksElapsed < 1.05) {
						ticksElapsed += 0.01F;
					}
					mc.timer.timerSpeed = 1F;
					mc.thePlayer.setSpeed(1.3 - ticksElapsed);
				}
			}
		}
		if(mode.equalsIgnorecase("RedeskyFar")) {
			if(mc.thePlayer.onGround) {
				mc.thePlayer.jump();
			} else {
				mc.timer.timerSpeed = 0.7F;
				mc.thePlayer.jumpMovementFactor = 0.07F;
				mc.thePlayer.motionY += 0.07F;
			}
		}
		if(mode.equalsIgnorecase("Ncp")) {
			if(mc.thePlayer.fallDistance > 1.3) {
				//mc.timer.timerSpeed = 1F;
				//this.toggle();
			}
	        double diffX = mc.thePlayer.posX - posX;
	        double diffY = mc.thePlayer.posY - posY;
	        double diffZ = mc.thePlayer.posZ - posZ;
	        
	        double dist = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
			if(mc.thePlayer.isMoving()) {
				ticks++;
				if(mc.thePlayer.onGround) {
					mc.thePlayer.jump();
				} else {
					mc.timer.timerSpeed = 1.05F;
					ticksElapsed += 0.01F;
					mc.thePlayer.setSpeed(0.7 - ticksElapsed);
				}
			}
			if(dist > 7.3) {
				this.toggle();
			} else {
				//mc.thePlayer.motionY = -0.2;
			}
		}
		if(mode.equalsIgnorecase("RedeskyFar")) {
		
		}
	}
	
	public void onEventSendPacket(EventSendPacket event) {
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
		super.onEventSendPacket(event);
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
