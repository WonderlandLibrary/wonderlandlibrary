package me.nyan.flush.module.impl.render;

import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event3D;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.impl.combat.Aura;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ColorSetting;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.notifications.Notification;
import me.nyan.flush.utils.player.PlayerUtils;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import org.apache.commons.io.FilenameUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;

public class ESP extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Outline", "Flush", "Box & BoxOutline", "Box", "BoxOutline", "Image");
    private final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, true),
            players = new BooleanSetting("Players", this, true),
            creatures = new BooleanSetting("Creatures", this, false),
            villagers = new BooleanSetting("Villagers", this, false),
            invisibles = new BooleanSetting("Invisibles", this, false),
            ignoreTeam = new BooleanSetting("Ignore Team", this, false);
    private final ColorSetting color = new ColorSetting("Color", this, 0xFFFF0000);
    private final NumberSetting outlineWidth = new NumberSetting("Outline Width", this, 1, 0.2, 3, 0.1);

    public String path;
    private DynamicTexture image;
    private ResourceLocation imageId;

    private float animationAngle;

    private Aura aura;

    public ESP() {
        super("ESP", Category.RENDER);
    }

    @Override
    public void onEnable() {
        flush.getNotificationManager().show(Notification.Type.ERROR, "ESP", "among us", 3000);

        if (aura == null) {
            aura = flush.getModuleManager().getModule(Aura.class);
        }

        animationAngle = 0;

        try {
            BufferedImage img = ImageIO.read(new File(path));
            if (img == null) {
                flush.getNotificationManager().show(Notification.Type.ERROR, "ESP", "Failed to load ESP image: format unsupported", 3000);
                return;
            }
            image = new DynamicTexture(img);
            imageId = new ResourceLocation("imageesp/" + FilenameUtils.getName(path));
        } catch (IOException e) {
            flush.getNotificationManager().show(Notification.Type.ERROR, "ESP", "Failed to load ESP image: " + e.getMessage(), 3000);
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRender3D(Event3D e) {
        int offset = 0;

        ArrayList<Entity> entities = mc.theWorld.loadedEntityList.stream()
                .sorted(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)))
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.reverse(entities);

        for (Entity entity : entities) {
            if (entity instanceof EntityLivingBase && isValid((EntityLivingBase) entity)) {
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * e.getPartialTicks() -
                        mc.getRenderManager().renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * e.getPartialTicks() -
                        mc.getRenderManager().renderPosY;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * e.getPartialTicks() -
                        mc.getRenderManager().renderPosZ;
                int color = rainbow.getValue() ? ColorUtils.getRainbow(offset / 8, 0.8F, 2) : this.color.getRGB();

                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);

                switch (mode.getValue().toUpperCase()) {
                    case "FLUSH":
                        drawFlushESP(entity, x, y, z, color);
                        animationAngle += 1 / 35F * Flush.getFrameTime();
                        break;

                    case "BOX & BOXOUTLINE":
                        drawESPBox(entity, x, y, z, ColorUtils.alpha(color, 100));
                    case "BOXOUTLINE":
                        drawESPBoxOutline(entity, x, y, z, ColorUtils.brightness(color, 1/0.7F));
                        break;

                    case "BOX":
                        drawESPBox(entity, x, y, z, ColorUtils.alpha(color, 100));
                        break;

                    case "IMAGE":
                        GlStateManager.pushMatrix();
                        GlStateManager.disableDepth();
                        GlStateManager.depthMask(false);
                        GlStateManager.enableTexture2D();

                        GlStateManager.translate(x, y + entity.height, z);
                        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                        GlStateManager.scale(-1, -1, 1);

                        if (image != null && imageId != null) {
                            RenderUtils.drawImage(image, -(entity.width / 2F), 0, entity.width, entity.height, imageId);
                        }

                        GlStateManager.enableDepth();
                        GlStateManager.depthMask(true);
                        GlStateManager.popMatrix();
                        break;
                }

                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                RenderUtils.glColor(-1);

                offset++;
            }
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }

    private void drawESPBoxOutline(Entity entity, double x, double y, double z, int color) {
        AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                entityBoundingBox.minX - entity.posX + x,
                entityBoundingBox.minY - entity.posY + y + 0.05D,
                entityBoundingBox.minZ - entity.posZ + z,
                entityBoundingBox.maxX - entity.posX + x,
                entityBoundingBox.maxY - entity.posY + y + 0.15D,
                entityBoundingBox.maxZ - entity.posZ + z
        );

        GL11.glLineWidth(outlineWidth.getValueFloat());
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        RenderUtils.drawBoundingBoxOutline(axisAlignedBB, color);
    }

    private void drawESPBox(Entity entity, double x, double y, double z, int color) {
        AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                entityBoundingBox.minX - entity.posX + x,
                entityBoundingBox.minY - entity.posY + y + 0.05D,
                entityBoundingBox.minZ - entity.posZ + z,
                entityBoundingBox.maxX - entity.posX + x,
                entityBoundingBox.maxY - entity.posY + y + 0.15D,
                entityBoundingBox.maxZ - entity.posZ + z
        );
        RenderUtils.fillBoundingBox(axisAlignedBB, color);
    }

    private void drawFlushESP(Entity entity, double x, double y, double z, int color) {
        y += 0.05;
        AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox();

        float angle = entity instanceof EntityPlayer ? animationAngle : animationAngle / 20F;

        float animation = (1 + MathHelper.cos(angle / 180F * MathHelper.PI)) * 0.5F;
        double size = Math.max(entityBoundingBox.maxX - entityBoundingBox.minX,
                entityBoundingBox.maxZ - entityBoundingBox.minZ);
        double radius = size * 0.85;
        double entityHeight = entity.height + 0.1;
        double v = entityHeight * animation;

        float direction = angle % 360F / 180F - 1;
        float f = direction < 0 ? animation : animation - 1;
        double height = (entityHeight / 2D) * f;
        if (v + height < 0) {
            height = 0 - v;
        }
        if (v + height > entityHeight) {
            height = entityHeight - v;
        }

        float dx = (float) (mc.thePlayer.posX - entity.posX);
        float dz = (float) (mc.thePlayer.posZ - entity.posZ);
        double distance = MathHelper.sqrt_float(dx * dx + dz * dz);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + v, z);

        glLineWidth(2);
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        glEnable(GL_LINE_SMOOTH);
        GlStateManager.shadeModel(GL_SMOOTH);

        glBegin(GL_TRIANGLE_STRIP);

        double a = 360 / Math.min((radius / distance * 500D * (Config.zoomMode ? 3 : 1)), 180);
        for (double i = -180; i <= 180; i += a) {
            double radians = Math.toRadians(i);
            RenderUtils.glColor(ColorUtils.alpha(color, 160));
            glVertex3d(Math.cos(radians) * radius, 0, Math.sin(radians) * radius);
            RenderUtils.glColor(ColorUtils.alpha(color, 0));
            glVertex3d(Math.cos(radians) * radius, height, Math.sin(radians) * radius);
        }

        RenderUtils.glColor(ColorUtils.alpha(color, 160));
        glVertex3d(Math.cos(Math.toRadians(180)) * radius, 0, Math.sin(Math.toRadians(180)) * radius);
        RenderUtils.glColor(ColorUtils.alpha(color, 0));
        glVertex3d(Math.cos(Math.toRadians(180)) * radius, height, Math.sin(Math.toRadians(180)) * radius);

        glEnd();

        RenderUtils.glColor(color);

        glBegin(GL_LINE_STRIP);
        for (double i = -180; i <= 180; i += a) {
            double radians = Math.toRadians(i);
            glVertex3d(Math.cos(radians) * radius, 0, Math.sin(radians) * radius);
        }
        glVertex3d(Math.cos(Math.toRadians(180)) * radius, 0, Math.sin(Math.toRadians(180)) * radius);
        glEnd();

        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        RenderUtils.glColor(-1);
        GlStateManager.popMatrix();
    }

    public boolean isValid(EntityLivingBase entity) {
        if (!aura.shouldDrawESP(entity)) {
            return false;
        }
        return PlayerUtils.isValid(entity, players.getValue(), creatures.getValue(),
                villagers.getValue(), invisibles.getValue(), ignoreTeam.getValue());
    }
}