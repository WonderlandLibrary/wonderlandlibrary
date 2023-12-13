/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayerDigging;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.math.RotationHelper;
/*    */ import org.neverhook.client.helpers.misc.TimerHelper;
/*    */ import org.neverhook.client.helpers.render.RenderHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class Fucker extends Feature {
/*    */   public static NumberSetting rad;
/*    */   private final NumberSetting fuckerDelay;
/*    */   private final ListSetting mode;
/* 27 */   private final TimerHelper timerUtils = new TimerHelper();
/*    */   private int xPos;
/*    */   private int yPos;
/*    */   private int zPos;
/*    */   private int blockid;
/*    */   
/*    */   public Fucker() {
/* 34 */     super("Fucker", "Автоматически рушит кровати и торты сквозь блоки", Type.Misc);
/* 35 */     this.mode = new ListSetting("Block", "Bed", () -> Boolean.valueOf(true), new String[] { "Bed", "Cake" });
/* 36 */     rad = new NumberSetting("Fucker Radius", 4.0F, 1.0F, 6.0F, 0.5F, () -> Boolean.valueOf(true));
/* 37 */     this.fuckerDelay = new NumberSetting("Fucker Delay", 100.0F, 0.0F, 1000.0F, 50.0F, () -> Boolean.valueOf(true));
/* 38 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)rad, (Setting)this.fuckerDelay });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onPreMotion(EventPreMotion event) {
/* 43 */     float radius = rad.getNumberValue();
/* 44 */     for (int x = (int)-radius; x < radius; x++) {
/* 45 */       for (int y = (int)radius; y > -radius; y--) {
/* 46 */         for (int z = (int)-radius; z < radius; z++) {
/* 47 */           this.xPos = (int)mc.player.posX + x;
/* 48 */           this.yPos = (int)mc.player.posY + y;
/* 49 */           this.zPos = (int)mc.player.posZ + z;
/* 50 */           BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
/* 51 */           Block block = mc.world.getBlockState(blockPos).getBlock();
/* 52 */           switch (this.mode.getOptions()) {
/*    */             case "Bed":
/* 54 */               this.blockid = 26;
/*    */               break;
/*    */             case "Cake":
/* 57 */               this.blockid = 354;
/*    */               break;
/*    */           } 
/* 60 */           if (Block.getIdFromBlock(block) == this.blockid)
/*    */           {
/* 62 */             if (block != null || blockPos != null) {
/* 63 */               float[] rotations = RotationHelper.getRotationVector(new Vec3d((blockPos.getX() + 0.5F), (blockPos.getY() + 0.5F), (blockPos.getZ() + 0.5F)), true, 2.0F, 2.0F, 360.0F);
/* 64 */               event.setYaw(rotations[0]);
/* 65 */               event.setPitch(rotations[1]);
/* 66 */               mc.player.renderYawOffset = rotations[0];
/* 67 */               mc.player.rotationYawHead = rotations[0];
/* 68 */               mc.player.rotationPitchHead = rotations[1];
/*    */               
/* 70 */               mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, mc.player.getHorizontalFacing()));
/* 71 */               if (this.timerUtils.hasReached(this.fuckerDelay.getNumberValue())) {
/* 72 */                 mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, mc.player.getHorizontalFacing()));
/* 73 */                 mc.player.swingArm(EnumHand.MAIN_HAND);
/* 74 */                 this.timerUtils.reset();
/*    */               } 
/*    */             }  } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onRender3D(EventRender3D event) {
/* 84 */     int playerX = (int)mc.player.posX;
/* 85 */     int playerZ = (int)mc.player.posZ;
/* 86 */     int playerY = (int)mc.player.posY;
/* 87 */     int range = (int)rad.getNumberValue();
/* 88 */     for (int y = playerY - range; y <= playerY + range; y++) {
/* 89 */       for (int x = playerX - range; x <= playerX + range; x++) {
/* 90 */         for (int z = playerZ - range; z <= playerZ + range; z++) {
/* 91 */           BlockPos pos = new BlockPos(x, y, z);
/* 92 */           if (mc.world.getBlockState(pos).getBlock() == Blocks.BED && 
/* 93 */             pos != null && mc.world.getBlockState(pos).getBlock() != Blocks.AIR)
/* 94 */             RenderHelper.blockEsp(pos, Color.RED, true); 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\Fucker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */