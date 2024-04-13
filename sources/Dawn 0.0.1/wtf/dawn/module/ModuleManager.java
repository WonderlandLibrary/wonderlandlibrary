package wtf.dawn.module;

import wtf.dawn.module.impl.combat.KillAura;
import wtf.dawn.module.impl.combat.Velocity;
import wtf.dawn.module.impl.ghost.AutoClicker;
import wtf.dawn.module.impl.movement.*;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager {
    public static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<>();

    public ModuleManager() {
        addAll(
                //Combat

                new Velocity(),
                new KillAura(),

                //Movement

                new Flight(),
                new Sprint(),
                new Speed(),
                new LongJump(),
                new HighJump(),
                //Player


                //Ghost
                new AutoClicker()


                //Visual


                //Player


                //Misc
        );

    }

    public void addAll(Module... modules) {
        for(Module m : modules)
        this.modules.add(m);
    }

    public CopyOnWriteArrayList<Module> getModules() {
        return modules;
    }

    public ArrayList<Module> getModulesInCategory(Category categoryIn) {
        ArrayList<Module> mods = new ArrayList<Module>();
        for(Module m : this.modules) {
            if(m.getCategory() == categoryIn) {
                mods.add(m);
            }
        }
        return mods;
    }
}
