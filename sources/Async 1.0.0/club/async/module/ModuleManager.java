package club.async.module;

import club.async.module.impl.combat.*;
import club.async.module.impl.movement.*;
import club.async.module.impl.hud.*;
import club.async.module.impl.player.*;
import club.async.module.impl.visual.*;

import java.util.ArrayList;

public class ModuleManager {

    private ArrayList<Module> modules = new ArrayList<>();
    private ArrayList<Module> moduleListMods = new ArrayList<>();

    public ModuleManager() {
        initModules();
        moduleListMods.addAll(modules);
    }

    /*
    Initializing all the Modules
    */
    private void initModules() {
        // Combat
        add(new KillAura());
        add(new TargetStrafe());
        add(new Velocity());

        // Movement
        add(new Speed());
        add(new Sprint());
        add(new Flight());
        add(new InvMove());
        add(new NoSlowDown());
        add(new LongJump());

        // Player
        add(new NoFall());
        add(new Disabler());
        add(new NoRotate());
        add(new Scaffold());
        add(new AntiVoid());
        add(new AutoRespawn());
        add(new BedBreaker());
        add(new Test());
        add(new ChestStealer());

        // Visual
        add(new NoBob());
        add(new NoFov());
        add(new NoHurtcam());
        add(new Ambiance());
        add(new Animations());

        // Hud
        add(new ClickGui());
        add(new Color());
        add(new WaterMark());
        add(new ModuleList());
    }

    private void add(Module module) {
        modules.add(module);
    }
    public final ArrayList<Module> getModules() {
        return modules;
    }
    public final ArrayList<Module> getModules(Category category) {
        ArrayList<Module> modules = new ArrayList<>();
        for (Module m : this.modules) {
            if (m.getCategory() == category)
                modules.add(m);
        }
        return modules;
    }
    public final ArrayList<Module> getModules(String name) {
        ArrayList<Module> modules = new ArrayList<>();
        for (Module m : this.modules) {
            if (m.getName().toLowerCase().contains(name.toLowerCase()))
                modules.add(m);
        }
        return modules;
    }
    public final Module getModule(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public boolean isToggled(Class tClass) {
        return moduleBy(tClass) != null && moduleBy(tClass).isEnabled();
    }

    public <T extends Module> T moduleBy(Class<T> tClass) {
        return (T) modules.stream().filter(mod -> mod.getClass().equals(tClass)).findFirst().orElse(null);
    }

    public final ArrayList<Module> getModuleListMods() {
        return moduleListMods;
    }

}