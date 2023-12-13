/*    */ package org.neverhook.client.cmd;
/*    */ 
/*    */ import org.neverhook.client.helpers.Helper;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ 
/*    */ public abstract class CommandAbstract
/*    */   implements Command, Helper {
/*    */   private final String name;
/*    */   private final String description;
/*    */   private final String usage;
/*    */   private final String[] aliases;
/*    */   
/*    */   public CommandAbstract(String name, String description, String usage, String... aliases) {
/* 14 */     this.name = name;
/* 15 */     this.description = description;
/* 16 */     this.aliases = aliases;
/* 17 */     this.usage = usage;
/*    */   }
/*    */   
/*    */   public void usage() {
/* 21 */     ChatHelper.addChatMessage("§cInvalid usage, try: " + this.usage + " or .help");
/*    */   }
/*    */   
/*    */   public String getUsage() {
/* 25 */     return this.usage;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 29 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getDescription() {
/* 33 */     return this.description;
/*    */   }
/*    */   
/*    */   public String[] getAliases() {
/* 37 */     return this.aliases;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\cmd\CommandAbstract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */