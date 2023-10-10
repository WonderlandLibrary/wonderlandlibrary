/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiMerchant
extends GuiContainer {
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation("textures/gui/container/villager.png");
    private IMerchant merchant;
    private MerchantButton nextButton;
    private MerchantButton previousButton;
    private int selectedMerchantRecipe;
    private IChatComponent chatComponent;

    public GuiMerchant(InventoryPlayer p_i45500_1_, IMerchant p_i45500_2_, World worldIn) {
        super(new ContainerMerchant(p_i45500_1_, p_i45500_2_, worldIn));
        this.merchant = p_i45500_2_;
        this.chatComponent = p_i45500_2_.getDisplayName();
    }

    @Override
    public void initGui() {
        super.initGui();
        int i2 = (this.width - this.xSize) / 2;
        int j2 = (this.height - this.ySize) / 2;
        this.nextButton = new MerchantButton(1, i2 + 120 + 27, j2 + 24 - 1, true);
        this.buttonList.add(this.nextButton);
        this.previousButton = new MerchantButton(2, i2 + 36 - 19, j2 + 24 - 1, false);
        this.buttonList.add(this.previousButton);
        this.nextButton.enabled = false;
        this.previousButton.enabled = false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s2 = this.chatComponent.getUnformattedText();
        this.fontRendererObj.drawString(s2, this.xSize / 2 - this.fontRendererObj.getStringWidth(s2) / 2, 6, 0x404040);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
        if (merchantrecipelist != null) {
            this.nextButton.enabled = this.selectedMerchantRecipe < merchantrecipelist.size() - 1;
            this.previousButton.enabled = this.selectedMerchantRecipe > 0;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        boolean flag = false;
        if (button == this.nextButton) {
            ++this.selectedMerchantRecipe;
            MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
            if (merchantrecipelist != null && this.selectedMerchantRecipe >= merchantrecipelist.size()) {
                this.selectedMerchantRecipe = merchantrecipelist.size() - 1;
            }
            flag = true;
        } else if (button == this.previousButton) {
            --this.selectedMerchantRecipe;
            if (this.selectedMerchantRecipe < 0) {
                this.selectedMerchantRecipe = 0;
            }
            flag = true;
        }
        if (flag) {
            ((ContainerMerchant)this.inventorySlots).setCurrentRecipeIndex(this.selectedMerchantRecipe);
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.selectedMerchantRecipe);
            this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|TrSel", packetbuffer));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
        int i2 = (this.width - this.xSize) / 2;
        int j2 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i2, j2, 0, 0, this.xSize, this.ySize);
        MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
        if (merchantrecipelist != null && !merchantrecipelist.isEmpty()) {
            int k2 = this.selectedMerchantRecipe;
            if (k2 < 0 || k2 >= merchantrecipelist.size()) {
                return;
            }
            MerchantRecipe merchantrecipe = (MerchantRecipe)merchantrecipelist.get(k2);
            if (merchantrecipe.isRecipeDisabled()) {
                this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableLighting();
                this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
                this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
        if (merchantrecipelist != null && !merchantrecipelist.isEmpty()) {
            int i2 = (this.width - this.xSize) / 2;
            int j2 = (this.height - this.ySize) / 2;
            int k2 = this.selectedMerchantRecipe;
            MerchantRecipe merchantrecipe = (MerchantRecipe)merchantrecipelist.get(k2);
            ItemStack itemstack = merchantrecipe.getItemToBuy();
            ItemStack itemstack1 = merchantrecipe.getSecondItemToBuy();
            ItemStack itemstack2 = merchantrecipe.getItemToSell();
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            this.itemRender.zLevel = 100.0f;
            this.itemRender.renderItemAndEffectIntoGUI(itemstack, i2 + 36, j2 + 24);
            this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, i2 + 36, j2 + 24);
            if (itemstack1 != null) {
                this.itemRender.renderItemAndEffectIntoGUI(itemstack1, i2 + 62, j2 + 24);
                this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack1, i2 + 62, j2 + 24);
            }
            this.itemRender.renderItemAndEffectIntoGUI(itemstack2, i2 + 120, j2 + 24);
            this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack2, i2 + 120, j2 + 24);
            this.itemRender.zLevel = 0.0f;
            GlStateManager.disableLighting();
            if (this.isPointInRegion(36, 24, 16, 16, mouseX, mouseY) && itemstack != null) {
                this.renderToolTip(itemstack, mouseX, mouseY);
            } else if (itemstack1 != null && this.isPointInRegion(62, 24, 16, 16, mouseX, mouseY) && itemstack1 != null) {
                this.renderToolTip(itemstack1, mouseX, mouseY);
            } else if (itemstack2 != null && this.isPointInRegion(120, 24, 16, 16, mouseX, mouseY) && itemstack2 != null) {
                this.renderToolTip(itemstack2, mouseX, mouseY);
            } else if (merchantrecipe.isRecipeDisabled() && (this.isPointInRegion(83, 21, 28, 21, mouseX, mouseY) || this.isPointInRegion(83, 51, 28, 21, mouseX, mouseY))) {
                this.drawCreativeTabHoveringText(I18n.format("merchant.deprecated", new Object[0]), mouseX, mouseY);
            }
            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
        }
    }

    public IMerchant getMerchant() {
        return this.merchant;
    }

    static class MerchantButton
    extends GuiButton {
        private final boolean field_146157_o;

        public MerchantButton(int buttonID, int x2, int y2, boolean p_i1095_4_) {
            super(buttonID, x2, y2, 12, 19, "");
            this.field_146157_o = p_i1095_4_;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                int i2 = 0;
                int j2 = 176;
                if (!this.enabled) {
                    j2 += this.width * 2;
                } else if (flag) {
                    j2 += this.width;
                }
                if (!this.field_146157_o) {
                    i2 += this.height;
                }
                this.drawTexturedModalRect(this.xPosition, this.yPosition, j2, i2, this.width, this.height);
            }
        }
    }
}

