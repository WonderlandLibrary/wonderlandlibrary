/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import me.AquaVit.liquidSense.modules.movement.Stair;
import net.ccbluex.liquidbounce.Gui.Notifications.Notificationsn;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
//import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.*;
//import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.*;
//import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.*;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aquavit.*;
//import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spartan.*;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.utils.ChatUtil;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "Speed", description = "Allows you to move faster.", category = ModuleCategory.MOVEMENT)
public class Speed extends Module {

    public float speed = 0.0f;
    public float setspeed(float speeds){
        return this.speed = speeds;
    }
    public float getspeed() {
        return this.speed;
    }
    public int stoptick;
    public boolean yn = false;

    private final SpeedMode[] speedModes = new SpeedMode[] {
            /*
            // NCP
            new NCPBHop(),
            new NCPFHop(),
            new SNCPBHop(),
            new NCPHop(),
            new YPort(),
            new YPort2(),
            new NCPYPort(),
            new Boost(),
            new Frame(),
            new MiJump(),
            new OnGround(),

            // AAC
            new AACBHop(),
            new AAC2BHop(),
            new AAC3BHop(),
            new AAC4BHop(),
            new AAC5BHop(),
            new AAC6BHop(),
            new AAC7BHop(),
            new AACHop3313(),
            new AACHop350(),
            new AACLowHop(),
            new AACLowHop2(),
            new AACLowHop3(),
            new AACGround(),
            new AACGround2(),
            new AACYPort(),
            new AACYPort2(),
            new AACPort(),
            new OldAACBHop(),

            // Spartan
            new SpartanYPort(),

            // Spectre
            new SpectreLowHop(),
            new SpectreBHop(),
            new SpectreOnGround(),
            new TeleportCubeCraft(),

            // Server
            new HiveHop(),
            new HypixelHop(),
            new MineplexGround(),

            // Other
            new SlowHop(),
            new CustomSpeed(),

             */

            // AquaVit
            new AAC4Hop(),
            new SlowDown(),
            new NewAAC4Hop(),
            new Hypixel(),
            new NewHypixelHop(),
    };

    public final ListValue modeValue = new ListValue("Mode", getModes(), "NCPBHop") {

        @Override
        protected void onChange(final String oldValue, final String newValue) {
            if(getState())
                onDisable();
        }

        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            if(getState())
                onEnable();
        }
    };

    public final FloatValue motionYValue = new FloatValue("MotionY", 0.42f, 0.0f, 0.42f);
    public final FloatValue movementSpeedValue = new FloatValue("MovementSpeed", 0.475f, 0.3f, 2.14f);
    public final BoolValue oldHypixel = new BoolValue("OldHypixel", true);
    public final ListValue newHypixelHopMode = new ListValue("NewHypixelHopMode", new String[]{"SlowHop","Normal"} , "Normal");
    public final FloatValue newHypixelHopDamageBoostValue = new FloatValue("NewHypixelHopDamageBoost", 0.45F, 0, 1F);
    public final BoolValue lagbackcheckValue = new BoolValue("AutoStop", true);
    public final IntegerValue stopticksValue = new IntegerValue("StopTicks",12,2,20);
    public final FloatValue customSpeedValue = new FloatValue("CustomSpeed", 1.6F, 0.2F, 2F);
    public final FloatValue customYValue = new FloatValue("CustomY", 0F, 0F, 4F);
    public final FloatValue customTimerValue = new FloatValue("CustomTimer", 1F, 0.1F, 2F);
    public final BoolValue customStrafeValue = new BoolValue("CustomStrafe", true);
    public final BoolValue resetXZValue = new BoolValue("CustomResetXZ", false);
    public final BoolValue resetYValue = new BoolValue("CustomResetY", false);

    public final FloatValue portMax = new FloatValue("AAC-PortLength", 1, 1, 20);
    public final FloatValue aacGroundTimerValue = new FloatValue("AACGround-Timer", 3F, 1.1F, 10F);
    public final FloatValue cubecraftPortLengthValue = new FloatValue("CubeCraft-PortLength", 1F, 0.1F, 2F);
    public final FloatValue mineplexGroundSpeedValue = new FloatValue("MineplexGround-Speed", 0.5F, 0.1F, 1F);
    public final BoolValue stiarcheckValue = new BoolValue("StairCheck", false);
    public int stopTicks;

    @EventTarget
    public void onJump(final JumpEvent event) {
        if(mc.thePlayer == null || stopTicks > 0)
            return;

        final SpeedMode speedMode = getMode();

        if (speedMode != null)
            speedMode.onJump(event);
    }


    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (this.lagbackcheckValue.get()) {
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
                packet.yaw = mc.thePlayer.rotationYaw;
                packet.pitch = mc.thePlayer.rotationPitch;
                mc.thePlayer.motionX *= 0.0;
                mc.thePlayer.motionZ *= 0.0;
                this.stopTicks = stopticksValue.get();
            }
        }
    }

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (stopTicks > 0) {
            stopTicks--;
            return;
        }
        if(mc.thePlayer.isSneaking())
            return;

        if(MovementUtils.isMoving())
            mc.thePlayer.setSprinting(true);

        final SpeedMode speedMode = getMode();

        if(speedMode != null)
            speedMode.onUpdate();
    }

    @EventTarget
    public void onMotion(final MotionEvent event) {
        if(mc.thePlayer.isSneaking() || event.getEventState() != EventState.PRE)
            return;

        if(MovementUtils.isMoving())
            mc.thePlayer.setSprinting(true);

        final SpeedMode speedMode = getMode();

        if(speedMode != null)
            speedMode.onMotion(event);
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        /*
        if(stoptick > 0) {
            mc.thePlayer.jumpMovementFactor = 0;
            mc.thePlayer.horseJumpPower = 0;
            mc.thePlayer.onGround = true;
        }
        if (mc.thePlayer.isSneaking() || stoptick > 0 || LiquidBounce.moduleManager.get(Scaffold.class).getState())
            return;

         */

        if (this.stopTicks > 0)
            return;

        final SpeedMode speedMode = getMode();

        if (speedMode != null)
            speedMode.onMove(event);
    }

    @EventTarget
    public void onTick(final TickEvent event) {
        if (this.stopTicks > 0)
            return;
        if(mc.thePlayer.isSneaking())
            return;

        final SpeedMode speedMode = getMode();

        if(speedMode != null)
            speedMode.onTick();
    }

    @Override
    public void onEnable() {
        if(mc.thePlayer == null)
            return;

        mc.timer.timerSpeed = 1F;

        Stair sp = (Stair) LiquidBounce.moduleManager.getModule("Stair");
        HUD hd = (HUD)LiquidBounce.moduleManager.getModule("HUD");

        if(sp.getState()){
            yn = true;
            LiquidBounce.moduleManager.getModule("Stair").setState(false);
            if(hd.no.get()) {
                ChatUtil.sendClientMessage("Debug Disable Stair", Notificationsn.Type.INFO);
            }
        }

        final SpeedMode speedMode = getMode();

        if(speedMode != null)
            speedMode.onEnable();
    }

    @Override
    public void onDisable() {
        HUD hd = (HUD)LiquidBounce.moduleManager.getModule("HUD");
        if(mc.thePlayer == null)
            return;

        mc.timer.timerSpeed = 1F;

        if(yn){
            LiquidBounce.moduleManager.getModule("Stair").setState(true);
            if(hd.no.get()) {
                ChatUtil.sendClientMessage("Debug Enable Stair", Notificationsn.Type.INFO);
            }
            yn = false;
        }

        final SpeedMode speedMode = getMode();

        if(speedMode != null)
            speedMode.onDisable();
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }

    private SpeedMode getMode() {
        final String mode = modeValue.get();

        for(final SpeedMode speedMode : speedModes)
            if(speedMode.modeName.equalsIgnoreCase(mode))
                return speedMode;

        return null;
    }

    private String[] getModes() {
        final List<String> list = new ArrayList<>();
        for(final SpeedMode speedMode : speedModes)
            list.add(speedMode.modeName);
        return list.toArray(new String[0]);
    }
}
