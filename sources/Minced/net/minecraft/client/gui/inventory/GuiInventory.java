// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.inventory.Slot;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.GuiButton;
import java.io.IOException;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import ru.tuskevich.util.math.ButtonAdvanced;
import net.minecraft.inventory.ContainerPlayer;
import ru.tuskevich.util.render.GlowUtility;
import ru.tuskevich.modules.impl.HUD.Hud;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.renderer.InventoryEffectRenderer;

public class GuiInventory extends InventoryEffectRenderer implements IRecipeShownListener
{
    private float oldMouseX;
    private float oldMouseY;
    private GuiButtonImage recipeButton;
    private final GuiRecipeBook recipeBookGui;
    private boolean widthTooNarrow;
    private boolean buttonClicked;
    
    public GuiInventory(final EntityPlayer player) {
        super(player.inventoryContainer);
        this.recipeBookGui = new GuiRecipeBook();
        this.allowUserInput = true;
    }
    
    @Override
    public void updateScreen() {
        if (GuiInventory.mc.playerController.isInCreativeMode()) {
            final Minecraft mc = GuiInventory.mc;
            final Minecraft mc2 = GuiInventory.mc;
            mc.displayGuiScreen(new GuiContainerCreative(Minecraft.player));
        }
        this.recipeBookGui.tick();
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        if (GuiInventory.mc.playerController.isInCreativeMode()) {
            final Minecraft mc = GuiInventory.mc;
            final Minecraft mc2 = GuiInventory.mc;
            mc.displayGuiScreen(new GuiContainerCreative(Minecraft.player));
        }
        else {
            super.initGui();
        }
        if (Hud.arrayListElements.get(0)) {
            GlowUtility.drawGlow((float)(this.width / 2 - 37), (float)(this.height / 2 - 105), 75.0f, 20.0f, 15, Hud.getColor(280));
        }
        this.widthTooNarrow = (this.width < 379);
        this.recipeBookGui.func_194303_a(this.width, this.height, GuiInventory.mc, this.widthTooNarrow, ((ContainerPlayer)this.inventorySlots).craftMatrix);
        this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
        this.recipeButton = new GuiButtonImage(10, this.guiLeft + 94, this.height / 2 - 22, 20, 18, 178, 0, 19, GuiInventory.INVENTORY_BACKGROUND);
        this.buttonList.add(this.recipeButton);
        this.buttonList.add(new ButtonAdvanced(31, this.width / 2 - 37, this.height / 2 - 105, 75, 20, "\u0412\u044b\u0431\u0440\u043e\u0441\u0438\u0442\u044c \u0432\u0441\u0435"));
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRenderer.drawString(I18n.format("container.crafting", new Object[0]), 97, 8, 4210752);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.hasActivePotionEffects = !this.recipeBookGui.isVisible();
        if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
            this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
            this.recipeBookGui.render(mouseX, mouseY, partialTicks);
        }
        else {
            this.recipeBookGui.render(mouseX, mouseY, partialTicks);
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.recipeBookGui.renderGhostRecipe(this.guiLeft, this.guiTop, false, partialTicks);
        }
        this.renderHoveredToolTip(mouseX, mouseY);
        this.recipeBookGui.renderTooltip(this.guiLeft, this.guiTop, mouseX, mouseY);
        this.oldMouseX = (float)mouseX;
        this.oldMouseY = (float)mouseY;
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GuiInventory.mc.getTextureManager().bindTexture(GuiInventory.INVENTORY_BACKGROUND);
        final int i = this.guiLeft;
        final int j = this.guiTop;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        final int posX = i + 51;
        final int posY = j + 75;
        final int scale = 30;
        final float mouseX2 = i + 51 - this.oldMouseX;
        final float mouseY2 = j + 75 - 50 - this.oldMouseY;
        final Minecraft mc = GuiInventory.mc;
        drawEntityOnScreen(posX, posY, scale, mouseX2, mouseY2, Minecraft.player);
    }
    
    public static void drawEntityOnScreen(final int posX, final int posY, final int scale, final float mouseX, final float mouseY, final EntityLivingBase ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, 50.0f);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        final float f = ent.renderYawOffset;
        final float f2 = ent.rotationYaw;
        final float f3 = ent.rotationPitch;
        final float f4 = ent.prevRotationYawHead;
        final float f5 = ent.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(mouseY / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        ent.renderYawOffset = (float)Math.atan(mouseX / 40.0f) * 20.0f;
        ent.rotationYaw = (float)Math.atan(mouseX / 40.0f) * 40.0f;
        ent.rotationPitch = -(float)Math.atan(mouseY / 40.0f) * 20.0f;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f2;
        ent.rotationPitch = f3;
        ent.prevRotationYawHead = f4;
        ent.rotationYawHead = f5;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    @Override
    protected boolean isPointInRegion(final int rectX, final int rectY, final int rectWidth, final int rectHeight, final int pointX, final int pointY) {
        return (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.isPointInRegion(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (!this.recipeBookGui.mouseClicked(mouseX, mouseY, mouseButton) && (!this.widthTooNarrow || !this.recipeBookGui.isVisible())) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (this.buttonClicked) {
            this.buttonClicked = false;
        }
        else {
            super.mouseReleased(mouseX, mouseY, state);
        }
    }
    
    @Override
    protected boolean hasClickedOutside(final int p_193983_1_, final int p_193983_2_, final int p_193983_3_, final int p_193983_4_) {
        final boolean flag = p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
        return this.recipeBookGui.hasClickedOutside(p_193983_1_, p_193983_2_, this.guiLeft, this.guiTop, this.xSize, this.ySize) && flag;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 10) {
            this.recipeBookGui.initVisuals(this.widthTooNarrow, ((ContainerPlayer)this.inventorySlots).craftMatrix);
            this.recipeBookGui.toggleVisibility();
            this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
            this.recipeButton.setPosition(this.guiLeft + 104, this.height / 2 - 22);
            this.buttonClicked = true;
        }
        else if (button.id == 31) {
            for (int i = 0; i < 46; ++i) {
                final PlayerControllerMP playerController = GuiInventory.mc.playerController;
                final Minecraft mc = GuiInventory.mc;
                final int windowId = Minecraft.player.inventoryContainer.windowId;
                final int slotId = i;
                final int mouseButton = 1;
                final ClickType throw1 = ClickType.THROW;
                final Minecraft mc2 = GuiInventory.mc;
                playerController.windowClick(windowId, slotId, mouseButton, throw1, Minecraft.player);
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (!this.recipeBookGui.keyPressed(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    protected void handleMouseClick(final Slot slotIn, final int slotId, final int mouseButton, final ClickType type) {
        super.handleMouseClick(slotIn, slotId, mouseButton, type);
        this.recipeBookGui.slotClicked(slotIn);
    }
    
    @Override
    public void recipesUpdated() {
        this.recipeBookGui.recipesUpdated();
    }
    
    @Override
    public void onGuiClosed() {
        this.recipeBookGui.removed();
        super.onGuiClosed();
    }
    
    @Override
    public GuiRecipeBook func_194310_f() {
        return this.recipeBookGui;
    }
}
