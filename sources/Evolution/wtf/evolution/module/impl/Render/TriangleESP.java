package wtf.evolution.module.impl.Render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.helpers.render.ColorUtil;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ColorSetting;
import wtf.evolution.settings.options.ModeSetting;

import java.awt.*;

import static wtf.evolution.helpers.render.RenderUtil.drawTriangle;

@ModuleInfo(name = "TriangleESP", type = Category.Render)
public class TriangleESP extends Module {


    public ModeSetting mode = new ModeSetting("Mode", "Static", "Static", "Fade", "Astolfo", "Rainbow").call(this);
    public ColorSetting color = new ColorSetting("Color", -1).setHidden(() -> !(mode.is("Static") || mode.is("Fade"))).call(this);

    @EventTarget
    public void onRender(EventDisplay event) {
        int i = 0;
        for (Entity entity : mc.world.loadedEntityList) {
            i+=50;
            if (entity == mc.player) continue;
            if (!(entity instanceof EntityPlayer)) continue;
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ;
            double cos = Math.cos(mc.player.rotationYaw * (Math.PI * 2 / 360));
            double sin = Math.sin(mc.player.rotationYaw * (Math.PI * 2 / 360));
            double rotY = -(z * cos - x * sin);
            double rotX = -(x * cos + z * sin);

            float angle = (float) (Math.atan2(rotY, rotX) * 180 / Math.PI);
            double xPos = ((50) * Math.cos(Math.toRadians(angle))) + (event.sr.getScaledWidth() / 2f);
            double y = ((50) * Math.sin(Math.toRadians(angle))) +  (event.sr.getScaledHeight() / 2f);

            GlStateManager.pushMatrix();
            GlStateManager.translate(xPos, y, 0);
            GlStateManager.rotate(angle + 180 + 90, 0, 0, 1);
            Color c = new Color(ColorUtil.applyColor(mode.get(), i, color.get(), 255).getRed(), ColorUtil.applyColor(mode.get(), i, color.get(), 255).getGreen(), ColorUtil.applyColor(mode.get(), i, color.get(), 255).getBlue(), Math.max((int) (255 - mc.player.getDistance(entity) * 5), 15));
            RenderUtil.drawBlurredShadow(0,-5, 10, 10, 15, c);
            drawTriangle(0, 0, 5, 7, c.getRGB(), c.darker().getRGB());
            GlStateManager.popMatrix();

        }
    }




}
