package wtf.monsoon.api.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.impl.modules.combat.*;
import wtf.monsoon.impl.modules.exploit.*;
import wtf.monsoon.impl.modules.misc.*;
import wtf.monsoon.impl.modules.movement.*;
import wtf.monsoon.impl.modules.player.*;
import wtf.monsoon.impl.modules.visual.ChestESP;
import wtf.monsoon.impl.modules.visual.CustomESP;
import wtf.monsoon.impl.modules.visual.ESP;
import wtf.monsoon.impl.modules.visual.Fullbright;

public class ModuleManager {
	
	public CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();
	
	public NotificationsModule notifs;
	public ModToggleGUI clickGuiMod;
	public BlockAnimation animations;
	public NoSlow noslow;
	public ESP esp;
	public HUDOptions arraylist;
	public TargetHUD targethud;
	public Aura killAura;
	public Blink blink;
	public Fly fly;
	public Speed speed;
	public Scaffold scaffold;
	public Crasher crasher;
	
	public ModuleManager() {
		
		modules.add(new Sprint());
		modules.add(notifs = new NotificationsModule());
		modules.add(clickGuiMod = new ModToggleGUI());
		modules.add(animations = new BlockAnimation());
		modules.add(noslow = new NoSlow());
		modules.add(new NoFall());
		modules.add(new Fastplace());
		modules.add(esp = new ESP());
		modules.add(new ChestESP());
		modules.add(new Fullbright());
		modules.add(new CustomESP());
		modules.add(arraylist = new HUDOptions());
		modules.add(targethud = new TargetHUD());
		modules.add(killAura = new Aura());
		modules.add(new Velocity());
		modules.add(blink = new Blink());
		modules.add(fly = new Fly());
		modules.add(new Step());
		modules.add(speed = new Speed());
		modules.add(new Longjump());
		modules.add(new HighJump());
		modules.add(scaffold = new Scaffold());
		modules.add(new ChestStealer());
		modules.add(new Phase());
		modules.add(new AutoArmor());
		modules.add(new Breaker());
		modules.add(new Disabler());
		modules.add(crasher = new Crasher());
		modules.add(new InvManager());
		modules.add(new LunarSpoofer());
		modules.add(new TargetStrafe());
		modules.add(new Criticals());
		modules.add(new AutoClicker());
		
	}
	
	public Module getModule(String name) {
		for (Module m : modules) {
			if (m.name.equalsIgnoreCase(name)) {
				return m;
			}
		}
		return null;
	}

	
	public List<Module> getModulesByCategory(Category c) {
        List<Module> list = new ArrayList<Module>();

        for(Module m : modules) {
            if(m.category == c) {
                list.add(m);
            }
        }
        return list;
    }
}
