package net.minecraft.client.gui;

import java.io.IOException;
import me.rhys.base.Lite;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;

public class GuiIngameMenu extends GuiScreen {
  private int field_146445_a;
  
  private int field_146444_f;
  
  public void initGui() {
    this.field_146445_a = 0;
    this.buttonList.clear();
    int i = -16;
    int j = 98;
    this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + i, I18n.format("menu.returnToMenu", new Object[0])));
    if (!this.mc.isIntegratedServerRunning())
      ((GuiButton)this.buttonList.get(0)).displayString = I18n.format("menu.disconnect", new Object[0]); 
    this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + i, I18n.format("menu.returnToGame", new Object[0])));
    this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + i, 98, 20, I18n.format("menu.options", new Object[0])));
    GuiButton guibutton;
    this.buttonList.add(guibutton = new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + i, 98, 20, I18n.format("menu.shareToLan", new Object[0])));
    this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.achievements", new Object[0])));
    this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.stats", new Object[0])));
    guibutton.enabled = (this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic());
    Lite.FILE_FACTORY.save();
  }
  
  protected void actionPerformed(GuiButton button) throws IOException {
    boolean flag, flag1;
    switch (button.id) {
      case 0:
        this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
      case 1:
        flag = this.mc.isIntegratedServerRunning();
        flag1 = this.mc.func_181540_al();
        button.enabled = false;
        this.mc.theWorld.sendQuittingDisconnectingPacket();
        this.mc.loadWorld((WorldClient)null);
        if (flag) {
          this.mc.displayGuiScreen(new GuiMainMenu());
        } else if (flag1) {
          RealmsBridge realmsbridge = new RealmsBridge();
          realmsbridge.switchToRealms(new GuiMainMenu());
        } else {
          this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
        } 
      default:
        return;
      case 4:
        this.mc.displayGuiScreen((GuiScreen)null);
        this.mc.setIngameFocus();
      case 5:
        this.mc.displayGuiScreen((GuiScreen)new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
      case 6:
        this.mc.displayGuiScreen((GuiScreen)new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
      case 7:
        break;
    } 
    this.mc.displayGuiScreen(new GuiShareToLan(this));
  }
  
  public void updateScreen() {
    super.updateScreen();
    this.field_146444_f++;
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    drawCenteredString(this.fontRendererObj, I18n.format("menu.game", new Object[0]), this.width / 2, 40, 16777215);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\gui\GuiIngameMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */