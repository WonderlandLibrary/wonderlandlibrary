/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Pair
 */
package net.ccbluex.liquidbounce.features.command.shortcuts;

import java.util.List;
import kotlin.Pair;
import net.ccbluex.liquidbounce.features.command.Command;

public final class Shortcut
extends Command {
    private final String name;
    private final List<Pair<Command, String[]>> script;

    @Override
    public void execute(String[] args) {
        Iterable $this$forEach$iv = this.script;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            Pair it = (Pair)element$iv;
            boolean bl = false;
            ((Command)it.getFirst()).execute((String[])it.getSecond());
        }
    }

    public final String getName() {
        return this.name;
    }

    public final List<Pair<Command, String[]>> getScript() {
        return this.script;
    }

    public Shortcut(String name, List<? extends Pair<? extends Command, String[]>> script) {
        super(name, new String[0]);
        this.name = name;
        this.script = script;
    }
}

