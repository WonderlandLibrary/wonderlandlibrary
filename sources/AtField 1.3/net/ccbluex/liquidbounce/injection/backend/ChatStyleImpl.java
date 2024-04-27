/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.TypeCastException
 *  net.minecraft.util.text.Style
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraft.util.text.event.ClickEvent
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.NoWhenBranchMatchedException;
import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.event.IClickEvent;
import net.ccbluex.liquidbounce.api.minecraft.util.IChatStyle;
import net.ccbluex.liquidbounce.api.minecraft.util.WEnumChatFormatting;
import net.ccbluex.liquidbounce.injection.backend.ClickEventImpl;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt$WhenMappings;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import org.jetbrains.annotations.Nullable;

public final class ChatStyleImpl
implements IChatStyle {
    private final Style wrapped;

    @Override
    public WEnumChatFormatting getColor() {
        WEnumChatFormatting wEnumChatFormatting;
        block25: {
            block24: {
                TextFormatting textFormatting = this.wrapped.func_150215_a();
                if (textFormatting == null) break block24;
                TextFormatting textFormatting2 = textFormatting;
                boolean bl = false;
                switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[textFormatting2.ordinal()]) {
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

    @Override
    public void setUnderlined(boolean bl) {
        this.wrapped.func_150228_d(Boolean.valueOf(bl));
    }

    @Override
    public IClickEvent getChatClickEvent() {
        IClickEvent iClickEvent;
        ClickEvent clickEvent = this.wrapped.func_150235_h();
        if (clickEvent != null) {
            ClickEvent clickEvent2 = clickEvent;
            boolean bl = false;
            iClickEvent = new ClickEventImpl(clickEvent2);
        } else {
            iClickEvent = null;
        }
        return iClickEvent;
    }

    @Override
    public boolean getUnderlined() {
        return this.wrapped.func_150234_e();
    }

    @Override
    public void setChatClickEvent(@Nullable IClickEvent iClickEvent) {
        ClickEvent clickEvent;
        Style style = this.wrapped;
        IClickEvent iClickEvent2 = iClickEvent;
        if (iClickEvent2 != null) {
            IClickEvent iClickEvent3 = iClickEvent2;
            Style style2 = style;
            boolean bl = false;
            IClickEvent iClickEvent4 = iClickEvent3;
            if (iClickEvent4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.ClickEventImpl");
            }
            ClickEvent clickEvent2 = ((ClickEventImpl)iClickEvent4).getWrapped();
            style = style2;
            clickEvent = clickEvent2;
        } else {
            clickEvent = null;
        }
        style.func_150241_a(clickEvent);
    }

    public final Style getWrapped() {
        return this.wrapped;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof ChatStyleImpl && ((ChatStyleImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public void setColor(@Nullable WEnumChatFormatting wEnumChatFormatting) {
        TextFormatting textFormatting;
        Style style = this.wrapped;
        WEnumChatFormatting wEnumChatFormatting2 = wEnumChatFormatting;
        if (wEnumChatFormatting2 != null) {
            TextFormatting textFormatting2;
            WEnumChatFormatting wEnumChatFormatting3 = wEnumChatFormatting2;
            Style style2 = style;
            boolean bl = false;
            switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[wEnumChatFormatting3.ordinal()]) {
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

    public ChatStyleImpl(Style style) {
        this.wrapped = style;
    }
}

