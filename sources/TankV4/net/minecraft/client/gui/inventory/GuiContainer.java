package net.minecraft.client.gui.inventory;

import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public abstract class GuiContainer extends GuiScreen {
   private Slot lastClickSlot;
   private Slot clickedSlot;
   private int dragSplittingLimit;
   private boolean isRightMouseClick;
   public Container inventorySlots;
   private int touchUpY;
   private Slot currentDragTargetSlot;
   protected static final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
   private long lastClickTime;
   private ItemStack shiftClickedSlot;
   private Slot returningStackDestSlot;
   private int dragSplittingButton;
   private long dragItemDropDelay;
   private boolean doubleClick;
   protected final Set dragSplittingSlots = Sets.newHashSet();
   protected boolean dragSplitting;
   private boolean ignoreMouseUp;
   private ItemStack returningStack;
   protected int guiLeft;
   private ItemStack draggedStack;
   private int touchUpX;
   private int lastClickButton;
   private Slot theSlot;
   protected int ySize = 166;
   private int dragSplittingRemnant;
   protected int xSize = 176;
   private long returningStackTime;
   protected int guiTop;

   public void onGuiClosed() {
      if (Minecraft.thePlayer != null) {
         this.inventorySlots.onContainerClosed(Minecraft.thePlayer);
      }

   }

   protected void mouseReleased(int var1, int var2, int var3) {
      Slot var4 = this.getSlotAtPosition(var1, var2);
      int var5 = this.guiLeft;
      int var6 = this.guiTop;
      boolean var7 = var1 < var5 || var2 < var6 || var1 >= var5 + this.xSize || var2 >= var6 + this.ySize;
      int var8 = -1;
      if (var4 != null) {
         var8 = var4.slotNumber;
      }

      if (var7) {
         var8 = -999;
      }

      Iterator var10;
      Slot var11;
      if (this.doubleClick && var4 != null && var3 == 0 && this.inventorySlots.canMergeSlot((ItemStack)null, var4)) {
         if (isShiftKeyDown()) {
            if (var4 != null && var4.inventory != null && this.shiftClickedSlot != null) {
               var10 = this.inventorySlots.inventorySlots.iterator();

               while(var10.hasNext()) {
                  var11 = (Slot)var10.next();
                  if (var11 != null && var11.canTakeStack(Minecraft.thePlayer) && var11.getHasStack() && var11.inventory == var4.inventory && Container.canAddItemToSlot(var11, this.shiftClickedSlot, true)) {
                     this.handleMouseClick(var11, var11.slotNumber, var3, 1);
                  }
               }
            }
         } else {
            this.handleMouseClick(var4, var8, var3, 6);
         }

         this.doubleClick = false;
         this.lastClickTime = 0L;
      } else {
         if (this.dragSplitting && this.dragSplittingButton != var3) {
            this.dragSplitting = false;
            this.dragSplittingSlots.clear();
            this.ignoreMouseUp = true;
            return;
         }

         if (this.ignoreMouseUp) {
            this.ignoreMouseUp = false;
            return;
         }

         boolean var9;
         if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
            if (var3 == 0 || var3 == 1) {
               if (this.draggedStack == null && var4 != this.clickedSlot) {
                  this.draggedStack = this.clickedSlot.getStack();
               }

               var9 = Container.canAddItemToSlot(var4, this.draggedStack, false);
               if (var8 != -1 && this.draggedStack != null && var9) {
                  this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, var3, 0);
                  this.handleMouseClick(var4, var8, 0, 0);
                  if (Minecraft.thePlayer.inventory.getItemStack() != null) {
                     this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, var3, 0);
                     this.touchUpX = var1 - var5;
                     this.touchUpY = var2 - var6;
                     this.returningStackDestSlot = this.clickedSlot;
                     this.returningStack = this.draggedStack;
                     this.returningStackTime = Minecraft.getSystemTime();
                  } else {
                     this.returningStack = null;
                  }
               } else if (this.draggedStack != null) {
                  this.touchUpX = var1 - var5;
                  this.touchUpY = var2 - var6;
                  this.returningStackDestSlot = this.clickedSlot;
                  this.returningStack = this.draggedStack;
                  this.returningStackTime = Minecraft.getSystemTime();
               }

               this.draggedStack = null;
               this.clickedSlot = null;
            }
         } else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty()) {
            this.handleMouseClick((Slot)null, -999, Container.func_94534_d(0, this.dragSplittingLimit), 5);
            var10 = this.dragSplittingSlots.iterator();

            while(var10.hasNext()) {
               var11 = (Slot)var10.next();
               this.handleMouseClick(var11, var11.slotNumber, Container.func_94534_d(1, this.dragSplittingLimit), 5);
            }

            this.handleMouseClick((Slot)null, -999, Container.func_94534_d(2, this.dragSplittingLimit), 5);
         } else if (Minecraft.thePlayer.inventory.getItemStack() != null) {
            if (var3 == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
               this.handleMouseClick(var4, var8, var3, 3);
            } else {
               var9 = var8 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
               if (var9) {
                  this.shiftClickedSlot = var4 != null && var4.getHasStack() ? var4.getStack() : null;
               }

               this.handleMouseClick(var4, var8, var3, var9 ? 1 : 0);
            }
         }
      }

      if (Minecraft.thePlayer.inventory.getItemStack() == null) {
         this.lastClickTime = 0L;
      }

      this.dragSplitting = false;
   }

   protected void mouseClickMove(int var1, int var2, int var3, long var4) {
      Slot var6 = this.getSlotAtPosition(var1, var2);
      ItemStack var7 = Minecraft.thePlayer.inventory.getItemStack();
      if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
         if (var3 == 0 || var3 == 1) {
            if (this.draggedStack == null) {
               if (var6 != this.clickedSlot && this.clickedSlot.getStack() != null) {
                  this.draggedStack = this.clickedSlot.getStack().copy();
               }
            } else if (this.draggedStack.stackSize > 1 && var6 != null && Container.canAddItemToSlot(var6, this.draggedStack, false)) {
               long var8 = Minecraft.getSystemTime();
               if (this.currentDragTargetSlot == var6) {
                  if (var8 - this.dragItemDropDelay > 500L) {
                     this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
                     this.handleMouseClick(var6, var6.slotNumber, 1, 0);
                     this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
                     this.dragItemDropDelay = var8 + 750L;
                     --this.draggedStack.stackSize;
                  }
               } else {
                  this.currentDragTargetSlot = var6;
                  this.dragItemDropDelay = var8;
               }
            }
         }
      } else if (this.dragSplitting && var6 != null && var7 != null && var7.stackSize > this.dragSplittingSlots.size() && Container.canAddItemToSlot(var6, var7, true) && var6.isItemValid(var7) && this.inventorySlots.canDragIntoSlot(var6)) {
         this.dragSplittingSlots.add(var6);
         this.updateDragSplitting();
      }

   }

   protected void keyTyped(char var1, int var2) throws IOException {
      if (var2 == 1 || var2 == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
         Minecraft.thePlayer.closeScreen();
      }

      this.checkHotbarKeys(var2);
      if (this.theSlot != null && this.theSlot.getHasStack()) {
         if (var2 == this.mc.gameSettings.keyBindPickBlock.getKeyCode()) {
            this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, 3);
         } else if (var2 == this.mc.gameSettings.keyBindDrop.getKeyCode()) {
            this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, 4);
         }
      }

   }

   private void updateDragSplitting() {
      ItemStack var1 = Minecraft.thePlayer.inventory.getItemStack();
      if (var1 != null && this.dragSplitting) {
         this.dragSplittingRemnant = var1.stackSize;

         ItemStack var4;
         int var5;
         for(Iterator var3 = this.dragSplittingSlots.iterator(); var3.hasNext(); this.dragSplittingRemnant -= var4.stackSize - var5) {
            Slot var2 = (Slot)var3.next();
            var4 = var1.copy();
            var5 = var2.getStack() == null ? 0 : var2.getStack().stackSize;
            Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, var4, var5);
            if (var4.stackSize > var4.getMaxStackSize()) {
               var4.stackSize = var4.getMaxStackSize();
            }

            if (var4.stackSize > var2.getItemStackLimit(var4)) {
               var4.stackSize = var2.getItemStackLimit(var4);
            }
         }
      }

   }

   private void drawSlot(Slot var1) {
      int var2 = var1.xDisplayPosition;
      int var3 = var1.yDisplayPosition;
      ItemStack var4 = var1.getStack();
      boolean var5 = false;
      boolean var6 = var1 == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick;
      ItemStack var7 = Minecraft.thePlayer.inventory.getItemStack();
      String var8 = null;
      if (var1 == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && var4 != null) {
         var4 = var4.copy();
         var4.stackSize /= 2;
      } else if (this.dragSplitting && this.dragSplittingSlots.contains(var1) && var7 != null) {
         if (this.dragSplittingSlots.size() == 1) {
            return;
         }

         if (Container.canAddItemToSlot(var1, var7, true) && this.inventorySlots.canDragIntoSlot(var1)) {
            var4 = var7.copy();
            var5 = true;
            Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, var4, var1.getStack() == null ? 0 : var1.getStack().stackSize);
            if (var4.stackSize > var4.getMaxStackSize()) {
               var8 = "" + EnumChatFormatting.YELLOW + var4.getMaxStackSize();
               var4.stackSize = var4.getMaxStackSize();
            }

            if (var4.stackSize > var1.getItemStackLimit(var4)) {
               var8 = "" + EnumChatFormatting.YELLOW + var1.getItemStackLimit(var4);
               var4.stackSize = var1.getItemStackLimit(var4);
            }
         } else {
            this.dragSplittingSlots.remove(var1);
            this.updateDragSplitting();
         }
      }

      zLevel = 100.0F;
      this.itemRender.zLevel = 100.0F;
      if (var4 == null) {
         String var9 = var1.getSlotTexture();
         if (var9 != null) {
            TextureAtlasSprite var10 = this.mc.getTextureMapBlocks().getAtlasSprite(var9);
            GlStateManager.disableLighting();
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            this.drawTexturedModalRect(var2, var3, var10, 16, 16);
            GlStateManager.enableLighting();
            var6 = true;
         }
      }

      if (!var6) {
         if (var5) {
            drawRect((double)var2, (double)var3, (double)(var2 + 16), (double)(var3 + 16), -2130706433);
         }

         GlStateManager.enableDepth();
         this.itemRender.renderItemAndEffectIntoGUI(var4, var2, var3);
         this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, var4, var2, var3, var8);
      }

      this.itemRender.zLevel = 0.0F;
      zLevel = 0.0F;
   }

   private void drawItemStack(ItemStack var1, int var2, int var3, String var4) {
      GlStateManager.translate(0.0F, 0.0F, 32.0F);
      zLevel = 200.0F;
      this.itemRender.zLevel = 200.0F;
      this.itemRender.renderItemAndEffectIntoGUI(var1, var2, var3);
      this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, var1, var2, var3 - (this.draggedStack == null ? 0 : 8), var4);
      zLevel = 0.0F;
      this.itemRender.zLevel = 0.0F;
   }

   private boolean isMouseOverSlot(Slot var1, int var2, int var3) {
      return this.isPointInRegion(var1.xDisplayPosition, var1.yDisplayPosition, 16, 16, var2, var3);
   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      super.mouseClicked(var1, var2, var3);
      boolean var4 = var3 == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100;
      Slot var5 = this.getSlotAtPosition(var1, var2);
      long var6 = Minecraft.getSystemTime();
      this.doubleClick = this.lastClickSlot == var5 && var6 - this.lastClickTime < 250L && this.lastClickButton == var3;
      this.ignoreMouseUp = false;
      if (var3 == 0 || var3 == 1 || var4) {
         int var8 = this.guiLeft;
         int var9 = this.guiTop;
         boolean var10 = var1 < var8 || var2 < var9 || var1 >= var8 + this.xSize || var2 >= var9 + this.ySize;
         int var11 = -1;
         if (var5 != null) {
            var11 = var5.slotNumber;
         }

         if (var10) {
            var11 = -999;
         }

         if (this.mc.gameSettings.touchscreen && var10 && Minecraft.thePlayer.inventory.getItemStack() == null) {
            this.mc.displayGuiScreen((GuiScreen)null);
            return;
         }

         if (var11 != -1) {
            if (this.mc.gameSettings.touchscreen) {
               if (var5 != null && var5.getHasStack()) {
                  this.clickedSlot = var5;
                  this.draggedStack = null;
                  this.isRightMouseClick = var3 == 1;
               } else {
                  this.clickedSlot = null;
               }
            } else if (!this.dragSplitting) {
               if (Minecraft.thePlayer.inventory.getItemStack() == null) {
                  if (var3 == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                     this.handleMouseClick(var5, var11, var3, 3);
                  } else {
                     boolean var12 = var11 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                     byte var13 = 0;
                     if (var12) {
                        this.shiftClickedSlot = var5 != null && var5.getHasStack() ? var5.getStack() : null;
                        var13 = 1;
                     } else if (var11 == -999) {
                        var13 = 4;
                     }

                     this.handleMouseClick(var5, var11, var3, var13);
                  }

                  this.ignoreMouseUp = true;
               } else {
                  this.dragSplitting = true;
                  this.dragSplittingButton = var3;
                  this.dragSplittingSlots.clear();
                  if (var3 == 0) {
                     this.dragSplittingLimit = 0;
                  } else if (var3 == 1) {
                     this.dragSplittingLimit = 1;
                  } else if (var3 == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                     this.dragSplittingLimit = 2;
                  }
               }
            }
         }
      }

      this.lastClickSlot = var5;
      this.lastClickTime = var6;
      this.lastClickButton = var3;
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      int var4 = this.guiLeft;
      int var5 = this.guiTop;
      this.drawGuiContainerBackgroundLayer(var3, var1, var2);
      GlStateManager.disableRescaleNormal();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      super.drawScreen(var1, var2, var3);
      RenderHelper.enableGUIStandardItemLighting();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var4, (float)var5, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableRescaleNormal();
      this.theSlot = null;
      short var6 = 240;
      short var7 = 240;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

      int var11;
      for(int var8 = 0; var8 < this.inventorySlots.inventorySlots.size(); ++var8) {
         Slot var9 = (Slot)this.inventorySlots.inventorySlots.get(var8);
         this.drawSlot(var9);
         if (this.isMouseOverSlot(var9, var1, var2) && var9.canBeHovered()) {
            this.theSlot = var9;
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int var10 = var9.xDisplayPosition;
            var11 = var9.yDisplayPosition;
            GlStateManager.colorMask(true, true, true, false);
            drawGradientRect((double)var10, (double)var11, (float)(var10 + 16), (float)(var11 + 16), -2130706433, -2130706433);
            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
         }
      }

      RenderHelper.disableStandardItemLighting();
      this.drawGuiContainerForegroundLayer(var1, var2);
      RenderHelper.enableGUIStandardItemLighting();
      InventoryPlayer var15 = Minecraft.thePlayer.inventory;
      ItemStack var16 = this.draggedStack == null ? var15.getItemStack() : this.draggedStack;
      if (var16 != null) {
         byte var17 = 8;
         var11 = this.draggedStack == null ? 8 : 16;
         String var12 = null;
         if (this.draggedStack != null && this.isRightMouseClick) {
            var16 = var16.copy();
            var16.stackSize = MathHelper.ceiling_float_int((float)var16.stackSize / 2.0F);
         } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
            var16 = var16.copy();
            var16.stackSize = this.dragSplittingRemnant;
            if (var16.stackSize == 0) {
               var12 = EnumChatFormatting.YELLOW + "0";
            }
         }

         this.drawItemStack(var16, var1 - var4 - var17, var2 - var5 - var11, var12);
      }

      if (this.returningStack != null) {
         float var18 = (float)(Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;
         if (var18 >= 1.0F) {
            var18 = 1.0F;
            this.returningStack = null;
         }

         var11 = this.returningStackDestSlot.xDisplayPosition - this.touchUpX;
         int var20 = this.returningStackDestSlot.yDisplayPosition - this.touchUpY;
         int var13 = this.touchUpX + (int)((float)var11 * var18);
         int var14 = this.touchUpY + (int)((float)var20 * var18);
         this.drawItemStack(this.returningStack, var13, var14, (String)null);
      }

      GlStateManager.popMatrix();
      if (var15.getItemStack() == null && this.theSlot != null && this.theSlot.getHasStack()) {
         ItemStack var19 = this.theSlot.getStack();
         this.renderToolTip(var19, var1, var2);
      }

      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      RenderHelper.enableStandardItemLighting();
   }

   private Slot getSlotAtPosition(int var1, int var2) {
      for(int var3 = 0; var3 < this.inventorySlots.inventorySlots.size(); ++var3) {
         Slot var4 = (Slot)this.inventorySlots.inventorySlots.get(var3);
         if (this.isMouseOverSlot(var4, var1, var2)) {
            return var4;
         }
      }

      return null;
   }

   protected boolean isPointInRegion(int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = this.guiLeft;
      int var8 = this.guiTop;
      var5 -= var7;
      var6 -= var8;
      return var5 >= var1 - 1 && var5 < var1 + var3 + 1 && var6 >= var2 - 1 && var6 < var2 + var4 + 1;
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   public GuiContainer(Container var1) {
      this.inventorySlots = var1;
      this.ignoreMouseUp = true;
   }

   public void initGui() {
      super.initGui();
      Minecraft.thePlayer.openContainer = this.inventorySlots;
      this.guiLeft = (width - this.xSize) / 2;
      this.guiTop = (height - this.ySize) / 2;
   }

   protected void handleMouseClick(Slot var1, int var2, int var3, int var4) {
      if (var1 != null) {
         var2 = var1.slotNumber;
      }

      Minecraft.playerController.windowClick(this.inventorySlots.windowId, var2, var3, var4, Minecraft.thePlayer);
   }

   protected abstract void drawGuiContainerBackgroundLayer(float var1, int var2, int var3);

   public void updateScreen() {
      super.updateScreen();
      if (!Minecraft.thePlayer.isEntityAlive() || Minecraft.thePlayer.isDead) {
         Minecraft.thePlayer.closeScreen();
      }

   }

   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
   }

   protected boolean checkHotbarKeys(int var1) {
      if (Minecraft.thePlayer.inventory.getItemStack() == null && this.theSlot != null) {
         for(int var2 = 0; var2 < 9; ++var2) {
            if (var1 == this.mc.gameSettings.keyBindsHotbar[var2].getKeyCode()) {
               this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, var2, 2);
               return true;
            }
         }
      }

      return false;
   }
}
