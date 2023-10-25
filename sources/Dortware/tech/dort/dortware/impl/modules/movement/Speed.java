package tech.dort.dortware.impl.modules.movement;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.RandomUtils;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.api.property.SliderUnit;
import tech.dort.dortware.api.property.impl.BooleanValue;
import tech.dort.dortware.api.property.impl.EnumValue;
import tech.dort.dortware.api.property.impl.NumberValue;
import tech.dort.dortware.api.property.impl.interfaces.INameable;
import tech.dort.dortware.impl.events.MovementEvent;
import tech.dort.dortware.impl.events.PacketEvent;
import tech.dort.dortware.impl.events.PlayerMovementUpdateEvent;
import tech.dort.dortware.impl.events.UpdateEvent;
import tech.dort.dortware.impl.events.enums.PacketDirection;
import tech.dort.dortware.impl.modules.combat.KillAura;
import tech.dort.dortware.impl.utils.movement.ACType;
import tech.dort.dortware.impl.utils.movement.MotionUtils;
import tech.dort.dortware.impl.utils.networking.PacketRunnableWrapper;
import tech.dort.dortware.impl.utils.networking.PacketUtil;
import tech.dort.dortware.impl.utils.networking.ServerUtils;
import tech.dort.dortware.impl.utils.time.Stopwatch;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dort, Auth
 */
public class Speed extends Module {

    private final EnumValue<Mode> mode = new EnumValue<>("Mode", this, Mode.values());
    private final List<Mode> ignoreFlagCheckModes = Arrays.asList(Mode.AAC, Mode.VANILLA);
    private final NumberValue speed = new NumberValue("Speed", this, 1, 0.125, 10, SliderUnit.BPT);
    private final BooleanValue flagCheck = new BooleanValue("Flag Check", this, true);
    private boolean lastDistanceReset, reset;
    private double movementSpeed, lastDistance, speedStage;
    private final ArrayDeque<Packet> packets = new ArrayDeque<>();
    private final Stopwatch fakeLagTimer = new Stopwatch();

    public Speed(ModuleData moduleData) {
        super(moduleData);
        register(mode, speed, flagCheck);
    }


    @Subscribe
    public void onUpdate(PlayerMovementUpdateEvent event) {
        if (event.getState() == PlayerMovementUpdateEvent.State.JUMP)
            return;
        switch (mode.getValue()) {
            case HAWK: {
                if (mc.thePlayer.isMovingOnGround()) {
                    mc.thePlayer.jump();
                    movementSpeed = 0.4;
                    return;
                } else {
                    movementSpeed -= 0.01;
                }
                float strafe = event.getStrafe();
                float forward = event.getForward();
                float friction = (float) movementSpeed;
                float var4 = strafe * strafe + forward * forward;

                if (var4 >= 1.0E-4F) {
                    var4 = MathHelper.sqrt_float(var4);

                    if (var4 < 1.0F) {
                        var4 = 1.0F;
                    }

                    var4 = friction / var4;
                    strafe *= var4;
                    forward *= var4;
                    float var5 = MathHelper.sin(mc.thePlayer.rotationYaw * (float) Math.PI / 180.0F);
                    float var6 = MathHelper.cos(mc.thePlayer.rotationYaw * (float) Math.PI / 180.0F);
                    mc.thePlayer.motionX += (strafe * var6 - forward * var5);
                    mc.thePlayer.motionZ += (forward * var6 + strafe * var5);
                }
                MotionUtils.setMotion(Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ));
                break;
            }
            case SHIVA: {
                if (mc.thePlayer.isMovingOnGround()) {
                    mc.thePlayer.jump();
                    if (movementSpeed < 1.4)
                        movementSpeed = 0.56;
                } else {
                    float strafe = event.getStrafe();
                    float forward = event.getForward();
                    float friction = (float) movementSpeed;
                    float var4 = strafe * strafe + forward * forward;

                    if (var4 >= 1.0E-4F) {
                        var4 = friction / Math.min(MathHelper.sqrt_float(var4), 1.0F);
                        strafe *= var4;
                        forward *= var4;
                        float var5 = MathHelper.sin(mc.thePlayer.rotationYaw * (float) Math.PI / 180.0F);
                        float var6 = MathHelper.cos(mc.thePlayer.rotationYaw * (float) Math.PI / 180.0F);
                        mc.thePlayer.motionX = (strafe * var6 - forward * var5);
                        mc.thePlayer.motionZ = (forward * var6 + strafe * var5);
                        movementSpeed *= 0.98;
                    }
                }
                break;
            }
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        Mode mode = this.mode.getValue();
        switch (mode) {
            case YPORT:
                if (!mc.thePlayer.isCollidedHorizontally && mc.thePlayer.isMovingOnGround() && !mc.gameSettings.keyBindJump.getIsKeyPressed() && ServerUtils.onHypixel())
                    event.setPosY(event.getPosY() + (lastDistanceReset ? 0.42F : 0F));
                break;

            case VERUS_FLOAT: {
                mc.thePlayer.cameraYaw = 0.1f;
                if (event.isPre())
                    mc.thePlayer.posY = (int) mc.thePlayer.posY;
                if (speedStage <= 9)
                    event.setOnGround(true);
                break;
            }

            case VERUS_YPORT:
                if (lastDistanceReset) {
                    event.setPosY(event.getPosY() - 0.0625D);
                }
                event.setOnGround(lastDistanceReset);
                break;

            case HYPIXEL_TIMER:
            case HYPIXEL_RANKED:
            case HYPIXEL:
                if (ServerUtils.onHypixel()) {
                    double baseValue = RandomUtils.nextDouble(1.0E-12, 1.0E-7);
                    if (mc.thePlayer.isMovingOnGround()) {
                        baseValue += 0.0025D;
                    } else {
                        baseValue = 0.0025D;
                    }
                    event.setPosY(event.getPosY() + baseValue);
                }
                break;

            case LUNAR:
                if (mc.thePlayer.ticksExisted % 2 != 0 && mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = 0.009;
                }

                event.setOnGround(mc.thePlayer.ticksExisted % 2 == 0);
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.125D, mc.thePlayer.posZ, true));
                break;
        }

        if (event.isPre()) {
            double x = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double z = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            this.lastDistance = (float) Math.hypot(x, z);
        }
    }

    @Subscribe
    public void onMove(MovementEvent event) {
        Mode mode = this.mode.getValue();
        double speed = this.speed.getCastedValue();
        switch (mode) {
            case INVADED: {
                float hDist = (float) MotionUtils.getBaseSpeed(ACType.OLD_AGC) + 0.8f;
                if (mc.thePlayer.isMovingOnGround()) {
                    event.setMotionY(mc.thePlayer.motionY = 0.42f);
                    hDist += 0.6;
                } else {
                    hDist -= 0.66 * (hDist - MotionUtils.getMotion(ACType.OLD_AGC));
                }
                MotionUtils.setMotion(event, hDist);
            }
            break;

            case AAC: {
                float hDist = (float) ((MotionUtils.getBaseSpeed(ACType.AAC) + 0.2f) * 1 + speed / 5.0f);
                if (mc.thePlayer.isMovingOnGround()) {
                    event.setMotionY(mc.thePlayer.motionY = 0.42D);
                    hDist += 0.125;
                } else {
                    hDist -= 0.36 * (hDist - MotionUtils.getMotion(ACType.AAC));
                }
                MotionUtils.setMotion(event, hDist);
            }
            break;

            case MINEPLEX2:
                mc.timer.timerSpeed = 1.0F;
                if (mc.thePlayer.isMovingOnGround()) {
                    mc.timer.timerSpeed = 2.2F;
                    event.setMotionY(mc.thePlayer.motionY = 0.42F);
                    movementSpeed = MotionUtils.getBaseSpeed(ACType.MINEPLEX) * 3.077D;
                    lastDistanceReset = true;
                    MotionUtils.setMotion(event, 0.0D);
                } else {
                    if (lastDistanceReset) {
                        movementSpeed -= 0.361D * (movementSpeed - MotionUtils.getSpeed());
                        lastDistanceReset = false;
                    }
                    movementSpeed = Math.max(movementSpeed - movementSpeed / 159, MotionUtils.getBaseSpeed(ACType.MINEPLEX));
                    MotionUtils.setMotion(event, movementSpeed);
                }
                break;

            case MINEPLEX: {
                mc.timer.timerSpeed = 1.0f;
                if (mc.thePlayer.isMovingOnGround()) {
//                    mc.timer.timerSpeed = 2.48F;
                    if (reset)
                        movementSpeed = 0.8F;
                    else
                        movementSpeed += 0.6F;
                    lastDistanceReset = true;
                    reset = false;
                    event.setMotionY(mc.thePlayer.motionY = 0.42F);
                    MotionUtils.setMotion(event, 0.0001D);
                } else {
                    if (lastDistanceReset)
                        lastDistance = movementSpeed;
                    lastDistanceReset = false;
                    if (movementSpeed <= 0.8F)
                        movementSpeed = lastDistance - 0.01F;
                    else if (movementSpeed < 2.2F)
                        movementSpeed = lastDistance * 0.98F;
                    else
                        movementSpeed = lastDistance * 0.97F;
                    movementSpeed = Math.min(speed + 0.053125F, movementSpeed);
                    movementSpeed = Math.max(movementSpeed, MotionUtils.getSpeed() + 0.163F);
                    MotionUtils.setMotion(event, movementSpeed);
                }
            }
            break;

            case VANILLA:
                MotionUtils.setMotion(event, speed);
                break;

            case VANILLA_HOP:
                if (mc.thePlayer.isMovingOnGround()) {
                    event.setMotionY(mc.thePlayer.motionY = MotionUtils.getMotion(0.42F));
                }
                MotionUtils.setMotion(event, speed);
                break;

            case LUNAR:
                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.ticksExisted % 3 == 0) {
                        movementSpeed = this.speed.getValue();
                    } else {
                        movementSpeed *= 0.96;
                    }

                    if (mc.thePlayer.onGround) {
                        event.setMotionY(mc.thePlayer.motionY = MotionUtils.getMotion(0.42F));
                    }

                    MotionUtils.setMotion(event, movementSpeed);
                }
                break;

            case VERUS_FLOAT:
                if (!mc.thePlayer.isMoving()) {
                    movementSpeed = 0;
                    speedStage = 10;
                    return;
                }
                BlockPos pos = new BlockPos(mc.thePlayer);
                if (mc.theWorld.getBlockState(pos.offsetDown(1)).getBlock() instanceof BlockAir) {
                    speedStage = 10;
                    return;
                }
                double speedPot = MotionUtils.getBaseSpeed(ACType.VERUS) - mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
                if (mc.thePlayer.isMoving() && mc.thePlayer.onGround) {
                    event.setMotionY(mc.thePlayer.motionY = MotionUtils.getMotion(0.42F));
                    movementSpeed = speedPot > 0 ? 0.32 : 0.2873;
                } else {
                    if (speedStage <= 9) {
                        if (speedStage == 1) {
                            movementSpeed = 0.52D;
                        } else if (speedStage < 2) {
                            movementSpeed = 0.6D;
                        } else if (speedPot > 0) {
                            movementSpeed = 0.61D;
                        }

                        if (speedStage == 3) {
                            speedPot += speedPot > 0 ? 0.2D : 0.15D;
                        }

                        if (speedStage == 5 || speedStage == 7) {
                            speedPot += speedPot > 0 ? 0.45D : 0.44D;
                        }

                        if (speedStage == 8 && speedPot > 0) {
                            movementSpeed = 1.45D;
                        }

                        event.setMotionY(mc.thePlayer.motionY = 0.0);
                    } else {
                        speedStage = 0;
                        movementSpeed = speedPot > 0 ? 0.45D : 0.35D;
                    }
                    speedStage++;
                }
                MotionUtils.setMotion(event, movementSpeed + speedPot);
                break;

            case VERUS_YPORT:
                lastDistanceReset = mc.thePlayer.ticksExisted % 6 != 0;
                if (lastDistanceReset) {
                    movementSpeed = MotionUtils.getBaseSpeed(ACType.VERUS) * 2.14F;
                } else {
                    movementSpeed = MotionUtils.getBaseSpeed(ACType.VERUS) + 0.012;
                }
                MotionUtils.setMotion(event, Math.abs(movementSpeed));
                break;

            case VERUS_HOP:
                PacketUtil.sendPacketNoEvent(new C0CPacketInput());
                if (mc.thePlayer.isMovingOnGround()) {
                    event.setMotionY(mc.thePlayer.motionY = MotionUtils.getMotion(0.42F));
                    movementSpeed = MotionUtils.getBaseSpeed(ACType.VERUS) * 4.0D;
                    lastDistanceReset = true;
                } else if (lastDistanceReset) {
                    movementSpeed = MotionUtils.getBaseSpeed(ACType.VERUS) * 3.0D;
                    lastDistanceReset = false;
                }
                MotionUtils.setMotion(event, movementSpeed);
                break;

//            case HYPIXEL2: {
//                Block blockBelow = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).offsetDown()).getBlock();
//                Block blockAbove = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).offsetUp(2)).getBlock();
//                Material material = blockBelow.getMaterial();
//                boolean speedUp = material == Material.ice || material == Material.packedIce;
//                boolean slowDown = !(blockAbove instanceof BlockAir);
//
//                if (mc.thePlayer.isMoving() && !MotionUtils.isOnWater()) {
//                    double baseSpeed = MotionUtils.getBaseSpeed(ACType.HYPIXEL);
//                    if (mc.thePlayer.onGround) {
//                        event.setMotionY(mc.thePlayer.motionY = MotionUtils.getMotion(ACType.NCP));
//                        baseSpeed = slowDown ? 0 : Math.max(MotionUtils.getBaseSpeed(ACType.HYPIXEL) * 2.149F, movementSpeed);
//                        movementSpeed *= slowDown ? 0 : (speedUp ? 2.5D : 1.5D);
//                        movementSpeed = Math.max(baseSpeed, movementSpeed);
//                        lastDistanceReset = true;
//                    } else if (lastDistanceReset) {
//                        Potion potion = Potion.moveSpeed;
//                        double diff = 0.66;
//                        if (mc.thePlayer.isPotionActive(potion.id)) {
//                            diff -= (mc.thePlayer.getActivePotionEffect(potion).getAmplifier() + 1) * 0.0225D;
//                        }
//                        movementSpeed -= diff * (movementSpeed - baseSpeed);
//                        lastDistanceReset = false;
//                    } else {
//                        movementSpeed = lastDistance - lastDistance / 159;
//                    }
//
//                    MotionUtils.setMotion(event, Math.max(movementSpeed, baseSpeed));
//                } else {
//                    movementSpeed = 0;
//                }
//            }
//            break;

            case HYPIXEL: {
                Block blockBelow = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).offsetDown()).getBlock();
                Block blockAbove = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).offsetUp(2)).getBlock();
                Material material = blockBelow.getMaterial();
                boolean speedUp = material == Material.ice || material == Material.packedIce;
                boolean slowDown = !(blockAbove instanceof BlockAir);
                if (mc.thePlayer.isMoving() && !MotionUtils.isOnWater()) {
                    double baseSpeed = MotionUtils.getBaseSpeed(ACType.HYPIXEL);
                    if (mc.thePlayer.onGround) {
                        event.setMotionY(mc.thePlayer.motionY = MotionUtils.getMotion(ACType.NCP));
                        baseSpeed = slowDown ? 0 : Math.max(MotionUtils.getBaseSpeed(ACType.HYPIXEL) * 2.15F, movementSpeed);
                        movementSpeed *= slowDown ? 0 : (speedUp ? 2.5D : 1.6D);
                        lastDistanceReset = true;
                    } else if (lastDistanceReset) {
                        movementSpeed -= 0.76D * (movementSpeed - baseSpeed);
                        lastDistanceReset = false;
                        reset = true;
                    } else {
                        movementSpeed = lastDistance - lastDistance / 159;
                    }

                    MotionUtils.setMotion(event, Math.max(movementSpeed, baseSpeed));
                } else {
                    movementSpeed = 0;
                }
            }
            break;

            case HYPIXEL_TIMER: {
                Block blockBelow = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).offsetDown()).getBlock();
                Block blockAbove = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).offsetUp(2)).getBlock();
                Material material = blockBelow.getMaterial();
                boolean speedUp = material == Material.ice || material == Material.packedIce;
                boolean slowDown = !(blockAbove instanceof BlockAir);
                if (mc.thePlayer.isMoving() && !MotionUtils.isOnWater()) {
                    double baseSpeed = MotionUtils.getBaseSpeed(ACType.HYPIXEL);
                    if (mc.thePlayer.onGround) {
                        event.setMotionY(mc.thePlayer.motionY = MotionUtils.getMotion(ACType.NCP));
                        baseSpeed = slowDown ? 0 : Math.max(MotionUtils.getBaseSpeed(ACType.HYPIXEL) * 2.15F, movementSpeed);
                        movementSpeed *= slowDown ? 0 : (speedUp ? 2.5D : 1.6D);
                        lastDistanceReset = true;
                        mc.timer.timerSpeed = 1.0F;
                    } else if (lastDistanceReset) {
                        mc.timer.timerSpeed = 1.1F;
                        movementSpeed -= 0.76D * (movementSpeed - baseSpeed);
                        lastDistanceReset = false;
                        reset = true;
                    } else {
                        movementSpeed = lastDistance - lastDistance / 159;
                    }

                    MotionUtils.setMotion(event, Math.max(movementSpeed, baseSpeed));
                } else {
                    mc.timer.timerSpeed = 1.0F;
                    movementSpeed = 0;
                }
            }
            break;

            case HYPIXEL_RANKED: {
                Block blockBelow = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).offsetDown()).getBlock();
                Block blockAbove = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).offsetUp(2)).getBlock();
                Material material = blockBelow.getMaterial();
                boolean speedUp = material == Material.ice || material == Material.packedIce;
                boolean slowDown = !(blockAbove instanceof BlockAir);
                if (mc.thePlayer.isMoving() && !MotionUtils.isOnWater()) {
                    double baseSpeed = MotionUtils.getBaseSpeed(ACType.HYPIXEL_RANKED);
                    if (mc.thePlayer.onGround) {
                        event.setMotionY(mc.thePlayer.motionY = MotionUtils.getMotion(ACType.NCP));
                        baseSpeed = slowDown ? 0 : Math.max(MotionUtils.getBaseSpeed(ACType.HYPIXEL_RANKED) * 2.15F, movementSpeed);
                        movementSpeed *= slowDown ? 0 : (speedUp ? 2.5D : 1.6D);
                        lastDistanceReset = true;
                    } else if (lastDistanceReset) {
                        movementSpeed -= 0.76D * (movementSpeed - baseSpeed);
                        lastDistanceReset = false;
                        reset = true;
                    } else {
                        movementSpeed = lastDistance - lastDistance / 159;
                    }

                    MotionUtils.setMotion(event, Math.max(movementSpeed, baseSpeed));
                } else {
                    movementSpeed = 0;
                }
            }
            break;

            case YPORT: {
                if (KillAura.currentTarget != null) {
                    Block blockBelow = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).offsetDown()).getBlock();
                    Block blockAbove = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).offsetUp(2)).getBlock();
                    Material material = blockBelow.getMaterial();
                    boolean speedUp = material == Material.ice || material == Material.packedIce;
                    boolean slowDown = !(blockAbove instanceof BlockAir);
                    if (mc.thePlayer.isMoving() && !MotionUtils.isOnWater()) {
                        double baseSpeed = MotionUtils.getBaseSpeed(ACType.HYPIXEL);
                        if (mc.thePlayer.onGround) {
                            event.setMotionY(mc.thePlayer.motionY = MotionUtils.getMotion(ACType.NCP));
                            baseSpeed = slowDown ? 0 : Math.max(MotionUtils.getBaseSpeed(ACType.HYPIXEL) * 2.15F, movementSpeed);
                            movementSpeed *= slowDown ? 0 : (speedUp ? 2.5D : 1.6D);
                            lastDistanceReset = true;
                        } else if (lastDistanceReset) {
                            movementSpeed -= 0.76D * (movementSpeed - baseSpeed);
                            lastDistanceReset = false;
                            reset = true;
                        } else {
                            movementSpeed = lastDistance - lastDistance / 159;
                        }

                        MotionUtils.setMotion(event, Math.max(movementSpeed, baseSpeed));
                    } else {
                        movementSpeed = 0;
                    }
                    return;
                }

                if (mc.thePlayer.isMovingOnGround()) {
                    MotionUtils.setMotion(event, 0);
                    MotionUtils.sendMotion(speed / 2.5D, MotionUtils.getBaseSpeed(ACType.HYPIXEL));
                }
                break;
            }

            case VELT: {
                if (mc.thePlayer.isMovingOnGround()) {
                    MotionUtils.setMotion(event, 0);
                    MotionUtils.sendMotion(speed / 2.5D, MotionUtils.getBaseSpeed(ACType.HYPIXEL));
                }
            }
            break;
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        switch (mode.getValue()) {
            case AAC:
                PacketRunnableWrapper.aacExploit.accept(event);
                break;

            case VELT:
                if (event.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer packetPlayer = event.getPacket();
                    if (fakeLagTimer.timeElapsed(250L)) {
                        while (!packets.isEmpty()) {
                            PacketUtil.sendPacketNoEvent(packets.poll());
                        }
                        fakeLagTimer.resetTime();
                    }
                    event.setCancelled(true);
                    packets.add(packetPlayer);
                }
                break;
        }

        if (event.getPacketDirection() == PacketDirection.INBOUND) {
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                if (!ignoreFlagCheckModes.contains(mode.getValue()) && flagCheck.getValue()) {
                    this.toggle();
                }
            }
        }
    }

    @Override
    public void onEnable() {
        packets.clear();
        fakeLagTimer.resetTime();
        reset = true;
        lastDistance = 0;
        movementSpeed = 0;
        speedStage = 9;
    }

    @Override
    public void onDisable() {
        switch (mode.getValue()) {
            case AAC:
                PacketRunnableWrapper.preventLagback();
                break;

            case VELT:
                for (Packet packet : packets) {
                    PacketUtil.sendPacketNoEvent(packet);
                }
                packets.clear();
                break;
        }
    }

    @Override
    public String getSuffix() {
        String mode = this.mode.getValue().getDisplayName();
        return " \2477" + mode;
    }

    public enum Mode implements INameable {
        VANILLA("Vanilla"), VANILLA_HOP("Vanilla Hop"), SHIVA("Shiva"), HAWK("Hawk"), INVADED("Invaded Lands"),
        HYPIXEL("Hypixel"), HYPIXEL_TIMER("Hypixel Timer"), HYPIXEL_RANKED("Hypixel Ranked"), YPORT("Hypixel Ground"), MINEPLEX("Mineplex"),
        MINEPLEX2("Mineplex 2"), AAC("AAC"), VERUS_FLOAT("Verus Float"),
        VERUS_HOP("Verus Hop"), VERUS_YPORT("Verus yPort"), LUNAR("Lunar"), VELT("Timer Blink");
        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }
}