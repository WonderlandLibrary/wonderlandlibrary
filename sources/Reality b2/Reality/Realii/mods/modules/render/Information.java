
package Reality.Realii.mods.modules.render;

import net.minecraft.util.ResourceLocation;

import java.awt.Color;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender2D;
import Reality.Realii.event.events.rendering.Shader3DEvent;
import Reality.Realii.guis.font.FontLoaders;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.combat.Velocity;
import Reality.Realii.mods.modules.world.Scaffold;

public class Information
        extends Module {
    public Information() {
        super("Information", ModuleType.Render);
    }

    @EventHandler
    public void renderHud(EventRender2D event) {
    	if(ModuleManager.getModuleByClass(Velocity.class).isEnabled() &&  Velocity.Mode.getValue().equals("HypixelAdvanced")) {
        if(mc.thePlayer.onGround) {
        FontLoaders.Bpsarial18.drawStringWithShadow("Velocity State =" + " 0,100"  , 4, 30,  new Color(255, 255, 255).getRGB());
    }
        
        if(!mc.thePlayer.onGround) {
            FontLoaders.Bpsarial18.drawStringWithShadow("Velocity State =" + " 0,0"  , 5, 30,  new Color(255, 255, 255).getRGB());
        }
    	}
        if(mc.thePlayer.isBlocking()) {
        	FontLoaders.Bpsarial18.drawStringWithShadow("Block State =" + " True"  , 5, 40,  new Color(255, 255, 255).getRGB());
        	
        }
        
        if(!mc.thePlayer.isBlocking()) {
        	FontLoaders.Bpsarial18.drawStringWithShadow("Block State =" + " False"  , 5, 40,  new Color(255, 255, 255).getRGB());
        	
        }
        
       
        

        
        
    
    }
    
    
}

