// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.auth.util;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class HWID
{
    public static String hwid;
    
    public static String getIP() throws IOException {
        final URL ip = new URL("http://checkip.amazonaws.com");
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ip.openStream()));
        return bufferedReader.readLine();
    }
    
    static {
        HWID.hwid = System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getProperty("os.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
    }
}
