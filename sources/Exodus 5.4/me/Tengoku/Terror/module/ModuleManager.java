/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module;

import java.util.ArrayList;
import java.util.List;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.module.combat.AntiBot;
import me.Tengoku.Terror.module.combat.AutoArmor;
import me.Tengoku.Terror.module.combat.AutoGapple;
import me.Tengoku.Terror.module.combat.Criticals;
import me.Tengoku.Terror.module.combat.KillAura;
import me.Tengoku.Terror.module.combat.Velocity;
import me.Tengoku.Terror.module.hidden.TabGUI;
import me.Tengoku.Terror.module.misc.AntiFlag;
import me.Tengoku.Terror.module.misc.AutoHypixelJoin;
import me.Tengoku.Terror.module.misc.AutoReport;
import me.Tengoku.Terror.module.misc.AutoTool;
import me.Tengoku.Terror.module.misc.Disabler;
import me.Tengoku.Terror.module.misc.FastMine;
import me.Tengoku.Terror.module.misc.Regen;
import me.Tengoku.Terror.module.misc.Spam;
import me.Tengoku.Terror.module.misc.Timer;
import me.Tengoku.Terror.module.movement.Fly;
import me.Tengoku.Terror.module.movement.LongJump;
import me.Tengoku.Terror.module.movement.SafeWalk;
import me.Tengoku.Terror.module.movement.Speed;
import me.Tengoku.Terror.module.movement.Sprint;
import me.Tengoku.Terror.module.movement.Step;
import me.Tengoku.Terror.module.movement.TargetStrafe;
import me.Tengoku.Terror.module.player.AntiAim;
import me.Tengoku.Terror.module.player.AntiDesync;
import me.Tengoku.Terror.module.player.AntiVoid;
import me.Tengoku.Terror.module.player.AutoPot;
import me.Tengoku.Terror.module.player.ChestStealer;
import me.Tengoku.Terror.module.player.FastPlace;
import me.Tengoku.Terror.module.player.InventoryCleaner;
import me.Tengoku.Terror.module.player.InventoryMove;
import me.Tengoku.Terror.module.player.NoFall;
import me.Tengoku.Terror.module.player.NoSlow;
import me.Tengoku.Terror.module.player.Phase;
import me.Tengoku.Terror.module.render.Animations;
import me.Tengoku.Terror.module.render.Blur;
import me.Tengoku.Terror.module.render.Bob;
import me.Tengoku.Terror.module.render.Chams;
import me.Tengoku.Terror.module.render.ClickGUI;
import me.Tengoku.Terror.module.render.ESP;
import me.Tengoku.Terror.module.render.FullBright;
import me.Tengoku.Terror.module.render.HUD;
import me.Tengoku.Terror.module.render.HeadRotations;
import me.Tengoku.Terror.module.render.NameTags;
import me.Tengoku.Terror.module.render.NoHurtCam;
import me.Tengoku.Terror.module.render.NoRotate;
import me.Tengoku.Terror.module.render.Radar;
import me.Tengoku.Terror.module.render.Scoreboard;
import me.Tengoku.Terror.module.render.SessionStats;
import me.Tengoku.Terror.module.render.TargetHUD;
import me.Tengoku.Terror.module.render.TimeChanger;
import me.Tengoku.Terror.module.render.Tracers;
import me.Tengoku.Terror.module.skyblock.AutoBegger;
import me.Tengoku.Terror.module.skyblock.AutoFarm;
import me.Tengoku.Terror.module.skyblock.AutoScam;
import me.Tengoku.Terror.module.skyblock.AutoSuperiorFinder;
import me.Tengoku.Terror.module.world.FastLadder;
import me.Tengoku.Terror.module.world.Scaffold;

public class ModuleManager {
    private ArrayList<Module> modules = new ArrayList();

    public ModuleManager() {
        this.modules.add(new Fly());
        this.modules.add(new Scoreboard());
        this.modules.add(new Step());
        this.modules.add(new Blur());
        this.modules.add(new TabGUI());
        this.modules.add(new FastLadder());
        this.modules.add(new SessionStats());
        this.modules.add(new TimeChanger());
        this.modules.add(new AutoPot());
        this.modules.add(new AntiDesync());
        this.modules.add(new AutoScam());
        this.modules.add(new Sprint());
        this.modules.add(new Tracers());
        this.modules.add(new AutoSuperiorFinder());
        this.modules.add(new AutoBegger());
        this.modules.add(new NoFall());
        this.modules.add(new AutoReport());
        this.modules.add(new Regen());
        this.modules.add(new AutoFarm());
        this.modules.add(new Animations());
        this.modules.add(new FullBright());
        this.modules.add(new FastPlace());
        this.modules.add(new NoHurtCam());
        this.modules.add(new Bob());
        this.modules.add(new ChestStealer());
        this.modules.add(new AutoTool());
        this.modules.add(new HeadRotations());
        this.modules.add(new KillAura());
        this.modules.add(new TargetStrafe());
        this.modules.add(new NameTags());
        this.modules.add(new AutoArmor());
        this.modules.add(new Spam());
        this.modules.add(new AntiAim());
        this.modules.add(new TargetHUD());
        this.modules.add(new NoRotate());
        this.modules.add(new AntiFlag());
        this.modules.add(new Chams());
        this.modules.add(new Timer());
        this.modules.add(new Speed());
        this.modules.add(new FastMine());
        this.modules.add(new HUD());
        this.modules.add(new AntiVoid());
        this.modules.add(new ClickGUI());
        this.modules.add(new Disabler());
        this.modules.add(new InventoryCleaner());
        this.modules.add(new InventoryMove());
        this.modules.add(new Scaffold());
        this.modules.add(new LongJump());
        this.modules.add(new Phase());
        this.modules.add(new Radar());
        this.modules.add(new Velocity());
        this.modules.add(new AntiBot());
        this.modules.add(new ESP());
        this.modules.add(new SafeWalk());
        this.modules.add(new Criticals());
        this.modules.add(new AutoGapple());
        this.modules.add(new NoSlow());
        this.modules.add(new AutoHypixelJoin());
    }

    public List<Module> getModulesByCategory(Category category) {
        ArrayList<Module> arrayList = new ArrayList<Module>();
        for (Module module : this.modules) {
            if (module.getCategory() != category) continue;
            arrayList.add(module);
        }
        return arrayList;
    }

    public Module getModuleByName(String string) {
        return this.modules.stream().filter(module -> module.getName().equalsIgnoreCase(string)).findFirst().orElse(null);
    }

    public Module getModuleByClass(Class clazz) {
        return this.modules.stream().filter(module -> module.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public ArrayList<Module> getModules() {
        return this.modules;
    }
}

