/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.GaussianFilter;
import com.jhlabs.image.ImageMath;
import com.jhlabs.image.WholeImageFilter;
import com.jhlabs.math.Function2D;
import com.jhlabs.math.ImageFunction2D;
import com.jhlabs.vecmath.Color4f;
import com.jhlabs.vecmath.Vector3f;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import java.util.Vector;

public class LightFilter
extends WholeImageFilter {
    public static final int COLORS_FROM_IMAGE = 0;
    public static final int COLORS_CONSTANT = 1;
    public static final int BUMPS_FROM_IMAGE = 0;
    public static final int BUMPS_FROM_IMAGE_ALPHA = 1;
    public static final int BUMPS_FROM_MAP = 2;
    public static final int BUMPS_FROM_BEVEL = 3;
    private float bumpHeight;
    private float bumpSoftness;
    private int bumpShape;
    private float viewDistance = 10000.0f;
    Material material;
    private Vector lights = new Vector();
    private int colorSource = 0;
    private int bumpSource = 0;
    private Function2D bumpFunction;
    private Image environmentMap;
    private int[] envPixels;
    private int envWidth = 1;
    private int envHeight = 1;
    private Vector3f l;
    private Vector3f v;
    private Vector3f n;
    private Color4f shadedColor;
    private Color4f diffuse_color;
    private Color4f specular_color;
    private Vector3f tmpv;
    private Vector3f tmpv2;
    protected static final float r255 = 0.003921569f;
    public static final int AMBIENT = 0;
    public static final int DISTANT = 1;
    public static final int POINT = 2;
    public static final int SPOT = 3;

    public LightFilter() {
        this.addLight(new DistantLight());
        this.bumpHeight = 1.0f;
        this.bumpSoftness = 5.0f;
        this.bumpShape = 0;
        this.material = new Material();
        this.l = new Vector3f();
        this.v = new Vector3f();
        this.n = new Vector3f();
        this.shadedColor = new Color4f();
        this.diffuse_color = new Color4f();
        this.specular_color = new Color4f();
        this.tmpv = new Vector3f();
        this.tmpv2 = new Vector3f();
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setBumpFunction(Function2D bumpFunction) {
        this.bumpFunction = bumpFunction;
    }

    public Function2D getBumpFunction() {
        return this.bumpFunction;
    }

    public void setBumpHeight(float bumpHeight) {
        this.bumpHeight = bumpHeight;
    }

    public float getBumpHeight() {
        return this.bumpHeight;
    }

    public void setBumpSoftness(float bumpSoftness) {
        this.bumpSoftness = bumpSoftness;
    }

    public float getBumpSoftness() {
        return this.bumpSoftness;
    }

    public void setBumpShape(int bumpShape) {
        this.bumpShape = bumpShape;
    }

    public int getBumpShape() {
        return this.bumpShape;
    }

    public void setViewDistance(float viewDistance) {
        this.viewDistance = viewDistance;
    }

    public float getViewDistance() {
        return this.viewDistance;
    }

    public void setEnvironmentMap(BufferedImage environmentMap) {
        this.environmentMap = environmentMap;
        if (environmentMap != null) {
            this.envWidth = environmentMap.getWidth();
            this.envHeight = environmentMap.getHeight();
            this.envPixels = this.getRGB(environmentMap, 0, 0, this.envWidth, this.envHeight, null);
        } else {
            this.envHeight = 1;
            this.envWidth = 1;
            this.envPixels = null;
        }
    }

    public Image getEnvironmentMap() {
        return this.environmentMap;
    }

    public void setColorSource(int colorSource) {
        this.colorSource = colorSource;
    }

    public int getColorSource() {
        return this.colorSource;
    }

    public void setBumpSource(int bumpSource) {
        this.bumpSource = bumpSource;
    }

    public int getBumpSource() {
        return this.bumpSource;
    }

    public void setDiffuseColor(int diffuseColor) {
        this.material.diffuseColor = diffuseColor;
    }

    public int getDiffuseColor() {
        return this.material.diffuseColor;
    }

    public void addLight(Light light) {
        this.lights.addElement(light);
    }

    public void removeLight(Light light) {
        this.lights.removeElement(light);
    }

    public Vector getLights() {
        return this.lights;
    }

    protected void setFromRGB(Color4f c, int argb) {
        c.set((float)(argb >> 16 & 0xFF) * 0.003921569f, (float)(argb >> 8 & 0xFF) * 0.003921569f, (float)(argb & 0xFF) * 0.003921569f, (float)(argb >> 24 & 0xFF) * 0.003921569f);
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int index = 0;
        int[] outPixels = new int[width * height];
        float width45 = Math.abs(6.0f * this.bumpHeight);
        boolean invertBumps = this.bumpHeight < 0.0f;
        Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
        Vector3f viewpoint = new Vector3f((float)width / 2.0f, (float)height / 2.0f, this.viewDistance);
        Vector3f normal = new Vector3f();
        Color4f envColor = new Color4f();
        Color4f diffuseColor = new Color4f(new Color(this.material.diffuseColor));
        Color4f specularColor = new Color4f(new Color(this.material.specularColor));
        Function2D bump = this.bumpFunction;
        if (this.bumpSource == 0 || this.bumpSource == 1 || this.bumpSource == 2 || bump == null) {
            if (this.bumpSoftness != 0.0f) {
                int bumpWidth = width;
                int bumpHeight = height;
                int[] bumpPixels = inPixels;
                if (this.bumpSource == 2 && this.bumpFunction instanceof ImageFunction2D) {
                    ImageFunction2D if2d = (ImageFunction2D)this.bumpFunction;
                    bumpWidth = if2d.getWidth();
                    bumpHeight = if2d.getHeight();
                    bumpPixels = if2d.getPixels();
                }
                int[] tmpPixels = new int[bumpWidth * bumpHeight];
                int[] softPixels = new int[bumpWidth * bumpHeight];
                Kernel kernel = GaussianFilter.makeKernel(this.bumpSoftness);
                GaussianFilter.convolveAndTranspose(kernel, bumpPixels, tmpPixels, bumpWidth, bumpHeight, true, false, false, GaussianFilter.WRAP_EDGES);
                GaussianFilter.convolveAndTranspose(kernel, tmpPixels, softPixels, bumpHeight, bumpWidth, true, false, false, GaussianFilter.WRAP_EDGES);
                Function2D bbump = bump = new ImageFunction2D(softPixels, bumpWidth, bumpHeight, 1, this.bumpSource == 1);
                if (this.bumpShape != 0) {
                    bump = new Function2D(bbump){
                        private Function2D original;
                        {
                            this.original = function2D;
                        }

                        @Override
                        public float evaluate(float x, float y) {
                            float v = this.original.evaluate(x, y);
                            switch (LightFilter.this.bumpShape) {
                                case 1: {
                                    v *= ImageMath.smoothStep(0.45f, 0.55f, v);
                                    break;
                                }
                                case 2: {
                                    v = v < 0.5f ? 0.5f : v;
                                    break;
                                }
                                case 3: {
                                    v = ImageMath.triangle(v);
                                    break;
                                }
                                case 4: {
                                    v = ImageMath.circleDown(v);
                                    break;
                                }
                                case 5: {
                                    v = ImageMath.gain(v, 0.75f);
                                }
                            }
                            return v;
                        }
                    };
                }
            } else if (this.bumpSource != 2) {
                bump = new ImageFunction2D(inPixels, width, height, 1, this.bumpSource == 1);
            }
        }
        float reflectivity = this.material.reflectivity;
        float areflectivity = 1.0f - reflectivity;
        Vector3f v1 = new Vector3f();
        Vector3f v2 = new Vector3f();
        Vector3f n = new Vector3f();
        Object[] lightsArray = new Light[this.lights.size()];
        this.lights.copyInto(lightsArray);
        for (int i = 0; i < lightsArray.length; ++i) {
            ((Light)lightsArray[i]).prepare(width, height);
        }
        float[][] heightWindow = new float[3][width];
        for (int x = 0; x < width; ++x) {
            heightWindow[1][x] = width45 * bump.evaluate(x, 0.0f);
        }
        for (int y = 0; y < height; ++y) {
            int x;
            boolean y0 = y > 0;
            boolean y1 = y < height - 1;
            position.y = y;
            for (x = 0; x < width; ++x) {
                heightWindow[2][x] = width45 * bump.evaluate(x, y + 1);
            }
            for (x = 0; x < width; ++x) {
                boolean x1;
                boolean x0 = x > 0;
                boolean bl = x1 = x < width - 1;
                if (this.bumpSource != 3) {
                    float m4;
                    int count = 0;
                    normal.z = 0.0f;
                    normal.y = 0.0f;
                    normal.x = 0.0f;
                    float m0 = heightWindow[1][x];
                    float m1 = x0 ? heightWindow[1][x - 1] - m0 : 0.0f;
                    float m2 = y0 ? heightWindow[0][x] - m0 : 0.0f;
                    float m3 = x1 ? heightWindow[1][x + 1] - m0 : 0.0f;
                    float f = m4 = y1 ? heightWindow[2][x] - m0 : 0.0f;
                    if (x0 && y1) {
                        v1.x = -1.0f;
                        v1.y = 0.0f;
                        v1.z = m1;
                        v2.x = 0.0f;
                        v2.y = 1.0f;
                        v2.z = m4;
                        n.cross(v1, v2);
                        n.normalize();
                        if ((double)n.z < 0.0) {
                            n.z = -n.z;
                        }
                        normal.add(n);
                        ++count;
                    }
                    if (x0 && y0) {
                        v1.x = -1.0f;
                        v1.y = 0.0f;
                        v1.z = m1;
                        v2.x = 0.0f;
                        v2.y = -1.0f;
                        v2.z = m2;
                        n.cross(v1, v2);
                        n.normalize();
                        if ((double)n.z < 0.0) {
                            n.z = -n.z;
                        }
                        normal.add(n);
                        ++count;
                    }
                    if (y0 && x1) {
                        v1.x = 0.0f;
                        v1.y = -1.0f;
                        v1.z = m2;
                        v2.x = 1.0f;
                        v2.y = 0.0f;
                        v2.z = m3;
                        n.cross(v1, v2);
                        n.normalize();
                        if ((double)n.z < 0.0) {
                            n.z = -n.z;
                        }
                        normal.add(n);
                        ++count;
                    }
                    if (x1 && y1) {
                        v1.x = 1.0f;
                        v1.y = 0.0f;
                        v1.z = m3;
                        v2.x = 0.0f;
                        v2.y = 1.0f;
                        v2.z = m4;
                        n.cross(v1, v2);
                        n.normalize();
                        if ((double)n.z < 0.0) {
                            n.z = -n.z;
                        }
                        normal.add(n);
                        ++count;
                    }
                    normal.x /= (float)count;
                    normal.y /= (float)count;
                    normal.z /= (float)count;
                }
                if (invertBumps) {
                    normal.x = -normal.x;
                    normal.y = -normal.y;
                }
                position.x = x;
                if (normal.z >= 0.0f) {
                    if (this.colorSource == 0) {
                        this.setFromRGB(diffuseColor, inPixels[index]);
                    } else {
                        this.setFromRGB(diffuseColor, this.material.diffuseColor);
                    }
                    if (reflectivity != 0.0f && this.environmentMap != null) {
                        this.tmpv2.set(viewpoint);
                        this.tmpv2.sub(position);
                        this.tmpv2.normalize();
                        this.tmpv.set(normal);
                        this.tmpv.normalize();
                        this.tmpv.scale(2.0f * this.tmpv.dot(this.tmpv2));
                        this.tmpv.sub(this.v);
                        this.tmpv.normalize();
                        this.setFromRGB(envColor, this.getEnvironmentMap(this.tmpv, inPixels, width, height));
                        diffuseColor.x = reflectivity * envColor.x + areflectivity * diffuseColor.x;
                        diffuseColor.y = reflectivity * envColor.y + areflectivity * diffuseColor.y;
                        diffuseColor.z = reflectivity * envColor.z + areflectivity * diffuseColor.z;
                    }
                    Color4f c = this.phongShade(position, viewpoint, normal, diffuseColor, specularColor, this.material, (Light[])lightsArray);
                    int alpha = inPixels[index] & 0xFF000000;
                    int rgb = (int)(c.x * 255.0f) << 16 | (int)(c.y * 255.0f) << 8 | (int)(c.z * 255.0f);
                    outPixels[index++] = alpha | rgb;
                    continue;
                }
                outPixels[index++] = 0;
            }
            float[] t = heightWindow[0];
            heightWindow[0] = heightWindow[1];
            heightWindow[1] = heightWindow[2];
            heightWindow[2] = t;
        }
        return outPixels;
    }

    protected Color4f phongShade(Vector3f position, Vector3f viewpoint, Vector3f normal, Color4f diffuseColor, Color4f specularColor, Material material, Light[] lightsArray) {
        this.shadedColor.set(diffuseColor);
        this.shadedColor.scale(material.ambientIntensity);
        for (int i = 0; i < lightsArray.length; ++i) {
            Light light = lightsArray[i];
            this.n.set(normal);
            this.l.set(light.position);
            if (light.type != 1) {
                this.l.sub(position);
            }
            this.l.normalize();
            float nDotL = this.n.dot(this.l);
            if (!((double)nDotL >= 0.0)) continue;
            float dDotL = 0.0f;
            this.v.set(viewpoint);
            this.v.sub(position);
            this.v.normalize();
            if (light.type == 3 && (dDotL = light.direction.dot(this.l)) < light.cosConeAngle) continue;
            this.n.scale(2.0f * nDotL);
            this.n.sub(this.l);
            float rDotV = this.n.dot(this.v);
            float rv = (double)rDotV < 0.0 ? 0.0f : rDotV / (material.highlight - material.highlight * rDotV + rDotV);
            if (light.type == 3) {
                float e = dDotL = light.cosConeAngle / dDotL;
                e *= e;
                e *= e;
                e *= e;
                e = (float)Math.pow(dDotL, light.focus * 10.0f) * (1.0f - e);
                rv *= e;
                nDotL *= e;
            }
            this.diffuse_color.set(diffuseColor);
            this.diffuse_color.scale(material.diffuseReflectivity);
            this.diffuse_color.x *= light.realColor.x * nDotL;
            this.diffuse_color.y *= light.realColor.y * nDotL;
            this.diffuse_color.z *= light.realColor.z * nDotL;
            this.specular_color.set(specularColor);
            this.specular_color.scale(material.specularReflectivity);
            this.specular_color.x *= light.realColor.x * rv;
            this.specular_color.y *= light.realColor.y * rv;
            this.specular_color.z *= light.realColor.z * rv;
            this.diffuse_color.add(this.specular_color);
            this.diffuse_color.clamp(0.0f, 1.0f);
            this.shadedColor.add(this.diffuse_color);
        }
        this.shadedColor.clamp(0.0f, 1.0f);
        return this.shadedColor;
    }

    private int getEnvironmentMap(Vector3f normal, int[] inPixels, int width, int height) {
        if (this.environmentMap != null) {
            float x;
            float angle = (float)Math.acos(-normal.y);
            float y = angle / (float)Math.PI;
            if (y == 0.0f || y == 1.0f) {
                x = 0.0f;
            } else {
                float f = normal.x / (float)Math.sin(angle);
                if (f > 1.0f) {
                    f = 1.0f;
                } else if (f < -1.0f) {
                    f = -1.0f;
                }
                x = (float)Math.acos(f) / (float)Math.PI;
            }
            x = ImageMath.clamp(x * (float)this.envWidth, 0.0f, (float)(this.envWidth - 1));
            y = ImageMath.clamp(y * (float)this.envHeight, 0.0f, (float)(this.envHeight - 1));
            int ix = (int)x;
            int iy = (int)y;
            float xWeight = x - (float)ix;
            float yWeight = y - (float)iy;
            int i = this.envWidth * iy + ix;
            int dx = ix == this.envWidth - 1 ? 0 : 1;
            int dy = iy == this.envHeight - 1 ? 0 : this.envWidth;
            return ImageMath.bilinearInterpolate(xWeight, yWeight, this.envPixels[i], this.envPixels[i + dx], this.envPixels[i + dy], this.envPixels[i + dx + dy]);
        }
        return 0;
    }

    public String toString() {
        return "Stylize/Light Effects...";
    }

    public class AmbientLight
    extends Light {
        @Override
        public String toString() {
            return "Ambient Light";
        }
    }

    public class DistantLight
    extends Light {
        public DistantLight() {
            this.type = 1;
        }

        @Override
        public String toString() {
            return "Distant Light";
        }
    }

    public static class Light
    implements Cloneable {
        int type = 0;
        Vector3f position;
        Vector3f direction;
        Color4f realColor = new Color4f();
        int color = -1;
        float intensity;
        float azimuth;
        float elevation;
        float focus = 0.5f;
        float centreX = 0.5f;
        float centreY = 0.5f;
        float coneAngle = 0.5235988f;
        float cosConeAngle;
        float distance = 100.0f;

        public Light() {
            this(4.712389f, 0.5235988f, 1.0f);
        }

        public Light(float azimuth, float elevation, float intensity) {
            this.azimuth = azimuth;
            this.elevation = elevation;
            this.intensity = intensity;
        }

        public void setAzimuth(float azimuth) {
            this.azimuth = azimuth;
        }

        public float getAzimuth() {
            return this.azimuth;
        }

        public void setElevation(float elevation) {
            this.elevation = elevation;
        }

        public float getElevation() {
            return this.elevation;
        }

        public void setDistance(float distance) {
            this.distance = distance;
        }

        public float getDistance() {
            return this.distance;
        }

        public void setIntensity(float intensity) {
            this.intensity = intensity;
        }

        public float getIntensity() {
            return this.intensity;
        }

        public void setConeAngle(float coneAngle) {
            this.coneAngle = coneAngle;
        }

        public float getConeAngle() {
            return this.coneAngle;
        }

        public void setFocus(float focus) {
            this.focus = focus;
        }

        public float getFocus() {
            return this.focus;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getColor() {
            return this.color;
        }

        public void setCentreX(float x) {
            this.centreX = x;
        }

        public float getCentreX() {
            return this.centreX;
        }

        public void setCentreY(float y) {
            this.centreY = y;
        }

        public float getCentreY() {
            return this.centreY;
        }

        public void prepare(int width, int height) {
            float lx = (float)(Math.cos(this.azimuth) * Math.cos(this.elevation));
            float ly = (float)(Math.sin(this.azimuth) * Math.cos(this.elevation));
            float lz = (float)Math.sin(this.elevation);
            this.direction = new Vector3f(lx, ly, lz);
            this.direction.normalize();
            if (this.type != 1) {
                lx *= this.distance;
                ly *= this.distance;
                lz *= this.distance;
                lx += (float)width * this.centreX;
                ly += (float)height * this.centreY;
            }
            this.position = new Vector3f(lx, ly, lz);
            this.realColor.set(new Color(this.color));
            this.realColor.scale(this.intensity);
            this.cosConeAngle = (float)Math.cos(this.coneAngle);
        }

        public Object clone() {
            try {
                Light copy = (Light)super.clone();
                return copy;
            }
            catch (CloneNotSupportedException e) {
                return null;
            }
        }

        public String toString() {
            return "Light";
        }
    }

    public static class Material {
        int diffuseColor = -7829368;
        int specularColor = -1;
        float ambientIntensity = 0.5f;
        float diffuseReflectivity = 1.0f;
        float specularReflectivity = 1.0f;
        float highlight = 3.0f;
        float reflectivity = 0.0f;
        float opacity = 1.0f;

        public void setDiffuseColor(int diffuseColor) {
            this.diffuseColor = diffuseColor;
        }

        public int getDiffuseColor() {
            return this.diffuseColor;
        }

        public void setOpacity(float opacity) {
            this.opacity = opacity;
        }

        public float getOpacity() {
            return this.opacity;
        }
    }

    public class PointLight
    extends Light {
        public PointLight() {
            this.type = 2;
        }

        @Override
        public String toString() {
            return "Point Light";
        }
    }

    public class SpotLight
    extends Light {
        public SpotLight() {
            this.type = 3;
        }

        @Override
        public String toString() {
            return "Spotlight";
        }
    }
}

