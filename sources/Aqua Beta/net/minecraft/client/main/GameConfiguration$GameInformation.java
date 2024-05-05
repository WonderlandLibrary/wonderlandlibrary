package net.minecraft.client.main;

public static class GameConfiguration.GameInformation {
    public final boolean isDemo;
    public final String version;

    public GameConfiguration.GameInformation(boolean isDemoIn, String versionIn) {
        this.isDemo = isDemoIn;
        this.version = versionIn;
    }
}
