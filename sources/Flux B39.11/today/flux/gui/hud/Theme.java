package today.flux.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import today.flux.gui.clickgui.classic.BlurBuffer;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.module.implement.Render.Hud;
import today.flux.utility.WorldRenderUtils;

import java.awt.*;

public interface Theme {
    Minecraft mc = Minecraft.getMinecraft();

    String getName();
    void render(float newWidth, float newHeight);

    void renderWatermark();

    static float renderBackground(float newWidth, float yIndex, String renderName, int stringWidth, Entity renderViewEntity) {
        float margin = 3.0f;
        float x;

        if (Hud.isMinecraftFont)
            x = newWidth - stringWidth - (Hud.colorbar.getValue() ? 4.5f : 2.0f);
        else
            x = newWidth - FontManager.normal2.getStringWidth(renderName) - (Hud.colorbar.getValue() ? 4.5f : 2.0f);


        if (Hud.background.getValue()) {
            if (!Hud.NoShader.getValue() && OpenGlHelper.shadersSupported && renderViewEntity instanceof EntityPlayer) {
                if (Hud.isMinecraftFont) {
                    BlurBuffer.blurArea((int) (x - margin + 1), (int) yIndex + (10 - Hud.offset.getValue()), stringWidth + margin + 5, 10, true);
                } else {
                    BlurBuffer.blurArea((int) (x - margin + 1), (int) yIndex + (10 - Hud.offset.getValue()), FontManager.normal2.getStringWidth(renderName) + margin + 5, 10, true);
                }
            }

            if (Hud.isMinecraftFont) {
                WorldRenderUtils.drawRects((int) (x - margin + 1), (int) yIndex + (10 - Hud.offset.getValue()), x + stringWidth + margin + 5, yIndex + 10, new Color(0, 0, 0, (int) Hud.backgroundAlpha.getValueState()));
            } else {
                WorldRenderUtils.drawRects((int) (x - margin + 1), (int) yIndex  + (10 - Hud.offset.getValue()), x + FontManager.normal2.getStringWidth(renderName) + margin + 5, yIndex + 10, new Color(0, 0, 0, (int) Hud.backgroundAlpha.getValueState()));
            }
        }

        return x;
    }
}
