package net.minecraft.client.main;

import java.io.File;

public static class GameConfiguration.FolderInformation {
    public final File mcDataDir;
    public final File resourcePacksDir;
    public final File assetsDir;
    public final String assetIndex;

    public GameConfiguration.FolderInformation(File mcDataDirIn, File resourcePacksDirIn, File assetsDirIn, String assetIndexIn) {
        this.mcDataDir = mcDataDirIn;
        this.resourcePacksDir = resourcePacksDirIn;
        this.assetsDir = assetsDirIn;
        this.assetIndex = assetIndexIn;
    }
}
