/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  joptsimple.internal.Strings
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.command.commands;

import java.util.Comparator;
import java.util.List;
import joptsimple.internal.Strings;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.modules.command.Command;
import net.dev.important.utils.ClientUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2={"Lnet/dev/important/modules/command/commands/HelpCommand;", "Lnet/dev/important/modules/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "LiquidBounce"})
public final class HelpCommand
extends Command {
    public HelpCommand() {
        boolean $i$f$emptyArray = false;
        super("help", new String[0]);
    }

    @Override
    public void execute(@NotNull String[] args2) {
        int maxPage;
        Intrinsics.checkNotNullParameter(args2, "args");
        int page = 1;
        if (args2.length > 1) {
            try {
                page = Integer.parseInt(args2[1]);
            }
            catch (NumberFormatException e) {
                this.chatSyntaxError();
            }
        }
        if (page <= 0) {
            this.chat("The number you have entered is too low, it must be over 0");
            return;
        }
        double maxPageDouble = (double)Client.INSTANCE.getCommandManager().getCommands().size() / 8.0;
        int n = maxPage = maxPageDouble > (double)((int)maxPageDouble) ? (int)maxPageDouble + 1 : (int)maxPageDouble;
        if (page > maxPage) {
            this.chat("The number you have entered is too big, it must be under " + maxPage + '.');
            return;
        }
        this.chat("\u00a7c\u00a7lHelp");
        ClientUtils.displayChatMessage("\u00a77> Page: \u00a78" + page + " / " + maxPage);
        Iterable $this$sortedBy$iv = Client.INSTANCE.getCommandManager().getCommands();
        boolean $i$f$sortedBy = false;
        List commands = CollectionsKt.sortedWith($this$sortedBy$iv, new Comparator(){

            public final int compare(T a, T b) {
                Command it = (Command)a;
                boolean bl = false;
                Comparable comparable = (Comparable)((Object)it.getCommand());
                it = (Command)b;
                Comparable comparable2 = comparable;
                bl = false;
                return ComparisonsKt.compareValues(comparable2, (Comparable)((Object)it.getCommand()));
            }
        });
        int i = 8 * (page - 1);
        while (i < 8 * page && i < commands.size()) {
            Command command = (Command)commands.get(i);
            ClientUtils.displayChatMessage("\u00a76> \u00a77" + Client.INSTANCE.getCommandManager().getPrefix() + command.getCommand() + (command.getAlias().length == 0 ? "" : " \u00a77(\u00a78" + Strings.join((String[])command.getAlias(), (String)"\u00a77, \u00a78") + "\u00a77)"));
            int n2 = i;
            i = n2 + 1;
        }
        ClientUtils.displayChatMessage("\u00a7a------------\n\u00a77> \u00a7c" + Client.INSTANCE.getCommandManager().getPrefix() + "help \u00a78<\u00a77\u00a7lpage\u00a78>");
    }
}

