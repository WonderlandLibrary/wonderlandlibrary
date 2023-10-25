package ru.smertnix.celestial.ui.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import ru.smertnix.celestial.utils.render.RenderUtils;

import java.util.Random;

public class Particle {
    public float x;

    public float y;

    public final float size;

    private final float ySpeed = (new Random()).nextInt(5);

    private final float xSpeed = (new Random()).nextInt(5);

    private int height;

    private int width;

    Particle(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = genRandom();
    }

    private float lint1(float f) {
        return 1.02F * (1.0F - f) + 1.0F * f;
    }

    private float lint2(float f) {
        return 1.02F + f * -0.01999998F;
    }

    void connect(float x, float y) {
        RenderUtils.connectPoints(getX(), getY(), x, y);
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    void interpolation() {
        for (int n = 0; n <= 64; n++) {
            float f = n / 64.0F;
            float p1 = lint1(f);
            float p2 = lint2(f);
            if (p1 != p2) {
                this.y -= f;
                this.x -= f;
            }
        }
    }

    void fall() {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        this.y += this.ySpeed;
        this.x += this.xSpeed;
        if (this.y > mc.displayHeight)
            this.y = 1.0F;
        if (this.x > mc.displayWidth)
            this.x = 1.0F;
        if (this.x < 1.0F)
            this.x = scaledResolution.getScaledWidth();
        if (this.y < 1.0F)
            this.y = scaledResolution.getScaledHeight();
    }

    private float genRandom() {
        return (float)(0.30000001192092896D + Math.random() * 1.2999999523162842D);
    }
}
