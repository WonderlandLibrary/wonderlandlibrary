package epsilon.modules.dev;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.lwjgl.input.Keyboard;

import epsilon.events.Event;
import epsilon.events.listeners.packet.EventSendPacket;
import epsilon.modules.Module;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

public class C0Fuckery extends Module{

	private final Queue<Packet> c0f = new ConcurrentLinkedDeque<>();
	
	public ModeSetting mode = new ModeSetting ("Mode", "Bus", "Bus", "Delayed", "Pinball", "Scramble", "BombBus", "Rewrite", "CatchNTag", "MimickPool", "LeakyPipe", "FullPipe","Cancel");
	
	public NumberSetting busSize = new NumberSetting ("Bus Size", 20, 2, 5000, 1);

	public NumberSetting dv = new NumberSetting ("DelayValue", 1000, 0, 10000, 20);

	public NumberSetting bombSize = new NumberSetting ("Bomb Size", 5, 2, 25, 1);
	
	public NumberSetting pinballDelay = new NumberSetting ("PinballResetDelay", 5, 2, 25, 1);

	public NumberSetting pinSize = new NumberSetting ("Pinball Shuttle Size", 20, 2, 5000, 1);

	public NumberSetting pipeLeak = new NumberSetting ("PipeLeakChance", 5, 1, 100, 1);

	public NumberSetting pipeWidth = new NumberSetting ("FullPipeCapacity", 20, 1, 100, 1);
	
	private final Timer timer = new Timer();
	private final Timer timer1 = new Timer();
	
	
	public C0Fuckery() {
		super("C0Fuckery", Keyboard.KEY_NONE, Category.DEV, "Lets you fuck with C0F");
		this.addSettings(mode, busSize, dv, bombSize, pinballDelay, pinballDelay, pipeLeak, pipeWidth);
	}
	
	public void onEvent(Event e) {
		
		if(e instanceof EventSendPacket) {
			
			Packet p = e.getPacket();
			
			if(p instanceof C0FPacketConfirmTransaction) {
				
				switch(mode.getMode()) {
				
				case "Bus":
					if(c0f.size()<busSize.getValue()) {
						e.setCancelled();
						c0f.add(p);
					}else {
						
						for(Packet c : c0f) 
							mc.getNetHandler().sendPacketNoEvent(c);
						
						c0f.clear();
					}
					
					break;
					
				case "Delayed":
					
					mc.getNetHandler().sendPacketNoEventDelayed(p, (long) dv.getValue());
					
					break;
					
				case "Pinball":
					
					if(c0f.size()<pinSize.getValue()) {
						e.setCancelled();
						c0f.add(p);
					}else {
						
						if(timer.hasTimeElapsed((long) pinballDelay.getValue(), true)) {
							c0f.clear();
						}else {
							
							if(timer1.hasTimeElapsed((long) dv.getValue(), true)) {
								mc.getNetHandler().sendPacketNoEvent(c0f.poll());
							}
							
						}
						
					}
					
					break;
				
				}
				
			}
			
			
		}
		
	}

}
