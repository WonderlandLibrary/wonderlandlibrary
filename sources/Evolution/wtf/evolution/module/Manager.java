package wtf.evolution.module;

import wtf.evolution.Main;
import wtf.evolution.module.impl.Combat.*;
import wtf.evolution.module.impl.Misc.*;
import wtf.evolution.module.impl.Movement.*;
import wtf.evolution.module.impl.Player.*;
import wtf.evolution.module.impl.Render.*;

import java.util.ArrayList;

public class Manager {
    public ArrayList<Module> m = new ArrayList<>();

    public Manager() {
        //COMBAT
        m.add(new AntiBot());
        m.add(new AutoArmor());
        m.add(new AutoTotem());
        m.add(new BowAimBot());
        m.add(new KillAura());
        m.add(new Resolver());
        m.add(new Velocity());
        m.add(new AutoPotion());
        m.add(new SuperKnockback());

        //MISC
        m.add(new BeaconExploit());
        m.add(new ItemSwapFix());
        m.add(new PasswordHider());
        m.add(new PlayerLogger());
        m.add(new XrayBypass());
        m.add(new BotJoiner());

        //MOVEMENT
        m.add(new Flight());
        m.add(new GuiWalk());
        m.add(new Jesus());
        m.add(new NoClip());
        m.add(new NoSlow());
        m.add(new Speed());
        m.add(new Spider());
        m.add(new Sprint());
        m.add(new Strafe());
        m.add(new NoPush());
        m.add(new NoWaterCollision());
        m.add(new HighJump());
        m.add(new LongJump());
        m.add(new WebLeave());

        //PLAYER
        m.add(new AutoRespawn());
        m.add(new MiddleClickPearl());
        m.add(new ItemScroller());
        m.add(new NoDelay());
        m.add(new Timer());
        m.add(new XCarry());
        m.add(new NoCom());
        m.add(new NoFall());
        m.add(new NoRotationSet());
        m.add(new AntiLagMachine());
        m.add(new Baritone());
        m.add(new Freecam());
        m.add(new GAppleTimer());
        m.add(new AutoLeave());
        m.add(new InventoryDroper());
        m.add(new BotSpammer());
        m.add(new Optimizer());
        m.add(new ClickTP());
        //RENDER
        m.add(new CustomModel());
        m.add(new BotStatictics());
        m.add(new ExtraTab());
        m.add(new ViewModel());
        m.add(new MiddleClickFriend());
        m.add(new Chams());
        m.add(new CameraClip());
        m.add(new SwingAnimation());
        m.add(new NameTag());
        m.add(new JumpCircle());
        m.add(new Trails());
        m.add(new ClickGui());
        m.add(new EnchantmentColor());
        m.add(new ESP());
        m.add(new FeatureList());
        m.add(new FogColor());
        m.add(new FullBright());
        m.add(new NoRender());
        m.add(new Predict());
        m.add(new Watermark());
        m.add(new WorldColor());
        m.add(new WorldTime());
        m.add(new Hotbar());
        m.add(new StaffAlert());
        m.add(new MotionGraph());
        m.add(new ItemESP());
        m.add(new ClientSound());
        m.add(new ChinaHat());
        m.add(new TriangleESP());
        m.add(new TargetHud());

        m.sort((o1, o2) -> o1.getSettings().size() >= o2.getSettings().size() ? 1 : -1);
    }

    public Module getModule(String name) {
        for (Module m : this.m) {
            if (m.name.equals(name)) {
                return m;
            }
        }
        return null;
    }


    public ArrayList<Module> getModulesFromCategory(Category category) {
        ArrayList<Module> modules = new ArrayList<>();
        for (Module m : this.m) {
            if (m.category == category) {
                modules.add(m);
            }
        }
        return modules;
    }

    public Module getModule(Class<?> clazz) {
        for (Module m : this.m) {
            if (m.getClass() == clazz) {
                return m;
            }
        }
        return null;
    }


}