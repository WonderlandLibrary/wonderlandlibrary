package me.crimp.claudius.managers;

import me.crimp.claudius.event.events.Render2DEvent;
import me.crimp.claudius.event.events.Render3DEvent;
import me.crimp.claudius.mod.Feature;
import me.crimp.claudius.mod.gui.ClickGui;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.modules.text.*;
import me.crimp.claudius.mod.modules.client.*;
import me.crimp.claudius.mod.modules.exploit.*;
import me.crimp.claudius.mod.modules.misc.*;
import me.crimp.claudius.mod.modules.movement.*;
import me.crimp.claudius.mod.modules.pvp.*;
import me.crimp.claudius.mod.modules.render.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

import java.util.*;
import java.util.stream.Collectors;

public class ModuleManager extends Feature {
    public static String GetDesc;
    public List<Module> modules;
    public List<Module> sortedModules = new ArrayList<>();
    public List<String> sortedModulesABC = new ArrayList<>();

    public void init() {
        this.modules = Arrays.asList(
                ClickGuiModule.INSTANCE,

                //exploit
                new PearlBait(),
                new ClipFly(),

                //misc
                new DonkeySwap(),
                new McfSync(),
                new ClickEnemy(),
                new AutoEmemy(),
                new EnemyAlert(),
                new ModuleSpawns(),
                new NoGlobalSounds(),

                //text
                new ChatSuffix(),
                new LagText(),
                new PopCounter(),
                new SelfPopCounter(),
                new AntiLog4j(),
                new AntiLagText(),
                new Notifications(),
                new ToggleMacros(),
                new ZeroBKitBot(),

                //movement
                new GroundStrafe(),
                new Step(),
                new NoSlow(),

                //client
                new HUD(),
                new RPC(),
                new Prefix(),
                new CustomBackground(),

                //fake
                new FakeExploit(),

                //pvp
                new Dispenser32k(),
                new MainHandTotem(),

                //render
                new PopChams(),
                new EntityAlerts(),
                new DeathEffect(),
                new ExtraTooltips(),
                new OtherPlayers(),
                new BetterWatermark(),
                new PlayerRadar(),
                new CoolRenderOptions(),
                new SwingSpeed(),
                new Nametags(),
                new BurrowEsp()
        );
    }

    public Module getModuleByName(String name) {
        for (Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : this.modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        return null;
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module module : this.modules) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> enabledModules = new ArrayList<>();
        for (Module module : this.modules) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<>();
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add(module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(((EventBus) MinecraftForge.EVENT_BUS)::register);
        this.modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        this.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        this.modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : this.modules) module.enabled.setValue(false);
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof ClickGui) return;
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) module.toggle();
        });
    }

    public void GetDesc() {
    }
}

