package Method.Client.utils;

import java.io.Serializable;

public class TimerUtils implements Serializable {
	private long lastMS = 0L;

	public boolean isDelay(long delay) {
		return System.currentTimeMillis() - this.lastMS >= delay;
	}
	
    public void setLastMS() {
		this.lastMS = System.currentTimeMillis();
	}
}
