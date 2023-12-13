package vestige.module;

import vestige.module.impl.combat.*;
import vestige.module.impl.exploit.*;
import vestige.module.impl.misc.*;
import vestige.module.impl.movement.*;
import vestige.module.impl.player.*;
import vestige.module.impl.visual.*;
import vestige.module.impl.world.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModuleManager {

    public final List<Module> modules = new ArrayList<>();
    public List<HUDModule> hudModules;

    public ModuleManager() {
        modules.add(new Killaura());
        modules.add(new Velocity());
        modules.add(new TargetStrafe());
        modules.add(new Teams());
        modules.add(new Backtrack());
        modules.add(new Tickbase());
        modules.add(new Reach());
        modules.add(new Autoclicker());
        modules.add(new AimAssist());
        modules.add(new DelayRemover());
        modules.add(new WTap());
        modules.add(new Criticals());
        modules.add(new Antibot());

        modules.add(new Sprint());
        modules.add(new Fly());
        modules.add(new Speed());
        modules.add(new Longjump());
        modules.add(new InventoryMove());
        modules.add(new Noslow());
        modules.add(new Blink());
        modules.add(new Safewalk());
        modules.add(new Step());

        modules.add(new ChestStealer());
        modules.add(new InventoryManager());
        modules.add(new Nofall());
        modules.add(new Antivoid());
        modules.add(new Timer());
        modules.add(new FastPlace());
        modules.add(new AutoTool());
        modules.add(new Autoplace());

        modules.add(new Scaffold());
        modules.add(new AutoBridge());
        modules.add(new Breaker());

        modules.add(new Watermark());
        modules.add(new ModuleList());
        modules.add(new IngameInfo());
        modules.add(new ClientTheme());
        modules.add(new ClickGuiModule());
        modules.add(new ESP());
        modules.add(new Chams());
        modules.add(new Animations());
        modules.add(new Rotations());
        modules.add(new TargetHUD());
        modules.add(new Keystrokes());
        modules.add(new Freelook());
        modules.add(new TimeChanger());
        modules.add(new Fullbright());
        modules.add(new NameProtect());
        modules.add(new Xray());

        modules.add(new Disabler());
        modules.add(new StrafeConverter());

        modules.add(new AnticheatModule());
        modules.add(new Autoplay());
        modules.add(new SelfDestruct());

        hudModules = modules.stream().filter(HUDModule.class::isInstance).map(HUDModule.class::cast).collect(Collectors.toList());
    }

    public <T extends Module> T getModule(Class<T> clazz) {
        Optional<Module> module = modules.stream().filter(m -> m.getClass().equals(clazz)).findFirst();

        if(module.isPresent()) {
            return (T) module.get();
        } else {
            return null;
        }
    }

    public <T extends Module> T getModuleByName(String name) {
        Optional<Module> module = modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst();

        if(module.isPresent()) {
            return (T) module.get();
        }

        return null;
    }

    public <T extends Module> T getModuleByNameNoSpace(String name) {
        Optional<Module> module = modules.stream().filter(m -> m.getName().replace(" ", "").equalsIgnoreCase(name)).findFirst();

        if(module.isPresent()) {
            return (T) module.get();
        }

        return null;
    }

}