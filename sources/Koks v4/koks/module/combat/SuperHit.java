package koks.module.combat;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.pathfinder.PathFinderHelper;
import koks.api.registry.module.Module;
import koks.api.utils.MovementUtil;
import koks.api.manager.value.annotation.Value;
import koks.api.utils.RotationUtil;
import koks.api.utils.TimeHelper;
import koks.event.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "SuperHit", description = "You can hit Entities from a far distance", category = Module.Category.COMBAT)
public class SuperHit extends Module {

    @Value(name = "Reach", minimum = 10, maximum = 500)
    int reach = 100;

    @Value(name = "StepDistance", minimum = 1, maximum = 6)
    int stepDistance = 6;

    @Value(name = "AllowGround")
    boolean allowGround = false;

    @Value(name = "Jump")
    boolean jump = false;

    @Value(name = "Mode", modes = {"Vanilla", "Karhu2.1.9 162"})
    String mode = "Vanilla";

    final TimeHelper timeHelper = new TimeHelper();

    ArrayList<Vec3i> positions = new ArrayList<>();

    public final PathFinderHelper pathFinderHelper = new PathFinderHelper();

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof final MouseOverEvent mouseOverEvent) {
            mouseOverEvent.setRange(reach);
            mouseOverEvent.setRangeCheck(false);
        }

        if (event instanceof final BlockReachEvent blockReachEvent) {
            if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null) {
                if (isValid(mc.objectMouseOver.entityHit)) {
                    blockReachEvent.setRange(reach);
                }
            }
        }

        if (event instanceof Render3DEvent) {
            if (timeHelper.hasReached(350)) {
                positions.clear();
            }
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

        if (event instanceof UpdateEvent) {
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit != null && mc.objectMouseOver.typeOfHit.equals(MovingObjectPosition.MovingObjectType.ENTITY) && getGameSettings().keyBindAttack.pressed) {
                /* Setting */
                final Entity entity = mc.objectMouseOver.entityHit;
                if (isValid(entity)) {
                    try {
                        pathFinderHelper.clear();
                        getGameSettings().keyBindAttack.pressed = false;
                        final double[] startPos = {getX(), getY(), getZ()};

                        if(mode.equalsIgnoreCase("Karhu2.1.9 162")) {
                            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 0.42, getZ(), true));
                            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(entity.posX, entity.posY, entity.posZ, false));
                            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY(), getZ(), false));
                        }

                        positions.clear();
                        positions = pathFinderHelper.findPath(getPlayer().getPositionVector().addVector(0, 1, 0), new Vec3(entity.posX, entity.posY + 1, entity.posZ), stepDistance, allowGround);

                        if (getPlayer().onGround && jump)
                            getPlayer().jump();

                        /* Teleport to Entity */
                        positions.forEach(vec3i -> sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(vec3i.getX(), vec3i.getY(), vec3i.getZ(), false)));

                        /* Attack */
                        getPlayerController().attackEntity(getPlayer(), entity);

                        /* Teleport Back */
                        pathFinderHelper.clear();
                        final ArrayList<Vec3i> endPath = pathFinderHelper.findPath(getPlayer().getPositionVector().addVector(0, 1, 0), new Vec3(startPos[0], startPos[1] + 1, startPos[2]), stepDistance, allowGround);
                        endPath.forEach(vec3 -> sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(), vec3.getY(), vec3.getZ(), false)));

                        timeHelper.reset();
                    } catch (NoSuchElementException ignore) {
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {

    }

    public boolean isValid(Entity entity) {
        if (entity.isInvisible())
            return false;
        return entity instanceof EntityLivingBase;
    }
}
