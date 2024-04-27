package net.ccbluex.liquidbounce.features.command;

import java.util.function.Predicate;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.command.shortcuts.Shortcut;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\n\u0000\n\n\u0000\n\n\u0000\u0000020H\n¢\b"}, d2={"<anonymous>", "", "it", "Lnet/ccbluex/liquidbounce/features/command/Command;", "test"})
final class CommandManager$unregisterShortcut$removed$1<T>
implements Predicate<Command> {
    final String $name;

    @Override
    public final boolean test(@NotNull Command it) {
        Intrinsics.checkParameterIsNotNull(it, "it");
        return it instanceof Shortcut && StringsKt.equals(it.getCommand(), this.$name, true);
    }

    CommandManager$unregisterShortcut$removed$1(String string) {
        this.$name = string;
    }
}
