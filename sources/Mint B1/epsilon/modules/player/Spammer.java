package epsilon.modules.player;

import java.util.ArrayList;
import java.util.List;

import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;

import org.lwjgl.input.Keyboard;

import epsilon.modules.Module;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class Spammer extends Module {

	private ArrayList<String> spam = new ArrayList();
	
	private int thru = 0;
	
	public Spammer() {
		super("Spammer", Keyboard.KEY_NONE, Category.PLAYER, "Spam-er");
	}
	
	public void onEnable() { 

		spam.clear();
		
		spam.add( Math.round(Math.random()*100)+"Did you know that womans ip is 127.0.0.1?   ");
		spam.add("Fun fact, 87% of car crashes are caused by woman drivers!   " + Math.round(Math.random()*100));
		spam.add("Did you know that 41% percent of children are touched by there fathers?, and of that 41% 142% are female   " + Math.round(Math.random()*100));
		spam.add("Fact: 100% of holocaust survivors were jewish, seems a little suspicious that only the jews survived   " + Math.round(Math.random()*100));
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(mc.thePlayer.ticksExisted%2==0) {
				
				if(thru>=spam.size()) 
					thru = 0;
				
	
				mc.getNetHandler().addToSendQueueWithoutEvent(new C01PacketChatMessage(spam.get(thru)));
	
				thru++;
			}
		}
		
		
	}

}
