package dev.rise.module.impl.render;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.MoveUtil;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.theme.ThemeType;
import dev.rise.util.render.theme.ThemeUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;
import java.util.ArrayList;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "MotionGraph", description = "Shows how much you're moving on a graph", category = Category.RENDER)
public class MotionGraph extends Module {
    private final NumberSetting width = new NumberSetting("Width", this, 180, 100, 300, 1);
    private final NumberSetting height = new NumberSetting("Height", this, 5, 1, 20, 1);
    private final NumberSetting y = new NumberSetting("Y Offset", this, -210, -500, 500, 1);

    private final ArrayList<Float> speeds = new ArrayList<>();
    private double lastVertices;
    private float biggestCock;

    @Override
    public void onUpdateAlwaysInGui() {
        if (lastVertices != 100) {
            synchronized (speeds) {
                speeds.clear();
                biggestCock = 0;
            }
        }

        lastVertices = 100;
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (speeds.size() > 100 - 2) {
            speeds.remove(0);
        }

        speeds.add((float) MoveUtil.getSpeed() * mc.timer.timerSpeed);

        biggestCock = -1;
        for (final float f : speeds) {
            if (f > biggestCock) {
                biggestCock = f;
            }
        }
    }

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        final ScaledResolution sr = new ScaledResolution(mc);

        GL11.glPushMatrix();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBegin(GL11.GL_LINES);

        if (speeds.size() > 3) {
            final float width = (float) (sr.getScaledWidth() / 2f - this.width.getValue() / 2f);

            for (int i = 0; i < speeds.size() - 1; i++) {
                RenderUtil.color(ThemeUtil.getThemeColor(i / 10f, ThemeType.GENERAL));
                final float y = (float) (speeds.get(i) * 10 * height.getValue());
                final float y2 = (float) (speeds.get(i + 1) * 10 * height.getValue());
                final float length = (float) (this.width.getValue() / (speeds.size() - 1));

                GL11.glVertex2f(width + (i * length), (float) (sr.getScaledHeight() / 2F - Math.min(y, 50) - this.y.getValue()));
                GL11.glVertex2f(width + ((i + 1) * length), (float) (sr.getScaledHeight() / 2F - Math.min(y2, 50) - this.y.getValue()));
            }
        }
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        RenderUtil.color(Color.WHITE);
        GlStateManager.resetColor();
        GL11.glPopMatrix();

    }

    private void drawRect(double left, double top, double right, double bottom) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }

        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        worldrenderer.begin(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
