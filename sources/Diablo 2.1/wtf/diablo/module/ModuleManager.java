package wtf.diablo.module;

import net.minecraft.client.Minecraft;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.impl.Combat.*;
import wtf.diablo.module.impl.Exploits.AutoDisable;
import wtf.diablo.module.impl.Exploits.Disabler;
import wtf.diablo.module.impl.Exploits.FastBow;
import wtf.diablo.module.impl.Exploits.LightningDetector;
import wtf.diablo.module.impl.Movement.*;
import wtf.diablo.module.impl.Movement.Timer;
import wtf.diablo.module.impl.Player.*;
import wtf.diablo.module.impl.Render.*;
import wtf.diablo.settings.Setting;
import wtf.diablo.settings.impl.ModeSetting;
import wtf.diablo.utils.font.CFont;
import wtf.diablo.utils.font.MCFontRenderer;
import wtf.diablo.utils.java.ReflectUtil;


import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager {
    private static final ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager(){
        setupModules();
    }

    public static ArrayList<Module> getModules(){
        return modules;
    }

    private void setupModules() {
        addModule(new Criticals());
        addModule(new KillAura());
        addModule(new TargetStrafe());
        addModule(new Velocity());
        addModule(new AutoPot());
        addModule(new KeepSprint());
        addModule(new InventoryMove());
        addModule(new Fly());
        addModule(new Sprint());
        addModule(new NoSlowdown());
        addModule(new Speed());
        addModule(new LongJump());
        addModule(new Stealer());
        addModule(new InventoryManager());
        addModule(new NoFall());
        addModule(new Scaffold());
        addModule(new AutoHypixel());
        addModule(new AntiVoid());
        addModule(new FastBow());
        addModule(new AntiBlind());
        addModule(new AutoTool());
        addModule(new FakePlayer());
        addModule(new ESP());
        addModule(new Nametags());
        addModule(new SessionInfo());
        addModule(new Hud());
        addModule(new Radar());
        addModule(new TimeChanger());
        addModule(new Gui());
        addModule(new NoWeather());
        addModule(new ClickGUI());
        addModule(new Animations());
        addModule(new Chams());
        addModule(new ChestESP());
        addModule(new LightningDetector());
        addModule(new AutoDisable());
        addModule(new Disabler());
        addModule(new Cape());
        addModule(new KillSay());
        addModule(new Step());
        addModule(new AutoClicker());
        addModule(new FullBright());
        addModule(new Glow());
        addModule(new Tracers());
        addModule(new Reach());
        addModule(new Timer());
        for(Module module : modules){
            System.out.println("[*] " + module.getName() + " - " + module.getDesc());
            for(Setting s : module.settings){
                if(s instanceof ModeSetting){
                    ModeSetting modeSetting = (ModeSetting) s;
                    System.out.println("   [-] " + s.getName() + ": "+ Arrays.toString(((ModeSetting) s).getModes()));
                }
            }
        }
        /*
        for (Class<?> mod : ReflectUtil.getReflects(this.getClass().getPackage().getName() + ".impl", Module.class)) {
            try {
                Module module = (Module) mod.getDeclaredConstructor().newInstance();
                addModule(module);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

         */
    }



    private void addModule(Module mod) {
        modules.add(mod);
    }

    public ArrayList<Module> getToggledModules() {
        ArrayList<Module> mods = new ArrayList<Module>();

        for (Module m : modules) {
            if (m.isToggled()) {
                mods.add(m);
            }
        }
        return mods;
    }

    public ArrayList<Module> sortedLengthToggled() {
        ArrayList<Module> mods = getToggledModules();

        mods.sort(Comparator.comparing(module -> Minecraft.getMinecraft().fontRendererObj.getStringWidth(((Module) module).getFormattedName())).reversed());
        return mods;
    }

    public ArrayList<Module> sortedLength(MCFontRenderer font) {
        ArrayList<Module> mods = new ArrayList<>(modules);
        try {
            if (Hud.hideVisuals.getValue()) {
                mods.removeIf(m -> m.getCategory() == Category.RENDER);
            }
        } catch (Exception ignored){}

        if(Hud.customFont.getValue()) {
            mods.sort(Comparator.comparing(module -> font.getStringWidth(((Module) module).getFormattedName())).reversed());
        } else {
            mods.sort(Comparator.comparing(module -> Minecraft.getMinecraft().fontRendererObj.getStringWidth(((Module) module).getFormattedName())).reversed());
        }
        return mods;
    }

    public ArrayList<Module> sortedLengthToggled(boolean seperator) {
        ArrayList<Module> mods = getToggledModules();

        mods.sort(Comparator.comparing(module -> Minecraft.getMinecraft().fontRendererObj.getStringWidth(((Module) module).getFormattedName(seperator))).reversed());
        return mods;
    }

    public ArrayList<Module> sortedLengthToggled(CFont font, boolean seperator) {
        ArrayList<Module> mods = getToggledModules();

        mods.sort(Comparator.comparing(module -> font.getStringWidth(((Module) module).getFormattedName(seperator))).reversed());
        return mods;
    }

    public ArrayList<Module> sortedLengthToggled(CFont font) {
        ArrayList<Module> mods = getToggledModules();

        mods.sort(Comparator.comparing(module -> font.getStringWidth(((Module) module).getFormattedName())).reversed());
        return mods;
    }

    public ArrayList<Module> sortedLengthDefault() {
        ArrayList<Module> mods = modules;

        mods.sort(Comparator.comparing(module -> Minecraft.getMinecraft().fontRendererObj.getStringWidth(((Module) module).getName())).reversed());
        return mods;
    }

    public ArrayList<Module> sortedModules() {
        ArrayList<Module> mods = modules;
        mods.sort(new Comparator<Module>() {
            public int compare(final Module mod, final Module mod2) {
                return mod.getName().compareTo(mod2.getName());
            }
        });
        return mods;
    }

    public ArrayList<Module> getModulesByCategory(Category c) {
        ArrayList<Module> mods = new ArrayList<>();
        for(Module m : modules) {
            if(m.getCategory() == c)
                mods.add(m);
        }
        return mods;
    }

    public ArrayList<Module> getModulesByCategory(Category c, MCFontRenderer font) {
        ArrayList<Module> mods = new ArrayList<>();
        for(Module m : sortedLength(font)) {
            if(m.getCategory() == c)
                mods.add(m);
        }
        return mods;
    }

    public static <T extends Module> T getModule(Class<T> clas) {
        return (T) getModules().stream().filter(module -> module.getClass() == clas).findFirst().orElse(null);
    }

    public void settingUpdateEvent() {
        for(Module m : modules){
            m.updateSettings();
        }
    }
    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

}
