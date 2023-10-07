package dev.monsoon.manager;

import dev.monsoon.Monsoon;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.enums.Category;
import dev.monsoon.module.implementation.combat.*;
import dev.monsoon.module.implementation.exploit.*;
import dev.monsoon.module.implementation.misc.*;
import dev.monsoon.module.implementation.movement.*;
import dev.monsoon.module.implementation.player.*;
import dev.monsoon.module.implementation.render.*;
import dev.monsoon.notification.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager {

    public CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();

    public BlockAnimation animations;
    public Killaura killAura;
    public Scaffold scaffold;
    public HUDOptions arraylist;
    public Blink blink;
    public Fly fly;
    public Speed speed;
    public Cheststealer cheststealer;
    public TargetHUD targethud;
    public ESP esp;
    public ModToggleGUI clickGuiMod;
    public DiscordRPC rpcModule;
    public Crasher crasher;
    public AntiBot antiBot;
    public FastEat fastEat;
    public Criticals criticals;
    public NameProtect nameProtect;
    public TakeABreak breakReminder;
    public NotificationsModule notifs;
    public WebhookSpammer wSpammer;

    public ModuleManager() {

        // Add modules
        modules.add(new Sprint());
        modules.add(nameProtect = new NameProtect());
        modules.add(new Fullbright());
        modules.add(fly = new Fly());
        modules.add(new Nofall());
        modules.add(blink = new Blink());
        modules.add(new AntiVoid());
        modules.add(speed = new Speed());
        modules.add(killAura = new Killaura());
        modules.add(new Fastplace());
        modules.add(new Timer());
        modules.add(new Velocity());
        modules.add(cheststealer = new Cheststealer());
        //modules.add(new Teleport());
        modules.add(fastEat = new FastEat());
        modules.add(new Noslow());
        modules.add(new Spammer());
        //modules.add(new AutoTool());
        modules.add(new HackerDetect());
        modules.add(new AutoClicker());
        modules.add(animations = new BlockAnimation());
        modules.add(arraylist = new HUDOptions());
        modules.add(new Autoarmor());
        modules.add(antiBot = new AntiBot());
        modules.add(new Invmove());
        modules.add(new Longjump());
        modules.add(new Phase());
        modules.add(esp = new ESP());
        modules.add(criticals = new Criticals());
        modules.add(new Safewalk());
        modules.add(clickGuiMod = new ModToggleGUI());
        //modules.add(new HeadRotations());
        modules.add(new ChestESP());
        modules.add(new Step());
        modules.add(new InvManager());
        modules.add(new Breaker());
        modules.add(new Disabler());
        //modules.add(nametags = new Nametags());
        modules.add(scaffold = new Scaffold());
        //modules.add(new RadioModules());
        //modules.add(new NoTitle());
        modules.add(targethud = new TargetHUD());
        //modules.add(new AutoPlay());
        modules.add(new HighJump());
        modules.add(rpcModule = new DiscordRPC());
        modules.add(crasher = new Crasher());
        modules.add(new ItemESP());
        modules.add(breakReminder = new TakeABreak());
        modules.add(new AntiBan());
        modules.add(new SimonESP());
        modules.add(new Overlay());
        modules.add(notifs = new NotificationsModule());
        modules.add(new FastBreak());
        modules.add(new TargetStrafe());
        //modules.add(wSpammer = new WebhookSpammer());

    }

    public static List<Module> getModulesByCategory(Category c) {
        List<Module> modules = new ArrayList<Module>();

        for(Module m : Monsoon.modules) {
            if(m.category == c)
                modules.add(m);
        }
        return modules;
    }
}
