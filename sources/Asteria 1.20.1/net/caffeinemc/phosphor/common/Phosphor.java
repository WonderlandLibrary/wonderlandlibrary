package net.caffeinemc.phosphor.common;

import net.caffeinemc.phosphor.api.config.ConfigManager;
import net.caffeinemc.phosphor.api.rotation.RotationManager;
import net.caffeinemc.phosphor.api.util.CPSCounter;
import net.caffeinemc.phosphor.api.util.CrystalUtils;
import net.caffeinemc.phosphor.api.util.DamageUtils;
import net.caffeinemc.phosphor.api.util.MouseSimulation;
import net.caffeinemc.phosphor.module.ModuleManager;
import net.minecraft.client.MinecraftClient;

import net.caffeinemc.phosphor.api.event.orbit.EventBus;
import net.caffeinemc.phosphor.api.event.orbit.IEventBus;

import java.lang.invoke.MethodHandles;

public final class Phosphor {
	public static String name = "Asteria";
	public static String packagePrefix = "net.caffeinemc.phosphor";

	public static MinecraftClient mc = MinecraftClient.getInstance();
	public static IEventBus EVENTBUS = new EventBus();
	public static Phosphor INSTANCE;

	public static ModuleManager moduleManager() {
		return INSTANCE.moduleManager;
	}

	public static ConfigManager configManager() {
		return INSTANCE.configManager;
	}

	public static RotationManager rotationManager() {
		return INSTANCE.rotationManager;
	}

	public static MouseSimulation mouseSimulation() {
		return INSTANCE.mouseSimulation;
	}

	public Phosphor() {
		INSTANCE = this;
	}

	public ModuleManager moduleManager;
	public ConfigManager configManager;
	public CrystalUtils crystalUtils;
	public RotationManager rotationManager;
	public MouseSimulation mouseSimulation;

	public void init() {
		this.moduleManager = new ModuleManager();
		this.configManager = new ConfigManager();
		this.crystalUtils = new CrystalUtils();
		this.rotationManager = new RotationManager();
		this.mouseSimulation = new MouseSimulation();

		EVENTBUS.registerLambdaFactory(packagePrefix, (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
		EVENTBUS.subscribe(moduleManager);
		EVENTBUS.subscribe(crystalUtils);
		EVENTBUS.subscribe(rotationManager);
		EVENTBUS.subscribe(mouseSimulation);
		EVENTBUS.subscribe(DamageUtils.class);
		EVENTBUS.subscribe(CPSCounter.class);

		this.configManager.loadConfig();
	}
}
