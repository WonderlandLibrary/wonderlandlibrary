package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.util.ArrayList;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.AAC5Fast;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.BlocksMCBhop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.CustomSpeed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.NewHypixelHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.VerusHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.VulcanHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.WatchDog;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="Speed", description="Allows you to move faster.", category=ModuleCategory.MOVEMENT)
public class Speed
extends Module {
    private final SpeedMode[] speedModes = new SpeedMode[]{new CustomSpeed(), new AAC5Fast(), new VerusHop(), new VulcanHop(), new BlocksMCBhop(), new NewHypixelHop(), new WatchDog()};
    public final ListValue modeValue = new ListValue("Mode", this.getModes(), "NCPBHop"){

        @Override
        protected void onChange(String oldValue, String newValue) {
            if (Speed.this.getState()) {
                Speed.this.onDisable();
            }
        }

        @Override
        protected void onChanged(String oldValue, String newValue) {
            if (Speed.this.getState()) {
                Speed.this.onEnable();
            }
        }
    };
    public final FloatValue customSpeedValue = new FloatValue("CustomSpeed", 1.6f, 0.2f, 2.0f);
    public final FloatValue customYValue = new FloatValue("CustomY", 0.0f, 0.0f, 4.0f);
    public final FloatValue customTimerValue = new FloatValue("CustomTimer", 1.0f, 0.1f, 2.0f);
    public final BoolValue customStrafeValue = new BoolValue("CustomStrafe", true);
    public final BoolValue resetXZValue = new BoolValue("CustomResetXZ", false);
    public final BoolValue resetYValue = new BoolValue("CustomResetY", false);
    public final FloatValue HypixelTimerValue = new FloatValue("Hypixel-MaxTimer", 2.3f, 0.2f, 5.0f);
    public final FloatValue HypixelDealyTimerValue = new FloatValue("Hypixel-MinTimer", 0.7f, 0.2f, 5.0f);
    public final FloatValue speedtimerValue = new FloatValue("SpeedTimer", 1.0f, 0.1f, 3.0f);
    public final FloatValue hypixelspeedValue = new FloatValue("hypixelspeed", 0.47f, 0.2f, 2.0f);
    public final FloatValue speedYValue = new FloatValue("speedY", 0.42f, 0.2f, 2.0f);

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        SpeedMode speedMode;
        if (Speed.mc2.player.isSneaking()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            Speed.mc2.player.setSprinting(true);
        }
        if ((speedMode = this.getMode()) != null) {
            speedMode.onUpdate();
        }
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        SpeedMode speedMode;
        if (Speed.mc2.player.isSneaking() || event.getEventState() != EventState.PRE) {
            return;
        }
        if (MovementUtils.isMoving()) {
            Speed.mc2.player.setSprinting(true);
        }
        if ((speedMode = this.getMode()) != null) {
            speedMode.onMotion();
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (Speed.mc2.player.isSneaking()) {
            return;
        }
        SpeedMode speedMode = this.getMode();
        if (speedMode != null) {
            speedMode.onMove(event);
        }
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (Speed.mc2.player.isSneaking()) {
            return;
        }
        SpeedMode speedMode = this.getMode();
        if (speedMode != null) {
            speedMode.onTick();
        }
    }

    @Override
    public void onEnable() {
        if (Speed.mc2.player == null) {
            return;
        }
        mc.getTimer().setTimerSpeed(1.0f);
        SpeedMode speedMode = this.getMode();
        if (speedMode != null) {
            speedMode.onEnable();
        }
    }

    @Override
    public void onDisable() {
        if (Speed.mc2.player == null) {
            return;
        }
        mc.getTimer().setTimerSpeed(1.0f);
        SpeedMode speedMode = this.getMode();
        if (speedMode != null) {
            speedMode.onDisable();
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        IPacket packet = event.getPacket();
        Speed speed = (Speed)LiquidBounce.moduleManager.getModule(Speed.class);
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    private SpeedMode getMode() {
        String mode = (String)this.modeValue.get();
        for (SpeedMode speedMode : this.speedModes) {
            if (!speedMode.modeName.equalsIgnoreCase(mode)) continue;
            return speedMode;
        }
        return null;
    }

    private String[] getModes() {
        ArrayList<String> list = new ArrayList<String>();
        for (SpeedMode speedMode : this.speedModes) {
            list.add(speedMode.modeName);
        }
        return list.toArray(new String[0]);
    }
}
