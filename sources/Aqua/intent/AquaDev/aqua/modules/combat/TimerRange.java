package intent.AquaDev.aqua.modules.combat;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.combat.Killaura;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;

public class TimerRange
extends Module {
    public static EntityPlayer target = null;
    String inRange;

    public TimerRange() {
        super("TimerRange", "TimerRange", 0, Category.Combat);
        Aqua.setmgr.register(new Setting("Range", (Module)this, 3.0, 3.0, 6.0, false));
        Aqua.setmgr.register(new Setting("Boost", (Module)this, 12.0, 1.0, 50.0, false));
        Aqua.setmgr.register(new Setting("OnHitTPTicks", (Module)this, 18.0, 0.0, 100.0, false));
        Aqua.setmgr.register(new Setting("OnHitTP", (Module)this, false));
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        TimerRange.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }

    public void onEvent(Event e) {
        if (e instanceof EventUpdate) {
            if (!Aqua.setmgr.getSetting("TimerRangeOnHitTP").isState()) {
                if (Aqua.moduleManager.getModuleByName("Killaura").isToggled()) {
                    float boost;
                    if (target == null) {
                        this.inRange = "out";
                    }
                    if (target != null) {
                        this.inRange = "in";
                    }
                    if ((target = this.searchTargets()) != null && Objects.equals((Object)this.inRange, (Object)"out")) {
                        this.inRange = "teleport";
                    }
                    if (target == null && Objects.equals((Object)this.inRange, (Object)"in")) {
                        this.inRange = "slow";
                    }
                    TimerRange.mc.timer.timerSpeed = Objects.equals((Object)this.inRange, (Object)"teleport") ? (boost = (float)Aqua.setmgr.getSetting("TimerRangeBoost").getCurrentNumber()) : (Objects.equals((Object)this.inRange, (Object)"slow") ? 0.2f : 1.0f);
                } else {
                    TimerRange.mc.timer.timerSpeed = 1.0f;
                }
            } else if (Aqua.moduleManager.getModuleByName("Killaura").isToggled()) {
                if (target == null) {
                    this.inRange = "out";
                }
                if (target != null) {
                    this.inRange = "in";
                }
                if ((target = this.searchTargets()) != null && Objects.equals((Object)this.inRange, (Object)"out")) {
                    this.inRange = "teleport";
                }
                if (target == null && Objects.equals((Object)this.inRange, (Object)"in")) {
                    this.inRange = "slow";
                }
                if (Killaura.target != null) {
                    float boost;
                    float boostTicks = (float)Aqua.setmgr.getSetting("TimerRangeOnHitTPTicks").getCurrentNumber();
                    TimerRange.mc.timer.timerSpeed = Objects.equals((Object)this.inRange, (Object)"teleport") || Killaura.target.hurtTime != 0 && (float)TimerRange.mc.thePlayer.ticksExisted % boostTicks == 0.0f && TimerRange.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) == null ? (boost = (float)Aqua.setmgr.getSetting("TimerRangeBoost").getCurrentNumber()) : (Objects.equals((Object)this.inRange, (Object)"slow") ? 0.2f : 1.0f);
                } else {
                    TimerRange.mc.timer.timerSpeed = 1.0f;
                }
            } else {
                TimerRange.mc.timer.timerSpeed = 1.0f;
            }
        }
    }

    public EntityPlayer searchTargets() {
        float range = (float)Aqua.setmgr.getSetting("TimerRangeRange").getCurrentNumber();
        EntityPlayer player = null;
        double closestDist = 100000.0;
        for (Entity o : TimerRange.mc.theWorld.loadedEntityList) {
            double dist;
            if (o.getName().equals((Object)TimerRange.mc.thePlayer.getName()) || !(o instanceof EntityPlayer) || !(TimerRange.mc.thePlayer.getDistanceToEntity(o) < range) || !((dist = (double)TimerRange.mc.thePlayer.getDistanceToEntity(o)) < closestDist)) continue;
            closestDist = dist;
            player = (EntityPlayer)o;
        }
        return player;
    }
}
