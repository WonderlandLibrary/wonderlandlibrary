/*     */ package org.neverhook.client.feature.impl.misc;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.math.RotationHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ColorSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class Nuker
/*     */   extends Feature
/*     */ {
/*     */   private final BooleanSetting sendRotations;
/*     */   private final BooleanSetting sortTrashBlocks;
/*     */   private final BooleanSetting autoNoBreakDelay;
/*     */   private final NumberSetting nukerHorizontal;
/*     */   private final NumberSetting nukerVertical;
/*     */   private final BooleanSetting nukerESP;
/*     */   private final ColorSetting color;
/*     */   private BlockPos blockPos;
/*     */   
/*     */   public Nuker() {
/*  36 */     super("Nuker", "Рушит блоки вокруг тебя", Type.Misc);
/*  37 */     this.nukerESP = new BooleanSetting("Nuker ESP", true, () -> Boolean.valueOf(true));
/*  38 */     this.color = new ColorSetting("Nuker Color", (new Color(16777215)).getRGB(), this.nukerESP::getBoolValue);
/*  39 */     this.sendRotations = new BooleanSetting("Send Rotations", true, () -> Boolean.valueOf(true));
/*  40 */     this.sortTrashBlocks = new BooleanSetting("Sort trash blocks", true, () -> Boolean.valueOf(true));
/*  41 */     this.autoNoBreakDelay = new BooleanSetting("No Delay", true, () -> Boolean.valueOf(true));
/*  42 */     this.nukerHorizontal = new NumberSetting("Nuker Horizontal", 3.0F, 1.0F, 5.0F, 1.0F, () -> Boolean.valueOf(true));
/*  43 */     this.nukerVertical = new NumberSetting("Nuker Vertical", 3.0F, 1.0F, 5.0F, 1.0F, () -> Boolean.valueOf(true));
/*  44 */     addSettings(new Setting[] { (Setting)this.color, (Setting)this.nukerESP, (Setting)this.sendRotations, (Setting)this.sortTrashBlocks, (Setting)this.autoNoBreakDelay, (Setting)this.nukerHorizontal, (Setting)this.nukerVertical });
/*     */   }
/*     */   
/*     */   private boolean canNuker(BlockPos pos) {
/*  48 */     IBlockState blockState = mc.world.getBlockState(pos);
/*  49 */     Block block = blockState.getBlock();
/*  50 */     return (block.getBlockHardness(blockState, (World)mc.world, pos) != -1.0F);
/*     */   }
/*     */   
/*     */   private BlockPos getPositionXYZ() {
/*  54 */     BlockPos blockPos = null;
/*  55 */     float vDistance = this.nukerVertical.getNumberValue();
/*  56 */     float hDistance = this.nukerHorizontal.getNumberValue(); float x;
/*  57 */     for (x = 0.0F; x <= hDistance; x++) {
/*  58 */       float y; for (y = 0.0F; y <= vDistance; y++) {
/*  59 */         float z; for (z = 0.0F; z <= hDistance; z++) {
/*  60 */           for (int reversedX = 0; reversedX <= 1; reversedX++, x = -x) {
/*  61 */             for (int reversedZ = 0; reversedZ <= 1; reversedZ++, z = -z) {
/*  62 */               BlockPos pos = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);
/*  63 */               if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR && canNuker(pos)) {
/*  64 */                 blockPos = pos;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  71 */     return blockPos;
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender3D(EventRender3D event) {
/*  76 */     if (mc.player == null || mc.world == null)
/*     */       return; 
/*  78 */     if (!this.nukerESP.getBoolValue()) {
/*     */       return;
/*     */     }
/*  81 */     if ((mc.world.getBlockState(this.blockPos).getBlock() == Blocks.GRASS || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.MONSTER_EGG || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.DIRT || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.GRAVEL || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.WATER || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.LAVA) && this.sortTrashBlocks.getBoolValue()) {
/*     */       return;
/*     */     }
/*  84 */     this.blockPos = getPositionXYZ();
/*  85 */     Color nukerColor = new Color(this.color.getColorValue());
/*  86 */     BlockPos blockPos = new BlockPos(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ());
/*  87 */     RenderHelper.blockEsp(blockPos, nukerColor, true);
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPreUpdate(EventPreMotion event) {
/*  92 */     if (mc.player == null || mc.world == null)
/*     */       return; 
/*  94 */     if (this.autoNoBreakDelay.getBoolValue()) {
/*  95 */       mc.playerController.blockHitDelay = 0;
/*     */     }
/*  97 */     this.blockPos = getPositionXYZ();
/*  98 */     float[] rotations = RotationHelper.getRotationVector(new Vec3d((this.blockPos.getX() + 0.5F), (this.blockPos.getY() + 0.5F), (this.blockPos.getZ() + 0.5F)), true, 2.0F, 2.0F, 360.0F);
/*  99 */     if ((mc.world.getBlockState(this.blockPos).getBlock() == Blocks.GRASS || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.MONSTER_EGG || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.DIRT || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.GRAVEL || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.WATER || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.LAVA) && this.sortTrashBlocks.getBoolValue()) {
/*     */       return;
/*     */     }
/* 102 */     if ((mc.player.getHeldItemOffhand().getItem() instanceof net.minecraft.item.ItemTool || mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemTool || mc.player.isCreative()) && 
/* 103 */       this.blockPos != null) {
/* 104 */       if (this.sendRotations.getBoolValue()) {
/* 105 */         event.setYaw(rotations[0]);
/* 106 */         event.setPitch(rotations[1]);
/* 107 */         mc.player.renderYawOffset = rotations[0];
/* 108 */         mc.player.rotationYawHead = rotations[0];
/* 109 */         mc.player.rotationPitchHead = rotations[1];
/*     */       } 
/* 111 */       if (canNuker(this.blockPos)) {
/* 112 */         mc.playerController.onPlayerDamageBlock(this.blockPos, mc.player.getHorizontalFacing());
/* 113 */         mc.player.swingArm(EnumHand.MAIN_HAND);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\Nuker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */