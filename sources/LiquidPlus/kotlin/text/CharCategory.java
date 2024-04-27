/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\f\n\u0002\b \b\u0086\u0001\u0018\u0000 -2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001-B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0086\u0002R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nj\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014j\u0002\b\u0015j\u0002\b\u0016j\u0002\b\u0017j\u0002\b\u0018j\u0002\b\u0019j\u0002\b\u001aj\u0002\b\u001bj\u0002\b\u001cj\u0002\b\u001dj\u0002\b\u001ej\u0002\b\u001fj\u0002\b j\u0002\b!j\u0002\b\"j\u0002\b#j\u0002\b$j\u0002\b%j\u0002\b&j\u0002\b'j\u0002\b(j\u0002\b)j\u0002\b*j\u0002\b+j\u0002\b,\u00a8\u0006."}, d2={"Lkotlin/text/CharCategory;", "", "value", "", "code", "", "(Ljava/lang/String;IILjava/lang/String;)V", "getCode", "()Ljava/lang/String;", "getValue", "()I", "contains", "", "char", "", "UNASSIGNED", "UPPERCASE_LETTER", "LOWERCASE_LETTER", "TITLECASE_LETTER", "MODIFIER_LETTER", "OTHER_LETTER", "NON_SPACING_MARK", "ENCLOSING_MARK", "COMBINING_SPACING_MARK", "DECIMAL_DIGIT_NUMBER", "LETTER_NUMBER", "OTHER_NUMBER", "SPACE_SEPARATOR", "LINE_SEPARATOR", "PARAGRAPH_SEPARATOR", "CONTROL", "FORMAT", "PRIVATE_USE", "SURROGATE", "DASH_PUNCTUATION", "START_PUNCTUATION", "END_PUNCTUATION", "CONNECTOR_PUNCTUATION", "OTHER_PUNCTUATION", "MATH_SYMBOL", "CURRENCY_SYMBOL", "MODIFIER_SYMBOL", "OTHER_SYMBOL", "INITIAL_QUOTE_PUNCTUATION", "FINAL_QUOTE_PUNCTUATION", "Companion", "kotlin-stdlib"})
public final class CharCategory
extends Enum<CharCategory> {
    @NotNull
    public static final Companion Companion;
    private final int value;
    @NotNull
    private final String code;
    public static final /* enum */ CharCategory UNASSIGNED;
    public static final /* enum */ CharCategory UPPERCASE_LETTER;
    public static final /* enum */ CharCategory LOWERCASE_LETTER;
    public static final /* enum */ CharCategory TITLECASE_LETTER;
    public static final /* enum */ CharCategory MODIFIER_LETTER;
    public static final /* enum */ CharCategory OTHER_LETTER;
    public static final /* enum */ CharCategory NON_SPACING_MARK;
    public static final /* enum */ CharCategory ENCLOSING_MARK;
    public static final /* enum */ CharCategory COMBINING_SPACING_MARK;
    public static final /* enum */ CharCategory DECIMAL_DIGIT_NUMBER;
    public static final /* enum */ CharCategory LETTER_NUMBER;
    public static final /* enum */ CharCategory OTHER_NUMBER;
    public static final /* enum */ CharCategory SPACE_SEPARATOR;
    public static final /* enum */ CharCategory LINE_SEPARATOR;
    public static final /* enum */ CharCategory PARAGRAPH_SEPARATOR;
    public static final /* enum */ CharCategory CONTROL;
    public static final /* enum */ CharCategory FORMAT;
    public static final /* enum */ CharCategory PRIVATE_USE;
    public static final /* enum */ CharCategory SURROGATE;
    public static final /* enum */ CharCategory DASH_PUNCTUATION;
    public static final /* enum */ CharCategory START_PUNCTUATION;
    public static final /* enum */ CharCategory END_PUNCTUATION;
    public static final /* enum */ CharCategory CONNECTOR_PUNCTUATION;
    public static final /* enum */ CharCategory OTHER_PUNCTUATION;
    public static final /* enum */ CharCategory MATH_SYMBOL;
    public static final /* enum */ CharCategory CURRENCY_SYMBOL;
    public static final /* enum */ CharCategory MODIFIER_SYMBOL;
    public static final /* enum */ CharCategory OTHER_SYMBOL;
    public static final /* enum */ CharCategory INITIAL_QUOTE_PUNCTUATION;
    public static final /* enum */ CharCategory FINAL_QUOTE_PUNCTUATION;
    private static final /* synthetic */ CharCategory[] $VALUES;

    private CharCategory(int value, String code) {
        this.value = value;
        this.code = code;
    }

    public final int getValue() {
        return this.value;
    }

    @NotNull
    public final String getCode() {
        return this.code;
    }

    public final boolean contains(char c) {
        return Character.getType(c) == this.value;
    }

    public static CharCategory[] values() {
        return (CharCategory[])$VALUES.clone();
    }

    public static CharCategory valueOf(String value) {
        return Enum.valueOf(CharCategory.class, value);
    }

    static {
        UNASSIGNED = new CharCategory(0, "Cn");
        UPPERCASE_LETTER = new CharCategory(1, "Lu");
        LOWERCASE_LETTER = new CharCategory(2, "Ll");
        TITLECASE_LETTER = new CharCategory(3, "Lt");
        MODIFIER_LETTER = new CharCategory(4, "Lm");
        OTHER_LETTER = new CharCategory(5, "Lo");
        NON_SPACING_MARK = new CharCategory(6, "Mn");
        ENCLOSING_MARK = new CharCategory(7, "Me");
        COMBINING_SPACING_MARK = new CharCategory(8, "Mc");
        DECIMAL_DIGIT_NUMBER = new CharCategory(9, "Nd");
        LETTER_NUMBER = new CharCategory(10, "Nl");
        OTHER_NUMBER = new CharCategory(11, "No");
        SPACE_SEPARATOR = new CharCategory(12, "Zs");
        LINE_SEPARATOR = new CharCategory(13, "Zl");
        PARAGRAPH_SEPARATOR = new CharCategory(14, "Zp");
        CONTROL = new CharCategory(15, "Cc");
        FORMAT = new CharCategory(16, "Cf");
        PRIVATE_USE = new CharCategory(18, "Co");
        SURROGATE = new CharCategory(19, "Cs");
        DASH_PUNCTUATION = new CharCategory(20, "Pd");
        START_PUNCTUATION = new CharCategory(21, "Ps");
        END_PUNCTUATION = new CharCategory(22, "Pe");
        CONNECTOR_PUNCTUATION = new CharCategory(23, "Pc");
        OTHER_PUNCTUATION = new CharCategory(24, "Po");
        MATH_SYMBOL = new CharCategory(25, "Sm");
        CURRENCY_SYMBOL = new CharCategory(26, "Sc");
        MODIFIER_SYMBOL = new CharCategory(27, "Sk");
        OTHER_SYMBOL = new CharCategory(28, "So");
        INITIAL_QUOTE_PUNCTUATION = new CharCategory(29, "Pi");
        FINAL_QUOTE_PUNCTUATION = new CharCategory(30, "Pf");
        $VALUES = charCategoryArray = new CharCategory[]{CharCategory.UNASSIGNED, CharCategory.UPPERCASE_LETTER, CharCategory.LOWERCASE_LETTER, CharCategory.TITLECASE_LETTER, CharCategory.MODIFIER_LETTER, CharCategory.OTHER_LETTER, CharCategory.NON_SPACING_MARK, CharCategory.ENCLOSING_MARK, CharCategory.COMBINING_SPACING_MARK, CharCategory.DECIMAL_DIGIT_NUMBER, CharCategory.LETTER_NUMBER, CharCategory.OTHER_NUMBER, CharCategory.SPACE_SEPARATOR, CharCategory.LINE_SEPARATOR, CharCategory.PARAGRAPH_SEPARATOR, CharCategory.CONTROL, CharCategory.FORMAT, CharCategory.PRIVATE_USE, CharCategory.SURROGATE, CharCategory.DASH_PUNCTUATION, CharCategory.START_PUNCTUATION, CharCategory.END_PUNCTUATION, CharCategory.CONNECTOR_PUNCTUATION, CharCategory.OTHER_PUNCTUATION, CharCategory.MATH_SYMBOL, CharCategory.CURRENCY_SYMBOL, CharCategory.MODIFIER_SYMBOL, CharCategory.OTHER_SYMBOL, CharCategory.INITIAL_QUOTE_PUNCTUATION, CharCategory.FINAL_QUOTE_PUNCTUATION};
        Companion = new Companion(null);
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2={"Lkotlin/text/CharCategory$Companion;", "", "()V", "valueOf", "Lkotlin/text/CharCategory;", "category", "", "kotlin-stdlib"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final CharCategory valueOf(int category) {
            CharCategory charCategory;
            int n = category;
            boolean bl = 0 <= n ? n < 17 : false;
            if (bl) {
                charCategory = CharCategory.values()[category];
            } else {
                boolean bl2 = 18 <= n ? n < 31 : false;
                if (bl2) {
                    charCategory = CharCategory.values()[category - 1];
                } else {
                    throw new IllegalArgumentException("Category #" + category + " is not defined.");
                }
            }
            return charCategory;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

