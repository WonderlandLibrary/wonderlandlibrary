/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.util.Properties;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;
import optifine.Blender;
import optifine.Config;
import optifine.ConnectedParser;
import optifine.RangeListInt;
import optifine.TextureUtils;

public class CustomSkyLayer {
    public String source = null;
    private int startFadeIn = -1;
    private int endFadeIn = -1;
    private int startFadeOut = -1;
    private int endFadeOut = -1;
    private int blend = 1;
    private boolean rotate = false;
    private float speed = 1.0f;
    private float[] axis = DEFAULT_AXIS;
    private RangeListInt days = null;
    private int daysLoop = 8;
    public int textureId = -1;
    public static final float[] DEFAULT_AXIS = new float[]{1.0f, 0.0f, 0.0f};

    public CustomSkyLayer(Properties props, String defSource) {
        ConnectedParser cp = new ConnectedParser("CustomSky");
        this.source = props.getProperty("source", defSource);
        this.startFadeIn = this.parseTime(props.getProperty("startFadeIn"));
        this.endFadeIn = this.parseTime(props.getProperty("endFadeIn"));
        this.startFadeOut = this.parseTime(props.getProperty("startFadeOut"));
        this.endFadeOut = this.parseTime(props.getProperty("endFadeOut"));
        this.blend = Blender.parseBlend(props.getProperty("blend"));
        this.rotate = this.parseBoolean(props.getProperty("rotate"), true);
        this.speed = this.parseFloat(props.getProperty("speed"), 1.0f);
        this.axis = this.parseAxis(props.getProperty("axis"), DEFAULT_AXIS);
        this.days = cp.parseRangeListInt(props.getProperty("days"));
        this.daysLoop = cp.parseInt(props.getProperty("daysLoop"), 8);
    }

    private int parseTime(String str) {
        if (str == null) {
            return -1;
        }
        String[] strs = Config.tokenize(str, ":");
        if (strs.length != 2) {
            Config.warn("Invalid time: " + str);
            return -1;
        }
        String hourStr = strs[0];
        String minStr = strs[1];
        int hour = Config.parseInt(hourStr, -1);
        int min = Config.parseInt(minStr, -1);
        if (hour >= 0 && hour <= 23 && min >= 0 && min <= 59) {
            if ((hour -= 6) < 0) {
                hour += 24;
            }
            int time = hour * 1000 + (int)((double)min / 60.0 * 1000.0);
            return time;
        }
        Config.warn("Invalid time: " + str);
        return -1;
    }

    private boolean parseBoolean(String str, boolean defVal) {
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

    private float parseFloat(String str, float defVal) {
        if (str == null) {
            return defVal;
        }
        float val = Config.parseFloat(str, Float.MIN_VALUE);
        if (val == Float.MIN_VALUE) {
            Config.warn("Invalid value: " + str);
            return defVal;
        }
        return val;
    }

    private float[] parseAxis(String str, float[] defVal) {
        if (str == null) {
            return defVal;
        }
        String[] strs = Config.tokenize(str, " ");
        if (strs.length != 3) {
            Config.warn("Invalid axis: " + str);
            return defVal;
        }
        float[] fs = new float[3];
        for (int ax = 0; ax < strs.length; ++ax) {
            fs[ax] = Config.parseFloat(strs[ax], Float.MIN_VALUE);
            if (fs[ax] == Float.MIN_VALUE) {
                Config.warn("Invalid axis: " + str);
                return defVal;
            }
            if (!(fs[ax] < -1.0f) && !(fs[ax] > 1.0f)) continue;
            Config.warn("Invalid axis values: " + str);
            return defVal;
        }
        float var9 = fs[0];
        float ay = fs[1];
        float az = fs[2];
        if (var9 * var9 + ay * ay + az * az < 1.0E-5f) {
            Config.warn("Invalid axis values: " + str);
            return defVal;
        }
        float[] as = new float[]{az, ay, -var9};
        return as;
    }

    public boolean isValid(String path) {
        if (this.source == null) {
            Config.warn("No source texture: " + path);
            return false;
        }
        this.source = TextureUtils.fixResourcePath(this.source, TextureUtils.getBasePath(path));
        if (this.startFadeIn >= 0 && this.endFadeIn >= 0 && this.endFadeOut >= 0) {
            int timeOff;
            int timeFadeOut;
            int timeOn;
            int timeSum;
            int timeFadeIn = this.normalizeTime(this.endFadeIn - this.startFadeIn);
            if (this.startFadeOut < 0) {
                this.startFadeOut = this.normalizeTime(this.endFadeOut - timeFadeIn);
                if (this.timeBetween(this.startFadeOut, this.startFadeIn, this.endFadeIn)) {
                    this.startFadeOut = this.endFadeIn;
                }
            }
            if ((timeSum = timeFadeIn + (timeOn = this.normalizeTime(this.startFadeOut - this.endFadeIn)) + (timeFadeOut = this.normalizeTime(this.endFadeOut - this.startFadeOut)) + (timeOff = this.normalizeTime(this.startFadeIn - this.endFadeOut))) != 24000) {
                Config.warn("Invalid fadeIn/fadeOut times, sum is not 24h: " + timeSum);
                return false;
            }
            if (this.speed < 0.0f) {
                Config.warn("Invalid speed: " + this.speed);
                return false;
            }
            if (this.daysLoop <= 0) {
                Config.warn("Invalid daysLoop: " + this.daysLoop);
                return false;
            }
            return true;
        }
        Config.warn("Invalid times, required are: startFadeIn, endFadeIn and endFadeOut.");
        return false;
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

    public void render(int timeOfDay, float celestialAngle, float rainBrightness) {
        float brightness = rainBrightness * this.getFadeBrightness(timeOfDay);
        if ((brightness = Config.limit(brightness, 0.0f, 1.0f)) >= 1.0E-4f) {
            GlStateManager.func_179144_i(this.textureId);
            Blender.setupBlend(this.blend, brightness);
            GlStateManager.pushMatrix();
            if (this.rotate) {
                GlStateManager.rotate(celestialAngle * 360.0f * this.speed, this.axis[0], this.axis[1], this.axis[2]);
            }
            Tessellator tess = Tessellator.getInstance();
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

    private float getFadeBrightness(int timeOfDay) {
        if (this.timeBetween(timeOfDay, this.startFadeIn, this.endFadeIn)) {
            int timeFadeOut = this.normalizeTime(this.endFadeIn - this.startFadeIn);
            int timeDiff = this.normalizeTime(timeOfDay - this.startFadeIn);
            return (float)timeDiff / (float)timeFadeOut;
        }
        if (this.timeBetween(timeOfDay, this.endFadeIn, this.startFadeOut)) {
            return 1.0f;
        }
        if (this.timeBetween(timeOfDay, this.startFadeOut, this.endFadeOut)) {
            int timeFadeOut = this.normalizeTime(this.endFadeOut - this.startFadeOut);
            int timeDiff = this.normalizeTime(timeOfDay - this.startFadeOut);
            return 1.0f - (float)timeDiff / (float)timeFadeOut;
        }
        return 0.0f;
    }

    private void renderSide(Tessellator tess, int side) {
        WorldRenderer wr = tess.getWorldRenderer();
        double tx = (double)(side % 3) / 3.0;
        double ty = (double)(side / 3) / 2.0;
        wr.startDrawingQuads();
        wr.addVertexWithUV(-100.0, -100.0, -100.0, tx, ty);
        wr.addVertexWithUV(-100.0, -100.0, 100.0, tx, ty + 0.5);
        wr.addVertexWithUV(100.0, -100.0, 100.0, tx + 0.3333333333333333, ty + 0.5);
        wr.addVertexWithUV(100.0, -100.0, -100.0, tx + 0.3333333333333333, ty);
        tess.draw();
    }

    public boolean isActive(World world, int timeOfDay) {
        if (this.timeBetween(timeOfDay, this.endFadeOut, this.startFadeIn)) {
            return false;
        }
        if (this.days != null) {
            long timeShift;
            long time = world.getWorldTime();
            for (timeShift = time - (long)this.startFadeIn; timeShift < 0L; timeShift += (long)(24000 * this.daysLoop)) {
            }
            int day = (int)(timeShift / 24000L);
            int dayOfLoop = day % this.daysLoop;
            if (!this.days.isInRange(dayOfLoop)) {
                return false;
            }
        }
        return true;
    }

    private boolean timeBetween(int timeOfDay, int timeStart, int timeEnd) {
        return timeStart <= timeEnd ? timeOfDay >= timeStart && timeOfDay <= timeEnd : timeOfDay >= timeStart || timeOfDay <= timeEnd;
    }
}

