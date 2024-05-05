/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command.shortcuts;

import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.command.Command;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u001e\u0010\u0004\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u00070\u00060\u0005\u00a2\u0006\u0002\u0010\bJ\u001b\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00030\u0007H\u0016\u00a2\u0006\u0002\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR)\u0010\u0004\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u00070\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0011"}, d2={"Lnet/ccbluex/liquidbounce/features/command/shortcuts/Shortcut;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "name", "", "script", "", "Lkotlin/Pair;", "", "(Ljava/lang/String;Ljava/util/List;)V", "getName", "()Ljava/lang/String;", "getScript", "()Ljava/util/List;", "execute", "", "args", "([Ljava/lang/String;)V", "KyinoClient"})
public final class Shortcut
extends Command {
    @NotNull
    private final String name;
    @NotNull
    private final List<Pair<Command, String[]>> script;

    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkParameterIsNotNull(args2, "args");
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

