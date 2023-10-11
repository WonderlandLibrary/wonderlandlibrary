package markgg.modules.impl.player;

import markgg.RazeClient;
import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.ModeSetting;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "Regen", category = Module.Category.PLAYER)
public class Regen extends Module{

	public ModeSetting mode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "Fast");

	@EventHandler
	private final Listener<MotionEvent> eventListener = e -> {
		if(e.getType() == MotionEvent.Type.PRE){
			switch (mode.getMode()) {
				case "Vanilla":
					mc.thePlayer.setHealth(20);
					mc.thePlayer.getFoodStats().setFoodLevel(20);
					break;
				case "Fast":
					if(mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth())
						for (int i = 0; i < 10; i++)
							mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
					break;
			}
		}
	};

	public void onEnable() {
		switch (mode.getMode()) {
			case "Vanilla":
				RazeClient.addChatMessage("This mode only works on vanilla 1.8 servers!");
				break;
			case "Fast":
				RazeClient.addChatMessage("This mode needs food to work!");
				break;
		}
	}

}
