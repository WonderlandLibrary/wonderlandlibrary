package net.ccbluex.liquidbounce.features.module.modules.render;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.render.DMGPUtil.Location;
import net.ccbluex.liquidbounce.features.module.modules.render.DMGPUtil.Particles;
import net.ccbluex.liquidbounce.injection.backend.EntityLivingBaseImplKt;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="DMGParticle", description="Display heatlh volume change value", category=ModuleCategory.RENDER)
public class DMGParticle
extends Module {
    private HashMap<EntityLivingBase, Float> healthMap = new HashMap();
    private List<Particles> particles = new ArrayList<Particles>();

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        EntityLivingBase entity;
        KillAura ka = (KillAura)LiquidBounce.moduleManager.getModule(KillAura.class);
        for (int i1 = 0; i1 < this.particles.size(); ++i1) {
            int i;
            Particles update = this.particles.get(i1);
            if ((i = ++update.ticks) < 10) {
                update.location.setY(update.location.getY() + (double)update.ticks * 0.002);
            }
            if (i <= 20) continue;
            this.particles.remove(update);
        }
        EntityLivingBase entityLivingBase = entity = ka.getTarget() == null ? null : EntityLivingBaseImplKt.unwrap(ka.getTarget());
        if (entity == null || entity == mc.getThePlayer()) {
            return;
        }
        if (!this.healthMap.containsKey(entity)) {
            this.healthMap.put(entity, Float.valueOf(entity.getHealth()));
        }
        float floatValue = this.healthMap.get(entity).floatValue();
        float health = entity.getHealth();
        Criticals criticals = (Criticals)LiquidBounce.moduleManager.get(Criticals.class);
        if (floatValue != health) {
            String text = floatValue - health < 0.0f ? "§a" + DMGParticle.roundToPlace((floatValue - health) * -1.0f, 1) : "§e" + DMGParticle.roundToPlace(floatValue - health, 1);
            Location location = new Location(entity);
            location.setY(entity.func_174813_aQ().minY + (entity.func_174813_aQ().maxY - entity.func_174813_aQ().minY) / 2.0);
            location.setX(location.getX() - 0.5 + (double)new Random(System.currentTimeMillis()).nextInt(5) * 0.15);
            location.setZ(location.getZ() - 0.5 + (double)new Random(System.currentTimeMillis() + 1L).nextInt(5) * 0.15);
            this.particles.add(new Particles(location, text));
            this.healthMap.remove(entity);
            this.healthMap.put(entity, Float.valueOf(entity.getHealth()));
        }
    }

    @EventTarget
    public void onRender(Render3DEvent event) {
        for (Particles p : this.particles) {
            double x = p.location.getX();
            mc.getRenderManager();
            double n = x - mc.getRenderManager().getRenderPosX();
            double y = p.location.getY();
            mc.getRenderManager();
            double n2 = y - mc.getRenderManager().getRenderPosY();
            double z = p.location.getZ();
            mc.getRenderManager();
            double n3 = z - mc.getRenderManager().getRenderPosZ();
            GlStateManager.pushMatrix();
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset((float)1.0f, (float)-1500000.0f);
            GlStateManager.translate((float)((float)n), (float)((float)n2), (float)((float)n3));
            GlStateManager.rotate((float)(-mc.getRenderManager().getPlayerViewY()), (float)0.0f, (float)1.0f, (float)0.0f);
            float textY = 1.0f;
            GlStateManager.rotate((float)mc.getRenderManager().getPlayerViewX(), (float)textY, (float)0.0f, (float)0.0f);
            double size = 0.025;
            GlStateManager.scale((double)-0.025, (double)-0.025, (double)0.025);
            DMGParticle.enableGL2D();
            DMGParticle.disableGL2D();
            GL11.glDepthMask((boolean)false);
            mc.getFontRendererObj().drawString(p.text, -(mc.getFontRendererObj().getStringWidth(p.text) / 2), -(mc.getFontRendererObj().getFontHeight() - 1), 0, true);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glDepthMask((boolean)true);
            GlStateManager.doPolygonOffset((float)1.0f, (float)1500000.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.popMatrix();
        }
    }

    public static void enableGL2D() {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
    }

    public static void disableGL2D() {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static double roundToPlace(double p_roundToPlace_0_, int p_roundToPlace_2_) {
        if (p_roundToPlace_2_ < 0) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(p_roundToPlace_0_).setScale(p_roundToPlace_2_, RoundingMode.HALF_UP).doubleValue();
    }
}
