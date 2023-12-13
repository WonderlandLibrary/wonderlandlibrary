package net.arikia.dev.drpc;

import com.sun.jna.Library;
import com.sun.jna.Native;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class DiscordRPC {
   private static final String DLL_VERSION = "3.4.0";
   private static final String LIB_VERSION = "1.6.2";

   public static void discordInitialize(String applicationId, DiscordEventHandlers handlers, boolean autoRegister) {
      DiscordRPC.DLL.INSTANCE.Discord_Initialize(applicationId, handlers, autoRegister ? 1 : 0, (String)null);
   }

   public static void discordRegister(String applicationId, String command) {
      DiscordRPC.DLL.INSTANCE.Discord_Register(applicationId, command);
   }

   public static void discordInitialize(String applicationId, DiscordEventHandlers handlers, boolean autoRegister, String steamId) {
      DiscordRPC.DLL.INSTANCE.Discord_Initialize(applicationId, handlers, autoRegister ? 1 : 0, steamId);
   }

   public static void discordRegisterSteam(String applicationId, String steamId) {
      DiscordRPC.DLL.INSTANCE.Discord_RegisterSteamGame(applicationId, steamId);
   }

   public static void discordUpdateEventHandlers(DiscordEventHandlers handlers) {
      DiscordRPC.DLL.INSTANCE.Discord_UpdateHandlers(handlers);
   }

   public static void discordShutdown() {
      DiscordRPC.DLL.INSTANCE.Discord_Shutdown();
   }

   public static void discordRunCallbacks() {
      DiscordRPC.DLL.INSTANCE.Discord_RunCallbacks();
   }

   public static void discordUpdatePresence(DiscordRichPresence presence) {
      DiscordRPC.DLL.INSTANCE.Discord_UpdatePresence(presence);
   }

   public static void discordClearPresence() {
      DiscordRPC.DLL.INSTANCE.Discord_ClearPresence();
   }

   public static void discordRespond(String userId, DiscordReply reply) {
      DiscordRPC.DLL.INSTANCE.Discord_Respond(userId, reply.reply);
   }

   private static void loadDLL() {
      String name = System.mapLibraryName("discord-rpc");
      OSUtil osUtil = new OSUtil();
      File homeDir;
      String finalPath;
      String tempPath;
      File f;
      String dir;
      InputStream in;
      Throwable var8;
      FileOutputStream out;
      Throwable var10;
      if (osUtil.isMac()) {
         homeDir = new File(System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support" + File.separator);
         dir = "darwin";
         tempPath = homeDir + File.separator + "discord-rpc" + File.separator + name;
      } else {
         if (!osUtil.isWindows()) {
            finalPath = "/discord/linux/" + name;

            try {
               f = File.createTempFile("drpc", name);
               in = DiscordRPC.class.getResourceAsStream(finalPath);
               var8 = null;

               try {
                  out = openOutputStream(f);
                  var10 = null;

                  try {
                     if (in == null) {
                        throw new FileNotFoundException("Native Linux .so library missing. Please open an issue. https://github.com/Vatuu/discord-rpc");
                     }

                     copyFile(in, out);
                     f.deleteOnExit();
                     System.load(f.getAbsolutePath());
                  } catch (Throwable var95) {
                     var10 = var95;
                     throw var95;
                  } finally {
                     if (out != null) {
                        if (var10 != null) {
                           try {
                              out.close();
                           } catch (Throwable var87) {
                              var10.addSuppressed(var87);
                           }
                        } else {
                           out.close();
                        }
                     }

                  }
               } catch (Throwable var97) {
                  var8 = var97;
                  throw var97;
               } finally {
                  if (in != null) {
                     if (var8 != null) {
                        try {
                           in.close();
                        } catch (Throwable var86) {
                           var8.addSuppressed(var86);
                        }
                     } else {
                        in.close();
                     }
                  }

               }

               return;
            } catch (IOException var99) {
               var99.printStackTrace();
               System.out.println("Fatal Discord RPC exception occurred. Discord RPC will be unavailable for this session.");
               return;
            }
         }

         homeDir = new File(System.getenv("TEMP"));
         boolean is64bit = System.getProperty("sun.arch.data.model").equals("64");
         dir = is64bit ? "win-x64" : "win-x86";
         tempPath = homeDir + File.separator + "discord-rpc" + File.separator + name;
      }

      finalPath = "/discord/" + dir + "/" + name;
      f = new File(tempPath);

      try {
         in = DiscordRPC.class.getResourceAsStream(finalPath);
         var8 = null;

         try {
            out = openOutputStream(f);
            var10 = null;

            try {
               copyFile(in, out);
               f.deleteOnExit();
            } catch (Throwable var90) {
               var10 = var90;
               throw var90;
            } finally {
               if (out != null) {
                  if (var10 != null) {
                     try {
                        out.close();
                     } catch (Throwable var89) {
                        var10.addSuppressed(var89);
                     }
                  } else {
                     out.close();
                  }
               }

            }
         } catch (Throwable var92) {
            var8 = var92;
            throw var92;
         } finally {
            if (in != null) {
               if (var8 != null) {
                  try {
                     in.close();
                  } catch (Throwable var88) {
                     var8.addSuppressed(var88);
                  }
               } else {
                  in.close();
               }
            }

         }
      } catch (IOException var94) {
         var94.printStackTrace();
      }

      System.load(f.getAbsolutePath());
   }

   private static void copyFile(InputStream input, OutputStream output) throws IOException {
      byte[] buffer = new byte[4096];

      int n;
      while(-1 != (n = input.read(buffer))) {
         output.write(buffer, 0, n);
      }

   }

   private static FileOutputStream openOutputStream(File file) throws IOException {
      if (file.exists()) {
         if (file.isDirectory()) {
            throw new IOException("File '" + file + "' exists but is a directory");
         }

         if (!file.canWrite()) {
            throw new IOException("File '" + file + "' cannot be written to");
         }
      } else {
         File parent = file.getParentFile();
         if (parent != null && !parent.mkdirs() && !parent.isDirectory()) {
            throw new IOException("Directory '" + parent + "' could not be created");
         }
      }

      return new FileOutputStream(file);
   }

   static {
      loadDLL();
   }

   private interface DLL extends Library {
      DLL INSTANCE = (DLL)Native.loadLibrary("discord-rpc", DLL.class);

      void Discord_Initialize(String var1, DiscordEventHandlers var2, int var3, String var4);

      void Discord_Register(String var1, String var2);

      void Discord_RegisterSteamGame(String var1, String var2);

      void Discord_UpdateHandlers(DiscordEventHandlers var1);

      void Discord_Shutdown();

      void Discord_RunCallbacks();

      void Discord_UpdatePresence(DiscordRichPresence var1);

      void Discord_ClearPresence();

      void Discord_Respond(String var1, int var2);
   }

   public static enum DiscordReply {
      NO(0),
      YES(1),
      IGNORE(2);

      public final int reply;

      private DiscordReply(int reply) {
         this.reply = reply;
      }
   }
}
