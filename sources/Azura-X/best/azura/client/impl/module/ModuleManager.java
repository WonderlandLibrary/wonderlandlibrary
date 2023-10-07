package best.azura.client.impl.module;


import best.azura.client.impl.Client;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.impl.module.impl.combat.*;
import best.azura.client.impl.module.impl.combat.copsncrims.CopsNCrims;
import best.azura.client.impl.module.impl.movement.*;
import best.azura.client.impl.module.impl.other.*;
import best.azura.client.impl.module.impl.player.*;
import best.azura.client.impl.module.impl.render.*;
import best.azura.client.impl.module.impl.render.cape.Cape;
import best.azura.client.impl.value.BooleanValue;
import best.azura.scripting.ScriptModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ModuleManager {
	
	public final ArrayList<Module> registered = new ArrayList<>();
	private final HashMap<Class<?>, Module> map = new HashMap<>();
	
	public ModuleManager() {
		start();
	}
	
	public void start() {
		
		registered.addAll(Arrays.asList(
				new KillAura(),
				new Velocity(),
				new Criticals(),
				new TargetStrafe(),
				new AutoHeal(),
				new CopsNCrims(),
				new NoSlow(),
				new Speed(),
				new Flight(),
				new GuiMove(),
				new LongJump(),
				new Sprint(),
				new NoFall(),
				new Phase(),
				new Step(),
				new Teleport(),
				new FastUse(),
				new FastBow(),
				new Scaffold(),
				new InvManager(),
				new ChestStealer(),
				new SpeedMine(),
				new NoRotate(),
				new ChestAura(),
				new AntiHunger(),
				new Disabler(),
				new FPSBooster(),
				new ClientModule(),
				new Timer(),
				new KillSults(),
				new Fucker(),
				new FreeCam(),
				new ChatBypass(),
				new IRCModule(),
				new AutoHypixel(),
				new AutoDisable(),
				new ExploitPatcher(),
				new ClickGUI(),
				new HUD(),
				new Brightness(),
				new Blink(),
				new CustomMinecraft(),
				new Ambience(),
				new Animations(),
				new ESP(),
				new Camera(),
				new Cape(),
				new Crosshair(),
				new Breadcrumbs(),
				new Emotes(),
				new ChinaHat(),
				new BlurModule(),
				new WorldColor(),
				new StreamerMode(),
				new GodMode(),
				new Particles(),
				new MidClickFriends(),
				new LightningDetector(),
				new FlagReset(),
				new FakeWatchdogBot(),
				new MinemenBot())
		);
		
	}

	public void postLoad() {
		for (Module module : registered) {
			if (module instanceof ScriptModule) continue;
			map.put(module.getClass(), module);
			if (!Client.INSTANCE.getValueManager().isRegistered(module))
				Client.INSTANCE.getValueManager().register(module);
			Client.INSTANCE.getValueManager().getValues(module).add(new BooleanValue("Hide", "Hide in Arraylist", false));
		}
	}

	public void loadScripts() {
		for (Module module : registered) {
			if (!(module instanceof ScriptModule)) continue;
			map.put(module.getClass(), module);
			Client.INSTANCE.getValueManager().register(module);
			Client.INSTANCE.getValueManager().getValues(module).add(new BooleanValue("Hide", "Hide in Arraylist", false));
		}
	}

	public void removeScripts() {
		final ArrayList<Module> modules = new ArrayList<>();
		for (Module module : registered) {
			if (module instanceof ScriptModule) {
				map.remove(module.getClass());
				Client.INSTANCE.getValueManager().getValueHash().remove(module);
			} else modules.add(module);
		}
		for (Class<?> clazz : map.keySet()) {
			if (clazz.equals(ScriptModule.class))
				map.remove(clazz);
		}
		registered.clear();
		registered.addAll(modules);
	}
	
	public Module getModuleByClass(Class<?> module) {
		return map.get(module);
	}
	
	public Module getModuleByName(String module) {
		AtomicReference<Module> finding = new AtomicReference<>(null);
		registered.stream().filter(m -> m.getName().replace(" ", "").equalsIgnoreCase(module.replace(" ", ""))).findFirst().ifPresent(finding::set);
		return finding.get();
	}
	
	public ArrayList<Module> getModules(Category category) {
		ArrayList<Module> result = new ArrayList<>();
		for (Module module : registered) {
			if (module.getCategory().equals(category)) {
				result.add(module);
			}
		}
		return result;
	}
	
	public Module getModule(Object o) {
		if (o == null) return null;
		if (o instanceof String) return getModuleByName(o.toString());
		if (o instanceof Class<?>) return getModuleByClass((Class<?>) o);
		return null;
	}
	
	public ArrayList<Module> getRegistered() {
		return registered;
	}
}
