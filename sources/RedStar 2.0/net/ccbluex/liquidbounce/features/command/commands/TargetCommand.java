package net.ccbluex.liquidbounce.features.command.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\b\n\n\u0000\n\n\n\b\n \n\b\u000020B¢J02\f\b00H¢\bJ!\t\b00\n2\f\b00H¢¨\f"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/TargetCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "Pride"})
public final class TargetCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (args.length > 1) {
            if (StringsKt.equals(args[1], "players", true)) {
                EntityUtils.targetPlayer = !EntityUtils.targetPlayer;
                this.chat("§7Target player toggled " + (EntityUtils.targetPlayer ? "on" : "off") + '.');
                this.playEdit();
                return;
            }
            if (StringsKt.equals(args[1], "mobs", true)) {
                EntityUtils.targetMobs = !EntityUtils.targetMobs;
                this.chat("§7Target mobs toggled " + (EntityUtils.targetMobs ? "on" : "off") + '.');
                this.playEdit();
                return;
            }
            if (StringsKt.equals(args[1], "animals", true)) {
                EntityUtils.targetAnimals = !EntityUtils.targetAnimals;
                this.chat("§7Target animals toggled " + (EntityUtils.targetAnimals ? "on" : "off") + '.');
                this.playEdit();
                return;
            }
            if (StringsKt.equals(args[1], "invisible", true)) {
                EntityUtils.targetInvisible = !EntityUtils.targetInvisible;
                this.chat("§7Target Invisible toggled " + (EntityUtils.targetInvisible ? "on" : "off") + '.');
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
    @NotNull
    public List<String> tabComplete(@NotNull String[] args) {
        List list;
        Intrinsics.checkParameterIsNotNull(args, "args");
        String[] stringArray = args;
        boolean bl = false;
        if (stringArray.length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                void $this$filterTo$iv$iv;
                Iterable $this$filter$iv = CollectionsKt.listOf("players", "mobs", "animals", "invisible");
                boolean $i$f$filter = false;
                Iterable iterable = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    String it = (String)element$iv$iv;
                    boolean bl2 = false;
                    if (!StringsKt.startsWith(it, args[0], true)) continue;
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
