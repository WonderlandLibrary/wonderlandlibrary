package koks.module.movement;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.utils.MovementUtil;
import koks.api.utils.TimeHelper;
import koks.event.*;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
@Module.Info(name = "HighJump", description = "You jump hhhhhiiiiiigggggggggghhhhhh", category = Module.Category.MOVEMENT)
public class HighJump extends Module {

    @Value(name = "Mode", modes = {"Intave13.0.8Slime", "CubeCraft", "Spartan426", "Spartan426-Good", "Spartan432", "Karhu2.1.9 163-PRE", "Matrix6.6.1 Cobweb"})
    String mode = "Intave13.0.8Slime";

    @Value(name = "Matrix6.6.1 Cobweb-Motion", displayName = "Motion", minimum = 1, maximum = 6)
    double matrix661Motion = 4;

    /* Spartan426 */
    @Value(name = "Spartan246-Height", displayName = "Height", minimum = 1, maximum = 10)
    int spartan246Height = 10;

    /* Karhu2.1.9 163-PRE */
    @Value(name = "Karhu2.1.9 163-PRE-Height", displayName = "Height", minimum = 2, maximum = 300)
    int karhu219163PREHeight = 50;

    final TimeHelper spartan426Time = new TimeHelper();
    int spartan426Ticks;
    double spartan426GoodStartY;
    boolean matrix661AllowBoost, matrix661Boosted;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        switch (name) {
            case "Matrix6.6.1 Cobweb-Motion":
                return mode.equalsIgnoreCase("Matrix6.6.1 Cobweb");
            case "Spartan246-Height":
                return mode.equalsIgnoreCase("Spartan426-Good") || mode.equalsIgnoreCase("Spartan426");
            case "Karhu2.1.9 163-PRE-Height":
                return mode.equalsIgnoreCase("Karhu2.1.9 163-PRE");
        }
        return super.isVisible(value, name);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        switch (mode) {
            case "Matrix6.6.1 Cobweb":
                if(event instanceof UpdateEvent) {
                    if(getPlayer().isInWeb && getBlockUnderPlayer(0.01F) != Blocks.web && !matrix661AllowBoost) {
                        getPlayer().motionY = matrix661Motion;
                        getTimer().timerSpeed = 0.8F;
                        getPlayer().isInWeb = false;
                        matrix661AllowBoost = true;
                    } else if(getPlayer().onGround) {
                        matrix661AllowBoost = false;
                        getTimer().timerSpeed = 1;
                    }  else
                        getTimer().timerSpeed = 1;
                    if((getPlayer().motionY > 0 || matrix661Boosted) && getPlayer().fallDistance > 3 && matrix661AllowBoost) {
                        getTimer().timerSpeed = 0.6F;
                        getPlayer().motionY += 0.01;
                        matrix661Boosted = true;
                    }
                }
                break;
            case "Spartan432":
                if (event instanceof final PacketEvent packetEvent) {
                    final Packet<?> packet = packetEvent.getPacket();
                    if (packetEvent.getType() == PacketEvent.Type.SEND) {
                        if (packet instanceof C03PacketPlayer) {
                            final C03PacketPlayer c03PacketPlayer = (C03PacketPlayer) packet;
                            c03PacketPlayer.onGround = true;
                        }
                    }
                }
                if (event instanceof final NoClipEvent noClipEvent) {
                    noClipEvent.setNoClip(true);
                }
                if (event instanceof TickEvent) {
                    getTimer().timerSpeed = 0.6F;
                    if (getPlayer().ticksExisted % 3 == 0)
                        getPlayer().motionY = 1.2;
                    else
                        getPlayer().motionY *= 0.6D;
                }
                break;
            case "Spartan426-Good":
                if (event instanceof final NoClipEvent noClipEvent) {
                    noClipEvent.setNoClip(getPlayer().ticksExisted % 2 == 0);
                }
                if(event instanceof final UpdateMotionEvent updateMotionEvent) {
                    if(updateMotionEvent.getType() == UpdateMotionEvent.Type.PRE) {
                        if(getPlayer().ticksExisted % 2 == 0) {
                            getTimer().timerSpeed = 7F;
                            getPlayer().onGround = true;
                            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + (double) ((float) spartan246Height), getZ(), false));
                            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), spartan426GoodStartY - 3, getZ(), true));
                        } else {
                            getTimer().timerSpeed = 2F;
                            getPlayer().motionY = 0;
                        }
                    }
                }
                break;
            case "Spartan426":
                if(event instanceof UpdateEvent) {
                    if(spartan426Time.hasReached(300, true)) {
                        spartan426Ticks++;
                        for (int i = 0; i < 3; i++)
                            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + spartan246Height, getZ(), true));
                        for (int i = 0; i < 10; i++)
                            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() - 1, getZ(), false));
                    }
                    if (spartan426Ticks >= 4) {
                        spartan426Ticks = 0;
                        setToggled(false);
                    }
                }
                break;
            case "Intave13.0.8Slime":
                if (event instanceof MoveEntityEvent) {
                    if ((getBlockUnderPlayer(1) == Blocks.slime_block) && getGameSettings().keyBindJump.pressed) {
                        getPlayer().motionY = 6.1F;
                    }
                }
                if (event instanceof LivingUpdateEvent) {
                    if (getGameSettings().keyBindSprint.pressed) {
                        if (getPlayer().ticksExisted % 3 == 1) {
                            getPlayer().motionZ *= 1.22F;
                            getPlayer().motionX *= 1.22F;
                            getPlayer().jumpMovementFactor *= 0.039F;
                        }
                    }
                    if (getGameSettings().keyBindSneak.pressed) {
                        getTimer().timerSpeed = 0.2F;
                    } else {
                        getTimer().timerSpeed = 1F;
                    }
                }
                break;
            case "CubeCraft":
                if (getPlayer().onGround)
                    getTimer().timerSpeed = 0.1F;
                else
                    getTimer().timerSpeed = 1.1F;

                if (getPlayer().hurtTime > 0)
                    if (!getPlayer().onGround)
                        if (getHurtTime() > 0) {
                            getTimer().timerSpeed = 1F;
                            getPlayer().motionY = 0.70F;
                        }
                break;
        }
    }


    @Override
    public void onEnable() {
        spartan426GoodStartY = getY();
        spartan426Ticks = 0;
        spartan426Time.reset();
        switch (mode) {
            case "Karhu2.1.9 163-PRE":
                sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 0.42, getZ(), false));
                sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + karhu219163PREHeight, getZ(), true));
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY(), getZ(), false));
                setToggled(false);
                break;
        }
    }

    @Override
    public void onDisable() {
        getTimer().timerSpeed = 1;
        switch (mode) {
            case "Spartan432":
                getPlayer().motionY = 0;
                break;
        }
    }

}

