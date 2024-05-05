package io.github.nevalackin.client.module;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import io.github.nevalackin.client.module.combat.healing.AutoPot;
import io.github.nevalackin.client.module.combat.healing.Regen;
import io.github.nevalackin.client.module.combat.miniGames.CripsVsBloods;
import io.github.nevalackin.client.module.combat.rage.Aura;
import io.github.nevalackin.client.module.combat.rage.TargetStrafe;
import io.github.nevalackin.client.module.combat.rage.Velocity;
import io.github.nevalackin.client.module.misc.inventory.AutoTool;
import io.github.nevalackin.client.module.misc.inventory.ChestStealer;
import io.github.nevalackin.client.module.misc.inventory.Inventory;
import io.github.nevalackin.client.module.misc.inventory.InventoryManager;
import io.github.nevalackin.client.module.misc.player.*;
import io.github.nevalackin.client.module.misc.world.FastBreak;
import io.github.nevalackin.client.module.misc.world.Scaffold;
import io.github.nevalackin.client.module.misc.world.Timer;
import io.github.nevalackin.client.module.movement.extras.Flight;
import io.github.nevalackin.client.module.movement.extras.LongJump;
import io.github.nevalackin.client.module.movement.extras.Speed;
import io.github.nevalackin.client.module.movement.main.NoFall;
import io.github.nevalackin.client.module.movement.main.NoSlowDown;
import io.github.nevalackin.client.module.movement.main.Sprint;
import io.github.nevalackin.client.module.movement.main.Step;
import io.github.nevalackin.client.module.render.esp.Glow;
import io.github.nevalackin.client.module.render.esp.OffScreenESP;
import io.github.nevalackin.client.module.render.esp.esp.ESP;
import io.github.nevalackin.client.module.render.model.Chams;
import io.github.nevalackin.client.module.render.model.HurtEffect;
import io.github.nevalackin.client.module.render.model.NoRender;
import io.github.nevalackin.client.module.render.overlay.*;
import io.github.nevalackin.client.module.render.overlay.netgraph.NetGraph;
import io.github.nevalackin.client.module.render.self.ChinaHat;
import io.github.nevalackin.client.module.render.self.SwingModifier;
import io.github.nevalackin.client.module.render.self.ThirdPerson;
import io.github.nevalackin.client.module.render.world.BlockOverlay;
import io.github.nevalackin.client.module.render.world.WorldColour;
import io.github.nevalackin.client.module.render.world.WorldTime;
import io.github.nevalackin.client.module.render.world.XRay;

import java.util.Arrays;
import java.util.Collection;

public final class ModuleManagerImpl implements ModuleManager {

    private final ClassToInstanceMap<Module> moduleInstances;

    public ModuleManagerImpl() {
        this.moduleInstances = this.populateInstanceMap(
                new Aura(),
                new CripsVsBloods(),
                new TargetStrafe(),
                new Velocity(),
                new Regen(),
                new AutoPot(),
                new Inventory(),
                new NoFall(),
                new ChestStealer(),
                new InventoryManager(),
                new NoRotate(),
                new AutoTool(),
                new PingSpoof(),
                new Blink(),
                new AutoHypixel(),
                new FastUse(),
                new Sprint(),
                new NoSlowDown(),
                new Speed(),
                new LongJump(),
                new Flight(),
                new Step(),
                new AutoTool(),
                new Scaffold(),
                new WorldTime(),
                new FastBreak(),
                new Timer(),
                new NoRender(),
                new OffScreenESP(),
                new Crosshair(),
                new Camera(),
                new NoOverlays(),
                new SwingModifier(),
                new WorldColour(),
                new HurtEffect(),
                new XRay(),
                new Chams(),
                new BlockOverlay(),
                new ChinaHat(),
                new NoFOV(),
                new ThirdPerson(),
                new Glow(),
                new ESP(),
                new Arraylist(),
                new Watermark(),
                new NetGraph()
        );
    }

    @Override
    public <T extends Module> T getModule(final Class<T> clazz) {
        return this.moduleInstances.getInstance(clazz);
    }

    @Override
    public Module getModule(String name) {
        return null;
    }

    @Override
    public <T extends Module> void registerModule(final Class<T> clazz, final T module) {
        this.moduleInstances.putInstance(clazz, module);
    }

    @Override
    public Collection<Module> getModules() {
        return this.moduleInstances.values();
    }

    @SuppressWarnings("unchecked")
    private ClassToInstanceMap<Module> populateInstanceMap(Module... modules) {
        final ClassToInstanceMap<Module> instanceMap = MutableClassToInstanceMap.create();
        Arrays.stream(modules).forEach(module -> instanceMap.putInstance((Class<Module>) module.getClass(), module));
        return instanceMap;
    }
}
