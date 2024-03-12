// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.settings.impl;

import com.google.gson.JsonObject;
import dev.tenacity.utils.render.ColorUtil;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import java.awt.Color;
import dev.tenacity.module.settings.Setting;

public class ColorSetting extends Setting
{
    private float hue;
    private float saturation;
    private float brightness;
    private Rainbow rainbow;
    
    public ColorSetting(final String name, final Color defaultColor) {
        this.hue = 0.0f;
        this.saturation = 1.0f;
        this.brightness = 1.0f;
        this.rainbow = null;
        this.name = name;
        this.setColor(defaultColor);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public Color getColor() {
        return this.isRainbow() ? this.getRainbow().getColor() : Color.getHSBColor(this.hue, this.saturation, this.brightness);
    }
    
    public Color getAltColor() {
        return this.isRainbow() ? this.getRainbow().getColor(40) : ColorUtil.darker(this.getColor(), 0.6f);
    }
    
    public void setColor(final Color color) {
        final float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
    }
    
    public void setColor(final float hue, final float saturation, final float brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public double getHue() {
        return this.hue;
    }
    
    public void setHue(final float hue) {
        this.hue = hue;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public double getSaturation() {
        return this.saturation;
    }
    
    public void setSaturation(final float saturation) {
        this.saturation = saturation;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public double getBrightness() {
        return this.brightness;
    }
    
    public void setBrightness(final float brightness) {
        this.brightness = brightness;
    }
    
    public String getHexCode() {
        final Color color = this.getColor();
        return String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public Rainbow getRainbow() {
        return this.rainbow;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setRainbow(final boolean rainbow) {
        if (rainbow) {
            this.rainbow = new Rainbow();
        }
        else {
            this.rainbow = null;
        }
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public boolean isRainbow() {
        return this.rainbow != null;
    }
    
    @Override
    public Object getConfigValue() {
        return this.isRainbow() ? this.getRainbow().getJsonObject() : Integer.valueOf(this.getColor().getRGB());
    }
    
    public static class Rainbow
    {
        private float saturation;
        private int speed;
        
        public Rainbow() {
            this.saturation = 1.0f;
            this.speed = 15;
        }
        
        @Exclude({ Strategy.NAME_REMAPPING })
        public Color getColor() {
            return this.getColor(0);
        }
        
        @Exclude({ Strategy.NAME_REMAPPING })
        public Color getColor(final int index) {
            return ColorUtil.rainbow(this.speed, index, this.saturation, 1.0f, 1.0f);
        }
        
        public JsonObject getJsonObject() {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("saturation", (Number)this.saturation);
            jsonObject.addProperty("speed", (Number)this.speed);
            return jsonObject;
        }
        
        @Exclude({ Strategy.NAME_REMAPPING })
        public float getSaturation() {
            return Math.max(0.0f, Math.min(1.0f, this.saturation));
        }
        
        @Exclude({ Strategy.NAME_REMAPPING })
        public void setSaturation(final float saturation) {
            this.saturation = saturation;
        }
        
        @Exclude({ Strategy.NAME_REMAPPING })
        public int getSpeed() {
            return this.speed;
        }
        
        @Exclude({ Strategy.NAME_REMAPPING })
        public void setSpeed(final int speed) {
            this.speed = speed;
        }
    }
}
