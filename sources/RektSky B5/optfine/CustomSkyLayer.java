/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import java.util.Properties;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import optfine.Blender;
import optfine.Config;
import optfine.TextureUtils;

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
    public int textureId = -1;
    public static final float[] DEFAULT_AXIS = new float[]{1.0f, 0.0f, 0.0f};

    public CustomSkyLayer(Properties p_i31_1_, String p_i31_2_) {
        this.source = p_i31_1_.getProperty("source", p_i31_2_);
        this.startFadeIn = this.parseTime(p_i31_1_.getProperty("startFadeIn"));
        this.endFadeIn = this.parseTime(p_i31_1_.getProperty("endFadeIn"));
        this.startFadeOut = this.parseTime(p_i31_1_.getProperty("startFadeOut"));
        this.endFadeOut = this.parseTime(p_i31_1_.getProperty("endFadeOut"));
        this.blend = Blender.parseBlend(p_i31_1_.getProperty("blend"));
        this.rotate = this.parseBoolean(p_i31_1_.getProperty("rotate"), true);
        this.speed = this.parseFloat(p_i31_1_.getProperty("speed"), 1.0f);
        this.axis = this.parseAxis(p_i31_1_.getProperty("axis"), DEFAULT_AXIS);
    }

    private int parseTime(String p_parseTime_1_) {
        if (p_parseTime_1_ == null) {
            return -1;
        }
        String[] astring = Config.tokenize(p_parseTime_1_, ":");
        if (astring.length != 2) {
            Config.warn("Invalid time: " + p_parseTime_1_);
            return -1;
        }
        String s2 = astring[0];
        String s1 = astring[1];
        int i2 = Config.parseInt(s2, -1);
        int j2 = Config.parseInt(s1, -1);
        if (i2 >= 0 && i2 <= 23 && j2 >= 0 && j2 <= 59) {
            if ((i2 -= 6) < 0) {
                i2 += 24;
            }
            int k2 = i2 * 1000 + (int)((double)j2 / 60.0 * 1000.0);
            return k2;
        }
        Config.warn("Invalid time: " + p_parseTime_1_);
        return -1;
    }

    private boolean parseBoolean(String p_parseBoolean_1_, boolean p_parseBoolean_2_) {
        if (p_parseBoolean_1_ == null) {
            return p_parseBoolean_2_;
        }
        if (p_parseBoolean_1_.toLowerCase().equals("true")) {
            return true;
        }
        if (p_parseBoolean_1_.toLowerCase().equals("false")) {
            return false;
        }
        Config.warn("Unknown boolean: " + p_parseBoolean_1_);
        return p_parseBoolean_2_;
    }

    private float parseFloat(String p_parseFloat_1_, float p_parseFloat_2_) {
        if (p_parseFloat_1_ == null) {
            return p_parseFloat_2_;
        }
        float f2 = Config.parseFloat(p_parseFloat_1_, Float.MIN_VALUE);
        if (f2 == Float.MIN_VALUE) {
            Config.warn("Invalid value: " + p_parseFloat_1_);
            return p_parseFloat_2_;
        }
        return f2;
    }

    private float[] parseAxis(String p_parseAxis_1_, float[] p_parseAxis_2_) {
        if (p_parseAxis_1_ == null) {
            return p_parseAxis_2_;
        }
        String[] astring = Config.tokenize(p_parseAxis_1_, " ");
        if (astring.length != 3) {
            Config.warn("Invalid axis: " + p_parseAxis_1_);
            return p_parseAxis_2_;
        }
        float[] afloat = new float[3];
        for (int i2 = 0; i2 < astring.length; ++i2) {
            afloat[i2] = Config.parseFloat(astring[i2], Float.MIN_VALUE);
            if (afloat[i2] == Float.MIN_VALUE) {
                Config.warn("Invalid axis: " + p_parseAxis_1_);
                return p_parseAxis_2_;
            }
            if (!(afloat[i2] < -1.0f) && !(afloat[i2] > 1.0f)) continue;
            Config.warn("Invalid axis values: " + p_parseAxis_1_);
            return p_parseAxis_2_;
        }
        float f2 = afloat[0];
        float f3 = afloat[1];
        float f1 = afloat[2];
        if (f2 * f2 + f3 * f3 + f1 * f1 < 1.0E-5f) {
            Config.warn("Invalid axis values: " + p_parseAxis_1_);
            return p_parseAxis_2_;
        }
        float[] afloat1 = new float[]{f1, f3, -f2};
        return afloat1;
    }

    public boolean isValid(String p_isValid_1_) {
        if (this.source == null) {
            Config.warn("No source texture: " + p_isValid_1_);
            return false;
        }
        this.source = TextureUtils.fixResourcePath(this.source, TextureUtils.getBasePath(p_isValid_1_));
        if (this.startFadeIn >= 0 && this.endFadeIn >= 0 && this.endFadeOut >= 0) {
            int l2;
            int k2;
            int j2;
            int i1;
            int i2 = this.normalizeTime(this.endFadeIn - this.startFadeIn);
            if (this.startFadeOut < 0) {
                this.startFadeOut = this.normalizeTime(this.endFadeOut - i2);
            }
            if ((i1 = i2 + (j2 = this.normalizeTime(this.startFadeOut - this.endFadeIn)) + (k2 = this.normalizeTime(this.endFadeOut - this.startFadeOut)) + (l2 = this.normalizeTime(this.startFadeIn - this.endFadeOut))) != 24000) {
                Config.warn("Invalid fadeIn/fadeOut times, sum is more than 24h: " + i1);
                return false;
            }
            if (this.speed < 0.0f) {
                Config.warn("Invalid speed: " + this.speed);
                return false;
            }
            return true;
        }
        Config.warn("Invalid times, required are: startFadeIn, endFadeIn and endFadeOut.");
        return false;
    }

    private int normalizeTime(int p_normalizeTime_1_) {
        while (p_normalizeTime_1_ >= 24000) {
            p_normalizeTime_1_ -= 24000;
        }
        while (p_normalizeTime_1_ < 0) {
            p_normalizeTime_1_ += 24000;
        }
        return p_normalizeTime_1_;
    }

    public void render(int p_render_1_, float p_render_2_, float p_render_3_) {
        float f2 = p_render_3_ * this.getFadeBrightness(p_render_1_);
        if ((f2 = Config.limit(f2, 0.0f, 1.0f)) >= 1.0E-4f) {
            GlStateManager.bindTexture(this.textureId);
            Blender.setupBlend(this.blend, f2);
            GlStateManager.pushMatrix();
            if (this.rotate) {
                GlStateManager.rotate(p_render_2_ * 360.0f * this.speed, this.axis[0], this.axis[1], this.axis[2]);
            }
            Tessellator tessellator = Tessellator.getInstance();
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

    private float getFadeBrightness(int p_getFadeBrightness_1_) {
        if (this.timeBetween(p_getFadeBrightness_1_, this.startFadeIn, this.endFadeIn)) {
            int k2 = this.normalizeTime(this.endFadeIn - this.startFadeIn);
            int l2 = this.normalizeTime(p_getFadeBrightness_1_ - this.startFadeIn);
            return (float)l2 / (float)k2;
        }
        if (this.timeBetween(p_getFadeBrightness_1_, this.endFadeIn, this.startFadeOut)) {
            return 1.0f;
        }
        if (this.timeBetween(p_getFadeBrightness_1_, this.startFadeOut, this.endFadeOut)) {
            int i2 = this.normalizeTime(this.endFadeOut - this.startFadeOut);
            int j2 = this.normalizeTime(p_getFadeBrightness_1_ - this.startFadeOut);
            return 1.0f - (float)j2 / (float)i2;
        }
        return 0.0f;
    }

    private void renderSide(Tessellator p_renderSide_1_, int p_renderSide_2_) {
        WorldRenderer worldrenderer = p_renderSide_1_.getWorldRenderer();
        double d0 = (double)(p_renderSide_2_ % 3) / 3.0;
        double d1 = (double)(p_renderSide_2_ / 3) / 2.0;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-100.0, -100.0, -100.0).tex(d0, d1).endVertex();
        worldrenderer.pos(-100.0, -100.0, 100.0).tex(d0, d1 + 0.5).endVertex();
        worldrenderer.pos(100.0, -100.0, 100.0).tex(d0 + 0.3333333333333333, d1 + 0.5).endVertex();
        worldrenderer.pos(100.0, -100.0, -100.0).tex(d0 + 0.3333333333333333, d1).endVertex();
        p_renderSide_1_.draw();
    }

    public boolean isActive(int p_isActive_1_) {
        return !this.timeBetween(p_isActive_1_, this.endFadeOut, this.startFadeIn);
    }

    private boolean timeBetween(int p_timeBetween_1_, int p_timeBetween_2_, int p_timeBetween_3_) {
        return p_timeBetween_2_ <= p_timeBetween_3_ ? p_timeBetween_1_ >= p_timeBetween_2_ && p_timeBetween_1_ <= p_timeBetween_3_ : p_timeBetween_1_ >= p_timeBetween_2_ || p_timeBetween_1_ <= p_timeBetween_3_;
    }
}

