/*    */ package org.neverhook.client.components;
/*    */ 
/*    */ import net.arikia.dev.drpc.DiscordEventHandlers;
/*    */ import net.arikia.dev.drpc.DiscordRichPresence;
/*    */ import net.arikia.dev.drpc.DiscordUser;
/*    */ 
/*    */ public class DiscordRPC {
/*    */   private boolean running = true;
/*  9 */   private long created = 0L;
/*    */   
/*    */   public void init() {
/* 12 */     this.running = true;
/* 13 */     this.created = System.currentTimeMillis();
/*    */     
/* 15 */     DiscordEventHandlers handlers = (new DiscordEventHandlers.Builder()).setReadyEventHandler(discordUser -> {  }).build();
/* 16 */     net.arikia.dev.drpc.DiscordRPC.discordInitialize("884154378167148614", handlers, true);
/*    */     
/* 18 */     (new Thread("")
/*    */       {
/*    */         public void run()
/*    */         {
/* 22 */           net.arikia.dev.drpc.DiscordRPC.discordRunCallbacks();
/*    */         }
/* 25 */       }).start();
/*    */   }
/*    */   
/*    */   public void shutdown() {
/* 29 */     this.running = false;
/* 30 */     net.arikia.dev.drpc.DiscordRPC.discordShutdown();
/*    */   }
/*    */   
/*    */   public void update(String firstLine, String secondLine) {
/* 34 */     DiscordRichPresence.Builder b = new DiscordRichPresence.Builder(secondLine);
/* 35 */     b.setBigImage("large", "");
/* 36 */     b.setDetails(firstLine);
/* 37 */     b.setStartTimestamps(this.created);
/* 38 */     net.arikia.dev.drpc.DiscordRPC.discordUpdatePresence(b.build());
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\components\DiscordRPC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */