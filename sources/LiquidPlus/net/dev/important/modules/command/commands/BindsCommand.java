/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.input.Keyboard
 */
package net.dev.important.modules.command.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.modules.command.Command;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.ClientUtils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2={"Lnet/dev/important/modules/command/commands/BindsCommand;", "Lnet/dev/important/modules/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "LiquidBounce"})
public final class BindsCommand
extends Command {
    public BindsCommand() {
        boolean $i$f$emptyArray = false;
        super("binds", new String[0]);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void execute(@NotNull String[] args2) {
        void $this$filterTo$iv$iv;
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length > 1 && StringsKt.equals(args2[1], "clear", true)) {
            for (Module module2 : Client.INSTANCE.getModuleManager().getModules()) {
                module2.setKeyBind(0);
            }
            this.chat("Removed all binds.");
            return;
        }
        this.chat("\u00a7c\u00a7lBinds");
        Iterable $this$filter$iv = Client.INSTANCE.getModuleManager().getModules();
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            Module it = (Module)element$iv$iv;
            boolean bl = false;
            if (!(it.getKeyBind() != 0)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        Iterable $this$forEach$iv = (List)destination$iv$iv;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            Module it = (Module)element$iv;
            boolean bl = false;
            ClientUtils.displayChatMessage("\u00a76> \u00a7c" + it.getName() + ": \u00a7a\u00a7l" + Keyboard.getKeyName((int)it.getKeyBind()));
        }
        this.chatSyntax("binds clear");
    }
}

