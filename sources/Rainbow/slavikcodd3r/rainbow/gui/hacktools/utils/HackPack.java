package slavikcodd3r.rainbow.gui.hacktools.utils;

import java.util.Random;

public class HackPack
{
    private static String fakeIP;
    private static String fakeUUID;
    private static String currentIPPort;
    public Random rand;
    public Random rand2;
    
    static {
        HackPack.fakeIP = "";
        HackPack.fakeUUID = "";
        HackPack.currentIPPort = "";
    }
    
    public HackPack() {
        this.rand = new Random();
        this.rand2 = new Random();
    }
    
    public static String getFakeIp() {
        return HackPack.fakeIP;
    }
    
    public static String getFakeUUID() {
        return HackPack.fakeUUID;
    }
    
    public static String getCurrentIPPort() {
        return HackPack.currentIPPort;
    }
    
    public static void setFakeIP(final String fIP) {
        HackPack.fakeIP = fIP;
    }
    
    public static void setFakeUUID(final String fUUID) {
        HackPack.fakeUUID = fUUID;
    }
    
    public static void setCurrentIPPort(final String cIPPort) {
        HackPack.currentIPPort = cIPPort;
    }
}
