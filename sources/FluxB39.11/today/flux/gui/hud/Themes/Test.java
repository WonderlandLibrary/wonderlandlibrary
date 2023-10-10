package today.flux.gui.hud.Themes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import today.flux.Flux;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.hud.HudRenderer;
import today.flux.gui.hud.Theme;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.implement.Render.Hud;
import today.flux.utility.AnimationUtils;

public class Test implements Theme {
    int count = 0;

    public String getName() {
        return "Test";
    }

    @Override
    public void render(float newWidth, float newHeight) {
        GlStateManager.pushMatrix();
        float yStart = 9 + HudRenderer.animationY;

        count = 0;
        for (Module m : Flux.INSTANCE.getModuleManager().getModulesRenderWithAnimation(mc.fontRendererObj)) {
            if (m.isHide()) continue;
            float startX = 2;

            if (m.isEnabled()) {
                m.animationY = AnimationUtils.getAnimationState(m.animationY, (mc.fontRendererObj.FONT_HEIGHT), (float) (mc.fontRendererObj.FONT_HEIGHT) * 11);
            } else {
                m.animationY = AnimationUtils.getAnimationState(m.animationY, 0, (float) (mc.fontRendererObj.FONT_HEIGHT) * 11);
            }

            if (!m.isEnabled() && m.animationY == 0)
                continue;

            if (Hud.renderRenderCategory.getValueState() && m.getCategory() == Category.Render)
                continue;

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);

            GuiRenderUtils.doGlScissor((int) startX, (int) yStart, newWidth, m.animationY + 15, 2f);
            mc.fontRendererObj.drawStringWithShadow(m.getDisplayText(), startX, yStart + 15, Hud.arraylistColor1.getColorInt());

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();

            yStart += m.animationY;
        }

        GlStateManager.popMatrix();
    }

    @Override
    public void renderWatermark() {
        mc.fontRendererObj.drawStringWithShadow(EnumChatFormatting.GRAY + "Flux" + EnumChatFormatting.WHITE + " - " + Flux.status + EnumChatFormatting.GRAY + " Build", 2, 3, 0);
        mc.fontRendererObj.drawStringWithShadow(EnumChatFormatting.GRAY + "FPS: " + Minecraft.getDebugFPS(), 2, 13, 0);
    }
}