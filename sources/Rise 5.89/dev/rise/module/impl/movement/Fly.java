/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.movement;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.BlockCollideEvent;
import dev.rise.event.impl.other.MoveButtonEvent;
import dev.rise.event.impl.other.MoveEvent;
import dev.rise.event.impl.other.StrafeEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.Vector;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.player.DamageUtil;
import dev.rise.util.player.MoveUtil;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This module allows you to fly like a bird. Pretty self explanatory.
 */
@ModuleInfo(name = "Fly", description = "Lets you fly", category = Category.MOVEMENT)
public final class Fly extends Module {

    public static boolean hypixelDisable;
    private final ModeSetting mode = new ModeSetting("Fly Mode", this, "Vanilla", "Vanilla", "Damage", "Creative",
            "Hypixel New", "Hypixel Fast Lag", "Verus", "Verus Lag", "VerusBlack", "Verus AirJump", "Bow Longjump", "Mush", "Viper", "Bow Fly", "ACR", "ACR2", "Collide",
            "Spartan", "Aac3", "MCCentral", "Exempted Value", "Kauri", "Taka", "Vicnix", "Wait for Damage", "Old NCP", "Minemenclub",
            "Redesky", "BlockFly", "Matrix1.17", "Vulcan", "Vulcan2", "MineBox");

    private final ModeSetting exemptedValueType = new ModeSetting("Value", this, "1", "1", "2", "3");
    private final ModeSetting redeskyMode = new ModeSetting("Mode", this, "Stable", "Stable", "Fast");
    private final NumberSetting flySpeed = new NumberSetting("Fly Speed", this, 1, 0.1, 9.5, 0.1);
    private final BooleanSetting autoStop = new BooleanSetting("Auto Stop", this, true);
    private final ModeSetting hypixelFlySpeed = new ModeSetting("Speed", this, "I want my mommy", "I want my mommy", "Normal", "Airstrike", "Intercontinental ballistic missile");
    private final NumberSetting bowFlyHBoost1 = new NumberSetting("H Boost first tick", this, 4, 0, 8.5, 0.1);
    private final NumberSetting bowFlyVBoost1 = new NumberSetting("V Boost first tick", this, 4, 0, 8.5, 0.1);
    private final NumberSetting bowFlyHBoost2 = new NumberSetting("H Boost second tick", this, 4, 0, 8.5, 0.1);
    private final NumberSetting bowFlyVBoost2 = new NumberSetting("V Boost second tick", this, 4, 0, 8.5, 0.1);
    private final NumberSetting multiplierInAir = new NumberSetting("MultiplierInAir", this, 1.08, 1, 1.4, 0.1);
    private final NumberSetting glide = new NumberSetting("Glide", this, 0.03, 0, 0.1, 0.01);
    private final NumberSetting timer = new NumberSetting("Timer Speed", this, 1, 0.1, 2, 0.1);
    private final BooleanSetting strafe = new BooleanSetting("Strafe", this, true);
    private final BooleanSetting inf = new BooleanSetting("Inf", this, false);
    private final ModeSetting vanillaBypass = new ModeSetting("Vanilla Kick Bypass", this, "Off", "Off", "Motion", "Packet");
    private final BooleanSetting delayKickBypass = new BooleanSetting("Delay Kick Bypass", this, false);
    private final BooleanSetting smoothCamera = new BooleanSetting("Smooth Camera", this, false);
    private final BooleanSetting fakeDamage = new BooleanSetting("Fake Damage", this, false);
    private final NumberSetting viewBobbing = new NumberSetting("View Bobbing", this, 0, 0, 0.1, 0.01);
    private final BooleanSetting groundSpoof = new BooleanSetting("GroundSpoof", this, false);
    private final BooleanSetting damage = new BooleanSetting("Damage", this, false);
    private final BooleanSetting sigmaFastFly = new BooleanSetting("Sigma Fast Fly", this, false);
    private final ConcurrentLinkedQueue<Packet<?>> packets = new ConcurrentLinkedQueue<>();

    private int i = 0;
    private int ticks, wasos, offGroundTicks, onGroundTicks, hypixelTicks, pearlSlot = -1;
    private boolean hypixelStart, jumped, flag, pearlFly, startFlyingCapability;
    private float speed, startSpeed = 0.05F;
    private int saveSlot = -1;
    private float oPositionY;
    private float fov = mc.gameSettings.fovSetting;
    private double moveSpeed, lastDist, d;
    private int stage;
    private double startingLocationX, startingLocationY, startingLocationZ, buffer;
    private boolean movingTowardsStartingLocation;

    private boolean bool;
    private int ticksSinceFlag, boostTicks;

    private boolean dmged;
    private boolean beingDmged;
    private boolean clipped;

    private final TimeUtil pearlTimer = new TimeUtil();

    private Vector lastMotion = new Vector(0, 0, 0);
    private long groundTimer;
    boolean firstEnable;
    boolean nearEntity;

    private final Vec3 lastReportedPosition = new Vec3(0, 0, 0);
    private int packet = 0;
    private final int tick = 0;

    private boolean hypixelVanillaFly;

    public static double roundToOnGround(final double posY) {
        return posY - (posY % 0.015625);
    }

    @Override
    protected void onDisable() {
        mc.gameSettings.fovSetting = fov;
        clipped = false;
        hypixelStart = false;
        mc.thePlayer.speedInAir = 0.02f;
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.capabilities.flySpeed = startSpeed;

        mc.timer.timerSpeed = 1;

        dmged = false;
        beingDmged = false;

        saveSlot = -1;
        speed = 0.0f;
        jumped = false;

        EntityPlayer.enableCameraYOffset = false;

        switch (mode.getMode()) {
            case "Creative":
                mc.thePlayer.capabilities.allowFlying = startFlyingCapability;
                break;

            case "Bow Longjump":
                if (flag) {
                    if (mc.thePlayer.inventory.currentItem != i)
                        PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                }
                break;

            case "Vicnix":
                mc.thePlayer.motionY = 0;
                break;

            case "Verus":
                PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                MoveUtil.strafe(0);
                break;

            case "MineBox":
            case "Damage":
            case "VerusBlack":
            case "Minemenclub":
            case "Verus Lag":
            case "Vanilla":
                MoveUtil.stop();
                break;

            case "Hypixel New":
                //   MoveUtil.strafe(0.2);
                break;

            case "Hypixel Fast Lag":
                if (PlayerUtil.isOnServer("Hypixel"))
                    PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                MoveUtil.stop();
                break;

            case "Hypixel":
            case "Hypixel Blink":
            case "Hypixel Infinite":
                if (pearlSlot != -1) {
                    MoveUtil.stop();
                    pearlSlot = -1;
                    pearlFly = false;
                }
                break;

            case "Matrix1.17":
                PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                break;

            case "Vulcan":
                mc.thePlayer.motionY = -0.09800000190735147;
                MoveUtil.stop();
                MoveUtil.strafe(0.1);
                break;
        }

        if (packets.isEmpty()) return;
        packets.forEach(PacketUtil::sendPacketWithoutEvent);
        packets.clear();
    }

    @Override
    protected void onEnable() {
        boostTicks = 0;
        fov = mc.gameSettings.fovSetting;
        clipped = false;
        hypixelStart = false;
        hypixelTicks = 0;
        flag = false;
        hypixelDisable = false;
        dmged = false;
        oPositionY = (float) mc.thePlayer.posY;
        moveSpeed = 0.18D;
        stage = 0;
        jumped = false;
        startSpeed = mc.thePlayer.capabilities.flySpeed;
        startFlyingCapability = mc.thePlayer.capabilities.allowFlying;
        packet = 0;
        bool = false;
        ticks = 0;
        startingLocationX = mc.thePlayer.posX;
        startingLocationZ = mc.thePlayer.posZ;
        startingLocationY = mc.thePlayer.posY;
        movingTowardsStartingLocation = false;
        ticksSinceFlag = 999;
        mc.timer.timerSpeed = 1.0F;
        speed = 0;
        buffer = 0;
        ticksSinceFlag = -99999999;


        // Good code?, Who cares.W
        if (fakeDamage.isEnabled() && !(mode.is("Verus") || mode.is("Minemenclub") || mode.is("Old NCP") || mode.is("Bow Longjump") || mode.is("Vulcan2") || mode.is("Damage") || mode.is("Wait for Damage") || mode.is("Bow Fly") || mode.is("ACR") || mode.is("Aac3") || mode.is("Taka"))) {
            mc.thePlayer.handleHealthUpdate((byte) 2);
        }

        switch (mode.getMode()) {
            case "Hypixel New":
                wasos = 0;
                if (!firstEnable) {
                    firstEnable = true;
                    this.registerNotification("You need speed effect to make the fly work.");
                }
                break;
            case "Redesky":
                if (!firstEnable) {
                    firstEnable = true;
                    this.registerNotification("Credits to Dort for the fly.");
                }
                break;

            case "Vulcan":
                if (inf.isEnabled() && !firstEnable) {
                    firstEnable = true;
                    this.registerNotification("Having inf on might flag.");
                }
                break;

            case "Verus Lag":
                flag = true;
                break;

            case "Verus":
                PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                break;
        }
    }


    @Override
    public void onUpdateAlwaysInGui() {
        bowFlyHBoost1.hidden = bowFlyHBoost2.hidden = bowFlyVBoost1.hidden = bowFlyVBoost2.hidden = glide.hidden = timer.hidden = multiplierInAir.hidden = strafe.hidden = !(mode.is("Bow Longjump"));

        autoStop.hidden = !mode.is("Verus");

        hypixelFlySpeed.hidden = !mode.is("Hypixel New");

        flySpeed.hidden = !(mode.is("Vanilla") || mode.is("MineBox") || mode.is("Hypixel Fast Lag") || mode.is("Taka") || mode.is("Minemenclub") || mode.is("Old NCP") || mode.is("Mush") || mode.is("Viper") || mode.is("Verus Lag") || mode.is("Verus") || mode.is("Collide") || mode.is("Creative") || mode.is("Damage") || mode.is("Hypixel Infinite") || mode.is("Hypixel Blink") || mode.is("Hypixel"));

        fakeDamage.hidden = (mode.is("Verus") || mode.is("Minemenclub") || mode.is("Old NCP") || mode.is("Bow Longjump") || mode.is("Vulcan2") || mode.is("Damage") || mode.is("Wait for Damage") || mode.is("Bow Fly") || mode.is("ACR") || mode.is("Aac3") || mode.is("Taka"));

        groundSpoof.hidden = !mode.is("Exempted Value");

        damage.hidden = !mode.is("Old NCP");

        inf.hidden = !mode.is("Vulcan");

        redeskyMode.hidden = !mode.is("Redesky");

        timer.hidden = !(mode.is("Hypixel") || mode.is("Hypixel Blink") || mode.is("Old NCP") || mode.is("Bow Longjump"));

        vanillaBypass.hidden = !mode.is("Vanilla");

        delayKickBypass.hidden = !(mode.is("Vanilla") && vanillaBypass.is("Packet"));

        exemptedValueType.hidden = !mode.is("Exempted Value");


    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (sigmaFastFly.isEnabled()) mc.gameSettings.fovSetting = 150;

        ++ticks;

        if (vanillaBypass.is("Packet")) {
            if (!delayKickBypass.isEnabled() || mc.thePlayer.ticksExisted % 10 == 0)
                handleVanillaKickBypass();
        }

        final double x = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        final double z = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        lastDist = Math.hypot(x, z);

        if (mc.thePlayer.onGround) {
            offGroundTicks = 0;
            ++onGroundTicks;
        } else {
            onGroundTicks = 0;
            ++offGroundTicks;
        }

        if (mc.thePlayer.ticksExisted < 2) {
            packets.clear();
            mc.timer.timerSpeed = 1;
            toggleModule();
        }

        mc.thePlayer.cameraYaw = (float) viewBobbing.getValue();
        EntityPlayer.enableCameraYOffset = false;

        if (smoothCamera.isEnabled()) {
            if (mc.thePlayer.posY > oPositionY || mode.is("Aac3")) {
                EntityPlayer.enableCameraYOffset = true;
                EntityPlayer.cameraYPosition = oPositionY;
            }
        }

        final Vector motion = new Vector(mc.thePlayer.motionX, 0, mc.thePlayer.motionZ);

        final Vector lastMotion = this.lastMotion;
        this.lastMotion = motion;

        switch (mode.getMode()) {
            case "Hypixel Fast Lag": {
                hypixelDisabler();
                break;
            }

            case "Vulcan2":
                ticksSinceFlag++;
                if (!(PlayerUtil.getBlockRelativeToPlayer(0, -0.2, 0) instanceof BlockAir) && mc.thePlayer.getDistanceSq(startingLocationX, startingLocationY, startingLocationZ) > 4 * 4) {
                    mc.thePlayer.jump();
                    ticksSinceFlag = 0;
                }

                if (!(ticksSinceFlag <= 20 && ticksSinceFlag >= 0)) {
                    mc.thePlayer.motionY = 0;
                    switch (offGroundTicks) {
                        case 1:
                            mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? 1 : 0.5;
                            break;
                        case 2:
                            mc.thePlayer.motionY = mc.gameSettings.keyBindSneak.isKeyDown() ? -1 : -0.5;
                            break;
                        case 3:
                            mc.thePlayer.motionY = 0;
                            offGroundTicks = 0;
                            break;
                    }
                } else if (ticksSinceFlag >= 4) {
                    mc.thePlayer.motionY = 0;
                    mc.thePlayer.setPosition(mc.thePlayer.posX, Math.round(event.getY() / 0.5) * 0.5, mc.thePlayer.posZ);
                }

                if ((ticksSinceFlag <= 20 && ticksSinceFlag >= 0 && ticksSinceFlag >= 4) || mc.thePlayer.posY % 0.5 == 0) {
                    if (PlayerUtil.generalAntiPacketLog()) {
                        final double mathGround2 = Math.round(event.getY() / 0.015625) * 0.015625;
                        MoveUtil.strafe(0.2974 - 0.128);

                        mc.thePlayer.setPosition(mc.thePlayer.posX, mathGround2, mc.thePlayer.posZ);

                        event.setY(mathGround2);
                        event.setGround(true);
                        mc.thePlayer.onGround = true;
                    }
                }

                if (bool || (!PlayerUtil.generalAntiPacketLog() && ticks > 15)) {
                    if (PlayerUtil.generalAntiPacketLog())
                        PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition((mc.thePlayer.posX + mc.thePlayer.lastTickPosX) / 2, (mc.thePlayer.posY + mc.thePlayer.lastTickPosY) / 2, (mc.thePlayer.posZ + mc.thePlayer.lastTickPosZ) / 2, true));
                    MoveUtil.strafe(MoveUtil.moveSpeed() * 1.5 * 2);
                    mc.timer.timerSpeed = 1.2f + mc.thePlayer.hurtTime / 3f;
                } else {
                    MoveUtil.strafe(MoveUtil.moveSpeed());
                }
                break;

            case "Vulcan":
                hypixelTicks++;
                mc.thePlayer.posY = startingLocationY;
                if (ticks == 1) {
                    event.setY(event.getY() - 0.1);
                }

                if (hypixelTicks == -9) {
                    if (inf.isEnabled()) mc.thePlayer.motionY = 2.9 / 3f;
                    else mc.thePlayer.motionY = 3.7;
                    MoveUtil.strafe(3.4);
                }

                if (inf.isEnabled()) {
                    if (MoveUtil.getSpeed() > 1 && hypixelTicks > -6) MoveUtil.strafe(0.34);
                }

                if (hypixelTicks == -6) {
                    MoveUtil.strafe(0.32);
                    mc.thePlayer.motionY = 0.24813599859094576 - 0.313605186719;
                }

                if (hypixelTicks > -6 && mc.thePlayer.ticksExisted % 2 == 0) {
                    mc.thePlayer.motionY = -0.09800000190735147;
                }

                if (inf.isEnabled()) {
                    if (hypixelStart && hypixelTicks % 35 == 0) mc.thePlayer.motionY = 2.9;
                }

                if (!MoveUtil.isMoving()) MoveUtil.stop();
                break;

            case "Hypixel New": {

                wasos++;

                if (clipped) {
                    boostTicks++;
                }

                double timer = 1;

                switch (hypixelFlySpeed.getMode()) {
                    case "Normal":
                        timer = 1.2f;
                        break;

                    case "Airstrike":
                        timer = 1.7f;
                        break;

                    case "Intercontinental ballistic missile":
                        timer = 1.9f;
                        break;
                }

                if (wasos > 1) {
                    MoveUtil.strafe(MoveUtil.getBaseMoveSpeed() * .938);
                    mc.thePlayer.motionY = 0;
                    if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() > 0) {
                        if (clipped) {
                            mc.timer.timerSpeed = (float) (boostTicks < 20 ? timer * 1.2f : boostTicks < 60 ? timer * 1.7F : timer);
                        } else {
                            mc.timer.timerSpeed = (float) timer;
                        }
                    } else {
                        mc.timer.timerSpeed = 0.7f;
                    }
                }

                switch (wasos) {
                    case 1:
                        mc.thePlayer.motionY = 0.05;
                        break;
                    case 3:
                    case 4:
                    case 5:
                        event.setY(event.getY() - 0.22);
                        break;
                }
                break;
            }

            case "Matrix1.17": {
                final Integer item = PlayerUtil.findItem(null);
                if (item == null) return;
                PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(item));

                mc.timer.timerSpeed = 1.6F;

                if (mc.thePlayer.movementInput.jump && mc.thePlayer.ticksExisted % 3 == 0) {
                    mc.timer.timerSpeed = 0.6F;
                    mc.thePlayer.motionY = 0.42F;
                }

                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }

                if (mc.thePlayer.fallDistance > 1) {
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(null));
                    mc.thePlayer.motionY = -((mc.thePlayer.posY) - Math.floor(mc.thePlayer.posY));
                }

                if (mc.thePlayer.ticksExisted % 4 == 0) {
                    if (mc.thePlayer.ticksExisted % 24 == 0) {
                        PacketUtil.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                    }
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(null));
                }

                if (mc.thePlayer.motionY == 0) {
                    mc.thePlayer.motionY = 0.42f;
                    mc.thePlayer.fallDistance = 0;
                }
                break;
            }

            case "MineBox":
            case "Vanilla": {
                float kickBypassMotion = (float) 0.0626D;

                if (mc.thePlayer.ticksExisted % 2 == 0)
                    kickBypassMotion = -kickBypassMotion;

                mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? flySpeed.getValue() : mc.gameSettings.keyBindSneak.isKeyDown() ? -flySpeed.getValue() : vanillaBypass.is("Motion") ? kickBypassMotion : Math.random() / 1000;

                if (MoveUtil.isMoving())
                    MoveUtil.strafe(flySpeed.getValue() + Math.random() / 100);
                else
                    MoveUtil.stop();
                break;
            }

            case "Verus Lag": {
                if (flag || mc.thePlayer.ticksExisted % 20 == 0) { // I have no idea why this works but it does
                    event.setY(event.getY() - 20 - Math.random());
                    flag = false;
                }

                if (mc.thePlayer.posY % (1.0F / 64.0F) < 0.005)
                    event.setGround(true);

                mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? flySpeed.getValue() : mc.gameSettings.keyBindSneak.isKeyDown() ? -flySpeed.getValue() : 0;

                if (MoveUtil.isMoving())
                    MoveUtil.strafe(flySpeed.getValue());
                else
                    MoveUtil.stop();
                break;
            }

            case "Verus": {
                if (ticks == 1) {
                    if (mc.thePlayer.isPotionActive(Potion.jump)) {
                        this.registerNotification("Cannot enable " + this.getModuleInfo().name() + " with Jump Boost.");
                        this.setEnabled(false);
                        return;
                    }

                    if (!mc.thePlayer.onGround) {
                        this.registerNotification("Cannot enable " + this.getModuleInfo().name() + " in air.");
                        this.setEnabled(false);
                        return;
                    }
                }

                if (offGroundTicks >= 2 && !mc.gameSettings.keyBindSneak.isKeyDown()) {
                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        if (mc.thePlayer.ticksExisted % 2 == 0)
                            mc.thePlayer.motionY = 0.42F;
                    } else {
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.jump();
                        }

                        if (mc.thePlayer.fallDistance > 1) {
                            mc.thePlayer.motionY = -((mc.thePlayer.posY) - Math.floor(mc.thePlayer.posY));
                        }

                        if (mc.thePlayer.motionY == 0) {
                            mc.thePlayer.jump();

                            mc.thePlayer.onGround = true;
                            mc.thePlayer.fallDistance = 0;
                            event.setGround(true);
                        }
                    }
                }

                if (ticks > 30 && autoStop.isEnabled())
                    MoveUtil.strafe();
                else
                    MoveUtil.strafe(flySpeed.getValue());

                if ((ticks == 30 && autoStop.isEnabled()) || !MoveUtil.isMoving())
                    MoveUtil.stop();

                if (ticks == 1) {
                    mc.timer.timerSpeed = 0.15F;
                    DamageUtil.damagePlayer(DamageUtil.DamageType.POSITION, 3.42F, 1, true, false);
                    mc.thePlayer.jump();
                    event.setGround(true);
                } else
                    mc.timer.timerSpeed = 1;
                break;
            }

            case "Minemenclub": {
                if (MoveUtil.isMoving() && (PlayerUtil.isOnServer("minemen") || PlayerUtil.isOnServer("mineman"))) {
                    Rise.addChatMessage("made by billionairkse111");
                    if (mc.thePlayer.onGround) {
                        if (!beingDmged) {
                            event.setY(event.getY() - (2.5 - Math.random() / 100));
                            jumped = false;
                        } else if (!jumped) {
                            mc.thePlayer.motionY = 0.6F;
                            jumped = true;
                        }
                    }

                    if (jumped) {
                        if (offGroundTicks == 5)
                            hypixelStart = true;

                        if (hypixelStart)
                            mc.thePlayer.motionY = -0.0625D;

                        if (MoveUtil.isMoving())
                            MoveUtil.strafe(flySpeed.getValue());
                        else
                            MoveUtil.stop();
                    }
                }

                beingDmged = mc.thePlayer.onGround && mc.thePlayer.hurtTime > 0;

//                if (offGroundTicks == 7) {
//                    packets.forEach(PacketUtil::sendPacketWithoutEvent);
//                    packets.clear();
//                }
//
//                if (mc.thePlayer.onGround) {
//                    mc.thePlayer.jump();
//                }
//
//                if (mc.thePlayer.fallDistance > 1) {
//                    mc.thePlayer.motionY = -((mc.thePlayer.posY) - Math.floor(mc.thePlayer.posY));
//                }
//
//                if (mc.thePlayer.motionY == 0) {
//                    mc.thePlayer.jump();
//
//                    offGroundTicks = 0;
//                    mc.thePlayer.onGround = true;
//                    mc.thePlayer.fallDistance = 0;
//                    event.setGround(true);
//                }
//
//                MoveUtil.strafe();
                break;
            }

            case "Hypixel":
            case "Hypixel Blink":
                if (pearlTimer.hasReached(3500L) && pearlSlot != -1) {
                    mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? flySpeed.getValue() : mc.gameSettings.keyBindSneak.isKeyDown() ? -flySpeed.getValue() : 0;

                    if (mc.thePlayer.posY % (1.0F / 64.0F) < 0.005)
                        event.setGround(true);

                    if (MoveUtil.isMoving())
                        MoveUtil.strafe(flySpeed.getValue());
                    else
                        MoveUtil.stop();
                } else if (stage == 2 && mode.is("Hypixel") && PlayerUtil.isOnServer("Hypixel")) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + RandomUtils.nextDouble(7E-8, 9E-8), mc.thePlayer.posZ);
                }

                if (pearlFly) {
                    event.setPitch(RandomUtils.nextFloat(89, 90));
                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, RandomUtils.nextFloat(89, 90), mc.thePlayer.onGround));
                    PacketUtil.sendPacket(new C09PacketHeldItemChange(pearlSlot));
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(pearlSlot)));
                    PacketUtil.sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    pearlFly = false;
                }
                break;

            case "Vicnix":
                final double speed = 0.6;

                if (MoveUtil.isMoving()) {
                    MoveUtil.strafe(speed);
                    moveSpeed += speed;
                } else {
                    MoveUtil.stop();
                }

                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.motionY = speed;
                    moveSpeed += speed;
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.motionY = -speed;
                    moveSpeed += speed;
                } else {
                    mc.thePlayer.motionY = 0 - Math.random() / 100;
                }

                moveSpeed += mc.thePlayer.getDistance(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ);
                break;

            case "Damage": {
                mc.timer.timerSpeed = 1;
                if (ticks == 1) {
                    mc.timer.timerSpeed = 0.25f;
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.42f, mc.thePlayer.posZ, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                    mc.thePlayer.jump();
                } else {
                    if (mc.thePlayer.moveForward != 0.0 || mc.thePlayer.moveStrafing != 0.0) {
                        MoveUtil.strafe(flySpeed.getValue());
                    } else {
                        MoveUtil.stop();
                    }

                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.thePlayer.motionY = flySpeed.getValue();
                    } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                        mc.thePlayer.motionY = -flySpeed.getValue();
                    } else {
                        mc.thePlayer.motionY = 0.0;
                    }
                }

                break;
            }
            case "Creative": {
                mc.thePlayer.capabilities.isFlying = true;
                mc.thePlayer.capabilities.flySpeed = (float) (flySpeed.getValue() / 16);
                break;
            }

            case "Bow Longjump": {
                ItemStack itemStack;

                ticks--;

                if (!jumped) {
                    event.setPitch(-RandomUtils.nextFloat(89, 90));
                }

                int sexySlot = -1;
                int shitSlot = -1;

                //Jumped is just saying the the bow or rod has been shot
                if (!jumped) {
                    for (i = 0; i < 9; ++i) {
                        itemStack = mc.thePlayer.inventoryContainer.getSlot(i + 36).getStack();

                        if (itemStack != null) {
                            final Item item = itemStack.getItem();

                            if (itemStack.getItem() instanceof ItemBow) {
                                for (int i = 0; i < mc.thePlayer.inventory.mainInventory.length; i++) {
                                    final ItemStack stack = mc.thePlayer.inventory.mainInventory[i];

                                    if (stack != null) {
                                        if (stack.getItem().getUnlocalizedName().contains("arrow")) {
                                            sexySlot = this.i;
                                        }
                                    }
                                }
                            }

                            if (item instanceof ItemFishingRod || item instanceof ItemSnowball || item instanceof ItemEgg) {
                                flag = true;
                                shitSlot = i;
                            }
                        }
                    }

                    if (sexySlot != -1) {
                        itemStack = mc.thePlayer.inventoryContainer.getSlot(sexySlot + 36).getStack();

                        if (mc.thePlayer.inventory.currentItem != sexySlot && ticks == 1)
                            PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(sexySlot));

                        if (ticks == 5 + randomInt(0, 1))
                            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(itemStack));

                        ticks++;

                        if (ticks == 9 + randomInt(1, 2)) {
                            PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            jumped = true;
                        }
                    } else if (shitSlot != -1) {
                        itemStack = mc.thePlayer.inventoryContainer.getSlot(shitSlot + 36).getStack();

                        if (mc.thePlayer.inventory.currentItem != shitSlot)
                            PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(shitSlot));

                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(itemStack));

                        jumped = true;
                    }
                }

                //Bow longjump

                if (!jumped)
                    mc.timer.timerSpeed = (float) timer.getValue();

                if (mc.thePlayer.hurtTime == 9) {

                    if (mc.thePlayer.inventory.currentItem != i)
                        PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));

                    hypixelStart = true;
                    mc.thePlayer.jump();

                    if (bowFlyVBoost1.getValue() != 0)
                        mc.thePlayer.motionY = bowFlyVBoost1.getValue();

                    if (bowFlyHBoost1.getValue() != 0)
                        MoveUtil.strafe(bowFlyHBoost1.getValue());

                }

                if (mc.thePlayer.hurtTime == 8) {
                    if (bowFlyVBoost2.getValue() != 0)
                        mc.thePlayer.motionY = bowFlyVBoost2.getValue();

                    if (bowFlyHBoost2.getValue() != 0)
                        MoveUtil.strafe(bowFlyHBoost2.getValue());
                }

                if (offGroundTicks <= 5 && !mc.thePlayer.onGround) {
                    mc.thePlayer.motionY += glide.getValue();
                }

                if (offGroundTicks <= 5 && !mc.thePlayer.onGround) {
                    MoveUtil.strafe(MoveUtil.getSpeed() * (1 + (multiplierInAir.getValue() - 1) / 5));
                }

                if (strafe.isEnabled())
                    MoveUtil.strafe();

                //NCP Longjump part, only have this part uncommented for it to work
                /*
                if (mc.thePlayer.hurtTime == 9) {
                    hypixelStart = true;
                }

                if (hypixelStart) {

                    if (mc.thePlayer.hurtTime == 8) {
                        mc.thePlayer.motionY = 0.45f;
                        MoveUtil.strafe(MoveUtil.getBaseMoveSpeed() * 2.59);
                    }

                    if (mc.thePlayer.hurtTime == 7) {
                        mc.thePlayer.motionY += 0.03;
                    }

                    if (mc.thePlayer.hurtTime <= 6) {
                        mc.thePlayer.motionY += 0.015;
                    }

                }

                MoveUtil.strafe();
                 */

                break;
            }

            case "Bow Fly": {
                ItemStack itemStack;

                ticks--;

                if (!jumped) {
                    event.setPitch(-90.0F);
                }

                int sexySlot = -1;
                int shitSlot = -1;

                //Jumped is just saying the the bow or rod has been shot
                if (!jumped) {
                    for (i = 0; i < 9; ++i) {
                        itemStack = mc.thePlayer.inventoryContainer.getSlot(i + 36).getStack();

                        if (itemStack != null) {
                            final Item item = itemStack.getItem();

                            if (itemStack.getItem() instanceof ItemBow) {
                                for (int i = 0; i < mc.thePlayer.inventory.mainInventory.length; i++) {
                                    final ItemStack stack = mc.thePlayer.inventory.mainInventory[i];

                                    if (stack != null) {
                                        if (stack.getItem().getUnlocalizedName().contains("arrow")) {
                                            sexySlot = this.i;
                                        }
                                    }
                                }
                            }

                            if (item instanceof ItemFishingRod || item instanceof ItemSnowball || item instanceof ItemEgg) {
                                flag = true;
                                shitSlot = i;
                            }
                        }
                    }

                    if (offGroundTicks <= 3 && !mc.thePlayer.onGround) {
                        mc.thePlayer.motionY += glide.getValue();
                        MoveUtil.strafe(MoveUtil.getSpeed() * multiplierInAir.getValue());
                    }

                    if (sexySlot != -1) {
                        itemStack = mc.thePlayer.inventoryContainer.getSlot(sexySlot + 36).getStack();

                        if (mc.thePlayer.inventory.currentItem != sexySlot && ticks == 1)
                            PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(sexySlot));

                        if (ticks == 5 + randomInt(0, 1))
                            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(itemStack));

                        ticks++;

                        if (ticks == 9 + randomInt(0, 1)) {
                            PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            jumped = true;
                        }
                    } else if (shitSlot != -1) {
                        itemStack = mc.thePlayer.inventoryContainer.getSlot(shitSlot + 36).getStack();

                        if (mc.thePlayer.inventory.currentItem != shitSlot)
                            PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(shitSlot));

                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, -90, mc.thePlayer.onGround));
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(itemStack));

                        jumped = true;
                    }
                }

                if (dmged) {
                    MoveUtil.strafe();

                    if (mc.thePlayer.hurtTime == 9) {
                        MoveUtil.strafe(MoveUtil.getBaseMoveSpeed() * 2.13);
                    } else {
                        MoveUtil.strafe(MoveUtil.getBaseMoveSpeed());
                    }

                    mc.thePlayer.motionY = 0;
                }
                break;
            }

            case "ACR": {
                mc.timer.timerSpeed = 1.0F;

                if (mc.thePlayer.fallDistance > 4.5) {
                    event.setGround(true);

                    mc.thePlayer.motionY = 0.42f;
                    ticks = -5;
                    MoveUtil.stop();

                    mc.timer.timerSpeed = 0.15f;
                    mc.thePlayer.fallDistance = 0;
                }

                if (ticks == -4) {
                    mc.thePlayer.motionY = 0.99;
                }

                break;
            }
            case "ACR2": {
                MoveUtil.strafe(0.06);
                if (mc.thePlayer.ticksExisted % 3 == 0) {
                    mc.thePlayer.motionY = 0.40444491418477213;
                    offGroundTicks = 0;
                }

                if (offGroundTicks == 1) {
                    MoveUtil.strafe(0.36);
                    mc.thePlayer.motionY = 0.33319999363422365;
                }

                break;
            }

            case "Hypixel Infinite":
                if (pearlTimer.hasReached(3500L) && pearlSlot != -1) {
                    mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? flySpeed.getValue() : mc.gameSettings.keyBindSneak.isKeyDown() ? -flySpeed.getValue() : 0;

                    if (mc.thePlayer.posY % (1.0F / 64.0F) < 0.005)
                        event.setGround(true);

                    if (MoveUtil.isMoving())
                        MoveUtil.strafe(flySpeed.getValue());
                    else
                        MoveUtil.stop();
                } else if (stage == 2 && mode.is("Hypixel") && PlayerUtil.isOnServer("Hypixel")) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + RandomUtils.nextDouble(7E-8, 9E-8), mc.thePlayer.posZ);
                }

                if (pearlFly) {
                    event.setPitch(RandomUtils.nextFloat(89, 90));
                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, RandomUtils.nextFloat(89, 90), mc.thePlayer.onGround));
                    PacketUtil.sendPacket(new C09PacketHeldItemChange(pearlSlot));
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(pearlSlot)));
                    PacketUtil.sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    pearlFly = false;
                }

                if (pearlSlot != -1)
                    return;

                if (!mc.thePlayer.onGround) {
                    if (PlayerUtil.isOnServer("Hypixel")) {
                        event.setY(event.getY() - 1.89 + Math.random() / 500);
                        event.setGround(true);
                    } else {
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(-1, -1, -1, true));
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition());
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                    }
                }
                mc.timer.timerSpeed = (float) (0.3 - Math.random() / 500);
                break;

            case "Verus AirJump": {
                MoveUtil.strafe();
                if (!mc.gameSettings.keyBindSneak.isKeyDown()) {
                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        if (mc.thePlayer.ticksExisted % 2 == 0)
                            mc.thePlayer.motionY = 0.42F;
                    } else {
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.jump();
                        }

                        if (mc.thePlayer.fallDistance > 1) {
                            mc.thePlayer.motionY = -((mc.thePlayer.posY) - Math.floor(mc.thePlayer.posY));
                        }

                        if (mc.thePlayer.motionY == 0) {
                            mc.thePlayer.jump();

                            mc.thePlayer.onGround = true;
                            mc.thePlayer.fallDistance = 0;
                            event.setGround(true);
                        }
                    }
                }
                break;
            }

            case "Collide": {
                if (!mc.gameSettings.keyBindSneak.isKeyDown()) {
                    if (MoveUtil.isMoving()) {
                        MoveUtil.strafe(flySpeed.getValue());
                    } else {
                        MoveUtil.stop();
                    }

                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.thePlayer.motionY = -(mc.thePlayer.posY - roundToOnGround(mc.thePlayer.posY + 0.5));
                    } else {
                        mc.thePlayer.motionY = -(mc.thePlayer.posY - roundToOnGround(mc.thePlayer.posY));
                    }

                    if (mc.thePlayer.posY % (1.0F / 64.0F) < 0.005) {
                        event.setGround(true);
                    }
                }

                break;
            }

            case "Viper": {
                if (!mc.gameSettings.keyBindSneak.isKeyDown()) {
                    if (mc.gameSettings.keyBindJump.isKeyDown())
                        mc.thePlayer.motionY = -(mc.thePlayer.posY - roundToOnGround(mc.thePlayer.posY + 0.5));
                    else
                        mc.thePlayer.motionY = -(mc.thePlayer.posY - roundToOnGround(mc.thePlayer.posY));

                    if (mc.thePlayer.posY % (1.0F / 64.0F) < 0.005)
                        event.setGround(true);
                } else
                    MoveUtil.strafe();

                if (MoveUtil.isMoving())
                    MoveUtil.sendMotion(flySpeed.getValue(), 0.2873);
                else
                    MoveUtil.stop();
                break;
            }

            case "Spartan": {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }

                if (mc.thePlayer.fallDistance > 1) {
                    mc.thePlayer.motionY = -((mc.thePlayer.posY) - Math.floor(mc.thePlayer.posY));
                }

                if (mc.thePlayer.motionY == 0) {
                    mc.thePlayer.jump();

                    mc.thePlayer.onGround = true;
                    event.setGround(true);
                    mc.thePlayer.fallDistance = 0;
                }
                break;
            }
            case "Aac3": {
                mc.timer.timerSpeed = 1f;
                if (mc.thePlayer.posY < -75) {
                    mc.thePlayer.motionY = 6.9;
                }
                mc.thePlayer.motionY -= 0.0001;
                break;
            }

            case "MCCentral": {
                if (mc.thePlayer.posY < oPositionY + 0.6 && !mc.gameSettings.keyBindSneak.isKeyDown() || mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.motionY += 0.12;

                    if (MoveUtil.isMoving()) {
                        mc.thePlayer.motionX *= 1.1;
                        mc.thePlayer.motionZ *= 1.1;
                    }

                    if (mc.gameSettings.keyBindJump.isKeyDown() || mc.gameSettings.keyBindSneak.isKeyDown()) {
                        oPositionY = (float) mc.thePlayer.posY;
                    }
                }
                if (ticks == 1) {
                    mc.thePlayer.motionX *= 1.4;
                    mc.thePlayer.motionZ *= 1.4;
                }

                break;
            }

            case "Exempted Value": {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                } else {
                    if (mc.thePlayer.fallDistance > 0) {

                        double v = 0;
                        switch (exemptedValueType.getMode()) {
                            case "1":
                                v = -0.0784000015258789;
                                break;
                            case "2":
                                v = -0.09800000190734864;
                                break;
                            case "3":
                                v = -0.09800000190735147;
                                break;
                        }

                        if (mc.thePlayer.motionY < v) {
                            mc.thePlayer.motionY = v;
                            if (groundSpoof.isEnabled())
                                event.setGround(true);
                        }
                    }
                }

                MoveUtil.strafe();
                break;
            }
            case "Kauri": {
                mc.timer.timerSpeed = 1.0F;

                if (!mc.gameSettings.keyBindSneak.isKeyDown()) {
                    ticks = 0;

                    if (MoveUtil.isMoving()) {
                        MoveUtil.strafe(mc.thePlayer.ticksExisted % 5 != 0 ? 0.25 : 0.625);
                    } else {
                        MoveUtil.stop();
                    }

                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.thePlayer.motionY = 0.42F;
                    } else {
                        mc.thePlayer.motionY = -(mc.thePlayer.posY - roundToOnGround(mc.thePlayer.posY));
                    }

                    event.setGround(true);
                } else {
                    if (ticks == 1) {
                        event.setGround(true);
                    }
                }
            }

            case "Taka": {
                if (stage > 2) mc.thePlayer.motionY = -0.005;
                mc.timer.timerSpeed = 1.0f;

                if (stage > 2) {
                    ticks++;

                    if (ticks <= 2) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2.02069E-14, mc.thePlayer.posZ);
                    } else if (ticks == 4) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.62029E-6, mc.thePlayer.posZ);
                    } else if (ticks >= 5) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.12039E-44, mc.thePlayer.posZ);
                        ticks = 0;
                    }

                }

                break;
            }

            case "New": {
                mc.thePlayer.motionY = 0;
                break;
            }

            case "VerusBlack": {
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                        mc.thePlayer.motionY = 0.42F;
                        MoveUtil.strafe(0.3);
                    }
                }
                break;
            }

            case "Wait for Damage": {
                if (mc.thePlayer.hurtTime > 0) {
                    if (mc.thePlayer.moveForward != 0.0 || mc.thePlayer.moveStrafing != 0.0) {
                        MoveUtil.strafe(flySpeed.getValue());
                    } else {
                        MoveUtil.stop();
                    }

                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.thePlayer.motionY = flySpeed.getValue();
                    } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                        mc.thePlayer.motionY = -flySpeed.getValue();
                    } else {
                        mc.thePlayer.motionY = 0.0;
                    }
                }

                break;
            }
            case "Old NCP": {
                if (mc.thePlayer.isCollidedHorizontally || !MoveUtil.isMoving() || (stage >= 15 && !jumped && !damage.isEnabled())) {
                    moveSpeed = MoveUtil.getBaseMoveSpeed();
                    mc.timer.timerSpeed = 1;
                }

                if (mc.thePlayer.hurtTime > 0)
                    jumped = true;


                if (stage > 2) {
                    hypixelTicks++;

                    double value = RandomUtils.nextDouble(10E-8, 12E-8);

                    if (mc.thePlayer.ticksExisted % 2 == 0)
                        value = -value;

                    if (!mc.thePlayer.onGround)
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + value, mc.thePlayer.posZ);

                    mc.thePlayer.motionY = 0;
                }
                break;
            }
            case "Mush": {
                if (stage > 2) {
                    hypixelTicks++;
                    mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? moveSpeed : mc.gameSettings.keyBindSneak.isKeyDown() ? -moveSpeed : 0;
                }
                break;
            }
            case "BlockFly": {
                final BlockPos down = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
                if (mc.theWorld.getBlockState(down).getBlock() instanceof BlockAir && (mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBlock) && mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), down, EnumFacing.UP, new Vec3(0.5, 0.5, 0.5)))
                    mc.thePlayer.swingItem();
                break;
            }
        }

    }

    @Override
    public void onMove(final MoveEvent event) {
        switch (mode.getMode()) {
            case "Old NCP":
                switch (stage) {
                    case 1:
                        if (mc.thePlayer.onGround)
                            event.setY(mc.thePlayer.motionY = MoveUtil.getJumpMotion(0.42F));

                        if (damage.isEnabled())
                            DamageUtil.damagePlayer(DamageUtil.DamageType.POSITION, 0.0625, true, true);

                        moveSpeed = 0.398 - Math.random() / 500;
                        break;

                    case 3:
                        moveSpeed = flySpeed.getValue() + Math.random() / 100;
                        mc.timer.timerSpeed = (float) timer.getValue();
                        break;

                    default:
                        moveSpeed -= moveSpeed / RandomUtils.nextDouble(159, 159.1);
                        mc.timer.timerSpeed = (float) Math.max(1, mc.timer.timerSpeed - (Math.random() / 50));
                        break;
                }

                MoveUtil.setMoveEventSpeed(event, Math.max(moveSpeed, MoveUtil.getBaseMoveSpeed()));
                stage++;
                break;

            case "Hypixel Infinite":
                if (pearlSlot == -1) {
                    event.setY(mc.thePlayer.motionY = 0);
                    MoveUtil.setMoveEventSpeed(event, mc.thePlayer.onGround ? MoveUtil.getBaseMoveSpeed() : 3.9);
                }

                if (pearlSlot != -1 && !pearlTimer.hasReached(3500L))
                    event.setCancelled(true);
                break;

            case "VerusBlack": {
                if (ticks % 14 == 0 && mc.thePlayer.onGround) {
                    MoveUtil.strafe(0.69);
                    event.setY(0.42F);
                    mc.thePlayer.motionY = -(mc.thePlayer.posY - roundToOnGround(mc.thePlayer.posY));
                } else {
                    if (mc.thePlayer.onGround) {
                        MoveUtil.strafe(1.01);
                    } else MoveUtil.strafe(0.41);
                }
                ticks++;
            }
            break;

            case "Hypixel":
                if (pearlSlot == -1) {
                    switch (stage) {
                        case 0:
                            DamageUtil.damagePlayer(DamageUtil.DamageType.POSITION, PlayerUtil.isOnServer("Hypixel") ? 0.03 : 0.0625, true, true);
                            stage++;
                            break;

                        case 1:
                            mc.timer.timerSpeed = (float) (timer.getValue() - Math.random() / 100);
                            moveSpeed = MoveUtil.getBaseMoveSpeed() + (0.095 + Math.random() / 100);
                            stage++;
                            break;

                        case 2:
                            if (MoveUtil.isMoving() && !mc.thePlayer.isCollidedHorizontally) {
                                moveSpeed = moveSpeed - (MoveUtil.getBaseMoveSpeed() / RandomUtils.nextDouble(59, 59.1));
                                mc.timer.timerSpeed = (float) Math.max(1, mc.timer.timerSpeed - (Math.random() / 100));
                            } else {
                                mc.timer.timerSpeed = 1;
                                moveSpeed = 0;
                            }

                            event.setY(mc.thePlayer.motionY = 0);
                            MoveUtil.setMoveEventSpeed(event, mc.thePlayer.ticksExisted % 3 != 0 ? Math.max(MoveUtil.getBaseMoveSpeed(), moveSpeed) : 0);
                            break;
                    }
                }

                if (pearlSlot != -1 && !pearlTimer.hasReached(3500L))
                    event.setCancelled(true);
                break;

            case "Hypixel Blink":
                if (pearlSlot == -1) {
                    switch (stage) {
                        case 0:
                            DamageUtil.damagePlayer(DamageUtil.DamageType.POSITION, PlayerUtil.isOnServer("Hypixel") ? 0.03 : 0.0625, true, true);
                            stage++;
                            break;

                        case 1:
                            event.setY(mc.thePlayer.motionY = MoveUtil.getJumpMotion((float) (0.2 + Math.random() / 100)));
                            mc.timer.timerSpeed = (float) (timer.getValue() - Math.random() / 100);
                            moveSpeed = MoveUtil.getBaseMoveSpeed() + (0.2 + Math.random() / 500);
                            stage++;
                            break;

                        case 2:
                            if (MoveUtil.isMoving() && !mc.thePlayer.isCollidedHorizontally) {
                                moveSpeed = moveSpeed - (MoveUtil.getBaseMoveSpeed() / RandomUtils.nextDouble(49, 49.1));
                                mc.timer.timerSpeed = (float) Math.max(1, mc.timer.timerSpeed - (Math.random() / 100));
                            } else {
                                mc.timer.timerSpeed = 1;
                                moveSpeed = 0;
                            }

                            event.setY(mc.thePlayer.motionY = (0.001 + Math.random() / 5000));
                            MoveUtil.setMoveEventSpeed(event, Math.max(MoveUtil.getBaseMoveSpeed(), moveSpeed));
                            break;
                    }
                }

                if (pearlSlot != -1 && !pearlTimer.hasReached(3500L))
                    event.setCancelled(true);
                break;

            case "Mush":
                switch (stage) {
                    case 1:
                        if (mc.thePlayer.onGround)
                            event.setY(mc.thePlayer.motionY = MoveUtil.getJumpMotion(0.42F));
                        moveSpeed = MoveUtil.getBaseMoveSpeed();
                        break;

                    case 3:
                        moveSpeed = flySpeed.getValue() + Math.random() / 100;
                        break;

                    default:
                        moveSpeed -= RandomUtils.nextDouble(10E-8, 12E-8);
                        break;
                }

                MoveUtil.setMoveEventSpeed(event, Math.max(moveSpeed, MoveUtil.getBaseMoveSpeed()));
                stage++;
                break;

            case "Bow Fly":
                if (!dmged)
                    event.setCancelled(true);
                break;

            case "Bow Longjump":
                if (!hypixelStart)
                    event.setCancelled(true);
                break;

            case "Taka": {
                final float jump = (0.42f);

                switch (stage) {
                    case 0:
                        if (mc.thePlayer.onGround) {
                            mc.timer.timerSpeed = 0.15F;
                            DamageUtil.damagePlayer(DamageUtil.DamageType.POSITION, 0.42F, true, true);
                        }
                        break;
                    case 1:
                        if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) event.setY(jump);

                        mc.thePlayer.motionY = jump;
                        moveSpeed = 0.2889634 - (Math.random() / 500);

                        break;
                }

                stage++;

                if (stage < 19) {
                    MoveUtil.strafe(flySpeed.getValue());
                } else {
                    MoveUtil.strafe(Math.max(moveSpeed, MoveUtil.getBaseMoveSpeed()));
                }

                if (mc.thePlayer.isCollidedHorizontally || !MoveUtil.isMoving()) {
                    moveSpeed = MoveUtil.getBaseMoveSpeed();
                }
                break;
            }

            case "Minemenclub": {
                if (!jumped)
                    event.setCancelled(true);
                break;
            }

            case "Redesky": {
                double speed = 4.5f;
                mc.timer.timerSpeed = 0.5f;

                if (redeskyMode.is("Fast")) {
                    speed = 8.5;
                    mc.timer.timerSpeed = 2;
                }

                if (!mc.isIntegratedServerRunning()) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 1.0E159, mc.thePlayer.posZ + 10, 0, 0, true));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, 0, 0, true));
                }

                MoveUtil.setMoveEventSpeed(event, speed);
                event.setY(mc.thePlayer.motionY = (mc.thePlayer.movementInput.jump ? speed : mc.thePlayer.movementInput.sneak ? -speed : 0) * 0.5);

                break;
            }
        }
    }

    private boolean aboveVoidPredicted() {

        final float multi = 8;

        for (int i = 0; i <= ((mc.thePlayer.onGround) ? 1.7 : 6); i++) {

            final BlockPos pos1 = new BlockPos(mc.thePlayer.posX + mc.thePlayer.motionX * multi + 0.3, mc.thePlayer.posY - i + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ + mc.thePlayer.motionZ * multi);
            final BlockPos pos2 = new BlockPos(mc.thePlayer.posX + mc.thePlayer.motionX * multi - 0.3, mc.thePlayer.posY - i + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ + mc.thePlayer.motionZ * multi);
            final BlockPos pos3 = new BlockPos(mc.thePlayer.posX + mc.thePlayer.motionX * multi, mc.thePlayer.posY - i + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ + mc.thePlayer.motionZ * multi + 0.3);
            final BlockPos pos4 = new BlockPos(mc.thePlayer.posX + mc.thePlayer.motionX * multi, mc.thePlayer.posY - i + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ + mc.thePlayer.motionZ * multi - 0.3);

            if (!(mc.theWorld.getBlockState(pos1).getBlock() instanceof BlockAir && mc.theWorld.getBlockState(pos2).getBlock() instanceof BlockAir && mc.theWorld.getBlockState(pos3).getBlock() instanceof BlockAir && mc.theWorld.getBlockState(pos4).getBlock() instanceof BlockAir)) {
                return false;
            }

        }

        return true;
    }

    @Override
    public void onStrafe(final StrafeEvent event) {
        switch (mode.getMode()) {
            case "Hypixel Fast Lag": {
                mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? 0.42F : mc.gameSettings.keyBindSneak.isKeyDown() ? mc.thePlayer.motionY : 0;
                event.setFriction((float) (mc.thePlayer.moveForward != 0 && mc.thePlayer.moveStrafing != 0 ? flySpeed.getValue() * 0.91F : flySpeed.getValue()));

                MoveUtil.stop();
                break;
            }

            case "Hypixel New": {
//                event.setSpeed((float) MoveUtil.getBaseMoveSpeed() * 0.91F, Math.random() / 2000);
                break;
            }
        }
    }

    @Override
    public void onMoveButton(final MoveButtonEvent event) {
        if (!mode.is("Creative") && !mode.is("Redesky"))
            event.setSneak(false);

        switch (mode.getMode()) {
            case "Vulcan2":
                if (ticksSinceFlag <= 20 && ticksSinceFlag > 0) {
                    event.setBackward(false);
                    event.setForward(false);
                    event.setLeft(false);
                    event.setRight(false);
                    MoveUtil.stop();
                }
                event.setJump(false);
                break;
        }
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (mc.isIntegratedServerRunning()) return;

        final Packet<?> p = event.getPacket();

        switch (mode.getMode()) {

            case "MineBox":
                if (PlayerUtil.generalAntiPacketLog() && PlayerUtil.isOnServer("minebox") && p instanceof C03PacketPlayer) {
                    event.setCancelled(true);
                }
                break;

            case "Vicnix":
                if (moveSpeed < 9.5 - MoveUtil.getSpeed() * 1.1) {
                    event.setCancelled(true);
                } else {
                    moveSpeed = 0;
                }
                break;

            case "Hypixel Blink":
                if (!(p instanceof C01PacketChatMessage) && pearlSlot == -1) {
                    packets.add(p);
                    event.setCancelled(true);
                }
                break;

            case "Spartan":
                if (flag) {
                    event.setCancelled(true);
                    if (!(p instanceof C00PacketKeepAlive) && !(p instanceof C0FPacketConfirmTransaction)) {
                        packets.add(p);
                    }

                    if (!packets.isEmpty() && mc.thePlayer.ticksExisted % 13 + (redeskyMode.is("Fast") ? 20 : 0) == 0) {
                        packets.forEach(PacketUtil::sendPacketWithoutEvent);
                        packets.clear();
                    }
                }
                break;

            case "Minemenclub":
                if (!jumped || mc.thePlayer == null) {
                    packets.clear();
                    return;
                }

                if (!(p instanceof C01PacketChatMessage)) {
                    packets.add(p);
                    event.setCancelled(true);
                }
                break;

            case "Verus":
                if (ticks < 30 || !autoStop.isEnabled()) {
                    if (p instanceof C0BPacketEntityAction) {
                        final C0BPacketEntityAction c0B = (C0BPacketEntityAction) p;

                        if (c0B.getAction().equals(C0BPacketEntityAction.Action.START_SPRINTING)) {
                            if (EntityPlayerSP.serverSprintState) {
                                PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                                EntityPlayerSP.serverSprintState = false;
                            }
                            event.setCancelled(true);
                        }

                        if (c0B.getAction().equals(C0BPacketEntityAction.Action.STOP_SPRINTING)) {
                            event.setCancelled(true);
                        }
                    }
                }
                break;

            case "Hypixel Fast Lag":
                packets.add(p);
                event.setCancelled(true);
                break;
        }
    }

    @Override
    public void onBlockCollide(final BlockCollideEvent event) {
        switch (mode.getMode()) {
            case "VerusBlack": {
                if (event.getBlock() instanceof BlockAir && !mc.thePlayer.isSneaking()) {
                    final double x = event.getX(), y = event.getY(), z = event.getZ();

                    if (y < mc.thePlayer.posY) {
                        event.setCollisionBoundingBox(AxisAlignedBB.fromBounds(-15, -1, -15, 15, 1, 15).offset(x, y, z));
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        final Packet<?> p = event.getPacket();

        if (p instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity) p).getEntityID() == mc.thePlayer.getEntityId()) {
            if (mode.is("Bow Fly")) {
                dmged = true;
                speed = 0.6f;
                mc.thePlayer.motionY = 0.42;
            }

            event.setCancelled(true);
        }

        if (mode.is("Minemenclub") && (p instanceof S12PacketEntityVelocity || p instanceof S27PacketExplosion)) {
            event.setCancelled(true);
        }

        if (p instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook) p;
            switch (mode.getMode()) {
                case "Hypixel New":
                    clipped = true;
                    break;
                case "Redesky":
                    flag = true;
                    break;

                case "Verus":
                    this.toggleModule();
                    this.registerNotification("Disabled " + this.getModuleInfo().name() + " due to flag.");
                    break;

                case "Minemenclub":
                    event.setCancelled(true);
                    break;

                case "Vulcan":
                    hypixelTicks = -10;
                    hypixelStart = true;
                    break;

                case "Vulcan2":
                    if (mc.thePlayer.ticksExisted > 20) {
                        if (Math.abs(s08.x - startingLocationX) + Math.abs(s08.y - startingLocationY) + Math.abs(s08.z - startingLocationZ) < 4) {
                            if (PlayerUtil.generalAntiPacketLog()) event.setCancelled(true);
                            if (!bool) {
                                mc.thePlayer.hurtTime = 9;
                                bool = true;
                                //        Rise.addChatMessage("Sent S40");
                            }
                        } else {
                            this.toggleModule();
                        }
                    }
                    break;
            }
        }
    }

    private void handleVanillaKickBypass() {
        if (System.currentTimeMillis() - groundTimer < 1000) return;

        final double x = mc.thePlayer.posX;
        final double y = mc.thePlayer.posY;
        final double z = mc.thePlayer.posZ;

        final double ground = calculateGround();

        for (double posY = y; posY > ground; posY -= 8D) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, posY, z, true));

            if (posY - 8D < ground) break; // Prevent next step
        }

        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, ground, z, true));


        for (double posY = ground; posY < y; posY += 8D) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, posY, z, true));

            if (posY + 8D > y) break; // Prevent next step
        }

        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));

        groundTimer = System.currentTimeMillis();
    }

    public double calculateGround() {
        final double y = mc.thePlayer.posY;

        final AxisAlignedBB playerBoundingBox = mc.thePlayer.getEntityBoundingBox();
        double blockHeight = 1D;

        for (double ground = y; ground > 0D; ground -= blockHeight) {
            final AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight, playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ);

            if (mc.theWorld.checkBlockCollision(customBox)) {
                if (blockHeight <= 0.05D)
                    return ground + blockHeight;

                ground += blockHeight;
                blockHeight = 0.05D;
            }
        }

        return 0F;
    }

    public double getFlightSpeed() {
        double baseSpeed = 0.262;

        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + (0.15 * amplifier);
        }

        return baseSpeed;
    }

    private void hypixelDisabler() {
        int newSlot = -1;

        for (int i = 0; i < 8; i++) {
            if (mc.thePlayer.inventory.currentItem == i)
                continue;

            final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);

            if (itemStack == null) {
                newSlot = i;
                continue;
            }

            final Item item = itemStack.getItem();

            if (!(itemStack.getDisplayName().contains("Right Click") || (item instanceof ItemPotion && ItemPotion.isSplash(itemStack.getMetadata()) || item instanceof ItemSword ||
                    item instanceof ItemEgg || item instanceof ItemSnowball || item instanceof ItemEnderPearl || item instanceof ItemEnderEye || item instanceof ItemFireball || item instanceof ItemFishingRod ||
                    item instanceof ItemEditableBook || item instanceof ItemExpBottle)))
                newSlot = i;
        }

        if (newSlot != -1) {
            PacketUtil.sendPacket(new C09PacketHeldItemChange(newSlot));
            PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(newSlot)));
            PacketUtil.sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            return;
        }

        final int nextSlot = mc.thePlayer.inventory.currentItem == 8 ? 0 : mc.thePlayer.inventory.currentItem + 1;
        final ItemStack stack = mc.thePlayer.inventory.getStackInSlot(nextSlot);
        if (stack == null || !stack.getDisplayName().contains("Right Click")) {
            PacketUtil.sendPacket(new C09PacketHeldItemChange(nextSlot));
            PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(nextSlot)));
            PacketUtil.sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
    }
}