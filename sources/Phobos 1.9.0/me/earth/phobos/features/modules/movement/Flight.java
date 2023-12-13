package me.earth.phobos.features.modules.movement;

import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.*;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.MathUtil;
import me.earth.phobos.util.Timer;
import me.earth.phobos.util.Util;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.MobEffects;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Flight
        extends Module {
    private static Flight INSTANCE = new Flight();
    private final Fly flySwitch = new Fly();
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.PACKET));
    public Setting<Boolean> better = this.register(new Setting<Object>("Better", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.PACKET));
    public Setting<Format> format = this.register(new Setting<Object>("Format", Format.DAMAGE, v -> this.mode.getValue() == Mode.DAMAGE));
    public Setting<PacketMode> type = this.register(new Setting<Object>("Type", PacketMode.Y, v -> this.mode.getValue() == Mode.PACKET));
    public Setting<Boolean> phase = this.register(new Setting<Object>("Phase", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.PACKET && this.better.getValue() != false));
    public Setting<Float> speed = this.register(new Setting<Object>("Speed", Float.valueOf(0.1f), Float.valueOf(0.0f), Float.valueOf(10.0f), v -> this.mode.getValue() == Mode.PACKET || this.mode.getValue() == Mode.DESCEND || this.mode.getValue() == Mode.DAMAGE, "The speed."));
    public Setting<Boolean> noKick = this.register(new Setting<Object>("NoKick", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.PACKET || this.mode.getValue() == Mode.DAMAGE));
    public Setting<Boolean> noClip = this.register(new Setting<Object>("NoClip", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.DAMAGE));
    public Setting<Boolean> groundSpoof = this.register(new Setting<Object>("GroundSpoof", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SPOOF));
    public Setting<Boolean> antiGround = this.register(new Setting<Object>("AntiGround", Boolean.valueOf(true), v -> this.mode.getValue() == Mode.SPOOF));
    public Setting<Integer> cooldown = this.register(new Setting<Object>("Cooldown", Integer.valueOf(1), v -> this.mode.getValue() == Mode.DESCEND));
    public Setting<Boolean> ascend = this.register(new Setting<Object>("Ascend", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.DESCEND));
    private final List<CPacketPlayer> packets = new ArrayList<CPacketPlayer>();
    private int teleportId = 0;
    private int counter = 0;
    private double moveSpeed;
    private double lastDist;
    private int level;
    private final Timer delayTimer = new Timer();

    public Flight() {
        super("Flight", "Makes you fly.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static Flight getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Flight();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onTickEvent(TickEvent.ClientTickEvent event) {
        if (Flight.fullNullCheck() || this.mode.getValue() != Mode.DESCEND) {
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            if (!Flight.mc.player.isElytraFlying()) {
                if (this.counter < 1) {
                    this.counter += this.cooldown.getValue().intValue();
                    Flight.mc.player.connection.sendPacket(new CPacketPlayer.Position(Flight.mc.player.posX, Flight.mc.player.posY, Flight.mc.player.posZ, false));
                    Flight.mc.player.connection.sendPacket(new CPacketPlayer.Position(Flight.mc.player.posX, Flight.mc.player.posY - 0.03, Flight.mc.player.posZ, true));
                } else {
                    --this.counter;
                }
            }
        } else {
            Flight.mc.player.motionY = this.ascend.getValue() != false ? (double) this.speed.getValue().floatValue() : (double) (-this.speed.getValue().floatValue());
        }
    }

    @Override
    public void onEnable() {
        CPacketPlayer.Position bounds;
        if (Flight.fullNullCheck()) {
            return;
        }
        if (this.mode.getValue() == Mode.PACKET) {
            this.teleportId = 0;
            this.packets.clear();
            bounds = new CPacketPlayer.Position(Flight.mc.player.posX, 0.0, Flight.mc.player.posZ, Flight.mc.player.onGround);
            this.packets.add(bounds);
            Flight.mc.player.connection.sendPacket(bounds);
        }
        if (this.mode.getValue() == Mode.CREATIVE) {
            Flight.mc.player.capabilities.isFlying = true;
            if (Flight.mc.player.capabilities.isCreativeMode) {
                return;
            }
            Flight.mc.player.capabilities.allowFlying = true;
        }
        if (this.mode.getValue() == Mode.SPOOF) {
            this.flySwitch.enable();
        }
        if (this.mode.getValue() == Mode.DAMAGE) {
            this.level = 0;
            if (this.format.getValue() == Format.PACKET && Flight.mc.world != null) {
                this.teleportId = 0;
                this.packets.clear();
                bounds = new CPacketPlayer.Position(Flight.mc.player.posX, Flight.mc.player.posY <= 10.0 ? 255.0 : 1.0, Flight.mc.player.posZ, Flight.mc.player.onGround);
                this.packets.add(bounds);
                Flight.mc.player.connection.sendPacket(bounds);
            }
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (this.mode.getValue() == Mode.DAMAGE) {
            if (this.format.getValue() == Format.DAMAGE) {
                if (event.getStage() == 0) {
                    Flight.mc.player.motionY = 0.0;
                    double motionY = 0.42f;
                    if (Flight.mc.player.onGround) {
                        if (Flight.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                            motionY += (float) (Flight.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
                        }
                        Flight.mc.player.motionY = motionY;
                        Phobos.positionManager.setPlayerPosition(Flight.mc.player.posX, Flight.mc.player.motionY, Flight.mc.player.posZ);
                        this.moveSpeed *= 2.149;
                    }
                }
                if (Flight.mc.player.ticksExisted % 2 == 0) {
                    Flight.mc.player.setPosition(Flight.mc.player.posX, Flight.mc.player.posY + MathUtil.getRandom(1.2354235325235235E-14, 1.2354235325235233E-13), Flight.mc.player.posZ);
                }
                if (Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Flight.mc.player.motionY += this.speed.getValue().floatValue() / 2.0f;
                }
                if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Flight.mc.player.motionY -= this.speed.getValue().floatValue() / 2.0f;
                }
            }
            if (this.format.getValue() == Format.NORMAL) {
                Flight.mc.player.motionY = Flight.mc.gameSettings.keyBindJump.isKeyDown() ? (double) this.speed.getValue().floatValue() : (Flight.mc.gameSettings.keyBindSneak.isKeyDown() ? (double) (-this.speed.getValue().floatValue()) : 0.0);
                if (this.noKick.getValue().booleanValue() && Flight.mc.player.ticksExisted % 5 == 0) {
                    Phobos.positionManager.setPlayerPosition(Flight.mc.player.posX, Flight.mc.player.posY - 0.03125, Flight.mc.player.posZ, true);
                }
                double[] dir = EntityUtil.forward(this.speed.getValue().floatValue());
                Flight.mc.player.motionX = dir[0];
                Flight.mc.player.motionZ = dir[1];
            }
            if (this.format.getValue() == Format.PACKET) {
                if (this.teleportId <= 0) {
                    CPacketPlayer.Position bounds = new CPacketPlayer.Position(Flight.mc.player.posX, Flight.mc.player.posY <= 10.0 ? 255.0 : 1.0, Flight.mc.player.posZ, Flight.mc.player.onGround);
                    this.packets.add(bounds);
                    Flight.mc.player.connection.sendPacket(bounds);
                    return;
                }
                Flight.mc.player.setVelocity(0.0, 0.0, 0.0);
                double posY = -1.0E-8;
                if (!Flight.mc.gameSettings.keyBindJump.isKeyDown() && !Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    if (EntityUtil.isMoving()) {
                        for (double x = 0.0625; x < (double) this.speed.getValue().floatValue(); x += 0.262) {
                            double[] dir = EntityUtil.forward(x);
                            Flight.mc.player.setVelocity(dir[0], posY, dir[1]);
                            this.move(dir[0], posY, dir[1]);
                        }
                    }
                } else if (Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                    for (int i = 0; i <= 3; ++i) {
                        Flight.mc.player.setVelocity(0.0, Flight.mc.player.ticksExisted % 20 == 0 ? (double) -0.04f : (double) (0.062f * (float) i), 0.0);
                        this.move(0.0, Flight.mc.player.ticksExisted % 20 == 0 ? (double) -0.04f : (double) (0.062f * (float) i), 0.0);
                    }
                } else if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    for (int i = 0; i <= 3; ++i) {
                        Flight.mc.player.setVelocity(0.0, posY - 0.0625 * (double) i, 0.0);
                        this.move(0.0, posY - 0.0625 * (double) i, 0.0);
                    }
                }
            }
            if (this.format.getValue() == Format.SLOW) {
                double posX = Flight.mc.player.posX;
                double posY = Flight.mc.player.posY;
                double posZ = Flight.mc.player.posZ;
                boolean ground = Flight.mc.player.onGround;
                Flight.mc.player.setVelocity(0.0, 0.0, 0.0);
                if (!Flight.mc.gameSettings.keyBindJump.isKeyDown() && !Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    double[] dir = EntityUtil.forward(0.0625);
                    Flight.mc.player.connection.sendPacket(new CPacketPlayer.Position(posX + dir[0], posY, posZ + dir[1], ground));
                    Flight.mc.player.setPositionAndUpdate(posX + dir[0], posY, posZ + dir[1]);
                } else if (Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Flight.mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, posY + 0.0625, posZ, ground));
                    Flight.mc.player.setPositionAndUpdate(posX, posY + 0.0625, posZ);
                } else if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Flight.mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, posY - 0.0625, posZ, ground));
                    Flight.mc.player.setPositionAndUpdate(posX, posY - 0.0625, posZ);
                }
                Flight.mc.player.connection.sendPacket(new CPacketPlayer.Position(posX + Flight.mc.player.motionX, Flight.mc.player.posY <= 10.0 ? 255.0 : 1.0, posZ + Flight.mc.player.motionZ, ground));
            }
            if (this.format.getValue() == Format.DELAY) {
                if (this.delayTimer.passedMs(1000L)) {
                    this.delayTimer.reset();
                }
                if (this.delayTimer.passedMs(600L)) {
                    Flight.mc.player.setVelocity(0.0, 0.0, 0.0);
                    return;
                }
                if (this.teleportId <= 0) {
                    CPacketPlayer.Position bounds = new CPacketPlayer.Position(Flight.mc.player.posX, Flight.mc.player.posY <= 10.0 ? 255.0 : 1.0, Flight.mc.player.posZ, Flight.mc.player.onGround);
                    this.packets.add(bounds);
                    Flight.mc.player.connection.sendPacket(bounds);
                    return;
                }
                Flight.mc.player.setVelocity(0.0, 0.0, 0.0);
                double posY = -1.0E-8;
                if (!Flight.mc.gameSettings.keyBindJump.isKeyDown() && !Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    if (EntityUtil.isMoving()) {
                        double[] dir = EntityUtil.forward(0.2);
                        Flight.mc.player.setVelocity(dir[0], posY, dir[1]);
                        this.move(dir[0], posY, dir[1]);
                    }
                } else if (Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Flight.mc.player.setVelocity(0.0, 0.062f, 0.0);
                    this.move(0.0, 0.062f, 0.0);
                } else if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Flight.mc.player.setVelocity(0.0, 0.0625, 0.0);
                    this.move(0.0, 0.0625, 0.0);
                }
            }
            if (this.noClip.getValue().booleanValue()) {
                Flight.mc.player.noClip = true;
            }
        }
        if (event.getStage() == 0) {
            if (this.mode.getValue() == Mode.CREATIVE) {
                Flight.mc.player.capabilities.setFlySpeed(this.speed.getValue().floatValue());
                Flight.mc.player.capabilities.isFlying = true;
                if (Flight.mc.player.capabilities.isCreativeMode) {
                    return;
                }
                Flight.mc.player.capabilities.allowFlying = true;
            }
            if (this.mode.getValue() == Mode.VANILLA) {
                Flight.mc.player.setVelocity(0.0, 0.0, 0.0);
                Flight.mc.player.jumpMovementFactor = this.speed.getValue().floatValue();
                if (this.noKick.getValue().booleanValue() && Flight.mc.player.ticksExisted % 4 == 0) {
                    Flight.mc.player.motionY = -0.04f;
                }
                double[] dir = MathUtil.directionSpeed(this.speed.getValue().floatValue());
                if (Flight.mc.player.movementInput.moveStrafe != 0.0f || Flight.mc.player.movementInput.moveForward != 0.0f) {
                    Flight.mc.player.motionX = dir[0];
                    Flight.mc.player.motionZ = dir[1];
                } else {
                    Flight.mc.player.motionX = 0.0;
                    Flight.mc.player.motionZ = 0.0;
                }
                if (Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Flight.mc.player.motionY = this.noKick.getValue().booleanValue() ? (Flight.mc.player.ticksExisted % 20 == 0 ? (double) -0.04f : (double) this.speed.getValue().floatValue()) : (Flight.mc.player.motionY += this.speed.getValue().floatValue());
                }
                if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Flight.mc.player.motionY -= this.speed.getValue().floatValue();
                }
            }
            if (this.mode.getValue() == Mode.PACKET && !this.better.getValue().booleanValue()) {
                this.doNormalPacketFly();
            }
            if (this.mode.getValue() == Mode.PACKET && this.better.getValue().booleanValue()) {
                this.doBetterPacketFly();
            }
        }
    }

    private void doNormalPacketFly() {
        if (this.teleportId <= 0) {
            CPacketPlayer.Position bounds = new CPacketPlayer.Position(Flight.mc.player.posX, 0.0, Flight.mc.player.posZ, Flight.mc.player.onGround);
            this.packets.add(bounds);
            Flight.mc.player.connection.sendPacket(bounds);
            return;
        }
        Flight.mc.player.setVelocity(0.0, 0.0, 0.0);
        if (Flight.mc.world.getCollisionBoxes(Flight.mc.player, Flight.mc.player.getEntityBoundingBox().expand(-0.0625, 0.0, -0.0625)).isEmpty()) {
            double ySpeed = 0.0;
            ySpeed = Flight.mc.gameSettings.keyBindJump.isKeyDown() ? (this.noKick.getValue().booleanValue() ? (Flight.mc.player.ticksExisted % 20 == 0 ? (double) -0.04f : (double) 0.062f) : (double) 0.062f) : (Flight.mc.gameSettings.keyBindSneak.isKeyDown() ? -0.062 : (Flight.mc.world.getCollisionBoxes(Flight.mc.player, Flight.mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty() ? (Flight.mc.player.ticksExisted % 4 == 0 ? (double) (this.noKick.getValue() != false ? -0.04f : 0.0f) : 0.0) : 0.0));
            double[] directionalSpeed = MathUtil.directionSpeed(this.speed.getValue().floatValue());
            if (Flight.mc.gameSettings.keyBindJump.isKeyDown() || Flight.mc.gameSettings.keyBindSneak.isKeyDown() || Flight.mc.gameSettings.keyBindForward.isKeyDown() || Flight.mc.gameSettings.keyBindBack.isKeyDown() || Flight.mc.gameSettings.keyBindRight.isKeyDown() || Flight.mc.gameSettings.keyBindLeft.isKeyDown()) {
                if (directionalSpeed[0] != 0.0 || directionalSpeed[1] != 0.0) {
                    if (Flight.mc.player.movementInput.jump && (Flight.mc.player.moveStrafing != 0.0f || Flight.mc.player.moveForward != 0.0f)) {
                        Flight.mc.player.setVelocity(0.0, 0.0, 0.0);
                        this.move(0.0, 0.0, 0.0);
                        for (int i = 0; i <= 3; ++i) {
                            Flight.mc.player.setVelocity(0.0, ySpeed * (double) i, 0.0);
                            this.move(0.0, ySpeed * (double) i, 0.0);
                        }
                    } else if (Flight.mc.player.movementInput.jump) {
                        Flight.mc.player.setVelocity(0.0, 0.0, 0.0);
                        this.move(0.0, 0.0, 0.0);
                        for (int i = 0; i <= 3; ++i) {
                            Flight.mc.player.setVelocity(0.0, ySpeed * (double) i, 0.0);
                            this.move(0.0, ySpeed * (double) i, 0.0);
                        }
                    } else {
                        for (int i = 0; i <= 2; ++i) {
                            Flight.mc.player.setVelocity(directionalSpeed[0] * (double) i, ySpeed * (double) i, directionalSpeed[1] * (double) i);
                            this.move(directionalSpeed[0] * (double) i, ySpeed * (double) i, directionalSpeed[1] * (double) i);
                        }
                    }
                }
            } else if (this.noKick.getValue().booleanValue() && Flight.mc.world.getCollisionBoxes(Flight.mc.player, Flight.mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty()) {
                Flight.mc.player.setVelocity(0.0, Flight.mc.player.ticksExisted % 2 == 0 ? (double) 0.04f : (double) -0.04f, 0.0);
                this.move(0.0, Flight.mc.player.ticksExisted % 2 == 0 ? (double) 0.04f : (double) -0.04f, 0.0);
            }
        }
    }

    private void doBetterPacketFly() {
        if (this.teleportId <= 0) {
            CPacketPlayer.Position bounds = new CPacketPlayer.Position(Flight.mc.player.posX, 10000.0, Flight.mc.player.posZ, Flight.mc.player.onGround);
            this.packets.add(bounds);
            Flight.mc.player.connection.sendPacket(bounds);
            return;
        }
        Flight.mc.player.setVelocity(0.0, 0.0, 0.0);
        if (Flight.mc.world.getCollisionBoxes(Flight.mc.player, Flight.mc.player.getEntityBoundingBox().expand(-0.0625, 0.0, -0.0625)).isEmpty()) {
            double ySpeed = 0.0;
            ySpeed = Flight.mc.gameSettings.keyBindJump.isKeyDown() ? (this.noKick.getValue().booleanValue() ? (Flight.mc.player.ticksExisted % 20 == 0 ? (double) -0.04f : (double) 0.062f) : (double) 0.062f) : (Flight.mc.gameSettings.keyBindSneak.isKeyDown() ? -0.062 : (Flight.mc.world.getCollisionBoxes(Flight.mc.player, Flight.mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty() ? (Flight.mc.player.ticksExisted % 4 == 0 ? (double) (this.noKick.getValue() != false ? -0.04f : 0.0f) : 0.0) : 0.0));
            double[] directionalSpeed = MathUtil.directionSpeed(this.speed.getValue().floatValue());
            if (Flight.mc.gameSettings.keyBindJump.isKeyDown() || Flight.mc.gameSettings.keyBindSneak.isKeyDown() || Flight.mc.gameSettings.keyBindForward.isKeyDown() || Flight.mc.gameSettings.keyBindBack.isKeyDown() || Flight.mc.gameSettings.keyBindRight.isKeyDown() || Flight.mc.gameSettings.keyBindLeft.isKeyDown()) {
                if (directionalSpeed[0] != 0.0 || directionalSpeed[1] != 0.0) {
                    if (Flight.mc.player.movementInput.jump && (Flight.mc.player.moveStrafing != 0.0f || Flight.mc.player.moveForward != 0.0f)) {
                        Flight.mc.player.setVelocity(0.0, 0.0, 0.0);
                        this.move(0.0, 0.0, 0.0);
                        for (int i = 0; i <= 3; ++i) {
                            Flight.mc.player.setVelocity(0.0, ySpeed * (double) i, 0.0);
                            this.move(0.0, ySpeed * (double) i, 0.0);
                        }
                    } else if (Flight.mc.player.movementInput.jump) {
                        Flight.mc.player.setVelocity(0.0, 0.0, 0.0);
                        this.move(0.0, 0.0, 0.0);
                        for (int i = 0; i <= 3; ++i) {
                            Flight.mc.player.setVelocity(0.0, ySpeed * (double) i, 0.0);
                            this.move(0.0, ySpeed * (double) i, 0.0);
                        }
                    } else {
                        for (int i = 0; i <= 2; ++i) {
                            Flight.mc.player.setVelocity(directionalSpeed[0] * (double) i, ySpeed * (double) i, directionalSpeed[1] * (double) i);
                            this.move(directionalSpeed[0] * (double) i, ySpeed * (double) i, directionalSpeed[1] * (double) i);
                        }
                    }
                }
            } else if (this.noKick.getValue().booleanValue() && Flight.mc.world.getCollisionBoxes(Flight.mc.player, Flight.mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty()) {
                Flight.mc.player.setVelocity(0.0, Flight.mc.player.ticksExisted % 2 == 0 ? (double) 0.04f : (double) -0.04f, 0.0);
                this.move(0.0, Flight.mc.player.ticksExisted % 2 == 0 ? (double) 0.04f : (double) -0.04f, 0.0);
            }
        }
    }

    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Mode.SPOOF) {
            if (Flight.fullNullCheck()) {
                return;
            }
            if (!Flight.mc.player.capabilities.allowFlying) {
                this.flySwitch.disable();
                this.flySwitch.enable();
                Flight.mc.player.capabilities.isFlying = false;
            }
            Flight.mc.player.capabilities.setFlySpeed(0.05f * this.speed.getValue().floatValue());
        }
    }

    @Override
    public void onDisable() {
        if (this.mode.getValue() == Mode.CREATIVE && Flight.mc.player != null) {
            Flight.mc.player.capabilities.isFlying = false;
            Flight.mc.player.capabilities.setFlySpeed(0.05f);
            if (Flight.mc.player.capabilities.isCreativeMode) {
                return;
            }
            Flight.mc.player.capabilities.allowFlying = false;
        }
        if (this.mode.getValue() == Mode.SPOOF) {
            this.flySwitch.disable();
        }
        if (this.mode.getValue() == Mode.DAMAGE) {
            Phobos.timerManager.reset();
            Flight.mc.player.setVelocity(0.0, 0.0, 0.0);
            this.moveSpeed = Strafe.getBaseMoveSpeed();
            this.lastDist = 0.0;
            if (this.noClip.getValue().booleanValue()) {
                Flight.mc.player.noClip = false;
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    @Override
    public void onLogout() {
        if (this.isOn()) {
            this.disable();
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (event.getStage() == 0 && this.mode.getValue() == Mode.DAMAGE && this.format.getValue() == Format.DAMAGE) {
            double forward = Flight.mc.player.movementInput.moveForward;
            double strafe = Flight.mc.player.movementInput.moveStrafe;
            float yaw = Flight.mc.player.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                event.setX(0.0);
                event.setZ(0.0);
            }
            if (forward != 0.0 && strafe != 0.0) {
                forward *= Math.sin(0.7853981633974483);
                strafe *= Math.cos(0.7853981633974483);
            }
            if (this.level != 1 || Flight.mc.player.moveForward == 0.0f && Flight.mc.player.moveStrafing == 0.0f) {
                if (this.level == 2) {
                    ++this.level;
                } else if (this.level == 3) {
                    ++this.level;
                    double difference = (Flight.mc.player.ticksExisted % 2 == 0 ? -0.05 : 0.1) * (this.lastDist - Strafe.getBaseMoveSpeed());
                    this.moveSpeed = this.lastDist - difference;
                } else {
                    if (Flight.mc.world.getCollisionBoxes(Flight.mc.player, Flight.mc.player.getEntityBoundingBox().offset(0.0, Flight.mc.player.motionY, 0.0)).size() > 0 || Flight.mc.player.collidedVertically) {
                        this.level = 1;
                    }
                    this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                }
            } else {
                this.level = 2;
                double boost = Flight.mc.player.isPotionActive(MobEffects.SPEED) ? 1.86 : 2.05;
                this.moveSpeed = boost * Strafe.getBaseMoveSpeed() - 0.01;
            }
            this.moveSpeed = Math.max(this.moveSpeed, Strafe.getBaseMoveSpeed());
            double mx = -Math.sin(Math.toRadians(yaw));
            double mz = Math.cos(Math.toRadians(yaw));
            event.setX(forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
            event.setZ(forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0) {
            CPacketPlayer packet;
            if (this.mode.getValue() == Mode.PACKET) {
                if (Flight.fullNullCheck()) {
                    return;
                }
                if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
                    event.setCanceled(true);
                }
                if (event.getPacket() instanceof CPacketPlayer) {
                    packet = event.getPacket();
                    if (this.packets.contains(packet)) {
                        this.packets.remove(packet);
                        return;
                    }
                    event.setCanceled(true);
                }
            }
            if (this.mode.getValue() == Mode.SPOOF) {
                if (Flight.fullNullCheck()) {
                    return;
                }
                if (!(this.groundSpoof.getValue().booleanValue() && event.getPacket() instanceof CPacketPlayer && Flight.mc.player.capabilities.isFlying)) {
                    return;
                }
                packet = event.getPacket();
                if (!packet.moving) {
                    return;
                }
                AxisAlignedBB range = Flight.mc.player.getEntityBoundingBox().expand(0.0, -Flight.mc.player.posY, 0.0).contract(0.0, -Flight.mc.player.height, 0.0);
                List<AxisAlignedBB> collisionBoxes = Flight.mc.player.world.getCollisionBoxes(Flight.mc.player, range);
                AtomicReference<Double> newHeight = new AtomicReference<Double>(0.0);
                collisionBoxes.forEach(box -> newHeight.set(Math.max((Double) newHeight.get(), box.maxY)));
                packet.y = newHeight.get();
                packet.onGround = true;
            }
            if (this.mode.getValue() == Mode.DAMAGE && (this.format.getValue() == Format.PACKET || this.format.getValue() == Format.DELAY)) {
                if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
                    event.setCanceled(true);
                }
                if (event.getPacket() instanceof CPacketPlayer) {
                    packet = event.getPacket();
                    if (this.packets.contains(packet)) {
                        this.packets.remove(packet);
                        return;
                    }
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() == 0) {
            SPacketPlayerPosLook packet;
            if (this.mode.getValue() == Mode.PACKET) {
                if (Flight.fullNullCheck()) {
                    return;
                }
                if (event.getPacket() instanceof SPacketPlayerPosLook) {
                    packet = event.getPacket();
                    if (Flight.mc.player.isEntityAlive() && Flight.mc.world.isBlockLoaded(new BlockPos(Flight.mc.player.posX, Flight.mc.player.posY, Flight.mc.player.posZ)) && !(Flight.mc.currentScreen instanceof GuiDownloadTerrain)) {
                        if (this.teleportId <= 0) {
                            this.teleportId = packet.getTeleportId();
                        } else {
                            event.setCanceled(true);
                        }
                    }
                }
            }
            if (this.mode.getValue() == Mode.SPOOF) {
                if (Flight.fullNullCheck()) {
                    return;
                }
                if (!(this.antiGround.getValue().booleanValue() && event.getPacket() instanceof SPacketPlayerPosLook && Flight.mc.player.capabilities.isFlying)) {
                    return;
                }
                packet = event.getPacket();
                double oldY = Flight.mc.player.posY;
                Flight.mc.player.setPosition(packet.x, packet.y, packet.z);
                AxisAlignedBB range = Flight.mc.player.getEntityBoundingBox().expand(0.0, (double) (256.0f - Flight.mc.player.height) - Flight.mc.player.posY, 0.0).contract(0.0, Flight.mc.player.height, 0.0);
                List<AxisAlignedBB> collisionBoxes = Flight.mc.player.world.getCollisionBoxes(Flight.mc.player, range);
                AtomicReference<Double> newY = new AtomicReference<Double>(256.0);
                collisionBoxes.forEach(box -> newY.set(Math.min((Double) newY.get(), box.minY - (double) Flight.mc.player.height)));
                packet.y = Math.min(oldY, newY.get());
            }
            if (this.mode.getValue() == Mode.DAMAGE && (this.format.getValue() == Format.PACKET || this.format.getValue() == Format.DELAY) && event.getPacket() instanceof SPacketPlayerPosLook) {
                packet = event.getPacket();
                if (Flight.mc.player.isEntityAlive() && Flight.mc.world.isBlockLoaded(new BlockPos(Flight.mc.player.posX, Flight.mc.player.posY, Flight.mc.player.posZ)) && !(Flight.mc.currentScreen instanceof GuiDownloadTerrain)) {
                    if (this.teleportId <= 0) {
                        this.teleportId = packet.getTeleportId();
                    } else {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this) && this.isEnabled() && !event.getSetting().equals(this.enabled)) {
            this.disable();
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent event) {
        if (event.getStage() == 1 && this.mode.getValue() == Mode.PACKET && this.better.getValue().booleanValue() && this.phase.getValue().booleanValue()) {
            event.setCanceled(true);
        }
    }

    private void move(double x, double y, double z) {
        CPacketPlayer.Position pos = new CPacketPlayer.Position(Flight.mc.player.posX + x, Flight.mc.player.posY + y, Flight.mc.player.posZ + z, Flight.mc.player.onGround);
        this.packets.add(pos);
        Flight.mc.player.connection.sendPacket(pos);
        Object bounds = this.better.getValue() != false ? this.createBoundsPacket(x, y, z) : new CPacketPlayer.Position(Flight.mc.player.posX + x, 0.0, Flight.mc.player.posZ + z, Flight.mc.player.onGround);
        this.packets.add((CPacketPlayer) bounds);
        Flight.mc.player.connection.sendPacket((Packet) bounds);
        ++this.teleportId;
        Flight.mc.player.connection.sendPacket(new CPacketConfirmTeleport(this.teleportId - 1));
        Flight.mc.player.connection.sendPacket(new CPacketConfirmTeleport(this.teleportId));
        Flight.mc.player.connection.sendPacket(new CPacketConfirmTeleport(this.teleportId + 1));
    }

    private CPacketPlayer createBoundsPacket(double x, double y, double z) {
        switch (this.type.getValue()) {
            case Up: {
                return new CPacketPlayer.Position(Flight.mc.player.posX + x, 10000.0, Flight.mc.player.posZ + z, Flight.mc.player.onGround);
            }
            case Down: {
                return new CPacketPlayer.Position(Flight.mc.player.posX + x, -10000.0, Flight.mc.player.posZ + z, Flight.mc.player.onGround);
            }
            case Zero: {
                return new CPacketPlayer.Position(Flight.mc.player.posX + x, 0.0, Flight.mc.player.posZ + z, Flight.mc.player.onGround);
            }
            case Y: {
                return new CPacketPlayer.Position(Flight.mc.player.posX + x, Flight.mc.player.posY + y <= 10.0 ? 255.0 : 1.0, Flight.mc.player.posZ + z, Flight.mc.player.onGround);
            }
            case X: {
                return new CPacketPlayer.Position(Flight.mc.player.posX + x + 75.0, Flight.mc.player.posY + y, Flight.mc.player.posZ + z, Flight.mc.player.onGround);
            }
            case Z: {
                return new CPacketPlayer.Position(Flight.mc.player.posX + x, Flight.mc.player.posY + y, Flight.mc.player.posZ + z + 75.0, Flight.mc.player.onGround);
            }
            case XZ: {
                return new CPacketPlayer.Position(Flight.mc.player.posX + x + 75.0, Flight.mc.player.posY + y, Flight.mc.player.posZ + z + 75.0, Flight.mc.player.onGround);
            }
        }
        return new CPacketPlayer.Position(Flight.mc.player.posX + x, 2000.0, Flight.mc.player.posZ + z, Flight.mc.player.onGround);
    }

    private enum PacketMode {
        Up,
        Down,
        Zero,
        Y,
        X,
        Z,
        XZ

    }

    public enum Format {
        DAMAGE,
        SLOW,
        DELAY,
        NORMAL,
        PACKET

    }

    public enum Mode {
        CREATIVE,
        VANILLA,
        PACKET,
        SPOOF,
        DESCEND,
        DAMAGE

    }

    private static class Fly {
        private Fly() {
        }

        protected void enable() {
            Util.mc.addScheduledTask(() -> {
                if (Util.mc.player == null || Util.mc.player.capabilities == null) {
                    return;
                }
                Util.mc.player.capabilities.allowFlying = true;
                Util.mc.player.capabilities.isFlying = true;
            });
        }

        protected void disable() {
            Util.mc.addScheduledTask(() -> {
                if (Util.mc.player == null || Util.mc.player.capabilities == null) {
                    return;
                }
                PlayerCapabilities gmCaps = new PlayerCapabilities();
                Util.mc.playerController.getCurrentGameType().configurePlayerCapabilities(gmCaps);
                PlayerCapabilities capabilities = Util.mc.player.capabilities;
                capabilities.allowFlying = gmCaps.allowFlying;
                capabilities.isFlying = gmCaps.allowFlying && capabilities.isFlying;
                capabilities.setFlySpeed(gmCaps.getFlySpeed());
            });
        }
    }
}
