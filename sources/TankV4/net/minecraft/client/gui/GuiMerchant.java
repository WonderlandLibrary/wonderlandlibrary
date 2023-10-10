package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import net.minecraft.client.Minecraft;
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

public class GuiMerchant extends GuiContainer {
   private GuiMerchant.MerchantButton previousButton;
   private GuiMerchant.MerchantButton nextButton;
   private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation("textures/gui/container/villager.png");
   private static final Logger logger = LogManager.getLogger();
   private IChatComponent chatComponent;
   private IMerchant merchant;
   private int selectedMerchantRecipe;

   public GuiMerchant(InventoryPlayer var1, IMerchant var2, World var3) {
      super(new ContainerMerchant(var1, var2, var3));
      this.merchant = var2;
      this.chatComponent = var2.getDisplayName();
   }

   public void drawScreen(int var1, int var2, float var3) {
      super.drawScreen(var1, var2, var3);
      MerchantRecipeList var4 = this.merchant.getRecipes(Minecraft.thePlayer);
      if (var4 != null && !var4.isEmpty()) {
         int var5 = (width - this.xSize) / 2;
         int var6 = (height - this.ySize) / 2;
         int var7 = this.selectedMerchantRecipe;
         MerchantRecipe var8 = (MerchantRecipe)var4.get(var7);
         ItemStack var9 = var8.getItemToBuy();
         ItemStack var10 = var8.getSecondItemToBuy();
         ItemStack var11 = var8.getItemToSell();
         GlStateManager.pushMatrix();
         RenderHelper.enableGUIStandardItemLighting();
         GlStateManager.disableLighting();
         GlStateManager.enableRescaleNormal();
         GlStateManager.enableColorMaterial();
         GlStateManager.enableLighting();
         this.itemRender.zLevel = 100.0F;
         this.itemRender.renderItemAndEffectIntoGUI(var9, var5 + 36, var6 + 24);
         this.itemRender.renderItemOverlays(this.fontRendererObj, var9, var5 + 36, var6 + 24);
         if (var10 != null) {
            this.itemRender.renderItemAndEffectIntoGUI(var10, var5 + 62, var6 + 24);
            this.itemRender.renderItemOverlays(this.fontRendererObj, var10, var5 + 62, var6 + 24);
         }

         this.itemRender.renderItemAndEffectIntoGUI(var11, var5 + 120, var6 + 24);
         this.itemRender.renderItemOverlays(this.fontRendererObj, var11, var5 + 120, var6 + 24);
         this.itemRender.zLevel = 0.0F;
         GlStateManager.disableLighting();
         if (this.isPointInRegion(36, 24, 16, 16, var1, var2) && var9 != null) {
            this.renderToolTip(var9, var1, var2);
         } else if (var10 != null && this.isPointInRegion(62, 24, 16, 16, var1, var2) && var10 != null) {
            this.renderToolTip(var10, var1, var2);
         } else if (var11 != null && this.isPointInRegion(120, 24, 16, 16, var1, var2) && var11 != null) {
            this.renderToolTip(var11, var1, var2);
         } else if (var8.isRecipeDisabled() && (this.isPointInRegion(83, 21, 28, 21, var1, var2) || this.isPointInRegion(83, 51, 28, 21, var1, var2))) {
            this.drawCreativeTabHoveringText(I18n.format("merchant.deprecated"), var1, var2);
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

   public void updateScreen() {
      super.updateScreen();
      MerchantRecipeList var1 = this.merchant.getRecipes(Minecraft.thePlayer);
      if (var1 != null) {
         this.nextButton.enabled = this.selectedMerchantRecipe < var1.size() - 1;
         this.previousButton.enabled = this.selectedMerchantRecipe > 0;
      }

   }

   static ResourceLocation access$0() {
      return MERCHANT_GUI_TEXTURE;
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      boolean var2 = false;
      if (var1 == this.nextButton) {
         ++this.selectedMerchantRecipe;
         MerchantRecipeList var3 = this.merchant.getRecipes(Minecraft.thePlayer);
         if (var3 != null && this.selectedMerchantRecipe >= var3.size()) {
            this.selectedMerchantRecipe = var3.size() - 1;
         }

         var2 = true;
      } else if (var1 == this.previousButton) {
         --this.selectedMerchantRecipe;
         if (this.selectedMerchantRecipe < 0) {
            this.selectedMerchantRecipe = 0;
         }

         var2 = true;
      }

      if (var2) {
         ((ContainerMerchant)this.inventorySlots).setCurrentRecipeIndex(this.selectedMerchantRecipe);
         PacketBuffer var4 = new PacketBuffer(Unpooled.buffer());
         var4.writeInt(this.selectedMerchantRecipe);
         this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|TrSel", var4));
      }

   }

   public void initGui() {
      super.initGui();
      int var1 = (width - this.xSize) / 2;
      int var2 = (height - this.ySize) / 2;
      this.buttonList.add(this.nextButton = new GuiMerchant.MerchantButton(1, var1 + 120 + 27, var2 + 24 - 1, true));
      this.buttonList.add(this.previousButton = new GuiMerchant.MerchantButton(2, var1 + 36 - 19, var2 + 24 - 1, false));
      this.nextButton.enabled = false;
      this.previousButton.enabled = false;
   }

   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      String var3 = this.chatComponent.getUnformattedText();
      this.fontRendererObj.drawString(var3, (double)(this.xSize / 2 - this.fontRendererObj.getStringWidth(var3) / 2), 6.0D, 4210752);
      this.fontRendererObj.drawString(I18n.format("container.inventory"), 8.0D, (double)(this.ySize - 96 + 2), 4210752);
   }

   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
      int var4 = (width - this.xSize) / 2;
      int var5 = (height - this.ySize) / 2;
      drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
      MerchantRecipeList var6 = this.merchant.getRecipes(Minecraft.thePlayer);
      if (var6 != null && !var6.isEmpty()) {
         int var7 = this.selectedMerchantRecipe;
         if (var7 < 0 || var7 >= var6.size()) {
            return;
         }

         MerchantRecipe var8 = (MerchantRecipe)var6.get(var7);
         if (var8.isRecipeDisabled()) {
            this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();
            drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
            drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
         }
      }

   }

   static class MerchantButton extends GuiButton {
      private final boolean field_146157_o;

      public MerchantButton(int var1, int var2, int var3, boolean var4) {
         super(var1, var2, var3, 12, 19, "");
         this.field_146157_o = var4;
      }

      public void drawButton(Minecraft var1, int var2, int var3) {
         if (this.visible) {
            var1.getTextureManager().bindTexture(GuiMerchant.access$0());
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean var4 = var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
            int var5 = 0;
            int var6 = 176;
            if (!this.enabled) {
               var6 += this.width * 2;
            } else if (var4) {
               var6 += this.width;
            }

            if (!this.field_146157_o) {
               var5 += this.height;
            }

            drawTexturedModalRect(this.xPosition, this.yPosition, var6, var5, this.width, this.height);
         }

      }
   }
}
