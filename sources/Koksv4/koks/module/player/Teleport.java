package koks.module.player;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.pathfinder.PathFinderHelper;
import koks.api.registry.module.Module;
import koks.api.utils.MovementUtil;
import koks.api.utils.TimeHelper;
import koks.api.manager.value.annotation.Value;
import koks.event.*;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Teleport", description = "You can teleport you", category = Module.Category.PLAYER)
public class Teleport extends Module {

    @Value(name = "Mode", modes = {"Vanilla", "Hypixel", "HypixelHover", "Karhu2.1.9 163-PRE"})
    String mode = "Vanilla";

    @Value(name = "Karhu2.1.9 163-PRE Instant", displayName = "Instant")
    boolean karhu219163Instant = false;

    public final PathFinderHelper pathFinderHelper = new PathFinderHelper();

    private final TimeHelper timeHelper = new TimeHelper();

    boolean hypixelBlink, hypixelTeleport, mineplexTeleport, mineplexPacket, karhu219163PREWait, matrixWasWeb, teleported;
    BlockPos teleportPosition;
    List<Vec3i> positions = new ArrayList<>();

    Vec3 deineMutter;

    private final ArrayList<Packet<?>> packets = new ArrayList<>();

    @Override
    @Event.Info
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        switch (name) {
            case "Karhu2.1.9 163-PRE Instant":
                return mode.equalsIgnoreCase("Karhu2.1.9 163-PRE");
        }
        return super.isVisible(value, name);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    public void onEvent(Event event) {
        if (event instanceof final BlockReachEvent blockReachEvent) {
            blockReachEvent.setRange(250);
        }

        if (event instanceof final PacketEvent packetEvent) {
            final Packet<? extends INetHandler> packet = packetEvent.getPacket();
            switch (packetEvent.getType()) {
                case RECEIVE:
                    switch (mode) {
                        case "HypixelHover":
                            if (packet instanceof S08PacketPlayerPosLook) {
                                if (hypixelBlink) {
                                    hypixelBlink = false;
                                    for (int i = 0; i < packets.size(); i++) {
                                        Packet<?> p = packets.get(i);
                                        sendPacket(p);
                                        packets.remove(p);
                                    }
                                    hypixelTeleport = true;
                                }
                            }
                            break;
                        case "Mineplex":
                            if (packet instanceof S39PacketPlayerAbilities) {
                                deineMutter = getPlayer().getPositionVector().addVector(0, 1, 0);
                                mineplexPacket = true;
                            }
                            if (packet instanceof S08PacketPlayerPosLook) {
                                if (mineplexPacket) {
                                    mineplexTeleport = true;
                                    mineplexPacket = false;
                                }
                            }
                            break;
                    }
                    break;
                case SEND:
                    switch (mode) {
                        case "HypixelHover":
                            if (hypixelBlink)
                                if (packet instanceof C03PacketPlayer) {
                                    packets.add(packet);
                                    event.setCanceled(true);
                                }
                            break;
                    }
                    break;
            }
        }

        if (event instanceof Render3DEvent) {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);

            GL11.glColor4f(1F, 1F, 1F, 1F);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            for (Vec3i pos : positions) {
                GL11.glVertex3d(pos.getX() - mc.renderManager.renderPosX, pos.getY() - mc.renderManager.renderPosY, pos.getZ() - mc.renderManager.renderPosZ);
            }
            GL11.glEnd();

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();

            if (teleported) {
                positions.clear();
            }
        }


        if (event instanceof UpdateEvent) {

            switch (mode) {
                case "Karhu2.1.9 163-PRE":
                    if (karhu219163PREWait && getPlayer().fallDistance > 0.2) {
                        karhu219163PREWait = false;
                        if(!karhu219163Instant) {
                            for (int i = 0; i < 2; i++)
                                getPlayer().jump();
                            doKarhu219163PRETeleport();
                        }
                    }
                    break;
                case "Matrix6.2.3": //TODO: https://www.youtube.com/watch?v=O31Z7953rbI
                    if (getPlayer().isInWeb && !matrixWasWeb) {
                        if (timeHelper.hasReached(250)) {
                            positions = pathFinderHelper.findPath(getPlayer().getPositionVector().addVector(0, 4, 0), new Vec3(teleportPosition.getX(), teleportPosition.getY() + 4, teleportPosition.getZ()), 1, false);
                            boolean ground = true;
                            for (Vec3i pos : positions) {
                                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(pos.getX(), pos.getY(), pos.getZ(), ground));
                                ground = !ground;
                            }
                            setPosition(teleportPosition.getX(), teleportPosition.getY(), teleportPosition.getZ());
                            matrixWasWeb = true;
                            timeHelper.reset();
                        }
                    } else {
                        timeHelper.reset();
                    }
                    break;
                case "Mineplex":
                    if (mineplexTeleport && !mineplexPacket) {
                        try {
                            pathFinderHelper.clear();
                            for (Vec3i position : positions = pathFinderHelper.findPath(getPlayer().getPositionVector(), deineMutter, 6, true)) {
                                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(position.getX(), position.getY(), position.getZ(), false));
                            }
                            setPosition(deineMutter.xCoord, deineMutter.yCoord, deineMutter.zCoord);
                        } catch (NoSuchElementException e) {
                            sendMessage("§cNo path found!");
                        }
                        mineplexPacket = false;
                        mineplexTeleport = false;
                    }
                    break;
                case "HypixelHover":
                    if (!getPlayer().onGround) {
                        if (getPlayer().fallDistance != 0) {
                            if (hypixelBlink && teleportPosition != null) {
                                getPlayer().motionY = 0;
                                setMotion(0);
                            }
                            if (getPlayer().motionY < 0 && !hypixelTeleport && teleportPosition != null) {
                                hypixelBlink = true;
                            }
                        }

                        if (teleportPosition != null) {
                            try {
                                positions.clear();
                                pathFinderHelper.clear();
                                positions = pathFinderHelper.findPath(new Vec3(getX(), getY(), getZ()), new Vec3(teleportPosition.getX(), teleportPosition.getY(), teleportPosition.getZ()), 1, true);
                            } catch (NoSuchElementException ignore) {
                            }

                            if (hypixelTeleport && !hypixelBlink) {
                                try {
                                    for (Vec3i position : positions) {
                                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(position.getX(), position.getY(), position.getZ(), true));
                                    }
                                    setPosition(teleportPosition.getX(), teleportPosition.getY(), teleportPosition.getZ());
                                    teleported = true;
                                } catch (NoSuchElementException ignore) {
                                }
                                if (timeHelper.hasReached(1000))
                                    setToggled(false);
                            } else {
                                timeHelper.reset();
                            }

                        }
                    }
                    break;
            }

            if (getGameSettings().keyBindAttack.pressed) {
                getGameSettings().keyBindAttack.pressed = false;
                if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    teleportPosition = mc.objectMouseOver.getBlockPos().add(0, 1, 0);
                    switch (mode) {
                        case "Matrix6.2.3":
                            sendMessage("Please go into a cobweb!");
                            matrixWasWeb = false;
                            break;
                        case "Karhu2.1.9 163-PRE":
                            if(karhu219163Instant) {
                                doKarhu219163PRETeleport();
                                karhu219163PREWait = false;
                            } else {
                                getPlayer().jump();
                                karhu219163PREWait = true;
                            }
                            break;
                        case "Hypixel":
                            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(teleportPosition.getX(), teleportPosition.getY(), teleportPosition.getZ(), false));
                            for (int i = 0; i < 15; i++)
                                if (getPlayer().ticksExisted % 2 != 1)
                                    sendPacket(new C03PacketPlayer(false));
                            break;
                        case "Vanilla":
                            pathFinderHelper.clear();
                            try {
                                positions.clear();
                                positions = pathFinderHelper.findPath(new Vec3(getX(), getY(), getZ()), new Vec3(teleportPosition.getX(), teleportPosition.getY(), teleportPosition.getZ()), 1, true);
                                for (Vec3i position : positions) {
                                    setPosition(position.getX(), position.getY(), position.getZ());
                                }
                                teleported = true;
                            } catch (NoSuchElementException ignore) {
                            }
                            break;
                        case "HypixelHover":
                            if (getPlayer().onGround)
                                getPlayer().jump();
                            break;
                    }
                }
            }
        }
    }

    public void doKarhu219163PRETeleport() {
        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 0.42, getZ(), true));
        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(teleportPosition.getX(), teleportPosition.getY(), teleportPosition.getZ(), false));
        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY(), getZ(), false));
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        karhu219163PREWait = false;
        teleportPosition = null;
        hypixelBlink = false;
        hypixelTeleport = false;
        mineplexTeleport = false;
        mineplexPacket = false;
        matrixWasWeb = true;
        positions.clear();
    }
}
