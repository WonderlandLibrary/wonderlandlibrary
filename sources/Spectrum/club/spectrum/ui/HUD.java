package club.spectrum.ui;

import club.spectrum.Spectrum;
import club.spectrum.modules.Module;
import club.spectrum.modules.Category;
import club.spectrum.utils.render.ColorUtils;
import club.spectrum.utils.render.RoundedUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Comparator;
import java.awt.Color;
import java.util.Collections;

import static club.spectrum.modules.ModuleManager.modules;
import static net.minecraft.client.gui.Gui.drawRect;


public class HUD extends GuiIngame {

    public static Minecraft mc = Minecraft.getMinecraft();

    public HUD(Minecraft mcIn) {
        super(mcIn);
    }

    public void renderGameOverlay(float p_175180_1_) {
        super.renderGameOverlay(p_175180_1_);
        renderMain();
        renderModuleList();
    }

    public void renderMain() {
        int counter = 0;
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int y = 1;
        float fadeOffset = 0;
        int firstColor = new Color(12, 232, 199).getRGB();
        int secondColor = new Color(12, 163, 232).getRGB();
        int color = ColorUtils.fadeBetween(Spectrum.INSTANCE.firstColor.getRGB(), Spectrum.INSTANCE.secondColor.getRGB(), counter * 150L);

        fadeOffset -= 3;
        mc.fontRendererObj.drawStringWithShadow("S", 4.0f, 4.0f, Spectrum.getHudColor(fadeOffset).getRGB());
        mc.fontRendererObj.drawStringWithShadow("pectrum", 10.0F, 4.0f, -1);
        fadeOffset -= 3;
        counter++;
    }

    public static class ModuleComparator implements Comparator<Module> {
        @Override
        public int compare(Module o1, Module o2) {
            if (Minecraft.getMinecraft().fontRendererObj.getStringWidth(o1.name) > Minecraft.getMinecraft().fontRendererObj.getStringWidth(o2.name)) {
                return -1;
            }
            if (Minecraft.getMinecraft().fontRendererObj.getStringWidth(o1.name) < Minecraft.getMinecraft().fontRendererObj.getStringWidth(o2.name)) {
                return 1;
            }
            return 0;
        }
    }

    public void renderModuleList() {
        Collections.sort(modules, new ModuleComparator());

        int yCount = 0;
        int index = 0;
        long x = 0;

        int counter = 0;
        for(Module f : Spectrum.INSTANCE.moduleManager.getModules()) {
            f.onRender();

            ScaledResolution sr = new ScaledResolution(mc);
            double offset = yCount * (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 4) + 4;

            if(f.isEnabled()) {
                int firstColor = new Color(12, 232, 199).getRGB();
                int secondColor = new Color(12, 163, 232).getRGB();
                int color = ColorUtils.fadeBetween(firstColor, secondColor, counter * 150L);

                int nameWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(f.getName());
                int rectWidth = nameWidth + 8;
                int rectHeight = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2;
                int rectX = sr.getScaledWidth() - rectWidth - 4;
                int rectY = (int) (1 + offset);

                drawRect(rectX, rectY - 2, rectX + rectWidth, rectY + rectHeight, 0x80000000);

                mc.fontRendererObj.drawStringWithShadow(f.getName(), rectX + 4, rectY + 1, color);

                int barWidth = 2;
                int barX = rectX + rectWidth - barWidth + 2;
                int barY = rectY;

                RoundedUtils.drawRoundedRectangle(barX, barY - 1, barX + barWidth, barY + rectHeight, 3, color);

                yCount++;
                index++;
                x++;
                counter++;
            }
        }
    }
}
