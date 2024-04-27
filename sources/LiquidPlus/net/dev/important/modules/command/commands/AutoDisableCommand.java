/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.command.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.modules.command.Command;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.misc.AutoDisable;
import net.dev.important.utils.ClientUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\bJ!\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2={"Lnet/dev/important/modules/command/commands/AutoDisableCommand;", "Lnet/dev/important/modules/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "LiquidBounce"})
public final class AutoDisableCommand
extends Command {
    public AutoDisableCommand() {
        String[] stringArray = new String[]{"ad"};
        super("autodisable", stringArray);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length == 2) {
            String string = args2[1].toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
            String string2 = string;
            if (Intrinsics.areEqual(string2, "list")) {
                Object it;
                Object element$iv$iv;
                Iterator $this$filterTo$iv$iv;
                this.chat("\u00a7c\u00a7lAutoDisable modules:");
                Iterable $this$filter$iv = Client.INSTANCE.getModuleManager().getModules();
                boolean $i$f$filter = false;
                Iterable iterable = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                Iterator iterator2 = $this$filterTo$iv$iv.iterator();
                while (iterator2.hasNext()) {
                    element$iv$iv = iterator2.next();
                    it = (Module)element$iv$iv;
                    boolean bl = false;
                    if (!(((Module)it).getAutoDisables().size() > 0)) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                Iterable $this$forEach$iv = (List)destination$iv$iv;
                boolean $i$f$forEach = false;
                for (Object element$iv : $this$forEach$iv) {
                    void $this$mapTo$iv$iv;
                    void $this$map$iv;
                    Module it2 = (Module)element$iv;
                    boolean bl = false;
                    element$iv$iv = it2.getAutoDisables();
                    it = new StringBuilder().append("\u00a76> \u00a7c").append(it2.getName()).append(" \u00a77| \u00a7a");
                    boolean $i$f$map = false;
                    void var12_31 = $this$map$iv;
                    Collection destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                    boolean $i$f$mapTo = false;
                    for (Object item$iv$iv : $this$mapTo$iv$iv) {
                        void d;
                        AutoDisable.DisableEvent disableEvent = (AutoDisable.DisableEvent)((Object)item$iv$iv);
                        Collection collection = destination$iv$iv2;
                        boolean bl2 = false;
                        String string3 = d.name().toLowerCase();
                        Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String).toLowerCase()");
                        collection.add(string3);
                    }
                    ClientUtils.displayChatMessage(((StringBuilder)it).append(CollectionsKt.joinToString$default((List)destination$iv$iv2, null, null, null, 0, null, null, 63, null)).toString());
                }
                return;
            }
            if (Intrinsics.areEqual(string2, "clear")) {
                Iterable $this$filter$iv = Client.INSTANCE.getModuleManager().getModules();
                boolean $i$f$filter = false;
                Iterable $this$filterTo$iv$iv = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                Iterator bl = $this$filterTo$iv$iv.iterator();
                while (bl.hasNext()) {
                    Object element$iv$iv = bl.next();
                    Module it = (Module)element$iv$iv;
                    boolean bl3 = false;
                    if (!(it.getAutoDisables().size() > 0)) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                Iterable $this$forEach$iv = (List)destination$iv$iv;
                boolean $i$f$forEach = false;
                for (Object element$iv : $this$forEach$iv) {
                    Module it = (Module)element$iv;
                    boolean bl4 = false;
                    it.getAutoDisables().clear();
                }
                this.chat("Successfully cleared the AutoDisable list.");
                return;
            }
        } else if (args2.length > 2) {
            Module module2 = Client.INSTANCE.getModuleManager().getModule(args2[1]);
            if (module2 == null) {
                this.chat("Module \u00a7a\u00a7l" + args2[1] + "\u00a73 not found.");
                return;
            }
            if (StringsKt.equals(args2[2], "clear", true)) {
                module2.getAutoDisables().clear();
                this.chat("Module \u00a7a\u00a7l" + module2.getName() + "\u00a73 has been removed from AutoDisable trigger list.");
                this.playEdit();
                return;
            }
            try {
                String string;
                String $this$filterTo$iv$iv = args2[2].toUpperCase();
                Intrinsics.checkNotNullExpressionValue($this$filterTo$iv$iv, "this as java.lang.String).toUpperCase()");
                AutoDisable.DisableEvent disableWhen = AutoDisable.DisableEvent.valueOf($this$filterTo$iv$iv);
                String added = "will now";
                if (module2.getAutoDisables().contains((Object)disableWhen)) {
                    if (module2.getAutoDisables().remove((Object)disableWhen)) {
                        added = "will no longer";
                    }
                } else {
                    module2.getAutoDisables().add(disableWhen);
                }
                switch (WhenMappings.$EnumSwitchMapping$0[disableWhen.ordinal()]) {
                    case 1: {
                        string = "when you get flagged.";
                        break;
                    }
                    case 2: {
                        string = "when you change the world.";
                        break;
                    }
                    case 3: {
                        string = "when the game end.";
                        break;
                    }
                    default: {
                        string = null;
                    }
                }
                String disableType = string;
                this.chat("Module \u00a7a\u00a7l" + module2.getName() + "\u00a73 " + added + " be disabled " + disableType);
                this.playEdit();
                return;
            }
            catch (IllegalArgumentException e) {
                this.chat("\u00a7c\u00a7lWrong auto disable type!");
                this.chatSyntax("autodisable <module> <clear/flag/world_change/game_end>");
                return;
            }
        }
        this.chatSyntax("autodisable <module/list> <clear/flag/world_change/game_end>");
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public List<String> tabComplete(@NotNull String[] args2) {
        List list;
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length == 0) {
            return CollectionsKt.emptyList();
        }
        String moduleName = args2[0];
        switch (args2.length) {
            case 1: {
                void $this$filterTo$iv$iv;
                String it;
                Iterable $this$mapTo$iv$iv;
                Iterable $this$map$iv = Client.INSTANCE.getModuleManager().getModules();
                boolean $i$f$map = false;
                Iterable iterable = $this$map$iv;
                Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                boolean $i$f$mapTo = false;
                for (Object item$iv$iv : $this$mapTo$iv$iv) {
                    Module module2 = (Module)item$iv$iv;
                    Collection collection = destination$iv$iv;
                    boolean bl = false;
                    collection.add(((Module)((Object)it)).getName());
                }
                Iterable $this$filter$iv = (List)destination$iv$iv;
                boolean $i$f$filter = false;
                $this$mapTo$iv$iv = $this$filter$iv;
                destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    it = (String)element$iv$iv;
                    boolean bl = false;
                    if (!StringsKt.startsWith(it, moduleName, true)) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                list = CollectionsKt.toList((List)destination$iv$iv);
                break;
            }
            case 2: {
                Object $this$filter$iv = new String[]{"clear", "flag", "world_change", "game_end"};
                $this$filter$iv = CollectionsKt.listOf($this$filter$iv);
                boolean $i$f$filter = false;
                Object $this$filterTo$iv$iv = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                Iterator iterator2 = $this$filterTo$iv$iv.iterator();
                while (iterator2.hasNext()) {
                    Object element$iv$iv = iterator2.next();
                    String it = (String)element$iv$iv;
                    boolean bl = false;
                    if (!StringsKt.startsWith(it, args2[1], true)) continue;
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

    @Metadata(mv={1, 6, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] nArray = new int[AutoDisable.DisableEvent.values().length];
            nArray[AutoDisable.DisableEvent.FLAG.ordinal()] = 1;
            nArray[AutoDisable.DisableEvent.WORLD_CHANGE.ordinal()] = 2;
            nArray[AutoDisable.DisableEvent.GAME_END.ordinal()] = 3;
            $EnumSwitchMapping$0 = nArray;
        }
    }
}

