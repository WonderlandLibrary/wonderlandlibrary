package today.flux.gui.hud.Themes;

import org.lwjgl.opengl.GL11;
import today.flux.Flux;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.gui.hud.HudRenderer;
import today.flux.gui.hud.Theme;
import today.flux.module.Module;
import today.flux.module.implement.Render.Hud;
import today.flux.utility.ColorUtils;
import today.flux.utility.WorldRenderUtils;

import java.awt.*;

public class Rainbow implements Theme {
    int count = 0;
    public int delay = 500;

    @Override
    public String getName() {
        return "Rainbow";
    }

    static float getYIndex(float newWidth, float newHeight, float yIndex, String renderName, int color) {
        float x = Theme.renderBackground(newWidth, yIndex, renderName, mc.fontRendererObj.getStringWidth(renderName), mc.getRenderViewEntity());

        if (Hud.colorbar.getValue()) {
            WorldRenderUtils.drawRects(WorldRenderUtils.getScaledResolution().getScaledWidth() - 1.5, yIndex + (10 - Hud.offset.getValue()), WorldRenderUtils.getScaledResolution().getScaledWidth(), yIndex + 10, new Color(color));
        }

        if (Hud.isMinecraftFont) {
            mc.fontRendererObj.drawStringWithShadow(renderName, x, yIndex + 1, color);
        } else {
            FontManager.normal2.drawString(renderName, x, yIndex, color);
        }

        yIndex += Hud.offset.getValue();
        return yIndex;
    }

    @Override
    public void render(float newWidth, float newHeight) {
        count = 0;
        GL11.glPushMatrix();
        // sort
        float yIndex = 2 + HudRenderer.animationY;
        for (Module module : Flux.INSTANCE.getModuleManager().getModulesRender(Hud.isMinecraftFont ? mc.fontRendererObj : FontManager.normal2)) {
            String renderName = module.getDisplayText();
            yIndex = Rainbow.getYIndex(newWidth, newHeight, yIndex, renderName, ColorUtils.rainbow(-100, (long) (++count * -50 * Hud.rainbowSpeed.getValue())));
        }

        GL11.glPopMatrix();
    }

    @Override
    public void renderWatermark() {
        if (Hud.isMinecraftFont) {
            mc.fontRendererObj.drawStringWithShadow(Hud.name, 3, 3, ColorUtils.rainbow(-100, (long) (0 * -50 * Hud.rainbowSpeed.getValue())));
        } else {
            FontManager.big.drawStringWithSuperShadow(Hud.name, 3, 2, ColorUtils.rainbow(-100, (long) (0 * -50 * Hud.rainbowSpeed.getValue())));
        }
    }
}
