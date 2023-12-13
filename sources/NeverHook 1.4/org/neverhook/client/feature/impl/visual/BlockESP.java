/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.misc.ClientHelper;
/*    */ import org.neverhook.client.helpers.render.RenderHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.ColorSetting;
/*    */ 
/*    */ public class BlockESP
/*    */   extends Feature {
/*    */   public static BooleanSetting enderChest;
/*    */   public static BooleanSetting chest;
/*    */   public static BooleanSetting clientColor;
/*    */   public static ColorSetting spawnerColor;
/*    */   public static BooleanSetting espOutline;
/*    */   public static ColorSetting chestColor;
/*    */   public static ColorSetting enderChestColor;
/*    */   public static ColorSetting shulkerColor;
/*    */   public static ColorSetting bedColor;
/*    */   private final BooleanSetting bed;
/*    */   private final BooleanSetting shulker;
/*    */   private final BooleanSetting spawner;
/*    */   
/*    */   public BlockESP() {
/* 32 */     super("BlockESP", "Подсвечивает опредленные блоки", Type.Visuals);
/* 33 */     chest = new BooleanSetting("Chest", true, () -> Boolean.valueOf(true));
/* 34 */     enderChest = new BooleanSetting("Ender Chest", false, () -> Boolean.valueOf(true));
/* 35 */     this.spawner = new BooleanSetting("Spawner", false, () -> Boolean.valueOf(true));
/* 36 */     this.shulker = new BooleanSetting("Shulker", false, () -> Boolean.valueOf(true));
/* 37 */     this.bed = new BooleanSetting("Bed", false, () -> Boolean.valueOf(true));
/* 38 */     chestColor = new ColorSetting("Chest Color", (new Color(16777215)).getRGB(), chest::getBoolValue);
/* 39 */     enderChestColor = new ColorSetting("EnderChest Color", (new Color(16777215)).getRGB(), enderChest::getBoolValue);
/* 40 */     shulkerColor = new ColorSetting("Shulker Color", (new Color(16777215)).getRGB(), this.shulker::getBoolValue);
/* 41 */     spawnerColor = new ColorSetting("Spawner Color", (new Color(16777215)).getRGB(), this.spawner::getBoolValue);
/* 42 */     bedColor = new ColorSetting("Bed Color", (new Color(16777215)).getRGB(), this.bed::getBoolValue);
/* 43 */     clientColor = new BooleanSetting("Client Colors", false, () -> Boolean.valueOf(true));
/* 44 */     espOutline = new BooleanSetting("ESP Outline", false, () -> Boolean.valueOf(true));
/* 45 */     addSettings(new Setting[] { (Setting)espOutline, (Setting)chest, (Setting)enderChest, (Setting)this.spawner, (Setting)this.shulker, (Setting)this.bed, (Setting)chestColor, (Setting)enderChestColor, (Setting)spawnerColor, (Setting)shulkerColor, (Setting)bedColor, (Setting)clientColor });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onRender3D(EventRender3D event) {
/* 50 */     Color colorChest = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(chestColor.getColorValue());
/* 51 */     Color enderColorChest = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(enderChestColor.getColorValue());
/* 52 */     Color shulkColor = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(shulkerColor.getColorValue());
/* 53 */     Color bedColoR = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(bedColor.getColorValue());
/* 54 */     Color spawnerColoR = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(spawnerColor.getColorValue());
/* 55 */     if (mc.player != null || mc.world != null)
/* 56 */       for (TileEntity entity : mc.world.loadedTileEntityList) {
/* 57 */         BlockPos pos = entity.getPos();
/* 58 */         if (entity instanceof net.minecraft.tileentity.TileEntityChest && chest.getBoolValue()) {
/* 59 */           RenderHelper.blockEsp(pos, new Color(colorChest.getRGB()), espOutline.getBoolValue()); continue;
/* 60 */         }  if (entity instanceof net.minecraft.tileentity.TileEntityEnderChest && enderChest.getBoolValue()) {
/* 61 */           RenderHelper.blockEsp(pos, new Color(enderColorChest.getRGB()), espOutline.getBoolValue()); continue;
/* 62 */         }  if (entity instanceof net.minecraft.tileentity.TileEntityBed && this.bed.getBoolValue()) {
/* 63 */           RenderHelper.blockEsp(pos, new Color(bedColoR.getRGB()), espOutline.getBoolValue()); continue;
/* 64 */         }  if (entity instanceof net.minecraft.tileentity.TileEntityShulkerBox && this.shulker.getBoolValue()) {
/* 65 */           RenderHelper.blockEsp(pos, new Color(shulkColor.getRGB()), espOutline.getBoolValue()); continue;
/* 66 */         }  if (entity instanceof net.minecraft.tileentity.TileEntityMobSpawner && this.spawner.getBoolValue())
/* 67 */           RenderHelper.blockEsp(pos, new Color(spawnerColoR.getRGB()), espOutline.getBoolValue()); 
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\BlockESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */