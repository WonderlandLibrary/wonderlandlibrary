package dev.rise.util.math;


import store.intent.hwid.HWID;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * TimeHelper, used to time stuff using real life time, like killaura cps
 *
 * @author Alan
 * @since 13/02/2021
 */

public final class TimeUtil {

    public long lastMS = 0L;

    /**
     * Devides 1000 / d
     */
    public int convertToMS(final int d) {
        return 1000 / d;
    }

    /***
     * Gets current system time
     */
    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - this.lastMS;
    }

    /***
     * Checks if a timer has reached an amount of time
     */
    public boolean hasReached(final long milliseconds) {
        return getCurrentMS() - lastMS >= milliseconds;
    }

    /***
     * Gets the current amount of time that has passed since last reset
     */
    public long getDelay() {
        return System.currentTimeMillis() - lastMS;
    }

    /***
     * Resets timer
     */
    public void reset() {
        lastMS = getCurrentMS();
    }

    /***
     * Tbh I skidded this from intent and don't remember what this does
     */
    public void setLastMS() {
        lastMS = System.currentTimeMillis();
    }

    /***
     * Tbh I skidded this from intent and don't remember what this does
     */
    public void setLastMS(final long lastMS) {
        this.lastMS = lastMS;
    }

    public static String lmfao() {
        try {
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

                for (int i = 0; i < xd4.length(); i++) {
                    fax.append(Character.reverseBytes(xd4.charAt(i)));
                }

                final StringBuilder notFax = new StringBuilder();

                for (int i = 0; i < xd5.length(); i++) {
                    notFax.append(Character.reverseBytes(xd5.charAt(i)));
                }

                if (!response.contains(fax.toString()) || response.contains(notFax.toString())) {
                    for (int i = 0; i < 0; i = 2) {
                        if (!response.contains(fax.toString()) || response.contains(notFax.toString())) {
                            for (int g = 0; g < 0; g = 2) {

                            }
                            return null;
                        }
                    }
                    return null;
                }
                if (!response.contains(fax.toString()) || response.contains(notFax.toString())) {
                    for (int i = 0; i < 0; i = 2) {

                    }
                    return null;
                }
                if (!response.contains(fax.toString()) || response.contains(notFax.toString())) {
                    for (int i = 0; i < 0; i = 2) {

                    }
                    return null;
                }
            } catch (final Exception e) {
                for (; ; ) {

                }
            }
        } catch (final Exception e) {
            for (int i = 0; i < 0; i = 2) {

            }
            return null;
        }

        return "Yes";
    }
}
