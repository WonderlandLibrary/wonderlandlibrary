// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.mainmenu.particles;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.animations.Direction;
import java.util.Random;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.utils.animations.Animation;

public class Particle
{
    private Animation fadeInAnimation;
    private Animation rotateAnimation;
    private float x;
    private float y;
    private final float initialX;
    private final float initialY;
    private final float speed;
    private final float rotation;
    private float ticks;
    private final ParticleImage particleImage;
    static int seed;
    
    public Particle(final ScaledResolution sr, final ParticleImage particleImage) {
        this.rotateAnimation = new DecelerateAnimation(10000, 1.0);
        final Random random = new Random();
        this.particleImage = particleImage;
        final float randomX = sr.getScaledWidth() + random.nextFloat() * (sr.getScaledWidth() / 2.0f);
        final float randomY = random.nextFloat() * -sr.getScaledHeight();
        this.initialX = randomX + randomX * (Particle.seed * 0.1f);
        this.initialY = randomY + randomY * (Particle.seed * 0.1f);
        this.ticks = random.nextFloat() * (sr.getScaledHeight() / 4.0f);
        this.speed = (particleImage.getParticleType().equals(ParticleType.BIG) ? 1.5f : 3.0f);
        this.rotation = random.nextFloat() * 360.0f;
        ++Particle.seed;
        if (Particle.seed > 7) {
            Particle.seed = 0;
        }
    }
    
    public void draw() {
        if (this.fadeInAnimation == null) {
            this.fadeInAnimation = new DecelerateAnimation(1000, 1.0);
        }
        this.rotateAnimation.setDirection(this.fadeInAnimation.finished(Direction.FORWARDS) ? Direction.FORWARDS : Direction.BACKWARDS);
        final float imgWidth = this.particleImage.getDimensions().getFirst() / 2.0f;
        final float imgHeight = this.particleImage.getDimensions().getSecond() / 2.0f;
        final float particleX = this.x + imgWidth / 2.0f;
        final float particleY = this.y + imgHeight / 2.0f;
        RenderUtil.resetColor();
        RenderUtil.setAlphaLimit(0.0f);
        RenderUtil.color(-1, this.fadeInAnimation.getOutput().floatValue());
        GlStateManager.enableBlend();
        GL11.glPushMatrix();
        GL11.glTranslatef(particleX, particleY, 0.0f);
        GL11.glRotatef(this.rotation * this.rotateAnimation.getOutput().floatValue(), 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(-particleX, -particleY, 0.0f);
        RenderUtil.drawImage(this.particleImage.getLocation(), this.x, this.y, imgWidth, imgHeight);
        GL11.glPopMatrix();
    }
    
    public Animation getFadeInAnimation() {
        return this.fadeInAnimation;
    }
    
    public Animation getRotateAnimation() {
        return this.rotateAnimation;
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public float getInitialX() {
        return this.initialX;
    }
    
    public float getInitialY() {
        return this.initialY;
    }
    
    public float getSpeed() {
        return this.speed;
    }
    
    public float getRotation() {
        return this.rotation;
    }
    
    public float getTicks() {
        return this.ticks;
    }
    
    public ParticleImage getParticleImage() {
        return this.particleImage;
    }
    
    public void setFadeInAnimation(final Animation fadeInAnimation) {
        this.fadeInAnimation = fadeInAnimation;
    }
    
    public void setRotateAnimation(final Animation rotateAnimation) {
        this.rotateAnimation = rotateAnimation;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
    
    public void setTicks(final float ticks) {
        this.ticks = ticks;
    }
    
    static {
        Particle.seed = 0;
    }
}
