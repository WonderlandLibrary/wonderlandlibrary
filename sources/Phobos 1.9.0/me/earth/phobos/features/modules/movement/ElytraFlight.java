package me.earth.phobos.features.modules.movement;

import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.MoveEvent;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.MathUtil;
import me.earth.phobos.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ElytraFlight
        extends Module {
    private static ElytraFlight INSTANCE = new ElytraFlight();
    private final Timer timer = new Timer();
    private final Timer bypassTimer = new Timer();
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.FLY));
    public Setting<Integer> devMode = this.register(new Setting<Object>("Type", 2, 1, 3, v -> this.mode.getValue() == Mode.BYPASS || this.mode.getValue() == Mode.BETTER, "EventMode"));
    public Setting<Float> speed = this.register(new Setting<Object>("Speed", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(10.0f), v -> this.mode.getValue() != Mode.FLY && this.mode.getValue() != Mode.BOOST && this.mode.getValue() != Mode.BETTER && this.mode.getValue() != Mode.OHARE, "The Speed."));
    public Setting<Float> vSpeed = this.register(new Setting<Object>("VSpeed", Float.valueOf(0.3f), Float.valueOf(0.0f), Float.valueOf(10.0f), v -> this.mode.getValue() == Mode.BETTER || this.mode.getValue() == Mode.OHARE, "Vertical Speed"));
    public Setting<Float> hSpeed = this.register(new Setting<Object>("HSpeed", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(10.0f), v -> this.mode.getValue() == Mode.BETTER || this.mode.getValue() == Mode.OHARE, "Horizontal Speed"));
    public Setting<Float> glide = this.register(new Setting<Object>("Glide", Float.valueOf(1.0E-4f), Float.valueOf(0.0f), Float.valueOf(0.2f), v -> this.mode.getValue() == Mode.BETTER, "Glide Speed"));
    public Setting<Float> tooBeeSpeed = this.register(new Setting<Object>("TooBeeSpeed", Float.valueOf(1.8000001f), Float.valueOf(1.0f), Float.valueOf(2.0f), v -> this.mode.getValue() == Mode.TOOBEE, "Speed for flight on 2b2t"));
    public Setting<Boolean> autoStart = this.register(new Setting<Boolean>("AutoStart", true));
    public Setting<Boolean> disableInLiquid = this.register(new Setting<Boolean>("NoLiquid", true));
    public Setting<Boolean> infiniteDura = this.register(new Setting<Boolean>("InfiniteDura", false));
    public Setting<Boolean> noKick = this.register(new Setting<Object>("NoKick", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.PACKET));
    public Setting<Boolean> allowUp = this.register(new Setting<Object>("AllowUp", Boolean.valueOf(true), v -> this.mode.getValue() == Mode.BETTER));
    public Setting<Boolean> lockPitch = this.register(new Setting<Boolean>("LockPitch", false));
    private boolean vertical;
    private Double posX;
    private Double flyHeight;
    private Double posZ;

    public ElytraFlight() {
        super("ElytraFlight", "Makes Elytra Flight better.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static ElytraFlight getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ElytraFlight();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (this.mode.getValue() == Mode.BETTER && !this.autoStart.getValue().booleanValue() && this.devMode.getValue() == 1) {
            ElytraFlight.mc.player.connection.sendPacket(new CPacketEntityAction(ElytraFlight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }
        this.flyHeight = null;
        this.posX = null;
        this.posZ = null;
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Mode.BYPASS && this.devMode.getValue() == 1 && ElytraFlight.mc.player.isElytraFlying()) {
            ElytraFlight.mc.player.motionX = 0.0;
            ElytraFlight.mc.player.motionY = -1.0E-4;
            ElytraFlight.mc.player.motionZ = 0.0;
            double forwardInput = ElytraFlight.mc.player.movementInput.moveForward;
            double strafeInput = ElytraFlight.mc.player.movementInput.moveStrafe;
            double[] result = this.forwardStrafeYaw(forwardInput, strafeInput, ElytraFlight.mc.player.rotationYaw);
            double forward = result[0];
            double strafe = result[1];
            double yaw = result[2];
            if (forwardInput != 0.0 || strafeInput != 0.0) {
                ElytraFlight.mc.player.motionX = forward * (double) this.speed.getValue().floatValue() * Math.cos(Math.toRadians(yaw + 90.0)) + strafe * (double) this.speed.getValue().floatValue() * Math.sin(Math.toRadians(yaw + 90.0));
                ElytraFlight.mc.player.motionZ = forward * (double) this.speed.getValue().floatValue() * Math.sin(Math.toRadians(yaw + 90.0)) - strafe * (double) this.speed.getValue().floatValue() * Math.cos(Math.toRadians(yaw + 90.0));
            }
            if (ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                ElytraFlight.mc.player.motionY = -1.0;
            }
        }
    }

    @SubscribeEvent
    public void onSendPacket(PacketEvent.Send event) {
        CPacketPlayer packet;
        if (event.getPacket() instanceof CPacketPlayer && this.mode.getValue() == Mode.TOOBEE) {
            packet = event.getPacket();
            if (ElytraFlight.mc.player.isElytraFlying()) {
                // empty if block
            }
        }
        if (event.getPacket() instanceof CPacketPlayer && this.mode.getValue() == Mode.TOOBEEBYPASS) {
            packet = event.getPacket();
            if (ElytraFlight.mc.player.isElytraFlying()) {
                // empty if block
            }
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (this.mode.getValue() == Mode.OHARE) {
            ItemStack itemstack = ElytraFlight.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isUsable(itemstack) && ElytraFlight.mc.player.isElytraFlying()) {
                event.setY(ElytraFlight.mc.gameSettings.keyBindJump.isKeyDown() ? (double) this.vSpeed.getValue().floatValue() : (ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown() ? (double) (-this.vSpeed.getValue().floatValue()) : 0.0));
                ElytraFlight.mc.player.addVelocity(0.0, ElytraFlight.mc.gameSettings.keyBindJump.isKeyDown() ? (double) this.vSpeed.getValue().floatValue() : (ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown() ? (double) (-this.vSpeed.getValue().floatValue()) : 0.0), 0.0);
                ElytraFlight.mc.player.rotateElytraX = 0.0f;
                ElytraFlight.mc.player.rotateElytraY = 0.0f;
                ElytraFlight.mc.player.rotateElytraZ = 0.0f;
                ElytraFlight.mc.player.moveVertical = ElytraFlight.mc.gameSettings.keyBindJump.isKeyDown() ? this.vSpeed.getValue().floatValue() : (ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown() ? -this.vSpeed.getValue().floatValue() : 0.0f);
                double forward = ElytraFlight.mc.player.movementInput.moveForward;
                double strafe = ElytraFlight.mc.player.movementInput.moveStrafe;
                float yaw = ElytraFlight.mc.player.rotationYaw;
                if (forward == 0.0 && strafe == 0.0) {
                    event.setX(0.0);
                    event.setZ(0.0);
                } else {
                    if (forward != 0.0) {
                        if (strafe > 0.0) {
                            yaw += (float) (forward > 0.0 ? -45 : 45);
                        } else if (strafe < 0.0) {
                            yaw += (float) (forward > 0.0 ? 45 : -45);
                        }
                        strafe = 0.0;
                        if (forward > 0.0) {
                            forward = 1.0;
                        } else if (forward < 0.0) {
                            forward = -1.0;
                        }
                    }
                    double cos = Math.cos(Math.toRadians(yaw + 90.0f));
                    double sin = Math.sin(Math.toRadians(yaw + 90.0f));
                    event.setX(forward * (double) this.hSpeed.getValue().floatValue() * cos + strafe * (double) this.hSpeed.getValue().floatValue() * sin);
                    event.setZ(forward * (double) this.hSpeed.getValue().floatValue() * sin - strafe * (double) this.hSpeed.getValue().floatValue() * cos);
                }
            }
        } else if (event.getStage() == 0 && this.mode.getValue() == Mode.BYPASS && this.devMode.getValue() == 3) {
            if (ElytraFlight.mc.player.isElytraFlying()) {
                event.setX(0.0);
                event.setY(-1.0E-4);
                event.setZ(0.0);
                double forwardInput = ElytraFlight.mc.player.movementInput.moveForward;
                double strafeInput = ElytraFlight.mc.player.movementInput.moveStrafe;
                double[] result = this.forwardStrafeYaw(forwardInput, strafeInput, ElytraFlight.mc.player.rotationYaw);
                double forward = result[0];
                double strafe = result[1];
                double yaw = result[2];
                if (forwardInput != 0.0 || strafeInput != 0.0) {
                    event.setX(forward * (double) this.speed.getValue().floatValue() * Math.cos(Math.toRadians(yaw + 90.0)) + strafe * (double) this.speed.getValue().floatValue() * Math.sin(Math.toRadians(yaw + 90.0)));
                    event.setY(forward * (double) this.speed.getValue().floatValue() * Math.sin(Math.toRadians(yaw + 90.0)) - strafe * (double) this.speed.getValue().floatValue() * Math.cos(Math.toRadians(yaw + 90.0)));
                }
                if (ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    event.setY(-1.0);
                }
            }
        } else if (this.mode.getValue() == Mode.TOOBEE) {
            if (!ElytraFlight.mc.player.isElytraFlying()) {
                return;
            }
            if (!ElytraFlight.mc.player.movementInput.jump) {
                if (ElytraFlight.mc.player.movementInput.sneak) {
                    ElytraFlight.mc.player.motionY = -(this.tooBeeSpeed.getValue().floatValue() / 2.0f);
                    event.setY(-(this.speed.getValue().floatValue() / 2.0f));
                } else if (event.getY() != -1.01E-4) {
                    event.setY(-1.01E-4);
                    ElytraFlight.mc.player.motionY = -1.01E-4;
                }
            } else {
                return;
            }
            this.setMoveSpeed(event, this.tooBeeSpeed.getValue().floatValue());
        } else if (this.mode.getValue() == Mode.TOOBEEBYPASS) {
            if (!ElytraFlight.mc.player.isElytraFlying()) {
                return;
            }
            if (!ElytraFlight.mc.player.movementInput.jump) {
                if (this.lockPitch.getValue().booleanValue()) {
                    ElytraFlight.mc.player.rotationPitch = 4.0f;
                }
            } else {
                return;
            }
            if (Phobos.speedManager.getSpeedKpH() > 180.0) {
                return;
            }
            double yaw = Math.toRadians(ElytraFlight.mc.player.rotationYaw);
            ElytraFlight.mc.player.motionX -= (double) ElytraFlight.mc.player.movementInput.moveForward * Math.sin(yaw) * 0.04;
            ElytraFlight.mc.player.motionZ += (double) ElytraFlight.mc.player.movementInput.moveForward * Math.cos(yaw) * 0.04;
        }
    }

    private void setMoveSpeed(MoveEvent event, double speed) {
        double forward = ElytraFlight.mc.player.movementInput.moveForward;
        double strafe = ElytraFlight.mc.player.movementInput.moveStrafe;
        float yaw = ElytraFlight.mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
            ElytraFlight.mc.player.motionX = 0.0;
            ElytraFlight.mc.player.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float) (forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float) (forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            double x = forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw));
            double z = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw));
            event.setX(x);
            event.setZ(z);
            ElytraFlight.mc.player.motionX = x;
            ElytraFlight.mc.player.motionZ = z;
        }
    }

    @Override
    public void onTick() {
        if (!ElytraFlight.mc.player.isElytraFlying()) {
            return;
        }
        switch (this.mode.getValue()) {
            case BOOST: {
                if (ElytraFlight.mc.player.isInWater()) {
                    mc.getConnection().sendPacket(new CPacketEntityAction(ElytraFlight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    return;
                }
                if (ElytraFlight.mc.gameSettings.keyBindJump.isKeyDown()) {
                    ElytraFlight.mc.player.motionY += 0.08;
                } else if (ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    ElytraFlight.mc.player.motionY -= 0.04;
                }
                if (ElytraFlight.mc.gameSettings.keyBindForward.isKeyDown()) {
                    float yaw = (float) Math.toRadians(ElytraFlight.mc.player.rotationYaw);
                    ElytraFlight.mc.player.motionX -= MathHelper.sin(yaw) * 0.05f;
                    ElytraFlight.mc.player.motionZ += MathHelper.cos(yaw) * 0.05f;
                    break;
                }
                if (!ElytraFlight.mc.gameSettings.keyBindBack.isKeyDown()) break;
                float yaw = (float) Math.toRadians(ElytraFlight.mc.player.rotationYaw);
                ElytraFlight.mc.player.motionX += MathHelper.sin(yaw) * 0.05f;
                ElytraFlight.mc.player.motionZ -= MathHelper.cos(yaw) * 0.05f;
                break;
            }
            case FLY: {
                ElytraFlight.mc.player.capabilities.isFlying = true;
            }
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (ElytraFlight.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA) {
            return;
        }
        switch (event.getStage()) {
            case 0: {
                if (this.disableInLiquid.getValue().booleanValue() && (ElytraFlight.mc.player.isInWater() || ElytraFlight.mc.player.isInLava())) {
                    if (ElytraFlight.mc.player.isElytraFlying()) {
                        mc.getConnection().sendPacket(new CPacketEntityAction(ElytraFlight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    }
                    return;
                }
                if (this.autoStart.getValue().booleanValue() && ElytraFlight.mc.gameSettings.keyBindJump.isKeyDown() && !ElytraFlight.mc.player.isElytraFlying() && ElytraFlight.mc.player.motionY < 0.0 && this.timer.passedMs(250L)) {
                    mc.getConnection().sendPacket(new CPacketEntityAction(ElytraFlight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    this.timer.reset();
                }
                if (this.mode.getValue() == Mode.BETTER) {
                    double[] dir = MathUtil.directionSpeed(this.devMode.getValue() == 1 ? (double) this.speed.getValue().floatValue() : (double) this.hSpeed.getValue().floatValue());
                    switch (this.devMode.getValue()) {
                        case 1: {
                            ElytraFlight.mc.player.setVelocity(0.0, 0.0, 0.0);
                            ElytraFlight.mc.player.jumpMovementFactor = this.speed.getValue().floatValue();
                            if (ElytraFlight.mc.gameSettings.keyBindJump.isKeyDown()) {
                                ElytraFlight.mc.player.motionY += this.speed.getValue().floatValue();
                            }
                            if (ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                ElytraFlight.mc.player.motionY -= this.speed.getValue().floatValue();
                            }
                            if (ElytraFlight.mc.player.movementInput.moveStrafe != 0.0f || ElytraFlight.mc.player.movementInput.moveForward != 0.0f) {
                                ElytraFlight.mc.player.motionX = dir[0];
                                ElytraFlight.mc.player.motionZ = dir[1];
                                break;
                            }
                            ElytraFlight.mc.player.motionX = 0.0;
                            ElytraFlight.mc.player.motionZ = 0.0;
                            break;
                        }
                        case 2: {
                            if (ElytraFlight.mc.player.isElytraFlying()) {
                                if (this.flyHeight == null) {
                                    this.flyHeight = ElytraFlight.mc.player.posY;
                                }
                            } else {
                                this.flyHeight = null;
                                return;
                            }
                            if (this.noKick.getValue().booleanValue()) {
                                this.flyHeight = this.flyHeight - (double) this.glide.getValue().floatValue();
                            }
                            this.posX = 0.0;
                            this.posZ = 0.0;
                            if (ElytraFlight.mc.player.movementInput.moveStrafe != 0.0f || ElytraFlight.mc.player.movementInput.moveForward != 0.0f) {
                                this.posX = dir[0];
                                this.posZ = dir[1];
                            }
                            if (ElytraFlight.mc.gameSettings.keyBindJump.isKeyDown()) {
                                this.flyHeight = ElytraFlight.mc.player.posY + (double) this.vSpeed.getValue().floatValue();
                            }
                            if (ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                this.flyHeight = ElytraFlight.mc.player.posY - (double) this.vSpeed.getValue().floatValue();
                            }
                            ElytraFlight.mc.player.setPosition(ElytraFlight.mc.player.posX + this.posX, this.flyHeight.doubleValue(), ElytraFlight.mc.player.posZ + this.posZ);
                            ElytraFlight.mc.player.setVelocity(0.0, 0.0, 0.0);
                            break;
                        }
                        case 3: {
                            if (ElytraFlight.mc.player.isElytraFlying()) {
                                if (this.flyHeight == null || this.posX == null || this.posX == 0.0 || this.posZ == null || this.posZ == 0.0) {
                                    this.flyHeight = ElytraFlight.mc.player.posY;
                                    this.posX = ElytraFlight.mc.player.posX;
                                    this.posZ = ElytraFlight.mc.player.posZ;
                                }
                            } else {
                                this.flyHeight = null;
                                this.posX = null;
                                this.posZ = null;
                                return;
                            }
                            if (this.noKick.getValue().booleanValue()) {
                                this.flyHeight = this.flyHeight - (double) this.glide.getValue().floatValue();
                            }
                            if (ElytraFlight.mc.player.movementInput.moveStrafe != 0.0f || ElytraFlight.mc.player.movementInput.moveForward != 0.0f) {
                                this.posX = this.posX + dir[0];
                                this.posZ = this.posZ + dir[1];
                            }
                            if (this.allowUp.getValue().booleanValue() && ElytraFlight.mc.gameSettings.keyBindJump.isKeyDown()) {
                                this.flyHeight = ElytraFlight.mc.player.posY + (double) (this.vSpeed.getValue().floatValue() / 10.0f);
                            }
                            if (ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                this.flyHeight = ElytraFlight.mc.player.posY - (double) (this.vSpeed.getValue().floatValue() / 10.0f);
                            }
                            ElytraFlight.mc.player.setPosition(this.posX.doubleValue(), this.flyHeight.doubleValue(), this.posZ.doubleValue());
                            ElytraFlight.mc.player.setVelocity(0.0, 0.0, 0.0);
                        }
                    }
                }
                double rotationYaw = Math.toRadians(ElytraFlight.mc.player.rotationYaw);
                if (ElytraFlight.mc.player.isElytraFlying()) {
                    switch (this.mode.getValue()) {
                        case VANILLA: {
                            float speedScaled = this.speed.getValue().floatValue() * 0.05f;
                            if (ElytraFlight.mc.gameSettings.keyBindJump.isKeyDown()) {
                                ElytraFlight.mc.player.motionY += speedScaled;
                            }
                            if (ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                ElytraFlight.mc.player.motionY -= speedScaled;
                            }
                            if (ElytraFlight.mc.gameSettings.keyBindForward.isKeyDown()) {
                                ElytraFlight.mc.player.motionX -= Math.sin(rotationYaw) * (double) speedScaled;
                                ElytraFlight.mc.player.motionZ += Math.cos(rotationYaw) * (double) speedScaled;
                            }
                            if (!ElytraFlight.mc.gameSettings.keyBindBack.isKeyDown()) break;
                            ElytraFlight.mc.player.motionX += Math.sin(rotationYaw) * (double) speedScaled;
                            ElytraFlight.mc.player.motionZ -= Math.cos(rotationYaw) * (double) speedScaled;
                            break;
                        }
                        case PACKET: {
                            this.freezePlayer(ElytraFlight.mc.player);
                            this.runNoKick(ElytraFlight.mc.player);
                            double[] directionSpeedPacket = MathUtil.directionSpeed(this.speed.getValue().floatValue());
                            if (ElytraFlight.mc.player.movementInput.jump) {
                                ElytraFlight.mc.player.motionY = this.speed.getValue().floatValue();
                            }
                            if (ElytraFlight.mc.player.movementInput.sneak) {
                                ElytraFlight.mc.player.motionY = -this.speed.getValue().floatValue();
                            }
                            if (ElytraFlight.mc.player.movementInput.moveStrafe != 0.0f || ElytraFlight.mc.player.movementInput.moveForward != 0.0f) {
                                ElytraFlight.mc.player.motionX = directionSpeedPacket[0];
                                ElytraFlight.mc.player.motionZ = directionSpeedPacket[1];
                            }
                            mc.getConnection().sendPacket(new CPacketEntityAction(ElytraFlight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                            mc.getConnection().sendPacket(new CPacketEntityAction(ElytraFlight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                            break;
                        }
                        case BYPASS: {
                            if (this.devMode.getValue() != 3) break;
                            if (ElytraFlight.mc.gameSettings.keyBindJump.isKeyDown()) {
                                ElytraFlight.mc.player.motionY = 0.02f;
                            }
                            if (ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                ElytraFlight.mc.player.motionY = -0.2f;
                            }
                            if (ElytraFlight.mc.player.ticksExisted % 8 == 0 && ElytraFlight.mc.player.posY <= 240.0) {
                                ElytraFlight.mc.player.motionY = 0.02f;
                            }
                            ElytraFlight.mc.player.capabilities.isFlying = true;
                            ElytraFlight.mc.player.capabilities.setFlySpeed(0.025f);
                            double[] directionSpeedBypass = MathUtil.directionSpeed(0.52f);
                            if (ElytraFlight.mc.player.movementInput.moveStrafe != 0.0f || ElytraFlight.mc.player.movementInput.moveForward != 0.0f) {
                                ElytraFlight.mc.player.motionX = directionSpeedBypass[0];
                                ElytraFlight.mc.player.motionZ = directionSpeedBypass[1];
                                break;
                            }
                            ElytraFlight.mc.player.motionX = 0.0;
                            ElytraFlight.mc.player.motionZ = 0.0;
                        }
                    }
                }
                if (!this.infiniteDura.getValue().booleanValue()) break;
                ElytraFlight.mc.player.connection.sendPacket(new CPacketEntityAction(ElytraFlight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                break;
            }
            case 1: {
                if (!this.infiniteDura.getValue().booleanValue()) break;
                ElytraFlight.mc.player.connection.sendPacket(new CPacketEntityAction(ElytraFlight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
        }
    }

    private double[] forwardStrafeYaw(double forward, double strafe, double yaw) {
        double[] result = new double[]{forward, strafe, yaw};
        if ((forward != 0.0 || strafe != 0.0) && forward != 0.0) {
            if (strafe > 0.0) {
                result[2] = result[2] + (double) (forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
                result[2] = result[2] + (double) (forward > 0.0 ? 45 : -45);
            }
            result[1] = 0.0;
            if (forward > 0.0) {
                result[0] = 1.0;
            } else if (forward < 0.0) {
                result[0] = -1.0;
            }
        }
        return result;
    }

    private void freezePlayer(EntityPlayer player) {
        player.motionX = 0.0;
        player.motionY = 0.0;
        player.motionZ = 0.0;
    }

    private void runNoKick(EntityPlayer player) {
        if (this.noKick.getValue().booleanValue() && !player.isElytraFlying() && player.ticksExisted % 4 == 0) {
            player.motionY = -0.04f;
        }
    }

    @Override
    public void onDisable() {
        if (ElytraFlight.fullNullCheck() || ElytraFlight.mc.player.capabilities.isCreativeMode) {
            return;
        }
        ElytraFlight.mc.player.capabilities.isFlying = false;
    }

    public enum Mode {
        VANILLA,
        PACKET,
        BOOST,
        FLY,
        BYPASS,
        BETTER,
        OHARE,
        TOOBEE,
        TOOBEEBYPASS

    }
}

