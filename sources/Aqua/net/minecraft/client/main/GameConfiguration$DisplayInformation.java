package net.minecraft.client.main;

public static class GameConfiguration.DisplayInformation {
    public final int width;
    public final int height;
    public final boolean fullscreen;
    public final boolean checkGlErrors;

    public GameConfiguration.DisplayInformation(int widthIn, int heightIn, boolean fullscreenIn, boolean checkGlErrorsIn) {
        this.width = widthIn;
        this.height = heightIn;
        this.fullscreen = fullscreenIn;
        this.checkGlErrors = checkGlErrorsIn;
    }
}
