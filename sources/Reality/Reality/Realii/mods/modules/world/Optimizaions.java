package Reality.Realii.mods.modules.world;

import Reality.Realii.Client;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.guis.notification.Notification;
import Reality.Realii.guis.notification.NotificationsManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.movement.Speed;
import Reality.Realii.mods.modules.world.Disabler;

public class Optimizaions extends Module {
    public Optimizaions(){
        super("Optimizaions", ModuleType.Misc);
        this.setEnabled(true);
    }
    
    
    
    
    @EventHandler
    private void onUpdate(EventPreUpdate e) {
    	
    }
   
}
