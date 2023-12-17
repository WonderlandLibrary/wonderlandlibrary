// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.client.gui.GuiButton;
import java.io.IOException;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;

public class GuiCrafting extends GuiContainer implements IRecipeShownListener
{
    private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES;
    private GuiButtonImage recipeButton;
    private final GuiRecipeBook recipeBookGui;
    private boolean widthTooNarrow;
    
    public GuiCrafting(final InventoryPlayer playerInv, final World worldIn) {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }
    
    public GuiCrafting(final InventoryPlayer playerInv, final World worldIn, final BlockPos blockPosition) {
        super(new ContainerWorkbench(playerInv, worldIn, blockPosition));
        this.recipeBookGui = new GuiRecipeBook();
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.widthTooNarrow = (this.width < 379);
        this.recipeBookGui.func_194303_a(this.width, this.height, GuiCrafting.mc, this.widthTooNarrow, ((ContainerWorkbench)this.inventorySlots).craftMatrix);
        this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
        this.recipeButton = new GuiButtonImage(10, this.guiLeft + 5, this.height / 2 - 49, 20, 18, 0, 168, 19, GuiCrafting.CRAFTING_TABLE_GUI_TEXTURES);
        this.buttonList.add(this.recipeButton);
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.recipeBookGui.tick();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
            this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
            this.recipeBookGui.render(mouseX, mouseY, partialTicks);
        }
        else {
            this.recipeBookGui.render(mouseX, mouseY, partialTicks);
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.recipeBookGui.renderGhostRecipe(this.guiLeft, this.guiTop, true, partialTicks);
        }
        this.renderHoveredToolTip(mouseX, mouseY);
        this.recipeBookGui.renderTooltip(this.guiLeft, this.guiTop, mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRenderer.drawString(I18n.format("container.crafting", new Object[0]), 28, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GuiCrafting.mc.getTextureManager().bindTexture(GuiCrafting.CRAFTING_TABLE_GUI_TEXTURES);
        final int i = this.guiLeft;
        final int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
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
    protected boolean hasClickedOutside(final int p_193983_1_, final int p_193983_2_, final int p_193983_3_, final int p_193983_4_) {
        final boolean flag = p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
        return this.recipeBookGui.hasClickedOutside(p_193983_1_, p_193983_2_, this.guiLeft, this.guiTop, this.xSize, this.ySize) && flag;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 10) {
            this.recipeBookGui.initVisuals(this.widthTooNarrow, ((ContainerWorkbench)this.inventorySlots).craftMatrix);
            this.recipeBookGui.toggleVisibility();
            this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
            this.recipeButton.setPosition(this.guiLeft + 5, this.height / 2 - 49);
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
    
    static {
        CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/crafting_table.png");
    }
}
