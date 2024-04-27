package net.ccbluex.liquidbounce.features.command.shortcuts;

import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.command.Command;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\n\n\u0000\n\n\u0000\n \n\n\n\b\n\n\b\u000020B-00\n\b0000¢\bJ\r02\f\b00H¢R0¢\b\n\u0000\b\t\nR)0\n\b0000¢\b\n\u0000\b\f¨"}, d2={"Lnet/ccbluex/liquidbounce/features/command/shortcuts/Shortcut;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "name", "", "script", "", "Lkotlin/Pair;", "", "(Ljava/lang/String;Ljava/util/List;)V", "getName", "()Ljava/lang/String;", "getScript", "()Ljava/util/List;", "execute", "", "args", "([Ljava/lang/String;)V", "Pride"})
public final class Shortcut
extends Command {
    @NotNull
    private final String name;
    @NotNull
    private final List<Pair<Command, String[]>> script;

    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        Iterable $this$forEach$iv = this.script;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            Pair it = (Pair)element$iv;
            boolean bl = false;
            ((Command)it.getFirst()).execute((String[])it.getSecond());
        }
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    @NotNull
    public final List<Pair<Command, String[]>> getScript() {
        return this.script;
    }

    public Shortcut(@NotNull String name, @NotNull List<? extends Pair<? extends Command, String[]>> script) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(script, "script");
        super(name, new String[0]);
        this.name = name;
        this.script = script;
    }
}
