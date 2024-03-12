// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import org.lwjgl.opengl.GL11;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.utils.misc.SoundUtils;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.event.impl.player.AttackEvent;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.settings.ParentAttribute;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import java.awt.Color;
import dev.tenacity.module.Category;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.Module;

public class Hitmarkers extends Module
{
    private final BooleanSetting playSound;
    private final ModeSetting sound;
    private final NumberSetting volume;
    private final NumberSetting spacing;
    private final NumberSetting thickness;
    private final NumberSetting length;
    private final BooleanSetting outline;
    private final ModeSetting colorMode;
    private final ColorSetting color;
    private final Animation animation;
    private final ResourceLocation skeet;
    private Entity lastAttackedEntity;
    
    public Hitmarkers() {
        super("Hitmarkers", "Hitmarkers", Category.RENDER, "Entity attack indicators");
        this.playSound = new BooleanSetting("Play Sound", false);
        this.sound = new ModeSetting("Sound", "Skeet", new String[] { "Skeet" });
        this.volume = new NumberSetting("Volume", 0.75, 1.0, 0.0, 0.05);
        this.spacing = new NumberSetting("Spacing", 2.0, 10.0, 1.0, 0.5);
        this.thickness = new NumberSetting("Thickness", 2.0, 10.0, 1.0, 0.5);
        this.length = new NumberSetting("Length", 3.0, 10.0, 2.0, 0.5);
        this.outline = new BooleanSetting("Outline", true);
        this.colorMode = new ModeSetting("Color Mode", "Sync", new String[] { "Sync", "Custom" });
        this.color = new ColorSetting("Hit Color", new Color(255, 200, 0));
        this.animation = new DecelerateAnimation(200, 1.0);
        this.skeet = new ResourceLocation("Tenacity/Sounds/skeethit.wav");
        this.sound.addParent(this.playSound, ParentAttribute.BOOLEAN_CONDITION);
        this.volume.addParent(this.playSound, ParentAttribute.BOOLEAN_CONDITION);
        this.color.addParent(this.colorMode, modeSetting -> modeSetting.is("Custom"));
        this.addSettings(this.playSound, this.sound, this.volume, this.spacing, this.thickness, this.length, this.outline, this.colorMode, this.color);
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent e) {
        if (this.animation.finished(Direction.FORWARDS)) {
            this.animation.changeDirection();
        }
        Color color = this.color.getColor();
        if (this.colorMode.is("Sync")) {
            color = HUDMod.getClientColors().getFirst();
        }
        color = ColorUtil.applyOpacity(color, this.animation.getOutput().floatValue());
        if (this.outline.isEnabled()) {
            final float outlineThickness = 1.0f;
            this.drawRotatedCrosshair(this.spacing.getValue().floatValue() - 0.25f, this.thickness.getValue().floatValue() + outlineThickness, this.length.getValue().floatValue() + 0.5f, ColorUtil.applyOpacity(Color.BLACK, this.animation.getOutput().floatValue()).getRGB());
        }
        this.drawRotatedCrosshair(this.spacing.getValue().floatValue(), this.thickness.getValue().floatValue(), this.length.getValue().floatValue(), color.getRGB());
    }
    
    @Override
    public void onAttackEvent(final AttackEvent e) {
        this.animation.setDirection(Direction.FORWARDS);
        if (this.playSound.isEnabled()) {
            this.lastAttackedEntity = e.getTargetEntity();
        }
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        if (event.isPre() && this.playSound.isEnabled() && this.lastAttackedEntity != null && this.lastAttackedEntity.hurtResistantTime == 19) {
            if (Hitmarkers.mc.thePlayer.getDistanceToEntity(this.lastAttackedEntity) < 10.0f) {
                final String mode = this.sound.getMode();
                switch (mode) {
                    case "Skeet": {
                        SoundUtils.playSound(this.skeet, this.volume.getValue().floatValue());
                        break;
                    }
                }
            }
            this.lastAttackedEntity = null;
        }
    }
    
    private void drawRotatedCrosshair(final float spacing, final float thickness, final float length, final int color) {
        final ScaledResolution sr = new ScaledResolution(Hitmarkers.mc);
        final float x = sr.getScaledWidth() / 2.0f;
        final float y = sr.getScaledHeight() / 2.0f;
        final float topLeftX = x - spacing;
        final float topLeftY = y - spacing - 0.5f;
        this.drawLine(topLeftX, topLeftY, topLeftX - length, topLeftY - length, thickness, color);
        final float topRightX = x + spacing + 1.0f;
        final float topRightY = y - spacing - 0.5f;
        this.drawLine(topRightX, topRightY, topRightX + length, topRightY - length, thickness, color);
        final float bottomLeftX = x - spacing;
        final float bottomLeftY = y + spacing + 0.5f;
        this.drawLine(bottomLeftX, bottomLeftY, bottomLeftX - length, bottomLeftY + length, thickness, color);
        final float bottomRightX = x + spacing + 1.0f;
        final float bottomRightY = y + spacing + 0.5f;
        this.drawLine(bottomRightX, bottomRightY, bottomRightX + length, bottomRightY + length, thickness, color);
    }
    
    private void drawLine(final float x, final float y, final float x1, final float y1, final float thickness, final int color) {
        RenderUtil.setAlphaLimit(0.0f);
        RenderUtil.resetColor();
        GLUtil.setup2DRendering();
        GL11.glLineWidth(thickness);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        RenderUtil.color(color);
        GL11.glBegin(3);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();
        GL11.glDisable(2848);
        GLUtil.end2DRendering();
    }
}
