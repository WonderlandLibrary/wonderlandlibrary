/*     */ package org.neverhook.client.feature.impl.combat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.math.RotationHelper;
/*     */ import org.neverhook.client.helpers.misc.TimerHelper;
/*     */ import org.neverhook.client.helpers.player.InventoryHelper;
/*     */ import org.neverhook.client.helpers.world.BlockHelper;
/*     */ import org.neverhook.client.helpers.world.EntityHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AntiCrystal
/*     */   extends Feature
/*     */ {
/*     */   private final NumberSetting rangeToBlock;
/*     */   private final NumberSetting delay;
/*     */   private final BooleanSetting throughWalls;
/*     */   private final BooleanSetting bedrockCheck;
/*     */   private final BooleanSetting obsidianCheck;
/*  36 */   private final TimerHelper timerHelper = new TimerHelper();
/*  37 */   private final ArrayList<BlockPos> invalidPositions = new ArrayList<>();
/*     */   
/*     */   public AntiCrystal() {
/*  40 */     super("AntiCrystal", "Автоматически ставит блок на обсидиан/бедрок в радиусе", Type.Combat);
/*  41 */     this.throughWalls = new BooleanSetting("Through Walls", true, () -> Boolean.valueOf(true));
/*  42 */     this.obsidianCheck = new BooleanSetting("Obsidian Check", true, () -> Boolean.valueOf(true));
/*  43 */     this.bedrockCheck = new BooleanSetting("Bedrock Check", false, () -> Boolean.valueOf(true));
/*  44 */     this.rangeToBlock = new NumberSetting("Range To Block", 5.0F, 3.0F, 6.0F, 0.1F, () -> Boolean.valueOf(true));
/*  45 */     this.delay = new NumberSetting("Place Delay", 0.0F, 0.0F, 2000.0F, 100.0F, () -> Boolean.valueOf(true));
/*  46 */     addSettings(new Setting[] { (Setting)this.obsidianCheck, (Setting)this.bedrockCheck, (Setting)this.throughWalls, (Setting)this.rangeToBlock, (Setting)this.delay });
/*     */   }
/*     */   
/*     */   public static int getSlotWithBlock() {
/*  50 */     for (int i = 0; i < 9; i++) {
/*  51 */       mc.player.inventory.getStackInSlot(i);
/*  52 */       if (mc.player.inventory.getStackInSlot(i).getItem() instanceof net.minecraft.item.ItemBlock) {
/*  53 */         return i;
/*     */       }
/*     */     } 
/*  56 */     return -1;
/*     */   }
/*     */   
/*     */   private boolean IsValidBlockPos(BlockPos pos) {
/*  60 */     IBlockState state = mc.world.getBlockState(pos);
/*  61 */     if ((state.getBlock() instanceof net.minecraft.block.BlockObsidian && this.obsidianCheck.getBoolValue()) || (state.getBlock() == Block.getBlockById(7) && this.bedrockCheck.getBoolValue()))
/*  62 */       return (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR); 
/*  63 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onPreMotion(EventPreMotion event) {
/*  69 */     setSuffix("" + (int)this.rangeToBlock.getNumberValue());
/*     */ 
/*     */ 
/*     */     
/*  73 */     int oldSlot = mc.player.inventory.currentItem;
/*  74 */     BlockPos pos = BlockHelper.getSphere(BlockHelper.getPlayerPos(), this.rangeToBlock.getNumberValue(), 6, false, true).stream().filter(this::IsValidBlockPos).min(Comparator.comparing(blockPos -> Double.valueOf(EntityHelper.getDistanceOfEntityToBlock((Entity)mc.player, blockPos)))).orElse(null);
/*  75 */     if (InventoryHelper.doesHotbarHaveBlock() && pos != null)
/*     */     {
/*     */ 
/*     */       
/*  79 */       if (this.timerHelper.hasReached(this.delay.getNumberValue()))
/*     */       {
/*  81 */         if (getSlotWithBlock() != -1) {
/*     */ 
/*     */ 
/*     */           
/*  85 */           if (!mc.world.isAirBlock(pos.up(1))) {
/*  86 */             this.invalidPositions.add(pos);
/*     */           }
/*     */           
/*  89 */           for (Entity e : mc.world.loadedEntityList) {
/*  90 */             if (e instanceof net.minecraft.entity.item.EntityEnderCrystal && 
/*  91 */               e.getPosition().getX() == pos.getX() && e.getPosition().getZ() == pos.getZ()) {
/*     */               return;
/*     */             }
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/*  98 */           if (!this.invalidPositions.contains(pos)) {
/*     */ 
/*     */             
/* 101 */             if (mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), false, true, false) != null && !this.throughWalls.getBoolValue()) {
/*     */               return;
/*     */             }
/*     */ 
/*     */             
/* 106 */             float[] rots = RotationHelper.getRotationVector(new Vec3d(pos.getX() + 0.5D, pos.getY() + 1.4D, pos.getZ() + 0.5D), true, 2.0F, 2.0F, 360.0F);
/* 107 */             event.setYaw(rots[0]);
/* 108 */             event.setPitch(rots[1]);
/* 109 */             mc.player.renderYawOffset = rots[0];
/* 110 */             mc.player.rotationYawHead = rots[0];
/* 111 */             mc.player.rotationPitchHead = rots[1];
/*     */ 
/*     */             
/* 114 */             mc.player.inventory.currentItem = getSlotWithBlock();
/*     */ 
/*     */             
/* 117 */             mc.playerController.processRightClickBlock(mc.player, mc.world, pos, EnumFacing.UP, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), EnumHand.MAIN_HAND);
/* 118 */             mc.player.swingArm(EnumHand.MAIN_HAND);
/*     */ 
/*     */             
/* 121 */             mc.player.inventory.currentItem = oldSlot;
/*     */ 
/*     */             
/* 124 */             this.timerHelper.reset();
/*     */           } 
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\AntiCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */