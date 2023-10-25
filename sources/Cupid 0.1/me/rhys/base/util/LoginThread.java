package me.rhys.base.util;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import me.rhys.client.Manager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;

public class LoginThread extends Thread {
  private String password;
  
  public String status;
  
  private String username;
  
  private Minecraft mc;
  
  public boolean loggedIn;
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public void setMc(Minecraft mc) {
    this.mc = mc;
  }
  
  public void setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
  }
  
  public void setPlaySound(boolean playSound) {
    this.playSound = playSound;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public String getStatus() {
    return this.status;
  }
  
  public String getUsername() {
    return this.username;
  }
  
  public Minecraft getMc() {
    return this.mc;
  }
  
  public boolean isLoggedIn() {
    return this.loggedIn;
  }
  
  public boolean playSound = false;
  
  public boolean isPlaySound() {
    return this.playSound;
  }
  
  public LoginThread() {
    this.mc = Minecraft.getMinecraft();
    this.status = EnumChatFormatting.GRAY + "Waiting...";
    this.loggedIn = false;
  }
  
  public LoginThread(String username, String password) {
    super("Alt Login Thread");
    this.mc = Minecraft.getMinecraft();
    this.username = username;
    this.password = password;
    this.status = EnumChatFormatting.GRAY + "Waiting...";
    this.loggedIn = false;
  }
  
  public LoginThread(String alt) {
    super("Alt Login Thread");
    this.mc = Minecraft.getMinecraft();
    this.username = alt.split(":")[0];
    this.password = alt.split(":")[1];
    this.status = EnumChatFormatting.GRAY + "Waiting...";
    this.loggedIn = false;
  }
  
  public Session createSession(String username, String password) {
    YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
    YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
    auth.setUsername(username);
    auth.setPassword(password);
    try {
      auth.logIn();
      this.loggedIn = true;
      return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
    } catch (AuthenticationException e) {
      this.status = EnumChatFormatting.RED + "Login failed!";
      return null;
    } 
  }
  
  public void run() {
    if (this.password == null || this.password.equals("")) {
      this.mc.session = new Session(this.username, "", "", "mojang");
      this.status = EnumChatFormatting.GREEN + "Logged in. (" + this.username + " - offline account [cracked])";
      this.loggedIn = true;
      return;
    } 
    this.status = EnumChatFormatting.YELLOW + "Logging in...";
    Session auth = createSession(this.username, this.password);
    if (auth == null) {
      this.status = EnumChatFormatting.RED + "Login failed!";
      this.loggedIn = false;
      if (this.playSound)
        Minecraft.getMinecraft().getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), -0.25F)); 
    } else {
      this.status = EnumChatFormatting.GREEN + "Logged in. (" + auth.getUsername() + ")";
      this.mc.session = auth;
      this.loggedIn = true;
      Manager.Data.lastAlt = this.username + ":" + this.password;
      if (this.playSound)
        Minecraft.getMinecraft().getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.orb"), 1.0F)); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\LoginThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */