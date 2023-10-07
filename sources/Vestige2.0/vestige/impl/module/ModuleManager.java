package vestige.impl.module;

import lombok.Getter;
import vestige.api.module.DraggableRenderModule;
import vestige.api.module.Module;
import vestige.impl.module.combat.*;
import vestige.impl.module.exploit.*;
import vestige.impl.module.ghost.*;
import vestige.impl.module.misc.*;
import vestige.impl.module.movement.*;
import vestige.impl.module.player.*;
import vestige.impl.module.render.*;
import vestige.impl.module.world.*;

import java.util.ArrayList;

@Getter
public class ModuleManager {

    private final ArrayList<Module> modules = new ArrayList<>();
    private final ArrayList<DraggableRenderModule> draggableRenderModules = new ArrayList<>();

    public ModuleManager() {
    	modules.add(new AutoArmor());
        modules.add(new Killaura());
        modules.add(new Velocity());
        modules.add(new TargetStrafe());
        modules.add(new Criticals());
        
        modules.add(new Autoclicker());
        modules.add(new Reach());
        modules.add(new Hitbox());
        modules.add(new DelayRemover());
        modules.add(new AutoBridge());
        
        modules.add(new Fly());
        modules.add(new Speed());
        modules.add(new Sprint());
        modules.add(new Noslow());
        modules.add(new InvMove());
        modules.add(new Longjump());
        modules.add(new Step());
        
        modules.add(new InvManager());
        modules.add(new Nofall());
        modules.add(new FastPlace());

        modules.add(new HUD());
        modules.add(new ClickGuiModule());
        modules.add(new ESP());
        modules.add(new Chams());
        modules.add(new TargetHUD());
        modules.add(new Animations());
        modules.add(new Fullbright());
        modules.add(new NameProtect());
        modules.add(new SessionInfo());
        modules.add(new TimeChanger());
        modules.add(new NoScoreboard());
        
        modules.add(new Antivoid());
        modules.add(new Disabler());
        modules.add(new BoatFly());
        modules.add(new SelfDestruct());
        modules.add(new Phase());

        //modules.add(new Benchmark());
        modules.add(new NoRotate());
        
        modules.add(new Breaker());
        modules.add(new ChestStealer());
        modules.add(new Scaffold());
        //modules.add(new OldScaffold());
        
        for(Module m : modules) {
        	if(m instanceof DraggableRenderModule) {
        		draggableRenderModules.add((DraggableRenderModule) m);
        	}
        }
    }
    
    public Module getModule(Class clazz) {
    	for(Module m : modules) {
    		if(m.getClass().equals(clazz)) {
    			return m;
    		}
    	}
    	return null;
    }
    
    public Module getModuleByName(String name) {
    	for(Module m : modules) {
    		if(m.getName().equalsIgnoreCase(name)) {
    			return m;
    		}
    	}
    	return null;
    }
    
}
