/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.CollectionsKt
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.EntityUtils;

public final class TargetCommand
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            if (StringsKt.equals((String)args[1], (String)"players", (boolean)true)) {
                EntityUtils.targetPlayer = !EntityUtils.targetPlayer;
                this.chat("\u00a77Target player toggled " + (EntityUtils.targetPlayer ? "on" : "off") + '.');
                this.playEdit();
                return;
            }
            if (StringsKt.equals((String)args[1], (String)"mobs", (boolean)true)) {
                EntityUtils.targetMobs = !EntityUtils.targetMobs;
                this.chat("\u00a77Target mobs toggled " + (EntityUtils.targetMobs ? "on" : "off") + '.');
                this.playEdit();
                return;
            }
            if (StringsKt.equals((String)args[1], (String)"animals", (boolean)true)) {
                EntityUtils.targetAnimals = !EntityUtils.targetAnimals;
                this.chat("\u00a77Target animals toggled " + (EntityUtils.targetAnimals ? "on" : "off") + '.');
                this.playEdit();
                return;
            }
            if (StringsKt.equals((String)args[1], (String)"invisible", (boolean)true)) {
                EntityUtils.targetInvisible = !EntityUtils.targetInvisible;
                this.chat("\u00a77Target Invisible toggled " + (EntityUtils.targetInvisible ? "on" : "off") + '.');
                this.playEdit();
                return;
            }
        }
        this.chatSyntax("target <players/mobs/animals/invisible>");
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public List<String> tabComplete(String[] args) {
        List list;
        String[] stringArray = args;
        boolean bl = false;
        if (stringArray.length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                void $this$filterTo$iv$iv;
                Iterable $this$filter$iv = CollectionsKt.listOf((Object[])new String[]{"players", "mobs", "animals", "invisible"});
                boolean $i$f$filter = false;
                Iterable iterable = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    String it = (String)element$iv$iv;
                    boolean bl2 = false;
                    if (!StringsKt.startsWith((String)it, (String)args[0], (boolean)true)) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                list = (List)destination$iv$iv;
                break;
            }
            default: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }

    public TargetCommand() {
        super("target", new String[0]);
    }
}

