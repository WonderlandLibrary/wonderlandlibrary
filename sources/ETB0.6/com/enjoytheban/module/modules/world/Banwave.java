package com.enjoytheban.module.modules.world;

import java.awt.Color;
import java.util.ArrayList;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.management.FriendManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.TimerUtil;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;

public class Banwave extends Module {

	private TimerUtil timer = new TimerUtil();
	public ArrayList<Entity> banned;

	private String banMessage = "twitter.com/CustomKKK";

	private Option<Boolean> tempBan = new Option("Temp Ban", "temp", false);
	private Numbers<Double> banDelay = new Numbers("Delay", "delay", 10.0, 1.0, 20.0, 1.0);

	public Banwave() {
		super("BanWave", new String[] { "dick", "banner" }, ModuleType.Player);
		setColor(new Color(255, 0, 0).getRGB());
		this.banned = new ArrayList<Entity>();
		addValues(tempBan, banDelay);
	}

	@Override
	public void onEnable() {
		this.banned.clear();
		super.onEnable();
	}

	@EventHandler
	public void onUpdate(EventPreUpdate event) {
		for (Object o : mc.theWorld.getLoadedEntityList()) {
			if (o instanceof EntityOtherPlayerMP) {
				EntityOtherPlayerMP e = (EntityOtherPlayerMP) o;
				if (!this.timer.hasReached(banDelay.getValue() * 100) || FriendManager.isFriend(e.getName())
						|| e.getName() == mc.thePlayer.getName() || this.banned.contains(e)) {
					continue;
				}
				if (tempBan.getValue()) {
					mc.thePlayer.sendChatMessage("/tempban " + e.getName() + " 7d" + " " + banMessage);
					System.out.println("/tempban " + e.getName() + " 7d" + " " + banMessage);
				} else {
					mc.thePlayer.sendChatMessage("/ban " + e.getName() + " " + banMessage);
					System.out.println("/ban " + e.getName() + " " + banMessage);
				}
				this.banned.add(e);
				this.timer.reset();
			}
		}
	}
}