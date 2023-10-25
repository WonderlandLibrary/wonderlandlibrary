package org.dreamcore.client.feature.impl.visual;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.*;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.render.EventRender2D;
import org.dreamcore.client.event.events.impl.render.EventRender3D;
import org.dreamcore.client.event.events.impl.render.EventRenderPlayerName;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.feature.impl.combat.AntiBot;
import org.dreamcore.client.feature.impl.misc.StreamerMode;
import org.dreamcore.client.feature.impl.player.ClipHelper;
import org.dreamcore.client.helpers.math.MathematicHelper;
import org.dreamcore.client.helpers.math.RotationHelper;
import org.dreamcore.client.helpers.misc.ChatHelper;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;
import org.dreamcore.client.ui.shader.shaders.EntityGlowShader;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

public class EntityESP extends Feature {

    public static ListSetting espMode;
    public static BooleanSetting tags;
    public static NumberSetting glowAlpha;
    private final int black = Color.BLACK.getRGB();
    private final ColorSetting colorEsp;
    private final ColorSetting triangleColor;
    private final BooleanSetting fullBox;
    private final BooleanSetting heathPercentage;
    private final BooleanSetting tagsBackground;
    private final BooleanSetting border;
    private final BooleanSetting filled = new BooleanSetting("Filled", false, () -> espMode.currentMode.equals("2D"));
    private final ColorSetting filledColor = new ColorSetting("Filled Color", Integer.MIN_VALUE, () -> filled.getBoolValue() && espMode.currentMode.equals("2D"));
    public BooleanSetting healRect = new BooleanSetting("Health Rect", true, () -> true);
    public BooleanSetting triangleESP;
    public BooleanSetting ignoreInvisible = new BooleanSetting("Ignore Invisible", true, () -> true);
    public ListSetting healcolorMode = new ListSetting("Color Health Mode", "Custom", () -> healRect.getBoolValue(), "Astolfo", "Health", "Rainbow", "Client", "Custom");
    private final ColorSetting healColor = new ColorSetting("Health Border Color", 0xffffffff, () -> healcolorMode.currentMode.equals("Custom"));
    public ListSetting csgoMode;
    public ListSetting colorMode = new ListSetting("Color Box Mode", "Custom", () -> espMode.currentMode.equals("Box") || espMode.currentMode.equals("2D"), "Astolfo", "Rainbow", "Client", "Custom");
    public ListSetting triangleMode = new ListSetting("Triangle Mode", "Custom", () -> triangleESP.getBoolValue(), "Astolfo", "Rainbow", "Client", "Custom");
    public NumberSetting xOffset;
    public NumberSetting yOffset;
    public NumberSetting size;

    public EntityESP() {
        super("EntityESP", "Показывает игроков, ник и их здоровье сквозь стены", Type.Visuals);
        espMode = new ListSetting("ESP Mode", "2D", () -> true, "2D", "Box", "Glow");
        border = new BooleanSetting("Border Rect", true, () -> espMode.currentMode.equals("2D"));
        csgoMode = new ListSetting("Border Mode", "Box", () -> espMode.currentMode.equals("2D") && border.getBoolValue(), "Box", "Corner");
        tags = new BooleanSetting("Render Tags", true, () -> espMode.currentMode.equals("2D"));
        tagsBackground = new BooleanSetting("Tags Background", true, tags::getBoolValue);
        colorEsp = new ColorSetting("ESP Color", new Color(0xFFFFFF).getRGB(), () -> !colorMode.currentMode.equals("Client") && (espMode.currentMode.equals("2D") || espMode.currentMode.equals("Box") || espMode.currentMode.equals("Glow")));
        glowAlpha = new NumberSetting("Glow Alpha", 1, 0.2F, 1, 0.1F, () -> espMode.currentMode.equals("Glow"));
        fullBox = new BooleanSetting("Full Box", false, () -> espMode.currentMode.equals("Box"));
        heathPercentage = new BooleanSetting("Health Percentage", false, () -> espMode.currentMode.equals("2D"));
        triangleESP = new BooleanSetting("Triangle ESP", true, () -> true);
        triangleColor = new ColorSetting("Triangle Color", Color.PINK.getRGB(), () -> triangleESP.getBoolValue() && triangleMode.currentMode.equals("Custom"));
        xOffset = new NumberSetting("Triangle XOffset", 10, 0, 50, 5, () -> triangleESP.getBoolValue());
        yOffset = new NumberSetting("Triangle YOffset", 0, 0, 50, 5, () -> triangleESP.getBoolValue());
        size = new NumberSetting("Triangle Size", 5, 0, 20, 1, () -> triangleESP.getBoolValue());
        addSettings(espMode, csgoMode, colorMode, healcolorMode, healColor, colorEsp, border, fullBox, filled, filledColor, healRect, heathPercentage, tags, tagsBackground, ignoreInvisible, triangleESP, triangleMode, triangleColor, xOffset, yOffset, size);
    }

    @EventTarget
    public void onRender3D(EventRender3D event3D) {
        if (!getState())
            return;

        int color = 0;

        switch (colorMode.currentMode) {
            case "Client":
                color = ClientHelper.getClientColor().getRGB();
                break;
            case "Custom":
                color = colorEsp.getColorValue();
                break;
            case "Astolfo":
                color = PaletteHelper.astolfo(false, 10).getRGB();
                break;
            case "Rainbow":
                color = PaletteHelper.rainbow(300, 1, 1).getRGB();
                break;
        }

        EntityGlowShader framebufferShader = EntityGlowShader.GLOW_SHADER;

        if (espMode.currentMode.equals("Glow")) {
            mc.gameSettings.entityShadows = false;
            framebufferShader.renderShader(event3D.getPartialTicks());
            for (Entity entity : mc.world.loadedEntityList) {
                if (!isValid(entity) || entity instanceof EntityItem)
                    continue;

                mc.getRenderManager().renderEntityStatic(entity, event3D.getPartialTicks(), true);
            }

            framebufferShader.stopRenderShader(new Color(colorEsp.getColorValue()), 3, 1);
        }

        if (espMode.currentMode.equals("Box")) {
            GlStateManager.pushMatrix();
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityPlayer) {
                    if (!(entity == mc.player && mc.gameSettings.thirdPersonView == 0)) {
                        RenderHelper.drawEntityBox(entity, new Color(color), fullBox.getBoolValue(), fullBox.getBoolValue() ? 0.15F : 0.90F);
                    }
                }
            }
            GlStateManager.popMatrix();
        }
    }

    @EventTarget
    public void onRenderTriangle(EventRender2D eventRender2D) {
        if (triangleESP.getBoolValue()) {
            int color = 0;
            switch (triangleMode.currentMode) {
                case "Client":
                    color = ClientHelper.getClientColor().getRGB();
                    break;
                case "Custom":
                    color = triangleColor.getColorValue();
                    break;
                case "Astolfo":
                    color = PaletteHelper.astolfo(false, 1).getRGB();
                    break;
                case "Rainbow":
                    color = PaletteHelper.rainbow(300, 1, 1).getRGB();
                    break;
            }

            ScaledResolution sr = new ScaledResolution(mc);
            float size = 50;
            float xOffset = sr.getScaledWidth() / 2F - 24.5F;
            float yOffset = sr.getScaledHeight() / 2F - 25.2F;
            for (EntityPlayer entity : mc.world.playerEntities) {
                if ((ignoreInvisible.getBoolValue() && entity.isInvisible()))
                    continue;

                if (entity != null && entity != mc.player) {
                    GlStateManager.pushMatrix();
                    GlStateManager.disableBlend();
                    double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX;
                    double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ;
                    double cos = Math.cos(mc.player.rotationYaw * (Math.PI * 2 / 360));
                    double sin = Math.sin(mc.player.rotationYaw * (Math.PI * 2 / 360));
                    double rotY = -(z * cos - x * sin);
                    double rotX = -(x * cos + z * sin);
                    if (MathHelper.sqrt(rotX * rotX + rotY * rotY) < size) {
                        float angle = (float) (Math.atan2(rotY, rotX) * 180 / Math.PI);
                        double xPos = ((size / 2) * Math.cos(Math.toRadians(angle))) + xOffset + size / 2;
                        double y = ((size / 2) * Math.sin(Math.toRadians(angle))) + yOffset + size / 2;
                        GlStateManager.translate(xPos, y, 0);
                        GlStateManager.rotate(angle, 0, 0, 1);
                        GlStateManager.scale(1.5, 1, 1);
                        RenderHelper.drawTriangle(this.xOffset.getNumberValue(), this.yOffset.getNumberValue(), this.size.getNumberValue() + 0.5F, 90, new Color(color).darker().getRGB());
                    }
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        String mode = espMode.getOptions();
        setSuffix(mode);

        float partialTicks = mc.timer.renderPartialTicks;
        int scaleFactor = event.getResolution().getScaleFactor();
        double scaling = scaleFactor / Math.pow(scaleFactor, 2);
        GL11.glPushMatrix();
        GlStateManager.scale(scaling, scaling, scaling);

        int color = 0;
        switch (colorMode.currentMode) {
            case "Client":
                color = ClientHelper.getClientColor().getRGB();
                break;
            case "Custom":
                color = colorEsp.getColorValue();
                break;
            case "Astolfo":
                color = PaletteHelper.astolfo(false, 1).getRGB();
                break;
            case "Rainbow":
                color = PaletteHelper.rainbow(300, 1, 1).getRGB();
                break;
        }

        float scale = 1;

        for (Entity entity : mc.world.loadedEntityList) {
            if ((ignoreInvisible.getBoolValue() && entity.isInvisible()))
                continue;

            if (isValid(entity) && RenderHelper.isInViewFrustum(entity)) {
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks();
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks();
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks();
                double width = entity.width / 1.5;
                double height = entity.height + ((entity.isSneaking() || (entity == mc.player && mc.player.isSneaking()) ? -0.3D : 0.2D));
                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                List<Vector3d> vectors = Arrays.asList(new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ),
                        new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ),
                        new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ),
                        new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));

                mc.entityRenderer.setupCameraTransform(partialTicks, 0);

                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = vectorRender2D(scaleFactor, vector.x - mc.getRenderManager().renderPosX, vector.y - mc.getRenderManager().renderPosY, vector.z - mc.getRenderManager().renderPosZ);
                    if (vector != null && vector.z > 0 && vector.z < 1) {

                        if (position == null) {
                            position = new Vector4d(vector.x, vector.y, vector.z, 0);
                        }

                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }

                if (position != null) {
                    mc.entityRenderer.setupOverlayRendering();
                    double posX = position.x;
                    double posY = position.y;
                    double endPosX = position.z;
                    double endPosY = position.w;
                    if (border.getBoolValue()) {
                        if (mode.equalsIgnoreCase("2D") && csgoMode.currentMode.equalsIgnoreCase("Box")) {

                            //top
                            RectHelper.drawRect(posX - 0.5, posY - 0.5, endPosX + 0.5, posY + 0.5 + 1, black);

                            //button
                            RectHelper.drawRect(posX - 0.5, endPosY - 0.5 - 1, endPosX + 0.5, endPosY + 0.5, black);

                            //left
                            RectHelper.drawRect(posX - 1.5, posY, posX + 0.5, endPosY + 0.5, black);

                            //right
                            RectHelper.drawRect(endPosX - 0.5 - 1, posY, endPosX + 0.5, endPosY + 0.5, black);

                            /* Main ESP */

                            //left
                            RectHelper.drawRect(posX - 1, posY, posX + 0.5 - 0.5, endPosY, color);

                            //Button
                            RectHelper.drawRect(posX, endPosY - 1, endPosX, endPosY, color);

                            //Top
                            RectHelper.drawRect(posX - 1, posY, endPosX, posY + 1, color);

                            //Right
                            RectHelper.drawRect(endPosX - 1, posY, endPosX, endPosY, color);
                        } else if (mode.equalsIgnoreCase("2D") && csgoMode.currentMode.equalsIgnoreCase("Corner")) {

                            //Top Left
                            RectHelper.drawRect(posX + 1, posY, posX - 1, posY + (endPosY - posY) / 4 + 0.5, black);

                            //Button Left
                            RectHelper.drawRect(posX - 1, endPosY, posX + 1, endPosY - (endPosY - posY) / 4.0 - 0.5, black);

                            //Top Left Corner
                            RectHelper.drawRect(posX - 1, posY - 0.5, posX + (endPosX - posX) / 3, posY + 1, black);

                            //Top Corner
                            RectHelper.drawRect(endPosX - (endPosX - posX) / 3 - 0, posY - 0.5, endPosX, posY + 1.5, black);

                            //Top Right Corner
                            RectHelper.drawRect(endPosX - 1.5, posY, endPosX + 0.5, posY + (endPosY - posY) / 4 + 0.5, black);

                            //Right Button Corner
                            RectHelper.drawRect(endPosX - 1.5, endPosY, endPosX + 0.5, endPosY - (endPosY - posY) / 4 - 0.5, black);

                            //Left Button
                            RectHelper.drawRect(posX - 1, endPosY - 1.5, posX + (endPosX - posX) / 3 + 0.5, endPosY + 0.5, black);

                            //Right Button
                            RectHelper.drawRect(endPosX - (endPosX - posX) / 3 - 0.5, endPosY - 1.5, endPosX + 0.5, endPosY + 0.5, black);

                            RectHelper.drawRect(posX + 0.5, posY, posX - 0.5, posY + (endPosY - posY) / 4, color);

                            RectHelper.drawRect(posX + 0.5, endPosY, posX - 0.5, endPosY - (endPosY - posY) / 4, color);

                            RectHelper.drawRect(posX - 0.5, posY, posX + (endPosX - posX) / 3, posY + 1, color);
                            RectHelper.drawRect(endPosX - (endPosX - posX) / 3 + 0.5, posY, endPosX, posY + 1, color);
                            RectHelper.drawRect(endPosX - 1, posY, endPosX, posY + (endPosY - posY) / 4 + 0.5, color);
                            RectHelper.drawRect(endPosX - 1, endPosY, endPosX, endPosY - (endPosY - posY) / 4, color);
                            RectHelper.drawRect(posX, endPosY - 1, posX + (endPosX - posX) / 3, endPosY, color);
                            RectHelper.drawRect(endPosX - (endPosX - posX) / 3, endPosY - 1, endPosX - 0.5, endPosY, color);
                        }
                    }
                    boolean living = entity instanceof EntityLivingBase;
                    EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                    float targetHP = entityLivingBase.getHealth();
                    targetHP = MathHelper.clamp(targetHP, 0F, 24F);
                    float maxHealth = entityLivingBase.getMaxHealth();
                    double hpPercentage = (targetHP / maxHealth);
                    double hpHeight2 = (endPosY - posY) * hpPercentage;

                    /*CLIP HELPER*/

                    if (dreamcore.instance.featureManager.getFeatureByClass(ClipHelper.class).getState()) {
                        BlockPos playerPosY = new BlockPos(0, mc.player.posY, 0);
                        BlockPos entityPosY = new BlockPos(0, entity.posY, 0);
                        if (RotationHelper.isLookingAtEntity(mc.player.rotationYaw, mc.player.rotationPitch, 0.15F, 0.15F, 0.15F, entity, ClipHelper.maxDistance.getNumberValue())) {
                            BlockPos distanceToY = new BlockPos(0, ((int) mc.player.posY - entity.posY), 0);
                            int findToClip = (int) entity.posY;
                            if (!playerPosY.equals(entityPosY) && mc.gameSettings.thirdPersonView == 0) {
                                if (RenderHelper.isInViewFrustum(entity)) {
                                    float diff = (float) (endPosX - posX) / 2;
                                    float textWidth = (mc.fontRendererObj.getStringWidth("Target" + " §7" + distanceToY.getY() + "m") * scale);
                                    float tagX = (float) ((posX + diff - textWidth / 2) * scale);
                                    mc.fontRendererObj.drawStringWithShadow("Target" + " §7" + distanceToY.getY() + "m", tagX, (float) endPosY, -1);
                                }
                                if (Mouse.isButtonDown(2)) {
                                    mc.player.setPositionAndUpdate(mc.player.posX, findToClip, mc.player.posZ);
                                    ChatHelper.addChatMessage("Clip to entity " + ChatFormatting.RED + entity.getName() + ChatFormatting.WHITE + " on Y " + ChatFormatting.RED + findToClip);
                                }
                            }
                        }
                    }
                    if (targetHP > 0) {
                        String string2 = "" + MathematicHelper.round(entityLivingBase.getHealth() / entityLivingBase.getMaxHealth() * 100F, 1) + "%";
                        if (living && heathPercentage.getBoolValue() && (!(espMode.currentMode.equals("Box")))) {
                            if (heathPercentage.getBoolValue()) {
                                GlStateManager.pushMatrix();
                                mc.fontRendererObj.drawStringWithShadow(string2, (float) posX - 36, (float) ((float) endPosY - hpHeight2), -1);
                                GlStateManager.popMatrix();
                            }
                        }

                        if (living && healRect.getBoolValue() && (!(espMode.currentMode.equals("Box")))) {
                            int colorHeal = 0;

                            switch (healcolorMode.currentMode) {
                                case "Client":
                                    colorHeal = ClientHelper.getClientColor().getRGB();
                                    break;
                                case "Custom":
                                    colorHeal = healColor.getColorValue();
                                    break;
                                case "Astolfo":
                                    colorHeal = PaletteHelper.astolfo(false, (int) entity.height).getRGB();
                                    break;
                                case "Rainbow":
                                    colorHeal = PaletteHelper.rainbow(300, 1, 1).getRGB();
                                    break;
                                case "Health":
                                    colorHeal = PaletteHelper.getHealthColor(((EntityLivingBase) entity).getHealth(), ((EntityLivingBase) entity).getMaxHealth());
                                    break;
                            }

                            RectHelper.drawRect(posX - 5, posY - 0.5, posX - 2.5, endPosY + 0.5, new Color(0, 0, 0, 125).getRGB());
                            RectHelper.drawRect(posX - 4.5, endPosY, posX - 3, endPosY - hpHeight2, colorHeal);
                        }

                        if (filled.getBoolValue()) {
                            RectHelper.drawRect(posX, posY, endPosX, endPosY, filledColor.getColorValue());
                        }

                        if (living && tags.getBoolValue() && !dreamcore.instance.featureManager.getFeatureByClass(NameTags.class).getState()) {
                            float scaledHeight = 10;
                            float diff = (float) (endPosX - posX) / 2;
                            String tag = entity.getDisplayName().getFormattedText();
                            int healthXyi = (int) ((EntityLivingBase) entity).getHealth();
                            if (healthXyi <= ((EntityLivingBase) entity).getMaxHealth() * 0.25F) {
                                tag = tag + "\u00a74";
                            } else if (healthXyi <= ((EntityLivingBase) entity).getMaxHealth() * 0.5F) {
                                tag = tag + "\u00a76";
                            } else if (healthXyi <= ((EntityLivingBase) entity).getMaxHealth() * 0.75F) {
                                tag = tag + "\u00a7e";
                            } else if (healthXyi <= ((EntityLivingBase) entity).getMaxHealth()) {
                                tag = tag + "\u00a72";
                            }
                            tag = tag + " " + MathematicHelper.round(((EntityLivingBase) entity).getHealth(), 0) + " ";

                            float textWidth = (mc.fontRendererObj.getStringWidth(tag) * scale);
                            float tagX = (float) ((posX + diff - textWidth / 2) * scale);
                            float tagY = (float) (posY * scale) - scaledHeight;
                            GlStateManager.pushMatrix();
                            GlStateManager.scale(scale, scale, scale);
                            GlStateManager.translate(0, -4, 0);
                            if (tagsBackground.getBoolValue()) {
                                RectHelper.drawRect((tagX - 2), (tagY - 2), tagX + textWidth * scale + 2, (tagY + 9), (new Color(0, 0, 0, 125)).getRGB());
                            }

                            mc.fontRendererObj.drawStringWithShadow(tag, tagX, tagY, Color.WHITE.getRGB());
                            GlStateManager.popMatrix();
                        }
                    }
                }
            }
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GlStateManager.enableBlend();
        GL11.glPopMatrix();
        mc.entityRenderer.setupOverlayRendering();
    }

    @EventTarget
    public void onRenderName(EventRenderPlayerName event) {
        if (!getState())
            return;

        event.setCancelled(tags.getBoolValue());
    }

    private boolean isValid(Entity entity) {
        if (mc.gameSettings.thirdPersonView == 0 && entity == mc.player)
            return false;
        if (entity.isDead)
            return false;
        if ((entity instanceof net.minecraft.entity.passive.EntityAnimal))
            return false;
        if ((entity instanceof EntityPlayer))
            return true;
        if ((entity instanceof EntityArmorStand))
            return false;
        if ((entity instanceof IAnimals))
            return false;
        if ((entity instanceof EntityItemFrame))
            return false;
        if (entity instanceof EntityArrow)
            return false;
        if ((entity instanceof EntityMinecart))
            return false;
        if ((entity instanceof EntityBoat))
            return false;
        if ((entity instanceof EntityDragonFireball))
            return false;
        if ((entity instanceof EntityXPOrb))
            return false;
        if ((entity instanceof EntityTNTPrimed))
            return false;
        if ((entity instanceof EntityExpBottle))
            return false;
        if ((entity instanceof EntityLightningBolt))
            return false;
        if ((entity instanceof EntityPotion))
            return false;
        if ((entity instanceof Entity))
            return false;
        if (((entity instanceof net.minecraft.entity.monster.EntityMob || entity instanceof net.minecraft.entity.monster.EntitySlime || entity instanceof net.minecraft.entity.boss.EntityDragon
                || entity instanceof net.minecraft.entity.monster.EntityGolem)))
            return false;
        return entity != mc.player;
    }

    private Vector3d vectorRender2D(int scaleFactor, double x, double y, double z) {
        float xPos = (float) x;
        float yPos = (float) y;
        float zPos = (float) z;
        IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
        FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
        if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector))
            return new Vector3d((vector.get(0) / scaleFactor), ((Display.getHeight() - vector.get(1)) / scaleFactor), vector.get(2));
        return null;
    }
}