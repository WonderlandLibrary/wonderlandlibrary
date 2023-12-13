package koks.manager.module;

import koks.Koks;
import koks.manager.module.impl.combat.*;
import koks.manager.module.impl.debug.*;
import koks.manager.module.impl.gui.*;
import koks.manager.module.impl.movement.*;
import koks.manager.module.impl.player.*;
import koks.manager.module.impl.render.*;
import koks.manager.module.impl.utilities.Disabler;
import koks.manager.module.impl.world.*;
import koks.manager.cl.Role;

import java.util.*;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:37
 */
public class ModuleManager {

    public ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        addModule(new Criticals());
        addModule(new FastBow());
        addModule(new Friends());
        addModule(new KillAura());
        addModule(new Rotations());
        addModule(new SuperHit());
        addModule(new Teams());
        addModule(new TNTBlock());
        addModule(new Velocity());
        addModule(new Debug());
        addModule(new Test());
        addModule(new ClickGui());
        addModule(new HUD());
        addModule(new BoatFly());
        addModule(new Fly());
        addModule(new InvMove());
        addModule(new Jesus());
        addModule(new NoCobweb());
        addModule(new NoFall());
        addModule(new NoSlowdown());
        addModule(new Speed());
        addModule(new Sprint());
        addModule(new StairSpeed());
        addModule(new Step());
        addModule(new VClip());
        addModule(new AutoArmor());
        addModule(new ChestStealer());
        addModule(new CivBreak());
        addModule(new FastPlace());
        addModule(new GodMode());
        addModule(new InventoryCleaner());
        addModule(new Phase());
        addModule(new SendPublic());
        addModule(new SetBack());
        addModule(new Teleport());
        addModule(new BlockOverlay());
        addModule(new CameraClip());
        addModule(new ChestESP());
        addModule(new CustomItem());
        addModule(new FakeRotations());
        addModule(new ItemESP());
        addModule(new MemoryCleaner());
        addModule(new NameProtect());
        addModule(new NameTags());
        addModule(new NoRotate());
        addModule(new OverArmor());
        addModule(new NoBob());
        addModule(new AntiAim());
        addModule(new NoFov());
        addModule(new NoHurtCam());
        addModule(new PlayerESP());
        addModule(new MotionGraph());
        addModule(new FakeAutoBlock());
        addModule(new Swing());
        addModule(new TrailESP());
        addModule(new DormantESP());
        addModule(new Scoreboard());
        addModule(new Tracers());
        addModule(new ItemTP());
        addModule(new WallSpeed());
        addModule(new Safewalk());
        addModule(new GommeMode());
        addModule(new XRay());
        addModule(new TrueSight());
        addModule(new Ambiance());
        addModule(new BedFucker());
        addModule(new Fullbright());
        addModule(new AntiVoid());
        addModule(new ShopSaver());
        addModule(new AntiBot());
        addModule(new DamageIndicator());
        addModule(new FastBreak());
        addModule(new IceSpeed());
        addModule(new NoPitchLimit());
        addModule(new AimBot());
        addModule(new Scaffold());
        addModule(new Blink());
        addModule(new Disabler());

        if(Koks.getKoks().CLManager.getUser().getRole() != Role.Developer) {
            modules.removeIf(module -> module.getCategory().equals(Module.Category.DEBUG));
        }

        modules.sort(Comparator.comparing(Module::getName));
    }

    public void addModule(Module module) {
        modules.add(module);
    }

    public Module getModule(Class<? extends Module> clazz) {
        for (Module module : getModules()) {
            if (module.getClass() == clazz) {
                return module;
            }
        }
        return null;
    }

    public Module getModule(String name) {
        for (Module module : getModules()) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }
}
