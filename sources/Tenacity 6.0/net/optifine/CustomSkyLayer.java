// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.optifine.util.NumUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;
import net.optifine.config.Matches;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.optifine.util.TextureUtils;
import net.minecraft.src.Config;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.optifine.render.Blender;
import net.optifine.config.ConnectedParser;
import java.util.Properties;
import net.minecraft.world.World;
import net.optifine.util.SmoothFloat;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.config.RangeListInt;

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
    private RangeListInt days;
    private int daysLoop;
    private boolean weatherClear;
    private boolean weatherRain;
    private boolean weatherThunder;
    public BiomeGenBase[] biomes;
    public RangeListInt heights;
    private float transition;
    private SmoothFloat smoothPositionBrightness;
    public int textureId;
    private World lastWorld;
    public static final float[] DEFAULT_AXIS;
    private static final String WEATHER_CLEAR = "clear";
    private static final String WEATHER_RAIN = "rain";
    private static final String WEATHER_THUNDER = "thunder";
    
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
        this.days = null;
        this.daysLoop = 8;
        this.weatherClear = true;
        this.weatherRain = false;
        this.weatherThunder = false;
        this.biomes = null;
        this.heights = null;
        this.transition = 1.0f;
        this.smoothPositionBrightness = null;
        this.textureId = -1;
        this.lastWorld = null;
        final ConnectedParser connectedparser = new ConnectedParser("CustomSky");
        this.source = props.getProperty("source", defSource);
        this.startFadeIn = this.parseTime(props.getProperty("startFadeIn"));
        this.endFadeIn = this.parseTime(props.getProperty("endFadeIn"));
        this.startFadeOut = this.parseTime(props.getProperty("startFadeOut"));
        this.endFadeOut = this.parseTime(props.getProperty("endFadeOut"));
        this.blend = Blender.parseBlend(props.getProperty("blend"));
        this.rotate = this.parseBoolean(props.getProperty("rotate"), true);
        this.speed = this.parseFloat(props.getProperty("speed"), 1.0f);
        this.axis = this.parseAxis(props.getProperty("axis"), CustomSkyLayer.DEFAULT_AXIS);
        this.days = connectedparser.parseRangeListInt(props.getProperty("days"));
        this.daysLoop = connectedparser.parseInt(props.getProperty("daysLoop"), 8);
        final List<String> list = this.parseWeatherList(props.getProperty("weather", "clear"));
        this.weatherClear = list.contains("clear");
        this.weatherRain = list.contains("rain");
        this.weatherThunder = list.contains("thunder");
        this.biomes = connectedparser.parseBiomes(props.getProperty("biomes"));
        this.heights = connectedparser.parseRangeListInt(props.getProperty("heights"));
        this.transition = this.parseFloat(props.getProperty("transition"), 1.0f);
    }
    
    private List<String> parseWeatherList(final String str) {
        final List<String> list = Arrays.asList("clear", "rain", "thunder");
        final List<String> list2 = new ArrayList<String>();
        final String[] astring = Config.tokenize(str, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            if (!list.contains(s)) {
                Config.warn("Unknown weather: " + s);
            }
            else {
                list2.add(s);
            }
        }
        return list2;
    }
    
    private int parseTime(final String str) {
        if (str == null) {
            return -1;
        }
        final String[] astring = Config.tokenize(str, ":");
        if (astring.length != 2) {
            Config.warn("Invalid time: " + str);
            return -1;
        }
        final String s = astring[0];
        final String s2 = astring[1];
        int i = Config.parseInt(s, -1);
        final int j = Config.parseInt(s2, -1);
        if (i >= 0 && i <= 23 && j >= 0 && j <= 59) {
            i -= 6;
            if (i < 0) {
                i += 24;
            }
            final int k = i * 1000 + (int)(j / 60.0 * 1000.0);
            return k;
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
        final float f = Config.parseFloat(str, Float.MIN_VALUE);
        if (f == Float.MIN_VALUE) {
            Config.warn("Invalid value: " + str);
            return defVal;
        }
        return f;
    }
    
    private float[] parseAxis(final String str, final float[] defVal) {
        if (str == null) {
            return defVal;
        }
        final String[] astring = Config.tokenize(str, " ");
        if (astring.length != 3) {
            Config.warn("Invalid axis: " + str);
            return defVal;
        }
        final float[] afloat = new float[3];
        for (int i = 0; i < astring.length; ++i) {
            afloat[i] = Config.parseFloat(astring[i], Float.MIN_VALUE);
            if (afloat[i] == Float.MIN_VALUE) {
                Config.warn("Invalid axis: " + str);
                return defVal;
            }
            if (afloat[i] < -1.0f || afloat[i] > 1.0f) {
                Config.warn("Invalid axis values: " + str);
                return defVal;
            }
        }
        final float f2 = afloat[0];
        final float f3 = afloat[1];
        final float f4 = afloat[2];
        if (f2 * f2 + f3 * f3 + f4 * f4 < 1.0E-5f) {
            Config.warn("Invalid axis values: " + str);
            return defVal;
        }
        final float[] afloat2 = { f4, f3, -f2 };
        return afloat2;
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
        final int i = this.normalizeTime(this.endFadeIn - this.startFadeIn);
        if (this.startFadeOut < 0) {
            this.startFadeOut = this.normalizeTime(this.endFadeOut - i);
            if (this.timeBetween(this.startFadeOut, this.startFadeIn, this.endFadeIn)) {
                this.startFadeOut = this.endFadeIn;
            }
        }
        final int j = this.normalizeTime(this.startFadeOut - this.endFadeIn);
        final int k = this.normalizeTime(this.endFadeOut - this.startFadeOut);
        final int l = this.normalizeTime(this.startFadeIn - this.endFadeOut);
        final int i2 = i + j + k + l;
        if (i2 != 24000) {
            Config.warn("Invalid fadeIn/fadeOut times, sum is not 24h: " + i2);
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
    
    private int normalizeTime(int timeMc) {
        while (timeMc >= 24000) {
            timeMc -= 24000;
        }
        while (timeMc < 0) {
            timeMc += 24000;
        }
        return timeMc;
    }
    
    public void render(final World world, final int timeOfDay, final float celestialAngle, final float rainStrength, final float thunderStrength) {
        final float f = this.getPositionBrightness(world);
        final float f2 = this.getWeatherBrightness(rainStrength, thunderStrength);
        final float f3 = this.getFadeBrightness(timeOfDay);
        float f4 = f * f2 * f3;
        f4 = Config.limit(f4, 0.0f, 1.0f);
        if (f4 >= 1.0E-4f) {
            GlStateManager.bindTexture(this.textureId);
            Blender.setupBlend(this.blend, f4);
            GlStateManager.pushMatrix();
            if (this.rotate) {
                float f5 = 0.0f;
                if (this.speed != Math.round(this.speed)) {
                    final long i = (world.getWorldTime() + 18000L) / 24000L;
                    final double d0 = this.speed % 1.0f;
                    final double d2 = i * d0;
                    f5 = (float)(d2 % 1.0);
                }
                GlStateManager.rotate(360.0f * (f5 + celestialAngle * this.speed), this.axis[0], this.axis[1], this.axis[2]);
            }
            final Tessellator tessellator = Tessellator.getInstance();
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
            this.renderSide(tessellator, 4);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            this.renderSide(tessellator, 1);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            this.renderSide(tessellator, 0);
            GlStateManager.popMatrix();
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            this.renderSide(tessellator, 5);
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            this.renderSide(tessellator, 2);
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            this.renderSide(tessellator, 3);
            GlStateManager.popMatrix();
        }
    }
    
    private float getPositionBrightness(final World world) {
        if (this.biomes == null && this.heights == null) {
            return 1.0f;
        }
        float f = this.getPositionBrightnessRaw(world);
        if (this.smoothPositionBrightness == null) {
            this.smoothPositionBrightness = new SmoothFloat(f, this.transition);
        }
        f = this.smoothPositionBrightness.getSmoothValue(f);
        return f;
    }
    
    private float getPositionBrightnessRaw(final World world) {
        final Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        if (entity == null) {
            return 0.0f;
        }
        final BlockPos blockpos = entity.getPosition();
        if (this.biomes != null) {
            final BiomeGenBase biomegenbase = world.getBiomeGenForCoords(blockpos);
            if (biomegenbase == null) {
                return 0.0f;
            }
            if (!Matches.biome(biomegenbase, this.biomes)) {
                return 0.0f;
            }
        }
        return (this.heights != null && !this.heights.isInRange(blockpos.getY())) ? 0.0f : 1.0f;
    }
    
    private float getWeatherBrightness(final float rainStrength, final float thunderStrength) {
        final float f = 1.0f - rainStrength;
        final float f2 = rainStrength - thunderStrength;
        float f3 = 0.0f;
        if (this.weatherClear) {
            f3 += f;
        }
        if (this.weatherRain) {
            f3 += f2;
        }
        if (this.weatherThunder) {
            f3 += thunderStrength;
        }
        f3 = NumUtils.limit(f3, 0.0f, 1.0f);
        return f3;
    }
    
    private float getFadeBrightness(final int timeOfDay) {
        if (this.timeBetween(timeOfDay, this.startFadeIn, this.endFadeIn)) {
            final int k = this.normalizeTime(this.endFadeIn - this.startFadeIn);
            final int l = this.normalizeTime(timeOfDay - this.startFadeIn);
            return l / (float)k;
        }
        if (this.timeBetween(timeOfDay, this.endFadeIn, this.startFadeOut)) {
            return 1.0f;
        }
        if (this.timeBetween(timeOfDay, this.startFadeOut, this.endFadeOut)) {
            final int i = this.normalizeTime(this.endFadeOut - this.startFadeOut);
            final int j = this.normalizeTime(timeOfDay - this.startFadeOut);
            return 1.0f - j / (float)i;
        }
        return 0.0f;
    }
    
    private void renderSide(final Tessellator tess, final int side) {
        final WorldRenderer worldrenderer = tess.getWorldRenderer();
        final double d0 = side % 3 / 3.0;
        final double d2 = side / 3 / 2.0;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-100.0, -100.0, -100.0).tex(d0, d2).endVertex();
        worldrenderer.pos(-100.0, -100.0, 100.0).tex(d0, d2 + 0.5).endVertex();
        worldrenderer.pos(100.0, -100.0, 100.0).tex(d0 + 0.3333333333333333, d2 + 0.5).endVertex();
        worldrenderer.pos(100.0, -100.0, -100.0).tex(d0 + 0.3333333333333333, d2).endVertex();
        tess.draw();
    }
    
    public boolean isActive(final World world, final int timeOfDay) {
        if (world != this.lastWorld) {
            this.lastWorld = world;
            this.smoothPositionBrightness = null;
        }
        if (this.timeBetween(timeOfDay, this.endFadeOut, this.startFadeIn)) {
            return false;
        }
        if (this.days != null) {
            final long i = world.getWorldTime();
            long j;
            for (j = i - this.startFadeIn; j < 0L; j += 24000 * this.daysLoop) {}
            final int k = (int)(j / 24000L);
            final int l = k % this.daysLoop;
            if (!this.days.isInRange(l)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean timeBetween(final int timeOfDay, final int timeStart, final int timeEnd) {
        return (timeStart <= timeEnd) ? (timeOfDay >= timeStart && timeOfDay <= timeEnd) : (timeOfDay >= timeStart || timeOfDay <= timeEnd);
    }
    
    @Override
    public String toString() {
        return "" + this.source + ", " + this.startFadeIn + "-" + this.endFadeIn + " " + this.startFadeOut + "-" + this.endFadeOut;
    }
    
    static {
        DEFAULT_AXIS = new float[] { 1.0f, 0.0f, 0.0f };
    }
}
