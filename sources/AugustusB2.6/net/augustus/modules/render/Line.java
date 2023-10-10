// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.lenni0451.eventapi.reflection.EventTarget;
import java.util.Iterator;
import net.minecraft.client.renderer.WorldRenderer;
import java.util.Arrays;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.augustus.events.EventRender3D;
import net.augustus.modules.Categorys;
import java.awt.Color;
import java.util.ArrayList;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.BooleanValue;
import net.augustus.modules.Module;

public class Line extends Module
{
    public final BooleanValue line;
    public final ColorSetting color;
    public final DoubleValue lineWidth;
    public final DoubleValue lineTime;
    public final BooleanValue killAura;
    public final ColorSetting killAuraColor;
    public final DoubleValue killAuraLineWidth;
    public final DoubleValue killAuraLineTime;
    private ArrayList<double[]> positions;
    
    public Line() {
        super("Line", new Color(10, 20, 15), Categorys.RENDER);
        this.line = new BooleanValue(0, "FeetLine", this, true);
        this.color = new ColorSetting(1, "Color", this, new Color(77, 0, 255, 255));
        this.lineWidth = new DoubleValue(2, "LineWidth", this, 2.0, 0.1, 4.0, 2);
        this.lineTime = new DoubleValue(3, "LineTime", this, 3000.0, 500.0, 20000.0, 0);
        this.killAura = new BooleanValue(4, "AuraLine", this, true);
        this.killAuraColor = new ColorSetting(5, "AuraColor", this, new Color(77, 0, 255, 255));
        this.killAuraLineWidth = new DoubleValue(6, "AuraLineWidth", this, 2.0, 0.1, 4.0, 2);
        this.killAuraLineTime = new DoubleValue(7, "AuraLineTime", this, 3000.0, 500.0, 20000.0, 0);
        this.positions = new ArrayList<double[]>();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.positions.clear();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.positions = new ArrayList<double[]>();
    }
    
    @EventTarget
    public void onEventRender3D(final EventRender3D eventRender3D) {
        if (this.line.getBoolean()) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glDisable(3553);
            GlStateManager.disableCull();
            GL11.glDepthMask(false);
            final float x = (float)(Line.mc.thePlayer.lastTickPosX + (Line.mc.thePlayer.posX - Line.mc.thePlayer.lastTickPosX) * Line.mc.getTimer().renderPartialTicks);
            final float y = (float)(Line.mc.thePlayer.lastTickPosY + this.lineWidth.getValue() / 100.0 + (Line.mc.thePlayer.posY - Line.mc.thePlayer.lastTickPosY) * Line.mc.getTimer().renderPartialTicks);
            final float z = (float)(Line.mc.thePlayer.lastTickPosZ + (Line.mc.thePlayer.posZ - Line.mc.thePlayer.lastTickPosZ) * Line.mc.getTimer().renderPartialTicks);
            this.positions.add(new double[] { x, y, z, (double)System.currentTimeMillis() });
            this.positions.removeIf(values -> this.shouldRenderPoint(values[3]));
            GL11.glColor4f(this.color.getColor().getRed() / 255.0f, this.color.getColor().getGreen() / 255.0f, this.color.getColor().getBlue() / 255.0f, this.color.getColor().getAlpha() / 255.0f);
            GL11.glLineWidth((float)this.lineWidth.getValue());
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            worldrenderer.begin(3, DefaultVertexFormats.POSITION);
            double[] lastPosition = { -2.0, -1.0, -1.0 };
            for (final double[] position : this.positions) {
                if (!Arrays.equals(lastPosition, new double[] { -2.0, -1.0, -1.0 }) && !Arrays.equals(lastPosition, position)) {
                    worldrenderer.pos((float)position[0] - Line.mc.getRenderManager().getRenderPosX(), (float)position[1] - Line.mc.getRenderManager().getRenderPosY(), (float)position[2] - Line.mc.getRenderManager().getRenderPosZ()).endVertex();
                }
                lastPosition = position;
            }
            tessellator.draw();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDepthMask(true);
            GlStateManager.enableCull();
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2848);
        }
    }
    
    private boolean shouldRenderPoint(final double time) {
        return Math.abs(time - System.currentTimeMillis()) > this.lineTime.getValue();
    }
}
