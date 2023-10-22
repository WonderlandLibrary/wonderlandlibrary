package cn.hanabi.altmanager;

import cn.hanabi.Hanabi;
import cn.hanabi.utils.RenderUtil;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class CustomGuiButton extends GuiButton {
   public CustomGuiButton(int buttonId, int x, int y, String buttonText) {
      super(buttonId, x, y, buttonText);
   }

   public CustomGuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
      super(buttonId, x, y, widthIn, heightIn, buttonText);
   }

   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      RenderUtil.drawRect((float)this.xPosition, (float)this.yPosition, (float)(this.xPosition + this.width), (float)(this.yPosition + this.height), Color.BLACK.getRGB());
      this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
      Hanabi.INSTANCE.fontManager.wqy16.drawCenteredString(this.displayString, (float)this.xPosition + (float)this.width / 2.0F, (float)this.yPosition + (float)(this.height - 8) / 2.0F, this.hovered ? (new Color(47, 116, 253, 255)).getRGB() : (new Color(200, 200, 200)).getRGB());
   }
}
