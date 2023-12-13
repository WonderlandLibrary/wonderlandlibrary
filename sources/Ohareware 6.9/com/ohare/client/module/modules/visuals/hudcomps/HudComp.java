package com.ohare.client.module.modules.visuals.hudcomps;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;
import com.ohare.client.Client;
import com.ohare.client.event.events.game.TickEvent;
import com.ohare.client.module.modules.visuals.HUD;
import com.ohare.client.utils.value.Value;

import net.minecraft.client.gui.ScaledResolution;

/**
 * made by oHare for oHareWare
 *
 * @since 5/31/2019
 **/
public class HudComp {
    public double x, y, width, height;
    private String label;
    private boolean enabled = true;

    private List<Value> values = new ArrayList<>();

    public HudComp(String label, double x, double y, double width, double height) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void addValues(Value... value) {
        values.addAll(Arrays.asList(value));
    }

    public Value find(String term) {
        for (Value value : values) {
            if (value.getLabel().equalsIgnoreCase(term)) {
                return value;
            }
        }
        return null;
    }

    public List<Value> getValues() {
        return values;
    }

    public void onEnable() {}

    public void onDisable() {}

    public void onRender(ScaledResolution sr) {}

    public void onResize(ScaledResolution sr) {}

    public void onFullScreen(float w,float h) {}

    public void onDrag() {}

    public void onKey(int key) {}

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void save(JsonObject directory) {
        directory.addProperty("x", x);
        directory.addProperty("y", y);
        directory.addProperty("enabled", enabled);
        values.forEach(val -> {
            directory.addProperty(val.getLabel(), val.getValue().toString());
        });
    }

    public void load(JsonObject directory) {
        directory.entrySet().forEach(data -> {
            switch (data.getKey()) {
        		case "name":
        			return;
                case "x":
                    setX(data.getValue().getAsInt());
                    return;
                case "y":
                    setY(data.getValue().getAsInt());
                    return;
                case "enabled":
                    setEnabled(data.getValue().getAsBoolean());
                    return;
            }
            Value val = find(data.getKey());
            if (val != null) val.setValue(data.getValue().getAsString());
        });
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWidth(double width) {
        this.width = width;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
