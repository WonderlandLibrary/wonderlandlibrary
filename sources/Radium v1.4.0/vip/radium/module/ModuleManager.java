package vip.radium.module;

import com.google.common.collect.ImmutableClassToInstanceMap;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import vip.radium.RadiumClient;
import vip.radium.event.impl.KeyPressEvent;
import vip.radium.command.impl.ModuleCommand;
import vip.radium.module.impl.combat.*;
import vip.radium.module.impl.esp.Chams;
import vip.radium.module.impl.esp.ChestESP;
import vip.radium.module.impl.esp.ESP;
import vip.radium.module.impl.esp.Indicators;
import vip.radium.module.impl.misc.*;
import vip.radium.module.impl.misc.hackerdetect.HackerDetector;
import vip.radium.module.impl.movement.*;
import vip.radium.module.impl.player.*;
import vip.radium.module.impl.visuals.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ModuleManager {

    private final ImmutableClassToInstanceMap<Module> instanceMap;

    public ModuleManager() {
        instanceMap = putInInstanceMap(
                // Movement
                new Sprint(),
                new Speed(),
                new NoSlowdown(),
                new Flight(),
                new Step(),
                new LongJump(),
                new AntiFall(),
                new Jesus(),
                new Scaffold(),
                // TODO: Phase
//                new Phase(),
                // Combat
                new KillAura(),
                new Velocity(),
                new Criticals(),
                new Reach(),
                new HitBoxExpand(),
                new AutoClicker(),
                new AutoPotion(),
                new Regen(),
                new TargetStrafe(),
                new AntiBot(),
                // Render
                new Hud(),
                new FullBright(),
                new ChestESP(),
                new Chams(),
                new ESP(),
                new Animations(),
                new BetterChat(),
                new WorldColor(),
                new Camera(),
                new Indicators(),
                new BlockOutline(),
                new Crosshair(),
                new Hitmarkers(),
                new TargetHUD(),
                new MoreParticles(),
                new DamageParticles(),
                new EnchantGlint(),
                new NoScoreboard(),
                // Other
                new InventoryMove(),
                new MemoryFix(),
                new TimeChanger(),
                new ChestStealer(),
                new AutoHypixel(),
                new HackerDetector(),
                new ChatBypass(),
                new AutoBow(),
                new Killsults(),
                // Player
                new NoRotate(),
                new AutoTool(),
                new InventoryManager(),
                new NoFall());

        getModules().forEach(Module::reflectProperties);

        getModules().forEach(Module::resetPropertyValues);

        RadiumClient.getInstance().getEventBus().subscribe(this);
    }

    @EventLink
    public final Listener<KeyPressEvent> onKeyPress = event -> {
        final int keyPressed = event.getKey();
        for (final Module module : this.getModules()) {
            final int moduleBind = module.getKey();
            if (moduleBind == 0)
                continue;
            if (moduleBind == keyPressed) {
                module.toggle();
                return;
            }
        }
    };

    public void postInit() {
        getModules().forEach(Module::resetPropertyValues);
    }

    private ImmutableClassToInstanceMap<Module> putInInstanceMap(Module... modules) {
        ImmutableClassToInstanceMap.Builder<Module> modulesBuilder = ImmutableClassToInstanceMap.builder();
        Arrays.stream(modules).forEach(module -> modulesBuilder.put((Class<Module>) module.getClass(), module));
        return modulesBuilder.build();
    }

    public Collection<Module> getModules() {
        return instanceMap.values();
    }

    public <T extends Module> T getModule(Class<T> moduleClass) {
        return instanceMap.getInstance(moduleClass);
    }

    public Module getModule(String label) {
        return getModules().stream().filter(module -> module.getLabel().replaceAll(" ", "").equalsIgnoreCase(label)).findFirst().orElse(null);
    }

    public static <T extends Module> T getInstance(Class<T> clazz) {
        return RadiumClient.getInstance().getModuleManager().getModule(clazz);
    }

    public List<Module> getModulesForCategory(ModuleCategory category) {
        return getModules().stream()
                .filter(module -> module.getCategory() == category)
                .collect(Collectors.toList());
    }
}