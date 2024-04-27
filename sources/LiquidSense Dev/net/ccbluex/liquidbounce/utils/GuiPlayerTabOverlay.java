package net.ccbluex.liquidbounce.utils;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Comparator;

@SideOnly(Side.CLIENT)
public class GuiPlayerTabOverlay extends Gui {
    /**
     * @Dev: LiquidSlowly
     */
    public static  Ordering<NetworkPlayerInfo> field_175252_a = Ordering.from(new GuiPlayerTabOverlay.PlayerComparator());


    @SideOnly(Side.CLIENT)
    static class PlayerComparator implements Comparator<NetworkPlayerInfo> {
        private PlayerComparator() {
        }

        public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
            ScorePlayerTeam lvt_3_1_ = p_compare_1_.getPlayerTeam();
            ScorePlayerTeam lvt_4_1_ = p_compare_2_.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare(lvt_3_1_ != null ? lvt_3_1_.getRegisteredName() : "", lvt_4_1_ != null ? lvt_4_1_.getRegisteredName() : "").compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
        }
    }

}
