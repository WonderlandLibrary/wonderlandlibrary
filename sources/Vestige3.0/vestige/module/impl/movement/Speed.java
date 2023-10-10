package vestige.module.impl.movement;

import lombok.Getter;
import net.minecraft.block.*;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import vestige.event.Listener;
import vestige.event.impl.*;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.DoubleSetting;
import vestige.setting.impl.IntegerSetting;
import vestige.setting.impl.ModeSetting;
import vestige.util.network.PacketUtil;
import vestige.util.player.MovementUtil;
import vestige.util.player.PlayerUtil;
import vestige.util.player.RotationsUtil;
import vestige.util.render.RenderUtil;
import vestige.util.world.BlockInfo;
import vestige.util.world.WorldUtil;

import java.util.ArrayList;

public class Speed extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "NCP", "Watchdog", "Blocksmc", "MMC", "Strafe", "Fake strafe");

    private final DoubleSetting vanillaSpeed = new DoubleSetting("Vanilla speed", () -> mode.is("Vanilla"), 1, 0.2, 9, 0.1);
    private final BooleanSetting autoJump = new BooleanSetting("Autojump", () -> mode.is("Vanilla") || mode.is("Strafe"), true);

    private final ModeSetting ncpMode = new ModeSetting("NCP Mode", () -> mode.is("NCP"), "Hop", "Hop", "Updated Hop");
    private final BooleanSetting damageBoost = new BooleanSetting("Damage Boost", () -> mode.is("NCP") && ncpMode.is("Updated Hop"), true);

    public final ModeSetting watchdogMode = new ModeSetting("Watchdog Mode", () -> mode.is("Watchdog"), "Strafe", "Strafe", "Semi-Strafe", "Strafeless", "Ground");

    private final BooleanSetting fast = new BooleanSetting("Fast", () -> mode.is("Watchdog") && (watchdogMode.is("Strafe") || watchdogMode.is("Strafeless")), true);

    private final DoubleSetting attributeSpeedOffground = new DoubleSetting("Attribute speed offground", () -> mode.is("Watchdog") && watchdogMode.is("Strafe"), 0.023, 0.02, 0.026, 0.001);

    private final DoubleSetting mult = new DoubleSetting("Mult", () -> mode.is("Watchdog") && watchdogMode.is("Strafeless") && fast.isEnabled(), 1.24, 1, 1.3, 0.005);
    private final DoubleSetting speedPotMult = new DoubleSetting("Speed pot mult", () -> mode.is("Watchdog") && watchdogMode.is("Strafeless") && fast.isEnabled(), 1.24, 1, 1.3, 0.005);

    private final BooleanSetting fullScaffold = new BooleanSetting("Full scaffold", () -> mode.is("MMC"), false);

    private final BooleanSetting allDirSprint = new BooleanSetting("All directions sprint", () -> mode.is("Strafe"), true);
    private final IntegerSetting minHurtTime = new IntegerSetting("Min hurttime", () -> mode.is("Strafe"), 10, 0, 10, 1);

    private final BooleanSetting sprint = new BooleanSetting("Sprint", () -> mode.is("Fake strafe"), true);
    private final BooleanSetting rotate = new BooleanSetting("Rotate", () -> mode.is("Fake strafe"), false);
    private final BooleanSetting groundStrafe = new BooleanSetting("Ground Strafe", () -> mode.is("Fake strafe"), false);
    private final ModeSetting velocityMode = new ModeSetting("Velocity handling", () -> mode.is("Fake strafe"), "Ignore", "Ignore", "Vertical", "Legit");
    private final ModeSetting clientSpeed = new ModeSetting("Client speed", () -> mode.is("Fake strafe"), "Normal", "Normal", "Custom");
    private final DoubleSetting customClientSpeed = new DoubleSetting("Custom client speed", () -> mode.is("Fake strafe") && clientSpeed.is("Custom"), 0.5, 0.15, 1, 0.025);
    private final BooleanSetting fakeFly = new BooleanSetting("Fake fly", () -> mode.is("Fake strafe"), false);
    private final BooleanSetting renderRealPosBox = new BooleanSetting("Render box at real pos", () -> mode.is("Fake strafe"), true);

    private final ModeSetting timerMode = new ModeSetting("Timer mode", () -> mode.is("NCP"), "None", "None", "Bypass", "Custom");
    private final DoubleSetting customTimer = new DoubleSetting("Custom timer", () -> (mode.is("NCP") && timerMode.is("Custom")) || mode.is("Watchdog"), 1, 0.1, 3, 0.05);

    private double speed;

    private boolean prevOnGround;

    private int counter, ticks, offGroundTicks, ticksSinceVelocity;

    private boolean takingVelocity, wasTakingVelocity;
    private double velocityX, velocityY, velocityZ;
    private double velocityDist;

    private float lastDirection;

    private float lastYaw;

    private double motionX, motionY, motionZ;

    @Getter
    private double actualX, actualY, actualZ, lastActualX, lastActualY, lastActualZ;

    private boolean actualGround;

    private boolean started, firstJumpDone;

    private boolean wasCollided;

    private int oldSlot;

    private final ArrayList<BlockPos> barriers = new ArrayList<>();

    private float lastForward, lastStrafe;

    public Speed() {
        super("Speed", Category.MOVEMENT);
        this.addSettings(mode, vanillaSpeed, autoJump, ncpMode, damageBoost, watchdogMode, fast, mult, speedPotMult, attributeSpeedOffground, fullScaffold, allDirSprint, minHurtTime, sprint, rotate, groundStrafe, velocityMode, clientSpeed, customClientSpeed, fakeFly, renderRealPosBox, timerMode, customTimer);
    }

    @Override
    public void onEnable() {
        prevOnGround = false;
        speed = 0.28;

        ticks = offGroundTicks = counter = 0;

        ticksSinceVelocity = Integer.MAX_VALUE;

        started = firstJumpDone = false;

        takingVelocity = wasTakingVelocity = false;

        motionX = mc.thePlayer.motionX;
        motionY = mc.thePlayer.motionY;
        motionZ = mc.thePlayer.motionZ;

        actualX = mc.thePlayer.posX;
        actualY = mc.thePlayer.posY;
        actualZ = mc.thePlayer.posZ;

        actualGround = mc.thePlayer.onGround;

        lastDirection = MovementUtil.getPlayerDirection();

        lastYaw = mc.thePlayer.rotationYaw;

        lastForward = mc.thePlayer.moveForward;
        lastStrafe = mc.thePlayer.moveStrafing;

        oldSlot = mc.thePlayer.inventory.currentItem;

        wasCollided = false;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;

        switch (mode.getMode()) {
            case "Vulcan":
                mc.thePlayer.inventory.currentItem = oldSlot;
                break;
            case "Watchdog":
                if(watchdogMode.is("Strafe")) {
                    mc.thePlayer.motionX *= 0.2;
                    mc.thePlayer.motionZ *= 0.2;
                }
                break;
            case "Fake strafe":
                mc.thePlayer.setPosition(actualX, actualY, actualZ);
                mc.thePlayer.motionX = motionX;
                mc.thePlayer.motionY = motionY;
                mc.thePlayer.motionZ = motionZ;

                mc.thePlayer.onGround = actualGround;
                break;
        }

        if(!barriers.isEmpty()) {
            for(BlockPos pos : barriers) {
                mc.theWorld.setBlockToAir(pos);
            }

            barriers.clear();
        }
    }

    @Listener
    public void onStrafe(StrafeEvent event) {
        switch (mode.getMode()) {
            case "Watchdog":
                if(watchdogMode.is("Test")) {
                    if(!mc.thePlayer.isSprinting()) {
                        event.setAttributeSpeed(event.getAttributeSpeed() * 1.3F);
                    }

                    if(mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.thePlayer.jump();
                    }
                }
                break;
            case "Strafe":
                if(allDirSprint.isEnabled()) {
                    if(!mc.thePlayer.isSprinting()) {
                        event.setAttributeSpeed(event.getAttributeSpeed() * 1.3F);
                    }
                }

                if(autoJump.isEnabled() && mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.jump();
                }
                break;
        }
    }

    @Listener
    public void onJump(JumpEvent event) {
        switch (mode.getMode()) {
            case "Strafe":
                if(allDirSprint.isEnabled()) {
                    event.setBoosting(MovementUtil.isMoving());
                    event.setYaw(MovementUtil.getPlayerDirection());
                }
                break;
            case "Watchdog":
                if(watchdogMode.is("Test")) {
                    event.setBoosting(MovementUtil.isMoving());
                    event.setYaw(MovementUtil.getPlayerDirection());
                }
                break;
            case "Test":
            case "Test2":
                event.setBoosting(MovementUtil.isMoving());
                event.setYaw(MovementUtil.getPlayerDirection());
                break;
        }
    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        switch (mode.getMode()) {
            case "Vulcan":
                for(int i = 8; i >= 0; i--) {
                    ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);

                    if(stack != null && stack.getItem() instanceof ItemBlock && !PlayerUtil.isBlockBlacklisted(stack.getItem())) {
                        mc.thePlayer.inventory.currentItem = i;
                        break;
                    }
                }

                if(mc.thePlayer.onGround) {
                    if(MovementUtil.isMoving()) {
                        mc.thePlayer.jump();
                        ticks = 0;
                    }
                } else {
                    if (ticks == 4) {
                        if(started) {
                            mc.thePlayer.motionY = -1;
                        }

                        double x = mc.thePlayer.motionX > 0 ? 1.5 : -1.5;
                        double z = mc.thePlayer.motionZ > 0 ? 1.5 : -1.5;

                        mc.playerController.syncCurrentPlayItem();
                        PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY - 2, mc.thePlayer.posZ + z), EnumFacing.UP.getIndex(), mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem), 0.5F, 1, 0.5F));
                        mc.thePlayer.swingItem();

                        started = true;
                    }

                    ticks++;
                }
                break;
            case "MMC":
                if(!started || fullScaffold.isEnabled()) break;
            case "Test":
                if(mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionY = 0;
                }
                break;
        }
    }

    @Listener
    public void onMove(MoveEvent event) {
        if(!takingVelocity && mc.thePlayer.onGround) {
            wasTakingVelocity = false;
        }

        double velocityExtra = 0.28 + MovementUtil.getSpeedAmplifier() * 0.07;

        float direction = MathHelper.wrapAngleTo180_float(MovementUtil.getPlayerDirection());

        float forward = mc.thePlayer.moveForward;
        float strafe = mc.thePlayer.moveStrafing;

        switch (mode.getMode()) {
            case "Vanilla":
                if(mc.thePlayer.onGround && MovementUtil.isMoving() && autoJump.isEnabled()) {
                    event.setY(mc.thePlayer.motionY = (double) mc.thePlayer.getJumpUpwardsMotion());
                }

                MovementUtil.strafe(event, vanillaSpeed.getValue());
                break;
            case "NCP":
                switch (ncpMode.getMode()) {
                    case "Hop":
                        if(mc.thePlayer.onGround) {
                            prevOnGround = true;

                            if(MovementUtil.isMoving()) {
                                event.setY(mc.thePlayer.motionY = (double) mc.thePlayer.getJumpUpwardsMotion());

                                speed *= 0.91;
                                speed += (ticks >= 8 ? 0.2 : 0.15) + mc.thePlayer.getAIMoveSpeed();

                                ticks = 0;
                            }
                        } else if(prevOnGround) {
                            speed *= 0.58;
                            speed += 0.026;

                            prevOnGround = false;
                        } else {
                            speed *= 0.91;
                            speed += 0.026;

                            ticks++;
                        }

                        if(speed > 0.2) {
                            speed -= 1E-6;
                        }
                        break;
                    case "Updated Hop":
                        if(mc.thePlayer.onGround) {
                            prevOnGround = true;

                            if(MovementUtil.isMoving()) {
                                MovementUtil.jump(event);

                                speed *= 0.91;

                                if(takingVelocity && damageBoost.isEnabled()) {
                                    speed = velocityDist + velocityExtra;
                                }

                                speed += 0.2 + mc.thePlayer.getAIMoveSpeed();
                            }
                        } else if(prevOnGround) {
                            speed *= 0.53;

                            if(takingVelocity && damageBoost.isEnabled()) {
                                speed = velocityDist + velocityExtra;
                            }

                            speed += 0.026F;

                            prevOnGround = false;
                        } else {
                            speed *= 0.91;

                            if(takingVelocity && damageBoost.isEnabled()) {
                                speed = velocityDist + velocityExtra;
                            }

                            speed += 0.026F;
                        }
                        break;
                }

                switch (timerMode.getMode()) {
                    case "None":
                        mc.timer.timerSpeed = 1F;
                        break;
                    case "Bypass":
                        mc.timer.timerSpeed = 1.08F;
                        break;
                    case "Custom":
                        mc.timer.timerSpeed = (float) customTimer.getValue();
                        break;
                }

                MovementUtil.strafe(event, speed);
                break;
            case "Watchdog":
                switch (watchdogMode.getMode()) {
                    case "Strafe":
                        if(mc.thePlayer.onGround) {
                            if(MovementUtil.isMoving()) {
                                prevOnGround = true;

                                MovementUtil.jump(event);

                                speed = 0.585 + MovementUtil.getSpeedAmplifier() * 0.065;
                            }
                        } else if(prevOnGround) {
                            if(ticks++ % 5 > 0 && fast.isEnabled()) {
                                speed *= 0.65F;
                            } else {
                                speed *= 0.53F;
                            }
                            prevOnGround = false;
                        } else {
                            speed = Math.min(speed, 0.35 + MovementUtil.getSpeedAmplifier() * 0.02);

                            speed *= 0.91F;

                            speed += (float) (attributeSpeedOffground.getValue()) * 0.98F;
                        }

                        MovementUtil.strafe(event, speed);
                        break;
                    case "Semi-Strafe":
                        if(mc.thePlayer.onGround) {
                            prevOnGround = true;

                            if(MovementUtil.isMoving()) {
                                MovementUtil.jump(event);

                                speed = 0.6 + MovementUtil.getSpeedAmplifier() * 0.075;
                            }
                        } else if(prevOnGround) {
                            speed *= 0.54F;
                            prevOnGround = false;
                        } else {
                            speed *= 0.91F;

                            speed += (mc.thePlayer.isSprinting() ? 0.026F : 0.02F) * 0.98F;
                        }

                        direction = MovementUtil.getPlayerDirection();

                        if(!mc.thePlayer.onGround) {
                            float dirChange = Math.abs(direction - lastDirection);

                            if(dirChange > 180) {
                                dirChange = 360 - dirChange;
                            }

                            double reduceMult = 1 - dirChange * 0.01;

                            speed *= reduceMult;

                            speed = Math.max(speed, 0.09);
                        }

                        if(mc.thePlayer.isCollidedHorizontally) {
                            speed = 0.09;
                        }

                        MovementUtil.strafe(event, speed);

                        lastDirection = direction;
                        break;
                    case "Strafeless":
                        if(MovementUtil.isMoving()) {
                            if(mc.thePlayer.onGround) {
                                prevOnGround = true;

                                MovementUtil.jump(event);

                                if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                                    MovementUtil.strafeNoTargetStrafe(event, 0.59 - Math.random() * 0.001 + MovementUtil.getSpeedAmplifier() * 0.08);
                                } else {
                                    MovementUtil.strafeNoTargetStrafe(event, 0.6 - Math.random() * 0.001);
                                }
                            } else {
                                if(prevOnGround) {
                                    if(mc.thePlayer.isSprinting()) {
                                        if(++counter > 1 && fast.isEnabled()) {
                                            if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                                                event.setX(event.getX() * speedPotMult.getValue());
                                                event.setZ(event.getZ() * speedPotMult.getValue());
                                            } else {
                                                event.setX(event.getX() * mult.getValue());
                                                event.setZ(event.getZ() * mult.getValue());
                                            }
                                        }
                                    }

                                    prevOnGround = false;
                                }
                            }
                        }

                        lastForward = forward;
                        lastStrafe = strafe;
                        break;
                    case "Ground":
                        if(mc.thePlayer.onGround) {
                            ticks = 0;

                            if(!started) {
                                MovementUtil.jump(event);
                                MovementUtil.strafe(event, 0.55 + MovementUtil.getSpeedAmplifier() * 0.07);
                                started = true;
                            } else {
                                event.setY(mc.thePlayer.motionY = 0.0005);
                                firstJumpDone = true;

                                speed = 0.335 + MovementUtil.getSpeedAmplifier() * 0.045F;
                            }
                        } else {
                            ticks++;

                            if(speed > 0.28) {
                                speed *= 0.995;
                            }
                        }

                        if(firstJumpDone && ticks <= 2) {
                            MovementUtil.strafe(event, speed);
                        }
                        break;
                }

                mc.timer.timerSpeed = (float) customTimer.getValue();
                break;
            case "Blocksmc":
                if(mc.thePlayer.onGround) {
                    prevOnGround = true;

                    if(MovementUtil.isMoving()) {
                        MovementUtil.jump(event);

                        speed = 0.57 + MovementUtil.getSpeedAmplifier() * 0.065;

                        if(takingVelocity && damageBoost.isEnabled()) {
                            speed = velocityDist + velocityExtra;
                        }

                        ticks = 1;
                    }
                } else if(prevOnGround) {
                    speed *= 0.53;

                    if(takingVelocity && damageBoost.isEnabled()) {
                        speed = velocityDist + velocityExtra;
                    }

                    speed += 0.026F;

                    prevOnGround = false;
                } else {
                    speed *= 0.91;

                    if(takingVelocity && damageBoost.isEnabled()) {
                        speed = velocityDist + velocityExtra;
                    }

                    speed += 0.026F;
                }

                if(takingVelocity) {
                    ticks = -7;
                }

                if(++ticks == 0 && !mc.thePlayer.onGround) {
                    speed = 0.28 + MovementUtil.getSpeedAmplifier() * 0.065;
                }

                MovementUtil.strafe(event, speed);
                break;
            case "Strafe":
                if(mc.thePlayer.hurtTime <= minHurtTime.getValue()) {
                    MovementUtil.strafe(event);
                }
                break;
            case "MMC":
                if(started) {
                    BlockInfo blockOver = WorldUtil.getBlockInfo(mc.thePlayer.posX, mc.thePlayer.posY + 2, mc.thePlayer.posZ, 2);
                    BlockInfo blockUnder = WorldUtil.getBlockUnder(mc.thePlayer.posY, 2);

                    counter++;

                    if(fullScaffold.isEnabled()) {
                        if(counter % 14 >= 12) {
                            MovementUtil.strafe(event, 0.04);
                        } else {
                            MovementUtil.strafe(event, 0.495);
                        }

                        event.setY(mc.thePlayer.motionY = 0);

                        if(counter % 2 == 0) {
                            if(blockOver != null) {
                                if(mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), blockOver.getPos(), blockOver.getFacing(), WorldUtil.getVec3(blockOver.getPos(), blockOver.getFacing(), false))) {
                                    PacketUtil.sendPacket(new C0APacketAnimation());
                                }
                            }
                        } else {
                            if(blockUnder != null) {
                                if(mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), blockUnder.getPos(), blockUnder.getFacing(), WorldUtil.getVec3(blockUnder.getPos(), blockUnder.getFacing(), false))) {
                                    PacketUtil.sendPacket(new C0APacketAnimation());
                                }
                            }
                        }
                    } else {
                        if(blockOver != null) {
                            if(mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), blockOver.getPos(), blockOver.getFacing(), WorldUtil.getVec3(blockOver.getPos(), blockOver.getFacing(), false))) {
                                PacketUtil.sendPacket(new C0APacketAnimation());
                            }
                        }
                    }
                } else {
                    float yaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);

                    double x = 0;
                    double z = 0;

                    EnumFacing facing = EnumFacing.UP;

                    if(yaw > 135 || yaw < -135) {
                        z = 1;
                        facing = EnumFacing.NORTH;
                    } else if(yaw > -135 && yaw < -45) {
                        x = -1;
                        facing = EnumFacing.EAST;
                    } else if(yaw > -45 && yaw < 45) {
                        z = -1;
                        facing = EnumFacing.SOUTH;
                    } else if(yaw > 45 && yaw < 135) {
                        x = 1;
                        facing = EnumFacing.WEST;
                    }

                    BlockPos pos;

                    switch (++counter) {
                        case 1:
                            pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY - 1, mc.thePlayer.posZ + z);

                            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, EnumFacing.UP, WorldUtil.getVec3(pos, EnumFacing.DOWN, true));
                            break;
                        case 2:
                            pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);

                            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, EnumFacing.UP, WorldUtil.getVec3(pos, EnumFacing.DOWN, true));
                            break;
                        case 3:
                            pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + 1, mc.thePlayer.posZ + z);

                            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, EnumFacing.UP, WorldUtil.getVec3(pos, EnumFacing.DOWN, true));
                            break;
                        case 5:
                            pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + 2, mc.thePlayer.posZ + z);

                            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, facing, WorldUtil.getVec3(pos, facing, true));

                            started = true;
                            counter = 0;
                            break;
                    }

                    PacketUtil.sendPacket(new C0APacketAnimation());

                    MovementUtil.strafe(event, 0.04);
                }
                break;
            case "Fake strafe":
                double distance = Math.hypot(mc.thePlayer.posX - actualX, mc.thePlayer.posZ - actualZ);

                if(fakeFly.isEnabled()) {
                    if(mc.gameSettings.keyBindJump.isKeyDown()) {
                        event.setY(0.35);
                    } else if(mc.gameSettings.keyBindSneak.isKeyDown()) {
                        event.setY(-0.35);
                    } else {
                        event.setY(0);
                    }

                    mc.thePlayer.motionY = 0;
                } else {
                    if(mc.thePlayer.onGround && MovementUtil.isMoving()) {
                        MovementUtil.jump(event);
                    }
                }

                if(!started) {
                    speed = 0.65;
                    started = true;
                } else {
                    if(clientSpeed.is("Normal")) {
                        double baseSpeed = 0.33 + MovementUtil.getSpeedAmplifier() * 0.02;

                        if(mc.thePlayer.onGround) {
                            speed = 0.33 + baseSpeed;
                        } else {
                            speed = Math.min(speed - baseSpeed * distance * 0.15, baseSpeed);
                        }

                        speed = Math.max(speed, 0.2);
                    } else if(clientSpeed.is("Custom")) {
                        //speed = Math.max(customClientSpeed.getValue() - distance * customClientSpeed.getValue() * 0.15, 0.3);
                        speed = customClientSpeed.getValue();
                    }
                }

                MovementUtil.strafe(event, speed);

                lastDirection = direction;
                break;
        }
    }

    @Listener
    public void onEntityAction(EntityActionEvent event) {
        switch (mode.getMode()) {
            case "Fake strafe":
                lastActualX = actualX;
                lastActualY = actualY;
                lastActualZ = actualZ;

                float direction = RotationsUtil.getRotationsToPosition(lastActualX, lastActualY, lastActualZ, mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)[0];

                float gcd = RotationsUtil.getGCD();

                float yawDiff = (direction - lastYaw);

                float fixedYawDiff = yawDiff - (yawDiff % gcd);

                direction = lastYaw + fixedYawDiff;

                float dir = direction * 0.017453292F;

                float friction = getFriction(actualX, actualY, actualZ) * 0.91F;

                if(actualGround) {
                    motionY = (double) mc.thePlayer.getJumpUpwardsMotion();

                    if(mc.thePlayer.isPotionActive(Potion.jump)) {
                        motionY += (double) ((float) (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                    }

                    if(!wasCollided) {
                        motionX -= (double)(MathHelper.sin(dir) * 0.2F);
                        motionZ += (double)(MathHelper.cos(dir) * 0.2F);
                    }
                }

                float aa = 0.16277136F / (friction * friction * friction);

                float attributeSpeed;

                mc.thePlayer.setSprinting(!wasCollided);

                if (actualGround)
                {
                    attributeSpeed = mc.thePlayer.getAIMoveSpeed() * aa;
                }
                else
                {
                    attributeSpeed = wasCollided ? 0.02F : 0.026F;
                }

                boolean oldActualGround = actualGround;

                float forward = 0.98F;
                float strafe = 0F;

                float thing = strafe * strafe + forward * forward;

                if (thing >= 1.0E-4F)
                {
                    thing = MathHelper.sqrt_float(thing);

                    if (thing < 1.0F)
                    {
                        thing = 1.0F;
                    }

                    thing = attributeSpeed / thing;
                    strafe = strafe * thing;
                    forward = forward * thing;
                    float f1 = MathHelper.sin(direction * (float)Math.PI / 180.0F);
                    float f2 = MathHelper.cos(direction * (float)Math.PI / 180.0F);
                    motionX += (double)(strafe * f2 - forward * f1);
                    motionZ += (double)(forward * f2 + strafe * f1);
                }

                if(groundStrafe.isEnabled() && actualGround) {
                    double speed = Math.hypot(motionX, motionZ);

                    motionX = -Math.sin(Math.toRadians(direction)) * speed;
                    motionZ = Math.cos(Math.toRadians(direction)) * speed;
                }

                double clientX = mc.thePlayer.posX;
                double clientY = mc.thePlayer.posY;
                double clientZ = mc.thePlayer.posZ;

                double clientMotionX = mc.thePlayer.motionX;
                double clientMotionY = mc.thePlayer.motionY;
                double clientMotionZ = mc.thePlayer.motionZ;

                boolean clientGround = mc.thePlayer.onGround;

                mc.thePlayer.setPosition(actualX, actualY, actualZ);

                mc.thePlayer.onGround = actualGround;

                mc.thePlayer.moveEntityNoEvent(motionX, motionY, motionZ);

                boolean collided = mc.thePlayer.isCollidedHorizontally;

                motionX = mc.thePlayer.posX - lastActualX;
                motionY = mc.thePlayer.posY - lastActualY;
                motionZ = mc.thePlayer.posZ - lastActualZ;

                actualX = mc.thePlayer.posX;
                actualY = mc.thePlayer.posY;
                actualZ = mc.thePlayer.posZ;

                actualGround = mc.thePlayer.onGround;

                mc.thePlayer.setPosition(clientX, clientY, clientZ);
                mc.thePlayer.onGround = clientGround;

                mc.thePlayer.motionX = clientMotionX;
                mc.thePlayer.motionY = clientMotionY;
                mc.thePlayer.motionZ = clientMotionZ;

                if(oldActualGround) {
                    motionX *= friction * 0.91F;
                    motionZ *= friction * 0.91F;
                } else {
                    motionX *= 0.91F;
                    motionZ *= 0.91F;
                }

                motionY -= 0.08;
                this.motionY *= 0.9800000190734863D;

                if(Math.abs(motionX) < 0.005) {
                    motionX = 0;
                }

                if(Math.abs(motionY) < 0.005) {
                    motionY = 0;
                }

                if(Math.abs(motionZ) < 0.005) {
                    motionZ = 0;
                }

                if(sprint.isEnabled()) {
                    event.setSprinting(!wasCollided);
                } else {
                    event.setSprinting(false);
                }

                mc.thePlayer.setSprinting(true);

                event.setSneaking(false);

                wasCollided = collided;
                break;
            case "Test":
            case "Test2":
                event.setSprinting(MovementUtil.isMoving());
                break;
        }
    }

    @Listener
    public void onMotion(MotionEvent event) {
        switch (mode.getMode()) {
            case "Fake strafe":
                event.setX(actualX);
                event.setY(actualY);
                event.setZ(actualZ);
                event.setOnGround(actualGround);

                float direction = RotationsUtil.getRotationsToPosition(lastActualX, lastActualY, lastActualZ, mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)[0];

                if(rotate.isEnabled()) {
                    final float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
                    final float gcd = f * f * f * 1.2F;

                    final float deltaYaw = direction - lastYaw;

                    final float fixedDeltaYaw = deltaYaw - (deltaYaw % gcd);

                    direction = lastYaw + fixedDeltaYaw;

                    lastYaw = direction;

                    event.setYaw(direction);
                }
                break;
            case "MMC":
                event.setYaw(event.getYaw() - 180);

                if(started) {
                    event.setPitch(counter % 2 == 0 || !fullScaffold.isEnabled() ? -82 : 82);
                }
                break;
        }

        takingVelocity = false;

        ticksSinceVelocity++;
    }

    @Listener
    public void onRender3D(Render3DEvent event) {
        switch (mode.getMode()) {
            case "Fake strafe":
                if(renderRealPosBox.isEnabled() && mc.gameSettings.thirdPersonView > 0) {
                    RenderUtil.prepareBoxRender(3.25F, 1F, 1F, 1F, 0.8F);

                    RenderUtil.renderCustomPlayerBox(mc.getRenderManager(), event.getPartialTicks(), actualX, actualY, actualZ, lastActualX, lastActualY, lastActualZ);

                    RenderUtil.stopBoxRender();
                }
                break;
        }
    }

    @Listener
    public void onReceive(PacketReceiveEvent event) {
        if(event.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = event.getPacket();

            if(mc.thePlayer.getEntityId() == packet.getEntityID()) {
                takingVelocity = wasTakingVelocity = true;

                velocityX = packet.getMotionX() / 8000.0D;
                velocityY = packet.getMotionY() / 8000.0D;
                velocityZ = packet.getMotionZ() / 8000.0D;

                velocityDist = Math.hypot(velocityX, velocityZ);

                ticksSinceVelocity = 0;

                if(mode.is("Fake strafe")) {
                    event.setCancelled(true);

                    switch (velocityMode.getMode()) {
                        case "Vertical":
                            motionY = velocityY;
                            break;
                        case "Legit":
                            motionX = velocityX;
                            motionY = velocityY;
                            motionZ = velocityZ;
                            break;
                    }
                }
            }
        } else if(event.getPacket() instanceof S08PacketPlayerPosLook) {
            if(mode.is("Fake strafe")) {
                this.setEnabled(false);
            }
        }
    }

    private float getFriction(double x, double y, double z) {
        Block block = mc.theWorld.getBlockState(new BlockPos(x, Math.floor(y) - 1, z)).getBlock();

        if(block != null) {
            if(block instanceof BlockIce || block instanceof BlockPackedIce) {
                return 0.98F;
            } else if(block instanceof BlockSlime) {
                return 0.8F;
            }
        }

        return 0.6F;
    }

    @Override
    public String getSuffix() {
        if(mode.is("Watchdog")) {
            return mode.getMode() + " (" + watchdogMode.getMode() + ")";
        }

        return mode.getMode();
    }

}
