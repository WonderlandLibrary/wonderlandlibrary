package best.azura.client.impl.module.impl.movement;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventCancelSprintCollision;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventUpdate;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.NumberValue;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@ModuleInfo(name = "Step", description = "Automatically step up blocks", category = Category.MOVEMENT)
public class Step extends Module {

    private final ModeValue mode = new ModeValue("Mode", "Change the mode of step bypass", "NCP", "NCP", "Watchdog", "Spartan B439", "Spartan B456", "Spartan Phase 474", "Verus", "Vanilla", "Watchdog Edit", "Minemora");
    private final NumberValue<Float> stepHeight = new NumberValue<>("Step Height", "Modify how high you want to step", 1.0F, 0.1F, 0.5F, 5.0F);
    private final NumberValue<Float> timer = new NumberValue<>("Timer", "Modify how high you want to step", 1.0F, 0.1F, 0.1F, 1.0F);
    private final NumberValue<Long> delayValue = new NumberValue<>("Delay", "Delay between stepping", 67L, 5L, 0L, 500L);
    private final BooleanValue speed = new BooleanValue("Speed", "Step while speeding", false);
    private final BooleanValue safe = new BooleanValue("Safe", "Cap the spartan B456 step height at 2.5 to prevent auto bans", () -> mode.getObject().equals("Spartan B456"), false);
    private final DelayUtil delay = new DelayUtil();
    private long lastLagBackTime;
    private int ticks;

    private final double[] STEP_VALUES = new double[] {
            0.33319999363422365,
            0.24813599859094576,
            0.16477328182606651,
            0.08307781780646721,
            0.0030162615090425808};

    private boolean shouldStep() {
        if (!mc.thePlayer.isCollidedHorizontally) return false;
        boolean empty = true;
        for (double d = 0; d < 0.2; d += 0.1) {
            double x = -Math.sin(mc.thePlayer.getDirection()) * d;
            double z = Math.cos(mc.thePlayer.getDirection()) * d;
            for (double d1 = 0; d1 < calculateHeight(); d1 += 0.1) {
                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
                        mc.thePlayer.getEntityBoundingBox().offset(x, d1, z)).isEmpty()) empty = false;
                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
                        mc.thePlayer.getEntityBoundingBox().offset(0, d1, 0)).isEmpty()) empty = true;
            }
        }
        return mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !empty &&
                mc.thePlayer.isMoving() && !(mc.thePlayer.isInWater() || mc.thePlayer.isInLava());
    }

    private double calculateHeight() {
        double x = -Math.sin(mc.thePlayer.getDirection()) * 0.4;
        double z = Math.cos(mc.thePlayer.getDirection()) * 0.4;
        double height = 0;
        for (double i = 0.5; i <= stepHeight.getObject() + 0.1; i += 0.1) {
            boolean step = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
                    mc.thePlayer.getEntityBoundingBox().offset(x, i, z)).isEmpty();
            if (step) {
                height = i;
                break;
            }
        }
        if (height <= 0.6) return 0;
        if (System.currentTimeMillis() - lastLagBackTime < 1000 && height > 1.1) return 0;
        return height;
    }

    @EventHandler
    public Listener<EventReceivedPacket> eventReceivedPacketListener = e -> {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) lastLagBackTime = System.currentTimeMillis();
    };

    @EventHandler
    public Listener<EventMotion> eventMotionListener = e -> {
        setSuffix(mode.getObject());
        switch (mode.getObject()) {
            case "Verus":
                if (!e.isPre()) break;
                if ((mc.thePlayer.isCollidedHorizontally || mc.gameSettings.keyBindSneak.pressed) && ticks == 0) {
                    ticks = 2;
                    mc.thePlayer.jump();
                    mc.thePlayer.motionY = 0.5f;
                    e.onGround = true;
                    mc.thePlayer.cameraPitch = -1.25573646f;
                }
                if (ticks > 0) ticks--;
                break;
            case "Watchdog":
                //fix for noslow bypass
                if (!e.isPre()) break;
                if (mc.thePlayer.isCollidedHorizontally
                        && mc.thePlayer.onGround) {
                    e.y = mc.thePlayer.posY;
                    e.onGround = true;
                }
                break;
            case "Watchdog Edit":
                if (!e.isPre()) break;
                if (mc.thePlayer.isCollidedHorizontally
                        && mc.thePlayer.onGround) {
                    e.y = mc.thePlayer.posY;
                    e.onGround = true;
                }
                stepHeight.setObject(Math.min(1.5f, stepHeight.getObject()));
                if ((shouldStep() || ticks > 0 && ticks < 5) && delay.hasReached(Math.max(50, delayValue.getObject()))) {
                    if (Client.INSTANCE.getModuleManager().getModule(Speed.class).isEnabled() && !speed.getObject()) return;
                    if (mc.gameSettings.keyBindJump.pressed) return;
                    final double height = calculateHeight();
                    switch (ticks) {
                        case 0:
                            e.y += 0.42f;
                            ticks++;
                            break;
                        case 1:
                            e.y += 0.753f;
                            ticks++;
                            break;
                        case 2:
                            if (height <= 1.1) {
                                e.y += height;
                                for (double d : STEP_VALUES)
                                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + d * height + 0.05, mc.thePlayer.posZ);
                                delay.reset();
                                break;
                            }
                            e.y += 0.753f + 0.24813599859;
                            ticks++;
                            break;
                        case 3:
                            e.y += 0.753f + 0.24813599859 - 0.015555072702198913;
                            ticks++;
                            break;
                        case 4:
                            e.y += 0.753f + 0.24813599859 + 0.17078707721880448;
                            for (double d : STEP_VALUES)
                                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + d * height + 0.05, mc.thePlayer.posZ);
                            ticks++;
                            delay.reset();
                            break;
                    }
                    mc.gameSettings.keyBindJump.pressed = false;
                    mc.thePlayer.posY = e.y;
                    if (ticks == 4) mc.thePlayer.posY += 0.015555072702198913 + 0.17078707721880448;
                    if (ticks == 5) mc.thePlayer.posY += 0.015555072702198913 + 0.17078707721880448 + 0.17078707721880448;
                } else {
                    mc.thePlayer.stepHeight = 0.5f;
                    if (ticks > 1) {
                        ticks = 0;
                        mc.timer.timerSpeed = 1.0f;
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.lastTickPosY, mc.thePlayer.posZ);
                    }
                }
                break;
        }
    };

    @EventHandler
    public Listener<EventCancelSprintCollision> eventCancelSprintCollisionListener = e -> {
        switch (mode.getObject()) {
            case "NCP":
            case "Watchdog":
            case "Watchdog Edit":
            case "Vanilla":
                e.cancelSprint = shouldStep() && (!Client.INSTANCE.getModuleManager().getModule(Speed.class).isEnabled() || speed.getObject()) && !mc.gameSettings.keyBindJump.pressed;
                break;
        }
    };

    @EventHandler
    public Listener<EventUpdate> eventUpdateListener = e -> {
        //System.out.println(mc.thePlayer.posY - (int) mc.thePlayer.posY + " | " + mc.thePlayer.onGround + " | " + mc.thePlayer.motionY);
        double height = calculateHeight();
        switch (mode.getObject()) {
            case "Watchdog":
                stepHeight.setObject(Math.min(1.5f, stepHeight.getObject()));
                if (shouldStep() && delay.hasReached(delayValue.getObject())) {
                    if (Client.INSTANCE.getModuleManager().getModule(Speed.class).isEnabled() && !speed.getObject()) return;
                    if (mc.gameSettings.keyBindJump.pressed) return;
                    mc.timer.timerSpeed = timer.getObject();
                    if (ticks == 0) {
                        mc.timer.timerSpeed = timer.getObject();
                        ticks = 3;
                        MovementUtil.spoof(0.42f, false);
                        MovementUtil.spoof(0.753f, false);
                        if (height > 1) {
                            MovementUtil.spoof(0.753f + 0.24813599859, false);
                            MovementUtil.spoof(0.753f + 0.24813599859 - 0.015555072702198913, false);
                            MovementUtil.spoof(0.753f + 0.24813599859 + 0.17078707721880448, false);
                        }
                        for (double d : STEP_VALUES) {
                            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + d * height + 0.05, mc.thePlayer.posZ);
                        }
                    }
                    ticks++;
                    delay.reset();
                } else {
                    mc.thePlayer.stepHeight = 0.5f;
                    if (ticks > 1) {
                        ticks = 0;
                        mc.timer.timerSpeed = 1.0f;
                    }
                }
                break;
            case "Vanilla":
                if (shouldStep()) {
                    if (Client.INSTANCE.getModuleManager().getModule(Speed.class).isEnabled() && !speed.getObject()) return;
                    if (mc.gameSettings.keyBindJump.pressed) return;
                    mc.timer.timerSpeed = timer.getObject();
                    if (ticks == 0) {
                        mc.timer.timerSpeed = timer.getObject();
                        ticks = 3;
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + height, mc.thePlayer.posZ);
                    }
                    ticks++;
                    delay.reset();
                } else {
                    mc.thePlayer.stepHeight = 0.5f;
                    if (ticks > 1) {
                        ticks = 0;
                        mc.timer.timerSpeed = 1.0f;
                    }
                }
                break;
            case "Minemora":
                if (shouldStep()) {
                    if (Client.INSTANCE.getModuleManager().getModule(Speed.class).isEnabled() && !speed.getObject()) return;
                    if (mc.gameSettings.keyBindJump.pressed) return;
                    mc.timer.timerSpeed = timer.getObject();
                    if (ticks == 0) {
                        mc.timer.timerSpeed = timer.getObject();
                        ticks = 3;
                        for (int i = 0; i <= height; i++)
                            MovementUtil.spoof(i, true);
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + height, mc.thePlayer.posZ);
                    }
                    ticks++;
                    delay.reset();
                } else {
                    mc.thePlayer.stepHeight = 0.5f;
                    if (ticks > 1) {
                        ticks = 0;
                        mc.timer.timerSpeed = 1.0f;
                    }
                }
                break;
            case "Spartan B439":
                stepHeight.setObject(Math.min(2.0f, stepHeight.getObject()));
                if (shouldStep() && delay.hasReached(delayValue.getObject())) {
                    if (Client.INSTANCE.getModuleManager().getModule(Speed.class).isEnabled() && !speed.getObject()) return;
                    if (mc.gameSettings.keyBindJump.pressed) return;
                    mc.timer.timerSpeed = timer.getObject();
                    if (ticks == (height > 1 ? 1 : 0)) {
                        mc.timer.timerSpeed = timer.getObject();
                        ticks = 3;
                        MovementUtil.spoof(0.42f, false);
                        MovementUtil.spoof(0.753f, false);
                        if (height > 1) {
                            MovementUtil.spoof(0.753f + 0.24813599859, false);
                            MovementUtil.spoof(0.753f + 0.24813599859 + 0.16477328182, false);
                        }
                        if (height > 1.6) {
                            MovementUtil.spoof(0.753f + 0.24813599859 + 0.16477328182 + 0.17078707721880448, false);
                            MovementUtil.spoof(0.753f + 0.24813599859 + 0.16477328182 + 0.17078707721880448 + 0.015555072702206019, false);
                            MovementUtil.spoof(0.753f + 0.24813599859 + 0.16477328182 + 0.17078707721880448 + 0.015555072702206019, true);
                            MovementUtil.spoof(0.753f + 0.24813599859 + 0.16477328182 + 0.17078707721880448 + 0.015555072702206019 + 0.42f, false);
                            MovementUtil.spoof(0.753f + 0.24813599859 + 0.16477328182 + 0.17078707721880448 + 0.015555072702206019 + 0.753f, false);
                        }
                        for (double d : STEP_VALUES) mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + d * height, mc.thePlayer.posZ);
                    }
                    if (ticks == 0 && height > 1) {
                        mc.thePlayer.jump();
                        mc.timer.timerSpeed = 1f;
                    }
                    ticks++;
                    delay.reset();
                } else {
                    mc.thePlayer.stepHeight = 0.5f;
                    if (ticks > 1) {
                        ticks = 0;
                        mc.timer.timerSpeed = 1.0f;
                    }
                }
                break;
            case "Spartan B456":
                if (safe.getObject()) stepHeight.setObject(Math.min(2.5f, stepHeight.getObject()));
                if (shouldStep() && delay.hasReached(delayValue.getObject())) {
                    if (Client.INSTANCE.getModuleManager().getModule(Speed.class).isEnabled() && !speed.getObject())
                        return;
                    if (mc.gameSettings.keyBindJump.pressed) return;
                    mc.timer.timerSpeed = timer.getObject();
                    if (ticks == 1) {
                        mc.timer.timerSpeed = timer.getObject();
                        ticks = 3;
                        if (height > 1) {
                            MovementUtil.spoof(2, true);
                            if (height > 2) MovementUtil.spoof(height, true);
                            MovementUtil.spoof(500, true);
                            MovementUtil.spoof(0, true);
                            for (double d : STEP_VALUES)
                                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + d * height, mc.thePlayer.posZ);
                        } else {
                            MovementUtil.spoof(0.41999998688697815, false);
                            MovementUtil.spoof(0.7531999805212015, false);
                            MovementUtil.spoof(1.001335979112147001, false);
                            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.001335979112147001, mc.thePlayer.posZ);
                        }
                    }
                    if (ticks == 0) {
                        MovementUtil.spoof(0.41999998688697815, false);
                        MovementUtil.spoof(0.7531999805212015, false);
                        MovementUtil.spoof(1.001335979112147001, false);
                        MovementUtil.spoof(0.7850277037892361, false);
                        MovementUtil.spoof(0.48071087633169185, false);
                        MovementUtil.spoof(0.10408037809303661, false);
                        mc.timer.timerSpeed = 1f;
                    }
                    ticks++;
                    delay.reset();
                } else {
                    mc.thePlayer.stepHeight = 0.5f;
                    if (ticks > 1) {
                        ticks = 0;
                        mc.timer.timerSpeed = 1.0f;
                    }
                }
                break;
            case "Spartan Phase 474":
                //stepHeight.setObject(Math.min(3.0f, stepHeight.getObject()));
                if (shouldStep() && delay.hasReached(delayValue.getObject())) {
                    if (Client.INSTANCE.getModuleManager().getModule(Speed.class).isEnabled() && !speed.getObject())
                        return;
                    if (mc.gameSettings.keyBindJump.pressed) return;
                    mc.timer.timerSpeed = timer.getObject();
                    if (height < 1.1) {
                        MovementUtil.spoof(0.41999998688697815, false);
                        MovementUtil.spoof(0.7531999805212015, false);
                        MovementUtil.spoof(1.16610926093821377, false);
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.16610926093821377, mc.thePlayer.posZ);
                        ticks = 3;
                    } else {
                        if (ticks == 1) {
                            mc.timer.timerSpeed = timer.getObject();
                            ticks = 3;
                            for (double d : STEP_VALUES)
                                MovementUtil.spoof(Math.min(3.0, height) * d, true);
                            for (double d : STEP_VALUES)
                                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + d * height + 0.1, mc.thePlayer.posZ);
                            MovementUtil.spoof(-1, true);
                        }
                    }
                    ticks++;
                    delay.reset();
                } else {
                    mc.thePlayer.stepHeight = 0.5f;
                    if (ticks > 1) {
                        ticks = 0;
                        mc.timer.timerSpeed = 1.0f;
                    }
                }
                break;
            case "NCP":
                stepHeight.setObject(Math.min(2, stepHeight.getObject()));
                if (shouldStep() && delay.hasReached(delayValue.getObject())) {
                    if (Client.INSTANCE.getModuleManager().getModule(Speed.class).isEnabled() && !speed.getObject()) return;
                    if (mc.gameSettings.keyBindJump.pressed) return;
                    mc.timer.timerSpeed = timer.getObject();
                    if (ticks == 0) {
                        ticks = 3;
                        mc.timer.timerSpeed = timer.getObject();
                        MovementUtil.spoof(0.42f, false);
                        MovementUtil.spoof(0.753f, false);
                        if (height > 1) {
                            MovementUtil.spoof(0.62f, false);
                            MovementUtil.spoof(0.5f, false);
                            MovementUtil.spoof(0.89f, false);
                            MovementUtil.spoof(1.2f, false);
                            MovementUtil.spoof(1.44f, false);
                            MovementUtil.spoof(1.42f, false);
                        }
                        for (double d : STEP_VALUES) {
                            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + d * height, mc.thePlayer.posZ);
                        }
                    }
                    ticks++;
                    delay.reset();
                } else {
                    mc.thePlayer.stepHeight = 0.5f;
                    if (ticks > 1) {
                        ticks = 0;
                        mc.timer.timerSpeed = 1.0f;
                    }
                }
                break;
        }
    };

}
