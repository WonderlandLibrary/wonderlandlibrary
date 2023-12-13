package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiScreenHorseInventory extends GuiContainer {
   private IInventory playerInventory;
   private float mousePosY;
   private EntityHorse horseEntity;
   private static final ResourceLocation horseGuiTextures = new ResourceLocation("textures/gui/container/horse.png");
   private float mousePosx;
   private IInventory horseInventory;

   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(horseGuiTextures);
      int var4 = (width - this.xSize) / 2;
      int var5 = (height - this.ySize) / 2;
      drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
      if (this.horseEntity.isChested()) {
         drawTexturedModalRect(var4 + 79, var5 + 17, 0, this.ySize, 90, 54);
      }

      if (this.horseEntity.canWearArmor()) {
         drawTexturedModalRect(var4 + 7, var5 + 35, 0, this.ySize + 54, 18, 18);
      }

      GuiInventory.drawEntityOnScreen((float)(var4 + 51), (double)(var5 + 60), 17, (float)(var4 + 51) - this.mousePosx, (float)(var5 + 75 - 50) - this.mousePosY, this.horseEntity);
   }

   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      this.fontRendererObj.drawString(this.horseInventory.getDisplayName().getUnformattedText(), 8.0D, 6.0D, 4210752);
      this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0D, (double)(this.ySize - 96 + 2), 4210752);
   }

   public GuiScreenHorseInventory(IInventory var1, IInventory var2, EntityHorse var3) {
      Minecraft.getMinecraft();
      super(new ContainerHorseInventory(var1, var2, var3, Minecraft.thePlayer));
      this.playerInventory = var1;
      this.horseInventory = var2;
      this.horseEntity = var3;
      this.allowUserInput = false;
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.mousePosx = (float)var1;
      this.mousePosY = (float)var2;
      super.drawScreen(var1, var2, var3);
   }
}
