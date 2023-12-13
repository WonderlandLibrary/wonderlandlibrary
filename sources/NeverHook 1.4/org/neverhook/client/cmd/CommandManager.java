/*    */ package org.neverhook.client.cmd;
/*    */ import java.util.ArrayList;
/*    */ import org.neverhook.client.cmd.impl.ClipCommand;
/*    */ import org.neverhook.client.cmd.impl.FakeHackCommand;
/*    */ import org.neverhook.client.cmd.impl.HelpCommand;
/*    */ import org.neverhook.client.cmd.impl.MacroCommand;
/*    */ import org.neverhook.client.cmd.impl.XrayCommand;
/*    */ import org.neverhook.client.event.EventManager;
/*    */ 
/*    */ public class CommandManager {
/* 11 */   private final ArrayList<Command> commands = new ArrayList<>();
/*    */   
/*    */   public CommandManager() {
/* 14 */     EventManager.register(new CommandHandler(this));
/* 15 */     this.commands.add(new FakeHackCommand());
/* 16 */     this.commands.add(new BindCommand());
/* 17 */     this.commands.add(new ClipCommand());
/* 18 */     this.commands.add(new AesCommand());
/* 19 */     this.commands.add(new FriendCommand());
/* 20 */     this.commands.add(new MacroCommand());
/* 21 */     this.commands.add(new ConfigCommand());
/* 22 */     this.commands.add(new HelpCommand());
/* 23 */     this.commands.add(new XrayCommand());
/*    */   }
/*    */   
/*    */   public List<Command> getCommands() {
/* 27 */     return this.commands;
/*    */   }
/*    */   
/*    */   public boolean execute(String args) {
/* 31 */     String noPrefix = args.substring(1);
/* 32 */     String[] split = noPrefix.split(" ");
/* 33 */     if (split.length > 0) {
/* 34 */       for (Command command : this.commands) {
/* 35 */         CommandAbstract abstractCommand = (CommandAbstract)command;
/* 36 */         String[] commandAliases = abstractCommand.getAliases();
/* 37 */         for (String alias : commandAliases) {
/* 38 */           if (split[0].equalsIgnoreCase(alias)) {
/* 39 */             abstractCommand.execute(split);
/* 40 */             return true;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     }
/* 45 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\cmd\CommandManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */