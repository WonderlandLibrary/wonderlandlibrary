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

public class AutoQuitOnBan extends Module {
	 public Mode mode = new Mode("Mode", "Mode", new String[]{"Panic", "Notify","Limbo"}, "Panic");

    public AutoQuitOnBan() {
        super("AntiStaff", ModuleType.Player);
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
    	if(mode.getValue().equals("Panic")) {
    		
        if (c.getMessage().contains("§l§cA player has been removed from your game.")) {
            NotificationsManager.addNotification(new Notification("A player was banned from your game by staff so Reality Client remove you from the game to Lobby", Notification.Type.Alert,5));
            mc.thePlayer.sendChatMessage("/lobby");
        
        }
        
        if (c.getMessage().contains("§l§4A player has been removed from your game. ")) {
            NotificationsManager.addNotification(new Notification("A player was banned from your game by staff so Reality Client remove you from the game to Lobby", Notification.Type.Alert,5));
            mc.thePlayer.sendChatMessage("/lobby");
        
        }
        
        if (c.getMessage().contains("player has been removed from your game")) {
            NotificationsManager.addNotification(new Notification("A player was banned from your game by staff so Reality Client remove you from the game to Lobby", Notification.Type.Alert,5));
            mc.thePlayer.sendChatMessage("/lobby");
        
        }
    	}
    	
    	if(mode.getValue().equals("Notify")) {
    		
            if (c.getMessage().contains("§l§cA player has been removed from your game.")) {
                NotificationsManager.addNotification(new Notification("A player was banned from your game by staff and there is a high chanse if you stay staff will ban you too", Notification.Type.Alert,5));
               
            
            }
            
            if (c.getMessage().contains("§l§4A player has been removed from your game. ")) {
                NotificationsManager.addNotification(new Notification("A player was banned from your game by staff and there is a high chanse if you stay staff will ban you too", Notification.Type.Alert,5));
               
            
            }
            
            if (c.getMessage().contains("player has been removed from your game")) {
                NotificationsManager.addNotification(new Notification("A player was banned from your game by staff and there is a high chanse if you stay staff will ban you too", Notification.Type.Alert,5));
               
            
            }
        	}
    	
    	if(mode.getValue().equals("limbo")) {
    		
            if (c.getMessage().contains("§l§cA player has been removed from your game.")) {
                NotificationsManager.addNotification(new Notification("A player was banned from your game by staff so Reality Client remove you from the game to limbo", Notification.Type.Alert,5));
                mc.thePlayer.sendChatMessage("/limbo");
            
            }
            
            if (c.getMessage().contains("§l§4A player has been removed from your game. ")) {
                NotificationsManager.addNotification(new Notification("A player was banned from your game by staff so Reality Client remove you from the game to limbo", Notification.Type.Alert,5));
                mc.thePlayer.sendChatMessage("/limbo");
            
            }
            
            if (c.getMessage().contains("player has been removed from your game")) {
                NotificationsManager.addNotification(new Notification("A player was banned from your game by staff so Reality Client remove you from the game to limbo", Notification.Type.Alert,5));
                mc.thePlayer.sendChatMessage("/limbo");
            
            }
        	}
        	
    	
    	
    	
    	
    	
    	
    	
       
    }

}
