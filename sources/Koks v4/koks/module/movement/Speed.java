package koks.module.movement;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.utils.InventoryUtil;
import koks.api.utils.MovementUtil;
import koks.api.utils.RandomUtil;
import koks.api.utils.TimeHelper;
import koks.event.*;
import koks.module.combat.KillAura;
import koks.event.DirectionSprintCheckEvent;
import net.minecraft.block.BlockCarpet;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Speed", category = Module.Category.MOVEMENT, description = "")
public class Speed extends Module implements Module.Unsafe {

    @Value(name = "Mode", modes = {"Timer", "Hypixel20201207", "Hypixel20210518", "Intave13", "Intave14.0.0", "Intave14.0.3", "Vulcan2.0.1", "Intave13Carpet", "Intave13.0.9MiniHop", "MCCentral", "Mineplex", "Mineplex FAST", "Mineplex SuperFAST", "Mineplex LowHop", "Mineplex Ground", "AAC4", "AAC3.3.12", "Tired", "Legit", "CubeCraft", "Spartan422.1 Ground", "Spartan441", "NCP3.16.0-YPort", "Verus b3733 Float"})
    String mode = "Legit";

    @Value(name = "Timer-Speed", displayName = "Speed", minimum = 0.1, maximum = 100)
    double timerSpeed = 1;

    @Value(name = "Legit-Strafing", displayName = "Strafing")
    boolean legitStrafing = false;

    @Value(name = "Timer-Randomizing", displayName = "Randomizing")
    boolean timerRandomizing = false;

    @Value(name = "Timer-RandomizeRange", minimum = 0.1, maximum = 30)
    double timerRandomizeRange = 1;

    @Value(name = "AAC4-BoostSpeed", displayName = "Boost", minimum = 1, maximum = 100)
    double aac4BoostSpeed = 50D;

    @Value(name = "AAC4-SlowingSpeed", displayName = "Slowness", minimum = 0.1D, maximum = 1D)
    double aac4SlowingSpeed = 0.1D;

    @Value(name = "AAC4-Steps", displayName = "Steps", minimum = 1, maximum = 100)
    int aac4Steps = 3;

    public float mineplexMotion, mineplexSpeed;
    public double verusB3733Speed;

    public int curSlot, aacSpeed, hypixelTime, verusB3733Stage;

    boolean wasCarpet, verusB3733SpoofGround, verusB3733FirstHop, verusB3733IsGround;

    public TimeHelper timeHelper = new TimeHelper();

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        return switch (name) {
            case "Legit-Strafing" -> mode.equalsIgnoreCase("Legit");
            case "Timer-Speed", "Timer-Randomizing" -> mode.equalsIgnoreCase("Timer");
            case "Timer-RandomizeRange" -> timerRandomizing;
            case "AAC4-BoostSpeed", "AAC4-SlowingSpeed", "AAC4-Steps" -> mode.equalsIgnoreCase("AAC4");
            default -> super.isVisible(value, name);
        };
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info(priority = Event.Priority.HIGH)
    public void onEvent(Event event) {
        final MovementUtil movementUtil = MovementUtil.getInstance();
        final RandomUtil randomUtil = RandomUtil.getInstance();
        final InventoryUtil inventoryUtil = InventoryUtil.getInstance();

        if (event instanceof final MoveWithHeadingEvent moveWithHeadingEvent) {
            switch (mode) {
                case "Hypixel20201207" ->
                        //STANDART 0.91F
                        moveWithHeadingEvent.setF4(0.932f);
                case "Intave13" -> moveWithHeadingEvent.setF4(0.91f);
            }
        }


        if (event instanceof final JumpEvent jumpEvent) {
            switch (mode) {
                case "Intave13":
                    jumpEvent.setHeight(0.3F);
                    jumpEvent.setBoost(0.2F);
                    break;
                case "Legit":
                    if (legitStrafing) {
                        jumpEvent.setYaw(movementUtil.getDirection(getPlayer().rotationYaw));
                    }
                    break;
            }
        }

        if (event instanceof final DirectionSprintCheckEvent sprintCheckEvent) {
            switch (mode) {
                case "Legit":
                    if (legitStrafing)
                        sprintCheckEvent.setSprintCheck(false);
                    break;
            }
        }

        if (event instanceof final PacketEvent packetEvent) {
            final Packet<?> packet = packetEvent.getPacket();
            switch (mode) {
                case "Verus b3733 Float":
                    if (packet instanceof C03PacketPlayer) {
                        final C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;
                        if (verusB3733SpoofGround) {
                            packetPlayer.onGround = true;
                        }
                    }
                    break;
            }
        }

        if (event instanceof UpdateEvent) {
            switch (mode) {
                case "Verus b3733 Float":
                    getTimer().timerSpeed = 1F;
                    if (!isMoving()) {
                        verusB3733Stage = 0;
                        verusB3733FirstHop = true;
                        return;
                    }
                    if (!verusB3733IsGround) {
                        verusB3733IsGround = getPlayer().onGround;
                        return;
                    }
                    if (verusB3733FirstHop) {
                        if (isMoving() && getPlayer().onGround) {
                            getPlayer().motionY = (double) 0.42F;
                            verusB3733SpoofGround = true;
                            verusB3733Stage = 0;
                        } else if (verusB3733Stage <= 7) {
                            getPlayer().motionY = 0;
                            verusB3733Stage++;
                        } else {
                            verusB3733SpoofGround = false;
                            verusB3733FirstHop = false;
                        }
                    } else {
                        if (isMoving() && getPlayer().onGround) {
                            verusB3733Speed = 0.5;
                            getPlayer().motionY = (double) 0.42F;
                            verusB3733SpoofGround = true;
                            verusB3733Stage = 0;
                        } else if (verusB3733Stage <= 7) {
                            verusB3733Speed += 0.12;
                            getPlayer().motionY = 0;
                            verusB3733Stage++;
                        } else {
                            verusB3733Speed = 0.24;
                            verusB3733SpoofGround = false;
                        }
                        movementUtil.setSpeed(verusB3733Speed - 1E-4);
                    }
                    break;
                case "NCP3.16.0-YPort":
                    getPlayer().setSprinting(true);
                    if (isMoving())
                        if (getPlayer().onGround) {
                            getPlayer().jump();
                            getPlayer().motionX *= 0.75;
                            getPlayer().motionZ *= 0.75;
                            getTimer().timerSpeed = 0.8F;
                        } else {
                            if (getPlayer().motionY < 0.4) {
                                getTimer().timerSpeed = 1.6F;
                                getPlayer().motionY = -1337.0;
                                movementUtil.setSpeed(0.26);
                            }
                        }
                    break;
                case "Spartan441":
                    if (isMoving())
                        if (getPlayer().onGround) {
                            movementUtil.setSpeed(1.9);
                            getPlayer().jump();
                        }
                    break;
                case "Spartan422.1 Ground":
                    if (isMoving()) {
                        if (getPlayer().ticksExisted % 5 == 0) {
                            getTimer().timerSpeed = 0.2F;
                        } else {
                            getTimer().timerSpeed = 6.5F;
                        }
                    }
                    break;
                case "Timer":
                    float timerSpeed = (float) this.timerSpeed;
                    if (timerRandomizing)
                        timerSpeed += randomUtil.getRandomGaussian(timerRandomizeRange);
                    getTimer().timerSpeed = (float) Math.max(0.1, timerSpeed);
                    break;
                case "Legit":
                    if (getPlayer().onGround && isMoving())
                        getPlayer().jump();
                    getPlayer().setSprinting(true);
                    if(legitStrafing)
                        movementUtil.setSpeed(movementUtil.getSpeed(getPlayer()));
                    break;
                case "AAC4":
                    if (getPlayer().onGround) {
                        aacSpeed++;
                        getPlayer().jump();
                        if (aacSpeed <= aac4Steps)
                            getTimer().timerSpeed = (float) aac4BoostSpeed;
                        else
                            getTimer().timerSpeed = (float) aac4SlowingSpeed;
                    } else {
                        getTimer().timerSpeed = 1.0F;
                    }

                    if (aacSpeed >= aac4Steps + 1)
                        aacSpeed = 0;
                    break;
                case "Intave13.0.9MiniHop":
                    getGameSettings().keyBindSprint.pressed = true;
                    if (getPlayer().onGround) {
                        getPlayer().jump();
                        getPlayer().motionY -= 0.2;
                    }
                    break;
                case "Intave14.0.0":
                    getTimer().timerSpeed = 1F;
                    if (getPlayer().onGround) {
                        if (getPlayer().ticksExisted % 20 == 0) {
                            getTimer().timerSpeed = 15000;
                        } else {
                            getTimer().timerSpeed = 1.0F;
                        }
                    }
                    break;
                case "Intave14.0.3":
                    if (getPlayer().onGround) {
                        if (getPlayer().ticksExisted % 5 == 0) {
                            getPlayer().jump();
                        }
                        getTimer().timerSpeed = 0.1F;
                    } else {
                        getTimer().timerSpeed = 1.75F;
                    }
                    break;
                case "Vulcan2.0.1":
                    getTimer().timerSpeed = 1F;
                    if (getPlayer().onGround) {
                        if (getPlayer().ticksExisted % 3 == 0) {
                            getTimer().timerSpeed = 180F;
                        } else {
                            getTimer().timerSpeed = 0.4F;
                        }
                    }
                    break;
                case "Intave13":
                    if (isMoving()) {
                        getGameSettings().keyBindSprint.pressed = true;
                        if (getPlayer().onGround)
                            getPlayer().jump();
                    }
                    break;

                case "Intave13Carpet":
                    if (isMoving()) {
                        boolean isCarpet = false;
                        for (int i = (int) getY(); i > 0; i--) {
                            if (getWorld().getBlockState(new BlockPos(getX(), i, getZ())).getBlock() instanceof BlockCarpet) {
                                isCarpet = true;
                                wasCarpet = true;
                            }
                        }
                        if (isCarpet) {
                            movementUtil.teleportTo(0.3);
                            if (getPlayer().onGround)
                                movementUtil.setPosition(getX(), getY() + 0.28, getZ());
                        }

                        if (getPlayer().onGround)
                            wasCarpet = false;
                    }
                    break;
                case "Tired":
                    if (getPlayer().motionY > -0.1) {
                        getGameSettings().keyBindSprint.pressed = false;
                        if (getPlayer().onGround && isMoving()) {
                            getPlayer().jump();
                            getPlayer().setSprinting(false);
                            movementUtil.setSpeed(0.06);
                            getTimer().timerSpeed = 5F;
                        } else {
                            getPlayer().setSprinting(true);
                            getPlayer().speedInAir = 0.23F;
                        }
                        if (getPlayer().motionY >= 0.3) {
                            getPlayer().motionY = 0;
                        }
                    } else {
                        getTimer().timerSpeed = 1F;
                    }
                    break;
                case "AAC3.3.12":
                    if (isMoving()) {
                        if (getPlayer().onGround) {
                            getPlayer().jump();
                        } else {
                            getPlayer().motionY -= 0.022;
                            getPlayer().jumpMovementFactor = 0.032F;
                        }
                    }
                    break;
                case "Hypixel20210518":
                    if (isMoving())
                        if (getPlayer().onGround) {
                            if (getPlayer().ticksExisted % 4 == 0)
                                getTimer().timerSpeed = 10;
                            else
                                getTimer().timerSpeed = 2;
                            if (getPlayer().ticksExisted % 10 == 0)
                                getTimer().timerSpeed = 1;
                            if (KillAura.getCurEntity() != null || getPlayer().isUsingItem())
                                getTimer().timerSpeed = 1F;
                        }
                    break;
                case "CubeCraft":
                    final boolean boost = (Math.abs(getYaw() - getPlayer().rotationYaw) < 90.0F);
                    if (isMoving())
                        if (getPlayer().onGround) {
                            getPlayer().motionY = 0.4D;
                            movementUtil.addMotion(0.2F, getPlayer().rotationYaw);
                            getTimer().timerSpeed = 1.0F;
                        } else {
                            getTimer().timerSpeed = 1.1F;
                            final double currentSpeed = Math.sqrt(getPlayer().motionX * getPlayer().motionX
                                    + getPlayer().motionZ * getPlayer().motionZ);
                            final double speed = 1;
                            movementUtil.setSpeed(speed * currentSpeed, getPlayer().rotationYaw);
                            getPlayer().motionY -= 0.00028;
                        }
                    break;
                case "Mineplex SuperFAST":
                    if (isMoving()) {
                        if (getPlayer().isCollidedHorizontally) {
                            mineplexMotion = 0.28f;
                            if (getPlayer().onGround) {
                                getPlayer().jump();
                            }
                        } else {
                            if (getPlayer().onGround) {
                                //0.48 works but only on straight lines
                                mineplexMotion += 0.43;
                                getPlayer().jump();
                                getPlayer().motionX = getPlayer().motionZ = 0;
                            } else {
                                if (getPlayer().motionY > 0) {
                                    getPlayer().motionY += 0.03f;
                                }
                                mineplexMotion -= mineplexMotion / 64;
                                MovementUtil.getInstance().setSpeed(mineplexMotion);
                            }
                        }
                    } else {
                        mineplexMotion = 0.02f;
                    }
                    break;
                case "Mineplex FAST":
                    if (!getPlayer().isInWeb) {
                        if (getPlayer().isCollidedHorizontally) {
                            setMotion(0);
                            mineplexMotion = 0.02F;
                        }
                        if (isMoving()) {
                            if (getPlayer().onGround) {
                                getPlayer().motionX = getPlayer().motionZ = 0;
                                mineplexMotion += 0.25F;
                                getPlayer().jump();
                            } else {
                                mineplexMotion -= mineplexMotion / 64;
                                movementUtil.setSpeed(mineplexMotion);
                            }
                        } else {
                            mineplexMotion = 0.02F;
                        }
                    }
                    break;
                case "Mineplex LowHop":
                    if (!getPlayer().isInWeb) {
                        if (getPlayer().isCollidedHorizontally) {
                            setMotion(0);
                            mineplexMotion = 0.02F;
                        }
                        if (isMoving()) {
                            if (getPlayer().onGround) {
                                getPlayer().motionX = getPlayer().motionZ = 0;
                                mineplexMotion += 0.45F;
                                getPlayer().motionY = 0.34;
                            } else {
                                mineplexMotion -= mineplexMotion / 60;
                                movementUtil.setSpeed(mineplexMotion);
                            }
                        } else {
                            mineplexMotion = 0.02F;
                        }
                    }
                    break;
                case "Mineplex Ground":
                    if (inventoryUtil.hasAir()) {
                        if (getPlayer().onGround && isMoving()) {
                            final int slot = inventoryUtil.getAir();
                            final ItemStack stack = getPlayer().inventory.getStackInSlot(slot);
                            final BlockPos blockPos = new BlockPos(getPlayer().posX, getPlayer().posY - 1, getPlayer().posZ);
                            final Vec3 vec = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());

                            sendPacket(new C09PacketHeldItemChange(slot));
                            getPlayerController().onPlayerRightClick(getPlayer(), mc.theWorld, null, blockPos, EnumFacing.UP, new Vec3(vec.xCoord * 0.4F, vec.yCoord * 0.4F, vec.zCoord * 0.4F));
                            final float targetSpeed = 0.8F;
                            if (targetSpeed > mineplexSpeed)
                                mineplexSpeed += targetSpeed / 8;
                            if (mineplexSpeed >= targetSpeed)
                                mineplexSpeed = targetSpeed;
                            movementUtil.setSpeed(mineplexSpeed);
                        } else {
                            mineplexSpeed = 0;
                        }
                    } else {
                        sendMessage("§cYou must have a Empty Slot!");
                        this.setToggled(false);
                    }
                    break;
                case "Mineplex":
                    if (!getPlayer().isInWeb) {
                        if (getPlayer().onGround && isMoving())
                            getPlayer().jump();

                        if (isMoving()) {
                            getPlayer().setSprinting(false);
                            getGameSettings().keyBindSprint.pressed = false;
                            movementUtil.setSpeed(0.35D);
                            getPlayer().speedInAir = 0.044F;
                        }
                    }
                    break;
                case "MCCentral":
                    if (getPlayer().onGround && isMoving()) {
                        getPlayer().jump();
                    } else {
                        if (isMoving())
                            movementUtil.setSpeed(0.7);
                    }
                    break;
            }
        }

        if (event instanceof final UpdateMotionEvent updateMotionEvent) {
            if (updateMotionEvent.getType().equals(UpdateMotionEvent.Type.POST)) {
                switch (mode) {
                    case "Hypixel20201207":
                        if (getPlayer().onGround && isMoving()) {
                            getPlayer().jump();
                            getPlayer().motionY = 0.41;
                            movementUtil.addMotion(0.065F);
                        } else {
                            getPlayer().motionY -= 0.004f;
                        }
                        break;
                }
            }
        }

    }

    @Override
    public void onEnable() {
        verusB3733IsGround = false;
        mineplexMotion = 0.2F;
        verusB3733FirstHop = true;
        verusB3733Stage = 0;
        verusB3733SpoofGround = false;
        curSlot = getPlayer().inventory.currentItem;
    }

    @Override
    public void onDisable() {
        getGameSettings().keyBindJump.pressed = isKeyDown(getGameSettings().keyBindJump.getKeyCode());
        getTimer().timerSpeed = 1.0F;
        getPlayer().speedInAir = 0.02F;
        mineplexSpeed = 0;
        if (mode.equalsIgnoreCase("Mineplex Ground"))
            sendPacket(new C09PacketHeldItemChange(curSlot));
    }
}
