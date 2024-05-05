package Reality.Realii.mods.modules.render;

import org.lwjgl.input.Keyboard;
import Reality.Realii.event.value.Mode;
import Reality.Realii.guis.clickgui.ClickGuisex;
import Reality.Realii.guis.clickgui2.RealityClickGui;
import Reality.Realii.guis.clickguisex.MainWindow;
import Reality.Realii.guis.material.themes.Classic;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;


public class ClickGui extends Module {
    public Mode mode = new Mode("Mode", "Mode", new String[]{"Material","Real","New","Idk"}, "Material");
    public static Mode LexiMode = new Mode("LexiMode", "LexiMode", new String[]{"Reality","Astolfo","Astolfo2","Astolfo3","Astolfo4","Ihassesdich","NotHot","Idk","Hideri"}, "Reality");
   


    public ClickGui() {
        super("ClickGui", ModuleType.Render);
        this.setKey(Keyboard.KEY_RSHIFT);
        addValues(this.mode,LexiMode);
    
        
    }


  
    @Override
    public void onEnable() {

    	if (mode.getValue().equals("New")) {
    		 mc.displayGuiScreen(new MainWindow());
    	}
    	
    	if (mode.getValue().equals("Idk")) {
    		 mc.displayGuiScreen(new ClickGuisex());
    	}
    	if (mode.getValue().equals("Real")) {
    		 mc.displayGuiScreen(new RealityClickGui());
    	}
             if (mode.getValue().equals("Material")) {
               
                mc.displayGuiScreen(new Classic());
            }
            
            
            
            
        
        

        this.setEnabled(false);
    }
}
