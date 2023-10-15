/*    */ package org.neverhook.client.helpers.misc;
/*    */ 
/*    */ import javax.sound.sampled.AudioInputStream;
/*    */ import javax.sound.sampled.AudioSystem;
/*    */ import javax.sound.sampled.Clip;
/*    */ import org.neverhook.client.NeverHook;
/*    */ 
/*    */ 
/*    */ public class MusicHelper
/*    */ {
/*    */   public static void playSound(String url) {
/* 12 */     (new Thread(() -> {
/*    */           try {
/*    */             Clip clip = AudioSystem.getClip();
/*    */             AudioInputStream inputStream = AudioSystem.getAudioInputStream(NeverHook.class.getResourceAsStream("/assets/minecraft/neverhook/songs/" + url));
/*    */             clip.open(inputStream);
/*    */             clip.start();
/* 18 */           } catch (Exception exception) {}
/*    */ 
/*    */         
/* 21 */         })).start();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\misc\MusicHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */