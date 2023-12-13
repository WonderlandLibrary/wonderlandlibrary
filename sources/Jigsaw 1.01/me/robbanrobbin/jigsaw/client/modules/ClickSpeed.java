package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.PacketEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.gui.ScreenPos;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;

public class ClickSpeed extends Module {
	
	private WaitTimer waitTimer = new WaitTimer();
	
	private double cps = 0.0d;
	
	private ArrayList<Long> times = new ArrayList<Long>();

	public ClickSpeed() {
		super("Click Speed", Keyboard.KEY_NONE, Category.PLAYER, "Shows your Clicks per Second (CPS)!");
	}

	@Override
	public void onToggle() {
		waitTimer.reset();
		
		times.clear();
		for(int i = 0; i < 5; i++) {
			times.add(1000l);
		}
		
		super.onToggle();
	}
	
	@Override
	public void onPacketSent(PacketEvent event) {
		Packet packet = event.getPacket();
		if(packet instanceof CPacketAnimation) {
			long timeDiff = waitTimer.getTime();
			
			times.add(timeDiff);
			
			long timesAdded = 0;
			if(times.size() > 5) {
				times.remove(0);
			}
			for(long l : times) {
				timesAdded += l;
			}
			double roundedTimes = (double)timesAdded / (double)times.size();
			
//			Jigsaw.chatMessage(roundedTimes);
			
			cps = 1d / (double)(roundedTimes / 1000);
			
			waitTimer.reset();
		}
		super.onPacketSent(event);
	}
	
	@Override
	public void onUpdate() {
		
		super.onUpdate();
	}
	
	@Override
	public void onGui() {
		Jigsaw.getUIManager().addToQueue(String.valueOf("§jCPS: §r" + Math.round(cps * 100.0) / 100.0), ScreenPos.LEFTUP);
		super.onGui();
	}

}
