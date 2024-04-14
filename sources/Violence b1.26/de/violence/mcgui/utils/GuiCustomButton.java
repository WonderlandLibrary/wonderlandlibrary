package de.violence.mcgui.utils;

import de.violence.mcgui.utils.GuiMains;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCustomButton {
   private String textureName;
   public int x;
   public int y;
   public boolean hovered;

   public GuiCustomButton(String textureName) {
      this.textureName = textureName;
   }

   public void onRender(int mouseX, int mouseY) {
      this.hovered = GuiMains.isHovered(mouseX, mouseY, this.x, this.y, 30, 10);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      ResourceLocation resourceLocation = new ResourceLocation("textures/gui/violence/" + this.textureName);
      Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
      float var10001 = (float)this.x * 10.0F;
      Minecraft.getMinecraft().currentScreen.drawTexturedModalRect(var10001, (float)this.y * 10.0F, 1, 1, 250, 250);
      GL11.glPopMatrix();
   }

   public boolean isHovered() {
      return this.hovered;
   }

   public String getTextureName() {
      return this.textureName;
   }
}
