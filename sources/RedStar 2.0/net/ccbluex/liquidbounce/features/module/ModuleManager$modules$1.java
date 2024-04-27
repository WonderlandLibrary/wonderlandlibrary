package net.ccbluex.liquidbounce.features.module;

import java.util.Comparator;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\n\u0000\n\b\n\u0000\n\n\b\u000002\n *002\n *00H\n¢\b"}, d2={"<anonymous>", "", "module1", "Lnet/ccbluex/liquidbounce/features/module/Module;", "kotlin.jvm.PlatformType", "module2", "compare"})
final class ModuleManager$modules$1<T>
implements Comparator<E> {
    public static final ModuleManager$modules$1 INSTANCE = new /* invalid duplicate definition of identical inner class */;

    @Override
    public final int compare(Module module1, Module module2) {
        return module1.getName().compareTo(module2.getName());
    }

    ModuleManager$modules$1() {
    }
}
