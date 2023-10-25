/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import baritone.events.events.player.EventUpdate;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFireworkCharge;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPlayerModel;
import org.celestial.client.event.events.impl.render.EventRender2D;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.event.events.impl.render.EventRenderPlayerName;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.misc.StreamerMode;
import org.celestial.client.feature.impl.player.ClipHelper;
import org.celestial.client.feature.impl.visuals.CustomModel;
import org.celestial.client.feature.impl.visuals.NameTags;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.font.MCFontRenderer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class EntityESP
extends Feature {
    public static ListSetting espMode;
    public static BooleanSetting tags;
    private final int black = Color.BLACK.getRGB();
    private final ColorSetting colorEsp;
    private final BooleanSetting friendHighlight;
    private final ColorSetting colorItemEsp;
    private final ColorSetting triangleColor;
    private final BooleanSetting fullBox;
    private final BooleanSetting tagsBackground;
    private final BooleanSetting armor;
    private final BooleanSetting border;
    public BooleanSetting healRect = new BooleanSetting("Health Rect", true, () -> EntityESP.espMode.currentMode.equals("2D"));
    public BooleanSetting hpDetails = new BooleanSetting("Health Details", true, () -> EntityESP.espMode.currentMode.equals("2D"));
    public BooleanSetting triangleESP;
    public BooleanSetting wireFrame;
    public static BooleanSetting glow;
    public static NumberSetting glowRadius;
    public ListSetting healcolorMode = new ListSetting("Color Health Mode", "Custom", () -> this.healRect.getCurrentValue() && EntityESP.espMode.currentMode.equals("2D"), "Astolfo", "Rainbow", "Client", "Custom");
    private final ColorSetting healColor = new ColorSetting("Health Border Color", -1, () -> this.healcolorMode.currentMode.equals("Custom") && !EntityESP.espMode.currentMode.equals("Box"));
    public ListSetting csgoMode;
    public ListSetting colorMode = new ListSetting("Color Box Mode", "Custom", () -> EntityESP.espMode.currentMode.equals("Box") || EntityESP.espMode.currentMode.equals("2D"), "Astolfo", "Rainbow", "Client", "Custom");
    public ListSetting triangleMode = new ListSetting("Triangle Mode", "Custom", () -> this.triangleESP.getCurrentValue(), "Astolfo", "Rainbow", "Client", "Custom");
    public ListSetting wireframeMode = new ListSetting("Wireframe Mode", "Custom", () -> this.wireFrame.getCurrentValue(), "Astolfo", "Rainbow", "Client", "Custom");
    public NumberSetting wireframeWidth = new NumberSetting("Wireframe Width", 1.0f, 0.1f, 5.0f, 0.1f, () -> this.wireFrame.getCurrentValue());
    private final ColorSetting wireFrameColor;
    public static ListSetting glowMode;
    public static ColorSetting glowColor;
    public NumberSetting xOffset;
    public NumberSetting yOffset;
    public NumberSetting size;
    public BooleanSetting itemESP;
    public ListSetting itemColorMode;
    public BooleanSetting itemsHighlight;
    public ColorSetting itemsHighlightColor;
    public BooleanSetting itemBoxes;
    public BooleanSetting itemTags;
    public BooleanSetting itemCircles;
    public NumberSetting segments;
    public BooleanSetting renderHP;
    public BooleanSetting mobESP;
    public static Entity clipEntity;

    public EntityESP() {
        super("EntityESP", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0438\u0433\u0440\u043e\u043a\u043e\u0432, \u043d\u0438\u043a \u0438 \u0438\u0445 \u0437\u0434\u043e\u0440\u043e\u0432\u044c\u0435 \u0441\u043a\u0432\u043e\u0437\u044c \u0441\u0442\u0435\u043d\u044b", Type.Visuals);
        espMode = new ListSetting("ESP Mode", "2D", () -> true, "2D", "Box");
        this.csgoMode = new ListSetting("Border Mode", "Box", () -> EntityESP.espMode.currentMode.equals("2D"), "Box", "Corner");
        this.border = new BooleanSetting("Border Rect", true, () -> EntityESP.espMode.currentMode.equals("2D"));
        tags = new BooleanSetting("Render Tags", true, () -> EntityESP.espMode.currentMode.equals("2D"));
        this.tagsBackground = new BooleanSetting("Tags Background", true, tags::getCurrentValue);
        this.armor = new BooleanSetting("Render Armor", true, () -> EntityESP.espMode.currentMode.equals("2D"));
        this.colorEsp = new ColorSetting("ESP Color", new Color(0xFFFFFF).getRGB(), () -> !this.colorMode.currentMode.equals("Client") && (EntityESP.espMode.currentMode.equals("2D") || EntityESP.espMode.currentMode.equals("Box")));
        this.friendHighlight = new BooleanSetting("Friend Highlight", false, () -> true);
        this.fullBox = new BooleanSetting("Full Box", false, () -> EntityESP.espMode.currentMode.equals("Box"));
        this.mobESP = new BooleanSetting("Mob ESP", false, () -> true);
        this.itemColorMode = new ListSetting("Item Color-Box Mode", "Custom", () -> this.itemESP.getCurrentValue(), "Astolfo", "Rainbow", "Client", "Custom");
        this.colorItemEsp = new ColorSetting("Item Box Color", Color.WHITE.getRGB(), () -> this.itemColorMode.currentMode.equals("Custom"));
        this.itemsHighlight = new BooleanSetting("Items Highlight", false, () -> true);
        this.itemsHighlightColor = new ColorSetting("Items Highlight Color", Color.WHITE.getRGB(), () -> this.itemsHighlight.getCurrentValue() && this.itemESP.getCurrentValue());
        this.itemESP = new BooleanSetting("Item ESP", true, () -> true);
        this.itemBoxes = new BooleanSetting("Item Boxes", true, () -> this.itemESP.getCurrentValue());
        this.itemTags = new BooleanSetting("Item Tags", true, () -> this.itemESP.getCurrentValue());
        this.itemCircles = new BooleanSetting("Item Circles", false, () -> this.itemESP.getCurrentValue());
        this.segments = new NumberSetting("Circle Segments", 50.0f, 3.0f, 50.0f, 1.0f, () -> this.itemESP.getCurrentValue() && this.itemCircles.getCurrentValue());
        this.wireFrame = new BooleanSetting("Wireframe", false, () -> true);
        this.wireFrameColor = new ColorSetting("Wireframe Color", Color.WHITE.getRGB(), () -> this.wireFrame.getCurrentValue() && this.wireframeMode.currentMode.equals("Custom"));
        glow = new BooleanSetting("Glow", false, () -> true);
        glowRadius = new NumberSetting("Glow Radius", 5.0f, 1.0f, 10.0f, 1.0f, () -> glow.getCurrentValue());
        glowColor = new ColorSetting("Glow Color", Color.WHITE.getRGB(), () -> glow.getCurrentValue() && EntityESP.glowMode.currentMode.equals("Custom"));
        this.triangleESP = new BooleanSetting("Triangle ESP", false, () -> true);
        this.triangleColor = new ColorSetting("Triangle Color", -1, () -> this.triangleESP.getCurrentValue() && this.triangleMode.currentMode.equals("Custom"));
        this.xOffset = new NumberSetting("Triangle XOffset", 10.0f, 0.0f, 50.0f, 5.0f, () -> this.triangleESP.getCurrentValue());
        this.yOffset = new NumberSetting("Triangle YOffset", 0.0f, 0.0f, 50.0f, 5.0f, () -> this.triangleESP.getCurrentValue());
        this.size = new NumberSetting("Triangle Size", 5.0f, 1.0f, 20.0f, 1.0f, () -> this.triangleESP.getCurrentValue());
        this.addSettings(espMode, this.csgoMode, this.colorMode, this.healcolorMode, this.healColor, this.border, this.fullBox, this.healRect, this.hpDetails, this.armor, tags, this.renderHP, this.tagsBackground, glow, glowMode, glowColor, glowRadius, this.wireFrame, this.wireframeMode, this.wireFrameColor, this.wireframeWidth, this.triangleESP, this.triangleMode, this.triangleColor, this.xOffset, this.yOffset, this.size, this.colorEsp, this.friendHighlight, this.mobESP, this.itemESP, this.itemColorMode, this.itemsHighlight, this.itemsHighlightColor, this.colorItemEsp, this.itemBoxes, this.itemTags, this.itemCircles, this.segments);
    }

    private void drawScaledString(String text, double x, double y, double scale, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, x);
        GlStateManager.scale(scale, scale, scale);
        MCFontRenderer.drawStringWithOutline(EntityESP.mc.fontRendererObj, text, 0.0f, 0.0f, color);
        GlStateManager.popMatrix();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(espMode.getOptions() + " " + this.csgoMode.getOptions());
        if (glow.getCurrentValue()) {
            for (EntityPlayer entityPlayer : EntityESP.mc.world.playerEntities) {
                entityPlayer.setGlowing(true);
            }
        } else {
            for (EntityPlayer entityPlayer : EntityESP.mc.world.playerEntities) {
                entityPlayer.setGlowing(false);
            }
        }
    }

    @Override
    public void onDisable() {
        for (EntityPlayer entityPlayer : EntityESP.mc.world.playerEntities) {
            entityPlayer.setGlowing(false);
        }
        super.onDisable();
    }

    @EventTarget
    public void onPlayerModel(EventPlayerModel event) {
        if (!this.wireFrame.getCurrentValue()) {
            return;
        }
        if (!this.isValid(event.getEntity())) {
            return;
        }
        int oneColor = this.wireFrameColor.getColor();
        int color = 0;
        switch (this.wireframeMode.currentMode) {
            case "Client": {
                color = ClientHelper.getClientColor().getRGB();
                break;
            }
            case "Custom": {
                color = oneColor;
                break;
            }
            case "Astolfo": {
                color = PaletteHelper.astolfo(5000.0f, 1).getRGB();
                break;
            }
            case "Rainbow": {
                color = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
            }
        }
        GlStateManager.pushMatrix();
        GL11.glPushAttrib(1048575);
        GL11.glPolygonMode(1032, 6913);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glEnable(3042);
        GlStateManager.blendFunc(770, 771);
        RenderHelper.color(color);
        GlStateManager.glLineWidth(this.wireframeWidth.getCurrentValue());
        event.getModelBase().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());
        GL11.glEnable(2929);
        GL11.glPopAttrib();
        GlStateManager.popMatrix();
    }

    @EventTarget
    public void onRender3D(EventRender3D event3D) {
        if (!this.getState()) {
            return;
        }
        Color onecolor1 = new Color(this.colorEsp.getColor());
        Color c1 = new Color(onecolor1.getRed(), onecolor1.getGreen(), onecolor1.getBlue(), 255);
        Color color = null;
        if (EntityESP.espMode.currentMode.equals("Box")) {
            GlStateManager.pushMatrix();
            for (Entity entity : EntityESP.mc.world.loadedEntityList) {
                if (!(entity instanceof EntityPlayer) || entity == EntityESP.mc.player && EntityESP.mc.gameSettings.thirdPersonView == 0) continue;
                switch (this.colorMode.currentMode) {
                    case "Client": {
                        color = ClientHelper.getClientColor();
                        break;
                    }
                    case "Custom": {
                        color = c1;
                        break;
                    }
                    case "Astolfo": {
                        color = PaletteHelper.astolfo(false, (int)entity.height);
                        break;
                    }
                    case "Rainbow": {
                        color = PaletteHelper.rainbow(300, 1.0f, 1.0f);
                    }
                }
                RenderHelper.drawEntityBox(entity, color, this.fullBox.getCurrentValue(), this.fullBox.getCurrentValue() ? 0.15f : 0.9f);
            }
            GlStateManager.popMatrix();
        }
        if (this.itemCircles.getCurrentValue()) {
            int oneColor = this.colorItemEsp.getColor();
            int color2 = 0;
            switch (this.itemColorMode.currentMode) {
                case "Client": {
                    color2 = ClientHelper.getClientColor().getRGB();
                    break;
                }
                case "Custom": {
                    color2 = oneColor;
                    break;
                }
                case "Astolfo": {
                    color2 = PaletteHelper.astolfo(5000.0f, 1).getRGB();
                    break;
                }
                case "Rainbow": {
                    color2 = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                }
            }
            for (Entity entity : EntityESP.mc.world.loadedEntityList) {
                if (entity == null || !(entity instanceof EntityItem)) continue;
                RenderHelper.drawCircle3D(entity, (double)entity.width + 0.1 - 0.001, event3D.getPartialTicks(), (int)this.segments.getCurrentValue(), 3.0f, Color.BLACK.getRGB());
                RenderHelper.drawCircle3D(entity, (double)entity.width + 0.1 + 0.001, event3D.getPartialTicks(), (int)this.segments.getCurrentValue(), 3.0f, Color.BLACK.getRGB());
                RenderHelper.drawCircle3D(entity, (double)entity.width + 0.1, event3D.getPartialTicks(), (int)this.segments.getCurrentValue(), 2.0f, color2);
            }
        }
    }

    @EventTarget
    public void onRenderTriangle(EventRender2D eventRender2D) {
        if (this.triangleESP.getCurrentValue()) {
            Color onecolor = new Color(this.triangleColor.getColor());
            Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
            int color = 0;
            switch (this.triangleMode.currentMode) {
                case "Client": {
                    color = ClientHelper.getClientColor().getRGB();
                    break;
                }
                case "Custom": {
                    color = c.getRGB();
                    break;
                }
                case "Astolfo": {
                    color = PaletteHelper.astolfo(false, 1).getRGB();
                    break;
                }
                case "Rainbow": {
                    color = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                }
            }
            ScaledResolution sr = new ScaledResolution(mc);
            float size = 50.0f;
            float xOffset = (float)sr.getScaledWidth() / 2.0f - 24.5f;
            float yOffset = (float)sr.getScaledHeight() / 2.0f - 25.2f;
            for (Entity entity : EntityESP.mc.world.loadedEntityList) {
                if (entity == null || !(entity instanceof EntityPlayer) || entity == EntityESP.mc.player || ((EntityLivingBase)entity).getHealth() < 0.0f) continue;
                GlStateManager.pushMatrix();
                GlStateManager.disableBlend();
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)EntityESP.mc.timer.renderPartialTicks - EntityESP.mc.getRenderManager().renderPosX;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)EntityESP.mc.timer.renderPartialTicks - EntityESP.mc.getRenderManager().renderPosZ;
                double cos = Math.cos((double)EntityESP.mc.player.rotationYaw * (Math.PI / 180));
                double sin = Math.sin((double)EntityESP.mc.player.rotationYaw * (Math.PI / 180));
                double rotY = -(z * cos - x * sin);
                double rotX = -(x * cos + z * sin);
                double rotX1 = 0.0 - rotX;
                double rotY1 = 0.0 - rotY;
                if (MathHelper.sqrt(rotX1 * rotX1 + rotY1 * rotY1) < size / 2.0f - 4.0f) {
                    float angle = (float)(Math.atan2(rotY - 0.0, rotX - 0.0) * 180.0 / Math.PI);
                    double xPos = (double)(size / 2.0f) * Math.cos(Math.toRadians(angle)) + (double)xOffset + (double)(size / 2.0f);
                    double y = (double)(size / 2.0f) * Math.sin(Math.toRadians(angle)) + (double)yOffset + (double)(size / 2.0f);
                    GlStateManager.translate(xPos, y, 0.0);
                    GlStateManager.rotate(angle, 0.0f, 0.0f, 1.0f);
                    RenderHelper.drawTriangle(this.xOffset.getCurrentValue(), this.yOffset.getCurrentValue(), this.size.getCurrentValue() + 1.0f, 90.0f, new Color(5, 5, 5, 150).getRGB());
                    RenderHelper.drawTriangle(this.xOffset.getCurrentValue(), this.yOffset.getCurrentValue(), this.size.getCurrentValue(), 90.0f, color);
                    RenderHelper.renderBlurredShadow(new Color(color).brighter(), (double)((int)this.xOffset.getCurrentValue() - 4), -3.0, (double)((int)this.size.getCurrentValue()), (double)((int)this.size.getCurrentValue()), 13);
                }
                GlStateManager.popMatrix();
            }
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        GlStateManager.pushMatrix();
        float partialTicks = EntityESP.mc.timer.renderPartialTicks;
        int scaleFactor = event.getResolution().getScaleFactor();
        double scaling = (double)scaleFactor / Math.pow(scaleFactor, 2.0);
        GlStateManager.scale(scaling, scaling, scaling);
        int black = this.black;
        float scale = 1.0f;
        float upscale = 1.0f / scale;
        for (Entity entity : EntityESP.mc.world.loadedEntityList) {
            boolean living;
            if (entity == null) continue;
            Color onecolor = new Color(this.colorEsp.getColor());
            Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
            int color = 0;
            switch (this.colorMode.currentMode) {
                case "Client": {
                    color = ClientHelper.getClientColor().getRGB();
                    break;
                }
                case "Custom": {
                    color = c.getRGB();
                    break;
                }
                case "Astolfo": {
                    color = PaletteHelper.astolfo(false, 1).getRGB();
                    break;
                }
                case "Rainbow": {
                    color = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                }
            }
            if (Celestial.instance.friendManager.isFriend(entity.getName()) && this.friendHighlight.getCurrentValue()) {
                color = Color.GREEN.getRGB();
            }
            if (!this.isValid(entity) || !RenderHelper.isInViewFrustum(entity)) continue;
            double x = RenderHelper.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
            double y = RenderHelper.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
            double z = RenderHelper.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
            double width = (double)entity.width / 1.5;
            double height = (double)entity.height + (entity.isSneaking() || entity == EntityESP.mc.player && EntityESP.mc.player.isSneaking() ? -0.3 : (!(!Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() || CustomModel.onlyMe.getCurrentValue() && entity != EntityESP.mc.player || !CustomModel.modelMode.currentMode.equals("Crab") && !CustomModel.modelMode.currentMode.equals("Chinchilla") && !CustomModel.modelMode.currentMode.equals("Red Panda")) ? -0.5 : 0.2));
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
            Vector3d[] vectors = new Vector3d[]{new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ)};
            EntityESP.mc.entityRenderer.setupCameraTransform(partialTicks, 0);
            Vector4d position = null;
            for (Vector3d vector : vectors) {
                vector = this.project2D(scaleFactor, vector.x - EntityESP.mc.getRenderManager().renderPosX, vector.y - EntityESP.mc.getRenderManager().renderPosY, vector.z - EntityESP.mc.getRenderManager().renderPosZ);
                if (vector == null || !(vector.z > 0.0) || !(vector.z < 1.0)) continue;
                if (position == null) {
                    position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                }
                position.x = Math.min(vector.x, position.x);
                position.y = Math.min(vector.y, position.y);
                position.z = Math.max(vector.x, position.z);
                position.w = Math.max(vector.y, position.w);
            }
            if (position == null) continue;
            EntityESP.mc.entityRenderer.setupOverlayRendering();
            double posX = position.x;
            double posY = position.y;
            double endPosX = position.z;
            double endPosY = position.w;
            double finalValue = 0.5;
            if (!(entity instanceof EntityItem)) {
                if (EntityESP.espMode.currentMode.equalsIgnoreCase("2D") && this.csgoMode.currentMode.equalsIgnoreCase("Box") && this.border.getCurrentValue()) {
                    RectHelper.drawRect(posX - 1.0, posY, posX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX - 1.0, posY - finalValue, endPosX + finalValue, posY + finalValue + finalValue, black);
                    RectHelper.drawRect(endPosX - finalValue - finalValue, posY, endPosX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX - 1.0, endPosY - finalValue - finalValue, endPosX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX - finalValue, posY, posX + finalValue - finalValue, endPosY, color);
                    RectHelper.drawRect(posX, endPosY - finalValue, endPosX, endPosY, color);
                    RectHelper.drawRect(posX - finalValue, posY, endPosX, posY + finalValue, color);
                    RectHelper.drawRect(endPosX - finalValue, posY, endPosX, endPosY, color);
                } else if (EntityESP.espMode.currentMode.equalsIgnoreCase("2D") && this.csgoMode.currentMode.equalsIgnoreCase("Corner") && this.border.getCurrentValue()) {
                    RectHelper.drawRect(posX + finalValue, posY, posX - 1.0, posY + (endPosY - posY) / 4.0 + finalValue, black);
                    RectHelper.drawRect(posX - 1.0, endPosY, posX + finalValue, endPosY - (endPosY - posY) / 4.0 - finalValue, black);
                    RectHelper.drawRect(posX - 1.0, posY - finalValue, posX + (endPosX - posX) / 3.0 + finalValue, posY + 1.0, black);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0 - 0.0, posY - finalValue, endPosX, posY + 1.0, black);
                    RectHelper.drawRect(endPosX - 1.0, posY, endPosX + finalValue, posY + (endPosY - posY) / 4.0 + finalValue, black);
                    RectHelper.drawRect(endPosX - 1.0, endPosY, endPosX + finalValue, endPosY - (endPosY - posY) / 4.0 - finalValue, black);
                    RectHelper.drawRect(posX - 1.0, endPosY - 1.0, posX + (endPosX - posX) / 3.0 + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0 - finalValue, endPosY - 1.0, endPosX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX, posY, posX - finalValue, posY + (endPosY - posY) / 4.0, color);
                    RectHelper.drawRect(posX, endPosY, posX - finalValue, endPosY - (endPosY - posY) / 4.0, color);
                    RectHelper.drawRect(posX - finalValue, posY, posX + (endPosX - posX) / 3.0, posY + finalValue, color);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0, posY, endPosX, posY + finalValue, color);
                    RectHelper.drawRect(endPosX - finalValue, posY, endPosX, posY + (endPosY - posY) / 4.0, color);
                    RectHelper.drawRect(endPosX - finalValue, endPosY, endPosX, endPosY - (endPosY - posY) / 4.0, color);
                    RectHelper.drawRect(posX, endPosY - finalValue, posX + (endPosX - posX) / 3.0, endPosY, color);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0, endPosY - finalValue, endPosX - finalValue, endPosY, color);
                }
            }
            if (living = entity instanceof EntityLivingBase) {
                String name;
                EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
                float targetHP = entityLivingBase.getHealth();
                targetHP = MathHelper.clamp(targetHP, 0.0f, 24.0f);
                float maxHealth = entityLivingBase.getMaxHealth();
                double hpPercentage = targetHP / maxHealth;
                double hpHeight2 = (endPosY - posY) * hpPercentage;
                float armorValue = entityLivingBase.getTotalArmorValue();
                if (living && this.armor.getCurrentValue() && !EntityESP.espMode.currentMode.equals("Box") && entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer)entity;
                    if (EntityESP.mc.player.getDistanceToEntity(player) < 10.0f) {
                        ItemStack stack3;
                        ItemStack stack2;
                        double ydiff = (endPosY - posY) / 4.0;
                        ItemStack stack = player.getEquipmentInSlot(4);
                        if (stack != null) {
                            double diff1 = posY + ydiff - 1.0 - (posY + 2.0);
                            double percent = 1.0 - (double)stack.getItemDamage() / (double)stack.getMaxDamage();
                            RenderHelper.renderItem(stack, (int)endPosX + 4, (int)posY + (int)ydiff - 1 - (int)(diff1 / 2.0) - 18);
                        }
                        if ((stack2 = player.getEquipmentInSlot(3)) != null) {
                            double diff1 = posY + ydiff * 2.0 - (posY + ydiff + 2.0);
                            String stackname = stack.getDisplayName().equalsIgnoreCase("Air") ? "0" : (!(stack2.getItem() instanceof ItemArmor) ? stack2.getDisplayName() : stack2.getMaxDamage() - stack2.getItemDamage() + "");
                            RenderHelper.renderItem(stack2, (int)endPosX + 4, (int)(posY + ydiff * 2.0) - (int)(diff1 / 2.0) - 18);
                        }
                        if ((stack3 = player.getEquipmentInSlot(2)) != null) {
                            double diff1 = posY + ydiff * 3.0 - (posY + ydiff * 2.0 + 2.0);
                            RenderHelper.renderItem(stack3, (int)endPosX + 4, (int)(posY + ydiff * 3.0) - (int)(diff1 / 2.0) - 18);
                        }
                        ItemStack stack4 = player.getEquipmentInSlot(1);
                        double diff1 = posY + ydiff * 4.0 - (posY + ydiff * 3.0 + 2.0);
                        RenderHelper.renderItem(stack4, (int)endPosX + 4, (int)(posY + ydiff * 4.0) - (int)(diff1 / 2.0) - 18);
                    }
                    double armorWidth = (endPosX - posX) * (double)armorValue / 20.0;
                    RectHelper.drawRect(posX - 0.5, endPosY + 1.0, posX - 0.5 + endPosX - posX, endPosY + 3.0, new Color(0, 0, 0, 120).getRGB());
                    if (armorValue > 0.0f) {
                        RectHelper.drawBorderedRect1(posX - 0.5, endPosY + 1.0, armorWidth, 2.0, 0.5, black, Color.CYAN.getRGB());
                    }
                }
                if (Celestial.instance.featureManager.getFeatureByClass(ClipHelper.class).getState() && entity != EntityESP.mc.player && entity instanceof EntityPlayer && RotationHelper.isLookingAtEntity(false, EntityESP.mc.player.rotationYaw, EntityESP.mc.player.rotationPitch, 0.15f, 0.15f, 0.15f, entity, ClipHelper.maxDistance.getCurrentValue())) {
                    BlockPos distanceToY = new BlockPos(0.0, (double)((int)EntityESP.mc.player.posY) - entity.posY, 0.0);
                    int findToClip = (int)entity.posY;
                    if (EntityESP.mc.gameSettings.thirdPersonView == 0) {
                        if (RenderHelper.isInViewFrustum(entity)) {
                            float diff = (float)(endPosX - posX) / 2.0f;
                            float textWidth = (float)EntityESP.mc.fontRendererObj.getStringWidth("Target \u00a77" + distanceToY.getY() + "m") * scale;
                            float tagX = (float)((posX + (double)diff - (double)textWidth / 2.0) * (double)upscale);
                            EntityESP.mc.fontRendererObj.drawStringWithShadow("Target \u00a77" + distanceToY.getY() + "m", tagX, (float)endPosY, -1);
                            clipEntity = entity;
                        }
                        if (Mouse.isButtonDown(2) && !ClipHelper.sunriseBypass.getCurrentValue()) {
                            EntityESP.mc.player.setPositionAndUpdate(EntityESP.mc.player.posX, findToClip, EntityESP.mc.player.posZ);
                            ChatHelper.addChatMessage("Clip to entity " + (Object)((Object)ChatFormatting.RED) + entity.getName() + (Object)((Object)ChatFormatting.WHITE) + " on Y " + (Object)((Object)ChatFormatting.RED) + findToClip);
                        }
                    }
                }
                if (!(targetHP > 0.0f)) continue;
                if (this.hpDetails.getCurrentValue() && !EntityESP.espMode.currentMode.equals("Box")) {
                    String healthDisplay = MathematicHelper.round(((EntityLivingBase)entity).getHealth(), 1) + " \u00a7c\u2764";
                    this.drawScaledString(healthDisplay, posX - 6.0 - (double)((float)EntityESP.mc.fontRendererObj.getStringWidth(healthDisplay) * 0.5f), endPosY - hpHeight2, 0.5, -1);
                }
                if (this.healRect.getCurrentValue() && !EntityESP.espMode.currentMode.equals("Box")) {
                    int colorHeal = 0;
                    switch (this.healcolorMode.currentMode) {
                        case "Client": {
                            colorHeal = ClientHelper.getClientColor().getRGB();
                            break;
                        }
                        case "Custom": {
                            colorHeal = this.healColor.getColor();
                            break;
                        }
                        case "Astolfo": {
                            colorHeal = PaletteHelper.astolfo(false, (int)entity.height).getRGB();
                            break;
                        }
                        case "Rainbow": {
                            colorHeal = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                        }
                    }
                    RectHelper.drawRect(posX - 4.0, posY - 0.5, posX - 1.5, endPosY + 0.5, new Color(0, 0, 0, 125).getRGB());
                    RectHelper.drawRect(posX - 3.5, endPosY, posX - 2.0, endPosY - hpHeight2, colorHeal);
                }
                if (!tags.getCurrentValue() || entity instanceof EntityPlayer && Celestial.instance.featureManager.getFeatureByClass(NameTags.class).getState()) continue;
                float scaledHeight = 10.0f;
                String string = Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getCurrentValue() && entity == EntityESP.mc.player ? "Protected" : (name = Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getCurrentValue() && entity != EntityESP.mc.player ? "Protected" : entity.getName());
                if (Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.friendNames.getCurrentValue() && Celestial.instance.friendManager.isFriend(entity.getName())) {
                    name = "Protected";
                }
                String health = MathematicHelper.round(((EntityLivingBase)entity).getHealth(), 0) + " HP";
                float diff = (float)(endPosX - posX) / 2.0f;
                float textWidth = (float)EntityESP.mc.fontRenderer.getStringWidth(name + " \u00a77[" + (Object)((Object)TextFormatting.RED) + health + "\u00a77]") * scale;
                float tagX = (float)((posX + (double)diff - (double)textWidth / 2.0) * (double)upscale);
                float tagY = (float)(posY * (double)upscale) - scaledHeight;
                GlStateManager.pushMatrix();
                GlStateManager.scale(scale, scale, scale);
                GlStateManager.translate(0.0f, -4.0f, 0.0f);
                if (this.tagsBackground.getCurrentValue()) {
                    RectHelper.drawRect(tagX - 2.0f, tagY - 2.0f, (double)(tagX + textWidth * upscale) + 2.0, tagY + 9.0f, new Color(0, 0, 0, 125).getRGB());
                }
                EntityESP.mc.fontRenderer.drawStringWithShadow(name + " \u00a77[" + (Object)((Object)TextFormatting.RED) + health + "\u00a77]", tagX, tagY, -1);
                GlStateManager.popMatrix();
                continue;
            }
            if (!(entity instanceof EntityItem)) continue;
            EntityItem entityItem = (EntityItem)entity;
            if (!this.itemESP.getCurrentValue()) continue;
            double dif = (endPosX - posX) / 2.0;
            double textWidth = (float)EntityESP.mc.fontRenderer.getStringWidth(((EntityItem)entity).getItem().getDisplayName()) * scale;
            float tagX = (float)((posX + dif - textWidth / 2.0) * (double)upscale);
            int oneColor = this.colorItemEsp.getColor();
            int color1 = 0;
            switch (this.itemColorMode.currentMode) {
                case "Client": {
                    color1 = ClientHelper.getClientColor().getRGB();
                    break;
                }
                case "Custom": {
                    color1 = oneColor;
                    break;
                }
                case "Astolfo": {
                    color1 = PaletteHelper.astolfo(5000.0f, 1).getRGB();
                    break;
                }
                case "Rainbow": {
                    color1 = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                }
            }
            if (this.itemsHighlight.getCurrentValue() && (entityItem.getItem().getItem() instanceof ItemSword || entityItem.getItem().getItem() instanceof ItemPotion || entityItem.getItem().getItem() instanceof ItemArmor || entityItem.getItem().getItem() instanceof ItemFireworkCharge || entityItem.getItem().getItem() instanceof ItemAppleGold)) {
                color1 = this.itemsHighlightColor.getColor();
            }
            if (this.itemBoxes.getCurrentValue()) {
                if (this.csgoMode.currentMode.equalsIgnoreCase("Box")) {
                    RectHelper.drawRect(posX - 1.0, posY, posX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX - 1.0, posY - finalValue, endPosX + finalValue, posY + finalValue + finalValue, black);
                    RectHelper.drawRect(endPosX - finalValue - finalValue, posY, endPosX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX - 1.0, endPosY - finalValue - finalValue, endPosX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX - finalValue, posY, posX + finalValue - finalValue, endPosY, color1);
                    RectHelper.drawRect(posX, endPosY - finalValue, endPosX, endPosY, color1);
                    RectHelper.drawRect(posX - finalValue, posY, endPosX, posY + finalValue, color1);
                    RectHelper.drawRect(endPosX - finalValue, posY, endPosX, endPosY, color1);
                } else if (this.csgoMode.currentMode.equalsIgnoreCase("Corner")) {
                    RectHelper.drawRect(posX + finalValue, posY, posX - 1.0, posY + (endPosY - posY) / 4.0 + finalValue, black);
                    RectHelper.drawRect(posX - 1.0, endPosY, posX + finalValue, endPosY - (endPosY - posY) / 4.0 - finalValue, black);
                    RectHelper.drawRect(posX - 1.0, posY - finalValue, posX + (endPosX - posX) / 3.0 + finalValue, posY + 1.0, black);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0 - 0.0, posY - finalValue, endPosX, posY + 1.0, black);
                    RectHelper.drawRect(endPosX - 1.0, posY, endPosX + finalValue, posY + (endPosY - posY) / 4.0 + finalValue, black);
                    RectHelper.drawRect(endPosX - 1.0, endPosY, endPosX + finalValue, endPosY - (endPosY - posY) / 4.0 - finalValue, black);
                    RectHelper.drawRect(posX - 1.0, endPosY - 1.0, posX + (endPosX - posX) / 3.0 + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0 - finalValue, endPosY - 1.0, endPosX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX, posY, posX - finalValue, posY + (endPosY - posY) / 4.0, color1);
                    RectHelper.drawRect(posX, endPosY, posX - finalValue, endPosY - (endPosY - posY) / 4.0, color1);
                    RectHelper.drawRect(posX - finalValue, posY, posX + (endPosX - posX) / 3.0, posY + finalValue, color1);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0, posY, endPosX, posY + finalValue, color1);
                    RectHelper.drawRect(endPosX - finalValue, posY, endPosX, posY + (endPosY - posY) / 4.0, color1);
                    RectHelper.drawRect(endPosX - finalValue, endPosY, endPosX, endPosY - (endPosY - posY) / 4.0, color1);
                    RectHelper.drawRect(posX, endPosY - finalValue, posX + (endPosX - posX) / 3.0, endPosY, color1);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0, endPosY - finalValue, endPosX - finalValue, endPosY, color1);
                }
            }
            if (!this.itemTags.getCurrentValue()) continue;
            EntityESP.mc.fontRenderer.drawStringWithShadow(((EntityItem)entity).getItem().getDisplayName(), tagX + 1.0f, (float)(posY - 10.0), color1);
        }
        GlStateManager.popMatrix();
        GL11.glEnable(2929);
        GlStateManager.enableBlend();
        EntityESP.mc.entityRenderer.setupOverlayRendering();
    }

    @EventTarget
    public void onRenderName(EventRenderPlayerName eventRenderName) {
        if (!this.getState()) {
            return;
        }
        eventRenderName.setCancelled(tags.getCurrentValue());
    }

    private boolean isValid(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity instanceof EntityItem && !this.itemESP.getCurrentValue()) {
            return false;
        }
        if (EntityESP.mc.gameSettings.thirdPersonView == 0 && entity == EntityESP.mc.player) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (entity instanceof EntityAnimal && !this.mobESP.getCurrentValue()) {
            return false;
        }
        if (entity instanceof EntityPlayer) {
            return true;
        }
        if (entity instanceof EntityArmorStand) {
            return false;
        }
        if (entity instanceof IAnimals && !this.mobESP.getCurrentValue()) {
            return false;
        }
        if (entity instanceof EntityItemFrame) {
            return false;
        }
        if (entity instanceof EntityArrow || entity instanceof EntitySpectralArrow) {
            return false;
        }
        if (entity instanceof EntityMinecart) {
            return false;
        }
        if (entity instanceof EntityBoat) {
            return false;
        }
        if (entity instanceof EntityDragonFireball) {
            return false;
        }
        if (entity instanceof EntityXPOrb) {
            return false;
        }
        if (entity instanceof EntityMinecartChest) {
            return false;
        }
        if (entity instanceof EntityTNTPrimed) {
            return false;
        }
        if (entity instanceof EntityMinecartTNT) {
            return false;
        }
        if (entity instanceof EntityVillager && !this.mobESP.getCurrentValue()) {
            return false;
        }
        if (entity instanceof EntityExpBottle) {
            return false;
        }
        if (entity instanceof EntityLightningBolt) {
            return false;
        }
        if (entity instanceof EntityPotion) {
            return false;
        }
        if (!(!(entity instanceof Entity) || entity instanceof EntityMob || entity instanceof EntityWaterMob || entity instanceof IAnimals || entity instanceof EntityLiving || entity instanceof EntityItem)) {
            return false;
        }
        if ((entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityDragon || entity instanceof EntityGolem) && !this.mobESP.getCurrentValue()) {
            return false;
        }
        return entity != EntityESP.mc.player;
    }

    private Vector3d project2D(int scaleFactor, double x, double y, double z) {
        float xPos = (float)x;
        float yPos = (float)y;
        float zPos = (float)z;
        IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
        FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat(2982, modelview);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector)) {
            return new Vector3d(vector.get(0) / (float)scaleFactor, ((float)Display.getHeight() - vector.get(1)) / (float)scaleFactor, vector.get(2));
        }
        return null;
    }

    static {
        glowMode = new ListSetting("Glow Mode", "Custom", () -> glow.getCurrentValue(), "Astolfo", "Rainbow", "Client", "Custom");
    }
}

