package io.github.nevalackin.client.module.render.overlay;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.client.event.render.world.HurtShakeEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.gui.ScaledResolution;

import static org.lwjgl.opengl.GL11.*;

public final class Camera extends Module {

    private final BooleanProperty noHurtShakeProperty = new BooleanProperty("No Hurt Shake", true);
    private final BooleanProperty hurtFlashProperty = new BooleanProperty("Hurt Flash", true);

    public Camera() {
        super("Camera", Category.RENDER, Category.SubCategory.RENDER_OVERLAY);

        this.register(this.noHurtShakeProperty, this.hurtFlashProperty);
    }

    @EventLink
    private final Listener<HurtShakeEvent> onHurtShake = event -> {
        if (this.noHurtShakeProperty.getValue())
            event.setCancelled();
    };

    @EventLink(0)
    private final Listener<RenderGameOverlayEvent> onRenderGameOverlay = event -> {
        if (this.hurtFlashProperty.getValue()) {
            final float hurtTimePercentage = (this.mc.thePlayer.hurtTime - event.getPartialTicks()) / this.mc.thePlayer.maxHurtTime;

            if (hurtTimePercentage > 0.0) {
                glDisable(GL_TEXTURE_2D);
                final boolean restore = DrawUtil.glEnableBlend();
                glShadeModel(GL_SMOOTH);
                glDisable(GL_ALPHA_TEST);

                final ScaledResolution scaledResolution = event.getScaledResolution();

                final float lineWidth = 20.f;

                glLineWidth(lineWidth);

                final int width = scaledResolution.getScaledWidth();
                final int height = scaledResolution.getScaledHeight();

                final int fadeOutColour = ColourUtil.fadeTo(0x00FF0000, 0xFFFF0000, hurtTimePercentage);

                glBegin(GL_QUADS);
                {
                    // Left
                    DrawUtil.glColour(fadeOutColour);
                    glVertex2f(0, 0);
                    glVertex2f(0, height);
                    DrawUtil.glColour(0x00FF0000);
                    glVertex2f(lineWidth, height - lineWidth);
                    glVertex2f(lineWidth, lineWidth);

                    // Right
                    DrawUtil.glColour(0x00FF0000);
                    glVertex2f(width - lineWidth, lineWidth);
                    glVertex2f(width - lineWidth, height - lineWidth);
                    DrawUtil.glColour(fadeOutColour);
                    glVertex2f(width, height);
                    glVertex2f(width, 0);

                    // Top
                    DrawUtil.glColour(fadeOutColour);
                    glVertex2f(0, 0);
                    DrawUtil.glColour(0x00FF0000);
                    glVertex2d(lineWidth, lineWidth);
                    glVertex2f(width - lineWidth, lineWidth);
                    DrawUtil.glColour(fadeOutColour);
                    glVertex2f(width, 0);

                    // Bottom
                    DrawUtil.glColour(0x00FF0000);
                    glVertex2f(lineWidth, height - lineWidth);
                    DrawUtil.glColour(fadeOutColour);
                    glVertex2d(0, height);
                    glVertex2f(width, height);
                    DrawUtil.glColour(0x00FF0000);
                    glVertex2f(width - lineWidth, height - lineWidth);
                }
                glEnd();

                glEnable(GL_ALPHA_TEST);
                glShadeModel(GL_FLAT);
                DrawUtil.glRestoreBlend(restore);
                glEnable(GL_TEXTURE_2D);
            }
        }
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
