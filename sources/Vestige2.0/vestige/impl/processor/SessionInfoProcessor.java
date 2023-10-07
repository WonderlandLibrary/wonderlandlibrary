package vestige.impl.processor;

import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.EventAltSwitched;
import vestige.api.event.impl.UpdateEvent;
import vestige.util.base.IMinecraft;
import vestige.util.misc.TimerUtil;
import vestige.util.network.ServerUtils;

public class SessionInfoProcessor implements IMinecraft {
	
	private String lastServer;
	private final TimerUtil timer;
	private boolean shouldResetTimer;
	
	public SessionInfoProcessor() {
		timer = new TimerUtil();
		shouldResetTimer = true;
		Vestige.getInstance().getEventManager().register(this);
	}

	@Listener
	public void onUpdate(UpdateEvent event) {
		if(mc.thePlayer.ticksExisted == 3) {
			if(ServerUtils.isOnSingleplayer() || ServerUtils.getCurrentServer() == null) {
				lastServer = "Singleplayer";
			} else {
				if(!ServerUtils.getCurrentServer().equals(lastServer) || shouldResetTimer) {
					timer.reset();
				}
				lastServer = ServerUtils.getCurrentServer();
			}
			
			shouldResetTimer = false;
		}
	}
	
	public void onAltSwitched(EventAltSwitched e) {
		if(e.changedAlt()) {
			shouldResetTimer = true;
		}
	}
	
	public TimerUtil getTimer() {
		return timer;
	}
	
	public int getHoursElapsed() {
		return (int) (timer.getTimeElapsed() / 3600000);
	}
	
	public int getMinutesElapsed() {
		return (int) (timer.getTimeElapsed() / 60000 - (getHoursElapsed() * 60));
	}
	
	public int getSecondsElapsed() {
		return (int) (timer.getTimeElapsed() / 1000 - (getHoursElapsed() * 3600) - (getMinutesElapsed() * 60));
	}
	
}
