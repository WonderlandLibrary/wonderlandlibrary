package info.sigmaclient.management.users;

import info.sigmaclient.json.Json;
import info.sigmaclient.json.JsonObject;
import info.sigmaclient.management.users.impl.*;
import info.sigmaclient.util.security.Crypto;
import info.sigmaclient.util.security.HardwareUtil;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Arithmo on 8/11/2017 at 9:57 PM.
 */
public class UserManager {

    private User userStatus = new Upgraded("Premium", "nigga");

    public User getUser() {
        return userStatus;
    }

    private String initialHWID;
    private String username;
    private String versionString = "unknown";
    private boolean updateAvailable = false;
    private boolean updateNeeded = false;
    private String newVersionName = "";
    private ArrayList<String> newChangelog = new ArrayList<>();
    private int updateProgress = 0;

    private String firstHWID;
    private String secondHWID;
    private boolean loginNeeded = false;
    private boolean premium = true;
    private boolean trolled = false;
    private boolean finishedLoginSequence = false;
    private String userSerialNumber = HardwareUtil.getUserSerialNumber();
    private String session = UUID.randomUUID().toString().replaceAll("-", "");
    private String premsTimestamp = null;
    private Challenge lastChallenge = null;

    private byte[] hwid;

    public UserManager() {

    }

    public void computeOldHwid() {
        try {
            secondHWID = "second hwid";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void computeNewHwid() {
        switch (getOSType()) {
            case WINDOWS:
                try {
                    initialHWID = HardwareUtil.getWindowsSerialNumber();
                } catch (IOException ignored) {
                }
                break;
            case LINUX:
                initialHWID = "linux";//HardwareUtil.getLinuxSerialNumber();
                break;
            case OSX:
                initialHWID = "macosx";//HardwareUtil.getMacSerialNumber();
                break;
        }
        try {
            firstHWID = "first hwid";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void checkUserStatus(String username) {
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
        //statusCheck();
        loadPremiumsClasses();
    }*/

    public void loadVersionString() {
        versionString = "latest";
    }

   /* public void statusCheck() {
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
    }*/


    public static Crypto.EnumOS getOSType() {
        String var0 = System.getProperty("os.name").toLowerCase();
        return var0.contains("win") ? Crypto.EnumOS.WINDOWS : (var0.contains("mac") ? Crypto.EnumOS.OSX : (var0.contains("solaris") ? Crypto.EnumOS.SOLARIS : (var0.contains("sunos") ? Crypto.EnumOS.SOLARIS : (var0.contains("linux") ? Crypto.EnumOS.LINUX : (var0.contains("unix") ? Crypto.EnumOS.LINUX : Crypto.EnumOS.UNKNOWN)))));
    }

    public boolean loadPremiumsClasses() {
        return PremiumLoader.load();
    }

    public boolean checkVersion() {
        return false;
    }

    public void update() {

    }

    private static void copy(File source, File destination) {
        try (FileInputStream fis = new FileInputStream(source); FileOutputStream fos = new FileOutputStream(destination)) {
            byte[] buff = new byte[1024];
            int readedLen = 0;
            while ((readedLen = fis.read(buff)) > 0) {
                fos.write(buff, 0, readedLen);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLoginLink() {
        loginNeeded = false;
        username = "Premium user";
        premium = true;
        userStatus = new Upgraded(username, secondHWID);
        if (premium) {
            loadPremiumsClasses();
        }
        return null;
    }

    public Challenge getChallenge() {
        if (lastChallenge != null && lastChallenge.isValid()) {
            return lastChallenge;
        }

        return null;
    }

    public String authenticate(Challenge challenge, String username, String password) {
        premium = true;
        if (premium) {
            loadPremiumsClasses();
        }
        return null;
    }

    public String register(Challenge challenge, String username, String password, String mail) {
        premium = true;
        if (premium) {
            loadPremiumsClasses();
        }
        return "Unable to connect to server";
    }

    public String claimPremium(Challenge challenge, String key) {
        premium = true;
        if (premium) {
            loadPremiumsClasses();
        }
        return null;
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public boolean isUpdateNeeded() {
        return updateNeeded;
    }

    public ArrayList<String> getNewChangelog() {
        return newChangelog;
    }

    public String getNewVersionName() {
        return newVersionName;
    }

    public int getUpdateProgress() {
        return updateProgress;
    }

    public boolean isLoginNeeded() {
        return loginNeeded;
    }

    public boolean isFinishedLoginSequence() {
        return finishedLoginSequence;
    }

    public void setFinishedLoginSequence() {
        finishedLoginSequence = true;
    }

    public String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String getVersionString() {
        return versionString;
    }

    public String getSecondHWID() {
        return secondHWID;
    }

    public String getSession() {
        return session;
    }

    public boolean isTrolled() {
        return trolled;
    }

    public String getUserSerialNumber() {
        return userSerialNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPremsTimestamp() {
        return premsTimestamp;
    }

    public boolean isPremium() {
        return premium;
    }
}
