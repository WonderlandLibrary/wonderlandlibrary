package net.caffeinemc.phosphor.module;

import net.caffeinemc.phosphor.api.event.events.KeyPressEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.gui.AsteriaMenu;
import net.caffeinemc.phosphor.module.modules.client.*;
import net.caffeinemc.phosphor.module.modules.combat.*;
import net.caffeinemc.phosphor.module.modules.misc.FakePlayerModule;
import net.caffeinemc.phosphor.module.modules.misc.PingSpoofModule;
import net.caffeinemc.phosphor.module.modules.misc.TeamsModule;
import net.caffeinemc.phosphor.module.modules.player.BridgeAssistModule;
import net.caffeinemc.phosphor.module.modules.player.CrystalOptimizerModule;
import net.caffeinemc.phosphor.module.modules.player.FastPlaceModule;
import net.caffeinemc.phosphor.module.modules.player.VelocityModule;
import net.caffeinemc.phosphor.module.modules.render.NoShieldDelayModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class ModuleManager {
	
	public ArrayList<Module> modules;
	
	public ModuleManager() {
		modules = new ArrayList<>();
		addModules();
	}

	public boolean isModuleEnabled(Class<? extends Module> moduleClass) {
		Module m = getModule(moduleClass);
		return m != null ? m.isEnabled() : false;
	}

	public boolean isModuleEnabled(String name) {
		Module m = modules.stream().filter(mm->mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
		return m != null ? m.isEnabled() : false;
	}

	@Nullable
	public <T extends Module> T getModule(Class<T> moduleClass) {
		for (Module module : modules) {
			if (moduleClass.isAssignableFrom(module.getClass())) {
				return (T) module;
			}
		}
		return null;
	}

	@Nullable
	public Module getModule(String name) {
		for (Module m : modules) {
			if (m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}

		return null;
	}

	public ArrayList<Module> getModules() {
		return new ArrayList<>(modules);
	}

	public List<Module> getModulesByCategory(Module.Category c) {
		List<Module> modules = new ArrayList<>();

		for (Module m : this.modules) {
			if (m.getCategory().equals(c))
				modules.add(m);
		}

		return modules;
	}

	public void addModule(Module module) {
		modules.add(module);
	}

	public void addModules() {
		// COMBAT
		addModule(new AimAssistModule());
		addModule(new AirAnchorModule());
		addModule(new AutoAnchorModule());
		addModule(new AutoArmorModule());
		addModule(new AutoCrystalModule());
		addModule(new AutoDoubleHandModule());
		addModule(new AutoHitCrystalModule());
		addModule(new AutoJumpResetModule());
		addModule(new AutoPotModule());
		addModule(new AutoTotemModule());
		addModule(new AxeSpamModule());
		addModule(new BlinkModule());
		addModule(new ChestStealerModule());
		addModule(new HitboxesModule());
		addModule(new InventoryTotemModule());
		addModule(new PotRefillModule());
		addModule(new ReachModule());
		addModule(new SilentAimModule());
		addModule(new SnowballThrowModule());
		addModule(new TriggerBotModule());
		addModule(new WebMacroModule());

		// RENDER
		addModule(new NoShieldDelayModule());

		// PLAYER
		addModule(new BridgeAssistModule());
		addModule(new CrystalOptimizerModule());
		addModule(new FastPlaceModule());
		addModule(new VelocityModule());

		// CLIENT
		addModule(new ArrayListModule());
		addModule(new AsteriaSettingsModule());
		addModule(new ConfigModule());
		addModule(new MiddleClickFriendModule());
		addModule(new SelfDestructModule());

		// MISC
		addModule(new PingSpoofModule());
		addModule(new FakePlayerModule());
		addModule(new TeamsModule());
	}

	@EventHandler
	private void onKeyPress(KeyPressEvent event) {
		if (!AsteriaMenu.isClientEnabled())
			return;

		if (event.action == GLFW.GLFW_RELEASE)
			return;

		if (MinecraftClient.getInstance().currentScreen instanceof ChatScreen)
			return;

		if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_F3))
			return;

		modules.stream().filter(m -> m.getKey() == event.key).forEach(Module::toggle);
	}
}
