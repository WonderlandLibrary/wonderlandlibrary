package koks.module.movement;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.utils.*;
import koks.event.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Fly", description = "You can fly", category = Module.Category.MOVEMENT)
public class Fly extends Module implements Module.Unsafe {

    public final ArrayList<Packet<?>> spartan439Packets = new ArrayList<>();
    private final TimeHelper redeSkyFlagTimeHelper = new TimeHelper(), spartan439TimeHelper = new TimeHelper(), matrix661GlideTimeHelper = new TimeHelper();
    private boolean allowedFly, aac4BoatRide, megaCraftDamaged, verus1187Damaged, verusb3733JumpSpoofed, verus1187Boosted, vanillaOnGround, dmg;
    private int verus1187Jumps, vanillaG, matrix661RandomDelay, verusb3733JumpStartY;

    @Value(name = "Mode", modes = {"Verus b1187", "Verus b3733", "AGCJump", "AAC4 (Boat)", "AAC3.3.12", "AAC1.9.10", "Hypixel20201207", "Hypixel20220104", "WatchCat2.1.7-b5", "RedeskyFlag", "Spartan406", "Spartan431", "Spartan439", "Vulcan2.0.1Void", "VerusOnlyMCOld", "Bizzi", "Vanilla", "AirJump", "Intave12.5.5", "Intave13Stairs", "Intave13.0.8", "Megacraft", "WatchDuck20211030", "MinemoraGlide", "Matrix6.6.1Glide"})
    String mode = "Vanilla";

    /* Verus b3733 */
    @Value(name = "Verus b3733-Mode", displayName = "Mode", modes = {"Slow", "Damage", "Jump"})
    String verusb3773Mode = "Slow";

    @Value(name = "Verus-Damage-Speed", displayName = "Speed", minimum = 1, maximum = 9)
    double verusDamageSpeed = 1;

    /*Megacraft*/
    @Value(name = "MegaCraft-Speed", displayName = "Speed", minimum = 0.1, maximum = 2)
    double megaCraftSpeed = 1;

    /*AAC 1.9.10*/
    @Value(name = "AAC1.9.10-Speed", displayName = "Speed", minimum = 0, maximum = 2.8D)
    double aac1910speed = 2.34D;
    @Value(name = "AAC1.9.10-MotionY", displayName = "MotionY", minimum = 0, maximum = 1.1D)
    double aac1910motion = 0.8D;

    /*AAC 3.3.12*/
    @Value(name = "AAC3.3.12-Boost", displayName = "Boost", minimum = 1, maximum = 10)
    int aac3312boost = 9;

    /*AAC 4 (Boat)*/
    @Value(name = "AAC4-Boat-Boost", displayName = "Boost", minimum = 1, maximum = 10)
    int aac4BoatBoost = 8;

    /*Intave 12.5.5*/
    @Value(name = "Intave12.5.5-Speed", displayName = "Speed", minimum = 0.1, maximum = 1)
    double intave1300Speed = 0.2;

    /*Intave 13.0.8*/
    @Value(name = "Intave13.0.8-Boost", displayName = "Boost", minimum = 0.6, maximum = 2.5)
    double intave1308Boost = 0.6;

    @Value(name = "Intave13.0.8-MotionY", displayName = "MotionY", minimum = 0, maximum = 1)
    double intave1308MotionY = 0.19D;

    @Value(name = "Intave13.0.8-SneakBoost", displayName = "Sneak-Boost")
    boolean intave1308BoostY = true;

    @Value(name = "Intave13.0.8-BoostMotion", displayName = "Boost-Motion", minimum = 1, maximum = 2)
    double intave1308BoostMotionY = 1.49D;

    /*Spartan406*/
    @Value(name = "Spartan406-Slowness", displayName = "Slowness", minimum = 0.1, maximum = 1)
    double spartan406Slowness = 0.6;

    @Value(name = "Spartan406-Boost", displayName = "Boost", minimum = 0.01, maximum = 1)
    double spartan406Boost = 0.01;

    /*Vanilla*/
    @Value(name = "Vanilla-Speed", displayName = "Speed", minimum = 0.01, maximum = 1)
    double vanillaFlySpeed = 0.05;

    @Value(name = "Vanilla-SpigotFlyKickBypass", displayName = "Spigot Fly Kick Bypass")
    boolean vanillaSpigotFlyKickBypass = false;

    /*Verus b1187*/
    @Value(name = "Verus b1187-Boost", displayName = "Boost")
    boolean verus1187Boost = true;

    @Value(name = "FastStop")
    boolean fastStop = true;

    @Value(name = "Bobbing", visual = true)
    boolean bobbing = true;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        switch (name) {
            case "Verus b3733-Mode":
                return mode.equalsIgnoreCase("Verus b3733");
            case "Verus b1187-Boost":
                return mode.equalsIgnoreCase("Verus b1187");
            case "Verus-Damage-Speed":
                return mode.equalsIgnoreCase("Verus b3733") && verusb3773Mode.equalsIgnoreCase("Damage");
            case "MegaCraft-Speed":
                return mode.equalsIgnoreCase("Megacraft");
            case "AAC1.9.10-Speed":
            case "AAC1.9.10-MotionY":
                return mode.equalsIgnoreCase("AAC1.9.10");
            case "AAC3.3.12-Boost":
                return mode.equalsIgnoreCase("AAC3.3.12");
            case "AAC4-Boat-Boost":
                return mode.equalsIgnoreCase("AAC4 (Boat)");
            case "Intave12.5.5-Speed":
                return mode.equalsIgnoreCase("Intave12.5.5");
            case "Intave13.0.8-Boost":
            case "Intave13.0.8-MotionY":
            case "Intave13.0.8-SneakBoost":
                return mode.equalsIgnoreCase("Intave13.0.8");
            case "Intave13.0.8-BoostMotion":
                return intave1308BoostY;
            case "Spartan406-Slowness":
            case "Spartan406-Boost":
                return mode.equalsIgnoreCase("Spartan406");
            case "Vanilla-SpigotFlyKickBypass":
            case "Vanilla-Speed":
                return mode.equalsIgnoreCase("Vanilla");
        }
        return super.isVisible(value, name);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info(priority = Event.Priority.HIGH)
    public void onEvent(Event event) {
        final MovementUtil movementUtil = MovementUtil.getInstance();
        final RandomUtil randomUtil = RandomUtil.getInstance();

        if (event instanceof Render2DEvent) {
            final RenderUtil renderUtil = RenderUtil.getInstance();
            final Resolution resolution = Resolution.getResolution();
            switch (mode) {
                case "Vanilla":
                    if (vanillaSpigotFlyKickBypass) {
                        final double left = resolution.width / 2F - 10;
                        final double right = resolution.width / 2F + 10;
                        renderUtil.drawRect(left, resolution.height / 2F + 10, right, resolution.height / 2F + 15, Integer.MIN_VALUE);
                        final double percent = vanillaG * 100F / 80F;
                        final double length = Math.abs(left - right);
                        final double width = percent * length / 100;
                        renderUtil.drawRect(resolution.width / 2F - 10, resolution.height / 2F + 10, left + width, resolution.height / 2F + 15, Color.RED.getRGB());
                    }
            }
        }

        if (event instanceof final BobbingEvent bobbingEvent) {
            if (bobbing) {
                bobbingEvent.setChangeBobbing(false);
            }
        }

        if (event instanceof final AnimationEvent animationEvent) {
            animationEvent.setRightLegRotation(1.5F, 0F, 0F);
            animationEvent.setLeftLegRotation(1.5F, 0F, 0F);
            animationEvent.setBodyRotation(1.5F, 0F, 0F);
            animationEvent.setBodyPosition(0, 0.7F, -0.5F);
            animationEvent.setHeadRotation(0.6F, 0F);
            animationEvent.setLeftArmRotation(-1.5F, 0F, 0F);
            animationEvent.setRightArmRotation(-1.5F, 0F, 0F);
            animationEvent.setRightArmPosition(0, 0.7F, -0.5F);
            animationEvent.setLeftArmPosition(0, 0.7F, -0.5F);
            animationEvent.setHeadPosition(0, 0.7F, -0.5F);
        }

        if(event instanceof final UpdateMotionEvent updateMotionEvent) {
            if (updateMotionEvent.getType() == UpdateMotionEvent.Type.MID) {
                if (bobbing) {
                    getPlayer().cameraYaw = 0.1F;
                }
            }
        }

        if (event instanceof VelocityEvent) {
            switch (mode) {
                case "AAC1.9.10":
                    event.setCanceled(true);
                    break;
            }
        }

        if (event instanceof
                final PacketEvent packetEvent) {
            final Packet<?> packet = packetEvent.getPacket();
            if (packetEvent.getType() == PacketEvent.Type.RECEIVE) {
                if (packet instanceof final S39PacketPlayerAbilities playerAbilities) {
                    allowedFly = playerAbilities.isAllowFlying();
                }
            } else {
                switch (mode) {
                    case "Spartan439":
                        if (!spartan439TimeHelper.hasReached(1000, true)) {
                            spartan439Packets.add(packet);
                            packetEvent.setCanceled(true);
                        } else {
                            spartan439Packets.forEach(this::sendPacketUnlogged);
                            spartan439Packets.clear();
                        }
                        break;
                    case "WatchDuck20211030":
                    case "Spartan431":
                        if (packet instanceof final C03PacketPlayer packetPlayer) {
                            packetPlayer.onGround = true;
                        }
                        break;
                    case "Verus b1187":
                        if (packet instanceof final C03PacketPlayer packetPlayer) {
                            if (verus1187Jumps < 4) {
                                packetPlayer.onGround = false;
                            }
                        }
                        break;
                    case "Verus b3733":
                        switch (verusb3773Mode) {
                            case "Jump":
                                if (packet instanceof C03PacketPlayer packetPlayer) {
                                    if (getPlayer().posY <= verusb3733JumpStartY) {
                                        if (!verusb3733JumpSpoofed) {
                                            verusb3733JumpSpoofed = true;
                                            packetPlayer.onGround = true;
                                        }
                                    } else {
                                        verusb3733JumpSpoofed = false;
                                    }
                                }
                                break;
                        }
                        break;
                }
            }
        }

        if (event instanceof
                final NoClipEvent noClipEvent) {
            switch (mode) {
                case "WatchDuck20211030":
                    if (!getPlayer().onGround)
                        noClipEvent.setNoClip(true);
                    break;
                case "Verus b1187":
                    if (verus1187Jumps == 4 && getPlayer().motionY == 0)
                        noClipEvent.setNoClip(true);
                    break;
                case "Intave12.5.5":
                    if (getY() != (int) getY() || getPlayer().motionY > 0)
                        return;
                    getPlayer().onGround = true;
                    noClipEvent.setNoClip(true);
                    getPlayer().motionY = 0;
                    getGameSettings().keyBindJump.pressed = false;
                    break;
                case "Intave13Stairs":
                    for (AxisAlignedBB axisAlignedBB : getWorld().getCollidingBoundingBoxes(getPlayer(), getPlayer().getEntityBoundingBox().expand(0.2, 0, 0.2))) {
                        final BlockPos blockPos = new BlockPos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
                        final Block block = getWorld().getBlockState(blockPos).getBlock();
                        if (block instanceof BlockSlab || block instanceof BlockStairs) {
                            noClipEvent.setNoClip(true);
                        }
                    }
                    break;
                case "Spartan431":
                    noClipEvent.setNoClip(true);
                    break;
            }
        }

        if (event instanceof MoveEntityEvent) {
            switch (mode) {
                case "Spartan406":
                    getPlayer().motionY = 0;
                    setMotion(0);
                    getTimer().timerSpeed = (float) spartan406Slowness;

                    if (isMoving()) {
                        movementUtil.blinkTo(spartan406Boost, getY() + (getGameSettings().keyBindJump.pressed ? 0.005 : getGameSettings().keyBindSneak.pressed ? -0.005 : 0), getPlayer().rotationYaw, true);
                    }
                    sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), -99999, getZ(), true));
                    break;
            }
        }

        if (event instanceof TickEvent) {
            switch (mode) {
                case "Spartan431":
                    getGameSettings().keyBindForward.pressed = true;
                    movementUtil.setSpeed(0.01);
                    getTimer().timerSpeed = 0.3F;
                    if (getPlayer().ticksExisted % 4 == 0)
                        getPlayer().jump();
                    else
                        getPlayer().motionY -= 0.05;
                    break;
            }
        }

        if (event instanceof UpdateEvent) {
            switch (mode) {
                case "Verus b3733":
                    switch (verusb3773Mode) {
                        case "Slow" -> {
                            getPlayer().motionY = 0;
                            if (getGameSettings().keyBindJump.pressed)
                                getPlayer().motionY = 0.2;
                            else if (getGameSettings().keyBindSneak.pressed)
                                getPlayer().motionY = -0.2;
                            sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
                            sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(getX(), getY() - 1.5, getZ()), 1, new ItemStack(Blocks.stone.getItem(getWorld(), new BlockPos(-1, -1, -1))), 0, 0.94f, 0));
                            getPlayer().setSprinting(false);
                            if (isMoving()) {
                                movementUtil.setSpeed(0.34);
                            }
                        }
                        case "Damage" -> {
                            if (getPlayer().hurtTime > 0) {
                                dmg = true;
                            }
                            if (!dmg) {
                                stopWalk();
                            } else {
                                resumeWalk();
                                if (isMoving()) {
                                    if (getPlayer().onGround && getPlayer().hurtTime > 0) {
                                        getPlayer().jump();
                                    } else {
                                        getPlayer().motionY = 0;
                                    }
                                    sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
                                    sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(getX(), getY() - 1.5, getZ()), 1, new ItemStack(Blocks.stone.getItem(getWorld(), new BlockPos(-1, -1, -1))), 0, 0.94f, 0));
                                    getTimer().timerSpeed = RandomUtil.getInstance().getRandomFloat(0.4F, 0.44F);
                                    movementUtil.setSpeed(verusDamageSpeed, getPlayer().rotationYaw, false);
                                }
                            }
                        }
                        case "Jump" -> {
                            getPlayer().setSprinting(true);
                            if (getPlayer().posY <= verusb3733JumpStartY) {
                                getPlayer().jump();
                            }
                            if (isMoving())
                                movementUtil.setSpeed(verusb3733JumpSpoofed ? 0.55 : 0.32);
                        }
                    }
                    break;
                case "MinemoraGlide":
                    //Credits: Schooaasch
                    if (getPlayer().motionY < 0)
                        getPlayer().motionY = -0.00784000015258789;
                    break;
                case "WatchDuck20211030":
                    getPlayer().motionY = 0;
                    if (isMoving())
                        movementUtil.setSpeed(0.4);
                    break;
                case "Megacraft":
                    getPlayer().motionY = -0.001;
                    if (getHurtTime() == 4)
                        megaCraftDamaged = true;
                    if (megaCraftDamaged) {
                        getGameSettings().keyBindForward.pressed = isKeyDown(getGameSettings().keyBindForward.getKeyCode());
                        getGameSettings().keyBindBack.pressed = isKeyDown(getGameSettings().keyBindBack.getKeyCode());
                        getGameSettings().keyBindLeft.pressed = isKeyDown(getGameSettings().keyBindLeft.getKeyCode());
                        getGameSettings().keyBindRight.pressed = isKeyDown(getGameSettings().keyBindRight.getKeyCode());
                        if (isMoving()) {
                            movementUtil.setSpeed(megaCraftSpeed);
                        }
                    } else {
                        setMotion(0);
                        getGameSettings().keyBindForward.pressed = false;
                        getGameSettings().keyBindBack.pressed = false;
                        getGameSettings().keyBindLeft.pressed = false;
                        getGameSettings().keyBindRight.pressed = false;
                    }
                    break;
                case "AGCJump":
                    if (getPlayer().ticksExisted % 2 == 0) {
                        getPlayer().jump();
                    }
                    break;
                case "Intave13Stairs":
                    for (AxisAlignedBB axisAlignedBB : getWorld().getCollidingBoundingBoxes(getPlayer(), getPlayer().getEntityBoundingBox().expand(0.2, 0, 0.2))) {
                        final BlockPos blockPos = new BlockPos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
                        final Block block = getWorld().getBlockState(blockPos).getBlock();
                        if (block instanceof BlockSlab || block instanceof BlockStairs) {
                            getPlayer().onGround = true;
                            getPlayer().motionY = 0;
                        }
                    }
                    break;
                case "Intave13.0.8":
                    getPlayer().motionY = intave1308MotionY / 10;
                    if (getGameSettings().keyBindSneak.pressed && intave1308BoostY) {
                        getPlayer().motionY = intave1308BoostMotionY / 10;
                    }
                    if (getGameSettings().keyBindJump.pressed) {
                        movementUtil.setSpeed(intave1308Boost, getPlayer().rotationYaw);
                    }
                    break;
                case "Intave12.5.5":
                    Keyboard.enableRepeatEvents(true);
                    if (isKeyDown(getGameSettings().keyBindForward.getKeyCode()) || isKeyDown(getGameSettings().keyBindBack.getKeyCode()) || isKeyDown(getGameSettings().keyBindLeft.getKeyCode()) || isKeyDown(getGameSettings().keyBindRight.getKeyCode())) {
                        if (getPlayer().ticksExisted % 2 == 0) {
                            //TODO: make this code better
                            Keyboard.enableRepeatEvents(true);
                            final float left = isKeyDown(getGameSettings().keyBindLeft.getKeyCode()) ? Keyboard.isKeyDown(getGameSettings().keyBindBack.getKeyCode()) ? 45 : Keyboard.isKeyDown(getGameSettings().keyBindForward.getKeyCode()) ? -45 : -90 : 0;
                            final float right = isKeyDown(getGameSettings().keyBindRight.getKeyCode()) ? Keyboard.isKeyDown(getGameSettings().keyBindBack.getKeyCode()) ? -45 : Keyboard.isKeyDown(getGameSettings().keyBindForward.getKeyCode()) ? 45 : 90 : 0;
                            final float back = isKeyDown(getGameSettings().keyBindBack.getKeyCode()) ? +180 : 0;
                            final float yaw = left + right + back;
                            final float direction = getYaw() + yaw;
                            Keyboard.enableRepeatEvents(false);
                            movementUtil.teleportTo(intave1300Speed, getY(), direction);
                        }
                        getGameSettings().keyBindForward.pressed = false;
                        getGameSettings().keyBindLeft.pressed = false;
                        getGameSettings().keyBindRight.pressed = false;
                        getGameSettings().keyBindBack.pressed = false;
                    }

                    Keyboard.enableRepeatEvents(false);
                    break;
                case "Spartan439":
                    if (getPlayer().fallDistance >= 1) {
                        getPlayer().jump();
                        getPlayer().fallDistance = 0;
                    }
                    break;
                case "AirJump":
                    getPlayer().onGround = true;
                    break;
                case "Vanilla":
                    if (getPlayer().onGround)
                        vanillaOnGround = true;

                    getGameSettings().keyBindJump.pressed = isKeyDown(getGameSettings().keyBindJump.getKeyCode());
                    getPlayer().capabilities.isFlying = vanillaOnGround;
                    getPlayer().capabilities.allowFlying = vanillaOnGround;
                    getPlayer().capabilities.setFlySpeed((float) vanillaFlySpeed);
                    if (vanillaSpigotFlyKickBypass)
                        if (getPlayer().posY - getPlayer().lastTickPosY >= -0.3125D && getBlockUnderPlayer(0.1F) == Blocks.air) {
                            vanillaG++;
                            if (vanillaG > 40) {
                                getGameSettings().keyBindJump.pressed = false;
                            }
                            if (vanillaG > 70) {
                                vanillaOnGround = false;
                            }
                            if (vanillaG > 80) {
                                sendMessage(randomInRange(0, 2) == 1 ? "Haram para schmeckt!" : "halal Spigot!");
                                vanillaG = 0;
                            }
                        } else {
                            vanillaG = 0;
                        }
                    break;
                case "Verus b1187":
                    if (verus1187Jumps < 4) {
                        setMotion(0);
                        getGameSettings().keyBindSprint.pressed = false;
                        getGameSettings().keyBindJump.pressed = false;
                        getGameSettings().keyBindForward.pressed = false;
                        getGameSettings().keyBindBack.pressed = false;
                        getGameSettings().keyBindLeft.pressed = false;
                        getGameSettings().keyBindRight.pressed = false;

                        if (getPlayer().onGround) {
                            getPlayer().jump();
                            verus1187Jumps++;
                        }
                    } else if (getPlayer().onGround || verus1187Damaged) {
                        getGameSettings().keyBindSprint.pressed = isKeyDown(getGameSettings().keyBindSprint.getKeyCode());
                        getGameSettings().keyBindForward.pressed = isKeyDown(getGameSettings().keyBindForward.getKeyCode());
                        getGameSettings().keyBindBack.pressed = isKeyDown(getGameSettings().keyBindBack.getKeyCode());
                        getGameSettings().keyBindLeft.pressed = isKeyDown(getGameSettings().keyBindLeft.getKeyCode());
                        getGameSettings().keyBindRight.pressed = isKeyDown(getGameSettings().keyBindRight.getKeyCode());
                        getGameSettings().keyBindJump.pressed = false;

                        getPlayer().motionY = 0;
                        if (getHurtTime() == 6 && verus1187Boost && !verus1187Boosted) {
                            movementUtil.teleportTo(7.2);
                            verus1187Boosted = true;
                        }

                        verus1187Damaged = true;
                    }
                    break;
                case "VerusOnlyMCOld":
                    getTimer().timerSpeed = 0.9F;
                    if (!getPlayer().onGround) {
                        getPlayer().capabilities.isFlying = false;
                        getPlayer().capabilities.isCreativeMode = true;
                        getPlayer().cameraYaw = 0.05F;
                        if (getPlayer().motionY < -0.4) {
                            movementUtil.teleportTo(3);
                            getPlayer().motionY *= -1.01;
                        }

                    }
                    break;
                case "Hypixel20220104":
                    mc.thePlayer.motionY = 0F;
                    if (mc.thePlayer.ticksExisted % 25 == 0) {
                        double yaw = Math.toRadians(getYaw());
                        double x3 = -Math.sin(yaw) * 7;
                        double z3 = Math.cos(yaw) * 7;
                        double y3 = 1.75;
                        mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x3, mc.thePlayer.posY - y3, mc.thePlayer.posZ + z3, false));
                    }
                    break;
                case "Hypixel20201207":
                    if (getPlayer().fallDistance > 3.5) {
                        sendPacket(new C03PacketPlayer(true));
                        getPlayer().onGround = true;
                        getPlayer().fallDistance = 0;
                        getPlayer().motionY = 0.64;
                        if (movementUtil.isMoving())
                            movementUtil.setSpeed(0.45);
                    }
                    break;
                case "AAC1.9.10":
                    if (getPlayer().fallDistance >= 3) {
                        sendPacket(new C03PacketPlayer(true));
                        getPlayer().fallDistance = 0;
                        if (getHurtTime() != 0) {
                            getPlayer().motionY = aac1910motion;
                        }
                    }
                    if (!getPlayer().onGround && (getPlayer().hurtTime != 0 || getPlayer().hurtResistantTime != 0))
                        movementUtil.setSpeed(0.25F * aac1910speed);
                    break;
                case "AAC3.3.12":
                    if (mc.thePlayer.posY <= -70) {
                        mc.thePlayer.motionY = aac3312boost;
                    }
                    break;
                case "AAC4 (Boat)":
                    if (getPlayer().isRiding()) aac4BoatRide = true;
                    else {
                        if (aac4BoatRide) {
                            final Vec3 look = getPlayer().getLookVec();
                            getPlayer().motionZ = look.zCoord * aac4BoatBoost;
                            getPlayer().motionX = look.xCoord * aac4BoatBoost;
                            getPlayer().motionY = 1;
                            aac4BoatRide = false;
                        }
                    }
                    break;
                case "WatchCat2.1.7-b5":
                    if (getPlayer().onGround)
                        getPlayer().jump();
                    else {
                        if (isMoving()) {
                            getPlayer().speedInAir = randomUtil.getRandomFloat(0.09F, 0.1F);
                            getPlayer().jumpMovementFactor = randomUtil.getRandomFloat(0.04F, 0.05F);
                            if (getPlayer().ticksExisted % 10 == 0) {
                                getTimer().timerSpeed = randomUtil.getRandomFloat(20F, 22F);
                                getPlayer().motionY = randomUtil.getRandomFloat(-0.01F, 0F);
                                getPlayer().motionY += randomUtil.getRandomFloat(0.3F, 0.4F);
                                getPlayer().motionY *= randomUtil.getRandomFloat(0.5F, 0.6F);
                            } else {
                                getTimer().timerSpeed = randomUtil.getRandomFloat(0.12F, 0.3F);
                                getPlayer().motionY -= randomUtil.getRandomFloat(0.01F, 0.02F);
                                getPlayer().motionY *= randomUtil.getRandomFloat(0.4F, 0.5F);
                            }
                        }
                    }
                    break;
                case "RedeskyFlag":
                    final double motionX = -Math.sin(Math.toRadians(movementUtil.getDirection(getPlayer().rotationYaw))) * 6;
                    final double motionZ = Math.cos(Math.toRadians(movementUtil.getDirection(getPlayer().rotationYaw))) * 6;

                    final double flagX = -Math.sin(Math.toRadians(movementUtil.getDirection(getPlayer().rotationYaw))) * 7.5;
                    final double flagZ = Math.cos(Math.toRadians(movementUtil.getDirection(getPlayer().rotationYaw))) * 7.5;

                    if (redeSkyFlagTimeHelper.hasReached(190)) {
                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX() + flagX, getY() + randomUtil.getRandomDouble(1.1, 1.2), getZ() + flagZ, true));
                        setPosition(getX() + motionX, getY(), getZ() + motionZ);
                        redeSkyFlagTimeHelper.reset();
                    }
                    break;
                case "Bizzi":
                    if (getGameSettings().keyBindSneak.pressed) {
                        getPlayer().motionY = 5.0;
                    }
                    if (getGameSettings().keyBindForward.pressed) {
                        getPlayer().jump();
                        getTimer().timerSpeed = 0.5f;
                        getPlayer().motionY = -0.5;
                    } else {
                        getTimer().timerSpeed = 0.7f;
                    }
                    break;
                case "Vulcan2.0.1Void":
                    getPlayer().motionY = 0;
                    if (isMoving())
                        movementUtil.setSpeed(getGameSettings().keyBindSprint.pressed ? 4.2 : 1.5);
                    if (getGameSettings().keyBindJump.isKeyDown())
                        getPlayer().motionY = 0.5;
                    if (getGameSettings().keyBindSneak.isKeyDown())
                        getPlayer().motionY = -0.5;
                    break;
                case "Matrix6.6.1Glide":
                    if (!getPlayer().onGround) {
                        getTimer().timerSpeed = 0.06F;
                        if (matrix661GlideTimeHelper.hasReached(matrix661RandomDelay, true)) {
                            getPlayer().motionY = -0.005;
                            matrix661RandomDelay += RandomUtil.getInstance().getRandomGaussian(10);
                            matrix661RandomDelay = Math.max(matrix661RandomDelay, 1000);
                        }
                    }
                    break;
            }
        }

    }


    @Override
    public void onEnable() {
        verusb3733JumpStartY = (int) getY();
        dmg = false;
        allowedFly = getPlayer().capabilities.allowFlying || getPlayer().capabilities.isFlying;
        vanillaOnGround = true;
        verus1187Jumps = verus1187Boost ? 0 : 4;
        verus1187Boosted = false;
        verus1187Damaged = false;
        megaCraftDamaged = false;
        aac4BoatRide = false;
        spartan439Packets.clear();
        spartan439TimeHelper.reset();
        switch (mode) {
            case "VerusOnlyMCOld":
            case "MinemoraGlide":
                if (getPlayer().onGround)
                    getPlayer().jump();
                break;
            case "Verus b3733":
                switch (verusb3773Mode) {
                    case "Damage":
                        if (getPlayer().onGround) {
                            getPlayer().jump();
                        }
                        sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
                        sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(getX(), getY() - 1.5, getZ()), 1, new ItemStack(Blocks.stone.getItem(getWorld(), new BlockPos(-1, -1, -1))), 0, 0.94f, 0));
                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 3.001, getZ(), false));
                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY(), getZ(), false));
                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY(), getZ(), true));
                        break;
                }
                break;
            case "Megacraft":
                if (!getPlayer().onGround) {
                    sendMessage("§cPlease toggle the fly only on ground!");
                    setToggled(false);
                    return;
                }
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 4, getZ(), false));
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY(), getZ(), false));
                sendPacket(new C03PacketPlayer(true));
                break;
        }
    }

    @Override
    public void onDisable() {
        dmg = false;
        matrix661RandomDelay = 1000;
        matrix661RandomDelay += RandomUtil.getInstance().getRandomGaussian(10);
        matrix661RandomDelay = Math.max(matrix661RandomDelay, 1000);

        getPlayer().capabilities.setFlySpeed(0.05F);
        getPlayer().speedInAir = 0.02F;
        getTimer().timerSpeed = 1.0F;
        if (fastStop) {
            getPlayer().motionX = 0;
            getPlayer().motionZ = 0;
        }

        if (mc.currentScreen == null)
            resumeWalk();
        getPlayer().capabilities.allowFlying = allowedFly;
        if (!allowedFly) {
            getPlayer().capabilities.isFlying = false;
        }
    }

}
