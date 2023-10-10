package today.flux.module.implement.Render;

import today.flux.Flux;
import today.flux.event.WorldRenderEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Combat.KillAura;
import today.flux.utility.WorldRenderUtils;
import today.flux.utility.WorldUtil;
import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

public class Tracers extends Module {
    public Tracers() {
        super("Tracers", Category.Render, false);
    }

    private double interpolate(double lastI, double i, float ticks, double ownI) {
        return lastI + (i - lastI) * (double) ticks - ownI;
    }

    @EventTarget(Priority.LOW)//fix bobbing issue
    public void onRender3D(WorldRenderEvent event) {
        for (EntityLivingBase entity : WorldUtil.getLivingPlayers()) {
            if (!ESP.isValidForESP(entity))
                continue;

            final double x = interpolate(entity.lastTickPosX, entity.posX, event.getPartialTicks(), this.mc.getRenderManager().getRenderPosX());
            final double y = interpolate(entity.lastTickPosY, entity.posY, event.getPartialTicks(), this.mc.getRenderManager().getRenderPosY());
            final double z = interpolate(entity.lastTickPosZ, entity.posZ, event.getPartialTicks(), this.mc.getRenderManager().getRenderPosZ());

            GL11.glLoadIdentity();
            this.mc.entityRenderer.orientCamera(event.getPartialTicks());

            WorldRenderUtils.enableGL3D(2.0F);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

            float distance = this.mc.thePlayer.getDistanceToEntity(entity);
            if (Flux.INSTANCE.getFriendManager().isFriend(entity.getName())) {
                GlStateManager.color(0.3f, 0.3f, 1.0F, 0.7F);
            } else if (ModuleManager.killAuraMod.isEnabled() && KillAura.target == entity) {
                GlStateManager.color(1f, 1f, 0F, 1F);
            } else {
                if (distance <= 32.0F) {
                    GlStateManager.color(1.0F, distance / 32.0F, distance / 32.0F, 0.7F);
                } else {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 0.7F);
                }
            }

            GL11.glBegin(1);
            GL11.glVertex3d(0.0D, (double) mc.thePlayer.getEyeHeight(), 0.0D);

            GL11.glVertex3d(x, y + 1.00D, z);

            GL11.glEnd();

            WorldRenderUtils.disableGL3D();

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
