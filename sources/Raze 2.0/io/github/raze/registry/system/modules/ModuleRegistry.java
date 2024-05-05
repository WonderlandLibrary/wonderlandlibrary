package io.github.raze.registry.system.modules;

import de.florianmichael.rclasses.storage.Storage;
import io.github.raze.events.collection.game.EventKeyboard;
import io.github.raze.events.collection.visual.EventRender2D;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.client.ActiveModules;
import io.github.raze.modules.collection.client.ClickGui;
import io.github.raze.modules.collection.client.Watermark;
import io.github.raze.modules.collection.combat.*;
import io.github.raze.modules.collection.misc.*;
import io.github.raze.modules.collection.movement.*;
import io.github.raze.modules.collection.player.*;
import io.github.raze.modules.collection.visual.*;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.modules.system.ingame.ModuleIngame;
import io.github.raze.utilities.system.Methods;

import java.util.List;
import java.util.stream.Collectors;

public class ModuleRegistry extends Storage<AbstractModule> implements Methods {

    public void init() {
        this.add(

                // Combat
                new NoClickDelay(),
                new AutoClicker(),
                new TriggerBot(),
                new AimAssist(),
                new Velocity(),
                new SnapAim(),
                new Aura(),

                // Miscellaneous
                new AntiResourceExploit(),
                new HackerDetector(),
                new NameProtect(),
                new FakePlayer(),
                new PingSpoof(),
                new AutoLogin(),
                new Disabler(),
                new SkinDerp(),
                new NoRotate(),

                // Movement
                new InventoryMove(),
                new AntiVoid(),
                new Scaffold(),
                new SafeWalk(),
                new LongJump(),
                new HighJump(),
                new Parkour(),
                new NoFall(),
                new NoSlow(),
                new Flight(),
                new Sprint(),
                new Spider(),
                new Eagle(),
                new Phase(),
                new Speed(),
                new Step(),
                new Test(),

                // Player
                new ChestStealer(),
                new AutoRespawn(),
                new FastPlace(),
                new AutoTool(),
                new Spammer(),
                new FastEat(),
                new Regen(),
                new Timer(),

                // Visual
                new ViewableRotations(),
                new NoAchievements(),
                new CustomCape(),
                new Animations(),
                new FullBright(),
                new Scoreboard(),
                new TargetHUD(),
                new AntiBlind(),
                new NameTags(),
                new ImageESP(),
                new ChestESP(),
                new XRay(),
                new ESP(),

                // Client
                new ActiveModules(),
                new Watermark(),
                new ClickGui()

        );
    }

    @Override
    public <V extends AbstractModule> V getByClass(final Class<V> clazz) {
        final AbstractModule feature = this.getList().stream().filter(m -> m.getClass().equals(clazz)).findFirst().orElse(null);
        if (feature == null) return null;
        return clazz.cast(feature);
    }

    public <M extends AbstractModule> M getModule(String input) {
        return (M) this.getList().stream().filter(module -> module.getName().equalsIgnoreCase(input)).findFirst().orElse(null);
    }

    public List<AbstractModule> getModulesByCategory(ModuleCategory input) {
        return this.getList().stream().filter(module -> module.getCategory().equals(input)).collect(Collectors.toList());
    }

    public List<AbstractModule> getEnabledModules() {
        return this.getList().stream().filter(AbstractModule::isEnabled).collect(Collectors.toList());
    }

    @Listen
    public void onKeyboard(EventKeyboard eventKeyboard) {
        this.getList().forEach(module -> {
            if (module.getKeyCode() == eventKeyboard.getKeyCode()) {
                module.toggle();
            }
        });
    }

    @Listen
    public void onRender2D(EventRender2D eventRender2D) {
        this.getList().forEach(module -> {
            if (module instanceof ModuleIngame) {
                ModuleIngame ingame = (ModuleIngame) module;

                if (!mc.gameSettings.showDebugInfo && module.isEnabled()) {
                    ingame.renderIngame();
                }
            }
        });
    }
}
