package de.tired.base.module.implementation.visual.esp.modes;

import de.tired.Tired;
import de.tired.base.event.events.Render2DEvent;
import de.tired.base.event.events.Render3DEvent;
import de.tired.base.event.events.Render3DEvent2;
import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.base.guis.newclickgui.setting.impl.ColorPickerSetting;
import de.tired.base.module.Module;
import de.tired.base.module.implementation.visual.esp.Esp;
import de.tired.base.module.implementation.visual.esp.EspExtension;
import de.tired.base.module.implementation.visual.esp.EspModeAnnotation;
import de.tired.util.render.ColorUtil;
import de.tired.util.render.ESPUtil;
import de.tired.util.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector4f;
import java.awt.*;

@EspModeAnnotation(name = "Real2D")
public class Real2D extends EspExtension {

    public Real2D() {
        super();
    }

    @Override
    public void onRender3D(Render3DEvent event2) {

    }

    @Override
    public void onRender3D2(Render3DEvent2 event2) {

    }

    @Override
    public void onRender2D(Render2DEvent event) {
        final Esp esp = Tired.INSTANCE.moduleManager.moduleBy(Esp.class);
        for (final Entity e : MC.theWorld.loadedEntityList) {
            if ((!esp.isInView.getValue() || RenderUtil.instance.isInViewFrustrum(e)) && esp.shouldRender(e)) {
                renderESP(e, esp);
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    public void renderESP(final Entity entity, Esp esp) {
        MC.entityRenderer.setupCameraTransform(MC.timer.renderPartialTicks, 0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Vector4f pos = ESPUtil.getEntityPositionsOn2D(entity);
        float x = pos.getX(), y = pos.getY(), right = pos.getZ();

        MC.entityRenderer.setupCameraTransform(MC.timer.renderPartialTicks, 0);


        MC.entityRenderer.setupOverlayRendering();
        final float bottom = pos.getW();
        float outlineThickness = .4f;
        GlStateManager.pushMatrix();
        //top


        float height = (bottom - y) + 1;

        Color firstColor = ColorUtil.interpolateColorsBackAndForth(15, 0, esp.real2DColor.ColorPickerC, esp.real2DColor.ColorPickerC, false);
        Color secondColor = ColorUtil.interpolateColorsBackAndForth(15, 90, esp.real2DColor.ColorPickerC, esp.real2DColor.ColorPickerC, false);

        if (esp.fadeColor.getValue()) {
            firstColor = ColorUtil.interpolateColorsBackAndForth(15, 0, esp.real2DColor.ColorPickerC, esp.real2DColor.ColorPickerC, false);
            secondColor = ColorUtil.interpolateColorsBackAndForth(15, 90, esp.real2DColor.ColorPickerC.darker(), esp.real2DColor.ColorPickerC.darker(), false);
        }



        GlStateManager.color(1, 1, 1, 1);
        //top
        RenderUtil.instance.drawGradientLR(x, y, (right - x), 1, 1, firstColor, secondColor);
        //left
        RenderUtil.instance.drawGradientTB(x, y, 1, bottom - y, 1, firstColor, secondColor);
        //bottom
        RenderUtil.instance.drawGradientLR(x, bottom, right - x, 1, 1, firstColor, secondColor);
        //right
        RenderUtil.instance.drawGradientTB(right, y, 1, (bottom - y) + 1, 1, secondColor, firstColor);

        RenderUtil.instance.drawRect2(x - .5f, y - outlineThickness, (right - x) + 2, outlineThickness, Color.BLACK.getRGB());

        //Left
        RenderUtil.instance.drawRect2(x - outlineThickness, y, outlineThickness, (bottom - y) + 1, Color.BLACK.getRGB());
        //bottom
        RenderUtil.instance.drawRect2(x - .5f, (bottom + 1), (right - x) + 2, outlineThickness, Color.BLACK.getRGB());
        //Right
        RenderUtil.instance.drawRect2(right + 1, y, outlineThickness, (bottom - y) + 1, Color.BLACK.getRGB());


        //top
        RenderUtil.instance.drawRect2(x + 1, y + 1, (right - x) - 1, outlineThickness, Color.BLACK.getRGB());
        //Left
        RenderUtil.instance.drawRect2(x + 1, y + 1, outlineThickness, (bottom - y) - 1, Color.BLACK.getRGB());
        //bottom
        RenderUtil.instance.drawRect2(x + 1, (bottom - outlineThickness), (right - x) - 1, outlineThickness, Color.BLACK.getRGB());
        //Right
        RenderUtil.instance.drawRect2(right - outlineThickness, y + 1, outlineThickness, (bottom - y) - 1, Color.BLACK.getRGB());

        final int dropShadowRadius = esp.shadowRadius.getValueInt();
        if (esp.dropShadow.getValue()) {
            if (!esp.colorShadow.getValue()) {
                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, x, y, (right - x), 1, dropShadowRadius);
                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, x, y, 1, bottom - y, dropShadowRadius);
                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, x, bottom, right - x, 1, dropShadowRadius);
                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, right, y, 1, (bottom - y) + 1, dropShadowRadius);

                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, x + 1, y + 1, (right - x) - 1, outlineThickness, dropShadowRadius);

                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, x + 1, y + 1, outlineThickness, (bottom - y) - 1, dropShadowRadius);

                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, x + 1, (bottom - outlineThickness), (right - x) - 1, outlineThickness, dropShadowRadius);

                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, right - outlineThickness, y + 1, outlineThickness, (bottom - y) - 1, dropShadowRadius);

            } else {
                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, x, y, (right - x), 1, dropShadowRadius);
                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, x, y, 1, bottom - y, dropShadowRadius);
                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, x, bottom, right - x, 1, dropShadowRadius);
                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, right, y, 1, (bottom - y) + 1, dropShadowRadius);

                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, x + 1, y + 1, (right - x) - 1, outlineThickness, dropShadowRadius);

                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, x + 1, y + 1, outlineThickness, (bottom - y) - 1, dropShadowRadius);

                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, x + 1, (bottom - outlineThickness), (right - x) - 1, outlineThickness, dropShadowRadius);

                RenderUtil.instance.doRenderShadow(esp.dropShadowColor.ColorPickerC, right - outlineThickness, y + 1, outlineThickness, (bottom - y) - 1, dropShadowRadius);


            }
        }
        if (esp.healthBar.getValue() && entity instanceof EntityPlayer) {
            final EntityPlayer entityPlayer = (EntityPlayer) entity;
            float healthValue = entityPlayer.getHealth() / entityPlayer.getMaxHealth();
            Color healthColor = healthValue > .75 ? new Color(66, 246, 123) : healthValue > .5 ? new Color(228, 255, 105) : healthValue > .35 ? new Color(236, 100, 64) : new Color(255, 65, 68);

            RenderUtil.instance.drawRect2(right + 2.5f, y - .5f, 2, height + 1, new Color(0, 0, 0, 180).getRGB());

            RenderUtil.instance.drawGradientTB(right + 3, y + (height - (height * healthValue)), 1, height * healthValue, 1, healthColor, healthColor.brighter());


        }
        GlStateManager.popMatrix();
    }

}
