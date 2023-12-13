package me.nyan.flush.module;

import me.nyan.flush.Flush;
import me.nyan.flush.event.EventManager;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event2D;
import me.nyan.flush.event.impl.EventKey;
import me.nyan.flush.module.impl.combat.*;
import me.nyan.flush.module.impl.misc.*;
import me.nyan.flush.module.impl.movement.*;
import me.nyan.flush.module.impl.player.*;
import me.nyan.flush.module.impl.render.*;
import me.nyan.flush.module.impl.world.*;
import me.nyan.flush.utils.render.RenderUtils;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ModuleManager {
    private final ArrayList<Module> modules = new ArrayList<>();

    public void load() {
        EventManager.register(this);

        //Module register

        // COMBAT
        addModules(Aura.class, AutoPot.class, Velocity.class, Criticals.class, AutoClicker.class, Reach.class,
                AntiBot.class, AimBot.class, TPAura.class/*, FightBot.class*/);
        //modules.add(new SigmerTPAura());

        // MOVEMENT
        addModules(AirJump.class, Fly.class, HighJump.class, InvMove.class, LongJump.class, NoSlow.class,
                Safewalk.class, Speed.class, Sprint.class, Step.class, TargetStrafe.class, AntiVoid.class);

        // PLAYER
        addModules(AutoArmor.class, AutoTool.class, Blink.class, Derp.class, Disabler.class, FastEat.class,
                FastPlace.class, Freecam.class, InventoryManager.class, NoFall.class, NoRotate.class, Phase.class,
                Eagle.class, ClickTP.class);

        // WORLD
        addModules(ChestAura.class, ChestStealer.class, Scaffold.class, Scaffold2.class, Timer.class, Breaker.class);

        // RENDER
        addModules(Animations.class, AntiBlindness.class, ChinaHat.class, ModuleClickGui.class, SigmaHUD.class, NoHurtCam.class,
                CameraNoClip.class, Chams.class, ChestESP.class, ESP.class, FullBright.class, ItemESP.class,
                ItemPhysics.class, NoFire.class, Nametags.class, TimeChanger.class, Tracers.class, Breadcrumbs.class,
                Search.class, /*NekosLife.class, */UnfocusedCpu.class);

        // MISC
        addModules(AntiCrash.class, AutoBeacon.class, AutoLogin.class, AutoPlay.class, Cape.class,
                FancyChat.class, KillSults.class, MCF.class, NameProtect.class, /*PacketLogger.class, */Spammer.class,
                ShootCraft.class, StaffChecker.class, AdbRegister.class);
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public Module getModule(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        return modules.stream().filter(module -> module.getCategory() == category).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Module> getEnabledModules() {
        return modules.stream().filter(Module::isEnabled).collect(Collectors.toCollection(ArrayList::new));
    }

    public <T extends Module> T getModule(Class<T> clazz) {
        return (T) modules.stream().filter(module -> clazz.equals(module.getClass())).findFirst().orElse(null);
    }

    @SubscribeEvent
    public void onKey(EventKey e) {
        modules.stream().filter(module -> module.getKeys().contains(e.getKey())).forEach(Module::toggle);
    }

    @SubscribeEvent
    public void on2D(Event2D e) {
        for (Module m : modules) {
            m.setSlidingLevel(m.getSlidingLevel() + RenderUtils.calculateSpeed(0.1F) * (m.isEnabled() ? 1 : -1));
        }
    }

    public void addModules(Class<?>... classes) {
        for (Class<?> module : classes) {
            try {
                modules.add((Module) module.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                Flush.LOGGER.error("Failed to initialize module");
            }
        }
    }
}
