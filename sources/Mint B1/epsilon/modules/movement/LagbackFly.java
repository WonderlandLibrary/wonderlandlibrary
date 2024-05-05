package epsilon.modules.movement;

import org.lwjgl.input.Keyboard;

import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.setting.ModeSetting;
import epsilon.util.MoveUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;

public class LagbackFly extends Module{
	
	public ModeSetting mode = new ModeSetting ("Mode", "Lag", "Lag", "Packet", "Factor", "Square", "Axis", "Jewish");
	
	private boolean lagBool, packetBool, factorBool, squareBool, axisBool, jewishBool;
	private double lagD, packetD, factorD, squareD, axisD, jewishD;
	private float gradTimer1, gradTimer2;
	
	
	public LagbackFly(){
		super("FlagFly", Keyboard.KEY_NONE, Category.MOVEMENT, ":trol:");
		this.addSettings(mode);
	}
	
	public void onEnable(){
		lagD = 0;
	}
	
	public void onDisable(){
		lagD = 0;
	}
	
	public void onEvent(Event e){
		MoveUtil move = new MoveUtil();
		if(e instanceof EventUpdate){
			if(e.isPre()){
				lagD++;
				
				float pitch = 0;
				float yaw = 0;
				
				switch(mode.getMode()) {
				case "Lag":
					if(lagD> 37)
						lagD = 0;
					move.strafe(move.getBaseMoveSpeed()+1);
					
					if(mc.gameSettings.keyBindJump.pressed)
						mc.thePlayer.motionY = 1;
					else if (mc.gameSettings.keyBindSneak.pressed)
						mc.thePlayer.motionY = -1;
					else 
						mc.thePlayer.motionY = 0;
					
					if(mc.thePlayer.ticksExisted%14==0) {
						double x = mc.thePlayer.posX;
						double z = mc.thePlayer.posZ;
						double yaww = mc.thePlayer.cameraYaw;
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + (-Math.sin(yaw) * 3), mc.thePlayer.posY,mc.thePlayer.posZ + (Math.sin(yaw) * 3), mc.thePlayer.onGround));
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX+-Math.random(), mc.thePlayer.posY+-Math.random(), mc.thePlayer.posZ+-Math.random(), mc.thePlayer.onGround));
						
					}else {
						mc.thePlayer.cameraPitch = pitch;
						mc.thePlayer.cameraYaw = yaw;
					}
					if(mc.thePlayer.cameraPitch != pitch) 
						mc.thePlayer.cameraPitch = pitch;
					
					if(mc.thePlayer.cameraYaw != yaw) 
						mc.thePlayer.cameraYaw = yaw;
					break;
				
				case "Packet":
					
					break;
				
				case "Factor":
					
					break;
					
				case "Square":
					
					break;
					
				case "Axis":	
					
					break;
					
				case "Jewish":
					
					break;
					
				}
				
			}
		}
		if(e instanceof EventMotion) {
			EventMotion event = (EventMotion)e;
			if(e.isPre()) {
				
				
				switch(mode.getMode()) {
				case "Lag":
					
					if(mc.thePlayer.ticksExisted%2==0) {
						event.setY(mc.thePlayer.lastTickPosY);
						event.setZ(mc.thePlayer.lastTickPosZ);
						event.setX(mc.thePlayer.lastTickPosX);
					}
					
					break;
				
				case "Packet":
					
					break;
				
				case "Factor":
					
					break;
					
				case "Square":
					
					break;
					
				case "Axis":	
					
					break;
					
				case "Jewish":
					
					break;
				}	
			}
			if(e.isPost()) {
				
			}
			
			
		}
	}

}