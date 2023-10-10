package ravenweave.client.module;

import net.minecraft.client.gui.FontRenderer;
import ravenweave.client.main.Raven;
import ravenweave.client.module.modules.HUD;
import ravenweave.client.module.modules.beta.*;
import ravenweave.client.module.modules.client.ClickGuiModule;
import ravenweave.client.module.modules.client.FakeHud;
import ravenweave.client.module.modules.client.Targets;
import ravenweave.client.module.modules.client.Terminal;
import ravenweave.client.module.modules.combat.*;
import ravenweave.client.module.modules.config.ConfigSettings;
import ravenweave.client.module.modules.hotkey.*;
import ravenweave.client.module.modules.minigames.*;
import ravenweave.client.module.modules.movement.*;
import ravenweave.client.module.modules.other.FakeChat;
import ravenweave.client.module.modules.other.MiddleClick;
import ravenweave.client.module.modules.other.Spin;
import ravenweave.client.module.modules.other.WaterBucket;
import ravenweave.client.module.modules.player.*;
import ravenweave.client.module.modules.render.*;
import ravenweave.client.module.modules.world.AntiBot;
import ravenweave.client.utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModuleManager {
    private List<Module> modules = new ArrayList<>();

    public static boolean initialized;
    public GuiModuleManager guiModuleManager;

    public ModuleManager() {
        if(initialized)
            return;
        this.guiModuleManager = new GuiModuleManager();
        addModule(new ChestStealer());
        addModule(new AutoArmor());
        addModule(new LeftClicker());
        addModule(new ClickAssist());
        addModule(new RightClicker());
        addModule(new AimAssist());
        addModule(new DelayRemover());
        addModule(new HitBoxes());
        addModule(new Reach());
        addModule(new Velocity());
        addModule(new Boost());
        addModule(new Fly());
        addModule(new InvMove());
        addModule(new KeepSprint());
        addModule(new NoSlow());
        addModule(new Sprint());
        addModule(new StopMotion());
        addModule(new LegitSpeed());
        addModule(new Timer());
        addModule(new VClip());
        addModule(new HClip());
        addModule(new AutoPlace());
        addModule(new BedAura());
        addModule(new FallSpeed());
        addModule(new FastPlace());
        addModule(new Freecam());
        addModule(new NoFall());
        addModule(new SafeWalk());
        addModule(new AntiBot());
        addModule(new Chams());
        addModule(new ChestESP());
        addModule(new Nametags());
        addModule(new PlayerESP());
        addModule(new Tracers());
        addModule(new HUD());
        addModule(new BridgeInfo());
        addModule(new DuelsStats());
        addModule(new MurderMystery());
        addModule(new SumoFences());
        addModule(new SlyPort());
        addModule(new FakeChat());
        addModule(new WaterBucket());
        addModule(new Terminal());
        addModule(new ClickGuiModule());
        addModule(new BridgeAssist());
        addModule(new Blocks());
        addModule(new Ladders());
        addModule(new Weapon());
        addModule(new Pearl());
        addModule(new Armor());
        addModule(new Healing());
        addModule(new Trajectories());
        addModule(new WTap());
        addModule(new BlockHit());
        addModule(new STap());
        addModule(new AutoWeapon());
        addModule(new BedwarsOverlay());
        addModule(new ShiftTap());
        addModule(new AutoBlock());
        addModule(new MiddleClick());
        addModule(new Projectiles());
        addModule(new FakeHud());
        addModule(new ConfigSettings());
        addModule(new JumpReset());
        addModule(new KillAura());
        addModule(new Spin());
        addModule(new AutoGHead());
        addModule(new AutoSoup());
        addModule(new Targets());
        addModule(new BHop());
        addModule(new AutoTool());
        addModule(new Blink());
        addModule(new NoJumpDelay());
        addModule(new Parkour());

        // BETA
        addModule(new Refill());
        addModule(new LongJump());
        addModule(new Radar());
        addModule(new Scaffold());
        addModule(new TargetHUD());
        addModule(new AutoClutch());

        initialized = true;
    }

    public void addModule(Module m) {
        modules.add(m);
    }

    public Module getModuleByName(String name) {
        if (!initialized)
            return null;

        for (Module module : modules)
			if (module.getName().replaceAll(" ", "").equalsIgnoreCase(name) || module.getName().equalsIgnoreCase(name))
                return module;
        return null;
    }

    public Module getModuleByClazz(Class<? extends Module> c) {
        if (!initialized)
            return null;

        for (Module module : modules)
			if (module.getClass().equals(c))
                return module;
        return null;
    }

    public List<Module> getModules() {
        ArrayList<Module> allModules = new ArrayList<>(modules);
        try {
            allModules.addAll(Raven.configManager.configModuleManager.getConfigModules());
        } catch (NullPointerException ignored) {
        }
        try {
            allModules.addAll(guiModuleManager.getModules());
        } catch (NullPointerException ignored) {
        }
        return allModules;
    }

    public List<Module> getConfigModules() {
        List<Module> modulesOfC = new ArrayList<>();

        for (Module mod : getModules())
			if (!mod.isClientConfig())
				modulesOfC.add(mod);

        return modulesOfC;
    }

    public List<Module> getClientConfigModules() {
        List<Module> modulesOfCC = new ArrayList<>();

        for (Module mod : getModules())
			if (mod.isClientConfig())
				modulesOfCC.add(mod);

        return modulesOfCC;
    }

    public List<Module> getModulesInCategory(Module.ModuleCategory categ) {
        ArrayList<Module> modulesOfCat = new ArrayList<>();

        for (Module mod : getModules())
			if (mod.moduleCategory().equals(categ))
				modulesOfCat.add(mod);

        return modulesOfCat;
    }

    public void sort() {
        modules.sort((o1, o2) -> Utils.mc.fontRendererObj.getStringWidth(o2.getName())
                - Utils.mc.fontRendererObj.getStringWidth(o1.getName()));
    }

    public void sortLongShort() {
        modules.sort(Comparator.comparingInt(o2 -> Utils.mc.fontRendererObj.getStringWidth(o2.getName())));
    }

    public void sortShortLong() {
        modules.sort((o1, o2) -> Utils.mc.fontRendererObj.getStringWidth(o2.getName())
                - Utils.mc.fontRendererObj.getStringWidth(o1.getName()));
    }

    public int getLongestActiveModule(FontRenderer fr) {
        int length = 0;
        for (Module mod : modules)
			if (mod.isEnabled())
				if (fr.getStringWidth(mod.getName()) > length)
					length = fr.getStringWidth(mod.getName());
        return length;
    }

    public int getBoxHeight(FontRenderer fr, int margin) {
        int length = 0;
        for (Module mod : modules)
			if (mod.isEnabled())
				length += fr.FONT_HEIGHT + margin;
        return length;
    }

}
