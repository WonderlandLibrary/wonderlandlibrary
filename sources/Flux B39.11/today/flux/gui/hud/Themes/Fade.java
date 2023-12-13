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

public class Fade implements Theme {
    @Override
    public String getName() {
        return "Fade";
    }

    public static float getYIndex(float newWidth, float newHeight, float yIndex, String renderName) {
        float x = Theme.renderBackground(newWidth, yIndex, renderName, mc.fontRendererObj.getStringWidth(renderName), mc.getRenderViewEntity());

        int color = ColorUtils.fadeBetween(Hud.arraylistColor1.getColorInt(), Hud.arraylistColor1.getColor().darker().darker().darker().getRGB(), (int) yIndex * (Hud.fadeDirection.isCurrentMode("Up") ? 7 : -7));

        if (Hud.colorbar.getValue()) {
            WorldRenderUtils.drawRects(WorldRenderUtils.getScaledResolution().getScaledWidth() - 1.5, yIndex + (10 - Hud.offset.getValue()), WorldRenderUtils.getScaledResolution().getScaledWidth(), yIndex + 10, new Color(color));
        }

        if (Hud.isMinecraftFont) {
            mc.fontRendererObj.drawStringWithShadow(renderName, x, yIndex + 1, color);
        } else {
            FontManager.normal2.drawStringWithShadow(renderName, x, yIndex, color);
        }

        yIndex += Hud.offset.getValue();
        return yIndex;
    }

    @Override
    public void render(float newWidth, float newHeight) {
        GL11.glPushMatrix();
        // sort
        float yIndex = 2 + HudRenderer.animationY;

        for (Module module : Flux.INSTANCE.getModuleManager().getModulesRender(Hud.isMinecraftFont ? mc.fontRendererObj : FontManager.normal2)) {
            String renderName = module.getDisplayText();
            yIndex = Fade.getYIndex(newWidth, newHeight, yIndex, renderName);
        }
        GL11.glPopMatrix();
    }

    @Override
    public void renderWatermark() {
        int color = ColorUtils.fadeBetween(Hud.arraylistColor1.getColorInt(), Hud.arraylistColor1.getColor().darker().darker().darker().getRGB());

        if (Hud.isMinecraftFont) {
            mc.fontRendererObj.drawStringWithShadow(Hud.name, 3, 3, color);
        } else {
            FontManager.big.drawStringWithSuperShadow(Hud.name, 3, 2, color);
        }
    }
}