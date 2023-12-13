/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.misc.ClientHelper;
/*    */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*    */ import org.neverhook.client.helpers.render.RenderHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.ColorSetting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ 
/*    */ public class BlockOverlay
/*    */   extends Feature {
/* 21 */   public ListSetting colorMode = new ListSetting("Color Box Mode", "Custom", () -> Boolean.valueOf(true), new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
/* 22 */   public ColorSetting colorPicker = new ColorSetting("Color BlockOverlay", -1, () -> Boolean.valueOf(this.colorMode.currentMode.equals("Custom")));
/* 23 */   public BooleanSetting outline = new BooleanSetting("Outline BlockOverlay", false, () -> Boolean.valueOf(true));
/*    */   
/*    */   public BlockOverlay() {
/* 26 */     super("BlockOverlay", "Показывает блоки на которые вы навелись", Type.Visuals);
/* 27 */     addSettings(new Setting[] { (Setting)this.colorMode, (Setting)this.colorPicker, (Setting)this.outline });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onRender3D(EventRender3D event) {
/* 32 */     int color = 0;
/* 33 */     switch (this.colorMode.currentMode) {
/*    */       case "Client":
/* 35 */         color = ClientHelper.getClientColor().getRGB();
/*    */         break;
/*    */       case "Custom":
/* 38 */         color = this.colorPicker.getColorValue();
/*    */         break;
/*    */       case "Astolfo":
/* 41 */         color = PaletteHelper.astolfo(false, mc.objectMouseOver.getBlockPos().getY()).getRGB();
/*    */         break;
/*    */       case "Rainbow":
/* 44 */         color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*    */         break;
/*    */     } 
/* 47 */     if (mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock() != Blocks.AIR) {
/* 48 */       GlStateManager.pushMatrix();
/* 49 */       RenderHelper.blockEsp(new BlockPos(mc.objectMouseOver.getBlockPos().getX(), mc.objectMouseOver.getBlockPos().getY(), mc.objectMouseOver.getBlockPos().getZ()), new Color(color), this.outline.getBoolValue());
/* 50 */       GlStateManager.popMatrix();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\BlockOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */