package me.aquavit.liquidsense.module.modules.render;

import com.google.common.collect.Maps;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render2DEvent;
import me.aquavit.liquidsense.event.events.Render3DEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.entity.EntityUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Map;

@ModuleInfo(name = "PointerESP", description = "PointerESP", category = ModuleCategory.RENDER)
public class PointerESP extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"CanSeen","Normal"},"CanSeen");
    private final IntegerValue size = new IntegerValue("Size", 10, 5, 25);
    private final IntegerValue radius = new IntegerValue("Radius", 45, 10, 200);
    private int alpha;
    private boolean plus_or_minus;
    private final EntityListener entityListener = new EntityListener();
    @Override
    public void onEnable() {
        alpha = 0;
        plus_or_minus = false;
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        entityListener.render3d(event);
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {

        mc.theWorld.loadedEntityList.forEach(o -> {
            if (o instanceof EntityLivingBase && EntityUtils.isSelected(o, true,false)) {
                EntityLivingBase entity = (EntityLivingBase) o;
                Vec3 pos = entityListener.getEntityLowerBounds().get(entity);
                if (pos != null) {
                    if (mc.thePlayer.canEntityBeSeen(entity) && modeValue.get().equalsIgnoreCase("CanSeen") && isOnScreen(pos)) {
                        GlStateManager.pushMatrix();
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                        ScaledResolution sr = new ScaledResolution(this.mc);
                        double twoDscale = sr.getScaleFactor() / Math.pow(sr.getScaleFactor(), 2);
                        GlStateManager.scale(twoDscale, twoDscale, twoDscale);
                        for (Map.Entry<Entity, Vec3> entry : entityListener.entityUpperBounds.entrySet()) {

                            if (entry.getKey() == entity){
                                double x = entry.getValue().xCoord;
                                double y = entry.getValue().yCoord;
                                RenderUtils.drawTracerPointer((float) x, (float)y, size.get(), 2, 1, getColor(entity, 255).getRGB());
                                GlStateManager.resetColor();
                            }

                        }
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                        GlStateManager.popMatrix();
                    }else{
                        int x = (Display.getWidth() / 2) / (mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale);
                        int y = (Display.getHeight() / 2) / (mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale);
                        float yaw = getRotations(entity) - mc.thePlayer.rotationYaw;
                        GL11.glTranslatef(x, y, 0);
                        GL11.glRotatef(yaw, 0, 0, 1);
                        GL11.glTranslatef(-x, -y, 0);
                        RenderUtils.drawTracerPointer(x, y - radius.get(), size.get(), 2, 1, getColor(entity, 255).getRGB());
                        GlStateManager.resetColor();
                        GL11.glTranslatef(x, y, 0);
                        GL11.glRotatef(-yaw, 0, 0, 1);
                        GL11.glTranslatef(-x, -y, 0);
                    }

                }
            }
        });
    }

    private boolean isOnScreen(Vec3 pos) {
        if (pos.xCoord > -1 && pos.zCoord < 1)
            return pos.xCoord / (mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale) >= 0 && pos.xCoord / (mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale) <= Display.getWidth() && pos.yCoord / (mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale) >= 0 && pos.yCoord / (mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale) <= Display.getHeight();

        return false;
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

    public static class EntityListener {
        private final Map<Entity, Vec3> entityUpperBounds = Maps.newHashMap();
        private final Map<Entity, Vec3> entityLowerBounds = Maps.newHashMap();

        private void render3d(Render3DEvent event) {
            if (!entityUpperBounds.isEmpty()) {
                entityUpperBounds.clear();
            }
            if (!entityLowerBounds.isEmpty()) {
                entityLowerBounds.clear();
            }
            for (Entity e : mc.theWorld.loadedEntityList) {
                Vec3 bound = getEntityRenderPosition(e);
                bound.add(new Vec3(0, e.height + 0.2, 0));
                Vec3 upperBounds = RenderUtils.to2D(bound.xCoord, bound.yCoord, bound.zCoord), lowerBounds = RenderUtils.to2D(bound.xCoord, bound.yCoord - 2, bound.zCoord);
                if (upperBounds != null && lowerBounds != null) {
                    entityUpperBounds.put(e, upperBounds);
                    entityLowerBounds.put(e, lowerBounds);
                }
            }
        }

        private Vec3 getEntityRenderPosition(Entity entity) {
            double partial = mc.timer.renderPartialTicks;

            double x = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partial) - mc.getRenderManager().viewerPosX;
            double y = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partial) - mc.getRenderManager().viewerPosY;
            double z = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partial) - mc.getRenderManager().viewerPosZ;

            return new Vec3(x, y, z);
        }

        public Map<Entity, Vec3> getEntityLowerBounds() {
            return entityLowerBounds;
        }
    }

}
