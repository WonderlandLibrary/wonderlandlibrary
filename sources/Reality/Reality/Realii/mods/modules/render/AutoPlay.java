package Reality.Realii.mods.modules.render;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.misc.EventChat;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Option;
import Reality.Realii.guis.notification.Notification;
import Reality.Realii.guis.notification.NotificationsManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;

public class AutoPlay extends Module {
	 public Mode mode = new Mode("Mode", "Mode", new String[]{"HypixelNormal", "Pika","Blocksmc"}, "HypixelNormal");

    public AutoPlay() {
        super("AutoPlay", ModuleType.Player);
        addValues(mode);
    }

    
    @EventHandler
    public void onUpdate(EventPreUpdate e) {
    	if (ArrayList2.Sufix.getValue().equals("On")) {
        	
            
    		this.setSuffix(this.mode.getModeAsString());
    	}
    		
    	 
    	 if (ArrayList2.Sufix.getValue().equals("Off")) {
         	
    	        
     		this.setSuffix("");
     	}
    }

    @EventHandler
    public void onChat(EventChat c) {
    	if(mode.getValue().equals("HypixelNormal")) {
    	
        if (c.getMessage().contains("play again?")) {
            NotificationsManager.addNotification(new Notification("sending you to another game...", Notification.Type.Alert,3));
            mc.thePlayer.sendChatMessage("/play solo_normal");
        
        }
    	}
    	
    	if(mode.getValue().equals("Pika")) {
            if (c.getMessage().contains("Play Again")) {
                NotificationsManager.addNotification(new Notification("The game will start in 3 seconds", Notification.Type.Alert,3));
                mc.thePlayer.sendChatMessage("/play solo_normal");
            }
        	}
       
    }

}
