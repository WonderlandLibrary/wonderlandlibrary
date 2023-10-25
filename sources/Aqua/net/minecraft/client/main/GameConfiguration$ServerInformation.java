package net.minecraft.client.main;

public static class GameConfiguration.ServerInformation {
    public final String serverName;
    public final int serverPort;

    public GameConfiguration.ServerInformation(String serverNameIn, int serverPortIn) {
        this.serverName = serverNameIn;
        this.serverPort = serverPortIn;
    }
}
