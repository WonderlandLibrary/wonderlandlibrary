/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ 
/*    */ public class PearlLogger
/*    */   extends Feature
/*    */ {
/*    */   private boolean canSend;
/*    */   
/*    */   public PearlLogger() {
/* 19 */     super("PearlLogger", "Показывает координаты эндер-перла игроков", Type.Player);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 24 */     this.canSend = true;
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 29 */     if (mc.world == null || mc.player == null)
/*    */       return; 
/* 31 */     Entity enderPearl = null;
/* 32 */     for (Entity e : mc.world.loadedEntityList) {
/* 33 */       if (e instanceof net.minecraft.entity.item.EntityEnderPearl) {
/* 34 */         enderPearl = e;
/*    */         break;
/*    */       } 
/*    */     } 
/* 38 */     if (enderPearl == null) {
/* 39 */       this.canSend = true;
/*    */       return;
/*    */     } 
/* 42 */     EntityPlayer throwerEntity = null;
/* 43 */     for (EntityPlayer entity : mc.world.playerEntities) {
/* 44 */       if (throwerEntity != null && 
/* 45 */         throwerEntity.getDistanceToEntity(enderPearl) <= entity.getDistanceToEntity(enderPearl)) {
/*    */         continue;
/*    */       }
/*    */       
/* 49 */       throwerEntity = entity;
/*    */     } 
/* 51 */     String facing = enderPearl.getHorizontalFacing().toString();
/* 52 */     if (facing.equals("west")) {
/* 53 */       facing = "east";
/* 54 */     } else if (facing.equals("east")) {
/* 55 */       facing = "west";
/*    */     } 
/* 57 */     if (throwerEntity == mc.player) {
/*    */       return;
/*    */     }
/* 60 */     String pos = ChatFormatting.GOLD + facing + ChatFormatting.WHITE + " | " + ChatFormatting.LIGHT_PURPLE + enderPearl.getPosition().getX() + " " + enderPearl.getPosition().getY() + " " + enderPearl.getPosition().getZ();
/* 61 */     if (throwerEntity != null && this.canSend) {
/* 62 */       ChatHelper.addChatMessage(NeverHook.instance.friendManager.isFriend(throwerEntity.getName()) ? (ChatFormatting.GREEN + throwerEntity.getName() + ChatFormatting.WHITE + " thrown pearl on " + pos) : (ChatFormatting.RED + throwerEntity.getName() + ChatFormatting.WHITE + " thrown pearl on " + pos));
/* 63 */       this.canSend = false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\PearlLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */