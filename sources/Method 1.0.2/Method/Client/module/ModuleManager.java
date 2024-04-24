package Method.Client.module;

import Method.Client.Main;
import Method.Client.module.ModMaker.AddModule;
import Method.Client.module.Onscreen.Display.*;
import Method.Client.module.combat.*;
import Method.Client.module.misc.*;
import Method.Client.module.movement.AutoHold;
import Method.Client.module.movement.InvMove;
import Method.Client.module.movement.Parkour;
import Method.Client.module.movement.Teleport;
import Method.Client.module.player.*;
import Method.Client.module.render.*;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {

    public static ArrayList<Module> modules = new ArrayList<>();
    public static ArrayList<Module> toggledModules = new ArrayList<>();
    public static ArrayList<Module> FileManagerLoader = new ArrayList<>();


    public ModuleManager() {
        /////////////////
        //// CHAT ///////
        /////////////////
        addModule(new Antispam());
        addModule(new ChatMutator());
        addModule(new TimeStamp());
        /////////////////
        //// Combat /////
        /////////////////
        addModule(new AntiBot());
        addModule(new AutoArmor());
        addModule(new BowMod());
        addModule(new InteractClick());
        /////////////////
        //// MISC ///////
        /////////////////
        addModule(new AntiCheat());
        addModule(new Antipacket());
        addModule(new AntiHandShake());
        addModule(new AutoNametag());
        Main.playerHistory = new PlayerHistory();
        addModule(Main.playerHistory);
        addModule(new EchestBP());
        addModule(new GuiModule());
        addModule(new InvMove());
        addModule(new ToolTipPlus());
        addModule(new NbtView());
        addModule(new ModSettings());
        addModule(new VersionSpoofer());
        addModule(new PluginsGetter());
        addModule(new GuiPeek());
        addModule(new Shulkerspy());
        addModule(new ServerCrash());
        addModule(new VanishDetector());
        /////////////////
        //// Movement ///
        /////////////////
        addModule(new AutoHold());
        addModule(new Parkour());
        addModule(new Teleport());
        /////////////////
        //// Onscreen ///
        /////////////////
        addModule(new Armor());
        addModule(new Biome());
        addModule(new Coords());
        addModule(new ChunkSize());
        addModule(new Direction());
        addModule(new Durability());
        addModule(new EnabledMods());
        addModule(new Enemypos());
        addModule(new KeyStroke());
        addModule(new Fps());
        addModule(new CombatItems());
        addModule(new Angles());
        addModule(new Blockview());
        addModule(new Hole());
        addModule(new Hunger());
        addModule(new Inventory());
        addModule(new NetherCords());
        addModule(new Ping());
        addModule(new Player());
        addModule(new PlayerCount());
        addModule(new PlayerSpeed());
        addModule(new Potions());
        addModule(new Server());
        addModule(new ServerResponce());
        addModule(new Time());
        addModule(new Tps());
        ////////////////
        //// Player ////
        ////////////////
        addModule(new AutoFish());
        addModule(new ChestStealer());
        addModule(new FoodMod());
        addModule(new Nowall());
        addModule(new Nuker());

        ////////////////
        //// Render ////
        ////////////////
        addModule(new Breadcrumb());
        addModule(new BossStack());
        addModule(new ChorusLocation());
        addModule(new ESP());
        addModule(new ExtraTab());
        addModule(new ArmorRender());
        addModule(new CrystalRender());
        addModule(new SeedViewer());
        addModule(new MotionBlur());
        addModule(new Signchanger());
        addModule(new NetherSky());
        addModule(new Search());
        addModule(new Trajectories());
        Main.xray = new Xray();
        addModule(Main.xray);


        ////////////////
        //// PROFILES //
        ////////////////

        // See filemanager

        ////////////////
        //// MAKER /////
        ////////////////
        addModule(new AddModule());


    }

    public static void addModule(Module m) {
        if (!modules.contains(m)) {
            if (modules.stream().noneMatch(module -> module.getName().equalsIgnoreCase(m.getName()))) {
                modules.add(m);
            }
        }
    }

    public static ArrayList<Module> getModules() {
        return modules;
    }

    public static ArrayList<Module> getEnabledmodules() {
        return toggledModules;
    }


    public static void onKeyPressed(int key) {
        for (Module m : modules) {
            int Keydiff = 0;
            for (Integer mKey : m.getKeys()) {
                if (mKey != Keyboard.KEY_LCONTROL && mKey != Keyboard.KEY_LSHIFT && mKey != Keyboard.KEY_LMENU)
                    Keydiff = mKey;
            }
            if (key == Keydiff) {
                if (m.getKeys().contains(Keyboard.KEY_LCONTROL))
                    if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
                        return;
                if (m.getKeys().contains(Keyboard.KEY_LSHIFT))
                    if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                        return;
                if (m.getKeys().contains(Keyboard.KEY_LMENU))
                    if (!Keyboard.isKeyDown(Keyboard.KEY_LMENU))
                        return;
                m.toggle();
            }
        }
    }

    public static Module getModuleByName(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static ArrayList<Module> getModulesInCategory(Category categoryIn) {
        ArrayList<Module> mods = new ArrayList<>();
        for (Module m : getsortedabc(false)) {
            if (m.getCategory() == categoryIn)
                mods.add(m);
        }
        return mods;
    }

    public static List<Module> getSortedMods(boolean reverse, boolean enabled, boolean visible) {
        List<Module> modules = enabled ? getEnabledmodules() : getModules();

        List<String> moduleNames = modules.stream()
                .filter(mod -> visible == mod.visible)
                .map(Module::getName)
                .sorted(Comparator.comparing(String::length))
                .collect(Collectors.toList());

        if (reverse) {
            Collections.reverse(moduleNames);
        }

        return moduleNames.stream()
                .map(ModuleManager::getModuleByName)
                .collect(Collectors.toList());
    }

    public static ArrayList<Module> getsortedabc(boolean reverse) {
        final ArrayList<Module> list = new ArrayList<>();
        final ArrayList<String> modules = new ArrayList<>();
        for (final Module module : getModules()) {
            if (module.getName() != null) {  // checking if module name is null
                modules.add(module.getName());
            }
        }
        // if null or empty, return unsorted list
        if (modules.isEmpty()) {
            return list;
        }
        Collections.sort(modules);
        if (reverse) {
            Collections.reverse(modules);
        }
        for (final String s : modules) {
            list.add(ModuleManager.getModuleByName(s));
        }
        return list;
    }




}
