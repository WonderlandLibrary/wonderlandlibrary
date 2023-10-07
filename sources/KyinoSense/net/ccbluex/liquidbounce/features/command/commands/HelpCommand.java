/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  joptsimple.internal.Strings
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.util.Comparator;
import java.util.List;
import joptsimple.internal.Strings;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/HelpCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "KyinoClient"})
public final class HelpCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args2) {
        int maxPage;
        Intrinsics.checkParameterIsNotNull(args2, "args");
        int page = 1;
        if (args2.length > 1) {
            try {
                String string = args2[1];
                boolean bl = false;
                page = Integer.parseInt(string);
            }
            catch (NumberFormatException e) {
                this.chatSyntaxError();
            }
        }
        if (page <= 0) {
            this.chat("The number you have entered is too low, it must be over 0");
            return;
        }
        double maxPageDouble = (double)LiquidBounce.INSTANCE.getCommandManager().getCommands().size() / 8.0;
        int n = maxPage = maxPageDouble > (double)((int)maxPageDouble) ? (int)maxPageDouble + 1 : (int)maxPageDouble;
        if (page > maxPage) {
            this.chat("The number you have entered is too big, it must be under " + maxPage + '.');
            return;
        }
        this.chat("\u00a7c\u00a7lHelp");
        ClientUtils.displayChatMessage("\u00a77> Page: \u00a78" + page + " / " + maxPage);
        String[] $this$sortedBy$iv = (String[])LiquidBounce.INSTANCE.getCommandManager().getCommands();
        boolean $i$f$sortedBy = false;
        String[] stringArray = $this$sortedBy$iv;
        boolean bl = false;
        Comparator comparator = new Comparator<T>(){

            public final int compare(T a, T b) {
                boolean bl = false;
                Command it = (Command)a;
                boolean bl2 = false;
                Comparable comparable = (Comparable)((Object)it.getCommand());
                it = (Command)b;
                Comparable comparable2 = comparable;
                bl2 = false;
                String string = it.getCommand();
                return ComparisonsKt.compareValues(comparable2, (Comparable)((Object)string));
            }
        };
        List commands = CollectionsKt.sortedWith(stringArray, comparator);
        for (int i = 8 * (page - 1); i < 8 * page && i < commands.size(); ++i) {
            Command command = (Command)commands.get(i);
            stringArray = command.getAlias();
            StringBuilder stringBuilder = new StringBuilder().append("\u00a76> \u00a77").append(LiquidBounce.INSTANCE.getCommandManager().getPrefix()).append(command.getCommand());
            bl = false;
            boolean bl2 = stringArray.length == 0;
            ClientUtils.displayChatMessage(stringBuilder.append(bl2 ? "" : " \u00a77(\u00a78" + Strings.join((String[])command.getAlias(), (String)"\u00a77, \u00a78") + "\u00a77)").toString());
        }
        ClientUtils.displayChatMessage("\u00a7a------------\n\u00a77> \u00a7c" + LiquidBounce.INSTANCE.getCommandManager().getPrefix() + "help \u00a78<\u00a77\u00a7lpage\u00a78>");
    }

    public HelpCommand() {
        super("help", new String[0]);
    }
}

