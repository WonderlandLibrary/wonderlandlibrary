package org.dreamcore.security.utils;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.zip.CRC32;

public class HashUtil {

    /*public static String hashInput(String hash, String input) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(hash);
            byte[] bytes = messageDigest.digest(input.getBytes());

            for (byte dig : bytes) {
                sb.append(Integer.toString((dig & 0xFF) + 256, 16).substring(1));
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }

    public static long getCrc32InFile(String file) {
        try {
            File f = new File(file);
            FileInputStream fis = new FileInputStream(f);
            CRC32 crc = new CRC32();
            byte[] buffer = new byte[65536];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                crc.update(buffer, 0, bytesRead);
            }
            return crc.getValue();
        } catch (Exception e) {
            System.exit(-1);
            Display.destroy();
            Minecraft.getInstance().shutdown();
            Runtime.getRuntime().halt(1);
        }
        return 0L;
    }
    */
}
