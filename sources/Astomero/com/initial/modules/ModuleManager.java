package com.initial.modules;

import com.initial.utils.render.*;
import com.initial.modules.impl.combat.*;
import com.initial.modules.impl.movement.*;
import com.initial.modules.impl.exploit.*;
import com.initial.modules.impl.player.*;
import com.initial.modules.impl.other.*;
import com.initial.modules.impl.visual.*;
import com.initial.events.impl.*;
import com.initial.*;
import com.initial.settings.impl.*;
import com.initial.settings.*;
import java.util.*;

public class ModuleManager
{
    private static ArrayList<Module> modules;
    
    public ModuleManager() {
        XRayUtils.initXrayBlocks();
        ModuleManager.modules.add(new TargetStrafe());
        ModuleManager.modules.add(new KillAura());
        ModuleManager.modules.add(new Velocity());
        ModuleManager.modules.add(new Criticals());
        ModuleManager.modules.add(new AutoArmor());
        ModuleManager.modules.add(new AutoHeal());
        ModuleManager.modules.add(new AutoPot());
        ModuleManager.modules.add(new FastBow());
        ModuleManager.modules.add(new AntiBot());
        ModuleManager.modules.add(new Regen());
        ModuleManager.modules.add(new CustomSpeed());
        ModuleManager.modules.add(new CustomFly());
        ModuleManager.modules.add(new HighJump());
        ModuleManager.modules.add(new Scaffold());
        ModuleManager.modules.add(new LongJump());
        ModuleManager.modules.add(new Flight());
        ModuleManager.modules.add(new NoSlow());
        ModuleManager.modules.add(new Speed());
        ModuleManager.modules.add(new Step());
        ModuleManager.modules.add(new Sprint());
        ModuleManager.modules.add(new InventoryManager());
        ModuleManager.modules.add(new AutoRespawn());
        ModuleManager.modules.add(new NoRotate());
        ModuleManager.modules.add(new AntiVoid());
        ModuleManager.modules.add(new FastMine());
        ModuleManager.modules.add(new InvMove());
        ModuleManager.modules.add(new AutoTool());
        ModuleManager.modules.add(new Stealer());
        ModuleManager.modules.add(new NoFall());
        ModuleManager.modules.add(new Jesus());
        ModuleManager.modules.add(new Annoy());
        ModuleManager.modules.add(new TimerNigger());
        ModuleManager.modules.add(new PingSpoof());
        ModuleManager.modules.add(new Disabler());
        ModuleManager.modules.add(new FastEat());
        ModuleManager.modules.add(new ClickTP());
        ModuleManager.modules.add(new AntiAim());
        ModuleManager.modules.add(new Phase());
        ModuleManager.modules.add(new Blink());
        ModuleManager.modules.add(new ScoreboardMover());
        ModuleManager.modules.add(new NameProtect());
        ModuleManager.modules.add(new AntiVanish());
        ModuleManager.modules.add(new ChatFilter());
        ModuleManager.modules.add(new FastPlace());
        ModuleManager.modules.add(new Killsults());
        ModuleManager.modules.add(new Streamer());
        ModuleManager.modules.add(new BlockOverlay());
        ModuleManager.modules.add(new Animations());
        ModuleManager.modules.add(new FullBright());
        ModuleManager.modules.add(new TabGUI());
        ModuleManager.modules.add(new ChinaHat());
        ModuleManager.modules.add(new NoHurtCam());
        ModuleManager.modules.add(new Crosshair());
        ModuleManager.modules.add(new NameTags());
        ModuleManager.modules.add(new ClickGUI());
        ModuleManager.modules.add(new WorldTime());
        ModuleManager.modules.add(new NoRender());
        ModuleManager.modules.add(new ChestESP());
        ModuleManager.modules.add(new Chams());
        ModuleManager.modules.add(new Radar());
        ModuleManager.modules.add(new XRay());
        ModuleManager.modules.add(new HUD());
    }
    
    public Module[] getModulesInCategory(final Category moduleCategory) {
        return ModuleManager.modules.stream().filter(module -> module.getCategory() == moduleCategory).toArray(Module[]::new);
    }
    
    public void onUpdate() {
        for (final Module m : ModuleManager.modules) {
            m.onUpdate();
        }
    }
    
    public void onEvent(final EventNigger e) {
        for (final Module m : Astomero.instance.moduleManager.getModules()) {
            if (!m.toggled) {
                continue;
            }
            m.onEvent(e);
        }
    }
    
    public ModeSet getModeSetByName(final String moduleName, final String settingName) {
        for (final Module m : this.getModules()) {
            if (m.name.equalsIgnoreCase(moduleName)) {
                for (final Setting s : m.settings) {
                    if (s.name.equalsIgnoreCase(settingName)) {
                        return (ModeSet)s;
                    }
                }
            }
        }
        return null;
    }
    
    public Module getMod(final String name) {
        return ModuleManager.modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    
    public static <T extends Module> T getModule(final Class<T> clazz) {
        return (T)ModuleManager.modules.stream().filter(mod -> mod.getClass() == clazz).findFirst().orElse(null);
    }
    
    public List<Module> getModulesByCategory(final Category c) {
        final List<Module> modules = new ArrayList<Module>();
        final ModuleManager moduleManager = Astomero.instance.moduleManager;
        for (final Module m : ModuleManager.modules) {
            if (m.category == c) {
                modules.add(m);
            }
        }
        return modules;
    }
    
    public ArrayList<Module> getModules() {
        return ModuleManager.modules;
    }
    
    public Module getModuleByName(final String name) {
        return ModuleManager.modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    
    public final Module getModuleOrNull(final Class clazz) {
        final List modules = ModuleManager.modules;
        for (int i = 0, modulesSize = modules.size(); i < modulesSize; ++i) {
            final Module module = modules.get(i);
            if (module.getClass() == clazz) {
                return module;
            }
        }
        return null;
    }
    
    static {
        ModuleManager.modules = new ArrayList<Module>();
    }
}
