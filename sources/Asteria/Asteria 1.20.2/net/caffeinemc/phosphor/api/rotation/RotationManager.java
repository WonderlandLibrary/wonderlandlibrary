package net.caffeinemc.phosphor.api.rotation;

import net.caffeinemc.phosphor.api.event.events.*;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.event.orbit.EventPriority;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

import static net.caffeinemc.phosphor.common.Phosphor.mc;

public class RotationManager {
    private boolean enabled;
    private boolean rotateBack;
    private boolean resetRotation;

    private RotationUtils.Rotation currentRotation;
    private float clientYaw, clientPitch;
    private float serverYaw, serverPitch;

    public RotationManager() {
        enabled = true;
        rotateBack = false;
        resetRotation = false;

        this.serverYaw = 0;
        this.serverPitch = 0;

        this.clientYaw = 0;
        this.clientPitch = 0;
    }

    public RotationUtils.Rotation getServerRotation() {
        return new RotationUtils.Rotation(serverYaw, serverPitch);
    }

    public void enable() {
        enabled = true;
        rotateBack = false;
    }

    public void disable() {
        if (isEnabled()) {
            enabled = false;
            if (!rotateBack) rotateBack = true;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setRotation(RotationUtils.Rotation rotation) {
        currentRotation = rotation;
    }

    public void setRotation(double yaw, double pitch) {
        setRotation(new RotationUtils.Rotation(yaw, pitch));
    }

    private void resetClientRotation() {
        mc.player.setYaw(clientYaw);
        mc.player.setPitch(clientPitch);

        resetRotation = false;
    }

    private void setClientRotation(RotationUtils.Rotation rotation) {
        this.clientYaw = mc.player.getYaw();
        this.clientPitch = mc.player.getPitch();

        mc.player.setYaw((float) rotation.yaw());
        mc.player.setPitch((float) rotation.pitch());

        resetRotation = true;
    }

    private void setServerRotation(RotationUtils.Rotation rotation) {
        this.serverYaw = (float) rotation.yaw();
        this.serverPitch = (float) rotation.pitch();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof PlayerMoveC2SPacket packet) {
            serverYaw = packet.getYaw(serverYaw);
            serverPitch = packet.getPitch(serverPitch);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    void onPacketReceive(PacketEvent.Receive event) {
        if (event.packet instanceof PlayerPositionLookS2CPacket packet) {
            serverYaw = packet.getYaw();
            serverPitch = packet.getPitch();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onSendMovementPacketPre(SendMovementPacketEvent.Pre event) {
        if (isEnabled() && currentRotation != null) {
            setClientRotation(currentRotation);
            setServerRotation(currentRotation);

            return;
        }

        if (rotateBack) {
            RotationUtils.Rotation serverRot = new RotationUtils.Rotation(serverYaw, serverPitch);
            RotationUtils.Rotation clientRot = new RotationUtils.Rotation(mc.player.getYaw(), mc.player.getPitch());

            if (RotationUtils.getTotalDiff(serverRot, clientRot) > 1) {
                RotationUtils.Rotation smoothRotation = RotationUtils.getSmoothRotation(serverRot, clientRot, 0.2);

                setClientRotation(smoothRotation);
                setServerRotation(smoothRotation);
            } else {
                rotateBack = false;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onSendMovementPacketPost(SendMovementPacketEvent.Post event) {
        if (currentRotation != null) {
            currentRotation.runCallback();
        }

        if (resetRotation) {
            resetClientRotation();
        }

        mc.player.setHeadYaw(serverYaw);
    }

    private boolean wasDisabled;

    @EventHandler(priority = EventPriority.LOWEST)
    private void onAttack(AttackEvent.Pre event) {
        if (!event.isCancelled() && isEnabled()) {
            enabled = false;
            wasDisabled = true;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onAttack(AttackEvent.Post event) {
        if (!isEnabled() && wasDisabled) {
            enabled = true;
            wasDisabled = false;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onItemUse(ItemUseEvent.Pre event) {
        if (!event.isCancelled() && isEnabled()) {
            enabled = false;
            wasDisabled = true;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onItemUse(ItemUseEvent.Post event) {
        if (!isEnabled() && wasDisabled) {
            enabled = true;
            wasDisabled = false;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onBlockBreak(BlockBreakEvent.Pre event) {
        if (!event.isCancelled() && isEnabled()) {
            enabled = false;
            wasDisabled = true;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onBlockBreak(BlockBreakEvent.Post event) {
        if (!isEnabled() && wasDisabled) {
            enabled = true;
            wasDisabled = false;
        }
    }
}
