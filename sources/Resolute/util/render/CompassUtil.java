// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.render;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import com.google.common.collect.Lists;
import vip.Resolute.util.font.FontUtil;
import java.util.List;
import vip.Resolute.util.font.MinecraftFontRenderer;

public class CompassUtil
{
    public float innerWidth;
    public float outerWidth;
    public boolean shadow;
    public float scale;
    public int accuracy;
    MinecraftFontRenderer yahei28U;
    MinecraftFontRenderer yahei18U;
    MinecraftFontRenderer yahei22U;
    public List<Degree> degrees;
    
    public CompassUtil(final float i, final float o, final float s, final int a, final boolean sh) {
        this.yahei28U = FontUtil.clientfont;
        this.yahei18U = FontUtil.clientsmall;
        this.yahei22U = FontUtil.clientmedium;
        this.degrees = (List<Degree>)Lists.newArrayList();
        this.innerWidth = i;
        this.outerWidth = o;
        this.scale = s;
        this.accuracy = a;
        this.shadow = sh;
        this.degrees.add(new Degree("N", 1));
        this.degrees.add(new Degree("195", 2));
        this.degrees.add(new Degree("210", 2));
        this.degrees.add(new Degree("NE", 3));
        this.degrees.add(new Degree("240", 2));
        this.degrees.add(new Degree("255", 2));
        this.degrees.add(new Degree("E", 1));
        this.degrees.add(new Degree("285", 2));
        this.degrees.add(new Degree("300", 2));
        this.degrees.add(new Degree("SE", 3));
        this.degrees.add(new Degree("330", 2));
        this.degrees.add(new Degree("345", 2));
        this.degrees.add(new Degree("S", 1));
        this.degrees.add(new Degree("15", 2));
        this.degrees.add(new Degree("30", 2));
        this.degrees.add(new Degree("SW", 3));
        this.degrees.add(new Degree("60", 2));
        this.degrees.add(new Degree("75", 2));
        this.degrees.add(new Degree("W", 1));
        this.degrees.add(new Degree("105", 2));
        this.degrees.add(new Degree("120", 2));
        this.degrees.add(new Degree("NW", 3));
        this.degrees.add(new Degree("150", 2));
        this.degrees.add(new Degree("165", 2));
    }
    
    public void draw(final ScaledResolution sr) {
        this.preRender(sr);
        final float center = (float)(sr.getScaledWidth() / 2);
        int count = 0;
        int cardinals = 0;
        int subCardinals = 0;
        int markers = 0;
        final float offset = 0.0f;
        final float yaaahhrewindTime = Minecraft.getMinecraft().thePlayer.rotationYaw % 360.0f * 2.0f + 1080.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        RenderUtils.doGlScissor(sr.getScaledWidth() / 2 - 120, 5, 220, 45);
        for (final Degree d : this.degrees) {
            final float location = center + count * 30 - yaaahhrewindTime;
            final float completeLocation = (float)((d.type == 1) ? (location - this.yahei28U.getStringWidth(d.text) / 2.0) : ((d.type == 2) ? (location - this.yahei18U.getStringWidth(d.text) / 2.0) : (location - this.yahei22U.getStringWidth(d.text) / 2.0)));
            final int opacity = this.opacity(sr, completeLocation);
            if (d.type == 1 && opacity != 16777215) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.yahei28U.drawString(d.text, completeLocation, 25.0f, this.opacity(sr, completeLocation));
                ++cardinals;
            }
            if (d.type == 2 && opacity != 16777215) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                RenderUtils.drawRect(location - 0.5f, 29.0, location + 0.5f, 34.0, this.opacity(sr, completeLocation));
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.yahei18U.drawString(d.text, completeLocation, 37.5f, this.opacity(sr, completeLocation));
                ++markers;
            }
            if (d.type == 3 && opacity != 16777215) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.yahei22U.drawString(d.text, completeLocation, (float)(25 + this.yahei28U.getHeight() / 2 - this.yahei22U.getHeight() / 2), this.opacity(sr, completeLocation));
                ++subCardinals;
            }
            ++count;
        }
        for (final Degree d : this.degrees) {
            final float location = center + count * 30 - yaaahhrewindTime;
            final float completeLocation = (float)((d.type == 1) ? (location - this.yahei28U.getStringWidth(d.text) / 2.0) : ((d.type == 2) ? (location - this.yahei18U.getStringWidth(d.text) / 2.0) : (location - this.yahei22U.getStringWidth(d.text) / 2.0)));
            if (d.type == 1) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.yahei28U.drawString(d.text, completeLocation, 25.0f, this.opacity(sr, completeLocation));
                ++cardinals;
            }
            if (d.type == 2) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                RenderUtils.drawRect(location - 0.5f, 29.0, location + 0.5f, 34.0, this.opacity(sr, completeLocation));
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.yahei18U.drawString(d.text, completeLocation, 37.5f, this.opacity(sr, completeLocation));
                ++markers;
            }
            if (d.type == 3) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.yahei22U.drawString(d.text, completeLocation, (float)(25 + this.yahei28U.getHeight() / 2 - this.yahei22U.getHeight() / 2), this.opacity(sr, completeLocation));
                ++subCardinals;
            }
            ++count;
        }
        for (final Degree d : this.degrees) {
            final float location = center + count * 30 - yaaahhrewindTime;
            final float completeLocation = (float)((d.type == 1) ? (location - this.yahei28U.getStringWidth(d.text) / 2.0) : ((d.type == 2) ? (location - this.yahei18U.getStringWidth(d.text) / 2.0) : (location - this.yahei22U.getStringWidth(d.text) / 2.0)));
            if (d.type == 1) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.yahei28U.drawString(d.text, completeLocation, 25.0f, this.opacity(sr, completeLocation));
                ++cardinals;
            }
            if (d.type == 2) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                RenderUtils.drawRect(location - 0.5f, 29.0, location + 0.5f, 34.0, this.opacity(sr, completeLocation));
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.yahei18U.drawString(d.text, completeLocation, 37.5f, this.opacity(sr, completeLocation));
                ++markers;
            }
            if (d.type == 3) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.yahei22U.drawString(d.text, completeLocation, (float)(25 + this.yahei28U.getHeight() / 2 - this.yahei22U.getHeight() / 2), this.opacity(sr, completeLocation));
                ++subCardinals;
            }
            ++count;
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
    }
    
    public void preRender(final ScaledResolution sr) {
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
    }
    
    public int opacity(final ScaledResolution sr, final float offset) {
        final int op = 0;
        final float offs = 255.0f - Math.abs(sr.getScaledWidth() / 2 - offset) * 1.8f;
        final Color c = new Color(255, 255, 255, (int)Math.min(Math.max(0.0f, offs), 255.0f));
        return c.getRGB();
    }
}
