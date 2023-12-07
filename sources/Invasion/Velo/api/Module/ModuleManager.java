package Velo.api.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import Velo.api.Module.Module.Category;
import Velo.impl.Modules.combat.*;
import Velo.impl.Modules.exploit.*;
import Velo.impl.Modules.misc.*;
import Velo.impl.Modules.movement.*;
import Velo.impl.Modules.other.Blink;
import Velo.impl.Modules.other.SoundModifier;
import Velo.impl.Modules.player.*;
import Velo.impl.Modules.visuals.*;
import Velo.impl.Modules.visuals.hud.Crosshair;
import Velo.impl.Modules.visuals.hud.HUD;
import Velo.impl.Modules.visuals.hud.Inventory;
import Velo.impl.Modules.visuals.hud.Keystrokes;
import Velo.impl.Modules.visuals.hud.Radar;
import Velo.modules.impl.movement.Ninja;

public class ModuleManager {
	
	public static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();
	private static Scoreboard scoreboard = new Scoreboard();
	public static void registerModules() {
		modules.add(new Flight());
		modules.add(new HUD());
		modules.add(new ClickGui());
		modules.add(new Speed());
		modules.add(new Scoreboard());
		modules.add(new TrueSight());
		modules.add(new Fullbright());
		modules.add(new Animations());
		modules.add(new Criticals());
		modules.add(new Velocity());
		modules.add(new WTap());
		modules.add(new Killaura());
		modules.add(new Longjump());
		modules.add(new NoFall());
		modules.add(new ChestStealer());
		modules.add(new InventoryMove());
        modules.add(new Ninja());
        modules.add(new AutoClicker());
        modules.add(new SoundModifier());
        modules.add(new BloodParticles());
        modules.add(new ChinaHat());
		modules.add(new InventoryManager());
		modules.add(new Sprint());
		modules.add(new FastEat());
		modules.add(new Phase());
		modules.add(new Antibot());
		modules.add(new Blur());
		modules.add(new Disabler());
		modules.add(new Step());
		modules.add(new Scaffold());
		modules.add(new NoHurtCam());
		modules.add(new Breaker());
		modules.add(new NoRotate());
		modules.add(new TimeChanger());
		modules.add(new Inventory());
		modules.add(new Keystrokes());
		modules.add(new CustomVelo());
		modules.add(new Crosshair());
		modules.add(new TargetStrafe());
		modules.add(new NoSlowDown());
		modules.add(new ESP2D());
		modules.add(new Blink());
		modules.add(new ChestESP());
		modules.add(new AutoArmor());
		modules.add(new Nametags());
		modules.add(new Chams());
		modules.add(new AntiVoid());
		modules.add(new Autotool());
		modules.add(new BlockOverlay());
		modules.add(new Hitbox());
		modules.add(new Freecam());
		modules.add(new Radar());
		modules.add(new Skeletons());
		modules.add(new AutoPot());
		modules.add(new DMGParticle());
	}
	
	public static List<Module> getModulesByCategory(Category c) {
		
		List<Module> modules1 = new ArrayList<Module>();
		
		for(Module m : modules) {
			if(m.category == c) {
				modules1.add(m);
			}
		}
		
		return modules1;
	}
	
	   public List getModules() {
		      return this.modules;
		   }
	
	   
	public static Module get(String mName) {
		for(Module m : ModuleManager.modules) {
			if(m.name == mName) {
				return m;
			}
		}
		return null;
	}
}
