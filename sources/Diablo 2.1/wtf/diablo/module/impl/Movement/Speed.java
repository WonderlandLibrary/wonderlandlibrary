package wtf.diablo.module.impl.Movement;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;
import wtf.diablo.events.EventType;
import wtf.diablo.events.impl.MoveEvent;
import wtf.diablo.events.impl.StrafeEvent;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.BooleanSetting;
import wtf.diablo.settings.impl.ModeSetting;
import wtf.diablo.settings.impl.NumberSetting;
import wtf.diablo.utils.player.MoveUtil;
import wtf.diablo.utils.world.EntityUtil;

@Setter
@Getter
public class Speed extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Watchdog", "WatchdogLow", "Watchdog2", "NCP BHop", "Verus", "Viper");
    public NumberSetting vanillaSpeed = new NumberSetting("Speed", 0.5, 0.02, 0.1, 1);
    public NumberSetting watchdogTimer = new NumberSetting("Timer", 1, 0.1, 1, 5);
    public NumberSetting watchdogSlowDown = new NumberSetting("Slowdown", 0.15, 0.01, 0, 0.5);
    public BooleanSetting AiTimer = new BooleanSetting("Use Timer", false);

    private double watchdogMultiplier;
    private double AiSpeed = 0;

    public Speed() {
        super("Speed", "Increases player speed", Category.MOVEMENT, ServerType.All);
        vanillaSpeed.setParent(mode, "Vanilla");
        watchdogTimer.setParent(mode, "Watchdog","WatchdogLow","Watchdog2");
        watchdogSlowDown.setParent(mode, "Watchdog");
        AiTimer.setParent(mode, "WatchdogAi");
        addSettings(mode, vanillaSpeed, watchdogTimer, watchdogSlowDown,AiTimer);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        this.setSuffix(mode.getMode());
        switch (mode.getMode()) {
            case "Vanilla":
                if (MoveUtil.isMoving() && mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = 0.42;
                }
                MoveUtil.setMotion(vanillaSpeed.getValue() * 2);
                break;
            case "Verus":
                if (MoveUtil.isMoving() && mc.thePlayer.onGround) {
                    mc.thePlayer.setSprinting(true);
                    mc.thePlayer.motionY = 0.41999998688697815;
                }
                e.setYaw(MoveUtil.getDirection());
                MoveUtil.setMotion(MoveUtil.getBaseMoveSpeed() * (mc.thePlayer.hurtTime != 0 ? 2.135 : 1.035));
                break;
            case "WatchdogStrict":
                e.setYaw(MoveUtil.getDirection());
                mc.timer.timerSpeed = watchdogTimer.getFloatValue();
                mc.thePlayer.setSprinting(true);
                if (e.getType() == EventType.Pre) {
                    if (MoveUtil.isMoving() && mc.thePlayer.onGround) {
                        watchdogMultiplier = 1.45f;
                        mc.thePlayer.motionY = 0.41999998688697815;
                    }

                    if (watchdogMultiplier > 1) {
                        watchdogMultiplier -= 0.2;
                    } else {
                        watchdogMultiplier = 1;
                    }
                }
                break;
            case "Watchdog2":
                e.setYaw(MoveUtil.getDirection());
                mc.timer.timerSpeed = watchdogTimer.getFloatValue();
                mc.thePlayer.setSprinting(true);
                if (e.getType() == EventType.Pre) {
                    if (MoveUtil.isMoving() && mc.thePlayer.onGround) {
                        watchdogMultiplier = 1.2f;
                        mc.thePlayer.jump(0.39999998688697815);
                    }

                    MoveUtil.setMotion(((MoveUtil.getBaseMoveSpeed() * watchdogMultiplier) * (1- watchdogSlowDown.getValue())));
                    if (watchdogMultiplier > 1) {
                        watchdogMultiplier -= 0.05;
                    }
                }
                break;
            case "Watchdog":
                if(e.getType() == EventType.Pre){
                    if(MoveUtil.isMoving()){
                        if(mc.thePlayer.onGround){
                            mc.thePlayer.motionY = 0.42F;
                            AiSpeed = 0.3323;
                            if(mc.thePlayer.isPotionActive(Potion.moveSpeed)){
                                AiSpeed = 0.465023;
                            }

                            mc.timer.timerSpeed = watchdogTimer.getFloatValue();
                            if(AiTimer.getValue()){
                                //mc.timer.timerSpeed = 1.55f;
                            }
                        }else{
                            if(AiTimer.getValue()){
                                //mc.timer.timerSpeed = 1.45f;
                            }
                        }
                        AiSpeed = AiSpeed - 0.006;
                        MoveUtil.setMotion(AiSpeed);
                    }

                }
                break;
            case "WatchdogLow":
                e.setYaw(MoveUtil.getDirection());
                mc.timer.timerSpeed = watchdogTimer.getFloatValue();
                mc.thePlayer.setSprinting(true);
                if (e.getType() == EventType.Pre) {
                    if (MoveUtil.isMoving() && mc.thePlayer.onGround) {
                        watchdogMultiplier = 1.2f;
                        mc.thePlayer.motionY = 0.31999998688697815;
                    }

                    MoveUtil.setMotion((MoveUtil.getBaseMoveSpeed() * 0.90151F) * watchdogMultiplier);
                    if (watchdogMultiplier > 1) {
                        watchdogMultiplier -= 0.05;
                    }
                }
                break;
            case "NCP BHop":
                if(e.getType() != EventType.Pre)
                    return;
                if(MoveUtil.isMoving()) {
                    if(mc.thePlayer.onGround) {
                        if(mc.thePlayer.hurtTime != 0)
                            MoveUtil.setMotion(MoveUtil.getSpeed()*1.075);
                        MoveUtil.setMotion(MoveUtil.getSpeed()*1.08);
                        MoveUtil.setMotion(MoveUtil.getSpeed()*MoveUtil.getSpeedPotMultiplier(0.1));
                        mc.thePlayer.jump();
                    }else {
                        mc.thePlayer.jumpMovementFactor = 0.0265f*MoveUtil.getSpeedPotMultiplier(0.15);
                        MoveUtil.setMotion(MoveUtil.getSpeed());
                    }
                }else {
                    mc.thePlayer.motionX = mc.thePlayer.motionZ = 0.0;
                }
                break;
        }

    }

    @Subscribe
    public void onMoveEvent(MoveEvent e){
        switch (mode.getMode()){
            case "WatchdogStrict":
                e.setMoveSpeed((MoveUtil.getBaseMoveSpeed() * (1.081237F - (watchdogSlowDown.getValue())) * watchdogMultiplier));
                break;
        }
    }

    @Subscribe
    public void onStrafe(StrafeEvent event) {
        switch (mode.getMode()) {
            case "Viper":
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                } else {
                    // completely flagless unless ur in archer because viper distance limiter
                    event.setFriction(event.getFriction() + 0.04F);
                }
                break;
        }
    }
}
