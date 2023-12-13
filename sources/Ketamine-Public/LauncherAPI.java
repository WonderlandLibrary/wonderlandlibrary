import net.minecraft.client.main.Main;

import java.io.File;

public class LauncherAPI {

    public static void launch() {
        final String appData = System.getenv("APPDATA");
        final File workingDirectory = new File(appData, ".minecraft/");

        Main.main(new String[]{
                "--version", "1.8.9",
                "--accessToken", "0",
                "--assetIndex", "1.8",
                "--userProperties", "{}",
//                "--gameDir", new File(workingDirectory, ".").getAbsolutePath(), // TODO :: Uncomment on release
                "--assetsDir", new File(workingDirectory, "assets/").getAbsolutePath()
        });
    }
}
