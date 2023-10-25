package org.dreamcore.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.ui.button.ImageButton;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GuiCapeSelector extends GuiScreen {

    protected ArrayList<ImageButton> imageButtons = new ArrayList<>();
    private int width, height;
    private float spin;

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(mc);
        width = sr.getScaledWidth() / 2;
        height = sr.getScaledHeight() / 2;
        this.imageButtons.clear();
        this.imageButtons.add(new ImageButton(new ResourceLocation("dreamcore/close-button.png"), width + 106, height - 135, 8, 8, "", 19));
        this.imageButtons.add(new ImageButton(new ResourceLocation("dreamcore/arrow/arrow-right.png"), width + 30, height + 52, 32, 25, "", 56));
        this.imageButtons.add(new ImageButton(new ResourceLocation("dreamcore/arrow/arrow-left.png"), width - 50, height + 52, 32, 25, "", 55));

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawWorldBackground(0);
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);
        RectHelper.drawSkeetRectWithoutBorder(width - 70, height - 80, width + 80, height + 20);
        RectHelper.drawSkeetButton(width - 70, height - 78, width + 80, height + 80);
        org.dreamcore.client.helpers.render.RenderHelper.drawImage(new ResourceLocation("dreamcore/skeet.png"), width - 110, height - 140, 230, 1, Color.WHITE);
        mc.circleregular.drawStringWithOutline("Cape Changer", width - 100, height - 133, -1);
        drawEntityOnScreen(width + 7, height + 38, spin, mc.player);
        spin += 0.9;
        for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.LIGHT_GRAY);
        }
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            for (ImageButton imageButton : this.imageButtons) {
                imageButton.onClick(mouseX, mouseY);
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void drawEntityOnScreen(float posX, float posY, float mouseX, EntityLivingBase entity) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, 50.0F);
        GlStateManager.scale((float) (-80), (float) 80, (float) 80);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = entity.renderYawOffset;
        float f1 = entity.rotationYaw;
        float f2 = entity.rotationPitchHead;
        float f3 = entity.prevRotationYawHead;
        float f4 = entity.rotationYawHead;
        GlStateManager.rotate(180, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        entity.renderYawOffset = mouseX;
        entity.rotationYaw = (mouseX);
        entity.rotationPitchHead = (float) 0;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;
        entity.prevRotationPitchHead = (float) 0;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getInstance().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        entity.renderYawOffset = f;
        entity.rotationYaw = f1;
        entity.rotationPitchHead = f2;
        entity.prevRotationPitchHead = f2;
        entity.prevRotationYawHead = f3;
        entity.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static class Selector {
        public static String capeName;

        public static String getCapeName() {
            return capeName;
        }

        public static void setCapeName(String capeName) {
            Selector.capeName = capeName;
        }
    }
}
