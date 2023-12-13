package me.aidanmees.trivia.client.bungeehack;

import java.io.IOException;

import net.mcleaks.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.Session;

public class GuiBungeeCord
  extends GuiScreen
{
  protected GuiTextField ipField;
  protected GuiTextField fakeNickField;
  protected GuiTextField realNickField;
  protected GuiScreen prevScreen;
  public static Minecraft mc = Minecraft.getMinecraft();
  
  public GuiBungeeCord(GuiScreen screen)
  {
    this.prevScreen = screen;
  }
  
  public void initGui()
  {
    int fieldWidth = 200;
    int fieldHeight = 20;
    
    this.buttonList.clear();
    
    this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 95, fieldWidth, fieldHeight, "Save"));
    this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 95 + fieldHeight + 4, fieldWidth, fieldHeight, "Cancel"));
    this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 95 + fieldHeight + 28, fieldWidth, fieldHeight, getEnableButtonText()));
    
    this.realNickField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, this.height / 5, fieldWidth, fieldHeight);
    this.fakeNickField = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 100, this.height / 5 + 40, fieldWidth, fieldHeight);
    
    this.ipField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, this.height / 5 + 80, fieldWidth, fieldHeight);
    
    this.ipField.setText(this.mc.getFakeIp());
    this.fakeNickField.setText(this.mc.getFakeNick());
    this.realNickField.setText(this.mc.getSession().getUsername());
  }
  


  private String getEnableButtonText() {
      return this.mc.isUUIDHack ? (Object)((Object)ChatColor.GREEN) + "Enabled" : (Object)((Object)ChatColor.RED) + "Disabled";
  }

  
  public void handleMouseInput()
    throws IOException
  {
    super.handleMouseInput();
  }
  
  protected void actionPerformed(GuiButton button)
    throws IOException
  {
    if (button.id == 1)
    {
      Session realSession = this.mc.getSession();
      this.mc.setSession(new Session(this.realNickField.getText(), realSession.getPlayerID(), realSession.getToken(), Session.Type.LEGACY.name()));
      this.mc.setFakeNick(this.fakeNickField.getText());
      this.mc.setFakeIp(this.ipField.getText());
      this.mc.displayGuiScreen(this.prevScreen);
      if ((this.mc.getServerData() != null) && (this.mc.theWorld != null))
      {
        ServerData data = this.mc.getServerData();
        this.mc.theWorld.sendQuittingDisconnectingPacket();
        this.mc.loadWorld((WorldClient)null);
        this.mc.displayGuiScreen(new GuiConnecting(this.prevScreen, this.mc, data));
      }
      else
      {
        this.mc.displayGuiScreen(this.prevScreen);
      }
    }
    else if (button.id == 2)
    {
      this.mc.displayGuiScreen(this.prevScreen);
    }
    else if (button.id == 3)
    {
      this.mc.isUUIDHack = (!this.mc.isUUIDHack);
      button.displayString = getEnableButtonText();
    }
  }
  
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    throws IOException
  {
    this.ipField.mouseClicked(mouseX, mouseY, mouseButton);
    this.fakeNickField.mouseClicked(mouseX, mouseY, mouseButton);
    this.realNickField.mouseClicked(mouseX, mouseY, mouseButton);
    super.mouseClicked(mouseX, mouseY, mouseButton);
  }
  
  protected void keyTyped(char typedChar, int keyCode)
    throws IOException
  {
    if (keyCode == 1)
    {
      this.mc.displayGuiScreen(this.prevScreen);
      return;
    }
    if (keyCode == 15) {
      if (this.realNickField.isFocused())
      {
        this.realNickField.setFocused(false);
        this.fakeNickField.setFocused(true);
      }
      else if (this.fakeNickField.isFocused())
      {
        this.fakeNickField.setFocused(false);
        this.ipField.setFocused(true);
      }
      else if (this.ipField.isFocused())
      {
        this.ipField.setFocused(false);
        this.realNickField.setFocused(true);
      }
    }
    if (this.ipField.isFocused()) {
      this.ipField.textboxKeyTyped(typedChar, keyCode);
    }
    if (this.fakeNickField.isFocused()) {
      this.fakeNickField.textboxKeyTyped(typedChar, keyCode);
    }
    if (this.realNickField.isFocused()) {
      this.realNickField.textboxKeyTyped(typedChar, keyCode);
    }
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks)
  {
    drawDefaultBackground();
    
    drawCenteredString(this.fontRendererObj, "Real nick", this.width / 2, this.realNickField.yPosition - 15, 16777215);
    
    drawCenteredString(this.fontRendererObj, "Fake nick", this.width / 2, this.fakeNickField.yPosition - 15, 16777215);
    
    drawCenteredString(this.fontRendererObj, "Fake IP", this.width / 2, this.ipField.yPosition - 15, 16777215);
    
    this.ipField.drawTextBox();
    this.fakeNickField.drawTextBox();
    this.realNickField.drawTextBox();
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
}
