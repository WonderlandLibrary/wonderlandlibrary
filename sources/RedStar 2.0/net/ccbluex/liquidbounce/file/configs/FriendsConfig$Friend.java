package net.ccbluex.liquidbounce.file.configs;

public class FriendsConfig$Friend {
    private final String playerName;
    private final String alias;

    FriendsConfig$Friend(String playerName, String alias) {
        this.playerName = playerName;
        this.alias = alias;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getAlias() {
        return this.alias;
    }
}
