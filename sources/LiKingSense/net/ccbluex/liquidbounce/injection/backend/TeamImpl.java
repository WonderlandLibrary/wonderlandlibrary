/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Team
 *  net.minecraft.util.text.TextFormatting
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam;
import net.ccbluex.liquidbounce.api.minecraft.util.WEnumChatFormatting;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt$WhenMappings;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0016J\u0010\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u0001H\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/TeamImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/ITeam;", "wrapped", "Lnet/minecraft/scoreboard/Team;", "(Lnet/minecraft/scoreboard/Team;)V", "chatFormat", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WEnumChatFormatting;", "getChatFormat", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WEnumChatFormatting;", "getWrapped", "()Lnet/minecraft/scoreboard/Team;", "equals", "", "other", "", "formatString", "", "name", "isSameTeam", "team", "LiKingSense"})
public final class TeamImpl
implements ITeam {
    @NotNull
    private final Team wrapped;

    @Override
    @NotNull
    public WEnumChatFormatting getChatFormat() {
        WEnumChatFormatting wEnumChatFormatting;
        Team team = this.wrapped;
        if (team == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.scoreboard.ScorePlayerTeam");
        }
        TextFormatting textFormatting = ((ScorePlayerTeam)team).func_178775_l();
        Intrinsics.checkExpressionValueIsNotNull((Object)textFormatting, (String)"(wrapped as ScorePlayerTeam).color");
        TextFormatting $this$wrap$iv = textFormatting;
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
    @NotNull
    public String formatString(@NotNull String name) {
        Intrinsics.checkParameterIsNotNull((Object)name, (String)"name");
        String string = this.wrapped.func_142053_d(name);
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.formatString(name)");
        return string;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean isSameTeam(@NotNull ITeam team) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)team, (String)"team");
        ITeam iTeam = team;
        Team team2 = this.wrapped;
        boolean $i$f$unwrap = false;
        Team team3 = ((TeamImpl)$this$unwrap$iv).getWrapped();
        return team2.func_142054_a(team3);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof TeamImpl && Intrinsics.areEqual((Object)((TeamImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final Team getWrapped() {
        return this.wrapped;
    }

    public TeamImpl(@NotNull Team wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

