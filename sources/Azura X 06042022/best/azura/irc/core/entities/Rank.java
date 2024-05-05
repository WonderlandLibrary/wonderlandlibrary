package best.azura.irc.core.entities;

import com.mojang.realmsclient.gui.ChatFormatting;

public class Rank {

    // Initialize Rank ID.
    int rank_Id;

    // Initialize Name, Kürzel und Rank Prefix.
    String name, kurzel, prefix;

    /**
     * Constructor for Rank Data.
     *
     * @param rank_Id          Database Rank ID.
     * @param name             Rank Name.
     * @param kurzel           Rank Kurzel.
     * @param prefix           Rank Prefix.
     */
    public Rank(int rank_Id, String name, String kurzel, String prefix) {
        this.rank_Id = rank_Id;
        this.name = name;
        this.kurzel = kurzel;
        this.prefix = prefix;
    }

    /**
     * Retrieve the wanted Data.
     *
     * @return the wanted Data.
     */
    public int getRank_Id() {
        return rank_Id;
    }

    /**
     * Retrieve the wanted Data.
     *
     * @return the wanted Data.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve the wanted Data.
     *
     * @return the wanted Data.
     */
    public String getKurzel() {
        return kurzel;
    }

    /**
     * Retrieve the wanted Data.
     *
     * @return the wanted Data.
     */
    public String getPrefix() {
        if (prefix == null) {
            return ChatFormatting.PREFIX_CODE + "aUser";
        }

        prefix = prefix.replace("\\u0026", ChatFormatting.PREFIX_CODE + "").replace('&', ChatFormatting.PREFIX_CODE);

        return prefix;
    }
}
