package epsilon.modules.movement;


import java.util.ArrayList;
import java.util.Map;
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
import epsilon.modules.Module.Category;
import epsilon.modules.exploit.Disabler;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.MathHelperEpsilon;
import epsilon.util.MoveUtil;
import epsilon.util.PacketUtils;
import epsilon.util.Timer;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import epsilon.util.Timer;

public class PacketFly extends Module{


	private final Queue<Packet> packetQueue = new ConcurrentLinkedDeque<>();
	
	private double speedX, speedY, speedZ;
	private int ticks = 0, teleportId;

    private Packet packets = new C03PacketPlayer.C04PacketPlayerPosition(0,0,0,false);
    private C04PacketPlayerPosition lagSploit;
    private final MoveUtil move = new MoveUtil();
    private final MathHelperEpsilon math = new MathHelperEpsilon();
    private float rots[] = new float[] {0,0};
    
    
	Timer timer = new Timer();
	public ModeSetting mode = new ModeSetting("Mode", "Custom", "Custom","NCP", "Hycraft","MinelandFastest", "BlockDrop","BlockDropSurvival","NCPFork1.8", "Matrix1.9+", "Spartan1.9+");
	
	public ModeSetting custom = new ModeSetting("CustomPacket", "FACTOR","FACTOR", "MINT", "SETBACK", "FAST", "SLOW", "ELYTRA", "DESYNC", "VECTOR", "OFFGROUND", "ONGROUND");
	public PacketFly(){
		super("PacketFly", Keyboard.KEY_NONE, Category.MOVEMENT, "Packet... fly, what do you think");
		this.addSettings(mode, custom);
	}
	
	//Not doing this shit today
	public void onDisable() {
		ticks = 0;
		mc.thePlayer.noClip = false;
		mc.thePlayer.speedInAir = 0.02f;
		mc.timer.timerSpeed = 1;
		
		for(Packet packet : packetQueue) {
			mc.getNetHandler().addToSendQueue(packet);
		}
		packetQueue.clear();
		
		packets = new C04PacketPlayerPosition();
		
		switch(mode.getMode()) {
		case "MinelandFastest":
			
			C04PacketPlayerPosition pa = (C04PacketPlayerPosition) packets;

			mc.getNetHandler().sendPacketNoEventDelayed(packets, 25);
			mc.getNetHandler().sendPacketNoEventDelayed(packets, 50);
			
			break;
		}

		mc.thePlayer.capabilities.isFlying = false;teleportId = 0;
	}

	private void place(BlockPos b, int placeDirect, ItemStack itemStack, float faceX, float faceY, float faceZ) {
		mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(b, placeDirect, itemStack, faceX, faceY, faceZ));
	}
	
	public void onEnable() {
		timer.reset();
		rots[0] = mc.thePlayer.cameraYaw;
		rots[1] = mc.thePlayer.cameraPitch;
		lagSploit = new C03PacketPlayer.C04PacketPlayerPosition(math.randomInRange(mc.thePlayer.posX-mc.gameSettings.renderDistanceChunks*16, mc.thePlayer.posX+mc.gameSettings.renderDistanceChunks*16),1,math.randomInRange(mc.thePlayer.posZ-mc.gameSettings.renderDistanceChunks*16, mc.thePlayer.posZ+mc.gameSettings.renderDistanceChunks*16), mc.thePlayer.onGround);
		switch(mode.getMode()) {
		
		case "MinelandFastest":
			
			
			break;
		
		case"Spartan1.9+":
			
			mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY-100, mc.thePlayer.posZ, false));
			
			break;
			
		case "Matrix1.9+":
		
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY+0.42f, mc.thePlayer.posZ);
		
			break;
			
			
			
		}
		
		for(Packet packet : packetQueue) {
			mc.getNetHandler().sendPacketNoEvent(packet);
		}
		packetQueue.clear();
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventReceivePacket && mc.thePlayer!=null) {

    		Packet p = e.getPacket();

			if(p instanceof S08PacketPlayerPosLook && mc.thePlayer.ticksExisted>1) {

				
			}
    		switch(mode.getMode()) {
    		
    		case "Hycraft":

    			/*if(p instanceof S08PacketPlayerPosLook && mc.thePlayer.ticksExisted>1) {
    				S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook) p;
    				Vec3 s08Pos = new Vec3 (s08.x, s08.y, s08.z);
    				Vec3 playerVec = new Vec3 (mc.thePlayer.getPositionVector().xCoord, mc.thePlayer.getPositionVector().yCoord, mc.thePlayer.getPositionVector().zCoord);

					mc.thePlayer.motionX = mc.thePlayer.motionZ=0;
    				C04PacketPlayerPosition s08toPos = new C04PacketPlayerPosition(s08.x, s08.y, s08.z, false);
					mc.getNetHandler().sendPacketNoEvent(s08toPos);
					mc.thePlayer.setPosition(s08.x, s08.y, s08.z);
					
    				e.setCancelled();
    			}*/
    			
    			break;
    		
    		case "MinelandFastest":
    			break;
    		
    		case"Matrix1.9+":
    			
    			

        		if(p instanceof S08PacketPlayerPosLook) {

        			S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) p;
        			Epsilon.addChatMessage("Server lag back");
        		}
        		
        		break;
        		
    		case "Custom":
    			
    			if(p instanceof S08PacketPlayerPosLook) {

        			S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) p;
        			e.setCancelled();
        			mc.getNetHandler().sendPacketNoEvent(new C06PacketPlayerPosLook(packet.x, packet.y, packet.z, packet.yaw, packet.pitch, mc.thePlayer.onGround));
        			Epsilon.addChatMessage("Responded accurately to S08");
        			for(int i =0; i<10; i++) {
        				mc.getNetHandler().sendPacketNoEvent(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, packet.yaw, packet.pitch, mc.thePlayer.onGround));
        			Epsilon.addChatMessage("Converted S08");
        			}
        			
    			}
    			
    			
    			break;
    		}
    		
		}
		
		if(e instanceof EventSendPacket && mc.thePlayer!=null) {

			MoveUtil move = new MoveUtil();
    		Packet p = e.getPacket();
    		switch(mode.getMode()) {
    		
    		case "MinelandFastest":
    			

				packetQueue.add(p);
				e.setCancelled();
    			break;
    		
    		case"Matrix1.9+":
    			if(p instanceof C03PacketPlayer.C05PacketPlayerLook || p instanceof C03PacketPlayer.C04PacketPlayerPosition || p instanceof C03PacketPlayer.C06PacketPlayerPosLook || p instanceof C02PacketUseEntity || p instanceof C0APacketAnimation || p instanceof C01PacketChatMessage) {
    				
					packetQueue.add(p);
    			}
				e.setCancelled();
				
				if(timer.hasTimeElapsed(1000, true)) {
					for(Packet packet : packetQueue) {
						mc.getNetHandler().sendPacketNoEvent(packet);
					}
					mc.timer.timerSpeed=1;
					packetQueue.clear();
					Epsilon.addChatMessage("Release");
				}else {
					mc.timer.timerSpeed+=0.01f;
				}
				
				
				break;
				
    		case "Custom":

				packetQueue.add(p);
				e.setCancelled();
    			break;
    			
    		case "Hycraft":
    			
    			if(teleportId>5) {
    				if(!(p instanceof C0FPacketConfirmTransaction))
    				packetQueue.add(p);
    				e.setCancelled();
    			}
      			
    			break;
    		}
		}
		
		if(e instanceof EventUpdate) {
			this.displayInfo = mode.getMode();
			if(e.isPost()) {
			}
		}
		
		if(e instanceof EventMotion && mc.thePlayer!=null) {
			
			MoveUtil move = new MoveUtil();

			EventMotion event = (EventMotion)e;
			
			if(e.isPre()) {
	    		switch(mode.getMode()) {
	    		

	    		case "Hycraft":
	    			mc.thePlayer.motionY = 0;
	    			break;
	    		
	    		case "MinelandFastest":
	    			if (mc.thePlayer.ticksExisted%10 ==0) {
						event.setOnGround(false);
						move.SetMomentum(0, 0, 0);
						mc.thePlayer.moveForward = 0;
						mc.thePlayer.jumpMovementFactor = 0;
					}else {
						move.strafe(move.getBaseMoveSpeed() + 0.2);
						event.setOnGround(true);
						if(mc.gameSettings.keyBindJump.pressed) 
							mc.thePlayer.motionY = 0.5;
						
						else {
							mc.thePlayer.motionY = 0;
							mc.thePlayer.posY = Math.round(event.getY() - 0.5);
						}
						
					}
	    			break;
	    		
	    		case "Matrix1.9+":
    				
	    			mc.thePlayer.motionY=0;
	    			
	    			
	    			break;
	    			
	    		case "Custom":
	    			mc.timer.timerSpeed+=0.01;
	    			mc.thePlayer.motionY = 0.1+-mc.timer.timerSpeed/10;
	    			move.strafe(move.getSpeed()+0.1);
	    			event.setOnGround(true);
	    			
	    			if(mc.thePlayer.isCollidedHorizontally)
	    				move.packetComedy(1.5, 0);
	    			
	    			break;
	    		
	    		case"NCP":
					final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
					
					mc.thePlayer.motionY = 0;
					MoveUtil moveUtil = new MoveUtil();
					if(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX - (-Math.sin(yaw) * 1), mc.thePlayer.posY - 1, mc.thePlayer.posZ - (Math.cos(yaw) * 1))).getBlock() instanceof BlockAir)
					moveUtil.strafe(moveUtil.getBaseMoveSpeed()+1);
					
					
					double expectedX;
					double expectedY;
				    double expectedZ;
					
					double x = mc.thePlayer.posX;
			        double y = mc.thePlayer.posY;
			        double z = mc.thePlayer.posZ;
			        

			        expectedX = x + (-Math.sin(yaw) * 9);
			        expectedZ = z + (Math.cos(yaw) * 9);
			        if(mc.gameSettings.keyBindJump.getIsKeyPressed()) 
				        y = 5;
					else if (mc.gameSettings.keyBindSneak.getIsKeyPressed())
						y = -5;
					else
						y = 0;
			        
					mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(expectedX, mc.thePlayer.posY + y, expectedZ, false));
					mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(expectedX, mc.thePlayer.posY, expectedZ, false));
					break;
					
		        
				}
			}
			
			if(e.isPost()) {

	    		switch(mode.getMode()) {
	    		
	    		case "MinelandFastest":
	    			
	    			break;
	    			
	    		case "Hycraft":
	    			
	    			if(teleportId<=2) {
	    				move.packetComedy(0, -0.1);
	    			}else if (teleportId>5) {
	    				move.strafe(move.getBaseMoveSpeed()+0.7);
	    				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY-0.00000001, mc.thePlayer.posZ);
	    			}else 
	    			if(mc.thePlayer.onGround)
	    				move.packetComedy(0, 0.42);
    				teleportId++;

	    			break;
	    			
	    		}
			}
		}
	}
	
}
