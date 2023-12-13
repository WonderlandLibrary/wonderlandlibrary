package io.github.nevalackin.client.module.render.esp.esp;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.render.game.FrustumUpdateEvent;
import io.github.nevalackin.client.event.render.game.UpdateFramebufferSizeEvent;
import io.github.nevalackin.client.event.render.model.RenderNameEvent;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.client.event.render.world.Render3DEvent;
import io.github.nevalackin.client.module.render.esp.esp.components.Bar;
import io.github.nevalackin.client.module.render.esp.esp.components.Box;
import io.github.nevalackin.client.module.render.esp.esp.components.Tag;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.ColourProperty;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.client.util.player.RotationUtil;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public final class ESP extends Module {

    private final BooleanProperty tagsProperty = new BooleanProperty("Tags", true);
    private final ColourProperty tagsColourProperty = new ColourProperty("Tags Colour", 0xFFFFFFFF, this.tagsProperty::getValue);
    private final BooleanProperty boxProperty = new BooleanProperty("Box", true);
    private final BooleanProperty chestProperty = new BooleanProperty("Chests", false, this.boxProperty::getValue);
    private final ColourProperty boxColourProperty = new ColourProperty("Box Colour", 0xFF67181E, this.boxProperty::getValue);
    private final EnumProperty<HealthBarColour> healthBarProperty = new EnumProperty<>("Health Bar", HealthBarColour.HEALTH);
    private final ColourProperty healthBarColourProperty = new ColourProperty("Health Bar Colour", ColourUtil.getClientColour(),
                                                                              () -> this.healthBarProperty.check() &&
                                                                                  this.healthBarProperty.getValue() != HealthBarColour.HEALTH);
    private final ColourProperty secondHealthBarColourProperty = new ColourProperty("Second Health Bar Colour", ColourUtil.getSecondaryColour(),
                                                                                    () -> this.healthBarProperty.check() &&
                                                                                        this.healthBarProperty.getValue() == HealthBarColour.GRADIENT);
    private final BooleanProperty healthProperty = new BooleanProperty("Health", true,
                                                                       () -> this.healthBarProperty.getValue() != HealthBarColour.OFF);
    private final BooleanProperty armourBarProperty = new BooleanProperty("Armour Bar", true);
    private final ColourProperty armourBarColourProperty = new ColourProperty("Armour Bar Colour", 0xFF4080FF, this.armourBarProperty::getValue);
    private final BooleanProperty skeletonsProperty = new BooleanProperty("Skeletons", true);

    private final List<Drawable> drawables = new ArrayList<>();

    private final double[] projectionBuffer = new double[2];
    private final double[] screenBuffer = new double[3];

    private Frustum frustum;

    public ESP() {
        super("ESP", Category.RENDER, Category.SubCategory.RENDER_ESP);

        this.tagsProperty.attachProperty(this.tagsColourProperty);
        this.boxProperty.attachProperty(this.boxColourProperty);
        this.healthBarProperty.attachProperty(this.healthBarColourProperty);
        this.healthBarProperty.attachProperty(this.secondHealthBarColourProperty);
        this.armourBarProperty.attachProperty(this.armourBarColourProperty);

        this.register(this.tagsProperty, this.tagsColourProperty,
                      this.boxProperty, this.boxColourProperty, this.chestProperty,
                      this.healthBarProperty, this.healthBarColourProperty, this.secondHealthBarColourProperty, this.healthProperty,
                      this.armourBarProperty, this.armourBarColourProperty,
                      this.skeletonsProperty);
    }

    @EventLink
    private final Listener<RenderNameEvent> onRenderName = event -> {
        if (this.tagsProperty.getValue() &&
            event.getEntity() instanceof EntityPlayer &&
            this.validatePlayer((EntityPlayer) event.getEntity())) {
            event.setCancelled();
        }
    };

    @EventLink(0)
    private final Listener<RenderGameOverlayEvent> onRenderGameOverlay = event -> {
        final ScaledResolution scaledResolution = event.getScaledResolution();
        final int scaling = scaledResolution.getScaleFactor();
        final double scale = 2.0 / scaling;

        glScaled(scale, scale, 1);

        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glLineWidth(1.f);

        final boolean restore = DrawUtil.glEnableBlend();

        for (int size = this.drawables.size(); size > 0; size--) {
            final Drawable drawable = this.drawables.remove(0);
            // Draw ESP drawable obj
            drawable.draw(KetamineClient.getInstance().getFontRenderer());
        }

        DrawUtil.glRestoreBlend(restore);

        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);

        glScaled(1 / scale, 1 / scale, 1);
    };

    @EventLink
    private final Listener<FrustumUpdateEvent> onFrustumCull = event -> {
        this.frustum = event.getFrustum();
    };

    @EventLink
    private final Listener<UpdateFramebufferSizeEvent> onUpdateFramebuffer = event ->
        this.updateScreenBuffer(new ScaledResolution(this.mc));

    @EventLink
    private final Listener<Render3DEvent> onRender3D = event -> {
        if (this.frustum == null) return;

        final ScaledResolution scaledResolution = event.getScaledResolution();
        final double maxBoxWidth = scaledResolution.getScaledWidth() / 3.0;

        // Player 2d ESP
        for (final EntityPlayer player : this.mc.theWorld.playerEntities) {
            if (this.validatePlayer(player)) {
                final AxisAlignedBB boundingBox = DrawUtil.interpolate(player,
                                                                       RotationUtil.getHittableBoundingBox(player),
                                                                       event.getPartialTicks());

                if (!this.frustum.isBoundingBoxInFrustum(boundingBox)) continue;

                final double[] position = DrawUtil.worldToScreen(DrawUtil.interpolate(player, event.getPartialTicks()),
                                                                 boundingBox, this.screenBuffer, this.projectionBuffer);

                if (position == null || position[2] - position[0] > maxBoxWidth) continue;

                final List<Bar> bars = new ArrayList<>();
                final List<Tag> tags = new ArrayList<>();

                if (this.healthBarProperty.getValue() != HealthBarColour.OFF) {
                    final float healthPercentage = player.getHealth() / player.getMaxHealth();

                    bars.add(new Bar(1.0, EnumPosition.LEFT, true,
                                     this.healthProperty.getValue(),
                                     this.healthBarProperty.getValue() == HealthBarColour.HEALTH ?
                                         ColourUtil.blendHealthColours(healthPercentage) :
                                         this.healthBarColourProperty.getValue(),
                                     this.secondHealthBarColourProperty.getValue(),
                                     this.healthBarProperty.getValue() == HealthBarColour.GRADIENT,
                                     () -> (double) healthPercentage));

                    // Absorption bar
                    if (player.getAbsorptionAmount() > 0.0F) {
                        final double percentage = player.getAbsorptionAmount() / player.getMaxHealth();

                        if (percentage <= 1.0) {
                            // Absorption bar
                            bars.add(new Bar(1.0, EnumPosition.LEFT, true, false, 0xFFFFFF00,
                                             0, false, () -> percentage));
                        } else {
                            double temp = percentage;

                            do {
                                final double min = Math.min(1.0F, temp);
                                temp = temp - min;

                                bars.add(new Bar(1.0, EnumPosition.LEFT, true, false, 0xFFFFFF00,
                                                 0, false, () -> min));
                            } while (temp > 1.0);

                            final double fTemp = temp;

                            bars.add(new Bar(1.0, EnumPosition.LEFT, true, false, 0xFFFFFF00,
                                             0, false, () -> fTemp));
                        }
                    }
                }

                if (this.armourBarProperty.getValue()) {
                    bars.add(new Bar(1.0, EnumPosition.BOTTOM, true, false,
                                     this.armourBarColourProperty.getValue(), 0, false,
                                     () -> player.getTotalArmorValue() / 20.0));
                }

                if (this.tagsProperty.getValue()) {
                    // Name tag
                    tags.add(new Tag(player.getDisplayName().getFormattedText(), EnumPosition.TOP, this.tagsColourProperty.getValue(), true));
                }

                this.drawables.add(new Drawable(position,
                                                this.boxProperty.getValue() ? new Box(0.5, this.boxColourProperty.getValue()) : null,
                                                bars, tags));
            }
        }

        if (this.chestProperty.getValue() && this.boxProperty.getValue()) {
            for (final TileEntity tileEntity : this.mc.theWorld.loadedTileEntityList) {
                if (this.validateTileEntity(tileEntity)) {
                    final AxisAlignedBB boundingBox = tileEntity.getBlockType().getCollisionBoundingBox(this.mc.theWorld,
                                                                                                        tileEntity.getPos(),
                                                                                                        tileEntity.getBlockType().getBlockState().getBaseState());

                    if (boundingBox == null) continue;

                    if (!this.frustum.isBoundingBoxInFrustum(boundingBox)) continue;

                    final double[] position = DrawUtil.worldToScreen(null, boundingBox,
                                                                     screenBuffer, this.projectionBuffer);

                    if (position == null || position[2] - position[0] > maxBoxWidth) continue;

                    this.drawables.add(new Drawable(position,
                                                    new Box(0.5, 0xFFFCA105),
                                                    null, null));
                }
            }
        }
    };

    private static void drawSkeleton(final float partialTicks,
                                     final EntityPlayer player,
                                     float[][] modelRotations) {
        glPushMatrix();
        final double x = DrawUtil.interpolate(player.prevPosX, player.posX, partialTicks);
        final double y = DrawUtil.interpolate(player.prevPosY, player.posY, partialTicks);
        final double z = DrawUtil.interpolate(player.prevPosZ, player.posZ, partialTicks);

        float rotationYawHead;
        final float prevRotationYawHead;
        float renderYawOffset;
        final float prevRenderYawOffset;

        useClientSideRots:
        {
//            if (player instanceof EntityPlayerSP) {
//                final EntityPlayerSP localPlayer = (EntityPlayerSP) player;
//
//                if (localPlayer.currentEvent != null && localPlayer.currentEvent.isRotating()) {
//                    final UpdatePositionEvent event = localPlayer.currentEvent;
//                    final float serverYaw = event.getYaw();
//                    final float prevServerYaw = event.getPrevYaw();
//                    rotationYawHead = serverYaw;
//                    prevRotationYawHead = prevServerYaw;
//                    renderYawOffset = serverYaw;
//                    prevRenderYawOffset = prevServerYaw;
//                    break useClientSideRots;
//                }
//            }

            rotationYawHead = player.rotationYawHead;
            prevRotationYawHead = player.prevRotationYawHead;
            renderYawOffset = player.renderYawOffset;
            prevRenderYawOffset = player.prevRenderYawOffset;
        }

        rotationYawHead = DrawUtil.interpolate(prevRotationYawHead, rotationYawHead, partialTicks);
        renderYawOffset = DrawUtil.interpolate(prevRenderYawOffset, renderYawOffset, partialTicks);
        final boolean sneaking = player.isSneaking();
        final float yOff = sneaking ? 0.6F : 0.75F;

        glTranslated(x, y, z);
        glRotatef(-renderYawOffset, 0.0F, 1.0F, 0.0F);
        glTranslatef(0.0F, 0.0F, sneaking ? -0.235F : 0.0F);

        // Right leg
        {
            glPushMatrix();
            final float[] rightLegAngles = modelRotations[3];
            glTranslatef(-0.125F, yOff, 0.0F);
            glRotatef(rightLegAngles[0] * 57.295776F, 1.0F, 0.0F, 0.0F);
            glRotatef(rightLegAngles[1] * 57.295776F, 0.0F, 1.0F, 0.0F);
            glRotatef(rightLegAngles[2] * 57.295776F, 0.0F, 0.0F, 1.0F);

            glBegin(GL_LINE_STRIP);
            {
                glVertex3i(0, 0, 0);
                glVertex3f(0.0F, -yOff + 0.125F, 0.0F);
            }
            glEnd();

            glPopMatrix();
        }

        // Left leg
        {
            glPushMatrix();

            final float[] leftLegAngles = modelRotations[4];

            glTranslatef(0.125F, yOff, 0.0F);

            glRotatef(leftLegAngles[0] * 57.295776F, 1.0F, 0.0F, 0.0F);
            glRotatef(leftLegAngles[1] * 57.295776F, 0.0F, 1.0F, 0.0F);
            glRotatef(leftLegAngles[2] * 57.295776F, 0.0F, 0.0F, 1.0F);

            glBegin(GL_LINE_STRIP);
            {
                glVertex3i(0, 0, 0);
                glVertex3f(0.0F, -yOff + 0.125F, 0.0F);
            }
            glEnd();

            glPopMatrix();
        }

        glTranslatef(0.0F, 0.0F, sneaking ? 0.25F : 0.0F);
        glPushMatrix();
        glTranslatef(0.0F, sneaking ? -0.05F : 0.0F, sneaking ? -0.01725F : 0.0F);

        // Right arm
        {
            glPushMatrix();

            final float[] rightArmAngles = modelRotations[1];

            glTranslatef(-0.375F, yOff + 0.55F, 0.0F);

            glRotatef(rightArmAngles[0] * 57.295776F, 1.0F, 0.0F, 0.0F);
            glRotatef(rightArmAngles[1] * 57.295776F, 0.0F, 1.0F, 0.0F);
            glRotatef(-rightArmAngles[2] * 57.295776F, 0.0F, 0.0F, 1.0F);

            glBegin(GL_LINE_STRIP);
            {
                glVertex3i(0, 0, 0);
                glVertex3f(0.0F, -0.45F, 0.0F);
            }
            glEnd();

            glPopMatrix();
        }

        // Left arm
        {
            glPushMatrix();

            final float[] leftArmAngles = modelRotations[2];

            glTranslatef(0.375F, yOff + 0.55F, 0.0F);

            glRotatef(leftArmAngles[0] * 57.295776F, 1.0F, 0.0F, 0.0F);
            glRotatef(leftArmAngles[1] * 57.295776F, 0.0F, 1.0F, 0.0F);
            glRotatef(-leftArmAngles[2] * 57.295776F, 0.0F, 0.0F, 1.0F);

            glBegin(GL_LINE_STRIP);
            {
                glVertex3i(0, 0, 0);
                glVertex3f(0.0F, -0.45F, 0.0F);
            }
            glEnd();

            glPopMatrix();
        }

        glRotatef(renderYawOffset - rotationYawHead, 0.0F, 1.0F, 0.0F);

        // Head
        {
            glPushMatrix();

            glTranslatef(0.0F, yOff + 0.55F, 0.0F);

            glRotatef(modelRotations[0][0] * 57.295776F, 1.0F, 0.0F, 0.0F);

            glBegin(GL_LINE_STRIP);
            {
                glVertex3i(0, 0, 0);
                glVertex3f(0.0F, 0.3F, 0.0F);
            }
            glEnd();

            glPopMatrix();
        }


        glPopMatrix();

        glRotatef(sneaking ? 25.0F : 0.0F, 1.0F, 0.0F, 0.0F);
        glTranslatef(0.0F, sneaking ? -0.16175F : 0.0F, sneaking ? -0.48025F : 0.0F);

        // Pelvis / Body / Chest
        glTranslated(0.0F, yOff, 0.0F);

        glBegin(GL_LINE_STRIP);
        {
            // Pelvis
            glVertex3f(-0.125F, 0.0F, 0.0F);
            glVertex3f(0.125F, 0.0F, 0.0F);

            // Body
            glVertex3i(0, 0, 0);
            glVertex3f(0.0F, 0.55F, 0.0F);

            // Chest
            glVertex3f(-0.375F, 0.55F, 0.0F);
            glVertex3f(0.375F, 0.55F, 0.0F);
        }
        glEnd();

        glPopMatrix();
    }

    private boolean validateTileEntity(final TileEntity entity) {
        return entity instanceof TileEntityChest && !entity.isInvalid() &&
            getSqDistToTileEntity(this.mc.thePlayer, entity) < 4096.0;
    }

    private static double getSqDistToTileEntity(final Entity start, final TileEntity entity) {
        final BlockPos chestBPos = entity.getPos();

        final double xDist = chestBPos.getX() - start.posX;
        final double yDist = chestBPos.getY() - start.posY;
        final double zDist = chestBPos.getZ() - start.posZ;

        return xDist * xDist + yDist * yDist + zDist * zDist;
    }

    private boolean validatePlayer(final EntityPlayer player) {
        return player.isEntityAlive() &&
            !player.isInvisible() &&
            (player instanceof EntityOtherPlayerMP || this.mc.gameSettings.getThirdPersonView() > 0);
    }

    @Override
    public void onEnable() {
        this.updateScreenBuffer(new ScaledResolution(this.mc));
    }

    private void updateScreenBuffer(final ScaledResolution scaledResolution) {
        final double scale = 2.0 / scaledResolution.getScaleFactor();
        this.screenBuffer[0] = scaledResolution.getScaledWidth() / scale;
        this.screenBuffer[1] = scaledResolution.getScaledHeight() / scale;
        this.screenBuffer[2] = 2.0;
    }

    @Override
    public void onDisable() {
        this.drawables.clear();
    }

    private enum HealthBarColour {
        OFF("Off"),
        HEALTH("Health"),
        COLOUR("Colour"),
        GRADIENT("Gradient");

        private final String name;

        HealthBarColour(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
