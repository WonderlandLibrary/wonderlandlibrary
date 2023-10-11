/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module;

import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.modules.AirMove;
import cow.milkgod.cheese.module.modules.AntiCactus;
import cow.milkgod.cheese.module.modules.AntiHunger;
import cow.milkgod.cheese.module.modules.Antivelocity;
import cow.milkgod.cheese.module.modules.AutoAccept;
import cow.milkgod.cheese.module.modules.AutoApple;
import cow.milkgod.cheese.module.modules.AutoArmor;
import cow.milkgod.cheese.module.modules.AutoPot;
import cow.milkgod.cheese.module.modules.AutoSoup;
import cow.milkgod.cheese.module.modules.BetterChat;
import cow.milkgod.cheese.module.modules.Blink;
import cow.milkgod.cheese.module.modules.CheeseClip;
import cow.milkgod.cheese.module.modules.CheeseSpeed;
import cow.milkgod.cheese.module.modules.CheeseTower;
import cow.milkgod.cheese.module.modules.CheeseWalker;
import cow.milkgod.cheese.module.modules.ChestStealer;
import cow.milkgod.cheese.module.modules.CivBreak;
import cow.milkgod.cheese.module.modules.Criticals;
import cow.milkgod.cheese.module.modules.ESP;
import cow.milkgod.cheese.module.modules.FastBow;
import cow.milkgod.cheese.module.modules.FastLadder;
import cow.milkgod.cheese.module.modules.Fastplace;
import cow.milkgod.cheese.module.modules.Firion;
import cow.milkgod.cheese.module.modules.FreeView;
import cow.milkgod.cheese.module.modules.Freecam;
import cow.milkgod.cheese.module.modules.FullBright;
import cow.milkgod.cheese.module.modules.Glide;
import cow.milkgod.cheese.module.modules.Gobble;
import cow.milkgod.cheese.module.modules.HOLYSHITOAIJDAS;
import cow.milkgod.cheese.module.modules.Inventorymove;
import cow.milkgod.cheese.module.modules.Inventoryplus;
import cow.milkgod.cheese.module.modules.Jesus;
import cow.milkgod.cheese.module.modules.Killaura;
import cow.milkgod.cheese.module.modules.MiddleClickFriend;
import cow.milkgod.cheese.module.modules.MouseFix;
import cow.milkgod.cheese.module.modules.Nametags;
import cow.milkgod.cheese.module.modules.NoFall;
import cow.milkgod.cheese.module.modules.NoRender;
import cow.milkgod.cheese.module.modules.NoRotate;
import cow.milkgod.cheese.module.modules.NoSlowDown;
import cow.milkgod.cheese.module.modules.Paralyze;
import cow.milkgod.cheese.module.modules.Phase;
import cow.milkgod.cheese.module.modules.Potionsaver;
import cow.milkgod.cheese.module.modules.Regen;
import cow.milkgod.cheese.module.modules.Respawn;
import cow.milkgod.cheese.module.modules.Search;
import cow.milkgod.cheese.module.modules.SkinDerp;
import cow.milkgod.cheese.module.modules.Sneak;
import cow.milkgod.cheese.module.modules.SoupGhost;
import cow.milkgod.cheese.module.modules.SpeedyGonzales;
import cow.milkgod.cheese.module.modules.Sprint;
import cow.milkgod.cheese.module.modules.Step;
import cow.milkgod.cheese.module.modules.StorageESP;
import cow.milkgod.cheese.module.modules.Teleport;
import cow.milkgod.cheese.module.modules.TickHop;
import cow.milkgod.cheese.module.modules.TickRegen;
import cow.milkgod.cheese.module.modules.Tracers;
import cow.milkgod.cheese.module.modules.TriggerBot;
import cow.milkgod.cheese.module.modules.Zoot;
import java.util.ArrayList;

public class ModuleManager {
    public static ArrayList<Module> activeModules = new ArrayList();

    public ModuleManager() {
        activeModules.add(new Sprint());
        activeModules.add(new NoFall());
        activeModules.add(new FullBright());
        activeModules.add(new BetterChat());
        activeModules.add(new NoSlowDown());
        activeModules.add(new Nametags());
        activeModules.add(new Teleport());
        activeModules.add(new CheeseWalker());
        activeModules.add(new Criticals());
        activeModules.add(new AutoPot());
        activeModules.add(new CivBreak());
        activeModules.add(new Search());
        activeModules.add(new ESP());
        activeModules.add(new Blink());
        activeModules.add(new NoRotate());
        activeModules.add(new Firion());
        activeModules.add(new Sneak());
        activeModules.add(new Potionsaver());
        activeModules.add(new SpeedyGonzales());
        activeModules.add(new NoRender());
        activeModules.add(new Fastplace());
        activeModules.add(new Gobble());
        activeModules.add(new Antivelocity());
        activeModules.add(new Inventorymove());
        activeModules.add(new Jesus());
        activeModules.add(new Phase());
        activeModules.add(new Glide());
        activeModules.add(new AutoSoup());
        activeModules.add(new AutoArmor());
        activeModules.add(new TriggerBot());
        activeModules.add(new FastBow());
        activeModules.add(new Step());
        activeModules.add(new AutoAccept());
        activeModules.add(new Regen());
        activeModules.add(new AutoApple());
        activeModules.add(new TickHop());
        activeModules.add(new Tracers());
        activeModules.add(new StorageESP());
        activeModules.add(new CheeseClip());
        activeModules.add(new AirMove());
        activeModules.add(new Freecam());
        activeModules.add(new Inventoryplus());
        activeModules.add(new AntiCactus());
        activeModules.add(new AntiHunger());
        activeModules.add(new Respawn());
        activeModules.add(new SkinDerp());
        activeModules.add(new Paralyze());
        activeModules.add(new ChestStealer());
        activeModules.add(new FastLadder());
        activeModules.add(new TickRegen());
        activeModules.add(new CheeseTower());
        activeModules.add(new CheeseSpeed());
        activeModules.add(new Zoot());
        activeModules.add(new HOLYSHITOAIJDAS());
        activeModules.add(new SoupGhost());
        activeModules.add(new FreeView());
        activeModules.add(new Killaura());
        activeModules.add(new MiddleClickFriend());
        MouseFix fix = new MouseFix();
        activeModules.add(fix);
    }

    public Module getModule(Class<? extends Module> clazz) {
        for (Module mod : ModuleManager.getModules()) {
            if (mod.getClass() != clazz) continue;
            return mod;
        }
        return null;
    }

    public static void arrayListSorter(String order) {
    }

    public static ArrayList<Module> getModules() {
        return activeModules;
    }

    public Module getModbyName(String name) {
        for (Module mod : ModuleManager.getModules()) {
            if (!mod.getName().equalsIgnoreCase(name)) continue;
            return mod;
        }
        return null;
    }

    public boolean getModState(String name) {
        for (Module mod : ModuleManager.getModules()) {
            if (mod.getName() != name) continue;
            return mod.getState();
        }
        return false;
    }

    public Module getModByAlias(String alias) {
        for (Module mod : ModuleManager.getModules()) {
            String[] alias2 = mod.getAlias();
            int length = alias2.length;
            int i2 = 0;
            while (i2 < length) {
                String aliases = alias2[i2];
                if (aliases.equalsIgnoreCase(alias)) {
                    return mod;
                }
                ++i2;
            }
        }
        return null;
    } 

    public ArrayList<Module> enabledModules() {
        ArrayList<Module> emods = new ArrayList<Module>();
        for (Module m2 : activeModules) {
            if (m2.getState()) {
                emods.add(m2);
                continue;
            }
            if (m2.getState() || !emods.contains(m2)) continue;
            emods.remove(m2);
        }
        return emods;
    }
}

