package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import today.flux.event.OldUIRenderEvent;
import today.flux.event.UIRenderEvent;
import today.flux.module.Category;
import today.flux.module.Module;

import org.lwjgl.opengl.GL11;

/**
 * Created by Admin on 2017/02/03.
 */
public class FPSHurtCam extends Module {
    public FPSHurtCam() {
        super("FPSHurtCam", Category.Render, false);
    }

    @EventTarget
    private void onRender3D(OldUIRenderEvent event) {
        drawVignette();
    }

    private void drawVignette() {
        if (this.mc.gameSettings.thirdPersonView != 0)
            return;

        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);

        int healthPersent = (int) ((this.mc.thePlayer.getHealth() / this.mc.thePlayer.getMaxHealth()) * 100);

        if (healthPersent <= 10) {
            GL11.glColor4f(0, 1.0F, 1.0F, 1.0F);
        } else if (healthPersent <= 20) {
            GL11.glColor4f(0, 0.8F, 0.8F, 1.0F);
        } else if (healthPersent <= 50) {
            GL11.glColor4f(0, 0.5F, 0.5F, 1.0F);
        } else {
            GL11.glColor4f(0, 0.0F, 0.0F, 0.0F);
        }

        ScaledResolution scaledResolution = new ScaledResolution(mc);

        this.mc.getTextureManager().bindTexture(GuiIngame.vignetteTexPath);
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        var10.begin(7, DefaultVertexFormats.POSITION_TEX);
        var10.pos(0.0D, (double) scaledResolution.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        var10.pos((double) scaledResolution.getScaledWidth(), (double) scaledResolution.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        var10.pos((double) scaledResolution.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        var10.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        var9.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }

}
