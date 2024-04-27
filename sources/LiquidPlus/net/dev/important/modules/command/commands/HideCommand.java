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
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.ClientUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\bJ!\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2={"Lnet/dev/important/modules/command/commands/HideCommand;", "Lnet/dev/important/modules/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "LiquidBounce"})
public final class HideCommand
extends Command {
    public HideCommand() {
        boolean $i$f$emptyArray = false;
        super("hide", new String[0]);
    }

    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length > 1) {
            if (StringsKt.equals(args2[1], "list", true)) {
                Iterator $this$filterTo$iv$iv;
                this.chat("\u00a7c\u00a7lHidden");
                Iterable $this$filter$iv = Client.INSTANCE.getModuleManager().getModules();
                boolean $i$f$filter = false;
                Iterable iterable = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                Iterator iterator2 = $this$filterTo$iv$iv.iterator();
                while (iterator2.hasNext()) {
                    Object element$iv$iv = iterator2.next();
                    Module it = (Module)element$iv$iv;
                    boolean bl = false;
                    if (!(!it.getArray())) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                Iterable $this$forEach$iv = (List)destination$iv$iv;
                boolean $i$f$forEach = false;
                for (Object element$iv : $this$forEach$iv) {
                    Module it = (Module)element$iv;
                    boolean bl = false;
                    ClientUtils.displayChatMessage(Intrinsics.stringPlus("\u00a76> \u00a7c", it.getName()));
                }
                return;
            }
            if (StringsKt.equals(args2[1], "clear", true)) {
                for (Module module2 : Client.INSTANCE.getModuleManager().getModules()) {
                    module2.setArray(true);
                }
                this.chat("Cleared hidden modules.");
                return;
            }
            if (StringsKt.equals(args2[1], "reset", true)) {
                for (Module module3 : Client.INSTANCE.getModuleManager().getModules()) {
                    module3.setArray(module3.getClass().getAnnotation(Info.class).array());
                }
                this.chat("Reset hidden modules.");
                return;
            }
            if (StringsKt.equals(args2[1], "category", true)) {
                Object v0;
                block15: {
                    if (args2.length < 3) {
                        this.chatSyntax("hide category <name>");
                        return;
                    }
                    for (Object element$iv : (Iterable)Client.INSTANCE.getModuleManager().getModules()) {
                        Module it = (Module)element$iv;
                        boolean bl = false;
                        if (!StringsKt.equals(it.getCategory().getDisplayName(), args2[2], true)) continue;
                        v0 = element$iv;
                        break block15;
                    }
                    v0 = null;
                }
                if (v0 != null) {
                    Iterable $this$filter$iv = Client.INSTANCE.getModuleManager().getModules();
                    boolean $i$f$filter = false;
                    Iterable $this$filterTo$iv$iv = $this$filter$iv;
                    Collection destination$iv$iv = new ArrayList();
                    boolean $i$f$filterTo = false;
                    Iterator bl = $this$filterTo$iv$iv.iterator();
                    while (bl.hasNext()) {
                        Object element$iv$iv = bl.next();
                        Module it = (Module)element$iv$iv;
                        boolean bl2 = false;
                        if (!StringsKt.equals(it.getCategory().getDisplayName(), args2[2], true)) continue;
                        destination$iv$iv.add(element$iv$iv);
                    }
                    Iterable $this$forEach$iv = (List)destination$iv$iv;
                    boolean $i$f$forEach = false;
                    for (Object element$iv : $this$forEach$iv) {
                        Module it = (Module)element$iv;
                        boolean bl3 = false;
                        it.setArray(false);
                    }
                    this.chat("All modules in category \u00a77" + args2[2] + "\u00a73 is now \u00a7a\u00a7lhidden.");
                    return;
                }
                this.chat("Couldn't find any category named \u00a77" + args2[2] + "\u00a73!");
                return;
            }
            Module module4 = Client.INSTANCE.getModuleManager().getModule(args2[1]);
            if (module4 == null) {
                this.chat("Module \u00a7a\u00a7l" + args2[1] + "\u00a73 not found.");
                return;
            }
            module4.setArray(!module4.getArray());
            this.chat("Module \u00a7a\u00a7l" + module4.getName() + "\u00a73 is now \u00a7a\u00a7l" + (module4.getArray() ? "visible" : "invisible") + "\u00a73 on the array list.");
            this.playEdit();
            return;
        }
        this.chatSyntax("hide <module/list/clear/reset/category>");
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public List<String> tabComplete(@NotNull String[] args2) {
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length == 0) {
            return CollectionsKt.emptyList();
        }
        String moduleName = args2[0];
        switch (args2.length) {
            case 1: {
                Object element$iv$iv;
                Object $this$filterTo$iv$iv;
                Object $this$filter$iv;
                String it;
                List<String> list;
                void $this$mapTo$iv$iv;
                Iterable $this$map$iv;
                Iterable iterable = Client.INSTANCE.getModuleManager().getModules();
                boolean $i$f$map = false;
                void var6_7 = $this$map$iv;
                Collection<String> destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                boolean $i$f$mapTo = false;
                for (Object item$iv$iv : $this$mapTo$iv$iv) {
                    Module module2 = (Module)item$iv$iv;
                    list = destination$iv$iv;
                    boolean bl = false;
                    list.add(((Module)((Object)it)).getName());
                }
                $this$map$iv = (List)destination$iv$iv;
                boolean $i$f$filter = false;
                $this$mapTo$iv$iv = $this$filter$iv;
                destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                Iterator iterator2 = $this$filterTo$iv$iv.iterator();
                while (iterator2.hasNext()) {
                    element$iv$iv = iterator2.next();
                    it = (String)element$iv$iv;
                    boolean bl = false;
                    if (!StringsKt.startsWith(it, moduleName, true)) continue;
                    destination$iv$iv.add((String)element$iv$iv);
                }
                List<String> moduleList = CollectionsKt.toMutableList((List)destination$iv$iv);
                $this$filter$iv = new String[]{"category", "list", "clear", "reset"};
                $this$filter$iv = CollectionsKt.listOf($this$filter$iv);
                list = moduleList;
                $i$f$filter = false;
                $this$filterTo$iv$iv = $this$filter$iv;
                destination$iv$iv = new ArrayList();
                $i$f$filterTo = false;
                iterator2 = $this$filterTo$iv$iv.iterator();
                while (iterator2.hasNext()) {
                    element$iv$iv = iterator2.next();
                    it = (String)element$iv$iv;
                    boolean bl = false;
                    if (!StringsKt.startsWith(it, moduleName, true)) continue;
                    destination$iv$iv.add((String)element$iv$iv);
                }
                list.addAll(destination$iv$iv);
                return moduleList;
            }
            case 2: {
                void $this$filterTo$iv$iv;
                Iterable $this$mapTo$iv$iv;
                if (!StringsKt.equals(moduleName, "category", true)) break;
                Category[] $this$map$iv = Category.values();
                boolean $i$f$map = false;
                Category[] $i$f$filter = $this$map$iv;
                Collection destination$iv$iv = new ArrayList($this$map$iv.length);
                boolean $i$f$mapTo = false;
                for (void item$iv$iv : $this$mapTo$iv$iv) {
                    void it;
                    void bl = item$iv$iv;
                    Collection collection = destination$iv$iv;
                    boolean bl2 = false;
                    collection.add(it.getDisplayName());
                }
                Iterable $this$filter$iv = (List)destination$iv$iv;
                boolean $i$f$filter2 = false;
                $this$mapTo$iv$iv = $this$filter$iv;
                destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    String it = (String)element$iv$iv;
                    boolean bl = false;
                    if (!StringsKt.startsWith(it, args2[1], true)) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                return CollectionsKt.toList((List)destination$iv$iv);
            }
        }
        return CollectionsKt.emptyList();
    }
}

