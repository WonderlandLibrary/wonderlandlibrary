package koks.module.player;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.utils.MovementUtil;
import koks.event.NoClipEvent;
import koks.event.UpdateEvent;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "AntiVoid", category = Module.Category.PLAYER, description = "")
public class AntiVoid extends Module {

    @Value(name = "Fall Distance", minimum = 3, maximum = 6)
    double fallDistance = 3;

    @Value(name = "OnlyVoid")
    boolean onlyVoid = true;

    //TODO: Intave14

    @Value(name = "Mode", modes = {"AAC1.9.10", "Hypixel20201207", "Hypixel20210926", "Matrix6.1.1", "Matrix2-6.1.1", "Naga20211011"})
    String mode = "HypixelOld";

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if(event instanceof final NoClipEvent noClipEvent) {
            if (onlyVoid && !isVoid(getPlayer()))
                return;
            switch(mode) {
                case "Naga20211011":
                    if(getPlayer().fallDistance > fallDistance && !getPlayer().onGround) {
                        noClipEvent.setNoClip(true);
                    }
                    break;
            }
        }
        if (event instanceof UpdateEvent) {
            final MovementUtil movementUtil = MovementUtil.getInstance();
            if (onlyVoid && !isVoid(getPlayer()))
                return;
            switch (mode) {
                case "Naga20211011":
                    if(getPlayer().fallDistance > fallDistance && !getPlayer().onGround) {
                        getPlayer().motionY = 0;
                        setMotion(0);
                    }
                    break;
                case "Hypixel20201207":
                    if (getPlayer().fallDistance > fallDistance && !getPlayer().onGround) {
                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 4, getZ(), false));
                    }
                    break;
                case "Hypixel20210926":
                    if (getPlayer().fallDistance >= 0.2F && !getPlayer().onGround) {
                        getPlayer().motionY = -1;
                    }
                    break;
                case "Hypixel15102021":
                    if (getPlayer().fallDistance > fallDistance) {
                        sendPacketUnlogged(new C03PacketPlayer(true));
                        movementUtil.blinkTo(0.5);
                    }
                    break;
                case "AAC1.9.10":
                    if (getPlayer().fallDistance > (fallDistance > 4.5 ? 4.5 : fallDistance) && !getPlayer().onGround) {
                        sendPacket(new C03PacketPlayer(true));
                        if (getHurtTime() != 0) {
                            getPlayer().fallDistance = 0;
                            getPlayer().motionY = 1.5;
                        }
                    }
                    break;
                case "Matrix6.1.1":
                    if (getPlayer().fallDistance > fallDistance) {
                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() - 2, getZ(), true));
                    }
                    break;
                case "Matrix2-6.1.1":
                    if (getPlayer().fallDistance > fallDistance) {
                        getPlayer().capabilities.isFlying = true;
                    }
                    break;
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
