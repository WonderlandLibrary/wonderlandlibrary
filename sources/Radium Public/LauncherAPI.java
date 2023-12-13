import net.minecraft.client.main.Main;

import java.io.File;

public final class LauncherAPI {

    public static void launch(boolean fullscreen) {
        final String userHome = System.getProperty("user.home", ".");
        final String applicationData = System.getenv("APPDATA");
        final String folder = (applicationData != null) ? applicationData : userHome;
        File workingDirectory = new File(folder, ".minecraft/");
        Main.main(new String[]{
                "--version", "1.8.9",
                "--accessToken", "0",
                (fullscreen ? "--fullscreen" : ""),
                "--assetIndex", "1.8",
                "--userProperties", "{}",
                "--gameDir", new File(workingDirectory, ".").getAbsolutePath(),
                "--assetsDir", new File(workingDirectory, "assets/").getAbsolutePath()});
    }
}
