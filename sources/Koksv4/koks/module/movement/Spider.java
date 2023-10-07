package koks.module.movement;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.utils.MovementUtil;
import koks.event.NoClipEvent;
import koks.event.TickEvent;
import koks.event.UpdateEvent;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Spider", description = "you can climb wally up", category = Module.Category.MOVEMENT)
public class Spider extends Module {

    boolean onGround;

    @Value(name = "Mode", modes = {"Intave13", "Intave13Gomme", "Karhu2.1.9 163-PRE", "Verus3616"})
    public String mode = "Intave13";

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        final MovementUtil movementUtil = MovementUtil.getInstance();
        switch (mode) {
            case "Verus3616":
                if(event instanceof TickEvent) {
                    if(getPlayer().isCollidedHorizontally)
                        if(getPlayer().ticksExisted % 2 == 0)
                            getPlayer().jump();
                }
                break;
            case "Intave13Gomme":
                if (event instanceof UpdateEvent) {
                    if (getPlayer().isCollidedHorizontally) {
                        if (getPlayer().onGround)
                            getPlayer().jump();
                        else if (getPlayer().motionY < 0.04)
                            getPlayer().motionY += 0.08;
                        if (getPlayer().motionY > 0.03) {
                            setPosition(getX(), getY() - 0.001, getZ());
                            getPlayer().onGround = true;
                            if (getPlayer().moveForward > 0)
                                movementUtil.setSpeed(0.04F);
                        }
                    }
                }
                break;
            case "Intave13":
                if (event instanceof UpdateEvent) {
                    if (getPlayer().isCollidedHorizontally && getPlayer().onGround) {
                        getPlayer().jump();
                        onGround = false;
                    } else if (!getPlayer().onGround && getPlayer().motionY < 0.2 && getPlayer().isCollidedHorizontally) {
                        onGround = true;
                        getPlayer().jump();
                    } else {
                        onGround = getPlayer().onGround;
                    }
                }
                break;
            case "Karhu2.1.9 163-PRE":
                if(event instanceof final NoClipEvent noClipEvent) {
                    if (getPlayer().isCollidedHorizontally && isMoving()) {
                        final double motionX = -Math.sin(Math.toRadians(movementUtil.getDirection(getYaw())));
                        final double motionZ = Math.cos(Math.toRadians(movementUtil.getDirection(getYaw())));
                        if (getWorld().getBlockState(new BlockPos(getX() + motionX, (int)getY() + 1, getZ() + motionZ)).getBlock() != Blocks.air) {
                            noClipEvent.setNoClip(true);
                        }
                    }
                }
                if(event instanceof UpdateEvent) {
                    if (getPlayer().isCollidedHorizontally) {
                        getTimer().timerSpeed = 0.3F;
                        if (getPlayer().ticksExisted % 2 == 0) {
                            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(),getY() + 0.42, getZ(), true));
                            movementUtil.blinkTo(0.2,getY(), getYaw(), false);
                            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(),getY() + 0.2, getZ(), false));
                        } else {
                            getPlayer().motionY = 0;
                            setMotion(0);
                        }
                    } else {
                        getTimer().timerSpeed = 1;
                    }
                }
                break;
        }

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
