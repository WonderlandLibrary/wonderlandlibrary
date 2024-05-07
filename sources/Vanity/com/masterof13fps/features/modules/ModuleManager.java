package com.masterof13fps.features.modules;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.impl.combat.*;
import com.masterof13fps.features.modules.impl.exploits.*;
import com.masterof13fps.features.modules.impl.misc.*;
import com.masterof13fps.features.modules.impl.movement.*;
import com.masterof13fps.features.modules.impl.player.*;
import com.masterof13fps.features.modules.impl.render.*;
import com.masterof13fps.features.modules.impl.world.*;
import com.masterof13fps.utils.FileUtils;
import com.masterof13fps.features.modules.impl.gui.ClickGUI;
import com.masterof13fps.features.modules.impl.gui.HUD;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModuleManager {

    public ArrayList<Module> modules = new ArrayList<Module>();
    Minecraft mc = Minecraft.mc();
    private File modulesFile;
    private File bindsFile;

    public ModuleManager() {

        try {
            modulesFile = new File(Client.main().getClientDir() + "/modules.txt");
            if (modulesFile.createNewFile()) {
                System.out.println("File created: " + modulesFile.getName());
            } else {
                System.out.println("File \"modules.txt\" already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            bindsFile = new File(Client.main().getClientDir() + "/binds.txt");
            if (bindsFile.createNewFile()) {
                System.out.println("File created: " + bindsFile.getName());
            } else {
                System.out.println("File \"binds.txt\" already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        modules.add(new InventoryMove());
        modules.add(new MemoryCleaner());
        modules.add(new ChestStealer());
        modules.add(new NoScoreboard());
        modules.add(new SeekerFinder());
        modules.add(new ServerFucker());
        modules.add(new FlagDetector());
        modules.add(new AutoRespawn());
        modules.add(new MotionGraph());
        modules.add(new LagDetector());
        modules.add(new NameProtect());
        modules.add(new Fullbright());
        modules.add(new FastLadder());
        modules.add(new NoRotation());
        modules.add(new InvCleaner());
        modules.add(new DamageCalc());
        modules.add(new SendPublic());
        modules.add(new FastPlace());
        modules.add(new AutoArmor());
        modules.add(new DeathDerp());
        modules.add(new Criticals());
        modules.add(new AutoClimb());
        modules.add(new SlimeJump());
        modules.add(new BowAimbot());
        modules.add(new BlockInfo());
        modules.add(new ShopSaver());
        modules.add(new FastBreak());
        modules.add(new ChestAura());
        modules.add(new FastFall());
        modules.add(new Velocity());
        modules.add(new IceSpeed());
        modules.add(new AutoSoup());
        modules.add(new Scaffold());
        modules.add(new SafeWalk());
        modules.add(new Paralyze());
        modules.add(new ClickGUI());
        modules.add(new HighJump());
        modules.add(new AutoWalk());
        modules.add(new TrailESP());
        modules.add(new NameTags());
        modules.add(new WorldMod());
        modules.add(new Commands());
        modules.add(new AirStuck());
        modules.add(new AntiVoid());
        modules.add(new LongJump());
        modules.add(new FastUse());
        modules.add(new AntiWeb());
        modules.add(new Trigger());
        modules.add(new AirJump());
        modules.add(new FastBow());
        modules.add(new Parkour());
        modules.add(new Crasher());
        modules.add(new Tracers());
        modules.add(new AntiBot());
        modules.add(new Spammer());
        modules.add(new Plugins());
        modules.add(new Sprint());
        modules.add(new AimBot());
        modules.add(new Strafe());
        modules.add(new NoFall());
        modules.add(new Fucker());
        modules.add(new NoSlow());
        modules.add(new Speed());
        modules.add(new Jesus());
        modules.add(new Tower());
        modules.add(new Blink());
        modules.add(new NoBob());
        modules.add(new Phase());
        modules.add(new Eagle());
        modules.add(new Nuker());
        modules.add(new NoEXP());
        modules.add(new Step());
        modules.add(new Zoot());
        modules.add(new Aura());
        modules.add(new ESP());
        modules.add(new HUD());
        modules.add(new Fly());
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public Module getByName(String name) {
        for (Module mod : modules) {
            if ((mod.name().trim().equalsIgnoreCase(name.trim()))
                    || (mod.toString().trim().equalsIgnoreCase(name.trim()))) {
                return mod;
            }
        }

        return null;
    }

    public Module getModule(Class<? extends Module> clazz) {
        for (Module mod : getModules()) {
            if (mod.getClass() == clazz) {
                return mod;
            }
        }
        return null;
    }

    public void saveModules() {
        try {
            List<String> formattedModules = new ArrayList<String>();
            modules.forEach(module -> {
                formattedModules.add(module.name() + ":" + module.state());
            });
            FileUtils.saveFile(modulesFile, formattedModules);
        } catch (Exception ignored) {
        }
    }

    public void loadModules() {
        try {
            Objects.requireNonNull(FileUtils.loadFile(modulesFile)).forEach(line -> {
                final String[] args = line.split(":");
                if (args.length == 2) {
                    Module module = getByName(args[0]);
                    boolean state = Boolean.parseBoolean(args[1]);

                    if (state && !(module.getClass().equals(ClickGUI.class)))
                        module.setState(true);
                }
            });
        } catch (Exception ignored) {
        }
    }

    public void saveBinds() {
        try {
            List<String> formattedBinds = new ArrayList<String>();
            modules.forEach(module -> {
                formattedBinds.add(module.name() + ":" + module.bind());
            });
            FileUtils.saveFile(bindsFile, formattedBinds);
        } catch (Exception ignored) {
        }
    }

    public void loadBinds() {
        Objects.requireNonNull(FileUtils.loadFile(bindsFile)).forEach(line -> {
            final String[] args = line.split(":");
            if (args.length == 2) {
                Module module = getByName(args[0]);
                int bind = Integer.parseInt(args[1]);

                module.setBind(bind);
            }
        });
    }

    public File getModulesFile() {
        return modulesFile;
    }

    public File getBindsFile() {
        return bindsFile;
    }
}