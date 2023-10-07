package wtf.monsoon.impl.modules.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventPreMotion;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.util.misc.PacketUtil;
import wtf.monsoon.api.util.misc.ServerUtil;

public class NoFall extends Module {

	int packetsent = 0;

	public NoFall() {
		super("Nofall", "Take no fall damage", Keyboard.KEY_NONE, Category.PLAYER);
	}


	@EventTarget
	public void onEvent(EventPreMotion e) {
		if(mc.thePlayer.fallDistance > 3) {
			e.setOnGround(true);
		}
	}
	
}
