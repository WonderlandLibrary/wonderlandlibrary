package vestige.impl.processor;

import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.UpdateEvent;
import vestige.util.base.IMinecraft;
import vestige.util.misc.TimerUtil;

public class BalanceCalculatorProcessor implements IMinecraft {
	
	private final TimerUtil timer = new TimerUtil();
	
	private double balance;
	private double displayedBalance;
	
	public BalanceCalculatorProcessor() {
		Vestige.getInstance().getEventManager().register(this);
		/*
		new Thread() {
			@Override
			public void run() {
				try {
					this.sleep(50);
					if(mc.thePlayer != null) {
						balance -= timer.getTimeElapsed();
						timer.reset();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
		*/
	}
	
	@Listener
	public void onUpdate(UpdateEvent event) {
		if(mc.thePlayer.ticksExisted == 1) {
			balance = 0;
			timer.reset();
		}
		
		if(mc.thePlayer.ticksExisted % 10 == 2) {
			displayedBalance = balance;
		}
	}
	
	public void onSend() {
		if(mc.thePlayer.ticksExisted > 1) {
			balance += 50;
			balance -= timer.getTimeElapsed();
			timer.reset();
		}
	}
	
	public double getBalance() {
		return displayedBalance;
	}
	
}
