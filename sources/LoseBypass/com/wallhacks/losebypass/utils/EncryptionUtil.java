/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Properties;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {
    private static final String CHARSET = "UTF-8";
    private static final int DEFAULT_KEY_SIZE = 128;

    public static String encrypt(String content, String password) {
        return EncryptionUtil.encrypt(content, password, ALGORITHM.AES);
    }

    public static String encrypt(String content, String password, ALGORITHM algorithm) {
        return EncryptionUtil.encrypt(content, password, null, algorithm);
    }

    public static String encrypt(String content, String password, byte[] iv, ALGORITHM algorithm) {
        try {
            SecretKeySpec keySpec = EncryptionUtil.generateKey(password, algorithm);
            Cipher cipher = Cipher.getInstance(algorithm.toString());
            if (iv == null) {
                cipher.init(1, keySpec);
            } else {
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(1, (Key)keySpec, ivSpec);
            }
            byte[] result = cipher.doFinal(content.getBytes(CHARSET));
            return EncryptionUtil.byte2Hex(result);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
        catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }
        catch (BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encryptedContent, String password) {
        return EncryptionUtil.decrypt(encryptedContent, password, ALGORITHM.AES);
    }

    public static String decrypt(String encryptedContent, String password, ALGORITHM algorithm) {
        return EncryptionUtil.decrypt(encryptedContent, password, null, algorithm);
    }

    public static String decrypt(String encryptedContent, String password, byte[] iv, ALGORITHM algorithm) {
        try {
            SecretKeySpec keySpec = EncryptionUtil.generateKey(password, algorithm);
            Cipher cipher = Cipher.getInstance(algorithm.toString());
            if (iv == null) {
                cipher.init(2, keySpec);
            } else {
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(2, (Key)keySpec, ivSpec);
            }
            byte[] result = cipher.doFinal(EncryptionUtil.hex2byte(encryptedContent));
            return new String(result, CHARSET);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
        catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }
        catch (BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static SecretKeySpec generateKey(String password, ALGORITHM algorithm) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm.toString());
            SecureRandom secureRandom = EncryptionUtil.isAndroidPlatform() ? SecureRandom.getInstance("SHA1PRNG", "Crypto") : SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            int keySize = 128;
            switch (1.$SwitchMap$com$wallhacks$losebypass$utils$EncryptionUtil$ALGORITHM[algorithm.ordinal()]) {
                case 1: {
                    keySize = 56;
                    break;
                }
            }
            keyGenerator.init(keySize, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] encodedFormat = secretKey.getEncoded();
            return new SecretKeySpec(encodedFormat, algorithm.toString());
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sha1(String content) {
        return EncryptionUtil.digest(content, ALGORITHM.SHA1);
    }

    public static String sha1(File file) {
        return EncryptionUtil.digest(file, ALGORITHM.SHA1);
    }

    public static String md5(String content) {
        return EncryptionUtil.digest(content, ALGORITHM.MD5);
    }

    public static String md5(File file) {
        return EncryptionUtil.digest(file, ALGORITHM.MD5);
    }

    public static String digest(String content, ALGORITHM algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm.toString());
            messageDigest.update(content.getBytes(CHARSET));
            byte[] result = messageDigest.digest();
            return EncryptionUtil.byte2Hex(result);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String digest(File file, ALGORITHM algorithm) {
        FilterInputStream inputStream = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm.toString());
            inputStream = new DigestInputStream(new FileInputStream(file), messageDigest);
            byte[] buffer = new byte[131072];
            while (inputStream.read(buffer) > 0) {
            }
            messageDigest = ((DigestInputStream)inputStream).getMessageDigest();
            byte[] result = messageDigest.digest();
            String string = EncryptionUtil.byte2Hex(result);
            return string;
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        byte[] byArray = bytes;
        int n = byArray.length;
        int n2 = 0;
        while (n2 < n) {
            byte b = byArray[n2];
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            sb.append(hex);
            ++n2;
        }
        return sb.toString();
    }

    private static byte[] hex2byte(String hex) {
        if (hex == null) return null;
        if (hex.length() < 1) {
            return null;
        }
        int len = hex.length() / 2;
        byte[] bytes = new byte[len];
        int i = 0;
        while (i < len) {
            int high = Integer.parseInt(hex.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hex.substring(i * 2 + 1, i * 2 + 2), 16);
            bytes[i] = (byte)(high * 16 + low);
            ++i;
        }
        return bytes;
    }

    private static boolean isAndroidPlatform() {
        Properties properties = System.getProperties();
        if (properties.getProperty("java.vendor").contains("Android")) return true;
        if (properties.getProperty("java.vm.vendor").contains("Android")) return true;
        return false;
    }

    public static void main(String[] args) {
        String content = "Test content, \u6d4b\u8bd5\u6d4b\u8bd5";
        String password = "password!@#";
        System.out.println("content:" + content + "\n");
        String encrypted = EncryptionUtil.encrypt(content, password);
        System.out.println("AES encrypt:" + encrypted);
        System.out.println("AES decrypt:" + EncryptionUtil.decrypt(encrypted, password) + "\n");
        encrypted = EncryptionUtil.encrypt(content, password, ALGORITHM.DES);
        System.out.println("DES encrypt:" + encrypted);
        System.out.println("DES decrypt:" + EncryptionUtil.decrypt(encrypted, password, ALGORITHM.DES) + "\n");
        System.out.println("md5 hash:" + EncryptionUtil.md5(content));
        System.out.println("sha256 hash:" + EncryptionUtil.digest(content, ALGORITHM.SHA256));
    }

    public static enum ALGORITHM {
        AES("AES"),
        DES("DES"),
        MD2("MD2"),
        MD5("MD5"),
        SHA1("SHA-1"),
        SHA256("SHA-256"),
        SHA384("SHA-384"),
        SHA512("SHA-512");

        private final String text;

        private ALGORITHM(String text) {
            this.text = text;
        }

        public String toString() {
            return this.text;
        }
    }
}

