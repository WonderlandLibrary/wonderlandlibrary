package net.smoothboot.client.module;

import net.smoothboot.client.mixinterface.ISubscription;
import net.smoothboot.client.mixinterface.Subscriptions;
import net.smoothboot.client.module.client.ClientSetting;
import net.smoothboot.client.module.client.ConfigManager;
import net.smoothboot.client.module.client.Hide;
import net.smoothboot.client.module.combat.*;
import net.smoothboot.client.module.misc.FastXP;
import net.smoothboot.client.module.misc.Fastplace;
import net.smoothboot.client.module.misc.MiddleClick_Looter;
import net.smoothboot.client.module.misc.Swap_Macro;
import net.smoothboot.client.module.player.Sprint;
import net.smoothboot.client.module.render.Nohurtcam;
import net.smoothboot.client.module.render.PlayerESP;

import java.util.ArrayList;
import java.util.List;

public class modmanager {

    public static final modmanager INSTANCE = new modmanager();
    private final List<Mod> modules = new ArrayList<>();

    public modmanager() {
        init();
        addmodules();
    }

    public List<Mod> getModule() {
        return modules;
    }

    public List<Mod> getEnabledModules() {
        List<Mod> enabled = new ArrayList<>();
        for (Mod module : modules) { if (module.isEnabled())
            enabled.add(module);
        }
        return enabled;
    }

    public List<Mod> getModulesInCategory(Mod.Category category) {
        List<Mod> categoryModules = new ArrayList<>();
        for (Mod mod:modules) {
            if (mod.getCategory() == category) {
                categoryModules.add(mod);
            }
        }
        return categoryModules;
    }

    public void add(Mod m) {
        modules.add(m);
        if (m instanceof ISubscription) {
            Subscriptions.addSub((ISubscription) m);
        }
    }

    private void init() {
        add(new AimAssist());
    }

    private void addmodules() {
        modules.add(new Anchor_Macro());
        modules.add(new Auto_Double_Hand());
        modules.add(new Auto_Totem());
        modules.add(new AutoStun());
        modules.add(new CW_Crystal());
        modules.add(new HitCrystal());
        modules.add(new Hotbar_Refill());
        modules.add(new Legit_Totem());
        modules.add(new Reach());
        modules.add(new TriggerBot());
        modules.add(new WTap());

        modules.add(new Fastplace());
        modules.add(new FastXP());
        modules.add(new MiddleClick_Looter());
        modules.add(new Swap_Macro());

        modules.add(new Sprint());

        modules.add(new Nohurtcam());
        modules.add(new PlayerESP());

        modules.add(new ClientSetting());
        modules.add(new ConfigManager());
        modules.add(new Hide());
    }
}