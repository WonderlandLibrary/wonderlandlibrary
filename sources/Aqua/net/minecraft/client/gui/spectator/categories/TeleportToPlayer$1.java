package net.minecraft.client.gui.spectator.categories;

import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import net.minecraft.client.network.NetworkPlayerInfo;

static final class TeleportToPlayer.1
implements Comparator<NetworkPlayerInfo> {
    TeleportToPlayer.1() {
    }

    public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
        return ComparisonChain.start().compare((Comparable)p_compare_1_.getGameProfile().getId(), (Comparable)p_compare_2_.getGameProfile().getId()).result();
    }
}
