package net.ccbluex.LiquidBase.module;

import net.ccbluex.LiquidBase.event.EventListener;
import net.ccbluex.LiquidBase.event.EventTarget;
import net.ccbluex.LiquidBase.event.events.KeyEvent;
import net.ccbluex.LiquidBase.module.modules.combat.KillAura;
import net.ccbluex.LiquidBase.module.modules.movement.Speed;
import net.ccbluex.LiquidBase.module.modules.movement.Sprint;
import net.ccbluex.LiquidBase.module.modules.render.ClickGUI;
import net.ccbluex.LiquidBase.module.modules.render.HUD;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
@SideOnly(Side.CLIENT)
public class ModuleManager implements EventListener {

    private static final List<Module> modules = new ArrayList<>();

    public void registerModules() {
        registerModule(new HUD());
        registerModule(new KillAura());
        registerModule(new Speed());
        registerModule(new ClickGUI());
        registerModule(new Sprint());
    }

    public void registerModule(final Module module) {
        modules.add(module);
    }

    public static Module getModule(final Class<? extends Module> targetModule) {
        synchronized(modules) {
            for(final Module currentModule : modules)
                if(currentModule.getClass() == targetModule)
                    return currentModule;
        }

        return null;
    }

    public static Module getModule(final String targetModule) {
        synchronized(modules) {
            for(final Module currentModule : modules)
                if(currentModule.getModuleName().equalsIgnoreCase(targetModule))
                    return currentModule;
        }

        return null;
    }

    public static List<Module> getModules() {
        return modules;
    }

    @EventTarget
    public void onKey(KeyEvent event) {
        synchronized(modules) {
            modules.stream().filter(module -> module.getKeyBind() == event.getKey()).forEach(module -> module.setState(!module.getState()));
        }
    }
}
