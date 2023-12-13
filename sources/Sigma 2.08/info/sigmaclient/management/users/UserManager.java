package info.sigmaclient.management.users;

import info.sigmaclient.Client;
import info.sigmaclient.management.users.impl.Blacklisted;
import info.sigmaclient.management.users.impl.Default;
import info.sigmaclient.management.users.impl.Staff;
import info.sigmaclient.management.users.impl.Upgraded;
import info.sigmaclient.util.security.Crypto;
import info.sigmaclient.util.security.HardwareUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

/**
 * Created by Arithmo on 8/11/2017 at 9:57 PM.
 */
public class UserManager {

    private User userStatus = new Default();

    public User getUser() {
        return userStatus;
    }

    private String initialHWID;
    private String username;

    public UserManager() {
    }

    public void checkUserStatus(String username) {
        this.username = username;
        switch (getOSType()) {
            case WINDOWS:
                try {
                    initialHWID = HardwareUtil.getWindowsSerialNumber();
                } catch (IOException ignored) {
                }
                break;
            case LINUX:
                initialHWID = HardwareUtil.getLinuxSerialNumber();
                break;
            case OSX:
                initialHWID = HardwareUtil.getMacSerialNumber();
                break;
        }
        try {
            firstUsername = Crypto.encrypt(Crypto.getSecret(), username);
            secondUsername = Crypto.encrypt(Crypto.getSecretOLD(), username);
            firstHWID = Crypto.encrypt(Crypto.getSecret(), initialHWID);
            secondHWID = Crypto.encrypt(Crypto.getSecretOLD(), HardwareUtil.getOLDWINDOWS());
        } catch (Exception ignored) {

        }
        //Do HWID Checking, if there's no valid user found, they're default.
        statusCheck();
    }

    private String firstUsername;
    private String secondUsername;
    private String firstHWID;
    private String secondHWID;

    public void statusCheck() {
        applyStatus(initialHWID, username);
    }

    private void applyStatus(String userHWID, String username) {
        (new Thread(() -> {
            String site = "http://pastebin.com/raw.php?i=AXyxaaSf";
            String onlineCheck = "www.pastebin.com";
            boolean connected;
            Socket sock = new Socket();
            InetSocketAddress addr = new InetSocketAddress(onlineCheck, 80);
            try {
                sock.connect(addr, 6000);
                connected = true;
            } catch (IOException e) {
                connected = false;
            } finally {
                try {
                    sock.close();
                } catch (final IOException e) {
                }
            }

            if (connected) {
                try {
                    final URL obj = new URL(site);
                    HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                    conn.setReadTimeout(5000);
                    conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                    conn.addRequestProperty("User-Agent", "Mozilla");
                    conn.addRequestProperty("Referer", "google.com");
                    boolean redirect = false;
                    // normally, 3xx is redirect
                    final int status = conn.getResponseCode();
                    if (status != HttpURLConnection.HTTP_OK) {
                        if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER) {
                            redirect = true;
                        }
                    }
                    if (redirect) {
                        String newUrl = conn.getHeaderField("Location");
                        String cookies = conn.getHeaderField("Set-Cookie");
                        conn = (HttpURLConnection) new URL(newUrl).openConnection();
                        conn.setRequestProperty("Cookie", cookies);
                        conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                        conn.addRequestProperty("User-Agent", "Mozilla");
                        conn.addRequestProperty("Referer", "google.com");
                    }
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        String[] split = line.split(":");
                        if ((split[0].equalsIgnoreCase(firstUsername) || split[0].equalsIgnoreCase(secondUsername))
                                && (split[1].equalsIgnoreCase(firstHWID) || split[1].equalsIgnoreCase(secondHWID))) {
                            switch (split[2]) {
                                case "Staff": {
                                    userStatus = new Staff(username, userHWID);
                                    break;
                                }
                                case "Blacklisted": {
                                    userStatus = new Blacklisted();
                                    break;
                                }
                                case "Upgraded": {
                                    userStatus = new Upgraded(username, userHWID);
                                    break;
                                }
                            }
                        }
                    }
                    in.close();
                } catch (final Exception ignored) {
                }
            } else {
                Client.instance = null;
            }
        })).start();
    }


    public static Crypto.EnumOS getOSType() {
        String var0 = System.getProperty("os.name").toLowerCase();
        return var0.contains("win") ? Crypto.EnumOS.WINDOWS : (var0.contains("mac") ? Crypto.EnumOS.OSX : (var0.contains("solaris") ? Crypto.EnumOS.SOLARIS : (var0.contains("sunos") ? Crypto.EnumOS.SOLARIS : (var0.contains("linux") ? Crypto.EnumOS.LINUX : (var0.contains("unix") ? Crypto.EnumOS.LINUX : Crypto.EnumOS.UNKNOWN)))));
    }

}
