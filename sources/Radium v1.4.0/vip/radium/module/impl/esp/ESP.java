package vip.radium.module.impl.esp;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import vip.radium.RadiumClient;
import vip.radium.event.EventBusPriorities;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.event.impl.render.Render3DEvent;
import vip.radium.event.impl.render.RenderNameTagEvent;
import vip.radium.event.impl.render.overlay.Render2DEvent;
import vip.radium.event.impl.world.WorldLoadEvent;
import vip.radium.gui.font.FontManager;
import vip.radium.gui.font.FontRenderer;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.property.impl.MultiSelectEnumProperty;
import vip.radium.utils.PlayerUtils;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.Colors;
import vip.radium.utils.render.OGLUtils;
import vip.radium.utils.render.RenderingUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

@ModuleInfo(label = "ESP", category = ModuleCategory.ESP)
public final class ESP extends Module {

    private static ESP espInst;
    private final MultiSelectEnumProperty<Targets> targetsProperty = new MultiSelectEnumProperty<>(
            "Targets",
            Targets.PLAYERS, Targets.FRIENDS, Targets.TEAMMATES, Targets.SELF
    );
    private final Property<Boolean> tagsProperty = new Property<>(
            "Tags",
            true);
    private final Property<Integer> tagsColorProperty = new Property<>(
            "Tags Color",
            0xFFFFFFFF,
            tagsProperty::getValue);
    private final Property<Boolean> tagsBackgroundProperty = new Property<>(
            "Tags Rect",
            true,
            tagsProperty::getValue);
    private final Property<Boolean> esp2dProperty = new Property<>(
            "2D ESP",
            true);
    private final Property<Boolean> boxProperty = new Property<>(
            "Box",
            true,
            esp2dProperty::getValue);
    private final Property<Integer> boxColorProperty = new Property<>(
            "Box Color",
            Colors.LIGHT_BLUE,
            () -> boxProperty.isAvailable() && boxProperty.getValue());
    private final Property<Boolean> armorBarProperty = new Property<>(
            "Armor Bar",
            true,
            esp2dProperty::getValue);
    private final Property<Integer> armorBarColorProperty = new Property<>("Armor Col", new Color(0, 255, 255).getRGB(),
            () -> armorBarProperty.isAvailable() && armorBarProperty.getValue());

    private final Property<Boolean> healthBarProperty = new Property<>(
            "HP Bar",
            true,
            esp2dProperty::getValue);
    private final Property<Integer> absorptionColorProperty = new Property<>(
            "Absorption",
            0xFFDD00FF,
            () -> healthBarProperty.isAvailable() && healthBarProperty.getValue());
    private final Property<Boolean> pulsingHealthBarProperty = new Property<>(
            "Pulsing HP",
            true,
            () -> healthBarProperty.isAvailable() && healthBarProperty.getValue());

    private final EnumProperty<HealthBarColor> healthBarColorModeProperty = new EnumProperty<>(
            "HP Color Mode",
            HealthBarColor.SOLID,
            () -> healthBarProperty.isAvailable() && healthBarProperty.getValue());
    private final Property<Integer> healthBarColorProperty = new Property<>("HP Color",
            new Color(0, 255, 89).getRGB(),
            () -> healthBarColorModeProperty.isAvailable() &&
                    healthBarColorModeProperty.getValue() == HealthBarColor.SOLID ||
                    healthBarColorModeProperty.getValue() == HealthBarColor.GRADIENT);

    private final Property<Integer> healthBarEndColorProperty = new Property<>("HP End", Colors.RED,
            () -> healthBarColorModeProperty.isAvailable() &&
                    healthBarColorModeProperty.getValue() == HealthBarColor.GRADIENT);

    private final Property<Boolean> skeletonsProperty = new Property<>(
            "Skeletons",
            true);
    private final DoubleProperty skeletonWidthProperty = new DoubleProperty(
            "Skeleton Width",
            0.5,
            skeletonsProperty::getValue,
            0.5,
            5.0,
            0.5);
    private final Property<Integer> skeletonsColorProperty = new Property<>(
            "Skeleton", Colors.LIGHT_BLUE, skeletonsProperty::getValue
    );
    private final Map<EntityPlayer, float[][]> playerRotationMap = new HashMap<>();
    private final Map<EntityPlayer, float[]> entityPosMap = new HashMap<>();
    @EventLink
    public final Listener<RenderNameTagEvent> onRenderNameTagEvent = event -> {
        if (tagsProperty.getValue() && entityPosMap.containsKey(event.getEntityLivingBase()))
            event.setCancelled();
    };
    @EventLink(EventBusPriorities.LOWEST)
    public final Listener<Render2DEvent> onRender2DEvent = e -> {
        final boolean tags = tagsProperty.getValue();
        final boolean esp2d = esp2dProperty.getValue();

        if (!esp2d && !tags) {
            return;
        }

        final boolean box = boxProperty.getValue();
        final boolean healthBar = healthBarProperty.getValue();
        final boolean armorBar = armorBarProperty.getValue();

        for (EntityPlayer player : entityPosMap.keySet()) {
            if ((player.getDistanceToEntity(Wrapper.getPlayer()) < 1.0F && Wrapper.isInFirstPerson()) ||
                    !RenderingUtils.isBBInFrustum(player.getEntityBoundingBox()))
                continue;

            final float[] positions = entityPosMap.get(player);
            final float x = positions[0];
            final float y = positions[1];
            final float x2 = positions[2];
            final float y2 = positions[3];

            final float health = player.getHealth();
            final float maxHealth = player.getMaxHealth();
            final float healthPercentage = health / maxHealth;

            if (tags) {
                final FontRenderer fontRenderer = FontManager.SP_FR;

                final String name = player.getGameProfile().getName();
                float halfWidth = fontRenderer.getWidth(name) / 2;
                final float xDif = x2 - x;
                final float middle = x + (xDif / 2);
                final float textHeight = fontRenderer.getHeight(name);
                float renderY = y - textHeight - 2;

                final float left = middle - halfWidth - 1;
                final float right = middle + halfWidth + 1;

                if (tagsBackgroundProperty.getValue()) {
                    Gui.drawRect(left, renderY - 1, right, renderY + textHeight + 1, 0x96000000);
                }

                fontRenderer.drawStringWithOutline(name, middle - halfWidth, renderY + 0.5F, this.tagsColorProperty.getValue());
            }

            if (esp2d) {
                glDisable(GL_TEXTURE_2D);
                OGLUtils.enableBlending();

                if (armorBar) {
                    final float armorPercentage = player.getTotalArmorValue() / 20.0F;
                    final float armorBarWidth = (x2 - x) * armorPercentage;

                    glColor4ub((byte) 0, (byte) 0, (byte) 0, (byte) 0x96);
                    glBegin(GL_QUADS);

                    // Background
                    {
                        glVertex2f(x, y2 + 0.5F);
                        glVertex2f(x, y2 + 2.5F);

                        glVertex2f(x2, y2 + 2.5F);
                        glVertex2f(x2, y2 + 0.5F);
                    }

                    if (armorPercentage > 0) {
                        OGLUtils.color(armorBarColorProperty.getValue());

                        // Bar
                        {
                            glVertex2f(x + 0.5F, y2 + 1);
                            glVertex2f(x + 0.5F, y2 + 2);

                            glVertex2f(x + armorBarWidth - 0.5F, y2 + 2);
                            glVertex2f(x + armorBarWidth - 0.5F, y2 + 1);
                        }
                    }

                    if (!healthBar)
                        glEnd();
                }

                if (healthBar) {
                    float healthBarLeft = x - 2.5F;
                    float healthBarRight = x - 0.5F;

                    glColor4ub((byte) 0, (byte) 0, (byte) 0, (byte) 0x96);

                    if (!armorBar)
                        glBegin(GL_QUADS);

                    // Background
                    {
                        glVertex2f(healthBarLeft, y);
                        glVertex2f(healthBarLeft, y2);

                        glVertex2f(healthBarRight, y2);
                        glVertex2f(healthBarRight, y);
                    }

                    healthBarLeft += 0.5F;
                    healthBarRight -= 0.5F;

                    final float heightDif = y - y2;
                    final float healthBarHeight = heightDif * healthPercentage;

                    final boolean pulsing = this.pulsingHealthBarProperty.getValue();

                    final float topOfHealthBar = y2 + 0.5F + healthBarHeight;

                    final HealthBarColor healthBarColorMode = healthBarColorModeProperty.getValue();

                    if (healthBarColorMode != HealthBarColor.GRADIENT) {
                        final int color = healthBarColorMode == HealthBarColor.SOLID ?
                                healthBarColorProperty.getValue() :
                                RenderingUtils.getColorFromPercentage(healthPercentage);

                        OGLUtils.color(pulsing ? RenderingUtils.fadeBetween(color, RenderingUtils.darker(color)) : color);

                        // Bar
                        {
                            glVertex2f(healthBarLeft, topOfHealthBar);
                            glVertex2f(healthBarLeft, y2 - 0.5F);

                            glVertex2f(healthBarRight, y2 - 0.5F);
                            glVertex2f(healthBarRight, topOfHealthBar);
                        }
                    } else {
                        glEnd();
                        final boolean needScissor = health < maxHealth;

                        if (needScissor) {
                            glEnable(GL_SCISSOR_TEST);
                            OGLUtils.startScissorBox(e.getResolution(), (int) healthBarLeft, (int) (y2 + healthBarHeight),
                                    2, (int) y2);
                        }

                        int startColor = healthBarColorProperty.getValue();
                        final int endColor = healthBarEndColorProperty.getValue();

                        glShadeModel(GL_SMOOTH);
                        glBegin(GL_QUADS);

                        startColor = pulsing ? RenderingUtils.fadeBetween(startColor, RenderingUtils.darker(startColor)) : startColor;
                        OGLUtils.color(startColor);

                        // Bar
                        {

                            glVertex2f(healthBarLeft, topOfHealthBar);
                            OGLUtils.color(pulsing ? RenderingUtils.fadeBetween(endColor, RenderingUtils.darker(endColor)) : endColor);
                            glVertex2f(healthBarLeft, y2 - 0.5F);
                            glVertex2f(healthBarRight, y2 - 0.5F);
                            OGLUtils.color(startColor);
                            glVertex2f(healthBarRight, topOfHealthBar);
                        }

                        glEnd();
                        glShadeModel(GL_FLAT);

                        if (needScissor)
                            glDisable(GL_SCISSOR_TEST);

                        glBegin(GL_QUADS);
                    }

                    final float absorption = player.getAbsorptionAmount();

                    final float absorptionPercentage = Math.min(1.0F, absorption / 20.0F);

                    final int absorptionColor = absorptionColorProperty.getValue();

                    final float absorptionHeight = heightDif * absorptionPercentage;

                    final float topOfAbsorptionBar = y2 + 0.5F + absorptionHeight;

                    OGLUtils.color(pulsing ? RenderingUtils.fadeBetween(absorptionColor, RenderingUtils.darker(absorptionColor)) : absorptionColor);

                    // Absorption Bar
                    {
                        glVertex2f(healthBarLeft, topOfAbsorptionBar);
                        glVertex2f(healthBarLeft, y2 - 0.5F);

                        glVertex2f(healthBarRight, y2 - 0.5F);
                        glVertex2f(healthBarRight, topOfAbsorptionBar);
                    }

                    if (!box)
                        glEnd();
                }

                if (box) {
                    glColor4ub((byte) 0, (byte) 0, (byte) 0, (byte) 0x96);
                    if (!healthBar)
                        glBegin(GL_QUADS);

                    // Background
                    {
                        // Left
                        glVertex2f(x, y);
                        glVertex2f(x, y2);
                        glVertex2f(x + 1.5F, y2);
                        glVertex2f(x + 1.5F, y);

                        // Right
                        glVertex2f(x2 - 1.5F, y);
                        glVertex2f(x2 - 1.5F, y2);
                        glVertex2f(x2, y2);
                        glVertex2f(x2, y);

                        // Top
                        glVertex2f(x + 1.5F, y);
                        glVertex2f(x + 1.5F, y + 1.5F);
                        glVertex2f(x2 - 1.5F, y + 1.5F);
                        glVertex2f(x2 - 1.5F, y);

                        // Bottom
                        glVertex2f(x + 1.5F, y2 - 1.5F);
                        glVertex2f(x + 1.5F, y2);
                        glVertex2f(x2 - 1.5F, y2);
                        glVertex2f(x2 - 1.5F, y2 - 1.5F);
                    }

                    OGLUtils.color(boxColorProperty.getValue());

                    // Box
                    {
                        // Left
                        glVertex2f(x + 0.5F, y + 0.5F);
                        glVertex2f(x + 0.5F, y2 - 0.5F);
                        glVertex2f(x + 1, y2 - 0.5F);
                        glVertex2f(x + 1, y + 0.5F);

                        // Right
                        glVertex2f(x2 - 1, y + 0.5F);
                        glVertex2f(x2 - 1, y2 - 0.5F);
                        glVertex2f(x2 - 0.5F, y2 - 0.5F);
                        glVertex2f(x2 - 0.5F, y + 0.5F);

                        // Top
                        glVertex2f(x + 0.5F, y + 0.5F);
                        glVertex2f(x + 0.5F, y + 1);
                        glVertex2f(x2 - 0.5F, y + 1);
                        glVertex2f(x2 - 0.5F, y + 0.5F);

                        // Bottom
                        glVertex2f(x + 0.5F, y2 - 1);
                        glVertex2f(x + 0.5F, y2 - 0.5F);
                        glVertex2f(x2 - 0.5F, y2 - 0.5F);
                        glVertex2f(x2 - 0.5F, y2 - 1);
                    }

                    glEnd();

                }

                glEnable(GL_TEXTURE_2D);
                glDisable(GL_BLEND);
            }
        }
    };

    private final Map<EntityPlayer, Relationship> playerRelationshipMap = new HashMap<>();
    @EventLink
    public final Listener<WorldLoadEvent> onWorldLoad = event -> {
        entityPosMap.clear();
        playerRotationMap.clear();
        playerRelationshipMap.clear();
    };
    @EventLink
    public final Listener<Render3DEvent> onRender3DEvent = e -> {
        final boolean skeletons = skeletonsProperty.getValue();
        final boolean project2D = esp2dProperty.getValue() || tagsProperty.getValue();
        if (project2D && !entityPosMap.isEmpty())
            entityPosMap.clear();

        if (skeletons) {
            glLineWidth(skeletonWidthProperty.getValue().floatValue());
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_LINE_SMOOTH);
            OGLUtils.color(skeletonsColorProperty.getValue());
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_TEXTURE_2D);
            glDepthMask(false);
        }

        final float partialTicks = e.getPartialTicks();

        for (final EntityPlayer player : Wrapper.getLoadedPlayers()) {
            if (!isValid(player))
                continue;
            if (project2D) {
                final double posX = (RenderingUtils.interpolate(player.prevPosX, player.posX, partialTicks) -
                        RenderManager.viewerPosX);
                final double posY = (RenderingUtils.interpolate(player.prevPosY, player.posY, partialTicks) -
                        RenderManager.viewerPosY);
                final double posZ = (RenderingUtils.interpolate(player.prevPosZ, player.posZ, partialTicks) -
                        RenderManager.viewerPosZ);

                final double halfWidth = player.width / 2.0D;
                final AxisAlignedBB bb = new AxisAlignedBB(posX - halfWidth, posY, posZ - halfWidth,
                        posX + halfWidth, posY + player.height + (player.isSneaking() ? -0.2D : 0.1D), posZ + halfWidth).expand(0.1, 0.1, 0.1);

                final double[][] vectors = {{bb.minX, bb.minY, bb.minZ},
                        {bb.minX, bb.maxY, bb.minZ},
                        {bb.minX, bb.maxY, bb.maxZ},
                        {bb.minX, bb.minY, bb.maxZ},
                        {bb.maxX, bb.minY, bb.minZ},
                        {bb.maxX, bb.maxY, bb.minZ},
                        {bb.maxX, bb.maxY, bb.maxZ},
                        {bb.maxX, bb.minY, bb.maxZ}};

                float[] projection;
                final float[] position = new float[]{Float.MAX_VALUE, Float.MAX_VALUE, -1.0F, -1.0F};

                for (final double[] vec : vectors) {
                    projection = OGLUtils.project2D((float) vec[0], (float) vec[1], (float) vec[2], 2);
                    if (projection != null && projection[2] >= 0.0F && projection[2] < 1.0F) {
                        final float pX = projection[0];
                        final float pY = projection[1];
                        position[0] = Math.min(position[0], pX);
                        position[1] = Math.min(position[1], pY);
                        position[2] = Math.max(position[2], pX);
                        position[3] = Math.max(position[3], pY);
                    }
                }

                entityPosMap.put(player, position);
            }

            if (skeletons) {
                drawSkeleton(partialTicks, player);
            }
        }

        if (skeletons) {
            glDepthMask(true);
            glDisable(GL_BLEND);
            glEnable(GL_TEXTURE_2D);
            glDisable(GL_LINE_SMOOTH);
            glEnable(GL_DEPTH_TEST);
        }
    };

    @EventLink
    public final Listener<UpdatePositionEvent> onTick = event -> {
        if (event.isPre()) {
            final Map<EntityPlayer, Relationship> relationshipMap = espInst.playerRelationshipMap;

            for (EntityPlayer player : Wrapper.getLoadedPlayers()) {
                Relationship relationship = relationshipMap.get(player);

                if (relationship != null) {
                    relationship.teammate = PlayerUtils.isTeamMate(player);
                    relationship.friend = RadiumClient.getInstance().getPlayerManager().isFriend(player);
                }
            }
        }
    };

    public ESP() {
        espInst = this;

        toggle();
    }

    public static boolean shouldDrawSkeletons() {
        return espInst.isEnabled() && espInst.skeletonsProperty.getValue();
    }

    public static void addEntity(EntityPlayer e,
                                 ModelPlayer model) {
        espInst.playerRotationMap.put(e, new float[][]{
                {model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ},
                {model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ},
                {model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ},
                {model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ},
                {model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ}
        });
    }

    // TODO: Bruh code but more optimized
    public static boolean isValid(Entity entity) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entity;

            if (!player.isEntityAlive()) {
                return false;
            }

            if (player.isInvisible() && !espInst.targetsProperty.isSelected(Targets.INVISIBLES)) {
                return false;
            }

            if (player instanceof EntityPlayerSP && (Wrapper.isInFirstPerson() || !espInst.targetsProperty.isSelected(Targets.SELF))) {
                return false;
            }

            final Map<EntityPlayer, Relationship> relationshipMap = espInst.playerRelationshipMap;

            Relationship relationship = relationshipMap.get(player);

            final boolean hasData = relationship != null;

            final boolean teammate;

            if (hasData) {
                teammate = relationship.teammate;
            } else {
                teammate = PlayerUtils.isTeamMate(player);
            }

            if (teammate && !espInst.targetsProperty.isSelected(Targets.TEAMMATES)) {
                return false;
            }

            final boolean friend;

            if (hasData) {
                friend = relationship.friend;
            } else {
                friend = RadiumClient.getInstance().getPlayerManager().isFriend(player);
            }

            if (friend && !espInst.targetsProperty.isSelected(Targets.FRIENDS)) {
                return false;
            }

            if (!hasData) {
                relationshipMap.put(player, new Relationship(teammate, friend));
            }

            return RenderingUtils.isBBInFrustum(entity.getEntityBoundingBox()) && Wrapper.getLoadedPlayers().contains(player);
        }

        return false;
    }

    @Override
    public void onDisable() {
        entityPosMap.clear();
        playerRotationMap.clear();
    }

    private void drawSkeleton(float pt,
                              EntityPlayer player) {
        float[][] entPos;
        if ((entPos = playerRotationMap.get(player)) != null) {
            glPushMatrix();
            float x = (float) (RenderingUtils.interpolate(player.prevPosX, player.posX, pt) -
                    RenderManager.renderPosX);
            float y = (float) (RenderingUtils.interpolate(player.prevPosY, player.posY, pt) -
                    RenderManager.renderPosY);
            float z = (float) (RenderingUtils.interpolate(player.prevPosZ, player.posZ, pt) -
                    RenderManager.renderPosZ);
            glTranslated(x, y, z);
            boolean sneaking = player.isSneaking();

            final float rotationYawHead;
            final float renderYawOffset;
            final float prevRenderYawOffset;

            useClientSideRots:
            {
                if (player instanceof EntityPlayerSP) {
                    final EntityPlayerSP localPlayer = (EntityPlayerSP) player;

                    if (localPlayer.currentEvent != null && localPlayer.currentEvent.isRotating()) {
                        final UpdatePositionEvent event = localPlayer.currentEvent;
                        final float serverYaw = event.getYaw();
                        rotationYawHead = serverYaw;
                        renderYawOffset = serverYaw;
                        prevRenderYawOffset = event.getPrevYaw();
                        break useClientSideRots;
                    }
                }

                rotationYawHead = player.rotationYawHead;
                renderYawOffset = player.renderYawOffset;
                prevRenderYawOffset = player.prevRenderYawOffset;
            }

            final float xOff = RenderingUtils.interpolate(prevRenderYawOffset, renderYawOffset, pt);
            float yOff = sneaking ? 0.6F : 0.75F;
            glRotatef(-xOff, 0.0F, 1.0F, 0.0F);
            glTranslatef(0.0F, 0.0F, sneaking ? -0.235F : 0.0F);

            // Right leg
            glPushMatrix();
            glTranslatef(-0.125F, yOff, 0.0F);
            if (entPos[3][0] != 0.0F)
                glRotatef(entPos[3][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
            if (entPos[3][1] != 0.0F)
                glRotatef(entPos[3][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
            if (entPos[3][2] != 0.0F)
                glRotatef(entPos[3][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
            glBegin(GL_LINE_STRIP);
            glVertex3i(0, 0, 0);
            glVertex3f(0.0F, -yOff, 0.0F);
            glEnd();
            glPopMatrix();

            // Left leg
            glPushMatrix();
            glTranslatef(0.125F, yOff, 0.0F);
            if (entPos[4][0] != 0.0F)
                glRotatef(entPos[4][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
            if (entPos[4][1] != 0.0F)
                glRotatef(entPos[4][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
            if (entPos[4][2] != 0.0F)
                glRotatef(entPos[4][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
            glBegin(GL_LINE_STRIP);
            glVertex3i(0, 0, 0);
            glVertex3f(0.0F, -yOff, 0.0F);
            glEnd();
            glPopMatrix();

            glTranslatef(0.0F, 0.0F, sneaking ? 0.25F : 0.0F);
            glPushMatrix();
            glTranslatef(0.0F, sneaking ? -0.05F : 0.0F, sneaking ? -0.01725F : 0.0F);

            // Right arm
            glPushMatrix();
            glTranslatef(-0.375F, yOff + 0.55F, 0.0F);
            if (entPos[1][0] != 0.0F)
                glRotatef(entPos[1][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
            if (entPos[1][1] != 0.0F)
                glRotatef(entPos[1][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
            if (entPos[1][2] != 0.0F)
                glRotatef(-entPos[1][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
            glBegin(GL_LINE_STRIP);
            glVertex3i(0, 0, 0);
            glVertex3f(0.0F, -0.5F, 0.0F);
            glEnd();
            glPopMatrix();

            // Left arm
            glPushMatrix();
            glTranslatef(0.375F, yOff + 0.55F, 0.0F);
            if (entPos[2][0] != 0.0F)
                glRotatef(entPos[2][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
            if (entPos[2][1] != 0.0F)
                glRotatef(entPos[2][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
            if (entPos[2][2] != 0.0F)
                glRotatef(-entPos[2][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
            glBegin(GL_LINE_STRIP);
            glVertex3i(0, 0, 0);
            glVertex3f(0.0F, -0.5F, 0.0F);
            glEnd();
            glPopMatrix();

            glRotatef(xOff - rotationYawHead, 0.0F, 1.0F, 0.0F);

            // Head
            glPushMatrix();
            glTranslatef(0.0F, yOff + 0.55F, 0.0F);
            if (entPos[0][0] != 0.0F)
                glRotatef(entPos[0][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
            glBegin(GL_LINE_STRIP);
            glVertex3i(0, 0, 0);
            glVertex3f(0.0F, 0.3F, 0.0F);
            glEnd();
            glPopMatrix();

            glPopMatrix();

            glRotatef(sneaking ? 25.0F : 0.0F, 1.0F, 0.0F, 0.0F);
            glTranslatef(0.0F, sneaking ? -0.16175F : 0.0F, sneaking ? -0.48025F : 0.0F);

            // Pelvis
            glPushMatrix();
            glTranslated(0.0F, yOff, 0.0F);
            glBegin(GL_LINE_STRIP);
            glVertex3f(-0.125F, 0.0F, 0.0F);
            glVertex3f(0.125F, 0.0F, 0.0F);
            glEnd();
            glPopMatrix();

            // Body
            glPushMatrix();
            glTranslatef(0.0F, yOff, 0.0F);
            glBegin(GL_LINE_STRIP);
            glVertex3i(0, 0, 0);
            glVertex3f(0.0F, 0.55F, 0.0F);
            glEnd();
            glPopMatrix();

            // Chest
            glPushMatrix();
            glTranslatef(0.0F, yOff + 0.55F, 0.0F);
            glBegin(GL_LINE_STRIP);
            glVertex3f(-0.375F, 0.0F, 0.0F);
            glVertex3f(0.375F, 0.0F, 0.0F);
            glEnd();
            glPopMatrix();

            glPopMatrix();
        }
    }

    private enum HealthBarColor {
        SOLID,
        HEALTH,
        GRADIENT
    }

    private enum Targets {
        PLAYERS,
        TEAMMATES,
        FRIENDS,
        INVISIBLES,
        SELF,
    }

    private static class Relationship {
        private boolean teammate;
        private boolean friend;

        public Relationship(boolean teammate, boolean friend) {
            this.teammate = teammate;
            this.friend = friend;
        }
    }
}