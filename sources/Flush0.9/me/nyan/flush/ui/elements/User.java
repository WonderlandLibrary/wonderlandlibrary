package me.nyan.flush.ui.elements;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private final String username;
    private final String hwid;
    private final int uid;

    public User(String username, String hwid, int uid) {
        this.username = username;
        this.hwid = hwid;
        this.uid = uid;

        try {
            byte[] hash = MessageDigest.getInstance("md5")
                    .digest((System.getenv("COMPUTERNAME")
                            + System.getProperty("user.name")
                            + System.getenv("PROCESSOR_IDENTIFIER")
                            + System.getenv("PROCESSOR_LEVEL"))
                            .getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    builder.append('0');
                }
                builder.append(hex);
            }

            if (!builder.toString().equals(hwid)) {
                while (true) ;
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
    }

    public String getUsername() {
        return username;
    }

    public String getHwid() {
        return hwid;
    }

    public int getUid() {
        return uid;
    }
}
