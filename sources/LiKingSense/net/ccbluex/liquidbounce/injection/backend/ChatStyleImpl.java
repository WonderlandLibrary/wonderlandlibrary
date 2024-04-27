/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.util.text.Style
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraft.util.text.event.ClickEvent
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.event.IClickEvent;
import net.ccbluex.liquidbounce.api.minecraft.util.IChatStyle;
import net.ccbluex.liquidbounce.api.minecraft.util.WEnumChatFormatting;
import net.ccbluex.liquidbounce.injection.backend.ClickEventImpl;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt$WhenMappings;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u001a\u001a\u00020\u00122\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0096\u0002R(\u0010\u0007\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u00068V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR(\u0010\r\u001a\u0004\u0018\u00010\f2\b\u0010\u0005\u001a\u0004\u0018\u00010\f8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R$\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0005\u001a\u00020\u00128V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019\u00a8\u0006\u001d"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/ChatStyleImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IChatStyle;", "wrapped", "Lnet/minecraft/util/text/Style;", "(Lnet/minecraft/util/text/Style;)V", "value", "Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent;", "chatClickEvent", "getChatClickEvent", "()Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent;", "setChatClickEvent", "(Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent;)V", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WEnumChatFormatting;", "color", "getColor", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WEnumChatFormatting;", "setColor", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/WEnumChatFormatting;)V", "", "underlined", "getUnderlined", "()Z", "setUnderlined", "(Z)V", "getWrapped", "()Lnet/minecraft/util/text/Style;", "equals", "other", "", "LiKingSense"})
public final class ChatStyleImpl
implements IChatStyle {
    @NotNull
    private final Style wrapped;

    @Override
    @Nullable
    public IClickEvent getChatClickEvent() {
        IClickEvent iClickEvent;
        ClickEvent clickEvent = this.wrapped.func_150235_h();
        if (clickEvent != null) {
            ClickEvent $this$wrap$iv = clickEvent;
            boolean $i$f$wrap = false;
            iClickEvent = new ClickEventImpl($this$wrap$iv);
        } else {
            iClickEvent = null;
        }
        return iClickEvent;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void setChatClickEvent(@Nullable IClickEvent value) {
        ClickEvent clickEvent;
        Style style = this.wrapped;
        IClickEvent iClickEvent = value;
        if (iClickEvent != null) {
            void $this$unwrap$iv;
            IClickEvent iClickEvent2 = iClickEvent;
            Style style2 = style;
            boolean $i$f$unwrap = false;
            void v2 = $this$unwrap$iv;
            if (v2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.ClickEventImpl");
            }
            ClickEvent clickEvent2 = ((ClickEventImpl)v2).getWrapped();
            style = style2;
            clickEvent = clickEvent2;
        } else {
            clickEvent = null;
        }
        style.func_150241_a(clickEvent);
    }

    @Override
    public boolean getUnderlined() {
        return this.wrapped.func_150234_e();
    }

    @Override
    public void setUnderlined(boolean value) {
        this.wrapped.func_150228_d(Boolean.valueOf(value));
    }

    @Override
    @Nullable
    public WEnumChatFormatting getColor() {
        WEnumChatFormatting wEnumChatFormatting;
        block25: {
            block24: {
                TextFormatting textFormatting = this.wrapped.func_150215_a();
                if (textFormatting == null) break block24;
                TextFormatting $this$wrap$iv = textFormatting;
                boolean $i$f$wrap = false;
                switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[$this$wrap$iv.ordinal()]) {
                    case 1: {
                        wEnumChatFormatting = WEnumChatFormatting.BLACK;
                        break block25;
                    }
                    case 2: {
                        wEnumChatFormatting = WEnumChatFormatting.DARK_BLUE;
                        break block25;
                    }
                    case 3: {
                        wEnumChatFormatting = WEnumChatFormatting.DARK_GREEN;
                        break block25;
                    }
                    case 4: {
                        wEnumChatFormatting = WEnumChatFormatting.DARK_AQUA;
                        break block25;
                    }
                    case 5: {
                        wEnumChatFormatting = WEnumChatFormatting.DARK_RED;
                        break block25;
                    }
                    case 6: {
                        wEnumChatFormatting = WEnumChatFormatting.DARK_PURPLE;
                        break block25;
                    }
                    case 7: {
                        wEnumChatFormatting = WEnumChatFormatting.GOLD;
                        break block25;
                    }
                    case 8: {
                        wEnumChatFormatting = WEnumChatFormatting.GRAY;
                        break block25;
                    }
                    case 9: {
                        wEnumChatFormatting = WEnumChatFormatting.DARK_GRAY;
                        break block25;
                    }
                    case 10: {
                        wEnumChatFormatting = WEnumChatFormatting.BLUE;
                        break block25;
                    }
                    case 11: {
                        wEnumChatFormatting = WEnumChatFormatting.GREEN;
                        break block25;
                    }
                    case 12: {
                        wEnumChatFormatting = WEnumChatFormatting.AQUA;
                        break block25;
                    }
                    case 13: {
                        wEnumChatFormatting = WEnumChatFormatting.RED;
                        break block25;
                    }
                    case 14: {
                        wEnumChatFormatting = WEnumChatFormatting.LIGHT_PURPLE;
                        break block25;
                    }
                    case 15: {
                        wEnumChatFormatting = WEnumChatFormatting.YELLOW;
                        break block25;
                    }
                    case 16: {
                        wEnumChatFormatting = WEnumChatFormatting.WHITE;
                        break block25;
                    }
                    case 17: {
                        wEnumChatFormatting = WEnumChatFormatting.OBFUSCATED;
                        break block25;
                    }
                    case 18: {
                        wEnumChatFormatting = WEnumChatFormatting.BOLD;
                        break block25;
                    }
                    case 19: {
                        wEnumChatFormatting = WEnumChatFormatting.STRIKETHROUGH;
                        break block25;
                    }
                    case 20: {
                        wEnumChatFormatting = WEnumChatFormatting.UNDERLINE;
                        break block25;
                    }
                    case 21: {
                        wEnumChatFormatting = WEnumChatFormatting.ITALIC;
                        break block25;
                    }
                    case 22: {
                        wEnumChatFormatting = WEnumChatFormatting.RESET;
                        break block25;
                    }
                    default: {
                        throw new NoWhenBranchMatchedException();
                    }
                }
            }
            wEnumChatFormatting = null;
        }
        return wEnumChatFormatting;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void setColor(@Nullable WEnumChatFormatting value) {
        TextFormatting textFormatting;
        Style style = this.wrapped;
        WEnumChatFormatting wEnumChatFormatting = value;
        if (wEnumChatFormatting != null) {
            TextFormatting textFormatting2;
            void $this$unwrap$iv;
            WEnumChatFormatting wEnumChatFormatting2 = wEnumChatFormatting;
            Style style2 = style;
            boolean $i$f$unwrap = false;
            switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[$this$unwrap$iv.ordinal()]) {
                case 1: {
                    textFormatting2 = TextFormatting.BLACK;
                    break;
                }
                case 2: {
                    textFormatting2 = TextFormatting.DARK_BLUE;
                    break;
                }
                case 3: {
                    textFormatting2 = TextFormatting.DARK_GREEN;
                    break;
                }
                case 4: {
                    textFormatting2 = TextFormatting.DARK_AQUA;
                    break;
                }
                case 5: {
                    textFormatting2 = TextFormatting.DARK_RED;
                    break;
                }
                case 6: {
                    textFormatting2 = TextFormatting.DARK_PURPLE;
                    break;
                }
                case 7: {
                    textFormatting2 = TextFormatting.GOLD;
                    break;
                }
                case 8: {
                    textFormatting2 = TextFormatting.GRAY;
                    break;
                }
                case 9: {
                    textFormatting2 = TextFormatting.DARK_GRAY;
                    break;
                }
                case 10: {
                    textFormatting2 = TextFormatting.BLUE;
                    break;
                }
                case 11: {
                    textFormatting2 = TextFormatting.GREEN;
                    break;
                }
                case 12: {
                    textFormatting2 = TextFormatting.AQUA;
                    break;
                }
                case 13: {
                    textFormatting2 = TextFormatting.RED;
                    break;
                }
                case 14: {
                    textFormatting2 = TextFormatting.LIGHT_PURPLE;
                    break;
                }
                case 15: {
                    textFormatting2 = TextFormatting.YELLOW;
                    break;
                }
                case 16: {
                    textFormatting2 = TextFormatting.WHITE;
                    break;
                }
                case 17: {
                    textFormatting2 = TextFormatting.OBFUSCATED;
                    break;
                }
                case 18: {
                    textFormatting2 = TextFormatting.BOLD;
                    break;
                }
                case 19: {
                    textFormatting2 = TextFormatting.STRIKETHROUGH;
                    break;
                }
                case 20: {
                    textFormatting2 = TextFormatting.UNDERLINE;
                    break;
                }
                case 21: {
                    textFormatting2 = TextFormatting.ITALIC;
                    break;
                }
                case 22: {
                    textFormatting2 = TextFormatting.RESET;
                    break;
                }
                default: {
                    throw new NoWhenBranchMatchedException();
                }
            }
            TextFormatting textFormatting3 = textFormatting2;
            style = style2;
            textFormatting = textFormatting3;
        } else {
            textFormatting = null;
        }
        style.func_150238_a(textFormatting);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof ChatStyleImpl && Intrinsics.areEqual((Object)((ChatStyleImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final Style getWrapped() {
        return this.wrapped;
    }

    public ChatStyleImpl(@NotNull Style wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

