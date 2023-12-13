/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.specials.flys;

import net.minecraft.client.Minecraft;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.exploits.FunnyFly;
import tk.rektsky.module.impl.exploits.SelfDamage;
import tk.rektsky.module.impl.movement.Fly;

public class VerusFunny {
    Minecraft mc = Minecraft.getMinecraft();
    boolean damaged = false;
    int times = 0;

    public void onEnable() {
        this.damaged = false;
        this.times = 0;
        ModulesManager.getModuleByClass(FunnyFly.class).setToggled(true);
        ModulesManager.getModuleByClass(SelfDamage.class).setToggled(true);
    }

    public void onDisable() {
        ModulesManager.getModuleByClass(SelfDamage.class).setToggled(false);
        ModulesManager.getModuleByClass(FunnyFly.class).setToggled(false);
    }

    @Subscribe
    public void onWorldTick(WorldTickEvent event) {
        if (this.times >= 2) {
            ModulesManager.getModuleByClass(Fly.class).setToggled(false);
            return;
        }
        if (ModulesManager.getModuleByClass(FunnyFly.class).enabledTicks % 2 == 0 && !this.damaged) {
            this.damaged = true;
            ModulesManager.getModuleByClass(SelfDamage.class).setToggled(true);
        }
        if (!ModulesManager.getModuleByClass(FunnyFly.class).isToggled()) {
            this.mc.thePlayer.motionX = 0.0;
            this.mc.thePlayer.motionY = 0.0;
            this.mc.thePlayer.motionZ = 0.0;
        }
        if (!ModulesManager.getModuleByClass(FunnyFly.class).isToggled()) {
            this.damaged = false;
            ModulesManager.getModuleByClass(FunnyFly.class).setToggled(true);
            ++this.times;
        }
    }
}

