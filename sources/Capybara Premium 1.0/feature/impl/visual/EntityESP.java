package fun.expensive.client.feature.impl.visual;

import com.mojang.realmsclient.gui.ChatFormatting;
import fun.rich.client.Rich;
import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.event.events.impl.render.EventRender2D;
import fun.rich.client.event.events.impl.render.EventRender3D;
import fun.rich.client.event.events.impl.render.EventRenderPlayerName;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.feature.impl.combat.AntiBot;
import fun.rich.client.feature.impl.misc.NameProtect;
import fun.rich.client.ui.font.MCFontRenderer;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ColorSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.math.MathematicHelper;
import fun.rich.client.utils.math.RotationHelper;
import fun.rich.client.utils.render.ClientHelper;
import fun.rich.client.utils.render.ColorUtils;
import fun.rich.client.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.*;
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
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

public class EntityESP
        extends Feature {
    public static ListSetting espMode;
    private final int black = Color.BLACK.getRGB();
    public static ColorSetting colorEsp;
    private final BooleanSetting friendHighlight;
    private final BooleanSetting fullBox;
    private final BooleanSetting armor;
    private final BooleanSetting border;
    public static BooleanSetting healRect = new BooleanSetting("Health Rect", true, () -> espMode.currentMode.equals("2D"));
    public BooleanSetting hpDetails = new BooleanSetting("Health Details", true, () -> espMode.currentMode.equals("2D"));
    public static ListSetting healcolorMode = new ListSetting("Color Health Mode", "Client", () -> healRect.getBoolValue() && espMode.currentMode.equals("2D"), "Astolfo", "Rainbow", "Client", "Custom");
    public static final ColorSetting healColor = new ColorSetting("Health Border Color", -1, () -> healcolorMode.currentMode.equals("Custom") && !espMode.currentMode.equals("Box"));
    public ListSetting csgoMode;
    public static ListSetting colorMode = new ListSetting("Color Box Mode", "Client", () -> espMode.currentMode.equals("Box") || espMode.currentMode.equals("2D"), "Astolfo", "Rainbow", "Client", "Custom");
    public BooleanSetting mobESP;
    public static Entity clipEntity;

    public EntityESP() {
        super("EntityESP", "Показывает игроков, ник и их здоровье сквозь стены", FeatureCategory.Visuals);
        espMode = new ListSetting("ESP Mode", "2D", () -> true, "2D", "Box");
        csgoMode = new ListSetting("Border Mode", "Box", () -> espMode.currentMode.equals("2D"), "Box", "Corner");
        border = new BooleanSetting("Border Rect", true, () -> espMode.currentMode.equals("2D"));
        armor = new BooleanSetting("Render Armor", true, () -> espMode.currentMode.equals("2D"));
        colorEsp = new ColorSetting("ESP Color", new Color(0xFFFFFF).getRGB(), () -> !colorMode.currentMode.equals("Client") && (espMode.currentMode.equals("2D") || espMode.currentMode.equals("Box")));
        friendHighlight = new BooleanSetting("Friend Highlight", false, () -> true);
        fullBox = new BooleanSetting("Full Box", false, () -> espMode.currentMode.equals("Box"));
        mobESP = new BooleanSetting("Mob ESP", false, () -> true);
        addSettings(espMode, csgoMode, colorMode, healcolorMode, healColor, border, fullBox, healRect, hpDetails, armor, colorEsp, friendHighlight, mobESP);
    }

    private void drawScaledString(String text, double x, double y, double scale, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, x);
        GlStateManager.scale(scale, scale, scale);
        MCFontRenderer.drawStringWithOutline(mc.fontRendererObj, text, 0.0f, 0.0f, color);
        GlStateManager.popMatrix();
    }


    @EventTarget
    public void onRender3D(EventRender3D event3D) {
        if (!isEnabled()) {
            return;
        }

        Color color = ClientHelper.getESPColor();

        if (espMode.currentMode.equals("Box")) {
            GlStateManager.pushMatrix();

            for (Entity entity : mc.world.loadedEntityList) {
                if (!(entity instanceof EntityPlayer) || entity == mc.player && mc.gameSettings.thirdPersonView == 0) {
                    continue;
                }

                RenderUtils.drawEntityBox(entity, color, fullBox.getBoolValue(), fullBox.getBoolValue() ? 0.15f : 0.9f);
            }

            GlStateManager.popMatrix();
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        float partialTicks = mc.timer.renderPartialTicks;
        int scaleFactor = event.getResolution().getScaleFactor();
        int black = this.black;

        for (Entity entity : mc.world.loadedEntityList) {

            if (entity == null) {
                continue;
            }
            int color = ClientHelper.getESPColor().getRGB();

            if (Rich.instance.friendManager.isFriend(entity.getName()) && friendHighlight.getBoolValue()) {
                color = Color.GREEN.getRGB();
            }

            if (!isValid(entity) || !RenderUtils.isInViewFrustum(entity)) {
                continue;
            }

            double x = RenderUtils.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
            double y = RenderUtils.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
            double z = RenderUtils.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
            double width = (double) entity.width / 1.5;
            double height = (double) entity.height + (entity.isSneaking() || entity == EntityESP.mc.player && EntityESP.mc.player.isSneaking() ? -0.3 : !(!Rich.instance.featureManager.getFeature(CustomModel.class).isEnabled() || CustomModel.onlyMe.getBoolValue() && entity != EntityESP.mc.player || !CustomModel.modelMode.currentMode.equals("Crab") && !CustomModel.modelMode.currentMode.equals("Amogus") && !CustomModel.modelMode.currentMode.equals("Chinchilla") && !CustomModel.modelMode.currentMode.equals("Red Panda")) ? -0.5 : 0.2);
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
            Vector3d[] vectors = new Vector3d[]{new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ)};
            mc.entityRenderer.setupCameraTransform(partialTicks, 0);
            Vector4d position = null;

            for (Vector3d vector : vectors) {
                vector = project2D(scaleFactor, vector.x - mc.getRenderManager().renderPosX, vector.y - mc.getRenderManager().renderPosY, vector.z - mc.getRenderManager().renderPosZ);

                if (vector == null || !(vector.z > 0.0) || !(vector.z < 1.0)) {
                    continue;
                }

                if (position == null) {
                    position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                }

                position.x = Math.min(vector.x, position.x);
                position.y = Math.min(vector.y, position.y);
                position.z = Math.max(vector.x, position.z);
                position.w = Math.max(vector.y, position.w);
            }

            if (position == null) {
                continue;
            }

            mc.entityRenderer.setupOverlayRendering();
            double posX = position.x;
            double posY = position.y;
            double endPosX = position.z;
            double endPosY = position.w;
            double finalValue = 0.5;

            if (espMode.currentMode.equalsIgnoreCase("2D") && csgoMode.currentMode.equalsIgnoreCase("Box") && border.getBoolValue()) {
                RenderUtils.drawRect(posX - 1.0, posY, posX + finalValue, endPosY + finalValue, black);
                RenderUtils.drawRect(posX - 1.0, posY - finalValue, endPosX + finalValue, posY + finalValue + finalValue, black);
                RenderUtils.drawRect(endPosX - finalValue - finalValue, posY, endPosX + finalValue, endPosY + finalValue, black);
                RenderUtils.drawRect(posX - 1.0, endPosY - finalValue - finalValue, endPosX + finalValue, endPosY + finalValue, black);
                RenderUtils.drawRect(posX - finalValue, posY, posX + finalValue - finalValue, endPosY, color);
                RenderUtils.drawRect(posX, endPosY - finalValue, endPosX, endPosY, color);
                RenderUtils.drawRect(posX - finalValue, posY, endPosX, posY + finalValue, color);
                RenderUtils.drawRect(endPosX - finalValue, posY, endPosX, endPosY, color);
            } else if (espMode.currentMode.equalsIgnoreCase("2D") && csgoMode.currentMode.equalsIgnoreCase("Corner") && border.getBoolValue()) {
                RenderUtils.drawRect(posX + finalValue, posY, posX - 1.0, posY + (endPosY - posY) / 4.0 + finalValue, black);
                RenderUtils.drawRect(posX - 1.0, endPosY, posX + finalValue, endPosY - (endPosY - posY) / 4.0 - finalValue, black);
                RenderUtils.drawRect(posX - 1.0, posY - finalValue, posX + (endPosX - posX) / 3.0 + finalValue, posY + 1.0, black);
                RenderUtils.drawRect(endPosX - (endPosX - posX) / 3.0 - 0.0, posY - finalValue, endPosX, posY + 1.0, black);
                RenderUtils.drawRect(endPosX - 1.0, posY, endPosX + finalValue, posY + (endPosY - posY) / 4.0 + finalValue, black);
                RenderUtils.drawRect(endPosX - 1.0, endPosY, endPosX + finalValue, endPosY - (endPosY - posY) / 4.0 - finalValue, black);
                RenderUtils.drawRect(posX - 1.0, endPosY - 1.0, posX + (endPosX - posX) / 3.0 + finalValue, endPosY + finalValue, black);
                RenderUtils.drawRect(endPosX - (endPosX - posX) / 3.0 - finalValue, endPosY - 1.0, endPosX + finalValue, endPosY + finalValue, black);
                RenderUtils.drawRect(posX, posY, posX - finalValue, posY + (endPosY - posY) / 4.0, color);
                RenderUtils.drawRect(posX, endPosY, posX - finalValue, endPosY - (endPosY - posY) / 4.0, color);
                RenderUtils.drawRect(posX - finalValue, posY, posX + (endPosX - posX) / 3.0, posY + finalValue, color);
                RenderUtils.drawRect(endPosX - (endPosX - posX) / 3.0, posY, endPosX, posY + finalValue, color);
                RenderUtils.drawRect(endPosX - finalValue, posY, endPosX, posY + (endPosY - posY) / 4.0, color);
                RenderUtils.drawRect(endPosX - finalValue, endPosY, endPosX, endPosY - (endPosY - posY) / 4.0, color);
                RenderUtils.drawRect(posX, endPosY - finalValue, posX + (endPosX - posX) / 3.0, endPosY, color);
                RenderUtils.drawRect(endPosX - (endPosX - posX) / 3.0, endPosY - finalValue, endPosX - finalValue, endPosY, color);
            }

            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            float targetHP = entityLivingBase.getHealth();
            targetHP = MathHelper.clamp(targetHP, 0.0f, 24.0f);
            float maxHealth = entityLivingBase.getMaxHealth();
            double hpPercentage = targetHP / maxHealth;
            double hpHeight2 = (endPosY - posY) * hpPercentage;
            int colorHeal = ClientHelper.getHealthColor().getRGB();

            if (hpDetails.getBoolValue() && !espMode.currentMode.equals("Box")) {
                String healthDisplay = String.valueOf(MathematicHelper.round(((EntityLivingBase) entity).getHealth(), 1));
                drawScaledString(healthDisplay, posX - 6.0 - (double) ((float) mc.fontRendererObj.getStringWidth(healthDisplay) * 0.5f), endPosY - hpHeight2, 0.5, colorHeal);
            }

            if (healRect.getBoolValue() && !espMode.currentMode.equals("Box")) {
                RenderUtils.drawRect(posX - 4.0, posY - 0.5, posX - 1.5, endPosY + 0.5, new Color(0, 0, 0, 125).getRGB());
                RenderUtils.drawRect(posX - 3.5, endPosY, posX - 2.0, endPosY - hpHeight2, colorHeal);
            }
        }
    }

    private boolean isValid(Entity entity) {
        if (entity == null) {
            return false;
        }

        if (mc.gameSettings.thirdPersonView == 0 && entity == mc.player) {
            return false;
        }

        if (entity.isDead) {
            return false;
        }

        if (entity instanceof EntityAnimal && !mobESP.getBoolValue()) {
            return false;
        }

        if (entity instanceof EntityPlayer) {
            return true;
        }

        if (entity instanceof EntityArmorStand) {
            return false;
        }

        if (entity instanceof IAnimals && !mobESP.getBoolValue()) {
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

        if (entity instanceof EntityVillager && !mobESP.getBoolValue()) {
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

        if ((entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityDragon || entity instanceof EntityGolem) && !mobESP.getBoolValue()) {
            return false;
        }

        return entity != mc.player;
    }

    private Vector3d project2D(int scaleFactor, double x, double y, double z) {
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

        if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector)) {
            return new Vector3d(vector.get(0) / (float) scaleFactor, ((float) Display.getHeight() - vector.get(1)) / (float) scaleFactor, vector.get(2));
        }

        return null;
    }
}
