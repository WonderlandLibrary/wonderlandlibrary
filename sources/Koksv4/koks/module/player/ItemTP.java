package koks.module.player;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.pathfinder.PathFinderHelper;
import koks.api.registry.module.Module;
import koks.api.utils.TimeHelper;
import koks.event.*;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Module.Info(name = "ItemTP", description = "You can teleport items to you", category = Module.Category.PLAYER)
public class ItemTP extends Module {

    @Value(name = "Reach", minimum = 10, maximum = 500)
    int reach = 100;

    final TimeHelper timeHelper = new TimeHelper();

    ArrayList<Vec3i> positions = new ArrayList<>();

    public final PathFinderHelper pathFinderHelper = new PathFinderHelper();

    boolean teleportBack, wantTeleport;

    double[] startPos;

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof final MouseOverEvent mouseOverEvent) {
            mouseOverEvent.setRange(reach);
            mouseOverEvent.setRangeCheck(false);
        }

        if (event instanceof final BlockReachEvent blockReachEvent) {
            if (mc.objectMouseOver != null) {
                if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    blockReachEvent.setRange(reach);
                }
            }
        }

        if (event instanceof Render3DEvent) {
            if (timeHelper.hasReached(350))
                positions.clear();
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
        }

        if (event instanceof final PacketEvent packetEvent) {
            final Packet<?> packet = packetEvent.getPacket();
            if (packetEvent.getType() == PacketEvent.Type.RECEIVE) {
                if (packet instanceof S0DPacketCollectItem) {
                    if (!teleportBack && wantTeleport)
                        teleportBack = true;
                }
            }
        }

        if (event instanceof UpdateEvent) {
            if (teleportBack && wantTeleport) {
                /* Teleport Back */
                try {
                    pathFinderHelper.clear();
                    final ArrayList<Vec3i> endPath = pathFinderHelper.findPath(getPlayer().getPositionVector().addVector(0, 1, 0), new Vec3(startPos[0], startPos[1] + 1, startPos[2]), 6, false);
                    endPath.forEach(vec3 -> {
                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(), vec3.getY(), vec3.getZ(), false));
                    });
                    teleportBack = false;
                    wantTeleport = false;
                } catch (NoSuchElementException ignore) {
                }
            }

            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit != null && mc.objectMouseOver.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK) && getGameSettings().keyBindAttack.pressed) {
                /* Setting */
                wantTeleport = true;
                teleportBack = false;
                try {
                    final BlockPos pos = mc.objectMouseOver.getBlockPos();
                    pathFinderHelper.clear();
                    getGameSettings().keyBindAttack.pressed = false;
                    startPos = new double[]{getX(), getY(), getZ()};
                    positions.clear();
                    positions = pathFinderHelper.findPath(getPlayer().getPositionVector().addVector(0, 1, 0), new Vec3(pos.getX(), pos.getY() + 1, pos.getZ()), 6, true);

                    /* Teleport to Position */
                    positions.forEach(vec3i -> {
                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(vec3i.getX(), vec3i.getY(), vec3i.getZ(), false));
                    });
                    timeHelper.reset();
                } catch (NoSuchElementException ignore) {
                }
            }
        }
    }

    @Override
    public void onEnable() {
        teleportBack = false;
        wantTeleport = false;
    }

    @Override
    public void onDisable() {

    }
}
