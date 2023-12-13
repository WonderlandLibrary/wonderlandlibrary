package me.nyan.flush.module.impl.combat;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event3D;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.event.impl.EventWorldChange;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.notifications.Notification;
import me.nyan.flush.utils.movement.PathFinder;
import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.other.Timer;
import me.nyan.flush.utils.player.PlayerUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class TPAura extends Module {
    private final Timer timer = new Timer();
    private ArrayList<Vec3> path;
    public EntityLivingBase target;

    private final NumberSetting maxCps = new NumberSetting("Max CPS", this, 2, 0, 20, 0.5),
            minCps = new NumberSetting("Min CPS", this, 0.5, 0, 20, 0.5),
            reach = new NumberSetting("Reach", this, 30, 6, 200, 1);
    private final BooleanSetting throughWalls = new BooleanSetting("Through Walls", this, true),
            noSwing = new BooleanSetting("No Swing", this, false),
            targetEsp = new BooleanSetting("Target ESP", this, true),
            players = new BooleanSetting("Players", this, true),
            creatures = new BooleanSetting("Creatures", this, false),
            villagers = new BooleanSetting("Villagers", this, false),
            invisibles = new BooleanSetting("Invisibles", this, false),
            ignoreTeam = new BooleanSetting("Ignore Team", this, false),
            autoDisable = new BooleanSetting("Auto Disable", this, false);

    public TPAura() {
        super("TPAura", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        target = null;
    }

    @Override
    public void onDisable() {
        target = null;
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        ArrayList<Entity> entities = mc.theWorld.loadedEntityList.stream().filter(
                entity -> entity instanceof EntityLivingBase && isValid((EntityLivingBase) entity))
                .sorted(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)))
                .collect(Collectors.toCollection(ArrayList::new));

        if (!entities.isEmpty() && target == null) {
            target = (EntityLivingBase) entities.get(MathUtils.getRandomNumber(0, entities.size()));
        }

        if (!isValid(target)) {
            target = null;
        }

        if (target == null || !timer.hasTimeElapsed(1000 / MathUtils.getRandomNumber(maxCps.getValueInt(), minCps.getValueInt()),
                                                    true)) return;

        path = PathFinder.findPathTo(target.getPositionVector().addVector(0, 2, 0), 8);

        for (Vec3 vec3 : path) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord,
                                                                                              true));
        }

        if (!noSwing.getValue()) {
            mc.thePlayer.swingItem();
        }

        mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));

        Collections.reverse(path);

        for (Vec3 vec3 : path) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord,
                                                                                              true));
        }
    }

    @SubscribeEvent
    public void onWorldChange(EventWorldChange e) {
        if (!autoDisable.getValue()) return;

        disable();
        flush.getNotificationManager().show(Notification.Type.INFO, name, name + " was " +
                EnumChatFormatting.RED + "disabled " + EnumChatFormatting.RESET + "because you teleported.");
    }

    @SubscribeEvent
    public void onRender3D(Event3D e) {
        /*
        if (path != null) {
            mc.entityRenderer.orientCamera(e.getPartialTicks());
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.color(1,1,1,1);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(2F);

            GL11.glBegin(GL11.GL_LINE_STRIP);
            for (Vec3 vec : path) {
                GL11.glVertex3d(
                        vec.xCoord - mc.getRenderManager().renderPosX,
                        vec.yCoord - mc.getRenderManager().renderPosY,
                        vec.zCoord - mc.getRenderManager().renderPosZ
                );
            }
            GL11.glEnd();

            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
        }

         */

        if (!targetEsp.getValue()) return;

        double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * e.getPartialTicks() - mc.getRenderManager().renderPosX;
        double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * e.getPartialTicks() - mc.getRenderManager().renderPosY;
        double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * e.getPartialTicks() - mc.getRenderManager().renderPosZ;

        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();

        drawESPBox(target, x, y, z, target.hurtTime > 0 && target.hurtTime < 14 ? 0x5000FF00 : 0x50FF0000);
        drawESPBoxOutline(target, x, y, z, target.hurtTime > 0 && target.hurtTime < 14 ? 0xFF00FF00 : 0xFFFF0000);

        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.color(1, 1, 1, 1);
    }

    private void drawESPBoxOutline(Entity entity, double x, double y, double z, int color) {
        AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                entityBoundingBox.minX - entity.posX + x,
                entityBoundingBox.minY - entity.posY + y + 0.05D,
                entityBoundingBox.minZ - entity.posZ + z,
                entityBoundingBox.maxX - entity.posX + x,
                entityBoundingBox.maxY - entity.posY + y + 0.15D,
                entityBoundingBox.maxZ - entity.posZ + z
        );

        GL11.glLineWidth(1F);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        RenderUtils.drawBoundingBoxOutline(axisAlignedBB, color);
    }

    private void drawESPBox(Entity entity, double x, double y, double z, int color) {
        AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                entityBoundingBox.minX - entity.posX + x,
                entityBoundingBox.minY - entity.posY + y + 0.05D,
                entityBoundingBox.minZ - entity.posZ + z,
                entityBoundingBox.maxX - entity.posX + x,
                entityBoundingBox.maxY - entity.posY + y + 0.15D,
                entityBoundingBox.maxZ - entity.posZ + z
        );

        RenderUtils.fillBoundingBox(axisAlignedBB, color);
    }

    public boolean isValid(EntityLivingBase entity) {
        return PlayerUtils.isValid(entity, players.getValue(), creatures.getValue(), villagers.getValue(), invisibles.getValue(),
                                   ignoreTeam.getValue()) && Math.abs(entity.posY - mc.thePlayer.posY) < 5 &&
                entity.getDistanceToEntity(mc.thePlayer) <= reach.getValue() &&
                (throughWalls.getValue() || mc.thePlayer.canEntityBeSeen(entity));
    }
}
