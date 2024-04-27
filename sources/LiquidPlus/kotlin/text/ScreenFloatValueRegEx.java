/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u00c2\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2={"Lkotlin/text/ScreenFloatValueRegEx;", "", "()V", "value", "Lkotlin/text/Regex;", "kotlin-stdlib"})
final class ScreenFloatValueRegEx {
    @NotNull
    public static final ScreenFloatValueRegEx INSTANCE;
    @JvmField
    @NotNull
    public static final Regex value;

    private ScreenFloatValueRegEx() {
    }

    static {
        ScreenFloatValueRegEx screenFloatValueRegEx;
        ScreenFloatValueRegEx $this$value_u24lambda_u2d0 = screenFloatValueRegEx = (INSTANCE = new ScreenFloatValueRegEx());
        boolean bl = false;
        String Digits = "(\\p{Digit}+)";
        String HexDigits = "(\\p{XDigit}+)";
        String Exp = Intrinsics.stringPlus("[eE][+-]?", Digits);
        String HexString = "(0[xX]" + HexDigits + "(\\.)?)|(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ')';
        String Number2 = '(' + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|(\\.(" + Digits + ")(" + Exp + ")?)|((" + HexString + ")[pP][+-]?" + Digits + ')';
        String fpRegex = "[\\x00-\\x20]*[+-]?(NaN|Infinity|((" + Number2 + ")[fFdD]?))[\\x00-\\x20]*";
        value = new Regex(fpRegex);
    }
}

