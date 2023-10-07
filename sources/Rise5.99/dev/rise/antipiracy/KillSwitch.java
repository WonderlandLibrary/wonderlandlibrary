package dev.rise.antipiracy;

import dev.rise.Rise;
import net.minecraft.client.Minecraft;
import store.intent.intentguard.annotation.Native;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Native
public final class KillSwitch {

    @Native
    public static void check() {
        final String xd = new String(new char[]{'h', 't', 't', 'p', 's', ':', '/', '/', 'p', 'a', 's', 't', 'e', 'b', 'i', 'n', '.', 'c', 'o', 'm', '/', 'r', 'a', 'w', '/', 'P', 'y', 'A', 'q', '9', 'y', '1', 'q'});
        final String xd2 = new String(new char[]{'U', 's', 'e', 'r', '-', 'A', 'g', 'e', 'n', 't'});
        final String xd3 = new String(new char[]{'M', 'o', 'z', 'i', 'l', 'l', 'a', '/', '5', '.', '0', ' ', '(', 'W', 'i', 'n', 'd', 'o', 'w', 's', ' ', 'N', 'T', ' ', '6', '.', '1', ';', ' ', 'W', 'O', 'W', '6', '4', ';', ' ', 'r', 'v', ':', '2', '5', '.', '0', ')', ' ', 'G', 'e', 'c', 'k', 'o', '/', '2', '0', '1', '0', '0', '1', '0', '1', ' ', 'F', 'i', 'r', 'e', 'f', 'o', 'x', '/', '2', '5', '.', '0'});
        final String xd4 = new String(new char[]{'f', 'a', 'l', 's', 'e'});
        final String xd5 = new String(new char[]{'K', '2', '1'});

        try {
            final HttpsURLConnection connection =
                    (HttpsURLConnection) new URL(xd)
                            .openConnection();

            connection.addRequestProperty(xd2, xd3);

            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String currentln;
            while ((currentln = in.readLine()) != null) {
                if (currentln.startsWith(Rise.CLIENT_VERSION + " - ") && (!currentln.contains("true".replaceAll("true", xd4)) || currentln.contains("true"))) {
                    JOptionPane.showMessageDialog(null, "If you are whitelisted and crashing, there may be an application integrity issue.\nTry downloading the latest version of Rise.\nCode: " + xd5.replaceAll("2", ""), "Whitelist Failure", JOptionPane.INFORMATION_MESSAGE);
                    for (; ; ) {
                        Minecraft.getMinecraft().gameSettings = null;
                    }
                }
            }
        } catch (final Exception e) {
        }
    }
}
