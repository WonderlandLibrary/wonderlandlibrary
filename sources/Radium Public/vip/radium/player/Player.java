package vip.radium.player;

public final class Player {

    private final String username;
    private String alias;
    private Enum<PlayerType> type;

    public Player(String username, Enum<PlayerType> type) {
        this(username, username, type);
    }

    public Player(String alias, String username, Enum<PlayerType> type) {
        this.alias = alias;
        this.username = username;
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUsername() {
        return username;
    }

    public Enum<PlayerType> getType() {
        return type;
    }

    public enum PlayerType {
        FRIEND, ENEMY
    }
}
