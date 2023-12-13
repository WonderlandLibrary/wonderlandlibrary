/*    */ package org.neverhook.client.ui.components.draggable;
/*    */ import java.util.ArrayList;
/*    */ import org.neverhook.client.ui.components.draggable.impl.ArmorComponent;
/*    */ import org.neverhook.client.ui.components.draggable.impl.InfoComponent;
/*    */ import org.neverhook.client.ui.components.draggable.impl.TargetHUDComponent;
/*    */ import org.neverhook.client.ui.components.draggable.impl.WaterMarkComponent;
/*    */ 
/*    */ public class DraggableManager {
/*  9 */   public ArrayList<DraggableModule> mods = new ArrayList<>();
/*    */   
/*    */   public DraggableManager() {
/* 12 */     this.mods.add(new ClientInfoComponent());
/* 13 */     this.mods.add(new InfoComponent());
/* 14 */     this.mods.add(new WaterMarkComponent());
/* 15 */     this.mods.add(new PotionComponent());
/* 16 */     this.mods.add(new ArmorComponent());
/* 17 */     this.mods.add(new TargetHUDComponent());
/*    */   }
/*    */   
/*    */   public ArrayList<DraggableModule> getMods() {
/* 21 */     return this.mods;
/*    */   }
/*    */   
/*    */   public void setMods(ArrayList<DraggableModule> mods) {
/* 25 */     this.mods = mods;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\draggable\DraggableManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */