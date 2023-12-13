package today.flux.gui.hud.Themes;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import today.flux.Flux;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.gui.fontRenderer.FontUtils;
import today.flux.gui.hud.HudRenderer;
import today.flux.gui.hud.Theme;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.implement.Render.Hud;
import today.flux.utility.AnimationUtils;
import today.flux.utility.ColorUtils;

public class Rainbow2 implements Theme {
    int count = 0;

    @Override
    public String getName() {
        return "Rainbow2";
    }

    @Override
    public void render(float newWidth, float newHeight) {
        GlStateManager.pushMatrix();
        float yStart = 0 + HudRenderer.animationY;

        count = 0;
        for (Module m : Flux.INSTANCE.getModuleManager().getModulesRenderWithAnimation(FontManager.sans18)) {
            if (m.isHide()) continue;
            FontUtils font = FontManager.sans18;
            float startX = newWidth - FontManager.sans18.getStringWidth(m.getDisplayText()) - 6;

            if (m.isEnabled()) {
                m.animationY = AnimationUtils.getAnimationState(m.animationY, (font.FONT_HEIGHT - 3), (font.FONT_HEIGHT - 3) * 9);
            } else {
                m.animationY = AnimationUtils.getAnimationState(m.animationY, 0,  (font.FONT_HEIGHT - 3) * 9);
            }

            if (!m.isEnabled() && m.animationY == 0)
                continue;

            if (Hud.renderRenderCategory.getValueState() && m.getCategory() == Category.Render)
                continue;

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);

            GuiRenderUtils.doGlScissor((int)newWidth - (int)startX, (int)yStart - 1, newWidth, m.animationY + 4,2f);
            FontManager.sans18.drawStringWithGudShadow(m.getDisplayText(), startX + 2, yStart, ColorUtils.rainbow(-100, (long) (++count * -50 * Hud.rainbowSpeed.getValue())));

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();

            yStart += m.animationY;
        }

        GlStateManager.popMatrix();
    }

    @Override
    public void renderWatermark() {
        FontManager.big.drawStringWithSuperShadow(Flux.NAME, 3, 2, ColorUtils.rainbow(-100, (long) (++count * -50 * Hud.rainbowSpeed.getValue())));
    }
}