/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.TypeCastException
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Team
 *  net.minecraft.util.text.TextFormatting
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.NoWhenBranchMatchedException;
import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam;
import net.ccbluex.liquidbounce.api.minecraft.util.WEnumChatFormatting;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt$WhenMappings;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.Nullable;

public final class TeamImpl
implements ITeam {
    private final Team wrapped;

    @Override
    public WEnumChatFormatting getChatFormat() {
        WEnumChatFormatting wEnumChatFormatting;
        Team team = this.wrapped;
        if (team == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.scoreboard.ScorePlayerTeam");
        }
        TextFormatting $this$wrap$iv = ((ScorePlayerTeam)team).func_178775_l();
        boolean $i$f$wrap = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[$this$wrap$iv.ordinal()]) {
            case 1: {
                wEnumChatFormatting = WEnumChatFormatting.BLACK;
                break;
            }
            case 2: {
                wEnumChatFormatting = WEnumChatFormatting.DARK_BLUE;
                break;
            }
            case 3: {
                wEnumChatFormatting = WEnumChatFormatting.DARK_GREEN;
                break;
            }
            case 4: {
                wEnumChatFormatting = WEnumChatFormatting.DARK_AQUA;
                break;
            }
            case 5: {
                wEnumChatFormatting = WEnumChatFormatting.DARK_RED;
                break;
            }
            case 6: {
                wEnumChatFormatting = WEnumChatFormatting.DARK_PURPLE;
                break;
            }
            case 7: {
                wEnumChatFormatting = WEnumChatFormatting.GOLD;
                break;
            }
            case 8: {
                wEnumChatFormatting = WEnumChatFormatting.GRAY;
                break;
            }
            case 9: {
                wEnumChatFormatting = WEnumChatFormatting.DARK_GRAY;
                break;
            }
            case 10: {
                wEnumChatFormatting = WEnumChatFormatting.BLUE;
                break;
            }
            case 11: {
                wEnumChatFormatting = WEnumChatFormatting.GREEN;
                break;
            }
            case 12: {
                wEnumChatFormatting = WEnumChatFormatting.AQUA;
                break;
            }
            case 13: {
                wEnumChatFormatting = WEnumChatFormatting.RED;
                break;
            }
            case 14: {
                wEnumChatFormatting = WEnumChatFormatting.LIGHT_PURPLE;
                break;
            }
            case 15: {
                wEnumChatFormatting = WEnumChatFormatting.YELLOW;
                break;
            }
            case 16: {
                wEnumChatFormatting = WEnumChatFormatting.WHITE;
                break;
            }
            case 17: {
                wEnumChatFormatting = WEnumChatFormatting.OBFUSCATED;
                break;
            }
            case 18: {
                wEnumChatFormatting = WEnumChatFormatting.BOLD;
                break;
            }
            case 19: {
                wEnumChatFormatting = WEnumChatFormatting.STRIKETHROUGH;
                break;
            }
            case 20: {
                wEnumChatFormatting = WEnumChatFormatting.UNDERLINE;
                break;
            }
            case 21: {
                wEnumChatFormatting = WEnumChatFormatting.ITALIC;
                break;
            }
            case 22: {
                wEnumChatFormatting = WEnumChatFormatting.RESET;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return wEnumChatFormatting;
    }

    @Override
    public String formatString(String name) {
        return this.wrapped.func_142053_d(name);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean isSameTeam(ITeam team) {
        void $this$unwrap$iv;
        ITeam iTeam = team;
        Team team2 = this.wrapped;
        boolean $i$f$unwrap = false;
        Team team3 = ((TeamImpl)$this$unwrap$iv).getWrapped();
        return team2.func_142054_a(team3);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof TeamImpl && ((TeamImpl)other).wrapped.equals(this.wrapped);
    }

    public final Team getWrapped() {
        return this.wrapped;
    }

    public TeamImpl(Team wrapped) {
        this.wrapped = wrapped;
    }
}

