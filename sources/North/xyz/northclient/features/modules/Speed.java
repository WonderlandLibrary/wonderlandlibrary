package xyz.northclient.features.modules;

import java.util.Random;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.server.network.NetHandlerLoginServer;
import org.lwjgl.input.Keyboard;
import xyz.northclient.draggable.impl.Watermark;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.MotionEvent;
import xyz.northclient.features.events.SendPacketEvent;
import xyz.northclient.features.values.ModeValue;
import xyz.northclient.util.MoveUtil;
import java.util.Timer;
import java.util.TimerTask;


@ModuleInfo(name = "Speed", description = "", category = Category.MOVEMENT,
        keyCode = Keyboard.KEY_V)
public class Speed extends AbstractModule {
    //"Vanilla", "New NCP", "WatchDog", "Vulcan", "Intave"
    public ModeValue speedMode =
            new ModeValue("Mode", this)
                    .add(new Watermark.StringMode("Legit", this))
                    .add(new Watermark.StringMode("WatchDog", this))
                    .add(new Watermark.StringMode("Vulcan", this))
                    .add(new Watermark.StringMode("NCP", this))
                    .add(new Watermark.StringMode("Intave", this))
                    .add(new Watermark.StringMode("Watchdog Smooth", this))
                    .add(new Watermark.StringMode("Watchdog TPHop", this))
                    .add(new Watermark.StringMode("BlocksMC Lowhop", this))
                    .add(new Watermark.StringMode("BlocksMC Fast", this))
                    .add(new Watermark.StringMode("Strafe", this))
                    .add(new Watermark.StringMode("MMC", this))

                    .setDefault("Legit");

    public ModeValue vulcanMode =
            new ModeValue("Vulcan Mode", this)
                    .add(new Watermark.StringMode("Ground", this))
                    .add(new Watermark.StringMode("YPort", this))
                    .setDefault("Ground");

    private boolean wasOnGround;
    private int stage;
    public double y;

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
    }

    @Override
    public void onEnable() {
        mc.timer.timerSpeed = 1.0f;
        y = 0;
    }

    @EventLink
    public void onMove(MotionEvent event) {
        setSuffix(speedMode.get().getName());

        switch (speedMode.get().getName()) {
            case "WatchDog":
                if (MoveUtil.isMoving()) {
                    final double baseMoveSpeed = MoveUtil.defaultSpeed();
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.42;
                        mc.thePlayer.jump();
                        MoveUtil.setSpeed(0.46);
                    }
                    //                    if (mc.thePlayer.hurtTime > 1) {
                    //                        mc.timer.timerSpeed = 1.09995f;
                    //                    }
                }
                break;
            case "Watchdog Smooth":
                if (MoveUtil.isMoving()) {
                    final double baseMoveSpeed = MoveUtil.defaultSpeed();

                    if (MoveUtil.isMoving()) {
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.motionY = 0.42;
                            //mc.thePlayer.jump();
                            MoveUtil.setSpeed(0.46);
                            if (mc.thePlayer.onGround)
                                mc.timer.timerSpeed = 0.9f; // Goofy ahh timer na ziemi
                        } else {
                            mc.timer.timerSpeed = 1.1f;
                            if (mc.thePlayer.isSwingInProgress || mc.thePlayer.hurtTime > 0) {
                                mc.timer.timerSpeed = 1.0f;
                            }
                        }
                    }
                }
            case "MMC":
                if (MoveUtil.isMoving()) {
                    final double baseMoveSpeed = MoveUtil.defaultSpeed();

                    if (MoveUtil.isMoving()) {
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.motionY = 0.42;
                            //mc.thePlayer.jump();
                            MoveUtil.setSpeed(0.46);
                        }
                    }
                }
            break;
            case "Watchdog TPHop":
                if (MoveUtil.isMoving()) {
                    final double baseMoveSpeed = MoveUtil.defaultSpeed();

                    if (MoveUtil.isMoving()) {
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.jump();
                            mc.timer.timerSpeed = 0.1f; // Goofy ahh timer na ziemi
                        } else {
                            mc.timer.timerSpeed = 4.0f; // fast as fuck timer w powietrzu
                            if (mc.thePlayer.hurtTime > 0 || mc.thePlayer.isSneaking()
                                    || mc.thePlayer.isDead)
                                mc.timer.timerSpeed = 1.0f;
                            if (mc.thePlayer.moveStrafing == 1)
                                mc.timer.timerSpeed = 0.5f;
                            if (mc.thePlayer.swingProgress == 1)
                                mc.timer.timerSpeed = 1.0f;
                        }
                    }
                }
                break;
            case "Vanilla":
                if (MoveUtil.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    }
                }
                break;
            case "Vulcan":
                switch (vulcanMode.get().getName()) {
                    case "Ground":
                        if (mc.thePlayer.isCollidedHorizontally) {
                            return;
                        }

                        if (MoveUtil.isMoving()) {
                            if (mc.thePlayer.onGround) {
                                mc.timer.timerSpeed =
                                        mc.thePlayer.ticksExisted % 10 == 0 ? 2.7f : 2.2f;

                                y = 0.01;

                                mc.thePlayer.motionY = 0.01;
                                MoveUtil.strafe(mc.thePlayer.isPotionActive(Potion.moveSpeed)
                                        ? mc.thePlayer.getActivePotionEffect(Potion.moveSpeed)
                                        .getAmplifier()
                                        == 0
                                        ? 0.53f
                                        : 0.594f
                                        : 0.48f);
                            } else {
                                mc.timer.timerSpeed = 0.65f;

                                if (y == .01) {
                                    MoveUtil.strafe(mc.thePlayer.isPotionActive(Potion.moveSpeed)
                                            ? mc.thePlayer.getActivePotionEffect(Potion.moveSpeed)
                                            .getAmplifier()
                                            == 0
                                            ? MoveUtil.getBaseMoveSpeed() * 1.08f
                                            : MoveUtil.getBaseMoveSpeed() * 1.115F
                                            : MoveUtil.getBaseMoveSpeed() * 1.04F);
                                    y = 0;
                                }
                            }
                        }

                        if (mc.thePlayer.fallDistance > 1)
                            mc.timer.timerSpeed = 1f;
                        break;
                    case "YPort":
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.jump();
                            MoveUtil.strafe(0.41f);
                        }

                        if (mc.thePlayer.offGroundTicks == 4) {
                            mc.thePlayer.motionY = -0.1;
                        }
                        if (mc.thePlayer.offGroundTicks == 6) {
                            mc.thePlayer.motionY = -0.44;
                        }
                        break;
                }
                break;
            case "Intave":
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    event.setYaw(mc.thePlayer.rotationYaw);
                }

                mc.thePlayer.jumpTicks = 0;
                mc.timer.timerSpeed = 1.1f;
                break;
            case "NCP":
                MoveUtil.strafe();
                if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                    mc.thePlayer.motionY = 0.42;
                    MoveUtil.strafe(0.31f);
                }
                if (MoveUtil.isMoving()) {
                    mc.timer.timerSpeed = (float) getRandomInRange(1.0, 1.05);
                }
                if (mc.thePlayer.hurtTime > 1) {
                    MoveUtil.setSpeed(0.49);
                }
                break;
            case "BlocksMC Lowhop":
                MoveUtil.strafe();
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = -0.0784000015258789;
                    MoveUtil.strafe(0.31f);

                    if (mc.thePlayer.offGroundTicks == 4 && mc.thePlayer.hurtTime == 0) {
                        mc.thePlayer.motionY = -0.0784000015258789;
                    }
                }
                break;
            case "Strafe":
                MoveUtil.strafe();
                if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                    mc.thePlayer.motionY = 0.42;
                    MoveUtil.strafe(0.31f);
                }
                break;
            case "BlocksMC Fast":
                MoveUtil.strafe();
                if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                    mc.thePlayer.motionY = 0.42;
                    MoveUtil.strafe(0.31f);
                    MoveUtil.setSpeed(0.46);
                }
                if (mc.thePlayer.onGround) {
                    mc.timer.timerSpeed = 0.9f;
                } else {
                    mc.timer.timerSpeed = 1.1f;
                }
                break;
        }}

    @EventLink
    public void onPacket(SendPacketEvent event) {
        if (mc.thePlayer == null)
            return;

        switch (speedMode.get().getName()) {
            case "Vulcan":
                switch (vulcanMode.get().getName()) {
                    case "Ground":
                        if (mc.thePlayer.isCollidedHorizontally)
                            return;

                        if (KillAura.target != null)
                            return;

                        if (MoveUtil.isMoving()) {
                            if (event.getPacket() instanceof C03PacketPlayer) {
                                ((C03PacketPlayer) event.getPacket()).y = mc.thePlayer.posY + y;
                            }
                        }
                        break;
                }
                break;
        }
    }


    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public static double getRandomInRange(double min, double max) {
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        double shifted = scaled + min;
        return shifted;
    }

    public void onLivingAttack() {
        setTimerSpeedForDuration(1.0f, 3);
    }

    public void onLivingDamage() {
        setTimerSpeedForDuration(1.0f, 3);
    }

    private void setTimerSpeedForDuration(float v, int i) {
    }
}