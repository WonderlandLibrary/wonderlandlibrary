// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import kotlin.jvm.internal.Intrinsics;
import java.util.Collection;

public class StringsKt___StringsKt extends StringsKt__StringsKt
{
    public static final <C extends Collection<? super Character>> C toCollection(final CharSequence $this$toCollection, final C destination) {
        Intrinsics.checkParameterIsNotNull($this$toCollection, "$this$toCollection");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        for (int i = 0; i < $this$toCollection.length(); ++i) {
            final char item = $this$toCollection.charAt(i);
            ((Collection<Character>)destination).add(item);
        }
        return destination;
    }
}
