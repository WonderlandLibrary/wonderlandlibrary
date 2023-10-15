/*    */ package org.neverhook.client.helpers;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import net.minecraft.network.Packet;
/*    */ import org.neverhook.client.helpers.misc.TimerHelper;
/*    */ 
/*    */ 
/*    */ public interface Helper
/*    */ {
/* 13 */   public static final Minecraft mc = Minecraft.getInstance();
/* 14 */   public static final Gui gui = new Gui();
/* 15 */   public static final Random random = new Random();
/* 16 */   public static final TimerHelper timerHelper = new TimerHelper();
/* 17 */   public static final ScaledResolution sr = new ScaledResolution(mc);
/*    */   
/*    */   default void sendPacket(Packet<?> packet) {
/* 20 */     mc.player.connection.sendPacket(packet);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\Helper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */