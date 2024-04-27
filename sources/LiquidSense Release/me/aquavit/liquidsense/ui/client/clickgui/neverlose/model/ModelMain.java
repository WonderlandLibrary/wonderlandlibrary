package me.aquavit.liquidsense.ui.client.clickgui.neverlose.model;

import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Impl;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Main;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ModelMain extends Main {

    public static void drawModel(int mouseX, int mouseY, Main main) {
        RenderUtils.drawNLRect(Impl.coordinateX + 470, Impl.coordinateY + 70, Impl.coordinateX + 630, Impl.coordinateY + 320, 2f, new Color(6, 16, 28, 245).getRGB()); //背景
        Fonts.font16.drawString("Interactive ESP Preview", Impl.coordinateX + 475, Impl.coordinateY + 75, new Color(255, 255, 255).getRGB());

        GL11.glPushMatrix();
        GL11.glTranslated(Impl.coordinateX + 550, Impl.coordinateY + 230, 0.0);
        drawPlayerOnScreen();
        GL11.glPopMatrix();

        RenderUtils.drawRect(Impl.coordinateX + 478,
                Impl.coordinateY + 251,
                Impl.coordinateX + 483 + Fonts.font14.getStringWidth("Box"),
                Impl.coordinateY + 262, new Color(3, 168, 245));
        Fonts.font14.drawString("Box", Impl.coordinateX + 480, Impl.coordinateY + 255, -1);

    }

    private static void drawPlayerOnScreen() {
        final Minecraft mc = Minecraft.getMinecraft();

        EntityOtherPlayerMP renderPlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
        renderPlayer.clonePlayer(mc.thePlayer, true);

        GlStateManager.resetColor();
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0F, 0F, 50F);
        GlStateManager.scale(-65F, 65F, 65F);
        GlStateManager.rotate(180F, 0F, 0F, 1F);

        GlStateManager.rotate(135F, 0F, 1F, 0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135F, 0F, 1F, 0F);
        GlStateManager.rotate((float) 0, 1F, 0F, 0F);

        renderPlayer.renderYawOffset = 0;
        renderPlayer.rotationYaw = 0;
        renderPlayer.rotationPitch = 0;
        renderPlayer.rotationYawHead = renderPlayer.rotationYaw;
        renderPlayer.prevRotationYawHead = renderPlayer.rotationYaw;
        //renderPlayer.inventory.armorInventory[2] = new ItemStack(Items.diamond_chestplate);  // 设置胸甲

        GlStateManager.translate(0F, 0F, 0F);

        RenderManager renderManager = mc.getRenderManager();
        renderManager.setPlayerViewY(180F);
        renderManager.setRenderShadow(false);
        renderManager.renderEntityWithPosYaw(renderPlayer, 0.0, 0.0, 0.0, 0F, 1F);
        renderManager.setRenderShadow(true);

        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.resetColor();
    }

    public static void Tag2D(float partialTicks, EntityLivingBase entity, String name) {
        final Minecraft mc = Minecraft.getMinecraft();
        double posX = RenderUtils.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
        double posY = RenderUtils.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
        double posZ = RenderUtils.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);

        double width = entity.width / 1.5;
        double height = (entity.height + (entity.isSneaking() ? -0.4 : 0.1) +
                (0.0));


        AxisAlignedBB aabb = new AxisAlignedBB(posX - width, posY, posZ - width, posX + width, posY + height + 0.05, posZ + width);
        List<Vector3d> vectors = Arrays.asList(
                new Vector3d(aabb.minX, aabb.minY, aabb.minZ),
                new Vector3d(aabb.minX, aabb.maxY, aabb.minZ),
                new Vector3d(aabb.maxX, aabb.minY, aabb.minZ),
                new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ),
                new Vector3d(aabb.minX, aabb.minY, aabb.maxZ),
                new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ),
                new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ),
                new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ)
        );
        mc.entityRenderer.setupCameraTransform(partialTicks, 0);
        Vector4d position = null;

        for (Vector3d vector : vectors) {
            vector = RenderUtils.project(vector.x - mc.getRenderManager().viewerPosX, vector.y - mc.getRenderManager().viewerPosY, vector.z - mc.getRenderManager().viewerPosZ);
            if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                if (position == null) {
                    position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                }
                position.x = Math.min(vector.x, position.x);
                position.y = Math.min(vector.y, position.y);
                position.z = Math.max(vector.x, position.z);
                position.w = Math.max(vector.y, position.w);
            }
        }

        mc.entityRenderer.setupOverlayRendering();
        if (position != null) {
            GL11.glPushMatrix();

            final float x = (float) position.x;
            final float x2 = (float) position.z;
            final float y = (float) position.y - 1;

            GL11.glPushMatrix();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            float offset = 2f;
            Fonts.minecraftFont.drawString(name, ((x + ((x2 - x) / 2)) - (Fonts.minecraftFont.getStringWidth(name) / 4f)) * offset, (y - Fonts.minecraftFont.FONT_HEIGHT / 2f - 2) * offset, -1, true);
            GL11.glPopMatrix();
            GL11.glScalef(1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
        }
    }
}
