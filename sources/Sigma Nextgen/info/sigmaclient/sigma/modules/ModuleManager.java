package info.sigmaclient.sigma.modules;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.modules.combat.*;
import info.sigmaclient.sigma.modules.gui.*;
import info.sigmaclient.sigma.modules.gui.hide.ClickGUI;
import info.sigmaclient.sigma.modules.gui.StaffActives;
import info.sigmaclient.sigma.modules.item.*;
import info.sigmaclient.sigma.modules.misc.*;
import info.sigmaclient.sigma.modules.movement.*;
import info.sigmaclient.sigma.modules.player.*;
import info.sigmaclient.sigma.modules.render.*;
import info.sigmaclient.sigma.modules.world.*;
import info.sigmaclient.sigma.modules.movement.BlockFly;
import info.sigmaclient.sigma.sigma5.SelfDestructManager;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import top.fl0wowp4rty.phantomshield.annotations.Native;
@Native

public class ModuleManager {
    public CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<>();
    public HashMap<Class<? extends Module>, Module> cache = new HashMap<>();
    public HashMap<String, String> mappingNames = new HashMap<>();
    public CopyOnWriteArrayList<Module> getModulesInType(Category category){
        CopyOnWriteArrayList<Module> modules1 = new CopyOnWriteArrayList<>();
        for(Module m : modules)
            if(m.category.equals(category))
                modules1.add(m);
        return modules1;
    }
    public void registerModule(Module module){
        if(module.notJello) return;
        modules.add(module);
        module.remapName = module.name;
//        if(mappingNames.containsKey(module.name) && SigmaNG.gameMode == SigmaNG.GAME_MODE.NURSULTRAN){
//            module.remapName = mappingNames.get(module.name);
//        }
//        for(Field field : module.getClass().getDeclaredFields()) {
//            Object obj;
//            try {
//                field.setAccessible(true);
//                obj = field.get(module);
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//            if (obj instanceof Value) {
//                ((Value<?>) obj).id = ((Value<?>) obj).name + ((Value<?>) obj).id;
//                module.values.add((Value<?>) obj);
//            }
//        }
    }
    public void init(){
//        mappingNames.put("KillAura", "Aura");
//        mappingNames.put("AttackParticles", "HitParticles");
//        mappingNames.put("Fly", "Flight");
//        mappingNames.put("BlockFly", "Scaffold");
//        mappingNames.put("ClickTP", "ClickTeleport");
//        mappingNames.put("FakeForge", "PayloadSpoof");
//        mappingNames.put("OldHitting", "SwordAnimations");
//        mappingNames.put("FastUse", "GappleTimer");
//        mappingNames.put("AntiPush", "NoPush");
//        mappingNames.put("Jesus", "LiquidWalk");
//        mappingNames.put("AntiKnockBack", "NoVelocity");
        // combats
        registerModule(new Aimbot());
        registerModule(new InteractRange());
        registerModule(new WTap());
        registerModule(new AutoTimer());
        registerModule(new AutoLava());
        registerModule(new Killaura());
        registerModule(new Regen());
        registerModule(new AntiKnockBack());
        registerModule(new NoInteract());
        registerModule(new AntiBot());
        registerModule(new FastBow());
        registerModule(new InfiniteAura());
        registerModule(new Criticals());
        registerModule(new Teams());

        // movements
        registerModule(new NoSlow());
        registerModule(new Step());
        registerModule(new WaterSpeed());
        registerModule(new ElytraFly());
        registerModule(new ElytraHelper());
        registerModule(new BoatFly());
        registerModule(new Strafe());
        registerModule(new AutoWalk());
        registerModule(new Spider());
        registerModule(new ClickTP());
        registerModule(new SafeWalk());
        registerModule(new Phase());
        registerModule(new InvMove());
        registerModule(new AutoSprint());
        registerModule(new BlockFly());
        registerModule(new NoWeb());
        registerModule(new LongJump());
        registerModule(new Fly());
        registerModule(new EntitySpeed());
        registerModule(new Speed());
        registerModule(new Jesus());
        registerModule(new HighJump());
        registerModule(new TargetStrafe());

        // renders
//        registerModule(new ColorPicker());
        registerModule(new NoHurtCam());
        registerModule(new NameTags());
        registerModule(new LowFire());
        registerModule(new CameraNoClip());
        registerModule(new Triangle());
        registerModule(new HoleFinder());
        registerModule(new Freecam());
        registerModule(new AntiBlind());
        registerModule(new DVDSimulator());
        registerModule(new ESP());
        registerModule(new Projectiles());
        registerModule(new XRay());
        registerModule(new Waypoints());
        registerModule(new Breadcrumbs());
        registerModule(new Trail());
        registerModule(new Particles());
        registerModule(new NameProtect());
        registerModule(new ChestESP());
        registerModule(new FPSBooster());
        registerModule(new Fullbright());

        // players
        registerModule(new NoFall());
        registerModule(new OldHitting());
        registerModule(new AutoRespawn());
        registerModule(new NoViewReset());
        registerModule(new Parkour());
        registerModule(new AntiPush());
        registerModule(new FastUse());
        registerModule(new Freeinventory());
        registerModule(new AntiVoid());
        registerModule(new Derp());
        registerModule(new FakePlayer());
        registerModule(new Blink());

        // gui
//        registerModule(new Shader());
        registerModule(new ActiveMods());
        registerModule(new PotionHUD());
        registerModule(new ShulkerInfo());
        registerModule(new BrainFreeze());
        registerModule(new ItemGlowing());
        registerModule(new KeyBinds());
        registerModule(new StaffActives());
        registerModule(new ColorChanger());
        registerModule(new NoScoreBoard());
        registerModule(new TargetHUD());
//        registerModule(new TimerIndicator());
        registerModule(new TabGUI());
        registerModule(new Compass());
        registerModule(new RearView());
        registerModule(new KeyStrokes());
        registerModule(new Cords());
        registerModule(new MiniMap());

        // worlds
        registerModule(new AntiCactus());
        registerModule(new AntiVanish());
        registerModule(new Disabler());
        registerModule(new AutoCrystal());
        registerModule(new AutoExplosion());
        registerModule(new Weather());
        registerModule(new Timer());
//        registerModule(new PacketFix());
        registerModule(new Auto32k());
        registerModule(new AutoAnchor());
        registerModule(new Nuker());
        registerModule(new CakeEater());
        registerModule(new NewChunks());
        registerModule(new NoteblockPlayer());
        registerModule(new FastPlace());
        registerModule(new Eagle());

        // items
        registerModule(new InvManager());
        registerModule(new AutoGapple());
        registerModule(new AutoMLG());
        registerModule(new AutoTools());
        registerModule(new TotemPoper());
        registerModule(new AutoTotem());
        registerModule(new AutoArmor());
        registerModule(new ChestStealer());
        registerModule(new AutoPotion());
        registerModule(new AutoSoup());

        // miscs
        registerModule(new FakeForge());
        registerModule(new ChatFilter());
        registerModule(new ChatCleaner());
        registerModule(new GamePlay());
        registerModule(new FakeLag());
//        registerModule(new FakeLagPlus());
        registerModule(new PortalGodMode());
        registerModule(new PortalGui());
//        registerModule(new AntiAim());
//        registerModule(new PacketDumper());
        registerModule(new ServerCrasher());
        registerModule(new MurderFucker());
        registerModule(new GameIdler());
//        registerModule(new TestCape());
        registerModule(new Jargon());
        registerModule(new AntiLevitation());
        registerModule(new AutoClicker());

//        if(SigmaNG.gameMode == SigmaNG.GAME_MODE.dest) {
//            registerModule(new KeepRange());
//            registerModule(new HUD());
//            registerModule(new Notifications());
//        }
        SigmaNG.SigmaNG.scriptModuleManager.loadAllScript();

        // hides
        registerModule(new ClickGUI());
        int moment = modules.size();
    }
    static class EmptyModule extends Module {
        public EmptyModule() {
            super("", null, "", false);
        }
    }
    EmptyModule NullModule = new EmptyModule();
    // Improve & Optimize
    public Module getModule(Class<? extends Module> moduleClass) {
        if(SelfDestructManager.destruct){
            return NullModule;
        }
        Module gg = cache.get(moduleClass);
        if (gg == null) {
            for (Module m : modules)
                if (m.getClass().equals(moduleClass)) {
                    cache.put(moduleClass, m);
                    return m;
                }
        } else {
            return gg;
        }
        return NullModule;
    }
    // Improve & Optimize
    public Module getModuleByName(String moduleClass) {
        for (Module m : modules)
            if (m.remapName.equalsIgnoreCase(moduleClass)) {
                return m;
            }
        return null;
    }
}
