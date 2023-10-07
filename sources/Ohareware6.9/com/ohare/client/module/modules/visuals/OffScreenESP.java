package com.ohare.client.module.modules.visuals;

import com.google.common.collect.Maps;
import com.ohare.client.event.events.render.Render2DEvent;
import com.ohare.client.event.events.render.Render3DEvent;
import com.ohare.client.module.Module;
import com.ohare.client.module.modules.combat.AntiBot;
import com.ohare.client.utils.MathUtils;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.value.impl.BooleanValue;
import com.ohare.client.utils.value.impl.NumberValue;
import com.sun.javafx.geom.Vec3d;
import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Map;


/**
 * made by oHare for oHareWare
 *
 * @since 7/23/2019
 **/
public class OffScreenESP extends Module {
    private int alpha;
    private boolean plus_or_minus;
    private BooleanValue twod = new BooleanValue("2D", true);
    private BooleanValue threed = new BooleanValue("3D", true);
    private NumberValue<Float> size = new NumberValue<>("Size", 10f, 5f, 25f, 0.1f);
    private NumberValue<Float> widthDiv = new NumberValue<>("Width Div", 2.2f, 1.8f, 3f, 0.01f);
    private NumberValue<Float> heightDiv = new NumberValue<>("Height Div", 1.3f, 1f, 2f, 0.01f);
    private NumberValue<Integer> radius = new NumberValue<>("Radius", 45, 10, 200, 1);
    private BooleanValue showOnPlayers = new BooleanValue("Show on Players", true);
    private BooleanValue fade = new BooleanValue("Fade",false);
    private EntityListener entityListener = new EntityListener();
    private BooleanValue players = new BooleanValue("Players", true);
    private BooleanValue animals = new BooleanValue("Animals", true);
    private BooleanValue mobs = new BooleanValue("Mobs", false);
    private BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private BooleanValue passives = new BooleanValue("Passives", true);

    public OffScreenESP() {
        super("OffScreenESP", Category.VISUALS, new Color(0x76DBFF).getRGB());
        setRenderlabel("OffScreen ESP");
        setDescription("OffScreen ESP.");
        addValues(players, animals, mobs, invisibles, passives,twod, threed,fade, showOnPlayers, radius, size, widthDiv, heightDiv);
    }

    @Override
    public void onEnable() {
        alpha = 0;
        plus_or_minus = false;
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (mc.theWorld != null && threed.isEnabled()) mc.theWorld.playerEntities.forEach(player -> {
            if (player != mc.thePlayer && !AntiBot.getBots().contains(player)) {
                drawAngle(event.getPartialTicks(), -getRotations(player), getColor(player,alpha));
            }
        });
        entityListener.render3d(event);
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        if(fade.isEnabled()) {
            if (alpha <= 60.f || alpha >= 255.f) plus_or_minus = !plus_or_minus;
            if (plus_or_minus) alpha++;
            else alpha--;
            alpha = (int) MathUtils.clamp(alpha, 255, 60);
        } else {
            alpha = 255;
        }
        if (!twod.isEnabled()) return;
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        mc.theWorld.loadedEntityList.forEach(o -> {
            if (o instanceof EntityLivingBase &&  isValid((EntityLivingBase)o)) {
                EntityLivingBase entity = (EntityLivingBase) o;
                Vec3d pos = entityListener.getEntityLowerBounds().get(entity);

                if (pos != null) {
                    if (isOnScreen(pos)) {
                        float x = (float) pos.x / scaledResolution.getScaleFactor();
                        float y = (float) pos.y / scaledResolution.getScaleFactor();
                        if (showOnPlayers.isEnabled()) {
                            RenderUtil.drawTracerPointer(x, y, size.getValue(), widthDiv.getValue(), heightDiv.getValue(), getColor(entity,255).getRGB());
                        }
                    } else {

                        int x = (Display.getWidth() / 2) / (mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale);
                        int y = (Display.getHeight() / 2) / (mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale);
                        float yaw = getRotations(entity) - mc.thePlayer.rotationYaw;
                        // Rotate around crosshair
                        GL11.glTranslatef(x, y, 0);
                        GL11.glRotatef(yaw, 0, 0, 1);
                        GL11.glTranslatef(-x, -y, 0);

                        RenderUtil.drawTracerPointer(x, y - radius.getValue(), size.getValue(), widthDiv.getValue(), heightDiv.getValue(), getColor(entity,alpha).getRGB());

                        // Fix rotate around crosshair
                        GL11.glTranslatef(x, y, 0);
                        GL11.glRotatef(-yaw, 0, 0, 1);
                        GL11.glTranslatef(-x, -y, 0);
                    }
                }
            }
        });
    }

    private boolean isOnScreen(Vec3d pos) {
        if (pos.x > -1 && pos.z < 1)
            return pos.x / (mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale) >= 0 && pos.x / (mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale) <= Display.getWidth() && pos.y / (mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale) >= 0 && pos.y / (mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale) <= Display.getHeight();

        return false;
    }

    private void drawAngle(float partialTicks, float angle, Color color) {
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        mc.entityRenderer.orientCamera(partialTicks);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1.5f);
        GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        {
            RenderUtil.color(color.getRGB());
            GL11.glVertex3d(-0.20, 0.0, 1.0);
            GL11.glVertex3d(0.0, 0.0, 1.25);
            GL11.glVertex3d(0.20, 0.0, 1.0);
            GL11.glVertex3d(-0.20, 0.0, 1.0);
            GL11.glEnd();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);
    }

    private boolean isValid(EntityLivingBase entity) {
        return !AntiBot.getBots().contains(entity) && entity != mc.thePlayer && isValidType(entity) && entity.getEntityId() != -1488 && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (players.isEnabled() && entity instanceof EntityPlayer) || (mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime) || (passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (animals.isEnabled() && entity instanceof EntityAnimal));
    }

    private float getRotations(EntityLivingBase ent) {
        final double x = ent.posX - mc.thePlayer.posX;
        final double z = ent.posZ - mc.thePlayer.posZ;
        final float yaw = (float) (-(Math.atan2(x, z) * 57.29577951308232));
        return yaw;
    }

    private Color getColor(EntityLivingBase player, int alpha) {
        float f = mc.thePlayer.getDistanceToEntity(player);
        float f1 = 40;
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        final Color clr = new Color(Color.HSBtoRGB(f2 / 3.0F, 1.0F, 1.0F) | 0xFF000000);
        return new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), alpha);
    }

    public class EntityListener {
        private Map<Entity, Vec3d> entityUpperBounds = Maps.newHashMap();
        private Map<Entity, Vec3d> entityLowerBounds = Maps.newHashMap();

        private void render3d(Render3DEvent event) {
            if (!entityUpperBounds.isEmpty()) {
                entityUpperBounds.clear();
            }
            if (!entityLowerBounds.isEmpty()) {
                entityLowerBounds.clear();
            }
            for (Entity e : mc.theWorld.loadedEntityList) {
                Vec3d bound = getEntityRenderPosition(e);
                bound.add(new Vec3d(0, e.height + 0.2, 0));
                Vec3d upperBounds = RenderUtil.to2D(bound.x, bound.y, bound.z), lowerBounds = RenderUtil.to2D(bound.x, bound.y - 2, bound.z);
                if (upperBounds != null && lowerBounds != null) {
                    entityUpperBounds.put(e, upperBounds);
                    entityLowerBounds.put(e, lowerBounds);
                }
            }
        }

        private Vec3d getEntityRenderPosition(Entity entity) {
            double partial = mc.timer.renderPartialTicks;

            double x = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partial) - mc.getRenderManager().viewerPosX;
            double y = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partial) - mc.getRenderManager().viewerPosY;
            double z = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partial) - mc.getRenderManager().viewerPosZ;

            return new Vec3d(x, y, z);
        }

        public Map<Entity, Vec3d> getEntityUpperBounds() {
            return entityUpperBounds;
        }

        public Map<Entity, Vec3d> getEntityLowerBounds() {
            return entityLowerBounds;
        }
    }
}
