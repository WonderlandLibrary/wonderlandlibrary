package best.azura.client.impl.module.impl.movement.longjump;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.impl.module.impl.movement.LongJump;
import best.azura.client.util.player.MovementUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class LongJumpSpartanB453Impl implements ModeImpl<LongJump> {

	@Override
	public LongJump getParent() {
		return (LongJump) Client.INSTANCE.getModuleManager().getModule(LongJump.class);
	}

	@Override
	public String getName() {
		return "Spartan B453";
	}

	@Override
	public void onEnable() {
		getParent().setEnabled(false);
		MovementUtil.spoof(0, true);
		for (int i = 1; i < 15; i++) {
			MovementUtil.spoof(-Math.sin(mc.thePlayer.getDirection()) * 0.15 * i, 9 * i, Math.cos(mc.thePlayer.getDirection()) * 0.15 * i, true);
			MovementUtil.spoof(-Math.sin(mc.thePlayer.getDirection()) * 0.25 * i, 9 * i, Math.cos(mc.thePlayer.getDirection()) * 0.25 * i, false);
		}
		MovementUtil.spoof(5000, true);
		MovementUtil.spoof(0, true);
	}

	@Override
	public void onDisable() {

	}


}


