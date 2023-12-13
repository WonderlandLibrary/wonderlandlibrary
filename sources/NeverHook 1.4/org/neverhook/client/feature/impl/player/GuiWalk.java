/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ 
/*    */ public class GuiWalk
/*    */   extends Feature
/*    */ {
/*    */   public GuiWalk() {
/* 15 */     super("GuiWalk", "Позволяет ходить в окрытых контейнерах", Type.Movement);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 20 */     KeyBinding[] keys = { mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint };
/*    */     
/* 22 */     if (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat || mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiEditSign) {
/*    */       return;
/*    */     }
/* 25 */     for (KeyBinding keyBinding : keys)
/* 26 */       keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\GuiWalk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */