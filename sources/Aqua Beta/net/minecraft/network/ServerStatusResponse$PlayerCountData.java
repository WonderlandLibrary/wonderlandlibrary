package net.minecraft.network;

import com.mojang.authlib.GameProfile;

public static class ServerStatusResponse.PlayerCountData {
    private final int maxPlayers;
    private final int onlinePlayerCount;
    private GameProfile[] players;

    public ServerStatusResponse.PlayerCountData(int maxOnlinePlayers, int onlinePlayers) {
        this.maxPlayers = maxOnlinePlayers;
        this.onlinePlayerCount = onlinePlayers;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public int getOnlinePlayerCount() {
        return this.onlinePlayerCount;
    }

    public GameProfile[] getPlayers() {
        return this.players;
    }

    public void setPlayers(GameProfile[] playersIn) {
        this.players = playersIn;
    }
}
