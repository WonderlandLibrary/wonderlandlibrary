/*    */ package org.neverhook.client.ui.components.draggable;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import org.neverhook.client.NeverHook;
/*    */ 
/*    */ public class GuiHudEditor
/*    */   extends GuiScreen
/*    */ {
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 10 */     drawWorldBackground(0);
/* 11 */     for (DraggableModule mod : NeverHook.instance.draggableManager.getMods()) {
/* 12 */       mod.render(mouseX, mouseY);
/*    */     }
/* 14 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\draggable\GuiHudEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */