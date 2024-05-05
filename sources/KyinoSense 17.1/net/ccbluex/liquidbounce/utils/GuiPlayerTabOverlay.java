/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ComparisonChain
 *  com.google.common.collect.Ordering
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.world.WorldSettings$GameType
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.utils;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import java.util.Comparator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class GuiPlayerTabOverlay
extends Gui {
    public static Ordering<NetworkPlayerInfo> field_175252_a = Ordering.from((Comparator)new PlayerComparator());

    @SideOnly(value=Side.CLIENT)
    static class PlayerComparator
    implements Comparator<NetworkPlayerInfo> {
        private PlayerComparator() {
        }

        @Override
        public int compare(NetworkPlayerInfo llIIIlIIlIIIlII, NetworkPlayerInfo llIIIlIIlIIIIll) {
            ScorePlayerTeam llIIIlIIlIIIIlI = llIIIlIIlIIIlII.func_178850_i();
            ScorePlayerTeam llIIIlIIlIIIIIl = llIIIlIIlIIIIll.func_178850_i();
            return ComparisonChain.start().compareTrueFirst(llIIIlIIlIIIlII.func_178848_b() != WorldSettings.GameType.SPECTATOR, llIIIlIIlIIIIll.func_178848_b() != WorldSettings.GameType.SPECTATOR).compare((Comparable)((Object)(llIIIlIIlIIIIlI != null ? llIIIlIIlIIIIlI.func_96661_b() : "")), (Comparable)((Object)(llIIIlIIlIIIIIl != null ? llIIIlIIlIIIIIl.func_96661_b() : ""))).compare((Comparable)((Object)llIIIlIIlIIIlII.func_178845_a().getName()), (Comparable)((Object)llIIIlIIlIIIIll.func_178845_a().getName())).result();
        }
    }
}

