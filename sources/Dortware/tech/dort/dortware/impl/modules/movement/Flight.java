package tech.dort.dortware.impl.modules.movement;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;
import org.lwjgl.input.Keyboard;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.api.module.enums.ModuleCategory;
import tech.dort.dortware.api.property.SliderUnit;
import tech.dort.dortware.api.property.impl.BooleanValue;
import tech.dort.dortware.api.property.impl.EnumValue;
import tech.dort.dortware.api.property.impl.NumberValue;
import tech.dort.dortware.api.property.impl.interfaces.INameable;
import tech.dort.dortware.impl.events.BlockCollisionEvent;
import tech.dort.dortware.impl.events.MovementEvent;
import tech.dort.dortware.impl.events.PacketEvent;
import tech.dort.dortware.impl.events.UpdateEvent;
import tech.dort.dortware.impl.utils.movement.ACType;
import tech.dort.dortware.impl.utils.movement.MotionUtils;
import tech.dort.dortware.impl.utils.networking.PacketRunnableWrapper;
import tech.dort.dortware.impl.utils.networking.PacketUtil;

/**
 * @author Dort, Auth
 */

public class Flight extends Module {

    //    private final ArrayDeque<Packet> packets = new ArrayDeque<>();
    private final EnumValue<Mode> mode = new EnumValue<>("Mode", this, Flight.Mode.values());
    public final EnumValue<DamageMode> damageMode = new EnumValue<>("Damage Mode", this, Flight.DamageMode.values());
    //    private final EnumValue<HypixelMode> hypixelMode = new EnumValue<>("Hypixel Mode", this, Flight.HypixelMode.values());
    private final NumberValue speed = new NumberValue("Speed", this, 1, 0.125, 10, SliderUnit.BPT);
    //    private final NumberValue startTimer = new NumberValue("Start Timer", this, 1, 0.1, 5);
//    private final NumberValue timerDuration = new NumberValue("Timer Duration", this, 500, 50, 5000, true);
//    private final NumberValue pulseDelay = new NumberValue("Pulse Delay", this, 150, 50, 5000, true);
//    private final NumberValue timer = new NumberValue("Timer", this, 2, 1, 5);
//    private final BooleanValue dynamicFov = new BooleanValue("Hypixel Dynamic FOV", this, false);
    private final BooleanValue viewBobbing = new BooleanValue("View Bobbing", this, true);
    private final BooleanValue flagCheck = new BooleanValue("Flag Check", this, true);
    private final BooleanValue antiKick = new BooleanValue("Anti Kick", this, false);
    //    private final BooleanValue pulse = new BooleanValue("Blink Pulse", this, false);
//    private final BooleanValue blink = new BooleanValue("Blink", this, false);
    private final BooleanValue stop = new BooleanValue("Stop", this, false);
    private final NumberValue verusSTicks = new NumberValue("Verus Ticks", this, 6, 3, 12, true);
    //    private boolean adjustSpeed;
    private int /*enabledTicks, */verusTicks;
    private double movementSpeed/*, hypixelSpeed*/;
//    private float startFov;
//    private final Stopwatch stopwatch = new Stopwatch();

    public Flight(ModuleData moduleData) {
        super(new ModuleData("Flight", Keyboard.KEY_G, ModuleCategory.MOVEMENT));
        if (moduleData != null) {
            register(mode, damageMode, /*hypixelMode,*/ speed, /*startTimer,*/ verusSTicks, /*timerDuration, pulseDelay, timer, dynamicFov,*/ viewBobbing, flagCheck, antiKick, /*pulse, blink,*/ stop);
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        final Mode mode = this.mode.getValue();
        if (viewBobbing.getValue() && !mode.equals(Mode.CREATIVE)) {
            mc.thePlayer.cameraYaw = 0.1F;
        }

        if (event.isPre()) {
            switch (mode) {
//                case HYPIXEL:
//                    if (mc.thePlayer.ticksExisted % 2 == 0) {
//                        double baseValue = RandomUtils.nextDouble(1.0E-12, 1.0E-7);
//                        if (hypixelMode.getValue() == HypixelMode.SAFE) {
//                            baseValue += 0.001D;
//                        } else {
//                            baseValue += 0.000025D;
//                        }
//                        event.setPosY(event.getPosY() + baseValue);
//                    }
//                    break;

                case FAITHFUL:
                    event.setOnGround(true);
                    break;

                case LUNAR:
                    final MovementInput movementInput = mc.thePlayer.movementInput;
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                        mc.thePlayer.motionY = mc.thePlayer.onGround ? 0.42F : movementInput.jump ? 0.6 : movementInput.sneak ? -0.6 : -0.0001;
                    } else {
                        mc.thePlayer.motionY = 0.009;
                    }
                    event.setOnGround(mc.thePlayer.ticksExisted % 2 == 0);
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.125D, mc.thePlayer.posZ, true));
                    break;

                case REDESKY:
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    } else {
                        mc.thePlayer.jumpMovementFactor = speed.getValue().floatValue() / 8;
                        mc.thePlayer.motionY *= mc.thePlayer.motionY > 0 ? 1.2F : 1.0F;
                        mc.timer.timerSpeed = 1.0F;
                    }

                    if (mc.thePlayer.isMoving()) {
                        mc.timer.timerSpeed = 0.8F;
                        mc.thePlayer.speedInAir = 0.01F;
                    }
                    break;

                case REDESKY2:
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    } else {
                        mc.thePlayer.jumpMovementFactor = speed.getValue().floatValue() / 8;
                        mc.thePlayer.motionY += 0.05F;
                    }
                    break;

                case COLLIDE:
                    if (mc.thePlayer.movementInput.sneak) {
                        MotionUtils.setMotion(0);
                        movementSpeed = 0;
                        verusTicks = verusSTicks.getValue().intValue();
                        return;
                    }

                    if (mc.thePlayer.movementInput.jump) {
                        MotionUtils.setMotion(0);
                        movementSpeed = 0;
                        verusTicks = verusSTicks.getValue().intValue();
                        if (mc.thePlayer.ticksExisted % 2 == 0) {
                            event.setOnGround(true);
                            mc.thePlayer.motionY = MotionUtils.getMotion(0.42F);
                        }
                    } else {
                        MotionUtils.setMotion(MotionUtils.getBaseSpeed(ACType.VERUS) * 2.1);
                        if (verusTicks <= verusSTicks.getValue() && mc.thePlayer.isMoving()) {
                            event.setOnGround(true);
                        }
                    }
                    break;

                case GHOSTLY:
                    PacketUtil.sendPacketNoEvent(new C0CPacketInput());

                    if (mc.thePlayer.movementInput.sneak) {
                        MotionUtils.setMotion(0);
                        return;
                    }

                    if (mc.thePlayer.movementInput.jump) {
                        MotionUtils.setMotion(0);
                        if (mc.thePlayer.ticksExisted % 2 == 0) {
                            event.setOnGround(true);
                            mc.thePlayer.motionY = MotionUtils.getMotion(0.42F);
                        }
                    }
                    break;

                case OLD_AGC:
                    if (!event.isPre())
                        return;
                    if (mc.thePlayer.isCollidedVertically) {
                        mc.thePlayer.motionY = MotionUtils.getMotion(0.42F);
                    }
                    break;
            }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        boolean flag = flagCheck.getValue();
        Mode mode = this.mode.getValue();

        if (mode == Mode.AAC) {
            PacketRunnableWrapper.aacExploit.accept(event);
            return;
        }

        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            if (flag && !mode.equals(Mode.AUSTRALIA_MOMENT))
                toggle();
        }

        if (event.getPacket() instanceof C03PacketPlayer) {
            switch (mode) {
                case HYPIXEL:
                case FAITHFUL:
                case AUSTRALIA_MOMENT:
                    event.setCancelled(true);
                    break;
            }
        }
    }

    @Subscribe
    public void onMove(MovementEvent event) {
        final Mode mode = this.mode.getValue();
        final MovementInput movementInput = mc.thePlayer.movementInput;
        boolean antiKick1 = antiKick.getValue();
        switch (mode) {
            case FAITHFUL:
                MotionUtils.sendMotion(speed.getCastedValue().floatValue(), 0.14D);
                double nigger = speed.getCastedValue().floatValue();
                MotionUtils.setYPacket(movementInput.jump ? nigger : movementInput.sneak ? -nigger : 0, .42f, 0);
                event.setMotionY(0);
                mc.thePlayer.motionY = 0;
                break;

//            case HYPIXEL:
//                if (mc.thePlayer.fallDistance > 3) {
//                    MotionUtils.setMotion(event, this.speed.getValue().floatValue() / 2F);
//                    float newSpeed = this.speed.getValue().floatValue() * 0.425F;
//                    event.setMotionY(movementInput.jump ? newSpeed : movementInput.sneak ? -newSpeed : 0);
//                    mc.thePlayer.motionY = 0;
//                    return;
//                }
//
//                if (mc.thePlayer.isMovingOnGround()) {
//                    event.setMotionY(mc.thePlayer.motionY = MotionUtils.getMotion(0.42F));
//                    if (speed.getValue() > 1.5) {
//                        if (mc.thePlayer.hurtResistantTime == 0) {
//                            MotionUtils.damagePlayer(true);
//                        }
//                        hypixelSpeed = MotionUtils.getSpeed() * 2.15F;
//                        adjustSpeed = true;
//                        mc.timer.timerSpeed = startTimer.getValue().floatValue();
//                    }
//                } else {
//                    event.setMotionY(mc.thePlayer.motionY = 0);
//
//                    if (dynamicFov.getValue()) {
//                        mc.gameSettings.fovSetting = (float) (startFov + hypixelSpeed) + mc.timer.timerSpeed;
//                    }
//
//                    if (adjustSpeed) {
//                        mc.timer.timerSpeed = timer.getValue().floatValue();
//                        hypixelSpeed = 1 + (speed.getValue() / 12.5F);
//                        stopwatch.resetTime();
//                        adjustSpeed = false;
//                    } else {
//                        if (stopwatch.timeElapsed(timerDuration.getValue().longValue())) {
//                            mc.timer.timerSpeed = 1F;
//                        }
//                        hypixelSpeed -= hypixelSpeed / 159;
//                    }
//
//                    if (!mc.thePlayer.isMoving() || mc.thePlayer.isCollidedHorizontally) {
//                        mc.timer.timerSpeed = 1F;
//                        hypixelSpeed = MotionUtils.getBaseSpeed(ACType.HYPIXEL);
//                    }
//                }
//
//                MotionUtils.setMotion(event, Math.max(MotionUtils.getBaseSpeed(ACType.HYPIXEL), hypixelSpeed));
//                break;

            case INVADED:
//                mc.timer.timerSpeed = 0.2f;
                if (mc.thePlayer.isSneaking())
                    return;
                mc.thePlayer.motionY = -0.0315F;
                if (mc.thePlayer.movementInput.jump && mc.thePlayer.ticksExisted % 2 == 0)
                    mc.thePlayer.motionY = .85F;
                MotionUtils.setMotion(mc.thePlayer.ticksExisted % 2 == 0 ? MotionUtils.getSpeed() * 3F : MotionUtils.getSpeed() * 5F);
                break;
            case OLD_AGC:
                if (mc.thePlayer.isMoving() && !movementInput.sneak) {
                    MotionUtils.setMotion(event, 1.242F);
                }
                mc.thePlayer.motionY = 0;
                if (mc.thePlayer.ticksExisted % 2 == 0) {
                    event.setMotionY(movementInput.jump ? 0.8 : movementInput.sneak ? -1.2 : antiKick1 ? -0.05F : 0);
                } else {
                    event.setMotionY(antiKick1 ? -0.05F : 0);
                }
                break;
            case AAC: {
                float speed = this.speed.getCastedValue().floatValue();
                speed /= 5.0;
                MotionUtils.setMotion(event, speed);
                event.setMotionY(movementInput.jump ? speed / 2 : movementInput.sneak ? -speed / 2 : 0);
                break;
            }
            case AUSTRALIA_MOMENT:
                mc.timer.timerSpeed = 0.75f;
                final float speed = this.speed.getCastedValue().floatValue() / 2.25f;
                MotionUtils.setMotion(event, speed);
                event.setMotionY(movementInput.jump ? speed / 3 : movementInput.sneak ? -speed / 3 : 0.42F);
                if (mc.thePlayer.ticksExisted % 2 == 0) {
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX + event.getMotionX(), mc.thePlayer.posY + event.getMotionY() + 0.0001, mc.thePlayer.posZ + event.getMotionZ(), mc.thePlayer.renderYawHead, mc.thePlayer.renderPitchHead, true));
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ + 20.0D, mc.thePlayer.renderYawHead, mc.thePlayer.renderPitchHead, true));
                }
                mc.thePlayer.motionY = 0.0f;
                break;
            case COLLIDE:

                if (mc.thePlayer.movementInput.jump || !mc.thePlayer.isMoving()) {
                    movementSpeed = 0.0;
                    verusTicks = 0;
//                    adjustSpeed = false;
                    return;
                }
                if (mc.thePlayer.isMovingOnGround()) {
                    event.setMotionY(mc.thePlayer.motionY = MotionUtils.getMotion(0.42F));
                    movementSpeed = MotionUtils.getBaseSpeed(ACType.VERUS);
                    MotionUtils.setMotion(event, -.01);
                    return;
                } else {
                    if (verusTicks <= verusSTicks.getValue()) {
                        if (verusTicks >= 1 && verusTicks <= 3) {
                            movementSpeed = 0.49 + MotionUtils.getBaseSpeed(ACType.VERUS) - 0.24;
                        } else {
                            movementSpeed += 0.02;
                        }
                        event.setMotionY(mc.thePlayer.motionY = 0.0F);
                    } else {
                        verusTicks = 0;
                        movementSpeed = MotionUtils.getBaseSpeed(ACType.VERUS);
                    }
                    verusTicks++;
                }
                MotionUtils.setMotion(event, Math.abs(movementSpeed));
                break;
            case HYPIXEL:
                MotionUtils.setMotion(event, this.speed.getCastedValue().floatValue());
                final float newSpeed1 = this.speed.getCastedValue().floatValue() * 0.5F;
                event.setMotionY(movementInput.sneak ? -newSpeed1 : 0);
                mc.thePlayer.motionY = 0;
                break;
            case VANILLA:
                MotionUtils.setMotion(event, this.speed.getCastedValue().floatValue());
                final float newSpeed = this.speed.getCastedValue().floatValue() * 0.5F;
                event.setMotionY(movementInput.jump ? newSpeed : movementInput.sneak ? -newSpeed : antiKick1 ? -0.1535 : 0);
                mc.thePlayer.motionY = 0;
                break;
            case GHOSTLY:
                MotionUtils.setMotion(event, this.speed.getCastedValue().floatValue());
                break;
            case CREATIVE:
                mc.thePlayer.capabilities.setFlySpeed(this.speed.getValue().floatValue() * 0.05F);
                mc.thePlayer.capabilities.isFlying = true;
                break;
            case LUNAR:
                if (mc.thePlayer.ticksExisted % 3 == 0) {
                    movementSpeed = this.speed.getValue();
                } else {
                    movementSpeed *= 0.96;
                }
                event.setMotionY(mc.thePlayer.motionY);
                MotionUtils.setMotion(event, movementSpeed);
                break;
        }
    }

    @Subscribe
    public void onCollision(BlockCollisionEvent event) {
        Mode mode = this.mode.getValue();
        switch (mode) {
            case GHOSTLY:
            case COLLIDE2:
            case COLLIDE:
                if (event.getBlock() instanceof BlockAir) {
                    if (mc.thePlayer.isSneaking())
                        return;
                    double x = event.getX();
                    double y = event.getY();
                    double z = event.getZ();
                    if (y < mc.thePlayer.posY) {
                        event.setAxisAlignedBB(AxisAlignedBB.fromBounds(-15, -1, -15, 15, 1, 15).offset(x, y, z));
                    }
                }
                break;
        }
    }

    @Override
    public void onEnable() {
        mc.thePlayer.capabilities.isFlying = false;
        Mode mode = this.mode.getValue();
//        enabledTicks = 0;
        movementSpeed = 0;
//        hypixelSpeed = 0;
//        adjustSpeed = false;
//        startFov = mc.gameSettings.fovSetting;
        switch (mode) {
            case GHOSTLY:
            case COLLIDE:
            case CREATIVE:
            case AAC:
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = MotionUtils.getMotion(0.42F);
                }
                break;
        }

//        packets.clear();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (stop.getValue() || mode.getValue() == Mode.COLLIDE) {
            MotionUtils.setMotion(0);
        }

        switch (mode.getValue()) {
            case AAC:
                PacketRunnableWrapper.preventLagback();
                break;

            case CREATIVE:
                if (!mc.thePlayer.capabilities.isCreativeMode) {
                    mc.thePlayer.capabilities.allowFlying = false;
                    mc.thePlayer.capabilities.isFlying = false;
                }
                break;

            case HYPIXEL:
                for (int i = 0; i < 3; i++) {
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 3.0D, mc.thePlayer.posZ, true));
                }
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 3.0D, mc.thePlayer.posZ);
                break;

            case REDESKY:
                mc.thePlayer.speedInAir = 0.02F;
                break;
        }
    }

    @Override
    public String getSuffix() {
        return " \2477" + mode.getValue().getDisplayName();
    }

    public enum Mode implements INameable {
        VANILLA("Vanilla"), HYPIXEL("Hypixel"), REDESKY("Redesky"), REDESKY2("Redesky 2"), FAITHFUL("Faithful"), AAC("AAC"), AUSTRALIA_MOMENT("AAC High Ping"), GHOSTLY("Ghostly"), INVADED("Invaded"), OLD_AGC("Old AGC"), /*HYPIXEL("Hypixel"),*/ COLLIDE("Verus"), COLLIDE2("Collide"), CREATIVE("Creative"), LUNAR("Lunar");
        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum HypixelMode implements INameable {
        INFINITE("Infinite"), SAFE("Safe");
        private final String name;

        HypixelMode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum DamageMode implements INameable {
        CARPET("NCP"), LILYPAD("NCP Hunger"), HIGH("High"), JUMP("Jump"), SLAB("Slab");
        private final String name;

        DamageMode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }
}