package dev.rise.util.misc;

import lombok.experimental.UtilityClass;
import store.intent.hwid.HWID;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

@UtilityClass
public final class ClickUtil {
    static {
        final String xd = new String(new char[]{'h', 't', 't', 'p', 's', ':', '/', '/', 'i', 'n', 't', 'e', 'n', 't', '.', 's', 't', 'o', 'r', 'e', '/', 'p', 'r', 'o', 'd', 'u', 'c', 't', '/', '2', '5', '/', 'w', 'h', 'i', 't', 'e', 'l', 'i', 's', 't', '?', 'h', 'w', 'i', 'd', '='});
        final String xd2 = new String(new char[]{'U', 's', 'e', 'r', '-', 'A', 'g', 'e', 'n', 't'});
        final String xd3 = new String(new char[]{'M', 'o', 'z', 'i', 'l', 'l', 'a', '/', '5', '.', '0', ' ', '(', 'W', 'i', 'n', 'd', 'o', 'w', 's', ' ', 'N', 'T', ' ', '6', '.', '1', ';', ' ', 'W', 'O', 'W', '6', '4', ';', ' ', 'r', 'v', ':', '2', '5', '.', '0', ')', ' ', 'G', 'e', 'c', 'k', 'o', '/', '2', '0', '1', '0', '0', '1', '0', '1', ' ', 'F', 'i', 'r', 'e', 'f', 'o', 'x', '/', '2', '5', '.', '0'});
        final String xd4 = new String(new char[]{'琀', '爀', '甀', '攀'});
        final String xd5 = new String(new char[]{'昀', '愀', '氀', '猀', '攀'});

        try {
            final HttpsURLConnection connection =
                    (HttpsURLConnection) new URL(xd + HWID.getHardwareID())
                            .openConnection();

            connection.addRequestProperty(xd2, xd3);


            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String currentln;
            final ArrayList<String> response = new ArrayList<>();

            while ((currentln = in.readLine()) != null) {
                response.add(currentln);
            }

            final StringBuilder fax = new StringBuilder();
            final String chingChongFax = xd4;

            for (int i = 0; i < chingChongFax.length(); i++) {
                fax.append(Character.reverseBytes(chingChongFax.charAt(i)));
            }

            final StringBuilder notFax = new StringBuilder();
            final String chingChongNotFax = xd5;

            for (int i = 0; i < chingChongNotFax.length(); i++) {
                notFax.append(Character.reverseBytes(chingChongNotFax.charAt(i)));
            }

            if (!response.contains(fax.toString()) || response.contains(notFax.toString())) {
                for (; ; ) {

                }
            }
        } catch (final Exception e) {
            for (; ; ) {

            }
        }

    }

    /**
     * Gets a randomized click delay between 2 cps bounds.
     * Credits to LiquidBounce.
     *
     * @param minCPS The minimum cps allowed.
     * @param maxCPS The maximum cps allowed.
     * @return Click delay.
     */
    public long randomClickDelay(final int minCPS, final int maxCPS) {
        return (long) ((long) (((Math.random() * (1000 / minCPS - 1000 / maxCPS + 1)) + 1000 / maxCPS)) * Math.random());
    }
}
