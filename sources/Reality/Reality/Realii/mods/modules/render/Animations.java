package Reality.Realii.mods.modules.render;

import Reality.Realii.event.EventHandler;

import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import net.minecraft.client.renderer.GlStateManager;

public class Animations extends Module {

    public static Mode mode = new Mode("Mode", "Mode", new String[]{"NONE", "1.7","Edit","Sus","Ketamine","MeltedButter","Spin","ray","DortWare","New3","New2","New", "Swang", "Swank","rotate","Exchibition3","Cleaner","Drop","Reality","exchibobo","Exchibition4","Pula","Clean","Rise","360","Exchibition","Exchibition2","Old"}, "1.7");
    public static Mode Swingmode = new Mode("Mode", "Mode", new String[]{"Smooth","Normal"}, "Normal");
    public static Numbers<Number>  scale = new Numbers<>("Scale", 0.15f, 0.8f, 15.0f, 8.0f);
    public static Numbers<Number>  x = new Numbers<>("x", 0.0F, -2.0F, 2.0F, 0.05f);
    public static Numbers<Number>  y = new Numbers<>("y",0.0F, -2.0F, 2.0F, 0.05f);
    public static Numbers<Number>  z = new Numbers<>("z", 0.0F, -2.0F, 2.0F, 0.05f);
    
    public static Numbers<Number> speed = new Numbers<>("Speed", 1, 1, 30, 1);
    private Option<Boolean> sufix = new Option<Boolean>("ShowSufix", "ShowSufix", false);

    public Animations() {
        super("Animations", ModuleType.Render);
        addValues(mode, speed, this.sufix, scale,x,z,y,Swingmode);
      
        
        
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
}
