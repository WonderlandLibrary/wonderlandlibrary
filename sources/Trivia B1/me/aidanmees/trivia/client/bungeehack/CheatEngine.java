package me.aidanmees.trivia.client.bungeehack;

import java.util.UUID;

import net.minecraft.client.Minecraft;

public class CheatEngine
{
  public static final CheatEngine instance = new CheatEngine();
  public static final String NAME = "Cheat Engine";
  public static final String VERSION = "0.1";
  public boolean fakeCreative;
  public String spoofedName;
  public String spoofedIp;
  
  public String getBungeeHack()
  {
    return "\000" + this.spoofedIp + "\000" + UUID.nameUUIDFromBytes(new StringBuilder("OfflinePlayer:").append(this.spoofedName).toString().getBytes()).toString().replaceAll("-", "");
  }
  
  public void init()
  {
    this.fakeCreative = false;
    this.spoofedName = Minecraft.getMinecraft().getSession().getUsername();
    this.spoofedIp = "127.0.0.1";
  }
}
