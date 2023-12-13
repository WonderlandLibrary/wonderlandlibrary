package de.tired.base.module.implementation.visual;

import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.util.animation.Animation;
import de.tired.util.animation.Easings;
import de.tired.base.annotations.ModuleAnnotation;

import de.tired.util.render.RenderUtil;
import de.tired.util.hook.Rotations;
import de.tired.util.math.vector.Vec;
import de.tired.util.render.ColorUtil;
import de.tired.base.dragging.DragHandler;
import de.tired.base.dragging.Draggable;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.EventTick;
import de.tired.base.event.events.Render2DEvent;
import de.tired.util.render.shaderloader.ShaderManager;

import de.tired.util.render.shaderloader.list.RoundedRectOutlineShaderGradient;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

import java.awt.*;

@ModuleAnnotation(name = "MouseVisualizer", category = ModuleCategory.RENDER)
public class MouseVisualizer extends Module {

    private final Draggable draggable = DragHandler.setupDrag(this, "MouseVisualizer", 50, 50, false);

    private final Animation xAnimation = new Animation(), yAnimation = new Animation();

    public MouseVisualizer() {
    }

    @EventTarget
    public void onTick(EventTick eventTick) {

    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        int defaultWidth = 80, defaultHeight = 80;
        draggable.setObjectWidth(defaultWidth);
        draggable.setObjectHeight(defaultHeight - 4);

        Color firstColor = ColorUtil.interpolateColorsBackAndForth(3, 0, new Color(84, 51, 158).brighter(), new Color(104, 127, 203), true);
        Color secondColor = ColorUtil.interpolateColorsBackAndForth(3, 90, new Color(84, 51, 158), new Color(104, 127, 203).darker(), false);

        RenderUtil.instance.doRenderShadow(Color.BLACK, draggable.getXPosition(), draggable.getYPosition(), defaultWidth, defaultHeight, 22);

        ShaderManager.shaderBy(RoundedRectShader.class).drawRound(draggable.getXPosition(), draggable.getYPosition(), defaultWidth, defaultHeight, 4, new Color(20, 20, 20, 255));
        ShaderManager.shaderBy(RoundedRectOutlineShaderGradient.class).drawRound(draggable.getXPosition(), draggable.getYPosition(), defaultWidth, defaultHeight, 4, 2, firstColor, secondColor);

        float xDifference = (Rotations.instance.yaw - Rotations.instance.beforeYaw) * 6;
        float yDifference = (Rotations.instance.pitch - Rotations.instance.beforePitch) * 15;

        xAnimation.update();
        xAnimation.animate(xDifference, .2, Easings.BACK_IN4);

        yAnimation.update();
        yAnimation.animate(yDifference, .2, Easings.BACK_IN4);

        final double xCalc = MathHelper.clamp_double(xAnimation.getValue(), -35, 35);
        final double yCalc = MathHelper.clamp_double(yAnimation.getValue(), -35, 35);

        Vec firstPoint = new Vec(((draggable.getXPosition() + defaultWidth / 2f)), (draggable.getYPosition() + defaultHeight / 2f), 0);

        Vec lastPoint = new Vec((draggable.getXPosition() + defaultWidth / 2f) + xCalc, (draggable.getYPosition() + defaultHeight / 2f) + yCalc, 0);


       RenderUtil.instance.line(firstPoint, lastPoint, Integer.MAX_VALUE);

        GlStateManager.resetColor();

        RenderUtil.instance.drawCircle(draggable.getXPosition() + defaultWidth / 2f + xCalc, draggable.getYPosition() + defaultHeight / 2f + yCalc, 2, Color.white.getRGB());

    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
