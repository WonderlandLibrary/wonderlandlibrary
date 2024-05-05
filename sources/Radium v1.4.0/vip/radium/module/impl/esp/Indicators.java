package vip.radium.module.impl.esp;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import vip.radium.event.EventBusPriorities;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.event.impl.render.overlay.Render2DEvent;
import vip.radium.event.impl.world.WorldLoadEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.utils.RotationUtils;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.Colors;
import vip.radium.utils.render.LockedResolution;
import vip.radium.utils.render.OGLUtils;
import vip.radium.utils.render.RenderingUtils;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

@ModuleInfo(label = "Indicators", category = ModuleCategory.ESP)
public final class Indicators extends Module {

    private final Property<Boolean> pulsingProperty = new Property<>("Pulsing", true);
    private final Property<Integer> arrowsColorProperty = new Property<>(
            "Color",
            Colors.RED);
    private final Property<Integer> secondColorProperty = new Property<>(
            "Color-2",
            0xFF000000, pulsingProperty::getValue);
    private final DoubleProperty arrowsRadiusProperty = new DoubleProperty(
            "Radius",
            100,
            10,
            200,
            1);
    private final Property<Boolean> outlineProperty = new Property<>(
            "Outline",
            false
    );
    private final Property<Boolean> fadeOutProperty = new Property<>(
            "Fade Out",
            true
    );
    private final Property<Boolean> scaleUpProperty = new Property<>(
            "Scale Up",
            true
    );
    private final DoubleProperty arrowsSizeProperty = new DoubleProperty(
            "Size",
            6,
            3,
            30,
            1);
    private final DoubleProperty stretchProperty = new DoubleProperty(
            "Stretch",
            1.5,
            1.0,
            2.0,
            0.05);
    private final EnumProperty<ArrowsShape> arrowShapeProperty = new EnumProperty<>(
            "Shape",
            ArrowsShape.EQUILATERAL);

    private final Map<EntityPlayer, Float> playerAlphaMap = new HashMap<>();

    @EventLink
    public final Listener<WorldLoadEvent> onWorldLoad = event -> {
        playerAlphaMap.clear();
    };

    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePos = event -> {
        if (fadeOutProperty.getValue()) {
            final EntityPlayer localPlayer = Wrapper.getPlayer();

            for (EntityPlayer player : Wrapper.getLoadedPlayers()) {
                if (player instanceof EntityOtherPlayerMP && player.isEntityAlive() && !player.isInvisible() && !RenderingUtils.isBBInFrustum(player.getEntityBoundingBox())) {
                    playerAlphaMap.put(player, 1.0F - player.getDistanceToEntity(localPlayer) / 40.0F);
                }
            }
        }
    };

    @EventLink(EventBusPriorities.LOWEST)
    public final Listener<Render2DEvent> onRender2DEvent = e -> {
        final LockedResolution lr = e.getResolution();
        final float middleX = lr.getWidth() / 2.0F;
        final float middleY = lr.getHeight() / 2.0F;
        final float pt = e.getPartialTicks();

        glPushMatrix();
        glTranslated(middleX + 0.5D, middleY, 1.0D);

        OGLUtils.enableBlending();
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_POLYGON_SMOOTH);
        glLineWidth(1.0F);

        final double distortion = stretchProperty.getValue();
        final int color = pulsingProperty.getValue() ? RenderingUtils.fadeBetween(arrowsColorProperty.getValue(), secondColorProperty.getValue()) : arrowsColorProperty.getValue();
        final boolean outline = outlineProperty.getValue();
        final ArrowsShape shape = arrowShapeProperty.getValue();

        for (EntityPlayer player : Wrapper.getLoadedPlayers()) {
            if (player instanceof EntityOtherPlayerMP && player.isEntityAlive() && !player.isInvisible() && !RenderingUtils.isBBInFrustum(player.getEntityBoundingBox())) {
                float yaw = RenderingUtils.interpolate(
                        RotationUtils.getOldYaw(player),
                        RotationUtils.getYawToEntity(player),
                        pt) -
                        RenderingUtils.interpolate(
                                Wrapper.getPlayer().prevRotationYaw,
                                Wrapper.getPlayer().rotationYaw,
                                pt);
                glPushMatrix();
                glScaled(distortion, 1.0, 1.0);
                glRotatef(yaw, 0, 0, 1);
                glTranslated(0.0D,
                        -arrowsRadiusProperty.getValue() - arrowsSizeProperty.getValue(),
                        0.0D);
                final double correction = 1 / distortion;
                glScaled(correction, 1.0, 1.0);

                double offset = 0;

                double arrowSize = arrowsSizeProperty.getValue();
                final float fadeOut = playerAlphaMap.getOrDefault(player, 1.0F);

                if (scaleUpProperty.getValue())
                    arrowSize += arrowSize * fadeOut / 3;

                if (outline) {
                    glBegin(GL_LINE_LOOP);
                    glColor4ub(
                            (byte) (color >> 16 & 255),
                            (byte) (color >> 8 & 255),
                            (byte) (color & 255),
                            (byte) 0xFF);
                    glVertex2d(0, 0);

                    switch (shape) {
                        case ARROW:
                            offset = (int) (arrowSize / 3.0D);
                            glVertex2d(-arrowSize + offset, arrowSize);
                            glVertex2d(0, arrowSize - offset);
                            glVertex2d(arrowSize - offset, arrowSize);
                            break;
                        case ISOSCELES:
                            offset = (int) (arrowSize / 3.0D);
                            glVertex2d(-arrowSize + offset, arrowSize);
                            glVertex2d(arrowSize - offset, arrowSize);
                            break;
                        case EQUILATERAL:
                            glVertex2d(-arrowSize, arrowSize);
                            glVertex2d(arrowSize, arrowSize);
                            break;
                    }

                    glEnd();
                }

                final int colorAlpha = color >> 24 & 255;

                glBegin(shape == ArrowsShape.ARROW ? GL_POLYGON : GL_TRIANGLE_STRIP);

                if (fadeOutProperty.getValue()) {
                    glColor4ub(
                            (byte) (color >> 16 & 255),
                            (byte) (color >> 8 & 255),
                            (byte) (color & 255),
                            (byte) (Math.max(colorAlpha, fadeOut * 255)));
                } else if (colorAlpha != 0xFF) {
                    glColor4ub(
                            (byte) (color >> 16 & 255),
                            (byte) (color >> 8 & 255),
                            (byte) (color & 255),
                            (byte) colorAlpha);
                }
                glVertex2d(0, 0);
                switch (shape) {
                    case ARROW:
                        if (!outline)
                            offset = (int) (arrowSize / 3.0D);
                        glVertex2d(-arrowSize + offset, arrowSize);
                        glVertex2d(0, arrowSize - offset);
                        glVertex2d(arrowSize - offset, arrowSize);
                        break;
                    case ISOSCELES:
                        if (!outline)
                            offset = (int) (arrowSize / 3.0D);
                        glVertex2d(-arrowSize + offset, arrowSize);
                        glVertex2d(arrowSize - offset, arrowSize);
                        break;
                    case EQUILATERAL:
                        glVertex2d(-arrowSize, arrowSize);
                        glVertex2d(arrowSize, arrowSize);
                        break;
                }

                glEnd();
                glPopMatrix();
            }
        }

        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_POLYGON_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        glPopMatrix();
    };

    private enum ArrowsShape {
        EQUILATERAL,
        ARROW,
        ISOSCELES
    }

}
