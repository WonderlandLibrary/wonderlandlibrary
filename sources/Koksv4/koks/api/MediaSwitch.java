package koks.api;

import lombok.AllArgsConstructor;

public class MediaSwitch {

    public Type currentType = Type.DISCORD, lastType;

    public void switchMedia() {
        lastType = currentType;
        currentType = Type.values()[currentType.ordinal() + 1 < Type.values().length ? currentType.ordinal() + 1 : 0];
    }

    @AllArgsConstructor
    public enum Type {
        DISCORD("discord", "https://discord.gg/NHyMHATaQv"), GUILDED("guilded", "https://www.guilded.gg/Bergbauprodukt"), TWITTER("twitter", "https://twitter.com/koksiges"), YOUTUBE("youtube", "https://www.youtube.com/channel/UCCwaOFolHK1wSMgP_y_4bWw");

        public String name, url;
    }
}
