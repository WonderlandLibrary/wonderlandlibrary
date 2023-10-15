/*    */ package org.neverhook.client.feature.impl.ghost;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.math.MathematicHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ 
/*    */ public class HitBoxes
/*    */   extends Feature
/*    */ {
/*    */   public static NumberSetting expand;
/*    */   
/*    */   public HitBoxes() {
/* 20 */     super("HitBoxes", "Увеличивает хитбокс у ентити", Type.Ghost);
/* 21 */     expand = new NumberSetting("Expand", 0.2F, 0.01F, 2.0F, 0.01F, () -> Boolean.valueOf(true));
/* 22 */     addSettings(new Setting[] { (Setting)expand });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventPreMotion event) {
/* 27 */     setSuffix("" + MathematicHelper.round(expand.getNumberValue(), 2));
/*    */     
/* 29 */     for (Entity entity : mc.world.playerEntities) {
/* 30 */       if (entity != mc.player) {
/* 31 */         float width = entity.width;
/* 32 */         float height = entity.height;
/* 33 */         float expandValue = expand.getNumberValue();
/* 34 */         entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - width - expandValue, entity.posY, entity.posZ + width + expandValue, entity.posX + width + expandValue, entity.posY + height + expandValue, entity.posZ - width - expandValue));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\ghost\HitBoxes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */