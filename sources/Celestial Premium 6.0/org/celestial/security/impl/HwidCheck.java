/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.security.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.celestial.client.helpers.Helper;
import org.celestial.security.utils.HashUtil;
import org.celestial.security.utils.HwidUtils;

public class HwidCheck
implements Helper {
    public void check() {
        try {
            String username = System.getProperty("user.name");
            HttpsURLConnection httpsClient = (HttpsURLConnection)new URL("https://adfjisiogdoi.xyz/checks/celestial/classic/maincheck.php?hwid=" + HwidUtils.getHwid() + "&username=" + username).openConnection();
            httpsClient.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(httpsClient.getInputStream()));
            String readLine = buffer.readLine();
            String hash = HashUtil.hashInput("SHA-512", "Abobus+Fb^.55i916b!etjl2=Ulu4bxA0mt2pX");
            if (readLine != null && readLine.contains(hash)) {
                System.exit(-1);
                Runtime.getRuntime().halt(1);
            } else {
                System.out.println(httpsClient.getURL());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
            Runtime.getRuntime().halt(1);
        }
    }
}

