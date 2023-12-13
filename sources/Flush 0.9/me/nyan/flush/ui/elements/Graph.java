package me.nyan.flush.ui.elements;

import me.nyan.flush.Flush;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class Graph {
    private final String name;
    private final ArrayList<Double> data;

    private GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay", 20);
    private boolean minecraftFont;

    public Graph(String name) {
        this.name = name;
        data = new ArrayList<>();
    }

    public void drawGraph(float x, float y, float width, float height) {
        double max = 0;
        double average = 0;
        for (double data : data) {
            if (data > max) max = MathUtils.snapToStep(data, 0.01);
            average += Math.min(data, height);
        }
        average = MathUtils.snapToStep(average / data.size(), 0.01);

        if (data.size() > width) {
            data.remove(0);
        }

        GL11.glLineWidth(1);
        /*
        RenderUtil.drawLine(x, y, x + width, y, 0xFFAAAAAA);
        RenderUtil.drawLine(x, y, x, y + height, 0xFFAAAAAA);
        RenderUtil.drawLine(x + width, y, x + width, y + height, 0xFFAAAAAA);
        RenderUtil.drawLine(x, y + height, x + width, y + height, 0xFFAAAAAA);
         */
        Gui.drawRect(x, y, x + width, y + height, 0x88000000);

        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.glScissor(x, y, x + width, y + height);

        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1);
        RenderUtils.glColor(-1);

        GL11.glBegin(GL11.GL_LINE_STRIP);
        int i = 0;
        for (double data : data) {
            GL11.glVertex2d(x + i, y + height - data / max * height);
            i++;
        }
        GL11.glEnd();

        RenderUtils.drawLine(x, (float) (y + height - average / max * height),
                x + width, (float) (y + height - average / max * height), 0xFF00FF00);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.color(1,1,1);
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();

        Object fr = minecraftFont ? Minecraft.getMinecraft().fontRendererObj : font;
        RenderUtils.drawString(fr, name, x + 3, y + 2, -1, true);
        RenderUtils.drawString(fr, average + " avg",
                x + width - 4 - RenderUtils.getStringWidth(fr, average + " avg"),
                y + 2, -1, true);
        RenderUtils.drawString(fr, max + " max",
                x + width - 4 - RenderUtils.getStringWidth(fr, max + " max"),
                y + 2 + RenderUtils.getFontHeight(fr), -1, true);
    }

    public void update(double value) { data.add(value); }

    public void clear() { data.clear(); }

    public ArrayList<Double> getData() { return data; }

    public String getName() { return name; }

    public void setFont(GlyphPageFontRenderer font) {
        this.font = font;
    }

    public void setMinecraftFont(boolean b) {
        this.minecraftFont = b;
    }
}
