package com.thealtening.api.data.info;

import com.google.gson.annotations.SerializedName;

public class AccountInfo {

    @SerializedName("hypixel.lvl")
    private int hypixelLevel;
    @SerializedName("hypixel.rank")
    private String hypixelRank;

    @SerializedName("mineplex.lvl")
    private int mineplexLevel;

    @SerializedName("mineplex.rank")
    private String mineplexRank;

    @SerializedName("labymod.cape")
    private boolean labymodCape;

    @SerializedName("5zig.cape")
    private boolean fiveZigCape;

    public int getHypixelLevel() {
        return hypixelLevel;
    }

    public String getHypixelRank() {
        return hypixelRank;
    }

    public int getMineplexLevel() {
        return mineplexLevel;
    }

    public String getMineplexRank() {
        return mineplexRank;
    }

    public boolean hasLabyModCape() {
        return labymodCape;
    }

    public boolean hasFiveZigCape() {
        return fiveZigCape;
    }

    @Override
    public String toString() {
        return String.format("AccountInfo[%s:%s:%s:%s:%s:%s]", hypixelLevel, hypixelRank, mineplexLevel, mineplexRank, labymodCape, fiveZigCape);
    }
}
