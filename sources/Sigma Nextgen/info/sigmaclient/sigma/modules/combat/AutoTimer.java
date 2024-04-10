package info.sigmaclient.sigma.modules.combat;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.player.WorldEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.ModuleManager;
import info.sigmaclient.sigma.modules.PremiumModule;
import info.sigmaclient.sigma.sigma5.jelloportal.viafix.ViaFixManager;
import info.sigmaclient.sigma.utils.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static info.sigmaclient.sigma.modules.world.Timer.violation;
import top.fl0wowp4rty.phantomshield.annotations.Native;

@Native
public class AutoTimer extends PremiumModule {
    public static NumberValue range = new NumberValue("MaxRange", 5, 0, 8, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue minRange = new NumberValue("MinRange", 5, 0, 8, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue delay = new NumberValue("Delay", 1, 0, 1, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue duration = new NumberValue("Duration", 0.1, 0, 1, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue timer = new NumberValue("Timer", 5, 1, 20, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue maxTicks = new NumberValue("Max per use ticks", 20, 1, 100, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue multi = new NumberValue("Store Ticks", 40, 10, 100, NumberValue.NUMBER_TYPE.FLOAT);
    static BooleanValue killauraOnly = new BooleanValue("Killaura Only", true);
    public AutoTimer() {
        super("AutoTimer", Category.Combat, "Auto timer (use matrix mode timer!)");
     registerValue(range);
     registerValue(minRange);
     registerValue(delay);
     registerValue(duration);
     registerValue(timer);
     registerValue(multi);
     registerValue(maxTicks);
     registerValue(killauraOnly);
    }
    public static int delayTicks = 0, durationTicks = 0, useTicks = 0;
    public static boolean isStopping = false, back = false;
    public boolean freezing = false;
    public static Entity findTarget() {
        List<Entity> e = new ArrayList<>();
        float f = range.getValue().floatValue();
        for (final Entity o : mc.world.getLoadedEntityList()) {
            if(!(o instanceof LivingEntity)) continue;
            LivingEntity livingBase = (LivingEntity) o;
            if (o instanceof PlayerEntity) {
                if (AntiBot.isServerBots((PlayerEntity) livingBase)) continue;
                if (livingBase.isAlive() && livingBase != mc.player &&
                        mc.player.getDistanceNearest(o) <= f) {
                    e.add(o);
                }
            }
        }
        if(e.size() == 0) return null;
        e.sort(Comparator.comparingInt(a -> (int) (a.getDistanceSqToEntity(mc.player) * 100)));
        return e.get(0);
    }
    @Override
    public void onWorldEvent(WorldEvent event) {
        super.onWorldEvent(event);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
    public static void onUpdate() {
//        if(!SigmaNG.SigmaNG.moduleManager.getModule(AutoTimer.class).enabled)
//            return;
        Entity target;
        // ?
            target = findTarget();
//        } else {
//            target = Killaura.attackTarget;
//        }
        if (killauraOnly.getValue()) {
            if(!SigmaNG.getSigmaNG().moduleManager.getModule(Killaura.class).enabled) {
                reset();
            }
        }
//        ChatUtils.sendMessageWithPrefix("1");
        if (target == null) {
            reset();
            return;
        }
//        ChatUtils.sendMessageWithPrefix("2");
        double dist = range.getValue().floatValue();
        double minDist = minRange.getValue().floatValue();
        if (mc.player.getDistanceNearest(target) > dist || mc.player.getDistanceNearest(target) < minDist) {
            reset();
            return;
        }
//        ChatUtils.sendMessageWithPrefix(String.valueOf(violation));
//        ChatUtils.sendMessageWithPrefix("3");
        delayTicks++;
        if (delayTicks > (int) (delay.getValue().floatValue() * 30)) {
//            ChatUtils.sendMessageWithPrefix("4");
            durationTicks++;
//            if (durationTicks <= 2) {
                ;
//            } else {
            mc.timer.setTimerSpeed(timer.getValue().floatValue());
            useTicks ++;
            if(!ViaFixManager.viaFixManager.isNegativeTimerFixEnable()) {
                if (violation < multi.getValue().floatValue() / timer.getValue().floatValue()) {
//                ChatUtils.sendMessageWithPrefix("6 " + violation);
                    violation += 1;
                    violation = MathHelper.clamp(violation, 0.0f, multi.getValue().floatValue() / timer.getValue().floatValue());
                } else {
//                ChatUtils.sendMessageWithPrefix("5 " + violation);
                    reset();
                    return;
                }
            }
            if(useTicks > maxTicks.getValue().intValue()) {
                reset();
            }
//            }
        }else{
//            reset();
            mc.timer.setTimerSpeed(1f);
        }
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            onUpdate();
        }
        super.onUpdateEvent(event);
    }
    public static void reset(){
        mc.timer.setTimerSpeed(1f);
        durationTicks = 0;
        delayTicks = 0;
        useTicks = 0;
    }
    @Override
    public void onDisable() {
        mc.timer.setTimerSpeed(1.0f);
        super.onDisable();
    }

}
