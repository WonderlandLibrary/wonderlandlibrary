/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.ConcurrentSet
 *  ru.wendoxd.wclassguard.WXFuscator
 */
package org.celestial.client.feature.impl.movement;

import io.netty.util.internal.ConcurrentSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.event.events.impl.packet.EventSendPacket;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.event.events.impl.player.EventPush;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.player.InventoryHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;
import ru.wendoxd.wclassguard.WXFuscator;

public class Flight
extends Feature {
    public static ListSetting flyMode;
    public static NumberSetting speed;
    public static NumberSetting sunriseSpeed;
    public static NumberSetting boostValue;
    public static NumberSetting motionValue;
    private final BooleanSetting startBoost;
    private final BooleanSetting autoDisable;
    private final BooleanSetting silent;
    private final BooleanSetting useTimer;
    private final NumberSetting timerSpeed;
    private final NumberSetting motionSpeed;
    private final NumberSetting delay;
    private final NumberSetting flySpeed;
    private final BooleanSetting blocksRequire;
    public static NumberSetting horizontalStrength;
    public static NumberSetting verticalStrength;
    private final TimerHelper sendTimer = new TimerHelper();
    public static boolean isHurt;
    public static int hurtTicks;
    private int disableTicks;
    private final Set<CPacketPlayer> packetFlyPackets = new ConcurrentSet();

    public Flight() {
        super("Flight", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0432\u0430\u043c \u043b\u0435\u0442\u0430\u0442\u044c \u0431\u0435\u0437 \u043a\u0440\u0435\u0430\u0442\u0438\u0432 \u0440\u0435\u0436\u0438\u043c\u0430", Type.Movement);
        flyMode = new ListSetting("Flight Mode", "Vanilla", () -> true, "Vanilla", "Sunrise New2", "Sunrise New", "WellMore", "WellMore New", "Matrix Pearl", "Matrix Pearl 6.6.0", "Matrix Pearl Test", "Matrix Disabler", "Sunrise Disabler", "Sunrise Drop", "JetMine", "Matrix Glide", "Packet");
        speed = new NumberSetting("Flight Speed", 5.0f, 0.1f, 15.0f, 0.01f, () -> Flight.flyMode.currentMode.equals("Vanilla") || Flight.flyMode.currentMode.equals("WellMore") || Flight.flyMode.currentMode.equals("WellMore New"));
        sunriseSpeed = new NumberSetting("Sunrise Speed", 0.45f, 0.1f, 1.0f, 0.01f, () -> Flight.flyMode.currentMode.equals("Sunrise Disabler"));
        this.startBoost = new BooleanSetting("Start Boost", false, () -> Flight.flyMode.currentMode.equals("Matrix Glide"));
        boostValue = new NumberSetting("Boost Value", 0.7f, 0.05f, 0.8f, 0.01f, () -> Flight.flyMode.currentMode.equals("Matrix Pearl") || Flight.flyMode.currentMode.equals("Matrix Pearl 6.6.0"));
        motionValue = new NumberSetting("Motion Value", 0.3f, 0.1f, 1.0f, 0.01f, () -> Flight.flyMode.currentMode.equals("Matrix Pearl") || Flight.flyMode.currentMode.equals("Matrix Pearl 6.6.0"));
        this.autoDisable = new BooleanSetting("Auto Disable", true, () -> Flight.flyMode.currentMode.equals("Matrix Pearl") || Flight.flyMode.currentMode.equals("Matrix Pearl 6.6.0"));
        this.motionSpeed = new NumberSetting("Motion Speed", 0.25f, 0.01f, 0.5f, 0.01f, () -> Flight.flyMode.currentMode.equals("Packet"));
        this.useTimer = new BooleanSetting("Use Timer", false, () -> Flight.flyMode.currentMode.equals("Packet"));
        this.timerSpeed = new NumberSetting("Timer Speed", 1.05f, 1.01f, 2.0f, 0.01f, () -> Flight.flyMode.currentMode.equals("Packet") && this.useTimer.getCurrentValue());
        this.silent = new BooleanSetting("Silent", true, () -> Flight.flyMode.currentMode.equals("Sunrise Drop"));
        this.blocksRequire = new BooleanSetting("Blocks Require", false, () -> Flight.flyMode.currentMode.equals("Sunrise Drop"));
        this.delay = new NumberSetting("Delay", 4.0f, 1.0f, 10.0f, 1.0f, () -> Flight.flyMode.currentMode.equals("Sunrise Drop") && this.silent.getCurrentValue());
        this.flySpeed = new NumberSetting("Fly Speed", 0.05f, 0.01f, 0.2f, 0.001f, () -> Flight.flyMode.currentMode.equals("Sunrise Drop"));
        horizontalStrength = new NumberSetting("Horizontal Strength", 5.0f, 0.0f, 10.0f, 0.1f, () -> Flight.flyMode.currentMode.equals("JetMine"));
        verticalStrength = new NumberSetting("Vertical Strength", 1.0f, 0.0f, 10.0f, 0.1f, () -> Flight.flyMode.currentMode.equals("JetMine"));
        this.addSettings(flyMode, this.motionSpeed, this.useTimer, this.timerSpeed, boostValue, motionValue, this.autoDisable, this.startBoost, sunriseSpeed, speed, this.silent, this.blocksRequire, this.flySpeed, this.delay, horizontalStrength, verticalStrength);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Flight.mc.player.speedInAir = 0.02f;
        Flight.mc.timer.timerSpeed = 1.0f;
        Flight.mc.player.capabilities.isFlying = false;
        if (flyMode.getOptions().equalsIgnoreCase("WellMore")) {
            Flight.mc.player.motionY = 0.0;
            Flight.mc.player.motionX = 0.0;
            Flight.mc.player.motionZ = 0.0;
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String mode = flyMode.getCurrentMode();
        if (!this.getState()) {
            return;
        }
        if (mode.equalsIgnoreCase("Matrix Glide")) {
            if (this.startBoost.getCurrentValue() && Flight.mc.player.onGround) {
                Flight.mc.player.jump();
                Flight.mc.player.onGround = false;
                Flight.mc.player.motionX *= 1.3;
                Flight.mc.player.motionZ *= 1.3;
            }
            if (!Flight.mc.player.onGround && Flight.mc.player.fallDistance >= 1.0f) {
                Flight.mc.player.motionY *= 0.007;
                Flight.mc.player.fallDistance = 0.0f;
            }
        }
        if (mode.equalsIgnoreCase("Sunrise Disabler")) {
            Flight.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            Flight.mc.player.capabilities.isFlying = false;
            Flight.mc.player.motionY = 0.0;
            if (Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                Flight.mc.player.motionY += (double)sunriseSpeed.getCurrentValue();
            }
            if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                Flight.mc.player.motionY -= (double)sunriseSpeed.getCurrentValue();
            }
            MovementHelper.setSpeed(sunriseSpeed.getCurrentValue());
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = flyMode.getCurrentMode();
        this.setSuffix(flyMode.getCurrentMode());
        if (!this.getState()) {
            return;
        }
        if (mode.equalsIgnoreCase("Wellmore")) {
            if (Flight.mc.player.onGround) {
                Flight.mc.player.jump();
            } else {
                Flight.mc.player.motionX = 0.0;
                Flight.mc.player.motionZ = 0.0;
                Flight.mc.player.motionY = -0.01;
                MovementHelper.setSpeed(speed.getCurrentValue());
                Flight.mc.player.speedInAir = 0.3f;
                if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Flight.mc.player.motionY -= 0.6;
                } else if (Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Flight.mc.player.motionY += 0.6;
                }
            }
        } else if (mode.equalsIgnoreCase("Sunrise New2")) {
            if (Flight.mc.player.onGround) {
                Flight.mc.player.jump();
            }
            if (!Flight.mc.player.onGround) {
                MovementHelper.strafePlayer();
                Flight.mc.timer.timerSpeed = 1.3f;
                Flight.mc.player.onGround = false;
                Flight.mc.player.connection.sendPacket(new CPacketSpectate(UUID.randomUUID()));
                MovementHelper.setSpeed(1.2f);
                if (Flight.mc.player.ticksExisted % 3 == 0) {
                    Flight.mc.player.motionY = 0.1;
                }
            }
        } else if (mode.equalsIgnoreCase("Vanilla")) {
            Flight.mc.player.capabilities.isFlying = true;
            MovementHelper.setSpeed(speed.getCurrentValue());
            if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                Flight.mc.player.motionY -= 0.5;
            } else if (Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                Flight.mc.player.motionY += 0.5;
            }
        } else if (mode.equalsIgnoreCase("Sunrise New")) {
            if (Flight.mc.player.onGround) {
                Flight.mc.player.jump();
            }
            if (!Flight.mc.player.onGround) {
                MovementHelper.strafePlayer();
                if (Flight.mc.player.ticksExisted % 7 == 0) {
                    Flight.mc.player.connection.sendPacket(new CPacketEntityAction(Flight.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                    Flight.mc.player.onGround = false;
                    Flight.mc.player.motionY = 0.6;
                    Flight.mc.player.connection.sendPacket(new CPacketEntityAction(Flight.mc.player, CPacketEntityAction.Action.START_SPRINTING));
                }
            }
        } else if (mode.equalsIgnoreCase("Sunrise Drop")) {
            int blockSlot = InventoryHelper.getBlocksAtHotbar();
            if (blockSlot == -1 && this.blocksRequire.getCurrentValue()) {
                NotificationManager.publicity("Flight", "You need blocks!", 4, NotificationType.ERROR);
                this.toggle();
                return;
            }
            if (blockSlot != -1 && Flight.mc.player.ticksExisted % (int)this.delay.getCurrentValue() == 0 && this.silent.getCurrentValue()) {
                Flight.mc.playerController.windowClick(Flight.mc.player.inventoryContainer.windowId, blockSlot, 0, ClickType.THROW, Flight.mc.player);
            }
            if (Flight.mc.player.onGround) {
                Flight.mc.player.jump();
            } else {
                Flight.mc.player.motionY = 0.0;
                MovementHelper.setSpeed(MovementHelper.getSpeed());
                float value = Flight.mc.player.rotationYaw * ((float)Math.PI / 180);
                if (MovementHelper.isMoving()) {
                    Flight.mc.player.motionX -= Math.sin(value) * (double)this.flySpeed.getCurrentValue();
                    Flight.mc.player.motionZ += Math.cos(value) * (double)this.flySpeed.getCurrentValue();
                }
                if (Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Flight.mc.player.motionY = 0.35;
                } else if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Flight.mc.player.motionY = -0.35;
                }
            }
        } else if (mode.equalsIgnoreCase("Matrix Disabler")) {
            if (this.sendTimer.hasReached(350.0)) {
                Flight.mc.player.connection.sendPacket(new CPacketEntityAction(Flight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                this.sendTimer.reset();
            }
            Flight.mc.player.jump();
            Flight.mc.player.motionX *= 1.1;
            Flight.mc.player.motionZ *= 1.1;
            MovementHelper.strafePlayer();
        } else if (mode.equalsIgnoreCase("Matrix Pearl")) {
            if (Flight.mc.player.hurtTime > 0 && Flight.mc.player.hurtTime <= 9 && isHurt) {
                ++this.disableTicks;
                if (Flight.mc.player.onGround) {
                    Flight.mc.player.jump();
                } else {
                    if (Flight.mc.player.ticksExisted % 2 == 0) {
                        Flight.mc.player.connection.sendPacket(new CPacketEntityAction(Flight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    }
                    Flight.mc.player.jump();
                    Flight.mc.player.motionY += (double)motionValue.getCurrentValue();
                    Flight.mc.player.jumpMovementFactor = boostValue.getCurrentValue();
                    MovementHelper.strafePlayer();
                }
            }
            if (this.disableTicks > 3 && !MovementHelper.isBlockUnder(0.5f)) {
                if (this.autoDisable.getCurrentValue()) {
                    this.toggle();
                }
                this.disableTicks = 0;
                isHurt = false;
                hurtTicks = 0;
            }
        } else if (mode.equalsIgnoreCase("Matrix Pearl 6.6.0")) {
            if (Flight.mc.player.hurtTime <= 0 || Flight.mc.player.hurtTime > 9 || isHurt) {
                // empty if block
            }
            if (++hurtTicks < 3) {
                return;
            }
            if (Flight.mc.player.hurtTime > 0 && Flight.mc.player.hurtTime <= 9 && isHurt) {
                ++this.disableTicks;
                if (Flight.mc.player.onGround) {
                    Flight.mc.player.jump();
                } else {
                    Flight.mc.player.jump();
                    Flight.mc.player.motionY += (double)motionValue.getCurrentValue();
                    Flight.mc.player.jumpMovementFactor = boostValue.getCurrentValue();
                    MovementHelper.strafePlayer();
                }
            }
            if (this.disableTicks > 3 && !MovementHelper.isBlockUnder(0.5f)) {
                if (this.autoDisable.getCurrentValue()) {
                    this.toggle();
                }
                this.disableTicks = 0;
                isHurt = false;
                hurtTicks = 0;
            }
        } else if (mode.equalsIgnoreCase("Matrix Pearl Test")) {
            if (Flight.mc.player.hurtTime > 0 && Flight.mc.player.hurtTime <= 9 && isHurt) {
                MovementHelper.strafe(MovementHelper.getSpeed());
                Flight.mc.timer.timerSpeed = 3.0f;
                Flight.mc.player.motionY += 0.41;
                Flight.mc.player.speedInAir = 0.8057f;
                if (MovementHelper.getSpeed() > 2.0f) {
                    Flight.mc.timer.timerSpeed = 5.0f;
                }
            } else {
                Flight.mc.timer.timerSpeed = 1.0f;
                Flight.mc.player.speedInAir = 0.02f;
            }
        } else if (mode.equalsIgnoreCase("JetMine")) {
            Flight.mc.player.connection.sendPacket(new CPacketEntityAction(Flight.mc.player, CPacketEntityAction.Action.START_SPRINTING));
            if (Flight.mc.player.onGround) {
                Flight.mc.player.jump();
            }
        }
    }

    @EventTarget
    public void onPacketFly(EventPreMotion event) {
        if (!Flight.flyMode.currentMode.equals("Packet")) {
            return;
        }
        double speed = Flight.mc.player.movementInput.jump && !MovementHelper.isMoving() ? 0.062 : (Flight.mc.player.movementInput.sneak ? -0.062 : 0.0);
        double[] strafing = MovementHelper.getSpeed(this.motionSpeed.getCurrentValue());
        if (this.useTimer.getCurrentValue()) {
            Flight.mc.timer.timerSpeed = this.timerSpeed.getCurrentValue();
        }
        for (int i = 1; i < 2; ++i) {
            Flight.mc.player.motionX = strafing[0] * (double)i * 1.0;
            Flight.mc.player.motionY = speed * (double)i;
            Flight.mc.player.motionZ = strafing[1] * (double)i * 1.0;
            this.sendPackets(Flight.mc.player.motionX, Flight.mc.player.motionY, Flight.mc.player.motionZ);
        }
    }

    @WXFuscator
    @EventTarget
    public void onWellMore(EventUpdate event) {
        if (Flight.flyMode.currentMode.equals("WellMore New")) {
            if (Flight.mc.player.onGround) {
                Flight.mc.player.jump();
            } else {
                Flight.mc.player.capabilities.isFlying = true;
                Flight.mc.player.capabilities.setFlySpeed(speed.getCurrentValue() / 5.0f);
                Flight.mc.player.motionX = 0.0;
                Flight.mc.player.motionY = 0.0;
                Flight.mc.player.motionZ = 0.0;
            }
        }
    }

    @WXFuscator
    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (!this.getState()) {
            return;
        }
        if (Flight.flyMode.currentMode.equals("Packet")) {
            if (event.getPacket() instanceof CPacketPlayer && !this.packetFlyPackets.remove(event.getPacket())) {
                event.setCancelled(true);
            }
        } else if (Flight.flyMode.currentMode.equals("WellMore New")) {
            if (event.getPacket() instanceof CPacketConfirmTeleport) {
                event.setCancelled(true);
            }
        } else if (Flight.flyMode.currentMode.equals("JetMine")) {
            float yaw = Flight.mc.player.rotationYaw * ((float)Math.PI / 180);
            if (event.getPacket() instanceof CPacketPlayer && !Flight.mc.player.onGround) {
                CPacketPlayer packet = (CPacketPlayer)event.getPacket();
                packet.x = Flight.mc.player.posX + Math.sin(yaw) * (double)horizontalStrength.getCurrentValue();
                packet.y = Flight.mc.player.posY - (double)verticalStrength.getCurrentValue();
                packet.z = Flight.mc.player.posZ - Math.cos(yaw) * (double)horizontalStrength.getCurrentValue();
            }
        }
    }

    @EventTarget
    public void onPush(EventPush event) {
        event.setCancelled(Flight.flyMode.currentMode.equals("Packet"));
    }

    @EventTarget
    public void onPacketReceive(EventReceivePacket event) {
        if (Flight.mc.player == null || Flight.mc.world == null) {
            this.toggle();
            EventManager.unregister(this);
            return;
        }
        if (Flight.flyMode.currentMode.equals("Packet") && event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            packet.yaw = Flight.mc.player.rotationYaw;
            packet.pitch = Flight.mc.player.rotationPitch;
        }
        if (Flight.flyMode.currentMode.equals("Sunrise New2") && event.getPacket() instanceof SPacketPlayerPosLook) {
            event.setCancelled(true);
        }
    }

    private void sendPackets(double x, double y, double z) {
        Vec3d vec = new Vec3d(x, y, z);
        Vec3d position = Flight.mc.player.getPositionVector().add(vec);
        Vec3d outOfBoundsVec = this.outOfBoundsVec3d(position);
        this.packetSender(new CPacketPlayer.Position(position.x, position.y, position.z, Flight.mc.player.onGround));
        this.packetSender(new CPacketPlayer.Position(outOfBoundsVec.x, outOfBoundsVec.y, outOfBoundsVec.z, Flight.mc.player.onGround));
    }

    private Vec3d outOfBoundsVec3d(Vec3d position) {
        return position.add(0.0, 1337.0, 0.0);
    }

    private void packetSender(CPacketPlayer packet) {
        this.packetFlyPackets.add(packet);
        Flight.mc.player.connection.sendPacket(packet);
    }

    public static void init() {
    }
}

