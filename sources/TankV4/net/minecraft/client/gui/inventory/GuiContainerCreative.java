package net.minecraft.client.gui.inventory;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiContainerCreative extends InventoryEffectRenderer {
   private boolean isScrolling;
   private Slot field_147064_C;
   private boolean field_147057_D;
   private boolean wasClicking;
   private List field_147063_B;
   private static final ResourceLocation creativeInventoryTabs = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
   private GuiTextField searchField;
   private float currentScroll;
   private CreativeCrafting field_147059_E;
   private static InventoryBasic field_147060_v = new InventoryBasic("tmp", true, 45);
   private static int selectedTabIndex;

   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      RenderHelper.enableGUIStandardItemLighting();
      CreativeTabs var4 = CreativeTabs.creativeTabArray[selectedTabIndex];
      CreativeTabs[] var8;
      int var7 = (var8 = CreativeTabs.creativeTabArray).length;

      int var6;
      for(var6 = 0; var6 < var7; ++var6) {
         CreativeTabs var5 = var8[var6];
         this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
         if (var5.getTabIndex() != selectedTabIndex) {
            this.func_147051_a(var5);
         }
      }

      this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + var4.getBackgroundImageName()));
      drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
      this.searchField.drawTextBox();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      int var10 = this.guiLeft + 175;
      var6 = this.guiTop + 18;
      var7 = var6 + 112;
      this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
      if (var4.shouldHidePlayerInventory()) {
         drawTexturedModalRect(var10, var6 + (int)((float)(var7 - var6 - 17) * this.currentScroll), 232 + (this != false ? 0 : 12), 0, 12, 15);
      }

      this.func_147051_a(var4);
      if (var4 == CreativeTabs.tabInventory) {
         GuiInventory.drawEntityOnScreen((float)(this.guiLeft + 43), (double)(this.guiTop + 45), 20, (float)(this.guiLeft + 43 - var2), (float)(this.guiTop + 45 - 30 - var3), Minecraft.thePlayer);
      }

   }

   public void drawScreen(int var1, int var2, float var3) {
      boolean var4 = Mouse.isButtonDown(0);
      int var5 = this.guiLeft;
      int var6 = this.guiTop;
      int var7 = var5 + 175;
      int var8 = var6 + 18;
      int var9 = var7 + 14;
      int var10 = var8 + 112;
      if (!this.wasClicking && var4 && var1 >= var7 && var2 >= var8 && var1 < var9 && var2 < var10) {
         this.isScrolling = this.needsScrollBars();
      }

      if (!var4) {
         this.isScrolling = false;
      }

      this.wasClicking = var4;
      if (this.isScrolling) {
         this.currentScroll = ((float)(var2 - var8) - 7.5F) / ((float)(var10 - var8) - 15.0F);
         this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
         ((GuiContainerCreative.ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
      }

      super.drawScreen(var1, var2, var3);
      CreativeTabs[] var14;
      int var13 = (var14 = CreativeTabs.creativeTabArray).length;

      for(int var12 = 0; var12 < var13; ++var12) {
         CreativeTabs var11 = var14[var12];
         if (var2 != 0) {
            break;
         }
      }

      if (this.field_147064_C != null && selectedTabIndex == CreativeTabs.tabInventory.getTabIndex() && this.isPointInRegion(this.field_147064_C.xDisplayPosition, this.field_147064_C.yDisplayPosition, 16, 16, var1, var2)) {
         this.drawCreativeTabHoveringText(I18n.format("inventory.binSlot"), var1, var2);
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableLighting();
   }

   public void initGui() {
      if (Minecraft.playerController.isInCreativeMode()) {
         super.initGui();
         this.buttonList.clear();
         Keyboard.enableRepeatEvents(true);
         this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRendererObj.FONT_HEIGHT);
         this.searchField.setMaxStringLength(15);
         this.searchField.setEnableBackgroundDrawing(false);
         this.searchField.setVisible(false);
         this.searchField.setTextColor(16777215);
         int var1 = selectedTabIndex;
         selectedTabIndex = -1;
         this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[var1]);
         this.field_147059_E = new CreativeCrafting(this.mc);
         Minecraft.thePlayer.inventoryContainer.onCraftGuiOpened(this.field_147059_E);
      } else {
         this.mc.displayGuiScreen(new GuiInventory(Minecraft.thePlayer));
      }

   }

   protected void mouseReleased(int var1, int var2, int var3) {
      if (var3 == 0) {
         int var4 = var1 - this.guiLeft;
         int var5 = var2 - this.guiTop;
         CreativeTabs[] var9;
         int var8 = (var9 = CreativeTabs.creativeTabArray).length;

         for(int var7 = 0; var7 < var8; ++var7) {
            CreativeTabs var6 = var9[var7];
            if (var5 != 0) {
               this.setCurrentCreativeTab(var6);
               return;
            }
         }
      }

      super.mouseReleased(var1, var2, var3);
   }

   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      CreativeTabs var3 = CreativeTabs.creativeTabArray[selectedTabIndex];
      if (var3.drawInForegroundOfTab()) {
         GlStateManager.disableBlend();
         this.fontRendererObj.drawString(I18n.format(var3.getTranslatedTabLabel()), 8.0D, 6.0D, 4210752);
      }

   }

   protected void updateActivePotionEffects() {
      int var1 = this.guiLeft;
      super.updateActivePotionEffects();
      if (this.searchField != null && this.guiLeft != var1) {
         this.searchField.xPosition = this.guiLeft + 82;
      }

   }

   protected void renderToolTip(ItemStack var1, int var2, int var3) {
      if (selectedTabIndex == CreativeTabs.tabAllSearch.getTabIndex()) {
         List var4 = var1.getTooltip(Minecraft.thePlayer, this.mc.gameSettings.advancedItemTooltips);
         CreativeTabs var5 = var1.getItem().getCreativeTab();
         if (var5 == null && var1.getItem() == Items.enchanted_book) {
            Map var6 = EnchantmentHelper.getEnchantments(var1);
            if (var6.size() == 1) {
               Enchantment var7 = Enchantment.getEnchantmentById((Integer)var6.keySet().iterator().next());
               CreativeTabs[] var11;
               int var10 = (var11 = CreativeTabs.creativeTabArray).length;

               for(int var9 = 0; var9 < var10; ++var9) {
                  CreativeTabs var8 = var11[var9];
                  if (var8.hasRelevantEnchantmentType(var7.type)) {
                     var5 = var8;
                     break;
                  }
               }
            }
         }

         if (var5 != null) {
            var4.add(1, "" + EnumChatFormatting.BOLD + EnumChatFormatting.BLUE + I18n.format(var5.getTranslatedTabLabel()));
         }

         for(int var12 = 0; var12 < var4.size(); ++var12) {
            if (var12 == 0) {
               var4.set(var12, var1.getRarity().rarityColor + (String)var4.get(var12));
            } else {
               var4.set(var12, EnumChatFormatting.GRAY + (String)var4.get(var12));
            }
         }

         this.drawHoveringText(var4, var2, var3);
      } else {
         super.renderToolTip(var1, var2, var3);
      }

   }

   public int getSelectedTabIndex() {
      return selectedTabIndex;
   }

   static InventoryBasic access$0() {
      return field_147060_v;
   }

   protected void keyTyped(char var1, int var2) throws IOException {
      if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex()) {
         if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat)) {
            this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
         } else {
            super.keyTyped(var1, var2);
         }
      } else {
         if (this.field_147057_D) {
            this.field_147057_D = false;
            this.searchField.setText("");
         }

         if (!this.checkHotbarKeys(var2)) {
            if (this.searchField.textboxKeyTyped(var1, var2)) {
               this.updateCreativeSearch();
            } else {
               super.keyTyped(var1, var2);
            }
         }
      }

   }

   static {
      selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();
   }

   public void onGuiClosed() {
      super.onGuiClosed();
      if (Minecraft.thePlayer != null && Minecraft.thePlayer.inventory != null) {
         Minecraft.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_147059_E);
      }

      Keyboard.enableRepeatEvents(false);
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.id == 0) {
         this.mc.displayGuiScreen(new GuiAchievements(this, Minecraft.thePlayer.getStatFileWriter()));
      }

      if (var1.id == 1) {
         this.mc.displayGuiScreen(new GuiStats(this, Minecraft.thePlayer.getStatFileWriter()));
      }

   }

   public void handleMouseInput() throws IOException {
      // $FF: Couldn't be decompiled
   }

   protected void func_147051_a(CreativeTabs var1) {
      boolean var2 = var1.getTabIndex() == selectedTabIndex;
      boolean var3 = var1.isTabInFirstRow();
      int var4 = var1.getTabColumn();
      int var5 = var4 * 28;
      int var6 = 0;
      int var7 = this.guiLeft + 28 * var4;
      int var8 = this.guiTop;
      byte var9 = 32;
      if (var2) {
         var6 += 32;
      }

      if (var4 == 5) {
         var7 = this.guiLeft + this.xSize - 28;
      } else if (var4 > 0) {
         var7 += var4;
      }

      if (var3) {
         var8 -= 28;
      } else {
         var6 += 64;
         var8 += this.ySize - 4;
      }

      GlStateManager.disableLighting();
      drawTexturedModalRect(var7, var8, var5, var6, 28, var9);
      zLevel = 100.0F;
      this.itemRender.zLevel = 100.0F;
      var7 += 6;
      var8 = var8 + 8 + (var3 ? 1 : -1);
      GlStateManager.enableLighting();
      GlStateManager.enableRescaleNormal();
      ItemStack var10 = var1.getIconItemStack();
      this.itemRender.renderItemAndEffectIntoGUI(var10, var7, var8);
      this.itemRender.renderItemOverlays(this.fontRendererObj, var10, var7, var8);
      GlStateManager.disableLighting();
      this.itemRender.zLevel = 0.0F;
      zLevel = 0.0F;
   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      if (var3 == 0) {
         int var4 = var1 - this.guiLeft;
         int var5 = var2 - this.guiTop;
         CreativeTabs[] var9;
         int var8 = (var9 = CreativeTabs.creativeTabArray).length;

         for(int var7 = 0; var7 < var8; ++var7) {
            CreativeTabs var6 = var9[var7];
            if (var4 == var5) {
               return;
            }
         }
      }

      super.mouseClicked(var1, var2, var3);
   }

   protected void handleMouseClick(Slot var1, int var2, int var3, int var4) {
      this.field_147057_D = true;
      boolean var5 = var4 == 1;
      var4 = var2 == -999 && var4 == 0 ? 4 : var4;
      ItemStack var7;
      InventoryPlayer var11;
      if (var1 == null && selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && var4 != 5) {
         var11 = Minecraft.thePlayer.inventory;
         if (var11.getItemStack() != null) {
            if (var3 == 0) {
               Minecraft.thePlayer.dropPlayerItemWithRandomChoice(var11.getItemStack(), true);
               Minecraft.playerController.sendPacketDropItem(var11.getItemStack());
               var11.setItemStack((ItemStack)null);
            }

            if (var3 == 1) {
               var7 = var11.getItemStack().splitStack(1);
               Minecraft.thePlayer.dropPlayerItemWithRandomChoice(var7, true);
               Minecraft.playerController.sendPacketDropItem(var7);
               if (var11.getItemStack().stackSize == 0) {
                  var11.setItemStack((ItemStack)null);
               }
            }
         }
      } else {
         int var10;
         if (var1 == this.field_147064_C && var5) {
            for(var10 = 0; var10 < Minecraft.thePlayer.inventoryContainer.getInventory().size(); ++var10) {
               Minecraft.playerController.sendSlotPacket((ItemStack)null, var10);
            }
         } else {
            ItemStack var6;
            if (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()) {
               if (var1 == this.field_147064_C) {
                  Minecraft.thePlayer.inventory.setItemStack((ItemStack)null);
               } else if (var4 == 4 && var1 != null && var1.getHasStack()) {
                  var6 = var1.decrStackSize(var3 == 0 ? 1 : var1.getStack().getMaxStackSize());
                  Minecraft.thePlayer.dropPlayerItemWithRandomChoice(var6, true);
                  Minecraft.playerController.sendPacketDropItem(var6);
               } else if (var4 == 4 && Minecraft.thePlayer.inventory.getItemStack() != null) {
                  Minecraft.thePlayer.dropPlayerItemWithRandomChoice(Minecraft.thePlayer.inventory.getItemStack(), true);
                  Minecraft.playerController.sendPacketDropItem(Minecraft.thePlayer.inventory.getItemStack());
                  Minecraft.thePlayer.inventory.setItemStack((ItemStack)null);
               } else {
                  Minecraft.thePlayer.inventoryContainer.slotClick(var1 == null ? var2 : GuiContainerCreative.CreativeSlot.access$0((GuiContainerCreative.CreativeSlot)var1).slotNumber, var3, var4, Minecraft.thePlayer);
                  Minecraft.thePlayer.inventoryContainer.detectAndSendChanges();
               }
            } else if (var4 != 5 && var1.inventory == field_147060_v) {
               var11 = Minecraft.thePlayer.inventory;
               var7 = var11.getItemStack();
               ItemStack var8 = var1.getStack();
               ItemStack var9;
               if (var4 == 2) {
                  if (var8 != null && var3 >= 0 && var3 < 9) {
                     var9 = var8.copy();
                     var9.stackSize = var9.getMaxStackSize();
                     Minecraft.thePlayer.inventory.setInventorySlotContents(var3, var9);
                     Minecraft.thePlayer.inventoryContainer.detectAndSendChanges();
                  }

                  return;
               }

               if (var4 == 3) {
                  if (var11.getItemStack() == null && var1.getHasStack()) {
                     var9 = var1.getStack().copy();
                     var9.stackSize = var9.getMaxStackSize();
                     var11.setItemStack(var9);
                  }

                  return;
               }

               if (var4 == 4) {
                  if (var8 != null) {
                     var9 = var8.copy();
                     var9.stackSize = var3 == 0 ? 1 : var9.getMaxStackSize();
                     Minecraft.thePlayer.dropPlayerItemWithRandomChoice(var9, true);
                     Minecraft.playerController.sendPacketDropItem(var9);
                  }

                  return;
               }

               if (var7 != null && var8 != null && var7.isItemEqual(var8)) {
                  if (var3 == 0) {
                     if (var5) {
                        var7.stackSize = var7.getMaxStackSize();
                     } else if (var7.stackSize < var7.getMaxStackSize()) {
                        ++var7.stackSize;
                     }
                  } else if (var7.stackSize <= 1) {
                     var11.setItemStack((ItemStack)null);
                  } else {
                     --var7.stackSize;
                  }
               } else if (var8 != null && var7 == null) {
                  var11.setItemStack(ItemStack.copyItemStack(var8));
                  var7 = var11.getItemStack();
                  if (var5) {
                     var7.stackSize = var7.getMaxStackSize();
                  }
               } else {
                  var11.setItemStack((ItemStack)null);
               }
            } else {
               this.inventorySlots.slotClick(var1 == null ? var2 : var1.slotNumber, var3, var4, Minecraft.thePlayer);
               if (Container.getDragEvent(var3) == 2) {
                  for(var10 = 0; var10 < 9; ++var10) {
                     Minecraft.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + var10).getStack(), 36 + var10);
                  }
               } else if (var1 != null) {
                  var6 = this.inventorySlots.getSlot(var1.slotNumber).getStack();
                  Minecraft.playerController.sendSlotPacket(var6, var1.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
               }
            }
         }
      }

   }

   private void setCurrentCreativeTab(CreativeTabs var1) {
      int var2 = selectedTabIndex;
      selectedTabIndex = var1.getTabIndex();
      GuiContainerCreative.ContainerCreative var3 = (GuiContainerCreative.ContainerCreative)this.inventorySlots;
      this.dragSplittingSlots.clear();
      var3.itemList.clear();
      var1.displayAllReleventItems(var3.itemList);
      if (var1 == CreativeTabs.tabInventory) {
         Container var4 = Minecraft.thePlayer.inventoryContainer;
         if (this.field_147063_B == null) {
            this.field_147063_B = var3.inventorySlots;
         }

         var3.inventorySlots = Lists.newArrayList();

         for(int var5 = 0; var5 < var4.inventorySlots.size(); ++var5) {
            GuiContainerCreative.CreativeSlot var6 = new GuiContainerCreative.CreativeSlot(this, (Slot)var4.inventorySlots.get(var5), var5);
            var3.inventorySlots.add(var6);
            int var7;
            int var8;
            int var9;
            if (var5 >= 5 && var5 < 9) {
               var7 = var5 - 5;
               var8 = var7 / 2;
               var9 = var7 % 2;
               var6.xDisplayPosition = 9 + var8 * 54;
               var6.yDisplayPosition = 6 + var9 * 27;
            } else if (var5 >= 0 && var5 < 5) {
               var6.yDisplayPosition = -2000;
               var6.xDisplayPosition = -2000;
            } else if (var5 < var4.inventorySlots.size()) {
               var7 = var5 - 9;
               var8 = var7 % 9;
               var9 = var7 / 9;
               var6.xDisplayPosition = 9 + var8 * 18;
               if (var5 >= 36) {
                  var6.yDisplayPosition = 112;
               } else {
                  var6.yDisplayPosition = 54 + var9 * 18;
               }
            }
         }

         this.field_147064_C = new Slot(field_147060_v, 0, 173, 112);
         var3.inventorySlots.add(this.field_147064_C);
      } else if (var2 == CreativeTabs.tabInventory.getTabIndex()) {
         var3.inventorySlots = this.field_147063_B;
         this.field_147063_B = null;
      }

      if (this.searchField != null) {
         if (var1 == CreativeTabs.tabAllSearch) {
            this.searchField.setVisible(true);
            this.searchField.setCanLoseFocus(false);
            this.searchField.setFocused(true);
            this.searchField.setText("");
            this.updateCreativeSearch();
         } else {
            this.searchField.setVisible(false);
            this.searchField.setCanLoseFocus(true);
            this.searchField.setFocused(false);
         }
      }

      this.currentScroll = 0.0F;
      var3.scrollTo(0.0F);
   }

   private void updateCreativeSearch() {
      GuiContainerCreative.ContainerCreative var1 = (GuiContainerCreative.ContainerCreative)this.inventorySlots;
      var1.itemList.clear();
      Iterator var3 = Item.itemRegistry.iterator();

      while(var3.hasNext()) {
         Item var2 = (Item)var3.next();
         if (var2 != null && var2.getCreativeTab() != null) {
            var2.getSubItems(var2, (CreativeTabs)null, var1.itemList);
         }
      }

      Enchantment[] var5;
      int var4 = (var5 = Enchantment.enchantmentsBookList).length;

      for(int var10 = 0; var10 < var4; ++var10) {
         Enchantment var8 = var5[var10];
         if (var8 != null && var8.type != null) {
            Items.enchanted_book.getAll(var8, var1.itemList);
         }
      }

      Iterator var9 = var1.itemList.iterator();
      String var11 = this.searchField.getText().toLowerCase();

      while(var9.hasNext()) {
         ItemStack var12 = (ItemStack)var9.next();
         boolean var13 = false;
         Iterator var7 = var12.getTooltip(Minecraft.thePlayer, this.mc.gameSettings.advancedItemTooltips).iterator();

         while(var7.hasNext()) {
            String var6 = (String)var7.next();
            if (EnumChatFormatting.getTextWithoutFormattingCodes(var6).toLowerCase().contains(var11)) {
               var13 = true;
               break;
            }
         }

         if (!var13) {
            var9.remove();
         }
      }

      this.currentScroll = 0.0F;
      var1.scrollTo(0.0F);
   }

   public void updateScreen() {
      if (!Minecraft.playerController.isInCreativeMode()) {
         this.mc.displayGuiScreen(new GuiInventory(Minecraft.thePlayer));
      }

      this.updateActivePotionEffects();
   }

   public GuiContainerCreative(EntityPlayer var1) {
      super(new GuiContainerCreative.ContainerCreative(var1));
      var1.openContainer = this.inventorySlots;
      this.allowUserInput = true;
      this.ySize = 136;
      this.xSize = 195;
   }

   class CreativeSlot extends Slot {
      private final Slot slot;
      final GuiContainerCreative this$0;

      public String getSlotTexture() {
         return this.slot.getSlotTexture();
      }

      public boolean isHere(IInventory var1, int var2) {
         return this.slot.isHere(var1, var2);
      }

      public ItemStack decrStackSize(int var1) {
         return this.slot.decrStackSize(var1);
      }

      public int getItemStackLimit(ItemStack var1) {
         return this.slot.getItemStackLimit(var1);
      }

      public ItemStack getStack() {
         return this.slot.getStack();
      }

      public boolean getHasStack() {
         return this.slot.getHasStack();
      }

      public void onPickupFromSlot(EntityPlayer var1, ItemStack var2) {
         this.slot.onPickupFromSlot(var1, var2);
      }

      public boolean isItemValid(ItemStack var1) {
         return this.slot.isItemValid(var1);
      }

      public int getSlotStackLimit() {
         return this.slot.getSlotStackLimit();
      }

      public void putStack(ItemStack var1) {
         this.slot.putStack(var1);
      }

      public void onSlotChanged() {
         this.slot.onSlotChanged();
      }

      static Slot access$0(GuiContainerCreative.CreativeSlot var0) {
         return var0.slot;
      }

      public CreativeSlot(GuiContainerCreative var1, Slot var2, int var3) {
         super(var2.inventory, var3, 0, 0);
         this.this$0 = var1;
         this.slot = var2;
      }
   }

   static class ContainerCreative extends Container {
      public List itemList = Lists.newArrayList();

      public ContainerCreative(EntityPlayer var1) {
         InventoryPlayer var2 = var1.inventory;

         int var3;
         for(var3 = 0; var3 < 5; ++var3) {
            for(int var4 = 0; var4 < 9; ++var4) {
               this.addSlotToContainer(new Slot(GuiContainerCreative.access$0(), var3 * 9 + var4, 9 + var4 * 18, 18 + var3 * 18));
            }
         }

         for(var3 = 0; var3 < 9; ++var3) {
            this.addSlotToContainer(new Slot(var2, var3, 9 + var3 * 18, 112));
         }

         this.scrollTo(0.0F);
      }

      public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
         if (var2 >= this.inventorySlots.size() - 9 && var2 < this.inventorySlots.size()) {
            Slot var3 = (Slot)this.inventorySlots.get(var2);
            if (var3 != null && var3.getHasStack()) {
               var3.putStack((ItemStack)null);
            }
         }

         return null;
      }

      public void scrollTo(float var1) {
         int var2 = (this.itemList.size() + 9 - 1) / 9 - 5;
         int var3 = (int)((double)(var1 * (float)var2) + 0.5D);
         if (var3 < 0) {
            var3 = 0;
         }

         for(int var4 = 0; var4 < 5; ++var4) {
            for(int var5 = 0; var5 < 9; ++var5) {
               int var6 = var5 + (var4 + var3) * 9;
               if (var6 >= 0 && var6 < this.itemList.size()) {
                  GuiContainerCreative.access$0().setInventorySlotContents(var5 + var4 * 9, (ItemStack)this.itemList.get(var6));
               } else {
                  GuiContainerCreative.access$0().setInventorySlotContents(var5 + var4 * 9, (ItemStack)null);
               }
            }
         }

      }

      public boolean canDragIntoSlot(Slot var1) {
         return var1.inventory instanceof InventoryPlayer || var1.yDisplayPosition > 90 && var1.xDisplayPosition <= 162;
      }

      protected void retrySlotClick(int var1, int var2, boolean var3, EntityPlayer var4) {
      }

      public boolean canMergeSlot(ItemStack var1, Slot var2) {
         return var2.yDisplayPosition > 90;
      }

      public boolean canInteractWith(EntityPlayer var1) {
         return true;
      }

      public boolean func_148328_e() {
         return this.itemList.size() > 45;
      }
   }
}
