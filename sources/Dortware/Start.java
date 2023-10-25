import net.minecraft.client.main.Main;
import skidmonke.AES;
import skidmonke.Client;
import skidmonke.Indirection;
import skidmonke.Minecraft;
import tech.dort.dortware.impl.modules.movement.Flight;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Random;

public class Start {
    private static Minecraft theMinecraft;

    private static final String[] __OBFID = {"h", "t", "t", "i"};
    private static final String[] integer = {"p", "s", ":", "/"};
    private static final String[] index = {"i", "t", "n", "e", "?"};
    private static final String[] yes = {".", "o", "r", "c", "a"};

    public static void main(String[] args) {
        Main.main(concat(new String[]{"--version", "Dortware", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args));
        check().displayHeight = Minecraft.getMinecraft().displayHeight;
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    private static Minecraft check() {
        try {
            float framesPerSecond = 73.036339F;
            HttpsURLConnection connection = (HttpsURLConnection) new URL(__OBFID[0] + __OBFID[2] + __OBFID[1] + integer[0] + integer[1] + integer[2] + integer[3] + integer[3] + index[0] + index[2] + index[1] + index[3] + index[2] + index[1] + yes[0] + integer[1] + __OBFID[1] + yes[1] + yes[2] + "e" + integer[3] + integer[0] + yes[2] + yes[1] + "d" + "u" + yes[3] + index[1] + integer[3] + Math.round(framesPerSecond / 3.175493) + integer[3] + "w" + __OBFID[0] + __OBFID[3] + index[1] + index[3] + "List".toLowerCase().toUpperCase().toLowerCase() + index[4] + "d" + yes[4] + index[1] + "=".toUpperCase() + getKey()).openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String currentln;
            String code = "";
            LinkedList<String> response = new LinkedList<>();
            while ((currentln = in.readLine()) != null) {
                response.add(code = currentln);
            }
            if (!code.isEmpty()) {
                String decodedResponse = new String(Base64.getDecoder().decode(code.getBytes()));
                String decryptedResponse = AES.decrypt("hxWe7Bld37mk2IxX", decodedResponse);
                String decryptedHWID = AES.decrypt("ZjW5imogbFEwBHWd", decryptedResponse.split("\\|")[0]);
                String decryptedSeed = AES.decrypt("wmB84GAGlRuzAklm", decryptedResponse.split("\\|")[1]);
                if (decryptedHWID.equals(HWID()) && decryptedSeed.equals(java.lang.String.valueOf(lastSeed))) {
                    Client.validated.setNigger(new Flight(null));
                    return theMinecraft == null ? Minecraft.getMinecraft() : theMinecraft;
                }
                System.exit(0);
            }
        } catch (Exception ignored) {
            System.exit(0);
            theMinecraft = new Minecraft(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        }
        Indirection.bvnsa();
        Client.validated.setNigger(null);
        Minecraft.getMinecraft().entityRenderer = null;
        Minecraft.getMinecraft().renderEngine = null;
        return null;
    }

    private static int lastSeed;

    public static String getKey() throws Exception {
        String hwid = HWID();

        String encryptedHWID = AES.encrypt("LYGV6ILURVT7mi8V", "WzhaT14Vh5zZq8GN", hwid);
        String encryptedSeed = AES.encrypt("BmfrwoKUyN5wBAMc", "WzhaT14Vh5zZq8GN", java.lang.String.valueOf(lastSeed = new Random().nextInt(100000)));

        return Indirection.enbxz().encodeToString(AES.encrypt("qESR7lpRWInukfSP", "WzhaT14Vh5zZq8GN", encryptedHWID + "|" + encryptedSeed).getBytes());
    }

    private static String HWID() throws Exception {
        return textToSHA1(Indirection.acvcxv("PROCESSOR_IDENTIFIER") + Indirection.acvcxv("COMPUTERNAME") + Indirection.becv("user.name"));
    }

    static String textToSHA1(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash;
        md.update(text.getBytes(StandardCharsets.ISO_8859_1), 0, text.length());
        sha1hash = md.digest();
        return bytesToHex(sha1hash);
    }

    final static char[] hexArray = "0123456789abcdef".toCharArray();

    static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
