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
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\b\n\n\u0000\n\n\n\b\n \n\b\u000020B¢J02\f\b00H¢\bJ!\t\b00\n2\f\b00H¢¨\f"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/BindCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "Pride"})
public final class BindCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (args.length > 2) {
            Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(args[1]);
            if (module == null) {
                this.chat("Module §a§l" + args[1] + "§3 not found.");
                return;
            }
            String string = args[2];
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toUpperCase();
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toUpperCase()");
            int key = Keyboard.getKeyIndex((String)string3);
            module.setKeyBind(key);
            this.chat("Bound module §a§l" + module.getName() + "§3 to key §a§l" + Keyboard.getKeyName((int)key) + "§3.");
            LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Notification", "Bound " + module.getName() + " to " + Keyboard.getKeyName((int)key), NotifyType.INFO, 0, 0, 24, null));
            this.playEdit();
            return;
        }
        this.chatSyntax(new String[]{"<module> <key>", "<module> none"});
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public List<String> tabComplete(@NotNull String[] args) {
        List<String> list;
        Intrinsics.checkParameterIsNotNull(args, "args");
        String[] stringArray = args;
        boolean bl = false;
        if (stringArray.length == 0) {
            return CollectionsKt.emptyList();
        }
        String moduleName = args[0];
        switch (args.length) {
            case 1: {
                void $this$filterTo$iv$iv;
                String it;
                Iterable $this$mapTo$iv$iv;
                Iterable $this$map$iv = LiquidBounce.INSTANCE.getModuleManager().getModules();
                boolean $i$f$map = false;
                Iterable iterable = $this$map$iv;
                Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                boolean $i$f$mapTo = false;
                for (Object item$iv$iv : $this$mapTo$iv$iv) {
                    Module module = (Module)item$iv$iv;
                    Collection collection = destination$iv$iv;
                    boolean bl2 = false;
                    String string = ((Module)((Object)it)).getName();
                    collection.add(string);
                }
                Iterable $this$filter$iv = (List)destination$iv$iv;
                boolean $i$f$filter = false;
                $this$mapTo$iv$iv = $this$filter$iv;
                destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    it = (String)element$iv$iv;
                    boolean bl3 = false;
                    if (!StringsKt.startsWith(it, moduleName, true)) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                list = CollectionsKt.toList((List)destination$iv$iv);
                break;
            }
            default: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }

    public BindCommand() {
        super("bind", new String[0]);
    }
}
