package koks.module.player;

import com.ibm.icu.impl.UBiDiProps;
import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.utils.MovementUtil;
import koks.api.utils.TimeHelper;
import koks.api.manager.value.annotation.Value;
import koks.event.HeadLookEvent;
import koks.event.PacketEvent;
import koks.event.UpdateEvent;
import koks.event.VelocityEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "GodMode", description = "You can't take any damage", category = Module.Category.PLAYER)
public class GodMode extends Module {

    @Value(name = "Mode", modes = {"Intave Border", "Intave", "DeadByDaylight2020", "DeadByDaylight2021", "Hawk 2008"})
    String mode = "Intave Border";

    @Value(name = "When sneak")
    boolean whenSneak = false;

    private final TimeHelper timeHelper = new TimeHelper();

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        final MovementUtil movementUtil = MovementUtil.getInstance();
        if(!isKeyDown(getGameSettings().keyBindSneak.getKeyCode()) && whenSneak)
            return;
        switch (mode) {
            case "Hawk 2008":
                if(event instanceof UpdateEvent) {
                    if(getPlayer().onGround) {
                        if(getPlayer().ticksExisted % 70 == 0) {
                            sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 0.5, getZ(), false));
                        }
                    }
                }
                break;
            case "Intave Border":
                if (event instanceof final HeadLookEvent headLookEvent) {
                    if (getPlayer().isOutsideBorder()) {
                        headLookEvent.setYaw(mc.thePlayer.rotationYaw);
                        headLookEvent.setPitch(mc.thePlayer.rotationPitch);
                    }
                }

                if (event instanceof UpdateEvent) {
                    if (!getPlayer().isOutsideBorder()) {
                        if (getGameSettings().keyBindForward.pressed) {
                            if (timeHelper.hasReached(150)) {
                                movementUtil.teleportTo(0.1D);
                                timeHelper.reset();
                            }
                        }
                    } else {
                        getPlayer().motionY = 0;
                        getGameSettings().keyBindForward.pressed = false;
                        getGameSettings().keyBindBack.pressed = false;
                        getGameSettings().keyBindLeft.pressed = false;
                        getGameSettings().keyBindRight.pressed = false;
                    }
                }
                break;
            case "DeadByDaylight2020":
                if (event instanceof UpdateEvent) {
                    getPlayer().ridingEntity = null;
                }
                break;
            case "DeadByDaylight2021":
                if(event instanceof UpdateEvent) {
                    if(getPlayer().ridingEntity != null) {
                        if(timeHelper.hasReached(500, true)) {
                            float direction = 1;
                            for (int i = 0; i < 150; i++) {
                                sendPacket(new C0CPacketInput(direction, 0, false, false));
                                direction *= -1;
                            }
                        }
                    }
                }
                break;
            case "Intave": //TODO: Rename
                if (event instanceof UpdateEvent) {
                    sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 4, getZ(), true));
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
