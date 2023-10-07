package info.sigmaclient.module.impl.other;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.event.RegisterEvent;

public class AutoSay extends Module {

	info.sigmaclient.util.Timer timer = new info.sigmaclient.util.Timer();
	public static final String WORDS = "SAY";
	public final String DELAY = "DELAY";

	public AutoSay(ModuleData data) {
		super(data);
		settings.put(WORDS, new Setting(WORDS, "/warp", "Message to send."));
		settings.put(DELAY, new Setting(DELAY, 500, "MS delay between messages.", 100, 100, 10000));
	}

	@Override
	@RegisterEvent(events = { EventTick.class })
	public void onEvent(Event event) {
		String message = ((String) settings.get(WORDS).getValue()).toString();
		if (timer.delay(((Number)settings.get(DELAY).getValue()).longValue())) {
			ChatUtil.sendChat(message + " " + (int)(Math.random() * 100000));
			timer.reset();
		}
	}
}
