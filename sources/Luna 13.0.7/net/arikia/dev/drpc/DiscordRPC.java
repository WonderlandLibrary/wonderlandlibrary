package net.arikia.dev.drpc;

import com.sun.jna.Library;
import com.sun.jna.Native;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;

public final class DiscordRPC
{
  private static final String DLL_VERSION = "3.2.0";
  
  public DiscordRPC() {}
  
  public static void discordInitialize(String applicationId, DiscordEventHandlers handlers, boolean autoRegister)
  {
    DLL.INSTANCE.Discord_Initialize(applicationId, handlers, autoRegister ? 1 : 0, null);
  }
  
  public static void discordRegister(String applicationId, String command)
  {
    DLL.INSTANCE.Discord_Register(applicationId, command);
  }
  
  public static void discordInitialize(String applicationId, DiscordEventHandlers handlers, boolean autoRegister, String steamId)
  {
    DLL.INSTANCE.Discord_Initialize(applicationId, handlers, autoRegister ? 1 : 0, steamId);
  }
  
  public static void discordRegisterSteam(String applicationId, String steamId)
  {
    DLL.INSTANCE.Discord_RegisterSteamGame(applicationId, steamId);
  }
  
  public static void discordUpdateEventHandlers(DiscordEventHandlers handlers)
  {
    DLL.INSTANCE.Discord_UpdateHandlers(handlers);
  }
  
  public static void discordShutdown()
  {
    DLL.INSTANCE.Discord_Shutdown();
  }
  
  public static void discordRunCallbacks()
  {
    DLL.INSTANCE.Discord_RunCallbacks();
  }
  
  public static void discordUpdatePresence(DiscordRichPresence presence)
  {
    DLL.INSTANCE.Discord_UpdatePresence(presence);
  }
  
  public static void discordClearPresence()
  {
    DLL.INSTANCE.Discord_ClearPresence();
  }
  
  public static void discordRespond(String userId, DiscordReply reply)
  {
    DLL.INSTANCE.Discord_Respond(userId, reply.reply);
  }
  
  private static void loadDLL()
  {
    String name = System.mapLibraryName("discord-rpc");
    String finalPath = "";
    String tempPath = "";
    if (SystemUtils.IS_OS_WINDOWS)
    {
      boolean is64bit = System.getProperty("sun.arch.data.model").equals("64");
      finalPath = is64bit ? "/win-x64/discord-rpc.dll" : "/win-x86/discord-rpc.dll";
      tempPath = System.getenv("TEMP") + "/discord-rpc.jar/discord-rpc.dll";
    }
    else if (SystemUtils.IS_OS_LINUX)
    {
      finalPath = "/linux/discord-rpc.so";
      tempPath = System.getenv("TMPDIR") + "/discord-rpc.jar/discord-rpc.so";
    }
    else if ((SystemUtils.IS_OS_MAC) || (SystemUtils.IS_OS_MAC_OSX))
    {
      finalPath = "/osx/discord-rpc.dylib";
      tempPath = System.getenv("TMPDIR") + "/discord-rpc/discord-rpc.dylib";
    }
    File f = new File(tempPath);
    try
    {
      InputStream in = DiscordRPC.class.getResourceAsStream(finalPath);Throwable localThrowable6 = null;
      try
      {
        OutputStream out = FileUtils.openOutputStream(f);Throwable localThrowable7 = null;
        try
        {
          IOUtils.copy(in, out);
          FileUtils.forceDeleteOnExit(f);
        }
        catch (Throwable localThrowable1)
        {
          localThrowable7 = localThrowable1;throw localThrowable1;
        }
        finally {}
      }
      catch (Throwable localThrowable4)
      {
        localThrowable6 = localThrowable4;throw localThrowable4;
      }
      finally
      {
        if (in != null) {
          if (localThrowable6 != null) {
            try
            {
              in.close();
            }
            catch (Throwable localThrowable5)
            {
              localThrowable6.addSuppressed(localThrowable5);
            }
          } else {
            in.close();
          }
        }
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    System.load(f.getAbsolutePath());
  }
  
  static {}
  
  private static abstract interface DLL
    extends Library
  {
    public static final DLL INSTANCE = (DLL)Native.loadLibrary("discord-rpc", DLL.class);
    
    public abstract void Discord_Initialize(String paramString1, DiscordEventHandlers paramDiscordEventHandlers, int paramInt, String paramString2);
    
    public abstract void Discord_Register(String paramString1, String paramString2);
    
    public abstract void Discord_RegisterSteamGame(String paramString1, String paramString2);
    
    public abstract void Discord_UpdateHandlers(DiscordEventHandlers paramDiscordEventHandlers);
    
    public abstract void Discord_Shutdown();
    
    public abstract void Discord_RunCallbacks();
    
    public abstract void Discord_UpdatePresence(DiscordRichPresence paramDiscordRichPresence);
    
    public abstract void Discord_ClearPresence();
    
    public abstract void Discord_Respond(String paramString, int paramInt);
  }
}
