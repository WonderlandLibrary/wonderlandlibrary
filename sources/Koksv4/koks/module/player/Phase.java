package koks.module.player;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.utils.MovementUtil;
import koks.api.utils.TimeHelper;
import koks.api.manager.value.annotation.Value;
import koks.event.NoClipEvent;
import koks.event.UpdateEvent;
import koks.event.UpdateMotionEvent;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Phase", description = "You can walk through walls", category = Module.Category.PLAYER)
public class Phase extends Module implements Module.Unsafe {

    @Value(name = "Mode", modes = {"Hive", "Intave13", "AAC1.9.10", "Redesky", "Spartan426", "Minemora"})
    String mode = "Hive";

    private final TimeHelper timeHelper = new TimeHelper();

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        final MovementUtil movementUtil = MovementUtil.getInstance();
        switch (mode) {
            case "Spartan426":
                if (event instanceof final NoClipEvent noClipEvent) {
                    noClipEvent.setNoClip(getPlayer().ticksExisted % 2 == 0 && getPlayer().isCollidedHorizontally && isMoving());
                }

                if (event instanceof final UpdateMotionEvent updateMotionEvent) {
                    if (updateMotionEvent.getType() == UpdateMotionEvent.Type.PRE && getPlayer().isCollidedHorizontally && isMoving()) {
                        if (getPlayer().ticksExisted % 2 == 0) {
                            getPlayer().onGround = false;
                            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() - 3, getZ(), true));
                        } else {
                            for (int i = 0; i < 10; i++)
                                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), -10, getZ(), false));
                            movementUtil.blinkTo(2, getY(), getYaw(), false);
                        }
                    }
                }
                break;
            case "Redesky":
                if (event instanceof UpdateEvent) {
                    if (getBlockUnderPlayer(0.5F) != Blocks.air)
                        setPosition(getX(), getY() - 0.1E-8D, getZ());
                }
                break;
            case "Hive":
                if (event instanceof UpdateEvent) {
                    if (getPlayer().isCollidedHorizontally) {
                        movementUtil.teleportTo(0.7D);
                        getPlayer().setSprinting(true);
                        getPlayer().motionY = 0;
                        getPlayer().onGround = true;
                    }
                }
                break;
            case "Minemora":
                if (event instanceof final NoClipEvent noClipEvent) {
                    noClipEvent.setNoClip(true);
                }
                if(event instanceof UpdateEvent) {
                    if(getBlockUnderPlayer(-2F) != Blocks.air) {
                        getPlayer().motionY = 0;
                        if(getGameSettings().keyBindSneak.pressed) {
                            getPlayer().motionY = -0.1;
                        } else if(getGameSettings().keyBindJump.pressed) {
                            getPlayer().motionY = 0.1;
                        }
                    }
                }
                break;
            case "Intave13":
                if (event instanceof UpdateEvent) {
                    if (getPlayer().isCollidedHorizontally) {
                        if (isMoving()) {
                            if (timeHelper.hasReached(150)) {
                                movementUtil.teleportTo(0.1);
                                timeHelper.reset();
                            }
                        }
                    }
                }
                break;
            case "AAC1.9.10":
                if (event instanceof UpdateEvent) {
                    setPosition(getX(), getY() - 2, getZ());

                    boolean ground = false;
                    for (int i = 0; i < 6; i++) {
                        ground = !ground;
                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() - 2, getZ(), ground));
                    }
                }
                break;
        }
    }

    @Override
    public void onEnable() {
        switch (mode) {
            case "Minemora":
                if (getPlayer().onGround)
                    getPlayer().jump();
                break;
        }
    }

    @Override
    public void onDisable() {

    }
}
