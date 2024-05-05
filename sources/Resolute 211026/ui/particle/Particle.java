// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.particle;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import vip.Resolute.util.render.RenderUtils;
import java.util.Random;

public class Particle
{
    public float x;
    public float y;
    public final float size;
    private final float ySpeed;
    private final float xSpeed;
    private int height;
    private int width;
    
    Particle(final int x, final int y) {
        this.ySpeed = (float)new Random().nextInt(5);
        this.xSpeed = (float)new Random().nextInt(5);
        this.x = (float)x;
        this.y = (float)y;
        this.size = this.genRandom();
    }
    
    private float lint1(final float f) {
        return 1.02f * (1.0f - f) + f;
    }
    
    private float lint2(final float f) {
        return 1.02f + f * -0.01999998f;
    }
    
    void connect(final float x, final float y) {
        RenderUtils.connectPoints(this.getX(), this.getY(), x, y);
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public float getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = (float)x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = (float)y;
    }
    
    void interpolation() {
        for (int n = 0; n <= 64; ++n) {
            final float f = n / 64.0f;
            final float p1 = this.lint1(f);
            final float p2 = this.lint2(f);
            if (p1 != p2) {
                this.y -= f;
                this.x -= f;
            }
        }
    }
    
    void fall() {
        final Minecraft mc = Minecraft.getMinecraft();
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        this.y += this.ySpeed;
        this.x += this.xSpeed;
        if (this.y > mc.displayHeight) {
            this.y = 1.0f;
        }
        if (this.x > mc.displayWidth) {
            this.x = 1.0f;
        }
        if (this.x < 1.0f) {
            this.x = (float)scaledResolution.getScaledWidth();
        }
        if (this.y < 1.0f) {
            this.y = (float)scaledResolution.getScaledHeight();
        }
    }
    
    private float genRandom() {
        return 0.3f + (float)Math.random() * 1.3f;
    }
}
