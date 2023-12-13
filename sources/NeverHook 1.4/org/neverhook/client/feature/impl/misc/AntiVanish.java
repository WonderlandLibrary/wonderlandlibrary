/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.Entity;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ 
/*    */ public class AntiVanish
/*    */   extends Feature
/*    */ {
/* 15 */   private final List<Entity> e = new ArrayList<>();
/*    */   
/*    */   public AntiVanish() {
/* 18 */     super("Anti Vanish", "Позволяет увидеть невидимых существ", Type.Misc);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 23 */     for (Entity entity : this.e) {
/* 24 */       entity.setInvisible(true);
/*    */     }
/* 26 */     this.e.clear();
/* 27 */     super.onEnable();
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 32 */     for (Entity entity : mc.world.loadedEntityList) {
/* 33 */       if (entity.isInvisible() && entity instanceof net.minecraft.entity.player.EntityPlayer) {
/* 34 */         entity.setInvisible(false);
/* 35 */         this.e.add(entity);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\AntiVanish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */