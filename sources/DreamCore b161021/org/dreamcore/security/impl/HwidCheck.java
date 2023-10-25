package org.dreamcore.security.impl;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.dreamcore.client.helpers.Helper;
import org.dreamcore.security.utils.HashUtil;
import org.dreamcore.security.utils.HwidUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class HwidCheck implements Helper {

    public void check() {
        /*try {
            String username = System.getProperty("user.name");
            HttpsURLConnection httpsClient = (HttpsURLConnection) new URL("https://adfjisiogdoi.xyz/checks/dreamcore/classic/maincheck.php?hwid=" + HwidUtils.getHwid() + "&username=" + username).openConnection();
            httpsClient.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(httpsClient.getInputStream()));
            String readLine = buffer.readLine();
            String hash = HashUtil.hashInput("SHA-1", HwidUtils.getHwid() + username + "*gxro5LRtZ~oUn%nD7vi");
            hash = HashUtil.hashInput("SHA-512", hash);
            if (readLine != null && readLine.equals(hash)) {
            } else {
                System.exit(-1);
                Display.destroy();
                Minecraft.getInstance().shutdown();
                Runtime.getRuntime().halt(1);
                Runtime.getRuntime().exit(1);
            }
        } catch (Exception e) {
            System.exit(-1);
            Display.destroy();
            Minecraft.getInstance().shutdown();
            Runtime.getRuntime().halt(1);
            Runtime.getRuntime().exit(1);
        }*/
    }
}
