/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  ru.wendoxd.wclassguard.WXFuscator
 */
package org.celestial.client.feature.impl.movement;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.event.events.impl.packet.EventSendPacket;
import org.celestial.client.event.events.impl.player.EventFullCube;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.event.events.impl.player.EventPush;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;
import ru.wendoxd.wclassguard.WXFuscator;

public class NoClip
extends Feature {
    public static final ListSetting noClipMode = new ListSetting("NoClip Mode", "Vanilla", "Vanilla", "Sunrise New", "Sunrise Old", "New Lorent", "Lorent Fast", "Lorent Packet", "Lorent Craft", "Lorent Down", "Really World");
    private final NumberSetting packetSpeed;
    private final NumberSetting customSpeedXZ;
    private final NumberSetting customSpeedY;
    private final NumberSetting tpSpeed;
    private final BooleanSetting destroyBlocks;
    private final Queue<Packet<?>> packets;
    private final TimerHelper timerHelper = new TimerHelper();
    private int insideBlockTicks;
    private int teleportId;
    private int enabledTicks;

    public NoClip() {
        super("NoClip", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0445\u043e\u0434\u0438\u0442\u044c \u0441\u043a\u0432\u043e\u0437\u044c \u0441\u0442\u0435\u043d\u044b", Type.Player);
        this.packets = new ConcurrentLinkedQueue();
        this.destroyBlocks = new BooleanSetting("Destroy Blocks", false, () -> !NoClip.noClipMode.currentMode.equals("New Lorent"));
        this.packetSpeed = new NumberSetting("Packet Speed", 0.03f, 0.01f, 0.5f, 0.01f, () -> NoClip.noClipMode.currentMode.equals("Lorent Packet"));
        this.customSpeedXZ = new NumberSetting("XZ Speed", 1.0f, 0.01f, 1.5f, 0.01f, () -> !NoClip.noClipMode.currentMode.equals("Lorent Down") && !NoClip.noClipMode.currentMode.equals("Lorent Fast") && !NoClip.noClipMode.currentMode.equals("Lorent Packet") && !NoClip.noClipMode.currentMode.equals("New Lorent"));
        this.customSpeedY = new NumberSetting("Y Speed", 0.5f, 0.01f, 1.5f, 0.01f, () -> NoClip.noClipMode.currentMode.equals("Vanilla"));
        this.tpSpeed = new NumberSetting("TP Speed", 10.0f, 3.0f, 50.0f, 1.0f, () -> NoClip.noClipMode.currentMode.equals("Lorent Fast"));
        this.addSettings(noClipMode, this.destroyBlocks, this.packetSpeed, this.customSpeedXZ, this.customSpeedY, this.tpSpeed);
    }

    @Override
    public void onDisable() {
        if (NoClip.noClipMode.currentMode.equals("Lorent Craft") || NoClip.noClipMode.currentMode.equals("Sunrise New")) {
            while (!this.packets.isEmpty()) {
                NoClip.mc.player.connection.sendPacket(this.packets.poll());
            }
        }
        NoClip.mc.timer.timerSpeed = 1.0f;
        this.insideBlockTicks = 0;
        this.enabledTicks = 0;
        super.onDisable();
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (NoClip.noClipMode.currentMode.equals("New Lorent") && event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            NoClip.mc.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
            NoClip.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(packet.getX(), NoClip.mc.player.posY, packet.getZ(), packet.getYaw(), packet.getPitch(), false));
            NoClip.mc.player.setPosition(packet.getX(), NoClip.mc.player.posY, packet.getZ());
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onUpdate(EventReceivePacket event) {
        SPacketPlayerPosLook packet;
        if (NoClip.noClipMode.currentMode.equals("Lorent Fast") && event.getPacket() instanceof SPacketPlayerPosLook) {
            packet = (SPacketPlayerPosLook)event.getPacket();
            packet.yaw = NoClip.mc.player.rotationYaw;
            packet.pitch = NoClip.mc.player.rotationPitch;
        }
        if (NoClip.noClipMode.currentMode.equals("Lorent Packet") && event.getPacket() instanceof SPacketPlayerPosLook) {
            packet = (SPacketPlayerPosLook)event.getPacket();
            NoClip.mc.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
            NoClip.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(packet.getX(), NoClip.mc.player.posY, packet.getZ(), packet.getYaw(), packet.getPitch(), false));
            NoClip.mc.player.setPosition(packet.getX(), NoClip.mc.player.posY, packet.getZ());
            event.setCancelled(true);
        }
        if (NoClip.noClipMode.currentMode.equals("Sunrise New")) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                packet = (SPacketPlayerPosLook)event.getPacket();
                NoClip.mc.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
            }
            if (event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onFullCube(EventFullCube event) {
        if (NoClip.noClipMode.currentMode.equals("Sunrise New")) {
            event.setCancelled(true);
        } else {
            event.setCancelled(!NoClip.noClipMode.currentMode.equals("Lorent Packet") && !NoClip.noClipMode.currentMode.equals("New Lorent"));
        }
    }

    @EventTarget
    public void onPush(EventPush event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (NoClip.noClipMode.currentMode.equals("New Lorent") && event.getPacket() instanceof CPacketPlayer && !NoClip.mc.player.onGround) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.y = NoClip.mc.player.posY + 50.0;
        }
    }

    @WXFuscator
    @EventTarget
    public void onSend(EventSendPacket event) {
        if (!this.getState()) {
            return;
        }
        if (NoClip.noClipMode.currentMode.equals("Lorent Craft") && event.getPacket() instanceof CPacketPlayer) {
            event.setCancelled(true);
            this.packets.add(event.getPacket());
        }
        if (NoClip.noClipMode.currentMode.equals("Sunrise New") && NoClip.mc.getCurrentServerData().serverIP.equalsIgnoreCase("play.sunmc.ru") && event.getPacket() instanceof CPacketPlayer) {
            if (NoClip.mc.player.ticksExisted % 2 == 0) {
                while (!this.packets.isEmpty()) {
                    NoClip.mc.player.connection.sendPacket(this.packets.poll());
                }
                return;
            }
            event.setCancelled(true);
            this.packets.add(event.getPacket());
        }
    }

    @WXFuscator
    @EventTarget
    public void onReceive(EventReceivePacket event) {
        if (NoClip.noClipMode.currentMode.equals("Sunrise New") && (NoClip.mc.player == null || NoClip.mc.world == null)) {
            this.toggle();
            EventManager.unregister(this);
        }
        if (NoClip.noClipMode.currentMode.equals("Sunrise Old")) {
            if (NoClip.mc.player == null || NoClip.mc.world == null) {
                this.toggle();
                EventManager.unregister(this);
                return;
            }
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onPreMotionUpdate2(EventPreMotion event) {
        if (NoClip.noClipMode.currentMode.equals("New Lorent")) {
            NoClip.mc.player.connection.sendPacket(new CPacketEntityAction(NoClip.mc.player, CPacketEntityAction.Action.START_SPRINTING));
            if (NoClip.mc.player.onGround) {
                NoClip.mc.player.jump();
            }
        }
    }

    @EventTarget
    public void onPreMotionUpdate(EventPreMotion event) {
        if (NoClip.noClipMode.currentMode.equals("New Lorent")) {
            NoClip.mc.player.setVelocity(0.0, 0.0, 0.0);
            event.setCancelled(true);
            float speedY = 0.0f;
            if (NoClip.mc.player.movementInput.jump) {
                if (!this.timerHelper.hasReached(3000.0)) {
                    speedY = NoClip.mc.player.ticksExisted % 20 == 0 ? -0.04f : 0.031f;
                } else {
                    this.timerHelper.reset();
                    speedY = -0.08f;
                }
            } else if (NoClip.mc.player.movementInput.sneak) {
                speedY = -0.0031f;
            }
            double[] dir = MovementHelper.forward(0.02f);
            NoClip.mc.player.motionX = dir[0];
            NoClip.mc.player.motionY = speedY;
            NoClip.mc.player.motionZ = dir[1];
            NoClip.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(NoClip.mc.player.posX + NoClip.mc.player.motionX, NoClip.mc.player.posY + NoClip.mc.player.motionY, NoClip.mc.player.posZ + NoClip.mc.player.motionZ, NoClip.mc.player.rotationYaw, NoClip.mc.player.rotationPitch, NoClip.mc.player.onGround));
            double x = NoClip.mc.player.posX + NoClip.mc.player.motionX;
            double y = NoClip.mc.player.posY + NoClip.mc.player.motionY;
            double z = NoClip.mc.player.posZ + NoClip.mc.player.motionZ;
            NoClip.mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y -= -1337.0, z, NoClip.mc.player.onGround));
        }
    }

    @WXFuscator
    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (NoClip.noClipMode.currentMode.equals("Lorent Packet")) {
            NoClip.mc.player.setVelocity(0.0, 0.0, 0.0);
            event.setCancelled(true);
            float speedY = 0.0f;
            if (NoClip.mc.player.movementInput.jump) {
                if (!this.timerHelper.hasReached(3000.0)) {
                    speedY = NoClip.mc.player.ticksExisted % 20 == 0 ? -0.04f : 0.031f;
                } else {
                    this.timerHelper.reset();
                    speedY = -0.08f;
                }
            } else if (NoClip.mc.player.movementInput.sneak) {
                speedY = -0.0031f;
            }
            double[] dir = MovementHelper.forward(this.packetSpeed.getCurrentValue());
            NoClip.mc.player.motionX = dir[0];
            NoClip.mc.player.motionY = speedY;
            NoClip.mc.player.motionZ = dir[1];
            NoClip.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(NoClip.mc.player.posX + NoClip.mc.player.motionX, NoClip.mc.player.posY + NoClip.mc.player.motionY, NoClip.mc.player.posZ + NoClip.mc.player.motionZ, NoClip.mc.player.rotationYaw, NoClip.mc.player.rotationPitch, NoClip.mc.player.onGround));
            double x = NoClip.mc.player.posX + NoClip.mc.player.motionX;
            double y = NoClip.mc.player.posY + NoClip.mc.player.motionY;
            double z = NoClip.mc.player.posZ + NoClip.mc.player.motionZ;
            NoClip.mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y -= -1337.0, z, NoClip.mc.player.onGround));
        } else if (NoClip.noClipMode.currentMode.equals("Sunrise New") && NoClip.mc.getCurrentServerData().serverIP.equalsIgnoreCase("play.sunmc.ru")) {
            NoClip.mc.player.motionY = 0.0;
            event.setOnGround(true);
            NoClip.mc.player.onGround = true;
        }
    }

    @WXFuscator
    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(NoClip.noClipMode.currentMode);
        if (NoClip.mc.world != null && NoClip.mc.player != null) {
            if (this.destroyBlocks.getCurrentValue() && (NoClip.mc.player.isCollidedHorizontally || MovementHelper.isInsideBlock2())) {
                double[] dir = MovementHelper.forward(0.5);
                NoClip.mc.playerController.onPlayerDamageBlock(new BlockPos(NoClip.mc.player.posX + dir[0], NoClip.mc.player.posY + 1.0, NoClip.mc.player.posZ + dir[1]), NoClip.mc.player.getHorizontalFacing());
                NoClip.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
            if (NoClip.noClipMode.currentMode.equals("Vanilla")) {
                NoClip.mc.player.motionY = 0.0;
                NoClip.mc.player.motionX *= (double)this.customSpeedXZ.getCurrentValue();
                NoClip.mc.player.motionZ *= (double)this.customSpeedXZ.getCurrentValue();
                if (NoClip.mc.gameSettings.keyBindJump.isKeyDown()) {
                    NoClip.mc.player.motionY += (double)this.customSpeedY.getCurrentValue();
                } else if (NoClip.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    NoClip.mc.player.motionY -= (double)this.customSpeedY.getCurrentValue();
                }
            } else if (NoClip.noClipMode.currentMode.equals("Lorent Fast")) {
                if (!NoClip.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    NoClip.mc.player.motionY = 0.0;
                    NoClip.mc.timer.timerSpeed = this.tpSpeed.getCurrentValue();
                } else {
                    NoClip.mc.player.motionX -= 5.0E-7;
                    NoClip.mc.player.motionZ += 5.0E-7;
                    NoClip.mc.player.motionY = -0.1;
                }
            } else if (NoClip.noClipMode.currentMode.equals("Lorent Down")) {
                NoClip.mc.timer.timerSpeed = 4.4f;
                NoClip.mc.player.motionX -= 5.0E-7;
                NoClip.mc.player.motionZ += 5.0E-7;
                NoClip.mc.player.motionY = -0.1;
            } else if (NoClip.noClipMode.currentMode.equals("Lorent Craft")) {
                NoClip.mc.player.motionY = 0.0;
                NoClip.mc.player.motionX *= (double)this.customSpeedXZ.getCurrentValue();
                NoClip.mc.player.motionZ *= (double)this.customSpeedXZ.getCurrentValue();
            } else if (NoClip.noClipMode.currentMode.equals("Sunrise Old")) {
                if (NoClip.mc.player == null || NoClip.mc.world == null) {
                    this.toggle();
                    EventManager.unregister(this);
                    return;
                }
                NoClip.mc.player.motionY = 0.0;
                NoClip.mc.player.motionX *= (double)this.customSpeedXZ.getCurrentValue();
                NoClip.mc.player.motionZ *= (double)this.customSpeedXZ.getCurrentValue();
                if (NoClip.mc.gameSettings.keyBindJump.isKeyDown()) {
                    NoClip.mc.player.motionY = 0.01;
                } else if (NoClip.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    NoClip.mc.player.motionY = -0.01;
                }
                MovementHelper.teleport(2.0);
            } else if (NoClip.noClipMode.currentMode.equals("Sunrise New")) {
                if (NoClip.mc.player == null || NoClip.mc.world == null) {
                    this.toggle();
                    EventManager.unregister(this);
                    return;
                }
                this.insideBlockTicks = MovementHelper.isInsideBlock2() ? ++this.insideBlockTicks : 0;
            } else if (NoClip.noClipMode.currentMode.equals("Really World")) {
                int ticks = 35;
                if (NoClip.mc.player.ticksExisted % ticks == 0) {
                    NoClip.mc.timer.timerSpeed = 1.0f;
                    this.toggle();
                    return;
                }
                NoClip.mc.player.motionY = 0.0;
                NoClip.mc.player.motionX *= (double)this.customSpeedXZ.getCurrentValue();
                NoClip.mc.player.motionZ *= (double)this.customSpeedXZ.getCurrentValue();
                NoClip.mc.timer.timerSpeed = 500.0f;
            }
        }
    }

    public static void init() {
    }
}

