package com.thealtening.api.response;

public class AccountDetails {
    private int hypixelLevel;
    private String hypixelRank;
    private int mineplexLevel;
    private String mineplexRank;
    private boolean labymodCape;
    private boolean fiveZigCape;

    public int getHypixelLevel() {
        return this.hypixelLevel;
    }

    public String getHypixelRank() {
        return this.hypixelRank;
    }

    public int getMineplexLevel() {
        return this.mineplexLevel;
    }

    public String getMineplexRank() {
        return this.mineplexRank;
    }

    public boolean hasLabyModCape() {
        return this.labymodCape;
    }

    public boolean hasFiveZigCape() {
        return this.fiveZigCape;
    }

    public String toString() {
        return String.format((String)"AccountDetails[%s:%s:%s:%s:%s:%s]", (Object[])new Object[]{this.hypixelLevel, this.hypixelRank, this.mineplexLevel, this.mineplexRank, this.labymodCape, this.fiveZigCape});
    }
}
