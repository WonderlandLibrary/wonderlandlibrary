// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Properties;

public class CustomSkyLayer
{
    public String source;
    private int startFadeIn;
    private int endFadeIn;
    private int startFadeOut;
    private int endFadeOut;
    private int blend;
    private boolean rotate;
    private float speed;
    private float[] axis;
    public int textureId;
    public static final float[] DEFAULT_AXIS;
    
    public CustomSkyLayer(final Properties props, final String defSource) {
        this.source = null;
        this.startFadeIn = -1;
        this.endFadeIn = -1;
        this.startFadeOut = -1;
        this.endFadeOut = -1;
        this.blend = 1;
        this.rotate = false;
        this.speed = 1.0f;
        this.axis = CustomSkyLayer.DEFAULT_AXIS;
        this.textureId = -1;
        this.source = props.getProperty("source", defSource);
        this.startFadeIn = this.parseTime(props.getProperty("startFadeIn"));
        this.endFadeIn = this.parseTime(props.getProperty("endFadeIn"));
        this.startFadeOut = this.parseTime(props.getProperty("startFadeOut"));
        this.endFadeOut = this.parseTime(props.getProperty("endFadeOut"));
        this.blend = Blender.parseBlend(props.getProperty("blend"));
        this.rotate = this.parseBoolean(props.getProperty("rotate"), true);
        this.speed = this.parseFloat(props.getProperty("speed"), 1.0f);
        this.axis = this.parseAxis(props.getProperty("axis"), CustomSkyLayer.DEFAULT_AXIS);
    }
    
    private int parseTime(final String str) {
        if (str == null) {
            return -1;
        }
        final String[] strs = Config.tokenize(str, ":");
        if (strs.length != 2) {
            Config.warn("Invalid time: " + str);
            return -1;
        }
        final String hourStr = strs[0];
        final String minStr = strs[1];
        int hour = Config.parseInt(hourStr, -1);
        final int min = Config.parseInt(minStr, -1);
        if (hour >= 0 && hour <= 23 && min >= 0 && min <= 59) {
            hour -= 6;
            if (hour < 0) {
                hour += 24;
            }
            final int time = hour * 1000 + (int)(min / 60.0 * 1000.0);
            return time;
        }
        Config.warn("Invalid time: " + str);
        return -1;
    }
    
    private boolean parseBoolean(final String str, final boolean defVal) {
        if (str == null) {
            return defVal;
        }
        if (str.toLowerCase().equals("true")) {
            return true;
        }
        if (str.toLowerCase().equals("false")) {
            return false;
        }
        Config.warn("Unknown boolean: " + str);
        return defVal;
    }
    
    private float parseFloat(final String str, final float defVal) {
        if (str == null) {
            return defVal;
        }
        final float val = Config.parseFloat(str, Float.MIN_VALUE);
        if (val == Float.MIN_VALUE) {
            Config.warn("Invalid value: " + str);
            return defVal;
        }
        return val;
    }
    
    private float[] parseAxis(final String str, final float[] defVal) {
        if (str == null) {
            return defVal;
        }
        final String[] strs = Config.tokenize(str, " ");
        if (strs.length != 3) {
            Config.warn("Invalid axis: " + str);
            return defVal;
        }
        final float[] fs = new float[3];
        for (int ax = 0; ax < strs.length; ++ax) {
            fs[ax] = Config.parseFloat(strs[ax], Float.MIN_VALUE);
            if (fs[ax] == Float.MIN_VALUE) {
                Config.warn("Invalid axis: " + str);
                return defVal;
            }
            if (fs[ax] < -1.0f || fs[ax] > 1.0f) {
                Config.warn("Invalid axis values: " + str);
                return defVal;
            }
        }
        final float var9 = fs[0];
        final float ay = fs[1];
        final float az = fs[2];
        if (var9 * var9 + ay * ay + az * az < 1.0E-5f) {
            Config.warn("Invalid axis values: " + str);
            return defVal;
        }
        final float[] as = { az, ay, -var9 };
        return as;
    }
    
    public boolean isValid(final String path) {
        if (this.source == null) {
            Config.warn("No source texture: " + path);
            return false;
        }
        this.source = TextureUtils.fixResourcePath(this.source, TextureUtils.getBasePath(path));
        if (this.startFadeIn < 0 || this.endFadeIn < 0 || this.endFadeOut < 0) {
            Config.warn("Invalid times, required are: startFadeIn, endFadeIn and endFadeOut.");
            return false;
        }
        final int timeFadeIn = this.normalizeTime(this.endFadeIn - this.startFadeIn);
        if (this.startFadeOut < 0) {
            this.startFadeOut = this.normalizeTime(this.endFadeOut - timeFadeIn);
        }
        final int timeOn = this.normalizeTime(this.startFadeOut - this.endFadeIn);
        final int timeFadeOut = this.normalizeTime(this.endFadeOut - this.startFadeOut);
        final int timeOff = this.normalizeTime(this.startFadeIn - this.endFadeOut);
        final int timeSum = timeFadeIn + timeOn + timeFadeOut + timeOff;
        if (timeSum != 24000) {
            Config.warn("Invalid fadeIn/fadeOut times, sum is more than 24h: " + timeSum);
            return false;
        }
        if (this.speed < 0.0f) {
            Config.warn("Invalid speed: " + this.speed);
            return false;
        }
        return true;
    }
    
    private int normalizeTime(int timeMc) {
        while (timeMc >= 24000) {
            timeMc -= 24000;
        }
        while (timeMc < 0) {
            timeMc += 24000;
        }
        return timeMc;
    }
    
    public void render(final int timeOfDay, final float celestialAngle, final float rainBrightness) {
        float brightness = rainBrightness * this.getFadeBrightness(timeOfDay);
        brightness = Config.limit(brightness, 0.0f, 1.0f);
        if (brightness >= 1.0E-4f) {
            GlStateManager.func_179144_i(this.textureId);
            Blender.setupBlend(this.blend, brightness);
            GlStateManager.pushMatrix();
            if (this.rotate) {
                GlStateManager.rotate(celestialAngle * 360.0f * this.speed, this.axis[0], this.axis[1], this.axis[2]);
            }
            final Tessellator tess = Tessellator.getInstance();
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
            this.renderSide(tess, 4);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            this.renderSide(tess, 1);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            this.renderSide(tess, 0);
            GlStateManager.popMatrix();
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            this.renderSide(tess, 5);
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            this.renderSide(tess, 2);
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            this.renderSide(tess, 3);
            GlStateManager.popMatrix();
        }
    }
    
    private float getFadeBrightness(final int timeOfDay) {
        if (this.timeBetween(timeOfDay, this.startFadeIn, this.endFadeIn)) {
            final int timeFadeOut = this.normalizeTime(this.endFadeIn - this.startFadeIn);
            final int timeDiff = this.normalizeTime(timeOfDay - this.startFadeIn);
            return timeDiff / timeFadeOut;
        }
        if (this.timeBetween(timeOfDay, this.endFadeIn, this.startFadeOut)) {
            return 1.0f;
        }
        if (this.timeBetween(timeOfDay, this.startFadeOut, this.endFadeOut)) {
            final int timeFadeOut = this.normalizeTime(this.endFadeOut - this.startFadeOut);
            final int timeDiff = this.normalizeTime(timeOfDay - this.startFadeOut);
            return 1.0f - timeDiff / timeFadeOut;
        }
        return 0.0f;
    }
    
    private void renderSide(final Tessellator tess, final int side) {
        final WorldRenderer wr = tess.getWorldRenderer();
        final double tx = side % 3 / 3.0;
        final double ty = side / 3 / 2.0;
        wr.startDrawingQuads();
        wr.addVertexWithUV(-100.0, -100.0, -100.0, tx, ty);
        wr.addVertexWithUV(-100.0, -100.0, 100.0, tx, ty + 0.5);
        wr.addVertexWithUV(100.0, -100.0, 100.0, tx + 0.3333333333333333, ty + 0.5);
        wr.addVertexWithUV(100.0, -100.0, -100.0, tx + 0.3333333333333333, ty);
        tess.draw();
    }
    
    public boolean isActive(final int timeOfDay) {
        return !this.timeBetween(timeOfDay, this.endFadeOut, this.startFadeIn);
    }
    
    private boolean timeBetween(final int timeOfDay, final int timeStart, final int timeEnd) {
        return (timeStart <= timeEnd) ? (timeOfDay >= timeStart && timeOfDay <= timeEnd) : (timeOfDay >= timeStart || timeOfDay <= timeEnd);
    }
    
    static {
        DEFAULT_AXIS = new float[] { 1.0f, 0.0f, 0.0f };
    }
}
