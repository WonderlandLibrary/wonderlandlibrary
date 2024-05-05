package epsilon.modules.movement;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.events.listeners.packet.EventReceivePacket;
import epsilon.events.listeners.packet.EventSendPacket;
import epsilon.modules.Module;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.MoveUtil;
import epsilon.util.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class HighJump extends Module{

	private final Queue<Packet> packetQueue = new ConcurrentLinkedDeque<>();
	
	public Timer timer = new Timer();
	public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Test","MineLatino","Hycraft","Grim", "Vulcan","Spartan", "AGC", "Redesky", "Matrix");
	public NumberSetting vanillam = new NumberSetting("VanillaMotion", 0.5, 0.0, 10.0, 0.25);
	private double y;
	private int ticks;
	private int damageValue =0;
	private double stophere = 0;
	private boolean jumped = false;
	MoveUtil move = new MoveUtil();
	public HighJump(){
		super("HighJump", Keyboard.KEY_C, Category.MOVEMENT, "Boing");
		this.addSettings(mode, vanillam);
	}
	
	public void onEnable() {
		
		currentTimer = 1f;
		ticks = 0;
		
		jumped = false;
		damageValue =0;
		
		switch(mode.getMode()) {
		
		case "Matrix":

			mc.thePlayer.motionY = 0.42f;
			move.strafe(move.getBaseMoveSpeed()+0.5);
			break;
			
		case "Test":

			move.damagePreservePacket();
			break;
			
		case "Minelatino":

			move.damage();
			mc.thePlayer.motionY = 1;
			move.strafe(2);
			break;
			
		case "Grim":

			mc.thePlayer.jump();
			break;
			
		case "Spartan":

			stophere = mc.thePlayer.posY;
			mc.thePlayer.motionY = 10;
			mc.timer.timerSpeed = (float) 0.9;
			break;
			
		case "Vulcan":
		case "Hycraft":
		case"AGC":

			damageValue++;
			mc.thePlayer.jump();
			move.setMoveSpeed(0);
			break;
		
		
		}
		
		
		
		
		y = mc.thePlayer.posY;
		
		for(Packet packet : packetQueue) {
			mc.getNetHandler().sendPacketNoEvent(packet);
		}
		packetQueue.clear();
			
		
	}
	public void onDisable() {
		damageValue =0;
		mc.timer.timerSpeed = 1;
		for(Packet packet : packetQueue) {
			mc.getNetHandler().sendPacketNoEvent(packet);
		}
		packetQueue.clear();
	}
	
	public void onEvent(Event e){
		if(e instanceof EventReceivePacket) {

    		Packet p = e.getPacket();
			switch(mode.getMode()) {
			
			case "Matrix":
				if(p instanceof S08PacketPlayerPosLook) {

					S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) p;
        			e.setCancelled();
        			mc.getNetHandler().sendPacketNoEvent(new C06PacketPlayerPosLook(packet.x, packet.y, packet.z, packet.yaw, packet.pitch, mc.thePlayer.onGround));
					Epsilon.addChatMessage("recieved");
        			mc.thePlayer.motionY = 0.42f;
        			move.strafe(move.getBaseMoveSpeed()+0.5);
					
					this.toggled = false;
				}
				
				break;
			
			case "Redesky":
			
				if(p instanceof S08PacketPlayerPosLook) {
					S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) p;
        			e.setCancelled();
        			mc.getNetHandler().sendPacketNoEvent(new C06PacketPlayerPosLook(packet.x, packet.y, packet.z, packet.yaw, packet.pitch, mc.thePlayer.onGround));
        			Epsilon.addChatMessage("Responded accurately to S08");

        			mc.getNetHandler().addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, packet.yaw, packet.pitch, mc.thePlayer.onGround));
        			Epsilon.addChatMessage("Converted S08");
        			
        			
    			}
				if(p instanceof S12PacketEntityVelocity) {
					e.setCancelled();
				}
				
				break;
			}
		}
		
		if(e instanceof EventSendPacket) {

    		Packet p = e.getPacket();
			switch(mode.getMode()) {
			
			case "Matrix":
				
				/*if(p instanceof C03PacketPlayer && !(p instanceof C03PacketPlayer.C04PacketPlayerPosition || p instanceof C03PacketPlayer.C06PacketPlayerPosLook))
					e.setCancelled();
				
				if (p instanceof C03PacketPlayer.C04PacketPlayerPosition || p instanceof C03PacketPlayer.C06PacketPlayerPosLook ||
		                p instanceof C08PacketPlayerBlockPlacement ||
		                p instanceof C0APacketAnimation ||
		                p instanceof C0BPacketEntityAction || p instanceof C02PacketUseEntity
		                || p instanceof C0FPacketConfirmTransaction) {
		            e.setCancelled();

		            packetQueue.clear();
		        }*/
				
				break;
			
			case "Redesky":
				packetQueue.add(p);
					//If this flags remove that line i forgor if we kept it or not cuz i had to move this from a different module that i have already updated to be different.
				
				if(timer.hasTimeElapsed(1000, true)) {
					for(Packet packet : packetQueue) {
						mc.getNetHandler().sendPacketNoEvent(packet);
					}
					packetQueue.clear();
					Epsilon.addChatMessage("Release");
				}
				break;
			}
		}
		
		if(e instanceof EventMotion) {
			EventMotion event = (EventMotion)e;
			if(e.isPre()) {
    			this.displayInfo = mode.getMode();
				
				switch(mode.getMode()) {
				
				case "Matrix":
					
					break;
				
				case "Redesky":
	    			mc.thePlayer.motionY += 0.05;
	    			
	    			
	    			if(mc.thePlayer.onGround)
	    				mc.thePlayer.jump();
	    			
					
					break;
					
				case "Test":

					if(mc.thePlayer.onGround) 
						mc.thePlayer.motionY = 0.42f;
						
					if(mc.thePlayer.hurtTime>0) {
						move.strafe(move.getBaseMoveSpeed());
						if(mc.thePlayer.fallDistance<=0) {
							mc.thePlayer.motionY+=0.1f;
						}	
						mc.timer.timerSpeed = (float) (1.5 + Math.random());
					}else {
						mc.timer.timerSpeed = 1;
					}
					break;
					
				case "Grim": //Flagboost

					if(!mc.thePlayer.onGround) {
						if(!(y+2<mc.thePlayer.posY) && !(mc.thePlayer.fallDistance>1)) {
							move.packetComedy(0, 0.5583478967);
							mc.thePlayer.stepHeight = 10;
							mc.thePlayer.onGround = true;
						}	else {
							mc.thePlayer.stepHeight = 0.5f;
						}
						
					}	else {
						y = mc.thePlayer.posY;
					}
					break;
				
				}
				
				if(mode.getMode()=="Vulcan" || mode.getMode() == "Hycraft"||mode.getMode()=="AGC") { //Fix later cba rn
					if(mc.thePlayer.onGround && damageValue<=4) {
						event.setOnGround(false);
						mc.thePlayer.jump();
						damageValue++;
						move.setMoveSpeed(0);
					}
					if(damageValue>3 && mc.thePlayer.onGround ) {
						mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
						damageValue++;
					}
					
					if(mode.getMode() == "Hycraft") {
						if(damageValue>4 && mc.thePlayer.hurtTime>0 && mc.thePlayer.hurtTime<=6) {
							mc.thePlayer.motionY = 0.6;
							move.strafe(move.getBaseMoveSpeed() + 0.1);
							damageValue++;
						}else if(damageValue>5 && mc.thePlayer.onGround) {
							this.toggled = false;
							damageValue = 0;
						}
					}
					if(mode.getMode() =="Vulcan") {
						if(damageValue>4 && mc.thePlayer.hurtTime>0 && mc.thePlayer.hurtTime<=4) {
							mc.thePlayer.motionY = 0.4;
							move.strafe(move.getBaseMoveSpeed()-0.01);
							damageValue++;
						}else if(damageValue>5 && mc.thePlayer.onGround) {
							this.toggled = false;
							damageValue = 0;
						}
						if(mc.thePlayer.fallDistance>0 && mc.thePlayer.fallDistance<1 && damageValue>4 && mc.thePlayer.motionY< -0.4) {
							mc.thePlayer.motionY =-0.4;
						}
					}
					if(mode.getMode()=="AGC") {
						if(damageValue>4 && mc.thePlayer.hurtTime>0 && mc.thePlayer.hurtTime<=10) {
							
							move.strafe(move.getBaseMoveSpeed()+1);
							if(mc.thePlayer.hurtTime>0 && mc.thePlayer.hurtTime<3) {
								mc.thePlayer.motionY =0.42f;
							}
							damageValue++;
						}else if(damageValue>5 && mc.thePlayer.onGround) {
							this.toggled = false;
							damageValue = 0;
						}
					}
				}
			}
		}
		
		if(e instanceof EventUpdate){
			if(e.isPre()) {
				
				if(mc.thePlayer.onGround) {
					mc.thePlayer.jump();
					jumped = true;
				}
				
				if(jumped) {
					switch(mode.getMode()) {
					/*case "Matrix":
						
						mc.timer.timerSpeed = currentTimer;
						
						if (ticks < 21) {
	                        mc.thePlayer.jump();
	                        mc.thePlayer.motionY *= 0.77;
	                        mc.thePlayer.motionX *= 0.4;
	                        mc.thePlayer.motionZ *= 0.4;
	                    } else {
	                        if (currentTimer > 0.77f) {
	                            currentTimer = Math.max(0.08F, currentTimer - 0.05F * 2);
	                        }
	                    }
	                    ticks++;
						
						break;*/
					}
				}
				
				
				if((mode.getMode()  == "Spartan" || mode.getMode() == "Hycraft") && stophere < mc.thePlayer.posY-80 && mc.thePlayer.motionY > 0.1) {
					mc.thePlayer.motionY = 0;
					mc.timer.timerSpeed = 1;
				}else if (mode.getMode() == "Spartan") {
					move.setMoveSpeed(0.25);
				}
			}
		}
	}
	
	private float currentTimer = 1F;
}