// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules;

import Lavish.Client;
import java.util.ArrayList;
import java.util.Iterator;
import Lavish.modules.exploit.Blink;
import Lavish.modules.exploit.Retard;
import Lavish.modules.exploit.PingSpoof;
import Lavish.modules.exploit.ChatBypass;
import Lavish.modules.exploit.Disabler;
import Lavish.modules.misc.Breaker;
import Lavish.modules.misc.Freecam;
import Lavish.modules.misc.UnfocusCPU;
import Lavish.modules.render.Fullbright;
import Lavish.modules.render.ESP;
import Lavish.modules.render.TargetHUD;
import Lavish.modules.render.Cape;
import Lavish.modules.render.Animation;
import Lavish.modules.render.ClickGUI;
import Lavish.modules.render.HUD;
import Lavish.modules.player.NoSlow;
import Lavish.modules.player.FastUse;
import Lavish.modules.player.AutoHeal;
import Lavish.modules.player.Timer;
import Lavish.modules.player.Nofall;
import Lavish.modules.player.Step;
import Lavish.modules.player.InvManager;
import Lavish.modules.player.AutoArmor;
import Lavish.modules.player.ChestStealer;
import Lavish.modules.player.Scaffold;
import Lavish.modules.movement.LongJump;
import Lavish.modules.movement.Phase;
import Lavish.modules.movement.Teleport;
import Lavish.modules.movement.Speed;
import Lavish.modules.movement.Sprint;
import Lavish.modules.movement.Fly;
import Lavish.modules.combat.Antibot;
import Lavish.modules.combat.Velocity;
import Lavish.modules.combat.Killaura;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager
{
    public CopyOnWriteArrayList<Module> modules;
    
    public ModuleManager() {
        (this.modules = new CopyOnWriteArrayList<Module>()).add(new Killaura());
        this.modules.add(new Velocity());
        this.modules.add(new Antibot());
        this.modules.add(new Fly());
        this.modules.add(new Sprint());
        this.modules.add(new Speed());
        this.modules.add(new Teleport());
        this.modules.add(new Phase());
        this.modules.add(new LongJump());
        this.modules.add(new Scaffold());
        this.modules.add(new ChestStealer());
        this.modules.add(new AutoArmor());
        this.modules.add(new InvManager());
        this.modules.add(new Step());
        this.modules.add(new Nofall());
        this.modules.add(new Timer());
        this.modules.add(new AutoHeal());
        this.modules.add(new FastUse());
        this.modules.add(new NoSlow());
        this.modules.add(new HUD());
        this.modules.add(new ClickGUI());
        this.modules.add(new Animation());
        this.modules.add(new Cape());
        this.modules.add(new TargetHUD());
        this.modules.add(new ESP());
        this.modules.add(new Fullbright());
        this.modules.add(new UnfocusCPU());
        this.modules.add(new Freecam());
        this.modules.add(new Breaker());
        this.modules.add(new Disabler());
        this.modules.add(new ChatBypass());
        this.modules.add(new PingSpoof());
        this.modules.add(new Retard());
        this.modules.add(new Blink());
    }
    
    public CopyOnWriteArrayList<Module> getModules() {
        return this.modules;
    }
    
    public CopyOnWriteArrayList<Module> getEnabledModules() {
        final CopyOnWriteArrayList<Module> enabledModules = new CopyOnWriteArrayList<Module>();
        for (final Module m : this.getModules()) {
            if (m.isEnabled()) {
                enabledModules.add(m);
            }
        }
        return enabledModules;
    }
    
    public ArrayList<Module> getModulesInCategory(final Category categoryIn) {
        final ArrayList<Module> mods = new ArrayList<Module>();
        for (final Module m : Client.instance.moduleManager.getModules()) {
            if (m.getCategory() == categoryIn) {
                mods.add(m);
            }
        }
        return mods;
    }
    
    public Module getModuleByName(final String name) {
        for (final Module m : this.getModules()) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }
    
    private void addModule(final Module module) {
        this.modules.add(module);
    }
}
