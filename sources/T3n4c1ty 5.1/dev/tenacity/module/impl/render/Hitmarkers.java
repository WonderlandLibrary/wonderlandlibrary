package dev.tenacity.module.impl.render;

import dev.tenacity.event.impl.player.AttackEvent;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;
import dev.tenacity.module.settings.ParentAttribute;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.misc.SoundUtils;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class Hitmarkers extends Module {

    private final BooleanSetting playSound = new BooleanSetting("Play Sound", false);
    private final ModeSetting sound = new ModeSetting("Sound", "Skeet", "Skeet");
    private final NumberSetting volume = new NumberSetting("Volume", 0.75, 1, 0, .05);
    private final NumberSetting spacing = new NumberSetting("Spacing", 2, 10, 1, .5);
    private final NumberSetting thickness = new NumberSetting("Thickness", 2, 10, 1, .5);
    private final NumberSetting length = new NumberSetting("Length", 3, 10, 2, .5);
    private final BooleanSetting outline = new BooleanSetting("Outline", true);
    private final ModeSetting colorMode = new ModeSetting("Color Mode", "Sync", "Sync", "Custom");
    private final ColorSetting color = new ColorSetting("Hit Color", new Color(255, 200, 0));


    private final Animation animation = new DecelerateAnimation(200, 1);

    private final ResourceLocation skeet = new ResourceLocation("Tenacity/Sounds/skeethit.wav");
    private Entity lastAttackedEntity;

    public Hitmarkers() {
        super("Hitmarkers", Category.RENDER, "Entity attack indicators");
        sound.addParent(playSound, ParentAttribute.BOOLEAN_CONDITION);
        volume.addParent(playSound, ParentAttribute.BOOLEAN_CONDITION);
        color.addParent(colorMode, modeSetting -> modeSetting.is("Custom"));
        addSettings(playSound, sound, volume, spacing, thickness, length, outline, colorMode, color);
    }

    @Override
    public void onRender2DEvent(Render2DEvent e) {
        if (animation.finished(Direction.FORWARDS)) {
            animation.changeDirection();
        }


        Color color = this.color.getColor();

        if(colorMode.is("Sync")){
            color = HUDMod.getClientColors().getFirst();
        }

        color = ColorUtil.applyOpacity(color, animation.getOutput().floatValue());

        if (outline.isEnabled()) {
            float outlineThickness = 1;
            drawRotatedCrosshair(spacing.getValue().floatValue() - .25f, thickness.getValue().floatValue() + outlineThickness,
                    length.getValue().floatValue() + .5f, ColorUtil.applyOpacity(Color.BLACK, animation.getOutput().floatValue()).getRGB());
        }

        drawRotatedCrosshair(spacing.getValue().floatValue(), thickness.getValue().floatValue(), length.getValue().floatValue(), color.getRGB());
    }

    @Override
    public void onAttackEvent(AttackEvent e) {
        animation.setDirection(Direction.FORWARDS);
        if (playSound.isEnabled()) {
            lastAttackedEntity = e.getTargetEntity();
        }
    }

    @Override
    public void onMotionEvent(MotionEvent event) {
        if (event.isPre() && playSound.isEnabled() && lastAttackedEntity != null && lastAttackedEntity.hurtResistantTime == 19) {
            if (mc.thePlayer.getDistanceToEntity(lastAttackedEntity) < 10) {
                switch (sound.getMode()) {
                    case "Skeet":
                        SoundUtils.playSound(skeet, volume.getValue().floatValue());
                        break;
                }
            }
            lastAttackedEntity = null;
        }
    }

    private void drawRotatedCrosshair(float spacing, float thickness, float length, int color) {
        ScaledResolution sr = new ScaledResolution(mc);
        float x = sr.getScaledWidth() / 2f, y = sr.getScaledHeight() / 2f;

        //top left
        float topLeftX = (x - spacing);
        float topLeftY = (y - spacing) - .5f;
        drawLine(topLeftX, topLeftY, topLeftX - length, topLeftY - length, thickness, color);

        //top right
        float topRightX = (x + spacing) + 1;
        float topRightY = (y - spacing) - .5f;
        drawLine(topRightX, topRightY, topRightX + length, topRightY - length, thickness, color);

        //bottom left
        float bottomLeftX = (x - spacing);
        float bottomLeftY = (y + spacing) + .5f;
        drawLine(bottomLeftX, bottomLeftY, bottomLeftX - length, bottomLeftY + length, thickness, color);

        //bottom right
        float bottomRightX = (x + spacing) + 1;
        float bottomRightY = (y + spacing) + .5f;
        drawLine(bottomRightX, bottomRightY, bottomRightX + length, bottomRightY + length, thickness, color);
    }


    private void drawLine(float x, float y, float x1, float y1, float thickness, int color) {
        RenderUtil.setAlphaLimit(0);
        RenderUtil.resetColor();
        GLUtil.setup2DRendering();
        glLineWidth(thickness);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        RenderUtil.color(color);
        glBegin(GL_LINE_STRIP);
        glVertex2f(x, y);
        glVertex2f(x1, y1);
        glEnd();
        glDisable(GL_LINE_SMOOTH);
        GLUtil.end2DRendering();
    }

}
