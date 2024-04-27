/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.command.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.modules.command.Command;
import net.dev.important.modules.module.Module;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\bJ!\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2={"Lnet/dev/important/modules/command/commands/ToggleCommand;", "Lnet/dev/important/modules/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "LiquidBounce"})
public final class ToggleCommand
extends Command {
    public ToggleCommand() {
        String[] stringArray = new String[]{"t"};
        super("toggle", stringArray);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length <= 1) {
            this.chatSyntax("toggle <module> [on/off]");
            return;
        }
        Module module2 = Client.INSTANCE.getModuleManager().getModule(args2[1]);
        if (module2 == null) {
            this.chat("Module '" + args2[1] + "' not found.");
            return;
        }
        if (args2.length > 2) {
            String string = args2[2].toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
            String newState = string;
            if (!Intrinsics.areEqual(newState, "on") && !Intrinsics.areEqual(newState, "off")) {
                this.chatSyntax("toggle <module> [on/off]");
                return;
            }
            module2.setState(Intrinsics.areEqual(newState, "on"));
        } else {
            module2.toggle();
        }
        this.chat((module2.getState() ? "Enabled" : "Disabled") + " module \u00a78" + module2.getName() + "\u00a73.");
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public List<String> tabComplete(@NotNull String[] args2) {
        List<String> list;
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length == 0) {
            return CollectionsKt.emptyList();
        }
        String moduleName = args2[0];
        if (args2.length == 1) {
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
        } else {
            list = CollectionsKt.emptyList();
        }
        return list;
    }
}

