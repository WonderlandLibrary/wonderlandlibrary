package io.github.nevalackin.client.module.render.self;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.render.world.Render3DEvent;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.entity.EntityPlayerSP;

import static org.lwjgl.opengl.GL11.*;

public final class ChinaHat extends Module {

    private final DoubleProperty pointsProperty = new DoubleProperty("Points", 30, 3, 180, 1);
    private final DoubleProperty sizeProperty = new DoubleProperty("Size", 0.5, 0.1, 3.0, 0.1);

    private final double[][] positions = new double[(int) this.pointsProperty.getMax() + 1][2];
    private final int[] segmentColours = new int[(int) this.pointsProperty.getMax() + 1];

    public ChinaHat() {
        super("China Hat", Category.RENDER, Category.SubCategory.RENDER_SELF);

        this.register(this.pointsProperty, this.sizeProperty);

        this.pointsProperty.addChangeListener(now ->
            this.computeChineseHatPoints(now.intValue(), this.sizeProperty.getValue()));
        this.sizeProperty.addChangeListener(now ->
            this.computeChineseHatPoints(this.pointsProperty.getValue().intValue(), now));
    }

    private void computeChineseHatPoints(final int points, final double radius) {
        for (int i = 0; i <= points; i++) {
            final double circleX = radius * StrictMath.cos(i * Math.PI * 2 / points);
            final double circleZ = radius * StrictMath.sin(i * Math.PI * 2 / points);

            this.positions[i][0] = circleX;
            this.positions[i][1] = circleZ;
        }
    }

    private void addCircleVertices(final int points, final int alpha) {
        for (int i = 0; i <= points; i++) {
            final double[] pos = this.positions[i];
            DrawUtil.glColour(this.segmentColours[i] + (alpha << 24));
            glVertex3d(pos[0], 0, pos[1]);
        }
    }

    @EventLink
    private final Listener<Render3DEvent> onRender3D = event -> {
        if (this.mc.gameSettings.getThirdPersonView() == 0) return;
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE);
        glDepthMask(false);
        glDisable(GL_DEPTH_TEST);
        glShadeModel(GL_SMOOTH);
        final boolean restore = DrawUtil.glEnableBlend();

        final EntityPlayerSP player = this.mc.thePlayer;
        final float partialTicks = event.getPartialTicks();

        final double x = DrawUtil.interpolate(player.prevPosX, player.posX, partialTicks);
        final double y = DrawUtil.interpolate(player.prevPosY, player.posY, partialTicks);
        final double z = DrawUtil.interpolate(player.prevPosZ, player.posZ, partialTicks);

        final int points = this.pointsProperty.getValue().intValue();
        final double radius = this.sizeProperty.getValue();

        // Pre-calculate colours
        {
            final long totalOffset = 2000L;
            final long pointOffset = totalOffset / points;

            for (int i = 0; i < this.segmentColours.length; i++) {
                this.segmentColours[i] = ColourUtil.blendRainbowColours(i * pointOffset);
            }
        }

        glPushMatrix();

        // Position
        {
            glTranslated(x, y + 1.9, z);
            if (player.isSneaking())
                glTranslated(0, -0.2, 0);
        }

        // Yaw
        {
            glRotatef(DrawUtil.interpolate(player.getLastTickYaw(), player.getYaw(), partialTicks), 0, -1, 0);
        }

        // Pitch
        {
            final float pitch = DrawUtil.interpolate(player.getLastTickPitch(), player.getPitch(), partialTicks);
            glRotatef(pitch / 3.f, 1, 0, 0);
            glTranslated(0, 0, pitch / 270.f);
        }

        // Outline
        {
            glEnable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
            glLineWidth(2.0f);

            glBegin(GL_LINE_LOOP);
            {
                this.addCircleVertices(points - 1, 0xFF);
            }
            glEnd();

            glDisable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        }

        // Cone
        {
            glBegin(GL_TRIANGLE_FAN);
            {
                glVertex3d(0, radius / 2, 0);

                this.addCircleVertices(points, 0x80);
            }
            glEnd();
        }

        glPopMatrix();

        DrawUtil.glRestoreBlend(restore);
        glDepthMask(true);
        glShadeModel(GL_FLAT);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
