package net.minecraft.client.network;

import net.minecraft.client.Minecraft;

public static class LanServerDetector.LanServer {
    private String lanServerMotd;
    private String lanServerIpPort;
    private long timeLastSeen;

    public LanServerDetector.LanServer(String motd, String address) {
        this.lanServerMotd = motd;
        this.lanServerIpPort = address;
        this.timeLastSeen = Minecraft.getSystemTime();
    }

    public String getServerMotd() {
        return this.lanServerMotd;
    }

    public String getServerIpPort() {
        return this.lanServerIpPort;
    }

    public void updateLastSeen() {
        this.timeLastSeen = Minecraft.getSystemTime();
    }
}
