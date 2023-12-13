package com.enjoytheban.module.modules.world;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.TimerUtil;

/*
 * Created by Jutting on Oct 12, 2018
 */

public class PinCracker extends Module {

	private TimerUtil time = new TimerUtil();
	int num;

	private Option<Boolean> login = new Option("/login?", "login", false);
	private Numbers<Double> delay = new Numbers("Delay", "Delay", 1.0, 0.0, 20.0, 1.0);

	public PinCracker() {
		super("PinCracker", new String[] { "pincracker" }, ModuleType.World);
		addValues(login, delay);
	}

	@EventHandler
	public void onUpdate(EventPreUpdate event) {
		this.setColor(new Color(200,200,100).getRGB());
		if (login.getValue()) {
			if (this.time.delay((float) (delay.getValue() * 100))) {
				mc.thePlayer.sendChatMessage("/login " + numbers());
				this.time.reset();
			}
		} else {
			if (this.time.delay((float) (delay.getValue() * 100))) {
				mc.thePlayer.sendChatMessage("/pin " + numbers());
				this.time.reset();
			}
		}
	}

	private int numbers() {
		if (num <= 10000) {
			num = num + 1;
		}
		return num;
	}

	@Override
	public void onDisable() {
		this.num = 0;
		super.onDisable();
	}

}
