package intent.AquaDev.aqua.modules;

import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.combat.AntiFireball;
import intent.AquaDev.aqua.modules.combat.Backtrack;
import intent.AquaDev.aqua.modules.combat.Killaura;
import intent.AquaDev.aqua.modules.combat.TimerRange;
import intent.AquaDev.aqua.modules.combat.Velocity;
import intent.AquaDev.aqua.modules.ghost.AutoClicker;
import intent.AquaDev.aqua.modules.ghost.Triggerbot;
import intent.AquaDev.aqua.modules.misc.AutoHypixel;
import intent.AquaDev.aqua.modules.misc.Disabler;
import intent.AquaDev.aqua.modules.movement.Fly;
import intent.AquaDev.aqua.modules.movement.Longjump;
import intent.AquaDev.aqua.modules.movement.NoSlow;
import intent.AquaDev.aqua.modules.movement.Speed;
import intent.AquaDev.aqua.modules.movement.Sprint;
import intent.AquaDev.aqua.modules.movement.Step;
import intent.AquaDev.aqua.modules.movement.TargetStrafe;
import intent.AquaDev.aqua.modules.player.ChestStealer;
import intent.AquaDev.aqua.modules.player.InvManager;
import intent.AquaDev.aqua.modules.player.InvMove;
import intent.AquaDev.aqua.modules.player.MCF;
import intent.AquaDev.aqua.modules.player.Nofall;
import intent.AquaDev.aqua.modules.visual.Animations;
import intent.AquaDev.aqua.modules.visual.Arraylist;
import intent.AquaDev.aqua.modules.visual.Blur;
import intent.AquaDev.aqua.modules.visual.CustomChat;
import intent.AquaDev.aqua.modules.visual.CustomHitEffekt;
import intent.AquaDev.aqua.modules.visual.CustomHotbar;
import intent.AquaDev.aqua.modules.visual.CustomScoreboard;
import intent.AquaDev.aqua.modules.visual.ESP;
import intent.AquaDev.aqua.modules.visual.FakeBlock;
import intent.AquaDev.aqua.modules.visual.GUI;
import intent.AquaDev.aqua.modules.visual.Glint;
import intent.AquaDev.aqua.modules.visual.GuiElements;
import intent.AquaDev.aqua.modules.visual.HUD;
import intent.AquaDev.aqua.modules.visual.HeldItem;
import intent.AquaDev.aqua.modules.visual.KeyStrokes;
import intent.AquaDev.aqua.modules.visual.NoHurtCam;
import intent.AquaDev.aqua.modules.visual.SessionInfo;
import intent.AquaDev.aqua.modules.visual.Shadow;
import intent.AquaDev.aqua.modules.visual.TargetHUD;
import intent.AquaDev.aqua.modules.visual.WorldColor;
import intent.AquaDev.aqua.modules.visual.WorldTime;
import intent.AquaDev.aqua.modules.world.Eagle;
import intent.AquaDev.aqua.modules.world.Scaffold;
import intent.AquaDev.aqua.utils.UnicodeFontRenderer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModuleManager {
    public List<Module> modules = new ArrayList();
    public UnicodeFontRenderer fontRenderer;

    public ModuleManager() {
        this.addModule((Module)new WorldColor());
        this.addModule((Module)new Nofall());
        this.addModule((Module)new Killaura());
        this.addModule((Module)new Velocity());
        this.addModule((Module)new Sprint());
        this.addModule((Module)new AutoClicker());
        this.addModule((Module)new Blur());
        this.addModule((Module)new Shadow());
        this.addModule((Module)new Arraylist());
        this.addModule((Module)new ESP());
        this.addModule((Module)new TargetHUD());
        this.addModule((Module)new SessionInfo());
        this.addModule((Module)new HUD());
        this.addModule((Module)new GUI());
        this.addModule((Module)new Eagle());
        this.addModule((Module)new Animations());
        this.addModule((Module)new AutoHypixel());
        this.addModule((Module)new Triggerbot());
        this.addModule((Module)new Fly());
        this.addModule((Module)new Disabler());
        this.addModule((Module)new Longjump());
        this.addModule((Module)new Speed());
        this.addModule((Module)new WorldTime());
        this.addModule((Module)new NoSlow());
        this.addModule((Module)new MCF());
        this.addModule((Module)new FakeBlock());
        this.addModule((Module)new ChestStealer());
        this.addModule((Module)new Scaffold());
        this.addModule((Module)new InvMove());
        this.addModule((Module)new CustomScoreboard());
        this.addModule((Module)new CustomChat());
        this.addModule((Module)new InvManager());
        this.addModule((Module)new GuiElements());
        this.addModule((Module)new TargetStrafe());
        this.addModule((Module)new Step());
        this.addModule((Module)new Glint());
        this.addModule((Module)new KeyStrokes());
        this.addModule((Module)new CustomHitEffekt());
        this.addModule((Module)new NoHurtCam());
        this.addModule((Module)new TimerRange());
        this.addModule((Module)new Backtrack());
        this.addModule((Module)new HeldItem());
        this.addModule((Module)new AntiFireball());
        this.addModule((Module)new CustomHotbar());
        System.out.println(this.modules + "true");
    }

    public void addModule(Module module) {
        this.modules.add(module);
    }

    public List<Module> getModules() {
        return this.modules;
    }

    public List<Module> getModulesOrdered(Category category, boolean length, UnicodeFontRenderer fontRenderer) {
        ArrayList mods = new ArrayList();
        for (Module module : this.getModules()) {
            if (module.getCategory() != category) continue;
            mods.add((Object)module);
        }
        if (length) {
            mods.sort((Comparator)new ModuelComparator(Aqua.INSTANCE.comfortaa));
        }
        return mods;
    }

    public Module getModuleByName(String moduleName) {
        for (Module mod : this.modules) {
            if (!mod.getName().trim().equalsIgnoreCase(moduleName) && !mod.toString().trim().equalsIgnoreCase(moduleName.trim())) continue;
            return mod;
        }
        return null;
    }

    public Module getModule(Class<? extends Module> clazz) {
        for (Module m : this.modules) {
            if (m.getClass() != clazz) continue;
            return m;
        }
        return null;
    }

   public class ModuelComparator implements Comparator<Module> {
        private UnicodeFontRenderer fontRenderer;

        public ModuelComparator(UnicodeFontRenderer fontRenderer) {
            this.fontRenderer = fontRenderer;
        }

       public int compare(Module o1, Module o2) {
           return -Integer.compare((int)this.fontRenderer.getStringWidth(o1.getDisplayname()), (int)this.fontRenderer.getStringWidth(o2.getDisplayname()));
       }
    }
}
