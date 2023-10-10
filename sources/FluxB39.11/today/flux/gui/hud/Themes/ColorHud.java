package today.flux.gui.hud.Themes;

import org.lwjgl.opengl.GL11;
import today.flux.Flux;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.gui.hud.HudRenderer;
import today.flux.gui.hud.Theme;
import today.flux.module.Module;
import today.flux.module.implement.Render.Hud;

public class ColorHud implements Theme {
    @Override
    public String getName() {
        return "Color";
    }

    @Override
    public void render(float newWidth, float newHeight) {
        GL11.glPushMatrix();

        float yIndex = 2 + HudRenderer.animationY;
        for (Module module : Flux.INSTANCE.getModuleManager().getModulesRender(Hud.isMinecraftFont ? mc.fontRendererObj : FontManager.normal2)) {
            String renderName = module.getDisplayText();

            yIndex = Rainbow.getYIndex(newWidth, newHeight, yIndex, renderName, Hud.arraylistColor1.getColorInt());
        }

        GL11.glPopMatrix();
    }

    @Override
    public void renderWatermark() {
        if (Hud.isMinecraftFont) {
            mc.fontRendererObj.drawStringWithShadow(Hud.name, 3, 3, Hud.watermarkColour.getColorInt());
        } else {
            FontManager.big.drawStringWithSuperShadow(Hud.name, 3, 2, Hud.watermarkColour.getColorInt());
        }
    }
}