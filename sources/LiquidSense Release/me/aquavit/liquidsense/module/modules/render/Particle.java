package me.aquavit.liquidsense.module.modules.render;

import me.aquavit.liquidsense.module.modules.blatant.Aura;
import me.aquavit.liquidsense.utils.module.Location;
import me.aquavit.liquidsense.utils.module.Particles;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.events.LivingUpdateEvent;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import me.aquavit.liquidsense.event.events.Render3DEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

@ModuleInfo(name = "Particle", description = "Particle", category = ModuleCategory.RENDER)
public class Particle extends Module {
    private HashMap<EntityLivingBase, Float> healthMap = new HashMap<EntityLivingBase, Float>();
    private List<Particles> particles = new ArrayList<Particles>();
    @EventTarget
    public void onLivingUpdate(LivingUpdateEvent e) {
        Aura a = (Aura) LiquidSense.moduleManager.getModule(Aura.class);
        EntityLivingBase entity = a.getTarget();
        if (entity != null && entity != mc.thePlayer) {
            if (!this.healthMap.containsKey(entity)) {
                this.healthMap.put(entity, entity.getHealth());
            }
            float floatValue = this.healthMap.get(entity);
            float health = entity.getHealth();
            if (floatValue != health) {
                String text;
                if (floatValue - health < 0.0f) {
                    text = "§a" + Particles.roundToPlace((floatValue - health) * -1.0f, 1);
                } else {
                    text = "§e" + Particles.roundToPlace(floatValue - health, 1);
                }
                Location location = new Location(entity);
                location.setY(entity.getEntityBoundingBox().minY
                        + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) / 2.0);
                location.setX(location.getX() - 0.5 + new Random(System.currentTimeMillis()).nextInt(5) * 0.1);
                location.setZ(location.getZ() - 0.5
                        + new Random(System.currentTimeMillis() + (0x203FF36645D9EA2EL ^ 0x203FF36645D9EA2FL)).nextInt(5)
                        * 0.1);
                this.particles.add(new Particles(location, text));
                this.healthMap.remove(entity);
                this.healthMap.put(entity, entity.getHealth());
            }
        }
    }
    @EventTarget
    public void onRender(Render3DEvent e) {
        for (Particles p : this.particles) {
            double x = p.location.getX();
            double n = x - mc.getRenderManager().viewerPosX;
            double y = p.location.getY();
            double n2 = y - mc.getRenderManager().viewerPosY;
            double z = p.location.getZ();
            double n3 = z - mc.getRenderManager().viewerPosZ;
            GlStateManager.pushMatrix();
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
            GlStateManager.translate((float) n, (float) n2, (float) n3);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            float textY;
            if (mc.gameSettings.thirdPersonView == 2) {
                textY = -1.0f;
            } else {
                textY = 1.0f;
            }
            GlStateManager.rotate(mc.getRenderManager().playerViewX, textY, 0.0f, 0.0f);
            final double size = 0.03;
            GlStateManager.scale(-size, -size, size);
            enableGL2D();
            disableGL2D();
            GL11.glDepthMask(false);
            mc.fontRendererObj.drawStringWithShadow(p.text,
                    (float) (-(mc.fontRendererObj.getStringWidth(p.text) / 2)),
                    (float) (-(mc.fontRendererObj.FONT_HEIGHT - 1)), 0);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDepthMask(true);
            GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.popMatrix();
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

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        ArrayList<Particles> copiedList = new ArrayList<>(this.particles);
        copiedList.forEach(this::update);
    }

    private void update(Particles update) {
        ++update.ticks;
        if (update.ticks <= 10) {
            update.location.setY(update.location.getY() + update.ticks * 0.005);
        }
        if (update.ticks > 20) {
            this.particles.remove(update);
        }
    }
}

