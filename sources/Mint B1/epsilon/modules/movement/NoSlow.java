package epsilon.modules.movement;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.modules.combat.KillAura;
import epsilon.settings.setting.ModeSetting;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.EnumFacing;
import epsilon.util.Timer;
import net.minecraft.util.BlockPos;

public class NoSlow extends Module{
	
	public ModeSetting mode = new ModeSetting ("Mode", "Vanilla", "Vanilla", "NCP", "Hypixel", "Zonecraft","Redesky", "MatrixSimple", "MatrixAdvanced");
	public static boolean doNotSlow, matrixfunni, enabled;
	public static int matrixval; //wtf?
	private boolean matrixbool;
	private final Timer timer = new Timer();
	public NoSlow(){
		super("NoSlow", Keyboard.KEY_NONE, Category.MOVEMENT, "No slowdown from items");
		this.addSettings(mode);
	}
	
	public void onEnable(){
		matrixbool = false;
		doNotSlow = enabled = true;
		timer.reset();
		timer.reset2();
	}
	
	public void onDisable(){
		doNotSlow = enabled = false; 
	}
	
	public void onEvent(Event e){
		if(mc.gameSettings.keyBindSprint.pressed && mc.thePlayer.moveForward !=0 && !mc.thePlayer.isCollidedVertically && !mc.thePlayer.isSprinting()) {
			mc.thePlayer.setSprinting(true);
		}
		if(mode.getMode() == "Vanilla" || mode.getMode() == "NCP" || mode.getMode() == "Hypixel") {
			doNotSlow = true;
		}else if (mode.getMode() == "MatrixSimple") {
			if(!mc.thePlayer.onGround && mc.thePlayer.fallDistance>0) {
				doNotSlow = true;
				matrixfunni = true;
				matrixval = (int) 0.9;
			}else if (!mc.thePlayer.onGround){
				matrixfunni = true;
				matrixval = (int) 0.9;
			}else if(mc.thePlayer.onGround){
				doNotSlow = false;
				matrixfunni = false;
				matrixval = (int) 0.8;
			}
		}else if (mode.getMode()=="MatrixAdvanced") {
			doNotSlow = true;
		}
		if(e instanceof EventMotion) {
			this.displayInfo = mode.getMode();
			if(e.isPre() && mc.thePlayer.isBlocking() || mc.thePlayer.isUsingItem()){

				switch(mode.getMode()) {
				
				case "Zonecraft":
					
					break;
				
				case "NCP":
					if(mc.thePlayer.isBlocking())
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
					
					break;
					
				case"MatrixAdvanced":
					
					if(matrixbool) {
						mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
						matrixbool = false;
					}
					
					break;
				
				}
				
			}	
			if(e.isPost() && (mc.thePlayer.isUsingItem() || mc.thePlayer.isBlocking())) {
				
				switch(mode.getMode()) {
				
				case "Zonecraft":
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
					
					mc.thePlayer.sendQueue.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
					
					break;
				
				case "NCP":
					
					if(mc.thePlayer.isBlocking())
						mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));

					
					break;
					
				case "Redesky":
				case "Hypixel":
					
					if(mc.thePlayer.isUsingItem()||mc.thePlayer.isBlocking()) {

						mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
						
					}
					
					break;
				
					

				case "MatrixAdvanced":
					if(timer.hasTimeElapsed(240, true)) {

						mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
						matrixbool = true;
					}				
					
					break;
				}
			}
		
		
		}
	}

}