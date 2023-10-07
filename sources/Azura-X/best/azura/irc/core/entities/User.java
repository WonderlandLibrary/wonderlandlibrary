package best.azura.irc.core.entities;

import best.azura.client.api.module.Module;
import best.azura.client.impl.Client;
import best.azura.client.impl.module.impl.other.StreamerMode;
import best.azura.client.impl.value.BooleanValue;
import com.mojang.realmsclient.gui.ChatFormatting;

/**
 * This class is used to save Data about an IRC User with
 * every needed Data about him.
 */
public class User {

    // The IRC Name of the User.
    String username;

    // The In-Game Name of the User.
    String minecraftName;

    // The IRC-Rank of the User.
    Rank rank;

    /**
     * Constructor for the IRC-User.
     * @param username the IRC-Username.
     * @param minecraftName the Minecraft-Username.
     * @param rank the IRC-Rank.
     */
    public User(String username, String minecraftName, Rank rank) {
        this.username = username;
        this.minecraftName = minecraftName;
        this.rank = rank;
    }

    /**
     * Retrieve the Minecraft In-Game Name of the IRC-User.
     * @return In-Game Name.
     */
    public String getMinecraftName() {
        return minecraftName;
    }

    /**
     * Change the Minecraft In-Game Name of the IRC-User.
     * @param minecraftName the new Minecraft Name.
     */
    public void setMinecraftName(String minecraftName) {
        this.minecraftName = minecraftName;
    }

    /**
     * Retrieve the Username of the IRC-User.
     * @return the Username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieve the IRC-Rank of the IRC-User.
     * @return the IRC-Rank.
     */
    public Rank getIrcRank() {
        return rank;
    }

    /**
     * Get a formatted Version.
     * @return formatted.
     */
    public String getFormattedIGN() {
        Module streamerMode = Client.INSTANCE.getModuleManager().getModule(StreamerMode.class);
        return ChatFormatting.PREFIX_CODE + "7(" + (streamerMode.isEnabled() && ((BooleanValue)Client.INSTANCE.getValueManager().getValue(streamerMode, "Hide Username")).getObject() ? getUsername() : getMinecraftName()) + ChatFormatting.PREFIX_CODE + "7)";
    }
}
