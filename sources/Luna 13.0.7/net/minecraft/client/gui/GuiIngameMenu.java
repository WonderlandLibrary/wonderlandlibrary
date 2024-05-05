package net.minecraft.client.gui;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.integrated.IntegratedServer;

public class GuiIngameMenu
  extends GuiScreen
{
  private int field_146445_a;
  private int field_146444_f;
  private static final String __OBFID = "CL_00000703";
  
  public GuiIngameMenu() {}
  
  public void initGui()
  {
    this.field_146445_a = 0;
    this.buttonList.clear();
    byte var1 = -16;
    boolean var2 = true;
    this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + var1, I18n.format("menu.returnToMenu", new Object[0])));
    if (!this.mc.isIntegratedServerRunning()) {
      ((GuiButton)this.buttonList.get(0)).displayString = I18n.format("menu.disconnect", new Object[0]);
    }
    this.buttonList.add(new GuiButton(4, width / 2 - 100, height / 4 + 24 + var1, I18n.format("menu.returnToGame", new Object[0])));
    this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + var1, 98, 20, I18n.format("menu.options", new Object[0])));
    GuiButton var3;
    this.buttonList.add(var3 = new GuiButton(7, width / 2 + 2, height / 4 + 96 + var1, 98, 20, I18n.format("menu.shareToLan", new Object[0])));
    this.buttonList.add(new GuiButton(5, width / 2 - 100, height / 4 + 48 + var1, 98, 20, I18n.format("gui.achievements", new Object[0])));
    this.buttonList.add(new GuiButton(6, width / 2 + 2, height / 4 + 48 + var1, 98, 20, I18n.format("gui.stats", new Object[0])));
    var3.enabled = ((this.mc.isSingleplayer()) && (!this.mc.getIntegratedServer().getPublic()));
  }
  
  protected void actionPerformed(GuiButton button)
    throws IOException
  {
    switch (button.id)
    {
    case 0: 
      this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
      break;
    case 1: 
      button.enabled = false;
      Minecraft.theWorld.sendQuittingDisconnectingPacket();
      this.mc.loadWorld(null);
      this.mc.displayGuiScreen(new GuiMainMenu());
    case 2: 
    case 3: 
    default: 
      break;
    case 4: 
      this.mc.displayGuiScreen(null);
      this.mc.setIngameFocus();
      break;
    case 5: 
      this.mc.displayGuiScreen(new GuiAchievements(this, Minecraft.thePlayer.getStatFileWriter()));
      break;
    case 6: 
      this.mc.displayGuiScreen(new GuiStats(this, Minecraft.thePlayer.getStatFileWriter()));
      break;
    case 7: 
      this.mc.displayGuiScreen(new GuiShareToLan(this));
    }
  }
  
  public void updateScreen()
  {
    super.updateScreen();
    this.field_146444_f += 1;
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks)
  {
    drawDefaultBackground();
    drawCenteredString(this.fontRendererObj, I18n.format("menu.game", new Object[0]), width / 2, 40, 16777215);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
}
