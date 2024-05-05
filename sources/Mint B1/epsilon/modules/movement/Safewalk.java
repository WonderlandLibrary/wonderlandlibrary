package epsilon.modules.movement;


import org.lwjgl.input.Keyboard;

import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.setting.ModeSetting;
import epsilon.util.MoveUtil;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;

public class Safewalk extends Module{
	public MoveUtil move = new MoveUtil();
	public ModeSetting walk = new ModeSetting ("Mode", "Eagle", "Eagle", "Magnet","Slowdown");
	private boolean sneaking;
	public Safewalk(){
		super("Safewalk", Keyboard.KEY_Y, Category.MOVEMENT, "Prevents you from walking off edges");
		this.addSettings(walk);
	}
	
	
	public void onDisable(){
		if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) {
            mc.gameSettings.keyBindSneak.setKeyPressed(false);
        }
		
	}
	
	public void onEvent(Event e){
		if(e instanceof EventUpdate){
			if(e.isPre()){
				if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() instanceof BlockAir && mc.thePlayer.onGround) {
		            sneaking = true;
		            if(walk.getMode() == "Eagle") mc.gameSettings.keyBindSneak.setKeyPressed(true);
		            if(walk.getMode() == "Magnet") move.setMoveSpeed(-0.051);
		            if(walk.getMode() == "Slowdown") move.setMoveSpeed(0);
		        } else {
		            if (sneaking) {
		            	if(walk.getMode() == "Eagle") mc.gameSettings.keyBindSneak.setKeyPressed(false);
		                sneaking = false;
		                
		            }
		        }
			}
		}
	}

}