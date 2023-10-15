/*     */ package org.neverhook.client.feature.impl.visual;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ 
/*     */ public class Trajectories
/*     */   extends Feature {
/*     */   public Trajectories() {
/*  19 */     super("Trajectories", "", Type.Visuals);
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender(EventRender3D event) {
/*  24 */     ItemStack stack = mc.player.getHeldItem(EnumHand.MAIN_HAND);
/*  25 */     if (!isItemValid(stack)) {
/*     */       return;
/*     */     }
/*  28 */     boolean isBow = stack.getItem() instanceof net.minecraft.item.ItemBow;
/*     */     
/*  30 */     double playerYaw = mc.player.rotationYaw;
/*  31 */     double playerPitch = mc.player.rotationPitch;
/*     */     
/*  33 */     double projectilePosX = (mc.getRenderManager()).renderPosX - Math.cos(Math.toRadians(playerYaw)) * 0.1599999964237213D;
/*  34 */     double projectilePosY = (mc.getRenderManager()).renderPosY + mc.player.getEyeHeight();
/*  35 */     double projectilePosZ = (mc.getRenderManager()).renderPosZ - Math.sin(Math.toRadians(playerYaw)) * 0.1599999964237213D;
/*     */     
/*  37 */     double projectileMotionX = -Math.sin(Math.toRadians(playerYaw)) * Math.cos(Math.toRadians(playerPitch)) * (isBow ? 1.0D : 0.4D);
/*  38 */     double projectileMotionY = -Math.sin(Math.toRadians(playerPitch - (isThrowablePotion(stack) ? 20 : false))) * (isBow ? 1.0D : 0.4D);
/*  39 */     double projectileMotionZ = Math.cos(Math.toRadians(playerYaw)) * Math.cos(Math.toRadians(playerPitch)) * (isBow ? 1.0D : 0.4D);
/*     */     
/*  41 */     double shootPower = mc.player.getItemInUseDuration();
/*     */     
/*  43 */     if (isBow) {
/*  44 */       shootPower /= 20.0D;
/*     */       
/*  46 */       shootPower = (shootPower * shootPower + shootPower * 2.0D) / 3.0D;
/*     */       
/*  48 */       if (shootPower < 0.1D)
/*  49 */         return;  if (shootPower > 1.0D) shootPower = 1.0D;
/*     */     
/*     */     } 
/*  52 */     double distance = Math.sqrt(projectileMotionX * projectileMotionX + projectileMotionY * projectileMotionY + projectileMotionZ * projectileMotionZ);
/*     */     
/*  54 */     projectileMotionX /= distance;
/*  55 */     projectileMotionY /= distance;
/*  56 */     projectileMotionZ /= distance;
/*     */     
/*  58 */     projectileMotionX *= (isBow ? shootPower : 0.5D) * 3.0D;
/*  59 */     projectileMotionY *= (isBow ? shootPower : 0.5D) * 3.0D;
/*  60 */     projectileMotionZ *= (isBow ? shootPower : 0.5D) * 3.0D;
/*     */     
/*  62 */     boolean projectileHasLanded = false;
/*  63 */     RayTraceResult landingPosition = null;
/*     */     
/*  65 */     GlStateManager.resetColor();
/*  66 */     GL11.glEnable(3042);
/*  67 */     GL11.glBlendFunc(770, 771);
/*  68 */     GL11.glEnable(2848);
/*  69 */     GlStateManager.disableTexture2D();
/*  70 */     RenderHelper.setColor(ClientHelper.getClientColor().getRGB());
/*  71 */     GL11.glLineWidth(1.5F);
/*  72 */     GL11.glBegin(3);
/*     */     
/*  74 */     while (!projectileHasLanded && projectilePosY > 0.0D) {
/*  75 */       Vec3d currentPosition = new Vec3d(projectilePosX, projectilePosY, projectilePosZ);
/*  76 */       Vec3d nextPosition = new Vec3d(projectilePosX + projectileMotionX, projectilePosY + projectileMotionY, projectilePosZ + projectileMotionZ);
/*     */       
/*  78 */       RayTraceResult possibleLandingPositon = mc.world.rayTraceBlocks(currentPosition, nextPosition, false, true, false);
/*     */       
/*  80 */       if (possibleLandingPositon != null && 
/*  81 */         possibleLandingPositon.typeOfHit != RayTraceResult.Type.MISS) {
/*  82 */         landingPosition = possibleLandingPositon;
/*  83 */         projectileHasLanded = true;
/*     */       } 
/*     */ 
/*     */       
/*  87 */       projectilePosX += projectileMotionX;
/*  88 */       projectilePosY += projectileMotionY;
/*  89 */       projectilePosZ += projectileMotionZ;
/*     */       
/*  91 */       projectileMotionX *= 0.99D;
/*  92 */       projectileMotionY *= 0.99D;
/*  93 */       projectileMotionZ *= 0.99D;
/*     */       
/*  95 */       projectileMotionY -= isBow ? 0.05D : (isThrowablePotion(stack) ? 0.05D : 0.03D);
/*     */       
/*  97 */       GL11.glVertex3d(projectilePosX - (mc.getRenderManager()).renderPosX, projectilePosY - (mc.getRenderManager()).renderPosY, projectilePosZ - (mc.getRenderManager()).renderPosZ);
/*     */     } 
/*     */     
/* 100 */     GL11.glEnd();
/* 101 */     GlStateManager.enableTexture2D();
/* 102 */     GL11.glDisable(2848);
/* 103 */     GL11.glDisable(3042);
/* 104 */     GlStateManager.resetColor();
/*     */     
/* 106 */     if (landingPosition != null && 
/* 107 */       landingPosition.typeOfHit == RayTraceResult.Type.BLOCK) {
/* 108 */       RenderHelper.blockEsp(landingPosition.getBlockPos(), ClientHelper.getClientColor(), true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isItemValid(ItemStack stack) {
/* 114 */     return (stack.getItem() instanceof net.minecraft.item.ItemBow || stack.getItem() instanceof net.minecraft.item.ItemEnderPearl || stack.getItem() instanceof net.minecraft.item.ItemEgg || stack.getItem() instanceof net.minecraft.item.ItemSnowball || isThrowablePotion(stack));
/*     */   }
/*     */   
/*     */   private boolean isThrowablePotion(ItemStack stack) {
/* 118 */     return stack.getItem() instanceof net.minecraft.item.ItemPotion;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\Trajectories.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */