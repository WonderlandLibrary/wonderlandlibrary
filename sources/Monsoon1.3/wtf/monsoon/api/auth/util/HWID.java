package wtf.monsoon.api.auth.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HWID {

    public static String hwid;

    static {
        try {
            hwid = System.getenv("COMPUTERNAME") + getIP() + System.getProperty("user.name") + System.getProperty("os.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getIP() throws IOException {
        URL ip = new URL("http://checkip.amazonaws.com");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                ip.openStream()));
        return bufferedReader.readLine();
    }
}
