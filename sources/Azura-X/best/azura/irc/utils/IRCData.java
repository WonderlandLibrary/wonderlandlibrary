package best.azura.irc.utils;

import best.azura.client.util.crypt.AESUtil;
import best.azura.client.util.crypt.RSAUtil;
import best.azura.irc.core.entities.*;
import best.azura.irc.core.channels.ChannelManager;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyPair;
import java.util.Base64;
import java.util.HashMap;

/**
 * Class to save IRC-SQL Data for usage.
 */
public class IRCData {

    // Create new Hashmap with Rank ID as key and IRCRank class as value.
    public HashMap<Integer, Rank> ircRankHashMap = new HashMap<>();

    // Instance of the Channel-Manager.
    public ChannelManager channelManager = new ChannelManager();

    public KeyPair keyPair;

    public Key AESKey;

    public IRCData() {
        try {
            if (keyPair == null) keyPair = RSAUtil.generateKeyPair();
        } catch (Exception ignored) {}
    }

    /**
     * Add an IRC-Rank to the Data list.
     * @param rankId the ID.
     * @param rank the Rank.
     */
    public void addIRCRank(int rankId, Rank rank) {
        if (!ircRankHashMap.containsKey(rankId)) {
            ircRankHashMap.put(rankId, rank);
        }
    }

    /**
     * Retrieve the Rank by his ID.
     * @param rankId Database internal Rank ID.
     * @return the Rank.
     */
    public Rank getRankById(int rankId) {

        // Check if there is a Rank.
        if (ircRankHashMap.containsKey(rankId)) {
            // Get the IRC Rank.
            return ircRankHashMap.get(rankId);
        }

        // Return null if not in Client.
        return null;
    }

    public void setAESKey(String key) {
        try {
            AESKey = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
        } catch (Exception ignored) {}
    }

    public Key getAESKey() {
        return AESKey;
    }
}
