package optifine;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiMessage extends GuiScreen {
  private GuiScreen parentScreen;
  
  private String messageLine1;
  
  private String messageLine2;
  
  private final List listLines2 = Lists.newArrayList();
  
  protected String confirmButtonText;
  
  private int ticksUntilEnable;
  
  public GuiMessage(GuiScreen p_i48_1_, String p_i48_2_, String p_i48_3_) {
    this.parentScreen = p_i48_1_;
    this.messageLine1 = p_i48_2_;
    this.messageLine2 = p_i48_3_;
    this.confirmButtonText = I18n.format("gui.done", new Object[0]);
  }
  
  public void initGui() {
    this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 74, this.height / 6 + 96, this.confirmButtonText));
    this.listLines2.clear();
    this.listLines2.addAll(this.fontRendererObj.listFormattedStringToWidth(this.messageLine2, this.width - 50));
  }
  
  protected void actionPerformed(GuiButton button) throws IOException {
    Config.getMinecraft().displayGuiScreen(this.parentScreen);
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    drawCenteredString(this.fontRendererObj, this.messageLine1, this.width / 2, 70, 16777215);
    int i = 90;
    for (Object s : this.listLines2) {
      drawCenteredString(this.fontRendererObj, (String)s, this.width / 2, i, 16777215);
      i += this.fontRendererObj.FONT_HEIGHT;
    } 
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
  
  public void setButtonDelay(int p_setButtonDelay_1_) {
    this.ticksUntilEnable = p_setButtonDelay_1_;
    for (GuiButton guibutton : this.buttonList)
      guibutton.enabled = false; 
  }
  
  public void updateScreen() {
    super.updateScreen();
    if (--this.ticksUntilEnable == 0)
      for (GuiButton guibutton : this.buttonList)
        guibutton.enabled = true;  
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\optifine\GuiMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */