package vip.radium.player;

import net.minecraft.entity.player.EntityPlayer;
import vip.radium.RadiumClient;
import vip.radium.utils.FileUtils;
import vip.radium.utils.Wrapper;
import vip.radium.utils.handler.Manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class PlayerManager extends Manager<Player> {

    public static final File PLAYERS_FILE = new File(RadiumClient.NAME, "players.txt");

    public PlayerManager() {
        super(loadPlayers());
    }

    private static List<Player> loadPlayers() {
        final ArrayList<Player> players = new ArrayList<>();
        if (PLAYERS_FILE.exists()) {
            List<String> lines = FileUtils.getLines(PLAYERS_FILE);

            for (String line : lines) {
                if (line.contains(":") && line.length() > 3) {
                    String[] split = line.split(":");

                    if (split.length == 3)
                        players.add(new Player(split[0], split[1], Player.PlayerType.valueOf(split[3])));
                }
            }
        } else {
            //TODO: Setup saving
        }
        return players;
    }

    public boolean isFriend(String username) {
        for (Player player : getElements()) {
            if (player.getUsername().equalsIgnoreCase(username) && player.getType() == Player.PlayerType.FRIEND)
                return true;
        }
        return false;
    }

    public boolean isEnemy(String username) {
        for (Player player : getElements()) {
            if (player.getUsername().equalsIgnoreCase(username) && player.getType() == Player.PlayerType.ENEMY)
                return true;
        }
        return false;
    }

    public String add(String playerName, String alias , boolean enemy) {
        for (EntityPlayer player : Wrapper.getLoadedPlayers()) {
            final String username = player.getGameProfile().getName();
            if (username.equalsIgnoreCase(playerName)) {
                getElements().add(new Player(alias, playerName, enemy ? Player.PlayerType.ENEMY : Player.PlayerType.FRIEND));
                return username;
            }
        }
        return null;
    }

    public String remove(String username) {
        for (Player player : getElements()) {
            final String playerName = player.getUsername();
            if (playerName.equalsIgnoreCase(username)) {
                getElements().remove(player);
                return playerName;
            }
        }
        return null;
    }

    public boolean isFriend(EntityPlayer player) {
        return isFriend(player.getGameProfile().getName());
    }
    public boolean isEnemy(EntityPlayer player) {
        return isEnemy(player.getGameProfile().getName());
    }
}
