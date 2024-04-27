package net.ccbluex.liquidbounce.features.command.special;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.misc.LiquidChat;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\n\n\b\n\n\b\n\n\u0000\n\n\n\b\n \n\b\u000020B¢J0\b2\f\t\b00\nH¢\fJ!\r\b002\f\t\b00\nH¢R0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/command/special/ChatAdminCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "lChat", "Lnet/ccbluex/liquidbounce/features/module/modules/misc/LiquidChat;", "getLChat", "()Lnet/ccbluex/liquidbounce/features/module/modules/misc/LiquidChat;", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "Pride"})
public final class ChatAdminCommand
extends Command {
    @NotNull
    private final LiquidChat lChat;

    @NotNull
    public final LiquidChat getLChat() {
        return this.lChat;
    }

    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (!this.lChat.getState()) {
            this.chat("§cError: §7LiquidChat is disabled!");
            return;
        }
        if (args.length > 1) {
            if (StringsKt.equals(args[1], "ban", true)) {
                if (args.length > 2) {
                    this.lChat.getClient().banUser(args[2]);
                } else {
                    this.chatSyntax("chatadmin ban <username>");
                }
            } else if (StringsKt.equals(args[1], "unban", true)) {
                if (args.length > 2) {
                    this.lChat.getClient().unbanUser(args[2]);
                } else {
                    this.chatSyntax("chatadmin unban <username>");
                }
            }
        } else {
            this.chatSyntax("chatadmin <ban/unban>");
        }
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
                Iterable $this$mapTo$iv$iv;
                String[] $this$map$iv = new String[]{"ban", "unban"};
                boolean $i$f$map = false;
                String[] stringArray2 = $this$map$iv;
                Collection destination$iv$iv = new ArrayList($this$map$iv.length);
                boolean $i$f$mapTo = false;
                Iterator iterator2 = $this$mapTo$iv$iv;
                int n = ((void)iterator2).length;
                for (int i = 0; i < n; ++i) {
                    String string;
                    void it;
                    void item$iv$iv;
                    void var11_14 = item$iv$iv = iterator2[i];
                    Collection collection = destination$iv$iv;
                    boolean bl2 = false;
                    void var13_16 = it;
                    boolean bl3 = false;
                    void v0 = var13_16;
                    if (v0 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    Intrinsics.checkExpressionValueIsNotNull(v0.toLowerCase(), "(this as java.lang.String).toLowerCase()");
                    collection.add(string);
                }
                Iterable $this$filter$iv = (List)destination$iv$iv;
                boolean $i$f$filter = false;
                $this$mapTo$iv$iv = $this$filter$iv;
                destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    String it = (String)element$iv$iv;
                    boolean bl4 = false;
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

    public ChatAdminCommand() {
        super("chatadmin", new String[0]);
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(LiquidChat.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.misc.LiquidChat");
        }
        this.lChat = (LiquidChat)module;
    }
}
