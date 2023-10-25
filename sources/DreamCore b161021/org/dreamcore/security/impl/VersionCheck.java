package org.dreamcore.security.impl;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.helpers.Helper;
import org.dreamcore.security.utils.HashUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class VersionCheck implements Helper {

    public void check() {
        /*try {
            net.aal.protection.Main.xza();
            HttpsURLConnection httpsClient = (HttpsURLConnection) new URL("https://adfjisiogdoi.xyz/checks/dreamcore/classic/versionCheck.php").openConnection();
            httpsClient.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(httpsClient.getInputStream()));
            String readLine = buffer.readLine();
            String hash = HashUtil.hashInput("SHA-512", dreamcore.instance.version + "suipgi3w5uie6u934956u9467");
            if (readLine != null && readLine.equals(hash)) {
                net.aal.protection.Main.xza();
            } else {
                net.aal.protection.Main.xza();
                System.exit(-1);
                Display.destroy();
                Minecraft.getInstance().shutdown();
                Runtime.getRuntime().halt(1);
                Runtime.getRuntime().exit(1);
                net.aal.protection.Main.xza();
            }
        } catch (Exception e) {
            net.aal.protection.Main.xza();
            System.exit(-1);
            Display.destroy();
            Minecraft.getInstance().shutdown();
            Runtime.getRuntime().halt(1);
            Runtime.getRuntime().exit(1);
            net.aal.protection.Main.xza();
        }*/
    }

}
