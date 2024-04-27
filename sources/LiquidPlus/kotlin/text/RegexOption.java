/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.text.FlagEnum;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\r\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002B\u0019\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0006R\u0014\u0010\u0005\u001a\u00020\u0004X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u0003\u001a\u00020\u0004X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\bj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010\u00a8\u0006\u0011"}, d2={"Lkotlin/text/RegexOption;", "", "Lkotlin/text/FlagEnum;", "value", "", "mask", "(Ljava/lang/String;III)V", "getMask", "()I", "getValue", "IGNORE_CASE", "MULTILINE", "LITERAL", "UNIX_LINES", "COMMENTS", "DOT_MATCHES_ALL", "CANON_EQ", "kotlin-stdlib"})
public final class RegexOption
extends Enum<RegexOption>
implements FlagEnum {
    private final int value;
    private final int mask;
    public static final /* enum */ RegexOption IGNORE_CASE = new RegexOption("IGNORE_CASE", 0, 2, 0, 2, null);
    public static final /* enum */ RegexOption MULTILINE = new RegexOption("MULTILINE", 1, 8, 0, 2, null);
    public static final /* enum */ RegexOption LITERAL = new RegexOption("LITERAL", 2, 16, 0, 2, null);
    public static final /* enum */ RegexOption UNIX_LINES = new RegexOption("UNIX_LINES", 3, 1, 0, 2, null);
    public static final /* enum */ RegexOption COMMENTS = new RegexOption("COMMENTS", 4, 4, 0, 2, null);
    public static final /* enum */ RegexOption DOT_MATCHES_ALL = new RegexOption("DOT_MATCHES_ALL", 5, 32, 0, 2, null);
    public static final /* enum */ RegexOption CANON_EQ = new RegexOption("CANON_EQ", 6, 128, 0, 2, null);
    private static final /* synthetic */ RegexOption[] $VALUES;

    private RegexOption(int value, int mask) {
        this.value = value;
        this.mask = mask;
    }

    /* synthetic */ RegexOption(String string, int n, int n2, int n3, int n4, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n4 & 2) != 0) {
            n3 = n2;
        }
        this(n2, n3);
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public int getMask() {
        return this.mask;
    }

    public static RegexOption[] values() {
        return (RegexOption[])$VALUES.clone();
    }

    public static RegexOption valueOf(String value) {
        return Enum.valueOf(RegexOption.class, value);
    }

    static {
        $VALUES = regexOptionArray = new RegexOption[]{RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.LITERAL, RegexOption.UNIX_LINES, RegexOption.COMMENTS, RegexOption.DOT_MATCHES_ALL, RegexOption.CANON_EQ};
    }
}

