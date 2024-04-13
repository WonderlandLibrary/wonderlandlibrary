package dev.tenacity.module.impl.movement;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.packet.PacketReceiveEvent;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.event.impl.player.UpdateEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.util.misc.ChatUtil;
import dev.tenacity.util.misc.MathUtil;
import dev.tenacity.util.misc.MathUtils;
import dev.tenacity.util.misc.TimerUtil;
import dev.tenacity.util.player.MovementUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.input.Keyboard.*;

public final class FlightModule extends Module {

    private boolean nonexistant;
    private boolean trupps_brain = nonexistant;
    private final List<Packet<?>> packetList = new ArrayList<>();
    public final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Mineland", "HvH Mode", "Verus", "Vulcan", "Negativity", "BlocksMC", "NCP", "Grim");
    public final ModeSetting verusSettings = new ModeSetting("Type", "Fast", "Funny");
    public final ModeSetting negativitySettings = new ModeSetting("Type", "Fast", "Infinite");
    public final ModeSetting minelandMode = new ModeSetting("Type", "Jank", "Fling");
    private final TimerUtil timerUtil = new TimerUtil();
    private float lastMovementYaw;
    private boolean blink = false;
    private double speed = 1.0;

    private boolean mineland = false;

    private boolean wasUnderBlock = false;
    private TimerUtil underBlockTimer = new TimerUtil();

    private double serverX, serverY, serverZ;
    private boolean teleported;
    private boolean damage;
    private boolean watchdog = false;
    public FlightModule() {
        super("Flight", "Makes you go zoom", ModuleCategory.MOVEMENT);
        minelandMode.addParent(mode, modeSetting -> mode.isMode("Mineland"));
        verusSettings.addParent(mode, modeSetting -> mode.isMode("Verus"));
        negativitySettings.addParent(mode, modeSetting -> mode.isMode("Negativity"));
        initializeSettings(mode, minelandMode, verusSettings, negativitySettings);
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        if (event.isPre()) {
            switch (mode.getCurrentMode()) {
                case "Vanilla": {
                    mc.thePlayer.onGround = true;
                    if (isKeyDown(KEY_SPACE)) {
                        mc.thePlayer.motionY = 3;
                        MovementUtil.setSpeed(3);
                    } else if (isKeyDown(KEY_LSHIFT)) {
                        mc.thePlayer.motionY = -3;
                        MovementUtil.setSpeed(3);
                    } else {
                        mc.thePlayer.motionY = 0;
                        MovementUtil.setSpeed(3);
                    }
                    MovementUtil.setSpeed(3);
                    break;
                }

                case "BlocksMC": {
                    if (MovementUtil.isMoving()) {
                        MovementUtil.setSpeed(5);
                        mc.thePlayer.motionY = 0.1;
                        if (MovementUtil.isMoving()) {
                            return;
                        } else {
                            ChatUtil.warn("Disabling to prevent flags!");
                            toggle();
                        }
                        if (mc.thePlayer.fallDistance > 2 || timerUtil.hasTimeElapsed(7000, true)) {
                            ChatUtil.warn("Disabling to prevent flags!");
                            toggle();
                        }
                        mc.thePlayer.setSprinting(true);
                        mc.thePlayer.sendPlayerAbilities();
                        mc.thePlayer.onGround = false;
                    }
                    if (mc.thePlayer.hurtTime > 20 && mc.thePlayer.isAirBorne) {
                        MovementUtil.setSpeed(9.972);
                        mc.thePlayer.motionY = 0.1;
                        mc.thePlayer.setSprinting(true);
                        mc.thePlayer.sendPlayerAbilities();
                        mc.thePlayer.onGround = false;
                    }
                    break;
                }

                case "HvH Mode": {
                    if (isKeyDown(KEY_LSHIFT) || isKeyDown(KEY_SPACE)) {
                        if (isKeyDown(KEY_SPACE)) {
                            mc.thePlayer.motionY = 5;
                        } else {
                            mc.thePlayer.motionY = -5;
                        }
                    } else {
                        mc.thePlayer.motionY = 0;
                    }
                     if (isKeyDown(KEY_W) || isKeyDown(KEY_A) || isKeyDown(KEY_S) || isKeyDown(KEY_D) || isKeyDown(KEY_SPACE) || isKeyDown(KEY_LSHIFT)) {
                         MovementUtil.setSpeed(5);
                    } else {
                         mc.thePlayer.motionY = 0;
                         mc.thePlayer.motionX = 0;
                         mc.thePlayer.motionZ = 0;
                     }
                    break;
                }

                case "Verus": {
                    switch (verusSettings.getCurrentMode()) {
                        case "Fast": {
                            MovementUtil.setSpeed(mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.353 : 0.3);
                            if (mc.thePlayer.hurtTime <= 9 && mc.thePlayer.hurtTime >= 1) {
                                int boostValue = 1;
                                MovementUtil.setSpeed(boostValue);
                            }
                            if (mc.thePlayer.onGround && !mc.thePlayer.isCollidedHorizontally) {
                                mc.thePlayer.jump();
                            }
                            mc.thePlayer.cameraYaw = 0.1f;
                            int tickCounter = 0;
                            ++tickCounter;
                            mc.thePlayer.motionY = 0;
                            MovementUtil.setSpeed(0.2555);
                            event.setOnGround(true);
                            if (mc.thePlayer.ticksExisted % 10 == 0) {
                                float sdp = MathUtils.getRandomFloat(0.14f, 0.04f);
                                Timer.timerSpeed = sdp;
                                ChatUtil.notify(String.valueOf(sdp));
                            } else {
                                float sus = MathUtils.getRandomFloat(5.6421f, 4.8421f);
                                Timer.timerSpeed = sus;
                                ChatUtil.notify(String.valueOf(sus));
                            }
                            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                                if (tickCounter > 30) {
                                    mc.thePlayer.jump();
                                }
                            }
                            if (tickCounter > 30) {
                                tickCounter = 0;
                            }
                            break;
                        }

                        case "Funny": {

                        }
                    }
                }

                case "Grim": {
                    break;
                }

                case "Vulcan": {
                      mc.thePlayer.sendPlayerAbilities();
                      mc.thePlayer.capabilities.allowFlying = true;
                      mc.thePlayer.capabilities.isFlying = true;
                      event.setOnGround(true);
                      break;
                    }

                case "NCP": {
                   break;
                }

                case "Negativity": {
                    switch (negativitySettings.getCurrentMode()) {
                        case "Fast": {
                            if (mc.thePlayer.onGround) {
                                mc.thePlayer.jump();
                                ChatUtil.error("§rPlease wait 1 more second before flying!");
                            }
                            if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
                                mc.thePlayer.motionY = 0;
                                mc.thePlayer.motionX = 0;
                                mc.thePlayer.motionZ = 0;
                                MovementUtil.setSpeed(0);
                                ChatUtil.error("§rPlease wait 1 more second before flying!");
                                if (timerUtil.hasTimeElapsed(10, true)) {
                                    mc.thePlayer.jump();
                                }
                            }
                            mc.thePlayer.capabilities.allowFlying = true;
                            mc.thePlayer.sendPlayerAbilities();
                            if (timerUtil.hasTimeElapsed(2000, true)) {
                                ChatUtil.error("§4! §cTimer flags detected §4!");
                            } else {
                                if (timerUtil.hasTimeElapsed(1000, true)) {
                                    ChatUtil.warn("§rWe recommend stopping because the AntiCheat §4will flag!");
                                    ChatUtil.warn("Wait until this message disappears before flying again!");
                                } else {
                                    if (timerUtil.hasTimeElapsed(1000, true)) {
                                        ChatUtil.error("§4Sending damage packet failed!");
                                        ChatUtil.error("§4This is a catastrophic error!");
                                        ChatUtil.error("§4Please report this bug!");
                                    }
                                }
                                if (!damage) {
                                    for (int i = 0; i < 15; i++) {
                                        mc.thePlayer.hurtTime = 30;
                                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.3, mc.thePlayer.posZ, false));
                                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ, false));
                                    }
                                }
                                damage = true;
                                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ, true));
                                mc.thePlayer.motionY = 0;
                                MovementUtil.setSpeed(9.9);
                                mc.thePlayer.setSprinting(true);
                                mc.thePlayer.sendPlayerAbilities();
                                mc.thePlayer.onGround = true;
                                break;
                            }
                            mc.thePlayer.motionY = 0;
                            MovementUtil.setSpeed(9.9);
                            mc.thePlayer.setSprinting(true);
                            mc.thePlayer.onGround = true;
                            break;
                        }
                        case "Infinite": {
                            mc.thePlayer.motionY = 0;
                        }
                        break;
                    }
                    break;
                }

                case "Mineland": {
                    switch (minelandMode.getCurrentMode()) {
                        case "Jank": {
                            if (timerUtil.hasTimeElapsed(mineland ? 500 : 800, true)) {
                                mineland = !mineland;
                            }
                            if (mineland) {
                                if (mc.thePlayer.ticksExisted % 2 == 0) {
                                    mc.thePlayer.jump();
                                    Timer.timerSpeed = 2.5F;
                                }

                                if (MovementUtil.getSpeed() > 3) {
                                    MovementUtil.setSpeed(20);
                                    Timer.timerSpeed = 2.5F;
                                }
                            } else {
                                if (mc.thePlayer.ticksExisted % 4 == 0) {
                                    mc.thePlayer.motionY = -(Math.random()) / 20;
                                    Timer.timerSpeed = 2.5F;
                                }
                            }
                        }

                        case "Fling": {
                            if (!teleported) {
                                if (mc.thePlayer.ticksExisted % 3 == 0) {
                                    MovementUtil.setSpeed(1);
                                    Timer.timerSpeed = 1f;
                                }
                                final double yaw = MovementUtil.getDirection();
                                final double pitch = MovementUtil.getDirection();
                                final double speed = 1;

                                event.setY(event.getY() - 0.9 + (mc.thePlayer.ticksExisted % 3 == 0 ? (0.42F) : 0));
                                event.setX(event.getX() + MathHelper.sin((float) yaw) * speed * 5);
                                event.setZ(event.getZ() - MathHelper.cos((float) yaw) * speed * 5);
                                event.setX(event.getX() + MathHelper.sin((float) pitch) * speed * 5);
                                event.setZ(event.getZ() - MathHelper.cos((float) pitch) * speed * 5);
                            } else {
                                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                                MovementUtil.setSpeed(3);
                                Timer.timerSpeed = 1f;
                                final double yaw = MovementUtil.getDirection();
                                final double pitch = MovementUtil.getDirection();
                                final double speed = 1;

                                event.setY(event.getY() - 0.8 + (mc.thePlayer.ticksExisted % 10 == 0 ? (0.42F) : 0));
                                event.setX(event.getX() + MathHelper.sin((float) yaw) * speed);
                                event.setZ(event.getZ() - MathHelper.cos((float) yaw) * speed);
                                event.setX(event.getX() + MathHelper.sin((float) pitch) * speed);
                                event.setZ(event.getZ() - MathHelper.cos((float) pitch) * speed);
                            }
                            break;
                        }
                    }
                }
            }
        }
    };

    public void vi() {
        if (isKeyDown(KEY_SPACE)) {
            mc.thePlayer.motionY = 1;
        }
        if (isKeyDown(KEY_LSHIFT)) {
            mc.thePlayer.motionY = -1;
        }
        MovementUtil.setSpeed(9.972);
        mc.thePlayer.motionY = 0f;
        mc.thePlayer.setSprinting(true);
        mc.thePlayer.sendPlayerAbilities();
        mc.thePlayer.onGround = false;
    }

    private final IEventListener<PacketReceiveEvent> onReceivePacket = event -> {
        Packet<?> packet = event.getPacket();
        // no skidding fdp!
        switch (mode.getCurrentMode()) {
            case "Mineland": {
                if(minelandMode.isMode("Fling")) {
                    if(packet instanceof S08PacketPlayerPosLook && !teleported) {
                        event.cancel();
                    } else if(packet instanceof S12PacketEntityVelocity) {
                        final S12PacketEntityVelocity wrapper = (S12PacketEntityVelocity) packet;

                        if (wrapper.getEntityID() == mc.thePlayer.getEntityId() && wrapper.motionY / 2000D > 0.1) {
                            MovementUtil.setSpeed(3);
                            Timer.timerSpeed = 1f;
                            teleported = true;
                        }
                    }
                }
                break;
            }
        }

        if(mode.isMode("Mineland") || mode.isMode("Watchdog") || mode.isMode("Negativity damage fast") || mode.isMode("HvH Mode")) {

            if(packet instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity teleport = (S12PacketEntityVelocity) packet;
                if(teleport.getEntityID() == mc.thePlayer.getEntityId()) {
                    mineland = true;
                    // why u deobf
                    if(mode.isMode("Watchdog") && teleport.getMotionY() > 0.2) {
                        watchdog = true;
                        if(mc.thePlayer.fallDistance > 0.2) {
                            for(int i = 0; i < 12; i++) {
                                mc.thePlayer.jump();
                            }
                        }
                        timerUtil.reset();
                    }
                }
            }
        }
    };

    @Override
    public void onEnable() {
        if (mode.isMode("Vulcan")) {
            ChatUtil.warn("Dont abuse the fly!");
        }
        if(mode.isMode("BlocksMC")) {
            ChatUtil.warn("Please be under a block to start flying!");
        }
        packetList.clear();
        damage = false;
        lastMovementYaw = -1;
        timerUtil.reset();
        if (mc.thePlayer == null)
            return;
        serverX = mc.thePlayer.posX;
        serverY = mc.thePlayer.posY;
        serverZ = mc.thePlayer.posZ;
        teleported = false;
        // I love ejaculating into peoples mouths!
        if(mode.isMode("Mineland") && (minelandMode.isMode("Fling Fast") || minelandMode.isMode("Fling Slow"))) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
        MovementUtil.setSpeed(0);
        if(!packetList.isEmpty()) {
            packetList.forEach(mc.getNetHandler()::addToSendQueueNoEvent);
            packetList.clear();
            mc.thePlayer.hurtTime = 0;
        }
        Timer.timerSpeed = 1;
        mc.thePlayer.capabilities.isFlying = false;
        if(!mc.thePlayer.capabilities.isCreativeMode) mc.thePlayer.capabilities.allowFlying = false;
        mineland = false;
        watchdog = false;
        blink = false;
        speed = 0;

        super.onDisable();
    }
    private final IEventListener<UpdateEvent> onUpdateEvent = event -> setSuffix(mode.getCurrentMode());

    public IEventListener<MotionEvent> getOnMotionEvent() {
        return onMotionEvent;
    }
}