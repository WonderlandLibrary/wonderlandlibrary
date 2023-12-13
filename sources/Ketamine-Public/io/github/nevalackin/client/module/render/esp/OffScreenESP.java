package io.github.nevalackin.client.module.render.esp;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.render.game.FrustumUpdateEvent;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.ColourProperty;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.client.util.player.RotationUtil;
import io.github.nevalackin.client.util.player.TeamsUtil;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glTranslated;

public final class OffScreenESP extends Module {

    private final EnumProperty<ColourMode> colourProperty = new EnumProperty<>("Colour Mode", ColourMode.TEAM);
    private final ColourProperty arrowColourProperty = new ColourProperty("Arrow Colour", 0xFF13A3FF,
                                                                          () -> this.colourProperty.getValue() == ColourMode.NORMAL);
    private final BooleanProperty pulsingColourProperty = new BooleanProperty("Pulsing", true);
    private final BooleanProperty outlineProperty = new BooleanProperty("Outline", false);
    private final DoubleProperty radiusProperty = new DoubleProperty("Radius", 30.0, 1.0, 200.0, 0.5);
    private final DoubleProperty arrowSizeProperty = new DoubleProperty("Size", 10.0, 1.0, 30.0, 0.5);
    private final BooleanProperty infoProperty = new BooleanProperty("Draw Info", false);

    private Frustum frustum;

    public OffScreenESP() {
        super("Off Screen ESP", Category.RENDER, Category.SubCategory.RENDER_ESP);

        this.register(this.colourProperty, this.arrowColourProperty, this.pulsingColourProperty, this.outlineProperty, this.radiusProperty, this.arrowSizeProperty, this.infoProperty);
    }

    @EventLink
    private final Listener<RenderGameOverlayEvent> onRenderGameOverlay = event -> {
        if (frustum == null) return;

        final ScaledResolution scaledResolution = event.getScaledResolution();
        final CustomFontRenderer fontRenderer = KetamineClient.getInstance().getFontRenderer();

        final float hWidth = scaledResolution.getScaledWidth() / 2.0f;
        final float hHeight = scaledResolution.getScaledHeight() / 2.0f;

        final float partialTicks = event.getPartialTicks();

        glTranslatef(hWidth, hHeight, 0);

        if (this.outlineProperty.getValue()) {
            glEnable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);

            glLineWidth(1);
        }

        final double arrowSize = this.arrowSizeProperty.getValue();
        final double radius = this.radiusProperty.getValue();

        this.mc.theWorld.playerEntities.stream()
            .filter(this::validatePlayer)
            .forEach(player -> {
                if (!frustum.isBoundingBoxInFrustum(player.getEntityBoundingBox())) {
                    final Entity local = this.mc.thePlayer;

                    final float currentRotation = DrawUtil.interpolate(local.prevRotationYaw, local.rotationYaw, partialTicks);

                    final double currentPosX = DrawUtil.interpolate(local.prevPosX, local.posX, partialTicks);
                    final double currentPosZ = DrawUtil.interpolate(local.prevPosZ, local.posZ, partialTicks);

                    final double playerPosX = DrawUtil.interpolate(player.prevPosX, player.posX, partialTicks);
                    final double playerPosZ = DrawUtil.interpolate(player.prevPosZ, player.posZ, partialTicks);

                    final float yawToPlayer = RotationUtil.calculateYawFromSrcToDst(currentRotation, currentPosX, currentPosZ,
                                                                                    playerPosX, playerPosZ) - currentRotation;

                    glPushMatrix();

                    final double rads = Math.toRadians(yawToPlayer);

                    final double aspectRatio = scaledResolution.getScaledWidth_double() / scaledResolution.getScaledHeight_double();

                    glTranslated(radius * Math.sin(rads) * aspectRatio,
                                 radius * -Math.cos(rads), 0);

                    final float health = player.getHealth() / player.getMaxHealth();

                    final int healthColour = ColourUtil.blendHealthColours(health);

                    final double barSize = arrowSize + 4;

                    // Draw name
                    if (this.infoProperty.getValue()) {
                        final String name = player.getGameProfile().getName();
                        fontRenderer.draw(name, -fontRenderer.getWidth(name) / 2, -arrowSize / 2.0 - fontRenderer.getHeight(name) - 4, 0xFFFFFFFF);
                    }

                    glDisable(GL_TEXTURE_2D);
                    boolean restore = DrawUtil.glEnableBlend();

                    // Draw health bar
                    if (this.infoProperty.getValue()) {
                        glBegin(GL_QUADS);
                        {
                            // Background
                            DrawUtil.glColour(0x96000000);
                            addQuadVertices(-barSize / 2.0, arrowSize / 2.0 + 2, barSize, 2);

                            // Coloured bar
                            DrawUtil.glColour(healthColour);
                            final double filled = (barSize - 1) * health;
                            addQuadVertices(-barSize / 2.0 + 0.5, arrowSize / 2.0 + 2 + 0.5, filled, 1);
                        }
                        glEnd();
                    }

                    int colour;

                    switch (this.colourProperty.getValue()) {
                        case TEAM:
                            colour = TeamsUtil.TeamsMode.NAME.getColourSupplier().getTeamColour(player);
                            break;
                        case HEALTH:
                            colour = healthColour;
                            break;
                        default:
                            colour = this.arrowColourProperty.getValue();
                    }

                    if (this.pulsingColourProperty.getValue()) {
                        colour = ColourUtil.fadeBetween(colour, colour & 0x50FFFFFF);
                    }

                    glRotatef(yawToPlayer, 0, 0, 1);

                    if (this.outlineProperty.getValue()) {
                        // Draw Outline
                        DrawUtil.glColour(colour | 0xFF000000);

                        glBegin(GL_LINE_LOOP);
                        {
                            addTriangleVertices(arrowSize);
                        }
                        glEnd();
                    }

                    // Draw Arrow

                    glEnable(GL_POLYGON_SMOOTH);
                    glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);

                    DrawUtil.glColour(colour);

                    glBegin(GL_TRIANGLES);
                    {
                        addTriangleVertices(arrowSize);
                    }
                    glEnd();

                    glDisable(GL_POLYGON_SMOOTH);
                    glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);

                    DrawUtil.glRestoreBlend(restore);
                    glEnable(GL_TEXTURE_2D);

                    glPopMatrix();
                }
            });

        if (this.outlineProperty.getValue()) {
            glDisable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        }

        glTranslatef(-hWidth, -hHeight, 0);
    };

    private static void addQuadVertices(final double x, final double y, final double width, final double height) {
        glVertex2d(x, y);
        glVertex2d(x, y + height);
        glVertex2d(x + width, y + height);
        glVertex2d(x + width, y);
    }

    private static void addTriangleVertices(final double size) {
        glVertex2d(0, -size / 2);
        glVertex2d(-size / 2, size / 2);
        glVertex2d(size / 2, size / 2);
    }

    private boolean validatePlayer(final EntityPlayer player) {
        return player instanceof EntityOtherPlayerMP && player.isEntityAlive() && !player.isInvisible();
    }

    @EventLink
    private final Listener<FrustumUpdateEvent> onUpdateFrustum = event -> {
        this.frustum = event.getFrustum();
    };

    @Override
    public void onEnable() {
        this.frustum = null;
    }

    @Override
    public void onDisable() {

    }

    private enum ColourMode {
        HEALTH("Health"),
        TEAM("Team"),
        NORMAL("Normal");

        private final String name;

        ColourMode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
