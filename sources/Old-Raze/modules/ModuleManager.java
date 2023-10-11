package markgg.modules;

import markgg.modules.impl.combat.*;
import markgg.modules.impl.ghost.*;
import markgg.modules.impl.misc.*;
import markgg.modules.impl.movement.*;
import markgg.modules.impl.player.*;
import markgg.modules.impl.render.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleManager {

	public final Map<Class<? extends Module>, Module> modules = new HashMap<>();

	public ModuleManager() {
		addModules(
				new BowAimbot(),
				new OldAura(),
				new AimAssist(),
				new AntiAFK(),
				new AutoClicker(),
				new NoClickDelay(),
				new ItemESP(),
				new MobESP(),
				new Speed(),
				new TargetHUD(),
				new Eagle(),
				new WTap(),
				new ChestESP(),
				new AntiExploit(),
				new Rotations(),
				new Cape(),
				new Disabler(),
				new AutoHypixel(),
				new Spammer(),
				new Timer(),
				new CustomSpeed(),
				new Respawn(),
				new Fly(),
				new Glide(),
				new LongJump(),
				new NoFall(),
				new NoSlowDown(),
				new ESP(),
				new OldScaffold(),
				new Sprint(),
				new Step(),
				new Phase(),
				new FastEat(),
				new ChestSteal(),
				new FastPlace(),
				new Fucker(),
				new AutoSword(),
				new Regen(),
				new NoBlind(),
				new ClickGUI(),
				new Animations(),
				new HUD2(),
				new FullBright(),
				new AutoTool()
				);
	}

	public Map<Class<? extends Module>, Module> getModules() {
		return modules;
	}

	private void addModules(Module... modules) {
		for (Module module : modules)
			this.getModules().put(module.getClass(), module);
	}

	public List<Module> getModulesByCategory(Module.Category c) {
		List<Module> modules = new ArrayList<>();

		for (Module m : this.getModules().values()) {
			if (m.category == c)
				modules.add(m);
		}

		return modules;
	}

	public void keyPress(int key) {
		for(Module m : this.getModules().values()) {
			if(m.getKey() == key)
				m.toggle();

		}
	}


	public <T extends Module> T getModule(final Class<T> clazz){
		return (T) this.modules.get(clazz);
	}

	public boolean isModuleToggled(String Module) {
		for (Module m : this.getModules().values()) {
			if (m.name == Module &&
					m.toggled)
				return true;
		}
		return false;
	}
}
