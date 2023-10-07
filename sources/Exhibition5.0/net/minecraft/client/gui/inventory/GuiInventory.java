// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.inventory;

import java.io.IOException;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.InventoryEffectRenderer;

public class GuiInventory extends InventoryEffectRenderer
{
    private float oldMouseX;
    private float oldMouseY;
    private static final String __OBFID = "CL_00000761";
    
    public GuiInventory(final EntityPlayer p_i1094_1_) {
        super(p_i1094_1_.inventoryContainer);
        this.allowUserInput = true;
    }
    
    @Override
    public void updateScreen() {
        if (this.mc.playerController.isInCreativeMode()) {
            this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.thePlayer));
        }
        this.func_175378_g();
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        if (this.mc.playerController.isInCreativeMode()) {
            this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.thePlayer));
        }
        else {
            super.initGui();
        }
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 86.0f, 16.0f, 4210752);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.oldMouseX = mouseX;
        this.oldMouseY = mouseY;
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiInventory.inventoryBackground);
        final int var4 = this.guiLeft;
        final int var5 = this.guiTop;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        drawEntityOnScreen(var4 + 51, var5 + 75, 30, var4 + 51 - this.oldMouseX, var5 + 75 - 50 - this.oldMouseY, this.mc.thePlayer);
    }
    
    public static void drawEntityOnScreen(final int p_147046_0_, final int p_147046_1_, final int p_147046_2_, final float p_147046_3_, final float p_147046_4_, final EntityLivingBase p_147046_5_) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(p_147046_0_, p_147046_1_, 50.0f);
        GlStateManager.scale(-p_147046_2_, p_147046_2_, p_147046_2_);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        final float var6 = p_147046_5_.renderYawOffset;
        final float var7 = p_147046_5_.rotationYaw;
        final float var8 = p_147046_5_.rotationPitch;
        final float var9 = p_147046_5_.prevRotationYawHead;
        final float var10 = p_147046_5_.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(p_147046_4_ / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        p_147046_5_.renderYawOffset = (float)Math.atan(p_147046_3_ / 40.0f) * 20.0f;
        p_147046_5_.rotationYaw = (float)Math.atan(p_147046_3_ / 40.0f) * 40.0f;
        p_147046_5_.rotationPitch = -(float)Math.atan(p_147046_4_ / 40.0f) * 20.0f;
        p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
        p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager var11 = Minecraft.getMinecraft().getRenderManager();
        var11.func_178631_a(180.0f);
        var11.func_178633_a(false);
        var11.renderEntityWithPosYaw(p_147046_5_, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        var11.func_178633_a(true);
        p_147046_5_.renderYawOffset = var6;
        p_147046_5_.rotationYaw = var7;
        p_147046_5_.rotationPitch = var8;
        p_147046_5_.prevRotationYawHead = var9;
        p_147046_5_.rotationYawHead = var10;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTextures();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
        }
    }
}
