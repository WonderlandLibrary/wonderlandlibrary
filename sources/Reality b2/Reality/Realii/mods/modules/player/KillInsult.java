package Reality.Realii.mods.modules.player;

import Reality.Realii.event.EventHandler;
import java.util.Random;
import Reality.Realii.event.events.misc.EventChat;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Option;
import Reality.Realii.guis.notification.Notification;
import Reality.Realii.guis.notification.NotificationsManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.combat.Killaura;
import Reality.Realii.mods.modules.render.ArrayList2;

public class KillInsult extends Module {
	

    public KillInsult() {
        super("KillInsult", ModuleType.Player);
       
    }

    
    @EventHandler
    public void onUpdate(EventPreUpdate e) {
    	if (ArrayList2.Sufix.getValue().equals("On")) {
        	
            
    		this.setSuffix("Normal");
    	}
    		
    	 
    	 if (ArrayList2.Sufix.getValue().equals("Off")) {
         	
    	        
     		this.setSuffix("");
     	}
    }
    
  
    
    

    @EventHandler
    public void onChat(EventChat c) {
    	  String ezz1 = "Why waste another game without Reality Client?" ;
          String ezz2 = "Buying Reality Client = being the best player";
          String ezz3 = "If you want to stop losing get reality client rn";
          String ezz4 = "You just got destroyed by Reality Client if you want to destroy others get Reality Client";
          String ezz5 = "You just got bambuzeld by Reality Client";
          String ezz6 = "Do you want to stop losing to other cheaters get Reality Client Today";
          String ezz7 = "Want to Stop losing every hvh? if yes get Reality Client";
          String ezz8 = "If you have 8 dollars around get Reality Client to Start winning";
          String ezz9 = "Get Better Kid Get Reality";
          String ezz10 = "Are do you get no bitches? get Reality Client to get all the bitches";
          
          String[] variables = {ezz1, ezz2, ezz3, ezz4, ezz5, ezz6, ezz7, ezz8, ezz9, ezz10};
          
        Random random = new Random();
        int index = random.nextInt(variables.length);
        
        String randomVariable = variables[index];
    

        String Ez = "was killed by " + mc.thePlayer.getName();
        String Ez2 = "was slain by " + mc.thePlayer.getName();
        String Ez3 = "was thrown to the void by " + mc.thePlayer.getName();
        String Ez4 = "was killed with magic while fighting " + mc.thePlayer.getName();
        String Ez5 = "couldn't fly while escaping " + mc.thePlayer.getName();
        String Ez6 = "fell to their death while escaping " + mc.thePlayer.getName();
        String Ez7 = "You killed";
        if (c.getMessage().contains(Ez) || c.getMessage().contains(Ez2)  || c.getMessage().contains(Ez3)  || c.getMessage().contains(Ez4)  || c.getMessage().contains(Ez5)  || c.getMessage().contains(Ez6) || c.getMessage().contains(Ez7)) {
        	  mc.thePlayer.sendChatMessage(randomVariable);
        }
    
    	 
    	

}
}
