/*    */ package org.neverhook.client.feature.impl.player;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.inventory.ClickType;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.ContainerChest;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.misc.TimerHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class ChestStealer extends Feature {
/* 17 */   public TimerHelper timer = new TimerHelper(); private final NumberSetting delay;
/*    */   
/*    */   public ChestStealer() {
/* 20 */     super("ChestStealer", "Автоматически забирает вещи из сундуков", Type.Player);
/* 21 */     this.delay = new NumberSetting("Stealer Speed", 10.0F, 0.0F, 100.0F, 1.0F, () -> Boolean.valueOf(true));
/* 22 */     addSettings(new Setting[] { (Setting)this.delay });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventPreMotion event) {
/* 27 */     setSuffix("" + (int)this.delay.getNumberValue());
/*    */     
/* 29 */     float delay = this.delay.getNumberValue() * 10.0F;
/*    */     
/* 31 */     if (mc.player.openContainer instanceof ContainerChest) {
/* 32 */       ContainerChest container = (ContainerChest)mc.player.openContainer;
/* 33 */       for (int index = 0; index < container.inventorySlots.size(); index++) {
/* 34 */         if (container.getLowerChestInventory().getStackInSlot(index).getItem() != Item.getItemById(0) && this.timer.hasReached(delay)) {
/* 35 */           mc.playerController.windowClick(container.windowId, index, 0, ClickType.QUICK_MOVE, (EntityPlayer)mc.player);
/* 36 */           this.timer.reset();
/*    */         
/*    */         }
/* 39 */         else if (isEmpty((Container)container)) {
/*    */           
/* 41 */           mc.player.closeScreen();
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   public boolean isWhiteItem(ItemStack itemStack) {
/* 47 */     return (itemStack.getItem() instanceof net.minecraft.item.ItemArmor || itemStack.getItem() instanceof net.minecraft.item.ItemEnderPearl || itemStack.getItem() instanceof net.minecraft.item.ItemSword || itemStack.getItem() instanceof net.minecraft.item.ItemTool || itemStack.getItem() instanceof net.minecraft.item.ItemFood || itemStack.getItem() instanceof net.minecraft.item.ItemPotion || itemStack.getItem() instanceof net.minecraft.item.ItemBlock || itemStack.getItem() instanceof net.minecraft.item.ItemArrow || itemStack.getItem() instanceof net.minecraft.item.ItemCompass);
/*    */   }
/*    */   
/*    */   private boolean isEmpty(Container container) {
/* 51 */     for (int index = 0; index < container.inventorySlots.size(); index++) {
/* 52 */       if (isWhiteItem(container.getSlot(index).getStack()))
/* 53 */         return false; 
/*    */     } 
/* 55 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\ChestStealer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */