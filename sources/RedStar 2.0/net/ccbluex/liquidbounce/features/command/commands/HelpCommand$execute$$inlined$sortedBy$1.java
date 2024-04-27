// ERROR: Unable to apply inner class name fixup
package net.ccbluex.liquidbounce.features.command.commands;

import java.util.Comparator;
import kotlin.Metadata;
import kotlin.comparisons.ComparisonsKt;
import net.ccbluex.liquidbounce.features.command.Command;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\n\u0000\n\b\n\b\n\b\n\b\n\b\n\b\n\b\u00000\"\b\u00002\n *HH2\n *HHH\n¢\b¨\b"}, d2={"<anonymous>", "", "T", "a", "kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I", "kotlin/comparisons/ComparisonsKt__ComparisonsKt$compareBy$2"})
public static final class HelpCommand$execute$.inlined.sortedBy.1<T>
implements Comparator<T> {
    @Override
    public final int compare(T a, T b) {
        boolean bl = false;
        Command it = (Command)a;
        boolean bl2 = false;
        Comparable comparable = (Comparable)((Object)it.getCommand());
        it = (Command)b;
        Comparable comparable2 = comparable;
        bl2 = false;
        String string = it.getCommand();
        return ComparisonsKt.compareValues(comparable2, (Comparable)((Object)string));
    }
}
