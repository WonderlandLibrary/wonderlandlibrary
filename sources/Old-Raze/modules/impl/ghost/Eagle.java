package markgg.modules.impl.ghost;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.NumberSetting;
import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "Eagle", category = Module.Category.GHOST)
public class Eagle extends Module {


	@EventHandler
	private final Listener<MotionEvent> motionEventListener = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock() instanceof BlockAir && mc.thePlayer.onGround)
				mc.gameSettings.keyBindSneak.pressed = true;
			else mc.gameSettings.keyBindSneak.pressed = false;
		}
	};
	
	public void onDisable() {
		if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) 
			mc.gameSettings.keyBindSneak.pressed = false;
	}

}
