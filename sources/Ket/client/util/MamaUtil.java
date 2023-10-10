package client.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UtilityClass
public class MamaUtil {

    @Getter
    private String username = "";

    private String get() throws NoSuchAlgorithmException {
        final String s = System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("NUMBER_OF_PROCESSORS") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_REVISION");
        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(s.getBytes());
        final StringBuilder sb = new StringBuilder();
        for (byte b : md.digest()) {
            final String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }

    private String[] getWhitelisted() throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(new URL("https://rufacisten.space/api/ketclient/userbase").openStream()));
        final StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) sb.append(s);
        br.close();
        return sb.toString().split(",");
    }

    private String getIP() throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(new URL("https://checkip.amazonaws.com").openStream()));
        final StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) sb.append(s);
        br.close();
        return sb.toString();
    }

    public boolean isWhitelisted() throws IOException, NoSuchAlgorithmException {
        final String s = get();
        boolean b = false;
        for (String s1 : getWhitelisted())
            if (s1.contains(":")) {
                if (s1.split(":")[0].equals(s)) {
                    b = true;
                    username = s1.split(":")[1];
                    break;
                }
            } else if (s1.equals(s)) {
                b = true;
                break;
            }
        return b;
    }
}
