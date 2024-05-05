package me.aidanmees.trivia.client.modules.Hidden;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.tools.Utils;
import me.aidanmees.trivia.gui.Level;
import me.aidanmees.trivia.gui.Notification;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;

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
//		for(EntityPlayer en : mc.theWorld.playerEntities) {
//			if(en instanceof EntityPlayerSP) {
//				continue;
//			}
//			if(!en.onGround && mc.thePlayer.ticksExisted > 140 && en.ticksExisted < 139 && Utils.getXZDist(mc.thePlayer.getPositionVector(), en.getPositionVector()) < 6) {
//				trivia.getNotificationManager().addNotification(new Notification(Level.WARNING, "You are getting watchdogged!!!"));
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
