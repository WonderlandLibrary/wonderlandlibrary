package Reality.Realii.mods.modules.player;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.misc.EventChat;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.render.ArrayList2;
import Reality.Realii.utils.cheats.player.Helper;

import java.awt.Color;

public class Spammer
extends Module {
	private Numbers<Number> SpammerDelay = new Numbers<>("SpammerDelay", 1, 1, 500, 87);
	  public Mode mode = new Mode("Mode", "Mode", new String[]{"Advertise", "Pika","Invaded"}, "Advertise");
    public Spammer(){
        super("Spammer", ModuleType.Player);
        this.addValues(SpammerDelay,mode);
    }
    public int counter;

  

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
    	if (ArrayList2.Sufix.getValue().equals("On")) {
        	
            
    		this.setSuffix(this.mode.getModeAsString());
    	}
    		
    	 
    	 if (ArrayList2.Sufix.getValue().equals("Off")) {
         	
    	        
     		this.setSuffix("");
     	}
    	 
    		if(mode.getValue().equals("Advertise")) {
    	if (mc.thePlayer.ticksExisted % SpammerDelay.getValue().intValue() == 0) {
                this.mc.thePlayer.sendChatMessage("Are you Tired of losing? If you want stop losing you should get Reality Client at discord.gg/s853WmbnNn");
    		}
    		}
    		
    		
    		if(mode.getValue().equals("Invaded")) {
    	    	if (mc.thePlayer.ticksExisted % SpammerDelay.getValue().intValue() == 0) {
    	                this.mc.thePlayer.sendChatMessage("Are u Tired of losing to kids that use their moms credi card to buy pay to win stuff? get reality to destroy everyone");
    	    		}
    	    		}
    		
    		if(mode.getValue().equals("Pika")) {
    	    	if (mc.thePlayer.ticksExisted % SpammerDelay.getValue().intValue() == 0) {
    	    	
    	     			 counter++;
					this.mc.thePlayer.sendChatMessage("GET BEAMED BY NYGHTFULL Join Reality client discord https://discord.gg/HGKEa6uB!!! " + counter);
    	     		
    	                
    	    		}
    	    		}
            }
        
    

}

