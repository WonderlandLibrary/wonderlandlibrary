/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  joptsimple.internal.Strings
 *  kotlin.collections.CollectionsKt
 *  kotlin.comparisons.ComparisonsKt
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.util.Comparator;
import java.util.List;
import joptsimple.internal.Strings;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.ClientUtils;

public final class HelpCommand
extends Command {
    @Override
    public void execute(String[] args) {
        int maxPage;
        int page = 1;
        if (args.length > 1) {
            try {
                String string = args[1];
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
                return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)((Comparable)((Object)string)));
            }
        };
        List commands = CollectionsKt.sortedWith((Iterable)stringArray, (Comparator)comparator);
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

