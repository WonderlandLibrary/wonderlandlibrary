// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.function.Consumer;
import vip.Resolute.events.impl.EventUpdate;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import vip.Resolute.events.impl.EventRender3D;
import java.util.Random;
import vip.Resolute.util.world.LocationUtils;
import vip.Resolute.util.render.TranslationUtils;
import vip.Resolute.events.impl.EventLivingUpdate;
import vip.Resolute.events.Event;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import java.util.HashMap;
import vip.Resolute.modules.Module;

public class DamageParticles extends Module
{
    private HashMap<EntityLivingBase, Float> healthMap;
    private List<Particles> particles;
    float anim;
    
    public DamageParticles() {
        super("DamageParticles", 0, "Displays health particles", Category.RENDER);
        this.healthMap = new HashMap<EntityLivingBase, Float>();
        this.particles = new ArrayList<Particles>();
        this.anim = 1320.0f;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventLivingUpdate) {
            final EntityLivingBase entity = (EntityLivingBase)((EventLivingUpdate)e).getEntity();
            if (entity == DamageParticles.mc.thePlayer) {
                return;
            }
            if (!this.healthMap.containsKey(entity)) {
                this.healthMap.put(entity, entity.getHealth());
            }
            this.anim = TranslationUtils.moveUD(this.anim, 0.0f, 0.08f, 0.08f);
            final float floatValue = this.healthMap.get(entity);
            final float health = entity.getHealth();
            if (floatValue != health) {
                String text;
                if (floatValue - health < 0.0f) {
                    text = "§a" + roundToPlace((floatValue - health) * -1.0f, 1);
                }
                else {
                    text = "§e" + roundToPlace(floatValue - health, 1);
                }
                final LocationUtils location = new LocationUtils(entity);
                location.setY(entity.getEntityBoundingBox().minY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) / 2.0);
                location.setX(location.getX() - 0.5 + new Random(System.currentTimeMillis()).nextInt(5) * 0.1);
                location.setZ(location.getZ() - 0.5 + new Random(System.currentTimeMillis() + 1L).nextInt(5) * 0.01);
                this.particles.add(new Particles(location, text));
                this.healthMap.remove(entity);
                this.healthMap.put(entity, entity.getHealth());
            }
        }
        if (e instanceof EventRender3D) {
            final EventRender3D event = (EventRender3D)e;
            for (final Particles p : this.particles) {
                final double x = p.location.getX();
                DamageParticles.mc.getRenderManager();
                final double n4 = x;
                DamageParticles.mc.getRenderManager();
                final double n = n4 - RenderManager.viewerPosX;
                final double y = p.location.getY();
                DamageParticles.mc.getRenderManager();
                final double n5 = y;
                DamageParticles.mc.getRenderManager();
                final double n2 = n5 - RenderManager.viewerPosY;
                final double z = p.location.getZ();
                DamageParticles.mc.getRenderManager();
                final double n6 = z;
                DamageParticles.mc.getRenderManager();
                final double n3 = n6 - RenderManager.viewerPosZ;
                GlStateManager.pushMatrix();
                GlStateManager.enablePolygonOffset();
                GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
                GlStateManager.translate((float)n, (float)n2, (float)n3);
                DamageParticles.mc.getRenderManager();
                GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                float textY;
                if (DamageParticles.mc.gameSettings.thirdPersonView == 2) {
                    textY = -1.0f;
                }
                else {
                    textY = 1.0f;
                }
                DamageParticles.mc.getRenderManager();
                GlStateManager.rotate(RenderManager.playerViewX, textY, 0.0f, 0.0f);
                final double size = 0.02;
                GlStateManager.scale(-0.02, -0.02, 0.02);
                enableGL2D();
                disableGL2D();
                GL11.glDepthMask(false);
                DamageParticles.mc.fontRendererObj.drawStringWithShadow(p.text, (float)(-(DamageParticles.mc.fontRendererObj.getStringWidth(p.text) / 2)), (float)(-(DamageParticles.mc.fontRendererObj.FONT_HEIGHT - 1)), new Color(255, 0, 0).getRGB());
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glDepthMask(true);
                GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
                GlStateManager.disablePolygonOffset();
                GlStateManager.popMatrix();
            }
        }
        if (e instanceof EventUpdate) {
            try {
                this.particles.forEach(this::update);
            }
            catch (ConcurrentModificationException exception) {
                exception.printStackTrace();
            }
        }
    }
    
    private void update(final Particles update) {
        ++update.ticks;
        if (update.ticks <= 10) {
            update.location.setY(update.location.getY() + update.ticks * 0.005);
        }
        if (update.ticks > 20) {
            this.particles.remove(update);
        }
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static double roundToPlace(final double p_roundToPlace_0_, final int p_roundToPlace_2_) {
        if (p_roundToPlace_2_ < 0) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(p_roundToPlace_0_).setScale(p_roundToPlace_2_, RoundingMode.HALF_UP).doubleValue();
    }
}
