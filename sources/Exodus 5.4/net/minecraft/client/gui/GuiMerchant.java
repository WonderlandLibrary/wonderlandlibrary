/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.ArrayList;
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
    private IChatComponent chatComponent;
    private static final Logger logger = LogManager.getLogger();
    private MerchantButton previousButton;
    private MerchantButton nextButton;
    private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation("textures/gui/container/villager.png");
    private IMerchant merchant;
    private int selectedMerchantRecipe;

    public GuiMerchant(InventoryPlayer inventoryPlayer, IMerchant iMerchant, World world) {
        super(new ContainerMerchant(inventoryPlayer, iMerchant, world));
        this.merchant = iMerchant;
        this.chatComponent = iMerchant.getDisplayName();
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        Object object;
        boolean bl = false;
        if (guiButton == this.nextButton) {
            ++this.selectedMerchantRecipe;
            object = this.merchant.getRecipes(Minecraft.thePlayer);
            if (object != null && this.selectedMerchantRecipe >= ((ArrayList)object).size()) {
                this.selectedMerchantRecipe = ((ArrayList)object).size() - 1;
            }
            bl = true;
        } else if (guiButton == this.previousButton) {
            --this.selectedMerchantRecipe;
            if (this.selectedMerchantRecipe < 0) {
                this.selectedMerchantRecipe = 0;
            }
            bl = true;
        }
        if (bl) {
            ((ContainerMerchant)this.inventorySlots).setCurrentRecipeIndex(this.selectedMerchantRecipe);
            object = new PacketBuffer(Unpooled.buffer());
            ((PacketBuffer)object).writeInt(this.selectedMerchantRecipe);
            this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|TrSel", (PacketBuffer)object));
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        super.drawScreen(n, n2, f);
        MerchantRecipeList merchantRecipeList = this.merchant.getRecipes(Minecraft.thePlayer);
        if (merchantRecipeList != null && !merchantRecipeList.isEmpty()) {
            int n3 = (width - this.xSize) / 2;
            int n4 = (height - this.ySize) / 2;
            int n5 = this.selectedMerchantRecipe;
            MerchantRecipe merchantRecipe = (MerchantRecipe)merchantRecipeList.get(n5);
            ItemStack itemStack = merchantRecipe.getItemToBuy();
            ItemStack itemStack2 = merchantRecipe.getSecondItemToBuy();
            ItemStack itemStack3 = merchantRecipe.getItemToSell();
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            this.itemRender.zLevel = 100.0f;
            this.itemRender.renderItemAndEffectIntoGUI(itemStack, n3 + 36, n4 + 24);
            this.itemRender.renderItemOverlays(this.fontRendererObj, itemStack, n3 + 36, n4 + 24);
            if (itemStack2 != null) {
                this.itemRender.renderItemAndEffectIntoGUI(itemStack2, n3 + 62, n4 + 24);
                this.itemRender.renderItemOverlays(this.fontRendererObj, itemStack2, n3 + 62, n4 + 24);
            }
            this.itemRender.renderItemAndEffectIntoGUI(itemStack3, n3 + 120, n4 + 24);
            this.itemRender.renderItemOverlays(this.fontRendererObj, itemStack3, n3 + 120, n4 + 24);
            this.itemRender.zLevel = 0.0f;
            GlStateManager.disableLighting();
            if (this.isPointInRegion(36, 24, 16, 16, n, n2) && itemStack != null) {
                this.renderToolTip(itemStack, n, n2);
            } else if (itemStack2 != null && this.isPointInRegion(62, 24, 16, 16, n, n2) && itemStack2 != null) {
                this.renderToolTip(itemStack2, n, n2);
            } else if (itemStack3 != null && this.isPointInRegion(120, 24, 16, 16, n, n2) && itemStack3 != null) {
                this.renderToolTip(itemStack3, n, n2);
            } else if (merchantRecipe.isRecipeDisabled() && (this.isPointInRegion(83, 21, 28, 21, n, n2) || this.isPointInRegion(83, 51, 28, 21, n, n2))) {
                this.drawCreativeTabHoveringText(I18n.format("merchant.deprecated", new Object[0]), n, n2);
            }
            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int n, int n2) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
        int n3 = (width - this.xSize) / 2;
        int n4 = (height - this.ySize) / 2;
        this.drawTexturedModalRect(n3, n4, 0, 0, this.xSize, this.ySize);
        MerchantRecipeList merchantRecipeList = this.merchant.getRecipes(Minecraft.thePlayer);
        if (merchantRecipeList != null && !merchantRecipeList.isEmpty()) {
            int n5 = this.selectedMerchantRecipe;
            if (n5 < 0 || n5 >= merchantRecipeList.size()) {
                return;
            }
            MerchantRecipe merchantRecipe = (MerchantRecipe)merchantRecipeList.get(n5);
            if (merchantRecipe.isRecipeDisabled()) {
                this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableLighting();
                this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
                this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        int n = (width - this.xSize) / 2;
        int n2 = (height - this.ySize) / 2;
        this.nextButton = new MerchantButton(1, n + 120 + 27, n2 + 24 - 1, true);
        this.buttonList.add(this.nextButton);
        this.previousButton = new MerchantButton(2, n + 36 - 19, n2 + 24 - 1, false);
        this.buttonList.add(this.previousButton);
        this.nextButton.enabled = false;
        this.previousButton.enabled = false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int n, int n2) {
        String string = this.chatComponent.getUnformattedText();
        this.fontRendererObj.drawString(string, this.xSize / 2 - this.fontRendererObj.getStringWidth(string) / 2, 6.0, 0x404040);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8.0, this.ySize - 96 + 2, 0x404040);
    }

    public IMerchant getMerchant() {
        return this.merchant;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        MerchantRecipeList merchantRecipeList = this.merchant.getRecipes(Minecraft.thePlayer);
        if (merchantRecipeList != null) {
            this.nextButton.enabled = this.selectedMerchantRecipe < merchantRecipeList.size() - 1;
            this.previousButton.enabled = this.selectedMerchantRecipe > 0;
        }
    }

    static class MerchantButton
    extends GuiButton {
        private final boolean field_146157_o;

        @Override
        public void drawButton(Minecraft minecraft, int n, int n2) {
            if (this.visible) {
                minecraft.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                boolean bl = n >= this.xPosition && n2 >= this.yPosition && n < this.xPosition + this.width && n2 < this.yPosition + this.height;
                int n3 = 0;
                int n4 = 176;
                if (!this.enabled) {
                    n4 += this.width * 2;
                } else if (bl) {
                    n4 += this.width;
                }
                if (!this.field_146157_o) {
                    n3 += this.height;
                }
                this.drawTexturedModalRect(this.xPosition, this.yPosition, n4, n3, this.width, this.height);
            }
        }

        public MerchantButton(int n, int n2, int n3, boolean bl) {
            super(n, n2, n3, 12, 19, "");
            this.field_146157_o = bl;
        }
    }
}

