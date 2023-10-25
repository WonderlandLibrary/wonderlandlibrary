/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.celestial.client.feature.impl.hud.ClickGui;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.ui.button.ImageButton;
import org.celestial.client.ui.font.MCFontRenderer;

public class GuiCapeSelector
extends GuiScreen {
    protected ArrayList<ImageButton> imageButtons = new ArrayList();
    private int width;
    private int height;
    private float spin;

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.width = sr.getScaledWidth() / 2;
        this.height = sr.getScaledHeight() / 2;
        this.imageButtons.clear();
        this.imageButtons.add(new ImageButton(new ResourceLocation("celestial/close-button.png"), this.width + 106, this.height - 135, 8, 8, "", 19));
        this.imageButtons.add(new ImageButton(new ResourceLocation("celestial/arrow/arrow-right.png"), this.width + 30, this.height + 50, 32, 25, "", 56));
        this.imageButtons.add(new ImageButton(new ResourceLocation("celestial/arrow/arrow-left.png"), this.width - 50, this.height + 50, 32, 25, "", 55));
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawWorldBackground(0);
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        if (ClickGui.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(Color.BLACK, (double)(this.width - 125), (double)(this.height - 150), 261.0, 245.0, 45);
        }
        RectHelper.drawSkeetRect(this.width - 70, this.height - 80, this.width + 80, this.height + 20);
        RectHelper.drawSkeetButton(this.width - 70, this.height - 78, this.width + 80, this.height + 80);
        RenderHelper.drawImage(new ResourceLocation("celestial/skeet.png"), this.width - 110, this.height - 140, 230.0f, 2.0f, ClientHelper.getClientColor());
        MCFontRenderer.drawStringWithOutline(this.mc.robotoRegularFontRender, "Cape Selector", (float)(this.width - 100), (float)(this.height - 133), -1);
        if (ClickGui.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(ClientHelper.getClientColor().brighter(), (double)(this.width - 110), (double)(this.height - 140), 235.0, 5.0, 35);
        }
        this.drawEntityOnScreen(this.width + 7, this.height + 38, 80, this.spin, 0.0f, this.mc.player);
        this.spin += 1.0f;
        for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
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

    private void drawEntityOnScreen(float posX, float posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, 50.0f);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitchHead;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        ent.renderYawOffset = mouseX;
        ent.rotationYaw = mouseX;
        ent.rotationPitchHead = mouseY;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        ent.prevRotationPitchHead = mouseY;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitchHead = f2;
        ent.prevRotationPitchHead = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
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

