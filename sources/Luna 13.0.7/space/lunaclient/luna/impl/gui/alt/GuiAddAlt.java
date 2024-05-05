package space.lunaclient.luna.impl.gui.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.net.Proxy;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.impl.managers.CustomFileManager;

public class GuiAddAlt
  extends GuiScreen
{
  private final GuiAltManager manager;
  private GuiPasswordField password;
  
  private class AddAltThread
    extends Thread
  {
    private final String password;
    private final String username;
    
    AddAltThread(String username, String password)
    {
      this.username = username;
      this.password = password;
      GuiAddAlt.this.status = "§7Waiting...";
    }
    
    private void checkAndAddAlt(String username, String password)
    {
      YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
      
      YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
      auth.setUsername(username);
      auth.setPassword(password);
      try
      {
        auth.logIn();
        Luna.INSTANCE.ALT_MANAGER.getAlts().add(new Alt(username, password));
        CustomFileManager.saveAlts();
        GuiAddAlt.this.status = ("§aAlt added. (" + username + ")");
      }
      catch (AuthenticationException e)
      {
        GuiAddAlt.this.status = "§4Alt failed!";
        e.printStackTrace();
      }
    }
    
    public void run()
    {
      if (this.password.equals(""))
      {
        Luna.INSTANCE.ALT_MANAGER.getAlts().add(new Alt(this.username, ""));
        CustomFileManager.saveAlts();
        GuiAddAlt.this.status = ("§aAlt added. (" + this.username + " - offline Name)");
        return;
      }
      GuiAddAlt.this.status = "§1Trying alt...";
      checkAndAddAlt(this.username, this.password);
    }
  }
  
  private String status = "§eWaiting...";
  private GuiTextField username;
  
  GuiAddAlt(GuiAltManager manager)
  {
    this.manager = manager;
  }
  
  protected void actionPerformed(GuiButton button)
  {
    switch (button.id)
    {
    case 0: 
      AddAltThread login = new AddAltThread(this.username.getText(), this.password.getText());
      login.start();
      break;
    case 1: 
      this.mc.displayGuiScreen(this.manager);
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
    }
  }
  
  public void drawScreen(int i, int j, float f)
  {
    drawDefaultBackground();
    
    drawCenteredString(this.fontRendererObj, "Add Alt", width / 2, 20, -1);
    this.username.drawTextBox();
    this.password.drawTextBox();
    if (this.username.getText().isEmpty()) {
      drawString(Minecraft.fontRendererObj, "Username / E-Mail", width / 2 - 96, 66, -7829368);
    }
    if (this.password.getText().isEmpty()) {
      drawString(Minecraft.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);
    }
    drawCenteredString(this.fontRendererObj, this.status, width / 2, 30, -1);
    
    super.drawScreen(i, j, f);
  }
  
  public void initGui()
  {
    Keyboard.enableRepeatEvents(true);
    this.buttonList.clear();
    this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 92 + 12, "Login"));
    this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 116 + 12, "Back"));
    this.buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 116 + 36, "Import user:pass"));
    this.username = new GuiTextField(1, Minecraft.fontRendererObj, width / 2 - 100, 60, 200, 20);
    this.password = new GuiPasswordField(Minecraft.fontRendererObj, width / 2 - 100, 100, 200, 20);
  }
  
  protected void keyTyped(char par1, int par2)
  {
    this.username.textboxKeyTyped(par1, par2);
    this.password.textBoxTyped(par1, par2);
    if ((par1 == '\t') && ((this.username.isFocused()) || (this.password.isFocused())))
    {
      this.username.setFocused(!this.username.isFocused());
      this.password.setFocused(!this.password.isFocused());
    }
    if (par1 == '\r') {
      actionPerformed((GuiButton)this.buttonList.get(0));
    }
  }
  
  protected void mouseClicked(int par1, int par2, int par3)
  {
    try
    {
      super.mouseClicked(par1, par2, par3);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    this.username.mouseClicked(par1, par2, par3);
    this.password.mouseClicked(par1, par2, par3);
  }
}
