import java.io.File;
import net.minecraft.client.main.Main;
import optifine.Utils;

public class InjectionAPI {
   public static void inject() {
      System.out.println("Loading Alpha Centauri...");
      Thread thread = new Thread(() -> {
         try {
            String userHome = System.getProperty("user.home", ".");
            File workingDirectory;
            switch(Utils.getPlatform()) {
            case LINUX:
               workingDirectory = new File(userHome, ".minecraft/");
               break;
            case WINDOWS:
               String applicationData = System.getenv("APPDATA");
               String folder = applicationData != null?applicationData:userHome;
               workingDirectory = new File(folder, ".minecraft/");
               break;
            case MACOS:
               workingDirectory = new File(userHome, "Library/Application Support/minecraft");
               break;
            default:
               workingDirectory = new File(userHome, "minecraft/");
            }

            Main.main(new String[]{"--version", "AlphaCentauri", "--accessToken", "0", "--assetIndex", "1.8", "--userProperties", "{}", "--gameDir", (new File(workingDirectory, ".")).getAbsolutePath(), "--assetsDir", (new File(workingDirectory, "assets/")).getAbsolutePath()});
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         System.exit(0);
      });
      thread.setDaemon(true);
      thread.start();
   }
}
