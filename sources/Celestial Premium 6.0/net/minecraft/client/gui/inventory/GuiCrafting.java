/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.inventory;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.visuals.ContainerBlur;
import org.celestial.client.helpers.render.RenderHelper;

public class GuiCrafting
extends GuiContainer
implements IRecipeShownListener {
    private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/crafting_table.png");
    private GuiButtonImage field_192049_w;
    private final GuiRecipeBook field_192050_x = new GuiRecipeBook();
    private boolean field_193112_y;
    private int blur;

    public GuiCrafting(InventoryPlayer playerInv, World worldIn) {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }

    public GuiCrafting(InventoryPlayer playerInv, World worldIn, BlockPos blockPosition) {
        super(new ContainerWorkbench(playerInv, worldIn, blockPosition));
    }

    @Override
    public void initGui() {
        this.blur = 0;
        super.initGui();
        this.field_193112_y = this.width < 379;
        this.field_192050_x.func_194303_a(this.width, this.height, this.mc, this.field_193112_y, ((ContainerWorkbench)this.inventorySlots).craftMatrix);
        this.guiLeft = this.field_192050_x.func_193011_a(this.field_193112_y, this.width, this.xSize);
        this.field_192049_w = new GuiButtonImage(10, this.guiLeft + 5, this.height / 2 - 49, 20, 18, 0, 168, 19, CRAFTING_TABLE_GUI_TEXTURES);
        this.buttonList.add(this.field_192049_w);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.field_192050_x.func_193957_d();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        ScaledResolution sr = new ScaledResolution(this.mc);
        ++this.blur;
        this.blur = MathHelper.clamp(this.blur, 0, ContainerBlur.blurStrength.getCurrentValueInt());
        if (Celestial.instance.featureManager.getFeatureByClass(ContainerBlur.class).getState()) {
            if (this.mc.gameSettings.ofFastRender) {
                this.mc.gameSettings.ofFastRender = false;
            }
            RenderHelper.renderBlur(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), this.blur / 2);
        }
        if (this.field_192050_x.func_191878_b() && this.field_193112_y) {
            GlStateManager.pushMatrix();
            this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
            GlStateManager.popMatrix();
            this.field_192050_x.func_191861_a(mouseX, mouseY, partialTicks);
        } else {
            this.field_192050_x.func_191861_a(mouseX, mouseY, partialTicks);
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.field_192050_x.func_191864_a(this.guiLeft, this.guiTop, true, partialTicks);
        }
        this.func_191948_b(mouseX, mouseY);
        this.field_192050_x.func_191876_c(this.guiLeft, this.guiTop, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 28.0f, 6.0f, 0x404040);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8.0f, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(CRAFTING_TABLE_GUI_TEXTURES);
        int i = this.guiLeft;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY) {
        return (!this.field_193112_y || !this.field_192050_x.func_191878_b()) && super.isPointInRegion(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (!(this.field_192050_x.func_191862_a(mouseX, mouseY, mouseButton) || this.field_193112_y && this.field_192050_x.func_191878_b())) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected boolean func_193983_c(int p_193983_1_, int p_193983_2_, int p_193983_3_, int p_193983_4_) {
        boolean flag = p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
        return this.field_192050_x.func_193955_c(p_193983_1_, p_193983_2_, this.guiLeft, this.guiTop, this.xSize, this.ySize) && flag;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 10) {
            this.field_192050_x.func_193014_a(this.field_193112_y, ((ContainerWorkbench)this.inventorySlots).craftMatrix);
            this.field_192050_x.func_191866_a();
            this.guiLeft = this.field_192050_x.func_193011_a(this.field_193112_y, this.width, this.xSize);
            this.field_192049_w.func_191746_c(this.guiLeft + 5, this.height / 2 - 49);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (!this.field_192050_x.func_191859_a(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        super.handleMouseClick(slotIn, slotId, mouseButton, type);
        this.field_192050_x.func_191874_a(slotIn);
    }

    @Override
    public void func_192043_J_() {
        this.field_192050_x.func_193948_e();
    }

    @Override
    public void onGuiClosed() {
        this.field_192050_x.func_191871_c();
        super.onGuiClosed();
    }

    @Override
    public GuiRecipeBook func_194310_f() {
        return this.field_192050_x;
    }
}

