package today.flux.gui.hud.Themes;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import today.flux.Flux;
import today.flux.gui.hud.HudRenderer;
import today.flux.gui.hud.Theme;
import today.flux.module.Module;
import today.flux.module.implement.Render.Hud;
import today.flux.utility.DelayTimer;
import today.flux.utility.ServerUtils;

import java.awt.*;

public class Nostalgia implements Theme {
    DelayTimer delayTimer = new DelayTimer();
    float index;

    @Override
    public String getName() {
        return "Nostalgia";
    }

    public static float getYIndex(float newWidth, float newHeight, float yIndex, String renderName, Color color) {
        float x = newWidth - mc.fontRendererObj.getStringWidth(renderName);

        mc.fontRendererObj.drawStringWithShadow(renderName, x - 1, yIndex, color.getRGB());

        yIndex += 10;
        return yIndex;
    }

    @Override
    public void render(float newWidth, float newHeight) {
        if (index > 1.0f)
            index = 0.0f;

        index += 0.001f * (delayTimer.getPassed() * 0.2f);
        delayTimer.reset();
        GL11.glPushMatrix();
        Flux.rainbow = Color.getHSBColor(index, 1.0f, 1.0f);

        // sort
        float diffIndex = index;

        float yIndex = 2 + HudRenderer.animationY;

        for (Module module : Flux.INSTANCE.getModuleManager().getModulesRender(mc.fontRendererObj)) {
            String renderName = module.getDisplayText();

            if (diffIndex > 1.0f)
                diffIndex -= 1.0f;

            final Color color = Color.getHSBColor(diffIndex, 1, 1);
            diffIndex += (0.2 / 10);
            yIndex = Nostalgia.getYIndex(newWidth, newHeight, yIndex, renderName, color);
        }

        GL11.glPopMatrix();
    }

    @Override
    public void renderWatermark() {
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.5, 1.5, 1.5);
        mc.fontRendererObj.drawStringWithShadow("F" + EnumChatFormatting.BLUE + "lux", 2, 2, Color.MAGENTA.getRGB());
        GlStateManager.popMatrix();
        mc.fontRendererObj.drawStringWithShadow("b" + Flux.VERSION, 38, 2, Color.white.getRGB());
        mc.fontRendererObj.drawStringWithShadow("Name: " + EnumChatFormatting.RED + this.mc.thePlayer.getName(), 3, 18, Color.green.getRGB());
        mc.fontRendererObj.drawStringWithShadow("Server: " + EnumChatFormatting.RED + (mc.isSingleplayer() ? "SinglePlayer" : ServerUtils.INSTANCE.isOnHypixel() ? "mc.hypixel.net" : mc.getCurrentServerData().serverIP), 3, 28, Color.GREEN.getRGB());
    }
}
