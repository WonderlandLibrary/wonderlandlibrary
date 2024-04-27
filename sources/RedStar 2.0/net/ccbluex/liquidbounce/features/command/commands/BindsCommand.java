package net.ccbluex.liquidbounce.features.command.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\n\b\u000020B¢J02\f\b00H¢\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/BindsCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "Pride"})
public final class BindsCommand
extends Command {
    /*
     * WARNING - void declaration
     */
    @Override
    public void execute(@NotNull String[] args) {
        void $this$filterTo$iv$iv;
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (args.length > 1 && StringsKt.equals(args[1], "clear", true)) {
            for (Module module : LiquidBounce.INSTANCE.getModuleManager().getModules()) {
                module.setKeyBind(0);
            }
            this.chat("Removed all binds.");
            return;
        }
        this.chat("§c§lBinds");
        Iterable $this$filter$iv = LiquidBounce.INSTANCE.getModuleManager().getModules();
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
            ClientUtils.displayChatMessage("§6> §c" + it.getName() + ": §a§l" + Keyboard.getKeyName((int)it.getKeyBind()));
        }
        this.chatSyntax("binds clear");
    }

    public BindsCommand() {
        super("binds", new String[0]);
    }
}
