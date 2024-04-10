//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\1\Desktop\Minecraft-Deobfuscator3000-1.2.3\config"!

package info.sigmaclient.sigma.utils.render.rendermanagers;

import Decoder.BASE64Decoder;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Objects;

public class DecodeTexture {
    DynamicTexture decode;
//    byte[] bytes;
    public ByteArrayInputStream inputStream;

    public Key getKey(String keySeed) {
        if (keySeed == null || keySeed.trim().length() == 0) {
            keySeed = key;
        }
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(keySeed.getBytes());
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(secureRandom);
            return generator.generateKey();
        } catch (Exception e) {
            Minecraft.getInstance().shutdown();
        }
        return null;
    }
    public byte[] decrypt(String cipherText) {
        Key secretKey = getKey(key);
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] c = decoder.decodeBuffer(cipherText);
            return cipher.doFinal(c);

        } catch (Exception e) {
            Minecraft.getInstance().shutdown();
        }
        return null;
    }

    public static byte[] readBytes3(InputStream in) throws IOException {
        BufferedInputStream bufin = new BufferedInputStream(in);
        int buffSize = in.available();
        ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);

        // System.out.println("Available bytes:" + in.available());

        byte[] temp = new byte[buffSize];
        int size = 0;
        while ((size = bufin.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        bufin.close();
        in.close();
        byte[] content = out.toByteArray();
        out.flush();
        out.close();
        return content;
    }

    public String read(final String file) {
        StringBuilder out = new StringBuilder();
        try {
            try (InputStream fis = this.getClass().getResourceAsStream("/" + file)) {
                try (InputStreamReader isr = new InputStreamReader(fis)) {
                    try (BufferedReader br = new BufferedReader(isr)) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            out.append(line).append("\n");
                        }
                    }
                }
                fis.close();
                return out.toString();
            }
        } catch (Exception e) {
           throw new RuntimeException("Texture is not loading!");
        }
//        return out.toString();
    }
    public byte[] get(String file){
        InputStream in = this.getClass().getResourceAsStream("/" + file);
            try {
                return readBytes3(in);
            } catch (IOException e) {
                Minecraft.getInstance().shutdown();
            }
            return null;
    }
    public static String imgType(InputStream inputStream) throws IOException {
        byte[] fileHeader = new byte[4];
        int read = inputStream.read(fileHeader, 0, fileHeader.length);

        String header = bytes2Hex(fileHeader);

        if (header.contains("FFD8FF")) {
            return "jpg";
        } else if (header.contains("89504E47")) {
            return "png";
        } else if (header.contains("47494638")) {
            return "gif";
        } else if (header.contains("424D")) {
            return "bmp";
        } else if (header.contains("52494646")) {
            return "webp";
        } else if (header.contains("49492A00")) {
            return "tif";
        } else {
            return "unknown" + header;
        }

    }

    public static String bytes2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xff);
            sb.append(hex.length() == 2 ? hex : ("0"+hex));
        }
        return sb.toString();
    }
    public BufferedImage getUrlImage(byte[] url) {
        InputStream buffin = new ByteArrayInputStream(url,0, url.length);
        BufferedImage img = null;
        try {
            img = ImageIO.read(buffin);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
    String key;
    public DecodeTexture(String file, String k) {
        key = k;
        byte[] data;
        data = decrypt(read(file));
//        this.bytes = data;
        inputStream = new ByteArrayInputStream(data);
        try {
            BufferedImage ol = getUrlImage(data);
            decode = new DynamicTexture(ol);
        } catch (Exception e) {
            Minecraft.getInstance().shutdown();
        }
    }
    public void bind(){
        RenderSystem.bindTexture(decode.getGlTextureId());
    }
}
