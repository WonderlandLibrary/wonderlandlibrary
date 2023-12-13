package summer.cheat.cheats.misc;

import summer.base.manager.Selection;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventPacket;
import summer.base.utilities.ChatUtils;
import summer.base.manager.config.Cheats;

public class Logger extends Cheats {

	public Logger() {
		super("Logger", "dev", Selection.MOVEMENT);
		// TODO Auto-generated constructor stub
	}

	@EventTarget
	public void onPacket(EventPacket e) {
		if (mc.thePlayer.ticksExisted % 20 == 0) {
			ChatUtils.sendMessage("\u00A74PACKET FOUND\u00A78: \u00A73" + "\u00A73" + e.getPacket());
		}

	}

}
