package space.lunaclient.luna.impl.gui.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.awt.AWTException;
import java.awt.TrayIcon.MessageType;
import java.net.Proxy;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import space.lunaclient.luna.impl.alert.DisplayTray;

public class AltLoginThread
  extends Thread
{
  private final Minecraft mc = Minecraft.getMinecraft();
  private final String password;
  private String status;
  private final String username;
  
  AltLoginThread(String username, String password)
  {
    super("Alt Login Thread");
    this.username = username;
    this.password = password;
    this.status = "§eWaiting...";
  }
  
  public void updateScreen()
  {
    GuiAltLogin.time = 100;
  }
  
  private Session createSession(String username, String password)
  {
    YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
    
    YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
    auth.setUsername(username);
    auth.setPassword(password);
    try
    {
      auth.logIn();
      return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth
        .getAuthenticatedToken(), "mojang");
    }
    catch (AuthenticationException localAuthenticationException) {}
    return null;
  }
  
  public String getStatus()
  {
    return this.status;
  }
  
  public void run()
  {
    if (this.password.isEmpty())
    {
      this.mc.session = new Session(this.username, "", "", "mojang");
      this.status = ("§aLogged in. (" + this.username + " - offline Name)");
      if (GuiAltLogin.isGeneratedAlt) {
        try
        {
          DisplayTray.displayTray("Luna", "Alt Generated! Logged in.", TrayIcon.MessageType.INFO);
        }
        catch (AWTException e)
        {
          e.printStackTrace();
        }
      }
      return;
    }
    this.status = "§1Logging in...";
    Session auth = createSession(this.username, this.password);
    if (auth == null)
    {
      this.status = "§4Login failed!";
    }
    else
    {
      this.status = ("§aLogged in. (" + auth.getUsername() + ")");
      this.mc.session = auth;
    }
  }
  
  public void setStatus(String status)
  {
    this.status = status;
  }
}
