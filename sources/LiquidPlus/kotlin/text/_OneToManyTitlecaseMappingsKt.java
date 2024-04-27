/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.text;

import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000\f\n\u0000\n\u0002\u0010\u000e\n\u0002\u0010\f\n\u0000\u001a\f\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0000\u00a8\u0006\u0003"}, d2={"titlecaseImpl", "", "", "kotlin-stdlib"})
public final class _OneToManyTitlecaseMappingsKt {
    @NotNull
    public static final String titlecaseImpl(char $this$titlecaseImpl) {
        char c = $this$titlecaseImpl;
        String string = String.valueOf(c).toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toUpperCase(Locale.ROOT)");
        String uppercase = string;
        if (uppercase.length() > 1) {
            String string2;
            if ($this$titlecaseImpl == '\u0149') {
                string2 = uppercase;
            } else {
                c = uppercase.charAt(0);
                string = uppercase;
                int n = 1;
                String string3 = string.substring(n);
                Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String).substring(startIndex)");
                String string4 = string3.toLowerCase(Locale.ROOT);
                Intrinsics.checkNotNullExpressionValue(string4, "this as java.lang.String).toLowerCase(Locale.ROOT)");
                string = string4;
                string2 = c + string;
            }
            return string2;
        }
        return String.valueOf(Character.toTitleCase($this$titlecaseImpl));
    }
}

