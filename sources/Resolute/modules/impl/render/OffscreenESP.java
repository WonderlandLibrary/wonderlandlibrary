// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import net.minecraft.client.renderer.entity.RenderManager;
import java.util.Iterator;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import vip.Resolute.ui.click.skeet.SkeetUI;
import org.lwjgl.opengl.Display;
import vip.Resolute.util.render.RenderUtils;
import vip.Resolute.util.player.RotationUtils;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.Vec3;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.events.impl.EventRender2D;
import vip.Resolute.events.impl.EventRender3D;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import java.awt.Color;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.modules.Module;

public class OffscreenESP extends Module
{
    public ColorSetting colorProp;
    public NumberSetting size;
    public NumberSetting radius;
    private EntityListener entityListener;
    private int alpha;
    private boolean plus_or_minus;
    
    public OffscreenESP() {
        super("OffscreenESP", 0, "", Category.RENDER);
        this.colorProp = new ColorSetting("Color", new Color(0, 104, 161));
        this.size = new NumberSetting("Size", 5.0, 5.0, 10.0, 0.1);
        this.radius = new NumberSetting("Radius", 100.0, 10.0, 100.0, 5.0);
        this.entityListener = new EntityListener();
        this.addSettings(this.colorProp, this.size, this.radius);
    }
    
    @Override
    public void onEnable() {
        this.alpha = 0;
        this.plus_or_minus = false;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventRender3D) {
            this.entityListener.render3d((EventRender3D)e);
        }
        if (e instanceof EventRender2D) {
            final EventRender2D event = (EventRender2D)e;
            final ScaledResolution lr = new ScaledResolution(OffscreenESP.mc);
            final float middleX = lr.getScaledWidth() / 2.0f;
            final float middleY = lr.getScaledHeight() / 2.0f;
            final float pt = event.getPartialTicks();
            EntityLivingBase entity;
            Vec3 pos;
            float arrowSize;
            float alpha;
            int color;
            final float n;
            final float n2;
            final float n3;
            float yaw;
            float offset;
            OffscreenESP.mc.theWorld.loadedEntityList.forEach(o -> {
                if (o instanceof EntityLivingBase && this.isValid(o)) {
                    entity = o;
                    pos = this.entityListener.getEntityLowerBounds().get(entity);
                    if (pos != null && !this.isOnScreen(pos)) {
                        GL11.glPushMatrix();
                        arrowSize = (float)this.size.getValue();
                        alpha = Math.max(1.0f - OffscreenESP.mc.thePlayer.getDistanceToEntity(entity) / 30.0f, 0.3f);
                        color = this.colorProp.getColor();
                        GL11.glTranslatef(n + 0.5f, n2, 1.0f);
                        yaw = (float)(RenderUtils.interpolate(RotationUtils.getYawToEntity(entity, true), RotationUtils.getYawToEntity(entity, false), n3) - RenderUtils.interpolate(OffscreenESP.mc.thePlayer.prevRotationYaw, OffscreenESP.mc.thePlayer.rotationYaw, n3));
                        GL11.glRotatef(yaw, 0.0f, 0.0f, 1.0f);
                        GL11.glTranslatef(0.0f, (float)(-this.radius.getValue() - this.size.getValue()), 0.0f);
                        GL11.glDisable(3553);
                        GL11.glBegin(5);
                        GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF), (byte)(alpha * 255.0f));
                        GL11.glVertex2f(0.0f, 0.0f);
                        offset = (float)(int)(arrowSize / 3.0f);
                        GL11.glVertex2f(-arrowSize + offset, arrowSize);
                        GL11.glVertex2f(arrowSize - offset, arrowSize);
                        GL11.glEnd();
                        GL11.glEnable(3553);
                        GL11.glPopMatrix();
                    }
                }
            });
        }
    }
    
    private boolean isOnScreen(final Vec3 pos) {
        return pos.xCoord > -1.0 && pos.zCoord < 1.0 && pos.xCoord / ((OffscreenESP.mc.gameSettings.guiScale == 0) ? 1 : OffscreenESP.mc.gameSettings.guiScale) >= 0.0 && pos.xCoord / ((OffscreenESP.mc.gameSettings.guiScale == 0) ? 1 : OffscreenESP.mc.gameSettings.guiScale) <= Display.getWidth() && pos.yCoord / ((OffscreenESP.mc.gameSettings.guiScale == 0) ? 1 : OffscreenESP.mc.gameSettings.guiScale) >= 0.0 && pos.yCoord / ((OffscreenESP.mc.gameSettings.guiScale == 0) ? 1 : OffscreenESP.mc.gameSettings.guiScale) <= Display.getHeight();
    }
    
    private boolean isValid(final EntityLivingBase entity) {
        return entity != OffscreenESP.mc.thePlayer && this.isValidType(entity) && entity.getEntityId() != -1488 && entity.isEntityAlive() && (!entity.isInvisible() || SkeetUI.isInvisibles());
    }
    
    private boolean isValidType(final EntityLivingBase entity) {
        return SkeetUI.isPlayers() && entity instanceof EntityPlayer;
    }
    
    public class EntityListener
    {
        private Map<Entity, Vec3> entityUpperBounds;
        private Map<Entity, Vec3> entityLowerBounds;
        
        public EntityListener() {
            this.entityUpperBounds = (Map<Entity, Vec3>)Maps.newHashMap();
            this.entityLowerBounds = (Map<Entity, Vec3>)Maps.newHashMap();
        }
        
        private void render3d(final EventRender3D event) {
            if (!this.entityUpperBounds.isEmpty()) {
                this.entityUpperBounds.clear();
            }
            if (!this.entityLowerBounds.isEmpty()) {
                this.entityLowerBounds.clear();
            }
            for (final Entity e : Module.mc.theWorld.loadedEntityList) {
                final Vec3 bound = this.getEntityRenderPosition(e);
                bound.add(new Vec3(0.0, e.height + 0.2, 0.0));
                final Vec3 upperBounds = RenderUtils.to2D(bound.xCoord, bound.yCoord, bound.zCoord);
                final Vec3 lowerBounds = RenderUtils.to2D(bound.xCoord, bound.yCoord - 2.0, bound.zCoord);
                if (upperBounds != null && lowerBounds != null) {
                    this.entityUpperBounds.put(e, upperBounds);
                    this.entityLowerBounds.put(e, lowerBounds);
                }
            }
        }
        
        private Vec3 getEntityRenderPosition(final Entity entity) {
            final double partial = Module.mc.timer.renderPartialTicks;
            final double n = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partial;
            Module.mc.getRenderManager();
            final double x = n - RenderManager.viewerPosX;
            final double n2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partial;
            Module.mc.getRenderManager();
            final double y = n2 - RenderManager.viewerPosY;
            final double n3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partial;
            Module.mc.getRenderManager();
            final double z = n3 - RenderManager.viewerPosZ;
            return new Vec3(x, y, z);
        }
        
        public Map<Entity, Vec3> getEntityLowerBounds() {
            return this.entityLowerBounds;
        }
    }
}
