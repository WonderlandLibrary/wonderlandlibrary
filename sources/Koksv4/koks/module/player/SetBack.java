package koks.module.player;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;
import koks.api.utils.MovementUtil;
import koks.api.utils.TimeHelper;
import koks.event.UpdateEvent;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.Random;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "SetBack", description = "Your respawn at the same position", category = Module.Category.PLAYER)
public class SetBack extends Module {

    double deathX, deathY, deathZ;
    boolean wasDamaged;
    final TimeHelper timeHelper = new TimeHelper();

    @Value(name = "Mode", modes = {"Intave13.0.6", "Intave13.0.8", "AAC3.0.1", "Matrix6.1.1"})
    String mode = "Intave13.0.8";

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        switch (mode) {
            case "Matrix6.1.1":
                if (event instanceof UpdateEvent) {
                    if (getPlayer().getHealth() <= 5) {
                        getPlayer().capabilities.isFlying = true;
                        getPlayer().capabilities.allowFlying = true;
                        getPlayer().motionY = 0.2;
                        wasDamaged = true;
                        timeHelper.reset();
                    } else {
                        if (wasDamaged) {
                            if (!timeHelper.hasReached(650)) {
                                getPlayer().motionY = 0.4;
                            } else {
                                wasDamaged = false;
                                getPlayer().capabilities.isFlying = false;
                                getPlayer().capabilities.allowFlying = false;
                            }
                        }
                    }
                }
                break;
            case "AAC3.0.1":
                if (event instanceof UpdateEvent) {
                    final Random rnd = new Random();
                    if (mc.currentScreen instanceof GuiGameOver) {
                        for (int i = 0; i < 30; i++) {
                            setPosition(getX() + i, getY() + 40, getZ() + i);
                        }
                        getPlayer().respawnPlayer();
                    }
                    if (getPlayer().getHealth() <= 6F) {
                        deathX = getX();
                        deathY = getY();
                        deathZ = getZ();
                        setPosition(getX() + rnd.nextDouble() * 1.2, getY() - 0.15, getZ() + rnd.nextDouble() * 1.2);
                    }
                }
                break;
            case "Intave13.0.6":
                if (event instanceof UpdateEvent) {
                    if (!getPlayer().isInWater()) {
                        if (getPlayer().getHealth() < 3F) {
                            for (int i = 0; i < 30; i++) {
                                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                                        mc.thePlayer.posY, mc.thePlayer.posZ, true));
                            }
                        }
                        if (mc.currentScreen instanceof GuiGameOver) {
                            for (int i = 0; i < 30; i++) {
                                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                                        mc.thePlayer.posY, mc.thePlayer.posZ, true));

                            }
                        }
                    }
                }
                break;
            case "Intave13.0.8":
                if (event instanceof UpdateEvent) {
                    if (!getPlayer().isInWater() && !isVoid(getPlayer())) {
                        if (getPlayer().getHealth() < 3F) {
                            for (int i = 0; i < 30; i++) {
                                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + randomInRange(3, 5), getZ(), true));
                            }
                        }
                        if (mc.currentScreen instanceof GuiGameOver) {
                            for (int i = 0; i < 30; i++) {
                                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + randomInRange(3, 5), getZ(), true));
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onEnable() {
        wasDamaged = false;
    }

    @Override
    public void onDisable() {

    }
}
