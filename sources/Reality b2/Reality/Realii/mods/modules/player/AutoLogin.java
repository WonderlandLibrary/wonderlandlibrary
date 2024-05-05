package Reality.Realii.mods.modules.player;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.misc.EventChat;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Option;
import Reality.Realii.guis.notification.Notification;
import Reality.Realii.guis.notification.NotificationsManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.render.ArrayList2;

public class AutoLogin extends Module {
	

    public AutoLogin() {
        super("AutoLogin", ModuleType.Player);
       
    }

    
    @EventHandler
    public void onUpdate(EventPreUpdate e) {
    	if (ArrayList2.Sufix.getValue().equals("On")) {
        	
            
    		this.setSuffix("All");
    	}
    		
    	 
    	 if (ArrayList2.Sufix.getValue().equals("Off")) {
         	
    	        
     		this.setSuffix("");
     	}
    }

    @EventHandler
    public void onChat(EventChat c) {
    	
    	
        if (c.getMessage().contains("/register")) {
        	 if( mc.thePlayer.ticksExisted % 100 == 0) {
            //NotificationsManager.addNotification(new Notification("Registerd you succesfully", Notification.Type.Alert,3));
            mc.thePlayer.sendChatMessage("/register pluto1234 pluto1234");
        	 }
        }
    	 
        
  
        if (c.getMessage().contains("/login")) {
        	 if( mc.thePlayer.ticksExisted % 100 == 0) {
        	
            //NotificationsManager.addNotification(new Notification("Logged you in succesfully", Notification.Type.Alert,3));
            mc.thePlayer.sendChatMessage("/login pluto1234");
        	 }
        
        }
    	 
    	

}
}
