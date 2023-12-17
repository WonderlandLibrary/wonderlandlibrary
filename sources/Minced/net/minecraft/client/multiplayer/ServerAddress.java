// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.multiplayer;

import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.net.IDN;

public class ServerAddress
{
    private final String ipAddress;
    private final int serverPort;
    
    private ServerAddress(final String address, final int port) {
        this.ipAddress = address;
        this.serverPort = port;
    }
    
    public String getIP() {
        try {
            return IDN.toASCII(this.ipAddress);
        }
        catch (IllegalArgumentException var2) {
            return "";
        }
    }
    
    public int getPort() {
        return this.serverPort;
    }
    
    public static ServerAddress fromString(final String addrString) {
        if (addrString == null) {
            return null;
        }
        String[] astring = addrString.split(":");
        if (addrString.startsWith("[")) {
            final int i = addrString.indexOf("]");
            if (i > 0) {
                final String s = addrString.substring(1, i);
                String s2 = addrString.substring(i + 1).trim();
                if (s2.startsWith(":") && !s2.isEmpty()) {
                    s2 = s2.substring(1);
                    astring = new String[] { s, s2 };
                }
                else {
                    astring = new String[] { s };
                }
            }
        }
        if (astring.length > 2) {
            astring = new String[] { addrString };
        }
        String s3 = astring[0];
        int j = (astring.length > 1) ? getInt(astring[1], 25565) : 25565;
        if (j == 25565) {
            final String[] astring2 = getServerAddress(s3);
            s3 = astring2[0];
            j = getInt(astring2[1], 25565);
        }
        return new ServerAddress(s3, j);
    }
    
    private static String[] getServerAddress(final String p_78863_0_) {
        try {
            final String s = "com.sun.jndi.dns.DnsContextFactory";
            Class.forName("com.sun.jndi.dns.DnsContextFactory");
            final Hashtable<String, String> hashtable = new Hashtable<String, String>();
            hashtable.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            hashtable.put("java.naming.provider.url", "dns:");
            hashtable.put("com.sun.jndi.dns.timeout.retries", "1");
            final DirContext dircontext = new InitialDirContext(hashtable);
            final Attributes attributes = dircontext.getAttributes("_minecraft._tcp." + p_78863_0_, new String[] { "SRV" });
            final String[] astring = attributes.get("srv").get().toString().split(" ", 4);
            return new String[] { astring[3], astring[2] };
        }
        catch (Throwable var6) {
            return new String[] { p_78863_0_, Integer.toString(25565) };
        }
    }
    
    private static int getInt(final String value, final int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        }
        catch (Exception var3) {
            return defaultValue;
        }
    }
}
