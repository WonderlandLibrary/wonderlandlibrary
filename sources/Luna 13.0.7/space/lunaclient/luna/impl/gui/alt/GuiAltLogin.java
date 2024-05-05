package space.lunaclient.luna.impl.gui.alt;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Keyboard;
import space.lunaclient.luna.api.genster.Genster;

public class GuiAltLogin
  extends GuiScreen
{
  private GuiPasswordField password;
  private final GuiScreen previousScreen;
  private AltLoginThread thread;
  private GuiTextField username;
  private GuiButton buttonGen;
  public static int time;
  static boolean isGeneratedAlt = false;
  
  GuiAltLogin(GuiScreen previousScreen)
  {
    this.previousScreen = previousScreen;
  }
  
  protected void actionPerformed(GuiButton button)
  {
    switch (button.id)
    {
    case 1: 
      this.mc.displayGuiScreen(this.previousScreen);
      break;
    case 2: 
      try
      {
        data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
      }
      catch (Exception ignored)
      {
        String data;
        break;
      }
      String data;
      if (data.contains(":"))
      {
        String[] credentials = data.split(":");
        this.username.setText(credentials[0]);
        this.password.setText(credentials[1]);
      }
      break;
    case 3: 
      time += 100;
      isGeneratedAlt = true;
      Genster.initGenster();
      this.thread = new AltLoginThread(Genster.gName, Genster.gPass);
      this.thread.start();
      break;
    case 0: 
      this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
      this.thread.start();
    }
  }
  
  public void drawScreen(int x, int y, float z)
  {
    drawDefaultBackground();
    this.username.drawTextBox();
    this.password.drawTextBox();
    drawCenteredString(Minecraft.fontRendererObj, "Alt Login", width / 2, 20, -1);
    drawCenteredString(Minecraft.fontRendererObj, this.thread == null ? "§aWaiting..." : this.thread.getStatus(), width / 2, 29, -1);
    if (this.username.getText().isEmpty())
    {
      isGeneratedAlt = false;
      drawString(Minecraft.fontRendererObj, "Username / E-Mail", width / 2 - 96, 66, -7829368);
    }
    if (this.password.getText().isEmpty())
    {
      isGeneratedAlt = false;
      drawString(Minecraft.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);
    }
    super.drawScreen(x, y, z);
  }
  
  public void initGui()
  {
    int var3 = height / 4 + 24;
    InputStream in = null;
    try
    {
      in = new URL("http://lunaclient.app/api/genster/onload/length.porn").openStream();
    }
    catch (IOException localIOException) {}
    this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
    this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
    this.buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 119 + 37, "Import user:pass"));
    try
    {
      btT = "Generate";
    }
    finally
    {
      String btT;
      IOUtils.closeQuietly(in);
    }
    String btT;
    this.buttonList.add(this.buttonGen = new GuiButton(3, width / 2 - 100, (int)(height / 4 + 143 + 37.000000001D), btT));
    this.username = new GuiTextField(1, Minecraft.fontRendererObj, width / 2 - 100, 60, 200, 20);
    this.password = new GuiPasswordField(Minecraft.fontRendererObj, width / 2 - 100, 100, 200, 20);
    this.username.setFocused(true);
    Keyboard.enableRepeatEvents(true);
  }
  
  protected void keyTyped(char character, int key)
  {
    try
    {
      super.keyTyped(character, key);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    if (character == '\t') {
      if (((!this.username.isFocused() ? 1 : 0) & (!this.password.isFocused() ? 1 : 0)) != 0)
      {
        this.username.setFocused(true);
      }
      else
      {
        this.username.setFocused(this.password.isFocused());
        this.password.setFocused(!this.username.isFocused());
      }
    }
    if (character == '\r') {
      actionPerformed((GuiButton)this.buttonList.get(0));
    }
    this.username.textboxKeyTyped(character, key);
    this.password.textBoxTyped(character, key);
  }
  
  protected void mouseClicked(int x, int y, int button)
  {
    try
    {
      super.mouseClicked(x, y, button);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    this.username.mouseClicked(x, y, button);
    this.password.mouseClicked(x, y, button);
  }
  
  public void onGuiClosed()
  {
    Keyboard.enableRepeatEvents(false);
  }
  
  public void updateScreen()
  {
    this.username.updateCursorCounter();
    this.password.updateCursorCounter();
    if (time > 0)
    {
      this.buttonGen.enabled = false;
      time = (int)(time - 0.5D);
    }
    else
    {
      this.buttonGen.enabled = true;
    }
  }
}
