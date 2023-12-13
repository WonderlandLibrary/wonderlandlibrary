package net.minecraft.client.main;

import com.mojang.authlib.properties.PropertyMap;
import java.io.File;
import java.net.Proxy;
import net.minecraft.util.Session;

public class GameConfiguration {
   public final GameConfiguration.ServerInformation serverInfo;
   public final GameConfiguration.DisplayInformation displayInfo;
   public final GameConfiguration.GameInformation gameInfo;
   public final GameConfiguration.FolderInformation folderInfo;
   public final GameConfiguration.UserInformation userInfo;

   public GameConfiguration(GameConfiguration.UserInformation var1, GameConfiguration.DisplayInformation var2, GameConfiguration.FolderInformation var3, GameConfiguration.GameInformation var4, GameConfiguration.ServerInformation var5) {
      this.userInfo = var1;
      this.displayInfo = var2;
      this.folderInfo = var3;
      this.gameInfo = var4;
      this.serverInfo = var5;
   }

   public static class GameInformation {
      public final String version;
      public final boolean isDemo;

      public GameInformation(boolean var1, String var2) {
         this.isDemo = var1;
         this.version = var2;
      }
   }

   public static class UserInformation {
      public final PropertyMap field_181172_c;
      public final Proxy proxy;
      public final PropertyMap userProperties;
      public final Session session;

      public UserInformation(Session var1, PropertyMap var2, PropertyMap var3, Proxy var4) {
         this.session = var1;
         this.userProperties = var2;
         this.field_181172_c = var3;
         this.proxy = var4;
      }
   }

   public static class ServerInformation {
      public final int serverPort;
      public final String serverName;

      public ServerInformation(String var1, int var2) {
         this.serverName = var1;
         this.serverPort = var2;
      }
   }

   public static class DisplayInformation {
      public final int height;
      public final int width;
      public final boolean fullscreen;
      public final boolean checkGlErrors;

      public DisplayInformation(int var1, int var2, boolean var3, boolean var4) {
         this.width = var1;
         this.height = var2;
         this.fullscreen = var3;
         this.checkGlErrors = var4;
      }
   }

   public static class FolderInformation {
      public final File assetsDir;
      public final String assetIndex;
      public final File resourcePacksDir;
      public final File mcDataDir;

      public FolderInformation(File var1, File var2, File var3, String var4) {
         this.mcDataDir = var1;
         this.resourcePacksDir = var2;
         this.assetsDir = var3;
         this.assetIndex = var4;
      }
   }
}
