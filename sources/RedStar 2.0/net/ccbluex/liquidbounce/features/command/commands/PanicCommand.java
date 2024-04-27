package net.ccbluex.liquidbounce.features.command.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\b\n\n\u0000\n\n\n\b\n \n\b\u000020B¢J02\f\b00H¢\bJ!\t\b00\n2\f\b00H¢¨\f"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/PanicCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "Pride"})
public final class PanicCommand
extends Command {
    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        $this$filter$iv = LiquidBounce.INSTANCE.getModuleManager().getModules();
        $i$f$filter = false;
        var5_5 = $this$filter$iv;
        destination$iv$iv = new ArrayList<E>();
        $i$f$filterTo = false;
        for (T element$iv$iv : $this$filterTo$iv$iv) {
            it = (Module)element$iv$iv;
            $i$a$-filter-PanicCommand$execute$modules$1 = false;
            if (!it.getState()) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        modules = (List)destination$iv$iv;
        msg = null;
        if (args.length <= 1) ** GOTO lbl-1000
        $i$f$filter = args[1];
        $this$filterTo$iv$iv = false;
        if ($i$f$filter.length() > 0) {
            $i$f$filter = args[1];
            $this$filterTo$iv$iv = false;
            v0 = $i$f$filter;
            if (v0 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            v1 = v0.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(v1, "(this as java.lang.String).toLowerCase()");
            $i$f$filter = v1;
            tmp = -1;
            switch ($i$f$filter.hashCode()) {
                case 96673: {
                    if (!$i$f$filter.equals("all")) break;
                    tmp = 1;
                    break;
                }
                case 1128455843: {
                    if (!$i$f$filter.equals("nonrender")) break;
                    tmp = 2;
                    break;
                }
            }
            switch (tmp) {
                case 1: {
                    msg = "all";
                    break;
                }
                case 2: {
                    $this$filter$iv = modules;
                    $i$f$filter = false;
                    $i$f$filterTo = $this$filter$iv;
                    destination$iv$iv = new ArrayList<E>();
                    $i$f$filterTo = false;
                    for (E element$iv$iv : $this$filterTo$iv$iv) {
                        it = (Module)element$iv$iv;
                        $i$a$-filter-PanicCommand$execute$1 = false;
                        if (!(it.getCategory() != ModuleCategory.RENDER)) continue;
                        destination$iv$iv.add(element$iv$iv);
                    }
                    modules = (List)destination$iv$iv;
                    msg = "all non-render";
                    break;
                }
                default: {
                    $this$filter$iv = ModuleCategory.values();
                    $i$f$filter = false;
                    destination$iv$iv = $this$filter$iv;
                    destination$iv$iv /* !! */  = new ArrayList<E>();
                    $i$f$filterTo = false;
                    element$iv$iv = $this$filterTo$iv$iv;
                    var12_27 = ((void)element$iv$iv).length;
                    for ($i$a$-filter-PanicCommand$execute$1 = 0; $i$a$-filter-PanicCommand$execute$1 < var12_27; ++$i$a$-filter-PanicCommand$execute$1) {
                        it = element$iv$iv = element$iv$iv[$i$a$-filter-PanicCommand$execute$1];
                        $i$a$-filter-PanicCommand$execute$categories$1 = false;
                        if (!StringsKt.equals(it.getDisplayName(), args[1], true)) continue;
                        destination$iv$iv /* !! */ .add(element$iv$iv);
                    }
                    categories = (List)destination$iv$iv /* !! */ ;
                    if (categories.isEmpty()) {
                        this.chat("Category " + args[1] + " not found");
                        return;
                    }
                    category = (ModuleCategory)categories.get(0);
                    $this$filter$iv = modules;
                    $i$f$filter = false;
                    destination$iv$iv /* !! */  = $this$filter$iv;
                    destination$iv$iv = new ArrayList<E>();
                    $i$f$filterTo = false;
                    for (T element$iv$iv : $this$filterTo$iv$iv) {
                        it = (Module)element$iv$iv;
                        $i$a$-filter-PanicCommand$execute$2 = false;
                        if (!(it.getCategory() == category)) continue;
                        destination$iv$iv.add(element$iv$iv);
                    }
                    modules = (List)destination$iv$iv;
                    msg = "all " + category.getDisplayName();
                    break;
                }
            }
        } else lbl-1000:
        // 2 sources

        {
            this.chatSyntax("panic <all/nonrender/combat/player/movement/render/world/misc/exploit/fun>");
            return;
        }
        for (Module module : modules) {
            module.setState(false);
        }
        this.chat("Disabled " + msg + " modules.");
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
                Iterable $this$filter$iv = CollectionsKt.listOf("all", "nonrender", "combat", "player", "movement", "render", "world", "misc", "exploit", "fun");
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

    public PanicCommand() {
        super("panic", new String[0]);
    }
}
