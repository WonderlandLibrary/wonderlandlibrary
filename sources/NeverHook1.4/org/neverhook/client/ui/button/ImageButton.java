/*     */ package org.neverhook.client.ui.button;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.inventory.GuiChest;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.ClickType;
/*     */ import net.minecraft.inventory.ContainerChest;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.neverhook.client.helpers.input.MouseHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.ui.GuiCapeSelector;
/*     */ import org.neverhook.client.ui.GuiConfig;
/*     */ import org.neverhook.client.ui.components.draggable.GuiHudEditor;
/*     */ 
/*     */ public class ImageButton {
/*     */   protected int height;
/*     */   protected String description;
/*     */   protected int width;
/*     */   protected Minecraft mc;
/*     */   protected ResourceLocation image;
/*     */   protected int target;
/*     */   protected int x;
/*  27 */   protected int ani = 0;
/*     */   protected int y;
/*     */   
/*     */   public ImageButton(ResourceLocation resourceLocation, int x, int y, int width, int height, String description, int target) {
/*  31 */     this.image = resourceLocation;
/*  32 */     this.x = x;
/*  33 */     this.y = y;
/*  34 */     this.width = width;
/*  35 */     this.height = height;
/*  36 */     this.description = description;
/*  37 */     this.target = target;
/*  38 */     this.mc = Minecraft.getInstance();
/*     */   }
/*     */   
/*     */   protected void hoverAnimation(int mouseX, int mouseY) {
/*  42 */     if (isHovered(mouseX, mouseY)) {
/*  43 */       if (this.ani < 3) {
/*  44 */         this.ani++;
/*     */       }
/*  46 */     } else if (this.ani > 0) {
/*  47 */       this.ani--;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onClick(int mouseX, int mouseY) {
/*  52 */     if (isHovered(mouseX, mouseY)) {
/*  53 */       if (this.target == 19) {
/*  54 */         Minecraft.getInstance().displayGuiScreen(null);
/*     */       }
/*  56 */       if (this.target == 22) {
/*  57 */         Minecraft.getInstance().displayGuiScreen((GuiScreen)new GuiConfig());
/*     */       }
/*  59 */       if (this.target == 23) {
/*  60 */         Minecraft.getInstance().displayGuiScreen((GuiScreen)new GuiCapeSelector());
/*     */       }
/*  62 */       if (this.target == 18) {
/*  63 */         this.mc.displayGuiScreen((GuiScreen)new GuiHudEditor());
/*     */       }
/*  65 */       if (this.target == 30) {
/*  66 */         GuiChest chest = (GuiChest)this.mc.currentScreen;
/*  67 */         if (chest != null) {
/*  68 */           (new Thread(() -> {
/*     */                 try {
/*     */                   for (int i = 0; i < chest.getInventoryRows() * 9; i++) {
/*     */                     ContainerChest container = (ContainerChest)this.mc.player.openContainer;
/*     */                     if (container != null) {
/*     */                       Thread.sleep(50L);
/*     */                       this.mc.playerController.windowClick(container.windowId, i, 0, ClickType.QUICK_MOVE, (EntityPlayer)this.mc.player);
/*     */                     } 
/*     */                   } 
/*  77 */                 } catch (Exception exception) {}
/*     */ 
/*     */               
/*  80 */               })).start();
/*     */         }
/*     */       } 
/*  83 */       if (this.target == 31) {
/*  84 */         GuiChest chest = (GuiChest)this.mc.currentScreen;
/*  85 */         if (chest != null) {
/*  86 */           (new Thread(() -> {
/*     */                 try {
/*     */                   for (int i = chest.getInventoryRows() * 9; i < chest.getInventoryRows() * 9 + 44; i++) {
/*     */                     Slot slot = chest.inventorySlots.inventorySlots.get(i);
/*     */                     if (slot.getStack() != null) {
/*     */                       Thread.sleep(50L);
/*     */                       chest.handleMouseClick(slot, slot.slotNumber, 0, ClickType.QUICK_MOVE);
/*     */                     } 
/*     */                   } 
/*  95 */                 } catch (Exception exception) {}
/*     */ 
/*     */               
/*  98 */               })).start();
/*     */         }
/*     */       } 
/* 101 */       if (this.target == 32) {
/* 102 */         for (int i = 0; i < 46; i++) {
/* 103 */           if (this.mc.player.inventoryContainer.getSlot(i).getHasStack()) {
/* 104 */             this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, i, 1, ClickType.THROW, (EntityPlayer)this.mc.player);
/*     */           }
/*     */         } 
/*     */       }
/* 108 */       if (this.target == 55) {
/* 109 */         switch (GuiCapeSelector.Selector.getCapeName()) {
/*     */           case "neverhookcape2":
/* 111 */             GuiCapeSelector.Selector.capeName = "nhplash4";
/*     */             break;
/*     */           case "nhplash4":
/* 114 */             GuiCapeSelector.Selector.capeName = "nhplash5";
/*     */             break;
/*     */           case "nhplash5":
/* 117 */             GuiCapeSelector.Selector.capeName = "nhplash6";
/*     */             break;
/*     */           case "nhplash6":
/* 120 */             GuiCapeSelector.Selector.capeName = "neverhookcape3";
/*     */             break;
/*     */           default:
/* 123 */             GuiCapeSelector.Selector.capeName = "neverhookcape2";
/*     */             break;
/*     */         } 
/*     */       }
/* 127 */       if (this.target == 56) {
/* 128 */         switch (GuiCapeSelector.Selector.getCapeName()) {
/*     */           case "neverhookcape2":
/* 130 */             GuiCapeSelector.Selector.capeName = "nhplash4";
/*     */             return;
/*     */           case "nhplash4":
/* 133 */             GuiCapeSelector.Selector.capeName = "nhplash5";
/*     */             return;
/*     */           case "nhplash5":
/* 136 */             GuiCapeSelector.Selector.capeName = "nhplash6";
/*     */             return;
/*     */           case "nhplash6":
/* 139 */             GuiCapeSelector.Selector.capeName = "neverhookcape3";
/*     */             return;
/*     */         } 
/* 142 */         GuiCapeSelector.Selector.capeName = "neverhookcape2";
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(int mouseX, int mouseY, Color color) {
/* 150 */     GlStateManager.pushMatrix();
/* 151 */     GlStateManager.disableBlend();
/* 152 */     hoverAnimation(mouseX, mouseY);
/* 153 */     if (this.ani > 0) {
/* 154 */       RenderHelper.drawImage(this.image, (this.x - this.ani), (this.y - this.ani), (this.width + this.ani * 2), (this.height + this.ani * 2), new Color(156, 156, 156, 255));
/*     */     } else {
/* 156 */       RenderHelper.drawImage(this.image, this.x, this.y, this.width, this.height, color);
/*     */     } 
/* 158 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   protected boolean isHovered(int mouseX, int mouseY) {
/* 162 */     return MouseHelper.isHovered(this.x, this.y, (this.x + this.width), (this.y + this.height), mouseX, mouseY);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\button\ImageButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */