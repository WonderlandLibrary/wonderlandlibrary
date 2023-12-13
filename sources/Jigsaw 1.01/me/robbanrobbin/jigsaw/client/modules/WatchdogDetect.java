package me.robbanrobbin.jigsaw.client.modules;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class WatchdogDetect extends Module {
	
	WaitTimer timer = new WaitTimer();
	
	public WatchdogDetect() {
		super("WatchdogDetect", 0, Category.HIDDEN, "Notifies you if you are getting watchdogged!");
		timer.time = 10000;
	}
	
	@Override
	public void onUpdate() {
//		if(!timer.hasTimeElapsed(10000, false)) {
//			return;
//		}
//		for(EntityPlayer en : mc.world.playerEntities) {
//			if(en instanceof EntityPlayerSP) {
//				continue;
//			}
//			if(!en.onGround && mc.player.ticksExisted > 140 && en.ticksExisted < 139 && Utils.getXZDist(mc.player.getPositionVector(), en.getPositionVector()) < 6) {
//				Jigsaw.getNotificationManager().addNotification(new Notification(Level.WARNING, "You are getting watchdogged!!!"));
//				timer.reset();
//			}
//		}
		super.onUpdate();
	}
	
	@Override
	public boolean getEnableAtStartup() {
		return true;
	}
	
	@Override
	public boolean dontToggleOnLoadModules() {
		return true;
	}
	
}
