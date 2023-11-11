package me.nyan.flush;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;
import java.util.zip.CRC32;

public class FuncraftGenerator {
    public static final int HWID_SIZE = 24;
    public static final int LAUNCHER_HASH_SIZE = 16;
    public static final int ALPHABET_SIZE = 26;

    private static final Random RANDOM = new Random();

    public static String createOfflineIP(String username) {
        byte[] hwid = new byte[HWID_SIZE];
        byte[] launcherHash = new byte[LAUNCHER_HASH_SIZE];
        RANDOM.nextBytes(hwid);
        RANDOM.nextBytes(launcherHash);

        return createOfflineIP(username, hwid, launcherHash);
    }

    public static String createOfflineIP(String username, byte[] hwid, byte[] launcherHash) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(stream);

        try {
            data.write(launcherHash, 0, LAUNCHER_HASH_SIZE);
            data.writeInt((int) (System.currentTimeMillis() / 1000L / 60L));

            byte[] nameBytes = username.getBytes(StandardCharsets.US_ASCII);
            data.writeByte(nameBytes.length);
            for (byte b : nameBytes) {
                data.writeByte(b);
            }
            data.write(hwid, 0, HWID_SIZE);

            byte[] byteArray = stream.toByteArray();
            CRC32 crc32 = new CRC32();
            crc32.update(byteArray, 0, byteArray.length);
            data.writeLong(crc32.getValue());
            crc32.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = Base64.getEncoder().encodeToString(stream.toByteArray())
                .replaceAll("\\+", "-")
                .replaceAll("/", "_")
                .replaceAll("=", "");

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i += 60) {
            if (i != 0) {
                builder.append(".");
            }
            builder.append(Character.toChars('A' + RANDOM.nextInt(ALPHABET_SIZE)));
            builder.append(s, i, Math.min(i + 60, s.length()));
            builder.append(Character.toChars('A' + RANDOM.nextInt(ALPHABET_SIZE)));
        }
        return builder + ".offline.funcraft.net";
    }
}
