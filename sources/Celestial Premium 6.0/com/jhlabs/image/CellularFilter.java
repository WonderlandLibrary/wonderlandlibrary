/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.Colormap;
import com.jhlabs.image.Gradient;
import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.WholeImageFilter;
import com.jhlabs.math.Function2D;
import com.jhlabs.math.Noise;
import java.awt.Rectangle;
import java.util.Random;

public class CellularFilter
extends WholeImageFilter
implements Function2D,
Cloneable {
    protected float scale = 32.0f;
    protected float stretch = 1.0f;
    protected float angle = 0.0f;
    public float amount = 1.0f;
    public float turbulence = 1.0f;
    public float gain = 0.5f;
    public float bias = 0.5f;
    public float distancePower = 2.0f;
    public boolean useColor = false;
    protected Colormap colormap = new Gradient();
    protected float[] coefficients = new float[]{1.0f, 0.0f, 0.0f, 0.0f};
    protected float angleCoefficient;
    protected Random random = new Random();
    protected float m00 = 1.0f;
    protected float m01 = 0.0f;
    protected float m10 = 0.0f;
    protected float m11 = 1.0f;
    protected Point[] results = new Point[3];
    protected float randomness = 0.0f;
    protected int gridType = 2;
    private float min;
    private float max;
    private static byte[] probabilities;
    private float gradientCoefficient;
    public static final int RANDOM = 0;
    public static final int SQUARE = 1;
    public static final int HEXAGONAL = 2;
    public static final int OCTAGONAL = 3;
    public static final int TRIANGULAR = 4;

    public CellularFilter() {
        for (int j = 0; j < this.results.length; ++j) {
            this.results[j] = new Point();
        }
        if (probabilities == null) {
            probabilities = new byte[8192];
            float factorial = 1.0f;
            float total = 0.0f;
            float mean = 2.5f;
            for (int i = 0; i < 10; ++i) {
                if (i > 1) {
                    factorial *= (float)i;
                }
                float probability = (float)Math.pow(mean, i) * (float)Math.exp(-mean) / factorial;
                int start = (int)(total * 8192.0f);
                int end = (int)((total += probability) * 8192.0f);
                for (int j = start; j < end; ++j) {
                    CellularFilter.probabilities[j] = (byte)i;
                }
            }
        }
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

    public void setAngle(float angle) {
        this.angle = angle;
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        this.m00 = cos;
        this.m01 = sin;
        this.m10 = -sin;
        this.m11 = cos;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setCoefficient(int i, float v) {
        this.coefficients[i] = v;
    }

    public float getCoefficient(int i) {
        return this.coefficients[i];
    }

    public void setAngleCoefficient(float angleCoefficient) {
        this.angleCoefficient = angleCoefficient;
    }

    public float getAngleCoefficient() {
        return this.angleCoefficient;
    }

    public void setGradientCoefficient(float gradientCoefficient) {
        this.gradientCoefficient = gradientCoefficient;
    }

    public float getGradientCoefficient() {
        return this.gradientCoefficient;
    }

    public void setF1(float v) {
        this.coefficients[0] = v;
    }

    public float getF1() {
        return this.coefficients[0];
    }

    public void setF2(float v) {
        this.coefficients[1] = v;
    }

    public float getF2() {
        return this.coefficients[1];
    }

    public void setF3(float v) {
        this.coefficients[2] = v;
    }

    public float getF3() {
        return this.coefficients[2];
    }

    public void setF4(float v) {
        this.coefficients[3] = v;
    }

    public float getF4() {
        return this.coefficients[3];
    }

    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    public Colormap getColormap() {
        return this.colormap;
    }

    public void setRandomness(float randomness) {
        this.randomness = randomness;
    }

    public float getRandomness() {
        return this.randomness;
    }

    public void setGridType(int gridType) {
        this.gridType = gridType;
    }

    public int getGridType() {
        return this.gridType;
    }

    public void setDistancePower(float distancePower) {
        this.distancePower = distancePower;
    }

    public float getDistancePower() {
        return this.distancePower;
    }

    public void setTurbulence(float turbulence) {
        this.turbulence = turbulence;
    }

    public float getTurbulence() {
        return this.turbulence;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    private float checkCube(float x, float y, int cubeX, int cubeY, Point[] results) {
        int numPoints;
        this.random.setSeed(571 * cubeX + 23 * cubeY);
        switch (this.gridType) {
            default: {
                numPoints = probabilities[this.random.nextInt() & 0x1FFF];
                break;
            }
            case 1: {
                numPoints = 1;
                break;
            }
            case 2: {
                numPoints = 1;
                break;
            }
            case 3: {
                numPoints = 2;
                break;
            }
            case 4: {
                numPoints = 2;
            }
        }
        for (int i = 0; i < numPoints; ++i) {
            Point p;
            float px = 0.0f;
            float py = 0.0f;
            float weight = 1.0f;
            switch (this.gridType) {
                case 0: {
                    px = this.random.nextFloat();
                    py = this.random.nextFloat();
                    break;
                }
                case 1: {
                    py = 0.5f;
                    px = 0.5f;
                    if (this.randomness == 0.0f) break;
                    px = (float)((double)px + (double)this.randomness * ((double)this.random.nextFloat() - 0.5));
                    py = (float)((double)py + (double)this.randomness * ((double)this.random.nextFloat() - 0.5));
                    break;
                }
                case 2: {
                    if ((cubeX & 1) == 0) {
                        px = 0.75f;
                        py = 0.0f;
                    } else {
                        px = 0.75f;
                        py = 0.5f;
                    }
                    if (this.randomness == 0.0f) break;
                    px += this.randomness * Noise.noise2(271.0f * ((float)cubeX + px), 271.0f * ((float)cubeY + py));
                    py += this.randomness * Noise.noise2(271.0f * ((float)cubeX + px) + 89.0f, 271.0f * ((float)cubeY + py) + 137.0f);
                    break;
                }
                case 3: {
                    switch (i) {
                        case 0: {
                            px = 0.207f;
                            py = 0.207f;
                            break;
                        }
                        case 1: {
                            px = 0.707f;
                            py = 0.707f;
                            weight = 1.6f;
                        }
                    }
                    if (this.randomness == 0.0f) break;
                    px += this.randomness * Noise.noise2(271.0f * ((float)cubeX + px), 271.0f * ((float)cubeY + py));
                    py += this.randomness * Noise.noise2(271.0f * ((float)cubeX + px) + 89.0f, 271.0f * ((float)cubeY + py) + 137.0f);
                    break;
                }
                case 4: {
                    if ((cubeY & 1) == 0) {
                        if (i == 0) {
                            px = 0.25f;
                            py = 0.35f;
                        } else {
                            px = 0.75f;
                            py = 0.65f;
                        }
                    } else if (i == 0) {
                        px = 0.75f;
                        py = 0.35f;
                    } else {
                        px = 0.25f;
                        py = 0.65f;
                    }
                    if (this.randomness == 0.0f) break;
                    px += this.randomness * Noise.noise2(271.0f * ((float)cubeX + px), 271.0f * ((float)cubeY + py));
                    py += this.randomness * Noise.noise2(271.0f * ((float)cubeX + px) + 89.0f, 271.0f * ((float)cubeY + py) + 137.0f);
                }
            }
            float dx = Math.abs(x - px);
            float dy = Math.abs(y - py);
            float d = this.distancePower == 1.0f ? dx + dy : (this.distancePower == 2.0f ? (float)Math.sqrt(dx * dx + dy * dy) : (float)Math.pow((float)Math.pow(dx *= weight, this.distancePower) + (float)Math.pow(dy *= weight, this.distancePower), 1.0f / this.distancePower));
            if (d < results[0].distance) {
                p = results[2];
                results[2] = results[1];
                results[1] = results[0];
                results[0] = p;
                p.distance = d;
                p.dx = dx;
                p.dy = dy;
                p.x = (float)cubeX + px;
                p.y = (float)cubeY + py;
                continue;
            }
            if (d < results[1].distance) {
                p = results[2];
                results[2] = results[1];
                results[1] = p;
                p.distance = d;
                p.dx = dx;
                p.dy = dy;
                p.x = (float)cubeX + px;
                p.y = (float)cubeY + py;
                continue;
            }
            if (!(d < results[2].distance)) continue;
            p = results[2];
            p.distance = d;
            p.dx = dx;
            p.dy = dy;
            p.x = (float)cubeX + px;
            p.y = (float)cubeY + py;
        }
        return results[2].distance;
    }

    @Override
    public float evaluate(float x, float y) {
        for (int j = 0; j < this.results.length; ++j) {
            this.results[j].distance = Float.POSITIVE_INFINITY;
        }
        int ix = (int)x;
        float fx = x - (float)ix;
        int iy = (int)y;
        float fy = y - (float)iy;
        float d = this.checkCube(fx, fy, ix, iy, this.results);
        if (d > fy) {
            d = this.checkCube(fx, fy + 1.0f, ix, iy - 1, this.results);
        }
        if (d > 1.0f - fy) {
            d = this.checkCube(fx, fy - 1.0f, ix, iy + 1, this.results);
        }
        if (d > fx) {
            this.checkCube(fx + 1.0f, fy, ix - 1, iy, this.results);
            if (d > fy) {
                d = this.checkCube(fx + 1.0f, fy + 1.0f, ix - 1, iy - 1, this.results);
            }
            if (d > 1.0f - fy) {
                d = this.checkCube(fx + 1.0f, fy - 1.0f, ix - 1, iy + 1, this.results);
            }
        }
        if (d > 1.0f - fx) {
            d = this.checkCube(fx - 1.0f, fy, ix + 1, iy, this.results);
            if (d > fy) {
                d = this.checkCube(fx - 1.0f, fy + 1.0f, ix + 1, iy - 1, this.results);
            }
            if (d > 1.0f - fy) {
                d = this.checkCube(fx - 1.0f, fy - 1.0f, ix + 1, iy + 1, this.results);
            }
        }
        float t = 0.0f;
        for (int i = 0; i < 3; ++i) {
            t += this.coefficients[i] * this.results[i].distance;
        }
        if (this.angleCoefficient != 0.0f) {
            float angle = (float)Math.atan2(y - this.results[0].y, x - this.results[0].x);
            if (angle < 0.0f) {
                angle += (float)Math.PI * 2;
            }
            t += this.angleCoefficient * (angle /= (float)Math.PI * 4);
        }
        if (this.gradientCoefficient != 0.0f) {
            float a = 1.0f / (this.results[0].dy + this.results[0].dx);
            t += this.gradientCoefficient * a;
        }
        return t;
    }

    public float turbulence2(float x, float y, float freq) {
        float t = 0.0f;
        for (float f = 1.0f; f <= freq; f *= 2.0f) {
            t += this.evaluate(f * x, f * y) / f;
        }
        return t;
    }

    public int getPixel(int x, int y, int[] inPixels, int width, int height) {
        float nx = this.m00 * (float)x + this.m01 * (float)y;
        float ny = this.m10 * (float)x + this.m11 * (float)y;
        nx /= this.scale;
        ny /= this.scale * this.stretch;
        float f = this.turbulence == 1.0f ? this.evaluate(nx, ny) : this.turbulence2(nx += 1000.0f, ny += 1000.0f, this.turbulence);
        f *= 2.0f;
        f *= this.amount;
        int a = -16777216;
        if (this.colormap != null) {
            int v = this.colormap.getColor(f);
            if (this.useColor) {
                int srcx = ImageMath.clamp((int)((this.results[0].x - 1000.0f) * this.scale), 0, width - 1);
                int srcy = ImageMath.clamp((int)((this.results[0].y - 1000.0f) * this.scale), 0, height - 1);
                v = inPixels[srcy * width + srcx];
                f = (this.results[1].distance - this.results[0].distance) / (this.results[1].distance + this.results[0].distance);
                f = ImageMath.smoothStep(this.coefficients[1], this.coefficients[0], f);
                v = ImageMath.mixColors(f, -16777216, v);
            }
            return v;
        }
        int v = PixelUtils.clamp((int)(f * 255.0f));
        int r = v << 16;
        int g = v << 8;
        int b = v;
        return a | r | g | b;
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int index = 0;
        int[] outPixels = new int[width * height];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                outPixels[index++] = this.getPixel(x, y, inPixels, width, height);
            }
        }
        return outPixels;
    }

    @Override
    public Object clone() {
        CellularFilter f = (CellularFilter)super.clone();
        f.coefficients = (float[])this.coefficients.clone();
        f.results = (Point[])this.results.clone();
        f.random = new Random();
        return f;
    }

    public String toString() {
        return "Texture/Cellular...";
    }

    public class Point {
        public int index;
        public float x;
        public float y;
        public float dx;
        public float dy;
        public float cubeX;
        public float cubeY;
        public float distance;
    }
}

