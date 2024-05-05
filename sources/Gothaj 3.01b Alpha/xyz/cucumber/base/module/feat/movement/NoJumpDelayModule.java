package xyz.cucumber.base.module.feat.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventMove;
import xyz.cucumber.base.events.ext.EventReceivePacket;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;

@ModuleInfo(category = Category.MOVEMENT, description = "Allows you to jump without delay", name = "No Jump Delay", key = Keyboard.KEY_NONE)
public class NoJumpDelayModule extends Mod {
	
	@EventListener
	public void onTick(EventTick e) {
		mc.thePlayer.jumpTicks = 0;
	}
}
