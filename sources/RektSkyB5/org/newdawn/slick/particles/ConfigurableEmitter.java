/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.particles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.util.FastTrig;
import org.newdawn.slick.util.Log;

public class ConfigurableEmitter
implements ParticleEmitter {
    private static String relativePath = "";
    public Range spawnInterval = new Range(100.0f, 100.0f);
    public Range spawnCount = new Range(5.0f, 5.0f);
    public Range initialLife = new Range(1000.0f, 1000.0f);
    public Range initialSize = new Range(10.0f, 10.0f);
    public Range xOffset = new Range(0.0f, 0.0f);
    public Range yOffset = new Range(0.0f, 0.0f);
    public RandomValue spread = new RandomValue(360.0f);
    public SimpleValue angularOffset = new SimpleValue(0.0f);
    public Range initialDistance = new Range(0.0f, 0.0f);
    public Range speed = new Range(50.0f, 50.0f);
    public SimpleValue growthFactor = new SimpleValue(0.0f);
    public SimpleValue gravityFactor = new SimpleValue(0.0f);
    public SimpleValue windFactor = new SimpleValue(0.0f);
    public Range length = new Range(1000.0f, 1000.0f);
    public ArrayList colors = new ArrayList();
    public SimpleValue startAlpha = new SimpleValue(255.0f);
    public SimpleValue endAlpha = new SimpleValue(0.0f);
    public LinearInterpolator alpha;
    public LinearInterpolator size;
    public LinearInterpolator velocity;
    public LinearInterpolator scaleY;
    public Range emitCount = new Range(1000.0f, 1000.0f);
    public int usePoints = 1;
    public boolean useOriented = false;
    public boolean useAdditive = false;
    public String name;
    public String imageName = "";
    private Image image;
    private boolean updateImage;
    private boolean enabled = true;
    private float x;
    private float y;
    private int nextSpawn = 0;
    private int timeout;
    private int particleCount;
    private ParticleSystem engine;
    private int leftToEmit;
    protected boolean wrapUp = false;
    protected boolean completed = false;
    protected boolean adjust;
    protected float adjustx;
    protected float adjusty;

    public static void setRelativePath(String path) {
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        relativePath = path;
    }

    public ConfigurableEmitter(String name) {
        this.name = name;
        this.leftToEmit = (int)this.emitCount.random();
        this.timeout = (int)this.length.random();
        this.colors.add(new ColorRecord(0.0f, Color.white));
        this.colors.add(new ColorRecord(1.0f, Color.red));
        ArrayList<Vector2f> curve = new ArrayList<Vector2f>();
        curve.add(new Vector2f(0.0f, 0.0f));
        curve.add(new Vector2f(1.0f, 255.0f));
        this.alpha = new LinearInterpolator(curve, 0, 255);
        curve = new ArrayList();
        curve.add(new Vector2f(0.0f, 0.0f));
        curve.add(new Vector2f(1.0f, 255.0f));
        this.size = new LinearInterpolator(curve, 0, 255);
        curve = new ArrayList();
        curve.add(new Vector2f(0.0f, 0.0f));
        curve.add(new Vector2f(1.0f, 1.0f));
        this.velocity = new LinearInterpolator(curve, 0, 1);
        curve = new ArrayList();
        curve.add(new Vector2f(0.0f, 0.0f));
        curve.add(new Vector2f(1.0f, 1.0f));
        this.scaleY = new LinearInterpolator(curve, 0, 1);
    }

    public void setImageName(String imageName) {
        if (imageName.length() == 0) {
            imageName = null;
        }
        this.imageName = imageName;
        if (imageName == null) {
            this.image = null;
        } else {
            this.updateImage = true;
        }
    }

    public String getImageName() {
        return this.imageName;
    }

    public String toString() {
        return "[" + this.name + "]";
    }

    public void setPosition(float x2, float y2) {
        this.setPosition(x2, y2, true);
    }

    public void setPosition(float x2, float y2, boolean moveParticles) {
        if (moveParticles) {
            this.adjust = true;
            this.adjustx -= this.x - x2;
            this.adjusty -= this.y - y2;
        }
        this.x = x2;
        this.y = y2;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void update(ParticleSystem system, int delta) {
        this.engine = system;
        if (!this.adjust) {
            this.adjustx = 0.0f;
            this.adjusty = 0.0f;
        } else {
            this.adjust = false;
        }
        if (this.updateImage) {
            this.updateImage = false;
            try {
                this.image = new Image(relativePath + this.imageName);
            }
            catch (SlickException e2) {
                this.image = null;
                Log.error(e2);
            }
        }
        if ((this.wrapUp || this.length.isEnabled() && this.timeout < 0 || this.emitCount.isEnabled() && this.leftToEmit <= 0) && this.particleCount == 0) {
            this.completed = true;
        }
        this.particleCount = 0;
        if (this.wrapUp) {
            return;
        }
        if (this.length.isEnabled()) {
            if (this.timeout < 0) {
                return;
            }
            this.timeout -= delta;
        }
        if (this.emitCount.isEnabled() && this.leftToEmit <= 0) {
            return;
        }
        this.nextSpawn -= delta;
        if (this.nextSpawn < 0) {
            this.nextSpawn = (int)this.spawnInterval.random();
            int count = (int)this.spawnCount.random();
            for (int i2 = 0; i2 < count; ++i2) {
                Particle p2 = system.getNewParticle(this, this.initialLife.random());
                p2.setSize(this.initialSize.random());
                p2.setPosition(this.x + this.xOffset.random(), this.y + this.yOffset.random());
                p2.setVelocity(0.0f, 0.0f, 0.0f);
                float dist = this.initialDistance.random();
                float power = this.speed.random();
                if (dist != 0.0f || power != 0.0f) {
                    float s2 = this.spread.getValue(0.0f);
                    float ang = s2 + this.angularOffset.getValue(0.0f) - this.spread.getValue() / 2.0f - 90.0f;
                    float xa = (float)FastTrig.cos(Math.toRadians(ang)) * dist;
                    float ya = (float)FastTrig.sin(Math.toRadians(ang)) * dist;
                    p2.adjustPosition(xa, ya);
                    float xv = (float)FastTrig.cos(Math.toRadians(ang));
                    float yv = (float)FastTrig.sin(Math.toRadians(ang));
                    p2.setVelocity(xv, yv, power * 0.001f);
                }
                if (this.image != null) {
                    p2.setImage(this.image);
                }
                ColorRecord start = (ColorRecord)this.colors.get(0);
                p2.setColor(start.col.r, start.col.g, start.col.b, this.startAlpha.getValue(0.0f) / 255.0f);
                p2.setUsePoint(this.usePoints);
                p2.setOriented(this.useOriented);
                if (!this.emitCount.isEnabled()) continue;
                --this.leftToEmit;
                if (this.leftToEmit <= 0) break;
            }
        }
    }

    public void updateParticle(Particle particle, int delta) {
        ++this.particleCount;
        particle.x += this.adjustx;
        particle.y += this.adjusty;
        particle.adjustVelocity(this.windFactor.getValue(0.0f) * 5.0E-5f * (float)delta, this.gravityFactor.getValue(0.0f) * 5.0E-5f * (float)delta);
        float offset = particle.getLife() / particle.getOriginalLife();
        float inv = 1.0f - offset;
        float colOffset = 0.0f;
        float colInv = 1.0f;
        Color startColor = null;
        Color endColor = null;
        for (int i2 = 0; i2 < this.colors.size() - 1; ++i2) {
            ColorRecord rec1 = (ColorRecord)this.colors.get(i2);
            ColorRecord rec2 = (ColorRecord)this.colors.get(i2 + 1);
            if (!(inv >= rec1.pos) || !(inv <= rec2.pos)) continue;
            startColor = rec1.col;
            endColor = rec2.col;
            float step = rec2.pos - rec1.pos;
            colOffset = inv - rec1.pos;
            colOffset /= step;
            colOffset = 1.0f - colOffset;
            colInv = 1.0f - colOffset;
        }
        if (startColor != null) {
            float r2 = startColor.r * colOffset + endColor.r * colInv;
            float g2 = startColor.g * colOffset + endColor.g * colInv;
            float b2 = startColor.b * colOffset + endColor.b * colInv;
            float a2 = this.alpha.isActive() ? this.alpha.getValue(inv) / 255.0f : this.startAlpha.getValue(0.0f) / 255.0f * offset + this.endAlpha.getValue(0.0f) / 255.0f * inv;
            particle.setColor(r2, g2, b2, a2);
        }
        if (this.size.isActive()) {
            float s2 = this.size.getValue(inv);
            particle.setSize(s2);
        } else {
            particle.adjustSize((float)delta * this.growthFactor.getValue(0.0f) * 0.001f);
        }
        if (this.velocity.isActive()) {
            particle.setSpeed(this.velocity.getValue(inv));
        }
        if (this.scaleY.isActive()) {
            particle.setScaleY(this.scaleY.getValue(inv));
        }
    }

    public boolean completed() {
        if (this.engine == null) {
            return false;
        }
        if (this.length.isEnabled()) {
            if (this.timeout > 0) {
                return false;
            }
            return this.completed;
        }
        if (this.emitCount.isEnabled()) {
            if (this.leftToEmit > 0) {
                return false;
            }
            return this.completed;
        }
        if (this.wrapUp) {
            return this.completed;
        }
        return false;
    }

    public void replay() {
        this.reset();
        this.nextSpawn = 0;
        this.leftToEmit = (int)this.emitCount.random();
        this.timeout = (int)this.length.random();
    }

    public void reset() {
        this.completed = false;
        if (this.engine != null) {
            this.engine.releaseAll(this);
        }
    }

    public void replayCheck() {
        if (this.completed() && this.engine != null && this.engine.getParticleCount() == 0) {
            this.replay();
        }
    }

    public ConfigurableEmitter duplicate() {
        ConfigurableEmitter theCopy = null;
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ParticleIO.saveEmitter(bout, this);
            ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
            theCopy = ParticleIO.loadEmitter(bin);
        }
        catch (IOException e2) {
            Log.error("Slick: ConfigurableEmitter.duplicate(): caught exception " + e2.toString());
            return null;
        }
        return theCopy;
    }

    public void addColorPoint(float pos, Color col) {
        this.colors.add(new ColorRecord(pos, col));
    }

    public boolean useAdditive() {
        return this.useAdditive;
    }

    public boolean isOriented() {
        return this.useOriented;
    }

    public boolean usePoints(ParticleSystem system) {
        return this.usePoints == 1 && system.usePoints() || this.usePoints == 2;
    }

    public Image getImage() {
        return this.image;
    }

    public void wrapUp() {
        this.wrapUp = true;
    }

    public void resetState() {
        this.wrapUp = false;
        this.replay();
    }

    public class Range {
        private float max;
        private float min;
        private boolean enabled = false;

        private Range(float min, float max) {
            this.min = min;
            this.max = max;
        }

        public float random() {
            return (float)((double)this.min + Math.random() * (double)(this.max - this.min));
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public float getMax() {
            return this.max;
        }

        public void setMax(float max) {
            this.max = max;
        }

        public float getMin() {
            return this.min;
        }

        public void setMin(float min) {
            this.min = min;
        }
    }

    public class ColorRecord {
        public float pos;
        public Color col;

        public ColorRecord(float pos, Color col) {
            this.pos = pos;
            this.col = col;
        }
    }

    public class LinearInterpolator
    implements Value {
        private ArrayList curve;
        private boolean active;
        private int min;
        private int max;

        public LinearInterpolator(ArrayList curve, int min, int max) {
            this.curve = curve;
            this.min = min;
            this.max = max;
            this.active = false;
        }

        public void setCurve(ArrayList curve) {
            this.curve = curve;
        }

        public ArrayList getCurve() {
            return this.curve;
        }

        public float getValue(float t2) {
            Vector2f p0 = (Vector2f)this.curve.get(0);
            for (int i2 = 1; i2 < this.curve.size(); ++i2) {
                Vector2f p1 = (Vector2f)this.curve.get(i2);
                if (t2 >= p0.getX() && t2 <= p1.getX()) {
                    float st = (t2 - p0.getX()) / (p1.getX() - p0.getX());
                    float r2 = p0.getY() + st * (p1.getY() - p0.getY());
                    return r2;
                }
                p0 = p1;
            }
            return 0.0f;
        }

        public boolean isActive() {
            return this.active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public int getMax() {
            return this.max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getMin() {
            return this.min;
        }

        public void setMin(int min) {
            this.min = min;
        }
    }

    public class RandomValue
    implements Value {
        private float value;

        private RandomValue(float value) {
            this.value = value;
        }

        public float getValue(float time) {
            return (float)(Math.random() * (double)this.value);
        }

        public void setValue(float value) {
            this.value = value;
        }

        public float getValue() {
            return this.value;
        }
    }

    public class SimpleValue
    implements Value {
        private float value;
        private float next;

        private SimpleValue(float value) {
            this.value = value;
        }

        public float getValue(float time) {
            return this.value;
        }

        public void setValue(float value) {
            this.value = value;
        }
    }

    public static interface Value {
        public float getValue(float var1);
    }
}

