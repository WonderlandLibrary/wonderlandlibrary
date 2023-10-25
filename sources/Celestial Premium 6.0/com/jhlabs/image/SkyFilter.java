/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ImageUtils;
import com.jhlabs.image.PointFilter;
import com.jhlabs.math.FBM;
import com.jhlabs.math.Function2D;
import com.jhlabs.math.Noise;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Random;

public class SkyFilter
extends PointFilter {
    private float scale = 0.1f;
    private float stretch = 1.0f;
    private float angle = 0.0f;
    private float amount = 1.0f;
    private float H = 1.0f;
    private float octaves = 8.0f;
    private float lacunarity = 2.0f;
    private float gain = 1.0f;
    private float bias = 0.6f;
    private int operation;
    private float min;
    private float max;
    private boolean ridged;
    private FBM fBm;
    protected Random random = new Random();
    private Function2D basis;
    private float cloudCover = 0.5f;
    private float cloudSharpness = 0.5f;
    private float time = 0.3f;
    private float glow = 0.5f;
    private float glowFalloff = 0.5f;
    private float haziness = 0.96f;
    private float t = 0.0f;
    private float sunRadius = 10.0f;
    private int sunColor = -1;
    private float sunR;
    private float sunG;
    private float sunB;
    private float sunAzimuth = 0.5f;
    private float sunElevation = 0.5f;
    private float windSpeed = 0.0f;
    private float cameraAzimuth = 0.0f;
    private float cameraElevation = 0.0f;
    private float fov = 1.0f;
    private float[] exponents;
    private float[] tan;
    private BufferedImage skyColors;
    private int[] skyPixels;
    private static final float r255 = 0.003921569f;
    private float width;
    private float height;
    float mn;
    float mx;

    public SkyFilter() {
        if (this.skyColors == null) {
            this.skyColors = ImageUtils.createImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("SkyColors.png")).getSource());
        }
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getOperation() {
        return this.operation;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return this.scale;
    }

    public void setStretch(float stretch) {
        this.stretch = stretch;
    }

    public float getStretch() {
        return this.stretch;
    }

    public void setT(float t) {
        this.t = t;
    }

    public float getT() {
        return this.t;
    }

    public void setFOV(float fov) {
        this.fov = fov;
    }

    public float getFOV() {
        return this.fov;
    }

    public void setCloudCover(float cloudCover) {
        this.cloudCover = cloudCover;
    }

    public float getCloudCover() {
        return this.cloudCover;
    }

    public void setCloudSharpness(float cloudSharpness) {
        this.cloudSharpness = cloudSharpness;
    }

    public float getCloudSharpness() {
        return this.cloudSharpness;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public float getTime() {
        return this.time;
    }

    public void setGlow(float glow) {
        this.glow = glow;
    }

    public float getGlow() {
        return this.glow;
    }

    public void setGlowFalloff(float glowFalloff) {
        this.glowFalloff = glowFalloff;
    }

    public float getGlowFalloff() {
        return this.glowFalloff;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setOctaves(float octaves) {
        this.octaves = octaves;
    }

    public float getOctaves() {
        return this.octaves;
    }

    public void setH(float H) {
        this.H = H;
    }

    public float getH() {
        return this.H;
    }

    public void setLacunarity(float lacunarity) {
        this.lacunarity = lacunarity;
    }

    public float getLacunarity() {
        return this.lacunarity;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public float getGain() {
        return this.gain;
    }

    public void setBias(float bias) {
        this.bias = bias;
    }

    public float getBias() {
        return this.bias;
    }

    public void setHaziness(float haziness) {
        this.haziness = haziness;
    }

    public float getHaziness() {
        return this.haziness;
    }

    public void setSunElevation(float sunElevation) {
        this.sunElevation = sunElevation;
    }

    public float getSunElevation() {
        return this.sunElevation;
    }

    public void setSunAzimuth(float sunAzimuth) {
        this.sunAzimuth = sunAzimuth;
    }

    public float getSunAzimuth() {
        return this.sunAzimuth;
    }

    public void setSunColor(int sunColor) {
        this.sunColor = sunColor;
    }

    public int getSunColor() {
        return this.sunColor;
    }

    public void setCameraElevation(float cameraElevation) {
        this.cameraElevation = cameraElevation;
    }

    public float getCameraElevation() {
        return this.cameraElevation;
    }

    public void setCameraAzimuth(float cameraAzimuth) {
        this.cameraAzimuth = cameraAzimuth;
    }

    public float getCameraAzimuth() {
        return this.cameraAzimuth;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public float getWindSpeed() {
        return this.windSpeed;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        long start = System.currentTimeMillis();
        this.sunR = (float)(this.sunColor >> 16 & 0xFF) * 0.003921569f;
        this.sunG = (float)(this.sunColor >> 8 & 0xFF) * 0.003921569f;
        this.sunB = (float)(this.sunColor & 0xFF) * 0.003921569f;
        this.mn = 10000.0f;
        this.mx = -10000.0f;
        this.exponents = new float[(int)this.octaves + 1];
        float frequency = 1.0f;
        for (int i = 0; i <= (int)this.octaves; ++i) {
            this.exponents[i] = (float)Math.pow(2.0, -i);
            frequency *= this.lacunarity;
        }
        this.min = -1.0f;
        this.max = 1.0f;
        this.width = src.getWidth();
        this.height = src.getHeight();
        int h = src.getHeight();
        this.tan = new float[h];
        for (int i = 0; i < h; ++i) {
            this.tan[i] = (float)Math.tan((double)(this.fov * (float)i / (float)h) * Math.PI * 0.5);
        }
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        int t = (int)(63.0f * this.time);
        Graphics2D g = dst.createGraphics();
        g.drawImage(this.skyColors, 0, 0, dst.getWidth(), dst.getHeight(), t, 0, t + 1, 64, null);
        g.dispose();
        BufferedImage clouds = super.filter(dst, dst);
        long finish = System.currentTimeMillis();
        System.out.println(String.valueOf(this.mn) + " " + this.mx + " " + (float)(finish - start) * 0.001f);
        this.exponents = null;
        this.tan = null;
        return dst;
    }

    public float evaluate(float x, float y) {
        int i;
        float value = 0.0f;
        x += 371.0f;
        y += 529.0f;
        for (i = 0; i < (int)this.octaves; ++i) {
            value += Noise.noise3(x, y, this.t) * this.exponents[i];
            x *= this.lacunarity;
            y *= this.lacunarity;
        }
        float remainder = this.octaves - (float)((int)this.octaves);
        if (remainder != 0.0f) {
            value += remainder * Noise.noise3(x, y, this.t) * this.exponents[i];
        }
        return value;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        float f;
        float fx = (float)x / this.width;
        float fy = (float)y / this.height;
        float haze = (float)Math.pow(this.haziness, 100.0f * fy * fy);
        float r = (float)(rgb >> 16 & 0xFF) * 0.003921569f;
        float g = (float)(rgb >> 8 & 0xFF) * 0.003921569f;
        float b = (float)(rgb & 0xFF) * 0.003921569f;
        float cx = this.width * 0.5f;
        float nx = (float)x - cx;
        float ny = y;
        ny = this.tan[y];
        nx = (fx - 0.5f) * (1.0f + ny);
        ny += this.t * this.windSpeed;
        float fg = f = this.evaluate(nx /= this.scale, ny /= this.scale * this.stretch);
        f = (f + 1.23f) / 2.46f;
        int a = rgb & 0xFF000000;
        float c = f - this.cloudCover;
        if (c < 0.0f) {
            c = 0.0f;
        }
        float cloudAlpha = 1.0f - (float)Math.pow(this.cloudSharpness, c);
        this.mn = Math.min(this.mn, cloudAlpha);
        this.mx = Math.max(this.mx, cloudAlpha);
        float centreX = this.width * this.sunAzimuth;
        float centreY = this.height * this.sunElevation;
        float dx = (float)x - centreX;
        float dy = (float)y - centreY;
        float distance2 = dx * dx + dy * dy;
        distance2 = (float)Math.pow(distance2, this.glowFalloff);
        float sun = 10.0f * (float)Math.exp(-distance2 * this.glow * 0.1f);
        r += sun * this.sunR;
        g += sun * this.sunG;
        b += sun * this.sunB;
        float ca = (1.0f - cloudAlpha * cloudAlpha * cloudAlpha * cloudAlpha) * this.amount;
        float cloudR = this.sunR * ca;
        float cloudG = this.sunG * ca;
        float cloudB = this.sunB * ca;
        float iCloudAlpha = 1.0f - (cloudAlpha *= haze);
        r = iCloudAlpha * r + cloudAlpha * cloudR;
        g = iCloudAlpha * g + cloudAlpha * cloudG;
        b = iCloudAlpha * b + cloudAlpha * cloudB;
        float exposure = this.gain;
        r = 1.0f - (float)Math.exp(-r * exposure);
        g = 1.0f - (float)Math.exp(-g * exposure);
        b = 1.0f - (float)Math.exp(-b * exposure);
        int ir = (int)(255.0f * r) << 16;
        int ig = (int)(255.0f * g) << 8;
        int ib = (int)(255.0f * b);
        int v = 0xFF000000 | ir | ig | ib;
        return v;
    }

    public String toString() {
        return "Texture/Sky...";
    }
}

