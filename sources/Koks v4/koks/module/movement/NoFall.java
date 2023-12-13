package koks.module.movement;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.MovementUtil;
import koks.event.NoClipEvent;
import koks.event.PacketEvent;
import koks.event.UpdateEvent;
import koks.module.combat.KillAura;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "NoFall", category = Module.Category.MOVEMENT, description = "Prevents you from getting fall damage")
public class NoFall extends Module {

    @Value(name = "Mode", modes = {"Mineplex", "Intave13", "AAC3.3.12", "AAC4", "AAC5.2.0", "AAC5.2.0 Infinite", "Redesky", "BAC1.0.4", "Spartan422.1", "Spartan423", "Spartan424", "Verus3616", "Matrix6.6.1"})
    String mode = "Mineplex";

    boolean matrix661Switch;

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof final PacketEvent packetEvent) {
            final Packet<?> packet = packetEvent.getPacket();
            switch (mode) {
                case "Matrix6.6.1":
                    if (packet instanceof final C03PacketPlayer packetPlayer) {
                        if (getPlayer().fallDistance >= 3.2 && !getPlayer().onGround && !matrix661Switch) {
                            packetPlayer.onGround = false;
                            matrix661Switch = true;
                        } else if (matrix661Switch) {
                            packetPlayer.onGround = true;
                            getPlayer().fallDistance = 0;
                            matrix661Switch = false;
                        }
                    }
                    break;
                case "Verus3616":
                    if(packet instanceof final C03PacketPlayer.C04PacketPlayerPosition position) {
                        if(getPlayer().fallDistance > 4)
                            position.y -= 100;
                    }
                case "Redesky":
                    if (packet instanceof C03PacketPlayer c03PacketPlayer) {
                        if (KillAura.getCurEntity() == null && getPlayer().fallDistance > 2) {
                            c03PacketPlayer.onGround = true;
                            packetEvent.setPacket(c03PacketPlayer);
                        }
                    }
                    break;
            }
        }
        if (event instanceof UpdateEvent) {
            final MovementUtil movementUtil = MovementUtil.getInstance();
            boolean hasAir = true;
            for (int position = 0; position < 2; position++) {
                if (getBlockUnderPlayer(position) != Blocks.air)
                    hasAir = false;
            }
            switch (mode) {
                case "Spartan424":
                    if (hasAir && getPlayer().fallDistance > 2) {
                        sendPacket(new C03PacketPlayer(true));
                        getPlayer().fallDistance = 0;
                    }
                    break;
                case "Spartan423":
                    if (getPlayer().fallDistance > 4) {
                        getPlayer().motionY = 0.42;
                        getPlayer().fallDistance = 0;
                    }
                    break;
                case "Spartan422.1":
                    if (getPlayer().fallDistance > 3) {
                        setMotion(0);
                        sendPacket(new C03PacketPlayer(true));
                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), -99999, getZ(), true));
                        getPlayer().fallDistance = 0;
                    }
                    break;
                case "BAC1.0.4":
                    if (getPlayer().fallDistance > 2 && hasAir) {
                        getPlayer().motionY = 0.019D;
                        movementUtil.teleportTo(0.6, getY(), getPlayer().rotationYaw);
                    }
                    break;
                case "Verus3616":
                    if(getPlayer().fallDistance > 4) {
                        getPlayer().fallDistance = 0;
                    }
                    break;
                case "Mineplex":
                    if (getPlayer().fallDistance > 2) {
                        sendPacket(new C03PacketPlayer(true));
                        getPlayer().fallDistance = 0;
                    }
                    break;
                case "Intave13":
                    if (getPlayer().fallDistance > 2F) {
                        getPlayer().onGround = true;
                        getPlayer().capabilities.isFlying = true;
                    }
                    break;
                case "AAC3.3.12":
                    getPlayer().motionY = -75;
                    break;
                case "AAC4":
                    if (!getPlayer().onGround && getPlayer().fallDistance > 1.2 && getPlayer().ticksExisted % 3 == 2) {
                        getPlayer().motionY = 0;
                        sendPacket(new C03PacketPlayer(true));
                        getPlayer().fallDistance = 1;
                    }
                    break;
                case "AAC5.2.0":
                    if (getBlockUnderPlayer(3) != Blocks.air && getPlayer().fallDistance > 2) {
                        for (int i = 0; i < 4; i++) {
                            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + i, getZ(), true));
                        }
                    }
                    break;
                case "AAC5.2.0 Infinite":
                    if(getPlayer().fallDistance > 4 && hasAir) {
                        getPlayer().motionY = 0.2;
                    }
                    break;
            }
        }
    }

    @Override
    public void onEnable() {
        matrix661Switch = false;
    }

    @Override
    public void onDisable() {

    }
}
