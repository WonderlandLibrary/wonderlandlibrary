package net.minecraft;

import java.io.File;
import net.minecraft.client.main.Main;

public class Start {
  public static void main(String[] args) {
    File workingDirectory;
    String applicationData, folder, userHome = System.getProperty("user.home", ".");
    switch (getPlatform()) {
      case LINUX:
        workingDirectory = new File(userHome, ".minecraft/");
        break;
      case WINDOWS:
        applicationData = System.getenv("APPDATA");
        folder = (applicationData != null) ? applicationData : userHome;
        workingDirectory = new File(folder, ".minecraft/");
        break;
      case MACOS:
        workingDirectory = new File(userHome, "Library/Application Support/minecraft");
        break;
      default:
        workingDirectory = new File(userHome, "minecraft/");
        break;
    } 
    Main.main(new String[] { 
          "--version", "Client", "--accessToken", "0", "--assetIndex", "1.8", "--userProperties", "{}", "--gameDir", (new File(workingDirectory, "."))
          
          .getAbsolutePath(), 
          "--assetsDir", (new File(workingDirectory, "assets/"))
          .getAbsolutePath() });
  }
  
  private static OS getPlatform() {
    String s = System.getProperty("os.name").toLowerCase();
    return s.contains("win") ? OS.WINDOWS : (s.contains("mac") ? OS.MACOS : (s.contains("solaris") ? OS.SOLARIS : (s.contains("sunos") ? OS.SOLARIS : (
      s.contains("linux") ? OS.LINUX : (s.contains("unix") ? OS.LINUX : OS.UNKNOWN)))));
  }
  
  public enum OS {
    LINUX, MACOS, SOLARIS, UNKNOWN, WINDOWS;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\Start.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */