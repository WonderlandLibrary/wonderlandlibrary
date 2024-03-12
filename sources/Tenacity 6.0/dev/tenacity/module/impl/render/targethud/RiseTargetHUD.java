// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.targethud;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.GradientUtil;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.utils.render.RoundedUtil;
import java.awt.Color;
import dev.tenacity.utils.render.ColorUtil;
import net.minecraft.entity.EntityLivingBase;
import java.util.ArrayList;
import dev.tenacity.utils.animations.ContinualAnimation;
import dev.tenacity.utils.time.TimerUtil;
import java.util.List;

public class RiseTargetHUD extends TargetHUD
{
    private boolean sentParticles;
    public final List<Particle> particles;
    private final TimerUtil timer;
    private final ContinualAnimation animatedHealthBar;
    
    public RiseTargetHUD() {
        super("Rise");
        this.particles = new ArrayList<Particle>();
        this.timer = new TimerUtil();
        this.animatedHealthBar = new ContinualAnimation();
    }
    
    @Override
    public void render(final float x, final float y, final float alpha, final EntityLivingBase target) {
        this.setWidth(Math.max(128.0f, RiseTargetHUD.riseFont18.getStringWidth("Name: " + target.getName()) + 42.5f));
        this.setHeight(42.5f);
        final int textColor = ColorUtil.applyOpacity(-1, alpha);
        RoundedUtil.drawRound(x, y, this.getWidth(), 45.0f, 3.5f, new Color(0, 0, 0, (int)(110.0f * alpha)));
        final int scaleOffset = (int)(target.hurtTime * 0.35f);
        final float healthPercent = (target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount());
        final float var = (this.getWidth() - 28.0f) * healthPercent;
        this.animatedHealthBar.animate(var, 18);
        GLUtil.startBlend();
        GradientUtil.drawGradientLR(x + 3.25f, y + 37.0f, this.animatedHealthBar.getOutput(), 5.0f, alpha, this.colorWheel.getColor1(), this.colorWheel.getColor2());
        for (final Particle p : this.particles) {
            p.x = x + 20.0f;
            p.y = y + 20.0f;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (p.opacity > 4.0f) {
                p.render2D();
            }
        }
        if (target instanceof AbstractClientPlayer) {
            final double offset = -(target.hurtTime * 23);
            RenderUtil.color(ColorUtil.applyOpacity(new Color(255, (int)(255.0 + offset), (int)(255.0 + offset)), alpha).getRGB());
            this.renderPlayer2D(x + 3.25f + scaleOffset / 2.0f, y + 3.75f + scaleOffset / 2.0f, (float)(30 - scaleOffset), (float)(30 - scaleOffset), (AbstractClientPlayer)target);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
        if (this.timer.hasTimeElapsed(16L, true)) {
            for (final Particle p : this.particles) {
                p.updatePosition();
                if (p.opacity < 1.0f) {
                    this.particles.remove(p);
                }
            }
        }
        final double healthNum = MathUtils.round(target.getHealth() + target.getAbsorptionAmount(), 1);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RiseTargetHUD.riseFont18.drawString(String.valueOf(healthNum), x + this.animatedHealthBar.getOutput() + 6.25f, y + 36.0f, textColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RiseTargetHUD.riseFont18.drawString("Name: " + target.getName(), x + 36.75f, y + 11.0f, textColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RiseTargetHUD.riseFont18.drawString("Distance: " + MathUtils.round(RiseTargetHUD.mc.thePlayer.getDistanceToEntity(target), 1), x + 36.75f, y + 22.5f, textColor);
        if (target.hurtTime == 9 && !this.sentParticles) {
            for (int i = 0; i <= 15; ++i) {
                final Particle particle = new Particle();
                particle.init(x + 20.0f, y + 20.0f, (float)((Math.random() - 0.5) * 2.0 * 1.4), (float)((Math.random() - 0.5) * 2.0 * 1.4), (float)(Math.random() * 4.0), (i % 2 == 0) ? this.colorWheel.getColor1() : this.colorWheel.getColor2());
                this.particles.add(particle);
            }
            this.sentParticles = true;
        }
        if (target.hurtTime == 8) {
            this.sentParticles = false;
        }
    }
    
    @Override
    public void renderEffects(final float x, final float y, final float alpha, final boolean glow) {
        if (glow) {
            RoundedUtil.drawGradientRound(x, y, this.getWidth(), this.getHeight(), 3.5f, ColorUtil.applyOpacity(this.colorWheel.getColor1(), alpha), ColorUtil.applyOpacity(this.colorWheel.getColor4(), alpha), ColorUtil.applyOpacity(this.colorWheel.getColor2(), alpha), ColorUtil.applyOpacity(this.colorWheel.getColor3(), alpha));
        }
        else {
            RoundedUtil.drawRound(x, y, this.getWidth(), 45.0f, 3.5f, ColorUtil.applyOpacity(Color.BLACK, alpha));
        }
    }
    
    public static class Particle
    {
        public float x;
        public float y;
        public float adjustedX;
        public float adjustedY;
        public float deltaX;
        public float deltaY;
        public float size;
        public float opacity;
        public Color color;
        
        public void render2D() {
            RoundedUtil.drawRound(this.x + this.adjustedX, this.y + this.adjustedY, this.size, this.size, this.size / 2.0f - 0.5f, ColorUtil.applyOpacity(this.color, this.opacity / 255.0f));
        }
        
        public void updatePosition() {
            for (int i = 1; i <= 2; ++i) {
                this.adjustedX += this.deltaX;
                this.adjustedY += this.deltaY;
                this.deltaY *= (float)0.97;
                this.deltaX *= (float)0.97;
                --this.opacity;
                if (this.opacity < 1.0f) {
                    this.opacity = 1.0f;
                }
            }
        }
        
        public void init(final float x, final float y, final float deltaX, final float deltaY, final float size, final Color color) {
            this.x = x;
            this.y = y;
            this.deltaX = deltaX;
            this.deltaY = deltaY;
            this.size = size;
            this.opacity = 254.0f;
            this.color = color;
        }
    }
}
