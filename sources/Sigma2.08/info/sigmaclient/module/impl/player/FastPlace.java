package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.event.RegisterEvent;

public class FastPlace extends Module {
	private static final String KEY_TIMES = "CLICKSPEED";

	public FastPlace(ModuleData data) {
		super(data);
		settings.put(FastPlace.KEY_TIMES, new Setting(FastPlace.KEY_TIMES, 4,"Tick delay between clicks.", 1, 0, 20));
	}

	@Override
	@RegisterEvent(events = { EventTick.class })
	public void onEvent(Event event) {
		mc.rightClickDelayTimer = Math.min(mc.rightClickDelayTimer, 1);
	}
}
