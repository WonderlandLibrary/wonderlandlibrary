package me.AquaVit.liquidSense.modules.render;

import me.AquaVit.liquidSense.API.Location;
import me.AquaVit.liquidSense.API.Particles;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.ccbluex.liquidbounce.features.module.modules.combat.Aura;

@ModuleInfo(name = "Particle", description = "Particle", category = ModuleCategory.RENDER)
public class Particle extends Module {
    private HashMap<EntityLivingBase, Float> healthMap = new HashMap<EntityLivingBase, Float>();
    private List<Particles> particles = new ArrayList<Particles>();
    @EventTarget
    public void onLivingUpdate(UpdateEvent e) {
        Aura a = (Aura) LiquidBounce.moduleManager.getModule(Aura.class);
        EntityLivingBase entity = a.getTarget();
        if (entity != null && entity != mc.thePlayer) {
            if (!this.healthMap.containsKey(entity)) {
                this.healthMap.put(entity, entity.getHealth());
            }
            float floatValue = this.healthMap.get(entity);
            float health = entity.getHealth();
            if (floatValue != health) {
                //System.out.println(floatValue+"float");
                //System.out.println(health+"heal");
                String text;
                if (floatValue - health < 0.0f) {
                    text = "§a" + roundToPlace((floatValue - health) * -1.0f, 1);
                } else {
                    text = "§e" + roundToPlace(floatValue - health, 1);
                }
                //System.out.println(text +"text");
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
            this.mc.getRenderManager();
            double n = x - mc.getRenderManager().viewerPosX;
            double y = p.location.getY();
            this.mc.getRenderManager();
            double n2 = y - mc.getRenderManager().viewerPosY;
            double z = p.location.getZ();
            this.mc.getRenderManager();
            double n3 = z - mc.getRenderManager().viewerPosZ;
            GlStateManager.pushMatrix();
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
            GlStateManager.translate((float) n, (float) n2, (float) n3);
            GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            float textY;
            if (this.mc.gameSettings.thirdPersonView == 2) {
                textY = -1.0f;
            } else {
                textY = 1.0f;
            }
            GlStateManager.rotate(this.mc.getRenderManager().playerViewX, textY, 0.0f, 0.0f);
            final double size = 0.03;
            GlStateManager.scale(-size, -size, size);
            enableGL2D();
            disableGL2D();
            GL11.glDepthMask(false);
            this.mc.fontRendererObj.drawStringWithShadow(p.text,
                    (float) (-(this.mc.fontRendererObj.getStringWidth(p.text) / 2)),
                    (float) (-(this.mc.fontRendererObj.FONT_HEIGHT - 1)), 0);
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

    public static double roundToPlace(double p_roundToPlace_0_, int p_roundToPlace_2_) {
        if (p_roundToPlace_2_ < 0) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(p_roundToPlace_0_).setScale(p_roundToPlace_2_, RoundingMode.HALF_UP).doubleValue();
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        this.particles.forEach(this::update);
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

