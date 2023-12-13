package com.ohare.client.gui.tab.component;

import java.awt.Color;

import com.ohare.client.Client;
import com.ohare.client.module.modules.visuals.HUD;

import net.minecraft.client.gui.ScaledResolution;

/**
 * made by oHare for oHareWare
 *
 * @since 7/13/2019
 **/
public class Component {
    private float x, y, width, height;
    private String label;

    public Component(String label, float x, float y, float width, float height) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void init() {

    }

    public float getX() {
        return x;
    }

    public void draw(ScaledResolution sr) {

    }

    public void keypress(int key) {

    }

    public void updatePosition() {

    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public int color(int index, int count) {
        float[] hsb = new float[3];
        HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule("hud");
        Color.RGBtoHSB(Color.getHSBColor(HUD.hue.getValue(), 1, 1).getRed(), Color.getHSBColor(HUD.hue.getValue(), 1, 1).getGreen(), Color.getHSBColor(HUD.hue.getValue(), 1, 1).getBlue(), hsb);

        float brightness = Math.abs(((getOffset() + (index / (float) count) * 2) % 2) - 1);
        brightness = 0.4f + (0.4f * brightness);

        hsb[2] = brightness % 1f;
        return Color.HSBtoRGB(hsb[0],hsb[1], hsb[2]);
    }
    private float getOffset() {
        return (System.currentTimeMillis() % 2000) / 1000f;
    }
}
