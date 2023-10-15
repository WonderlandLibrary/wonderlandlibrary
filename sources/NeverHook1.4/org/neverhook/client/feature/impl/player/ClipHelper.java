/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.input.EventMouse;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.math.RotationHelper;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class ClipHelper extends Feature {
/*    */   public static BooleanSetting disableBlockLight;
/*    */   public static NumberSetting maxDistance;
/*    */   
/*    */   public ClipHelper() {
/* 21 */     super("ClipHelper", "Клипается по Y оси при нажатии на колесо мыши по игроку", Type.Player);
/* 22 */     maxDistance = new NumberSetting("Max Distance", 50.0F, 5.0F, 150.0F, 1.0F, () -> Boolean.valueOf(true));
/* 23 */     disableBlockLight = new BooleanSetting("Disable block light", true, () -> Boolean.valueOf(true));
/* 24 */     addSettings(new Setting[] { (Setting)maxDistance, (Setting)disableBlockLight });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onMouse(EventMouse event) {
/* 29 */     for (Entity entity : mc.world.loadedEntityList) {
/* 30 */       BlockPos playerPosY = new BlockPos(0.0D, mc.player.posY, 0.0D);
/* 31 */       BlockPos entityPosY = new BlockPos(0.0D, entity.posY, 0.0D);
/* 32 */       if (RotationHelper.isLookingAtEntity(mc.player.rotationYaw, mc.player.rotationPitch, 0.15F, 0.15F, 0.15F, entity, maxDistance.getNumberValue())) {
/* 33 */         int findToClip = (int)entity.posY;
/* 34 */         if (!playerPosY.equals(entityPosY) && mc.gameSettings.thirdPersonView == 0 && 
/* 35 */           event.getKey() == 2) {
/* 36 */           mc.player.setPositionAndUpdate(mc.player.posX, findToClip, mc.player.posZ);
/* 37 */           ChatHelper.addChatMessage("Clip to entity " + ChatFormatting.RED + entity.getName() + ChatFormatting.WHITE + " on Y " + ChatFormatting.RED + findToClip);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\ClipHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */