package com.canon.majik.api.utils.client;

import java.awt.Color;

import net.minecraft.client.renderer.GlStateManager;

public class TColor extends Color {

    private static final long serialVersionUID = 1L;

    public TColor(int rgb) {
        super(rgb);
    }

    public TColor(int rgba, boolean hasalpha) {
        super(rgba,hasalpha);
    }

    public TColor(int r, int g, int b) {
        super(r,g,b);
    }

    public TColor(int r, int g, int b, int a) {
        super(r,g,b,a);
    }

    public TColor(Color color) {
        super(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha());
    }

    public TColor(TColor color, int a) {
        super(color.getRed(),color.getGreen(),color.getBlue(),a);
    }

    public static TColor fromHSB (float hue, float saturation, float brightness) {
        return new TColor(Color.getHSBColor(hue,saturation,brightness));
    }

    public float getHue() {
        return RGBtoHSB(getRed(),getGreen(),getBlue(),null)[0];
    }

    public float getSaturation() {
        return RGBtoHSB(getRed(),getGreen(),getBlue(),null)[1];
    }

    public float getBrightness() {
        return RGBtoHSB(getRed(),getGreen(),getBlue(),null)[2];
    }

    public void glColor() {
        GlStateManager.color(getRed()/255.0f,getGreen()/255.0f,getBlue()/255.0f,getAlpha()/255.0f);
    }

}
